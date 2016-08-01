package es.dipucr.sigem.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

/**
 * [eCenpri-Felipe #504]
 * Devuelve la categoría del tablón para el documento
 */
public class GetCategoriaTablonTagRule implements IRule 
{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(AdministracionTagRule.class);
	
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
        try
        {
        	//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        StringBuffer sbCategoria = new StringBuffer();
	        String numexp = rulectx.getNumExp();
	        IItemCollection collection = entitiesAPI.getEntities("ETABLON_PUBLICACION", numexp);
	        
	        if (collection.next()){
	        	IItem itemPublicacion = (IItem)collection.iterator().next();
	        	String categoria = itemPublicacion.getString("CATEGORIA");
	        	sbCategoria.append(categoria);
	        	String categoriaOtros = itemPublicacion.getString("CATEGORIA_OTROS");
	        	if (StringUtils.isNotEmpty(categoriaOtros)){
	        		sbCategoria.append(": ");
	        		sbCategoria.append(categoriaOtros);
	        	}
	        }

	        return sbCategoria.toString();
	        
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error la categoria de la publicación.", e);
	    }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
