package es.dipucr.sigem.api.rule.procedures.comisionServicio;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * [eCenpri-Felipe #693]
 * Regla que crea el trámite de "Firmar Comisión de Servicio"
 * @author Felipe
 * @since 27.03.2018
 */
public class CrearTramiteComisionServicioRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(CrearTramiteComisionServicioRule.class);
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación de la fase y el trámite
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{			
			//Creamos el trámite
			TramitesUtil.crearTramiteDesdeRegistro(Constants.COMISIONSERV.CODTRAM_FIRMA, rulectx);
		}
		catch (Exception e) {
			logger.error("Error en la generación de los trámites y envío a firma de los anticipos del expediente: " + rulectx.getNumExp() + ". " +e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación de los trámites y envío a firma de los anticipos del expediente: " + rulectx.getNumExp() + ". " +e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
