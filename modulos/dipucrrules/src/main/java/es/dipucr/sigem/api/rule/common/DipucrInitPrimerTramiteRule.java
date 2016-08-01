package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * Regla que inicia el primer trámite de la fase actal de un procedimiento
 * @author Felipe
 * @since 20.09.2012
 */
public class DipucrInitPrimerTramiteRule implements IRule {
	
//	private static final Logger logger = Logger.getLogger(DipucrInitPrimerTramiteRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
	        
//	        String strQuery = null;
	        IItemCollection col = null;
	        
	        //Obtiene el expediente de la entidad
	        String numexpActual = rulectx.getNumExp();	
//	        strQuery = "WHERE NUMEXP_HIJO='" + numexpActual + "'";
//	        col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
//        	IItem itemExpRelacionados = (IItem)col.iterator().next();
//        	String numexpPadre = itemExpRelacionados.getString("NUMEXP_PADRE");
        				
			//Obtiene el número de fase del decreto
			String strQueryAux = "WHERE NUMEXP='" + numexpActual + "'";
			col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_FASES, strQueryAux);
			IItem itemExpediente = (IItem)col.iterator().next();
			int idFase = itemExpediente.getInt("ID");
			int idFaseDecreto = itemExpediente.getInt("ID_FASE");
			
			//Obtenemos el id del trámite de "Creación del decreto, traslados y notificaciones"
			strQueryAux = "WHERE ID_FASE = " + idFaseDecreto + " ORDER BY ORDEN ASC";
			col = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, strQueryAux);
			IItem tramite = (IItem)col.iterator().next();
			int idTramite = tramite.getInt("ID");
			
			//Creo el tramite, el primero de la fase actual del procedimiento
			transaction.createTask(idFase, idTramite);
	        
			//Importar participantes?
//			DipucrCommonFunctions.importarParticipantes(cct, entitiesAPI, numexpInforme, numexpDecreto);
		
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
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		 return true;
	}

}
