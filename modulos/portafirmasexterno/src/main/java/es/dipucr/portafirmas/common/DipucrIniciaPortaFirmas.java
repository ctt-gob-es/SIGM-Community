package es.dipucr.portafirmas.common;

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

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;

public class DipucrIniciaPortaFirmas implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrIniciaPortaFirmas.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {			
			logger.info("INICIO - " + this.getClass().getName());
	        generaPortaFirma(rulectx);			
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
	private void generaPortaFirma(IRuleContext rulectx) throws Exception {
		
		IClientContext cct = (ClientContext) rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = cct.getAPI();
		ITXTransaction tx = invesflowAPI.getTransactionAPI();
		IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
		
		String numExpHijo = "";
		try{
			numExpHijo = ExpedientesRelacionadosUtil.iniciaExpedienteHijoFirmaDocExternos(cct, rulectx.getNumExp(), false, true);
		}
		catch(ISPACRuleException e){
			logger.error("Error al iniciar el expediente de Firma de documentos externos. NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar el expediente de Firma de documentos externos. NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
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
				
			tx.createTask(idFase, idTramite);	
				
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try {
			IItemCollection docTramites = DocumentosUtil.getDocumentosByTramites(rulectx, rulectx.getNumExp(), rulectx.getTaskId());
			Iterator<IItem> itDocTr = docTramites.iterator();
			if(!itDocTr.hasNext()){
				rulectx.setInfoMessage("Falta anexar el documento a firmar");
				return false;
			}
			else {
				IItem doc = itDocTr.next();
				if(doc!=null){
					String infopagRDE = doc.getString("INFOPAG_RDE");
				}
				return true;
			}
			
		} catch (ISPACException e) {
			logger.error("Error al obtener el documento de convenio con el NUMEXP. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el documento de convenio con el NUMEXP. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
	}
	
	
}