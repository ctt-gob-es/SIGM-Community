package es.dipucr.sigem.api.rule.procedures.certificadosPersonal;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.ProcedimientosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * [eCenpri-Felipe #632]
 * Regla que se ejecuta al iniciar el trámite de "Notificación del Certificado"
 * @author Felipe
 * @since 19.09.2012
 */
public class PrepararCartaCertPersonalRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(PrepararCartaCertPersonalRule.class);

	
	protected static final String _COD_PCD_CARTA = Constants.CERTPERSONAL._COD_PCD_CARTA;
	protected static final String _DOC_CERTIFICADO = Constants.CERTPERSONAL._DOC_CERTIFICADO;
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Validamos si podemos insertar el tercero
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}
	
	/**
	 * Copiamos el documento del primer trámite
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{

			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			
			//Obtenemos el asunto
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			String asunto = itemExpediente.getString("ASUNTO");
			
			//Generamos el nuevo expediente relacionado
			HashMap<String, String> mapParams = new HashMap<String, String>();
			mapParams.put("nombreDocumento", _DOC_CERTIFICADO);
			Date dFechaSolicitud = itemExpediente.getDate("FREG");
			//INICIO [dipucr-Felipe #1121]
			if (null != dFechaSolicitud){
				mapParams.put("fechaSolicitud", FechasUtil.getFormattedDate(dFechaSolicitud, "d 'de' MMMM 'de' yyyy"));
			}
			else{
				mapParams.put("fechaSolicitud", "[FECHA_SOLICITUD]");
			}
			//FIN [dipucr-Felipe #1121]
			IItemCollection col = entitiesAPI.getEntities("RRHH_CERTPERSONAL", numexp);
			IItem itemCertificado = (IItem)col.iterator().next();
			mapParams.put("nombreCertificado", itemCertificado.getString("DESC_CERTIFICADO"));
			
			String numexpPcd = ProcedimientosUtil.crearPcdRelacionado(rulectx, numexp, _COD_PCD_CARTA, 
					mapParams, asunto, "Certificado/Carta digital", true, null);
			
			//Añadimos el participante al nuevo expediente
			String strNif = itemExpediente.getString("NIFCIFTITULAR");
			ParticipantesUtil.insertarParticipanteByNIF(rulectx, numexpPcd, strNif,
					ParticipantesUtil._TIPO_INTERESADO, ParticipantesUtil._TIPO_PERSONA_FISICA, "");
			
		}
		catch (Exception e) {
			logger.error("Error al crear el documento de notificación del certificado. " +e.getMessage(), e);
			throw new ISPACRuleException("Error al crear el documento de notificación del certificado", e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
