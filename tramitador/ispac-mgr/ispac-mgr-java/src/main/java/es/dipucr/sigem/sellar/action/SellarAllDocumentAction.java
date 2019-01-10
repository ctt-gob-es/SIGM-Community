package es.dipucr.sigem.sellar.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationHelper;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.BaseDispatchAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;

import es.dipucr.notificador.model.EnvioWS;
import es.dipucr.notificador.model.TerceroWS;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.sgm.tram.sign.DipucrSignConnector;
import es.jccm.notificador.ws.AplicacionesServiceProxy;

public class SellarAllDocumentAction extends BaseDispatchAction {

	// Id de los documentos seleccionados
	private String idDocSeleccionado = "";

	/**
	 * Ruta por defecto de la imagen del logo dipu
	 * /home/sigem/SIGEM/conf/SIGEM_Tramitacion
	 */
	private static final String IMAGE_PATH_DIPUCR = "firma/logoCabecera.png";

	/**
	 * Ruta por defecto de la imagen de fondo del PDF.
	 */
	private static final String DEFAULT_PDF_BG_IMAGE_PATH = "firma/fondo.gif";
	public final static String PATH_IMAGEN_BAND = "PATH_IMAGE_BAND";//[dipucr-Felipe #507]

	/**
	 * Ruta por defecto del fichero con el texto de la banda lateral del PDF.
	 * /home/sigem/SIGEM/conf/SIGEM_Tramitacion
	 */
	private static final String DEFAULT_PDF_BG_DATA_PATH = "firma/datosFirmaRegistro.txt";

//	public static final Logger logger = Logger.getLogger(SellarAllDocumentAction.class);
	public static final Logger logger = Logger.getLogger("loggerRegistro");

	File fileConcatenado;
	String pathFileTempConcatenada;

	public SellarAllDocumentAction() {
	}

	public SellarAllDocumentAction(String idDocSeleccionado) {
		this.idDocSeleccionado = idDocSeleccionado;
	}

