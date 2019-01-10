package aww.sigem.expropiaciones.util;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class ConsignaTransferenciaUtil {
	
	private static final Logger logger = 
		Logger.getLogger(ConsignaTransferenciaUtil.class);
	/**
	 * Devuelve una lista de numeros de expediente de propietarios
	 * @param entitiesAPI
	 * @param numExpFinca
	 * @return
	 * @throws Exception
	 */
	static public List listaExpedientesPropietariosFinca(IEntitiesAPI entitiesAPI, String numExpFinca) throws ISPACException{
		
		//Buscar los expedientes de los Expropiados
		//logger.warn("Expediente de Finca: " + numExpFinca);
		String strQuery = "WHERE NUMEXP_PADRE = '" + numExpFinca + "' AND RELACION = 'Expropiado/Finca'";
		IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
		Iterator it = collection.iterator();
		IItem item = null;	
		List expExpropiados = new ArrayList();	
				
		while (it.hasNext()) {
		   item = (IItem)it.next();
		   expExpropiados.add(item.getString("NUMEXP_HIJO"));
		   //logger.warn("Expediente de Expropiado listaExpedientesPropietariosFinca: " + item.getString("NUMEXP_HIJO"));			
		}
		
		return expExpropiados;
		
	}
	
	
	static public List listaExpedientesPropietariosConsigna(IEntitiesAPI entitiesAPI, String numExpFinca) throws ISPACException {
		
		List listaExpPropietarios = listaExpedientesPropietariosFinca(entitiesAPI, numExpFinca);
		
		Iterator itExpropiados = listaExpPropietarios.iterator();
		List expExpropiados = new ArrayList();
		
		String numExpExpropiado;
		
		while(itExpropiados.hasNext()){
			numExpExpropiado = (String)itExpropiados.next();
			if (isConsigna(entitiesAPI, numExpExpropiado)) {
				expExpropiados.add(numExpExpropiado);
			} 			
		}
		
		return expExpropiados;
		
	}
	
	static public List listaExpedientesPropietariosTransferencia(IEntitiesAPI entitiesAPI, String numExpFinca) throws ISPACException {
		
		List listaExpPropietarios = listaExpedientesPropietariosFinca(entitiesAPI, numExpFinca);
		
		Iterator itExpropiados = listaExpPropietarios.iterator();
		List expExpropiados = new ArrayList();
		
		String numExpExpropiado;
		
		while(itExpropiados.hasNext()){
			numExpExpropiado = (String)itExpropiados.next();
			if (!isConsigna(entitiesAPI, numExpExpropiado)) {
				expExpropiados.add(numExpExpropiado);
			} 			
		}
		
		return expExpropiados;
		
	}
	
	static public String porcentajeFinca(IEntitiesAPI entitiesAPI, String numExpPropietario, String numExpFinca) throws ISPACException{
		
		String strQuery = "WHERE NUMEXP_EXPROPIADO = '" + numExpPropietario + "' AND NUMEXP = '" + numExpFinca + "'";
		IItemCollection collection = entitiesAPI.queryEntities("EXPR_FINCA_EXPROPIADO_PAGO", strQuery);
		Iterator it = collection.iterator();
		IItem item = null;	
				
		if (it.hasNext()) {
			 item = (IItem)it.next();
			 if (item.getString("PORCENTAJE_PROP").equals("100,00"))
				 return "";
			 else 
				 return "Porcentaje propiedad: " + item.getString("PORCENTAJE_PROP");
			 
		}
		
		return "";
	}	
	
	// Obtiene únicamente el nombre
	static public String nombrePropietarioFinca(IEntitiesAPI entitiesAPI, String numExpPropietario) throws ISPACException{
		String strQuery = "WHERE NUMEXP = '" + numExpPropietario + "'";
		IItemCollection collection = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
		Iterator it = collection.iterator();
		IItem item = null;	
				
		if (it.hasNext()) {
		   item = (IItem)it.next();
		   return item.getString("NOMBRE");		   
		   		
		} else {
		   return "Desconocido";				
		}			
		
	}
	
	
	/**
	 * Método para calcular la cantidad a pagar a un propietario
	 * Lo hace a partir de la entidad EXPR_FINCA_EXPR_VALOR_PAGO
	 * @param entitiesAPI
	 * @param numExpFinca
	 * @param numExpPropietario
	 * @return
	 */
	static public double cantidadPagoPropietarioFinca(IEntitiesAPI entitiesAPI, String numExpFinca, String numExpPropietario) throws ISPACException{
		//logger.warn("numExpExprop: " +numExpPropietario);
		String strQuery = "WHERE NUMEXP_EXPROPIADO = '" + numExpPropietario + "' AND NUMEXP = '" + numExpFinca + "'";
		IItemCollection collection = entitiesAPI.queryEntities("EXPR_FINCA_EXPROPIADO_PAGO", strQuery);
		Iterator it = collection.iterator();
		IItem item = null;	
				
		if (it.hasNext()) {
		   item = (IItem)it.next();
		   //logger.warn("cantidad pago" + item.getDouble("CANTIDADPAGO"));
		   double cantidad;
		   if (item.getDouble("CANTIDADPAGO") >= 0.01)
			   cantidad = item.getDouble("CANTIDADPAGO"); 
		   else
			   cantidad=0;
		   
		   //logger.warn("variable cantidad " +FuncionesUtil.redondeoDecimales(cantidad));
		   return FuncionesUtil.redondeoDecimales(cantidad);		   
		   		
		} else {
		   return 0;				
		}			
		
	}
	

	/**
	 * Método para calcular si se consigna o se transfiere la cantidad a pagar
	 * Depende de si el expropiado ha facilitado su cuenta bancaria
	 * @param entitiesAPI
	 * @param numExpPropietario
	 * @return
	 */
	static public boolean isConsigna(IEntitiesAPI entitiesAPI, String numExpPropietario) throws ISPACException {
		//logger.warn("Expediente de Propietario: " + numExpPropietario);
		String strQuery = "WHERE NUMEXP = '" + numExpPropietario + "'";
		IItemCollection collection = entitiesAPI.queryEntities("EXPR_CCC", strQuery);
		Iterator it = collection.iterator();
	
		if (it.hasNext()) {
		   return false;			
		} else {
			return true;	
		}		
		
	}
	
	static private String codificarCuenta(String cuenta){
		String numCuenta="";
		String otrosDigitos="";
		String cuentaCodificada="";		
		
		//logger.warn("Cuenta a codificar " + cuenta);		
		
		otrosDigitos = cuenta.substring(0, cuenta.length() - 10);
		
		numCuenta= cuenta.substring(cuenta.length() - 10, cuenta.length());
		
		cuentaCodificada=otrosDigitos+"******"+numCuenta.substring(6);
		
		//logger.warn("cuentaCodificada " + cuentaCodificada);	
		
		return cuentaCodificada;
	}
	
	/**
	 * Método para calcular si se consigna o se transfiere la cantidad a pagar
	 * Depende de si el expropiado ha facilitado su cuenta bancaria
	 * @param entitiesAPI
	 * @param numExpPropietario
	 * @return
	 */
	static public String datosCuentaCorriente(IEntitiesAPI entitiesAPI, String numExpPropietario, boolean codificar) throws ISPACException {
		//logger.warn("Expediente de Propietario: " + numExpPropietario);
		String strQuery = "WHERE NUMEXP = '" + numExpPropietario + "'";
		IItemCollection collection = entitiesAPI.queryEntities("EXPR_CCC", strQuery);
		Iterator it = collection.iterator();
			
		String ccc = "";		
		if (it.hasNext()) {
			IItem item = (IItem) it.next();
			if (item.getString("CCC")==null || item.getString("ENTIDAD")==null) return "";
			ccc += item.getString("ENTIDAD");
			ccc += " ";
			if (codificar == false)
				ccc += item.getString("CCC");
			else
				ccc += codificarCuenta(item.getString("CCC"));
			return ccc;
		} else {
			return "";	
		}		
		
		
	}

}
