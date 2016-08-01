package es.dipucr.sigem.api.rule.common.cartaDigital;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;

public class DipucrIniciaCartaDigital implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrIniciaCartaDigital.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {			
			logger.info("INICIO - " + this.getClass().getName());
			IClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			IItemCollection collection = entitiesAPI.getTaskDocuments(rulectx.getNumExp(), rulectx.getTaskId());
	        Iterator<IItem> it = collection.iterator();
	        if (it.hasNext()){
	        	//Compruebo que no este registrado para que se cree la carta digital
	        	//porque si esta registrado quiere decir que ya se ha notificado
	        	IItem doc = it.next();
	        	if(doc!=null && doc.getString("SPAC_DT_DOCUMENTOS:NREG")==null){
	        		generaCarta(rulectx, doc);
	        	}	        	
	        }			
			logger.info("FIN - " + this.getClass().getName());
		} catch (ISPACException e) { 
			logger.error("Error al generar la comunicación administrativa. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa. " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error al generar la comunicación administrativa. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa. " + e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private void generaCarta(IRuleContext rulectx, IItem doc) throws Exception {
		
		IClientContext cct = (ClientContext) rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		ITXTransaction tx = invesflowAPI.getTransactionAPI();
		IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
		
		String numExpHijo = "";
		try{
			numExpHijo = ExpedientesRelacionadosUtil.iniciaExpedienteHijoCartaDigital(cct, rulectx.getNumExp(), true, true);
		}
		catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw e;
		}
		
		if(StringUtils.isNotEmpty(numExpHijo)){
			IProcess itemProcess = invesflowAPI.getProcess(numExpHijo);
        	int idProcess = itemProcess.getInt("ID");
			IItemCollection collExpsAux = invesflowAPI.getStagesProcess(idProcess);
			Iterator itExpsAux = collExpsAux.iterator();

			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			int idFase = iExpedienteAux.getInt("ID");
			int idFaseDecreto = iExpedienteAux.getInt("ID_FASE");			
			IItemCollection iTramiteProp = procedureAPI.getStageTasks(idFaseDecreto); 
			Iterator ITramiteProp = iTramiteProp.iterator();
			int idTramite=0;

			IItem tramite = (IItem)ITramiteProp.next();
			idTramite = tramite.getInt("ID");
				
			int idTramiteExpCartaDigital = tx.createTask(idFase, idTramite);
			try{
				// Abrir transacción para que no se pueda generar un documento sin fichero
		        cct.beginTX();
		        String infopag = doc.getString("SPAC_DT_DOCUMENTOS:INFOPAG");
				String infopag_rde = doc.getString("SPAC_DT_DOCUMENTOS:INFOPAG_RDE");
				
				if(StringUtils.isNotEmpty(infopag_rde))	infopag = infopag_rde;

				String extension = doc.getString("SPAC_DT_DOCUMENTOS:EXTENSION");
				String extension_rde = doc.getString("SPAC_DT_DOCUMENTOS:EXTENSION_RDE");
				
				if(StringUtils.isNotEmpty(extension_rde)) extension = extension_rde;
				
				String descripcion = doc.getString("SPAC_DT_DOCUMENTOS:DESCRIPCION");
				String nombre = doc.getString("SPAC_DT_DOCUMENTOS:NOMBRE");
				
				File documento = DocumentosUtil.getFile(cct, infopag, null, null);
				
				int tpdoc = Integer.MIN_VALUE;
				
				if(nombre.toUpperCase().trim().contains("ANEXO")){
	        		tpdoc = DocumentosUtil.getTipoDoc(cct, "ANEXO", DocumentosUtil.BUSQUEDA_EXACTA, true);
	        	}
	        	else{
	        		tpdoc = DocumentosUtil.getTipoDoc(cct, "PLANTILLA CARTA DIGITAL", DocumentosUtil.BUSQUEDA_LIKE, true);
	        	}
		        
		        if(tpdoc > Integer.MIN_VALUE){
		    		DocumentosUtil.generaYAnexaDocumento(rulectx, idTramiteExpCartaDigital, tpdoc, descripcion, documento, extension);
				}
		        if (documento != null & documento.exists()) documento.delete();
			}
			catch (Throwable e){
				// Si se produce algún error se hace rollback de la transacción
				cct.endTX(false);
				
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

				extraInfo = extraInfo + ", " + e.getMessage();
		
				logger.error("Error al generar la comunicación administrativa. " + extraInfo, e);
				throw new ISPACRuleException("Error al generar la comunicación administrativa. " + extraInfo, e);
			}
			cct.endTX(true);			
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	
}