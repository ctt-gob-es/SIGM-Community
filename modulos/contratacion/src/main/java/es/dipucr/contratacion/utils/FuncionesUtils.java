package es.dipucr.contratacion.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class FuncionesUtils {
	
	private static final Logger logger = Logger.getLogger(FuncionesUtils.class);
	
	@SuppressWarnings("unchecked")
	public static String getNumExpProcContrat_PetContrat (IRuleContext rulectx) throws ISPACRuleException{
		String numexp = "";
		try{			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			/***********************************************************************/
		
			String consultaSQL = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp()+ "' AND RELACION='Petición Contrato'";
	        IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
			Iterator<IItem> itExp = itemCollection.iterator();
			while(itExp.hasNext()){
				IItem itSesion = itExp.next();
				if(itSesion.getString("NUMEXP_HIJO")!=null) numexp = itSesion.getString("NUMEXP_HIJO");
			}
		}catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return numexp;
	}

}
