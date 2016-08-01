package es.dipucr.sigem.api.rule.procedures.licenciasObra;

import java.util.Date;

import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;


/**
 * [eCenpri-Felipe ticket #1039]
 * Regla que a partir del presupuesto y la fianza rellenada por los servicios
 * técnicos, calcula el coste total de la licencia de obra
 * @author Felipe
 * @since 23.01.14
 */
public class CalcularCosteTotalLicenciaObraRule implements IRule 
{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validación y obtención de los datos de la licencia de obras
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		String numexp = null;
		IClientContext cct = rulectx.getClientContext();
		IEntitiesAPI entitiesAPI = null;
		
		try{
			entitiesAPI = cct.getAPI().getEntitiesAPI();
			numexp = rulectx.getNumExp();
			
			//Controlamos que la licencia de obra tenga fecha de entrada en el registro
			IItem itemExpediente = entitiesAPI.getExpedient(numexp);
			Date freg = itemExpediente.getDate("FREG");
			if (null == freg){
				rulectx.setInfoMessage("No es posible aplicar un rango de impuestos al presupuesto porque" +
						" la licencia no tiene fecha de entrada en el registro. Consulte con el tramitador.");
				return false;
			}
			
			//Controlamos que los servicios técnicos hayan rellenado los campos obligatorios
			String msgError = "Es necesario rellenar los campos presupuesto, fianza y condiciones especiales " +
					"(este último sólo si es necesario) en la pestaña 'Licencia de Obras'";
			
			//Recuperamos los parámetros de filtrado
			IItemCollection colParametros = entitiesAPI.getEntities("LICENCIA_OBRAS", numexp);
			if (!colParametros.iterator().hasNext()){
				rulectx.setInfoMessage(msgError);
				return false;
			}
			
			IItem itemDatosLicencia = (IItem)colParametros.iterator().next();
			
			String sPresupuesto = itemDatosLicencia.getString("PRESUPUESTO");
			String sFianza = itemDatosLicencia.getString("FIANZA");
			
			if (StringUtils.isEmpty(sPresupuesto) || StringUtils.isEmpty(sFianza)){
				rulectx.setInfoMessage(msgError);
				return false;
			}
			
			/** AQUÍ EMPIEZA LO QUE DEBERÍA SER EL execute **/
			
			double presupuesto = itemDatosLicencia.getDouble("PRESUPUESTO");
			double fianza = itemDatosLicencia.getDouble("FIANZA");
			double costeTotal = Double.MIN_VALUE;
			
			//Recuperamos los valores de la entidad "Licencias de Obra - Impuestos"
			//Sólo puede haber un expediente abierto de este tipo y los valores no pueden solaparse
			StringBuffer sbQuery = new StringBuffer();
			sbQuery.append("WHERE ((" + presupuesto + " > minimo"); 
			sbQuery.append(" AND " + presupuesto + " <= maximo)");
			sbQuery.append(" OR (" + presupuesto + " > minimo");
			sbQuery.append(" AND maximo is null))");
			sbQuery.append(" AND (('" + freg + "' >= CAST(anio_inicio||'-01-01 00:00:00' AS TIMESTAMP)"); 
			sbQuery.append(" AND '" + freg + "' <= CAST(anio_fin||'-12-31 23:59:59' AS TIMESTAMP))");
			sbQuery.append(" OR ('" + freg + "' >= CAST(anio_inicio||'-01-01 00:00:00' AS TIMESTAMP)");
			sbQuery.append(" AND anio_fin is null))");
			IItemCollection collection = entitiesAPI.queryEntities("LIC_OBRAS_IMPUESTOS", sbQuery.toString());
			
			if (collection.toList().size() == 0){
				rulectx.setInfoMessage("No hay ningún rango de valores definido para el presupuesto y fecha actuales: " +
						presupuesto + " euros, solicitado el día " + FechasUtil.getFormattedDate(freg) + 
						". Revise la pestaña de Impuestos en el expediente 'Licencia de Obras - Administración'");
				return false;
			}
			else if (collection.toList().size() > 1){
				rulectx.setInfoMessage("Hay más de un rango de valores definido para el presupuesto actual, " +
						presupuesto + " euros. Revise la pestaña de Impuestos en el expediente 'Licencia de Obras - Administración'");
				return false;
			}
			
			IItem itemImpuesto = (IItem) collection.iterator().next();
			double porcentaje = itemImpuesto.getDouble("PORCENTAJE");
			
			//Calculamos el impuesto a partir del presupuesto y el porcentaje aplicable
			double impuesto = presupuesto * porcentaje / 100;
			itemDatosLicencia.set("IMPUESTO", impuesto);
			
			//Calculamos el coste total de la licencia, impuesto + fianza
			costeTotal = impuesto + fianza;
			itemDatosLicencia.set("TOTAL", costeTotal);
			
			//Actualizamos los datos en la BBDD
			itemDatosLicencia.store(cct);
			
		}
		catch (Exception e) {
			throw new ISPACRuleException("Error al realizar las validaciones y " +
					"realizar los cáculos en la pestaña Licencia de Obras", e);
		}
		return true;
	}
	
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;	
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
