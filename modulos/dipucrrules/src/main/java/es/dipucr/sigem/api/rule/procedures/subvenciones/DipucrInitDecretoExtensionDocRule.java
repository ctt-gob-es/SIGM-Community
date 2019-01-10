package es.dipucr.sigem.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrInitDecretoExtensionDocRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrInitDecretoExtensionDocRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		OpenOfficeHelper ooHelper = null;
		try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
	        ooHelper = OpenOfficeHelper.getInstance();
	        //----------------------------------------------------------------------------------------------
	        
	        String taskId = cct.getSsVariable("taskId");
	        
	        //Obtiene el expediente de la entidad
	        String numexp_decr = rulectx.getNumExp();	
	        String strQuery = "WHERE NUMEXP_HIJO='"+numexp_decr+"'";
	        IItemCollection col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
	        Iterator it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
        	IItem relacion = (IItem)it.next();
        	String numexp_ent = relacion.getString("NUMEXP_PADRE");
        	col = entitiesAPI.getEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, numexp_ent);
	        it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
	        IItem convocatoria = (IItem)it.next();
        	
	        //Inicializa los datos del Decreto
			//IItem decreto = entitiesAPI.createEntity("SGD_DECRETO", numexp_decr);
	        strQuery = "WHERE NUMEXP='"+numexp_decr+"'";
	        col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SGD_DECRETO, strQuery);
	        it = col.iterator();
	        IItem decreto = null;
	        if (!it.hasNext())
	        {
	        	//return new Boolean(false);
	        	decreto = entitiesAPI.createEntity(Constants.TABLASBBDD.SGD_DECRETO, numexp_decr);
	        }
	        else decreto = (IItem)it.next();
        	
        	String titulo = "";
        	if (convocatoria.getString("TITULO")!=null) titulo = convocatoria.getString("TITULO"); else titulo = "";
        	if(titulo.equals("")){
        		 IItem expediente = ExpedientesUtil.getExpediente(cct, numexp_ent);
    	        if (expediente == null)
    	        {
    	        	return new Boolean(false);
    	        }
    	        titulo = expediente.getString("ASUNTO");
        	}
			if (decreto != null)
			{
				decreto.set("EXTRACTO_DECRETO", titulo);
				decreto.store(cct);
			}else{
				return new Boolean(false);
			}
			
			//Obtiene el número de fase del decreto
			String strQueryAux = "WHERE NUMEXP='" + numexp_decr + "'";
			logger.info("strQueryAux "+strQueryAux);
			IItemCollection collExpsAux = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, strQueryAux);
			Iterator itExpsAux = collExpsAux.iterator();
			if (! itExpsAux.hasNext()) {
				return new Boolean(false);
			}
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");
			logger.info("idFase "+idFase);
			int idFaseDecreto = iExpedienteAux.getInt("ID_FASE");
			
			strQueryAux = "WHERE ID_FASE = "+idFaseDecreto+" ORDER BY ORDEN ASC";
			IItemCollection iTramiteProp = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQueryAux);
			Iterator ITramiteProp = iTramiteProp.iterator();
			int idTramite=0;
			IItem tramite = (IItem)ITramiteProp.next();
			idTramite = tramite.getInt("ID");
			
			//Creo el tramite 'Creación del Decreto, traslado y notificaciones'
			int idTramitePropuesta = transaction.createTask(idFase, idTramite);
			logger.info("idTramitePropuesta "+idTramitePropuesta);
			
			logger.info("Creado el tramite");
			
			//Se busca el id del tramite
			String sQueryTramite = "WHERE ID_TRAM_EXP="+taskId+" AND FECHA_CIERRE IS NULL AND NUMEXP='"+numexp_ent+"' ORDER BY FECHA_INICIO";
			IItemCollection icTramite = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, sQueryTramite);
			Iterator<IItem> itTramite = icTramite.iterator();
			int tramiteExpPadre = 0;
			if(itTramite.hasNext()){
				IItem itemTramite = itTramite.next();
				tramiteExpPadre = itemTramite.getInt("ID_TRAM_EXP");
			}
			
			IItemCollection documentsCollection = entitiesAPI.getDocuments(numexp_ent, "ID_TRAMITE = "+tramiteExpPadre, "FDOC DESC");
			
			Iterator <IItem> itDocProp = documentsCollection.iterator();
			
			String descripcion = "";
			
			while(itDocProp.hasNext()){
				IItem documentacionPropuesta = itDocProp.next();
				
				IItem nuevoDocumentoDocPropuesta = (IItem)genDocAPI.createTaskDocument(idTramitePropuesta, documentacionPropuesta.getInt("ID_TPDOC"));
				logger.info("Creado el documento");
				
				String infopag = documentacionPropuesta.getString("INFOPAG");
				descripcion = documentacionPropuesta.getString("DESCRIPCION");
				String extension = documentacionPropuesta.getString("EXTENSION");			
				nuevoDocumentoDocPropuesta.set("INFOPAG", infopag);
				nuevoDocumentoDocPropuesta.set("NOMBRE", Constants.TIPODOC.DOCUMENTACION);
				nuevoDocumentoDocPropuesta.set("DESCRIPCION", descripcion);
				nuevoDocumentoDocPropuesta.set("EXTENSION", extension);
				nuevoDocumentoDocPropuesta.store(cct);
			}
			
			
	        
	        //Se crea un documento de decreto para luego adjuntarlo si se seleccionara 'Iniciar expediente de Decreto'
			
			//CABECERA DECRETO
			logger.info("DECRETO CABECERA INICIO "+Constants.PLANTILLADOC.DECRETO_CABECERA);
