package ieci.tdw.ispac.api.rule.procedures.sancionador;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

/**
 * 
 * @author teresa
 * @date 24/03/2010
 * @propósito Modificar el campo Transición de un expediente con el valor del código del trámite de inicio y fin a cerrado
 */
public class CloseStatusTaskSancionadorRule implements IRule {
	
	private static String STR_entidad = "URB_SANCIONADOR";
	private static String STR_iniciado = "_I";
	private static String STR_cerrado = "_C";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        IItem exp = null;
	        String numExp = rulectx.getNumExp();
	        String strQuery = "WHERE NUMEXP='" + numExp + "'";
	        IItemCollection collExps = entitiesAPI.queryEntities(STR_entidad, strQuery);
	        Iterator itExps = collExps.iterator();
	        String transicion = null;
	        
	        if (itExps.hasNext()){
	        	exp = (IItem)itExps.next();
	        	transicion = exp.getString("TRANSICION");
	        	if (ValidateStatusTaskSancionadorRule.isTramiteIniciado(transicion)){
	        		transicion = transicion.replace(STR_iniciado, STR_cerrado);
	        		exp.set("TRANSICION", transicion);
	        		exp.store(cct);
	        	}else{
	        		throw new ISPACInfo("Estado erróneo del expediente");
	        	}
	        }else{
	        	throw new ISPACInfo("No se ha inicializado el estado del expediente");
	        }
			
		}catch(ISPACException e){
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
