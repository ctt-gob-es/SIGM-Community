package es.dipucr.sigem.api.rule.procedures.subvenciones.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DipucrTablasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExcelUtils;

public class DipucrGeneraInformeIntervConvPlanEmpleoRule extends DipucrAutoGeneraDocIniTramiteRule{

	private static final Logger logger = Logger.getLogger(DipucrGeneraInformeIntervConvPlanEmpleoRule.class);	

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - DipucrGeneraInformeIntervConvPlanEmpleoRule");
		
		tipoDocumento = "Informe de Intervención";
		plantilla = "Informe Intervención Plan de Empleo";
		
		logger.warn("FIN - DipucrGeneraInformeIntervConvPlanEmpleoRule");
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		OpenOfficeHelper ooHelper = null;
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			IProcedureAPI procedureAPI = invesFlowAPI.getProcedureAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        Object connectorSession = null;
	    	ooHelper = OpenOfficeHelper.getInstance();
	    	
	        //Obtiene el expediente
	        String numexp = rulectx.getNumExp();
	        IItem entityDocument = null;
			int documentTypeId = 0;
			int documentId  = 0;
			int templateId = 0;
			int taskId = rulectx.getTaskId();
			
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			
			IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);     		
    		Iterator it = taskTpDocCollection.iterator();
			while (it.hasNext()){
    			IItem taskTpDoc = (IItem)it.next();
    			if ((((String)taskTpDoc.get("CT_TPDOC:NOMBRE")).trim().toUpperCase()).equals((tipoDocumento).trim().toUpperCase())){    				
    				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
    			}
    		}
			
			cct.beginTX();
			setSsVariables(cct, rulectx);
			cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));			
			
			IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
			Iterator docs = tpDocsTemplatesCollection.iterator();
			while (docs.hasNext()){
				IItem tpDocsTemplate = (IItem)docs.next();
				if(((String)tpDocsTemplate.get("NOMBRE")).trim().toUpperCase().equals(plantilla.trim().toUpperCase())){
					templateId = tpDocsTemplate.getInt("ID");
				}
			}
			
    		entityDocument  = genDocAPI.createTaskDocument(taskId, documentTypeId);
    		documentId = entityDocument.getKeyInt();
												
			// Generar el documento a partir la plantilla 
    		IItem entityTemplate = genDocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
									
			String docref = entityTemplate.getString("INFOPAG");
			String sMimetype = genDocAPI.getMimeType(connectorSession, docref);
			entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
			String mime = "application/vnd.oasis.opendocument.text";
			String templateDescripcion = entityTemplate.getString("DESCRIPCION");
			entityTemplate.set("DESCRIPCION", templateDescripcion);

			entityTemplate.store(cct);
			
			//Abre el documento
			String extension = "odt";
			String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			OutputStream out = new FileOutputStream(fileName);
    		connectorSession = genDocAPI.createConnectorSession();
    		genDocAPI.getDocument(connectorSession, docref, out);    		
    		XComponent xComponent = ooHelper.loadDocument("file://" + fileName);
    		
    		//Concatenamos las bases
    		
    		//Recuperamos el documento con las bases
    		IItemCollection itemCol = entitiesAPI.getDocuments(numexp, "NOMBRE='Bases Subvención Plan de Empleo' OR NOMBRE = 'Convocatoria - Bases'", "FDOC DESC");
    		Iterator iteratorDoc = itemCol.iterator();
    		if(iteratorDoc.hasNext()){
    			String infoPagBases = ((IItem)iteratorDoc.next()).getString("INFOPAG");
	        	File fileBases = DocumentosUtil.getFile(cct, infoPagBases, null, null);
	        	DipucrCommonFunctions.concatena(xComponent, "file://" + fileBases.getPath());
	        	fileBases.delete();
    		}
    		
    		//Insertamos los valores del XML
    		List valoresExcel = null;
		    
    		//Recuperamos el documento excel
    		itemCol = entitiesAPI.getDocuments(numexp, "NOMBRE='Datos Subvención Plan de Empleo' OR NOMBRE='Convocatoria - Datos'", "FDOC DESC");
    		iteratorDoc = itemCol.iterator();
    		if(iteratorDoc.hasNext()){
    			String infoPagBases = ((IItem)iteratorDoc.next()).getString("INFOPAG");
	        	File fileExcel = DocumentosUtil.getFile(cct, infoPagBases, null, null);
	        	
	        	valoresExcel = ExcelUtils.getAllBySheet(fileExcel, 0);
	    		DipucrTablasUtil.pintaTabla(xComponent, valoresExcel, ExcelUtils.getNumColumn(fileExcel, 0), false);
	    		fileExcel.delete();
    		}

    		//Recuperamos el documento con los Anexos
    		itemCol = entitiesAPI.getDocuments(numexp, "NOMBRE='Anexos Subvencion Plan de Empleo' OR NOMBRE = 'Convocatoria - Anexos'", "FDOC DESC");
    		iteratorDoc = itemCol.iterator();
    		if(iteratorDoc.hasNext()){
    			String infoPagAnexos = ((IItem)iteratorDoc.next()).getString("INFOPAG");
	        	File fileAnexos = DocumentosUtil.getFile(cct, infoPagAnexos, null, null);
	        	DipucrCommonFunctions.concatena(xComponent, "file://" + fileAnexos.getPath());
	        	fileAnexos.delete();
    		}
			
			 //Guarda el documento
			String fileNameOut = FileTemporaryManager.getInstance().newFileName(".odt");
			fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
    		OpenOfficeHelper.saveDocument(xComponent,"file://" + fileNameOut,"");
    		File fileOut = new File(fileNameOut);
    		InputStream in = new FileInputStream(fileOut);			    		
    		genDocAPI.setDocument(connectorSession, documentId, docref, in, (int)(fileOut.length()), mime);

    		deleteSsVariables(cct);
    		cct.deleteSsVariable("NOMBRE_TRAMITE");    	
    		
    		cct.endTX(true);    		
	        return new Boolean(true);
        }
    	catch(Exception e) 
        {
        	logger.error("No se ha podido generar el documento. " + e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido generar el documento. " + e.getMessage(), e);
        } finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
	}
	
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			cct.setSsVariable("ANIO", ""+Calendar.getInstance().get(Calendar.YEAR));
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void deleteSsVariables(IClientContext cct) {	
		try {
			cct.deleteSsVariable("ANIO");
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}
}