package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class ComprobarUsuarioCompareceRule extends VerificarUsuarioComparece {
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger("loggerRegistro");

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {		

        try {
        	        	
        	//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	      //Obtengo los participantes del expediente.
			logger.warn("INICIO");
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(),"ROL='INT'","ID");
			lParticipantes = participantes.toList();
			
			logger.warn("FIN");			
        } catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
        
		return new Boolean(true);
	}


}
