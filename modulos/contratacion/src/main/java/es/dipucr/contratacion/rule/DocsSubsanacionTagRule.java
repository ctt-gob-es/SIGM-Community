package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

public class DocsSubsanacionTagRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
	        StringBuffer listDocumentos = new StringBuffer("");  //Listado de documentos a subsanar 
	        
	        String consulta = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE LIKE '%Subsanación%'";
	        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, consulta);
	        
	        Iterator<IItem> it = collection.iterator();
	        
	        while (it.hasNext())
	        {
	        	String documento = ((IItem)it.next()).getString("DESCRIPCION");
	        	if (listDocumentos.length() > 0) listDocumentos.append("\n\n");
	        	if(documento != null){
	        		listDocumentos.append("- " + documento);
	        	}
	        }

	        return listDocumentos.toString();
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error confecionando listado de documentos a subsanar.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
