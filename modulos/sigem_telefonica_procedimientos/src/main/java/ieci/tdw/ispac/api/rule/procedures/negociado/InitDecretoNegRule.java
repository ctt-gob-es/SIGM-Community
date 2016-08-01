package ieci.tdw.ispac.api.rule.procedures.negociado;

import java.util.Iterator;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

/**
 * 
 * @author teresa
 * @date 17/11/2009
 * @propósito Inicializa el expediente de decreto asociado al expediente de Convocatoria Negociado de contratación actual.
 */
public class InitDecretoNegRule implements IRule {

	protected String STR_entidad = "";
	protected String STR_extracto = "";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
    	try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        //Obtiene el expediente de la entidad
	        String numexp_decr = rulectx.getNumExp();	
	        String strQuery = "WHERE NUMEXP_HIJO='"+numexp_decr+"'";
	        IItemCollection col = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
	        Iterator it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
	        
        	IItem relacion = (IItem)it.next();
        	String numexp_ent = relacion.getString("NUMEXP_PADRE");
        	col = entitiesAPI.getEntities(STR_entidad, numexp_ent);
	        it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
	        IItem entidad = (IItem)it.next();
        	
	        
	        //Inicializa los datos del Decreto
			//IItem decreto = entitiesAPI.createEntity("SGD_DECRETO", numexp_decr);
	        strQuery = "WHERE NUMEXP='"+numexp_decr+"'";
	        col = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
	        it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return new Boolean(false);
	        }
        	IItem decreto = (IItem)it.next();
			if (decreto != null)
			{
				decreto.set("EXTRACTO_DECRETO", STR_extracto);
				decreto.store(cct);
			}else{
				return new Boolean(false);
			}
			
			//Actualiza el campo "estado" de la entidada para
			//que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
	        entidad.set("ESTADO", "Decreto");
	        entidad.store(cct);
	        
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

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

}
