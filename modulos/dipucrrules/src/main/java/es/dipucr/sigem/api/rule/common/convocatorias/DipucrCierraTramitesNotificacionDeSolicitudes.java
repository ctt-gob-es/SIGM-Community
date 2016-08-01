package es.dipucr.sigem.api.rule.common.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipucrCierraTramitesNotificacionDeSolicitudes implements IRule{
	private static final Logger logger = Logger.getLogger(DipucrCierraTramitesNotificacionDeSolicitudes.class);
	
	private IClientContext cct;

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
		String numexpSolicitud = "";
    	try{
			//----------------------------------------------------------------------------------------------
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
	        logger.info("INICIO - "+this.getClass().getName());

	        String numexp = rulectx.getNumExp();
        
	        ArrayList<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getProcedimientosRelacionadosHijos(numexp, entitiesAPI);
	        int i = 0;
	        for(String numExpSolicitud : expedientesRelacionados){
	        	numexpSolicitud = numExpSolicitud;
	        	if( i != 0){	        		
	        		IItem expSolicitud = ExpedientesUtil.getExpediente(cct, numExpSolicitud);
	        		
	        		if(expSolicitud != null){
	        			String estadoAdm = expSolicitud.getString("ESTADOADM");
	        			if(StringUtils.isNotEmpty(estadoAdm)){
	        				if(estadoAdm.equals("NT") || estadoAdm.equals("NR")){       					
	        			        String strQuery = "WHERE NUMEXP='" + numExpSolicitud + "' AND UPPER(NOMBRE) LIKE '%NOTIFICACI%'";
	        			        IItemCollection collectionTrams = entitiesAPI.queryEntities("SPAC_TRAMITES", strQuery);
	        			        Iterator<?> itTrams = collectionTrams.iterator();
	        		        	IItem tram = null;
	        			        while (itTrams.hasNext()){
	        			        	tram = ((IItem)itTrams.next());
	        			        	int idTram = tram.getInt("ID");
	        			        	transaction.closeTask(idTram);
	        			        }
	        				}
	        			}
	        		}
	        	}
	        	i++;
	        }
	        	      
        	logger.info("FIN - "+this.getClass().getName());
    		return true;
    		
    	 } catch(Exception e) {
         	logger.error("Error al cerrar los trámites del expediente: " + numexpSolicitud + ", por favor compruebe que no está bloqueado por algún usuario.",e);
         	throw new ISPACRuleException("Error al cerrar los trámites del expediente: " + numexpSolicitud + ", por favor compruebe que no está bloqueado por algún usuario.",e);
         }
    }
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}