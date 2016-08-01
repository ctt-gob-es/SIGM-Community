package aww.sigem.expropiaciones.rule.tags;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * Inicializa valores de Finca al iniciar el expediente
 */
public class HoraActualTagRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(HoraActualTagRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
			String horaActual="";
			int hora, minutos;
			
			Calendar calendario = Calendar.getInstance();
			hora =calendario.get(Calendar.HOUR_OF_DAY);
			minutos = calendario.get(Calendar.MINUTE);

			horaActual = hora + "," + minutos;
			
			return horaActual;
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
