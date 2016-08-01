package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Date;

import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

/**
 * [eCenpri-Felipe #40]
 * Carga y actualización de los contadores del BOP en el proc. "BOP - Reinicio contadores"
 * @author Felipe
 *
 */
public class CargarActualizarContadoresBopRule extends GenerateLiquidacionRecibos implements IRule 
{
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		ClientContext cct = null;
		
		try{
			//******************************************************
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //******************************************************

	        String strQuery = null;
	        IItemCollection collection = null;
	        IItem itemBopReinicio = null;
	        IItem itemBopPublicacion = null;
	      
	        //Recuperamos los valores de la entidad
	        itemBopReinicio = rulectx.getItem();
	        
	        String update = itemBopReinicio.getString("UPDATE_BD");
	        
	        //Carga de los contadores por fecha
	        if (StringUtils.isEmpty(update) || update.equals("NO")){
	        	Date dFecha = itemBopReinicio.getDate("FECHA");
	        	
	        	if (null == dFecha){
	        		rulectx.setInfoMessage("Para realizar la carga de los contadores" +
	        				" debe rellenar al menos la fecha");
	        		return new Boolean(false);
	        	}

	        	strQuery = "WHERE FECHA = DATE('"+ FechasUtil.getFormattedDateForQuery(dFecha) +"')";
	        	collection = entitiesAPI.queryEntities("BOP_PUBLICACION", strQuery);
	        	
	        	if (collection.next()){
	        		itemBopPublicacion = (IItem) collection.iterator().next();
	        		itemBopReinicio.set("NUM_BOP", itemBopPublicacion.get("NUM_BOP"));
	        		itemBopReinicio.set("NUM_ANUNCIO", itemBopPublicacion.get("NUM_ULTIMO_ANUNCIO"));
	        		itemBopReinicio.set("NUM_PAGINA", itemBopPublicacion.get("NUM_ULTIMA_PAGINA"));
//	        		itemBopReinicio.store(cct);
	        	}
	        	else{
	        		rulectx.setInfoMessage("No se ha recuperado ningún boletín para la fecha introducida");
	        		return new Boolean(false);
	        	}
	        	
	        }
	        //Actualización de contadores
	        else{
	        	itemBopReinicio = rulectx.getItem();
	        	
	        	String numBop = itemBopReinicio.getString("NUM_BOP");
	        	String numAnuncio = itemBopReinicio.getString("NUM_ANUNCIO");
	        	String numPagina = itemBopReinicio.getString("NUM_PAGINA");
	        	Date dFechaUpdate = itemBopReinicio.getDate("FECHA");
	        	
	        	if (StringUtils.isEmpty(numBop) || StringUtils.isEmpty(numAnuncio) 
	        			|| StringUtils.isEmpty(numPagina) || null == dFechaUpdate)
	        	{
	        		rulectx.setInfoMessage("Es necesario rellenar todos los parámetros.");
	        		return new Boolean(false);
	        	}
	        	
	        	updateContador(rulectx, "num_bop", numBop);
	        	updateContador(rulectx, "num_anuncio", numAnuncio);
	        	updateContador(rulectx, "num_pagina", numPagina);
	        	String fecha = FechasUtil.getFormattedDate(dFechaUpdate, "yyyyMMdd");
	        	updateContador(rulectx, "fecha_ultimo_bop", fecha);
	        	
	        	rulectx.setInfoMessage("Se han actualizado los contadores en la BBDD.");
        		return new Boolean(true);
	        }
	        
        }
        catch (Exception e) {
        	throw new ISPACRuleException("Error al cargar/recuperar los contadores del BOP" , e);
		}
		return new Boolean(true);
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}
	
	/**
	 * Actualiza el valor del contador en la tabla BOP_VLDTBL_CONTADORES
	 * @param entitiesAPI
	 * @param nombre
	 * @throws ISPACException 
	 */
	private static void updateContador(IRuleContext rulectx, String nombre, String valor) throws ISPACException{
		
		//******************************************************
        ClientContext cct = (ClientContext) rulectx.getClientContext();
        IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
        //******************************************************
        
		String strQuery = null;
		IItem itemContador = null;
		IItemCollection collection = null;
		
		strQuery = "WHERE VALOR = '" + nombre + "'";
        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
		itemContador = (IItem) collection.iterator().next();
		
		itemContador.set("SUSTITUTO", valor);
		itemContador.store(cct);
	}
	

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
