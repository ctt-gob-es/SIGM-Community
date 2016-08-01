package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

/**
 * Inicializa valores de Finca al iniciar el expediente
 */
public class ExpropiacionesTestTagRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ExpropiacionesTestTagRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
			//logger.warn("Ejecutando la regla ExpropiacionesTestTagRule");
			
			return "TestTagRule ejecutado correctamente";
			
		} catch (Exception e) {
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
