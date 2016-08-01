package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.ArrayList;
import java.util.Date;

public class AvisosUtil {


	public static int _MAXLENGTH_MESSAGE = 254;

	/**
	 * Borra los avisos de un expediente que contengan un cierto mensaje
	 * @param rulectx
	 * @param mensaje
	 * @return
	 * @throws ISPACRuleException
	 * @author Felipe-ecenpri
	 * Ticket #32 - Aviso fin circuito de firma
	 */
	public static boolean borrarAvisos(IRuleContext rulectx, String mensaje) throws ISPACRuleException{
		
		try {
			
			IClientContext ctx = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			String numexp = rulectx.getNumExp();
			String strQuery = "WHERE ID_EXPEDIENTE='" + numexp + 
				"' AND MENSAJE LIKE '%" + mensaje + "%'";
			entitiesAPI.deleteEntities("SPAC_AVISOS_ELECTRONICOS", strQuery);
			
			return true;
			
		} catch (Exception e){
			
			throw new ISPACRuleException
				("Error notificar la firma al usuario creador del documento.", e);
		}
	}
	
	/**
	 * Borra los avisos de un expediente que contengan un cierto mensaje
	 * @param rulectx
	 * @param mensaje
	 * @return
	 * @throws ISPACRuleException
	 * @author Felipe-ecenpri
	 * Ticket #365
	 */
	public static boolean borrarAvisos(IRuleContext rulectx, ArrayList<String> listMensajes) throws ISPACRuleException{
		
		try {
			
			IClientContext ctx = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			String numexp = rulectx.getNumExp();
			StringBuffer sbQuery = new StringBuffer();
			sbQuery.append("WHERE ID_EXPEDIENTE='");
			sbQuery.append(numexp);
			sbQuery.append("' AND (");
			String mensaje = null;
			for (int i = 0; i < listMensajes.size(); i++){
				mensaje = listMensajes.get(i);
				sbQuery.append("MENSAJE LIKE '%");
				sbQuery.append(mensaje);
				sbQuery.append("%'");
				if (i != (listMensajes.size() - 1)){
					sbQuery.append(" OR ");
				}
			}
			sbQuery.append(")");
			entitiesAPI.deleteEntities("SPAC_AVISOS_ELECTRONICOS", sbQuery.toString());
			
			return true;
			
		} catch (Exception e){
			
			throw new ISPACRuleException
				("Error notificar la firma al usuario creador del documento.", e);
		}
	}
	
	/**
	 * Genera un nuevo aviso en la tabla SPAC_AVISOS_ELECTRONICOS
	 * @param entitiesAPI
	 * @param processId
	 * @param numexp
	 * @param message
	 * @param destiny
	 * @param ctx
	 * @throws ISPACException
	 */
	public static void generarAviso(IEntitiesAPI entitiesAPI,
			int processId, String numexp, String message, String destiny,
			IClientContext ctx) throws ISPACException
	{
	
		IItem notice = entitiesAPI.createEntity("SPAC_AVISOS_ELECTRONICOS", "");

		notice.set("ID_PROC", processId);
		notice.set("TIPO_DESTINATARIO", 1);
		notice.set("FECHA", new Date());
		notice.set("ID_EXPEDIENTE", numexp);
		notice.set("ESTADO_AVISO", 0);
		
		//Controlamos que el mensaje no supere el máximo de la columna
		if (message.length() > _MAXLENGTH_MESSAGE){
			message = message.substring(0, (_MAXLENGTH_MESSAGE - 5));
			message = message + "...";
		}
		notice.set("MENSAJE", message);
		
		// El tipo de aviso se pone como registro telemático (RT).
		notice.set("TIPO_AVISO", 2);
		notice.set("UID_DESTINATARIO", destiny);

		notice.store(ctx);
	}
	
}
