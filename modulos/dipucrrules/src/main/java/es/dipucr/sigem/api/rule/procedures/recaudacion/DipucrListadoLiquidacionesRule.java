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

public class DipucrListadoLiquidacionesRule implements IRule
{
	private static final Logger logger = Logger.getLogger(DipucrListadoLiquidacionesRule.class);
	
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
		        
		        String municipioLiquidacion = "";
		        
		        if (it.hasNext()) 
		        {
	                IItem item = ((IItem)it.next());
	                municipioLiquidacion = item.getString("MUNICIPIO");
	                IItemCollection collectionMunicipio = entitiesAPI.queryEntities("REC_VLDTBL_MUNICIPIOS", "WHERE VALOR = '"+municipioLiquidacion+"'");
	                Iterator it2 = collectionMunicipio.iterator();
	                if(it2.hasNext()){
	                	municipioLiquidacion = ((IItem)it2.next()).getString("SUSTITUTO");
	                }
	                if(municipioLiquidacion != null && !municipioLiquidacion.equals(""))listado += "Municipio: "+municipioLiquidacion+"\n";               
	                for (int i=1 ; i<=24 ; i++)
	                {	                	
	                	String nLiq = getLiquidacion(item, i, rulectx.getNumExp(), entitiesAPI);
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
	
	@SuppressWarnings("rawtypes")
	private String getLiquidacion(IItem solicitud, int nLiq, String numExp, IEntitiesAPI entitiesAPI) throws ISPACException
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
				logger.info("No se encuentra la liquidación indicada. nLiq=" + String.valueOf(nLiq));
				bOk = false;
			}
		}
		String fraccionamiento = solicitud.getString("FRACCIONAMIENTO");
		if(fraccionamiento!= null && !fraccionamiento.toUpperCase().equals("SI")){
			if(strLiq != null && !strLiq.equals("")){
				String aux = "";
				IItem item = null;	
				String strQuery = "WHERE NUMEXP = '" + numExp + "'";
				String tabla = "";
				
				//Las 10 primeras liquidaciones buscamos en la primera tabla REC_APLAZA_01
				if(nLiq <= 10) tabla = "REC_APLAZA_01";		
				else if (nLiq <=20) tabla = "REC_APLAZA_02";	
				else tabla = "REC_APLAZA_03";
				
				IItemCollection collection = entitiesAPI.queryEntities(tabla, strQuery);
			    Iterator it = collection.iterator();
			    if(it.hasNext()){		        	
			        item = (IItem) it.next();		        			    
			        
					strLiq += "\n";
		        	aux = item.getString("PRINCIPAL"+nLiq);
		        	if(aux == null || aux.equals(""))
		        		strLiq += "";
		        	else
		        		strLiq += "\t\tCuota: "+aux + "\n";
		        	
		        	aux = item.getString("RECARGO"+nLiq);
		        	if(aux == null || aux.equals(""))
		        		strLiq += "";
		        	else
			        	strLiq += "\t\tRecargo de apremio: "+aux + "\n";
		        	
		        	aux = item.getString("INTERESES"+nLiq);
		        	if(aux == null || aux.equals(""))
		        		strLiq += "";
		        	else
			        	strLiq += "\t\tIntereses: "+aux + "\n";
			        
		        	aux = item.getString("CUOTA_MUNICIPAL"+nLiq);
		        	if(aux == null || aux.equals(""))
		        		strLiq += "";
		        	else
			        	strLiq += "\t\tPrincipal Cuota Municipal: "+aux + "\n";
			        
		        	aux = item.getString("INTERESES_CUOTA"+nLiq);
		        	if(aux == null || aux.equals(""))
		        		strLiq += "";
		        	else
			        	strLiq += "\t\tIntereses de demora Cuota Municipal: "+aux + "\n";
			        
		        	aux = item.getString("RECARGO_PROVINCIAL"+nLiq);
		        	if(aux == null || aux.equals(""))
		        		strLiq += "";
		        	else 
		        		strLiq += "\t\tPrincipal Recargo Provincial: "+aux + "\n";
			        
		        	aux = item.getString("INTERESES_RECARGO"+nLiq);
		        	if(aux == null || aux.equals(""))
		        		strLiq += "";
		        	else 
			        	strLiq += "\t\tIntereses de demora Recargo Provincial: "+aux + "\n";		        
		        	
		        	aux = item.getString("PLAZO_INGRESO"+nLiq);
		        	if(aux == null || aux.equals(""))
		        		strLiq += "";
		        	else
		        		strLiq += "\t\tFin Plazo Ingreso: "+aux;
			    }
			}
		}
		
        return strLiq;
	}
}