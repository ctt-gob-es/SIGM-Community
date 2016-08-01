package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

public class CierreSesionRule implements IRule {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        //Se obtiene la lista de propuestas de la sesión
	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        IItemCollection collAllProps = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
	        Iterator itAllProps = collAllProps.iterator();
        	IItem iProp = null;
	        while(itAllProps.hasNext()) {
	        	iProp = ((IItem)itAllProps.next());
	        	cierraPropuesta(rulectx, iProp);
	        }

	        //Ahora lo mismo pero con las urgencias
	        collAllProps = entitiesAPI.queryEntities("SECR_URGENCIAS", strQuery);
	        itAllProps = collAllProps.iterator();
        	iProp = null;
	        while(itAllProps.hasNext()) {
	        	iProp = ((IItem)itAllProps.next());
	        	cierraPropuesta(rulectx, iProp);
	        }
	        
	        //Se eliminan las relaciones con esta sesión de gobierno
	        strQuery = "WHERE NUMEXP_HIJO='" + rulectx.getNumExp() + "'";
	        entitiesAPI.deleteEntities("SPAC_EXP_RELACIONADOS", strQuery);
	        
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido cerrar la sesión",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
	private void cierraPropuesta(IRuleContext rulectx, IItem iProp) throws ISPACRuleException
	{
		try
		{
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	
	        //Actualizamos el expediente de origen
	    	String numExpOrigen = iProp.getString("NUMEXP_ORIGEN");
	    	String strQuery = "WHERE NUMEXP='" + numExpOrigen + "'";
	        IItemCollection collProps = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
	        Iterator itProps = collProps.iterator();
	    	IItem iPropOrigen = null;
	        if ( itProps.hasNext()) 
	        {
	        	iPropOrigen = ((IItem)itProps.next());
	        	iPropOrigen.set("NOTAS", iProp.getString("NOTAS"));
	        	iPropOrigen.set("DEBATE", iProp.getString("DEBATE"));
	        	iPropOrigen.set("ACUERDOS", iProp.getString("ACUERDOS"));
	        	iPropOrigen.set("DICTAMEN", iProp.getString("DICTAMEN"));
	        	iPropOrigen.set("N_SI", iProp.getString("N_SI"));
	        	iPropOrigen.set("N_NO", iProp.getString("N_NO"));
	        	iPropOrigen.set("N_ABS", iProp.getString("N_ABS"));
	        	iPropOrigen.store(cct);
	        }
	        
	        //Actualizamos el estado administrativo
	        strQuery = "WHERE NUMEXP='" + numExpOrigen + "'";
	        IItemCollection collExps = entitiesAPI.queryEntities("SPAC_EXPEDIENTES", strQuery);
	        Iterator itExps = collExps.iterator();
	        if (itExps.hasNext()) 
	        {
	        	String strOrgano = CommonFunctions.getOrganoSesion(rulectx, null);
	        	if ( strOrgano.compareTo("COMI")==0 ||
	        	     strOrgano.compareTo("MESA")==0 )
	        	{
	        		//En Comisiones Informativas y Mesa de Contratación
	        		//no cerramos las propuestas sino que las etiquetamos como "Dictaminada"
	        		IItem iExpediente = ((IItem)itExps.next());
	        		iExpediente.set("ESTADOADM", "SEC_DC");
	        		iExpediente.store(cct);
	        	}
	        	else
	        	{
	        		//En Pleno y Junta de Gobierno etiquetamos
	        		//como "Archivada" y luego cerramos el expediente
	        		IItem iExpediente = ((IItem)itExps.next());
	        		iExpediente.set("ESTADOADM", "SEC_AR");
	        		iExpediente.store(cct);
	        		int id = iExpediente.getInt("ID");
	        		tx.closeProcess(id);
	        	}
	        }
        } 
		catch(Exception e) 
		{
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido cerrar la propuesta. Compruebe que no quedan trámites abiertos en las propuestas.",e);
        }
	}
}