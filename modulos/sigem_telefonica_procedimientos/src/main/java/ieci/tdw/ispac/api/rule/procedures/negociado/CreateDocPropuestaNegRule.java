package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 
 * @author teresa
 * @date 13/11/2009
 * @propósito Actualiza el campo estado de la entidad para mostrar los enlaces de Propuesta/Decreto, crea el documento zip de
 * Contenido de la propuesta a partir de los documentos especificados en la consulta STR_queryDocumentos y lo asocia al trámite actual.
 */
public class CreateDocPropuestaNegRule implements IRule {

	protected String STR_entidad = "";
	protected String STR_queryDocumentos = "" ;	
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(CreateDocPropuestaNegRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
		
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        //----------------------------------------------------------------------------------------------
			
	        //Actualiza el campo estado de la entidad
	        //de modo que permita mostrar los enlaces para crear Propuesta/Decreto
	        String numexp = rulectx.getNumExp();
	        IItemCollection col = entitiesAPI.getEntities(STR_entidad, numexp);
	        Iterator it = col.iterator();
	        if (it.hasNext())
	        {
		        IItem entidad = (IItem)it.next();
		        entidad.set("ESTADO", "Inicio");
		        entidad.store(cct);
	        }
			
	        //Si la consulta STR_queryDocumentos no está vacía generamos el documento zip Contenido de la propuesta y lo asignamos
	        //al trámite actual
	        if (STR_queryDocumentos != null && !STR_queryDocumentos.equals("")){
		        String strQuery = "NOMBRE = '";
		        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_CT_TPDOC, "WHERE " + STR_queryDocumentos);
		        int i = 0;
		        it = collection.iterator();
		        while (it.hasNext() && i<5){
		        	IItem itemDoc = (IItem)it.next();
		        	if (i==0){
		        		strQuery += itemDoc.getString("NOMBRE") + "'";
		        	}else{
		        		strQuery += " OR NOMBRE = '"+ itemDoc.getString("NOMBRE") + "'";
		        	}
		        	i++;
		        }
	
				logger.debug("strQuery: "+strQuery);
				
				IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), strQuery, "FDOC DESC");
	
				List filesConcatenate = new ArrayList();
				i=0;
		        it = documentsCollection.iterator();
		        IItem itemDoc = null;
		        while (it.hasNext() && i<5){
		        	itemDoc = (IItem)it.next();
		        	filesConcatenate.add(itemDoc);
					i++;
				}
				
				
				//Obtenemos el id del tipo de documento de "Contenido de la propuesta"
		        strQuery = "WHERE NOMBRE = 'Contenido de la propuesta'";
		        
				logger.debug("strQuery: "+strQuery);
	
				IItemCollection typesDocumentCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_CT_TPDOC, strQuery);
				
				int idTypeDocument = 0;
				if (typesDocumentCollection!=null && documentsCollection.next()){
					idTypeDocument = ((IItem)typesDocumentCollection.iterator().next()).getInt("ID");
				}
				if (idTypeDocument == 0){
					throw new ISPACInfo("Error al obtener el tipo de documento.");
				}
	
				File zipFile = createDocumentsZipFile(genDocAPI, filesConcatenate);
				
		        //Generamos el documento zip
				String sExtension = "zip";
				String sName = "Contenido de la propuesta";//Campo descripción del documento
				String sMimeType = MimetypeMapping.getMimeType(sExtension);
				if (sMimeType == null) {
				    throw new ISPACInfo("Error al obtener el tipo Mime");
				}
				int keyId = 0;
				IItem entityDocument = null;
				int taskId = rulectx.getTaskId(); 
				int currentId = taskId;
						
				// Ejecución en un contexto transaccional
				boolean bCommit = false;
				
				try {
			        // Abrir transacción para que no se pueda generar un documento sin fichero
			        cct.beginTX();
			        entityDocument = genDocAPI.createTaskDocument(currentId, idTypeDocument);
			        
			        keyId = entityDocument.getKeyInt();
					// Establecer la extensión del documento para mostrar un icono descriptivo del tipo de documento en la lista de documentos
					entityDocument.set("EXTENSION", sExtension);
					entityDocument.store(cct);
					
					
			        InputStream in = new FileInputStream(zipFile);
			        int length = (int)zipFile.length();
			        Object connectorSession = null;
					try {
						
						connectorSession = genDocAPI.createConnectorSession();
						genDocAPI.attachTaskInputStream(connectorSession, currentId, keyId, in, length, sMimeType, sName);
					
					}
					finally {
						if (connectorSession != null) {
							genDocAPI.closeConnectorSession(connectorSession);
						}
			    	}
					// Si todo ha sido correcto se hace commit de la transacción
					bCommit = true;
				}
				catch (Exception e) {
					// Si se produce algún error
					throw new ISPACInfo("Error al generar el documento.");
				}
				finally {
					cct.endTX(bCommit);
				}
			        
		        // Eliminar el zip
		        FileUtils.deleteFile(zipFile);
		}
			
		}catch(ISPACException e){
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
	public static File getFile(IGenDocAPI gendocAPI, String strInfoPag) throws ISPACException
	{
		File file = null;
		Object connectorSession = null;
		
		try
		{
			connectorSession = gendocAPI.createConnectorSession();

			String extension = "doc";
				
			String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			
			OutputStream out = new FileOutputStream(fileName);
			gendocAPI.getDocument(connectorSession, strInfoPag, out);
			
			file = new File(fileName);
		}
		catch(Exception e)
		{
			throw new ISPACException(e); 	
		}
		return file;
	}
	
	
	/**
	 * Crea un zip con los documentos especificados.
	 * @param session API de sesiones.
	 * @param docs Lista de documentos.
	 * @return Fichero zip.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static File createDocumentsZipFile(IGenDocAPI genDocAPI, List docs)
			throws ISPACException {

		// Manejador de ficheros temporales
		FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();

		// Crear un fichero temporal para el zip
		File zipFile = ftMgr.newFile(".zip");

		if (!CollectionUtils.isEmpty(docs)) {

			try {
				// Iniciar el zip
				ZipOutputStream out = new ZipOutputStream(
						new FileOutputStream(zipFile));
				out.setEncoding("CP437");
				
				// Incluir los documentos en el zip
				for (int i = 0; i < docs.size(); i++) {
					createDocumentZipEntry(genDocAPI, out, (IItem) docs.get(i));
				}
	
				// Finalizar el zip
				out.close();
			} catch (IOException e) {
				logger.error("Error al crear el zip: " + zipFile.getAbsolutePath(), e);
			}
		}

		return zipFile;
	}
	
	private static void createDocumentZipEntry(IGenDocAPI genDocAPI,
			ZipOutputStream out, IItem doc) throws ISPACException {

		Object connectorSession = null;
		try {
			connectorSession = genDocAPI.createConnectorSession();

			if (doc != null) {
	
				// Obtener el GUID del documento
				String guid = doc.getString("INFOPAG");
	
				if (StringUtils.isNotBlank(guid)) {
	
					// Incluir la entrada del documento en el zip
					out.putNextEntry(new ZipEntry(getDocumentName(doc)));
	
					// Obtener el contenido del fichero
					genDocAPI.getDocument(connectorSession, guid, out);
	
					// Finalizar entrada de documento
					out.closeEntry();
				}
			}
		} catch (IOException e) {
			logger.warn("Error al incluir el documento", e);
		}finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}				
	}
	
	private static String getDocumentName(IItem doc) throws ISPACException {
		StringBuffer fullName = new StringBuffer();
		
		// Número de expediente
		String numExp = doc.getString("NUMEXP");
		fullName.append(numExp);
		
		// Nombre del documento
		String name = doc.getString("DESCRIPCION");
		if (StringUtils.isBlank(name)) {
			name = doc.getString("NOMBRE");
			if (StringUtils.isBlank(name)) {
				name = new StringBuffer("unknown_").append(doc.getString("ID"))
						.toString();
			}
		}
		fullName.append("_").append(name);
		
		// Identificador del documento
		fullName.append("_").append(doc.getString("ID")); 

		// Extensión del documento
		String ext = doc.getString("EXTENSION");
		if (StringUtils.isNotBlank(ext)) {
			fullName.append(".").append(ext);
		}

		return normalize(fullName.toString());
	}
	
	private static String normalize(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}
	

}
