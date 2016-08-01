package es.dipucr.contratacion.rule.doc;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

public class ValoresCodiceRule implements IRule{
	
	
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	String valor = "";
        try{
 		//--------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //-----------------------------------------------------------------------------

//	        String strDate = new String();
	        String strEntity = rulectx.get("entity");
	        String strProperty = rulectx.get("property");
	        
	        String consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
			IItemCollection collection = entitiesAPI.queryEntities(strEntity, consulta);
	        Iterator<IItem> it = collection.iterator();
	        String tipo = "";
	        
	        while (it.hasNext()){
	        	IItem contrato = (IItem)it.next();
	        	tipo= contrato.getString(strProperty);
	        }

	        if(tipo!=""){
	        	String [] v_cod_tipo_contrato = tipo.split(" - ");
	        	if(v_cod_tipo_contrato.length > 0){
	        		valor = v_cod_tipo_contrato[1];
	        	}
	        	
	        }
	        
            
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la fecha actual",e);
        }
		return valor;
    }


	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
}


