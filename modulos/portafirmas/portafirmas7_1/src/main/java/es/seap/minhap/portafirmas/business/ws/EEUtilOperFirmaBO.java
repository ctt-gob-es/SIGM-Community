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

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.InfoCertificado;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.ResultadoValidarCertificado;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.DatosFirmados;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.ResultadoValidacionInfo;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.ApplicationLogin;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.CertificateList;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.ConfiguracionAmpliarFirma;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.EeUtilService;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.InSideException;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.ResultadoAmpliarFirma;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilConfigManager;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilOperClientManager;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class EEUtilOperFirmaBO {

	@Autowired
	private EeUtilOperClientManager eeUtilOperClientManager;
	
	@Autowired
	private EeUtilConfigManager eeUtilConfigManager;

	@Autowired
	private UtilComponent util;
	
	@Autowired
	private MailToAdminBO mailToAdminBO;

	Logger log = Logger.getLogger(EEUtilOperFirmaBO.class);

	/**
	 * Comprueba si está activo estampar sello de tiempo para una configuración determinada
	 * @param idConf id de la configuración
	 * @return
	 * 			true si está activado, false en caso contrario.
	 */
	public boolean checkTimestamp (long idConf)  {
		return util.parametroValorS (idConf, Constants.C_PARAMETER_EEUTIL_SELLO_ACTIVE, eeUtilConfigManager);		
	}
	
	/**
	 * Método que estampa un sello de tiempo en una firma
	 * @param formatoAmpliacion 
	 * @param listadoFirmantes Listado de certificados de firmantes de la firma sobre los que se aplica la ampliación
	 * @return Firma con sello de tiempo estampado
	 * @throws EeutilException
	 */
	public byte[] estamparSelloDeTiempo(byte[] firma, List<byte[]> certificados, long idConf) throws EeutilException {
		/*EeUtilService port = getTimeStampServicePort();

		// Se crean los datos de aplicación
        ApplicationLogin appInfo = createApplicationLogin();*/
		
		Map<String, String> params = null;
		
		// Carga de la configuración
		params = eeUtilConfigManager.cargarConfiguracion (idConf);
		EeUtilService eeutilOperClient = null;
			
		if (params == null) {
			log.error("Se ha producido un error cargando la configuración del servicio eeutil en la llamada a estamparSelloDeTiempo de EEUtilOperFirma");
			//sendMailToAdmin(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILOPERFIRMA);
			throw new EeutilException ("No se han encontrado parametros de configuracion de EEUtilOperFirma");
		}
		
		try {
			eeutilOperClient = eeUtilOperClientManager.getEEUtilOperFirmaClient(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_URL));
		} catch (Throwable t) {
			log.error("Se ha producido un error obteniendo el servicio eeutil en la llamada a estamparSelloDeTiempo de EEUtilOperFirma", t);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CREAR_SERVICIO_UTILOPERFIRMA, t);
			throw new EeutilException ("No se ha podido obtener el stub de EEUtilOperFirma", t);
		}
		
		
        ApplicationLogin appInfo = createApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_USER), params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_PASSWORD));

        // Se crea la lista de certificados en caso de existir
        CertificateList certificateList = null;
        if (certificados != null && !certificados.isEmpty()) {
        	certificateList = new CertificateList();
        	for (byte[] certificado : certificados) {
        		certificateList.getCertificates().add(certificado);
        	}
        }

        ConfiguracionAmpliarFirma configuracionAmpliarFirma = 
        			crearConfiguracionAmpliarFirm(Constants.UPGRADE_TIMESTAMP, certificateList, true);

        byte[] firmaConTimestamp = null;
        try {
        	ResultadoAmpliarFirma resultado = eeutilOperClient.ampliarFirma(appInfo, firma, configuracionAmpliarFirma);
			firmaConTimestamp = resultado.getFirma();
        } catch (InSideException e) {
        	log.error("InsideException" + e.getFaultInfo().getDescripcion());
        	log.error("Se ha producido un error en la llamada a ampliarFirma de EEUtilOperFirma: " , e);
        	mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_AMPLIAR_FIRMA, e );
        	throw new EeutilException ("Se ha producido un error en la llamada a ampliarFirma de EEUtilOperFirma: "  + e.getFaultInfo().getDescripcion(), e);
        } catch (Throwable t) {
        	log.error("Se ha producido un error en la llamada a ampliarFirma de EEUtilOperFirma: " , t);
        	mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_AMPLIAR_FIRMA, t);
        	throw new EeutilException ("Se ha producido un error en la llamada a ampliarFirma de EEUtilOperFirma: " + t.getMessage());
		}

		return firmaConTimestamp;
	}
	
	/**
	 * Crea el objeto de autenticación contra eeutil
	 * @param idaplicacion identificador de la aplicación Portafirmas en eeeutil
	 * @param password password de la aplicación Portafirmas en eeutil.
	 * @return 
	 * @throws EeutilException
	 */
	private ApplicationLogin createApplicationLogin (String idaplicacion, String password) throws EeutilException{
		if (idaplicacion == null) {
			throw new EeutilException ("El usuario de EEUtilOperFirma es nulo");
		}
		if (password == null) {
			throw new EeutilException ("El password de EEUtilOperFirma es nulo");
		}
		ApplicationLogin appInfo = new ApplicationLogin();
		appInfo.setIdaplicacion(idaplicacion);
		appInfo.setPassword(password);
		/*appInfo.setIdaplicacion(this.user);
		appInfo.setPassword(this.password);*/
        return appInfo;
	}
	
	/**
	 * Método que prepara la configuración de una ampliación de una firma
	 * @param ampliacionFirma Ampliación a realizar a la firma
	 * @param listaFirmantes Listado de certificados de los firmantes de la firma sobre los que se aplica la ampliación
	 * @param ignorarPeriodoDeGracia Indica si desea ignorarse el periodo de gracia
	 * @return Configuración de ampliación de firma
	 */
	private static ConfiguracionAmpliarFirma crearConfiguracionAmpliarFirm(String ampliacionFirma,
																		   CertificateList listaFirmantes,
																		   Boolean ignorarPeriodoDeGracia) {
		ConfiguracionAmpliarFirma configuracionAmpliarFirma = new ConfiguracionAmpliarFirma();
		configuracionAmpliarFirma.setFormatoAmpliacion(ampliacionFirma);
		configuracionAmpliarFirma.setCertificadosFirmantes(listaFirmantes);
		configuracionAmpliarFirma.setIgnorarPeriodoDeGracia(ignorarPeriodoDeGracia);
		return configuracionAmpliarFirma;
	}

	/**
	 * Comprueba si está activado validar firma contra eeutil para una configuracion
	 * @param idConf 
	 * 				Identificador de la configuración.
	 * @return 
	 * 				true si está activado, false en caso contrario.
	 */
	public boolean checkValidarFirma (long idConf)  {
		return util.parametroValorS (idConf, Constants.C_PARAMETER_EEUTIL_VALIDATE_SIGN_ACTIVE, eeUtilConfigManager);		
	}

	/**
	 * Valida una firma
	 * @param sign firma
	 * @param document documento firmado (si es nulo, va implícito en la firma)
	 * @param hash hash firmado (puede ser null).
	 * @param algoritmoHash algoritmo con que se ha calculado el hash
	 * @param tipoFirma tipo de la firma
	 * @param idConf identificador de la configuración
	 * @return "OK" si la firma es válida, motivo de no validez en caso de no ser válida.
	 * @throws EeutilException
	 */
	public String validarFirma (byte[] sign, byte[] document, byte[] hash, String algoritmoHash, String tipoFirma, long idConf) throws EeutilException {
		
		/*EeUtilService port = getEeUtilServicePort();
		ApplicationLogin appInfo = createApplicationLogin();*/
		
		Map<String, String> params = null;
		
		// Carga de la configuración
		params = eeUtilConfigManager.cargarConfiguracion (idConf);
		EeUtilService eeutilOperClient = null;
			
		if (params == null) {
			log.error("Se ha producido un error cargando la configuración del servicio eeutil en la llamada a validarFirma de EEUtilOperFirma");
			//sendMailToAdmin(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILOPERFIRMA);
			throw new EeutilException ("No se han encontrado parametros de configuracion de EEUtilOperFirma");
		}
		
		try {
			eeutilOperClient = eeUtilOperClientManager.getEEUtilOperFirmaClient(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_URL));
		} catch (Throwable t) {
			log.error("Se ha producido un error obteniendo el servicio eeutil en la llamada a validarFirma de EEUtilOperFirma", t);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CREAR_SERVICIO_UTILOPERFIRMA, t);
			throw new EeutilException ("No se ha podido obtener el stub de EEUtilOperFirma", t);
		}
		
		
        ApplicationLogin appInfo = createApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_USER), params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_PASSWORD));
		
		DatosFirmados dFirmados = createDatosFirmados(document, hash, algoritmoHash);
		ResultadoValidacionInfo res = null;
		
		try {
			//res = port.validacionFirma(appInfo, sign, getFormat(tipoFirma), dFirmados);
			// Tipo firma = null para no tener problemas con valide
			res = eeutilOperClient.validacionFirma(appInfo, sign, null, dFirmados);
		} catch (Throwable t) {
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_VALIDAR_FIRMA, t);
			log.error("Se ha producido un error en la llamada a validacionFirma de EEUtilOperFirma", t);
			throw new EeutilException ("Se ha producido un error en la llamada a validacionFirma de EEUtilOperFirma" + t.getMessage());
		}
		
		String resultado = "OK";
		if (!res.isEstado()) {
			resultado = res.getDetalle();
		}
		
		return resultado;
	}

