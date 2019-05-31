package es.ieci.tecdoc.isicres.document.connector.alfrescoCMIS;

import org.apache.log4j.Logger;

import ctsi.alfresco.cmis.CatalogoAlfrescoCmis;
import es.ieci.tecdoc.isicres.document.connector.alfresco.vo.AlfrescoConnectorConfigurationVO;


/**
 * 
 * @author Iecisa 
 * @version $Revision$ 
 *
 */
public class AlfrescoCMISConnection {

	private static final Logger log = Logger.getLogger(AlfrescoCMISConnection.class);
	
	/*private RepositoryServiceSoapBindingStub repository;
	private ContentServiceSoapBindingStub  contentRepository;
	private AuthoringServiceSoapBindingStub authoringService;*/
	private CatalogoAlfrescoCmis catalogoAlfrescoCmis; 

 
	/**
	 * Inicio de la conexión
	 * @throws Exception 
	 * 
	 */
	public void connection(AlfrescoConnectorConfigurationVO connectorVO) throws Exception{			
		/*// Se establece la ruta de conexion cn el api
		WebServiceFactory.setEndpointAddress(connectorVO.getPathAPI());
			
		//Se inicia Sesion en Alfresco
		try {
			AuthenticationUtils.startSession(connectorVO.getUsuario(), connectorVO.getPass());
		} catch (AuthenticationFault e) {
			// TODO Auto-generated catch block
			log.error("Error al establecer la conexion", e);
			throw e;
		}				
					
		// Se recupera el servicio para acceder al repositorio
		repository = WebServiceFactory.getRepositoryService();
		contentRepository = WebServiceFactory.getContentService();
		authoringService = WebServiceFactory.getAuthoringService();*/
		
		String servidor = connectorVO.getProtocolo() + "://" + connectorVO.getIp(); 
		catalogoAlfrescoCmis = new CatalogoAlfrescoCmis(servidor, connectorVO.getPuerto(), connectorVO.getUsuario(), connectorVO.getPass(),  connectorVO.getVersion());
		
	}
	
	/**
	 * Se cierra la conexion
	 */
	public void endConnection(){		
		//AuthenticationUtils.endSession();
		if (catalogoAlfrescoCmis != null) {
			catalogoAlfrescoCmis.closeSession();
		}
	}

	
	public CatalogoAlfrescoCmis getCatalogoAlfrescoCmis() {
		return catalogoAlfrescoCmis;
	}

	public void setCatalogoAlfrescoCMIS(CatalogoAlfrescoCmis catalogoAlfrescoCmis) {
		this.catalogoAlfrescoCmis = catalogoAlfrescoCmis;
	}
}