package ieci.tdw.ispac.api.rule.procedures.recaudacion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
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
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class InitTaskResolucionRule implements IRule {

	private static final Logger logger = Logger.getLogger(InitTaskResolucionRule.class);

	protected String STR_queryDocumentos = "";
	protected String STR_entidad = "";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
    	String numexp = "";
    	try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        //----------------------------------------------------------------------------------------------

	        //Actualiza el campo estado de la entidad
	        //de modo que permita mostrar los enlaces para crear Propuesta/Decreto
	        numexp = rulectx.getNumExp();
	        IItemCollection col = entitiesAPI.getEntities(STR_entidad, numexp);
	        Iterator it = col.iterator();
	        IItem entidad = null;
	        if (it.hasNext())
	        {
		        entidad = (IItem)it.next();
	        }
	        else
	        {
	        	entidad = entitiesAPI.createEntity(STR_entidad, numexp);
	        }
	        entidad.set("ESTADO", "Inicio");
	        entidad.store(cct);
	        
	        //Crea un ZIP con la documentación para la propuesta
	        //--------------------------------------------------
	
	        //Obtenemos los documentos a partir de su nombre
			ArrayList<IItem> filesConcatenate = new ArrayList<IItem>();

			IItemCollection documentsCollection = entitiesAPI.getDocuments(numexp, STR_queryDocumentos, "FDOC DESC");
	        it = documentsCollection.iterator();
	        IItem itemDoc = null;
	        while (it.hasNext()){
	        	itemDoc = (IItem)it.next();
	        	filesConcatenate.add(itemDoc);
			}
			
			//Obtenemos el id del tipo de documento de "Contenido de la propuesta"
			int idTypeDocument = DocumentosUtil.getTipoDoc(cct, "Contenido de la propuesta", DocumentosUtil.BUSQUEDA_EXACTA, false);
			if (idTypeDocument ==  Integer.MIN_VALUE){
				throw new ISPACInfo("Error al obtener el tipo de documento.");
			}
	
			// Crear el zip con los documentos
			//    http://www.manual-java.com/codigos-java/compresion-decompresion-archivos-zip-java.html
			File zipFile = createDocumentsZipFile(genDocAPI, filesConcatenate);
			
	        //Generamos el documento zip
			String sExtension = "zip";
			String sName = "Contenido de la solicitud";//Campo descripción del documento

			cct.beginTX();
			DocumentosUtil.generaYAnexaDocumento(rulectx, idTypeDocument, sName, zipFile, sExtension);
			cct.endTX(true);
			if(zipFile != null && zipFile.exists()) zipFile.delete();
        }
    	catch(ISPACRuleException e){
    		logger.error("Error al iniciar el trámite de resolución del expediente " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("Error al iniciar el trámite de resolución del expediente " + numexp + ". " + e.getMessage(), e);
        } catch (ISPACException e) {
    		logger.error("Error al iniciar el trámite de resolución del expediente " + numexp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("Error al iniciar el trámite de resolución del expediente " + numexp + ". " + e.getMessage(), e);
		}
    	return new Boolean(true);
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
	/**
	 * Crea un zip con los documentos especificados.
	 * @param session API de sesiones.
	 * @param docs Lista de documentos.
	 * @return Fichero zip.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static File createDocumentsZipFile(IGenDocAPI genDocAPI, ArrayList<IItem> docs)
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
