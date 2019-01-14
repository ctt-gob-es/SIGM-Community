package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.webempleado.services.ayudasSociales.AyudasSocialesWSProxy;


/**
 * [dipucr-Felipe #510]
 * Regla que anula uan ayuda social en la bbdd de ayudasss (portal empleado)
 * @since 30.06.17
 */
public class AnularAyudaAyudasssRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(AnularAyudaAyudasssRule.class);
	
	public static final String COD_ESTADO_ANULADA = "A"; 

	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación de la fase y el trámite
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			
			//Petición al webservice
			IItemCollection colAyudasss = entitiesAPI.getEntities("AYUDASSS", numexp);
			IItem itemAyudasss = (IItem) colAyudasss.iterator().next();
			
			String idAyuda = itemAyudasss.getString("ID_AYUDA");
			
			AyudasSocialesWSProxy ws = new AyudasSocialesWSProxy();
			ws.anularAyuda(idAyuda);
			
			//Buscamos el expediente original de la ayuda y la marcamos como anulada
			String query = "WHERE ID_AYUDA = '" + idAyuda + "' ORDER BY ID";
			colAyudasss = entitiesAPI.queryEntities("AYUDASSS", query);
			itemAyudasss = (IItem) colAyudasss.iterator().next();
			
			//Marcamos el estado anulado en la pestaña
			itemAyudasss.set("ESTADO", COD_ESTADO_ANULADA);
			itemAyudasss.store(cct);
			
			//Ponemos anulada en el asunto
			String numexpOriginal = itemAyudasss.getString("NUMEXP");
			IItem itemExpedientOriginal = entitiesAPI.getExpedient(numexpOriginal);
			
			String asunto = itemExpedientOriginal.getString("ASUNTO");
			asunto = "[ANULADA] " + asunto;
			itemExpedientOriginal.set("ASUNTO", asunto);
			itemExpedientOriginal.store(cct);
		
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la anulación de la ayuda " + e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
