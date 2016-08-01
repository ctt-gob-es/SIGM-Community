package es.dipucr.sigem.api.rule.procedures.recaudacion.aytos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.participantes.DipucrInsertarParticipantesTrasladoRule;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrBenefIBIInsertaparticipanteAyuntamientoRule extends DipucrInsertarParticipantesTrasladoRule{
private static final Logger logger = Logger.getLogger(DipucrBenefIBIInsertaparticipanteAyuntamientoRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();	        
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();	
			
			//Obtenemos los datos del ayuntamiento de otro expediente en el que haya participado
	        
			String strQuery = "WHERE NUMEXP ='"+rulectx.getNumExp()+"'";

	    	IItemCollection datos = entitiesAPI.queryEntities("REC_BENEFICIO_IBI", strQuery);
	    	
	    	if(datos.iterator().hasNext()){
	    		idTraslado = ((IItem)datos.iterator().next()).getString("MUN_1");
	    	}
	    	else idTraslado = "";
		
			email = "";		
			if(idTraslado == null || idTraslado.equals("")) return false;
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		try {
			if(StringUtils.isNotEmpty(idTraslado))
				ParticipantesUtil.insertarParticipanteById(rulectx, rulectx.getNumExp(), idTraslado, ParticipantesUtil._TIPO_INTERESADO, ParticipantesUtil._TIPO_PERSONA_JURIDICA, email);
		} catch (ISPACException e) {		
			logger.error(e.getMessage(), e);
		}
		logger.info("FIN -" + this.getClass().getName());
		return new Boolean(true);
	}
}