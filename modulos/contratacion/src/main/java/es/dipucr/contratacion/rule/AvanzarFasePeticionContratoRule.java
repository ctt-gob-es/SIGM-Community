package es.dipucr.contratacion.rule;

import java.util.Iterator;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * Ticket #504 - Avanzar la fase del expediente
 * @author Felipe-ecenpri
 * @since 04.04.2012
 */
public class AvanzarFasePeticionContratoRule implements IRule {
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(AvanzarFasePeticionContratoRule.class);
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
    	String peticContratacion = "";
    	
    	try{ 
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
	      //Obtengo el número de expediente de la petición de contrato
			String consulta = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION LIKE '%Petición Contrato%'";
			IItemCollection itCollExpRel = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consulta);
			
			@SuppressWarnings("unchecked")
			Iterator<IItem> itExpRel = itCollExpRel.iterator();
	        if (itExpRel.hasNext())
	        {
	        	IItem iPetCont = itExpRel.next();
	        	peticContratacion = iPetCont.getString("NUMEXP_PADRE");
	        	ExpedientesUtil.avanzarFase(cct, peticContratacion);
	        }
	        return true;
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
	        throw new ISPACRuleException("Error al avanzar fase del expediente. Numexp:" + peticContratacion, e);
	    } 
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException
    {
    }

}
