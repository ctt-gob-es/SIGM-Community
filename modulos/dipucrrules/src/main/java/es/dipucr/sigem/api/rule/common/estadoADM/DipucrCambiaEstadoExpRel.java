package es.dipucr.sigem.api.rule.common.estadoADM;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrCambiaEstadoExpRel implements IRule{
	private static final Logger logger = Logger.getLogger(DipucrCambiaEstadoExpRel.class);
	
	protected String estadoIni;
	protected String estadofin;

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        logger.info("INICIO - "+this.getClass().getName());
	        String numexp = rulectx.getNumExp();
	        
	        //Recuperamos los expedientes relacionados
	        String strQuery = "WHERE NUMEXP_PADRE='" + numexp + "'";
	        IItemCollection expRelCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
	        Iterator expRelIt = expRelCol.iterator();	  	        
	        if(expRelIt.hasNext()){
	        	while (expRelIt.hasNext()){
	        		IItem expRel = (IItem)expRelIt.next();
	        		//Solo trabajamos con aquellos expedientes en estado RESOLUCION - RS
	        		String numexpHijo = expRel.getString("NUMEXP_HIJO");
	        		
	        		IItem expHijo = ExpedientesUtil.getExpediente(cct, numexpHijo); 
	        		if(expHijo != null){
	        			if(expHijo.get("ESTADOADM").equals(estadoIni)){
	        				expHijo.set("ESTADOADM", estadofin);
	        				expHijo.store(cct);
	        			}
	        		}	        		
	        	}
	        }    	       
        	logger.info("FIN - "+this.getClass().getName());
    		return true;
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}