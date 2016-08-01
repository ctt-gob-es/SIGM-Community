package es.dipucr.sigem.api.rule.procedures.planesProvinciales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrIniciaPlanProvincialRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrIniciaPlanProvincialRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {	
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - DipucrIniciaPlanProvincialRule");
		try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        String strQuery = null;
	        IItemCollection col = null;
	        
	        //Obtiene el expediente de la entidad	       
        				
			//Obtiene el número de fase del decreto
	        strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
			col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, strQuery);
			IItem itemExpediente = (IItem)col.iterator().next();
		//	int idFase = itemExpediente.getInt("ID");
			int idFaseDecreto = itemExpediente.getInt("ID_FASE");
			
			//Obtenemos el id del trámite de "Providencia"
			strQuery = "WHERE ID_FASE = " + idFaseDecreto + " AND NOMBRE = 'Providencia' ORDER BY ORDEN ASC";
			col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQuery);
		//	IItem tramiteProvidencia = (IItem)col.iterator().next();
			//int idTramiteProvidencia = tramiteProvidencia.getInt("ID");
			
			//Obtenemos el id del trámite de "Comunicación Ayuntamientos Planes Provinciales"
			strQuery = "WHERE ID_FASE = " + idFaseDecreto + " AND NOMBRE LIKE 'Comunicación Ayuntamientos Planes Provinciales' ORDER BY ORDEN ASC";
			col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQuery);
		//	IItem tramiteComunacion = (IItem)col.iterator().next();
		//	int idTramiteComunicacion = tramiteComunacion.getInt("ID");
			
			//Creo los trámites
			//int idTramiteProv = transaction.createTask(idFase, idTramiteProvidencia);

			//int idTramiteComu = transaction.createTask(idFase, idTramiteComunicacion);
	        
			logger.info("FIN - DipucrIniciaPlanProvincialRule");
	        return new Boolean(true);
        }
    	catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido inicializar el decreto.",e);
        }
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
}