//        	DocumentosUtil.generarDocumento(rulectx, Constants.PLANTILLADOC.DECRETO_CABECERA, Constants.TIPODOC.DECRETOS_CONVOCATORIA, null, numexp_ent, idTramitePropuesta);
        	DocumentosUtil.generarDocumentoDoc(rulectx, Constants.PLANTILLADOC.DECRETO_CABECERA, Constants.TIPODOC.DECRETOS_CONVOCATORIA, null, numexp_ent, idTramitePropuesta);
        	String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, Constants.PLANTILLADOC.DECRETO_CABECERA);
        	File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
        	XComponent xComponent = ooHelper.loadDocument("file://" + file.getPath());
    		file.delete();
    		logger.info("FIN CABECERA DECRETO");
    		
    		//CABECERA NOTIFICACIONES
    		logger.info("NOTIFICACIONES CABECERA INICIO "+Constants.PLANTILLADOC.NOTIFICACIONES_CABECERA);
//        	DocumentosUtil.generarDocumento(rulectx, Constants.PLANTILLADOC.NOTIFICACIONES_CABECERA, Constants.TIPODOC.PLANTILLA_NOTIFICACIONES_TRAMITE_RESOLUCION, null, numexp_ent, idTramitePropuesta);
        	DocumentosUtil.generarDocumentoDoc(rulectx, Constants.PLANTILLADOC.NOTIFICACIONES_CABECERA, Constants.TIPODOC.PLANTILLA_NOTIFICACIONES_TRAMITE_RESOLUCION, null, numexp_ent, idTramitePropuesta);
        	String strInfoPagNotificaciones = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, Constants.PLANTILLADOC.NOTIFICACIONES_CABECERA);
        	File fileNotificaciones = DocumentosUtil.getFile(cct, strInfoPagNotificaciones, null, null);
        	XComponent xComponentNotificaciones = ooHelper.loadDocument("file://" + fileNotificaciones.getPath());
        	fileNotificaciones.delete();
    		logger.info("FIN CABECERA NOTIFICACIÓN");
    		  		
			
    		//Cuerpo
    		//Cojo el contenido de la propuesta
    		String query = "WHERE NUMEXP='"+numexp_ent+"' ORDER BY ID DESC";
			IItemCollection ids_prop_Collection = entitiesAPI.queryEntities("DPCR_ID_PROPUESTA", query);
    		//IItemCollection ids_prop_Collection = entitiesAPI.getEntities("DPCR_ID_PROPUESTA", numexp_ent, " ORDER BY ID DESC");
    		Iterator ids_prop_Iterator = ids_prop_Collection.iterator();
			int id_doc = 0;
			if(ids_prop_Iterator.hasNext()){
				IItem id_prop = (IItem) ids_prop_Iterator.next();
				id_doc = id_prop.getInt("ID_DOC");
				
	    		IItem iPropuesta = entitiesAPI.getDocument(id_doc);
	    		if(iPropuesta != null){
			        String infopag = iPropuesta.getString("INFOPAG");
			        
			        //Cuerpo de decreto
		        	file = DocumentosUtil.getFile(cct, infopag, null, null);
		        	DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), DipucrCommonFunctions._FORMAT_DOC);
		    		file.delete();
		    		//Cuerpo de notificaciones
		    		fileNotificaciones = DocumentosUtil.getFile(cct, infopag, null, null);
		    		DipucrCommonFunctions.ConcatenaByFormat(xComponentNotificaciones, "file://" + fileNotificaciones.getPath(), DipucrCommonFunctions._FORMAT_DOC);
		        	fileNotificaciones.delete();
		    		
		    		
		    		//Pie
			    	logger.info("DECRETO PIE INICIO "+Constants.PLANTILLADOC.DECRETO_PIE);
//			    	DocumentosUtil.generarDocumento(rulectx, Constants.PLANTILLADOC.DECRETO_PIE, Constants.TIPODOC.DECRETOS_CONVOCATORIA, null, numexp_ent, idTramitePropuesta);
			    	DocumentosUtil.generarDocumentoDoc(rulectx, Constants.PLANTILLADOC.DECRETO_PIE, Constants.TIPODOC.DECRETOS_CONVOCATORIA, null, numexp_ent, idTramitePropuesta);
			    	strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, Constants.PLANTILLADOC.DECRETO_PIE);
			    	logger.info(strInfoPag);
			    	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			    	DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), DipucrCommonFunctions._FORMAT_DOC);
					file.delete();
					logger.info("FIN PIE DECRETO");
					
					//Pie Notificaciones
			    	logger.info("DECRETO PIE INICIO "+Constants.PLANTILLADOC.NOTIFICACIONES_PIE);
