package es.dipucr.sigem.api.rule.common.utils;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [ecenpri-Felipe Ticket #39] Nuevo procedimiento Propuesta de Solicitud de Anuncio
 * @since 04.08.2010
 * @author Felipe
 */
public class FasesUtil {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(FasesUtil.class);
	
	public static IItem getFase(IClientContext cct, String numexp) throws ISPACRuleException{
		IItem resultado = null;
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IItemCollection fasesCollection = entitiesAPI.getEntities(Constants.TABLASBBDD.SPAC_FASES, numexp);
			for(Object oFase : fasesCollection.toList()){
				resultado = (IItem) oFase;
			}
		} 
		catch(Exception e){
			logger.error("Error al recuperar la fase del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al recuperar la fase del expediente: " + numexp + ". " + e.getMessage(), e);
		}
		
		return resultado;
	}
	
	/**
	 * INICIO [Teresa] #Ticket#181# SIGEM metodo que devuelve la fase en la que se encuentra un tramite
	 * Apartir de del id de la fase sacar el nombre de la fase
	 * **/
	public static String getFase(IRuleContext rulectx, IEntitiesAPI entitiesAPI, int idFase){
		String fase = "";
		
		try {
			String sQuery = "WHERE ID="+idFase+"";
			IItemCollection itemCollection = entitiesAPI.queryEntities("SPAC_P_FASES", sQuery);
			Iterator<?> it = itemCollection.iterator();
	        
	        while (it.hasNext()) {
	        	IItem item = ((IItem)it.next());
	        	fase = item.getString("NOMBRE");
	        }
			
		} catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
		return fase;
	}
	
	/**
	 * FIN [Teresa] #Ticket#181# SIGEM metodo que devuelve la fase en la que se encuentra un tramite
	 * **/
	
	public static String getNombreFase(IClientContext cct, int idFase) throws ISPACRuleException{
		String nombre = "";
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IItemCollection fasesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_FASES, "WHERE ID = " + idFase);
			
			for(Object oFase : fasesCollection.toList()){
				nombre= ((IItem) oFase).getString("NOMBRE");
			}
		}
		catch(Exception e){
			logger.error("Error al recuperar el nombre de la fase con id: " + idFase+ ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al recuperar el nombre de la fase con id: " + idFase+ ". " + e.getMessage(), e);
		}
		return nombre;
	}
	
	public static String getNombreFaseActiva(IClientContext cct, String numexp) throws ISPACRuleException{
		String nombre = "";
		
		try{
			IItem faseActiva = getFase(cct, numexp);
			if(faseActiva != null){
				int idFase = faseActiva.getInt("ID_FASE");
			
				nombre = getNombreFase(cct, idFase);
			}
		}
		catch(Exception e){
			logger.error("Error al recuperar el nombre de la fase activa del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al recuperar el nombre de la fase activa del expediente: " + numexp + ". " + e.getMessage(), e);
		}
		
		return nombre;
	}
}
