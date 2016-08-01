package ieci.tdw.ispac.api.rule.procedures.urbanismo;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

public class InitDecretoIlegalRule implements IRule {

	protected String STR_entidad = "";
	protected String STR_extracto = "";
	protected String STR_estado = "";
	protected String STR_queryDocumento = "";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
    	try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        //----------------------------------------------------------------------------------------------

	        //Obtiene el expediente de la entidad
	        String numexp_decr = rulectx.getNumExp();	
	        String strQuery = "WHERE NUMEXP_HIJO='"+numexp_decr+"'";
	        IItemCollection col = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
	        Iterator it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
        	IItem relacion = (IItem)it.next();
        	String numexp_ent = relacion.getString("NUMEXP_PADRE");
        	col = entitiesAPI.getEntities(STR_entidad, numexp_ent);
	        it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
	        IItem entidad = (IItem)it.next();
        	
	        //Inicializa los datos del Decreto
			//IItem decreto = entitiesAPI.createEntity("SGD_DECRETO", numexp_decr);
	        strQuery = "WHERE NUMEXP='"+numexp_decr+"'";
	        col = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
	        it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
        	IItem decreto = (IItem)it.next();
			if (decreto != null)
			{
				decreto.set("EXTRACTO_DECRETO", STR_extracto);
				decreto.store(cct);
			}else{
				return new Boolean(false);
			}
			
			//Actualiza el campo "estado" de la entidada para
			//que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
	        entidad.set(STR_estado, "Decreto");
	        entidad.store(cct);
			
			//Añade el documento con el contenido del decreto
	        //---------------------------------------------

	        // Obtener el documento del expediente de la entidad
			IItemCollection documentsCollection = entitiesAPI.getDocuments(numexp_ent, STR_queryDocumento, "FDOC DESC");
			IItem contenidoPropuesta = null;
			if (documentsCollection!=null && documentsCollection.next()){
				contenidoPropuesta = (IItem)documentsCollection.iterator().next();
			}else{
				throw new ISPACInfo("No se ha encontrado el documento de la solicitud");
			}
			
			//Obtiene el número de fase del decreto
			String strQueryAux = "WHERE NUMEXP='" + numexp_decr + "'";
			IItemCollection collExpsAux = entitiesAPI.queryEntities("SPAC_FASES", strQueryAux);
			Iterator itExpsAux = collExpsAux.iterator();
			if (! itExpsAux.hasNext()) {
				return new Boolean(false);
			}
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");
			

			// Copiar los valores de los campos INFOPAG - DESCRIPCION - EXTENSION - ID_PLANTILLA
			if (contenidoPropuesta!=null){

				// Crear el documento del mismo tipo que el Contenido del decreto pero asociado al nuevo expediente de Decreto
				IItem nuevoDocumento = (IItem)genDocAPI.createStageDocument(idFase,contenidoPropuesta.getInt("ID_TPDOC"));

				String infopag = contenidoPropuesta.getString("INFOPAG");
				String infopagrde = contenidoPropuesta.getString("INFOPAG_RDE");
				String repositorio = contenidoPropuesta.getString("REPOSITORIO");
				String descripcion = contenidoPropuesta.getString("DESCRIPCION");
				String extension = contenidoPropuesta.getString("EXTENSION");			
				int idPlantilla = contenidoPropuesta.getInt("ID_PLANTILLA");
				nuevoDocumento.set("INFOPAG", infopag);
				nuevoDocumento.set("INFOPAG_RDE", infopagrde);
				nuevoDocumento.set("REPOSITORIO", repositorio);
				nuevoDocumento.set("DESCRIPCION", descripcion);
				nuevoDocumento.set("EXTENSION", extension);
				if (String.valueOf(idPlantilla)!=null && String.valueOf(idPlantilla).trim().length()!=0){
					nuevoDocumento.set("ID_PLANTILLA", idPlantilla);
				}
				try
				{
					String codVerificacion = contenidoPropuesta.getString("COD_VERIFICACION");
					nuevoDocumento.set("COD_VERIFICACION", codVerificacion);
				}
				catch(ISPACException e)
				{
					//No existe el campo
				}

				nuevoDocumento.store(cct);
			}

			return new Boolean(true);
        }
    	catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido inicializar el decreto.",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
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
	/*public static File createDocumentsZipFile(IGenDocAPI genDocAPI, List docs)
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
	}*/

}

