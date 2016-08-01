package es.dipucr.sigem.DetallesCircuitoFirma.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.common.constants.SignCircuitStates;
import ieci.tdw.ispac.ispaclib.sign.ISignConnector;
import ieci.tdw.ispac.ispaclib.sign.SignConnectorFactory;
import ieci.tdw.ispac.ispaclib.sign.SignDetailEntry;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.sign.exception.InvalidSignatureValidationException;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.XmlFacade;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispacmgr.action.ShowSignDetailAction;
import ieci.tdw.ispac.ispacmgr.action.form.EntityForm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bouncycastle.util.encoders.Base64;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

public class ShowSignDetailCustomAction extends BaseAction {

	private static final String SIGNER_DATE_SEPARATOR = ";;";

	public static final Logger logger = Logger
			.getLogger(ShowSignDetailAction.class);

	/**
	 * Método execute
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		//INICIO [dipucr-Felipe #208] Pasamos directamente el id de document
		String documentId = request.getParameter("document");
        if (null == documentId) {
        	//Si document viene vacío, buscamos por field para los linkFrame (como antiguamente)
        	//Esto se sigue usando dentro del detalle del documento
        	EntityForm defaultForm = (EntityForm) form;
        	String field = request.getParameter("field");
        	if (null == field){
        		return null;
        	}
        	else{
        		documentId = defaultForm.getProperty(field);
        		if (null == documentId){
        			return null;
        		}
        	}
        }
        //FIN [dipucr-Felipe #208]

		if (logger.isDebugEnabled()) {
			logger.debug("Solicitando detalle de firma de un documento: "
					+ documentId);
		}

		@SuppressWarnings("rawtypes")
		List details = showSignInfo(session, Integer.parseInt(documentId));
		request.setAttribute("details", details);

		return mapping.findForward("success");
	}

	/**
	 * Método con el código principal de muestreo de información de firma
	 * 
	 * @param session
	 * @param documentId
	 * @return
	 * @throws ISPACException
	 * @throws InvalidSignatureValidationException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List showSignInfo(SessionAPI session, int documentId)
			throws ISPACException, InvalidSignatureValidationException {

		IGenDocAPI genDocAPI = session.getClientContext().getAPI().getGenDocAPI();
		IEntitiesAPI entitiesAPI = session.getClientContext().getAPI().getEntitiesAPI();
		ISignAPI signAPI = session.getClientContext().getAPI().getSignAPI();
		Object connectorSession = null;
		String signProperty = null;

		//[dipucr-Felipe #147]
//		IItem iitem = entitiesAPI.getDocument(documentId);
		IItem iitem = DocumentosUtil.getDocumento(entitiesAPI, documentId);

		List details = new ArrayList();

		String state = iitem.getString("ESTADOFIRMA");
		if (state.equals(ISignAPI.ESTADO_SIN_FIRMA)) {
			return details;
		}

		// Referencia al documento firmado almacenado en el Repositorio de
		// Documentos Electronicos
		String infoPageRDE = iitem.getString("INFOPAG_RDE");

		List list = new ArrayList();
		List certificates = new ArrayList();
		SignDetailEntry entry = null;

		try {
			//[dipucr-Felipe 3#230]
			IItemCollection collectionPasosFirma = signAPI.getStepsByDocument(documentId);
			
			if (StringUtils.isNotBlank(infoPageRDE)) { // Existe al menos un firmante.
				
				connectorSession = genDocAPI.createConnectorSession();
				if (!genDocAPI.existsDocument(connectorSession, infoPageRDE)) {
					logger.error("No se ha encontrado el documento fisico con identificador: '"
							+ infoPageRDE + "' en el repositorio de documentos");
					throw new ISPACInfo("exception.documents.notExists", false);
				}

				// Obtenemos el xml con las firmas adjuntadas antes de añadir la nueva
				signProperty = genDocAPI.getDocumentProperty(connectorSession,
						infoPageRDE, "Firma");

				XmlFacade xmlFacade = new XmlFacade(signProperty);

				//[dipucr-Felipe #208]
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				genDocAPI.getDocument(connectorSession, infoPageRDE, baos);

				list = xmlFacade.getList("/" + SignDocument.TAG_FIRMAS + "/"
						+ SignDocument.TAG_FIRMA);
				String tipo = xmlFacade.get("/" + SignDocument.TAG_FIRMAS
						+ "/@" + SignDocument.ATTR_TYPE);

				if (SignDocument.TYPE_PORTAFIRMAS.equals(tipo)) {
					String signerInfo = null;
					String[] signerDateInfo = null;
					for (int i = 0; i < list.size(); i++) {
						signerInfo = new String(Base64.decode((String) list
								.get(i)));
						signerDateInfo = signerInfo
								.split(SIGNER_DATE_SEPARATOR);
						entry = new SignDetailEntry();
						entry.setIntegrity(ISignAPI.INTEGRIDAD_PORTAFIRMAS);
						entry.setSignDate(signerDateInfo[1]);
						entry.setFirmado(true);
						entry.setAuthor(signerDateInfo[0]);
						details.add(entry);
					}
				} else {

					certificates = xmlFacade.getList("/"
							+ SignDocument.TAG_CERTIFICADOS + "/"
							+ SignDocument.TAG_CERTIFICADO);

					ISignConnector signConnector = SignConnectorFactory.getSignConnector();

					logger.debug("Firmas del documento: \n " + list.toString());

					//[dipucr-Felipe 3#230]
					List<IItem> listPasosFirma= collectionPasosFirma.toList();
					
					for (int i = 0; i < list.size(); i++) {
						Map results = null;
						entry = new SignDetailEntry();
						// Verificamos la integridad de la firma

						//byte signedContent[] = Base64
						//		.encode(baos.toByteArray());
						//results = signConnector.verify((String) list.get(i),new String(Base64.encode(signedContent)));
						// Obtenemos la informacion del firmante
//						if (i < certificates.size()) {
//
//							entry.setAuthor(signConnector
//									.getInfoCert((String) certificates.get(i)));
//						} else {
//
//							// Firmado por el servidor de @ firma
//						}

						//entry.setIntegrity((String) results.get(ISignAPI.INTEGRIDAD_VALIDE));
//						if (StringUtils.isNotEmpty((String) results.get(ISignAPI.DN))) {
//							entry.setAuthor((String) results.get(ISignAPI.DN));
//
//						} else {
//							if (results.get(ISignAPI.NOMBRE) != null) {
//								entry.setAuthor(results.get(ISignAPI.NOMBRE)
//										+ " " + results.get(ISignAPI.APELLIDOS));
//							} else {
						entry.setIntegrity(ISignAPI.INTEGRIDAD_VALIDE);
						entry.setAuthor((String) list.get(i));
//							}
//						}

						//INICIO [dipucr-Felipe 3#230]
						String sFechaFirma = null;
						if (listPasosFirma.size() > 0){
							Date dFechaFirma = listPasosFirma.get(i).getDate("FECHA");
							sFechaFirma = FechasUtil.getFormattedDate(dFechaFirma);
						}
						else{
							sFechaFirma = iitem.getString("FFIRMA");
						}
						entry.setSignDate(sFechaFirma);
						//FIN [dipucr-Felipe 3#230]
						entry.setFirmado(true);
						details.add(entry);
					}
				}
			}
			
			//INICIO [dipucr-Felipe #208] Antiguas firmas de circuito sin metadatos
			int numFirmante = 0;
			while (collectionPasosFirma.next()) {
				numFirmante++;
				IItem itemPasoFirma = collectionPasosFirma.value();
				if (numFirmante > list.size()) {
					entry = new SignDetailEntry();
					entry.setAuthor((String) itemPasoFirma.get("NOMBRE_FIRMANTE"));
					int estado = itemPasoFirma.getInt("ESTADO");
					if (estado == SignCircuitStates.FINALIZADO
							|| estado == SignCircuitStates.FIRMADO_REPARO
							|| estado == SignCircuitStates.RECHAZADO)
					{
						String sFechaFirma = FechasUtil.getFormattedDate(itemPasoFirma.getDate("FECHA"));
						entry.setSignDate(sFechaFirma);
						entry.setFirmado(true);
					} else {
						entry.setSignDate("");
						entry.setFirmado(false);
					}
					logger.debug(entry);
					details.add(entry);
				}
			}
			//FIN [dipucr-Felipe #208]

		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
		}
		return details;
	}
}
