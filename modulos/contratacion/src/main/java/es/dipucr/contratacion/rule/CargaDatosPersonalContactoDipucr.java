package es.dipucr.contratacion.rule;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class CargaDatosPersonalContactoDipucr implements IRule{
	
	private static final Logger logger = Logger.getLogger(CargaDatosPersonalContactoDipucr.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			/*************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			 /***********************************************************/
			
			IItem itemPersCont = entitiesAPI.createEntity("CONTRATACION_PERS_CONTACTO","");
			
			itemPersCont.set("NUMEXP", rulectx.getNumExp());
			itemPersCont.set("NOMBRE", "Eduardo Serrano Granados");
			itemPersCont.set("EMAIL", "compras@dipucr.es"); 
			itemPersCont.set("CALLE", "C/ Toledo, 17"); 
			itemPersCont.set("CP", 13071); 
			itemPersCont.set("LOCALIDAD", "Ciudad Real"); 
			itemPersCont.set("PROVINCIA", "Ciudad Real");
			itemPersCont.set("LOCALIZACIONGEOGRAFICA", "ES422 - Ciudad Real");
			itemPersCont.set("MOVIL", "926292575");
			
			itemPersCont.set("NOMBRESECRE", "M Angeles Carrero Martinez");
			itemPersCont.set("EMAILSECRE", "angeles_carrero@dipucr.es"); 
			itemPersCont.set("CALLESECRE", "C/ Toledo, 17"); 
			itemPersCont.set("CPSECRE", 13071); 
			itemPersCont.set("LOCALIDADSECRE", "Ciudad Real"); 
			itemPersCont.set("PROVINCIASECRE", "Ciudad Real");
			itemPersCont.set("LOCALIZACIONGEOGRAFICASECRE", "ES422 - Ciudad Real");
			itemPersCont.set("MOVILSECRE", "926295660");
			itemPersCont.store(cct);
	        
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error a la hora de insertar el personal de contacto. ",e);
		}
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
