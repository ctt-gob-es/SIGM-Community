package aww.sigem.expropiaciones.rule.validacion;

import java.util.Iterator;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class ValidarFincaExpropiado implements IRule  {

	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ValidarFincaExpropiado.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return false;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		// Validar si se han introducido los expropiados en la tabla FincaExpropiadoPago.
		try{
			//logger.warn("Comienza la ejecucion de la regla ValidarFincaExpropiado.");
			
			// El expediente en el que se encuentra es la finca.
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();			
			
				
			// Comprobar si hay una entrada para esa finca. Si la hay, es porque se ha introducido un expropiado.
			String strQueryFincaExprop = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
			IItemCollection collectionFincaExprop = entitiesAPI.queryEntities("EXPR_FINCA_EXPROPIADO_PAGO", strQueryFincaExprop);
			Iterator itFincaExprop = collectionFincaExprop.iterator();
				
			if(!itFincaExprop.hasNext()) {
				rulectx.setInfoMessage("No se ha rellenado la entidad Finca Expropiado Pago para la finca " +  rulectx.getNumExp() + ". Se deben rellenar los porcentajes de propiedad de los propietarios.");
				return false;
			}			
			
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al comprobar si se ha rellenado la entidad entidad Finca Expropiado Pago.", e);
	    } 
		return true;
	}

}
