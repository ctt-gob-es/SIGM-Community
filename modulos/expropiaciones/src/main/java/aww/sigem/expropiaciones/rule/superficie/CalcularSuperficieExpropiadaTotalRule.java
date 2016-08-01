package aww.sigem.expropiaciones.rule.superficie;


import java.util.Iterator;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.rule.valoracion.CalculoValoracion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class CalcularSuperficieExpropiadaTotalRule implements IRule {
	

		/** Logger de la clase. */
		private static final Logger logger = 
			Logger.getLogger(CalcularSuperficieExpropiadaTotalRule.class);
		
		public void cancel(IRuleContext rulectx) throws ISPACRuleException {

			
		}

		public Object execute(IRuleContext rulectx) throws ISPACRuleException {
			try {

				// Si pasa la regla de validacion es porque estan rellenas las superficies expropiadas.
				IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();			
					
				String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
				IItemCollection collection = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery);
					
				Iterator it = collection.iterator();
				IItem item = null;
					
				item = (IItem)it.next();
				String superficie_util = item.getString("SUP_EXPROPIADA_UTIL");
				String superficie_residual = item.getString("SUP_EXPROPIADA_RESIDUAL");

				double superficie_total = Double.parseDouble(superficie_util) + Double.parseDouble(superficie_residual);
				item.set("SUP_EXPROPIADA", superficie_total);

				// Guardar en la bd la modificacion de la finca.
				item.store(rulectx.getClientContext());  

		   } catch (Exception e) {
					throw new ISPACRuleException("Error al calcular la superficie expropiada total" +e);
		   }
		   return null;
		}

		public boolean init(IRuleContext rulectx) throws ISPACRuleException {
			return false;
		}

		public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
			try{
				
				IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
				String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
				IItemCollection collection = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery);
				
				Iterator it = collection.iterator();
				IItem item = null;
				
				if(!it.hasNext()){ 
					rulectx.setInfoMessage("No se pueden comprobar los campos de las superficies porque no se ha encontrado ninguna finca con numexp." +rulectx.getNumExp());
					return false;
				}
				
				item = (IItem)it.next();
				String superficie_util = item.getString("SUP_EXPROPIADA_UTIL");
				String superficie_residual = item.getString("SUP_EXPROPIADA_RESIDUAL");
				
				if (superficie_util==null){
					rulectx.setInfoMessage("Se debe rellenar al menos el campo Superficie Expropiada Util");
					return false;
				}else{
					if (superficie_residual==null){				
						item.set("SUP_EXPROPIADA_RESIDUAL", "0");
						superficie_residual = "0";
						// Guardar en la bd la modificacion de la finca.
						item.store(rulectx.getClientContext());  
					}
				}
				
				if (Double.parseDouble(superficie_util) + Double.parseDouble(superficie_residual) > Double.parseDouble(item.getString("SUP_PARCELA"))){
					rulectx.setInfoMessage("La superficie a expropiar no puede ser mayor que la superficie de la parcela");
					return false;
				}
			} catch (Exception e) {
				throw new ISPACRuleException("Error al comprobar los campos de las superficies expropiadas", e);
			} 
		return true;
	}

}
