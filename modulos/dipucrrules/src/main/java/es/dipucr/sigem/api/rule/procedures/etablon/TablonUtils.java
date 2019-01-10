package es.dipucr.sigem.api.rule.procedures.etablon;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class TablonUtils {

	public static boolean generarAvisoUsuario(IRuleContext rulectx, String cabecera, 
			String motivo, IItem itemDocumento, IItem itemPublicacion) 
		throws Exception
	{
		//*********************************************
		IClientContext ctx = rulectx.getClientContext();
		    IInvesflowAPI invesFlowAPI = ctx.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		//*********************************************
		
		String numexp = rulectx.getNumExp();
		int idDoc = itemDocumento.getKeyInt();
		
		StringBuffer sbMessage = new StringBuffer();
		sbMessage.append("<b>");
		sbMessage.append(cabecera);
		sbMessage.append("</b><br/>");
		if (StringUtils.isNotEmpty(motivo)){
			sbMessage.append("Motivo: ");
			sbMessage.append(motivo);
			sbMessage.append("<br/>");
		}
		sbMessage.append("Documento: ");
		sbMessage.append(itemDocumento.getString("NOMBRE"));
		sbMessage.append("<br/>");
		sbMessage.append("Título: ");
		sbMessage.append(itemPublicacion.getString("TITULO"));
		
		String destino = DocumentosUtil.getAutorUID(entitiesAPI, idDoc);
		AvisosUtil.generarAviso(entitiesAPI, invesFlowAPI.getProcess(numexp).getInt("ID"),
				numexp, sbMessage.toString(), destino, ctx);
	
		return true;
	}
}
