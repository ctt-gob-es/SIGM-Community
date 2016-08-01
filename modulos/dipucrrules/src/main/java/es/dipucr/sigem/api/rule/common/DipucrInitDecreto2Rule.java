package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACInfo;
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

public class DipucrInitDecreto2Rule implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrInitDecreto2Rule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try
    	{
			logger.info("INICIO - " + this.getClass().getName());
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
	        OpenOfficeHelper ooHelper = OpenOfficeHelper.getInstance();
	        //----------------------------------------------------------------------------------------------
	        
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
        	col = entitiesAPI.getEntities("DPCR_RESOLUCION2", numexp_ent);
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
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
        	IItem decreto = (IItem)it.next();
        	
        	String titulo = "";
        	if (convocatoria.getString("TITULO")!=null) titulo = convocatoria.getString("TITULO"); else titulo = "";
        	titulo = titulo.toLowerCase();
        	if(titulo.equals("")){
        		IItem expediente = ExpedientesUtil.getExpediente(cct, numexp_ent);
    	        if (expediente == null ){
    	        	return new Boolean(false);
    	        }    	        
    	        titulo = expediente.getString("ASUNTO");
        	}
			if (decreto != null){
				decreto.set("EXTRACTO_DECRETO", titulo);
				decreto.store(cct);
			}else{
				return new Boolean(false);
			}
			
			//Obtiene el número de fase del decreto
			String strQueryAux = "WHERE NUMEXP='" + numexp_decr + "'";
			IItemCollection collExpsAux = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, strQueryAux);
			Iterator itExpsAux = collExpsAux.iterator();
			if (! itExpsAux.hasNext()) {
				return new Boolean(false);
			}
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");
			int idFaseDecreto = iExpedienteAux.getInt("ID_FASE");
			
			strQueryAux = "WHERE ID_FASE = "+idFaseDecreto+" ORDER BY ORDEN ASC";
			IItemCollection iTramiteProp = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQueryAux);
			Iterator ITramiteProp = iTramiteProp.iterator();
			int idTramite=0;
			IItem tramite = (IItem)ITramiteProp.next();
			idTramite = tramite.getInt("ID");
			
			//Creo el tramite 'Creación del Decreto, traslado y notificaciones'
			int idTramitePropuesta = transaction.createTask(idFase, idTramite);
			
			//Obtengo el zip con los documentos firmados
			
			// Obtener el documento zip "Contenido de la propuesta" del expediente de la entidad
			IItemCollection documentsCollection = entitiesAPI.getDocuments(numexp_ent, "NOMBRE = '"+Constants.TIPODOC.DOCUMENTACION_PROPUESTA+"'", "FDOC DESC");
			IItem contenidoPropuesta = null;
			if (documentsCollection!=null && documentsCollection.next()){
				contenidoPropuesta = (IItem)documentsCollection.iterator().next();
			}else{
				throw new ISPACInfo("No se ha encontrado el documento de Contenido de la propuesta");
			}
			
			//Este documento es el zip con todos los datos de la subvencion
			IItem nuevoDocumento = (IItem)genDocAPI.createTaskDocument(idTramitePropuesta, contenidoPropuesta.getInt("ID_TPDOC"));
			
			String infopag = contenidoPropuesta.getString("INFOPAG");
			String descripcion = contenidoPropuesta.getString("DESCRIPCION");
			String extension = contenidoPropuesta.getString("EXTENSION");			
			nuevoDocumento.set("INFOPAG", infopag);
			nuevoDocumento.set("NOMBRE", Constants.TIPODOC.DOCUMENTACION);
			nuevoDocumento.set("DESCRIPCION", descripcion);
			nuevoDocumento.set("EXTENSION", extension);
			nuevoDocumento.store(cct);
			
	      //Se crea un documento de decreto para luego adjuntarlo si se seleccionara 'Iniciar expediente de Decreto'
			
			//CABECERA DECRETO
        	DocumentosUtil.generarDocumento(rulectx, Constants.PLANTILLADOC.DECRETO_CABECERA, Constants.TIPODOC.DECRETOS_CONVOCATORIA, null, numexp_ent, idTramitePropuesta);
        	String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, Constants.PLANTILLADOC.DECRETO_CABECERA);
        	File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
        	XComponent xComponent = ooHelper.loadDocument("file://" + file.getPath());
    		file.delete();
//    		FIN CABECERA DECRETO
    		
    		//CABECERA NOTIFICACIONES
        	DocumentosUtil.generarDocumento(rulectx, Constants.PLANTILLADOC.NOTIFICACIONES_CABECERA, Constants.TIPODOC.PLANTILLA_NOTIFICACIONES_TRAMITE_RESOLUCION, null, numexp_ent, idTramitePropuesta);
        	String strInfoPagNotificaciones = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, Constants.PLANTILLADOC.NOTIFICACIONES_CABECERA);
        	File fileNotificaciones = DocumentosUtil.getFile(cct, strInfoPagNotificaciones, null, null);
        	XComponent xComponentNotificaciones = ooHelper.loadDocument("file://" + fileNotificaciones.getPath());
        	fileNotificaciones.delete();