	@SuppressWarnings({ "unchecked", "resource" })
	public ActionForward selectOption(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		ClientContext cct = session.getClientContext();

		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(
				cct);
		IState currentState = managerAPI.currentState(getStateticket(request));

		IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();

		// Variables
		IItem entityDocument = null;
		int documentId = 0;

		int idTipDoc = 0;
		int idPlantilla = 0;
		ArrayList<FileInputStream> inputStreamNotificaciones = new ArrayList<FileInputStream>();
		ArrayList<String> filePathNotificaciones = new ArrayList<String>();
		// MQE ticket #325
		try {
			idTipDoc = DocumentosUtil.getTipoDoc(cct, "Documentos Sellados", DocumentosUtil.BUSQUEDA_EXACTA, false);

			String consulta = "SELECT ID FROM SPAC_P_PLANTDOC WHERE NOMBRE='Documentos Sellados' AND ID_TPDOC="
					+ idTipDoc + ";";
			ResultSet planDocIterator = cct.getConnection()
					.executeQuery(consulta).getResultSet();
			if (planDocIterator.next())
				idPlantilla = planDocIterator.getInt("ID");

			// MQE Saco todos los documentos que hay que hay que sellar.
			// Sólo tenemos en cuenta aquellos que han sido firmados y
			// registrados, a los que han sido registrados
			// no les hacemos caso
			String stateTicket = getStateticket(request);
			// logger.warn("stateTicket "+stateTicket);
			// El string que devuelve este metodo tiene esta forma
			// 113|226|9499|230|921|15611|6393|DPCR2010%2F6309|6|7|15617|true|false|0|0|0|0|0|0
			// en la posicion 11 es donde esta el key ahí íria el id del
			// documento asi que lo
			// voy a hacer es sacar esta posicion y meter a mano el id del
			// documento
			String[] aStateTicket = stateTicket.split("\\|");
			String tramite = "";
			if (aStateTicket != null && aStateTicket.length > 11) {
				tramite = aStateTicket[5];
			}

			// MQE Saco todos los documentos que hay que hay que sellar.
			// Sólo tenemos en cuenta aquellos que han sido firmados y
			// registrados, a los que han sido registrados
			// no les hacemos caso

			String strQuery = "WHERE ID_TRAMITE=" + tramite
					+ " AND (NREG IS NOT NULL AND NREG != '')";
			logger.warn("-----------------" + this.idDocSeleccionado);
			// [Teresa Ticket 631]
			if (!this.idDocSeleccionado.equals("null")
					&& !this.idDocSeleccionado.equals("")) {
				logger.warn("-----------------DENTRO");
				strQuery += this.idDocSeleccionado;
			}
			strQuery += " AND UPPER(EXTENSION_RDE) = 'PDF' ORDER BY DESCRIPCION;";
			logger.warn("strQuery " + strQuery);

			IItemCollection collAllDoc = DocumentosUtil.queryDocumentos(cct, strQuery);
			Iterator<IItem> itcollAllDoc = collAllDoc.iterator();

			// Creamos el nuevo documento.
			int taskId = currentState.getTaskId();
			logger.warn("taskId " + taskId);
			cct.beginTX();

			entityDocument = genDocAPI.createTaskDocument(
					Integer.parseInt(tramite), idTipDoc);
			entityDocument.set("EXTENSION", "pdf");
			entityDocument.set("ID_PLANTILLA", idPlantilla);
			entityDocument.set("DESCRIPCION", "Documentos Sellados");
			entityDocument.store(cct);
			cct.endTX(true);
			documentId = entityDocument.getKeyInt();

			String tipoRegistro = "";
			String numRegistro = "";
			String fechaRegistro = "";
			String departamento = "";
			String pathFileTemp = "";

			pathFileTempConcatenada = FileTemporaryManager.getInstance()
					.newFileName(".pdf");

			File fileSello, file;
			FileInputStream fis;

			// tabla SPAC_DT_TRAMITES
			IItem iTask = entitiesAPI.getTask(Integer.parseInt(tramite));
			int idTramite = iTask.getInt("ID_TRAM_PCD");
			// tabla SPAC_P_TRAMITES
			strQuery = "WHERE ID_TRAMITE_BPM='" + idTramite + "'";
			// logger.debug("strQuery: "+strQuery);
			IItemCollection collection = entitiesAPI.queryEntities(
					SpacEntities.SPAC_P_TRAMITES, strQuery);
			Iterator<IItem> it = collection.iterator();
			int id_pcd = 0;
			if (it.hasNext()) {
				id_pcd = ((IItem) it.next()).getInt("ID_PCD");

			} else {
				throw new ISPACInfo(
						"No se ha encontrado la fase actual del expediente.");
			}
			// tabla SPAC_PROCEDIMIENTO

			strQuery = "SELECT NOMBRE FROM "
					+ Constants.TABLASBBDD.SPAC_P_PROCEDIMIENTOS + " WHERE ID="
					+ id_pcd + "";
			planDocIterator = cct.getConnection().executeQuery(strQuery)
					.getResultSet();

			if (planDocIterator == null) {
				throw new ISPACInfo("No hay ningun procediento asociado");
			}
			String nombreProc = "";
			if (planDocIterator.next())
				if (planDocIterator.getString("NOMBRE") != null)
					nombreProc = planDocIterator.getString("NOMBRE");
				else
					nombreProc = "";

			// Se va a insertar en un pdf los datos de a los usuarios que se le
			// ha mandado las notificaciones.

			
			File fileCompareceNombre = FileTemporaryManager.getInstance().newFile("." + "pdf");
			Document documentComparece = new Document();

			PdfCopy.getInstance(documentComparece, new FileOutputStream(fileCompareceNombre));
			documentComparece.open();

			String imagePath = ConfigurationHelper
					.getConfigFilePath(IMAGE_PATH_DIPUCR);

			File logoURL = new File(imagePath);
			if (logoURL != null) {
				Image logo = Image.getInstance(imagePath);
				documentComparece.add(logo);
			}

			documentComparece.add(new Phrase("\n\n"));

			documentComparece.add(new Phrase(
					"PARTICIPANTES ENVIADOS A COMPARECE"));
			documentComparece.add(new Phrase("\n\n"));
			while (itcollAllDoc.hasNext()) {
				tipoRegistro = "";
				numRegistro = "";
				fechaRegistro = "";
				departamento = "";
				String usuario = "";
				String usuarioDestinoId = "";
				String descripcion = "";
				String numexp = "";
				String nombreDOC = "";
				String codigoCotejo = "";
				IItem document = (IItem) itcollAllDoc.next();

				String infoPag = document.getString("INFOPAG");
				String infoPagRDE = document.getString("INFOPAG_RDE");

				file = null;
				if (StringUtils.isNotBlank(infoPagRDE)) {
					file = DocumentosUtil.getFile(cct, infoPagRDE, null, null);
				} else {
					file = DocumentosUtil.getFile(cct, infoPag, null, null);
				}

				int idDoc = 0;
				pathFileTemp = FileTemporaryManager.getInstance().put(
						file.getAbsolutePath(), ".pdf");
				if (document.getString("TP_REG") != null)
					tipoRegistro = document.getString("TP_REG");
				else
					tipoRegistro = "";
				if (document.getString("NREG") != null)
					numRegistro = document.getString("NREG");
				else
					numRegistro = "";
				if (document.getString("FREG") != null)
					fechaRegistro = document.getString("FREG");
				else
					fechaRegistro = "";
				if (document.getString("ORIGEN") != null)
					departamento = document.getString("ORIGEN");
				else
					departamento = "";
				if (document.getString("DESCRIPCION") != null)
					descripcion = document.getString("DESCRIPCION");
				else
					descripcion = "";
				if (document.getString("NUMEXP") != null)
					numexp = document.getString("NUMEXP");
				else
					numexp = "";
				if (document.getString("NOMBRE") != null)
					nombreDOC = document.getString("NOMBRE");
				else
					nombreDOC = "";
				if (document.getString("COD_COTEJO") != null)
					codigoCotejo = document.getString("COD_COTEJO");
				else
					codigoCotejo = "";
				idDoc = document.getInt("ID");
				// logger.warn("file "+file+" pathFileTemp "+pathFileTemp+" infoPagRDE "+infoPagRDE+" tipoRegistro "+tipoRegistro+" numRegistro "+numRegistro+" fechaRegistro "+fechaRegistro+" departamento "+departamento);
				// addGrayBandNuevo(file,pathFileTemp, tipoRegistro,
				// numRegistro, fechaRegistro,departamento);
				addGrayBand(file, pathFileTemp, infoPagRDE, tipoRegistro,numRegistro, fechaRegistro, departamento);
				
				//[Ticket#153#Teresa] ALSIGM3 Quitar el fichero blanco.pdf del código para que no aparezca en '/tmpcenpri/SIGEM/temp/temporary'
				//String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/blanco.pdf";
				//rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
				//logger.warn("rutaFileName " + rutaFileName);

				fileSello = FileTemporaryManager.getInstance().newFile("." + "pdf");
				// logger.warn("fileSello "+fileSello);
				fis = new FileInputStream(fileSello);

				logger.warn("descriopcion. " + descripcion);

				// String[] vDescrip = descripcion.split(" - ");
				// logger.warn("vDescrip.length "+vDescrip.length);
				/**
				 * Inicio [Teresa Ticket #339#] SIGEM secretaria problemas a la
				 * hora de registrar
				 * **/
				// if (!(vDescrip.length <= 1)) {
				// usuario = vDescrip[1];
				// // logger.warn("usuario "+usuario);
				// }
				if (document.getString("DESTINO") != null)
					usuario = document.getString("DESTINO");
				else
					usuario = "";
				if (document.getString("DESTINO_ID") != null)
					usuarioDestinoId = document.getString("DESTINO_ID");
				else
					usuarioDestinoId = "";
				logger.warn("usuario " + usuario + " usuarioDestinoId "
						+ usuarioDestinoId);
				Vector<String> dni = new Vector<String>();

				boolean usuComparece = false;
				// logger.warn("usuario.equals "+usuario.equals(""));
				if (!usuario.equals("")) {
					//String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
					// logger.warn("entidad "+entidad);
					//if (entidad.equals("005") || entidad.equals("001")) {
						// logger.warn("dentro ");
						try {
							usuComparece = esUsuarioComparece(cct, entitiesAPI,
									numexp, usuario, usuarioDestinoId, dni,
									descripcion);
						} catch (RemoteException e) {
							logger.error(e.getMessage(), e);
							throw new ISPACRuleException("Error. ", e);
						} catch (ISPACRuleException e) {
							logger.error(e.getMessage(), e);
							throw new ISPACRuleException("Error. ", e);
						} 
					//}
				}
				/**
				 * Fin [Teresa Ticket #339#] SIGEM secretaria problemas a la
				 * hora de registrar
				 * **/
				logger.warn("usuComparece " + usuComparece);
				String[] aDni = new String[dni.size()];
				for (int i = 0; i < dni.size(); i++) {
					aDni[i] = (String) dni.get(i);
					logger.warn(aDni[i]);
				}
				logger.warn(aDni);
				if (usuComparece) {
					logger.warn("Insertar Ayuntamiento en documento comparece. "
							+ usuario);
					String nombreDOCSinAcento = DipucrCommonFunctions
							.limpiarCaracteresEspeciales(nombreDOC);
					documentComparece.add(new Phrase(usuario + " : "));

					documentComparece.add(new Phrase("\n"));
					// Le mandamos el documento original
					// No va a tener la banda gris del registro de salida
					// xq al añadirla se perdian las propiedades de los
					// firmantes.
					// Solución. mandar por el comparece el original
					FileInputStream fisOriginal = new FileInputStream(file);
					envioDocComparece(entitiesAPI, invesFlowAPI, document, cct,
							fileSello.getPath(), aDni, numexp, nombreProc,
							descripcion, nombreDOCSinAcento, idDoc,
							codigoCotejo, documentComparece, usuario,
							inputStreamNotificaciones, filePathNotificaciones,
							fis, fileSello.getAbsolutePath(), departamento, file, fisOriginal,
							file.getPath(), taskId);

				} else {
					// MQE ticket #325
					inputStreamNotificaciones.add(fis);
					filePathNotificaciones.add(fileSello.getAbsolutePath());
				}

			}
			// if(errorComparece){
			// documentComparece.add(new
			// Phrase(Constants.MENSAJE.MENSAJECOMPARECE));
			// ISPACException info = new
			// ISPACException(Constants.MENSAJE.MENSAJECOMPARECE, true);
			// request.getSession().setAttribute("infoAlert", info);
			// }
			documentComparece.close();

			// MQE ticket #325
			fileConcatenado = concatenaPdf(inputStreamNotificaciones,
					filePathNotificaciones, genDocAPI);

			if (fileConcatenado != null) {
				DocumentosUtil.anexaDocumento(cct, genDocAPI, taskId, documentId, fileConcatenado, Constants._EXTENSION_PDF, "Documentos Sellados");
			}

			int idTipDocAux = DocumentosUtil.getTipoDoc(cct, Constants.COMPARECE.PARTICIPANTES_COMPARECE, DocumentosUtil.BUSQUEDA_EXACTA, false);
			if(idTipDocAux > Integer.MIN_VALUE){
				idTipDoc = idTipDocAux;
			}

			consulta = "SELECT ID FROM SPAC_P_PLANTDOC WHERE NOMBRE='"+ Constants.COMPARECE.PARTICIPANTES_COMPARECE	+ "' AND ID_TPDOC=" + idTipDoc + ";";
			planDocIterator = cct.getConnection().executeQuery(consulta)
					.getResultSet();
			int idPlantillaCompar = 0;
			if (planDocIterator.next())
				idPlantillaCompar = planDocIterator.getInt("ID");

			if (fileCompareceNombre != null) {

				entityDocument = DocumentosUtil.generaYAnexaDocumento(cct, Integer.parseInt(tramite), idTipDoc, Constants.COMPARECE.PARTICIPANTES_COMPARECE, fileCompareceNombre, Constants._EXTENSION_PDF);
				entityDocument.set("ID_PLANTILLA", idPlantillaCompar);
				
				entityDocument.store(cct);
				cct.endTX(true);
			}

		} catch (ISPACInfo e) {
			logger.error("Error al sellar el documento", e);
			e.setRefresh(false);
			return mapping.findForward("failure");
			// throw e;
		} catch (Exception e) {
			logger.error("Error al sellar el documento", e);
			// throw new ISPACInfo(e,false);
			return mapping.findForward("failure");
		} finally {

			// MQE ticket #325
			for (int i = 0; i < inputStreamNotificaciones.size(); i++)
				((FileInputStream) inputStreamNotificaciones.get(i)).close();
			System.gc();
		}
		return mapping.findForward("success");
	}

