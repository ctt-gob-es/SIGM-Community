package es.gob.afirma.signfolder.server.proxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import es.gob.afirma.core.misc.Base64;
import es.gob.afirma.signfolder.server.proxy.SignLine.SignLineType;

/**
 * Factor&iacute;a para la creaci&oacute;n de respuestas XML hacia el
 * dispositivo cliente de firmas multi-fase.
 * 
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s
 */
final class XmlResponsesFactory {

	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; //$NON-NLS-1$

	private static final String XML_PRESIGN_OPEN = "<pres>"; //$NON-NLS-1$
	private static final String XML_PRESIGN_CLOSE = "</pres>"; //$NON-NLS-1$
	private static final String XML_POSTSIGN_OPEN = "<posts>"; //$NON-NLS-1$
	private static final String XML_POSTSIGN_CLOSE = "</posts>"; //$NON-NLS-1$

	private XmlResponsesFactory() {
		// No instanciable
	}

	static String createPresignResponse(final TriphaseRequestBean triRequest) {
		final StringBuilder sb = new StringBuilder(XML_HEADER);
		sb.append(XML_PRESIGN_OPEN);
		for (int i = 0; i < triRequest.size(); i++) {
			sb.append(createSingleReqPresignNode(triRequest.get(i)));
		}
		sb.append(XML_PRESIGN_CLOSE);
		return sb.toString();
	}

