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
import es.seap.minhap.portafirmas.ws.eeutil.vis.ApplicationLogin;
import es.seap.minhap.portafirmas.ws.eeutil.vis.DocumentoContenido;
import es.seap.minhap.portafirmas.ws.eeutil.vis.EeUtilService;
import es.seap.minhap.portafirmas.ws.eeutil.vis.InSideException;
import es.seap.minhap.portafirmas.ws.eeutil.vis.Item;
import es.seap.minhap.portafirmas.ws.eeutil.vis.SalidaVisualizacion;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilConfigManager;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilVisClientManager;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class EEUtilVisBO {
	
	@Autowired
	private EeUtilVisClientManager eeUtilVisClientManager;
	
	@Autowired
	private EeUtilConfigManager eeUtilConfigManager;

	@Autowired
	private MailToAdminBO mailToAdminBO;
	
	Logger log = Logger.getLogger(EEUtilVisBO.class);

	/**
	 * Obtiene el PDF a partir de un fichero
	 * @param identificador identificador del documento
	 * @param nombre nombre del documento
	 * @param documento bytes del documento
	 * @param dmime mime del documento
	 * @return bytes del PDF resultante
	 * @throws EeutilException
	 */
	public byte[] getVisualizarContenidoOriginal (String identificador, String nombre, byte[] documento, String dmime) throws EeutilException {
		
		Map<String, String> params = null;
		
		// Carga de la configuración
		params = eeUtilConfigManager.cargarConfiguracion (eeUtilConfigManager.configuracionPorDefecto());
		EeUtilService eeutilVisClient = null;
			
		if (params == null) {
			log.error("Se ha producido un error cargando la configuración del servicio eeutil en la llamada a visualizarContenidoOriginal de EEUtilVis");
			//sendMailToAdmin(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILVIS);
			throw new EeutilException ("No se han encontrado parametros de configuracion de EEUtilVis");
		}
		
		try {
			eeutilVisClient = eeUtilVisClientManager.getEEUtilVisClient(params.get(Constants.C_PARAMETER_EEUTIL_VIS_URL));
		} catch (Exception t) {
			log.error("Se ha producido un error obteniendo el servicio eeutil en la llamada a visualizarContenidoOriginal de EEUtilVis", t);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CREAR_SERVICIO_UTILVIS, t);
			throw new EeutilException ("No se ha podido obtener el stub de EEUtilVis", t);
		}
		
		
        ApplicationLogin appInfo = createApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_VIS_USER), params.get(Constants.C_PARAMETER_EEUTIL_VIS_PASSWORD));
        
		Item item = new Item();
		item.setIdentificador("PF"+identificador);
		item.setNombre(nombre);
		DocumentoContenido documentoContenido = new DocumentoContenido();
		documentoContenido.setBytesDocumento(documento);
		documentoContenido.setMimeDocumento(dmime);
		item.setDocumentoContenido(documentoContenido);
		
		SalidaVisualizacion salidaVisualizacion = new SalidaVisualizacion();
		try {
			salidaVisualizacion = eeutilVisClient.visualizarContenidoOriginal(appInfo, item);
		} catch (InSideException ie) {
        	log.error("Se ha producido un error en la llamada a visualizarContenidoOriginal de EEUtilVis" , ie);
        	mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_VISUALIZAR_CONTENIDO_ORIGINAL, ie);
        	throw new EeutilException("Se ha producido un error en la llamada a visualizarContenidoOriginal de EEUtilVis" + ie.getMessage());
        } catch (Exception e) {
        	log.error("Se ha producido un error en la llamada a visualizarContenidoOriginal de EEUtilVis" , e);
        	mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_VISUALIZAR_CONTENIDO_ORIGINAL, e);
        	throw new EeutilException("Se ha producido un error en la llamada a visualizarContenidoOriginal de EEUtilVis" + e.getMessage());        
        } catch (Throwable t) {
        	log.error("Se ha producido un error en la llamada a visualizarContenidoOriginal de EEUtilVis" , t);
        	mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_VISUALIZAR_CONTENIDO_ORIGINAL, t);
        	throw new EeutilException("Se ha producido un error en la llamada a visualizarContenidoOriginal de EEUtilVis" + t.getMessage());
        }
        
        return salidaVisualizacion.getDocumentoContenido().getBytesDocumento();
		
	}

	private ApplicationLogin createApplicationLogin (String idaplicacion, String password) throws EeutilException{
		if (idaplicacion == null) {
			throw new EeutilException ("El usuario de EEUtilVis es nulo");
		}
		if (password == null) {
			throw new EeutilException ("El password de EEUtilVis es nulo");
		}
		ApplicationLogin appInfo = new ApplicationLogin();
		appInfo.setIdaplicacion(idaplicacion);
		appInfo.setPassword(password);
		/*appInfo.setIdaplicacion(this.user);
		appInfo.setPassword(this.password);*/
        return appInfo;
	}
   
//private void sendMailToAdmin (String evento) throws EeutilException{
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
	

	
}
