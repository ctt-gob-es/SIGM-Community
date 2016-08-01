package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.webempleado.services.ayudasSociales.AnticiposWSProxy;


/**
 * [eCenpri-Felipe ticket #346]
 * Regla para enviar la respuesta de la firma al portal del empleado
 * y cerrar tanto el trámite como el expediente
 * Se genera tambien el decreto relacionado
 * @author Felipe
 * @since 30.06.2011
 */
public class RespuestaSolicitudAnticiposRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(RespuestaSolicitudAnticiposRule.class);
	
	protected static final String _TRAMITE_FIRMAS = "Firmar anticipos"; //TODO Constants
	protected static final String _DOC_SOLICITUD = "Anticipo reintegrable";
	protected static final String _EXP_DECRETO_NOMINAS = "Decretos Nominas";
	protected static final String _ASUNTO_DECRETO = "Solicitud Anticipo Reintegrable Empleado Público";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación de la fase, el trámite y envío al Jefe de departamento para firma
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			//*********************************************
			//Respuesta al portal del empleado -> Se actualiza el estado
			//*********************************************
			//Vemos si la solicitud ha sido firmada o rechazada
			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
			String estado = itemDocumento.getString("ESTADOFIRMA");
			String strMotivo = null;
			boolean bFirmado = false;
			
			if (estado.equals(SignStatesConstants.FIRMADO)){
				bFirmado = true;
			}
			else if (estado.equals(SignStatesConstants.FIRMADO_CON_REPAROS)){
				bFirmado = true;
				strMotivo = itemDocumento.getString("MOTIVO_REPARO");
			}
			else if (estado.equals(SignStatesConstants.RECHAZADO)){
				bFirmado = false;
				strMotivo = itemDocumento.getString("MOTIVO_RECHAZO");
			}
			
			//Desglosamos el NIF del Interesado principal
			String numexp = DocumentosUtil.getNumExp(entitiesAPI, idDoc);
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			String strNif = itemExpediente.getString("NIFCIFTITULAR");
			
			//Hacemos la petición al servicio web
			AnticiposWSProxy wsAnticipos = new AnticiposWSProxy();
			wsAnticipos.ponerAnticipoValidado(strNif, bFirmado, strMotivo);
			
			//Creación del expediente de decreto
			//Sólo si el anticipo ha sido firmado y no rechazado
			if (bFirmado){
				String numexpDecreto = DecretosUtil.crearDecretoRelacionado(rulectx, numexp, _EXP_DECRETO_NOMINAS, 
						_DOC_SOLICITUD, _ASUNTO_DECRETO, _ASUNTO_DECRETO, "Anticipo reintegrable/Decreto");
				
				//INICIO [Felipe #444] Insertar los participantes al expediente
				ParticipantesUtil.insertarParticipanteByCodigo(rulectx, numexpDecreto, "TESORERIA",
						ParticipantesUtil._TIPO_TRASLADO, ParticipantesUtil._TIPO_PERSONA_FISICA);
				
				ParticipantesUtil.insertarParticipanteByCodigo(rulectx, numexpDecreto, "INTERVENCION",
						ParticipantesUtil._TIPO_TRASLADO, ParticipantesUtil._TIPO_PERSONA_FISICA);
				
				ParticipantesUtil.insertarParticipanteByNIF(rulectx, numexpDecreto, strNif,
						ParticipantesUtil._TIPO_INTERESADO, ParticipantesUtil._TIPO_PERSONA_FISICA, "");
				//FIN [Felipe #444]
			}
			else{
				//TODO: Si se rechaza tambien podríamos crear un decreto negando el anticipo
				//Preguntar a luis de juan
			}
			
			//Cerramos el trámite y el expediente
			ExpedientesUtil.cerrarExpediente(cct, numexp);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación de los trámites y envío a firma de los anticipos. " + e.getMessage(), e);
		}
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
