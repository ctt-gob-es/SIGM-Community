package es.dipucr.sigem.api.rule.procedures.recaudacion;

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

public class DipucrGetColumnasLiqDevoluciones implements IRule
{
	private static final Logger logger = Logger.getLogger(DipucrGetColumnasLiqDevoluciones.class);
	
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
	        logger.info("INICIO - DipucrGetColumnasLiqDevoluciones");
	        StringBuffer listado = new StringBuffer();  //Listado de liquidaciones 
	        String entidad = rulectx.get("entity");
	        String columna = rulectx.get("patronCol");
	        int colIni = Integer.parseInt(rulectx.get("colIni"));
	        int colFin = Integer.parseInt(rulectx.get("colFin"));
        	if (!StringUtils.isEmpty(entidad))
        	{
		        String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
		        IItemCollection collection = entitiesAPI.queryEntities(entidad, strQuery);
		        Iterator it = collection.iterator();
		        while (it.hasNext()) 
		        {
	                IItem item = ((IItem)it.next());
	                for (int i=colIni ; i<=colFin ; i++)
	                {
	                	String nLiq = item.getString(columna + i);
	                	if (nLiq != null && nLiq.length()>0)   
	                	{
	                		listado.append(nLiq);
	                		listado.append( "\n");
	                		//Para cada liquidación recuperamos sus datos
	                		String consulta = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND LIQUIDACION = '"+nLiq+"'";
	                		IItemCollection liquidaciones = entitiesAPI.queryEntities("REC_DAT_LIQ", consulta);
	        		        Iterator datos_liq = liquidaciones.iterator();
	        		        while (datos_liq.hasNext()) 
	        		        {
	        		        	IItem dato_liq = (IItem) datos_liq.next();	        		        	
	        		        	String fecha = "";
	        		        	String entidadLiq = "";
	        		        	String sucursal = "";
	        		        	String cuotaPagada = "";
	        		        	String recargoPagada = "";
	        		        	for(int j=1; j<=4; j++){
		        		        	fecha = dato_liq.getString("FECHA_"+j);
		        		        	entidadLiq = dato_liq.getString("ENTIDAD_"+j);
		        		        	sucursal = dato_liq.getString("SUCURSAL_"+j);
		        		        	cuotaPagada = dato_liq.getString("CUOTA_PAGADA_"+j);
		        		        	recargoPagada = dato_liq.getString("RECARGO_PAGADO_"+j);
		        		        	double importe = 0;
		        		        	
		        		        	if(fecha != null || entidadLiq != null || sucursal != null 
		        		        			|| cuotaPagada != null || recargoPagada != null)
		        		        				listado.append( "\t");
		        		        	if(fecha != null)
		        		        		listado.append(fecha+" - ");
		        		        	if(entidadLiq != null)
		        		        		listado.append(entidadLiq+"/");
		        		        	if(sucursal != null)
		        		        		listado.append(sucursal+" - ");
		        		        	if(cuotaPagada != null)	{
		        		        		importe += Double.parseDouble(cuotaPagada.replace(",", "."));
		        		        	}
		        		        	if(recargoPagada != null){	        		        			        		        	
		        		        		importe += Double.parseDouble(recargoPagada.replace(",", "."));
		        		        	}
		        		        	
		        		        	
		        		        	if(fecha != null || entidadLiq != null || sucursal != null 
		        		        			|| cuotaPagada != null || recargoPagada != null){
		        		        		listado.append(importe);		        		        	
		        		        		listado.append( "\n");
		        		        	}
	        		        	}
	        		        }
	                	}
	                }
		        }
        	}
        	logger.info("FIN - DipucrGetColumnasLiqDevoluciones");
    		return listado.toString();
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la lista de liquidaciones",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}