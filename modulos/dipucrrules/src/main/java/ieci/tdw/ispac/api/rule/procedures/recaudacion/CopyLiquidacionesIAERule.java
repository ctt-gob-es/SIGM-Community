package ieci.tdw.ispac.api.rule.procedures.recaudacion;

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

public class CopyLiquidacionesIAERule implements IRule {
	
	private static final Logger logger = Logger.getLogger(CopyLiquidacionesIAERule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
        try
        {
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            
            String strEntidadSolicitud = "REC_BENEFICIO_IAE";
            String strEntidadLiquidaciones = "REC_DAT_LIQ";
            
			logger.info("Buscando la solicitud...");
    		String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
    		IItemCollection col1 = entitiesAPI.queryEntities(strEntidadSolicitud, strQuery);
    		Iterator it1 = col1.iterator();
    		if (it1.hasNext())
    		{
    			IItem solicitud = (IItem)it1.next();
    			
    			logger.info("Buscando liquidaciones...");
        		strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
        		IItemCollection col2 = entitiesAPI.queryEntities(strEntidadLiquidaciones, strQuery);
        		Iterator it2 = col2.iterator();
        		if (!it2.hasNext())
        		{
    		        String strYears = solicitud.getString("YEARS");

        			for (int i=1 ; i<=20 ; i++)
        			{
        				logger.info("Indice: " + i);
        				String strIndex = String.valueOf(i);
        				String key_mun = "MUN_" + strIndex;
        				String key_liq = "LIQ_" + strIndex;
        				String key_epi = "EPI_" + strIndex;
        				String key_ref = "REF_" + strIndex;
	        			String strLiq = solicitud.getString(key_liq);
	        			if (strLiq != null && strLiq.length()>0)
	        			{
		        			//Copio los datos de la solicitud a la entidad de liquidaciones
	        				logger.info("Copiando...");
		    		        IItem item = entitiesAPI.createEntity(strEntidadLiquidaciones, rulectx.getNumExp());

		    		        item.set("LIQUIDACION", strLiq);

		    		        String strMun = solicitud.getString(key_mun);
		    		        if (strMun != null)
		    		        {
		    		        	item.set("MUNICIPIO", strMun);
		    		        }
		    		        
		    		        String strEpi = solicitud.getString(key_epi);
		    		        if (strEpi != null)
		    		        {
		    		        	item.set("EPIGRAFE", strEpi);
		    		        }
		    		        
		    		        String strRef = solicitud.getString(key_ref);
		    		        if (strRef != null)
		    		        {
		    		        	item.set("REF_AEAT", strRef);
		    		        }
		    		        
		    		        if (strYears != null)
		    		        {
		    		        	item.set("YEARS", strYears);
		    		        }
		    		        
		    		        logger.info("Guardando...");
		    	        	item.store(cct);
	        			}
        			}
        			logger.info("Copia terminada");
        		}
        		else
        		{
        			logger.info("Ya hay liquidaciones. Operación cancelada");
        		}
    		}
    		else
    		{
    			logger.info("No se encuentra la solicitud");
    		}
	    }
        catch (Exception e) 
        {
	        throw new ISPACRuleException("Error en CopyLiquidacionesRule.", e);
	    }
        return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
}