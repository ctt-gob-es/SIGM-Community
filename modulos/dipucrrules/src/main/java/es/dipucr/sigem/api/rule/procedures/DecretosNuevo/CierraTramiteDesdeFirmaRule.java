package es.dipucr.sigem.api.rule.procedures.DecretosNuevo;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.BloqueosFirmaUtil;
import es.dipucr.sigem.api.rule.common.utils.CircuitosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class CierraTramiteDesdeFirmaRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(CierraTramiteDesdeFirmaRule.class);
	public static String _MENSAJE_FIRMADO_REPARO = "Documento Firmado con Reparo: ";
	public static String _MENSAJE_RECHAZADO = "Documento Rechazado: ";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			
			//*********************************************
			ClientContext cct = (ClientContext) rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			if(estadoFirmaRechazadoReparo(rulectx)){
				//Obtenemos los datos de la solicitud de licencias
				int idDoc = rulectx.getInt("ID_DOCUMENTO");
				
				//[dipucr_Felipe #1115] Rehacemos la regla
				IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
				int idTramite = itemDocumento.getInt("ID_TRAMITE");
				
				BloqueosFirmaUtil.cerrarSesionYEliminarBloqueos(cct, idTramite); //[dipucr-Felipe #1115]
				TramitesUtil.cerrarTramite(idTramite, rulectx);
			}
			
		}catch (Exception e) {
			logger.error("Error en la generación de los trámites y envío a firma de los decretos. " + e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación de los trámites y envío a firma de los decretos. " + e.getMessage(), e);
		}
		return new Boolean (true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	private boolean estadoFirmaRechazadoReparo(IRuleContext rulectx) throws ISPACRuleException{
		boolean cierraTramite = true;
		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			int idDoc = rulectx.getInt("ID_DOCUMENTO");
			int idCircuito = rulectx.getInt("ID_CIRCUITO");//TODO
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);//TODO
			String numexp = itemDocumento.getString("NUMEXP");
			
//			if (FirmaUtil.isFirma(entitiesAPI, numexp, idDoc)) {
			String strEstadoFirma = itemDocumento.getString("ESTADOFIRMA");
			
			if ( strEstadoFirma.equals(SignStatesConstants.FIRMADO_CON_REPAROS)
					|| strEstadoFirma.equals(SignStatesConstants.RECHAZADO))
			{
				cierraTramite = false;
				// Generar un aviso en la bandeja de avisos electrónicos
				StringBuffer sbMessage = new StringBuffer();
				sbMessage.append("<b>");
				if (strEstadoFirma.equals(SignStatesConstants.FIRMADO_CON_REPAROS)){
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
				sbMessage.append(CircuitosUtil.getNombreCircuito(cct, idCircuito));
				String destino = DocumentosUtil.getAutorUID(entitiesAPI, idDoc);
				
				AvisosUtil.generarAviso(entitiesAPI, invesFlowAPI.getProcess(numexp).getInt("ID"),
						numexp, sbMessage.toString(), destino, cct);
			}
			
		}catch (Exception e) {
			logger.error("Error en la generación de los trámites y envío a firma de los decretos. " + e.getMessage(), e);
			throw new ISPACRuleException("Error en la generación de los trámites y envío a firma de los decretos. " + e.getMessage(), e);
		}
		return cierraTramite;
	}

	public boolean validate(IRuleContext rulectx) {
		return true;
	}

}
