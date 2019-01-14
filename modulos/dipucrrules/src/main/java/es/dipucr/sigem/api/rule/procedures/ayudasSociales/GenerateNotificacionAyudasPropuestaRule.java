package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.Exception;

import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;

/**
 * [dipucr-Felipe #573]
 * Genera las notificaciones individualizadas de la ayuda de la propuesta
 */
public class GenerateNotificacionAyudasPropuestaRule extends GenerateNotificacionAyudasAbstractRule 
{
	 
	private static final Logger LOGGER = Logger.getLogger(GenerateNotificacionAyudasPropuestaRule.class);	

	public static final double[] DISTRIBUCION_4_COLUMNAS_RS = {18, 35, 32, 15};
		
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		LOGGER.info("INICIO - "+this.getClass().getName());
		
		tipoDocumento = "Notificacion Ayudas";
		plantilla = "Notificacion Ayudas Propuesta";
		refTablas = "%TABLA1%";
		numexp = rulectx.getNumExp();
		
		try{
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IItemCollection col = entitiesAPI.getEntities("AYUDASSS_PROPUESTA", numexp);
			if (col.next()){
				IItem itemPropuesta = col.value();
				tipoRegimen = itemPropuesta.getString("TIPO_REGIMEN");
			}
		}
		catch(ISPACException ex){
			LOGGER.error("Error al cargar el tipo regimen de la propuesta", ex);
			throw new ISPACRuleException("Error al cargar el tipo regimen de la propuesta", ex);
		}

		return true;
	}
	
	
	public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
		try{
		
			IItemCollection colAyudasEmpleado = entitiesAPI.queryEntities
					("AYUDASSS", "WHERE NIF_EMPLEADO = '" + nifParticipante + "'"
							+ " AND NUMEXP = '" + numexp + "' ORDER BY PARENTESCO, BENEFICIARIO ");
			@SuppressWarnings("unchecked")
			List<IItem> listAyudasEmpleado = colAyudasEmpleado.toList();
			int numFilas = listAyudasEmpleado.size();
	       
	        if (refTabla.equals("%TABLA1%") && numFilas > 0){  
				
	        	int numFilasTabla = numFilas + 1;
	        	if (numFilas > 1){
	        		//Si el empleado tiene más de una ayuda, se le añade una fila más de totales
	        		numFilasTabla++;
	        	}
	        	
		   		XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, 
		   				numFilasTabla, DISTRIBUCION_4_COLUMNAS_RS.length);
		   		
		   		if (null != tabla){

		   			LibreOfficeUtil.colocaColumnas(tabla, DISTRIBUCION_4_COLUMNAS_RS);
		   			
	                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, "PARENTESCO");
	                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, "BENEFICIARIO");
	                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, "CONCEPTO");
	                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, "IMPORTE");
					
	                double totalAyudas = 0.0;
	                int i = 1;
	                for (IItem itemAyuda : listAyudasEmpleado){
	                	
	                	i++;
	                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, itemAyuda.getString("PARENTESCO"));
	                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, itemAyuda.getString("BENEFICIARIO"));
	                    LibreOfficeUtil.setTextoCelda(tabla, 3, i, itemAyuda.getString("CONCEPTO"));
	                    LibreOfficeUtil.setTextoCelda(tabla, 4, i, itemAyuda.getString("IMPORTE_CONCEDIDO") + " €");
	                    totalAyudas += Double.valueOf(itemAyuda.getString("IMPORTE_CONCEDIDO"));
	                }
	                
	                if (numFilas > 1){
	                	i++;
	                	BigDecimal bdTotalAyudas = new BigDecimal(totalAyudas).setScale(2, RoundingMode.HALF_DOWN);//[dipucr-Felipe #721]
	                	LibreOfficeUtil.setTextoCelda(tabla, 3, i, "TOTAL");
	                    LibreOfficeUtil.setTextoCelda(tabla, 4, i, bdTotalAyudas + " €");
	                }
		   		}
				
	        }
		}
		catch(ISPACException e) {
			LOGGER.error(e.getMessage(), e);
		}
        catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
    
}



