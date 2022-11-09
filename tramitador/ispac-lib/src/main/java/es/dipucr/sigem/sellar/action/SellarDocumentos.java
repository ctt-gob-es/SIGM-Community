package es.dipucr.sigem.sellar.action;

import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.AltaRemesaEnvios;
import https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios.ResultadoAltaRemesaEnvios;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IThirdPartyAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationHelper;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.sign.DatosCompletosFirma;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;
import ieci.tecdoc.sgm.core.services.entidades.Entidad;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.DipucrDatosParticipante;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.notifica.NotificaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.GestorDatosFirma;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.NotificaUtilV2;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfFirmaUtils;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.sgm.tram.sign.DipucrSignConnector;

public class SellarDocumentos{

	public static final Logger logger = Logger.getLogger("loggerRegistro");	
	private IClientContext cct;
	// Id de los documentos seleccionados
	private String idDocSeleccionado = "";
	int contadorNotifica;
		
	File fileNotifica = null;
	Document documentNotifica = null;
	PdfWriter pdfCopyNotifica = null;
	
	AltaRemesaEnvios remesa = null;
	ResultadoAltaRemesaEnvios resultado_remesa = null;

	/**
	 * Ruta por defecto de la imagen de fondo del PDF.
	 */
	private static final String DEFAULT_PDF_BG_IMAGE_PATH = "firma/fondo.gif";
	public final static String PATH_IMAGEN_BAND = "PATH_IMAGE_BAND";//[dipucr-Felipe #507]

	private static final String DEFAULT_PDF_BG_DATA_PATH = "firma/datosFirmaRegistro.txt";
	
	public int taskId;

	File fileConcatenado;
	String pathFileTempConcatenada;

	public SellarDocumentos() {
		this.cct = new ClientContext();
	}

	public SellarDocumentos(IClientContext cct) {
		this.cct = cct;
	}
	
	public SellarDocumentos(IClientContext cct, int taskId) {
		this.cct = cct;
		this.taskId = taskId;
	}
	
