package es.dipucr.sigem.api.rule.common.cartaDigital;

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
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

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

public class DipucrGeneraCartaDigital implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrGeneraCartaDigital.class);
	private String plantillaCartaDigital = "";
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {		
		try{
			logger.info("INICIO - " + this.getClass().getName());
			plantillaCartaDigital = DocumentosUtil.getPlantillaDefecto(rulectx.getClientContext(), rulectx.getTaskProcedureId());
		}
		catch(ISPACException e){
			logger.error("Error al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		try{
		
			generaCarta(rulectx);
			logger.info("FIN - " + this.getClass().getName());
		} catch (ISPACException e) {
			logger.error("NO SE PUEDE RECUPERAR EL ID DEL DOCUMENTO. CONSULTE CON EL ADMINISTRADOR, " + e.getMessage(), e);
			throw new ISPACRuleException("NO SE PUEDE RECUPERAR EL ID DEL DOCUMENTO. CONSULTE CON EL ADMINISTRADOR, " + e.getMessage(), e);	    	
	    }
		return null;
	}

	@SuppressWarnings("rawtypes")
	private void generaCarta(IRuleContext rulectx) throws ISPACRuleException {
		OpenOfficeHelper ooHelper = null;
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
	    	
			// Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
        	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);     		
    		Iterator it = taskTpDocCollection.iterator();
    		while (it.hasNext()){
    			IItem taskTpDoc = (IItem)it.next();
    			if ((((String)taskTpDoc.get("CT_TPDOC:NOMBRE")).trim().toUpperCase()).equals(("Comunicación generica").trim().toUpperCase())){
    				
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
	        			if(((String)tpDocsTemplate.get("NOMBRE")).trim().toUpperCase().equals(plantillaCartaDigital.trim().toUpperCase())){
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
					//[eCenpri_Manu Ticket #249]
		    		if(null != out) out.close();
		    		
		    		entityTemplateT.delete(cct);
					entityDocumentT.delete(cct);
					
					ooHelper.dispose();  		
	        	}
    		}	
			cct.endTX(true);
		} catch(Exception e) {
			logger.error("Error al generar la carta digital en el expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la carta digital en el expediente: " + numexp + ". " + e.getMessage(), e);	    	
	    }
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
	}

	public void deleteSsVariables(IClientContext cct) {	
	}	
}
