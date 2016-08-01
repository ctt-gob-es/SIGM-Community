package aww.sigem.expropiaciones.rule.valoracion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.rule.planificador.CalcularDiasTotalesRule;
import aww.sigem.expropiaciones.util.FuncionesUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class CalculoValoracion implements IRule {

	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(CalculoValoracion.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try{
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
			IItemCollection collection = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery);
			
			Iterator it = collection.iterator();
			IItem item = null;
			
			if(!it.hasNext()){ 
				rulectx.setInfoMessage("No se puede iniciar el Cálculo de la Valoración porque no se encontraron fincas.");
				return false;
			}
			
			item = (IItem)it.next();
			String superficie = item.getString("SUP_EXPROPIADA");
			String importe = item.getString("IMPORTE");

			if (superficie==null){
				rulectx.setInfoMessage("La superficie a expropiar de la finca no puede estar vacía. Utilice comas para separar los miles.");
				return false;
			}						
			else if (importe==null){
				rulectx.setInfoMessage("El importe en euros del metro cuadrado de la finca no puede estar vacío. Utilice la notacion punto si va a utilizar decimales.");
				return false;
			} 				
			
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al calcular la valoración.", e);
	    } 
		return true;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			//logger.warn("Ejecutando regla CalculoValoracion");
			
			// Obtener el importe por metro cuadrado y la superficie a expropiar de la finca y el porcentaje propiedad de cada propietario.
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			// Obtenemos la finca.
			//logger.warn("rulectx:" + rulectx.getNumExp());
			String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
			IItemCollection collection = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery);
			
			
			Iterator it = collection.iterator();
			IItem item = null;
				
			if(!it.hasNext()) 
				logger.error("No se encontraron Fincas");
			else{			
					// Extraemos los campos necesarios para calcular la valoracion.
				   item = (IItem)it.next();
				   
				   String superficie = item.getString("SUP_EXPROPIADA");
				   
				   // Si la superficie contiene comas se eliminan porque son el separador de miles.
				   superficie = (superficie!=null&&superficie.indexOf(",")!=-1)?superficie.replace(",", ""):superficie;
				   
				   // El importe puede contener decimales.
				   String importe = item.getString("IMPORTE");
				   				      
				   String strQueryPago = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
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
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
		return null;
	}
}
