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

public class CargaTipoPresentacionOferManual implements IRule{
	
	public static final Logger logger = Logger.getLogger(CargaTipoPresentacionOferManual.class);
	public static String tipoPresentacionOferta = "2 - Manual";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			IItemCollection contratacion_datos_licCollection = entitiesAPI.getEntities("CONTRATACION_DATOS_LIC", rulectx.getNumExp());
			@SuppressWarnings("unchecked")
			Iterator <IItem> contratacion_datos_licIterator = contratacion_datos_licCollection.iterator();
			if(contratacion_datos_licIterator.hasNext()){
				IItem contratacion_datos_lic = contratacion_datos_licIterator.next();
				contratacion_datos_lic.set("PRESENT_OFERTA", tipoPresentacionOferta);
				contratacion_datos_lic.store(cct);
			}
			else{
				IItem contratacion_datos_lic = entitiesAPI.createEntity("CONTRATACION_DATOS_LIC","");
				contratacion_datos_lic.set("NUMEXP", rulectx.getNumExp());
				contratacion_datos_lic.set("PRESENT_OFERTA", tipoPresentacionOferta);
				contratacion_datos_lic.store(cct);
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
