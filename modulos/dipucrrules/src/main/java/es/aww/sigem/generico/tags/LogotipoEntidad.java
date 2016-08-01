package es.aww.sigem.generico.tags;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.templates.TemplateGraphicInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

/**
 * [eCenpri-Manu Ticket #307] ALSIGM3 Logotipo Entidad en Documentos
 * @author Manu
 *
 */
public class LogotipoEntidad implements IRule{
	private static final Logger logger = Logger.getLogger(LogotipoEntidad.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {    
			return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String entityId = null;
		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		if (info != null) {
			entityId = info.getOrganizationId();
		}
		InetAddress address = null;
		String dir = "";
		try {
			address = InetAddress.getLocalHost();
			dir = address.getHostName();
//			dir = address.getHostAddress(); //Si no funciona por DNS se pone esta y tira de la IP, pero mejor la otra.
		} catch (UnknownHostException e) {
			logger.error("Error al recupear la dirección de la máquina. " + e.getMessage(), e);
		}		

		String url = "http://" + dir + ":8080/SIGEM_TramitacionWeb/imagenesServlet/logos/logo.gif?idEntidad=" + entityId;
		logger.info("URL: "+url);

		TemplateGraphicInfo graphic = new TemplateGraphicInfo (url,false);
		return graphic;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}

