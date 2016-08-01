package es.dipucr.sigem.api.rule.common.tablon;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.tablon.services.TablonWSProxy;

public class DipucrGeneraTramitePublicacionTablonEdictos implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrGeneraTramitePublicacionTablonEdictos.class);
	
	protected String plantillaAnuncioTablon = "";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {			
			logger.info("INICIO - "+DipucrGeneraTramitePublicacionTablonEdictos.class);
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			
			String titulo = "";
			String descripcion = "";
			String codCategoria = "";
			String codServicio = "";
			String categoria = "";
			String servicio = "";

			String numexp = rulectx.getNumExp();
			
			IItem expediente = rulectx.getClientContext().getAPI().getEntitiesAPI().getExpedient(rulectx.getNumExp());
			titulo = expediente.getString("NOMBREPROCEDIMIENTO");
			descripcion = expediente.getString("ASUNTO");
			
			String departamento = "";
			int id_pcd_actual = ExpedientesUtil.getExpediente(cct, numexp).getInt("ID_PCD");
			IItem procedimiento = procedureAPI.getProcedureById(id_pcd_actual);
			if(procedimiento != null){
				String act_func = (String) procedimiento.get("CTPROCEDIMIENTOS:ACT_FUNC");
				if(StringUtils.isNotEmpty(act_func)){
					codCategoria = act_func;
				}
				departamento = (String) procedimiento.get("CTPROCEDIMIENTOS:ORG_RSLTR");
			}
			
			if(StringUtils.isNotEmpty(departamento)){
				String sqlQueryPart = "WHERE VALOR = '"+departamento+"'";
	        	IItemCollection codServCollection = entitiesAPI.queryEntities("DPCR_MAP_DEP_COD_SERV_TABL", sqlQueryPart);
	        	Iterator codServIterator = codServCollection.iterator();
	        	if (codServIterator.hasNext()){
	        		IItem codServ = (IItem)codServIterator.next();
	        		codServicio = codServ.getString("SUSTITUTO");
	        	}
			}
			        	
			TablonWSProxy wsTablon = new TablonWSProxy();
			String codEntidad = EntidadesAdmUtil.obtenerEntidad((ClientContext) cct);
			
			categoria = wsTablon.getCategoriaByCodigo(codEntidad, codCategoria).getDescripcion();
			servicio = wsTablon.getServicioByCodigo(codEntidad, codServicio).getDescripcion();
			
			plantillaAnuncioTablon = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
			
			IItem eTablon = null; 			
			IItemCollection eTablonCollection = entitiesAPI.getEntities("ETABLON_PUBLICACION", numexp);
			Iterator eTablonIterator = eTablonCollection.iterator();
			if(eTablonIterator.hasNext()){
				eTablon = (IItem) eTablonIterator.next();
			}
			else{
				eTablon = entitiesAPI.createEntity("ETABLON_PUBLICACION", numexp);
			}			
			
			eTablon.set("TITULO", titulo);			
			eTablon.set("DESCRIPCION", descripcion);
			eTablon.set("COD_CATEGORIA", codCategoria);
			eTablon.set("CATEGORIA", categoria);			
			eTablon.set("COD_SERVICIO", codServicio);
			eTablon.set("SERVICIO", servicio);
			
			eTablon.store(cct);
			
			generaAnuncioTablon(rulectx);			
			
			logger.info("FIN - "+DipucrGeneraTramitePublicacionTablonEdictos.class);
		} catch (ISPACException e) { 
			logger.error("NO SE PUEDE RECUPERAR EL ID DEL DOCUMENTO. CONSULTE CON EL ADMINISTRADOR, " + e.getMessage(), e);
			throw new ISPACRuleException("NO SE PUEDE RECUPERAR EL ID DEL DOCUMENTO. CONSULTE CON EL ADMINISTRADOR, " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("NO SE PUEDE RECUPERAR EL ID DEL DOCUMENTO. CONSULTE CON EL ADMINISTRADOR, " + e.getMessage(), e);
			throw new ISPACRuleException("NO SE PUEDE RECUPERAR EL ID DEL DOCUMENTO. CONSULTE CON EL ADMINISTRADOR, " + e.getMessage(), e);
		}
		return null;
	}
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	private void generaAnuncioTablon(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try{
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			cct.endTX(true);					
			
			// Variables
			IItem entityDocument = null;
			int documentTypeId = 0;
			int templateId = 0;
			int taskId = rulectx.getTaskId();
			numexp = rulectx.getNumExp();
			
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			
	    	int documentId = 0;
	    	Object connectorSession = null;
	    	OpenOfficeHelper ooHelper = null;
	    	
			// Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
        	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);     		
    		Iterator it = taskTpDocCollection.iterator();
    		while (it.hasNext()){
    			IItem taskTpDoc = (IItem)it.next();
    			if ((((String)taskTpDoc.get("CT_TPDOC:NOMBRE")).trim().toUpperCase()).equals(("eTablon - Publicación").trim().toUpperCase())){
    				
    				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
    			}
    		}
    		//Asignamos el nombre del trátime ya que si no no lo muestra
    		setSsVariables(cct, rulectx);
    		cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
    		
    		//Comprobamos que haya encontrado el Tipo de documento
    		if (documentTypeId != 0){
        		// Comprobar que el tipo de documento tiene asociado una plantilla
	        	IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
	        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
	        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
	        	}else{
	        		Iterator docs = tpDocsTemplatesCollection.iterator();
	        		boolean encontrado= false;
	        		while (docs.hasNext() && !encontrado){
	        			IItem tpDocsTemplate = (IItem)docs.next();
	        			if(((String)tpDocsTemplate.get("NOMBRE")).trim().toUpperCase().equals(plantillaAnuncioTablon.trim().toUpperCase())){
	        				templateId = tpDocsTemplate.getInt("ID");
	        				encontrado= true;
	        			}
	        		}
	        		IItem entityDocumentT  = genDocAPI.createTaskDocument(taskId, documentTypeId);
					int documentIdT = entityDocumentT.getKeyInt();						
					
					
					IItem entityTemplateT = genDocAPI.attachTaskTemplate(connectorSession, taskId, documentIdT, templateId);
					String infoPagT = entityTemplateT.getString("INFOPAG");
					entityTemplateT.store(cct);
					
	        		
	        		entityDocument  = genDocAPI.createTaskDocument(taskId, documentTypeId);
					documentId = entityDocument.getKeyInt();

					String sFileTemplate = DocumentosUtil.getFile(cct, infoPagT, null, null).getName();
														
					// Generar el documento a partir la plantilla 
					IItem entityTemplate = genDocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, sFileTemplate);
											
					String docref = entityTemplate.getString("INFOPAG");
					String sMimetype = genDocAPI.getMimeType(connectorSession, docref);
					entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
					String mime = "application/vnd.oasis.opendocument.text";
					String templateDescripcion = entityTemplate.getString("DESCRIPCION");
					entityTemplate.set("DESCRIPCION", templateDescripcion);
					entityTemplate.set("DESTINO", cct.getSsVariable("NOMBRE"));
					
					entityTemplate.store(cct);
					
					deleteSsVariables(cct);
					cct.deleteSsVariable("NOMBRE_TRAMITE");
					
					//Abre el documento
					String extension = "odt";
					String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
					fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
					OutputStream out = new FileOutputStream(fileName);
		    		connectorSession = genDocAPI.createConnectorSession();
		    		genDocAPI.getDocument(connectorSession, docref, out);
					File file = new File(fileName);
		    		ooHelper = OpenOfficeHelper.getInstance();
		    		XComponent xComponent = ooHelper.loadDocument("file://" + fileName);
		    		
		    		//Concatenamos las bases
		    		
		    		//Recuperamos el documento Informe del servicio
		    		IItem doc = DocumentosUtil.getUltimaPropuestaFirmada(cct, numexp);
		    		if(doc != null){
		    			String infoPag = doc.getString("INFOPAG");
			        	File fileBases = DocumentosUtil.getFile(cct, infoPag, null, null);
			        	DipucrCommonFunctions.Concatena(xComponent, "file://" + fileBases.getPath(), ooHelper);
			        	fileBases.delete();
					}
		    		
		    		 //Guarda el documento
					String fileNameOut = FileTemporaryManager.getInstance().newFileName(".doc");
					fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
		    		OpenOfficeHelper.saveDocument(xComponent,"file://" + fileNameOut,"");
		    		File fileOut = new File(fileNameOut);
		    		InputStream in = new FileInputStream(fileOut);			    		
		    		genDocAPI.setDocument(connectorSession, documentId, docref, in, (int)(fileOut.length()), mime);

		    		cct.deleteSsVariable("NOMBRE_TRAMITE");
		    		
		    		//Borra archivos temporales
		    		file.delete();
		    		fileOut.delete();
		    		
		    		entityTemplateT.delete(cct);
					entityDocumentT.delete(cct);
					
					DocumentosUtil.deleteFile(sFileTemplate);  
					ooHelper.dispose();
	        	}
    		}	
			cct.endTX(true);
		} catch(Exception e) {
			logger.error("ERROR al generar el documento del anuncio del e-Tablón del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR al generar el documento del anuncio del e-Tablón del expediente: " + numexp + ". " + e.getMessage(), e);	    	
	    }
	}
	
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
	}

	public void deleteSsVariables(IClientContext cct) {	
	}
}