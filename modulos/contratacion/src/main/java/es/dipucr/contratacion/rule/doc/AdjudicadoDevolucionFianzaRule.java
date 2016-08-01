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

public class AdjudicadoDevolucionFianzaRule  implements IRule{

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

	        String strProperty = rulectx.get("property");
	        
	        
	      //Obtengo el numexp del procedimiento de Petición de contratación
	        String sqlQueryPart = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"'";
	        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        String numexpContratacion = "";
	        Iterator<IItem> itExpRel = exp_relacionados.iterator();
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpContratacion = itemExpRel.getString("NUMEXP_PADRE");
	        }
	        
	        	
        	
        	String strQuery = "WHERE NUMEXP='" + numexpContratacion + "'";
        	IItemCollection collectionTramit = entitiesAPI.queryEntities("CONTRATACION_DATOS_TRAMIT", strQuery);
			Iterator<IItem> itTra = collectionTramit.iterator();
			if (itTra.hasNext()) {
				IItem itemDatosContrato = itTra.next();
        		if(strProperty.equals("EMPRESA")){
        			valor = itemDatosContrato.getString("EMP_ADJ_CONT");
        		}
        		if(strProperty.equals("PRECIO")){
        			valor = itemDatosContrato.getString("IMP_ADJ_SINIVA");
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


