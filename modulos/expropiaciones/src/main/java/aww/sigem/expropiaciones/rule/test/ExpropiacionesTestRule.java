package aww.sigem.expropiaciones.rule.test;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

/**
 * Inicializa valores de Finca al iniciar el expediente
 */
public class ExpropiacionesTestRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ExpropiacionesTestRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			rulectx.setInfoMessage("Esta es una prueba de ejecución de la regla ExpropiacionesTestRule ");
			
			logger.error("Esta es una prueba de log ERROR de la regla ExpropiacionesTestRule");
			logger.warn("Esta es una prueba de log WARN de la regla ExpropiacionesTestRule");
			//El nivel de error está en WARN. El INFO no se ve
			logger.info("Esta es una prueba de log INFO de la regla ExpropiacionesTestRule");
			
			//Las excepciones se muestran por pantalla
			throw new Exception("Esta es una prueba de ejecución de la regla ExpropiacionesTestRule ");
		} catch (Exception e) {
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
