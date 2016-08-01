package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * Asigna el número de anuncio a asociar al expediente de BOP - Solicitud de inserción de anuncio.
 * Si el año actual coincide con el año de la tabla de validación lo obtiene incrementando el último número de anuncio utilizado y lo 
 * actualiza en la tabla de validación global de contadores.
 * Si el año actual no coincide con el año de la tabla de validación (año nuevo) asigna el nuevo año en la tabla de validación, reinicia los
 * contadores y asigna el primer número de anuncio del año.
 *
 */
public class GetNumAnuncioSolicitudRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		int longitudNumAnuncio = 10;
		
		ClientContext cct = null;
		
        try{
			//----------------------------------------------------------------------------------------------
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
			// Abrir transacción
	        cct.beginTX();
	        
	        String numAnuncioSolicitud = null;
	        String strQuery = null;
	        IItemCollection collection = null;
	        Iterator it = null;
	        IItem item = null;
	        
	        //Obtenemos el valor del Año de la tabla de validación global
	        strQuery = "WHERE VALOR = 'Año'";
	        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
	        it = collection.iterator();
	        int iAnio = -1;
	        if (it.hasNext()){
	        	item = (IItem)it.next();
	        	iAnio = Integer.parseInt(item.getString("SUSTITUTO"));
	        }
	        
	        if (iAnio > -1){
		        //Lo comparamos con el año actual (si fueran distintos hay que reiniciar los contadores ya que acaba de comenzar un nuevo año)
		        Calendar gc = new GregorianCalendar();
		        int iAnioActual = gc.get(Calendar.YEAR);
		        
		        if(iAnio == iAnioActual){
			        strQuery = "WHERE VALOR = 'num_anuncio_sol'";
			        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
			        it = collection.iterator();
			        String auxNumAnuncioSolicitud = null;
			        int iNumAnuncioSolicitud =  -1;
			        if (it.hasNext()){
			        	item = (IItem)it.next();
			        	iNumAnuncioSolicitud = Integer.parseInt(item.getString("SUSTITUTO"))+1;
			        	auxNumAnuncioSolicitud = String.valueOf(iNumAnuncioSolicitud);
			        	//Rellenamos con 0s a la izquierda hasta completar la longitud de longitudNumAnuncio dígitos
			        	while (auxNumAnuncioSolicitud.length() < longitudNumAnuncio){
			        		auxNumAnuncioSolicitud = "0"+auxNumAnuncioSolicitud;
			        	}
			        	numAnuncioSolicitud = iAnio + auxNumAnuncioSolicitud;
			        	//Actualizar el último número de anuncio utilizado en la tabla de validación global
			        	item.set("SUSTITUTO", iNumAnuncioSolicitud);
			        	item.store(cct);
			        }
			        if (iNumAnuncioSolicitud > -1){
			        	
			        	strQuery = "WHERE NUMEXP = '"+ cct.getStateContext().getNumexp()+"'";
				        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
				        it = collection.iterator();
				        if (it.hasNext()){
				        	item = (IItem)it.next();
				        	//Actualizar el número de anuncio en el expediente de Solicitud de anuncio
				        	item.set("NUM_ANUNCIO", numAnuncioSolicitud);
				        	item.store(cct);
				        }
			        }

		        }else{
		        	//Reiniciar los contadores ya que acaba de comenzar un nuevo año y posteriormente asignar el primer número de anuncio
		        	//	al expediente actual
		        	//Asignar el valor del año nuevo en la tabla de validación
		        	item.set("SUSTITUTO", iAnioActual);
		        	item.store(cct);
		        	
			        //Reiniciar el valor de los contadores en la tabla de validación
			        strQuery = "WHERE VALOR = 'num_bop' OR VALOR = 'num_pagina' OR VALOR = 'num_anuncio' OR VALOR = 'num_anuncio_sol'";
			        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
			        it = collection.iterator();
			        while (it.hasNext()){
			        	item = (IItem)it.next();
			        	if(item.getString("VALOR").equalsIgnoreCase("num_anuncio_sol")){
			        		item.set("SUSTITUTO", 1);
			        		item.store(cct);
			        	}else{
			        		item.set("SUSTITUTO", 0);
			        		item.store(cct);
			        	}
			        }
			        
			        //Asignar el primer número de anuncio
		        	//Rellenamos con 0s a la izquierda hasta completar la longitud de longitudNumAnuncio dígitos
		        	String auxNumAnuncioSolicitud = "1";
			        while (auxNumAnuncioSolicitud.length() < longitudNumAnuncio){
		        		auxNumAnuncioSolicitud = "0"+auxNumAnuncioSolicitud;
		        	}
			        
		        	strQuery = "WHERE NUMEXP = '"+ cct.getStateContext().getNumexp()+"'";
			        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
			        it = collection.iterator();
			        if (it.hasNext()){
			        	item = (IItem)it.next();
			        	item.set("NUM_ANUNCIO", iAnioActual + auxNumAnuncioSolicitud);
			        	item.store(cct);
			        }
			        
		        }
		        
	        }
	        
	        return numAnuncioSolicitud;
	        
	    } catch (Exception e) {
	    	
			// Si se produce algún error se hace rollback de la transacción
			try {
				cct.endTX(false);
			} catch (ISPACException e1) {
				e1.printStackTrace();
			}
	    	
	        throw new ISPACRuleException("Error al obtener el número de anuncio de solicitud.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
