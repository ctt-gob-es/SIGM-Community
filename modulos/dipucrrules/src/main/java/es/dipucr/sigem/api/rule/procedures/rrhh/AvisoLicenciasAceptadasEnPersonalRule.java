package es.dipucr.sigem.api.rule.procedures.rrhh;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;


/**
 * [eCenpri-Felipe ticket #549]
 * Regla que notifica al jefe de departamento de la aceptación
 * de licencias disintas a 05 y 07 por parte del departamento de personal
 * @author Felipe
 * @since 11.01.2012
 */
public class AvisoLicenciasAceptadasEnPersonalRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(AnularLicenciasRule.class);
	
	//--------------------------------------------------------------------------------------------------
    // Constantes
    //--------------------------------------------------------------------------------------------------
	private static final String EMAIL_SUBJECT_VAR_NAME = "LICENCIAS_ACEPTADAS_EMAIL_SUBJECT";
	private static final String EMAIL_CONTENT_VAR_NAME = "LICENCIAS_ACEPTADAS_EMAIL_CONTENT";
	
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
			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			String numexp = DocumentosUtil.getNumExp(entitiesAPI, idDoc);
			
			//INICIO [dipucr-Felipe #197]
			//Sólo si la licencia esta firmada, en los rechazos no enviaremos el correo
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
			String estado = itemDocumento.getString("ESTADOFIRMA");
			
			if (estado.equals(SignStatesConstants.FIRMADO) || 
				estado.equals(SignStatesConstants.FIRMADO_CON_REPAROS))
			{//FIN [dipucr-Felipe #197]
				//*********************************************
				//Se notifica al jefe de departamento
				//*********************************************
				//Obtenemos los datos del expediente para obtener NIF y nombre empleado
				IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
				
				//Obtenemos el objeto con los datos de la solicitud
				collection = entitiesAPI.getEntities("RRHH_LICENCIAS", numexp);
				IItem itemLicencias = (IItem)collection.iterator().next();
	
	    		String strCodDepartamento = itemLicencias.getString("COD_DEPARTAMENTO");
				
	    		//Estos circuitos de firma están configurados en la tabla de validación RRHH_VLDTBL_CIRCUITOS_LICENCIAS
	    		int idCircuitoFirma = LicenciasUtils.getIdCircuitoPorDepartamento
	    			(entitiesAPI, strCodDepartamento);
	    		IItem signStep = signAPI.getCircuitStep(idCircuitoFirma, 1);
	
	    		Map<String, String> variables = LicenciasUtils.getVariablesLicencias(itemLicencias, itemExpediente);
	    		MailUtil.enviarCorreoCircuitoConVariables
	    			(rulectx, signStep, EMAIL_SUBJECT_VAR_NAME, EMAIL_CONTENT_VAR_NAME, variables);
			}
			
		}
		catch (Exception e) {
			logger.error("Error al notificar al jefe de departamento de la licencia. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al notificar al jefe de departamento de la licencia. " + e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
