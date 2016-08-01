package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import es.dipucr.sigem.api.rule.common.utils.CircuitosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

/**
 * Ticket #32 - Aviso fin circuito de firma
 * @author Felipe-ecenpri
 * @since 03.06.2010
 * #93 Se añaden el trámite y el circuito al aviso
 * #365 Se añaden los estados "Rechazado" o "Firmado con Reparo", que no se tenían en cuenta
 */
public class AvisoFinFirmaAntonioLlanosCaudilloRule implements IRule {
	
//	private static final Logger logger = Logger.getLogger(AvisoFinFirmaRule.class);
	public static String _MENSAJE_FIRMADO = "Documento Firmado: ";
	public static String _MENSAJE_FIRMADO_REPARO = "Documento Firmado con Reparo: ";
	public static String _MENSAJE_RECHAZADO = "Documento Rechazado: ";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			
			IClientContext ctx = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			int idCircuito = rulectx.getInt("ID_CIRCUITO");//TODO
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);//TODO
			String numexp = itemDocumento.getString("NUMEXP");
			
//			if (FirmaUtil.isFirma(entitiesAPI, numexp, idDoc)) {
			String strEstadoFirma = itemDocumento.getString("ESTADOFIRMA");
			
			if (strEstadoFirma.equals(SignStatesConstants.FIRMADO)
					|| strEstadoFirma.equals(SignStatesConstants.FIRMADO_CON_REPAROS)
					|| strEstadoFirma.equals(SignStatesConstants.RECHAZADO))
			{
				// Generar un aviso en la bandeja de avisos electrónicos
				StringBuffer sbMessage = new StringBuffer();
				sbMessage.append("<b>");
				if (strEstadoFirma.equals(SignStatesConstants.FIRMADO)){
					sbMessage.append(_MENSAJE_FIRMADO);
				}
				else if (strEstadoFirma.equals(SignStatesConstants.FIRMADO_CON_REPAROS)){
					sbMessage.append(_MENSAJE_FIRMADO_REPARO);
				}
				else{
					sbMessage.append(_MENSAJE_RECHAZADO);
				}
				sbMessage.append(itemDocumento.getString("NOMBRE"));
				sbMessage.append("</b><br/>");
				IItem itemTramite = entitiesAPI.getTask(itemDocumento.getInt("ID_TRAMITE"));
				sbMessage.append("Trámite: ");
				sbMessage.append(itemTramite.getString("NOMBRE"));
				sbMessage.append("<br/>");
				sbMessage.append("Circuito de firma: ");
				sbMessage.append(CircuitosUtil.getNombreCircuito(ctx, idCircuito));
				String destino = "1-19";
				
				AvisosUtil.generarAviso(entitiesAPI, invesflowAPI.getProcess(numexp).getInt("ID"),
						numexp, sbMessage.toString(), destino, ctx);

				return true;
			}
			
			return false;
			
		} catch (Exception e){
			
			throw new ISPACRuleException
				("Error notificar la firma al usuario creador del documento.", e);
		}
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
