package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;

/**
 * [eCenpri-Felipe ticket #346]
 * Regla que notifica a los firmantes indicándoles que hay una solicitud
 * de anticipo reintegrable pendiente de firma
 * @author Felipe
 * @since 29.06.2011
 */
public class AvisoSolicitudAnticiposRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(AvisoSolicitudAnticiposRule.class);
	
	//--------------------------------------------------------------------------------------------------
    // Constantes
    //--------------------------------------------------------------------------------------------------
	private static final String EMAIL_SUBJECT_VAR_NAME = "ANTICIPOS_EMAIL_SUBJECT";
	private static final String EMAIL_CONTENT_VAR_NAME = "ANTICIPOS_EMAIL_CONTENT";
	
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
			collection = entitiesAPI.getEntities("DPCR_ANTICIPOS", numexp);
			IItem itemAnticipos = (IItem)collection.iterator().next();
			
    		//Obtenemos el circuito y el paso concreto
    		int idCircuitoFirma  = rulectx.getInt("ID_CIRCUITO");
			int idPaso = rulectx.getInt("ID_PASO");
    		IItem signStep = signAPI.getCircuitStep(idCircuitoFirma, idPaso);

    		Map<String, String> variables = getMappingVariables(itemAnticipos, itemExpediente);
    		MailUtil.enviarCorreoCircuitoConVariables
    			(rulectx, signStep, EMAIL_SUBJECT_VAR_NAME, EMAIL_CONTENT_VAR_NAME, variables);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException
				("Error al notificar al jefe de departamento de la solicitud de licencias. " + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Devuelve el mapping de variables de la solicitud de anticipos para 
	 * sustituirlas en el texto del correo electrónico
	 * @param itemAnticipos
	 * @param itemExpediente
	 * @return
	 * @throws ISPACException
	 */
	private Map<String, String> getMappingVariables(IItem itemAnticipos, IItem itemExpediente)
			throws ISPACException
	{
		Map<String,String> variables = new HashMap<String,String>();
		variables.put("EMPLEADO_NIF", itemExpediente.getString("NIFCIFTITULAR"));
		variables.put("EMPLEADO_NOMBRE", itemExpediente.getString("IDENTIDADTITULAR"));
		variables.put("DEPARTAMENTO", itemAnticipos.getString("DEPARTAMENTO"));
		variables.put("TIPO_CONTRATO", itemAnticipos.getString("TIPO_CONTRATO"));
		variables.put("IMPORTE_TOTAL", itemAnticipos.getString("IMPORTE_TOTAL"));
		variables.put("NUM_MESES", itemAnticipos.getString("NUM_MESES"));
		variables.put("IMPORTE_MES", itemAnticipos.getString("IMPORTE_MES"));
		variables.put("IMPORTE_ULTIMO_MES", itemAnticipos.getString("IMPORTE_ULTIMO_MES"));
		return variables;
	}
	

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
