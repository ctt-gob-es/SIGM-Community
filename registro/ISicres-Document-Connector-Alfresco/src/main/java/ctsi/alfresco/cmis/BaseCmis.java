package ctsi.alfresco.cmis;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.MimetypesFileTypeMap;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;

//import ctsi.ExceptionCTSI;
//import ctsi.GeneralCTSI;

public class BaseCmis {
	
	private String ALFRESCO_SERVER;
	private String ALFRESCO_PORT;
	private String ALFRESCO_USER;
	private String ALFRESCO_PWD;
	private String ALFRESCO_VERSION;
			
	public static final String WRITE_ENCODING = "UTF-8";
	public static final String CMIS_DOCUMENT = "cmis:document";
	public static final String GROUP_PREFIX = "GROUP_";
	
	public static final String CONTENT_QNAME_ALFRESCO = "{http://www.alfresco.org/model/content/1.0}";
	public static final String SITE_QNAME_ALFRESCO = "{http://www.alfresco.org/model/site/1.0}";
	public static final String OBJECT_COORDINATOR = CONTENT_QNAME_ALFRESCO + "cmobject.Coordinator";
	public static final String OBJECT_COLLABORATOR = CONTENT_QNAME_ALFRESCO + "cmobject.Collaborator";
	public static final String OBJECT_EDITOR = CONTENT_QNAME_ALFRESCO + "cmobject.Editor";
	public static final String OBJECT_CONSUMER = CONTENT_QNAME_ALFRESCO + "cmobject.Consumer";
	public static final String OBJECT_CONTRIBUTOR = CONTENT_QNAME_ALFRESCO + "cmobject.Contributor";
	
	public static final String SITE_COLLABORATOR = SITE_QNAME_ALFRESCO + "site.SiteCollaborator";
	public static final String SITE_CONTRIBUTOR = SITE_QNAME_ALFRESCO + "site.SiteContributor";
	public static final String SITE_MANAGER = SITE_QNAME_ALFRESCO + "site.SiteManager";
	public static final String SITE_CONSUMER = SITE_QNAME_ALFRESCO + "site.SiteConsumer";
	
	public static final String UUID_PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
	public static final String TOKEN_EXCEPTION_STR = "The security token could not be authenticated or authorized";
	
	public static final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:");
	public MimetypesFileTypeMap mediaTypes = new MimetypesFileTypeMap();
	
	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private HttpRequestFactory requestFactory;
	
	private Session session;
	
	public BaseCmis(Properties p) throws Exception{
		// Carga de las propiedades necesarias para conexión a Alfresco.
		ALFRESCO_SERVER = p.getProperty("ALFRESCO_SERVER");
		// OJO: El puerto es opcional. ALFRESCO_SERVER puede tener incluido el puerto también
		// Se mantiene por compatibilidad con la versión de WS
		ALFRESCO_PORT = p.getProperty("ALFRESCO_PORT");
		ALFRESCO_USER = p.getProperty("ALFRESCO_USER");
		ALFRESCO_PWD = p.getProperty("ALFRESCO_PWD");
		ALFRESCO_VERSION = p.getProperty("ALFRESCO_VERSION");
		
		if(!hasPropertiesActivo()){
			throw new Exception("No se han podido cargar algún parámetro de configuración inicial");
		}
		
		//RegisterMediaTypes
		registerMediaTypes();
	}
	
	public BaseCmis(String alfrescoServer, String alfrescoPort, String alfrescoUser, String alfrescoPwd, String alfrescoVersion)  throws Exception{
		ALFRESCO_SERVER = alfrescoServer;
		ALFRESCO_PORT = alfrescoPort;
		ALFRESCO_USER = alfrescoUser;
		ALFRESCO_PWD = alfrescoPwd;
		ALFRESCO_VERSION = alfrescoVersion;
		
		//RegisterMediaTypes
		registerMediaTypes();
	}
	
