package es.dipucr.sigem.sellar.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationHelper;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
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
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.comparece.CompareceConfiguration;
import es.dipucr.sigem.api.rule.common.utils.CompareceUtil;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.sgm.tram.sign.DipucrSignConnector;

public class SellarDocumentos{

	public static final Logger logger = Logger.getLogger("loggerRegistro");
	
	private IClientContext cct;

	// Id de los documentos seleccionados
	private String idDocSeleccionado = "";

	/**
	 * Ruta por defecto del fichero con el texto de la banda lateral del PDF.
	 * /home/sigem/SIGEM/conf/SIGEM_Tramitacion
	 */
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
		
		String tipoRegistro = "";
		String numRegistro = "";
		String sFechaRegistro = "";
		String departamento = "";
		String pathFileTemp = "";
		String destino = "";
		String descripcion = "";
		String numexp = "";
		
		File fileSello, documentoRegistrado = null;
		String rutaFileName = "";
		
		IInvesflowAPI invesFlowAPI = cct.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();		

		int idTipDoc = 0;
		int idPlantillaSellar = 0;
		int idPlantillaComparece = 0;
		ArrayList<File> listaDocumentosSellar = new ArrayList<File>();
		File fileComparece = null;
		Document documentComparece = null;
		PdfWriter pdfCopy = null;

		try {
			Font fuenteTitulo = new Font(Font.TIMES_ROMAN);
			fuenteTitulo.setStyle(Font.BOLD);
			fuenteTitulo.setSize(15);

			Font fuenteDocumento = new Font(Font.TIMES_ROMAN);
			fuenteDocumento.setStyle(Font.BOLD);
			
			idTipDoc = DocumentosUtil.getTipoDoc(cct, "Documentos Sellados", DocumentosUtil.BUSQUEDA_EXACTA, false);

			IItemCollection tipoDocSellarCollection = cct.getAPI().getEntitiesAPI().queryEntities("SPAC_P_PLANTDOC", "WHERE NOMBRE='Documentos Sellados' AND ID_TPDOC=" + idTipDoc + ";");
			Iterator<?> tipoDocSellarIterator = tipoDocSellarCollection.iterator();
			
			if (tipoDocSellarIterator.hasNext()) {
				idPlantillaSellar = ((IItem) tipoDocSellarIterator.next()).getInt("ID_TPDOC");
			}
			
			IItemCollection tipoDocCompareceCollection = cct.getAPI().getEntitiesAPI().queryEntities("SPAC_P_PLANTDOC", "WHERE NOMBRE='" + Constants.COMPARECE.PARTICIPANTES_COMPARECE + "' AND ID_TPDOC=" + idTipDoc + ";");
			Iterator<?> tipoDocCompareceIterator = tipoDocCompareceCollection.iterator();
			
			if (tipoDocCompareceIterator.hasNext()) {
				idPlantillaComparece = ((IItem) tipoDocCompareceIterator.next()).getInt("ID");
			}
		
			String consultaDocumentosRegistrados = "WHERE ID_TRAMITE=" + taskId + " AND (NREG IS NOT NULL AND NREG != '')";
			
			if (StringUtils.isNotEmpty(this.idDocSeleccionado)) {
				logger.debug("Se va a sellar o enviar a COMPARECE el documento: " + this.idDocSeleccionado);
				consultaDocumentosRegistrados += " AND ID = " + this.idDocSeleccionado;
			}
			consultaDocumentosRegistrados += " AND UPPER(EXTENSION_RDE) = 'PDF' ORDER BY DESCRIPCION;";
			logger.debug("Se van a enviar a COMPARECE o sellar los documentos: " + consultaDocumentosRegistrados);

			IItemCollection documentosRegistradosCollection = DocumentosUtil.queryDocumentos(cct, consultaDocumentosRegistrados);
			Iterator<?> documentosRegistradosIterator = documentosRegistradosCollection.iterator();

			pathFileTempConcatenada = FileTemporaryManager.getInstance().newFileName(".pdf");
			int contadorComparece = 0;
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
				
				//Se vuelve a comprobar si se han dado de alta en COMPARECE los que antes no estaban, si es así se envía por comparece ahora.
				compruebaEnvioComparece(cct, numexp, document);
			
				IItemCollection enviadosCompareceCollection = entitiesAPI.getEntities(Constants.TABLASBBDD.DPCR_ACUSES_COMPARECE, numexp, " IDENT_DOC = '" + idDoc + "'");
				Iterator<?> enviadosCompareceIterator = enviadosCompareceCollection.iterator();				
				if(enviadosCompareceIterator.hasNext()){
					contadorComparece++;
					if(fileComparece == null){
						//fileComparece = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/documentoComparece.pdf");
						fileComparece = new File(FileTemporaryManager.getInstance().getFileTemporaryPath()  + File.separator + FileTemporaryManager.getInstance().newFileName(".pdf"));						
						documentComparece = new Document();

						pdfCopy = PdfCopy.getInstance(documentComparece, new FileOutputStream(fileComparece));
						documentComparece.open();

						String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
						String imageLogoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_LOGO_PATH_DIPUCR);
						String imageFondoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_FONDO_PATH_DIPUCR);
						String imagePiePath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_PIE_PATH_DIPUCR);

