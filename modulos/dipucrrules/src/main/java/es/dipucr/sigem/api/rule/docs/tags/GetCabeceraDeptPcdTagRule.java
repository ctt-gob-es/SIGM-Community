package es.dipucr.sigem.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

/**
 * Devuelve la cabecera a cargar debajo del escudo en las plantillas
 * del expediente para un determinado procedimiento.
 * Los valores se cargan de la tabla DPCR_CABECERA_DEPT_PCD
 * COD_PCD - Etiqueta de la cabecera
 * @author Felipe
 * @since [dipucr-Felipe # 1136] 30.09.14 
 */
public class GetCabeceraDeptPcdTagRule implements IRule {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
        try{
        	String numexp = rulectx.getNumExp();
        	IClientContext cct = rulectx.getClientContext();
        	IInvesflowAPI invesflowAPI = cct.getAPI();
        	IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
        	IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
        	
        	IItem itemExpediente = entitiesAPI.getExpedient(numexp);
        	int idPcd = itemExpediente.getInt("ID_PCD");
        	
        	IItem itemPcd = procedureAPI.getProcedureById(idPcd);
        	String codPcd = itemPcd.getString("CTPROCEDIMIENTOS:COD_PCD");
        	
        	String strQuery = "WHERE VALOR = '" + codPcd + "'";
			IItemCollection collection = entitiesAPI.
					queryEntities("DPCR_CABECERA_DEPT_PROC", strQuery);
			
			String result = null;
			if (collection.toList().size() > 0){
				IItem itemCabecera = (IItem) collection.iterator().next();
				result = itemCabecera.getString("SUSTITUTO");
			}
			else{
				result = "[ERROR]";
			}
			
	        return result;
	        
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error obteniendo el departamento "
	        		+ "cabecera para el procedimiento.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
