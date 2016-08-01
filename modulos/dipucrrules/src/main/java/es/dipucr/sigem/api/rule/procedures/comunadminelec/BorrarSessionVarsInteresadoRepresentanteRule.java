package es.dipucr.sigem.api.rule.procedures.comunadminelec;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

/**
 * [eCenpri-Felipe #1202]
 * Regla que borra de las variables de sesión los datos de interesado/representante
 * cargados mediante la regla CargarSessionVarsInteresadoRepresentanteRule
 */
public class BorrarSessionVarsInteresadoRepresentanteRule implements IRule 
{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.
			getLogger(BorrarSessionVarsInteresadoRepresentanteRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException 
	{
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException 
	{
		return true;
	}
	
	/**
	 * Ejecución de la regla
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		
        try
        {
        	//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        //----------------------------------------------------------------------------------------------
	        
			cct.deleteSsVariable("NIFCIF");
			cct.deleteSsVariable("NOMBRE");
			cct.deleteSsVariable("DIRNOT");
			cct.deleteSsVariable("C_POSTAL");
			cct.deleteSsVariable("LOCALIDAD");
			cct.deleteSsVariable("CAUT");
			cct.deleteSsVariable("EMAIL");
			cct.deleteSsVariable("MOVIL");
				
			return new Boolean(true);
	        
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error al borrar de la sesión los datos de interesado/representante", e);
	    }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