	private static String createSingleReqPresignNode(final TriphaseRequest triphaseRequest) {
		final StringBuilder sb = new StringBuilder("<req id=\""); //$NON-NLS-1$
		sb.append(triphaseRequest.getRef());
		sb.append("\" status=\""); //$NON-NLS-1$
		if (triphaseRequest.isStatusOk()) {
			sb.append("OK\">"); //$NON-NLS-1$
			for (final TriphaseSignDocumentRequest docReq : triphaseRequest) {
				sb.append("<doc docid=\"").append(docReq.getId()) //$NON-NLS-1$
						.append("\" cop=\"").append(docReq.getCryptoOperation()) //$NON-NLS-1$
						.append("\" sigfrmt=\"").append(docReq.getSignatureFormat()) //$NON-NLS-1$
						.append("\" mdalgo=\"").append(docReq.getMessageDigestAlgorithm()).append("\">") //$NON-NLS-1$ //$NON-NLS-2$
						.append("<params>") //$NON-NLS-1$
						.append(docReq.getParams() != null ? escapeXmlCharacters(docReq.getParams()) : "") //$NON-NLS-1$
						.append("</params>") //$NON-NLS-1$
						.append("<result>"); //$NON-NLS-1$
				// Ahora mismo las firmas se envian de una en una, asi que
				// usamos directamente la primera de ellas
				final Map<String, String> triSign = docReq.getPartialResult().getTriSigns().get(0).getDict();
				for (final String key : triSign.keySet().toArray(new String[triSign.size()])) {
					sb.append("<p n=\"").append(key).append("\">").append(triSign.get(key)).append("</p>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
				sb.append("</result></doc>"); //$NON-NLS-1$
			}
			sb.append("</req>"); //$NON-NLS-1$
		} else {
			String exceptionb64 = null;
			final Throwable t = triphaseRequest.getThrowable();
			if (t != null) {
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
					t.printStackTrace(new PrintWriter(baos));
					exceptionb64 = Base64.encode(baos.toByteArray());
				} catch (final IOException e) {
					// No hacemos nada
				}
			}

			if (exceptionb64 != null) {
				sb.append("KO\" exceptionb64=\"") //$NON-NLS-1$
						.append(exceptionb64).append("\" />"); //$NON-NLS-1$
			} else {
				sb.append("KO\" />"); //$NON-NLS-1$
			}
		}
		return sb.toString();
	}

	public static String createPostsignResponse(final TriphaseRequestBean triRequest) {
		final StringBuilder sb = new StringBuilder(XML_HEADER);
		sb.append(XML_POSTSIGN_OPEN);
		for (int i = 0; i < triRequest.size(); i++) {
			sb.append(createSingleReqPostsignNode(triRequest.get(i)));
		}
		sb.append(XML_POSTSIGN_CLOSE);
		return sb.toString();
	}

	private static String createSingleReqPostsignNode(final TriphaseRequest triphaseRequest) {
		final StringBuilder sb = new StringBuilder("<req id=\""); //$NON-NLS-1$
		sb.append(triphaseRequest.getRef()).append("\" status=\""). //$NON-NLS-1$
				append(triphaseRequest.isStatusOk() ? "OK" : "KO"). //$NON-NLS-1$ //$NON-NLS-2$
				append("\"/>"); //$NON-NLS-1$

		return sb.toString();
	}

	static String createRequestsListResponse(final PartialSignRequestsList partialSignRequests) {

		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER);
		sb.append("<list n='"); //$NON-NLS-1$
		sb.append(partialSignRequests.getTotalSignRequests());
		sb.append("'>"); //$NON-NLS-1$

		for (final SignRequest sr : partialSignRequests.getCurrentSignRequests()) {
			sb.append("<rqt id=\"").append(sr.getId()) //$NON-NLS-1$
					.append("\" priority=\"").append(sr.getPriority()) //$NON-NLS-1$
					.append("\" workflow=\"").append(sr.isWorkflow()) //$NON-NLS-1$
					.append("\" forward=\"").append(sr.isForward()) //$NON-NLS-1$
					.append("\" type=\"").append(sr.getType()) //$NON-NLS-1$
					.append("\">"); //$NON-NLS-1$
			
			String auxSubj = "";
			auxSubj = sr.getSubject();
			auxSubj = deleteHtmlCharacters(auxSubj);

			sb.append("<subj>").append(escapeXmlCharacters(auxSubj)).append("</subj>"); //$NON-NLS-1$ //$NON-NLS-2$
			//Agustin, en vez del sender meto la aplicacion agrego un campo nuevo al objeto SignRequest
			sb.append("<snder>").append(escapeXmlCharacters(sr.getAplicacion())).append("</snder>"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append("<view>").append(sr.getView()).append("</view>"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append("<date>").append(sr.getDate()).append("</date>"); //$NON-NLS-1$ //$NON-NLS-2$
			if (sr.getExpDate() != null) {
				sb.append("<expdate>").append(sr.getExpDate()).append("</expdate>"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			sb.append("<docs>"); //$NON-NLS-1$
			for (final SignRequestDocument doc : sr.getDocumentsRequests()) {
				sb.append("<doc docid=\"").append(doc.getId()).append("\">"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append("<nm>").append(escapeXmlCharacters(doc.getName())).append("</nm>"); //$NON-NLS-1$ //$NON-NLS-2$
				if (doc.getSize() != null) {
					sb.append("<sz>").append(doc.getSize()).append("</sz>"); //$NON-NLS-1$ //$NON-NLS-2$
				}
				sb.append("<mmtp>").append(escapeXmlCharacters(doc.getMimeType())).append("</mmtp>"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append("<sigfrmt>").append(escapeXmlCharacters(doc.getSignFormat())).append("</sigfrmt>"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append("<mdalgo>").append(escapeXmlCharacters(doc.getMessageDigestAlgorithm())).append("</mdalgo>"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append("<params>").append(doc.getParams() != null ? escapeXmlCharacters(doc.getParams()) : "") //$NON-NLS-1$ //$NON-NLS-2$
						.append("</params>"); //$NON-NLS-1$
				sb.append("</doc>"); //$NON-NLS-1$
			}
			sb.append("</docs>"); //$NON-NLS-1$
			sb.append("</rqt>"); //$NON-NLS-1$
		}

		sb.append("</list>"); //$NON-NLS-1$

		return sb.toString();
	}

	static String createRejectsResponse(final RequestResult[] requestResults) {

		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER);
		sb.append("<rjcts>"); //$NON-NLS-1$
		for (final RequestResult rr : requestResults) {
			sb.append("<rjct id=\"").append(rr.getId()) //$NON-NLS-1$
					.append("\" status=\"").append(rr.isStatusOk() ? "OK" : "KO") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					.append("\"/>"); //$NON-NLS-1$
		}
		sb.append("</rjcts>"); //$NON-NLS-1$

		return sb.toString();
	}

	/**
	 * Crea un XML con la informaci&oacute;n de detalle de una solicitud de
	 * firma.
	 * 
	 * @param requestDetails
	 *            Detalle de la solicitud.
	 * @return XML con los datos detallados de la solicitud.
	 */
	static String createRequestDetailResponse(final Detail requestDetails) {

		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER);

		sb.append("<dtl id=\"").append(requestDetails.getId()) //$NON-NLS-1$
				.append("\" priority=\"").append(requestDetails.getPriority()) //$NON-NLS-1$
				.append("\" workflow=\"").append(requestDetails.isWorkflow()) //$NON-NLS-1$
				.append("\" forward=\"").append(requestDetails.isForward()) //$NON-NLS-1$
				.append("\" type=\"").append(requestDetails.getType()) //$NON-NLS-1$
				.append("\">"); //$NON-NLS-1$
		
		String auxSubj = "";
		auxSubj = requestDetails.getSubject();
		auxSubj = deleteHtmlCharacters(auxSubj);

		sb.append("<subj>").append(escapeXmlCharacters(auxSubj)).append("</subj>"); //$NON-NLS-1$ //$NON-NLS-2$

		if (requestDetails.getText() != null) {						
			
			//[DipucR Agustín] Sacar enlace para consulta por el indice electronico			
			String auxMsg = requestDetails.getText();
			String result= "";
			String auxMsgSplit[] = null;
			String caracterSeparador = "\"";
			auxMsgSplit = auxMsg.split(caracterSeparador);
			result = result.concat("Puede consultar el expediente completo en la siguiente dirección, copie y pegue la url en el navegador de su móvil:\n\n");
			result = result.concat(auxMsgSplit[1]);				
			
			sb.append("<msg>").append(escapeXmlCharacters(result)).append("</msg>"); //$NON-NLS-1$ //$NON-NLS-2$
			
		}

		sb.append("<snders>"); //$NON-NLS-1$
		for (final String sender : requestDetails.getSenders()) {
			sb.append("<snder>").append(escapeXmlCharacters(sender)).append("</snder>"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.append("</snders>"); //$NON-NLS-1$

		sb.append("<date>").append(requestDetails.getDate()).append("</date>"); //$NON-NLS-1$ //$NON-NLS-2$
		if (requestDetails.getExpDate() != null) {
			sb.append("<expdate>").append(requestDetails.getExpDate()).append("</expdate>"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.append("<app>").append(escapeXmlCharacters(requestDetails.getApp())).append("</app>"); //$NON-NLS-1$ //$NON-NLS-2$
		if (requestDetails.getRejectReason() != null) {
			sb.append("<rejt>").append(escapeXmlCharacters(requestDetails.getRejectReason())).append("</rejt>"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.append("<ref>").append(escapeXmlCharacters(requestDetails.getRef())).append("</ref>"); //$NON-NLS-1$ //$NON-NLS-2$

		sb.append("<signlinestype>").append(requestDetails.getSignLinesFlow()).append("</signlinestype>"); //$NON-NLS-1$ //$NON-NLS-2$

		sb.append("<sgnlines>"); //$NON-NLS-1$
		for (final SignLine signLine : requestDetails.getSignLines()) {
			sb.append("<sgnline"); //$NON-NLS-1$
			if (signLine.getType() == SignLineType.VISTOBUENO) {
				sb.append(" type='VISTOBUENO'"); //$NON-NLS-1$
			}
			sb.append(">"); //$NON-NLS-1$
			for (final String receiver : signLine.getSigners()) {
				sb.append("<rcvr>").append(escapeXmlCharacters(receiver)).append("</rcvr>"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			sb.append("</sgnline>"); //$NON-NLS-1$
		}
		sb.append("</sgnlines>"); //$NON-NLS-1$

		sb.append("<docs>"); //$NON-NLS-1$
		for (final SignRequestDocument doc : requestDetails.getDocs()) {
			sb.append("<doc docid=\"").append(doc.getId()).append("\">"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append("<nm>").append(escapeXmlCharacters(doc.getName())).append("</nm>"); //$NON-NLS-1$ //$NON-NLS-2$
			if (doc.getSize() != null) {
				sb.append("<sz>").append(doc.getSize()).append("</sz>"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			sb.append("<mmtp>").append(doc.getMimeType()).append("</mmtp>"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append("<sigfrmt>").append(doc.getSignFormat()).append("</sigfrmt>"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append("<mdalgo>").append(doc.getMessageDigestAlgorithm()).append("</mdalgo>"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append("<params>").append(doc.getParams() != null ? escapeXmlCharacters(doc.getParams()) : "") //$NON-NLS-1$ //$NON-NLS-2$
					.append("</params>"); //$NON-NLS-1$
			sb.append("</doc>"); //$NON-NLS-1$
		}
		sb.append("</docs>"); //$NON-NLS-1$

		// Los adjuntos son opcionales
		boolean firstTime = true;
		for (final SignRequestDocument att : requestDetails.getAttached()) {
			if (firstTime) {
				sb.append("<attachedList>"); //$NON-NLS-1$
				firstTime = false;
			}
			sb.append("<attached docid=\"").append(att.getId()).append("\">"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append("<nm>").append(escapeXmlCharacters(att.getName())).append("</nm>"); //$NON-NLS-1$ //$NON-NLS-2$
			if (att.getSize() != null) {
				sb.append("<sz>").append(att.getSize()).append("</sz>"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			sb.append("<mmtp>").append(att.getMimeType()).append("</mmtp>"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append("<sigfrmt>").append(att.getSignFormat()).append("</sigfrmt>"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append("<mdalgo>").append(att.getMessageDigestAlgorithm()).append("</mdalgo>"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append("<params>").append(att.getParams() != null ? escapeXmlCharacters(att.getParams()) : "") //$NON-NLS-1$ //$NON-NLS-2$
					.append("</params>"); //$NON-NLS-1$
			sb.append("</attached>"); //$NON-NLS-1$
		}
		if (!firstTime) {
			sb.append("</attachedList>"); //$NON-NLS-1$
		}

		sb.append("</dtl>"); //$NON-NLS-1$

		return sb.toString();
	}

	/**
	 * Crea un XML con la informaci&oacute;n para configurar la
	 * aplicaci&oacute;n.
	 * 
	 * @param appConfig
	 *            Configuraci&oacute;n..
	 * @return XML con la configuraci&oacute;n..
	 */
	static String createConfigurationResponse(final AppConfiguration appConfig) {

		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER);

		sb.append("<appConf>"); //$NON-NLS-1$
		for (int i = 0; i < appConfig.getAppIdsList().size(); i++) {
			sb.append("<app id=\"").append(appConfig.getAppIdsList().get(i)).append("\">"); //$NON-NLS-1$//$NON-NLS-2$
			sb.append(escapeXmlCharacters(appConfig.getAppNamesList().get(i)));
			sb.append("</app>"); //$NON-NLS-1$
		}
		sb.append("</appConf>"); //$NON-NLS-1$

		return sb.toString();
	}

	/**
	 * Crea un XML con la informaci&oacute;n para configurar la
	 * aplicaci&oacute;n.
	 * 
	 * @param appConfig
	 *            Configuraci&oacute;n..
	 * @return XML con la configuraci&oacute;n..
	 */
	static String createConfigurationNewResponse(final AppConfiguration appConfig) {

		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER);

		sb.append("<appConf>"); //$NON-NLS-1$
		for (int i = 0; i < appConfig.getAppIdsList().size(); i++) {
			sb.append("<app id=\"").append(appConfig.getAppIdsList().get(i)).append("\">"); //$NON-NLS-1$//$NON-NLS-2$
			sb.append(escapeXmlCharacters(appConfig.getAppNamesList().get(i)));
			sb.append("</app>"); //$NON-NLS-1$
		}
		if (appConfig.getRolesList() != null && appConfig.getRolesList().size() > 0) {
			sb.append("<roles>"); //$NON-NLS-1$
			List<String> rls = appConfig.getRolesList();
			for (int e = 0; e < rls.size(); e++) {
				sb.append("<role>"); //$NON-NLS-1$
				sb.append(rls.get(e));
				sb.append("</role>"); //$NON-NLS-1$
			}
			sb.append("</roles>"); //$NON-NLS-1$
		}
		sb.append("</appConf>"); //$NON-NLS-1$

		return sb.toString();
	}

	public static String createApproveRequestsResponse(final ApproveRequestList approveRequests) {
		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER);

		sb.append("<apprq>"); //$NON-NLS-1$
		for (final ApproveRequest req : approveRequests) {
			sb.append("<r id=\"").append(req.getRequestTagId()) //$NON-NLS-1$
					.append("\" ok=\"").append(req.isOk() ? "OK" : "KO").append("\"/>"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		sb.append("</apprq>"); //$NON-NLS-1$

		return sb.toString();
	}

	public static String createRequestLoginResponse(final LoginRequestData loginRequestData, final String ssid) {
		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER).append("<lgnrq id='").append(loginRequestData.getId()); //$NON-NLS-1$
		if (ssid != null) {
			sb.append("' ssid='").append(ssid); //$NON-NLS-1$
		}
		sb.append("'>") //$NON-NLS-1$
				.append(Base64.encode(loginRequestData.getData())).append("</lgnrq>"); //$NON-NLS-1$

		return sb.toString();
	}

	public static String createRequestClaveLoginResponse(final String claveUrl, final String sessionId) {
		final StringBuilder sb = new StringBuilder().append(XML_HEADER).append("<lgnrq>") //$NON-NLS-1$
				.append("<url>") //$NON-NLS-1$
				.append(escapeXmlCharacters(claveUrl)).append("</url>"); //$NON-NLS-1$
		if (sessionId != null) {
			sb.append("<sessionId>") //$NON-NLS-1$
					.append(sessionId).append("</sessionId>"); //$NON-NLS-1$
		}
		sb.append("</lgnrq>"); //$NON-NLS-1$

		return sb.toString();
	}

	public static String createValidateLoginResponse(final ValidateLoginResult validateLoginData) {
		final StringBuilder sb = new StringBuilder().append(XML_HEADER).append("<vllgnrq ok='") //$NON-NLS-1$
				.append(validateLoginData.isLogged()).append("'"); //$NON-NLS-1$
		if (validateLoginData.isLogged()) {
			sb.append(" dni='").append(validateLoginData.getDni()).append("'"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			sb.append(" er='").append(validateLoginData.getError().toString()).append("'"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.append("/>"); //$NON-NLS-1$

		return sb.toString();
	}

	public static String createRequestLogoutResponse() {
		final StringBuilder sb = new StringBuilder().append(XML_HEADER).append("<lgorq/>"); //$NON-NLS-1$

		return sb.toString();
	}

	public static String createNotificationRegistryResponse(final NotificationRegistryResult registryResult) {
		final StringBuilder sb = new StringBuilder().append(XML_HEADER).append("<reg ok='") //$NON-NLS-1$
				.append(registryResult.isRegistered()).append("'"); //$NON-NLS-1$
		if (!registryResult.isRegistered()) {
			sb.append(" err='").append(registryResult.getError()).append("'"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.append("/>"); //$NON-NLS-1$

		return sb.toString();
	}

	/**
	 * Construye la respuesta del servicio de prefirma con ClaveFirma.
	 * 
	 * @param trId
	 *            Identificador de transacci&oacute;n.
	 * @param url
	 *            URL de redirecci&oacute;n.
	 * @return XML con la respuesta de la operacion de prefirma con ClaveFirma.
	 */
	public static String createFireSignResponse(final boolean status, final int errorType) {

		final StringBuilder resp = new StringBuilder(XML_HEADER).append("<cfsig ok='").append(status).append("'"); //$NON-NLS-1$ //$NON-NLS-2$
		if (!status) {
			resp.append(" er='").append(errorType).append("'"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		resp.append("/>"); //$NON-NLS-1$

		return resp.toString();
	}

	/**
	 * Construye el XML con el resultado de una operaci&oacute;n de datos de
	 * carga en FIRe.
	 * 
	 * @param requestInfo
	 *            Resultado de carga de datos.
	 * @return XML con el resultado de la operaci&oacute;n de carga.
	 */
	public static String createFireLoadDataResponse(final FireLoadDataResult requestInfo) {
		final StringBuilder sb = new StringBuilder().append(XML_HEADER).append("<cfrqt ok='") //$NON-NLS-1$
				.append(requestInfo.isStatusOk()).append("'>") //$NON-NLS-1$
				.append(escapeXmlCharacters(requestInfo.getUrlRedirect())).append("</cfrqt>"); //$NON-NLS-1$

		return sb.toString();
	}

	/**
	 * Protege las cadenas que pueden crear malformaciones en un XML para que su
	 * contenido no sea tratado por el procesador XML.
	 * 
	 * @param text
	 *            Texto a proteger.
	 * @return Cadena protegida.
	 */
	private static String escapeXmlCharacters(final String text) {
		return text == null ? null : "<![CDATA[" + text + "]]>"; //$NON-NLS-1$//$NON-NLS-2$
	}
	
	/**
	 * Quita caracteres html de las cadenas
	 * 
	 * @param text
	 *            Texto a proteger.
	 * @return Cadena protegida.
	 */
	private static String deleteHtmlCharacters(final String text) {
		
		String result = "";
		result = text;
		result = result.replaceAll("<b>", "");
		result = result.replaceAll("</b>", "");
		result = result.replaceAll("<br/>", "\n");
		result = result.replaceAll("</br>", "");
		
		return result; 
	}

	/**
	 * Genera la respuesta XML del servicio de recuperación de usuarios por
	 * role.
	 * 
	 * @param result
	 *            Resultado obtenido de Portafirmas Web.
	 * @return la respuesta XML para Portafirmas Android.
	 */
	public static String createGetUserByRoleResponse(GetUserByRoleResult result) {
		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER);
		sb.append("<rsgtrl");
		sb.append(" n=\"");
		sb.append(result.getRoles().size());
		sb.append("\">");
		if (result.isError()) {
			sb.append("<err>");
			String msg;
			switch (result.getErrorType()) {
			case 1:
				msg = "Error de comunicación con Portafirmas Web";
				break;
			case 2:
				msg = "Eror en la petición realizada a Portafirmas Web";
				break;
			case 3:
				msg = "Error en el procesado de la petición/respuesta de Portafirmas Web";
				break;
			default:
				msg = "Error desconocido";

			}
			sb.append(msg);
			sb.append("</err>");
		} else {
			sb.append("roles");
			addRolesToResult(sb, result.getRoles());
			sb.append("/roles");
		}
		sb.append("</rsgtrl>");
		return sb.toString();
	}

	/**
	 * Añade la lista de usuarios como XML al stringBuilder recibido como
	 * parámetro.
	 * 
	 * @param sb
	 *            Objeto donde se añadiran los usuarios.
	 * @param roles
	 *            Lista de usuarios a añadir.
	 */
	private static void addRolesToResult(StringBuilder sb, List<Role> roles) {
		if (roles != null) {
			for (int i = 0; i < roles.size(); i++) {
				sb.append("<role>");
				Role role = roles.get(i);
				sb.append("<name>");
				sb.append(role.getName());
				sb.append("</name>");
				sb.append("<surname>");
				sb.append(role.getSurname());
				sb.append("</surname>");
				sb.append("<secondSurname>");
				sb.append(role.getSecondSurname());
				sb.append("</secondSurname>");
				sb.append("<LDAPUser>");
				sb.append(role.getLDAPUser());
				sb.append("</LDAPUser>");
				sb.append("<ID>");
				sb.append(role.getID());
				sb.append("</ID>");
				sb.append("<position>");
				sb.append(role.getPosition());
				sb.append("</position>");
				sb.append("<headquarter>");
				sb.append(role.getHeadquarter());
				sb.append("</headquarter>");
				if (role.getProfiles() != null && role.getProfiles().size() > 0) {
					sb.append("<profiles>");
					for (int e = 0; e < role.getProfiles().size(); e++) {
						sb.append("<profile>");
						sb.append(role.getProfiles().get(e).getValue());
						sb.append("</profile>");
					}
					sb.append("</profiles>");
				}
				if (role.getDataContact() != null && role.getDataContact().size() > 0) {
					sb.append("<dataContacts>");
					for (int a = 0; a < role.getDataContact().size(); a++) {
						sb.append("<dataContact>");
						sb.append("<email>");
						sb.append(role.getDataContact().get(a).getEmail());
						sb.append("</email>");
						sb.append("<notify>");
						sb.append(role.getDataContact().get(a).isNotify());
						sb.append("</notify>");
						sb.append("</dataContact>");
					}
					sb.append("</dataContacts>");
				}
				sb.append("<attachSignature>");
				sb.append(role.isAttachSignature());
				sb.append("</attachSignature>");
				sb.append("<attachReport>");
				sb.append(role.isAttachReport());
				sb.append("</attachReport>");
				sb.append("<pageSize>");
				sb.append(role.getPageSize());
				sb.append("</pageSize>");
				sb.append("<applyAppFilter>");
				sb.append(role.isApplyAppFilter());
				sb.append("</applyAppFilter>");
				sb.append("<showPreviousSigner>");
				sb.append(role.isShowPreviousSigner());
				sb.append("</showPreviousSigner>");
				sb.append("<verifierIdentifier>");
				sb.append(role.getVerifierIdentifier());
				sb.append("</verifierIdentifier>");
				sb.append("<verifierName>");
				sb.append(role.getVerifierName());
				sb.append("</verifierName>");
				sb.append("<status>");
				sb.append(role.getStatus());
				sb.append("</status>");
				sb.append("<sentReceived>");
				sb.append(role.getSentReceived());
				sb.append("</sentReceived>");
				sb.append("<type>");
				sb.append(role.getType());
				sb.append("</type>");
				sb.append("<senderReceiver>");
				sb.append(role.getSenderReceiver());
				sb.append("</senderReceiver>");
				sb.append("<initDate>");
				sb.append(role.getInitDate());
				sb.append("</initDate>");
				sb.append("<authorization>");
				sb.append(role.getAuthorization());
				sb.append("</authorization>");
				sb.append("<endDate>");
				sb.append(role.getEndDate());
				sb.append("</endDate>");
				sb.append("</role>");
			}
		}
	}

	/**
	 * Método que genera la respuesta del servicio de recuperación de usuario.
	 * 
	 * @param result
	 *            Resultado recibida del portafirmas-web.
	 * @return resultado a enviar al portafirmas-móvil.
	 */
	public static String createGetUserResponse(GetUserResult result) {
		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER);
		sb.append("<rsgtsr");
		sb.append(" n=\"");
		sb.append(result.getUsers().size());
		sb.append("\">");
		if (result.isError()) {
			sb.append("<err>");
			String msg;
			switch (result.getErrorType()) {
			case 1:
				msg = "Error de comunicación con Portafirmas Web";
				break;
			case 2:
				msg = "Error en la petición realizada a Portafirmas Web";
				break;
			case 3:
				msg = "Error en el procesado de la petición/respuesta de Portafirmas Web";
				break;
			default:
				msg = "Error desconocido";

			}
			sb.append(msg);
			sb.append("</err>");
		} else {
			sb.append("<rsgtus>");
			addUsersToResult(sb, result.getUsers());
			sb.append("</rsgtus>");
		}
		sb.append("</rsgtsr>");
		return sb.toString();
	}

	/**
	 * Añade la lista de usuarios como XML al stringBuilder recibido como
	 * parámetro.
	 * 
	 * @param sb
	 *            Objeto donde se añadiran los usuarios.
	 * @param users
	 *            Lista de usuarios a añadir.
	 */
	private static void addUsersToResult(StringBuilder sb, List<User> users) {
		if (users != null) {
			for (int i = 0; i < users.size(); i++) {
				sb.append("<user>");
				User user = users.get(i);
				sb.append("<name>");
				sb.append(user.getName());
				sb.append("</name>");
				sb.append("<surname>");
				sb.append(user.getSurname());
				sb.append("</surname>");
				sb.append("<secondSurname>");
				sb.append(user.getSecondSurname());
				sb.append("</secondSurname>");
				sb.append("<LDAPUser>");
				sb.append(user.getLDAPUser());
				sb.append("</LDAPUser>");
				sb.append("<ID>");
				sb.append(user.getID());
				sb.append("</ID>");
				sb.append("<position>");
				sb.append(user.getPosition());
				sb.append("</position>");
				sb.append("<headquarter>");
				sb.append(user.getHeadquarter());
				sb.append("</headquarter>");
				if (user.getProfiles() != null && user.getProfiles().size() > 0) {
					sb.append("<profiles>");
					for (int e = 0; e < user.getProfiles().size(); e++) {
						sb.append("<profile>");
						sb.append(user.getProfiles().get(e).getValue());
						sb.append("</profile>");
					}
					sb.append("</profiles>");
				}
				if (user.getDataContact() != null && user.getDataContact().size() > 0) {
					sb.append("<dataContacts>");
					for (int a = 0; a < user.getDataContact().size(); a++) {
						sb.append("<dataContact>");
						sb.append("<email>");
						sb.append(user.getDataContact().get(a).getEmail());
						sb.append("</email>");
						sb.append("<notify>");
						sb.append(user.getDataContact().get(a).isNotify());
						sb.append("</notify>");
						sb.append("</dataContact>");
					}
					sb.append("</dataContacts>");
				}
				sb.append("<attachSignature>");
				sb.append(user.isAttachSignature());
				sb.append("</attachSignature>");
				sb.append("<attachReport>");
				sb.append(user.isAttachReport());
				sb.append("</attachReport>");
				sb.append("<pageSize>");
				sb.append(user.getPageSize());
				sb.append("</pageSize>");
				sb.append("<applyAppFilter>");
				sb.append(user.isApplyAppFilter());
				sb.append("</applyAppFilter>");
				sb.append("<showPreviousSigner>");
				sb.append(user.isShowPreviousSigner());
				sb.append("</showPreviousSigner>");
				sb.append("</user>");
			}
		}
	}

	/**
	 * Método que genera la respuesta para el servicio de validación de
	 * peticiones.
	 * 
	 * @param result
	 *            Resultado de la operación recibida por portafirmas-web.
	 * @return resultado a enviar al portafirmas-móvil.
	 */
	public static String createVerifyPetitionsResponse(VerifyPetitionsResult result) {
		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER);
		sb.append("<verifrp>");
		sb.append("<verify");
		// Atributo OK.
		sb.append(" ok=\"");
		sb.append(result.isResult());
		sb.append("\">");
		// Mensaje de error.
		if (result.getErrorType() == 1 || result.getErrorType() == 2 || result.getErrorType() == 3) {
			sb.append("<errorMsg>");
			switch (result.getErrorType()) {
			case 1:
				sb.append("Error de comunicación con Portafirmas Web");
				break;
			case 2:
				sb.append("Error en la petición realizada a Portafirmas Web");
				break;
			case 3:
				sb.append("Error en el procesado de la petición/respuesta de Portafirmas Web");
				break;
			}
			sb.append("</errorMsg>");
		}
		sb.append("</verify>");
		sb.append("</verifrp>");
		return sb.toString();
	}

	/**
	 * Método que genera la respuesta para el servicio de creación de roles.
	 * 
	 * @param result
	 *            Resultado de la operación recibida por portafirmas-web.
	 * @return resultado a enviar al portafirmas-móvil.
	 */
	public static String createCreationRoleResponse(CreateRoleResult result) {
		final StringBuilder sb = new StringBuilder();
		sb.append(XML_HEADER);
		sb.append("<crtnwrl>");
		sb.append("<resp");
		// Atributo success.
		sb.append(" success=\"");
		sb.append(result.isSuccess());
		sb.append("\">");
		// Mensaje de error.
		if (result.getErrorType() == 1 || result.getErrorType() == 2 || result.getErrorType() == 3) {
			sb.append("<errorMsg>");
			switch (result.getErrorType()) {
			case 1:
				sb.append("Error de comunicación con Portafirmas Web");
				break;
			case 2:
				sb.append("Error en la petición realizada a Portafirmas Web");
				break;
			case 3:
				sb.append("Error en el procesado de la petición/respuesta de Portafirmas Web");
				break;
			}
			sb.append("</errorMsg>");
		}
		sb.append("</resp>");
		sb.append("</crtnwrl>");
		return sb.toString();
	}
}
