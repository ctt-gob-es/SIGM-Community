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

import es.dipucr.sigem.api.rule.common.avisos.DipucrAvisoFirmanteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;

/**
 * [eCenpri-Felipe ticket #366]
 * Regla que notifica al jefe de departamento de la solicitud de licencias
 * @author Felipe
 * @since 09.05.2011
 */
public class AvisoSolicitudLicenciasRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(AvisoSolicitudLicenciasRule.class);
	
	//--------------------------------------------------------------------------------------------------
    // Constantes
    //--------------------------------------------------------------------------------------------------
	private static final String EMAIL_SUBJECT_VAR_NAME = "LICENCIAS_EMAIL_SUBJECT";
	private static final String EMAIL_CONTENT_VAR_NAME = "LICENCIAS_EMAIL_CONTENT";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Notifica al jefe de departamento de la solicitud de licencias
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
			
			//*********************************************
			//Se notifica al jefe de departamento
			//*********************************************
			//Obtenemos los datos del expediente para obtener NIF y nombre empleado
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			
			//Obtenemos el objeto con los datos de la solicitud
			collection = entitiesAPI.getEntities("RRHH_LICENCIAS", numexp);
			if (collection.toList().size() > 0){
				IItem itemLicencias = (IItem)collection.iterator().next();
				
	    		//Obtenemos el circuito y el paso concreto
	    		int idCircuitoFirma  = rulectx.getInt("ID_CIRCUITO");
				int idPaso = rulectx.getInt("ID_PASO");
	    		IItem signStep = signAPI.getCircuitStep(idCircuitoFirma, idPaso);
	
	    		Map<String, String> variables = LicenciasUtils.getVariablesLicencias(itemLicencias, itemExpediente);
	    		MailUtil.enviarCorreoCircuitoConVariables
	    			(rulectx, signStep, EMAIL_SUBJECT_VAR_NAME, EMAIL_CONTENT_VAR_NAME, variables);
			}
			else{//[dipucr-Felipe 3#693]
				return new DipucrAvisoFirmanteRule().execute(rulectx);
			}
		}
		catch (Exception e) {
			logger.error("Error al notificar al jefe de departamento de la solicitud de licencias. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al notificar al jefe de departamento de la solicitud de licencias. " + e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