	public SellarDocumentos(IClientContext cct, String idDocSeleccionado) throws ISPACException {
		this.cct = cct;
		this.idDocSeleccionado = idDocSeleccionado;
		try {
			this.taskId = (DocumentosUtil.getDocumento(cct.getAPI().getEntitiesAPI(), Integer.parseInt(idDocSeleccionado))).getInt("ID_TRAMITE");
		} catch (NumberFormatException e) {
			logger.error("Error al recuperar el identificador del trámite para el documento: " + idDocSeleccionado + ". El id no es un número entero. " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el identificador del trámite para el documento: " + idDocSeleccionado + ". El id no es un número entero. " + e.getMessage(), e);			
		} catch (ISPACException e) {
			logger.error("Error al recuperar el identificador del trámite para el documento: " + idDocSeleccionado + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar el identificador del trámite para el documento: " + idDocSeleccionado + ". " + e.getMessage(), e);
		}
	}
	
	public SellarDocumentos(IClientContext cct, int taskId, String idDocSeleccionado) {
		this.cct = cct;
		this.taskId = taskId;
		this.idDocSeleccionado = idDocSeleccionado;
	}

	public void sellarDocumentos() throws Exception {

		IItem entityDocument = null;
		int documentId = 0;
		contadorNotifica = 0;
		
		String tipoRegistro = "";
		String numRegistro = "";
		String sFechaRegistro = "";
		String departamento = "";
		String pathFileTemp = "";
		String destino = "";
		String descripcion = "";
		String numexp = "";
		
		File documentoRegistrado = null;
		
		IInvesflowAPI invesFlowAPI = cct.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();	
		IThirdPartyAPI thirdPartyAPI = invesFlowAPI.getThirdPartyAPI();

		int idTipDoc = 0;
		int idPlantillaSellar = 0;
		int idPlantillaComparece = 0;
		ArrayList<File> listaDocumentosSellar = new ArrayList<File>();

		try {
			Font fuenteTitulo = new Font(Font.TIMES_ROMAN);
			fuenteTitulo.setStyle(Font.BOLD);
			fuenteTitulo.setSize(15);

			Font fuenteDocumento = new Font(Font.TIMES_ROMAN);
			fuenteDocumento.setStyle(Font.BOLD);
			
			idTipDoc = DocumentosUtil.getTipoDoc(cct, "Documentos Sellados", DocumentosUtil.BUSQUEDA_EXACTA, false);

			IItemCollection tipoDocSellarCollection = cct.getAPI().getEntitiesAPI().queryEntities("SPAC_P_PLANTDOC", 
					"WHERE NOMBRE='Documentos Sellados' AND ID_TPDOC=" + idTipDoc + ";");
			Iterator<?> tipoDocSellarIterator = tipoDocSellarCollection.iterator();
			
			if (tipoDocSellarIterator.hasNext()) {
				idPlantillaSellar = ((IItem) tipoDocSellarIterator.next()).getInt("ID_TPDOC");
			}
			
			IItemCollection tipoDocCompareceCollection = cct.getAPI().getEntitiesAPI().queryEntities("SPAC_P_PLANTDOC", 
					"WHERE NOMBRE='" + Constants.COMPARECE.PARTICIPANTES_COMPARECE + "' AND ID_TPDOC=" + idTipDoc + ";");
			Iterator<?> tipoDocCompareceIterator = tipoDocCompareceCollection.iterator();
			
			if (tipoDocCompareceIterator.hasNext()) {
				idPlantillaComparece = ((IItem) tipoDocCompareceIterator.next()).getInt("ID");
			}
		
			String consultaDocumentosRegistrados = "WHERE ID_TRAMITE=" + taskId + " AND (NREG IS NOT NULL AND NREG != '')";
			
			if (StringUtils.isNotEmpty(this.idDocSeleccionado)) {
				logger.debug("Se va a sellar o enviar a Notifica el documento: " + this.idDocSeleccionado);
				consultaDocumentosRegistrados += " AND ID = " + this.idDocSeleccionado;
			}
			consultaDocumentosRegistrados += " AND UPPER(EXTENSION_RDE) = 'PDF' AND ID_NOTIFICACION IS NULL ORDER BY DESCRIPCION;";
			logger.debug("Se van a enviar a Notifica o sellar los documentos: " + consultaDocumentosRegistrados);

			IItemCollection documentosRegistradosCollection = DocumentosUtil.queryDocumentos(cct, consultaDocumentosRegistrados);
			Iterator<?> documentosRegistradosIterator = documentosRegistradosCollection.iterator();

			pathFileTempConcatenada = FileTemporaryManager.getInstance().newFileName(".pdf");
			
			if(!documentosRegistradosIterator.hasNext())
				//[dipucr-Felipe #1606] Error controlado ISPACInfo. HTML que saldrá en popup de avisos
				throw new ISPACInfo("<b>NO SE HA PODIDO REALIZAR EL REGISTRO DE SALIDA</b>"
						+ "<br/>Para registrar un documento se deben cumplir los siguientes requisitos:"
						+ "<br/><br/><b>1.</b> Debe SELECCIONAR el documento a registrar o pulsar sobre el botón 'Registrar todos'."
						+ "<br/><b>2.</b> Debe estar FIRMADO."
						+ "<br/><b>3.</b> Debe ser de tipo SALIDA y tener asignado un DESTINO, que se carga de los participantes del expediente. "
						+ "Si no tiene destino, asignelo dentro de las propiedades del documento (icono carpeta).");
			
			while (documentosRegistradosIterator.hasNext()) {
				tipoRegistro = "";
				numRegistro = "";
				sFechaRegistro = "";
				departamento = "";
				destino = "";
				descripcion = "";
				numexp = "";
				IItem document = (IItem) documentosRegistradosIterator.next();

				String infoPag = document.getString("INFOPAG");
				String infoPagRDE = document.getString("INFOPAG_RDE");

				documentoRegistrado = null;
				if (StringUtils.isNotBlank(infoPagRDE)) {
					documentoRegistrado = DocumentosUtil.getFile(cct, infoPagRDE, null, null);
				} else {
					documentoRegistrado = DocumentosUtil.getFile(cct, infoPag, null, null);
				}			

				int idDoc = 0;

				idDoc = document.getInt("ID");

				pathFileTemp = FileTemporaryManager.getInstance().put(documentoRegistrado.getAbsolutePath(), ".pdf");
				
				if (document.getString("TP_REG") != null)
					tipoRegistro = document.getString("TP_REG");
				else
					tipoRegistro = "";
				
				if (document.getString("NREG") != null)
					numRegistro = document.getString("NREG");
				else
					numRegistro = "";
				
				if (document.getDate("FREG") != null)
					sFechaRegistro = FechasUtil.getFormattedDate(document.getDate("FREG"), "dd/MM/yyyy HH:mm:ss");
				else
					sFechaRegistro = "";
				
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
				
				if (document.getString("DESTINO") != null)
					destino = document.getString("DESTINO");
				else
					destino = "";
				
				//Agustin #414 Integracion de Notifica-DEH
				//Manda la notificacion a Comparece o Notifica
				enviaNotificacion(cct, thirdPartyAPI, numexp, document);
				
				try{
					
					if(!(ConfigurationMgr.getVarGlobal(cct, Constants.NOTIFICA.API_KEY_NOTIFICA).equals(""))){
						generaParcipantesNotifica(entitiesAPI, numexp, idDoc, descripcion, destino, fuenteTitulo, fuenteDocumento, sFechaRegistro, 
								documentoRegistrado,pathFileTemp, infoPagRDE, tipoRegistro, numRegistro, departamento, document, listaDocumentosSellar);
					}
					else{
						logger.info("La entidad no tiene configurado la variable de sistema API_KEY_NOTIFICA, no se generan participantes Notifica");
					}
				
				}
			    catch(Exception e){
			    	logger.error("La entidad no tiene configurado la variable de sistema API_KEY_NOTIFICA, expcepcion:"+e.getMessage());
			    }
			}			
			
			if (fileNotifica != null && fileNotifica.exists()) {
				documentNotifica.close();
				pdfCopyNotifica.close();
				
				cct.beginTX();		
				entityDocument = DocumentosUtil.generaYAnexaDocumento(cct, taskId, idTipDoc, Constants.NOTIFICA.PARTICIPANTES_NOTIFICA,	fileNotifica, Constants._EXTENSION_PDF);
				entityDocument.set("ID_PLANTILLA", idPlantillaComparece);
				
				entityDocument.store(cct);
				cct.endTX(true);
				fileNotifica.delete();
			}			
			
			// MQE ticket #325
			
			generaDocumentosSelladosRegistradosMenosLosDeCompareceParaEnviarlosPorCarta(listaDocumentosSellar);
			
			if (listaDocumentosSellar != null && !listaDocumentosSellar.isEmpty()) {
				fileConcatenado = PdfUtil.concatenarArchivos(listaDocumentosSellar, true);

				if (fileConcatenado != null) {
					// Creamos el nuevo documento.
					cct.beginTX();
						entityDocument = genDocAPI.createTaskDocument(taskId, idTipDoc);
						
						entityDocument.set("EXTENSION", "pdf");
						entityDocument.set("ID_PLANTILLA", idPlantillaSellar);
						entityDocument.set("DESCRIPCION", "Documentos Sellados");
						entityDocument.store(cct);
					cct.endTX(true);
					
					documentId = entityDocument.getKeyInt();
					
					DocumentosUtil.anexaDocumento(cct, genDocAPI, taskId, documentId, fileConcatenado, Constants._EXTENSION_PDF, "Documentos Sellados");
				}
			}

			int idTipDocAux = DocumentosUtil.getTipoDoc(cct, Constants.COMPARECE.PARTICIPANTES_COMPARECE, DocumentosUtil.BUSQUEDA_EXACTA, false);
			if(idTipDocAux > Integer.MIN_VALUE){
				idTipDoc = idTipDocAux;
			}
		} catch (ISPACInfo e) {
			logger.error("Error controlado al sellar el documento. " + e.getMessage(), e);
			e.setRefresh(false);
//			throw new ISPACException("Error al registrar el documento. " + e.getMessage(), e);
			throw e; //[dipucr-Felipe #1606] Lanzamos las excepciones ISPACInfo sin convertir para que se muestren en el popup de información
		} catch (Exception e) {
			logger.error("Error al sellar el documento. " + e.getMessage(), e);
			throw new ISPACException("Error al registrar el documento."+ e.getMessage(), e);
			
		} finally {			

			if(fileConcatenado != null && fileConcatenado.exists()) fileConcatenado.delete();
			if(listaDocumentosSellar != null && listaDocumentosSellar.size() > 0){
				for(int i = 0; i < listaDocumentosSellar.size(); i++){
					if(listaDocumentosSellar.get(i) != null && listaDocumentosSellar.get(i).exists()) listaDocumentosSellar.get(i).delete();
				}
			}		
			if (fileNotifica != null && fileNotifica.exists()) {
				fileNotifica.delete();
			}
			if(documentoRegistrado != null && documentoRegistrado.exists()) documentoRegistrado.delete();
			//[Dipucr-Manu Tikcet #406] + ALSIGM3 Registrar el tramitador es un poco lento
			//			System.gc();
		}
	}
	
	private void generaDocumentosSelladosRegistradosMenosLosDeCompareceParaEnviarlosPorCarta(ArrayList<File> listaDocumentosSellar) throws Exception{
		
		logger.info("carga de documentos que no se han enviado a Notifica ni a Comparece");
		
		String tipoRegistro = "";
		String numRegistro = "";
		String sFechaRegistro = "";
		String departamento = "";
		String destino = "";
		String descripcion = "";
		String numexp = "";
		String infoPag = "";
		String infoPagRDE = ""; 
		int idDoc = 0;
		String pathFileTemp = "";
		String rutaFileName = "";
		File auxFile= null;
		
		String consultaDocumentosConEstadoEnvioPostal =  "WHERE ID_TRAMITE=" + taskId + " AND (NREG IS NOT NULL AND NREG != '') AND UPPER(EXTENSION_RDE) = 'PDF'  AND ESTADONOTIFICACION !='CO' ORDER BY DESCRIPCION";		

		IItemCollection documentosRegistradosCollectionConEstadoEnvioPostal;
		
		try {
		
		documentosRegistradosCollectionConEstadoEnvioPostal = DocumentosUtil.queryDocumentos(cct, consultaDocumentosConEstadoEnvioPostal);
		
		Iterator<?> documentosRegistradosCollectionConEstadoEnvioPostalIterator = documentosRegistradosCollectionConEstadoEnvioPostal.iterator();
		
		if(documentosRegistradosCollectionConEstadoEnvioPostalIterator.hasNext()){			
		
			while (documentosRegistradosCollectionConEstadoEnvioPostalIterator.hasNext()) {
				
				IItem document = (IItem) documentosRegistradosCollectionConEstadoEnvioPostalIterator.next();
				
				if (document != null){
					
						infoPag = document.getString("INFOPAG");
						infoPagRDE = document.getString("INFOPAG_RDE");		
						idDoc = document.getInt("ID");						
						
						if (document.getString("TP_REG") != null)
							tipoRegistro = document.getString("TP_REG");
						else
							tipoRegistro = "";
						
						if (document.getString("NREG") != null)
							numRegistro = document.getString("NREG");
						else
							numRegistro = "";
						
						if (document.getDate("FREG") != null)
							sFechaRegistro = FechasUtil.getFormattedDate(document.getDate("FREG"), "dd/MM/yyyy HH:mm:ss");
						else
							sFechaRegistro = "";
						
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
						
						if (document.getString("DESTINO") != null)
							destino = document.getString("DESTINO");
						else
							destino = "";						
									
						File documentoRegistradoConEstadoEnvioPostal = null;
						
						if (StringUtils.isNotBlank(infoPagRDE)) {
							
							documentoRegistradoConEstadoEnvioPostal = DocumentosUtil.getFile(cct, infoPagRDE, null, null);
							
						} else {
							
							documentoRegistradoConEstadoEnvioPostal = DocumentosUtil.getFile(cct, infoPag, null, null);
							
						}
						
						pathFileTemp = FileTemporaryManager.getInstance().put(documentoRegistradoConEstadoEnvioPostal.getAbsolutePath(), ".pdf");
						
						//[dipucr-Felipe #1246] Portafirmas - Compatibilidad con firma 3 fases
						IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
						if (!entitiesAPI.isDocumentPortafirmas(document)){
							addDatosFirma(documentoRegistradoConEstadoEnvioPostal, pathFileTemp, document); //[dipucr-Felipe #791-3]
						}
						
						File file = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp);
						pathFileTemp = FileTemporaryManager.getInstance().put(file.getAbsolutePath(), ".pdf");
						addGrayBand(file, pathFileTemp, infoPagRDE, tipoRegistro, numRegistro, sFechaRegistro, departamento);
						
						
						rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
						logger.debug("Documento sellado: '" + descripcion + "', ruta: " + rutaFileName);
						auxFile = new File(rutaFileName);
				        
						if(auxFile!=null)
							
							listaDocumentosSellar.add(auxFile);	
						
						}
				else
				{
					logger.error("ERROR PROBLEMA AL CARGA DOCUMENTO QUE NO SE HA MANDADO POR NOTIFICACION ELECTRONICA");
				}
			}
		}
		else
		{
			logger.info("Todos los documentos enviados por Notificación Electrónica");
		}		
		
		} catch (ISPACException e) {
			logger.error("ERROR AL CARGAR EN LA LISTA DE DOCUMENTOS SELLADOS LOS DOCUMENTOS QUE NO SE HAN ENVIADO POR NOTIFICACION ELECTRONICA " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("ERROR AL GENERAR BANDA GRIS DE DOCUMENTOS SELLADOS " + e.getMessage(), e);
			throw new Exception("ERROR AL GENERAR BANDA GRIS DE DOCUMENTOS SELLADOS", e);
		}
		
	}	
	
	/**
	 * [dipucr-Felipe #791-3]
	 * Simula los datos de firma en modo imagen para el documentos sellados
	 * @param file
	 * @param document
	 * @throws Exception 
	 */
	private void addDatosFirma(File file, String pathFileTemp, IItem document) throws Exception {
		
		String idEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
		PdfReader reader = new PdfReader(file.getAbsolutePath());
		
		String ruta = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
		FileOutputStream fileOut = new FileOutputStream(ruta, true);
		
		List<DatosCompletosFirma> listFirmas = GestorDatosFirma.getFirmasDocumento(cct, document, idEntidad);
        int numberOfSigners = listFirmas.size();
        int signerPosition = 1;
        
        PdfStamper stp = new PdfStamper(reader, fileOut);
        int numberOfPages = reader.getNumberOfPages();
        
	    for (DatosCompletosFirma datosFirma : listFirmas){
        	
        	PdfFirmaUtils.addImagenFirma(cct, idEntidad, datosFirma.getFirmante(), document, stp, 
        			numberOfPages, fileOut, numberOfSigners, signerPosition, datosFirma.getFechaFirma());
        
        	signerPosition++;
        }
        stp.close();
        reader.close();
        fileOut.close();
	}
	

	
	private void generaParcipantesNotifica(IEntitiesAPI entitiesAPI, String numexp, int idDoc, String descripcion,String destino, Font fuenteTitulo, Font fuenteDocumento, String sFechaRegistro, File documentoRegistrado, String pathFileTemp, String infoPagRDE, String tipoRegistro, String numRegistro, String departamento, IItem document, ArrayList<File> listaDocumentosSellar) throws Exception{
		
		IItemCollection enviadosNotificaCollection = entitiesAPI.getEntities(Constants.TABLASBBDD.DPCR_ACUSES_NOTIFICA, numexp, " IDENT_DOC = '" + idDoc + "'");
		Iterator<?> enviadosNotificaIterator = enviadosNotificaCollection.iterator();			
		File fileSello = null;
		String rutaFileName="";
		
		if(enviadosNotificaIterator.hasNext()){
			contadorNotifica++;
			if(fileNotifica == null){
				//fileNotifica = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/documentoNotifica.pdf");
				fileNotifica = new File(FileTemporaryManager.getInstance().getFileTemporaryPath()  + File.separator + FileTemporaryManager.getInstance().newFileName(".pdf"));						
				documentNotifica = new Document();

				pdfCopyNotifica = PdfCopy.getInstance(documentNotifica, new FileOutputStream(fileNotifica));
				documentNotifica.open();

				String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
				String imageLogoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + NotificaConfiguration.getInstanceNoSingleton(entidad).getProperty(NotificaConfiguration.IMAGE_LOGO_PATH_DIPUCR);
				String imageFondoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + NotificaConfiguration.getInstanceNoSingleton(entidad).getProperty(NotificaConfiguration.IMAGE_FONDO_PATH_DIPUCR);
				String imagePiePath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + NotificaConfiguration.getInstanceNoSingleton(entidad).getProperty(NotificaConfiguration.IMAGE_PIE_PATH_DIPUCR);

				File logoURL = new File(imageLogoPath);
				if (logoURL != null) {
					Image logo = Image.getInstance(imageLogoPath);
					logo.scalePercent(50);
					documentNotifica.add(logo);
				}
				File fondoURL = new File(imageFondoPath);
				if(fondoURL != null){
					Image fondo = Image.getInstance(imageFondoPath);
					fondo.setAbsolutePosition(250, 50);
					fondo.scalePercent(70);
					documentNotifica.add(fondo);
					
				}

				File pieURL = new File(imagePiePath);
				if(pieURL != null){
					Image pie = Image.getInstance(imagePiePath);
					pie.setAbsolutePosition(documentNotifica.getPageSize().getWidth() - 550, 15);
					pie.scalePercent(80);
					documentNotifica.add(pie);
					
				}

				documentNotifica.add(new Phrase("\n\n"));
				Paragraph titulo = new Paragraph(new Phrase("DOCUMENTOS ENVIADOS A NOTIFICA", fuenteTitulo));
				titulo.setAlignment(Element.ALIGN_CENTER);
				documentNotifica.add(titulo);
				documentNotifica.add(new Phrase("\n"));
			}
			logger.debug("Documento enviado a Notifica: " + descripcion);
			
			logger.debug("Insertar Ayuntamiento en documento Notifica. " + destino);

			documentNotifica.add(new Phrase(contadorNotifica + ".- Destino del registro: " + destino + " : "));
			documentNotifica.add(new Phrase("\n"));					
			documentNotifica.add(new Phrase("\t\tDocumento: " + descripcion + " : ", fuenteDocumento));
			documentNotifica.add(new Phrase("\n"));
			documentNotifica.add(new Phrase("\t\t\t Interesado o Representante - ID Notificación - Fecha de notificación"));
			documentNotifica.add(new Phrase("\n"));
			int contadorRepresentantes = 0;
			while(enviadosNotificaIterator.hasNext()){	
				contadorRepresentantes++;
				//se ha enviado por Notifica
				IItem enviadosNotificaItem = (IItem) enviadosNotificaIterator.next();
				String dniNotificado = StringUtils.defaultString(enviadosNotificaItem.getString("DNI_NOTIFICADO"));
				String idNotificacion = StringUtils.defaultString(enviadosNotificaItem.getString("ID_NOTIFICACION"));
				
				documentNotifica.add(new Phrase("\t\t\t" + dniNotificado + " - " + idNotificacion + " - " + sFechaRegistro + "."));
				documentNotifica.add(new Phrase("\n"));
			}
			documentNotifica.add(new Phrase("\n"));
		}

	}

	
	
	private void enviaNotificacion(IClientContext ctx, IThirdPartyAPI thirdPartyAPI, String numexp, IItem documento) throws ISPACException {
		try {
			
			String nif="";
			
			
				int idDoc = documento.getKeyInt();
				
				String descripcion = "";
				String asunto = "";
				String nombreDoc = documento.getString("NOMBRE");
				String destinatario = documento.getString("DESTINO");
				String destinatarioId = documento.getString("DESTINO_ID");				
				
				logger.debug("Usuario: " + destinatario + ", usuarioDestinoId: " + destinatarioId);
				logger.debug("Descripción: " + descripcion);
					
				Vector<String> dnisRepresentantesComparece = new Vector<String>();
				
				DipucrDatosParticipante interesado = new DipucrDatosParticipante();
				File documentoEnviarNotifica = null;
				
				if(StringUtils.isNotEmpty(destinatario)){
					
					String infoPag = documento.getString("INFOPAG_RDE");
					
					if(StringUtils.isEmpty(infoPag))
						infoPag = documento.getString("INFOPAG"); 
					
					String extension = documento.getString("EXTENSION_RDE");
				
					if(StringUtils.isEmpty(extension))
						extension = documento.getString("EXTENSION");
					
					//byte[] documentContent;
					//[Dipucr-Manu Ticket #404] - INICIO - ALSIGM3 Problema al registrar y enviar a comparece.
					documentoEnviarNotifica = DocumentosUtil.getFile(ctx, infoPag, "", extension);
					//[Dipucr-Manu Ticket #404] - FIN - ALSIGM3 Problema al registrar y enviar a comparece.
					
					//documentContent = IOUtils.toByteArray(new FileInputStream(documentoEnviarComparece));
			
					try{
						asunto = ExpedientesUtil.getAsunto(ctx, numexp);
					}
					catch(ISPACException e){
						logger.error(e.getMessage(), e);
						asunto = "";
					}
					
					descripcion = nombreDoc + "  -  Ref.Exp.: " + numexp + " - Asunto Exp.: " + asunto;		   	    
									
					IInvesflowAPI invesFlowAPI = ctx.getAPI();
					IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
					
					IItem documentoItem = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
					if(documentoItem != null){
						
						if (documentoItem.getString("NUMEXP") != null)
							numexp = DipucrCommonFunctions.limpiarCaracteresEspeciales(documentoItem.getString("NUMEXP"));
						else
							numexp = "";
						try {
							interesado = this.dameParticipante(ctx, numexp, destinatario, destinatarioId, dnisRepresentantesComparece, descripcion);
							
							//Validacion del tipo persona, ver en la documentacion de notifica el fichero de hacienda de tipos de nif
							//String tipo_persona = documento.getString("DESTINO_TIPO");
							if(null == interesado.getNif()){
								logger.error("Error, existe un participante del expediente con el nif vacio, debe corregirlo:"+interesado.getNombreDestinatario());
								//throw new ISPACRuleException("Error, existe un participante del expediente con el nif vacio, debe corregirlo");	
								cct.beginTX();
								documento.set("DESTINO_TIPO","");
								documento.store(cct);
								cct.endTX(true);
							}								
							else if(interesado.getNif().startsWith("A")||//Sociedades anónimas
								interesado.getNif().startsWith("B")||//Sociedades de responsabilidad limitada
							   	interesado.getNif().startsWith("C")||//Sociedades colectivas
							   	interesado.getNif().startsWith("D")||//Sociedades comanditarias
								interesado.getNif().startsWith("E")||//Comunidades de bienes y herencias yacentes
								interesado.getNif().startsWith("F")||//Sociedades cooperativas
								interesado.getNif().startsWith("G")||//Asociaciones
								interesado.getNif().startsWith("H")||//Comunidades de propietarios en régimen de propiedad horizontal
								interesado.getNif().startsWith("J")||//Sociedades civiles, con o sin personalidad jurídica
								interesado.getNif().startsWith("P")||//Corporaciones Locales
								interesado.getNif().startsWith("Q")||//Organismos públicos
								interesado.getNif().startsWith("R")||//Congregaciones e instituciones religiosas
								interesado.getNif().startsWith("S")||//Órganos de la Administración del Estado y de las Comunidades Autónomas
								interesado.getNif().startsWith("U")||//Uniones Temporales de Empresas
								interesado.getNif().startsWith("V")||//Otros tipos no definidos en el resto de claves
								interesado.getNif().startsWith("N")||//Entidades extranjeras
								interesado.getNif().startsWith("W"))//Establecimientos permanentes de entidades no residentes en España
							{
								cct.beginTX();
								documento.set("DESTINO_TIPO","J");
								documento.store(cct);
								cct.endTX(true);			
							}
							else{
								cct.beginTX();
								documento.set("DESTINO_TIPO","F");
								documento.store(cct);
								cct.endTX(true);
							}
							
						} catch (RemoteException e) {
							logger.error("Error en el envío de las notificaciones electrónicas, es posible que se haya cortado la comunicación con la plataforma Notifica o Comparece. " + e.getMessage(), e);
							throw new ISPACRuleException("Error en el envío de las notificaciones electrónicas, es posible que se haya cortado la comunicación con la plataforma Notifica o Comparece. " + e.getMessage(), e);
						} catch (ISPACRuleException e) {
							logger.error("Error en el envío de las notificaciones electrónicas, revise los datos de los participantes del expediente. " + e.getMessage(), e);
							throw new ISPACRuleException("Error en el envío de las notificaciones electrónicas, revise los datos de los participantes del expediente. " + e.getMessage(), e);
						}
						
												
						//Agustin #414 Integracion de Notifica-DEH
						
							
							if(null!=interesado.getNif()){
								
								nif = interesado.getNif().trim();						
							
									
								Entidad entidadAdm = EntidadesAdmUtil.obtenerEntidadObject(ctx);
								String destino = DocumentosUtil.getAutorUID(entitiesAPI, idDoc);
								
								// Notifica v1 #1550
								/*
								Tipo_envio envio_type = NotificaUtil.para_notifica_altaEnvio_generaTipoEnvio(
										numexp, //concepto
										entidadAdm.getDir3(), //codigoDIR3
										entidadAdm.getNombreLargo(), //nombre de la entidad													
										documentContent, //pathDocumentoNotificacion 
										"no", //generaCSV 
										NotificaUtil.SHAsum(documentContent), //hash_sha1
										"no", //normalizadoParaCIE
										entidadAdm.getDeh(), //codigo procedimiento DEH
										nif, //nif interesado DEH
										false, //obligadoInteresado,
										10, //diasAntesDeCaducar,
										0, //diasEnCarpetaCiudadana,
										0, //diasDeEsperaAntesDeEnviar,
										nif, //nifDestinatario
										interesado.getNombreDestinatario(), //nombreDestinatario, 
										"", //apellidosDestinatario,
										interesado.getEmailDestinatario(), //emailDestinatario,
										interesado.getTelefonoDestinatario(), //telefonoDestinatario,
										String.valueOf(documentoItem.getKeyInt()), //idNotificacionReferenciaEmisor,
										"normal", //servicioNormaOurgente,
										"concreto", //tipo_domicilio,
										entidadAdm.getSia(), //codigoSIA, 
										"Notificación Electrónica Genérica", //nombreDeProcedimientoSIA
										"Notificación"//notificación o comunicación tipoEnvio
								);		
								
								if(!(ConfigurationMgr.getVarGlobal(ctx, Constants.NOTIFICA.API_KEY_NOTIFICA).equals(""))){
									NotificaUtil nu = new NotificaUtil(ConfigurationMgr.getVarGlobal(ctx, Constants.NOTIFICA.API_KEY_NOTIFICA));							
									nu.notifica_altaEnvio(ctx, numexp, destino, idDoc, documentoItem, envio_type);									
								}
								else{									
									logger.info("La entidad no tiene configurado la variable de sistema API_KEY_NOTIFICA, la notificación se enviará sólo por Comparece");									
								}
								
								*/
								
								
								// Notifica v2 #1550
								
								if(!(ConfigurationMgr.getVarGlobal(ctx, Constants.NOTIFICA.API_KEY_NOTIFICA).equals(""))){
									
										NotificaUtilV2 nuv2 = new NotificaUtilV2(ConfigurationMgr.getVarGlobal(ctx, Constants.NOTIFICA.API_KEY_NOTIFICA));
																				
										String asunto_ = DocumentosUtil.getNumExp(entitiesAPI, idDoc).concat(": ").concat(ExpedientesUtil.getAsunto(ctx, DocumentosUtil.getNumExp(entitiesAPI, idDoc)));
																		
										//Mandar asunto del expediente en el asunto de la notificacion, quitar caracteres que no sean letras o numeros
										String normalized = Normalizer.normalize(asunto_, Form.NFD);
										//asunto_ = normalized.replaceAll("[^A-Za-z0-9]", " ");
										asunto_ = normalized.replaceAll("[^ A-Za-z0-9/:]", "");
										
										if(asunto_.length()>250)
											asunto_=asunto_.substring(0,249);
								
										Date date = new Date();
									    long timeMilli = date.getTime();
										remesa = nuv2.getClienteNotifica().para_notifica_altaremesa(
														asunto_, //no puedo mandar mas de 255 caracteres
														DocumentosUtil.getNombreDocumento(entitiesAPI,idDoc),
														documentoEnviarNotifica,
														entidadAdm.getDir3(),
														interesado.getDir3(),
														nif,
														interesado.getNombreDestinatario().trim(),
														interesado.getApellidosDestinatario().trim(),
														10, //int diasParaCaducar
														new String(String.valueOf(idDoc)).concat(String.valueOf(timeMilli)),
														entidadAdm.getSia(),
														"2" //tipoEnvio notificación o comunicación.			
										);												
										try{
											if(StringUtils.isNotEmpty(interesado.getEmailDestinatario())){
												String mailInicio = "<img src='cid:escudo' width='200px'/>"
														+ "<p align='justify' style='font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 11.5px; color: #3c5876; margin-left: 10px'>";
												StringBuffer sbContenido = new StringBuffer(mailInicio);
												sbContenido.append("<b>ESTE EMAIL SE CORRESPONDE CON UN AVISO DE UNA NOTIFICACIÓN.</b></br>");
												sbContenido.append("Le informamos que está disponible una nueva notificación para "+interesado.getApellidosDestinatario().toUpperCase()
														+" "+interesado.getNombreDestinatario().toUpperCase()+" con los siguientes datos:</br>");										
												

												sbContenido.append("<ul><li type='circle'>Organismo emisor: "+EntidadesAdmUtil.obtenerNombreLargoEntidadById(ctx)+"</li>");
												sbContenido.append("<li type='circle'>Concepto: "+numexp+"</li>");
												sbContenido.append("<li type='circle'>Asunto: "+asunto+"</li>");
												sbContenido.append("</ul>");
												
												String enlaceHtml = "Puede acceder a esta notificación en la Dirección Electrónica Habilitada única (DEHú), disponible en: "
														+ "<a href='https://dehu.redsara.es/' target='_blank'>https://dehu.redsara.es/</a> </br>";
												sbContenido.append(enlaceHtml);
												sbContenido.append("De acuerdo con lo previsto en los artículos 41 y 43 de la Ley 39/2015, de 1 de octubre, del Procedimiento Administrativo Común de las Administraciones Públicas, la aceptación de la notificación, el rechazo expreso de la notificación o bien la presunción de rechazo por no haber accedido a la notificación durante el periodo de puesta a disposición, dará por efectuado el trámite de notificación y se continuará el procedimiento.</br>");
												
												sbContenido.append("Usted puede recibir esta notificación por distintas vías electrónicas o incluso en papel por vía postal. Si usted accediera al contenido de esta notificación por más de una de estas vías, sepa que los efectos jurídicos, si los hubiera, siempre empiezan a contar desde la fecha en que se produzca su primer acceso.</br>");
												
												
												String asuntEmail = "Envío: Aviso de nueva notificación puesta a disposición";
												MailUtil.enviarCorreoConAcusesLogo(ctx, numexp, interesado.getEmailDestinatario(), null, sbContenido.toString() , asuntEmail, nombreDoc, interesado.getNombreDestinatario().trim(), false);
											}											
										} catch (AddressException e) {
											logger.error("Error en el envío del email " + numexp + ". " + e.getMessage(), e);
										} catch (SendFailedException e) {
											logger.error("Error en el envío del email " + numexp + ". " + e.getMessage(), e);
										}
															
										resultado_remesa=nuv2.notifica_altaRemesaEnvios(ctx, numexp, destino, idDoc, documentoItem, remesa);
								}
								else{
										
										logger.info("La entidad no tiene configurado la variable de sistema API_KEY_NOTIFICA, la notificación se enviará sólo por Comparece");
										
								}
								
								
								
							
							}
							
							//Si el nif es nulo no envia la notificacion marca el estado de envío de la notificación como erróneo
							else{
								
								documentoItem.set("ESTADONOTIFICACION", Constants.NOTIFICACIONES_VALOR.VALOR_ESTADO_ERROR);
								documentoItem.store(ctx);
								
							}

							
						}
						
					
					}				
				
			
		} catch (ISPACException e1) {
			logger.error("Error al enviar el documento a notifica del expediente " + numexp + ". " + e1.getMessage(), e1);
			throw new ISPACException("Error SellarDocumentos al enviar el documento a comparece del expediente " + numexp + ". " + e1.getMessage(), e1);
		} catch (FileNotFoundException e1) {
			logger.error("Error al recuperar el contenido del documentos del expediente " + numexp + ". " + e1.getMessage(), e1);
			throw new ISPACException("Error SellarDocumentos al recuperar el contenido del documentos del expediente " + numexp + ". " + e1.getMessage(), e1);
		} catch (IOException e1) {
			logger.error("Error al recuperar el contenido del documentos del expediente " + numexp + ". " + e1.getMessage(), e1);
			throw new ISPACException("Error SellarDocumentos al recuperar el contenido del documentos del expediente " + numexp + ". " + e1.getMessage(), e1);
		} catch (NoSuchAlgorithmException e1) {
			logger.error("Error NoSuchAlgorithmException " + numexp + ". " + e1.getMessage(), e1);
			throw new ISPACException("Error SellarDocumentos NoSuchAlgorithmException" + numexp + ". " + e1.getMessage(), e1);
		}
	}
	
	public String generateHashCode(SignDocument signDocument, ClientContext cct) throws ISPACException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String infoPag = signDocument.getItemDoc().getString("INFOPAG");
		if (StringUtils.isEmpty(infoPag)) {
			infoPag = signDocument.getItemDoc().getString("INFOPAG_RDE");
			throw new ISPACInfo("No existe un documento físico para poder firmar");
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
		logger.debug("hexHash byte " + hash.toString());

		String hexHash = toHexadecimal(hash);
		logger.debug("hexHash " + hexHash);

		return hexHash;
	}

	public String generateIdHash(String cadena) throws ISPACRuleException {

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

	void addGrayBand(File file, String pathFileTemp, String infoPagRDE, String tipoRegistro, String numRegistro, String fechaRegistro, String departamento) throws Exception {

		float margen = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.MARGIN_BAND));
		float bandSize = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.SIZE_BAND));