	private void insertarAcuseCompare(IEntitiesAPI entitiesAPI,
			IInvesflowAPI invesFlowAPI, ClientContext cct, String numexp,
			int idDocumenNot, String idResponsable, Date fechaFin, int idDoc,
			String dniNotificado, int taskId) throws ISPACRuleException {

		IItem notice;
		try {
			notice = entitiesAPI.createEntity(
					Constants.TABLASBBDD.DPCR_ACUSES_COMPARECE, "");
			notice.set("NUMEXP", numexp);
			notice.set("ID_PROC", invesFlowAPI.getProcess(numexp).getInt("ID"));
			notice.set("ID_NOTIFICACION", idDocumenNot);
			notice.set("ID_RESPONSABLE", idResponsable);
			notice.set("IDENT_DOC", idDoc);
			notice.set("ESTADO", 0);
			notice.set("DNI_NOTIFICADO", dniNotificado);
			notice.set("TRAMITE", taskId);

			notice.store(cct);

		} catch (ISPACException e) {
			throw new ISPACRuleException("Error. ", e);
		}

	}

	// Se encarga de enviar el documento al comparece y ademas de insertar en la
	// tabla dpcr_acuse_comparece
	// los datos del notificado
	@SuppressWarnings("deprecation")
	private boolean envioDocComparece(IEntitiesAPI entitiesAPI,
			IInvesflowAPI invesFlowAPI, IItem document, ClientContext cct,
			String path, String[] dni, String numexp, String nombreProc,
			String descripcion, String nombre, int idDoc, String codigoCotejo,
			Document documentComparece, String usuario,
			ArrayList<FileInputStream> inputStreamNotificaciones,
			ArrayList<String> filePathNotificaciones, FileInputStream fis,
			String rutaFileName, String departamento, File fileOriginal,
			FileInputStream fisOriginal, String filePathOriginal, int taskId)
			throws ISPACRuleException {
		// Acceso al servicio web de comparece

		boolean tieneParticipantesComparece = false;

		AplicacionesServiceProxy asp = new AplicacionesServiceProxy();

		// String [] dnis = new String[dni.size()];
		// for(int i = 0; i< dni.size();i++){
		// dnis[i]=(String) dni.get(i);
		// }
		// dnis[0]=new String("47062508T");
		// dnis[1]=new String("05695305E");

		// String procedimiento="Prueba de conexion con comparece";
		// String expediente="DPCR2010/666666666";
		// incrementar 10 dias a la fecha que se tiene

		// [Ticket #319 teresa] INICIO SIGEM Comparece añadir la fecha de
		// caducidad en la notificaciones

		Calendar fecha = Calendar.getInstance(); // obtiene la fecha actual
		fecha.add(Calendar.DATE, 10); // Incrementando 10 dias a la fecha

		int diaCaducidad = fecha.getTime().getDate();
		int mesCaducidad = fecha.getTime().getMonth() + 1;
		int anyoCaducidad = fecha.getTime().getYear() + 1900;

		// [Ticket #319 teresa] FIN SIGEM Comparece añadir la fecha de caducidad
		// en la notificaciones

		// String
		// descripcion="Esta es una notificacion para Antonio de parte de Agustin";
		// String nombre="prueba";
		String extension = Constants._EXTENSION_PDF;
		// Se va a mandar el documento original
		// DataHandler notificacion = new DataHandler(new FileDataSource(path));
		DataHandler notificacion = new DataHandler(new FileDataSource(
				filePathOriginal));

		try {
			// Sacar el hash del documento
			// Documento a firmar
			SignDocument signDocument = new SignDocument(document);

			// String hashDocument = generateHashCode(signDocument, cct);
			String hashDocument = generateHashCode(signDocument, cct);
			GregorianCalendar calendario = new GregorianCalendar();
			logger.warn("HASHDOCUMENTO " + hashDocument);
			for (int i = 0; i < dni.length; i++) {
				SimpleDateFormat dateformat = new SimpleDateFormat(
						"dd-MMMM-yyyy HH:mm:ss");

				if (!comprobarEnvioComparece(entitiesAPI, numexp, idDoc,
						dni[i], taskId)) {
					String[] dniEnvio = new String[1];
					dniEnvio[0] = dni[i];
					// logger.warn("dniEnvio[0] '"+dniEnvio[0]+"'");
					String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
					// logger.warn(entidad);
					// logger.warn("longitud."+dniEnvio.length);
					// logger.warn(dniEnvio);
					// logger.warn(nombreProc);
					// logger.warn(numexp);
					// logger.warn(descripcion);
					// logger.warn(diaCaducidad);
					// logger.warn(mesCaducidad);
					// logger.warn(anyoCaducidad);
					// logger.warn(notificacion);
					// logger.warn(nombre);
					// logger.warn(extension);
					// logger.warn(hashDocument);
					// logger.warn(codigoCotejo);
					// EnvioWS [] envio = asp.notificar("1", dniEnvio,
					// nombreProc, numexp, descripcion, diaCaducidad,
					// mesCaducidad, anyoCaducidad, notificacion, nombre,
					// extension, hashDocument, codigoCotejo);
					EnvioWS[] envio = asp.notificar(entidad, dniEnvio,
							nombreProc, numexp, descripcion, diaCaducidad,
							mesCaducidad, anyoCaducidad, notificacion, nombre,
							extension, hashDocument, codigoCotejo);
					logger.warn("envio " + envio);

					for (int j = 0; j < envio.length; j++) {
						logger.warn("envio.getIdNotificacion() "
								+ envio[j].getIdNotificacion());
						logger.warn("dni notificado " + envio[j].getDni());
						if (envio[j].getIdNotificacion() > 0) {
							tieneParticipantesComparece = true;

							dateformat = new SimpleDateFormat(
									"dd-MMMM-yyyy HH:mm:ss");

							if (dni.length > 1) {
								documentComparece.add(new Phrase("\t - "
										+ dni[i]
										+ " - "
										+ dateformat.format(calendario
												.getTime()) + "."));
								documentComparece.add(new Phrase("\n"));
							} else {
								documentComparece.add(new Phrase("\t - "
										+ dni[0]
										+ " - "
										+ dateformat.format(calendario
												.getTime()) + "."));
								documentComparece.add(new Phrase("\n"));
							}

							// Enviar a dipcr_aviso_comparece
							String destino = DocumentosUtil.getAutorUID(
									entitiesAPI, idDoc);
							insertarAcuseCompare(entitiesAPI, invesFlowAPI,
									cct, numexp, envio[j].getIdNotificacion(),
									destino, fecha.getTime(), idDoc, dni[i],
									taskId);
							// AvisosUtil.generarAviso(entitiesAPI,
							// invesFlowAPI.getProcess(numexp).getInt("ID"),
							// numexp, descripcion, destino, ctx);
						} else {
							if (!tieneParticipantesComparece) {
								logger.error("envio[i].isEmail() "
										+ envio[j].isEmail());
								logger.error("envio[i].isNotificacion() "
										+ envio[j].isNotificacion());
								logger.error("envio[i].isSms() "
										+ envio[j].isSms());

								logger.warn("Error a la hora de mandar la notificacion por comparece. Se ha generado la notificación de documentos sellados ");
								logger.warn("numexp " + numexp + " usuario "
										+ descripcion + " departamento "
										+ departamento);
								inputStreamNotificaciones.add(fisOriginal);
								filePathNotificaciones.add(filePathOriginal);
								// errorComparece = true;

								String destino = DocumentosUtil.getAutorUID(
										entitiesAPI, idDoc);
								insertarAcuseCompare(entitiesAPI, invesFlowAPI,
										cct, numexp, 0, destino,
										fecha.getTime(), idDoc, dni[i], taskId);
							}
						}
					}
				} else {
					logger.warn("Ya ha sido mandado la notificacion a este usuario");
					logger.warn("numexp " + numexp + " dni[i] " + dni[i]);
					logger.warn("dni.length " + dni.length);
					if (dni.length > 1) {
						documentComparece
								.add(new Phrase("\t - "
										+ dni[i]
										+ " - "
										+ dateformat.format(calendario
												.getTime()) + "."));
						documentComparece.add(new Phrase("\n"));
					} else {
						documentComparece
								.add(new Phrase("\t - "
										+ dni[0]
										+ " - "
										+ dateformat.format(calendario
												.getTime()) + "."));
						documentComparece.add(new Phrase("\n"));
					}
				}
			}
		} catch (RemoteException e) {
			throw new ISPACRuleException("Error. ", e);
		} catch (ISPACException e) {
			throw new ISPACRuleException("Error. ", e);
		} catch (DocumentException e) {
			throw new ISPACRuleException("Error. ", e);
		}

		return tieneParticipantesComparece;

	}

