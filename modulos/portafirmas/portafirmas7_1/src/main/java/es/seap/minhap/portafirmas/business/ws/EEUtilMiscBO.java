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

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.XMLUtil;
import es.seap.minhap.portafirmas.ws.eeutil.misc.ApplicationLogin;
import es.seap.minhap.portafirmas.ws.eeutil.misc.ContenidoInfo;
import es.seap.minhap.portafirmas.ws.eeutil.misc.DocumentoEntrada;
import es.seap.minhap.portafirmas.ws.eeutil.misc.EeUtilService;
import es.seap.minhap.portafirmas.ws.eeutil.misc.InSideException;
import es.seap.minhap.portafirmas.ws.eeutil.misc.SalidaVisualizacion;
import es.seap.minhap.portafirmas.ws.eeutil.misc.TCNInfo;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilConfigManager;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilMiscClientManager;;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class EEUtilMiscBO {
	
	@Autowired
	private EeUtilMiscClientManager eeUtilMiscClientManager;
	
	@Autowired
	private EeUtilConfigManager eeUtilConfigManager;

	@Autowired
	private UtilComponent util;
	
	@Autowired
	private MailToAdminBO mailToAdminBO;

	Logger log = Logger.getLogger(EEUtilMiscBO.class);

	/**
	 * Obtiene el PDF a partir de un TCN
	 * @param tcnBytes bytes del TCN
	 * @return bytes del PDF resultante
	 * @throws EeutilException
	 */
	public byte[] getPdfFromTCN (byte[] tcnBytes) throws EeutilException {
		
		Map<String, String> params = null;
		
		// Carga de la configuración
		params = eeUtilConfigManager.cargarConfiguracion (eeUtilConfigManager.configuracionPorDefecto());
		EeUtilService eeutilMiscClient = null;
			
		if (params == null) {
			log.error("Se ha producido un error cargando la configuración del servicio eeutil en la llamada a getPdfFromTCN de EEUtilMisc");
			//sendMailToAdmin(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILMISC);
			throw new EeutilException ("No se han encontrado parametros de configuracion de EEUtilMisc");
		}
		
		try {
			eeutilMiscClient = eeUtilMiscClientManager.getEEUtilMiscClient(params.get(Constants.C_PARAMETER_EEUTIL_MISC_URL));
		} catch (Exception t) {
			log.error("Se ha producido un error obteniendo el servicio eeutil en la llamada a getPdfFromTCN de EEUtilMisc", t);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CREAR_SERVICIO_UTILMISC, t);
			throw new EeutilException ("No se ha podido obtener el stub de EEUtilMisc", t);
		}
		
		
        ApplicationLogin appInfo = createApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_MISC_USER), params.get(Constants.C_PARAMETER_EEUTIL_MISC_PASSWORD));
        
		
		/*EeUtilService port = getReportServicePort();
		
		ApplicationLogin appInfo = createApplicationLogin();*/
		
		TCNInfo tcnInfo = new TCNInfo ();
		ContenidoInfo contenidoInfo = new ContenidoInfo ();
		contenidoInfo.setContenido(tcnBytes);
		contenidoInfo.setTipoMIME("text-tcn/html");
		tcnInfo.setContenido(contenidoInfo);
		
		try {
			contenidoInfo = eeutilMiscClient.convertirTCNAPdf(appInfo, tcnInfo);
		} catch (InSideException ie) {
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CONVERTIR_TCN, ie);
        	log.error("Se ha producido un error en la llamada a convertirTCNAPdf de EEUtilMisc" , ie);
        	throw new EeutilException("Se ha producido un error en la llamada a convertirTCNAPdf de EEUtilMisc" + ie.getMessage());
        } catch (Exception e) {
        	mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CONVERTIR_TCN, e);
        	log.error("Se ha producido un error en la llamada a convertirTCNAPdf de EEUtilMisc" , e);
        	throw new EeutilException("Se ha producido un error en la llamada a convertirTCNAPdf de EEUtilMisc" + e.getMessage());        
        } catch (Throwable t) {
        	mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CONVERTIR_TCN, t);
        	log.error("Se ha producido un error en la llamada a convertirTCNAPdf de EEUtilMisc" , t);
        	throw new EeutilException("Se ha producido un error en la llamada a convertirTCNAPdf de EEUtilMisc" + t.getMessage());
        }
        
        return contenidoInfo.getContenido();
		
	}

	private ApplicationLogin createApplicationLogin (String idaplicacion, String password) throws EeutilException{
		if (idaplicacion == null) {
			throw new EeutilException ("El usuario de EEUtilMisc es nulo");
		}
		if (password == null) {
			throw new EeutilException ("El password de EEUtilMisc es nulo");
		}
		ApplicationLogin appInfo = new ApplicationLogin();
		appInfo.setIdaplicacion(idaplicacion);
		appInfo.setPassword(password);
		/*appInfo.setIdaplicacion(this.user);
		appInfo.setPassword(this.password);*/
        return appInfo;
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
   
	public boolean checkViewTCN (long idConf)  {
		return util.parametroValorS (idConf, Constants.C_PARAMETER_EEUTIL_VISUALIZAR_TCN_ACTIVO, eeUtilConfigManager);		
	}
	
	public boolean isPDFA(byte[] pdfBytes) throws EeutilException, InSideException {
		Map<String, String> params = eeUtilConfigManager.cargarConfiguracion (eeUtilConfigManager.configuracionPorDefecto());
		if (params == null) {
			log.error("Se ha producido un error cargando la configuración del servicio eeutil en la llamada a isPDFA de EEUtilMisc");
			//sendMailToAdmin(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILMISC);
			throw new EeutilException ("No se han encontrado parametros de configuracion de EEUtilMisc");
		}
		
		EeUtilService eeutilMiscClient;
		try {
			eeutilMiscClient = eeUtilMiscClientManager.getEEUtilMiscClient(params.get(Constants.C_PARAMETER_EEUTIL_MISC_URL));
		} catch (Exception e) {
			log.error("Se ha producido un error obteniendo el servicio eeutil en la llamada a isPDFA de EEUtilMisc", e);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CREAR_SERVICIO_UTILMISC, e);
			throw new EeutilException ("No se ha podido obtener el stub de EEUtilMisc", e);
		}
		
		
		ApplicationLogin appInfo = createApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_MISC_USER), params.get(Constants.C_PARAMETER_EEUTIL_MISC_PASSWORD));
		DocumentoEntrada documentoEntrada = new DocumentoEntrada();
		documentoEntrada.setContenido(pdfBytes);
		

		try {
			return eeutilMiscClient.comprobarPDFA(appInfo, documentoEntrada, null);
		} catch (Exception e) {
			log.error("Se ha producido un error en la llamada a comprobarPDFA de EEUtilMisc: " , e);
        	mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_COMPROBAR_PDFA, e);
        	throw new EeutilException ("Se ha producido un error en la llamada a comprobarPDFA de EEUtilMisc: " + e.getMessage());
		}
	}
	
	/**
	 * Obtiene el PDF a partir de una Facturae
	 * @param facturaeBytes bytes del Facturae
	 * @return bytes del PDF resultante
	 * @throws EeutilException
	 */
	public byte[] getPdfFromFacturae (byte[] facturaeBytes) throws EeutilException {
		
		Map<String, String> params = null;
		
		// Carga de la configuración
		params = eeUtilConfigManager.cargarConfiguracion (eeUtilConfigManager.configuracionPorDefecto());
		EeUtilService eeutilMiscClient = null;
			
		if (params == null) {
			log.error("Se ha producido un error cargando la configuración del servicio eeutil en la llamada a getPdfFromFacturae de EEUtilMisc");
			//sendMailToAdmin(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILMISC);
			throw new EeutilException ("No se han encontrado parametros de configuracion de EEUtilMisc");
		}
		
		try {
			eeutilMiscClient = eeUtilMiscClientManager.getEEUtilMiscClient(params.get(Constants.C_PARAMETER_EEUTIL_MISC_URL));
		} catch (Exception t) {
			log.error("Se ha producido un error obteniendo el servicio eeutil en la llamada a getPdfFromFacturae de EEUtilMisc", t);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CREAR_SERVICIO_UTILMISC, t);
			throw new EeutilException ("No se ha podido obtener el stub de EEUtilMisc", t);
		}
		
		ApplicationLogin appInfo = createApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_MISC_USER), params.get(Constants.C_PARAMETER_EEUTIL_MISC_PASSWORD));
        
		String version = XMLUtil.checkVersionFacturae(facturaeBytes);
		
		SalidaVisualizacion salidaVisualizacion = null;
		
		try {
			salidaVisualizacion = eeutilMiscClient.visualizarFacturaePDF(appInfo, facturaeBytes, version);
		} catch (InSideException ie) {
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CONVERTIR_EFACTURA_PDF, ie);
        	log.error("Se ha producido un error en la llamada a visualizarFacturaePDF de EEUtilMisc" , ie);
        	throw new EeutilException("Se ha producido un error en la llamada a visualizarFacturaePDF de EEUtilMisc" + ie.getMessage());
        } catch (Exception e) {
        	mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CONVERTIR_EFACTURA_PDF, e);
        	log.error("Se ha producido un error en la llamada a visualizarFacturaePDF de EEUtilMisc" , e);
        	throw new EeutilException("Se ha producido un error en la llamada a visualizarFacturaePDF de EEUtilMisc" + e.getMessage());        
        } catch (Throwable t) {
        	mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CONVERTIR_EFACTURA_PDF, t);
        	log.error("Se ha producido un error en la llamada a visualizarFacturaePDF de EEUtilMisc" , t);
        	throw new EeutilException("Se ha producido un error en la llamada a visualizarFacturaePDF de EEUtilMisc" + t.getMessage());
        }
        
        return salidaVisualizacion.getDocumentoContenido().getBytesDocumento();
		
	}

	
}
