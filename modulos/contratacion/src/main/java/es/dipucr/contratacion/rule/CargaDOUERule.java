package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class CargaDOUERule implements IRule{
	
	public static final Logger logger = Logger.getLogger(CargaDOUERule.class);
	public static String doue = "NO";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			IItemCollection contratacion_doue = entitiesAPI.getEntities("CONTRATACION_DOUE", rulectx.getNumExp());
			@SuppressWarnings("unchecked")
			Iterator <IItem> contratacion_datos_licIterator = contratacion_doue.iterator();
			if(contratacion_datos_licIterator.hasNext()){
				IItem contratacion_douei = contratacion_datos_licIterator.next();
				contratacion_douei.set("PUB_ANUNCIO", doue);
				contratacion_douei.store(cct);
			}
			else{
				IItem contratacion_douei = entitiesAPI.createEntity("CONTRATACION_DOUE","");
				contratacion_douei.set("NUMEXP", rulectx.getNumExp());
				contratacion_douei.set("PUB_ANUNCIO", doue);
				contratacion_douei.store(cct);
			}
			

		
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
