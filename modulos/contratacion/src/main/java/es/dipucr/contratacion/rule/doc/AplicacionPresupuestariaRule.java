package es.dipucr.contratacion.rule.doc;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.sql.ResultSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class AplicacionPresupuestariaRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(AplicacionPresupuestariaRule.class);
	
	
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("unchecked")
	public String execute(IRuleContext rulectx) throws ISPACRuleException{
    	StringBuffer valor = new StringBuffer("");
        try{
 		//--------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //-----------------------------------------------------------------------------

	        
	        String consulta = "WHERE NUMEXP = '"+rulectx.getNumExp()+"'";
	        logger.warn("consulta "+consulta);
			IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_LIC", consulta);
	        Iterator<IItem> it = collection.iterator();
	        
	        if (it.hasNext()){
	        	IItem contrato = (IItem)it.next();
	        	int id = contrato.getInt("ID");
	        	
	        	consulta="SELECT VALUE FROM CONTRATACION_DATOS_LIC_S WHERE REG_ID = "+id+" AND FIELD = 'APLICAPRESUP'";
	        	logger.warn("consulta "+consulta);
		        ResultSet datos = cct.getConnection().executeQuery(consulta).getResultSet();
		        String value = "";
		        if(datos!=null)
		      	{
		        	while(datos.next()){
		          		if (datos.getString("VALUE")!=null) value = datos.getString("VALUE"); else value="";
		          		logger.warn("value "+value);
		          		valor.append(value+";\n");

		          	}
		      	}
		        
	        }

	        
            
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la fecha actual",e);
        }
        logger.warn("valor.toString() "+valor.toString());
		return valor.toString();
    }


	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
}


