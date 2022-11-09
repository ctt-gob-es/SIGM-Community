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

/**
 * Business object that implements the logical methods NotificacionGenerica web services, 
 * where the server side manager system.
 */
package es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.business;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.cxf.interceptor.Fault;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfDocelwebRequestSmanagerDTO;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business.InterfazGenericaBO;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.fault.SolicitudFault;
import es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.fault.NotificacionGenericaFault;

/**
 * @author MINHAP
 * Business object that implements the logical methods NotificacionGenerica web services, 
 * where the server side manager system.
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class NotificacionGenericaServerBO {

	@Autowired
	InterfazGenericaBO interfazGenericaBO;
	
	/**
	 * The logger object.
	 */
	private Logger log = Logger.getLogger(NotificacionGenericaServerBO.class);

	/**
	 * The Base DAO.
	 */
	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private TagBO tagBO;
	
	// INICIO LÓGICA DE LOS SERVICIOS WEB DOCEL

	/**
	 * Transactional method to notify a return request service.
	 * @param pfirmaSystem The portafirmas system code
	 * @param idSolicitud The transaction ID
	 * @param estSolicitud The request state (F or A)
	 * @return If the process success then return 'S'
	 * @throws NotificacionGenericaFault
	 * @throws SolicitudFault
	 */
	@Transactional(readOnly = false)
	public String notificarDevolucionSolicitud(String pfirmaSystem, Long idSolicitud, String estSolicitud) throws NotificacionGenericaFault, SolicitudFault {
		if (pfirmaSystem != null && idSolicitud != null && estSolicitud != null) {
			// Comprobamos la correspondencia entre la solicitud y el sistema portafirmas que la está tramitando.
			PfDocelwebRequestSmanagerDTO docelRequest = checkDocelRequestTransactionForSManager(pfirmaSystem, idSolicitud);
			if (docelRequest.getlReturnRequest()) {
				throw new NotificacionGenericaFault(DocelwebConstants.CODE_E08_RETRY_REQUEST + DocelwebConstants.FAULT_CODE_SEPARATOR + "Request transaction [" + idSolicitud + "] for the portafirmas system [" + pfirmaSystem + "] already notified", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E08_RETRY_REQUEST);
			} else {
				// Se procede a modificar el estado de la solicitud y se marca como notificada.
				guardarEstado(estSolicitud, docelRequest);
			}
			// Devolvemos 'S' si se ha notificado correctamente.
			return DocelwebConstants.YES;
		} else {
			throw new NotificacionGenericaFault(DocelwebConstants.MSJ_ERROR_PARAMETRO_NULO, Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E00_UNKNOWN);
		}
	}

	private void guardarEstado(String estSolicitud, PfDocelwebRequestSmanagerDTO docelRequest) {
		StringTokenizer stkEstado = new StringTokenizer(estSolicitud, DocelwebConstants.STATE_DELIMITER);
		String estado = stkEstado.nextToken();
		String textoRechazo = null;
		if(stkEstado.hasMoreTokens()) {
			textoRechazo = stkEstado.nextToken();
		}
		
		// En caso de que esté firmado, se marca para que la recupere la Spring Task correspondiente
		if(DocelwebConstants.REQUEST_STATE_SIGNED.equals(estado)) {
			docelRequest.setdState(DocelwebConstants.REQUEST_STATE_SIGN_NOTIFIED);
			baseDAO.insertOrUpdate(docelRequest);
		} else if (DocelwebConstants.REQUEST_STATE_REJECTED.equals(estado)) {
			tagBO.changeStateToRejected(docelRequest, textoRechazo);
		}
	}

	/**
	 * Checks if the request transaction is valid and exists in the system manager.
	 * @param pfirmaSystem The portafirmas system used by the system manager client
	 * @param transactionId The request transaction ID
	 * @return The system manager request
	 * @throws SolicitudFault
	 */
	private PfDocelwebRequestSmanagerDTO checkDocelRequestTransactionForSManager(String pfirmaSystem, Long transactionId) throws SolicitudFault {
		PfDocelwebRequestSmanagerDTO docelRequest = null;
		log.debug("Checking request with transaction ID [" + transactionId + "] for the portafirmas system [" + pfirmaSystem + "]");
		if (transactionId != null) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(DocelwebConstants.QUERY_PARAM_DOCEL_SP_TRANSACTION_ID, transactionId);
			parameters.put(DocelwebConstants.QUERY_PARAM_DOCEL_SPFIRMA_ID, pfirmaSystem);
			docelRequest = (PfDocelwebRequestSmanagerDTO) baseDAO.queryElementMoreParameters(DocelwebConstants.QUERY_DOCEL_REQUEST_SMANAGER_BY_SPFIRMA_DATA, parameters);
			if (docelRequest != null) {
				log.debug("System manager request with transaction ID [" + transactionId + "] found for the portafirmas system [" + pfirmaSystem + "]");
			} else {
				log.debug("System manager request with transaction ID [" + transactionId + "] not found for the portafirmas system [" + pfirmaSystem + "]");
			}
		}
		if (docelRequest == null) {
			log.error("System manager request with transaction ID [" + transactionId + "] is invalid for the portafirmas system [" + pfirmaSystem + "]");
			throw new SolicitudFault(DocelwebConstants.CODE_E05_UNKNOWN_REQUEST + DocelwebConstants.FAULT_CODE_SEPARATOR + "Unrecognized request transaction ID [" + transactionId + "] for the portafirmas system [" + pfirmaSystem + "]", Fault.FAULT_CODE_CLIENT, DocelwebConstants.CODE_E05_UNKNOWN_REQUEST);
		}
		return docelRequest;
	}

	// FIN COMPROBACIONES

}
