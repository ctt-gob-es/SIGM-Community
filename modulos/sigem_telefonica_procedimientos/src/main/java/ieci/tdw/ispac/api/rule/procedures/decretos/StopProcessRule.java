package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class StopProcessRule implements IRule
{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(StopProcessRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException
    {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        IItemCollection docs = null;
        IItem doc = null;
        IItem exp = null;
        String strQuery2 = null;

        try
        {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        ITXTransaction tx = invesFlowAPI.getTransactionAPI();
/*
	        // Se obtiene la fecha de publicación
	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        logger.warn("Query: " + strQuery);
	        IItemCollection bops = entitiesAPI.queryEntities("BOP_PUBLICACION", strQuery);
	        Iterator it = bops.iterator();
        	IItem bop = null;
	        if(it.hasNext()) {
	        	bop = ((IItem)it.next());
	        }
			String fecha = bop.getString("FECHA");
			Date fechaPub = bop.getDate("FECHA"); 
			logger.warn("Fecha: " + fecha);

			//Se obtiene la lista de solicitudes de anuncio para dicho BOP
	        strQuery = "WHERE fecha_publicacion='" + df.format(fechaPub) + "'";
	        logger.warn("Query: " + strQuery);
	        docs = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
	        it = docs.iterator();
	        while (it.hasNext())
	        {
	        	doc = (IItem)it.next();
        		String exped = doc.getString("NUMEXP");
    			logger.warn("Número de expediente que se va a cerrar: " + exped);
*/
	        
	        
	        String numExp = rulectx.getNumExp();
        		// Cerramos el expediente
    	        strQuery2 = "WHERE NUMEXP='" + numExp + "'";
    	        logger.warn("Query: " + strQuery2);
    	        IItemCollection collExps = entitiesAPI.queryEntities("SPAC_EXPEDIENTES", strQuery2);
    	        Iterator itExps = collExps.iterator();
    	        if (itExps.hasNext()) 
    	        {
    	        	exp = (IItem)itExps.next();
    	        	int id = exp.getInt("ID");
    	        	
    	        	/*
    		        String strQuery3 = "WHERE NUMEXP='" + numExp + "'";
    		        IItemCollection trams = entitiesAPI.queryEntities("SPAC_TRAMITES", strQuery3);
    		        Iterator it3 = trams.iterator();
    	        	IItem tram = null;
    		        if (it3.hasNext()) 
    		        {
    		        	tram = ((IItem)it3.next());
    		        	int idTram = tram.getInt("ID");
    	        		logger.warn("Cerrando el trámite con ID = " + idTram);
    		        	tx.closeTask(idTram);
    		        }
    		        */
	        
	        		
    		        String strQuery3 = "WHERE NUMEXP='" + numExp + "'";
    		        IItemCollection fases = entitiesAPI.queryEntities("SPAC_FASES", strQuery3);
    		        Iterator it4 = fases.iterator();
    	        	IItem fase = null;
    		        if (it4.hasNext()) 
    		        {
    		        	fase = ((IItem)it4.next());
    		        	int idFase = fase.getInt("ID");
    	        		logger.warn("Cerrando la fase con ID = " + idFase);
    		        	//tx.closeStage(idFase);
    		        }
    		        
	        		logger.warn("Cerrando la solicitud de anuncio con ID = " + id);
	        		tx.closeProcess(id);
	        		
	        		
    	        }
    	        /*
	        }
*/
	        return new Boolean(true);
        } 
        catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido cerrar la sesión",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException
	{
    }
}
