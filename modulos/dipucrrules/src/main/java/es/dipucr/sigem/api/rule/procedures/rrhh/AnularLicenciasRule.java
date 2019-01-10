package es.dipucr.sigem.api.rule.procedures.rrhh;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;


/**
 * [eCenpri-Felipe ticket #366]
 * Regla que realiza la anulación de licencias
 * - Marca la licencia como anulada en la BBDD
 * - Notifica al jefe de departamento de la anulación
 * @author Felipe
 * @since 29.04.2011
 */
public class AnularLicenciasRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(AnularLicenciasRule.class);
	
	//--------------------------------------------------------------------------------------------------
    // Constantes
    //--------------------------------------------------------------------------------------------------
	private static final String EMAIL_SUBJECT_VAR_NAME = "ANULA_LICENCIAS_EMAIL_SUBJECT";
	private static final String EMAIL_CONTENT_VAR_NAME = "ANULA_LICENCIAS_EMAIL_CONTENT";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Marcar la licencia como Anulada en la BBDD y notificación por mail
	 * al jefe de departamento
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ISignAPI signAPI = invesFlowAPI.getSignAPI();
			//*********************************************
			
			IItemCollection collection = null;	
			String numexp = rulectx.getNumExp();
			
			//*********************************************
			//Se notifica al jefe de departamento
			//*********************************************
			//Obtenemos los datos del expediente para obtener NIF y nombre empleado
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			
			//Obtenemos el objeto con los datos de la solicitud
			collection = entitiesAPI.getEntities("RRHH_LICENCIAS", numexp);
			IItem itemLicencias = (IItem)collection.iterator().next();
			
			String strObservaciones = itemLicencias.getString("OBSERVACIONES");
    		String strCodDepartamento = itemLicencias.getString("COD_DEPARTAMENTO");
			
    		//Estos circuitos de firma están configurados en la tabla de validación RRHH_VLDTBL_CIRCUITOS_LICENCIAS
    		int idCircuitoFirma = LicenciasUtils.getIdCircuitoPorDepartamento
    			(entitiesAPI, strCodDepartamento);
    		IItem signStep = signAPI.getCircuitStep(idCircuitoFirma, 1);

    		Map<String, String> variables = LicenciasUtils.getVariablesLicencias(itemLicencias, itemExpediente);
    		MailUtil.enviarCorreoCircuitoConVariables
    			(rulectx, signStep, EMAIL_SUBJECT_VAR_NAME, EMAIL_CONTENT_VAR_NAME, variables);
			
			//*********************************************
			//Respuesta al portal del empleado -> Se actualiza el estado
			//*********************************************
    		String strIdSolicitud = itemLicencias.getString("ID_SOLICITUD");
    		LicenciasWSDispatcher.ponerLicenciaAnulada(cct, strIdSolicitud, strObservaciones);
			
			//Cerramos el trámite y el expediente
			ExpedientesUtil.cerrarExpediente(cct, numexp);
		}
		catch (Exception e) {
			logger.error("Error al marcar la licencia como anulada o al notificar al jefe de departamento. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al marcar la licencia como anulada o al notificar al jefe de departamento. " + e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
