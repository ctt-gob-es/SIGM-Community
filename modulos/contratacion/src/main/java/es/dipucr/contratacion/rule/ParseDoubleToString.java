package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.utils.ParseNumeroToTexto;

public class ParseDoubleToString implements IRule
{
	private static final Logger logger = Logger.getLogger(ParseDoubleToString.class);

    public boolean init(IRuleContext rctx) throws ISPACRuleException
    {
        return true;
    }

    public boolean validate(IRuleContext rctx) throws ISPACRuleException
    {
        return true;
    }

	public Object execute(IRuleContext rctx) throws ISPACRuleException
    {      
    	String cadenaPropiedad = "";
    	String entityname = "";
        String property = "";

        String numexp = rctx.getNumExp();

        try
        {
			IClientContext cct = rctx.getClientContext();
			
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
            
            entityname = rctx.get("entity");
            property = rctx.get("property");
            
            logger.info("entityname "+entityname);
            logger.info("property "+property);
            
            IItemCollection itemCollection = entitiesAPI.getEntities(entityname, numexp);
            Iterator<?> iCollection = itemCollection.iterator();
            
            if (iCollection.hasNext()) {
				
				IItem entity = (IItem) iCollection.next();
				String propiedad = entity.getString(property);

				BigDecimal dPropiedad = new BigDecimal(0);
				if(null != propiedad && !StringUtils.isEmpty(propiedad.trim())){
					dPropiedad = new BigDecimal(propiedad.trim());
				}
				logger.info("presupuesto "+propiedad);
				ParseNumeroToTexto parseNumeroToTexto = new ParseNumeroToTexto();
				cadenaPropiedad = parseNumeroToTexto.convertNumberToLetter(dPropiedad);
				logger.info("cadenaPresup "+cadenaPropiedad);
            }
            
			
			return cadenaPropiedad;
        }
        catch(ISPACException e)
        {
        	logger.error("Error obteniendo el valor sustituto. Propiedad: " + property + ", de la entidad: " + entityname + ", en el expediente: " + numexp + ". " + e.getMessage(), e);
            throw new ISPACRuleException("Error obteniendo el valor sustituto. Propiedad: " + property + ", de la entidad: " + entityname + ", en el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rctx) throws ISPACRuleException
    {

    }
}
