package es.dipucr.contratacion.rule.doc;

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

public class AdjudicadoRule  implements IRule{
	
	public static final Logger LOGGER = Logger.getLogger(AdjudicadoRule.class);
	
	
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
	        
	        
	        String consulta = "WHERE NUMEXP_PADRE = '"+rulectx.getNumExp()+"' AND RELACION='Plica'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", consulta);
			Iterator<IItem> it = collection.iterator(); 
			
			StringBuffer sexpRela = new StringBuffer("");
			
			if(it.hasNext()){
				IItem expRelacionados = (IItem)it.next();
	        	String numexpLicitador = expRelacionados.getString("NUMEXP_HIJO");
	        	sexpRela.append(" NUMEXP= '"+numexpLicitador+"' ");
			}
			while(it.hasNext()){
				IItem expRelacionados = (IItem)it.next();
	        	String numexpLicitador = expRelacionados.getString("NUMEXP_HIJO");
	        	sexpRela.append("OR NUMEXP = '"+numexpLicitador+"' ");
			}
	        
	        if (!sexpRela.toString().equals("")){
	        	
	        	consulta= "WHERE ("+sexpRela.toString()+") AND APTO='SI'";
	        	IItemCollection collectionPlica = entitiesAPI.queryEntities("CONTRATACION_PLICA", consulta);
	        	Iterator<IItem> itPlica = collectionPlica.iterator();
	        	//Este es el adjudicado
	        	if(collectionPlica.toList().size()==1){
	        		if(itPlica.hasNext()){
	        			IItem plica = itPlica.next();
		        		IItem expedientePlica = entitiesAPI.getExpedient(plica.getString("NUMEXP"));
		        		if(strProperty.equals("NOMBRE")){
		        			valor = expedientePlica.getString("IDENTIDADTITULAR");
		        		}
		        		if(strProperty.equals("REPRESENTANTE")){
		        			valor = plica.getString("REPRESENTANTE");
		        		}
		        		if(strProperty.equals("CIF")){
		        			valor = expedientePlica.getString("NIFCIFTITULAR");
		        		}
		        		if(strProperty.equals("DOMICILIO")){
		        			valor = expedientePlica.getString("DOMICILIO");
		        		}
		        	}
	        	}
	        	else{
	        		if(!itPlica.hasNext()){
	        			valor="No existe ningún Licitador que sea apto";
	        		}
	        		else{
	        			valor="Existe más de un Licitador apto";
	        		}
	        	}
	        	
	        }  
	        else{
	        	valor = "No existen Licitadores";
	        }
	        
            
        } catch (ISPACException e) {
        	LOGGER.error("Error. "+e.getMessage(),e);
        	throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		return valor;
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
}


