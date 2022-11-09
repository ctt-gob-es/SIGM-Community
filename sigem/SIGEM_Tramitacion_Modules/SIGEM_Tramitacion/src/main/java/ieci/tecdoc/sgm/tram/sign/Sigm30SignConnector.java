package ieci.tecdoc.sgm.tram.sign;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.common.constants.EntityLockStates;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.messages.MessagesFormatter;
import ieci.tdw.ispac.ispaclib.sign.SignDetailEntry;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.core.base64.Base64Util;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.cripto.firma.ServicioFirmaDigital;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.gestioncsv.CodigosAplicacionesConstants;
import ieci.tecdoc.sgm.core.services.gestioncsv.InfoDocumentoCSV;
import ieci.tecdoc.sgm.core.services.gestioncsv.InfoDocumentoCSVForm;
import ieci.tecdoc.sgm.core.services.gestioncsv.ServicioGestionCSV;
import ieci.tecdoc.sgm.core.services.tiempos.ServicioTiempos;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignature;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;

public class Sigm30SignConnector extends Sigem23SignConnector implements ISigemSignConnector {

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(Sigm30SignConnector.class);

	/**
	 * Tipo mime para el CSV
	 */
	protected static final String INFODOCUMENTO_CSV_TIPO_MIME = "application/pdf";

	/**
	 * Constructor.
	 *
	 */
	public Sigm30SignConnector() {
		super();
	}
	
	/**
	 * Obtiene el documento , pdf si ya se hubiese firmado alguna vez o el original en otro caso.
	 * Añade los datos del firmante y genera o modifica  el pdf.
	 * @param changeState
	 * @throws ISPACException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void generatePdfToSign(boolean changeState) throws ISPACException{

		String infoPag = signDocument.getItemDoc().getString("INFOPAG");
		String antInfoPagRDE = signDocument.getItemDoc().getString("INFOPAG_RDE");

		IInvesflowAPI invesflowAPI = clientContext.getAPI();

		// Obtener el documento original convertido a PDF
		File originalPDFFile = getFile(infoPag);

		// CSV para el nuevo documento firmado
		String newCodCotejo = null;

		try {

			ServicioFirmaDigital firmaDigital = LocalizadorServicios.getServicioFirmaDigital();
			String pathFileTemp = FileTemporaryManager.getInstance().put(originalPDFFile.getAbsolutePath(), ".pdf");
			
			//INICIO [dipucr-Felipe #1413] Consolidamos los campos de formulario, si los tuviera
			File fileTempInicial = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp);
			File fileOriginalSoloTexto = PdfUtil.convertirPdfSoloTexto(fileTempInicial);
			pathFileTemp = fileOriginalSoloTexto.getName();
			//FIN [dipucr-Felipe #1413]

			// Fecha de la firma del PDF
			ServicioTiempos servicioTiempos = LocalizadorServicios.getServicioTiempos();
			Date date = servicioTiempos.getCurrentDate();

			// Código de cotejo
			String codCotejo = signDocument.getItemDoc().getString("COD_COTEJO");
			if (StringUtils.isBlank(codCotejo)) {
				newCodCotejo = addCodVerificacion(signDocument, date, clientContext);
				codCotejo = newCodCotejo;
			}

			// Lista de firmantes
			List signerList = getSignerList(signDocument);

			//[dipucr-Felipe #1246] Comprobamos si el fichero original tenía anexos antes de incrustrar la banda gris
			Map<String, byte[]> mapAnexos  = PdfUtil.extraerAnexos(originalPDFFile);
			
			// Añadir la banda gris lateral al documento PDF
			//[dipucr-Felipe #1377] Demasiados firmantes en banda gris
			String rutaCompletaTemp = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFileTemp;
			final int MAX_GRAYBAND_SIGNERS = Integer.valueOf(ISPACConfiguration.getInstance().getProperty(MAX_BAND_SIGNERS));
			if (signerList.size() > MAX_GRAYBAND_SIGNERS){
				addAnexoFirmantes(rutaCompletaTemp, signerList);
				File fileTemp = new File(rutaCompletaTemp);
				addGrayBandAnexoFirmantes(fileTemp, date, signDocument, codCotejo, signerList);
			}
			else{
//				addGrayBand(originalPDFFile, pathFileTemp, date, signDocument, codCotejo, signerList);
				addGrayBand(fileOriginalSoloTexto, pathFileTemp, date, signDocument, codCotejo, signerList);
			}
			
			//INICIO [dipucr-Felipe #1246] Volvemos a anexar los ficheros
			for (Map.Entry<String, byte[]> anexoEntry : mapAnexos.entrySet()) {
			    String filename = anexoEntry.getKey();
			    File fileAnexo = FileTemporaryManager.getInstance().newFile("." + FilenameUtils.getExtension(filename));
			    FileUtils.writeByteArrayToFile(fileAnexo, anexoEntry.getValue());
			    
			    PdfUtil.anexarDocumento(new File(rutaCompletaTemp), fileAnexo, filename);
			}
			//FIN [dipucr-Felipe #1246]

			// Obtenemos la información del expediente
			IItem expediente = invesflowAPI.getEntitiesAPI().getExpedient(signDocument.getNumExp());

			/* INICIO - INCRUSTAR FIRMA EN PDF */
			PdfReader reader = new PdfReader(rutaCompletaTemp);
			FileOutputStream fout = new FileOutputStream(originalPDFFile.getAbsolutePath());
			PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');

