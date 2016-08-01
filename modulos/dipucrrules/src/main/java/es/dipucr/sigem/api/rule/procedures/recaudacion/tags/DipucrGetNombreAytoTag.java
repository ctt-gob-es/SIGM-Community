package es.dipucr.sigem.api.rule.procedures.recaudacion.tags;

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

public class DipucrGetNombreAytoTag implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrGetNombreAytoTag.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		String strAyto = "";
        try
        {
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();

	        String strEntity = rulectx.get("entity");
	        String strCampo = rulectx.get("campo");
	        if ( ! StringUtils.isEmpty(strEntity) && 
	        	 ! StringUtils.isEmpty(strCampo) )
	        {
		        String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
		        IItemCollection collection = entitiesAPI.queryEntities(strEntity, strQuery);
		        Iterator it = collection.iterator();
		        IItem item = null;
		        if (it.hasNext()) {
	            	item = ((IItem)it.next());
	            	String strValor = item.getString(strCampo);
	            	
	            	String strQuery1 = "WHERE VALOR = '" + strValor + "'";
			        IItemCollection collection1 = entitiesAPI.queryEntities("REC_VLDTBL_MUNICIPIOS", strQuery1);
			        Iterator it2 = collection1.iterator();
			        
			        if(it2.hasNext()){
			        	IItem municipio = (IItem)it2.next();
			        	strAyto = municipio.getString("SUSTITUTO");
			        }	            	
		        }
	        }
	    }
        catch (Exception e) 
        {
        	logger.error("Error en DipucrGetNombreAytoTag");
	        throw new ISPACRuleException("Error en DipucrGetNombreAytoTag.", e);	        
	    }
        return strAyto;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}