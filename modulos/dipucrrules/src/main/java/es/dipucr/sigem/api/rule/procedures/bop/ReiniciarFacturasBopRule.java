package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

/**
 * [eCenpri-Felipe #153]
 * Reinicio de los contadores de facturación y eliminación de facturas erróneas
 * @author Felipe
 *
 */
public class ReiniciarFacturasBopRule extends GenerateLiquidacionRecibos implements IRule 
{
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	@SuppressWarnings("rawtypes")
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
	        Iterator it = null;
	        IItem itemBopReinicioFact = null;
	        IItem itemFactura = null;
	        IItem itemAnuncio = null;
	        IItem itemContador = null;
	      
	        //Recuperamos los valores de la entidad
	        itemBopReinicioFact = rulectx.getItem();
	        
	        FacturaBop datosFactura = new FacturaBop();
	        String ultimaFactura = itemBopReinicioFact.getString("ULTIMA_FACTURA");
        	
        	if (null == ultimaFactura){
        		rulectx.setInfoMessage("Debe rellenar el campo 'Última factura'");
        		return new Boolean(false);
        	}
        	else if (!BopFacturasUtil.comprobarFormatoFactura(ultimaFactura, datosFactura)){
        		rulectx.setInfoMessage("El formato de la factura es incorrecto");
        		return new Boolean(false);
        	}
        	else if (datosFactura.isbTipoAlbaran()){
        		rulectx.setInfoMessage("No es posible reiniciar las facturas de tipo Albarán." +
        				" Consulte con el adminsitrador");
        		return new Boolean(false);
        	}

        	String serieFactura = BopFacturasUtil.getSerieFactura(ultimaFactura); //[dipucr-Felipe #1397] Limitamos a la serie
        	strQuery = "WHERE NUM_FACTURA LIKE '" + serieFactura + "%' AND NUM_FACTURA > '" + ultimaFactura + "'";
        	collection = entitiesAPI.queryEntities("BOP_FACTURAS", strQuery);
        	
        	//Si el parametro de actualizar está a "NO" o vacío se marca el número de facturas a actualizar
	    	int totalFacturas = collection.toList().size();
	    	itemBopReinicioFact.set("TOTAL_FACTURAS", totalFacturas);
//	    	itemBopReinicioFact.store(cct);

	    	String update = itemBopReinicioFact.getString("UPDATE_BD");
	    	//Actualizar
			if (StringUtils.isNotEmpty(update) && update.equals("SI")){
		    	
		    	//Borramos las facturas
		    	it = collection.iterator();
		    	while (it.hasNext()){
		    		itemFactura = (IItem) it.next();
		    		itemFactura.delete(cct);
		    	}
		    	
		    	//Borramos la referencia a estas facturas en los anuncios
	        	collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
	        	it = collection.iterator();
		    	while (it.hasNext()){
		    		itemAnuncio = (IItem) it.next();
		    		itemAnuncio.set("NUM_FACTURA", "");//NULL??
		    		itemAnuncio.store(cct);
		    	}
		    	
		    	//Actualizamos el contador de facturas
		    	String contador = BopFacturasUtil.getContadorBySerie(serieFactura); //[dipucr-Felipe #1397] Modificamos el contador correspondiente
		    	strQuery = "WHERE VALOR = '" + contador + "'";
		    	collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
		    	itemContador = (IItem) collection.iterator().next();
		    	int numeroFactura = Integer.valueOf(datosFactura.getNumero());
		    	itemContador.set("SUSTITUTO", numeroFactura);
		    	itemContador.store(cct);
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
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
