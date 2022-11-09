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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import es.seap.minhap.portafirmas.business.ws.QueryServiceBO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.configuration.EmailNoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.configuration.ExternalAppNoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.message.EmailNoticeMessage;
import es.seap.minhap.portafirmas.ws.advice.client.AdviceService;
import es.seap.minhap.portafirmas.ws.advice.client.ClientManager;
import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.Document;
import es.seap.minhap.portafirmas.ws.bean.Request;
import es.seap.minhap.portafirmas.ws.bean.Signature;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;


/**
 * Service used to execute an email notice from a
 * {@link EmailNoticeConfiguration} and a {@link EmailNoticeMessage}. Uses
 * quartz to schedule as jobs, retrying notices in case of fail
 * 
 * @author Guadaltel
 */
public class ExternalAppNoticeServiceJob implements NoticeServiceJob {

	private static final Logger log = Logger.getLogger(ExternalAppNoticeServiceJob.class);

	@Autowired
	private QueryServiceBO queryServiceBO;

	@Autowired
	private ClientManager clientManager;
	
	/**
	 * @see es.seap.minhap.portafirmas.utils.notice.service.NoticeServiceJob#execute(org.quartz.JobExecutionContext)
	 * M&eacute;todo que lanza la ejecuci&oacute;n de una tarea de notificaci&oacute;n para una aplicaci&oacute;n externa.
	 * @param context Contexto de ejecuci&oacute;n de la tarea.
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
//		Lifecycle.beginCall();
		Boolean resultado = Boolean.FALSE;
		ExternalAppNoticeConfiguration configuration = null;
		try {
			
			// Necesario para que se injecten los Autowired
			SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
			
			configuration = (ExternalAppNoticeConfiguration) context.getJobDetail().getJobDataMap().get(Constants.NOTICE_CONFIGURATION);

			AdviceService adviceServiceClient = clientManager.getAdviceServiceClient(configuration.getWsdlLocation());
			
			Authentication authentication = new Authentication();
			authentication.setUserName(configuration.getUserName());
			authentication.setPassword(configuration.getPassword());		
			
			String requestId = configuration.getRequest().getIdentifier();
			
			Request request = getRequest(requestId);
			
			List<Signature> firmas = getSignatures (request);

			//Obtiene las firmas de los documentos de la petici&oacute;n
			/*for (Document document: request.getDocumentList().getDocument()) {
				Signature signature = queryServiceBO.downloadSignNoAuthentication(document.getIdentifier());
				firmas.add(signature);
			}*/
			
			//log.warn("Envio de updateRequestStatus por " + authentication.getUserName() + " petición " + request.getIdentifier());
			resultado = adviceServiceClient.updateRequestStatus(authentication, request, firmas.toArray(new Signature[0]));
			//log.warn("Resultado de updateRequestStatus para la aplicacion: " + authentication.getUserName() + " petición " + request.getIdentifier() + ": " + resultado);
		} catch (Throwable e) {
			String idPeticion= null;
			if (configuration != null && configuration.getRequest() != null) {
				idPeticion = configuration.getRequest().getIdentifier();
				log.error("Error enviando la Notificación al External App " + configuration.getWsdlLocation() + " para la Petición " + idPeticion + ". Cause: " + e.getMessage());
				
			}else{
				log.error("Error enviando la Notificación al External App. Cause: " + e.getMessage());
			}
			throw new JobExecutionException(e);
		}
		
		// Se comenta porque si contesta correctamente diciendo FALSE, no se debe lanzar excepción. Que si no, se replanifica.
//		if (Boolean.FALSE.equals(resultado)) {
//			String mensaje = "El servicio alojado en " + configuration.getWsdlLocation() + " ha devuelto FALSE para la petición " + configuration.getRequest().getIdentifier();			
//			throw new JobExecutionException(mensaje);
//		}
	
	}

	@Transactional (readOnly=false)
	public Request getRequest (String requestId) throws PfirmaException {
		return queryServiceBO.queryRequestNoAuthentication(requestId);
	}
	
	@Transactional (readOnly=false)
	public List<Signature> getSignatures (Request request) throws PfirmaException {
		List<Signature> firmas = new ArrayList<>();
		//Obtiene las firmas de los documentos de la petici&oacute;n
		for (Document document: request.getDocumentList().getDocument()) {
			Signature signature = queryServiceBO.downloadSignNoAuthentication(document.getIdentifier());
			signature.setIdentifier(document.getIdentifier());
			firmas.add(signature);
		}
		return firmas;
	}
}
