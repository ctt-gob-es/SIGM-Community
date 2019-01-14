package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class NoCreaTramiteSiYaEstaAbiertoRule implements IRule {
	
	protected static final Logger LOGGER = Logger.getLogger(NoCreaTramiteSiYaEstaAbiertoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean crearTramite = true;
		try {
			IItemCollection tramiteCollection = TramitesUtil.getTramites(rulectx.getClientContext(), rulectx.getNumExp(), "ID_TRAM_EXP="+rulectx.getTaskId(), "");
			Iterator<IItem> iteraTramite = tramiteCollection.iterator();
			if(iteraTramite.hasNext()){
				IItem tramite = iteraTramite.next();
				String nombre = tramite.getString("NOMBRE");
				String query = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='"+nombre+"'";
				IItemCollection tramitesAbiertos = TramitesUtil.tramitesAbiertos(rulectx, query);
				Iterator<IItem> itTramitesAbiertos = tramitesAbiertos.iterator();
				if(itTramitesAbiertos.hasNext()){
					crearTramite = false;
					rulectx.setInfoMessage("Debe terminar el trámite '"+nombre+"' para poder iniciar uno nuevo.");
				}
			}
			
		} catch (ISPACException e) {
			throw new ISPACRuleException("Error al consultar el trámite del número expediente. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		
		return crearTramite;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

}
