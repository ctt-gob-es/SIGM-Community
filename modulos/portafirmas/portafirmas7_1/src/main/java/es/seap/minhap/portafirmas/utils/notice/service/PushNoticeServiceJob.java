/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.utils.notice.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.configuration.PushService;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.notice.bean.UsuarioPush;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;
import es.seap.minhap.portafirmas.utils.notice.message.PushNoticeMessage;
import es.seap.minhap.portafirmas.ws.sim.EnvioMensajesServiceWSBindingPortType;
import es.seap.minhap.portafirmas.ws.sim.clientmanager.SIMClientManager;
import es.seap.minhap.portafirmas.ws.sim.peticion.DestinatarioPush;
import es.seap.minhap.portafirmas.ws.sim.peticion.DestinatariosPush;
import es.seap.minhap.portafirmas.ws.sim.peticion.MensajePush;
import es.seap.minhap.portafirmas.ws.sim.peticion.ObjectFactory;
import es.seap.minhap.portafirmas.ws.sim.peticion.Peticion;
import es.seap.minhap.portafirmas.ws.sim.respuesta.Mensajes;
import es.seap.minhap.portafirmas.ws.sim.respuesta.ResponseStatusType;
import es.seap.minhap.portafirmas.ws.sim.respuesta.Respuesta;
import es.seap.minhap.portafirmas.ws.sim.util.SimConstants;

public class PushNoticeServiceJob implements NoticeServiceJob {

	private static final Logger log = Logger.getLogger(PushNoticeServiceJob.class);
	
	@Autowired
	private ApplicationBO appBO;
	
	@Autowired
	private SIMClientManager simClientManager;
	
	@Autowired
	private PushService pushService;
	
	@Autowired
	private UserAdmBO userAdmBO;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		Respuesta respuesta = null;
		try {
			PushNoticeMessage pushNoticeMessage = (PushNoticeMessage) context.getJobDetail().getJobDataMap().get(Constants.NOTICE_MESSAGE);
			String event = (String) context.getJobDetail().getJobDataMap().get(Constants.NOTICE_EVENT);
			AbstractBaseDTO abstractDTO = (AbstractBaseDTO) context.getJobDetail().getJobDataMap().get(Constants.NOTICE_ABSTRACT_DTO);

			// Necesario para que se inyecten los Autowired
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

			HashMap<String,String> parametrosSIM = appBO.obtenerParametrosSIM();
			
			String wsdlLocation = parametrosSIM.get(Constants.SIM_ENVIO_URL);
			EnvioMensajesServiceWSBindingPortType servicioSIM = simClientManager.getEnvioMensajesCliente(wsdlLocation);
			Peticion peticion = crearPeticionSIM(abstractDTO, event, parametrosSIM, pushNoticeMessage);
			respuesta = servicioSIM.enviarMensaje(peticion);

			//Respuesta 3013: No se ha encontrado el identificado del dispositivo
			//Se debe desactivar el flag notify_push del usuario que envía la petición
			if(respuesta.getStatus().getStatusText().equals("KO")){
				List<es.seap.minhap.portafirmas.ws.sim.peticion.Mensajes2> msgs = respuesta.getMensajes();
				if(msgs != null && !msgs.isEmpty()){
					es.seap.minhap.portafirmas.ws.sim.peticion.Mensajes2 msg = msgs.get(0);
					if(msg.getMensaje() != null){
						ResponseStatusType rspStatusType = msg.getMensaje().getErrorMensaje();
						if(rspStatusType != null){
							String statusCode = rspStatusType.getStatusCode();
							if(statusCode.equals(SimConstants.ENV_MSG_SIM_3013_COD)){
								String idUsuario = peticion.getMensajes().getMensajePush().get(0).getDestinatariosPush()
										.getDestinatarioPush().get(0).getIdentificadorUsuario();
								PfUsersDTO userDTO = userAdmBO.queryUsersByIdentifier(idUsuario);
								userDTO.setLNotifyPush(false);
								userAdmBO.saveUser(userDTO);
							}
						}
					}
				}
			}

			pushService.imprimirRespuesta(respuesta);
		}catch (Exception e) {
			pushService.imprimirRespuesta(respuesta);
			String error = "Error controlado al enviar petición a SIM.";
			log.error(error, e);
			throw new JobExecutionException(new NoticeException(error, new Exception(error)));
		}
		
		
	}
	
	private Peticion crearPeticionSIM(AbstractBaseDTO abstractDTO, String event, HashMap<String,String> parametrosSIM, PushNoticeMessage pushNoticeMessage) throws NoticeException {	
		ObjectFactory factoriaPeticionesSIM = new ObjectFactory();
		Peticion peticion = factoriaPeticionesSIM.createPeticion();
		peticion.setUsuario(parametrosSIM.get(Constants.SIM_USER));
		peticion.setPassword(parametrosSIM.get(Constants.SIM_PASSWORD));
		peticion.setServicio(parametrosSIM.get(Constants.SIM_SERVICE));
		peticion.setMensajes(obtenerMensajes(factoriaPeticionesSIM, pushNoticeMessage));
		peticion.setNombreLote(abstractDTO.getPrimaryKeyString());
		return peticion;
	}
	
	private Mensajes obtenerMensajes(ObjectFactory factoriaPeticionesSIM, PushNoticeMessage pushNoticeMessage) {	
		MensajePush mensajePush = factoriaPeticionesSIM.createMensajePush();
		mensajePush.setTitulo(pushNoticeMessage.getTitulo());
		mensajePush.setCuerpo(pushNoticeMessage.getCuerpo());
		mensajePush.setDestinatariosPush(obtenerDestinatarios(factoriaPeticionesSIM, pushNoticeMessage));
		Mensajes mensajes = factoriaPeticionesSIM.createMensajes();
		mensajes.getMensajePush().add(mensajePush);
		return mensajes;
	}
	
	private DestinatariosPush obtenerDestinatarios(ObjectFactory factoriaPeticionesSIM, PushNoticeMessage pushNoticeMessage) {
		DestinatariosPush destinatariosPush = factoriaPeticionesSIM.createDestinatariosPush();
		for (Iterator<UsuarioPush> it = pushNoticeMessage.getDestinatarios().iterator(); it.hasNext();) {
			UsuarioPush usuarioPush = it.next();
			DestinatarioPush destinatarioPush = factoriaPeticionesSIM.createDestinatarioPush();
			destinatarioPush.setDocUsuario(usuarioPush.getDocUsuario());
			destinatarioPush.setIdentificadorUsuario(usuarioPush.getIdUsuario());
			if(Util.esVacioONulo(usuarioPush.getIdExterno())){
				destinatarioPush.setIdExterno(StringUtils.EMPTY);
			}else{
				destinatarioPush.setIdExterno(usuarioPush.getIdExterno());
			}
			destinatariosPush.getDestinatarioPush().add(destinatarioPush);
		}
		return destinatariosPush;
	}
	
	
}