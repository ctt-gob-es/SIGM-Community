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
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil.TiposRegistros;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;

public class DipucrIniciaCartaDigital implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(DipucrIniciaCartaDigital.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {			
			LOGGER.info("INICIO - " + this.getClass().getName());
			
			IClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			ITXTransaction tx = invesflowAPI.getTransactionAPI();
			IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
			
			String numExpHijo = "";
        	int idTramiteExpCartaDigital = -1;

			IItemCollection collection = DocumentosUtil.getDocumentosByTramites(rulectx, rulectx.getNumExp(), rulectx.getTaskId());
			
	        Iterator<?> it = collection.iterator();
    			
    		while (it.hasNext()){
    			IItem doc = (IItem) it.next();
    			
            	//Compruebo que no este registrado para que se cree la carta digital porque si esta registrado quiere decir que ya se ha notificado
				String tipoDoc = doc.getString(DocumentosUtil.TP_REG);
				
				if( (TiposRegistros.SALIDA.equals(tipoDoc) || "ANEXO".equals(doc.getString(DocumentosUtil.NOMBRE).toUpperCase())) && StringUtils.isEmpty(doc.getString(DocumentosUtil.NREG))){
					
	    			if(StringUtils.isEmpty(numExpHijo)){
	    				numExpHijo = ExpedientesRelacionadosUtil.iniciaExpedienteHijoCartaDigital(cct, rulectx.getNumExp(), true, true);
	    				
		    			IProcess itemProcess = invesflowAPI.getProcess(numExpHijo);
		            	int idProcess = itemProcess.getInt("ID");
		    			IItemCollection collExpsAux = invesflowAPI.getStagesProcess(idProcess);
		    			Iterator<?> itExpsAux = collExpsAux.iterator();

		    			IItem iExpedienteAux = (IItem) itExpsAux.next();
		    			int idFase = iExpedienteAux.getInt("ID");
		    			int idFaseDecreto = iExpedienteAux.getInt("ID_FASE");			
		    			IItemCollection iTramiteProp = procedureAPI.getStageTasks(idFaseDecreto); 
		    			
		    			Iterator<?> ITramiteProp = iTramiteProp.iterator();
		    			int idTramite=0;

		    			IItem tramite = (IItem) ITramiteProp.next();
		    			idTramite = tramite.getInt("ID");
		    				
		    			idTramiteExpCartaDigital = tx.createTask(idFase, idTramite);
		    		}
			
	    			generaCarta(rulectx, doc, numExpHijo, idTramiteExpCartaDigital);
    			}
	        }			
			LOGGER.info("FIN - " + this.getClass().getName());
		} catch (ISPACRuleException e) { 
			LOGGER.error("Error al generar la comunicación administrativa. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
		} catch (ISPACException e) { 
			LOGGER.error("Error al generar la comunicación administrativa. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error al generar la comunicación administrativa. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
		}
		return null;
	}

	private void generaCarta(IRuleContext rulectx, IItem doc, String numExpHijo, int idTramiteExpCartaDigital) throws Exception {
		
		IClientContext cct = (ClientContext) rulectx.getClientContext();
		
		if(StringUtils.isNotEmpty(numExpHijo)){
			try{
				// Abrir transacción para que no se pueda generar un documento sin fichero
		        cct.beginTX();
		        String infopag = doc.getString(DocumentosUtil.INFOPAG);
				String infopag_rde = doc.getString(DocumentosUtil.INFOPAG_RDE);
				
				if(StringUtils.isNotEmpty(infopag_rde))	infopag = infopag_rde;

				String extension = doc.getString(DocumentosUtil.EXTENSION);
				String extension_rde = doc.getString(DocumentosUtil.EXTENSION_RDE);
				
				if(StringUtils.isNotEmpty(extension_rde)) extension = extension_rde;
				
				String descripcion = doc.getString(DocumentosUtil.DESCRIPCION);
				String nombre = doc.getString(DocumentosUtil.NOMBRE);
				
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
		        if (documento != null & documento.exists()){
		        	documento.delete();
		        }
			}
			catch (Exception e){
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
		
				LOGGER.error("Error al generar la comunicación administrativa. " + extraInfo, e);
				throw new ISPACRuleException("Error al generar la comunicación administrativa. " + extraInfo, e);
			}
			cct.endTX(true);			
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean terminarTramite = false;
		try {		
			IClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IItemCollection collection = entitiesAPI.getTaskDocuments(rulectx.getNumExp(), rulectx.getTaskId());
	        Iterator<IItem> it = collection.iterator();
	        if (!it.hasNext()){
	        	rulectx.setInfoMessage("Es necesario añadir un documento para genera el expediente de Comunicación Administrativa Electrónica");
	        }
	        else{
	        	terminarTramite = true;
	        }
		} catch (ISPACException e) { 
			LOGGER.error("Error al generar la comunicación administrativa. "+rulectx.getNumExp()+" - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa.  "+rulectx.getNumExp()+" - " + e.getMessage(), e);
		}
		return terminarTramite;
	}
	
	
}