package es.dipucr.sigem.api.rule.common.popUp;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.RuleFactory;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.procedure.PEventoDAO;
import ieci.tdw.ispac.ispaclib.db.DbResultSet;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class DipucrPopUpIniFase implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrPopUpIniFase.class);
	public IClientContext cct;
	public IRuleContext ruleContext;
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext paramIRuleContext)
			throws ISPACRuleException {
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		cct = rulectx.getClientContext();
		ruleContext = rulectx;
		
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		String observacionesFase = "";
		
		try{
			logger.info("INICIO - " + this.getClass().getName());
			
			IClientContext cct = rulectx.getClientContext();
			
			processEvents(EventsDefines.EVENT_OBJ_STAGE, rulectx.getTaskProcedureId(), EventsDefines.EVENT_EXEC_START);
			
			cct.endTX(true);			
		
			StringBuffer consulta = new StringBuffer(" SELECT OBSERVACIONES FROM SPAC_CT_FASES WHERE ");
        	consulta.append(" ID IN (SELECT ID_CTFASE FROM SPAC_P_FASES ");
        	consulta.append(" WHERE ID = " + rulectx.getStageProcedureId() + ")");

        
        	DbResultSet fasesRS = cct.getConnection().executeQuery(consulta.toString());
        	if(fasesRS != null){
        		while (fasesRS.getResultSet().next()){	        		
		        	observacionesFase = fasesRS.getResultSet().getString("OBSERVACIONES");
		        	
		        	if(StringUtils.isNotEmpty(observacionesFase)){
		        		rulectx.setInfoMessage("OBSERVACIONES: " + observacionesFase);
		        	}		
        		}
			}
		}
		catch(Exception e){
			logger.error("Error al recuperar las observaciones de la fase.", e);
			throw new ISPACRuleException("Error al recuperar las observaciones de la fase.", e);
		}
		
		logger.info("FIN - " + this.getClass().getName());
		
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList processEvents(int TypeObj, int nIdObj, int EventCode) throws ISPACException{
		ArrayList rules = getRules(TypeObj, nIdObj, EventCode);
		if (rules.size() <= 0) {
			return null;
		}
		initRules(rules);
		
		if (!(validateRules(rules))){
			throw new ISPACInfo(ruleContext.getInfoMessage());
		}
		
		return executeRules(rules);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getRules(int TypeObj, int nIdObj, int EventCode) throws ISPACException{
		ArrayList rules = new ArrayList();
		RuleFactory rulefactory = new RuleFactory(cct);
		CollectionDAO events = PEventoDAO.getEvents(cct.getConnection(), TypeObj, nIdObj, EventCode);
		
		while (events.next()){
			PEventoDAO event = (PEventoDAO)events.value();
			IRule rule = rulefactory.instanceRule(event);
			logger.info("rule.getClass().getName: " + rule.getClass().getName());
			logger.info("this.getClass().getName: " + this.getClass().getName());
			if (!rule.getClass().getName().equals(this.getClass().getName())){
				logger.info("añadimos al regla");
				rules.add(rule);
			}
		}
		
		return rules;
	}
	
	 @SuppressWarnings("rawtypes")
	public boolean initRules(ArrayList rules) throws ISPACException{
		 Iterator it = rules.iterator();
		 while (it.hasNext()){
			 IRule rule = (IRule)it.next();
			 init(rule);
		 }
		 return true;
	}
	 
	private void init(IRule rule) throws ISPACException{
		try{
			rule.init(ruleContext);
		}
		catch (ISPACRuleException ruleexcep){
			throw ruleexcep;
		}
		catch (Exception e){
			throw new ISPACException("Error ejecutando regla", e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public boolean validateRules(ArrayList rules) throws ISPACException{
		Iterator it=rules.iterator();
		while (it.hasNext()){
			IRule rule=(IRule)it.next();
			if (!validate(rule))
				return false;
		}
		return true;
	}
	
	private boolean validate(IRule rule) throws ISPACException{
		try{
			return rule.validate(ruleContext);
		}
		catch(ISPACRuleException ruleexcep){
			throw ruleexcep;
		}
		catch(Exception e){
			throw new ISPACException("Error ejecutando regla",e);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList executeRules(ArrayList rules)throws ISPACException{
		ArrayList retobjs=new ArrayList();
		
		Iterator it=rules.iterator();
		while (it.hasNext()){
			IRule rule=(IRule)it.next();
			retobjs.add(execute(rule));
		}
		return retobjs;
	}
	
	private Object execute(IRule rule)throws ISPACException{
		try{
			return rule.execute(ruleContext);
		}
		catch(ISPACRuleException ruleexcep){
			throw ruleexcep;
		}
		catch(Exception e){
			throw new ISPACException("Error ejecutando regla",e);
		}
	}
}
