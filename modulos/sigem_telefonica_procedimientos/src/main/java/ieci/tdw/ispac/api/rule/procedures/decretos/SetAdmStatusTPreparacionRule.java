package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * 
 * @author diezp
 * @date 27/11/2008
 * @propósito Modificar el campo Estado Administrativo con el valor "DECRETO FIRMADO"
 */
public class SetAdmStatusTPreparacionRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(SetAdmStatusDFRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			IItem item = ExpedientesUtil.getExpediente(rulectx.getClientContext(), rulectx.getNumExp());
			logger.warn("item.getString(ESTADOADM) "+item.getString("ESTADOADM"));
			if(!item.getString("ESTADOADM").equals("RC")){
				item.set("ESTADOADM", "DF");
				item.store(rulectx.getClientContext());
			}
		}catch(ISPACException e){
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
