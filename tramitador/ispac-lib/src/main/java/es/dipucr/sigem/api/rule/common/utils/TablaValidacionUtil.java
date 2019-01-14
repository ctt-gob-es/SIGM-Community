package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;

public class TablaValidacionUtil {

	/**
     * Devuelve el sustituto de valor de la tabla pasada como parámetro
     * @return
	 * @throws ISPACRuleException 
     */
	public static String getSustituto(IEntitiesAPI entitiesAPI, String entidad, String valor) throws ISPACRuleException {
		
		String resultado = null;
		
		try{
	        String strQuery = "WHERE VALOR = '" + valor + "'";
	        IItemCollection collection = entitiesAPI.queryEntities(entidad, strQuery);
	        if (collection.toList().size() == 1){
	        	IItem item = (IItem)collection.toList().get(0);
	        	resultado = item.getString("SUSTITUTO");
	        }
		}
		catch(Exception e){
			throw new ISPACRuleException("Error al obtener el sustituto del valor " +
					valor + " en la tabla de validación " + entidad + ": " + e.getMessage());
		}
        return resultado;
	}
}
