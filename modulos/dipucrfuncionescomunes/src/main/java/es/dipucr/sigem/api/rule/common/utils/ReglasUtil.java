package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.EventManager;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.tx.TXTramiteDAO;
import ieci.tdw.ispac.ispactx.TXTransactionDataContainer;

/**
 * [dipucr-Felipe]
 * Clase con funciones comunes para reglas
 * @since 08.07.2011
 * @author Felipe
 */
public class ReglasUtil {
	
	/**
	 * Recupera el id de la regla por su nombre
	 * @param invesflowAPI
	 * @param nombreRegla
	 * @return
	 * @throws ISPACException
	 */
	public static int getRuleId(IInvesflowAPI invesflowAPI, String nombreRegla) throws ISPACException{
		
		try{
			int ruleId = Integer.MIN_VALUE;
			IItemCollection collection = invesflowAPI.getCatalogAPI().getCTRules(nombreRegla);
			
			if (collection.toList().size() > 0){
				IItem regla = (IItem)collection.iterator().next();
				ruleId = regla.getInt("ID");
			}
			else{
				throw new ISPACException("Regla no encontrada");
			}
			
			return ruleId;
		}
		catch(Exception ex){
			throw new ISPACException
			(
				"Error al recuperar la regla " + nombreRegla + ": "
				+ ex.getMessage(), ex
			);
		}
		
	}
	
	/**
	 * Ejecuta la regla 
	 * @param cct
	 * @param process
	 * @param nombreRegla
	 * @throws ISPACException
	 */
	public static void ejecutarReglaPcd(IClientContext cct, 
			IProcess process, String nombreRegla) throws ISPACException
	{
		IInvesflowAPI invesflowAPI = cct.getAPI();
		
		IItemCollection iCollection = invesflowAPI.getCatalogAPI().getCTRules(nombreRegla);
		IItem regla = (IItem)iCollection.iterator().next();
		int ruleId = regla.getInt("ID");
		EventManager eventmgr = new EventManager(cct);
		eventmgr.newContext();
		eventmgr.getRuleContextBuilder().addContext(process);
		eventmgr.processRule(ruleId);
	}
	
	/**
	 * Ejecuta la regla en el trámite
	 * @param cct
	 * @param idTramite
	 * @param nombreRegla
	 * @throws ISPACException
	 */
	public static void ejecutarReglaTramite(IClientContext cct, 
			int idTramite, String nombreRegla) throws ISPACException
	{
		IInvesflowAPI invesflowAPI = cct.getAPI();
		
		TXTransactionDataContainer dtc = new TXTransactionDataContainer((ClientContext) cct);
		int ruleId = ReglasUtil.getRuleId(invesflowAPI, nombreRegla);
		TXTramiteDAO txTask = dtc.getTask(idTramite);
		EventManager eventmgr = new EventManager(cct);
		eventmgr.newContext();
		eventmgr.getRuleContextBuilder().addContext(txTask);
		eventmgr.processRule(ruleId);
	}
}
