package es.dipucr.sigem.api.rule.common.documento;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ITemplateAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DipucrTablasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExcelUtils;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipucrAnexaDocumentacionADocumentoRule implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrAnexaDocumentacionADocumentoRule.class);	

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
			return true;
	}
	
	@SuppressWarnings( "rawtypes" )
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			IProcedureAPI procedureAPI = invesFlowAPI.getProcedureAPI();
			ICatalogAPI catalogAPI = cct.getAPI().getCatalogAPI();
			ITemplateAPI templateAPI = cct.getAPI().getTemplateAPI();
	        //----------------------------------------------------------------------------------------------
			
			logger.info("INICIO - " + this.getClass().getName());
	        
	        Object connectorSession = null;
	    	OpenOfficeHelper ooHelper = null;
	    	
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
    			documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
    		}
			
			IItemCollection planDocIterator = catalogAPI.queryCTEntities(ICatalogAPI.ENTITY_CT_TEMPLATE,
					" WHERE ID_TPDOC = "+documentTypeId+" ORDER BY FECHA DESC");
        	/**
        	 * [Teresa] Cambio del if para que no dependa del numero de plantillas
        	 * **/	      
	        //MQE primero miramos si hay alguna plantilla específica si no hay, hacemos lo que hacía, es decir tomar la genérica
        	//if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty())
	        int templateIdTemp = 0;
        	if(planDocIterator!=null){
        		//Primero buscamos si hay alguna específica y cogemos la primera, si no la hay, tomamos la genérica
        		boolean encontrado = false;
        		
        		List plantillas = CollectionBean.getBeanList(planDocIterator);
        		Iterator iter = plantillas.iterator(); 
        		int i = 1;
        		while(iter.hasNext() && !encontrado) {    
        			ItemBean item = (ItemBean) iter.next();
            		templateId = item.getItem().getInt("ID");
            		
            		if(i==1) templateIdTemp = templateId;
            		i++;
        			if(templateAPI.isProcedureTemplate(templateId)){        	
        				IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
            	        if (expediente != null){
	        				String consulta="SELECT ID FROM SPAC_P_PLANTILLAS WHERE ID_P_PLANTDOC ='"+templateId+"' AND ID_PCD='"+expediente.getString("ID_PCD")+"'";
	        				
	        				DbCnt cnt = cct.getConnection();
	        		        ResultSet pertenece = cnt.executeQuery(consulta).getResultSet();
	        		        if(pertenece!= null && pertenece.next()){
	        		        	encontrado = true;     
	        		        }
	        		        cct.releaseConnection(cnt);
            	        }
        			}
        			else{
        				//Si es genérica, la guardamos
        				templateIdTemp = templateId;
        			}
        		}
        		if(!encontrado) templateId = templateIdTemp;
        		//throw new ISPACInfo("No hay ninguna plantilla asociada al tipo de documento");        		
    	    }
			
			cct.setSsVariable("NOMBRE_TRAMITE", processTask.getString("NOMBRE"));
	
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
//				File file = new File(fileName);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + fileName);
    		
    		//Concatenamos la documentacion
    		
    		//Busco los documentos que existan en el trámite Anexar Documentación
    		int id_tramite_pcd = 0;
    		int pcdId = rulectx.getProcedureId();
    		String strQueryTramite = "WHERE ID_PCD="+pcdId+" AND NOMBRE='Anexar Documentación'";
			IItemCollection colTramite = entitiesAPI.queryEntities("SPAC_P_TRAMITES", strQueryTramite);
	        Iterator itTramite = colTramite.iterator();
	        if (itTramite.hasNext())
	        {
		        IItem tramiteTramite = (IItem)itTramite.next();
		        id_tramite_pcd = tramiteTramite.getInt("ID");
	        }

	        IItemCollection itemCol = entitiesAPI.getDocuments(numexp, "ID_FASE_PCD = "+rulectx.getStageProcedureId()+" AND ID_TRAMITE_PCD = "+id_tramite_pcd, "FDOC DESC");
    		Iterator iteratorDoc = itemCol.iterator();

    		while(iteratorDoc.hasNext()){
    			IItem iitemDoc = (IItem)iteratorDoc.next();
    			String infoPagBases = iitemDoc.getString("INFOPAG");
    			String extensionDocumento = iitemDoc.getString("EXTENSION");
    			File file = DocumentosUtil.getFile(cct, infoPagBases, null, null);
    			if(extensionDocumento.equals("doc") || extensionDocumento.equals("odt")){
    	        	DipucrCommonFunctions.concatena(xComponent, "file://" + file.getPath());
    	        	
    			}
    			if(extensionDocumento.equals("xls") || extensionDocumento.equals("ods")){
    				List valoresExcel = ExcelUtils.getAllBySheet(file, 0);
    	    		DipucrTablasUtil.pintaTabla(xComponent, valoresExcel, ExcelUtils.getNumColumn(file, 0), false);
    			}
    			file.delete();
    		}
    		
			 //Guarda el documento
			String fileNameOut = FileTemporaryManager.getInstance().newFileName(".odt");
			fileNameOut = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameOut;
    		OpenOfficeHelper.saveDocument(xComponent,"file://" + fileNameOut,"");
    		File fileOut = new File(fileNameOut);
    		InputStream in = new FileInputStream(fileOut);			    		
    		genDocAPI.setDocument(connectorSession, documentId, docref, in, (int)(fileOut.length()), mime);

    		cct.deleteSsVariable("NOMBRE_TRAMITE");
    		
			if(null != ooHelper){
	        	ooHelper.dispose();
   	        }
    		
    		logger.info("FIN - " + this.getClass().getName());
    		
	        return new Boolean(true);
        }
    	catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido generar el documento.",e);
        }
	}
	
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
	}

	public void deleteSsVariables(IClientContext cct) {	
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}
