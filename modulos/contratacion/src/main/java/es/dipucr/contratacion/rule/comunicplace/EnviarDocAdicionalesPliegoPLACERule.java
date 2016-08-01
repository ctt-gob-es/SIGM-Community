package es.dipucr.contratacion.rule.comunicplace;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.common.DipucrFuncionesComunes;

public class EnviarDocAdicionalesPliegoPLACERule implements IRule{
	
	public static final Logger logger = Logger.getLogger(EnviarDocAdicionalesPliegoPLACERule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("unchecked")
	public Boolean execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			//Compruebo que no se haya mandado antes el anuncio.
//			IItemCollection colAdjudicacion = entitiesAPI.getEntities("CONTRATACION_ADJUDICACION", rulectx.getNumExp());
//			Iterator <IItem> iterAdjudicacion = colAdjudicacion.iterator();
//			if(iterAdjudicacion.hasNext()){
//				IItem itemAdjudicacion = iterAdjudicacion.next();				
//				if(itemAdjudicacion.getString("CONTRATACION_ADJUDICACION")!=null && itemAdjudicacion.getString("CONTRATACION_ADJUDICACION").equals("NO")){		
						DipucrFuncionesComunes.envioDocumentosAdicionales(rulectx);
//				}
//			}
		}
		catch(ISPACRuleException e){
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
		} catch (ISPACException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
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