//	private void sendMailToAdmin (String evento) throws EeutilException{
//		
//		String entorno = applicationBO.getEnvironment().getTvalue();
//
//		
//		if (noticeBO.isAdminNoticeEnabled()) {
//			try {
//				noticeService.doNoticeEeutilException(Constants.EMAIL_NOTICE, Constants.NOTICE_EEUTIL_EXCEPTION, evento, entorno, "");
//			} catch (NoticeException e) {
//				log.error("No se puede enviar la notificación a los administradores: " + e.getMessage() + e);
//				throw new EeutilException ("No se puede enviar la notificación a los administradores: ", e);
//			}
//		}
//	}
   
	private static DatosFirmados createDatosFirmados (byte[] document, byte[] hash, String algoritmoHash) {
		DatosFirmados dFirmados = null;
		if (document != null || hash != null || algoritmoHash != null) {
			dFirmados = new DatosFirmados();
			dFirmados.setDocumento(document);
			dFirmados.setHash(hash);
			dFirmados.setAlgoritmo(algoritmoHash);
		}
		return dFirmados;
	}

	/**
	 * Comprueba si está activado validar certificado para la configuracion por defecto.
	 * @return
	 * 				true si está activado, false en caso contrario.
	 */
	public boolean checkValidarCertificado ()  {
		return util.parametroValorS (eeUtilConfigManager.configuracionPorDefecto(), Constants.C_PARAMETER_EEUTIL_VALIDATE_CERT_ACTIVE, eeUtilConfigManager);		
	}

	/**
	 * Valida un certificado
	 * @param certificado
	 * @return
	 * @throws EeutilException
	 */
	@Deprecated
	public ResultadoValidarCertificado validarCertificado (String certificado) throws EeutilException {
		
		/*EeUtilService port = getEeUtilServicePort();
		ApplicationLogin appInfo = createApplicationLogin();*/
		
		Map<String, String> params = null;
		
		// Carga de la configuración
		params = eeUtilConfigManager.cargarConfiguracion (eeUtilConfigManager.configuracionPorDefecto());
		EeUtilService eeutilOperClient = null;
			
		if (params == null) {
			log.error("Se ha producido un error cargando la configuración del servicio eeutil en la llamada a validarCertificado de EEUtilOperFirma");
			//sendMailToAdmin(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILOPERFIRMA);
			throw new EeutilException ("No se han encontrado parametros de configuracion de EEUtilOperFirma");
		}
		
		try {
			eeutilOperClient = eeUtilOperClientManager.getEEUtilOperFirmaClient(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_URL));
		} catch (Throwable t) {
			log.error("Se ha producido un error obteniendo el servicio eeutil en la llamada a validarCertificado de EEUtilOperFirma", t);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CREAR_SERVICIO_UTILOPERFIRMA, t);
			throw new EeutilException ("No se ha podido obtener el stub de EEUtilOperFirma", t);
		}
		
		
        ApplicationLogin appInfo = createApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_USER), params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_PASSWORD));
		
		ResultadoValidarCertificado resultado = null;
		
		try {
			resultado = eeutilOperClient.validarCertificado(appInfo, certificado);
		} catch (Exception e) {
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_VALIDAR_CERTIFICADO, e);
			log.error("Se ha producido un error en la llamada a validarCertificado de EEUtilOperFirma", e);
			throw new EeutilException ("Se ha producido un error en la llamada a validarCertificado de EEUtilOperFirma" + e.getMessage());
		}
		
		return resultado;
	}
	
	/**
	 * Valida un certificado
	 * @param certificado
	 * @return
	 * @throws EeutilException
	 */
	public InfoCertificado getInfoCertificado (String certificado) throws EeutilException {
		
		/*EeUtilService port = getEeUtilServicePort();
		ApplicationLogin appInfo = createApplicationLogin();*/
		
		Map<String, String> params = null;
		
		// Carga de la configuración
		params = eeUtilConfigManager.cargarConfiguracion (eeUtilConfigManager.configuracionPorDefecto());
		EeUtilService eeutilOperClient = null;
			
		if (params == null) {
			log.error("Se ha producido un error cargando la configuración del servicio eeutil en la llamada a getInfoCertificado de EEUtilOperFirma");
			//sendMailToAdmin(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILOPERFIRMA);
			throw new EeutilException ("No se han encontrado parametros de configuracion de EEUtilOperFirma");
		}
		
		try {
			eeutilOperClient = eeUtilOperClientManager.getEEUtilOperFirmaClient(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_URL));
		} catch (Throwable t) {
			log.error("Se ha producido un error obteniendo el servicio eeutil en la llamada a getInfoCertificado de EEUtilOperFirma", t);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CREAR_SERVICIO_UTILOPERFIRMA, t);
			throw new EeutilException ("No se ha podido obtener el stub de EEUtilOperFirma", t);
		}
		
		
        ApplicationLogin appInfo = createApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_USER), params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_PASSWORD));
		
        InfoCertificado resultado = null;
		
		try {
			resultado = eeutilOperClient.getInfoCertificado(appInfo, certificado);
		} catch (Exception e) {
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_INFO_CERTIFICADO, e);
			log.error("Se ha producido un error en la llamada a getInfoCertificado de EEUtilOperFirma", e);
			throw new EeutilException ("Se ha producido un error en la llamada a getInfoCertificado de EEUtilOperFirma" + e.getMessage());
		}
		
		return resultado;
	}
	
	/**
	 * Método que estampa un sello de tiempo en una firma
	 * @param listadoFirmantes Listado de certificados de firmantes de la firma sobre los que se aplica la ampliación
	 * @return Firma con sello de tiempo estampado
	 * @throws EeutilException
	 */
	public byte[] ampliarFirma(String format, byte[] firma, List<byte[]> certificados, long idConf) throws EeutilException {
		Map<String, String> params = null;
		
		// Carga de la configuración
		params = eeUtilConfigManager.cargarConfiguracion (idConf);
		EeUtilService eeutilOperClient = null;
			
		if (params == null) {
			log.error("Se ha producido un error cargando la configuración del servicio eeutil en la llamada a ampliarFirma de EEUtilOperFirma");
			//sendMailToAdmin(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILOPERFIRMA);
			throw new EeutilException ("No se han encontrado parametros de configuracion de EEUtilOperFirma");
		}
		
		try {
			eeutilOperClient = eeUtilOperClientManager.getEEUtilOperFirmaClient(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_URL));
		} catch (Throwable t) {
			log.error("Se ha producido un error obteniendo el servicio eeutil en la llamada a ampliarFirma de EEUtilOperFirma", t);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CREAR_SERVICIO_UTILOPERFIRMA, t);
			throw new EeutilException ("No se ha podido obtener el stub de EEUtilOperFirma", t);
		}
		
		
        ApplicationLogin appInfo = createApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_USER), params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_PASSWORD));

        // Se crea la lista de certificados en caso de existir
        CertificateList certificateList = null;
        if (certificados != null && !certificados.isEmpty()) {
        	certificateList = new CertificateList();
        	certificateList.getCertificates().addAll(certificados);
        }

        ConfiguracionAmpliarFirma configuracionAmpliarFirma = 
        			crearConfiguracionAmpliarFirm(format, certificateList, true);

        byte[] firmaConTimestamp = null;
        try {
        	ResultadoAmpliarFirma resultado = eeutilOperClient.ampliarFirma(appInfo, firma, configuracionAmpliarFirma);
			firmaConTimestamp = resultado.getFirma();
        } catch (InSideException e) {
        	mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_AMPLIAR_FIRMA, e);
        	log.error("InsideException" + e.getFaultInfo().getDescripcion());
        	throw new EeutilException ("Se ha producido un error en la llamada a ampliarFirma de EEUtilOperFirma: " + e.getFaultInfo().getDescripcion(), e);
		} catch (Throwable t) {
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_AMPLIAR_FIRMA, t);
        	log.error("Se ha producido un error en la llamada a ampliarFirma de EEUtilOperFirma: " , t);
        	throw new EeutilException ("Se ha producido un error en la llamada a ampliarFirma de EEUtilOperFirma: " + t.getMessage(), t);
		}

		return firmaConTimestamp;
	}

}
