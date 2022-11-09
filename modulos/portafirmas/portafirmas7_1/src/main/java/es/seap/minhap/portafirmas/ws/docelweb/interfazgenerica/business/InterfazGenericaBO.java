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

package es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.guadaltel.framework.signer.impl.util.ConstantsSigner;
import es.seap.minhap.interfazGenerica.domain.Portafirmas;
import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.SignBO;
import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfDocelwebRequestSmanagerDTO;
import es.seap.minhap.portafirmas.domain.PfDocelwebSmDocDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersRemitterDTO;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business.exception.InterfazGenericaClientException;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TODocumento;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TODocumentoSalida;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types.TOSolicitudNueva;
import es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.business.NotificacionGenericaClientBO;
import es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.business.NotificacionGenericaServerBO;

/**
 * @author domingo.sanchez
 *
 * Además de la clases de negocio que cubren las necesidades del
 * cliente {@code <}{@link NotificacionGenericaClientBO }{@code >} y del
 * productor {@code <}{@link NotificacionGenericaServerBO }{@code >}
 * de los servicios web de la interfaz genérica, se implementa esta clase,
 * como interfaz para invocar el cliente.
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class InterfazGenericaBO {

	private Logger log = Logger.getLogger(InterfazGenericaBO.class);

	@Autowired
	private InterfazGenericaClientBO interfazGenericaClientBO;
	
	@Autowired
	private SignBO signBO;
	
	@Autowired
	private TagBO tagBO;
	
	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private RequestBO requestBO;
	
	@Autowired
	private ApplicationVO applicationVO;
	
	@Autowired
	private UtilComponent util;

	/**
	 * Llama a enviarPeticion cuando:
	 *  - En cascada, si al usuario que le toca es usuario externo.
	 *  - En paralelo, si los usuarios internos terminaron de firmar y de dar de visto bueno
	 *    y existe un usuario externo como destinatario pendiente de firmar.
	 * @param peticion
	 * @throws InterfazGenericaClientException 
	 * @throws DocumentCantBeDownloadedException 
	 */
	public void procesamientoExternos(PfRequestsDTO peticion) throws InterfazGenericaClientException, DocumentCantBeDownloadedException {
		Set<PfRequestTagsDTO> tagSet = peticion.getPfRequestsTags();
		// si es firma en cascada
		if(peticion.getLcascadeSign()) {
			PfUsersDTO destinatario = this.obtenerDestinatario(peticion);
			if(esExterno(destinatario)) {
				for (PfRequestTagsDTO pfRequestTagsDTO : tagSet) {
					boolean coincideUsuario = pfRequestTagsDTO.getPfUser().equals(destinatario);
					boolean estaPendiente = estaPendiente(pfRequestTagsDTO);
					if(coincideUsuario && estaPendiente) {
						this.crearPeticionExterna(pfRequestTagsDTO);
						break;  //Si se encuentra el usuario externo al que enviar, se deja de buscar.
								//Los demás usuarios están en cascada y se gestionarán cuando les toque.
					}
				}
			}
		} else if(!existeUsuarioInternoPendiente(tagSet)) {
			for (PfRequestTagsDTO pfRequestTagsDTO : tagSet) {
				boolean esUsuarioExterno = pfRequestTagsDTO.getPfUser().getCtype().equals(Constants.C_TYPE_USER_EXTERNAL);
				boolean estaPendiente = estaPendiente(pfRequestTagsDTO);
				if(esUsuarioExterno && estaPendiente) {
					this.crearPeticionExterna(pfRequestTagsDTO);
					break;  //Si se encuentra un usuario externo al que enviar, se deja de buscar.
							//Los demás externos pendientes de la firma en paralelo se gestionarán
							//cuando se obtenga la firma de respuesta.
				}
			}
		}			
	}

	private boolean esExterno(PfUsersDTO destinatario) {
		return destinatario != null
				&& destinatario.getCtype().equals(Constants.C_TYPE_USER_EXTERNAL);
	}

	/**
	 * Envía una petición de firma al portafirmas al que pertenece el usuario de la línea de firma.
	 * @param peticion
	 * @param destinatario
	 * @return 
	 * @throws InterfazGenericaClientException 
	 * @throws DocumentCantBeDownloadedException 
	 */
	public void crearPeticionExterna(PfRequestTagsDTO etiquetaPeticion) throws InterfazGenericaClientException, DocumentCantBeDownloadedException {
		Portafirmas portafirmas = etiquetaPeticion.getPfUser().getPortafirmas();
		PfDocelwebRequestSmanagerDTO docelSystemManagerRequest = new PfDocelwebRequestSmanagerDTO();
		docelSystemManagerRequest.setdDescription(obtenerDescripcion(etiquetaPeticion.getPfRequest()));
		docelSystemManagerRequest.setdPriority(obtenerPrioridad());
		docelSystemManagerRequest.setdState(DocelwebConstants.REQUEST_STATE_NEW);
		docelSystemManagerRequest.setIdTransaccion(DocelwebConstants.INIT_ID_TRANSACTION);
		docelSystemManagerRequest.setlReturnRequest(false);
		docelSystemManagerRequest.setPortafirmas(portafirmas);
		docelSystemManagerRequest.setPfEtiquetaPeticion(etiquetaPeticion);
		baseDAO.insertOrUpdate(docelSystemManagerRequest);
	}

	private PfUsersDTO obtenerDestinatario(PfRequestsDTO peticion) throws InterfazGenericaClientException, DocumentCantBeDownloadedException {
		PfUsersDTO destinatario = null;
		PfSignLinesDTO siguienteLineaFirma = tagBO.lastSignLineNotSigned(peticion);
		if(siguienteLineaFirma != null) {
			for (PfSignersDTO firmante : siguienteLineaFirma.getPfSigners()) {
				destinatario = firmante.getPfUser();
				break;
			}
		}
		return destinatario;
	}
	
	public List<Long> registrarDocumentos(PfDocelwebRequestSmanagerDTO pfDocelwebRequest) throws Exception {
		PfRequestTagsDTO etiquetaPeticion = pfDocelwebRequest.getPfEtiquetaPeticion();
		Portafirmas portafirmas = pfDocelwebRequest.getPortafirmas();
		List<Long> docIds = new ArrayList<Long>();
		List<PfDocumentsDTO> pfDocumentsList = etiquetaPeticion.getPfRequest().getPfDocumentsList();
		for (PfDocumentsDTO documento : pfDocumentsList) {
			TODocumento doc = obtenerDocumento(documento);
			Long idDocumento = interfazGenericaClientBO.registrarDocumento(portafirmas, doc, documento, pfDocelwebRequest.getIdTransaccion());
			docIds.add(idDocumento);
		}
		return docIds;
	}

	private TODocumento obtenerDocumento(PfDocumentsDTO documento) throws Exception {
		TODocumento documentoIG = new TODocumento();
		documentoIG.setContenido(signBO.obtenerContenidoFirmaDirecto(documento.getChash()));
		documentoIG.setDescripcion(documento.getDname());
		documentoIG.setNecesitaFirma(documento.getLsign()?Constants.C_YES:Constants.C_NOT);
		documentoIG.setNombreDocumento(documento.getDname());
		documentoIG.setTipoDocumento(DocelwebConstants.ELECTRONIC_DOC);
		return documentoIG;
	}

	public TOSolicitudNueva obtenerPeticion(PfRequestTagsDTO etiquetaPeticion) {
		PfRequestsDTO peticion = etiquetaPeticion.getPfRequest();
		TOSolicitudNueva solicitud = new TOSolicitudNueva();
		solicitud.setNombreSolicitante(obtenerRemitente(peticion));
		solicitud.setLoginFirmante(etiquetaPeticion.getPfUser().getCidentifier());
		solicitud.setDescripcion(obtenerDescripcion(peticion));
		solicitud.setFirmaMultiple(obtenerTipoFirmaMultiple(peticion));
		String tipoFirma = obtenerTipoFirma(etiquetaPeticion);
		solicitud.setTipoFirma(tipoFirma);
		solicitud.setVersionFirma(obtenerVersionFirma(tipoFirma));
		solicitud.setPrioridad(obtenerPrioridad());
		return solicitud;
	}

	private String obtenerRemitente(PfRequestsDTO peticion) {
		Set<PfUsersRemitterDTO> remitentes = peticion.getPfUsersRemitters();
		String remitente = "";
		for (PfUsersRemitterDTO pfUsersRemitterDTO : remitentes) {
			remitente = pfUsersRemitterDTO.getPfUser().getFullName();
		}
		return remitente;
	}

	private String obtenerDescripcion(PfRequestsDTO peticion) {
		if(util.esVacioONulo(peticion.getDreference())) {
			return peticion.getDsubject();
		} else {
			return peticion.getDsubject() + " - " + peticion.getDreference();
		}
	}

	private String obtenerPrioridad() {
		return DocelwebConstants.PRIORITY_HIGH;
	}

	private String obtenerVersionFirma(String tipoFirma) {
		//Sólo si es XAdES
		if(tipoFirma.toUpperCase().equals(Constants.SIGN_FORMAT_XADES.toUpperCase())) {
			return DocelwebConstants.XAdES_132;
		}
		return null;
	}

	/**
	 * CAdES, XAdES, PAdES
	 * @param etiquetaPeticion
	 * @return
	 */
	private String obtenerTipoFirma(PfRequestTagsDTO etiquetaPeticion) {
		Long idConf = etiquetaPeticion.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
		Configuration config = signBO.loadSignProperties(idConf);
		return util.getTipoFirmaInterfazGenerica(config.getString(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT));
	}

	private String obtenerTipoFirmaMultiple(PfRequestsDTO peticion) {
		if(!existeDocumentoFirmado(peticion)) {
			return DocelwebConstants.MULTISIGN_NO;
		} else if(peticion.getLcascadeSign()) {
			return DocelwebConstants.MULTISIGN_SERIES;
		} else {
			return DocelwebConstants.MULTISIGN_PARALLEL;
		}
	}

	private boolean existeDocumentoFirmado(PfRequestsDTO peticion) {
		boolean esDocumentoFirmado = false;
		List<PfDocumentsDTO> documentos = peticion.getPfDocumentsList();
		for (PfDocumentsDTO documento : documentos) {
			// se comprueba que no sea un anexo, es decir, debe ser firmable
			if (documento.getLsign()) {
				// se comprueba que el documento original no es ya un documento firmado
				if(documento.getLissign()) {
					esDocumentoFirmado = true;
					break;
				} else {
					// se comprueba que el documento ya ha sido firmado por otros usuarios
					List<AbstractBaseDTO> signs = signBO.orderedSigns(documento.getChash());
					if (!signs.isEmpty()) {
						esDocumentoFirmado = true;
						break;
					}
				}
			}
		}
		return esDocumentoFirmado;
	}

	private boolean existeUsuarioInternoPendiente(Set<PfRequestTagsDTO> tagsList) {
		for (PfRequestTagsDTO pfRequestTagsDTO : tagsList) {
			boolean esUsuarioInterno = 
				   pfRequestTagsDTO.getPfUser().getCtype().equals(Constants.C_TYPE_USER_USER)
				|| pfRequestTagsDTO.getPfUser().getCtype().equals(Constants.C_TYPE_USER_JOB);
			boolean estaPendiente = estaPendiente(pfRequestTagsDTO);
			if(esUsuarioInterno && estaPendiente) {
				return true;
			}
		}
		return false;
	}

	private boolean estaPendiente(PfRequestTagsDTO pfRequestTagsDTO) {
		PfTagsDTO nuevo = applicationVO.getStateTags().get(Constants.C_TAG_NEW);
		PfTagsDTO leido = applicationVO.getStateTags().get(Constants.C_TAG_READ);
		PfTagsDTO enEspera = applicationVO.getStateTags().get(Constants.C_TAG_AWAITING);
		PfTagsDTO validada = applicationVO.getStateTags().get(Constants.C_TAG_AWAITING_PASSED);
		PfTagsDTO etiqueta = pfRequestTagsDTO.getPfTag();
		if(    etiqueta.equals(nuevo)
			|| etiqueta.equals(leido)
			|| etiqueta.equals(enEspera)
			|| etiqueta.equals(validada)) {
			return true;
		}
		return false;
	}

	@Transactional(readOnly = false, rollbackFor={Exception.class})	
	public void recuperarFirmas(PfDocelwebRequestSmanagerDTO docelRequest) throws Exception {
		
		String estadoPeticion = interfazGenericaClientBO.consultarEstadoSolicitud(docelRequest);
		log.info("Estado petición : " + estadoPeticion);
		
		StringTokenizer stkEstado = new StringTokenizer(estadoPeticion, DocelwebConstants.STATE_DELIMITER);
		String estado = stkEstado.nextToken();
		String textoRechazo = null;
		if(stkEstado.hasMoreTokens()) {
			textoRechazo = stkEstado.nextToken();
		}

		
		if(DocelwebConstants.REQUEST_STATE_SIGNED.equals(estado)) {
			recuperarFirma(docelRequest);	
		} else if (DocelwebConstants.REQUEST_STATE_REJECTED.equals(estado)) {
			tagBO.changeStateToRejected(docelRequest, textoRechazo);
		}
	}

	private void recuperarFirma(PfDocelwebRequestSmanagerDTO docelRequest) throws Exception {

		PfRequestTagsDTO pfRequestTag = docelRequest.getPfEtiquetaPeticion();
		PfUsersDTO pfUser = pfRequestTag.getPfUser();
		long idConf = pfRequestTag.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
		Configuration conf = signBO.loadSignProperties(idConf);
		String formatoFirma = conf.getString(ConstantsSigner.AFIRMA5_SIGNATURE_FORMAT);
		
		List<PfDocelwebSmDocDTO> documentos = this.getSystemManagerRequestDocList(docelRequest); 
		for (PfDocelwebSmDocDTO pfDocelwebSmDocDTO : documentos) {
			TODocumentoSalida documentoSalida = interfazGenericaClientBO.consultarDocumento(docelRequest.getPortafirmas(), docelRequest.getIdTransaccion(), pfDocelwebSmDocDTO.getXDocumento());
			byte[] firma = documentoSalida.getContenido();
			String documentHash = pfDocelwebSmDocDTO.getPfDocumentDTO().getChash();
			signBO.endSignDocumento(pfRequestTag, null, pfUser, false, true, conf, null, firma, formatoFirma, documentHash, true);			
		}
		
		// Se marca la petición como firmada
		List<AbstractBaseDTO> req = new ArrayList<AbstractBaseDTO>();
		pfRequestTag.setValidada(true);
		AbstractBaseDTO requestSigned = signBO.queryRequestData(pfRequestTag, pfUser);
		req.add(requestSigned);
		
		tagBO.changeStateToSignedList(req, docelRequest);
		
		requestBO.markAsUnReadForRemitters(pfRequestTag.getPfRequest());
		
		procesamientoExternos(pfRequestTag.getPfRequest());
	}

	private List<PfDocelwebSmDocDTO> getSystemManagerRequestDocList(PfDocelwebRequestSmanagerDTO docelRequest) {
		return baseDAO.queryListOneParameter(DocelwebConstants.QUERY_DOCEL_SM_DOC_BY_REQUEST_ID, DocelwebConstants.QUERY_PARAM_DOCEL_SM_REQUEST_ID, docelRequest.getPrimaryKey());
	}

	public List<PfDocelwebRequestSmanagerDTO> getSMRequestByStatus(Date fecha, String ... estado) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		List<String> estados = Arrays.asList(estado);
		parametros.put("estados", estados );
		parametros.put("fecha", fecha);
		return baseDAO.queryListMoreParameters(DocelwebConstants.QUERY_DOCEL_SM_REQUESTS_BY_STATUS, parametros);
	}

	public List<PfDocelwebRequestSmanagerDTO> getSMOldRequestByStatus(Date fecha, String ... estado) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		List<String> estados = Arrays.asList(estado);
		parametros.put("estados", estados);
		parametros.put("fecha", fecha);
		return baseDAO.queryListMoreParameters(DocelwebConstants.QUERY_DOCEL_SM_OLD_REQUESTS_BY_STATUS, parametros);
	}

}
