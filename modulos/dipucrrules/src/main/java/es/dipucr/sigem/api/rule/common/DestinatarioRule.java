package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DestinatarioRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(DestinatarioRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		String property = "";
        try{
        	//----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();	        
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();			
            //----------------------------------------------------------------------------------------------
	
	        StringBuffer textoPart = new StringBuffer();  //Tag a devolver 

	        //Obtenemos los parámetros para generar los tags
	        property = rulectx.get("property");
	        
	        if(property.equals("AYUNTAMIENTO")){
		    	IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(),  "NOMBRE LIKE '%AYUNTAMIENTO%'", "");
		    	Iterator<?> itPart = participantes.iterator();
		    	if(itPart.hasNext()){
		    		IItem parti = (IItem) itPart.next();
		    		String nombre = "";
		    		if(parti.getString("NOMBRE")!= null)nombre=parti.getString("NOMBRE");
		    		String localidad = "";
		    		if(parti.getString("LOCALIDAD")!= null)localidad=parti.getString("LOCALIDAD");
		    		String dirnot = "";
		    		if(parti.getString("DIRNOT")!= null)dirnot=parti.getString("DIRNOT");
		    		String cp = "";
		    		if(parti.getString("C_POSTAL")!= null)cp=parti.getString("C_POSTAL");
		    		String caut = "";
		    		if(parti.getString("CAUT")!= null)caut=parti.getString("CAUT");
		    		
		    		textoPart.append(nombre+"\n");
		    		textoPart.append(dirnot+"\n");
		    		textoPart.append(cp+" "+localidad+" "+caut+"\n");
		    	}
	        }
	       
	        return textoPart.toString();
	        
	    } catch (Exception e) {
	    	logger.error("Error obteniendo el tag " + property + ". " + e.getMessage(), e);
	        throw new ISPACRuleException("Error obteniendo el tag. ", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
