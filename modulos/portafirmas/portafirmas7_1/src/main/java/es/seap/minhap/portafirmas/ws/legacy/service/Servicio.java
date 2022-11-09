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

package es.seap.minhap.portafirmas.ws.legacy.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import es.guadaltel.framework.authenticator.impl.util.Base64Coder;
import es.seap.minhap.portafirmas.business.administration.DocumentTypeAdmBO;
import es.seap.minhap.portafirmas.business.ws.legacy.RequestWSLegacyBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfBlocksDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfNoticeRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsTextDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.ws.legacy.bean.DocumentoWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.EntregaWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.PeticionWS;
import es.seap.minhap.portafirmas.ws.legacy.bean.UsuarioWS;

/**
 * Java class for pfirma v1 services implementation.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@Deprecated
public class Servicio implements ApplicationContextAware {
	private static final Logger logger = Logger.getLogger(Servicio.class);

	private static final int NPRIORIDAD3 = 3;

	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * M&eacute;todo para consultar si una petici&oacute;n est&aacute; entregada.
	 * @param peticion Identificador de la petici&oacute;n.
	 * @return Boolean que indica si se ha entregado o no.
	 */
	@Deprecated
	public boolean entregadaPeticion(String peticion) {
		logger.info("entregadaPeticion init: " + peticion);
		boolean result = false;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.isSendRequestWS(peticion);

			return result;
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = false;
		}
		logger.info("entregadaPeticion end:" + result);
		return result;
	}

	/**
	 * M&eacute;todo para entregar una petici&oacute;n.
	 * @param peticion Identificador de la petici&oacute;n.
	 * @return Long que indica si se ha entregado bien (=0) o no (-1).
	 */
	@Deprecated
	public long entregarPeticion(String peticion) {
		logger.info("entregarPeticion init: " + peticion);
		long result = 0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.sendRequestWS(peticion);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("entregarPeticion end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo para iniciar el proceso de creaci&oacute;n de una petici&oacute;n nueva.
	 * El identificador que se retorna se requiere para completar el proceso de creaci&oacute;n de la petici&oacute;n. 
	 * @param usuario dni del usuario propietario o remitente de la petici&oacute;n.
	 * @return Identificador de la petici&oacute;n en el caso de que todo vaya bien &oacute; null en el caso contrario.
	 */
	@Deprecated
	public String insertarPeticion(String usuario) {
		logger.info("insertarPeticion init: " + usuario);
		String result = "";
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.insertRequestWS(usuario);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = null;
		}
		logger.info("insertarPeticion end: " + result);
		return result;
	}

	/**
	 * M&eacute;todo para actualizar la informaci&oacute;n contenida en una petici&oacute;n.
	 * @param chash Identificador de petici&oacute;n.
	 * @param aplicacion Aplicaci&oacute;n a la que se asocia la peticion.
	 * @param firmaCascada Booleano que define la firma en paralelo o bien en cascada.
	 * @param firmaOrdenada Booleano que define que la firma de documento a su v&eacute;z este limitada en un determinada orden (deprecado).
	 * @param notificaEmail Booleano de noficaci&oacute;n del remitente v&iacute;a email.
	 * @param notificaMovil Booleano de noficaci&oacute;n del remitente v&iacute;a sms.
	 * @param notificaAviso Booleano de noficaci&oacute;n del remitente.
	 * @param fInicio Fecha de inicio a partir de la cual se puede firmar el/los documentos de la petici&oacute;n.
	 * @param fCaducidad Fecha m&aacute;xima de firma, a efectos &uacute;nicamente informativos.
	 * @param remitenteNombre Nombre del remitente.
	 * @param remitenteEmail Direcci&oacute;n de correo el&eacute;ctronico para el remintente.
	 * @param remitenteMovil N&uacute;mero de movil del remitente.
	 * @param dReferencia Referencia, por lo general, expediente al que pertenece la petici&oacute;n en la aplicaci&oacute;n cliente a t&iacute;tulo informativo.
	 * @param dAsunto Asunto sobre el que versa la petici&oacute;n como t&iacute;tulo descriptivo de la petici&oacute;n.
	 * @param nPrioridad Prioridad de la petici&oacute;n.
	 * @param usuario Usuario de portafirmas para vincular la petici&oacute;n a un usuario del propio portafirmas.
	 * @param petChash Identificador de la petici&oacute;n para el anidamiento de peticiones.
	 * @param tTexto Texto aclarativo y ampliado como nota adjunta a la petici&oacute;n .
	 * @return 0 si todo es correcto, -1 en caso contrario.
	 */
	@Deprecated
	public long actualizarPeticion(String chash, String aplicacion,
			boolean firmaCascada, boolean firmaOrdenada, boolean notificaEmail,
			boolean notificaMovil, boolean notificaAviso, Calendar fInicio,
			Calendar fCaducidad, String remitenteNombre, String remitenteEmail,
			String remitenteMovil, String dReferencia, String dAsunto,
			BigDecimal nPrioridad, String usuario, String petChash,
			String tTexto) {
		logger.info("actualizarPeticion init: " + chash + " ...");
		long result = 0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.updateRequestWS(chash, aplicacion,
					firmaCascada, firmaOrdenada, notificaEmail, notificaMovil,
					notificaAviso, fInicio, fCaducidad, remitenteNombre,
					remitenteEmail, remitenteMovil, dReferencia, dAsunto,
					nPrioridad, usuario, petChash, tTexto);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("actualizarPeticion end: " + result);
		return result;
	}

	@Deprecated
	public PeticionWS consultarPeticion(String peticion) {
		logger.info("consultarPeticion init: " + peticion);
		PeticionWS result = null;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			PfRequestsDTO reqDTO = requestWSLegacyBO.queryRequestWS(peticion);
			PfRequestsTextDTO reqTextDTO = requestWSLegacyBO
					.queryRequestTextWS(reqDTO);
			String remitterName = requestWSLegacyBO.queryRemitter(reqDTO,
					Constants.C_HISTORICREQUEST_REMITTERNAME);
			String remitterEmail = requestWSLegacyBO.queryRemitter(reqDTO,
					Constants.C_HISTORICREQUEST_REMITTEREMAIL);
			String remitterMobile = requestWSLegacyBO.queryRemitter(reqDTO,
					Constants.C_HISTORICREQUEST_REMITTERMOBILE);
			if (reqDTO != null) {
				result = requestToPeticion(reqDTO, reqTextDTO, remitterName,
						remitterEmail, remitterMobile);
			}
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = null;
		}
		logger.info("consultarPeticion end: " + result);
		return (PeticionWS) sustituirNulos(result);
	}

	@Deprecated
	public UsuarioWS[] consultarDestinatariosPeticion(String peticion) {
		logger.info("consultarDestinatariosPeticion init: " + peticion);
		UsuarioWS[] result = null;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			List<AbstractBaseDTO> list = requestWSLegacyBO
					.querySignersWS(peticion);
			if (list != null && !list.isEmpty()) {
				result = userListToUsuarioArray(list);
			}

		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = null;
		}
		logger.info("consultarDestinatariosPeticion end: " + Arrays.toString(result));
		return result;
	}

	@Deprecated
	public long insertarDestinatarioPeticion(String dni, String peticion) {
		logger.info("insertarDestinatarioPeticion init: " + dni + ", "
				+ peticion);
		long result = 0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.insertRequestSignerWS(dni, null, null,
					null, peticion, false);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("insertarDestinatarioPeticion end: " + result);
		return result;
	}

	@Deprecated
	public long eliminarDestinatarioPeticion(String dni, String peticion) {
		logger.info("eliminarDestinatarioPeticion init: " + dni + ", "
				+ peticion);
		long result = 0;

		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.deleteRequestSignerWS(dni, peticion);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("eliminarDestinatarioPeticion end: " + result);
		return result;
	}

	@Deprecated
	public UsuarioWS consultarUsuario(String dni) {
		logger.info("consultarUsuario init: " + dni);
		UsuarioWS res = null;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			PfUsersDTO userDTO = requestWSLegacyBO.queryUserWS(dni);
			if (userDTO != null) {
				res = userToUsuario(userDTO);
			}
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			res = null;
		}
		logger.info("consultarUsuario init: " + res);
		return (UsuarioWS) sustituirNulos(res);
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public DocumentoWS[] consultarDocumentosPeticion(String peticion) {
		logger.info("consultarDocumentosPeticion init: " + peticion);
		DocumentoWS[] result = null;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			Object[] list = requestWSLegacyBO.queryDocumentsWS(peticion);
			if (list != null) {
				List<AbstractBaseDTO> listDoc = (List<AbstractBaseDTO>) list[0];
				List<BigDecimal> listSize = (List<BigDecimal>) list[1];
				result = documentListToDocumentoArray(peticion, listDoc,
						listSize);
			}

		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = null;
		}
		logger.info("consultarDocumentosPeticion end: " + Arrays.toString(result));
		return result;
	}

	@Deprecated
	public String descargarDocumento(String documento) {
		logger.info("descargarDocumento init: " + documento);
		byte[] result = null;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.downloadDocumentWS(documento);
		} catch (Exception e) {
			logger.error("Error en WS:" + e.getMessage());
			result = null;
		}
		logger.info("descargarDocumento end: "
				+ (result != null ? "NO NULO" : "NULO"));

		String stringResult = null;

		try {
			Base64Coder coder = new Base64Coder();
			stringResult = new String(coder.encodeBase64(result));
		}
		catch (Exception e) {
			logger.error("Error en WS:" + e.getMessage());
			result = null;
		}
		logger.info("descargarDocumento end: "
				+ (result != null ? "NO NULO" : "NULO"));

		return stringResult;
	}

	@Deprecated
	public long eliminarDocumentoPeticion(String documento) {
		logger.info("eliminarDocumentoPeticion init: " + documento);
		long result = 0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.deleteDocumentWS(documento);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("eliminarDocumentoPeticion end: " + result);
		return result;
	}

	@Deprecated
	public String insertarDocumentoPeticion(String peticion, String tipoDoc,
			String nombreDoc, String tipoMime, byte[] documento) {
		logger.info("insertarDocumentoPeticion init: " + peticion + " ...");
		String result = "";
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.insertRequestDocumentWS(peticion,
					tipoDoc, nombreDoc, tipoMime, documento);

			return result;
		} catch (Exception ex) {
			result = null;
			logger.error("Error en WS:", ex);
		}
		logger.info("insertarDocumentoPeticion end: " + result);
		return result;
	}

	@Deprecated
	public String[] valoresTiposDocumento() {
		logger.info("valoresTiposDocumento init");
		String[] res = null;
		try {
			DocumentTypeAdmBO documentTypeAdmBO = applicationContext.getBean(DocumentTypeAdmBO.class);
			List<AbstractBaseDTO> list = documentTypeAdmBO.queryList();
			if (list != null && !list.isEmpty()) {
				res = documentTypeListToStringArray(list);
			}
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			res = null;
		}
		logger.info("valoresTiposDocumento end: " + Arrays.toString(res));
		return res;
	}

	@Deprecated
	public String[] consultarNotificacionesPeticion(String peticion) {
		logger.info("consultarNotificacionesPeticion init: " + peticion);
		String[] result = null;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			List<AbstractBaseDTO> list = requestWSLegacyBO
					.queryNoticesRequestWS(peticion);
			if (list != null && !list.isEmpty()) {
				result = noticeListToStringArray(list);
			}
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = null;
		}
		logger.info("consultarNotificacionesPeticion end: " + Arrays.toString(result));
		return result;
	}

	@Deprecated
	public long insertarNotificacionPeticion(String peticion, String estado) {
		logger.info("insertarNotificacionPeticion init:" + peticion + " "
				+ estado);
		long result = 0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.insertNoticeWS(peticion, estado);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("insertarNotificacionPeticion end: " + result);
		return result;
	}

	@Deprecated
	public long eliminarNotificacionPeticion(String peticion, String estado) {
		logger.info("eliminarNotificacionPeticion init: " + peticion + " "
				+ estado);
		long result = 0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.deleteNoticeWS(peticion, estado);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("eliminarNotificacionPeticion end:" + result);
		return result;
	}

	@Deprecated
	public EntregaWS[] consultarEntregasPeticion(String peticion) {
		logger.info("consultarEntregasPeticion init: " + peticion);
		EntregaWS[] result = null;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			PfRequestsDTO req = requestWSLegacyBO
					.queryStatesRequestWS(peticion);
			if (req != null) {
				result = stateListToEntregaArray(req);
			}
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = null;
		}
		logger.info("consultarEntregasPeticion end:" + Arrays.toString(result));
		return result;
	}

	@Deprecated
	public String[] valoresEstados() {
		logger.info("valoresEstados init");
		String[] result = null;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			List<AbstractBaseDTO> list = requestWSLegacyBO.queryStatesListWS();
			if (list != null && !list.isEmpty()) {
				result = stateListToStringArray(list);
			}
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = null;
		}
		if(result != null){
			logger.info("valoresEstados init: " + Arrays.toString(result));
		}
		return result;
	}

	@Deprecated
	public String descargarPKCS7(String documento, String idTransaccion) {
		logger.info("descargarPKCS7 init: " + documento + ", " + idTransaccion);
		byte[] result = null;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.downloadSignWS(documento, idTransaccion);
		} catch (Exception e) {
			logger.error("Error en WS:" + e.getMessage());
			result = null;
		}
		logger.info("descargarPKCS7 end: "
				+ (result != null ? "NO NULO" : "NULO"));

		String stringResult = null;

		try {
			Base64Coder coder = new Base64Coder();
			stringResult = new String(coder.encodeBase64(result));
		}
		catch (Exception e) {
			logger.error("Error en WS:" + e.getMessage());
			result = null;
		}
		logger.info("descargarFirma end: "
				+ (result != null ? "NO NULO" : "NULO"));

		return stringResult;
	}

	@Deprecated
	public long documentoAccion(String hashDoc, String estado, String accion,
			String tipo) {
		logger.info("documentoAccion init: " + hashDoc + " ...");
		long result = 0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.insertActionRequestWS(hashDoc, estado,
					accion, tipo);
		} catch (Exception ex) {
			result = -1;
			logger.error("Error en WS:", ex);
		}
		logger.info("documentoAccion end: " + result);
		return result;

	}

	/*
	 * ------------------- Nuevos servicios: 9/12/2005
	 */

	@Deprecated
	public long cambiarDestinatario(String hashPeticion, String dniOrigen,
			String dniDestino, String nombre, String apellido1,
			String apellido2, String nuevo) {
		logger.info("cambiarDestinatario init: " + hashPeticion + " ...");
		long result = 0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.changeSignerRequestWS(hashPeticion,
					dniOrigen, dniDestino, nombre, apellido1, apellido2, nuevo);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("cambiarDestinatario end: " + result);
		return result;
	}

	@Deprecated
	public String consultarObservacionesEntrega(String hashDoc, String dni) {
		logger.info("consultarObservacionesEntrega init: " + hashDoc + " ,"
				+ dni);
		String result = "";
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.queryObservationRequestWS(hashDoc, dni);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = "";
		}
		logger.info("consultarObservacionesEntrega end: " + result);
		return result;
	}

	/*
	 * ----------- Nuevos servicios en v1.2 (05/04/2006)
	 */

	@Deprecated
	public long insertarDestinatarioPeticion(UsuarioWS usuario, String peticion) {
		logger.info("insertarDestinatarioPeticion init: " + usuario + " ,"
				+ peticion);
		long result = 0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			if (usuario != null) {
				result = requestWSLegacyBO.insertRequestSignerWS(usuario
						.getCDNI(), usuario.getDNOMBRE(), usuario.getDAPELL1(),
						usuario.getDAPELL2(), peticion, true);
			} else {
				logger.error("Usuario no valido");
				result = -1;
			}
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("insertarDestinatarioPeticion end: " + result);
		return result;
	}

	@Deprecated
	public long eliminarDestinatarioPeticion(UsuarioWS usuario, String peticion) {
		logger.info("eliminarDestinatarioPeticion init: " + usuario + " ,"
				+ peticion);
		long result = 0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.deleteRequestSignerWS(usuario.getCDNI(),
					peticion);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("eliminarDestinatarioPeticion end: " + result);
		return result;
	}

	@Deprecated
	public long cambiarDestinatario(String hashPeticion, UsuarioWS usuO,
			UsuarioWS usuD, String nuevo) {
		logger.info("cambiarDestinatario init: " + hashPeticion + " ...");
		long result = 0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.changeSignerRequestWS(hashPeticion, usuO
					.getCDNI(), usuD.getCDNI(), usuD.getDNOMBRE(), usuD
					.getDAPELL1(), usuD.getDAPELL2(), nuevo);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("cambiarDestinatario end: " + result);
		return result;
	}

	@Deprecated
	public long eliminarPeticion(String hashPet) {
		logger.info("eliminarPeticion init: " + hashPet);
		long result = 0;

		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.deleteRequestWS(hashPet);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("eliminarPeticion init: " + result);
		return result;
	}

	@Deprecated
	public UsuarioWS[] obtenerListaUsuarios() {
		logger.info("obtenerListaUsuarios init");
		UsuarioWS[] result = null;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			List<AbstractBaseDTO> list = requestWSLegacyBO.queryUsersAllWS();
			if (list != null && !list.isEmpty()) {
				result = userListToUsuarioArray(list);
			}
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = null;
		}
		logger.info("obtenerListaUsuarios end:" + Arrays.toString(result));
		return result;
	}

	@Deprecated
	public double obtenerIdTransaccion(String hashDocumento) {
		logger.info("obtenerIdTransaccion init: " + hashDocumento);
		double result = 0.0;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.queryDocumetSignWS(hashDocumento);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = -1;
		}
		logger.info("obtenerIdTransaccion end:" + result);
		return result;
	}

	public String consultarFormatoFirmaPeticion(String peticion) {
		logger.info("consultarFormatoFirmaPeticion init: " + peticion);
		String result = null;
		try {
			RequestWSLegacyBO requestWSLegacyBO = applicationContext.getBean(RequestWSLegacyBO.class);
			result = requestWSLegacyBO.queryRequestSignFormat(peticion);
		} catch (Exception ex) {
			logger.error("Error en WS:", ex);
			result = "";
		}
		logger.info("consultarFormatoFirmaPeticion end:" + result);
		return result;
	}

	/*
	 * AUXILIARY METHODS
	 */

	private PeticionWS requestToPeticion(PfRequestsDTO reqDTO,
			PfRequestsTextDTO reqTextDTO, String remitterName,
			String remitterEmail, String remitterMobile) {
		PeticionWS result = new PeticionWS();
		result.setAPLCAPLICACION(reqDTO.getPfApplication().getCapplication());
		result.setCHASH(reqDTO.getChash());
		result.setDASUNTO(reqDTO.getDsubject());
		result.setDREFERENCIA(reqDTO.getDreference());

		// remitente por defecto
		result.setDREMITENTENOMBRE(remitterName);
		result.setDREMITENTEEMAIL(remitterEmail);
		result.setDREMITENTEMOVIL(remitterMobile);
		result.setLNOTIFICAAVISO(Constants.C_NOT);
		result.setLNOTIFICAEMAIL(Constants.C_NOT);
		result.setLNOTIFICAMOVIL(Constants.C_NOT);
		result.setFCADUCIDAD(Util.getInstance().dateToCalendar(
				reqDTO.getFexpiration()));
		result.setFENTRADA(Util.getInstance()
				.dateToCalendar(reqDTO.getFentry()));
		result
				.setFINICIO(Util.getInstance().dateToCalendar(
						reqDTO.getFstart()));
		result.setLFIRMAENCASCADA(reqDTO.getLcascadeSign() ? Constants.C_YES
				: Constants.C_NOT);
		result.setLFIRMAORDENADA(Constants.C_NOT);
		result.setNPRIORIDAD(new BigDecimal(NPRIORIDAD3));
		result.setPETCHASH(null);
		result.setTTEXTO(reqTextDTO.getTrequest());

		return result;
	}

	private UsuarioWS[] userListToUsuarioArray(List<AbstractBaseDTO> list) {
		UsuarioWS[] res;
		res = new UsuarioWS[list.size()];
		UsuarioWS userWS = null;
		int counter = 0;
		for (AbstractBaseDTO userDTO : list) {
			userWS = userToUsuario(userDTO);
			res[counter++] = (UsuarioWS) sustituirNulos(userWS);
		}
		return res;
	}

	private UsuarioWS userToUsuario(AbstractBaseDTO userDTO) {
		UsuarioWS userWS;
		userWS = new UsuarioWS();
		userWS.setCDNI(((PfUsersDTO) userDTO).getCidentifier());
		userWS.setDAPELL1(((PfUsersDTO) userDTO).getDsurname1());
		userWS.setDAPELL2(((PfUsersDTO) userDTO).getDsurname2());
		userWS.setDNOMBRE(((PfUsersDTO) userDTO).getDname());
		return userWS;
	}

	private DocumentoWS[] documentListToDocumentoArray(String peticion,
			List<AbstractBaseDTO> listDoc, List<BigDecimal> listSize) {
		DocumentoWS[] res;
		res = new DocumentoWS[listDoc.size()];
		DocumentoWS docWS = null;
		int counter = 0;
		for (AbstractBaseDTO docDTO : listDoc) {
			docWS = documentToDocumento(peticion, docDTO, listSize.get(counter));
			res[counter++] = (DocumentoWS) sustituirNulos(docWS);
		}
		return res;
	}

	private DocumentoWS documentToDocumento(String peticion,
			AbstractBaseDTO docDTO, BigDecimal size) {
		DocumentoWS docWS;
		docWS = new DocumentoWS();
		docWS.setCHASH(((PfDocumentsDTO) docDTO).getChash());
		docWS.setDARCHIVO(((PfDocumentsDTO) docDTO).getDname());
		docWS.setDMIME(((PfDocumentsDTO) docDTO).getDmime());
		docWS.setFCREACION(Util.getInstance().dateToCalendar(
				((PfDocumentsDTO) docDTO).getFcreated()));
		docWS.setFMODIFICACION(Util.getInstance().dateToCalendar(
				((PfDocumentsDTO) docDTO).getFmodified()));
		docWS.setNTAMANIO(size);
		docWS.setPETCHASH(peticion);
		docWS.setTDOCCTIPODOCUMENTO(((PfDocumentsDTO) docDTO)
				.getPfDocumentType().getCdocumentType());
		return docWS;
	}

	private EntregaWS[] stateListToEntregaArray(PfRequestsDTO reqDTO) {
		EntregaWS[] res;
		int dim = reqDTO.getPfDocuments().size()
				* reqDTO.getPfRequestsTags().size();
		res = new EntregaWS[dim];
		int counter = 0;
		EntregaWS entregaWS = null;
		PfSignsDTO sign = null;
		PfBlocksDTO block = null;

		for (AbstractBaseDTO docDTO : reqDTO.getPfDocuments()) {
			for (AbstractBaseDTO rTagDTO : reqDTO.getPfRequestsTags()) {
				entregaWS = new EntregaWS();
				entregaWS.setCESTADO(((PfRequestTagsDTO) rTagDTO).getPfTag()
						.getCtag());
				if (Constants.C_TAG_SIGNED.equals(((PfRequestTagsDTO) rTagDTO)
						.getPfTag().getCtag())) {
					for (AbstractBaseDTO signDTO : ((PfDocumentsDTO) docDTO)
							.getPfSigns()) {
						sign = (PfSignsDTO) signDTO;
						if (sign.getPfSigner().getPfUser().getPrimaryKey().equals(((PfRequestTagsDTO) rTagDTO)
								.getPfUser().getPrimaryKey())
								&& !sign.getCtype().equals(
										Constants.C_TYPE_SIGNLINE_PASS)) {
							if (sign.getPfBlockSigns() != null
									&& !sign.getPfBlockSigns().isEmpty()) {
								block = sign.getPfBlockSigns().iterator()
										.next().getPfBlock();
								entregaWS.setCTRANSACTIONID(block
										.getCtransaction());
								entregaWS.setFESTADO(Util.getInstance()
										.dateToCalendar(block.getFsign()));
							} else {
								entregaWS.setCTRANSACTIONID(sign
										.getCtransaction());
								entregaWS.setFESTADO(Util.getInstance()
										.dateToCalendar(sign.getFstate()));
							}
						}
					}
				} else {
					entregaWS.setFESTADO(Util.getInstance().dateToCalendar(
							((PfRequestTagsDTO) rTagDTO).getFmodified()));
				}
				entregaWS.setDARCHIVO(((PfDocumentsDTO) docDTO).getDname());
				entregaWS.setDESTCDNI(((PfRequestTagsDTO) rTagDTO).getPfUser()
						.getCidentifier());
				entregaWS.setDOCCHASH(((PfDocumentsDTO) docDTO).getChash());
				res[counter++] = (EntregaWS) sustituirNulos(entregaWS);
			}
		}
		return res;
	}

	private String[] documentTypeListToStringArray(List<AbstractBaseDTO> list) {
		String[] res;
		res = new String[list.size()];
		int contador = 0;
		for (AbstractBaseDTO abstractBaseDTO : list) {
			res[contador++] = ((PfDocumentTypesDTO) abstractBaseDTO)
					.getCdocumentType();
		}
		return res;
	}

	private String[] noticeListToStringArray(List<AbstractBaseDTO> list) {
		String[] res;
		res = new String[list.size()];
		int counter = 0;
		for (AbstractBaseDTO noticesDTO : list) {
			res[counter++] = ((PfNoticeRequestsDTO) noticesDTO).getPfTag()
					.getCtag();
		}
		return res;
	}

	private String[] stateListToStringArray(List<AbstractBaseDTO> list) {
		String[] result;
		result = new String[list.size()];
		int contador = 0;
		for (AbstractBaseDTO tagDTO : list) {
			result[contador++] = ((PfTagsDTO) tagDTO).getCtag();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private Object sustituirNulos(Object o) {
		try {

			if (o != null) {
				Method[] methods = o.getClass().getMethods();
				for (Method method : methods) {
					String nameGet = method.getName();

					if (nameGet.indexOf("get") > -1) {
						Object res = method.invoke(o);
						if (method.getReturnType().getName().indexOf("String") > -1
								&& res == null) {
							String nameSet = nameGet.replace("get", "set");
							Object[] args = { "" };
							Class[] argsClass = { String.class };
							o.getClass().getMethod(nameSet, argsClass).invoke(
									o, args);

						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error al cambiar nulos:", e);
		}

		return o;
	}

	/*
	 * public Object[] sustituirNulosArray(Object[] o) { if (o != null) { for
	 * (Object object : o) { sustituirNulos(object); } } return o; }
	 */

}