//    		FIN CABECERA NOTIFICACIÓN
			
    		//Cuerpo
    		//Cojo el contenido de la propuesta
    		int taskIdExpedienteInicial = Integer.parseInt(cct.getSsVariable("taskId"));
    		IItemCollection colProp = entitiesAPI.getDocuments(numexp_ent, "UPPER(DESCRIPCION) LIKE '%"+Constants.CONSTANTES.PROPUESTA +"%2%' AND NUMEXP='"+numexp_ent+"' AND ID_TRAMITE != "+taskIdExpedienteInicial,  "FDOC DESC");
    		Iterator itProp = colProp.iterator();
    		IItem iPropuesta = (IItem)itProp.next();
	        infopag = iPropuesta.getString("INFOPAG");
	        
	        //Cuerpo de decreto
        	file = DocumentosUtil.getFile(cct, infopag, null, null);
        	DipucrCommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
    		file.delete();
    		//Cuerpo de notificaciones
    		fileNotificaciones = DocumentosUtil.getFile(cct, infopag, null, null);
        	DipucrCommonFunctions.Concatena(xComponentNotificaciones, "file://" + fileNotificaciones.getPath(), ooHelper);
        	fileNotificaciones.delete();
    		
    		
    		//Pie
	    	DocumentosUtil.generarDocumento(rulectx, Constants.PLANTILLADOC.DECRETO_PIE, Constants.TIPODOC.DECRETOS_CONVOCATORIA, null, numexp_ent, idTramitePropuesta);
	    	strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, Constants.PLANTILLADOC.DECRETO_PIE);
	    	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
	    	DipucrCommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
			file.delete();
//			FIN PIE 
			
			//Pie Notificaciones
	    	DocumentosUtil.generarDocumento(rulectx, Constants.PLANTILLADOC.NOTIFICACIONES_PIE, Constants.TIPODOC.PLANTILLA_NOTIFICACIONES_TRAMITE_RESOLUCION, null, numexp_ent, idTramitePropuesta);
	    	strInfoPagNotificaciones = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, Constants.PLANTILLADOC.NOTIFICACIONES_PIE);
	    	fileNotificaciones = DocumentosUtil.getFile(cct, strInfoPagNotificaciones, null, null);
	    	DipucrCommonFunctions.Concatena(xComponentNotificaciones, "file://" + fileNotificaciones.getPath(), ooHelper);
	    	fileNotificaciones.delete();
//			FIN PIE 
			
			//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName("."+Constants._EXTENSION_ODT);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			
			//Guarda el resultado en repositorio temporal
			String fileNameNotificaciones = FileTemporaryManager.getInstance().newFileName("."+Constants._EXTENSION_ODT);
			fileNameNotificaciones = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameNotificaciones;
			fileNotificaciones = new File(fileNameNotificaciones);
			
			
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
			
			OpenOfficeHelper.saveDocument(xComponentNotificaciones,"file://" + fileNotificaciones.getPath(),"");
			
			//Guarda el resultado en gestor documental
			int tpdoc = DocumentosUtil.getTipoDoc(cct, Constants.DECRETOS._DOC_DECRETO, DocumentosUtil.BUSQUEDA_EXACTA, false);
			DocumentosUtil.generaYAnexaDocumento(rulectx, idTramitePropuesta, tpdoc, Constants.DECRETOS._DOC_DECRETO, file, Constants._EXTENSION_ODT);
			
			//Guarda el resultado en gestor documental Notificaciones
			int tpdocNotif = DocumentosUtil.getTipoDoc(cct, Constants.DECRETOS._DOC_NOTIFICACIONES, DocumentosUtil.BUSQUEDA_EXACTA, false);
			DocumentosUtil.generaYAnexaDocumento(rulectx, idTramitePropuesta, tpdocNotif, Constants.DECRETOS._DOC_NOTIFICACIONES, fileNotificaciones, Constants._EXTENSION_ODT);
			
			//Borra los documentos intermedios del gestor documental
	        IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "(NOMBRE LIKE '%" + Constants.TIPODOC.DECRETOS_CONVOCATORIA + "%' OR NOMBRE='"+Constants.TIPODOC.PLANTILLA_NOTIFICACIONES_TRAMITE_RESOLUCION+"')", "");
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
	        infopag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, descripcion);
    		file = DocumentosUtil.getFile(cct, infopag, null, null);
	        DipucrCommonFunctions.Concatena(xComponent, "file://" + file.getPath(), ooHelper);
	    	file.delete();
			
			IItem iPropuestaExp = ExpedientesUtil.getExpediente(cct, numexp_decr);
			if (iPropuesta != null){
				iPropuestaExp.set("ASUNTO", titulo);
				iPropuestaExp.store(cct);
			}
			
			//Actualiza el campo "estado" de la entidada para
			//que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
			convocatoria.set("ESTADO", "Decreto");
			convocatoria.store(cct);			
			
			/**
			 * [Teresa] INICIO Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
			 * **/
			//Importar participantes.
			ParticipantesUtil.importarParticipantes(cct, entitiesAPI, numexp_ent, numexp_decr);
			/**
			 * [Teresa] FIN Ticket #237#SIGEM Subvenciones importar participantes a proced. de propuesta y decreto
			 * **/
			ooHelper.dispose();
			logger.info("FIN - " + this.getClass().getName());
	        return new Boolean(true);
        }
    	catch(Exception e) 
        {
    		logger.error(e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido inicializar el decreto. " + e.getMessage(), e);
        }
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		 return true;
	}

}
