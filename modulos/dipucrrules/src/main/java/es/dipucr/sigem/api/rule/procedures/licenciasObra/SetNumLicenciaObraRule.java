package es.dipucr.sigem.api.rule.procedures.licenciasObra;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;


/**
 * [eCenpri-Felipe ticket #1039]
 * Regla que asigna un nuevo número de licencia de obra iniciar el trámite
 * de Generación del Título de Licencia
 * @author Felipe
 * @since 23.01.14
 */
public class SetNumLicenciaObraRule implements IRule 
{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Asignación del número de licencia
	 * Ejecutamos el código del execute en el método validate
	 * para poder mostrar los errores.
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		String numexp = null;
		IClientContext cct = rulectx.getClientContext();
		IEntitiesAPI entitiesAPI = null;
		
		try{
			entitiesAPI = cct.getAPI().getEntitiesAPI();
			numexp = rulectx.getNumExp();
			
			IItemCollection collection = entitiesAPI.getEntities("LICENCIA_OBRAS", numexp);
			if(collection.toList().size() == 0) {
				rulectx.setInfoMessage("El expediente de licencia de obra no tiene datos de" +
						" licencia asignados en la pestaña 'Licencia de Obras'");
				return false;
			}
			else if (collection.toList().size() == 1){
			
				IItem itemDatosLicencia = (IItem) collection.iterator().next();
				
				String sNumLicenciaOld = itemDatosLicencia.getString("NUM_LICENCIA");
				
				if (StringUtils.isEmpty(sNumLicenciaOld)){
				//Obtenemos el nuevo número de licencia
					int[] arrNumLicencia = getNumLicencia(entitiesAPI);
					itemDatosLicencia.set("ANIO_LICENCIA", arrNumLicencia[0]);
					itemDatosLicencia.set("NUM_LICENCIA", arrNumLicencia[1]);
					itemDatosLicencia.store(cct);
				}
//				else{
//					rulectx.setInfoMessage("La licencia de obra ya tiene número de licencia asignado.");
//					return false;
//				}
			}
			else if(collection.toList().size() > 1) {
				rulectx.setInfoMessage("El expediente de licencia de obra relacionado tiene " +
						"duplicados los datos de licencia en la BBDD. Consulte con el administrador.");
				return false;
			}
			
		}
		catch (Exception e) {
			throw new ISPACRuleException("Error al asignar número de licencia", e);
		}
		return true;
	}
	
	/**
	 * Método que genera un nuevo número de licencia a partir del último número
	 * de licencia encontrado en la BBDD
	 * @param entitiesAPI
	 * @return
	 * @throws ISPACException
	 */
	protected int[] getNumLicencia(IEntitiesAPI entitiesAPI) throws ISPACException{
		
		//Vector resultado
		int[] arrNumLicencia = new int[2];
		
		//Obtenemos el año actual
		int anioActual = FechasUtil.getAnyoActual();
		
		//Obtenemos los números de licencias asignados este año
		String query = "WHERE ANIO_LICENCIA = " + anioActual + "ORDER BY NUM_LICENCIA DESC";
		IItemCollection collection = entitiesAPI.queryEntities("LICENCIA_OBRAS", query);

		arrNumLicencia[0] = anioActual;
		if (collection.toList().size() == 0){
			arrNumLicencia[1] = 1;
		}
		else{
			IItem itemUltimaLicencia = (IItem) collection.iterator().next();
			arrNumLicencia[1] = itemUltimaLicencia.getInt("NUM_LICENCIA") + 1;
		}
		
		return arrNumLicencia;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return null;
	}
	
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
