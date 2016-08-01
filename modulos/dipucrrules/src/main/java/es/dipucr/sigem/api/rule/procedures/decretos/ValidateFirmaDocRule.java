package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * 
 * @author diezp
 * @date 27/11/2008
 * @propósito Valida que el documento esté firmado. Para ello, comprueba que el campo Estado Firma sea igual a "Firmado"
 */
public class ValidateFirmaDocRule implements IRule{

	private static final Logger logger = Logger.getLogger(ValidateFirmaDocRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{	
			try{
				//Comprobar si el campo motivo rechazo tiene asignado un valor
				
				//----------------------------------------------------------------------------------------------
		        ClientContext cct = (ClientContext) rulectx.getClientContext();
		        IInvesflowAPI invesFlowAPI = cct.getAPI();
		        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		        //----------------------------------------------------------------------------------------------
		
		        IItem exp = null;
		        String motivoRechazo = null;
		        String numExp = rulectx.getNumExp();
		        String strQuery = "WHERE NUMEXP='" + numExp + "'";
		        IItemCollection collExps = entitiesAPI.queryEntities("SGD_RECHAZO_DECRETO", strQuery);
		        Iterator<?> itExps = collExps.iterator();
		        if (itExps.hasNext()) 
		        {
		        	exp = (IItem)itExps.next();
		        	motivoRechazo = exp.getString("RECHAZO_DECRETO");
		        	
		        	if (motivoRechazo!=null && !motivoRechazo.equals("")){
		        		return true;
		        	}
		        }
			}catch (Exception e){
				try {
					throw new ISPACInfo("Se ha producido un error al ejecutar la regla de trasladar decreto.");
				} catch (ISPACInfo e1) {
		        	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
				}
			}
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IItem item = ExpedientesUtil.getExpediente(rulectx.getClientContext(), rulectx.getNumExp());
	        
	        if(item.getString("ESTADOADM") != null && item.getString("ESTADOADM").equals("RC")){
	        	return true;
	        }else{
	        	// Comprobar que se haya anexado un documento
				IItemCollection docsCollection = entitiesAPI.getTaskDocuments(rulectx.getNumExp(), rulectx.getTaskId());
				
				if (docsCollection==null || docsCollection.toList().size()==0){
					rulectx.setInfoMessage("No se puede cerrar el trámite del expediente "+rulectx.getNumExp()+" ya que no se ha adjuntado un documento");
					return false;
				}				
				// Comprobar que se haya firmado el documento
				int taskId = rulectx.getTaskId();
				String sqlQuery = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA in ('02','03','04')";
				IItemCollection itemCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery, "");
				
				if (!itemCollection.next()){
					rulectx.setInfoMessage("No se puede cerrar el trámite del expediente "+rulectx.getNumExp()+" ya que tiene documentos sin firmar");
					//throw new ISPACRuleException("No se puede cerrar el trámite del expediente "+rulectx.getNumExp()+" ya que tiene documentos sin firmar");
					return false;
				}else{
					return true;
				}
	        }
		} catch (Exception e) {
        	logger.error("Error al comprobar el estado de la firma de los documentos en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
	        throw new ISPACRuleException("Error al comprobar el estado de la firma de los documentos en el expediente: "+ rulectx.getNumExp() + ". " + e.getMessage(), e);
	    } 
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
