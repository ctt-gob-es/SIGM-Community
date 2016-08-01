package es.dipucr.sigem.api.rule.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class DipucrGetTodaColumnaSustitutoValidationTableTag implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrGetTodaColumnaSustitutoValidationTableTag.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String strSustituto = "";
		StringBuffer listado = new StringBuffer();
        try {
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();

	        String strEntity = rulectx.get("entity");
	        String strCampoValor = rulectx.get("campoValor");
	        String strTablaValidacion = rulectx.get("validationTable");
	        
	        if ( !StringUtils.isEmpty(strEntity) && !StringUtils.isEmpty(strCampoValor)){
		        String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
		        
		        IItemCollection collection = entitiesAPI.queryEntities(strEntity, strQuery);
		        Iterator<?> it = collection.iterator();
		        while (it.hasNext()) {
			        IItem item = (IItem)it.next();
		            String strValor = item.getString(strCampoValor);
		            	
		            String strQuery1 = "WHERE VALOR = '" + strValor + "'";
		            IItemCollection collection1 = entitiesAPI.queryEntities(strTablaValidacion, strQuery1);
		            Iterator<?> it2 = collection1.iterator();
		            
		            if(it2.hasNext()){
		            	IItem valor = (IItem)it2.next();
		            	strSustituto = valor.getString("SUSTITUTO");
		            	if (StringUtils.isNotEmpty(strSustituto)) {						
							listado.append(strSustituto);							
		            	}
		            	listado.append("\n");
			        }
				}
	        }
	    }
        catch (Exception e) {
        	logger.error("Error al obtener el valor del sustituto." + e.getMessage(), e);
	        throw new ISPACRuleException("Error al recuperar el valor del sustituto.", e);	        
	    }
        return listado.toString();
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}