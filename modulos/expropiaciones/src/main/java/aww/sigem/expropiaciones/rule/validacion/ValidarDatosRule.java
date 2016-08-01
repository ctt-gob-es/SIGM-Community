package aww.sigem.expropiaciones.rule.validacion;

import java.util.Iterator;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.rule.valoracion.CalculoValoracion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class ValidarDatosRule implements IRule  {

	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ValidarDatosRule.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return false;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		// Validar si se han marcado los checkbox que indican que se han rellenado todos los datos de fincas y expropiados.
		try{
			//logger.warn("Comienza la ejecucion de la regla ValidarDatosRule.");
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();			
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
			//logger.warn(strQuery);
			IItemCollection collection = entitiesAPI.queryEntities("EXPR_VALIDAR_DATOS", strQuery);
		
			Iterator it = collection.iterator();
			IItem item = null;
			
			if(it.hasNext()) {
				//logger.warn("Existe una entrada en EXPR_VALIDAR_DATOS para el expediente " +  rulectx.getNumExp());
				item = (IItem)it.next();
				String fincas = item.getString("COMPROBAR_FINCA");
				String exprop = item.getString("COMPROBAR_EXPROPIADO");
				
				if (fincas == null)
					fincas = "";
				
				if (exprop == null)
					exprop = "";
				
				if ((!exprop.equals("SI")) && (!fincas.equals("SI"))){
					rulectx.setInfoMessage("Debe confirmar en la pestaña Validacion Datos que se han introducido todos los datos de las fincas y los expropiados.");
					return false;
				}	
				
				if (!fincas.equals("SI")){
					rulectx.setInfoMessage("Debe introducir los datos de las fincas y marcar la validacion correspondiente en la pestaña Validacion Datos.");
					return false;
				}
				
				if (!exprop.equals("SI")){
					rulectx.setInfoMessage("Debe introducir los datos de los expropiados y marcar la validacion correspondiente en la pestaña Validacion Datos.");
					return false;
				}					
			}else{
				rulectx.setInfoMessage("Debe confirmar en la pestaña Validacion Datos que se han introducido todos los datos de las fincas y los expropiados.");
				return false;
			}
			
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al comprobar si se ha confirmado que se han introducido los datos de fincas y expropiados.", e);
	    } 
		return true;
	}

}
