package es.dipucr.sigem.api.rule.common.avisos;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.CTRuleDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.CircuitosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

/**
 * [dipucr-Felipe #665]
 * @since 06.02.2018
 * @author FELIPE
 * 
 * @deprecated La regla no funciona. No encuentro la forma de conseguir que recuperemos el id_regla
 * o el nombre en el cátalogo desde este contexto. Sólo tenemos acceso a la clase de la regla, pero la clase
 * para todas las instancias de la regla es la misma, por lo que no hay manera de saber cúal la llamó
 */
public class AvisoFinFirmaUsuarioDatosRegla implements IRule {
	
	private static final Logger LOGGER = Logger.getLogger(AvisoFinFirmaUsuarioDatosRegla.class);
	
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
			
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			DbCnt cnt = cct.getConnection();
			CTRuleDAO rule = CTRuleDAO.findRuleByClass(cnt, this.getClass().getCanonicalName());
			String nombreUsuario = rule.getString("DESCRIPCION");
			
			if (StringUtils.isEmpty(nombreUsuario)){
				LOGGER.error("En la configuración de la regla, debe incluir el nombre del usuario a avisar en el campo descripción.");
			}
			else{
				String sResponsable = UsuariosUtil.getCampoUsuario((ClientContext) cct, nombreUsuario, "ID");
				if (StringUtils.isEmpty(sResponsable)){
					LOGGER.error("En la configuración de la regla, el usuario incluido '" + nombreUsuario + "' no existe");
				}
				else{
					sResponsable = "1-" + sResponsable;
		
					int idDoc = rulectx.getInt("ID_DOCUMENTO");
					int idCircuito = rulectx.getInt("ID_CIRCUITO");
					IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
					String numexp = itemDocumento.getString("NUMEXP");
					
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
						sbMessage.append(CircuitosUtil.getNombreCircuito(cct, idCircuito));
						
						AvisosUtil.generarAviso(entitiesAPI, invesflowAPI.getProcess(numexp).getInt("ID"),
								numexp, sbMessage.toString(), sResponsable, cct);
		
						return true;
					}
				}
				cct.releaseConnection(cnt);
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
