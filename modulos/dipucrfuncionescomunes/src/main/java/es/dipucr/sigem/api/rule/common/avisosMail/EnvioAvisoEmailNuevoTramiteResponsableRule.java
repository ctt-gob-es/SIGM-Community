package es.dipucr.sigem.api.rule.common.avisosMail;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.resp.User;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.idoc.admin.api.ObjFactory;
import ieci.tecdoc.idoc.admin.internal.UserImpl;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class EnvioAvisoEmailNuevoTramiteResponsableRule extends DipucrEnviaDocEmailConAcuse{

	protected static final Logger LOGGER = Logger.getLogger(EnvioAvisoEmailNuevoTramiteResponsableRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try{
			VAR_EMAILS = "";
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IRespManagerAPI respManagerAPI = invesflowAPI.getRespManagerAPI();
			numexp = rulectx.getNumExp();
			
			String entidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
			
			IItem pTramite = TramitesUtil.getPTramiteById(rulectx, rulectx.getTaskProcedureId());
			String idResponsable = pTramite.getString("ID_RESP");			
			String nombreTramite = pTramite.getString("NOMBRE");
			if(StringUtils.isEmpty(nombreTramite)){
				nombreTramite = "";
			}
			
			IResponsible responsable = respManagerAPI.getResp(idResponsable);

			if(responsable.isUser()){
				String usrName = responsable.getName();
				UserImpl user=(UserImpl) ObjFactory.createUser();
				try {
					user.load(usrName, entidad);
					VAR_EMAILS = user.getUserData().getEmail();
				} catch (Exception e2) {
					LOGGER.error("Error al recuperar el correo electrónico del usuario: "+ usrName + ". " + e2.getMessage(), e2);								
				}
			} else {
				IItemCollection usuariosCollection = responsable.getRespUsers();
				Iterator<?> usuarioIterator = usuariosCollection.iterator();
				
				while (usuarioIterator.hasNext()){
					User usuario = (User) usuarioIterator.next();
					String usrName = usuario.getName();
	
					UserImpl user=(UserImpl) ObjFactory.createUser();
					try {
						user.load(usrName, entidad);
						VAR_EMAILS += user.getUserData().getEmail();
					
					} catch (Exception e2) {
						LOGGER.error("Error al recuperar el correo electrónico del usuario: "+ usuario + ". " + e2.getMessage(), e2);								
					}
					if(usuarioIterator.hasNext()){
						VAR_EMAILS += MailUtil.DEFAULT_MAIL_SEPARATOR;
					}
				}
			}
				
			nombreNotif = responsable.getName();
			contenido = "Tiene un nuevo Trámite " + nombreTramite + " en AL-SIGM a visualizar con el número de expediente " + numexp;
			asunto = "[AL-SIGM] Tiene un nuevo Trámite " + nombreTramite + " en AL-SIGM a visualizar con el número de expediente " + numexp;
			
			conDocumento = false;
		} catch (ISPACException e) {
			LOGGER.error("Error al enviar email a: " + nombreNotif + ", en el expediente: " + numexp + ". "+ e.getMessage(), e);
		}
		
		return true;
	}
}
