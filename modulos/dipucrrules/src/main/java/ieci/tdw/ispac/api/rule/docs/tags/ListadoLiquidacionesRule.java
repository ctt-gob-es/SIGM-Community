package ieci.tdw.ispac.api.rule.docs.tags;

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

public class ListadoLiquidacionesRule implements IRule
{
	private static final Logger logger = Logger.getLogger(ListadoLiquidacionesRule.class);
	
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
	                for (int i=1 ; i<=8 ; i++)
	                {
	                	String nLiq = getLiquidacion(item, i);
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
    		return listado;
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la lista de liquidaciones",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
	private String getLiquidacion(IItem solicitud, int nLiq) throws ISPACException
	{
		String strLiq = "";
		boolean bOk = true;

		//Intento encontrar el campo N_LIQ indicado por nLiq
		try
		{
			strLiq = solicitud.getString("N_LIQ_" + nLiq);
		}
		catch(Exception e)
		{
			bOk = false;
		}
		
		//Si no lo encuentro quizás sea porque se llame LIQ_
		if (!bOk)
		{
			try
			{
				strLiq = solicitud.getString("LIQ_" + nLiq);
			}
			catch(Exception e)
			{
				//Tampoco funciona. Algo falla.
				logger.error("No se encuentra la liquidación indicada. nLiq=" + String.valueOf(nLiq) + " en el expediente: " + solicitud.getString("NUMEXP") + ". " + e.getMessage(), e);
				bOk = false;
			}
		}
		
        return strLiq;
	}
}