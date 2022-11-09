package es.dipucr.sigem.api.rule.common.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrIniciaSolicitudAnuncioInternoBOP implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(DipucrIniciaSolicitudAnuncioInternoBOP.class);
	
	//Se puede heredar de esta clase, y al darle valor a �sta variable con el id del circuito de firma deseado, adem�s de generar el anuncio 
	//comienza el circuito de firma 
	protected int idCircuitoFirma = -1;

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		//No hace nada
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {			
			LOGGER.info("INICIO - " +this.getClass().getName());
			IClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TRAMITE ="+rulectx.getTaskId(), "");
	        Iterator<?> it = collection.iterator();
	        while (it.hasNext()){
	        	IItem doc = (IItem)it.next();
	        	String descripcionDoc = doc.getString("DESCRIPCION");
	        	generaAnuncio(rulectx, descripcionDoc);
	        }			
			LOGGER.info("FIN - " + this.getClass().getName());
		} catch (ISPACRuleException e) { 
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	private void generaAnuncio(IRuleContext rulectx, String descripcionDoc) throws Exception {
		
		IClientContext cct = (ClientContext) rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		ITXTransaction tx = invesflowAPI.getTransactionAPI();
		IGenDocAPI genDocAPI = invesflowAPI.getGenDocAPI();
		OpenOfficeHelper ooHelper = OpenOfficeHelper.getInstance();
		ISignAPI signAPI = invesflowAPI.getSignAPI();
		
		// Obtener el c�digo de procedimiento para el n�mero de expediente
		Map<String, String> params = new HashMap<String, String>();
		params.put("COD_PCD", "PCD-121");

		// Crear el proceso del expediente
		int nIdProcess2 = tx.createProcess(121, params);
		IProcess process;
		
		process = invesflowAPI.getProcess(nIdProcess2);
		
		String numExpHijo = process.getString("NUMEXP");
		
		ExpedientesRelacionadosUtil.relacionaExpedientes(cct, rulectx.getNumExp(), numExpHijo, "Anuncio BOP");
		
		TramitesUtil.setNumexpRelacionado(cct, rulectx.getNumExp(), rulectx.getTaskId(), numExpHijo);
		
		TramitesUtil.cargarObservacionesTramite(cct, false, rulectx.getNumExp(), rulectx.getTaskId(), "Anuncio BOP - Exp.Relacionado: " + numExpHijo);

		//Creamos el tr�mite
		String strQueryAux = "WHERE NUMEXP = '" + numExpHijo + "'";
		IItemCollection collExpsAux = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, strQueryAux);
		Iterator<?> itExpsAux = collExpsAux.iterator();

		IItem iExpedienteAux = ((IItem)itExpsAux.next());
		int idFase = iExpedienteAux.getInt("ID");
		int idFaseDecreto = iExpedienteAux.getInt("ID_FASE");
		strQueryAux = "WHERE ID_FASE = "+idFaseDecreto+" ORDER BY ORDEN ASC";
		IItemCollection iTramiteProp = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQueryAux);
		Iterator<?> ITramiteProp = iTramiteProp.iterator();
		int idTramite=0;
		IItem tramite = (IItem)ITramiteProp.next();
		idTramite = tramite.getInt("ID");
		
		//Creo el tramite 'Creaci�n del Decreto, traslado y notificaciones'			
		int idTramitePropuesta = tx.createTask(idFase, idTramite);
		
		Object connectorSession = null;
		try{
			connectorSession = genDocAPI.createConnectorSession();
			// Abrir transacci�n para que no se pueda generar un documento sin fichero
	        cct.beginTX();				
			
	        int tpdoc = DocumentosUtil.getTipoDoc(cct, Constants.BOP._DOC_ANUNCIO, DocumentosUtil.BUSQUEDA_EXACTA, false);
	        
        	IItem entityDocument = genDocAPI.createTaskDocument(idTramitePropuesta, tpdoc);
			int documentId = entityDocument.getKeyInt();

			// Generar el documento a partir de la plantilla
			IItem entityTemplate = genDocAPI.attachTaskTemplate(connectorSession, idTramitePropuesta, documentId, 97);
			entityTemplate.set("EXTENSION", "odt");
			String templateDescripcion = entityTemplate.getString("DESCRIPCION");

			entityTemplate.set("DESCRIPCION", templateDescripcion);
			entityTemplate.store(cct);
			entityTemplate.store(cct);
		}
		catch (Exception e){
			// Si se produce alg�n error se hace rollback de la transacci�n
			cct.endTX(false);
			
			String message = "exception.documents.generate";
			String extraInfo = null;
			Throwable eCause = e.getCause();
			
			if (eCause instanceof ISPACException){
				if (eCause.getCause() instanceof NoConnectException){
					extraInfo = "exception.extrainfo.documents.openoffice.off"; 
				}
				else{
					extraInfo = eCause.getCause().getMessage();
				}
			}
			else if (eCause instanceof DisposedException){
				extraInfo = "exception.extrainfo.documents.openoffice.stop";
			}
			else{
				extraInfo = e.getMessage();
			}			
			throw new ISPACInfo(message, extraInfo);
		}
		finally{
			if (connectorSession != null){
				genDocAPI.closeConnectorSession(connectorSession);
			}
		}
		cct.endTX(true);
		
		String strInfoPag = "";
    	
        IItemCollection collection = DocumentosUtil.getDocumentsByDescripcion(numExpHijo, cct, Constants.BOP._DOC_ANUNCIO);
        Iterator<?> it = collection.iterator();
        if (it.hasNext()) {
        	IItem doc = (IItem)it.next();
        	strInfoPag = doc.getString("INFOPAG");
        }
		File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
    	XComponent xComponent = ooHelper.loadDocument("file://" + file.getPath());
		file.delete();   		  		
		
		//Documento original
		IItemCollection colProp = entitiesAPI.getDocuments(rulectx.getNumExp(), "DESCRIPCION LIKE '" + descripcionDoc + "' AND ID_TRAMITE = " + rulectx.getTaskId(), "");
		Iterator<?> itProp = colProp.iterator();
		IItem iPropuesta = (IItem)itProp.next();
        String infopag = iPropuesta.getString("INFOPAG");
        
        //Cuerpo de decreto
    	file = DocumentosUtil.getFile(cct, infopag, null, null);
    	DipucrCommonFunctions.concatena(xComponent, "file://" + file.getPath());
		file.delete();
		    		
		//Guarda el resultado en repositorio temporal
		String fileName = FileTemporaryManager.getInstance().newFileName("."+Constants._EXTENSION_ODT);
		fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
		file = new File(fileName);
		
		OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
		connectorSession = genDocAPI.createConnectorSession();
		
		//Guarda el resultado en gestor documental
        int tpdoc = DocumentosUtil.getTipoDoc(cct, Constants.BOP._DOC_ANUNCIO, DocumentosUtil.BUSQUEDA_EXACTA, false);

		IItem newdoc = DocumentosUtil.generaYAnexaDocumento(cct, idTramitePropuesta, tpdoc, descripcionDoc, file, Constants._EXTENSION_ODT);
		int docId = newdoc.getInt("ID");
		
		if(idCircuitoFirma > -1){
			signAPI.initCircuitPortafirmas(idCircuitoFirma, docId);//[dipucr-Felipe #1246]
		}
		
		//Borra los documentos intermedios del gestor documental
        collection = entitiesAPI.getDocuments(numExpHijo, "DESCRIPCION != '" + descripcionDoc + "'","");
        it = collection.iterator();
        while (it.hasNext())
        {
        	IItem doc = (IItem)it.next();
        	entitiesAPI.deleteDocument(doc);
        }
        infopag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, Constants.BOP._DOC_ANUNCIO);
		file = DocumentosUtil.getFile(cct, infopag, null, null);
        DipucrCommonFunctions.concatena(xComponent, "file://" + file.getPath());
    	file.delete();
    	DocumentosUtil.deleteFile(fileName);
    	
		if(null != ooHelper){
        	ooHelper.dispose();
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return Boolean.TRUE;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return Boolean.TRUE;
	}
}