		try {
			PdfReader readerInicial = new PdfReader(file.getAbsolutePath());

			int n = readerInicial.getNumberOfPages();
			int largo = (int) readerInicial.getPageSize(1).getHeight();
			int ancho = (int) readerInicial.getPageSize(1).getWidth();
			Rectangle r = new Rectangle(ancho, largo);

			bandSize = 15;

			Image imagen = this.createBgImage();
			Document document = new Document(r);
			String ruta = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
			FileOutputStream fileOut = new FileOutputStream(ruta, true);

			PdfWriter writer = PdfWriter.getInstance(document, fileOut);
			document.open();

			for (int i = 1; i <= n; i++) {
				PdfImportedPage page = writer.getImportedPage(readerInicial, i);
				Image image = Image.getInstance(page);

				image.setAbsolutePosition(bandSize, 0.0F);
//				image.setAbsolutePosition(0.0F, 0.0F);
				image.scaleAbsoluteWidth(r.getWidth() - bandSize);
				image.scaleAbsoluteHeight(r.getHeight());
				imagen.setRotationDegrees(90F);
				document.add(image);
				if (imagen != null) {
					for (int j = 0; j < largo; j = (int) ((float) j + imagen.getWidth())) {
						imagen.setAbsolutePosition(0.0F, j);
						imagen.scaleAbsoluteHeight(bandSize);
						document.add(imagen);
					}
				}
				PdfContentByte over = writer.getDirectContent();
				getImagen(over, margen, margen, n, i, tipoRegistro,	numRegistro, fechaRegistro, departamento);

				if(i < n){
					largo = (int) readerInicial.getPageSize(i + 1).getHeight();
					ancho = (int) readerInicial.getPageSize(i + 1).getWidth();
					r = new Rectangle(ancho, largo);
					document.setPageSize(r);
				}
				
				document.newPage();
			}

			document.close();

			fileOut.close();
			writer.close();
			readerInicial.close();

		} catch (ISPACException e) {
			logger.error("Error al añadir la banda lateral al PDF " + e.getMessage(), e);
			throw new ISPACRuleException("Error. ", e);
		} catch (Exception exc) {
			logger.error("Error al añadir la banda lateral al PDF " + exc.getMessage(), exc);
			throw new ISPACException(exc);
		}
	}

	protected Image createBgImage() throws ISPACException {

		// Ruta relativa de la imagen de fondo
		String imageFullPath = null;
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
			imageFullPath = fc.getBaseFilePath() + File.separator + imagePath;
			//FIN [dipucr-Felipe #507]

			if (logger.isInfoEnabled()) {
				logger.info("Imagen de fondo del PDF: " + imageFullPath);
			}
			// Construir la imagen de fondo
			Image image = Image.getInstance(imageFullPath);
			image.setAbsolutePosition(200F, 400F);
			return image;

		} catch (Exception e) {
			logger.error("Error al leer la imagen de fondo al sellar el documento: " + imageFullPath, e);
			throw new ISPACException("Error al leer la imagen de fondo al sellar el documento: " + imageFullPath, e);
		}
	}

	protected void getImagen(PdfContentByte pdfContentByte, float margen,
			float x, int numberOfPages, int pageActual, String tipoReg,
			String numReg, String fecReg, String departamento)
			throws ISPACException {

		try {
			String font = ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.FONT_BAND);
			String encoding = ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.ENCODING_BAND);
			float fontSize = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(DipucrSignConnector.FONTSIZE_BAND));

			BaseFont bf = BaseFont.createFont(font, encoding, false);
			pdfContentByte.beginText();
			pdfContentByte.setFontAndSize(bf, fontSize);

			BufferedReader br = new BufferedReader(new FileReader(this.getDataFile()));
			String sCadena = null;
			int i = 0;
			String scadenaReg = "";
			// Para que escriba en la tercera línea
			pdfContentByte.setTextMatrix(0.0F, 1.0F, -1F, 0.0F, x, margen);

			while ((sCadena = br.readLine()) != null) {
				logger.debug("Texto de la banda gris de registro: " + scadenaReg);
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

			br.close();

		} catch (Exception e) {
			logger.error("Error al componer la imagen de la banda lateral", e);
			throw new ISPACException("Error al componer la imagen de la banda lateral", e);
		}
	}

	protected File getDataFile() throws ISPACException {

		// Ruta relativa del texto de la banda lateral
		String dataPath = ISPACConfiguration.getInstance().getProperty(DEFAULT_PDF_BG_DATA_PATH);
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
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		String dataFullPath = "";
		String filePath = "config_" + entidad + File.separator + basename + ext;
		dataFullPath = ConfigurationHelper.getConfigFilePath(filePath);
		if (StringUtils.isBlank(dataFullPath)) {
			logger.debug("No existe el fichero de configuración: " + dataFullPath);
			filePath = "config_" + File.separator + basename + ext;
			dataFullPath = ConfigurationHelper.getConfigFilePath(filePath);
			if (StringUtils.isBlank(dataFullPath)) {
				logger.debug("No existe el fichero de configuración: " + filePath);
				filePath = basename + ext;
				dataFullPath = ConfigurationHelper.getConfigFilePath(filePath);
			} 				
		}
		
		if (StringUtils.isBlank(dataFullPath)) {
			// Ruta absoluta del texto de la banda lateral
			dataFullPath = ConfigurationHelper.getConfigFilePath(dataPath);
		}

		if (logger.isInfoEnabled()) {
			logger.info("Texto de la banda lateraldel PDF: " + dataFullPath);
		}
		return new File(dataFullPath);
	}

	public ActionForward muestraDoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		response.setContentType(MimetypeMapping.getMimeType("pdf"));
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + pathFileTempConcatenada + "\"");
		response.setContentLength((int) fileConcatenado.length());

		ServletOutputStream ouputStream = response.getOutputStream();
		FileUtils.copy(fileConcatenado, ouputStream);
		ouputStream.flush();
		ouputStream.close();

		return null;
	}
	
	
