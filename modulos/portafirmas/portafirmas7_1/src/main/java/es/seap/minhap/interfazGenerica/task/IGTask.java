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

package es.seap.minhap.interfazGenerica.task;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfDocelwebRequestSmanagerDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business.InterfazGenericaBO;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business.InterfazGenericaClientBO;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TOSolicitudNueva;
import net.javacrumbs.shedlock.core.SchedulerLock;

@Component
public class IGTask {
	Logger log = Logger.getLogger(IGTask.class);
	
	@Autowired
	InterfazGenericaBO interfazGenericaBO;
	
	@Autowired
	InterfazGenericaClientBO interfazGenericaClientBO;
	
	@Autowired
	DateComponent utilFecha;
	
	@Autowired
	private BaseDAO baseDAO;

	private final static String ENVIAR_CRON = "0 */5 * * * *"; // cada 5 min
	private final static String RECUPERAR_CRON = "0 */5 * * * *"; // cada 5 min
	private final static String RECUPERAR_DIARIO_CRON = "0 30 4 * * *"; // cada día a las 4:30
	private final static String ANULAR_CRON = "0 0 4 * * *"; // cada día 4:00

    @Scheduled(cron = ENVIAR_CRON)
    @SchedulerLock(name = "enviarPeticionesIGTask", lockAtLeastForString = "PT1M", lockAtMostForString = "PT1M")
    public void enviarPeticiones() {
		List<PfDocelwebRequestSmanagerDTO> solicitudesPendientes = 
				interfazGenericaBO.getSMRequestByStatus(trimestre(), DocelwebConstants.REQUEST_STATE_NEW);
		procesarReenvioPeticiones(solicitudesPendientes);
    }

    @Scheduled(cron = RECUPERAR_CRON)
    @SchedulerLock(name = "recuperarFirmaIGTask", lockAtLeastForString = "PT1M", lockAtMostForString = "PT1M")
    public void recuperarFirma() {
		List<PfDocelwebRequestSmanagerDTO> solicitudesPendientes = 
				interfazGenericaBO.getSMRequestByStatus(trimestre(), DocelwebConstants.REQUEST_STATE_SIGN_NOTIFIED);
		procesarRecuperarFirmas(solicitudesPendientes);
    }

    @Scheduled(cron = RECUPERAR_DIARIO_CRON)
    @SchedulerLock(name = "recuperarFirmaDiarioIGTask", lockAtLeastForString = "PT1M", lockAtMostForString = "PT1M")
   public void recuperarFirmaDiario() {
		List<PfDocelwebRequestSmanagerDTO> solicitudesPendientes = 
    			interfazGenericaBO.getSMRequestByStatus(trimestre(), DocelwebConstants.REQUEST_STATE_WAITING);
		procesarRecuperarFirmas(solicitudesPendientes);
    }

    @Scheduled(cron = ANULAR_CRON)
    @SchedulerLock(name = "anularPeticionesIGTask", lockAtLeastForString = "PT1M", lockAtMostForString = "PT1M")
    public void anularPeticiones() {
		List<PfDocelwebRequestSmanagerDTO> solicitudesPendientes = 
				interfazGenericaBO.getSMOldRequestByStatus(trimestre(),
    					DocelwebConstants.REQUEST_STATE_NEW,
    					DocelwebConstants.REQUEST_STATE_WAITING,
    					DocelwebConstants.REQUEST_STATE_SIGN_NOTIFIED);
		procesarAnularFirmas(solicitudesPendientes);
    }
    

    private Date trimestre() {
		return utilFecha.addDays(new Date(), -90);
	}

	private void procesarRecuperarFirmas(List<PfDocelwebRequestSmanagerDTO> solicitudesPendientes) {
		for (PfDocelwebRequestSmanagerDTO solicitudPendiente : solicitudesPendientes) {
			try {
	   			PfDocelwebRequestSmanagerDTO pfDocelwebRequest = (PfDocelwebRequestSmanagerDTO) baseDAO.queryElementOneParameter(
    					DocelwebConstants.QUERY_DOCEL_REQUEST_SMANAGER,
    					DocelwebConstants.QUERY_PARAM_DOCEL_SM_REQUEST_ID,
    					solicitudPendiente.getPrimaryKey());
	   			
				interfazGenericaBO.recuperarFirmas(pfDocelwebRequest);
			} catch (Exception e) {
				log.error("Error al recuperar firma transacción: " + solicitudPendiente.getPrimaryKeyString(), e);
			}
		}
	}

	private void procesarReenvioPeticiones(List<PfDocelwebRequestSmanagerDTO> solicitudesPendientes) {
		for (PfDocelwebRequestSmanagerDTO solicitudPendiente : solicitudesPendientes) {
    		try {
    			PfDocelwebRequestSmanagerDTO pfDocelwebRequest = (PfDocelwebRequestSmanagerDTO) baseDAO.queryElementOneParameter(
    					DocelwebConstants.QUERY_DOCEL_REQUEST_SMANAGER,
    					DocelwebConstants.QUERY_PARAM_DOCEL_SM_REQUEST_ID,
    					solicitudPendiente.getPrimaryKey());

				PfRequestTagsDTO etiquetaPeticion = pfDocelwebRequest.getPfEtiquetaPeticion();
				TOSolicitudNueva solicitud = interfazGenericaBO.obtenerPeticion(etiquetaPeticion);
				interfazGenericaClientBO.comenzarSolicitud(pfDocelwebRequest, solicitud);
				List<Long> docIds = interfazGenericaBO.registrarDocumentos(pfDocelwebRequest);
				
				interfazGenericaClientBO.registrarSolicitud(pfDocelwebRequest, docIds);
			} catch (Exception e) {
				log.error("Error en petición externa: " + solicitudPendiente.getPrimaryKeyString(), e);
			}
		}
	}

	/**
	 * Si se ANULA una firma de portafirmas externo, la petición en nuestro portafirmas aparecerá como RECHAZADA
	 * @param solicitudesPendientes
	 */
	private void procesarAnularFirmas(List<PfDocelwebRequestSmanagerDTO> solicitudesPendientes) {
		for (PfDocelwebRequestSmanagerDTO solicitudPendiente : solicitudesPendientes) {
    		try {
    			PfDocelwebRequestSmanagerDTO docelRequest = (PfDocelwebRequestSmanagerDTO) baseDAO.queryElementOneParameter(
    					DocelwebConstants.QUERY_DOCEL_REQUEST_SMANAGER,
    					DocelwebConstants.QUERY_PARAM_DOCEL_SM_REQUEST_ID,
    					solicitudPendiente.getPrimaryKey());
    			interfazGenericaClientBO.anularSolicitudSpringTask(docelRequest);
    		} catch (Exception e) {
				log.error("Error en petición externa: " + solicitudPendiente.getPrimaryKeyString(), e);
			}
		}
	}

}
