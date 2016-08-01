package aww.sigem.expropiaciones.rule.test;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

/**
 * Inicializa valores de Finca al iniciar el expediente
 */
public class ExpropiacionesValidationTestRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ExpropiacionesValidationTestRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try {
						
			
			logger.error("Esta es una prueba de log ERROR de la regla ExpropiacionesValidationTestRule");
			logger.warn("Esta es una prueba de log WARN de la regla ExpropiacionesValidationTestRule");
			//El nivel de error está en WARN. El INFO no se ve
			logger.info("Esta es una prueba de log INFO de la regla ExpropiacionesValidationTestRule");
			
			
			rulectx.setInfoMessage("Esta es una prueba de ejecución de la regla ExpropiacionesValidationTestRule ");
			return false;
			
		} catch (Exception e) {
			throw new ISPACRuleException(e);
		}
		
		
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		return null;
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
