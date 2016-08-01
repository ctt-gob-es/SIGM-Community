package aww.sigem.expropiaciones.rule.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import java.util.Iterator;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.util.ExpropiacionesUtil;

public class FechaAprobacionEEFTagRule implements IRule {

		/** Logger de la clase. */
		private static final Logger logger = 
			Logger.getLogger(FechaAprobacionEEFTagRule.class);
		
		public boolean init(IRuleContext rulectx) throws ISPACRuleException {
			return true;
		}

		public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
			return true;
		}
		
		public void cancel(IRuleContext rulectx) throws ISPACRuleException {
			
		}
		
		public Object execute(IRuleContext rulectx) throws ISPACRuleException {
			
			try {
				return ExpropiacionesUtil.campoTablaEEF("FECHA_APROBACION", rulectx);
			} catch (Exception e) {
				logger.error("Se produjo una excepción", e);
				throw new ISPACRuleException(e);
			}
	}

}
