package es.dipucr.sigem.api.rule.common.responsables;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;

public class DipucrPonRespProcATramite implements IRule {
	private static final Logger logger = Logger.getLogger(DipucrPonRespProcATramite.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - "+this.getClass().getName());
		try {
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);//TODO
			String numexp = itemDocumento.getString("NUMEXP");
			
			IResponsible responsable = ResponsablesUtil.getRespProcedimientoByNumexp(rulectx, numexp);
			int id_tramite = itemDocumento.getInt("ID_TRAMITE");			

			ResponsablesUtil.setRespTramite(rulectx, id_tramite, responsable);
		} catch (ISPACException e) {
			logger.error("ERROR al asignar el responsable de trámite. " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR al asignar el responsable de trámite. " + e.getMessage(), e);
		}
		logger.info("FIN - "+this.getClass().getName());
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}