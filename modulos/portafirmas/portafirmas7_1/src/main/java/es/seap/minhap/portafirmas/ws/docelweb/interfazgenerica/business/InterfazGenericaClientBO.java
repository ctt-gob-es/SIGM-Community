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
 * Business object that implements the InterfazGenerica web service client logic methods,
 * where the client side manager system.
 */
package es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.interfazGenerica.domain.Portafirmas;
import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfDocelwebRequestSmanagerDTO;
import es.seap.minhap.portafirmas.domain.PfDocelwebSmDocDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.InterfazGenerica;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business.exception.InterfazGenericaClientException;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.AnularSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.AnularSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ComenzarSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ComenzarSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarDocumentoElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarDocumentoResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarEstadoSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarEstadoSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.ConsultarSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarDocumentoElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarDocumentoResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarSolicitudElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.RegistrarSolicitudResponseElement;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TODocumento;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TODocumentoSalida;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TOSolicitudConsulta;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TOSolicitudNueva;
import es.seap.minhap.portafirmas.ws.docelweb.wss.DocelwebClientManager;

/**
 * @author MINHAP
 * Business object that implements the InterfazGenerica web service client logic methods,
 * where the client side manager system.
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class InterfazGenericaClientBO {

	@Resource(name="interfazGenericaProperties")
	private Properties interfazGenericaProperties;

	/**
	 * The logger object.
	 */
	private Logger log = Logger.getLogger(InterfazGenericaServerBO.class);

	/**
	 * The Base DAO.
	 */
	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private TagBO tagBO;
	
	/**
	 * The InterfazGenerica client manager.
	 */
	@Autowired
	private DocelwebClientManager clientManager;

	/**
	 * Intenta enviar la petición a portafirmas externo, creada y pendiente en BB.DD.
	 */
	@Transactional
	public void comenzarSolicitud(PfDocelwebRequestSmanagerDTO pfDocelRequestSM, TOSolicitudNueva request) throws InterfazGenericaClientException {
		try {
			Portafirmas portafirmas = pfDocelRequestSM.getPortafirmas();
			InterfazGenerica interfazGenericaProxy = clientManager.getInterfazGenericaClient(portafirmas);
			ComenzarSolicitudElement comenzarSolicitudParameters = new ComenzarSolicitudElement();
			comenzarSolicitudParameters.setCodigoSistemaGestor(interfazGenericaProperties.getProperty(DocelwebConstants.MI_PORTAFIRMAS));
			comenzarSolicitudParameters.setSolicitud(request);
			ComenzarSolicitudResponseElement comenzarSolicitudReturn = interfazGenericaProxy.comenzarSolicitud(comenzarSolicitudParameters);
			// Se actualiza la solicitud en el sistema de gestión.
			pfDocelRequestSM.setIdTransaccion(comenzarSolicitudReturn.getResult());
			baseDAO.insertOrUpdate(pfDocelRequestSM);
		} catch (Exception e) {
			log.error("Invoking InterfazGenerica.comenzarSolicitud fail", e);
			throw new InterfazGenericaClientException("Se ha producido un error invocando el WS InterfazGenerica.comenzarSolicitud", e);
		}
	}

	/**
	 * Call the InterfazGenerica.registrarDocumento web service method with given params config.
	 * @param portafirmas The configuration ID. If null, then use the default configuration
	 * @param document The Document Type
	 * @param documento 
	 * @param transactionId The transaction ID
	 * @return The document ID
	 * @throws InterfazGenericaClientException
	 */
	@Transactional
	public Long registrarDocumento(Portafirmas portafirmas, TODocumento toDocumento, PfDocumentsDTO pfDocumento, Long transactionId) throws InterfazGenericaClientException {
		try {
			InterfazGenerica interfazGenericaProxy = clientManager.getInterfazGenericaClient(portafirmas);
			log.info("Invoking InterfazGenerica.registrarDocumento start");
			RegistrarDocumentoElement registrarDocumentoParameters = new RegistrarDocumentoElement();
			PfDocelwebRequestSmanagerDTO systemManagerRequest = getSManagerRequestBySpfirmaData(portafirmas.getcPortafirmas(), transactionId);
			if (systemManagerRequest != null) {
				registrarDocumentoParameters.setCodigoSistemaGestor(interfazGenericaProperties.getProperty(DocelwebConstants.MI_PORTAFIRMAS));
				registrarDocumentoParameters.setIdSolicitud(transactionId);
				registrarDocumentoParameters.setDocumento(toDocumento);
				RegistrarDocumentoResponseElement registrarDocumentoReturn = interfazGenericaProxy.registrarDocumento(registrarDocumentoParameters);
				// Se registra el identificador de documento en el sistema de gestión.
				createNewSystemManagerDocument(toDocumento, pfDocumento, systemManagerRequest, registrarDocumentoReturn.getResult());
				log.info("Invoking InterfazGenerica.registrarDocumento end");
				return registrarDocumentoReturn.getResult();
			} else {
				log.error("InterfazGenerica.registrarDocumento - Don´t exists a system manager request for de system pfirma [" + portafirmas.getNombre() + "] and transaction id [" + transactionId + "]");
				throw new InterfazGenericaClientException("Don´t exists a system manager request for de system pfirma [" + portafirmas.getNombre() + "] and transaction id [" + transactionId + "]");
			}
		} catch (Exception e) {
			log.error("Invoking InterfazGenerica.registrarDocumento fail", e);
			throw new InterfazGenericaClientException("Se ha producido un error invocando el WS InterfazGenerica.registrarDocumento", e);
		}
	}

	/**
	 * Call the InterfazGenerica.consultarEstadoSolicitud web service method with given params config.
	 * @param portafirmas
	 * @param transactionId The transaction ID
	 * @param docId The document ID
	 * @return The document info
	 * @throws InterfazGenericaClientException
	 */
	@Transactional
	public TODocumentoSalida consultarDocumento(Portafirmas portafirmas, Long transactionId, Long docId) throws InterfazGenericaClientException {
		try {
			InterfazGenerica interfazGenericaProxy = clientManager.getInterfazGenericaClient(portafirmas);
			log.info("Invoking InterfazGenerica.consultarDocumento start");
			ConsultarDocumentoElement consultarDocumentoParameters = new ConsultarDocumentoElement();
			consultarDocumentoParameters.setCodigoSistemaGestor(interfazGenericaProperties.getProperty(DocelwebConstants.MI_PORTAFIRMAS));
			consultarDocumentoParameters.setIdSolicitud(transactionId);
			consultarDocumentoParameters.setIdDocumento(docId);
			ConsultarDocumentoResponseElement consultarEstadoSolicitudReturn = interfazGenericaProxy.consultarDocumento(consultarDocumentoParameters);
			// Modelo system manager inalterado
			log.info("Invoking InterfazGenerica.consultarDocumento end");
			return consultarEstadoSolicitudReturn.getResult();
		} catch (Exception e) {
			log.error("Invoking InterfazGenerica.consultarDocumento fail", e);
			throw new InterfazGenericaClientException("Se ha producido un error invocando el WS InterfazGenerica.consultarDocumento", e);
		}
	}

	/**
	 * Call the InterfazGenerica.registrarSolicitud web service method with given params config.
	 * @param portafir The configuration ID. If null, then use the default configuration
	 * @param transactionId The transaction ID
	 * @param docIds The document IDs list
	 * @return Boolean value with the process execution state
	 * @throws InterfazGenericaClientException
	 */
	@Transactional
	public boolean registrarSolicitud(PfDocelwebRequestSmanagerDTO pfDocelRequestSM, List<Long> docIds) throws InterfazGenericaClientException {
		try {
			Portafirmas portafirmas = pfDocelRequestSM.getPortafirmas();
			Long idTransaccion = pfDocelRequestSM.getIdTransaccion();
			InterfazGenerica interfazGenericaProxy = clientManager.getInterfazGenericaClient(portafirmas);
			log.info("Invoking InterfazGenerica.registrarSolicitud start");
			RegistrarSolicitudElement registraSolicitudParameters = new RegistrarSolicitudElement();
			PfDocelwebRequestSmanagerDTO systemManagerRequest = getSManagerRequestBySpfirmaData(portafirmas.getcPortafirmas(), idTransaccion);
			if (systemManagerRequest != null) {
				if (docIds != null && !docIds.isEmpty()) {
					registraSolicitudParameters.setCodigoSistemaGestor(interfazGenericaProperties.getProperty(DocelwebConstants.MI_PORTAFIRMAS));
					registraSolicitudParameters.setIdSolicitud(idTransaccion);
					registraSolicitudParameters.getDocumento().addAll(docIds);
					RegistrarSolicitudResponseElement registrarSolicitudReturn = interfazGenericaProxy.registrarSolicitud(registraSolicitudParameters);
					log.info("Invoking InterfazGenerica.registrarSolicitud end");
					if (registrarSolicitudReturn.getResult().equalsIgnoreCase(DocelwebConstants.YES)) {
						// Si se registra correctamente, se modifica el estado a 'P' de la solicitud en el sistema de gestión.
						systemManagerRequest.setdState(DocelwebConstants.REQUEST_STATE_WAITING);
						baseDAO.insertOrUpdate(systemManagerRequest);
						return true;
					} else {
						return false;
					}
				} else {
					log.error("InterfazGenerica.registrarSolicitud - The registration request must send all registered document IDs");
					throw new InterfazGenericaClientException("The registration request must send all registered document IDs");
				}
			} else {
				log.error("InterfazGenerica.registrarSolicitud - Don´t exists a system manager request for de system pfirma [" + portafirmas.getNombre() + "] and transaction id [" + idTransaccion + "]");
				throw new InterfazGenericaClientException("Don´t exists a system manager request for de system pfirma [" + portafirmas.getNombre() + "] and transaction id [" + idTransaccion + "]");
			}
		} catch (Exception e) {
			log.error("Invoking InterfazGenerica.registrarSolicitud fail", e);
			throw new InterfazGenericaClientException("Se ha producido un error invocando el WS InterfazGenerica.registrarSolicitud", e);
		}
	}

	/**
	 * Call the InterfazGenerica.consultarEstadoSolicitud web service method with given params config.
	 * @param portafirmas
	 * @param transactionId The transaction ID
	 * @return The request state char string (N, P, F, A, R)
	 * @throws InterfazGenericaClientException
	 */
	public String consultarEstadoSolicitud(PfDocelwebRequestSmanagerDTO docelRequest) throws InterfazGenericaClientException {
		try {
			InterfazGenerica interfazGenericaProxy = clientManager.getInterfazGenericaClient(docelRequest.getPortafirmas());
			log.info("Invoking InterfazGenerica.consultarEstadoSolicitud start");
			ConsultarEstadoSolicitudElement consultarEstadoSolicitudParameters = new ConsultarEstadoSolicitudElement();
			consultarEstadoSolicitudParameters.setCodigoSistemaGestor(interfazGenericaProperties.getProperty(DocelwebConstants.MI_PORTAFIRMAS));
			consultarEstadoSolicitudParameters.setIdSolicitud(docelRequest.getIdTransaccion());
			ConsultarEstadoSolicitudResponseElement consultarEstadoSolicitudReturn = interfazGenericaProxy.consultarEstadoSolicitud(consultarEstadoSolicitudParameters);
			// Modelo system manager inalterado
			log.info("Invoking InterfazGenerica.consultarEstadoSolicitud end");
			return consultarEstadoSolicitudReturn.getResult();
		} catch (Exception e) {
			log.error("Invoking InterfazGenerica.consultarEstadoSolicitud fail", e);
			throw new InterfazGenericaClientException("Se ha producido un error invocando el WS InterfazGenerica.consultarEstadoSolicitud", e);
		}
	}

	/**
	 * Retorno, entre otros, una lista de códigos de documento que se pueden utilizar
	 * para recuperar a través del método consultarDocumento.
	 * No se usa porque los códigos de los documentos se obtienen de BB.DD.,
	 * almacenados al dar de alta a los documentos.
	 */
	@Transactional
	public TOSolicitudConsulta consultarSolicitud(Portafirmas portafirmas, Long transactionId) throws InterfazGenericaClientException {
		try {
			InterfazGenerica interfazGenericaProxy = clientManager.getInterfazGenericaClient(portafirmas);
			log.info("Invoking InterfazGenerica.consultarSolicitud start");
			ConsultarSolicitudElement consultarSolicitudParameters = new ConsultarSolicitudElement();
			consultarSolicitudParameters.setCodigoSistemaGestor(interfazGenericaProperties.getProperty(DocelwebConstants.MI_PORTAFIRMAS));
			consultarSolicitudParameters.setIdSolicitud(transactionId);
			ConsultarSolicitudResponseElement consultarSolicitudReturn = interfazGenericaProxy.consultarSolicitud(consultarSolicitudParameters);
			// Modelo system manager inalterado
			log.info("Invoking InterfazGenerica.consultarSolicitud end");
			return consultarSolicitudReturn.getResult();
		} catch (Exception e) {
			log.error("Invoking InterfazGenerica.consultarSolicitud fail", e);
			throw new InterfazGenericaClientException("Se ha producido un error invocando el WS InterfazGenerica.consultarSolicitud", e);
		}
	}

	@Transactional
	public boolean anularSolicitud(PfDocelwebRequestSmanagerDTO docelRequest) throws InterfazGenericaClientException {
		return anular(docelRequest);
	}

	@Transactional
	public void anularSolicitudSpringTask(PfDocelwebRequestSmanagerDTO docelRequest) throws InterfazGenericaClientException {
		tagBO.changeStateToRejected(docelRequest, DocelwebConstants.MENSAJE_ANULACION);
		anular(docelRequest);
	}
	
	private boolean anular(PfDocelwebRequestSmanagerDTO docelRequest) throws InterfazGenericaClientException {
		try {
			Portafirmas portafirmas = docelRequest.getPortafirmas();
			Long transactionId = docelRequest.getIdTransaccion();
			InterfazGenerica interfazGenericaProxy = clientManager.getInterfazGenericaClient(portafirmas);
			log.info("Invoking InterfazGenerica.anularSolicitud start");
			AnularSolicitudElement anularSolicitudParameters = new AnularSolicitudElement();
			PfDocelwebRequestSmanagerDTO systemManagerRequest = getSManagerRequestBySpfirmaData(portafirmas.getcPortafirmas(), transactionId);
			if (systemManagerRequest != null) {
				anularSolicitudParameters.setCodigoSistemaGestor(interfazGenericaProperties.getProperty(DocelwebConstants.MI_PORTAFIRMAS));
				anularSolicitudParameters.setIdSolicitud(transactionId);
				AnularSolicitudResponseElement anularSolicitudReturn = interfazGenericaProxy.anularSolicitud(anularSolicitudParameters);
				log.info("Invoking InterfazGenerica.anularSolicitud end");
				if (anularSolicitudReturn.getResult().equalsIgnoreCase(DocelwebConstants.YES)) {
					// Si se anula correctamente, se modifica el estado de la solicitud a 'A' en el sistema de gestión.
					systemManagerRequest.setdState( DocelwebConstants.REQUEST_STATE_VOID);
					baseDAO.insertOrUpdate(systemManagerRequest);
					return true;
				} else {
					return false;
				}
			} else {
				log.error("InterfazGenerica.anularSolicitud - Don´t exists a system manager request for de system pfirma [" + portafirmas.getNombre() + "] and transaction id [" + transactionId + "]");
				throw new InterfazGenericaClientException("InterfazGenerica.anularSolicitud - Don´t exists a system manager request for de system pfirma [" + portafirmas.getNombre() + "] and transaction id [" + transactionId + "]");
			}
		} catch (Exception e) {
			log.error("Invoking InterfazGenerica.anularSolicitud fail", e);
			throw new InterfazGenericaClientException("Se ha producido un error invocando el WS InterfazGenerica.anularSolicitud", e);
		}
	}

	private PfDocelwebRequestSmanagerDTO getSManagerRequestBySpfirmaData(String systemPfirma, Long spfirmaTransactionId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(DocelwebConstants.QUERY_PARAM_DOCEL_SPFIRMA_ID, systemPfirma);
		parameters.put(DocelwebConstants.QUERY_PARAM_DOCEL_SP_TRANSACTION_ID, spfirmaTransactionId);
		return (PfDocelwebRequestSmanagerDTO) baseDAO.queryElementMoreParameters(DocelwebConstants.QUERY_DOCEL_REQUEST_SMANAGER_BY_SPFIRMA_DATA, parameters);
	}

	private void createNewSystemManagerDocument(TODocumento document, PfDocumentsDTO pfDocumento, PfDocelwebRequestSmanagerDTO systemManagerRequest, Long idDocument) {
		PfDocelwebSmDocDTO smDocDTO = new PfDocelwebSmDocDTO();
		smDocDTO.setXDocumento(idDocument);
		smDocDTO.setDDescripcion(document.getDescripcion());
		smDocDTO.setPfDocelwebSolicitudSgestion(systemManagerRequest);
		smDocDTO.setPfDocumentDTO(pfDocumento);
		baseDAO.insertOrUpdate(smDocDTO);
	}

}
