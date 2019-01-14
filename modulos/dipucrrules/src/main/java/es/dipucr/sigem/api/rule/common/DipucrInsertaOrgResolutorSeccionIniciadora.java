package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

/**
 * [dipucr-Felipe #545]
 * @author Felipe
 *
 */
public class DipucrInsertaOrgResolutorSeccionIniciadora implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
			String numexp = rulectx.getNumExp();
			
			IItem itemExpediente = entitiesAPI.getExpedient(numexp);
			int idPcd = itemExpediente.getInt("ID_PCD");
			
			IItem itemProcedure = procedureAPI.getProcedureById(idPcd);
			String orgRsltr = itemProcedure.getString("CTPROCEDIMIENTOS:ORG_RSLTR");
			
			IRespManagerAPI respAPI = rulectx.getClientContext().getAPI().getRespManagerAPI();
			IResponsible resp = respAPI.getResp(orgRsltr);
			String nombreOrg = resp.getName();
			
			itemExpediente.set("IDSECCIONINICIADORA", orgRsltr);
			itemExpediente.set("SECCIONINICIADORA", nombreOrg);
			itemExpediente.store(cct);
		}
		catch(Exception ex){
			throw new ISPACRuleException("Error al insertar el órgano resolutor "
					+ "como sección iniciadora: " + ex.getMessage(), ex);
		}

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
