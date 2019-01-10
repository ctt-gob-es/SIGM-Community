package es.dipucr.sigem.api.rule.procedures.expropiaciones;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class CierreExpedienteyRelacionadosRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(CierreExpedienteyRelacionadosRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			/****************************************************************************/
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			/****************************************************************************/
			
			IItemCollection exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"+ rulectx.getNumExp() + "'");
			Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
			Vector<String> expCerrar = new Vector<String>();
			while(exp_relacionadosIterator.hasNext()){
				IItem expRela = exp_relacionadosIterator.next();
				expCerrar.add(expRela.getString("NUMEXP_HIJO"));
			}
			ExpedientesUtil.cerrarExpedientes(rulectx.getClientContext(), expCerrar);
			
		} catch (ISPACException e) {
			LOGGER.error("ERROR. " + e.getMessage(), e);
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
