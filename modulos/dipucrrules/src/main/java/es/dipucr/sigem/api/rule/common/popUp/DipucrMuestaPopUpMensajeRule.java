package es.dipucr.sigem.api.rule.common.popUp;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.RuleFactory;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.procedure.PEventoDAO;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

public abstract class DipucrMuestaPopUpMensajeRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrMuestaPopUpMensajeRule.class);
	public IClientContext cct;
	public IRuleContext ruleContext;
	
	public String mensaje = "";	
	
	
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext paramIRuleContext)
			throws ISPACRuleException {
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
				
		try{
			//----------------------------------------------------------------------------------------------
	        cct = (ClientContext) rulectx.getClientContext();
	        ruleContext = rulectx;
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
			logger.info("INICIO - " + this.getClass().getName());

			cct.endTX(true);
			cct.beginTX();
				
			boolean esTramite = false;
			
			esTramite = StringUtils.isNotEmpty(""+rulectx.getTaskId());
			
			if(esTramite){
				//Actualizamos el campo resposable que falta
				IItemCollection tramCollection = entitiesAPI.queryEntities("SPAC_TRAMITES", "WHERE ID = '" + rulectx.getTaskId() + "'");
				Iterator tramIterator = tramCollection.iterator();
				while (tramIterator.hasNext()){
					IItem tram = (IItem) tramIterator.next();
					
					IItem stage = invesFlowAPI.getStage(rulectx.getStageId());
					IItem process = invesFlowAPI.getProcedure(rulectx.getProcedureId());				
					String taskRespId = invesFlowAPI.getTask(rulectx.getTaskId()).getString("ID_RESP");
					
					taskRespId = setDefaultResp(taskRespId,stage.getString("ID_RESP"));
					taskRespId = setDefaultResp(taskRespId,process.getString("ID_RESP"));
					taskRespId = setDefaultResp(taskRespId,cct.getRespId());
	
					tram.set("ID_RESP", taskRespId);
					
					tram.set("ID_TRAMITE_BPM", rulectx.getTaskId());
					tram.store(cct);
				}
			}
		
			processEvents(EventsDefines.EVENT_OBJ_TASK, rulectx.getTaskProcedureId(), EventsDefines.EVENT_EXEC_START);
			
			cct.endTX(true);			

			if(StringUtils.isNotEmpty(getMensaje())){
				rulectx.setInfoMessage(getMensaje());
			}
		}
		catch(Exception e){
			logger.error("Error al mostrar el mensaje.", e);
			throw new ISPACRuleException("Error al recuperar las observaciones del trámite.", e);
		}
		
		logger.info("FIN - " + this.getClass().getName());
		
		return false;
	}
	
	private static String setDefaultResp(String objectResp ,String sRespId)	throws ISPACException{
		if (objectResp == null || objectResp.length()==0 ){
			objectResp = sRespId;
		}
		return objectResp;
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

	public String getMensaje() {
		return mensaje;
	}
}
