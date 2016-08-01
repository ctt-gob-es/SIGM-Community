package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class BopFacturasUtil {
	
	public static final Logger logger = Logger.getLogger(BopUtils.class);

	/**
	 * Constantes
	 */
	public final static int _LONGITUD_NUMFACTURA = 7;
	
	//[eCenpri-Felipe #153]
	public final static String _SERIE_FACTURA = "BOP";
	public final static String _SERIE_FACE = "FACE";//[dipucr-Felipe #1311]
	public final static String _SERIE_ALBARAN = "ALB";
	public final static String _FACT_SEPARATOR = "-";
	
	//INICIO [dipucr-Felipe #1311]
	public final static String _TIPO_CON_CREDITO = "Pago con crédito";
	public final static String _TIPO_SIN_CREDITO = "Pago sin crédito";
	public final static String _TIPO_ALBARAN = "Albarán";
	
	public final static String _COUNTER_FACTURA = "num_factura";
	public final static String _COUNTER_FACE = "num_efactura";
	public final static String _COUNTER_ALBARAN = "num_albaran";
	public final static String _COUNTER_ANYO = "anyo_factura";
	
	public final static String _DESC_FACTURA = "Factura";
	public final static String _DESC_FACE = "Factura electrónica";
	public final static String _DESC_ALBARAN = "Albarán";
	//FIN [dipucr-Felipe #1311]
	
	
	/**
	 * [dipucr-Felipe #1311]
	 * En función del tipo de liquidación recupera la serie correspondiente
	 * y se llama a la función getNumFactura
	 * @param rulectx
	 * @return
	 * @throws ISPACRuleException
	 */
	public static String getNumFactura(IRuleContext rulectx, String tipoLiquidacion) throws ISPACRuleException{
		
		String serie = getSerieByTipoFactura(tipoLiquidacion);
		String nombreContador = getContadorByTipoFactura(tipoLiquidacion);
		
		return getNumFacturaSerie(rulectx, serie, nombreContador);
	}
	
	/**
	 * [dipucr-Felipe #1311]
	 * @param tipoLiquidacion
	 * @return
	 */
	public static String getSerieByTipoFactura(String tipoLiquidacion) {

		String serie = null;
		if (tipoLiquidacion.equals(_TIPO_CON_CREDITO)){
			serie = _SERIE_FACE;
		}
		else if (tipoLiquidacion.equals(_TIPO_SIN_CREDITO)){
			serie = _SERIE_FACTURA;
		}
		else if (tipoLiquidacion.equals(_TIPO_ALBARAN)){
			serie = _SERIE_ALBARAN;
		}
		return serie;
	}

	/**
	 * [dipucr-Felipe #1311]
	 * @param tipoLiquidacion
	 * @return
	 */
	public static String getContadorByTipoFactura(String tipoLiquidacion) {

		String nombreContador = null;
		if (tipoLiquidacion.equals(_TIPO_CON_CREDITO)){
			nombreContador = _COUNTER_FACE;
		}
		else if (tipoLiquidacion.equals(_TIPO_SIN_CREDITO)){
			nombreContador = _COUNTER_FACTURA;
		}
		else if (tipoLiquidacion.equals(_TIPO_ALBARAN)){
			nombreContador = _COUNTER_ALBARAN;
		}
		return nombreContador;
	}
	
	/**
	 * [dipucr-Felipe #1397]
	 * @param serie
	 * @return
	 */
	public static String getContadorBySerie(String serie) {

		String nombreContador = null;
		if (serie.equals(_SERIE_FACE)){
			nombreContador = _COUNTER_FACE;
		}
		else if (serie.equals(_SERIE_FACTURA)){
			nombreContador = _COUNTER_FACTURA;
		}
		else if (serie.equals(_SERIE_ALBARAN)){
			nombreContador = _COUNTER_ALBARAN;
		}
		return nombreContador;
	}
	
	/**
	 * [dipucr-Felipe #1397]
	 * @param numFactura
	 * @return
	 */
	public static String getSerieFactura(String numFactura) {

		String nombreContador = null;
		if (numFactura.startsWith(_SERIE_FACE)){
			nombreContador = _SERIE_FACE;
		}
		else if (numFactura.startsWith(_SERIE_FACTURA)){
			nombreContador = _SERIE_FACTURA;
		}
		else if (numFactura.startsWith(_SERIE_ALBARAN)){
			nombreContador = _SERIE_ALBARAN;
		}
		return nombreContador;
	}
	
	/**
	 * [dipucr-Felipe #1311]
	 * @param tipoLiquidacion
	 * @return
	 */
	public static String getDescripcionByTipoFactura(String tipoLiquidacion) {

		String descripcion = null;
		if (tipoLiquidacion.equals(_TIPO_CON_CREDITO)){
			descripcion = _DESC_FACE;
		}
		else if (tipoLiquidacion.equals(_TIPO_SIN_CREDITO)){
			descripcion = _DESC_FACTURA;
		}
		else if (tipoLiquidacion.equals(_TIPO_ALBARAN)){
			descripcion = _DESC_ALBARAN;
		}
		return descripcion;
	}
	
	
	/**
	 * 
	 * @param rulectx
	 * @param serie [dipucr-Felipe #1311]
	 * @param nombreContador [dipucr-Felipe #1311]
	 * @return
	 * @throws ISPACRuleException
	 */
	@SuppressWarnings("rawtypes")
	private static String getNumFacturaSerie(IRuleContext rulectx, String serie, String nombreContador) 
			throws ISPACRuleException 
	{
		ClientContext cct = null;
		
        try{
			//----------------------------------------------------------------------------------------------
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
			// Abrir transacción
	        cct.beginTX();
	        
	        String numFactura = null;
	        String strQuery = null;
	        IItemCollection collection = null;
	        Iterator it = null;
	        IItem item = null;
	        
	        //Obtenemos el valor del anyo_factura de la tabla de validación global
	        strQuery = "WHERE VALOR = '" + _COUNTER_ANYO + "'";
	        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
	        it = collection.iterator();
	        int iAnio = -1;
	        if (it.hasNext()){
	        	
	        	item = (IItem)it.next();
	        	iAnio = Integer.parseInt(item.getString("SUSTITUTO"));

	        	//[dipucr-Felipe #1311]
	        	strQuery = "WHERE VALOR = '" + nombreContador + "'";
	        		
		        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
		        it = collection.iterator();
		        String auxNumFactura = null;
		        int iNumFactura =  -1;
		        if (it.hasNext())
		        {
		        	item = (IItem)it.next();
		        	iNumFactura = Integer.parseInt(item.getString("SUSTITUTO")) + 1;
		        	auxNumFactura = String.valueOf(iNumFactura);
		        	
		        	//[eCenpri-Felipe #153]
		        	numFactura = generateNumFactura(String.valueOf(iAnio), auxNumFactura, serie);
			        	
		        	//Actualizar el último número de factura utilizado en la tabla de validación global
		        	item.set("SUSTITUTO", iNumFactura);
		        	item.store(cct);
		        }
		        else{
		        	throw new ISPACRuleException("Error al generar las facturas: " + 
		        			"Algún contador no existe o su valor es incorrecto.");
		        }
	        }
	        else{
	        	throw new ISPACRuleException("Error al generar las facturas: " +
	        			"El valor del contador 'anyo_factura' de la tabla 'BOP_VLDTBL_CONTADORES' " +
	        			"no existe o es incorrecto");
	        }
	        return numFactura;
	        
	    } catch (Exception e) {
	    	
			// Si se produce algún error se hace rollback de la transacción
			try {
				cct.endTX(false);
			} catch (ISPACException e1) {
				logger.error(e1.getMessage(), e1);
			}
	    	
	        throw new ISPACRuleException("Error al generar las facturas", e);
	    }     
	}
	
	/**
	 * [eCenpri-Felipe 26.02.13 #153]
	 * Método de generación del número de factura
	 * @param anio
	 * @param numero
	 * @param serie [dipucr-Felipe #1311]
	 * @return
	 */
	private static String generateNumFactura(String anio, String numero, String serie) {
		
		String numFactura = null;
		
		//Rellenamos con 0s a la izquierda hasta completar la longitud de longitudNumFactura dígitos
    	while (numero.length() < _LONGITUD_NUMFACTURA)
    	{
    		numero = "0" + numero;
    	}
    	
    	//[dipucr-Felipe #1311]
    	numFactura = serie + anio + _FACT_SEPARATOR + numero;
    	
    	return numFactura; 
	}
	
	/**
	 * Inserta la factura en la tabla BOP_FACTURAS
	 * @param cct
	 * @param entitiesAPI
	 * @param numFactura
	 * @param tipoLiquidacion
	 * @throws ISPACException 
	 */
	@SuppressWarnings("rawtypes")
	public static void insertarFactura(ClientContext cct, IEntitiesAPI entitiesAPI, 
			String numFactura, String tipoLiquidacion, String numexp, String nifciftitular,
			String nombreTitular, double coste, double costeTotal, Date fechaFactura) throws ISPACException{
		
		String strQuery = "WHERE NUM_FACTURA = '"+ numFactura +"'";
		IItemCollection collection = entitiesAPI.queryEntities("BOP_FACTURAS", strQuery);	
    	Iterator it = collection.iterator();
        
    	//Sólo insertaremos cuando no exista ya el registro
        if (!it.hasNext()){
	        IItem itemFactura = entitiesAPI.createEntity("BOP_FACTURAS", numexp);
	        itemFactura.set("NUM_FACTURA", numFactura);
	        itemFactura.set("FECHA_FACTURA", fechaFactura);
	        itemFactura.set("TIPO_FACTURACION", tipoLiquidacion);
	        itemFactura.set("NIFCIFTITULAR", nifciftitular);
	        itemFactura.set("IDENTIDADTITULAR", nombreTitular);
	        itemFactura.set("IMPORTE_BASE", coste);
	        itemFactura.set("IMPORTE_IVA", costeTotal - coste);
	        itemFactura.set("IMPORTE_TOTAL", costeTotal);
	        itemFactura.store(cct);
        }
	}
	
	/**
	 * [eCenpri-Felipe 26.02.13 #153]
	 * @param numFactura
	 * @param oFactura Objeto factura pasado por referencia en el que guardamos
	 * el tipo de factura, el año y el numero de factura
	 * @return
	 */
	public static boolean comprobarFormatoFactura(String numFactura, FacturaBop oFactura) {
		
		//Comprobamos que la factura está separada por "-"
		String [] arrFactura = numFactura.split(_FACT_SEPARATOR);
		if (arrFactura.length != 2){
			return false;
		}
		
		//Comprobamos primero el anio
		String anio = null;
		Integer iAnio = Integer.MIN_VALUE;
		//[dipucr-Felipe #1311]
		if (numFactura.startsWith(_SERIE_ALBARAN)){
			oFactura.setbTipoAlbaran(true);
			anio = arrFactura[0].replace(_SERIE_ALBARAN, "");
		}
		else if (numFactura.startsWith(_SERIE_FACE)){
			oFactura.setbTipoAlbaran(false);
			anio = arrFactura[0].replace(_SERIE_FACE, "");
		}
		else if (numFactura.startsWith(_SERIE_FACTURA)){
			oFactura.setbTipoAlbaran(false);
			anio = arrFactura[0].replace(_SERIE_FACTURA, "");
		}
		else{
			return false;
		}
		try{
			iAnio = Integer.valueOf(anio);
		}
		catch(NumberFormatException e){
			return false;
		}
		oFactura.setAnio(String.valueOf(iAnio));
		
		//Comprobamos el número de la factura
		String numero = arrFactura[1];
		int iNumero = Integer.MIN_VALUE;
		if (numero.length() != _LONGITUD_NUMFACTURA){
			return false;
		}
		try{
			iNumero = Integer.valueOf(numero);
		}
		catch(NumberFormatException e){
			return false;
		}
		oFactura.setNumero(String.valueOf(iNumero));
		
		return true;
	}
}
