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

package es.seap.minhap.portafirmas.business.ws;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.JAXBMarshaller;
import es.seap.minhap.portafirmas.ws.afirma5.Afirma5Constantes;
import es.seap.minhap.portafirmas.ws.afirma5.clientmanager.Afirma5ClientManager;
import es.seap.minhap.portafirmas.ws.afirma5.clientmanager.ConfigManagerBO;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.RespuestaAmpliarFirma;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.DSSSignature;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ResponseBaseType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.VerifyRequest;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.Afirma5DssAfirmaVerifyConverter;
import es.seap.minhap.portafirmas.ws.afirma5.exception.Afirma5Exception;
import es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.RespuestaValidarCertificado;
import es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.converter.Afirma5ValidarCertificadoConverter;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.RespuestaValidarFirma;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.converter.Afirma5ValidarFirmaConverter;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class Afirma5BO {

	@Autowired
	private ConfigManagerBO configManagerBO;
	
	@Autowired
	private Afirma5ClientManager afirma5ClientManager;
	
	Logger log = Logger.getLogger(Afirma5BO.class);
	
	/**
	 * Comprueba si para una configuración dada está activo el validar certificado contra Afirma
	 * @param idConf
	 * @return
	 * @throws Afirma5Exception
	 */
	public boolean estaActivoValidarCertificado (long idConf) {		
		return parametroValorS (idConf, Afirma5Constantes.FIRMA_VALIDAR_CERTIFICADO_ACTIVO);		
	}
	
	/**
	 * Comprueba si para la configuracion por defecto está activo el validar certificado contra Afirma
	 * @param idConf
	 * @return
	 * @throws Afirma5Exception
	 */
	public boolean estaActivoValidarCertificado ()  {
		return estaActivoValidarCertificado (configManagerBO.getDefaultConfigurationId());
	}
	
	/**
	 * Comprueba si para una configuración dada está activo el validar firma contra Afirma
	 * @param idConf
	 * @return
	 * @throws Afirma5Exception
	 */
	public boolean estaActivoValidarFirma (long idConf)  {
		return parametroValorS (idConf, Afirma5Constantes.FIRMA_VALIDAR_FIRMA_ACTIVO);
	}
	
	
	/**
	 * Comprueba si para una configuración dada está activo el estampar sello de tiempo contra Afirma
	 * @param idConf
	 * @return
	 * @throws Afirma5Exception
	 */
	public boolean estaActivoSello (long idConf)  {
		return parametroValorS (idConf, Afirma5Constantes.FIRMA_SELLO_ACTIVO);
	}
		
//	/**
//	 * Devuelve un mapa con las propiedades del certificado validado. Los parámetros de afirma se tomarán
//	 * de la configuración por defecto.
//	 * @param certificate
//	 * @return
//	 * @throws Afirma5Exception
//	 */
//	public RespuestaValidarCertificado queryDetailsFromCertificateAfirma5(String certificate) throws Afirma5Exception {
//		Long idConf = configManagerBO.getDefaultConfigurationId ();
//		return queryDetailsFromCertificateAfirma5 (certificate, idConf);
//	}
	
	/**
	 * Devuelve un objeto con el resultado de la validacion del certificado. Los parámetros de afirma se tomarán
	 * de la configuración pasada como parámetro.
	 * @param certificate
	 * @param idConf identificador de configuración de la aplicación
	 * @return
	 * @throws Afirma5Exception
	 * @throws SocketTimeoutException 
	 */
	public RespuestaValidarCertificado queryDetailsFromCertificateAfirma5 (String certificate, long idConf) throws Afirma5Exception, SocketTimeoutException {
		log.debug ("Inicio queryDetailsFromCertificateAfirma5");
		
		Map<String, String> params = null;
		es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeEntrada mensajeEntrada = null;
		String peticionString = null;
		String respuestaString = null;
		
		es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.Validacion validacionService = null;
		
		es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeSalida mensajeSalida = null;
		
		
		log.debug ("Cargando configuracion de idConf " + idConf);
		// Carga de la configuración
		params = configManagerBO.getConfiguration (idConf);
			
		if (params == null) {
			throw new Afirma5Exception ("No se han encontrado parametros de configuracion de Afirma");
		}
		
		try {
			log.debug ("Creando MensajeEntrada para ValidarCertificado");
			// Creación de la petición
			mensajeEntrada = Afirma5ValidarCertificadoConverter.crearPeticionValidarCertificado(certificate, params);
			log.debug ("Aplanando MensajeEntrada");
			// Aplanamiento del mensaje
			peticionString = JAXBMarshaller.marshallRootElement(mensajeEntrada, es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeEntrada.class);
			log.debug("Petición ValidarCertificado: " + peticionString);
		} catch (Exception e) {
			throw new Afirma5Exception ("No se puede crear el mensaje de petición de ValidarCertificado ", e);
		}
		
		try {
			log.debug ("Obteniendo servicio ValidarCertificado");
			// Obtención del servicio
			validacionService = afirma5ClientManager.getValidacionServiceClient(params);
			//Para forzar las pruebas en el caso de que @Firma esté KO
			//throw new SocketTimeoutException();
		} catch (Exception e) {
			if(e.getClass().equals(SocketTimeoutException.class)){
				log.error("ERROR: Afirma5BO.validarCertificado: "
						+ "El servicio ValidarCertificado no está disponible: ", e);
				throw new SocketTimeoutException("El servicio ValidarCertificado no está disponible: " + e);
			}else{
				log.error("ERROR: Afirma5BO.validarCertificado: "
						+ "No se puede obtener el servicio ValidarCertificado: ", e);
				throw new Afirma5Exception ("No se puede obtener el servicio ValidarCertificado ", e);
			}
		}
		
	try {
			log.debug ("Invocando operacion validarCertificado");
			
			// Se invoca a la operación
			respuestaString = validacionService.validarCertificado(peticionString);
			log.debug ("Respuesta recibida validarCertificado : " + respuestaString);
			//Para forzar las pruebas en el caso de que @Firma esté KO
			//throw new SocketTimeoutException();
		}catch (Exception e) {
			if(e.getClass().equals(SocketTimeoutException.class)){
				log.error("ERROR: Afirma5BO.validarCertificado: "
						+ "El servicio validarCertificado no está disponible: ", e);
				//Para las pruebas de indisponibilidad en la invocación de la operación ValidarCertificado
				throw new SocketTimeoutException("La operación validarCertificado del servicio ValidarCertificado no está disponible: " + e);
			}else{
				log.error("ERROR: Afirma5BO.ValidarCertificado: "
						+ "No se puede obtener el servicio ValidarCertificado: ", e);
				throw new Afirma5Exception ("Error invocando a operación ValidarCertificado ", e);
			}
		}
		
		try {
			log.debug ("Desaplanando MensajeSalida de ValidarCertificado");
			// Desaplanamiento del mensaje de respuesta
			mensajeSalida = JAXBMarshaller.unmarshallRootElement(respuestaString, es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.MensajeSalida.class);
		} catch (Exception e) {
			throw new Afirma5Exception ("No se puede desaplanar la respuesta del servicio ValidarCertificado ", e);
		}
		
		RespuestaValidarCertificado resultado = Afirma5ValidarCertificadoConverter.mensajeSalidaToRespuestaValidarCertificado(mensajeSalida);
		return resultado;
	}
	
	/**
	 * Valida una firma contra Afirma.
	 * @param sign
	 * @param document
	 * @param hash
	 * @param algoritmoHash
	 * @param tipoFirma
	 * @param idConf identificador de configuracion de la aplicación.
	 * @return
	 * @throws Afirma5Exception
	 * @throws SocketTimeoutException 
	 */
	public RespuestaValidarFirma validarFirma (byte[] sign, byte[] document, byte[] hash, String algoritmoHash, 
			String tipoFirma, long idConf) throws Afirma5Exception, SocketTimeoutException {
		log.debug ("Inicio validarFirma");
		
		Map<String, String> params = null;
		
		es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.MensajeEntrada mensajeEntrada = null;
		String peticionString = null;
		String respuestaString = null;
		
		es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.Firma validacionFirmaService = null;
		
		es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.MensajeSalida mensajeSalida = null;
		
		log.debug ("Cargando configuracion de idConf " + idConf);
		// Carga de la configuración
		params = configManagerBO.getConfiguration (idConf);
			
		if (params == null) {
			throw new Afirma5Exception ("No se han encontrado parametros de configuracion de Afirma");
		}
		
		try {
			log.debug ("Creando MensajeEntrada para ValidarFirma");
			// Creación de la petición
			mensajeEntrada = Afirma5ValidarFirmaConverter.crearPeticionValidarFirma(sign, document, hash, algoritmoHash, tipoFirma, params);
			log.debug ("Aplanando MensajeEntrada");
			// Aplanamiento del mensaje
			peticionString = JAXBMarshaller.marshallRootElement(mensajeEntrada, es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.MensajeEntrada.class);
		} catch (Exception e) {
			throw new Afirma5Exception ("No se puede crear el mensaje de petición de ValidarFirma ", e);
		}
		
		try {
			log.debug ("Obteniendo servicio ValidarFirma");
			// Obtención del servicio
			validacionFirmaService = afirma5ClientManager.getFirmaServiceClient(params);
			//Para las pruebas de indisponibilidad en la creación del WS
			//throw new SocketTimeoutException("");
		} catch (Exception e) {
			if(e.getClass().equals(SocketTimeoutException.class)){
				log.error("ERROR: Afirma5BO.validarFirma: "
						+ "El servicio validarFirma no está disponible: ", e);
				throw new SocketTimeoutException("El servicio ValidarFirma no está disponible: " + e);
			}else{
				log.error("ERROR: Afirma5BO.validarFirma: "
						+ "No se puede obtener el servicio ValidarFirma: ", e);
				throw new Afirma5Exception ("No se puede obtener el servicio ValidarFirma ", e);	
			}
		}
		
		try {
			log.debug ("Invocando operacion ValidarFirma");
			// Se invoca a la operación
			respuestaString = validacionFirmaService.validarFirma(peticionString);
			//Para las pruebas de indisponibilidad en la creación del WS
			//throw new SocketTimeoutException("");
		}catch (Exception e) {
			if(e.getClass().equals(SocketTimeoutException.class)){
				log.error("ERROR: Afirma5BO.validarFirma: "
						+ "El servicio validarFirma no está disponible: ", e);
				//Para las pruebas de indisponibilidad en la invocación de la operación ValidarFirma
				throw new SocketTimeoutException("La operación validarFirma del servicio ValidarFirma no está disponible: " + e);
			}else{
				log.error("ERROR: Afirma5BO.validarFirma: "
						+ "No se puede obtener el servicio ValidarFirma: ", e);
				throw new Afirma5Exception ("Error invocando a operacion ValidarFirma ", e);
			}
		}
		
		try {
			log.debug ("Desaplanando MensajeSalida de ValidarFirma");
			// Desaplanamiento del mensaje de respuesta
			mensajeSalida = JAXBMarshaller.unmarshallRootElement(respuestaString, es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.MensajeSalida.class);
		} catch (Exception e) {
			throw new Afirma5Exception ("No se puede desaplanar la respuesta del servicio ValidarFirma ", e);
		}
		
		RespuestaValidarFirma resultado = Afirma5ValidarFirmaConverter.mensajeSalidaToRespuestaValidarFirma(mensajeSalida);
		return resultado;
	}
	
	/**
	 * Amplia una firma a otra con un formato más extendido
	 * @param firma
	 * @param certificados
	 * @param idConf
	 * @param format 
	 * @return
	 * @throws Afirma5Exception
	 */
	public RespuestaAmpliarFirma estamparSelloDeTiempo (byte[] firma, List<byte[]> certificados, long idConf, String format) throws Afirma5Exception {
		
		RespuestaAmpliarFirma resultado = null;
		try {
			Map<String, String> params = configManagerBO.getConfiguration (idConf);
			if (params == null) {
				throw new Exception("No se han encontrado parametros de configuracion de Afirma");
			}
			
			Afirma5DssAfirmaVerifyConverter converter = new Afirma5DssAfirmaVerifyConverter();
			converter.inicializar(firma);
			
			String formatoAmpliacion = Afirma5Constantes.UPGRADE_TIMESTAMP;
			if (format.contains(Constants.SIGN_FORMAT_PDF)) {
				formatoAmpliacion = Afirma5Constantes.UPGRADE_TIMESTAMP_PDF;
			}
		
			VerifyRequest verifyRequest = converter.crearPeticionAmpliarFirma (firma,
																 certificados, 
																 true, 
																 formatoAmpliacion, 
																 params);
			
			String verifyRequestString = JAXBMarshaller.marshallRootElement(verifyRequest, VerifyRequest.class);
			//log.info("Peticion upgrade DSSAfirmaVerify: " + verifyRequestString);

			// Obtención del servicio
			DSSSignature dssSignature = afirma5ClientManager.getDSSSignatureServiceClient(params);
			
			// Se invoca a la operación
			String verifyResponseString = dssSignature.verify(verifyRequestString);
			//log.info("Respuesta recibida verify : " + verifyResponseString);

			// Desaplanamiento del mensaje de respuesta
			ResponseBaseType response = JAXBMarshaller.unmarshallRootElement(verifyResponseString, ResponseBaseType.class);
			
			resultado = converter.responseTypeToResultadoAmpliarFirma(response);

		} catch (Throwable t) {
			throw new Afirma5Exception ("No se pudo ampliar la firma. ", t);
		}
		
		return resultado;
		
	}
	
	/**
	 * Comprueba si el valor de un parámetro asociado a una configuración es 'S'
	 * @param idConf
	 * @param parametro
	 * @return
	 */
	private boolean parametroValorS (Long idConf, String parametro) {
		
		boolean valorSi = false;
		
		Map<String, String> params = null;
		log.debug ("Cargando configuracion de idConf " + idConf);
		// Carga de la configuración
		params = configManagerBO.getConfiguration (idConf);
			
		String valorParam = params.get(parametro);
		
		if (valorParam != null && valorParam.equals(Constants.C_YES)) {
			valorSi = true;
		}
		return valorSi;
	}
	
	/**
	 * Amplia una firma a otra con un formato más extendido
	 * @param firma
	 * @param certificados
	 * @param idConf
	 * @param format 
	 * @return
	 * @throws Afirma5Exception
	 */
	public RespuestaAmpliarFirma ampliarFirma (String format, byte[] firma, List<byte[]> certificados, long idConf) throws Afirma5Exception {
		
		Map<String, String> params = null;
		VerifyRequest verifyRequest = null;
		DSSSignature dssSignature = null;
		String verifyRequestString = null;
		String verifyResponseString = null;
		
		ResponseBaseType response = null;
		
		Afirma5DssAfirmaVerifyConverter converter = null;
		
		log.debug ("Cargando configuracion de idConf " + idConf);
		// Carga de la configuración
		params = configManagerBO.getConfiguration (idConf);
			
		if (params == null) {
			throw new Afirma5Exception ("No se han encontrado parametros de configuracion de Afirma");
		}
		
		try {
			converter = new Afirma5DssAfirmaVerifyConverter();
			log.debug ("Inicializando Afirma5DssAfirmaVerifyConverter");
			converter.inicializar(firma);
			
			log.debug ("Creando VerifyRequest para ampliacion de firma");
			verifyRequest = converter.crearPeticionAmpliarFirma (firma,
																 certificados, 
																 true, 
																 format, 
																 params);
			
			log.debug ("Aplanando VerifyRequest");
			verifyRequestString = JAXBMarshaller.marshallRootElement(verifyRequest, VerifyRequest.class);
			log.debug ("Peticion upgrade DSSAfirmaVerify: " + verifyRequestString);
		} catch (Exception e) {
			throw new Afirma5Exception ("No se puede crear el mensaje de petición de upgrade DSSAfirmaVerify ", e);
		}
		
		try {
			log.debug ("Obteniendo servicio DSSSignature");
			// Obtención del servicio
			dssSignature = afirma5ClientManager.getDSSSignatureServiceClient(params);
		} catch (Throwable t) {
			throw new Afirma5Exception ("No se puede obtener el servicio DSSSignature ", t);
		}
		
		try {
			log.debug ("Invocando operacion verify");
			
			// Se invoca a la operación
			verifyResponseString = dssSignature.verify(verifyRequestString);
			
			log.debug ("Respuesta recibida verify : " + verifyResponseString);
		} catch (Throwable t) {
			throw new Afirma5Exception ("Error invocando a operacion verify ", t);
		}
		
		try {
			log.debug ("Desaplanando VerifyResponse de Verify");
			// Desaplanamiento del mensaje de respuesta
			response = JAXBMarshaller.unmarshallRootElement(verifyResponseString, ResponseBaseType.class);
			
		} catch (Exception e) {
			throw new Afirma5Exception ("No se puede desaplanar la respuesta del servicio DSSSignature ", e);
		}
		
		RespuestaAmpliarFirma resultado = converter.responseTypeToResultadoAmpliarFirma(response);
		return resultado;
		
	}

	public RespuestaValidarFirma validarFirma(byte[] firma, long idConfig) throws SocketTimeoutException, Afirma5Exception {
		return validarFirma(firma, null, null, null, null, idConfig);
	}

	public boolean validarFirma(byte[] firma) throws SocketTimeoutException, Afirma5Exception {
		boolean esFirmaValida = true;
		long idConfig = configManagerBO.getDefaultConfigurationId();
		RespuestaValidarFirma respuestaValidarFirma = null;
		try {
			respuestaValidarFirma = this.validarFirma(firma, idConfig);
		}catch (Exception e){
			log.error("Fallo al validar firma contra @firma", e);
			esFirmaValida = false;
		}
		if (respuestaValidarFirma.isError() || !respuestaValidarFirma.isValido()) {
			esFirmaValida = false;
		}
		return esFirmaValida;
	}

}
