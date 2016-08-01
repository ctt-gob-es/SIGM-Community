package es.dipucr.sigem.api.rule.common.participantes;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class DipucrInsertarParticipantesFromDatosEspecificosRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrInsertarParticipantesFromDatosEspecificosRule.class);
	
	public final static String PARTICIPANTE_SEPARATOR = "::";
	public final static String DATOS_SEPARATOR = "#";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		String error = "";
		IClientContext cct = rulectx.getClientContext();
		
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			String otrosDatos = TramitesUtil.getDatosEspecificosOtrosDatos(cct, rulectx.getTaskProcedureId());
			
			if (!StringUtils.isEmpty(otrosDatos)){
				List<String> listParticipantes = Arrays.asList(otrosDatos.split(PARTICIPANTE_SEPARATOR));
				String tipoPersona = null;
				String id = null;
				String tipoRelacion = null;
				String email = null;
				
				for (String participante : listParticipantes){
					String[] arrParticipante = participante.split(DATOS_SEPARATOR);
					if (arrParticipante.length == 4){
						tipoPersona = arrParticipante[0].trim();
						if (!ParticipantesUtil._TIPO_PERSONA_FISICA.equals(tipoPersona) && 
							!ParticipantesUtil._TIPO_PERSONA_JURIDICA.equals(tipoPersona)){
							error = "El tipo de persona debe ser P o J : TIPO_PERSONA;ID;TIPO_RELACION;EMAIL";
							logger.error(error);
							throw new ISPACRuleException(error);
						}
						id = arrParticipante[1].trim();
						tipoRelacion = arrParticipante[2].trim();
						String strQuery = "WHERE VALOR='" + tipoRelacion + "'";
						IItemCollection colTiposRelacion = entitiesAPI.queryEntities("SPAC_TBL_002", strQuery);
						if (colTiposRelacion.toList().size() == 0){
							error = "El código de tipo de relación '" + tipoRelacion + "' no forma parte de los "
									+ "tipos de relación establecidos en la tabla SPAC_TBL_002 : TIPO_PERSONA;ID;TIPO_RELACION;EMAIL"; 
							logger.error(error);
							throw new ISPACRuleException(error);
						}
						
						email = arrParticipante[3].trim();
						
						ParticipantesUtil.insertarParticipanteById(rulectx, rulectx.getNumExp(), 
								id, tipoRelacion, tipoPersona, email);
					}
					else{
						error = "El participante " + participante + " no cumple con el formato establecido: "
								+ "TIPO_PERSONA;ID;TIPO_RELACION;EMAIL";
						logger.error(error);
						throw new ISPACRuleException(error);
					}
				}
			}
			else{
				logger.warn("No existen datos específicos de participantes para el trámite");
			}
		}
		catch (Exception ex){
			error = "Error al recuperar los participantes desde la pestaña de datos específicos";
			logger.error(error, ex);
			throw new ISPACRuleException(error, ex);
		}
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return false;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}