//			    	DocumentosUtil.generarDocumento(rulectx, Constants.PLANTILLADOC.NOTIFICACIONES_PIE, Constants.TIPODOC.PLANTILLA_NOTIFICACIONES_TRAMITE_RESOLUCION, null, numexp_ent, idTramitePropuesta);
			    	DocumentosUtil.generarDocumentoDoc(rulectx, Constants.PLANTILLADOC.NOTIFICACIONES_PIE, Constants.TIPODOC.PLANTILLA_NOTIFICACIONES_TRAMITE_RESOLUCION, null, numexp_ent, idTramitePropuesta);
			    	strInfoPagNotificaciones = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, Constants.PLANTILLADOC.NOTIFICACIONES_PIE);
			    	logger.info(strInfoPagNotificaciones);
			    	fileNotificaciones = DocumentosUtil.getFile(cct, strInfoPagNotificaciones, null, null);
			    	DipucrCommonFunctions.ConcatenaByFormat(xComponentNotificaciones, "file://" + fileNotificaciones.getPath(), DipucrCommonFunctions._FORMAT_DOC);
			    	fileNotificaciones.delete();
					logger.info("FIN PIE DECRETO");
					
					//Guarda el resultado en repositorio temporal
					String fileName = FileTemporaryManager.getInstance().newFileName("."+Constants._EXTENSION_DOC);
					fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
					file = new File(fileName);
					
					//Guarda el resultado en repositorio temporal
					String fileNameNotificaciones = FileTemporaryManager.getInstance().newFileName("."+Constants._EXTENSION_DOC);
					fileNameNotificaciones = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameNotificaciones;
					fileNotificaciones = new File(fileNameNotificaciones);
					
					
					OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
					
					OpenOfficeHelper.saveDocument(xComponentNotificaciones,"file://" + fileNotificaciones.getPath(),"");
					
					//Guarda el resultado en gestor documental
					int tpdoc = DocumentosUtil.getTipoDoc(cct, Constants.DECRETOS._DOC_DECRETO, DocumentosUtil.BUSQUEDA_EXACTA, false);

					DocumentosUtil.generaYAnexaDocumento(rulectx, idTramitePropuesta, tpdoc, Constants.DECRETOS._DOC_DECRETO, file, Constants._EXTENSION_DOC);
					if(file != null && file.exists()) file.delete();
					
					//Guarda el resultado en gestor documental Notificaciones
					int tpdocNotif = DocumentosUtil.getTipoDoc(cct, Constants.DECRETOS._DOC_NOTIFICACIONES, DocumentosUtil.BUSQUEDA_EXACTA, false);
					DocumentosUtil.generaYAnexaDocumento(rulectx, idTramitePropuesta, tpdocNotif, Constants.DECRETOS._DOC_NOTIFICACIONES, fileNotificaciones, Constants._EXTENSION_DOC);
					
					//Borra los documentos intermedios del gestor documental
					IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE LIKE '%" + Constants.TIPODOC.DECRETOS_CONVOCATORIA + "%' OR NOMBRE='"+Constants.TIPODOC.PLANTILLA_NOTIFICACIONES_TRAMITE_RESOLUCION+"'", "FDOC DESC");
			        it = collection.iterator();
			        while (it.hasNext())
			        {
			        	IItem doc = (IItem)it.next();
			        	entitiesAPI.deleteDocument(doc);
			        }
			    	
			    	if(fileNotificaciones != null && fileNotificaciones.exists()) fileNotificaciones.delete();
	    		}
			}
			else{
				throw new ISPACRuleException("NO SE PUEDE RECUPERAR EL ID DEL DOCUMENTO. CONSULTE CON EL ADMINISTRADOR");
			}
			
			
			//Actualizo el asunto de la tabla spac_expedientes
			strQuery = "WHERE NUMEXP='"+numexp_decr+"'";
			IItem iPropuestaExp = ExpedientesUtil.getExpediente(cct, numexp_decr);
			if(iPropuestaExp != null){
				iPropuestaExp.set("ASUNTO", titulo);
				iPropuestaExp.store(cct);
				logger.info("CAMBIADO EL ASUNTO DE SPAC_EXPEDIENTES ");
			}
			
			//Actualiza el campo "estado" de la entidada para
			//que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
			convocatoria.set("ESTADO", "Decreto");
			convocatoria.store(cct);
			logger.info("Actualiza el campo ");
			
			/**
			 * [Teresa] INICIO Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
			 * **/
			//Importar participantes.
			ParticipantesUtil.importarParticipantes(cct, entitiesAPI, numexp_ent, numexp_decr);
			/**
			 * [Teresa] FIN Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
			 * **/
	        return new Boolean(true);
        } catch(Exception e) {
    		logger.error("Error generando los documentos de la resolución: "+e.getMessage());
        	if (e instanceof ISPACRuleException) {
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido inicializar el decreto.",e);
        } finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		 return true;
	}

}
