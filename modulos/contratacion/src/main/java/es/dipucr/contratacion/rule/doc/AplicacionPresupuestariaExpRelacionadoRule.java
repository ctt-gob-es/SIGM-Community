package es.dipucr.contratacion.rule.doc;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.sql.ResultSet;
import java.util.Iterator;

public class AplicacionPresupuestariaExpRelacionadoRule implements IRule{
	
	
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
	        
	      //Busqueda del expediente de contratación
	        String numexpContratacion = expPadreContratacion(rulectx);

	        
	        String consulta = "WHERE NUMEXP = '"+numexpContratacion+"'";
			IItemCollection collection = entitiesAPI.queryEntities("CONTRATACION_DATOS_LIC", consulta);
	        Iterator<IItem> it = collection.iterator();
	        
	        if (it.hasNext()){
	        	IItem contrato = (IItem)it.next();
	        	int id = contrato.getInt("ID");
	        	
	        	consulta="SELECT VALUE FROM CONTRATACION_DATOS_LIC_S WHERE REG_ID = "+id+" AND FIELD = 'APLICAPRESUP'";
		        ResultSet datos = cct.getConnection().executeQuery(consulta).getResultSet();
		        String value = "";
		        if(datos!=null)
		      	{
		        	while(datos.next()){
		          		if (datos.getString("VALUE")!=null) value = datos.getString("VALUE"); else value="";
		          		valor.append(value+";\n");

		          	}
		      	}
		        
	        }

	        
            
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la fecha actual",e);
        }
		return valor.toString();
    }
    
    private String expPadreContratacion(IRuleContext rulectx) throws ISPACRuleException {
		String numexpConta = "";
		try{
	 		//--------------------------------------------------------------------------------
		        ClientContext cct = (ClientContext) rulectx.getClientContext();
		        IInvesflowAPI invesFlowAPI = cct.getAPI();
		        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		        //-----------------------------------------------------------------------------
		      //Obtengo los expedientes relacionados
	            String consultaSQL = "WHERE NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";
	            //logger.warn("consultaSQL "+consultaSQL);
	            IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
	            @SuppressWarnings("unchecked")
				Iterator<IItem> itExpRelLicitador = itemCollection.iterator();
	            while(itExpRelLicitador.hasNext()){
	            	IItem padreLicitador = itExpRelLicitador.next();
	            	String numexpLicitador = padreLicitador.getString("NUMEXP_PADRE");
	            	 IItemCollection itemCollContratacion = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_HIJO = '" + numexpLicitador + "'");
	            	 @SuppressWarnings("unchecked")
	 				Iterator<IItem> itExpRelContratacion = itemCollContratacion.iterator();
	            	 while(itExpRelContratacion.hasNext()){
	            		 IItem expContratacion = itExpRelContratacion.next();
	            		 numexpConta = expContratacion.getString("NUMEXP_PADRE");
	            	 }
	            }
	            
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la fecha actual",e);
        }
		return numexpConta;
	}


	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
}


