package ieci.tdw.ispac.ispaclib.gendoc.converter;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;

/**
 * Clase que convierte documentos de un formato a otro.
 *
 */
public class DocumentConverter {

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(DocumentConverter.class);


	/**
	 * Extensiones que el openOffice no genera el Xcomponent pero con extensiones compatibles si
	 */
	private static final Properties extsToPdf= new Properties();

	static {
		initExtsToPdf();
	}

	private static void initExtsToPdf() {

	try {
		extsToPdf.load(DocumentConverter.class.getClassLoader().getResourceAsStream("ieci/tdw/ispac/ispaclib/gendoc/converter/ConversorToGeneratePdf.properties"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		logger.error("Error loading internal ieci/tdw/ispac/ispaclib/gendoc/converter/ConversorToGeneratePdf.properties", e);
	}
	}

	/**
	 * Convierte un documento a PDF.
	 * @param invesflowAPI API de invesFlow.
	 * @param documentId Identificador del documento.
	 * @return Ruta del fichero de salida.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static String convert2PDF(IInvesflowAPI invesflowAPI, int documentId) throws ISPACException {

		// Obtener la información del documento
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		IItem docItem = entitiesAPI.getDocument(documentId);
		if (docItem != null) {

			// Convertir el fichero
			return convert2PDF(invesflowAPI,
					docItem.getString("INFOPAG"),
					docItem.getString("EXTENSION"));

		} else {
			throw new ISPACException("No se ha encontrado el documento");
		}
	}


	/**
	 * Convierte documentos a PDF.
	 * @param invesflowAPI API de invesFlow.
	 * @param documentIds Identificadores de documentos.
	 * @return Rutas de los ficheros de salida.
	 * @throws ISPACException si ocurre algún error.
	 */

	public static String[] convert2PDF(IInvesflowAPI invesflowAPI, int[] documentIds) throws ISPACException {
		return convert2PDF(invesflowAPI, documentIds, false);
	}


	/**
	 * Convierte documentos a PDF.
	 * @param invesflowAPI API de invesFlow.
	 * @param documentIds Identificadores de documentos.
	 * @param sign  Indica si se trabaja con los documentos firmados o no
	 * @return Rutas de los ficheros de salida.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static String[] convert2PDF(IInvesflowAPI invesflowAPI, int[] documentIds , boolean sign) throws ISPACException {

		// Obtener la información de los documentos
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		String ids = "";
		String separator = "";
		for (int i = 0; i < documentIds.length; i++) {
			ids += separator + documentIds[i];
			separator = " , ";
		}

		Map docs = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, "WHERE ID IN ( " + ids + " )").toMap();

		String[] guids = new String[docs.size()];
		String[] exts = new String[docs.size()];

		String infoPag="INFOPAG";
		String extension="EXTENSION";
		if(sign){
			infoPag="INFOPAG_RDE";
			extension="EXTENSION_RDE";
		}
		for (int i = 0; i < documentIds.length; i++) {
			IItem doc = (IItem) docs.get(new Integer(documentIds[i]));
			guids[i] = doc.getString(infoPag);
			exts[i] = doc.getString(extension);
		}

		return convert2PDF(invesflowAPI, guids, exts);
	}
	
	/**
	 * [dipucr-Felipe #160]
	 * Convierte documentos a PDF.
	 * @param invesflowAPI API de invesFlow.
	 * @param documentIds Identificadores de documentos.
	 * @param sign [dipucr-Felipe #160] Elimino el parámetro
	 * @return Rutas de los ficheros de salida.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static String[] convert2PDFKeepSignedDocuments(IInvesflowAPI invesflowAPI, int[] documentIds) throws ISPACException {

		// Obtener la información de los documentos
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		String ids = "";
		String separator = "";
		for (int i = 0; i < documentIds.length; i++) {
			ids += separator + documentIds[i];
			separator = " , ";
		}

		Map docs = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, "WHERE ID IN ( " + ids + " )").toMap();

		String[] guids = new String[docs.size()];
		String[] exts = new String[docs.size()];

		//En vez de discenir por el parámetro de firma sign miro si tiene INFOPAG_RDE,
		//y en este caso saco el firmado, y si no, el no firmado
		for (int i = 0; i < documentIds.length; i++) {
			IItem doc = (IItem) docs.get(new Integer(documentIds[i]));
			
			String infopag = doc.getString("INFOPAG_RDE");
			String extension = doc.getString("EXTENSION_RDE");
			if (StringUtils.isEmpty(infopag)){
				infopag = doc.getString("INFOPAG");
				extension = doc.getString("EXTENSION");
			}
			
			guids[i] = infopag;
			exts[i] = extension;
		}

		return convert2PDF(invesflowAPI, guids, exts);
	}


	/**
	 * Convierte un documento a PDF.
	 * @param invesflowAPI API de invesFlow.
	 * @param guid GUID del documento en el gestor documental.
	 * @param extension Extensión del documento
	 * @return Ruta del fichero de salida.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static String convert2PDF(IInvesflowAPI invesflowAPI, String guid, String extension) throws ISPACException  {

		// Obtener el tipo MIME
		IGenDocAPI genDocAPI = invesflowAPI.getGenDocAPI();
		Object connectorSession = null;

		try {
			connectorSession = genDocAPI.createConnectorSession();

			return convert2PDF(connectorSession, genDocAPI, guid, extension);

		}
		catch (ISPACInfo e) {
			logger.error("Extension "+extension+" no permitida para convertir a pdf",e);
			throw new ISPACInfo(e,false);
		}
		catch (ISPACException e) {
			logger.error("Error al convertir el documento a PDF: guid=[" + guid + "], extension=[" + extension + "]", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error al convertir el documento a PDF: guid=[" + guid + "], extension=[" + extension + "]", e);
			throw new ISPACException(e);
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}
	}

	public static String[] convert2PDF(IInvesflowAPI invesflowAPI, String[] guids, String[] extensions) throws ISPACException {

		// Obtener el tipo MIME
		IGenDocAPI genDocAPI = invesflowAPI.getGenDocAPI();
		Object connectorSession = null;

		try {

			connectorSession = genDocAPI.createConnectorSession();

			String[] finalFilesPath = new String[guids.length];
			for (int i = 0; i < guids.length; i++) {
				finalFilesPath[i] = convert2PDF(connectorSession, genDocAPI, guids[i], extensions[i]);
			}

			return finalFilesPath;

		} catch (ISPACException e) {
			logger.error("Error al convertir el documento a PDF", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error al convertir el documento a PDF", e);
			throw new ISPACException(e);
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}
	}

	protected static String convert2PDF(Object connectorSession, IGenDocAPI genDocAPI, String guid, String extension) throws ISPACException {

		try {

			String mime = genDocAPI.getMimeType(connectorSession, guid);

			checkSourceMimeType(mime);


			FileTemporaryManager fileTmpMgr = FileTemporaryManager.getInstance();


			//Obtenemos la extesion a utilizar para convertir a pdf.
			if(extsToPdf.containsKey(extension)){
				extension=extsToPdf.getProperty(extension);
			}

			// Fichero origen
			File sourceFile = fileTmpMgr.newFile("." + extension);

			//Obtiene el fichero origen del gestor documental
			OutputStream out = new FileOutputStream(sourceFile);
			genDocAPI.getDocument(connectorSession, guid, out);
			out.close();

			// Comprobar si los formatos de origen y destino son iguales
			if (!"pdf".equalsIgnoreCase(extension)) {

				// Path del fichero origen
				String sourceFileURL = new StringBuffer().append("file:///").append(sourceFile.getPath()).toString();

				// Path del fichero destino
				File finalFile = fileTmpMgr.newFile(".pdf");
				//String finalFileURL = new StringBuffer().append("file:///").append(finalFile.getPath()).toString();
				String finalFileURL = finalFile.toURL().toString();

				// Llama a OpenOffice para convertir el fichero a PDF
				OpenOfficeHelper.getInstance().load_and_Convert(sourceFileURL, finalFileURL, extension, "pdf");

				// Eliminar el fichero temporal de origen
				sourceFile.delete();

				return finalFile.getAbsolutePath();

			} else {

				// Devolvemos el fichero pdf, pues no hace falta convertirlo
				return sourceFile.getAbsolutePath();

			}

		}


		catch (ISPACInfo e) {
			logger.error("Extension "+extension+" no permitida para convertir a pdf",e);
			throw new ISPACInfo(e, false);
		}
		catch (Exception e) {
			logger.error("Error al convertir el documento a PDF: guid=[" + guid + "], extension=[" + extension + "]", e);
			throw new ISPACException(e);
		}
	}

	protected static void checkSourceMimeType(String mimeType) throws ISPACInfo {
		if (StringUtils.isBlank(mimeType)
				|| !(mimeType.equalsIgnoreCase("application/msword")
						|| StringUtils.containsIgnoreCase(mimeType, "application/excel")
						
						//INICIO [eCenpri-Felipe #449]
						//Metemos todos los alias posibles del Excel. El último es el oficial
						|| StringUtils.containsIgnoreCase(mimeType, "application/x-excel")						 
						|| StringUtils.containsIgnoreCase(mimeType, "application/vndms-excel") 
						|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.ms-excel") 
						//FIN [eCenpri-Felipe #449]

						|| StringUtils.containsIgnoreCase(mimeType, "application/pdf")
						|| StringUtils.containsIgnoreCase(mimeType, "application/rtf")
						|| StringUtils.containsIgnoreCase(mimeType, "application/powerpoint")
						|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.text")
						|| mimeType.equalsIgnoreCase("application/vnd.oasis.opendocument.text-template") //odt sxw stw sdw  ott oth  odm
						|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.spreadsheet")
						|| mimeType.equalsIgnoreCase("application/vnd.oasis.opendocument.spreadsheet-template") //sxc stc sdc ods ots
						|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.chart") //*.odc
						|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.presentation")
						|| mimeType.equalsIgnoreCase("application/vnd.oasis.opendocument.presentation-template") //sxi sti sdd sdp odp otp
						|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.graphics") //*.odG
						|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.formula") //*.odf
						|| mimeType.equalsIgnoreCase("application/vnd.oasis.opendocument.image")
						|| mimeType.equalsIgnoreCase("application/vnd.oasis.opendocument.graphics-template")
						|| StringUtils.containsIgnoreCase(mimeType, "image/jpeg")
						|| StringUtils.containsIgnoreCase(mimeType, "image/pjpeg")
						|| StringUtils.containsIgnoreCase(mimeType, "image/gif")
						|| StringUtils.containsIgnoreCase(mimeType, "image/dib")
						|| StringUtils.containsIgnoreCase(mimeType, "image/x-xbitmap")
						|| StringUtils.containsIgnoreCase(mimeType, "image/tiff")
						|| StringUtils.containsIgnoreCase(mimeType, "image/x-tiff")
						|| StringUtils.containsIgnoreCase(mimeType, "image/tif")
						|| StringUtils.containsIgnoreCase(mimeType, "image/png")
						|| StringUtils.containsIgnoreCase(mimeType, "image/bmp")
						|| StringUtils.containsIgnoreCase(mimeType, "text/plain")
						|| StringUtils.containsIgnoreCase(mimeType, "text/xml")
						|| StringUtils.containsIgnoreCase(mimeType, "text/html")
						|| StringUtils.containsIgnoreCase(mimeType, "text/rtf")
						|| StringUtils.containsIgnoreCase(mimeType, "application/xml")
						|| StringUtils.containsIgnoreCase(mimeType, "application/x-msexcel")
						|| StringUtils.containsIgnoreCase(mimeType, "application/x-rtf")
						|| StringUtils.containsIgnoreCase(mimeType, "application/vndms-powerpoint")
						|| StringUtils.containsIgnoreCase(mimeType, "image/x-windows-bmp"))) {
			throw new ISPACInfo("El tipo del documento a convertir no es correcto '" + mimeType + "'", false);

		}
	}

	/**
	 * Concatena varios documentos en un único PDF.
	 * @param invesflowAPI API de invesFlow.
	 * @param docIds Identificadores de los documentos a concatenar.
	 * @return Ruta del fichero de salida.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static String concatenate2PDF(IInvesflowAPI invesflowAPI, int[] docIds) throws ISPACException{

		return concatenate2PDF(invesflowAPI, docIds, false, false);
	}
	
	/**
	 * [dipucr-Felipe #160]
	 * Concatena varios documentos en un único PDF.
	 * @param invesflowAPI API de invesFlow.
	 * @param docIds Identificadores de los documentos a concatenar.
	 * @param sign  Indica si se trabaja con los documentos firmados o no
	 * @return Ruta del fichero de salida.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static String concatenate2PDF(IInvesflowAPI invesflowAPI, int[] docIds, boolean sign) throws ISPACException{

		return concatenate2PDF(invesflowAPI, docIds, sign, false);
	}


	/**
	 * Concatena varios documentos en un único PDF.
	 * @param invesflowAPI API de invesFlow.
	 * @param docIds Identificadores de los documentos a concatenar.
	 * @param sign  Indica si se trabaja con los documentos firmados o no
	 * @param bKeepSignedDocs [dipucr-Felipe #160] Mantiene los documentos firmados, sin recuperar el documento anterior a la firma
	 * @return Ruta del fichero de salida.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static String concatenate2PDF(IInvesflowAPI invesflowAPI, int[] docIds , boolean sign , boolean bKeepSignedDocs) throws ISPACException{

		// Convertir los documentos a PDF
		String[] files;
		//INICIO [dipucr-Felipe #160]
		if (bKeepSignedDocs){
			files = convert2PDFKeepSignedDocuments(invesflowAPI, docIds);
		}
		else{//FIN [dipucr-Felipe #160]
			files= convert2PDF(invesflowAPI, docIds, sign);
		}

		if (ArrayUtils.isEmpty(files)){
			return null;
		}

		String targetFilePath = null;

		try {

			if (files.length == 1) {
				targetFilePath = files[0];
			} else {

				File targetFile = FileTemporaryManager.getInstance().newFile(".pdf");

				PdfCopyFields copy = new PdfCopyFields(new FileOutputStream(targetFile));
				for (int i = 0; i < files.length; i++) {
					copy.addDocument(new PdfReader(files[i]));
				}
				copy.close();

				targetFilePath = targetFile.getAbsolutePath();
			}

		} catch (IOException e) {
			logger.error("Error al concatenar documentos en PDF", e);
			throw new ISPACException(e);
		} catch (DocumentException e) {
			logger.error("Error al concatenar documentos en PDF", e);
			throw new ISPACException(e);
		}

		return targetFilePath;
	}


}
