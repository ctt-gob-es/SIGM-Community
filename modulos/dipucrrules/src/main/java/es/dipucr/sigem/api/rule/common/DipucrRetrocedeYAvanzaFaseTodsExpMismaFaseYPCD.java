package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrRetrocedeYAvanzaFaseTodsExpMismaFaseYPCD implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrRetrocedeYAvanzaFaseTodsExpMismaFaseYPCD.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		logger.info("INICIO - DipucrRetrocedeYAvanzaFaseTodsExpMismaFaseYPCD");
		String numexp = "";
		
		int idFase_pcd = Integer.MIN_VALUE;
		
		try{
			numexp = rulectx.getNumExp();
			
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			idFase_pcd = rulectx.getStageProcedureId();
			
			IItemCollection fasesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, "WHERE NUMEXP !='" + numexp + "' AND ID_FASE = " + idFase_pcd);
			Iterator<?> fasesIterator = fasesCollection.iterator();
			while(fasesIterator.hasNext()){
				IItem fase = (IItem) fasesIterator.next();
				String numexp_cerrar = fase.getString("NUMEXP");
				ExpedientesUtil.retrocederFase(cct, numexp_cerrar);
				ExpedientesUtil.avanzarFase(cct, numexp_cerrar);
			}
		}
		catch(ISPACRuleException e){
			logger.error("ERROR al generar el trámite del foliado o al avanzar fase del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR al generar el trámite del foliado o al avanzar fase del expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("ERROR al generar el trámite del foliado o al avanzar fase del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR al generar el trámite del foliado o al avanzar fase del expediente: " + numexp + ". " + e.getMessage(), e);		
		}
		logger.info("FIN - DipucrRetrocedeYAvanzaFaseTodsExpMismaFaseYPCD");
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
