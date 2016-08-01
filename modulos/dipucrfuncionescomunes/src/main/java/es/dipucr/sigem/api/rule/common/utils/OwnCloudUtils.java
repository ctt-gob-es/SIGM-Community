package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.jackrabbit.webdav.DavServletResponse;
import org.apache.log4j.Logger;

import es.dipucr.ownCloud.EasySSLSocketFactory;
import es.dipucr.ownCloud.OwnCloudConfiguration;
import es.dipucr.ownCloud.RemoteFile;
import es.dipucr.ownCloud.WebdavClient;
import es.dipucr.ownCloud.WebdavUtils;

public class OwnCloudUtils {

	private static final Logger logger = Logger.getLogger(OwnCloudUtils.class);

	public static final String COD_ERROR = "ERROR";
	public static final String COD_OK = "OK";

	public static final int ESTADO_ERROR = -1;
	public static final int ESTADO_CREADO_CORRECTO = DavServletResponse.SC_CREATED;
	public static final int ESTADO_YA_EXISTE = DavServletResponse.SC_CONFLICT;
	public static final String MIMETYPE_DIRECTORIOS = "DIR";

	private static MultiThreadedHttpConnectionManager mConnManager = null;

	/** Default timeout for waiting data from the server */
	private static final int DEFAULT_DATA_TIMEOUT = 60000;
	/** Default timeout for establishing a connection */
	private static final int DEFAULT_CONNECTION_TIMEOUT = 60000;	
	
	public static String compartir(String username, String password, String objCompartir) {
		String resultado = OwnCloudUtils.COD_ERROR;
		try {
			OwnCloudConfiguration ownCloudConfig = OwnCloudConfiguration.getInstance();
			
			String dirCloud = ownCloudConfig.getProperty(OwnCloudConfiguration.URL_OWNCLOUD);
			WebdavClient cliente = OwnCloudUtils.crearOwnCloudCliente(dirCloud, username, password);
			
			String urlCompartir = ownCloudConfig.getProperty(OwnCloudConfiguration.DIR_COMPARTIR);
			resultado = cliente.compartir(urlCompartir, WebdavUtils.encodePath(objCompartir));
			if(StringUtils.isEmpty(resultado)) resultado = OwnCloudUtils.COD_ERROR;
			
		} catch (ISPACException e) {
			logger.error("Error al compartir el archivo/carpeta: " + objCompartir + ". " + e.getMessage(), e);
		}
		return resultado;
	}
	
	public static boolean crear(String username, String password, String path) {
		boolean resultado = false;
		try {
			OwnCloudConfiguration ownCloudConfig = OwnCloudConfiguration.getInstance();
			
			String dirCloud = ownCloudConfig.getProperty(OwnCloudConfiguration.URL_OWNCLOUD);
			WebdavClient cliente = OwnCloudUtils.crearOwnCloudCliente(dirCloud, username, password);
			
			String urlWebdav = ownCloudConfig.getProperty(OwnCloudConfiguration.DIR_WEBDAV_OWNCLOUD);
			if (!OwnCloudUtils.existe(cliente, path))
				resultado = cliente.createDirectory(urlWebdav + WebdavUtils.encodePath(path));
			else resultado = true;

		} catch (ISPACException e) {
			logger.error("Error al crear el archivo/carpeta: " + path + ", con el usuario: " + username + ", password: " + password + ". " + e.getMessage(), e);
		}
		return resultado;
	}	
	
	public static boolean deleteCarpeta(String username, String password, String nombreCarpeta) {
		boolean resultado = false;
		try {
			OwnCloudConfiguration ownCloudConfig = OwnCloudConfiguration.getInstance();
			
			String dirCloud = ownCloudConfig.getProperty(OwnCloudConfiguration.URL_OWNCLOUD);
			WebdavClient cliente = OwnCloudUtils.crearOwnCloudCliente(dirCloud, username, password);
			
			String urlCompartir = ownCloudConfig.getProperty(OwnCloudConfiguration.DIR_WEBDAV_OWNCLOUD);

			if (OwnCloudUtils.existe(cliente, nombreCarpeta)){

				resultado = cliente.deleteFile(urlCompartir+"/"+nombreCarpeta);				
			}
				

		} catch (ISPACException e) {
			logger.error("Error al eliminar el archivo/carpeta: " + nombreCarpeta + ", con el usuario: " + username + ", password: " + password + ". " + e.getMessage(), e);
		}
		return resultado;
	}
	
	public static boolean descargarDoc(String username, String password, String docDescargar, File fileLocal) {
		boolean resultado = false;
		try {
			OwnCloudConfiguration ownCloudConfig = OwnCloudConfiguration.getInstance();
			
			String dirCloud = ownCloudConfig.getProperty(OwnCloudConfiguration.URL_SIN_OWNCLOUD);
			WebdavClient cliente = OwnCloudUtils.crearOwnCloudCliente(dirCloud, username, password);
			

			resultado = cliente.downloadFile(docDescargar, fileLocal);
				

		} catch (ISPACException e) {
			logger.error("Error al descargar el documento: " + docDescargar + ", con el usuario: " + username + ", password: " + password + ". " + e.getMessage(), e);
		}
		return resultado;
	}
	
