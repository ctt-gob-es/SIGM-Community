package es.dipucr.sigem.api.rule.procedures.recaudacion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class DipucrListadoRefCatastRule implements IRule
{
	private static final Logger logger = Logger.getLogger(DipucrListadoRefCatastRule.class);
	
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        logger.info("INICIO - DipucrListadoRefCatastRule");
	        String listado = "";  //Listado de liquidaciones 
	        String entidad = rulectx.get("entity");
        	if (!StringUtils.isEmpty(entidad))
        	{
		        String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
		        IItemCollection collection = entitiesAPI.queryEntities(entidad, strQuery);
		        Iterator it = collection.iterator();
		        if (it.hasNext()) 
		        {
	                IItem item = ((IItem)it.next());
	                for (int i=1 ; i<=20 ; i++)
	                {
	                	String nLiq = getRefCatastral(item, i);
	                	if (nLiq != null && nLiq.length()>0)   
	                	{
	                		if (i > 1)
	                		{
	                			listado += "\n";
	                		}
	                		listado += nLiq;
	                	}
	                }
		        }
        	}
        	logger.info("FIN - DipucrListadoRefCatastRule");
    		return listado;
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la lista de liquidaciones",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
	private String getRefCatastral(IItem solicitud, int nLiq) throws ISPACException
	{
		StringBuffer strRef = new StringBuffer();

		//Intento encontrar el campo N_LIQ indicado por nLiq
		try
		{
			if(solicitud.getString("REF1_" + nLiq) != null && !solicitud.getString("REF1_" + nLiq).equals("")){
				strRef.append(solicitud.getString("REF1_" + nLiq));			
				strRef.append("-");
				strRef.append(solicitud.getString("REF2_" + nLiq));
				strRef.append("-");
				strRef.append(solicitud.getString("REF3_" + nLiq));
				strRef.append("-");
				strRef.append(solicitud.getString("REF4_" + nLiq));
			}
		}
		catch(Exception e)
		{}
		
        return strRef.toString();
	}
}