package es.dipucr.jaxb.tribunalcuentas.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

public class RellenarPersonaContactoRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(RellenarPersonaContactoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			
			IItem item = entitiesAPI.createEntity("CONTRATACION_RENDCUENT_CONTA", rulectx.getNumExp());
			item.set("NOMBRE", "Eduardo");
			item.set("APELLIDO1", "Serrano");
			item.set("APELLIDO2", "Granados");
			item.set("DIRECCION", "C/ Toledo, 17");
			item.set("PROVINCIA", "13 - Ciudad Real");
			item.set("MUNICIPIO", "13034 - Ciudad Real");
			item.set("CODIGOPOSTAL", "13003");
			item.set("TELEFONO", "926292575");
			item.set("EMAIL", "compras@dipucr.es");
			item.store(cct);
			
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