	// Devuelve la URL del servidor de alfresco, puerto incluído
	public String getAlfrescoServerUrl() {
		// OJO: El puerto puede ser opcional
		String alfrescoServerUrl = ALFRESCO_SERVER;
		if (!ALFRESCO_PORT.isEmpty()) {
			alfrescoServerUrl += ":" + ALFRESCO_PORT;
		}
		
		return alfrescoServerUrl;
	}
	
	public String getAlfrescoAPIUrl() {
       return getAlfrescoServerUrl() + "/alfresco/service/api/";
    }
	
	public String getAlfrescoSlingshotUrl(){
		return getAlfrescoServerUrl() + "/alfresco/service/slingshot/";
	}
	
	public String getAlfrescoKensoftUrl() {
	       return getAlfrescoServerUrl() + "/alfresco/service/keensoft/";
	    }
	
	/**
	 * Comprueba que todas las propiedades están inicializadas
	 * */
	private boolean hasPropertiesActivo(){
		if(!ALFRESCO_SERVER.isEmpty() || !ALFRESCO_USER.isEmpty() && !ALFRESCO_PWD.isEmpty()){ 
			return false;
		} else return true;
	}
	
	/**
	 * Registramos nuevos MimeTypes
	 * */
	private void registerMediaTypes() {
	  // Common MIME types used for uploading attachments.
	  mediaTypes = new MimetypesFileTypeMap();
	  mediaTypes.addMimeTypes("application/msword doc");
	  mediaTypes.addMimeTypes("application/vnd.ms-excel xls");
	  mediaTypes.addMimeTypes("application/pdf pdf");
	  mediaTypes.addMimeTypes("text/richtext rtx");
	  mediaTypes.addMimeTypes("text/csv csv");
	}
	
	private String getAlfrescoUser(){ return this.ALFRESCO_USER; }
	private String getAlfrescoPwd(){ return this.ALFRESCO_PWD; }
	
	
	/**
	 * ###############################################################################################################################
	 * **************************** SESIÓN ****************************************
	 * ###############################################################################################################################
	 * **/	
	/**
	 * Inicia la sesión con alfresco. Necesita que todas las propiedades hayan sido inicializadas.
	 * 
	 * @return ticket de la sesión con alfresco.
	 * @throws ExceptionCTSI
	 */
	public Session getCmisSession() throws Exception{
		if (session == null) { 			
			Map<String, String> parameter = new HashMap<String, String>();
			parameter.put(SessionParameter.USER, this.ALFRESCO_USER);
			parameter.put(SessionParameter.PASSWORD, this.ALFRESCO_PWD);
			parameter.put(SessionParameter.ATOMPUB_URL, getAlfrescoServerUrl() + AlfrescoURLResolver.getCmisURL(ALFRESCO_VERSION));
			parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
			parameter.put(SessionParameter.COMPRESSION, "true"); 
			SessionFactory factory = SessionFactoryImpl.newInstance();
			session = factory.getRepositories(parameter).get(0).createSession();
	    }
	    return session;
	}
	
	/**
	 * Cierra la sessión activa con alfresco y libera el ticket
	 * */
	public void closeSession(){
		this.session.clear();
		this.session = null;
	}
	
	/**
     * Uses basic authentication to create an HTTP request factory.
     *
     * @return HttpRequestFactory
     */
    public HttpRequestFactory getRequestFactory() {
        if (this.requestFactory == null) {
            this.requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                    request.setParser(new JsonObjectParser(new JacksonFactory()));
                    request.getHeaders().setBasicAuthentication(getAlfrescoUser(), getAlfrescoPwd());
                }
            });
        }
        return this.requestFactory;
    }
	
	/**
	 * ###############################################################################################################################
	 * **************************** UTILIDADES ****************************************
	 * ###############################################################################################################################
	 * **/	
	public static String getGuid(String id){
		if(id.indexOf(";") > 0) return id.substring(0, id.indexOf(";"));
		else return id;
	}
	public ItemIterable<CmisObject> getRootFolderContents() {
		Folder rootFolder = (Folder) this.session.getRootFolder();
		return rootFolder.getChildren();
	}
}