	public static boolean existe(String username, String password, String path){
		boolean resultado = false;
		try {
			OwnCloudConfiguration ownCloudConfig = OwnCloudConfiguration.getInstance();
			
			String dirCloud = ownCloudConfig.getProperty(OwnCloudConfiguration.URL_OWNCLOUD);
			WebdavClient cliente = OwnCloudUtils.crearOwnCloudCliente(dirCloud, username, password);
			
			resultado = OwnCloudUtils.existe(cliente, path);
		} catch (ISPACException e) {
			logger.error("Error al comprobar si existe el archivo/carpeta: " + path + ", con el usuario: " + username + ", password: " + password + ". " + e.getMessage(), e);
		}
		return resultado;
	}

	public static ArrayList<Object> getContenidoCarpeta(String username, String password, String nombreCarpeta) {
		ArrayList<Object> resultado = null;
		String urlCompartir = "";
		try {
			OwnCloudConfiguration ownCloudConfig = OwnCloudConfiguration.getInstance();
			
			String dirCloud = ownCloudConfig.getProperty(OwnCloudConfiguration.URL_OWNCLOUD);
			WebdavClient cliente = OwnCloudUtils.crearOwnCloudCliente(dirCloud, username, password);
			
			urlCompartir = ownCloudConfig.getProperty(OwnCloudConfiguration.DIR_WEBDAV_OWNCLOUD);
	
			if (OwnCloudUtils.existe(cliente, nombreCarpeta)){
				resultado = cliente.getContenidoCarpeta(urlCompartir+"/"+nombreCarpeta);
			}
		} catch (ISPACException e) {
			logger.error("Error al obtener el contenido de la carpeta: " + nombreCarpeta + ", con el usuario: " + username + ", password: " + password + ". " + e.getMessage(), e);
		}
		return resultado;
	}

	public static String getNombreDoc(RemoteFile fichero){
		
		String nombreDoc = "";
		String url = fichero.getRemotePath();
		
		String[] tmp = url.split("/");
	    if (tmp.length > 0){
	    	try {
				nombreDoc = URLDecoder.decode(tmp[tmp.length - 1], "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("Error al decodificar el nombre del archivo/carpeta: " + tmp[tmp.length - 1] + ". " + e.getMessage(), e);
				nombreDoc = tmp[tmp.length - 1];
			}
	    }
	    return nombreDoc;
	}

	public static ArrayList<Object> obtenerDocsCarpeta(String username, String password, String nombreCarpeta) {
		ArrayList<Object> resultado = new ArrayList<Object>();
		try {
			ArrayList<Object> resultadoTemp = getContenidoCarpeta(username, password, nombreCarpeta);
			for(int i = 0; resultadoTemp != null && i < resultadoTemp.size(); i++){
				RemoteFile fichero = (RemoteFile) resultadoTemp.get(i);
				if(!fichero.getMimeType().equals(OwnCloudUtils.MIMETYPE_DIRECTORIOS)){
					resultado.add(fichero);
				}
			}
		} catch (Exception e) {
			logger.error("Error al recuperar todos los documentos insertados en la carpeta: " + nombreCarpeta + ", con el usuario usuario: " + username + ", password: " + password + ". " + e.getMessage(), e);
		}
		return resultado;
	}

	private static boolean existe(WebdavClient cliente, String path){
		boolean resultado = false;
		try {
			OwnCloudConfiguration ownCloudConfig = OwnCloudConfiguration.getInstance();			
			
			String urlWebdav = ownCloudConfig.getProperty(OwnCloudConfiguration.DIR_WEBDAV_OWNCLOUD);			
			resultado = cliente.existsFile(urlWebdav + WebdavUtils.encodePath(path));
			
		} catch (ISPACException e) {
			logger.error("Error al comprobar si existe el archivo/carpeta: " + path + ". " + e.getMessage(), e);
		}
		return resultado;
	}
	
	private static WebdavClient createOwnCloudClient(String url) {

		allowSelfsignedCertificates(true);

		WebdavClient client = new WebdavClient(getMultiThreadedConnManager());

		allowSelfsignedCertificates(true);
		client.setDefaultTimeouts(DEFAULT_DATA_TIMEOUT,	DEFAULT_CONNECTION_TIMEOUT);
		client.setBaseUrl(url);

		return client;
	}
	
	private static WebdavClient crearOwnCloudCliente(String url, String username, String password) throws ISPACException {
		WebdavClient cliente = null;

		cliente = createOwnCloudClient(url);
		cliente.setCredentials(username, password);

		return cliente;
	}
	
	private static MultiThreadedHttpConnectionManager getMultiThreadedConnManager() {
		if (mConnManager == null) {
			mConnManager = new MultiThreadedHttpConnectionManager();
			mConnManager.getParams().setDefaultMaxConnectionsPerHost(5);
			mConnManager.getParams().setMaxTotalConnections(5);
		}
		return mConnManager;
	}

	private static void allowSelfsignedCertificates(boolean allow) {
		Protocol pr = null;
		try {
			pr = Protocol.getProtocol("https");
		} catch (IllegalStateException e) {
			// nothing to do here; really
		}
		boolean isAllowed = (pr != null && pr.getSocketFactory() instanceof EasySSLSocketFactory);
		if (allow && !isAllowed) {
			Protocol.registerProtocol("https", new Protocol("https", new EasySSLSocketFactory(), 443));
		} else if (!allow && isAllowed) {
			// TODO - a more strict SocketFactory object should be provided here
		}
	}

}
