package es.dipucr.sigem.api.rule.procedures.rrhh;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LicenciasUtils {

	/**
	 * Recupera el circuito de firma definido para un código de departamento en
	 * la tabla RRHH_VLDTBL_CTOS_LICENCIAS
	 * @param entitiesAPI
	 * @param strCodDepartamento
	 * @return
	 * @throws ISPACRuleException
	 */
	public static int getIdCircuitoPorDepartamento(IEntitiesAPI entitiesAPI, String strCodDepartamento)
			throws ISPACRuleException
	{
		//Estos circuitos de firma están configurados en la tabla de validación RRHH_VLDTBL_CIRCUITOS_LICENCIAS
		int idCircuitoFirma = Integer.MIN_VALUE;
		
		try{
			//Recuperamos de la tabla de validación el circuito asignado
			String strQuery = "WHERE VALOR = '" + strCodDepartamento + "'";
			IItemCollection collection = entitiesAPI.queryEntities("RRHH_VLDTBL_CTOS_LICENCIAS", strQuery);
			List listCircuitos = collection.toList();
			
			if (listCircuitos.size() == 0){
				throw new ISPACRuleException("No hay ningún circuito añadido en la tabla de validación " +
						"RRHH_VLDTBL_CIRCUITOS_LICENCIAS para el Departamento(VALOR) " + strCodDepartamento);
			}
			else if (listCircuitos.size() > 1){
				throw new ISPACRuleException("Sa ha definido más de un circuito de firma en la tabla de validación " +
						"RRHH_VLDTBL_CIRCUITOS_LICENCIAS para el Departamento(VALOR) " + strCodDepartamento);
			}
			else if (listCircuitos.size() == 1){
				IItem itemCircuito = (IItem) listCircuitos.get(0);
				idCircuitoFirma = Integer.valueOf(itemCircuito.getString("SUSTITUTO"));
			}
		}
		catch (Exception e) {
			throw new ISPACRuleException("Error:", e);
		}
		return idCircuitoFirma;
	}
	
	/**
	 * Devuelve el hash de variables de la solicitud o anulación para sustituirlas en el
	 * texto del correo electrónico
	 * @param itemLicencias
	 * @param itemExpediente
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> getVariablesLicencias(IItem itemLicencias, IItem itemExpediente) throws Exception {
		
		Map<String,String> variables = new HashMap<String,String>();
		variables.put("EMPLEADO_NIF", itemExpediente.getString("NIFCIFTITULAR"));
		variables.put("EMPLEADO_NOMBRE", itemExpediente.getString("IDENTIDADTITULAR"));
		variables.put("ID_SOLICITUD", itemLicencias.getString("ID_SOLICITUD"));
		variables.put("COD_LICENCIA", itemLicencias.getString("COD_LICENCIA"));
		variables.put("LICENCIA", itemLicencias.getString("LICENCIA"));
		variables.put("FECHA_INICIO", itemLicencias.getString("FECHA_INICIO"));
		variables.put("FECHA_FIN", itemLicencias.getString("FECHA_FIN"));
		variables.put("NUM_DIAS", itemLicencias.getString("NUM_DIAS"));
		variables.put("FECHAS_SOLICITADAS", itemLicencias.getString("FECHAS_SOLICITADAS"));
		return variables;	
	}
	
}