	/**
	 * [ecenpri-Teresa Ticket #405] SIGEM Comparece-Sigem no permitir mandar la
	 * misma notificación a la misma persona
	 * 
	 * @since 06.07.2011
	 * @author Teresa
	 * @throws ISPACRuleException
	 */
	@SuppressWarnings("unchecked")
	private boolean comprobarEnvioComparece(IEntitiesAPI entitiesAPI,
			String numexp, int idDoc, String dni, int taskId)
			throws ISPACRuleException {

		boolean mandado = false;

		try {
			String strQuery = "WHERE NUMEXP = '" + numexp
					+ "' AND IDENT_DOC = '" + idDoc + "' AND DNI_NOTIFICADO='"
					+ dni + "' AND TRAMITE=" + taskId + "";
			logger.warn("strQuery. " + strQuery);
			IItemCollection collection = entitiesAPI.queryEntities(
					"DPCR_ACUSES_COMPARECE", strQuery);
			Iterator<IItem> it = collection.iterator();
			if (it.hasNext()) {
				mandado = true;
			}
		} catch (ISPACException e) {
			throw new ISPACRuleException("Error. ", e);
		}

		logger.warn("mandado " + mandado);

		return mandado;
	}

	public String generateHashCode(SignDocument signDocument, ClientContext cct)
			throws ISPACException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String infoPag = signDocument.getItemDoc().getString("INFOPAG");
		if (StringUtils.isEmpty(infoPag)) {
			infoPag = signDocument.getItemDoc().getString("INFOPAG_RDE");
			throw new ISPACInfo(
					"No existe un documento físico para poder firmar");
		}

		IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
		Object connectorSession = null;
		try {
			connectorSession = genDocAPI.createConnectorSession();
			genDocAPI.getDocument(connectorSession, infoPag, out);
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
		}

		String contentBase64 = new String(Base64.encodeBase64(out.toByteArray()));

		// Crea un digest con el algoritmo SHA-1
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new ISPACRuleException("Error. ", e);
		}

		// Genera el código hash de la cadena
		byte[] hash = md.digest(contentBase64.getBytes());
		logger.warn("hexHash byrte " + hash.toString());

		String hexHash = toHexadecimal(hash);
		logger.warn("hexHash " + hexHash);

		// SignDocumentMgr signDocumentMgr = new SignDocumentMgr(signDocument,
		// cct);
		// String hashCode = signDocumentMgr.generateHashCode();
		return hexHash;
	}

	public String generateIdHash(String cadena) throws ISPACRuleException {

		// Convertir el array de bytes a un String

		String result = "";
		result = toHexadecimal(Base64.decodeBase64(cadena));

		return result;
	}

	public static String toHexadecimal(byte[] datos) {
		String resultado = "";
		ByteArrayInputStream input = new ByteArrayInputStream(datos);
		String cadAux;
		int leido = input.read();
		while (leido != -1) {
			cadAux = Integer.toHexString(leido);
			if (cadAux.length() < 2) // Hay que añadir un 0
				resultado += "0";
			resultado += cadAux;
			leido = input.read();
		}
		return resultado;
	}

	@SuppressWarnings("unchecked")
	private boolean esUsuarioComparece(ClientContext cct,
			IEntitiesAPI entitiesAPI, String numexp, String usuario,
			String usuarioDestinoId, Vector<String> dni, String descripcion)
			throws RemoteException, ISPACRuleException {
		boolean esUsuarioComp = false;

		try {
			String sqlQueryPart = "";
			if (usuarioDestinoId.equals("") || usuarioDestinoId.equals("0")) {
				sqlQueryPart = "WHERE NOMBRE='" + usuario + "' AND NUMEXP='" + numexp + "'";
			} else {
				sqlQueryPart = "WHERE ID_EXT='" + usuarioDestinoId + "' AND NUMEXP='" + numexp + "'";
			}

			logger.warn("sqlQueryPart " + sqlQueryPart);
			IItemCollection participantes = ParticipantesUtil.queryParticipantes(cct, sqlQueryPart);
			// Si es = a 0 quiere decir que esta en sus expedientes relacionados
			// entonces miro en cualquier otro expediente para sacar si dni
			if (participantes.toList().size() == 0) {

				if (usuarioDestinoId.equals("") || usuarioDestinoId.equals("0")) {
					sqlQueryPart = "WHERE NOMBRE='" + usuario + "' ORDER BY ID DESC";
				} else {
					sqlQueryPart = "WHERE ID_EXT='" + usuarioDestinoId + "' ORDER BY ID DESC";
				}

				// logger.warn("sqlQueryPart expediente relacionado "+sqlQueryPart);
				participantes = ParticipantesUtil.queryParticipantes(cct, sqlQueryPart);
			}
			// Este participante se encuentra dentro del expediente actual
			if (participantes.toList().size() != 0) {
				Iterator<IItem> itPariticipantes = participantes.iterator();

				IItem partic = null;

				String ndoc = "";
				while (itPariticipantes.hasNext()) {
					partic = (IItem) itPariticipantes.next();
					if (partic.getString("NDOC") != null) {
						ndoc = partic.getString("NDOC");
						logger.warn("ndoc " + ndoc);

						//[Dipucr-Manu Ticket#458] - INICIO - ALSIGM3 Error al sellar documentos registrados y comprobar si están en Comparece.
						if(StringUtils.isNotEmpty(ndoc)){
							ndoc = ndoc.trim();
						}
						//[Dipucr-Manu Ticket#458] - FIN - ALSIGM3 Error al sellar documentos registrados y comprobar si están en Comparece.						

						break;
					}
				}
				logger.warn("ndoc " + ndoc);
				AplicacionesServiceProxy asp = new AplicacionesServiceProxy();
				// Compruebo si ese dni pertenece a algun ayuntamiento y
				// tiene representantes.
				boolean representante = existeRepresentante(entitiesAPI, ndoc,
						dni);
				logger.warn("representante " + representante);
				String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
				if (representante) {
					for (int i = 0; i < dni.size(); i++) {

						// TerceroWS tercero =
						// asp.consultarDNI(Constants.COMPARECE.SIGEM,
						// (String)dni.get(i));
						logger.warn("dni representante. " + (String) dni.get(i));
						TerceroWS tercero = asp.consultarDNI(entidad,
								(String) dni.get(i));
						logger.warn("tercero " + tercero);
						if (tercero != null) {
							esUsuarioComp = true;
						} else {
							esUsuarioComp = false;
						}

					}
				} else {
					// TerceroWS tercero =
					// asp.consultarDNI(Constants.COMPARECE.SIGEM, ndoc);
					TerceroWS tercero = asp.consultarDNI(entidad, ndoc);
					logger.warn("ndoc. " + ndoc + " tercero " + tercero);
					dni.add(ndoc);
					if (tercero != null) {
						esUsuarioComp = true;
					} else {
						esUsuarioComp = false;
					}

				}
			}
			// //Este participante se encuentra en expediente relacionados
			// else{
			// sqlQueryPart = "WHERE NOMBRE='"+usuario+"'";
			// participantes =
			// entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES,
			// sqlQueryPart);
			//
			// if(participantes!=null){
			// Iterator itPariticipantes = participantes.iterator();
			// IItem partic = (IItem)itPariticipantes.next();
			// String ndoc ="";
			// if (partic.getString("NDOC")!=null) ndoc =
			// partic.getString("NDOC"); else ndoc="";
			//
			// //Compruebo si ese dni pertenece a algun ayuntamiento y tiene
			// representantes.
			//
			//
			// AplicacionesServiceProxy asp = new AplicacionesServiceProxy();
			// boolean representante = comprarRepresentante(entitiesAPI, ndoc,
			// dni);
			// if(representante){
			// for(int i = 0; i<dni.size(); i++){
			// boolean b = asp.consultarDNI(Constants.COMPARECE.SIGEM,
			// (String)dni.get(i));
			// dni.add(ndoc);
			// esUsuarioComp = b;
			// }
			// }
			// else{
			// boolean b = asp.consultarDNI(Constants.COMPARECE.SIGEM, ndoc);
			// dni.add(ndoc);
			// esUsuarioComp = b;
			//
			// }
			//
			// }
			//
			// }
		} catch (ISPACException e) {
			logger.error("Error en la busqueda de usuarios", e);
			throw new ISPACRuleException("Error. ", e);
		}
		return esUsuarioComp;
	}

	@SuppressWarnings("unchecked")
	private boolean existeRepresentante(IEntitiesAPI entitiesAPI, String ndoc,
			Vector<String> dni) throws ISPACException {
		boolean tieneRepresentante = false;

		// nombre procedimiento PCD-51=Datos del Tercero y Gestión de
		// Representantes BOP
		String sQuery = "WHERE NIFCIFTITULAR ='"
				+ ndoc
				+ "' AND (CODPROCEDIMIENTO='CR-TERC-01' OR CODPROCEDIMIENTO='PCD-222' OR CODPROCEDIMIENTO='PCD-223') AND FCIERRE IS NULL";

		logger.warn("sQuery " + sQuery);

		IItemCollection itemCollectionpCD = entitiesAPI.queryEntities(SpacEntities.SPAC_EXPEDIENTES, sQuery);
		if (itemCollectionpCD != null && itemCollectionpCD.next()) {
			IItem expediente = ((IItem) itemCollectionpCD.iterator().next());
			String numexp = expediente.getString("NUMEXP");
			sQuery = "WHERE NUMEXP='" + numexp + "'";
			itemCollectionpCD = entitiesAPI.queryEntities(Constants.TABLASBBDD.DPCR_PARTICIPANTES_COMPARECE, sQuery);
			Iterator<IItem> it = itemCollectionpCD.iterator();
			while (it.hasNext()) {
				IItem partiComp = (IItem) it.next();
				String notificacion = "";
				if (partiComp.getString("NOTIFICACION") != null)
					notificacion = partiComp.getString("NOTIFICACION");
				else
					notificacion = "";
				if (notificacion.equals("SI")) {
					tieneRepresentante = true;
					String dniRepres = "";
					if (partiComp.getString("DNI_PARTICIPANTE") != null)
						dniRepres = partiComp.getString("DNI_PARTICIPANTE");
					else
						dniRepres = "";
					dni.add(dniRepres);
				}
			}
		}

		return tieneRepresentante;
	}

	void addGrayBand(File file, String pathFileTemp, String infoPagRDE,
			String tipoRegistro, String numRegistro, String fechaRegistro,
			String departamento) throws Exception {

		float margen = Float.parseFloat(ISPACConfiguration.getInstance()
				.getProperty(DipucrSignConnector.MARGIN_BAND));
		float bandSize = Float.parseFloat(ISPACConfiguration.getInstance()
				.getProperty(DipucrSignConnector.SIZE_BAND));

		try {

			PdfReader readerInicial = new PdfReader(file.getAbsolutePath());
			int n = readerInicial.getNumberOfPages();
			int largo = (int) readerInicial.getPageSize(n).getHeight();

			// tamaño de la banda
			// bandSize =
			// Float.parseFloat(ISPACConfiguration.getInstance().getProperty(SIZE_INICIAL_BAND));
			bandSize = 15;

			Image imagen = this.createBgImage();
			Document document = new Document();
			String ruta = FileTemporaryManager.getInstance()
					.getFileTemporaryPath() + "/" + pathFileTemp;
			FileOutputStream fileOut = new FileOutputStream(ruta, true);

			PdfWriter writer = PdfWriter.getInstance(document, fileOut);
			document.open();
			Rectangle r = document.getPageSize();

			for (int i = 1; i <= n; i++) {
				PdfImportedPage page = writer.getImportedPage(readerInicial, i);
				Image image = Image.getInstance(page);

				image.setAbsolutePosition(bandSize, 0.0F);
				image.scaleAbsoluteWidth(r.getWidth() - bandSize);
				image.scaleAbsoluteHeight(r.getHeight());
				imagen.setRotationDegrees(90F);
				document.add(image);
				if (imagen != null) {
					for (int j = 0; j < largo; j = (int) ((float) j + imagen
							.getWidth())) {
						imagen.setAbsolutePosition(0.0F, j);
						imagen.scaleAbsoluteHeight(bandSize);
						document.add(imagen);
					}
				}
				PdfContentByte over = writer.getDirectContent();
				getImagen(over, margen, margen, n, i, tipoRegistro,
						numRegistro, fechaRegistro, departamento);

				document.newPage();
			}

			document.close();

			// INICIO [eCenpri-Felipe Ticket#195]
			// Error al firmar fichero grandes - Cerrar descriptores
			fileOut.close();
			writer.close();
			readerInicial.close();
			// FIN [eCenpri-Felipe Ticket#195]

		} catch (ISPACException e) {
			logger.error("Error al añadir la banda lateral al PDF", e);
			throw new ISPACRuleException("Error. ", e);
		} catch (Exception exc) {
			logger.error("Error al añadir la banda lateral al PDF", exc);
			throw new ISPACException(exc);
		}
	}

	protected Image createBgImage() throws ISPACException {

		// Ruta relativa de la imagen de fondo
		String imagePath = ISPACConfiguration.getInstance().getProperty(PATH_IMAGEN_BAND);
		if (StringUtils.isBlank(imagePath)) {
			imagePath = DEFAULT_PDF_BG_IMAGE_PATH;
		}
		
		try {

			// Ruta absoluta de la imagen de fondo
			//INICIO [dipucr-Felipe #507]
//			String imageFullPath = ConfigurationHelper.getConfigFilePath(imagePath);
			String idEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
			FirmaConfiguration fc = FirmaConfiguration.getInstanceNoSingleton(idEntidad);
			String imageFullPath = fc.getBaseFilePath() + File.separator + imagePath;
			//FIN [dipucr-Felipe #507]
			
			if (logger.isInfoEnabled()) {
				logger.info("Imagen de fondo del PDF: " + imageFullPath);
			}

			// Construir la imagen de fondo
			Image image = Image.getInstance(imageFullPath);
			image.setAbsolutePosition(200F, 400F);
			return image;

		} catch (Exception e) {
			logger.error("Error al leer la imagen de fondo del PDF firmado: "
					+ imagePath, e);
			throw new ISPACException(e);
		}
	}

	protected void getImagen(PdfContentByte pdfContentByte, float margen,
			float x, int numberOfPages, int pageActual, String tipoReg,
			String numReg, String fecReg, String departamento)
			throws ISPACException {

		try {

			String font = ISPACConfiguration.getInstance().getProperty(
					DipucrSignConnector.FONT_BAND);
			String encoding = ISPACConfiguration.getInstance().getProperty(
					DipucrSignConnector.ENCODING_BAND);
			float fontSize = Float.parseFloat(ISPACConfiguration.getInstance()
					.getProperty(DipucrSignConnector.FONTSIZE_BAND));
			// logger.warn("font "+font+" encoding "+encoding+" fontSize "+fontSize);
			// float positionY =
			// Float.parseFloat(ISPACConfiguration.getInstance().getProperty(MARGIN_BAND));

			BaseFont bf = BaseFont.createFont(font, encoding, false);
			pdfContentByte.beginText();
			pdfContentByte.setFontAndSize(bf, fontSize);

			BufferedReader br = new BufferedReader(new FileReader(
					this.getDataFile()));
			String sCadena = null;
			int i = 0;
			String scadenaReg = "";
			// Para que escriba en la tercera línea
			// x += (fontSize*3);
			pdfContentByte.setTextMatrix(0.0F, 1.0F, -1F, 0.0F, x, margen);

			while ((sCadena = br.readLine()) != null) {
				logger.warn("scadenaReg " + scadenaReg);
				if (i == 0) {
					scadenaReg += sCadena + " " + tipoReg + " ";
				}
				if (i == 1) {
					scadenaReg += sCadena + " " + numReg + " ";
				}
				if (i == 2) {
					scadenaReg += sCadena + " " + fecReg + " ";
				}
				if (i == 3) {
					scadenaReg += sCadena + " " + departamento;
				}
				i++;
			}
			pdfContentByte.showText(scadenaReg);

			pdfContentByte.endText();

			// INICIO [eCenpri-Felipe Ticket#195]
			br.close();

		} catch (Exception e) {
			logger.error("Error al componer la imagen de la banda lateral", e);
			throw new ISPACException(e);
		}
	}

	protected File getDataFile() throws ISPACException {

		// Ruta relativa del texto de la banda lateral
		String dataPath = ISPACConfiguration.getInstance().getProperty(
				DEFAULT_PDF_BG_DATA_PATH);
		if (StringUtils.isBlank(dataPath)) {
			dataPath = DEFAULT_PDF_BG_DATA_PATH;
		}

		String basename = null;
		String ext = null;
		int dotIx = dataPath.lastIndexOf(".");
		if (dotIx > 0) {
			basename = dataPath.substring(0, dotIx);
			ext = dataPath.substring(dotIx);
		} else {
			basename = dataPath;
		}

		// Ruta absoluta del texto localizado de la banda lateral
		String dataFullPath = ConfigurationHelper.getConfigFilePath(basename
				+ ext);
		if (StringUtils.isBlank(dataFullPath)) {

			// Ruta absoluta del texto de la banda lateral
			dataFullPath = ConfigurationHelper.getConfigFilePath(dataPath);
		}

		if (logger.isInfoEnabled()) {
			logger.info("Texto de la banda lateraldel PDF: " + dataFullPath);
		}
		// logger.warn("dataFullPath: " + dataFullPath);
		return new File(dataFullPath);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private File concatenaPdf(
			ArrayList<FileInputStream> inputStreamNotificaciones,
			ArrayList<String> filePathNotificaciones, IGenDocAPI genDocAPI)
			throws ISPACRuleException {

		File resultado = null;

		try {
			// Creamos un reader para el documento
			PdfReader reader = null;

			Document document = null;
			PdfCopy writer = null;
			boolean primero = true;// Indica si es el primer fichero (sobre el
									// que se concatenarán el resto de ficheros)
			InputStream f = null;

			int pageOffset = 0;
			ArrayList master = new ArrayList();

			if (inputStreamNotificaciones.size() != 0) {
				Iterator inputStreamNotificacionesIterator = inputStreamNotificaciones
						.iterator();
				while (inputStreamNotificacionesIterator.hasNext()) {
					reader = new PdfReader(
							(InputStream) inputStreamNotificacionesIterator
									.next());
					// FileInputStream file = new
					// FileInputStream(DipucrCommonFunctions.getFile(genDocAPI,(String)inputStreamNotificacionesIterator.next(),"pdf"));
					// reader = new PdfReader((InputStream)file);

					reader.consolidateNamedDestinations();
					int n = reader.getNumberOfPages();
					List bookmarks = SimpleBookmark.getBookmark(reader);

					if (bookmarks != null) {
						if (pageOffset != 0)
							SimpleBookmark.shiftPageNumbers(bookmarks,
									pageOffset, null);
						master.addAll(bookmarks);
					}

					pageOffset += n;

					if (primero) {
						// Creamos un objeto Document
						document = new Document(
								reader.getPageSizeWithRotation(1));
						// Creamos un writer que escuche al documento
						resultado = new File(
								(String) filePathNotificaciones.get(0));

						writer = new PdfCopy(document, new FileOutputStream(
								resultado));

						writer.setViewerPreferences(PdfCopy.PageModeUseOutlines);
						// Abrimos el documento
						document.open();
						primero = false;
					}

					// Añadimos el contenido
					PdfImportedPage page;

					document.newPage();

					for (int i = 0; i < n;) {
						i++;
						page = writer.getImportedPage(reader, i);
						writer.addPage(page);
					}

					// MQE Ticket #180 para poder imprimir en dos caras
					if (n % 2 == 1) {
						document.newPage();
						//[Ticket#153#Teresa] ALSIGM3 Quitar el fichero blanco.pdf del código para que no aparezca en '/tmpcenpri/SIGEM/temp/temporary'
						//String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/blanco.pdf";
						//f = new FileInputStream(rutaFileName);
						File fileCompareceNombre = PdfUtil.blancoPDF();						
						InputStream iPStr = new FileInputStream (fileCompareceNombre);
						
						reader = new PdfReader(iPStr);
						page = writer.getImportedPage(reader, 1);
						writer.addPage(page);
					}
					// MQE Fin modificaciones Ticket #180 para poder imprimir en
					// dos caras
					reader.close();
					if (f != null) {
						f.close();
						f = null;
					}
					// file.close();
				}// while
			}
			if (!master.isEmpty())
				writer.setOutlines(master);
			if (inputStreamNotificaciones.size() != 0)
				document.close();
		} catch (Exception e) {
			logger.warn("Error. "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(), e);
		}
		return resultado;
	}

	public ActionForward muestraDoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		response.setContentType(MimetypeMapping.getMimeType("pdf"));
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ pathFileTempConcatenada + "\"");
		response.setContentLength((int) fileConcatenado.length());

		ServletOutputStream ouputStream = response.getOutputStream();
		FileUtils.copy(fileConcatenado, ouputStream);
		ouputStream.flush();
		ouputStream.close();

		return null;
	}
}