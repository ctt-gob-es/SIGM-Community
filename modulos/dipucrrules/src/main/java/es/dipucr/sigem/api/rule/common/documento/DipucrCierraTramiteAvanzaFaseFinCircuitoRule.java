package es.dipucr.sigem.api.rule.common.documento;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.sign.SignCircuitHeaderDAO;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipucrCierraTramiteAvanzaFaseFinCircuitoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrCierraTramiteAvanzaFaseFinCircuitoRule.class);
	
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
			logger.info("INICIO DipucrCierraTramiteAvanzaFaseFinCircuitoRule");
			
			IClientContext ctx = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			ITXTransaction tx = invesflowAPI.getTransactionAPI();

			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			int idCircuito = rulectx.getInt("ID_CIRCUITO");//TODO
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);//TODO
			String numexp = itemDocumento.getString("NUMEXP");
			int idTramite = itemDocumento.getInt("ID_TRAMITE");			
			
//			if (FirmaUtil.isFirma(entitiesAPI, numexp, idDoc)) {
			String strEstadoFirma = itemDocumento.getString("ESTADOFIRMA");
			
			if (strEstadoFirma.equals(SignStatesConstants.FIRMADO)
					|| strEstadoFirma.equals(SignStatesConstants.FIRMADO_CON_REPAROS)
					|| strEstadoFirma.equals(SignStatesConstants.RECHAZADO))
			{
				// Generar un aviso en la bandeja de avisos electrónicos
				//Si ha sido firmado o firmado con reparos, cierra el trámite y avanza fase
				StringBuffer sbMessage = new StringBuffer();
				sbMessage.append("<b>");
				if (strEstadoFirma.equals(SignStatesConstants.FIRMADO)){
					sbMessage.append(_MENSAJE_FIRMADO);
					tx.closeTask(idTramite);
					ExpedientesUtil.avanzarFase(ctx, numexp);					
				}
				else if (strEstadoFirma.equals(SignStatesConstants.FIRMADO_CON_REPAROS)){
					sbMessage.append(_MENSAJE_FIRMADO_REPARO);
					tx.closeTask(idTramite);
					ExpedientesUtil.avanzarFase(ctx, numexp);
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
				sbMessage.append(getNombreCircuito(ctx, idCircuito));
				String destino = DocumentosUtil.getAutorUID(entitiesAPI, idDoc);
				
				AvisosUtil.generarAviso(entitiesAPI, invesflowAPI.getProcess(numexp).getInt("ID"),
						numexp, sbMessage.toString(), destino, ctx);				

				logger.info("FIN DipucrCierraTramiteAvanzaFaseFinCircuitoRule");
				return true;
			}
			
			return false;
			
		} catch (Exception e){
			
			throw new ISPACRuleException
				("Error notificar la firma al usuario creador del documento.", e);
		}
	}
	
	/**
	 * Ticket #93 - [ecenpri-Felipe] Añadir trámite y circuito al aviso
	 * Recupera el nombre del circuito a partir de su id
	 * @param ctx: Contexto
	 * @param idCircuito: Identificador del circuito
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings("rawtypes")
	private String getNombreCircuito(IClientContext ctx,
			int idCircuito) throws ISPACException {

		String nombreCircuito = "";
		IItemCollection collection = (IItemCollection) SignCircuitHeaderDAO.
				getCircuit(ctx.getConnection(), idCircuito).disconnect();

		Iterator it = collection.iterator();
		IItem item = null;
		if (it.hasNext()) {
			item = (IItem) it.next();
			nombreCircuito = item.getString("DESCRIPCION");
		}
		return nombreCircuito;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}