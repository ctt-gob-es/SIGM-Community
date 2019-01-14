package es.dipucr.portafirmas;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SpacExpRelacionadosInfoAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class EliminarSpacExpRelaInfoRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(EnviaDocPortaFirmasExternoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			SpacExpRelacionadosInfoAPI spacexprelaInfoObjeto = new SpacExpRelacionadosInfoAPI((ClientContext) rulectx.getClientContext());
			IItem spacExpRelaInfo = spacexprelaInfoObjeto.getInfoSpacExpRelacionaPadreIdTramitePadre(rulectx.getNumExp(), rulectx.getTaskId());
			if(null != spacExpRelaInfo){
				if(spacExpRelaInfo.getInt("ID")>0)	spacexprelaInfoObjeto.deleteSpacExpRelacionadosInfoByPadreHijo(spacExpRelaInfo.getInt("ID"));
			}
		} catch (ISPACException e) {
			LOGGER.error("Numexp. "+rulectx.getNumExp()+" Error - "+e.getMessage(),e);
			throw new ISPACRuleException("Numexp. "+rulectx.getNumExp()+" Error - "+e.getMessage(),e);
		}

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