			PdfSignatureAppearance sap = stp.getSignatureAppearance();

			sap.setLayer2Font(new Font(Font.HELVETICA, 8, Font.BOLDITALIC, new Color(0, 0, 0)));
			sap.setLayer4Text(" ");

			Calendar signDate = Calendar.getInstance();
			signDate.setTime(date);
		    sap.setSignDate(signDate);

		    sap.setReason(expediente.getString("ASUNTO"));
		    sap.setLocation("Es");
		    // INICIO [dipucr-Felipe #1246]
		    ieci.tecdoc.sgm.core.services.entidades.Entidad entidad = EntidadesAdmUtil.obtenerEntidadObject(clientContext);
		    String provider = entidad.getNombreLargo() + " (sello.dipucr.es)";
		    sap.setProvider(provider);
		    // FIN [dipucr-Felipe #1246]

		    PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKMS, PdfName.ADBE_PKCS7_DETACHED);

			if (sap.getReason() != null) {
				dic.setReason(sap.getReason());
			}
			if (sap.getLocation() != null) {
				dic.setLocation(sap.getLocation());
			}
			//[dipucr-Felipe #1246]
			if (sap.getProvider() != null) {
				dic.setName(sap.getProvider());
			}

			dic.setDate(new PdfDate(sap.getSignDate()));
			sap.setCryptoDictionary(dic);

			HashMap exc = new HashMap();
			exc.put(PdfName.CONTENTS, new Integer(SIGN_SIZE * 2 + 2));

			sap.preClose(exc);

			InputStream inp = sap.getRangeStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte buf2[] = new byte[8192];
			int n;
			while((n = inp.read(buf2)) > 0) {
				out.write(buf2, 0, n);
			}

			byte[] firma = Base64Util.decode(firmaDigital.firmar(out.toByteArray()));

			byte[] outc = new byte[SIGN_SIZE];
			System.arraycopy(firma, 0, outc, 0, firma.length);
			PdfDictionary dic2 = new PdfDictionary();
			dic2.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
			sap.close(dic2);

			/* FIN - INCRUSTAR FIRMA EN PDF */
			
			if (StringUtils.isBlank(antInfoPagRDE)) {
				addNewSign(originalPDFFile, changeState);
			} else {
				addSign(originalPDFFile, changeState);
			}
			
			//[dipucr-Felipe #1246] Bloquear decretos
			if (StringUtils.isNotEmpty(signDocument.getNumDecreto())){
				IItem itemDoc = signDocument.getItemDoc();
				itemDoc.set("BLOQUEO", EntityLockStates.TOTAL_LOCK);
				itemDoc.store(clientContext);
			}
			
			doAfterSign();
			
		} catch (Exception e) {

			// Si se produce algún error
			// anular el CSV si ya fue generado
			anularCodVerificacion(newCodCotejo);

			doErrorSign();

			logger.error("Error en la firma del documento", e);
			throw new ISPACException(e);
		}
	}


	/**
	 * Generar el CSV y
	 * actualizar la referencia en el campo COD_COTEJO en la tabla de documentos.
	 *
	 * @param signDocument Documento a firmar.
	 * @param signDate Fecha de la firma.
	 * @param clientContext Contexto de tramitación.
	 * @return Código seguro de verificación.
	 * @throws ISPACException Si se produce algún error, en caso afirmativo, se anula el CSV si fue generado.
	 */
	public synchronized String addCodVerificacion(SignDocument signDocument, Date signDate, IClientContext clientContext) throws ISPACException {

		InfoDocumentoCSV infoDocumento = null;

		try {
			ServicioGestionCSV servicioGestionCSV = LocalizadorServicios.getServicioGestionCSV();

			// Información del documento
			IItem itemDoc = signDocument.getItemDoc();

			// Identificador de la entidad
			String entityId = getEntityId();

			// Generación del CSV para el documento
			// haciendo uso del servicio de gestión de CSV de generación y consulta
			Entidad entidad = new Entidad();
			entidad.setIdentificador(entityId);
			InfoDocumentoCSVForm infoDocumentoForm = new InfoDocumentoCSVForm();
			infoDocumentoForm
					.setCodigoAplicacion(CodigosAplicacionesConstants.TRAMITACION_EXPEDIENTES_CODE);
			infoDocumentoForm.setDisponible(true);
			// La fecha de caducidad es null porque nunca caduca
			infoDocumentoForm.setFechaCaducidad(null);
			// Fecha del documento (la de la firma)
			infoDocumentoForm.setFechaCreacion(signDate);
			// Nombre del documento más extensión
			// y tipo Mime asociado
			infoDocumentoForm.setNombre(itemDoc.getString("NOMBRE") + ".pdf");
			infoDocumentoForm.setTipoMime(INFODOCUMENTO_CSV_TIPO_MIME);

			// Generar el CSV invocando al Servicio de Gestión de CSV
			infoDocumento = servicioGestionCSV.generarCSV(entidad,
					infoDocumentoForm);

			if (logger.isDebugEnabled()) {
				logger.debug("SigemSignConnector.addCodVerificacion: Generado el CSV: [" + infoDocumento.getCsv()
						+ "] para el documento: [" + infoDocumento.getId() + "].");
			}

			// Establecer el código de cotejo electrónico a partir del CSV
			itemDoc.set("COD_COTEJO", infoDocumento.getCsv());

			//Bloqueamos el documento para la edicion
			// itemDoc.set("BLOQUEO", DocumentLockStates.EDIT_LOCK);
			itemDoc.store(clientContext);

			return infoDocumento.getCsv();

		} catch (Exception e) {

			// Si se produce algún error
			// anular el CSV si ya fue generado
			if (infoDocumento != null) {
				anularCodVerificacion(infoDocumento.getCsv());
			}

			logger.error("Error en la firma del documento al generar el CSV", e);
			throw new ISPACException(e);
		}
	}

	/**
	 * Anula el código de verificación generado para el documento.
	 *
	 * @param codCotejo
	 */
	protected void anularCodVerificacion(String codCotejo) {

		if (codCotejo != null) {

			// Eliminar el CSV
			try {
				Entidad entidad = new Entidad();
				entidad.setIdentificador(getEntityId());

				ServicioGestionCSV servicioGestionCSV = LocalizadorServicios.getServicioGestionCSV();
				InfoDocumentoCSV infoDocumentoCSV = servicioGestionCSV.getInfoDocumentoByCSV(entidad, codCotejo);
				if (infoDocumentoCSV != null) {

					// Eliminar la información de CSV del documento
					servicioGestionCSV.deleteInfoDocumento(entidad, infoDocumentoCSV.getId());
				}
			} catch (Exception e) {
				logger.error("Error en la firma al eliminar el CSV", e);
			}
		}
	}

	/**
	 * [dipucr-Felipe #1377]
	 * @param rutaCompletaTemp
	 * @param signerList
	 * @throws ISPACException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addAnexoFirmantes(String rutaCompletaTemp, List signerList) throws ISPACException {

		try{
			File fileAnexo = generarAnexoFirmantes(signerList);
			File fileTemp = new File(rutaCompletaTemp);
			
			ArrayList<File> listFicheros = new ArrayList<File>();
			listFicheros.add(fileTemp);
			listFicheros.add(fileAnexo);
			
			File fileConcatenado = PdfUtil.concatenarArchivos(listFicheros);
			FileUtils.copyFile(fileConcatenado, fileTemp);
		}
		catch(Exception ex){
			String error = "Error al añadir el anexo de firmantes al justificante";
			logger.error(error, ex);
			throw new ISPACException(error);
		}
		
	}
	
	/**
	 * [dipucr-Felipe #1377] 
	 */
	private static final String IMG_LOGO_CABECERA = "logoCabecera.gif";
	private static final String IMG_PIE = "pie.jpg";
	private static final Font FUENTE_TITULO = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
	private static final Font FUENTE_TEXTO = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL);
	private static final Font FUENTE_FIRMANTES = FontFactory.getFont(FontFactory.COURIER, 10, Font.NORMAL);
	
	/**
	 * [dipucr-Felipe #1377]
	 * @param cct
	 * @param signerList
	 * @return
	 * @throws ISPACException 
	 */
	public File generarAnexoFirmantes(List<SignDetailEntry> signerList) throws ISPACException{
		
		File fileAnexoFirmantes = null;
		String idEntidad = EntidadesAdmUtil.obtenerEntidad(clientContext);
		int idDoc = signDocument.getItemDoc().getKeyInt();
		
		String dir = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + idEntidad + File.separator + "img_exp_fol" + File.separator, "/SIGEM_TramitacionWeb");
		String urlImgLogoCabecera = dir + IMG_LOGO_CABECERA;
		String urlImgPie = dir + IMG_PIE;
		
		try{
			fileAnexoFirmantes = new File(FileTemporaryManager.getInstance().getFileTemporaryPath(), idEntidad + "_" + idDoc);
			
			FileOutputStream fos = new FileOutputStream(fileAnexoFirmantes);
			
			com.lowagie.text.Document documento = new com.lowagie.text.Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(documento, fos);
			documento.open();
			
			Image imagen = null;
			
			// Añadimos el escudo de la entidad
			try {
				imagen = Image.getInstance(urlImgLogoCabecera);
				imagen.setAbsolutePosition(50, documento.getPageSize().getHeight() - 100);
				
				float ancho = documento.getPageSize().getWidth() - documento.leftMargin() - documento.rightMargin();
				float alto = 50;
				
				imagen.scaleToFit(ancho, alto);
				documento.add(imagen);
			} catch (Exception e) {
				logger.error( "ERROR no se ha encontrado la imagen de logo de la entidad: " + urlImgLogoCabecera + ". " + e.getMessage(), e);
				throw new ISPACException( "ERROR no se ha encontrado la imagen de logo de la entidad: " + urlImgLogoCabecera + ". " + e.getMessage(), e);
			}

			// Añadimos el pie de página de la diputación
			try {
				imagen = Image.getInstance(urlImgPie);
				imagen.setAbsolutePosition( documento.getPageSize().getWidth() - 550, 15);
				imagen.scalePercent(80);
				documento.add(imagen);
			} catch (Exception e) {
				logger.error( "ERROR no se ha encontrado la imagen de pie de página: " + urlImgPie + ". " + e.getMessage(), e);
				throw new ISPACException( "ERROR no se ha encontrado la imagen de pie de página: " + urlImgPie + ". " + e.getMessage(), e);
			}
									
			documento.add(new Paragraph(new Phrase("\n\n\n\n")));
			
			Paragraph parrafoInicial = new Paragraph();
			parrafoInicial.add(new Phrase("INFORMACIÓN DE FIRMANTES\n", FUENTE_TITULO));
			parrafoInicial.add(new Phrase("Se detallan a continuación los firmantes del documento: ", FUENTE_TEXTO));
			documento.add(parrafoInicial);
			
			Paragraph parrafoFirmas = new Paragraph();
			parrafoFirmas.add(new Phrase("\n", FUENTE_FIRMANTES));
			
			//Ordenamos la lista de firmantes por fecha (interfaz Comparator)
			Collections.sort(signerList);
			
			for (int signerCont = 0; signerCont < signerList.size(); signerCont++) {
				Object signer = signerList.get(signerCont);
				parrafoFirmas.add(new Phrase(getSignLine(signer) + "\n", FUENTE_FIRMANTES));
			}
			documento.add(parrafoFirmas);
						
			documento.close();
		}
		catch(Exception ex){
			String error = "Error al generar el anexo con la información de firmantes del documento " + idDoc;
			logger.error(error, ex);
			throw new ISPACException(error, ex);
		}
		return fileAnexoFirmantes;
	}
	
	/**
	 * [dipucr-Felipe #1377]
	 * @param file
	 * @param pathFileTemp
	 * @param signDate
	 * @param signDocument
	 * @param codCotejo
	 * @param signerList
	 * @throws ISPACException 
	 */
	@SuppressWarnings("rawtypes")
	protected void addGrayBandAnexoFirmantes(File file, Date signDate, SignDocument signDocument, String codCotejo, List signerList) throws ISPACException{
		
		try{
			String pathFicheroFirmaOrigen = FileTemporaryManager.getInstance().put(file.getAbsolutePath(), ".pdf");
			String pathFicheroFirmaDestino = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + pathFicheroFirmaOrigen;
			PdfReader reader = new PdfReader(pathFicheroFirmaDestino);
			FileOutputStream fout = new FileOutputStream(file.getAbsolutePath());
			PdfStamper stp = new PdfStamper(reader, fout);
			
			PdfContentByte pdfContentByte = null;
			
			String dateFirma = FechasUtil.getFormattedDate(signDate);
				
			float margen = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(MARGIN_BAND));
			int num_paginas = reader.getNumberOfPages();	
			
			//Obtiene la imagen gris .gif que se utiliza en la banda
			Image imagen_gris = createBgImage();
			
			for(int i=1; i<=num_paginas; i++){	
			
				int largo = (int) reader.getPageSize(i).getHeight();
//				int ancho = (int) reader.getPageSize(i).getWidth();
				
				//Añadimos el rectángulo gris del fondo de la imagen
				imagen_gris.scaleAbsoluteWidth(largo);
				imagen_gris.scaleAbsoluteHeight(30);
				imagen_gris.setAbsolutePosition(0, 0);
				imagen_gris.setRotationDegrees(90F);
	
				pdfContentByte = stp.getOverContent(i);
				pdfContentByte.addImage(imagen_gris);
				//Fin de añadir el rectángulo gris del fondo de la imagen
				
				//Añadimos el texto de la izquierda con la información de la firma 
				getImagenAnexoFirmantes(dateFirma, pdfContentByte, margen, true, margen, num_paginas, i, codCotejo);
			}					
			stp.close();
		}
		catch(Exception ex){
			logger.error("Error al agregar la banda lateral", ex);
			throw new ISPACException(ex);
		}
	}
	
	/**
	 * [dipucr-Felipe #1377]
	 */
	protected void getImagenAnexoFirmantes(String dateFirma, PdfContentByte pdfContentByte, float margen, boolean vh, float x, 
			int numberOfPages, int pageActual, String codCotejo) throws ISPACException {
		
		try {
		
			String font = ISPACConfiguration.getInstance().getProperty(FONT_BAND);
			String encoding = ISPACConfiguration.getInstance().getProperty(ENCODING_BAND);
			float fontSize = Float.parseFloat(ISPACConfiguration.getInstance().getProperty(FONTSIZE_BAND));
			
			BaseFont bf = BaseFont.createFont(font, encoding, false);
			pdfContentByte.beginText();
			pdfContentByte.setFontAndSize(bf, fontSize);
			
			FirmaConfiguration fc = FirmaConfiguration.getInstance(clientContext);
			
			String sDescPagina = "última ";
			String sPagina = "";
			
			String texto = fc.get("firmar.grayband.text");
			texto = MessagesFormatter.format(texto, new String[] {
				sDescPagina,
				sPagina,
				String.valueOf(numberOfPages), 
				String.valueOf(pageActual), 
				String.valueOf(numberOfPages),
				codCotejo });

			fontSize += 3F;
			String[] arrTexto = texto.split("\n");
			for (String linea : arrTexto){
				if (vh){
					pdfContentByte.setTextMatrix(0.0F, 1.0F, -1F, 0.0F, x, margen);
					pdfContentByte.showText(linea);
					x += fontSize;
				}
				else{
	                pdfContentByte.setTextMatrix(margen, x);
					pdfContentByte.showText(linea);
	                x -= fontSize;
				}
			}
			pdfContentByte.endText();
			
		} catch (Exception e) {
			logger.error("Error al componer la imagen de la banda lateral", e);
			throw new ISPACException(e);
		}
	}
	
}
