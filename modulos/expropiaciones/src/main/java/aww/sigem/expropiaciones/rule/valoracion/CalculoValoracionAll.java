package aww.sigem.expropiaciones.rule.valoracion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.util.FuncionesUtil;

public class CalculoValoracionAll implements IRule {

	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(CalculoValoracion.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean resultado = true;
		try{
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			
			String numExpropiacion = rulectx.getNumExp(); 
			
			String strQuery = "WHERE NUMEXP_PADRE = '" + numExpropiacion + "' AND RELACION = 'Finca/Expropiacion'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			
			Iterator it = collection.iterator();
			
			String mensajeSuperficie = "";
			String mensajeImporte = "";
			while (it.hasNext()) {
				IItem iExpRelaFinca = (IItem) it.next();
				
				String numExpRela = iExpRelaFinca.getString("NUMEXP_HIJO");
				strQuery = "WHERE NUMEXP = '" + numExpRela + "'";
				IItemCollection collectionFinca = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery);		
				Iterator itFinca = collectionFinca.iterator();
		
				IItem item = null;
				
				if(!itFinca.hasNext()){ 
					rulectx.setInfoMessage("No se puede iniciar el Cálculo de la Valoración porque no se encontraron fincas.");
					return false;
				}
				
				item = (IItem)itFinca.next();
				String superficie = item.getString("SUP_EXPROPIADA");
				String importe = item.getString("IMPORTE");
				if(superficie==null){
					mensajeSuperficie += numExpRela +" , ";
				}
				if (importe==null){
					mensajeImporte += numExpRela + " , ";
				}
			}
			String mensajeError = "";
			if (!mensajeSuperficie.equals("")){
				mensajeError = "- La superficie a expropiar de la finca no puede estar vacía. " +
						"Utilice comas para separar los miles. Expedientes a modificar: "+ mensajeSuperficie +"";
				resultado = false;
			}						
			if (!mensajeImporte.equals("")){
				mensajeError += " - El importe en euros del metro cuadrado de la finca no puede estar vacío. " +
						"Utilice la notacion punto si va a utilizar decimales. Expedientes a modificar: "+ mensajeImporte+".";
				resultado = resultado & false;
			}
			if(resultado == false){
				rulectx.setInfoMessage(mensajeError);
			}
			
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al calcular la valoración.", e);
	    } 
		return resultado;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			//logger.warn("Ejecutando regla CalculoValoracion");
			
			// Obtener el importe por metro cuadrado y la superficie a expropiar de la finca y el porcentaje propiedad de cada propietario.
			/***********************************************************************************/
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			/***********************************************************************************/
			
			String numExpropiacion = rulectx.getNumExp(); 
			
			String strQuery = "WHERE NUMEXP_PADRE = '" + numExpropiacion + "' AND RELACION = 'Finca/Expropiacion'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			
			Iterator it = collection.iterator();
			while (it.hasNext()) {
				IItem iExpRelaFinca = (IItem) it.next();
				
				String numExpRela = iExpRelaFinca.getString("NUMEXP_HIJO");
				//logger.warn("numExpRela "+numExpRela);
				strQuery = "WHERE NUMEXP = '" + numExpRela + "'";
				IItemCollection collectionFinca = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery);		
				Iterator itFinca = collectionFinca.iterator();
					
				if(!itFinca.hasNext()) 
					logger.error("No se encontraron Fincas");
				else{			
						// Extraemos los campos necesarios para calcular la valoracion.
					IItem itemFinca = (IItem)itFinca.next();
					// Obtenemos la finca.
					   String numExpFinca = itemFinca.getString("NUMEXP");
					   //logger.warn("rulectx:" + numExpFinca);
					   
					   String superficie = itemFinca.getString("SUP_EXPROPIADA");
					   
					   // Si la superficie contiene comas se eliminan porque son el separador de miles.
					   superficie = (superficie!=null&&superficie.indexOf(",")!=-1)?superficie.replace(",", ""):superficie;
					   
					   // El importe puede contener decimales.
					   String importe = itemFinca.getString("IMPORTE");
					   //logger.warn("importe:" + importe);
					   				      
					   String strQueryPago = "WHERE NUMEXP = '" + numExpFinca + "'";
					   IItemCollection collectionPago = entitiesAPI.queryEntities("EXPR_FINCA_EXPROPIADO_PAGO", strQueryPago);
					   
					   Iterator itPago = collectionPago.iterator();
					   IItem itemPago = null;
					   double valoracionParcial;
					   
					   if(!itPago.hasNext())
						   logger.error("No exite la finca a pagar");
					   else{
						   // Si hay uno o mas propietarios.
						   while (itPago.hasNext()){
							   
							   valoracionParcial = Double.parseDouble(superficie) * Double.parseDouble(importe);
							   
							   itemPago = (IItem)itPago.next();					   
							   
							   String porcentaje = itemPago.getString("PORCENTAJE_PROP");
							   
							   // Si el porcentaje contiene comas se cambian por puntos.
							   porcentaje = (porcentaje!=null&&porcentaje.indexOf(",")!=-1)?porcentaje.replace(",", "."):porcentaje;
							   						   
							   valoracionParcial = valoracionParcial * Double.parseDouble(porcentaje) * 0.01;
							   
							   //logger.warn("valoracion antes " + valoracionParcial);
							   
							   valoracionParcial = FuncionesUtil.redondeoDecimales(valoracionParcial);
							   
							   //logger.warn("valoracion despues " + valoracionParcial);
							   // Ahora hay que almacenar la valoracion en el campo cantidad pago de la tabla EXPR_FINCA_EXPROPIADO_PAGO.
							   
							   itemPago.set("CANTIDADPAGO", valoracionParcial);
							   itemPago.store(rulectx.getClientContext());  
						   }	
					   }
				}
			}
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
		return null;
	}
}

