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

import java.util.Iterator;

public class ValoresCodiceExpeContratacionRule implements IRule{
	
	
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	String valor = "";
        try{
 		//--------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //-----------------------------------------------------------------------------

	        String strEntity = rulectx.get("entity");
	        String strProperty = rulectx.get("property");
	        String codice = rulectx.get("codice");
	        
	        //Busqueda del expediente de contratación
	        String numexpContratacion = expPadreContratacion(rulectx);
	        
	        String consulta = "WHERE NUMEXP = '"+numexpContratacion+"'";
			IItemCollection collection = entitiesAPI.queryEntities(strEntity, consulta);
	        @SuppressWarnings("unchecked")
			Iterator<IItem> it = collection.iterator();
	        String tipo = "";
	        
	        if (it.hasNext()){
	        	IItem contrato = (IItem)it.next();
	        	tipo= contrato.getString(strProperty);
	        }
	        
	        if(codice != null && codice.equals("ValoresCodiceRule")){
	        	

		        if(tipo!=""){
		        	String [] v_cod_tipo_contrato = tipo.split(" - ");
		        	if(v_cod_tipo_contrato.length > 0){
		        		valor = v_cod_tipo_contrato[1];
		        	}
		        	
		        }
	        }
	        else{
	        	valor = tipo;
	        }	        
	        
            
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la fecha actual",e);
        }
		return valor;
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


