package es.dipucr.sigem.api.rule.common;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class TramiteCreaExpedienteRelacionadoRule implements IRule{
	private static final Logger logger = Logger.getLogger(TramiteCreaExpedienteRelacionadoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			IItemCollection tramiteCollection = TramitesUtil.getTramites(rulectx.getClientContext(), rulectx.getNumExp(), "ID_TRAM_EXP="+rulectx.getTaskId(), "");
			Iterator<IItem> iteraTramite = tramiteCollection.iterator();
			if(iteraTramite.hasNext()){
				IItem tramite = iteraTramite.next();
				tramite.set("OBSERVACIONES", "Trámite utilizado para la creación de un expediente relacionado");
				tramite.store(rulectx.getClientContext());
			}
			
		} catch (ISPACException e) {
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