public DipucrDatosParticipante dameParticipante(IClientContext ctx, String numexp, String destinatario, String destinatarioId, Vector<String> dnisRepresentantesComparece, String descripcion)	throws RemoteException, ISPACRuleException{
		
		DipucrDatosParticipante participante = new DipucrDatosParticipante();

		try {
			
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			String sqlQuery = "";
			if (StringUtils.isEmpty(destinatarioId) || destinatarioId.equals("0")) {				
				destinatario = StringUtils.replace(destinatario, "'", "\\'");
				sqlQuery = ParticipantesUtil.NOMBRE + " = E'" + destinatario + "'";
			} else {
				sqlQuery = ParticipantesUtil.ID_EXT + " = '" + destinatarioId + "'";
			}

			logger.debug("sqlQueryPart " + sqlQuery);
			
			IItemCollection participantes = ParticipantesUtil.getParticipantes(ctx, numexp, sqlQuery, "");
			// Si es = a 0 quiere decir que esta en sus expedientes relacionados
			// entonces miro en cualquier otro expediente para sacar si dni
			if (participantes.toList().size() == 0) {

				participantes = ParticipantesUtil.getParticipantes(ctx, "", sqlQuery, ParticipantesUtil.ID + " DESC");
			}

			if (participantes.toList().size() != 0) {
				Iterator<?> itParticipantes = participantes.iterator();

				IItem partic = null;
				String dniDestinatario = "";
				
				while (itParticipantes.hasNext() && StringUtils.isEmpty(dniDestinatario)) {
					partic = (IItem) itParticipantes.next();

					if (partic.getString(ParticipantesUtil.NDOC) != null) {
						dniDestinatario = partic.getString(ParticipantesUtil.NDOC);
						logger.debug("Se encuntra NDOC, se sale con valor: " + dniDestinatario);
					}
				}
				logger.debug("Se tiene NDOC: " + dniDestinatario);
				
				participante.setNombreDestinatario( partic.getString("NOMBRE"));
				participante.setEmailDestinatario(partic.getString("DIRECCIONTELEMATICA"));
				participante.setTelefonoDestinatario(partic.getString("TFNO_MOVIL"));
				participante.setNif(partic.getString("NDOC"));
				participante.setDir3(partic.getString("DIR3"));				
				
			}

		} catch (ISPACException e) {
			logger.error("Error al recuperar participante" + e.getMessage(), e);
			throw new ISPACRuleException("Error  al recuperar participante" + e.getMessage(), e);
		}
		return participante;
	}

//public static void main(String args[]) {
//	
//	String name ="ÑÑññDCRP2022/23423: Solicitud de subvención nominativa del Ayuntamiento de Carrión de Calatrava, para Mejora instalaciones y vías municipales º*?¿¡|@"; //example
//	//String name ="Asunto de prueba Agustín  º @: / # , . . ñoñica. * ^<>ºª|?"; //example	
//	String normalized = Normalizer.normalize(name, Form.NFD);
//	System.out.println(normalized);
//	String result = normalized.replaceAll("[^ A-Za-z0-9/:]", "");
//	//String result = name.replaceAll("[^ a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ/:]", "");
//	System.out.println(result);
//
//}
	
	
	
}