						File logoURL = new File(imageLogoPath);
						if (logoURL != null) {
							Image logo = Image.getInstance(imageLogoPath);
							logo.scalePercent(50);
							documentComparece.add(logo);
						}
						File fondoURL = new File(imageFondoPath);
						if(fondoURL != null){
							Image fondo = Image.getInstance(imageFondoPath);
							fondo.setAbsolutePosition(250, 50);
							fondo.scalePercent(70);
							documentComparece.add(fondo);
							
						}

						File pieURL = new File(imagePiePath);
						if(pieURL != null){
							Image pie = Image.getInstance(imagePiePath);
							pie.setAbsolutePosition(documentComparece.getPageSize().getWidth() - 550, 15);
							pie.scalePercent(80);
							documentComparece.add(pie);
							
						}

						documentComparece.add(new Phrase("\n\n"));
						Paragraph titulo = new Paragraph(new Phrase("DOCUMENTOS ENVIADOS A COMPARECE", fuenteTitulo));
						titulo.setAlignment(Element.ALIGN_CENTER);
						documentComparece.add(titulo);
						documentComparece.add(new Phrase("\n"));
					}
					logger.debug("Documento enviado a COMPARECE: " + descripcion);
					
					logger.debug("Insertar Ayuntamiento en documento comparece. " + destino);

					documentComparece.add(new Phrase(contadorComparece + ".- Destino del registro: " + destino + " : "));
					documentComparece.add(new Phrase("\n"));					
					documentComparece.add(new Phrase("\t\tDocumento: " + descripcion + " : ", fuenteDocumento));
					documentComparece.add(new Phrase("\n"));
					documentComparece.add(new Phrase("\t\t\t Interesado o Representante - ID Notificación - Fecha de notificación"));
					documentComparece.add(new Phrase("\n"));
					int contadorRepresentantes = 0;
					while(enviadosCompareceIterator.hasNext()){	
						contadorRepresentantes++;
						//se ha enviado por comparece
						IItem enviadosCompareceItem = (IItem) enviadosCompareceIterator.next();
						String dniNotificado = enviadosCompareceItem.getString("DNI_NOTIFICADO");
						String idNotificacion = enviadosCompareceItem.getString("ID_NOTIFICACION");
						
						documentComparece.add(new Phrase("\t\t\t" + contadorRepresentantes + ".- " + dniNotificado + " - " + idNotificacion + " - " + sFechaRegistro + "."));
						documentComparece.add(new Phrase("\n"));
					}
					documentComparece.add(new Phrase("\n"));
				}
				else{					 
					addGrayBand(documentoRegistrado, pathFileTemp, infoPagRDE, tipoRegistro, numRegistro, sFechaRegistro, departamento);
					rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
					logger.debug("Documento sellado: '" + descripcion + "', ruta: " + rutaFileName);
					
					//[Manu Ticket #110] - INICIO - ALSIGM3 Cambiar registro de salida para que actualice el campo ESTADONOTIFICACION
					document.set("ESTADONOTIFICACION", "OK");
					document.store(cct);
		      		//[Manu Ticket #110] - FIN - ALSIGM3 Cambiar registro de salida para que actualice el campo ESTADONOTIFICACION

					fileSello = new File(rutaFileName);
					listaDocumentosSellar.add(fileSello);
				}
			}
			
			if (fileComparece != null && fileComparece.exists()) {
				documentComparece.close();
				pdfCopy.close();
				
				entityDocument = DocumentosUtil.generaYAnexaDocumento(cct, taskId, idTipDoc, Constants.COMPARECE.PARTICIPANTES_COMPARECE, fileComparece, Constants._EXTENSION_PDF);
				entityDocument.set("ID_PLANTILLA", idPlantillaComparece);
				
				entityDocument.store(cct);
				cct.endTX(true);
				fileComparece.delete();
			}
			
			// MQE ticket #325
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

			int idTipDocAux = DocumentosUtil.getTipoDoc(cct, Constants.COMPARECE.PARTICIPANTES_COMPARECE, DocumentosUtil.BUSQUEDA_EXACTA, false);
			if(idTipDocAux > Integer.MIN_VALUE){
				idTipDoc = idTipDocAux;
			}
		} catch (ISPACInfo e) {
			logger.error("Error al sellar el documento. " + e.getMessage(), e);
			e.setRefresh(false);
			throw new ISPACException("Error al sellar el documento. " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error al sellar el documento. " + e.getMessage(), e);
			throw new ISPACException("Error al sellar el documento."+ e.getMessage(), e);
			
		} finally {
			if(fileConcatenado != null && fileConcatenado.exists()) fileConcatenado.delete();
			if(listaDocumentosSellar != null && listaDocumentosSellar.size() > 0){
				for(int i = 0; i < listaDocumentosSellar.size(); i++){
					if(listaDocumentosSellar.get(i) != null && listaDocumentosSellar.get(i).exists()) listaDocumentosSellar.get(i).delete();
				}
			}
			if (fileComparece != null && fileComparece.exists()) {
				fileComparece.delete();
			}
			if(documentoRegistrado != null && documentoRegistrado.exists()) documentoRegistrado.delete();
			System.gc();
		}
	}
	
	private void compruebaEnvioComparece(IClientContext ctx, String numexp, IItem documento) throws ISPACException {
		try {
			//[Manu ] INICIO - Conector registro con conexión al comparece.
			
			if(CompareceConfiguration.getInstanceNoSingleton(EntidadesAdmUtil.obtenerEntidad(ctx)).getProperty(CompareceConfiguration.TIENE_COMPARECE).equals("S")){
				int idDoc = documento.getKeyInt();
				
				String descripcion = "";
				String asunto = "";
				String nombreDoc = documento.getString("NOMBRE");
				String destinatario = documento.getString("DESTINO");
				String destinatarioId = documento.getString("DESTINO_ID");
				
				String infoPag = documento.getString("INFOPAG_RDE");
				
				if(StringUtils.isEmpty(infoPag))
					infoPag = documento.getString("INFOPAG"); 
				
				String extension = documento.getString("EXTENSION_RDE");
			
				if(StringUtils.isEmpty(extension))
					extension = documento.getString("EXTENSION");
				
				byte[] documentContent;
				documentContent = IOUtils.toByteArray(new FileInputStream(DocumentosUtil.getFile(ctx, infoPag, nombreDoc, extension)));
		
				try{
					asunto = ExpedientesUtil.getAsunto(ctx, numexp);
				}
				catch(ISPACException e){
					logger.error(e.getMessage(), e);
					asunto = "";
				}
				
				descripcion = nombreDoc + "  -  Ref.Exp.: " + numexp + " - Asunto Exp.: " + asunto;
		   	    
				logger.debug("Usuario: " + destinatario + ", usuarioDestinoId: " + destinatarioId);
				logger.debug("Descripción: " + descripcion);
					
				Vector<String> dnisRepresentantesComparece = new Vector<String>();
				
				boolean usuComparece = false;
				
				if(StringUtils.isNotEmpty(destinatario)){
					
					IInvesflowAPI invesFlowAPI = ctx.getAPI();
					IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
					
					IItem documentoItem = DocumentosUtil.getDocumento(entitiesAPI, idDoc);
					if(documentoItem != null){
						if (documentoItem.getString("NUMEXP") != null)
							numexp = DipucrCommonFunctions.limpiarCaracteresEspeciales(documentoItem.getString("NUMEXP"));
						else
							numexp = "";
						try {
							usuComparece = CompareceUtil.esUsuarioComparece(ctx, numexp, destinatario, destinatarioId, dnisRepresentantesComparece, descripcion);
							
						} catch (RemoteException e) {
							logger.error("Error al conectar con COMPARECE al comprobar si es usuario comparece. " + e.getMessage(), e);
							throw new ISPACRuleException("Error al conectar con COMPARECE al comprobar si es usuario comparece. " + e.getMessage(), e);
						} catch (ISPACRuleException e) {
							logger.error("Error al comprobar si es usuario comparece. " + e.getMessage(), e);
							throw new ISPACRuleException("Error al comprobar si es usuario comparece. " + e.getMessage(), e);
						}
						
						if (usuComparece){
							CompareceUtil.envioDocComparece(ctx, numexp, idDoc, dnisRepresentantesComparece, destinatario, documentoItem, documentContent, extension);
						}
						logger.debug("¿El usuario: " + destinatario + " es usuario de COMPARECE?" + usuComparece);
					}
				}
			}
		} catch (ISPACException e1) {
			logger.error("Error al enviar el documento a comparece del expediente " + numexp + ". " + e1.getMessage(), e1);
			throw new ISPACException("Error al enviar el documento a comparece del expediente " + numexp + ". " + e1.getMessage(), e1);
		} catch (FileNotFoundException e1) {
			logger.error("Error al recuperar el contenido del documentos del expediente " + numexp + ". " + e1.getMessage(), e1);
			throw new ISPACException("Error al recuperar el contenido del documentos del expediente " + numexp + ". " + e1.getMessage(), e1);
		} catch (IOException e1) {
			logger.error("Error al recuperar el contenido del documentos del expediente " + numexp + ". " + e1.getMessage(), e1);
			throw new ISPACException("Error al recuperar el contenido del documentos del expediente " + numexp + ". " + e1.getMessage(), e1);
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
			int largo = (int) readerInicial.getPageSize(n).getHeight();

			bandSize = 15;

			Image imagen = this.createBgImage();
			Document document = new Document();
			String ruta = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
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
				getImagen(over, margen, margen, n, i, tipoRegistro,	numRegistro, fechaRegistro, departamento);

				document.newPage();
			}

			document.close();

			fileOut.close();
			writer.close();
			readerInicial.close();

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
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		String imagePath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.DEFAULT_PDF_BG_IMAGE_PATH);
		try {
			if (logger.isInfoEnabled()) {
				logger.info("Imagen de fondo del PDF: " + imagePath);
			}
			// Construir la imagen de fondo
			Image image = Image.getInstance(imagePath);
			image.setAbsolutePosition(200F, 400F);
			return image;

		} catch (Exception e) {
			logger.error("Error al leer la imagen de fondo del PDF firmado: " + imagePath, e);
			throw new ISPACException("Error al leer la imagen de fondo del PDF firmado: " + imagePath, e);
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
}