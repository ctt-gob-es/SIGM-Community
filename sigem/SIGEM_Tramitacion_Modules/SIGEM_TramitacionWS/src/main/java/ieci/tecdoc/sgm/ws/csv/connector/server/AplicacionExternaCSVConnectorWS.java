/**
 *
 */
package ieci.tecdoc.sgm.ws.csv.connector.server;

import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.audit.business.vo.AuditContext;
import ieci.tdw.ispac.audit.context.AuditContextHolder;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.utils.InetUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.ws.csv.connector.server.helper.DocumentsHelper;

import java.util.Locale;

import org.apache.axis.Constants;
import org.apache.axis.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author IECISA
 * @version $Revision$
 *
 */
public class AplicacionExternaCSVConnectorWS {

	/**
	 * Logger de la clase.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AplicacionExternaCSVConnectorWS.class);

	/*
	 *
	 * Es necesario conocer la entidad para conocer el Datasource que hay que
	 * utilizar para la conexi�n (MultiEntityDataSource)
	 */

	public boolean existeDocumento(java.lang.String csv, String entidad) throws java.rmi.RemoteException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AplicacionExternaCSVConnectorWS - existeDocumento(CSV: [{}], Entidad: [{}]) - Inicio", csv, entidad);
		}

		boolean existe = false;

		try {
			// Establecer la entidad para la multientidad
			setOrganizationUserInfo(entidad);

			// Crear el contexto de tramitaci�n para la consulta
			ClientContext context = createClientContext();

			// Auditor�a
			setAuditContext();

			// Comprobar si existe un documento con el CSV recibido
			existe = DocumentsHelper.existeDocumento(context, csv);

		} catch (Throwable e){
			LOGGER.error("Error al comprobar si existe el documento para el CSV: [" + csv + "]", e);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AplicacionExternaCSVConnectorWS - existeDocumento(CSV: [{}], Entidad: [{}]) - Fin", csv, entidad);
		}

		return existe;
	}

	public byte[] getContenidoDocumento(java.lang.String csv, String entidad)
			throws java.rmi.RemoteException {

		// Contenido del documento
		byte[] content = null;

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AplicacionExternaCSVConnectorWS - getContenidoDocumento(CSV: [{}], Entidad: [{}]) - Inicio", csv, entidad);
		}

		try {
			// Establecer la entidad para la multientidad
			setOrganizationUserInfo(entidad);

			// Crear el contexto de tramitaci�n para la consulta
			ClientContext context = createClientContext();

			// Auditor�a
			setAuditContext();

			content = DocumentsHelper.getContenidoDocumento(context, csv);

		} catch (Throwable e){
			LOGGER.error("Error al obtener el contenido del documento con CSV: [" + csv + "]", e);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AplicacionExternaCSVConnectorWS - getContenidoDocumento(CSV: [{}], Entidad: [{}]) - Fin", csv, entidad);
		}

		return content;
	}
	
	public boolean existeDocumentoOriginal(java.lang.String csv, String entidad) throws java.rmi.RemoteException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AplicacionExternaCSVConnectorWS - existeDocumento(CSV: [{}], Entidad: [{}]) - Inicio", csv, entidad);
		}

		boolean existe = false;

		try {
			// Establecer la entidad para la multientidad
			setOrganizationUserInfo(entidad);

			// Crear el contexto de tramitaci�n para la consulta
			ClientContext context = createClientContext();

			// Auditor�a
			setAuditContext();

			// Comprobar si existe un documento con el CSV recibido
			existe = DocumentsHelper.existeDocumentoOriginal(context, csv);

		} catch (Throwable e){
			LOGGER.error("Error al comprobar si existe el documento para el CSV: [" + csv + "]", e);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AplicacionExternaCSVConnectorWS - existeDocumento(CSV: [{}], Entidad: [{}]) - Fin", csv, entidad);
		}

		return existe;
	}
	
	public byte[] getContenidoDocumentoOriginal(java.lang.String csv, String entidad) throws java.rmi.RemoteException {

		// Contenido del documento
		byte[] content = null;

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AplicacionExternaCSVConnectorWS - getContenidoDocumento(CSV: [{}], Entidad: [{}]) - Inicio", csv, entidad);
		}

		try {
			// Establecer la entidad para la multientidad
			setOrganizationUserInfo(entidad);

			// Crear el contexto de tramitaci�n para la consulta
			ClientContext context = createClientContext();

			// Auditor�a
			setAuditContext();

			content = DocumentsHelper.getContenidoDocumentoOriginal(context, csv);

		} catch (Throwable e){
			LOGGER.error("Error al obtener el contenido del documento con CSV: [" + csv + "]", e);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("AplicacionExternaCSVConnectorWS - getContenidoDocumento(CSV: [{}], Entidad: [{}]) - Fin", csv, entidad);
		}

		return content;
	}

	/**
	 * Establecer la entidad para la multientidad.
	 *
	 * @param idEntidad Identificador de la entidad
	 */
	protected void setOrganizationUserInfo(String idEntidad) {

		OrganizationUserInfo info = new OrganizationUserInfo();
		info.setOrganizationId(idEntidad);
		info.getSpacPoolName();

		OrganizationUser.setOrganizationUserInfo(info);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Establecida la entidad en la sesion de tramitacion: " + idEntidad);
		}
	}

	/**
	 * Crear el contexto de tramitaci�n.
	 *
	 * @return Contexto de tramitaci�n.
	 * @throws Exception
	 */
	protected ClientContext createClientContext() throws Exception {

		ClientContext context = new ClientContext();
		context.setAPI(new InvesflowAPI(context));
		context.setLocale(new Locale("es", "ES"));

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Creado el contexto de tramitacion.");
		}

		return context;
	}

	/**
	 * Establecer el contexto de auditor�a para la tramitaci�n de expedientes.
	 */
	protected void setAuditContext() {

		// Auditor�a
		AuditContext auditContext = new AuditContext();

		auditContext.setUserHost("REMOTE WS CLIENT");
		auditContext.setUserIP(getUserIP());
		auditContext.setUser("TRAMITACION_WS_AplicacionExternaCSVConnectorWS");
		auditContext.setUserId("SYSTEM");

		// A�adir en el ThreadLocal el objeto AuditContext
		AuditContextHolder.setAuditContext(auditContext);
	}

	/**
	 * Obtener la IP para la auditor�a.
	 *
	 * @return IP remota o en caso de no obtenerla, la IP local.
	 */
	protected String getUserIP() {

		String userIP = "";

		MessageContext messageContext = MessageContext.getCurrentContext();
		if (messageContext != null) {
			userIP = (String) messageContext.getProperty(Constants.MC_REMOTE_ADDR);
		}

		if (StringUtils.isBlank(userIP)) {
			try {
				userIP = InetUtils.getLocalHostAddress();
			} catch (Exception e) {
				LOGGER.debug("Error al obtener la IP de LocalHost", e);
			}
		}

		return userIP;
	}
}
