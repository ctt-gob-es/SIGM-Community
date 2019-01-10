package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

import com.lowagie.text.Anchor;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;
import es.dipucr.sigem.api.rule.common.expFol.DocIncluir;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FileUtils;

public class DipucrGenerarExpedienteFoliadoConIndiceRule implements IRule {

	private static final Logger LOGGER = Logger.getLogger(DipucrGenerarExpedienteFoliadoConIndiceRule.class);

	public OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
	public String entityId = info.getOrganizationId();

	public String dir = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entityId + File.separator + "img_exp_fol"+ File.separator, "/SIGEM_TramitacionWeb");
	// public String dir =
	// "/sigem/app/SIGEM/conf/SIGEM_TramitacionWeb/skinEntidad_" + entityId +
	// "/img_exp_fol/";

	public final String IMG_CONTRAPORTADA = dir + "contraPortada.png";
	public final String IMG_FONDO = dir + "fondo.png";
	public final String IMG_LOGO_CABECERA = dir + "logoCabecera.gif";
	public final String IMG_LOGO_PORTADA = dir + "logoPortada.png";
	public final String IMG_PIE = dir + "pie.jpg";
	public final String IMG_PORTADA = dir + "portada.png";

	public static final Properties EXTS_TO_PDF = new Properties();
	public static final int DOCUMENTOS_POR_PAGINA = 25;

	public ArrayList<String[]> camposIndice = new ArrayList<String[]>();
	public int numPagsIndice = 0;
	public int numPagsIndiceReales = 0;

	public String numExpPadre = "";

	public File errores = null;
	public FileWriter erroresFW = null;
	public boolean hayErrores = false;

	public boolean añadePortada = true;
	public boolean añadeIndice = true;
	public boolean añadeContraportada = true;

	public ArrayList<HashMap<String, Object>> marcas = new ArrayList<HashMap<String, Object>>();
	public List<HashMap<String, Object>> newBookmarks = new ArrayList<HashMap<String, Object>>();

	public String tipoDoc = "%EXPEDIENTE FOLIADO CON ÍNDICE%";
	public String nombreDoc = "";

	public Color colorAsunto = null;
	public Color bColorAsunto = null;

	public Color colorNumPag = null;
	public Color bColorNumPag = null;

	@SuppressWarnings("rawtypes")
	public String getNumExpFoliar(IRuleContext rulectx, IEntitiesAPI entitiesAPI) {
		String resultado = "";
		try {
			IItemCollection expedienteDestino = entitiesAPI.getEntities( "DIPUCR_EXPEDIENTE_FOLIADO", rulectx.getNumExp());
			Iterator itExpedienteDestino = expedienteDestino.iterator();

			if (itExpedienteDestino.hasNext()){
				resultado = ((IItem) itExpedienteDestino.next()).getString( "EXPEDIENTEDESTINO").trim();
			}
		} catch (ISPACException e) {
			LOGGER.error("Error al recuperar el expediente a foliar.", e);
		}

		return resultado;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		String numexp = "";
		try {
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------

			LOGGER.info("Inicio " + this.getClass().getName());

			numexp = rulectx.getNumExp();
			numExpPadre = getNumExpFoliar(rulectx, entitiesAPI);
			
			LOGGER.warn("Se inicia foliado en el expediente -> "+ numexp + "con expediente padre -> " + numExpPadre);

			ArrayList<DocIncluir> docsDelExp = new ArrayList<DocIncluir>();

			ArrayList<String> expedientes = ExpedientesRelacionadosUtil.getProcedimientosRelacionadosHijos(numExpPadre, entitiesAPI);

			if (expedientes != null && expedientes.size() > 0) {
				FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();
				File documentoResumen = ftMgr.newFile(".pdf");

				errores = ftMgr.newFile(".txt");
				erroresFW = new FileWriter(errores);

				erroresFW.write("\t\tDocumentos el expediente '" + numExpPadre + "' que no se han incluido en el expediente foliado:\n\n");

				IItem expedienteOriginal = ExpedientesUtil.getExpediente(cct, numExpPadre);

				String consultaDocumentos = getConsultaDocumentos(expedientes, rulectx, expedienteOriginal);

				LOGGER.info("MQE consulta documentos: " + consultaDocumentos);

				IItemCollection documentsCollection = DocumentosUtil.queryDocumentos(cct, consultaDocumentos);
				docsDelExp = getDocsDelExp(documentsCollection);

				String varColorAsunto = DipucrCommonFunctions.getVarGlobal("COLOR_ASUNTO_EXPEDIENTE_FOLIADO");
				
				if (StringUtils.isEmpty(varColorAsunto)) {
					LOGGER.error("Error al obtener el color del texto del asunto de la portada del expediente " + numexp + ". Revise el valor de la varibale de sistema: COLOR_ASUNTO_EXPEDIENTE_FOLIADO");
					throw new ISPACRuleException( "Error al obtener el color del texto del asunto de la portada del expediente " + numexp + ". Avise al administrador del sistema");
				} else {
					String[] varColorAsuntoSplit = varColorAsunto.split(",");
					try {
						colorAsunto = new Color(
								Integer.parseInt(varColorAsuntoSplit[0]),
								Integer.parseInt(varColorAsuntoSplit[1]),
								Integer.parseInt(varColorAsuntoSplit[2]),
								Integer.parseInt(varColorAsuntoSplit[3]));
						bColorAsunto = new Color(
								Integer.parseInt(varColorAsuntoSplit[0]),
								Integer.parseInt(varColorAsuntoSplit[1]),
								Integer.parseInt(varColorAsuntoSplit[2]),
								Integer.parseInt(varColorAsuntoSplit[3]));
						
					} catch (Exception e) {
						LOGGER.error("Error al obtener el color del texto del asunto de la portada del expediente " + numexp + ". Revise el valor de la varibale de sistema: COLOR_ASUNTO_EXPEDIENTE_FOLIADO", e);
						throw new ISPACRuleException( "Error al obtener el color del texto del asunto de la portada del expediente " + numexp + ". Avise al administrador del sistema", e);
					}
				}

				String varColorNumPag = DipucrCommonFunctions.getVarGlobal("COLOR_NUM_PAG_INDICE_FOLIADO");
				if (StringUtils.isEmpty(varColorNumPag)) {
					LOGGER.error("Error al obtener el color del texto del asunto de la portada del expediente " + numexp + ". Revise el valor de la varibale de sistema: COLOR_ASUNTO_EXPEDIENTE_FOLIADO");
					throw new ISPACRuleException( "Error al obtener el color del texto del asunto de la portada del expediente " + numexp + ". Avise al administrador del sistema");
				} else {
					String[] varColorNumPagSplit = varColorNumPag.split(",");
					try {
						colorNumPag = new Color(
								Integer.parseInt(varColorNumPagSplit[0]),
								Integer.parseInt(varColorNumPagSplit[1]),
								Integer.parseInt(varColorNumPagSplit[2]),
								Integer.parseInt(varColorNumPagSplit[3]));
						bColorNumPag = new Color(
								Integer.parseInt(varColorNumPagSplit[0]),
								Integer.parseInt(varColorNumPagSplit[1]),
								Integer.parseInt(varColorNumPagSplit[2]),
								Integer.parseInt(varColorNumPagSplit[3]));
					} catch (Exception e) {
						LOGGER.error("Error al obtener el color del texto del asunto de la portada del expediente " + numexp + ". Revise el valor de la varibale de sistema: COLOR_ASUNTO_EXPEDIENTE_FOLIADO", e);
						throw new ISPACRuleException( "Error al obtener el color del texto del asunto de la portada del expediente " + numexp + ". Avise al administrador del sistema", e);
					}
				}

				documentoResumen = generarPdf(docsDelExp, documentoResumen.getPath(), entitiesAPI, cct, rulectx);
				
				erroresFW.flush();
				erroresFW.close();

				/**
				 * [Teresa] Para que cambie el tipo de documento y lo coja de
				 * datos específicos.
				 **/
				String plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
				int idTypeDocument = 0;
				if (StringUtils.isNotEmpty(plantilla)) {
					String tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
					idTypeDocument = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_LIKE, true);
				} else {
					idTypeDocument = DocumentosUtil.getTipoDoc(cct, tipoDoc, DocumentosUtil.BUSQUEDA_LIKE, true);
				}

				if (idTypeDocument == Integer.MIN_VALUE) {
					throw new ISPACInfo( "Error al obtener el tipo de documento '" + tipoDoc + "' del expediente " + numexp);
				}

				String sName = "";
				
				if (StringUtils.isNotEmpty(nombreDoc)) {
					sName = nombreDoc;
				} else {
					sName = expedienteOriginal.getString("ASUNTO");
				}

				DocumentosUtil.generaYAnexaDocumento(rulectx, idTypeDocument, sName, documentoResumen, "pdf");

				documentoResumen.delete();

				if (hayErrores) {
					DocumentosUtil.generaYAnexaDocumento(rulectx, idTypeDocument, "Documentos no incluidos", errores, "txt");
				}
				if (errores != null && errores.exists()){
					errores.delete();
				}
			}
			LOGGER.info("FIN " + this.getClass().getName());

		} catch (ISPACException e) {
			LOGGER.error("ERROR generando expediente foliado: " + numExpPadre, e);
			throw new ISPACRuleException("ERROR generando expediente foliado: " + numExpPadre, e);
		} catch (FileNotFoundException e) {
			LOGGER.error("ERROR generando expediente foliado: " + numExpPadre, e);
			throw new ISPACRuleException("ERROR generando expediente foliado: " + numExpPadre, e);
		} catch (IOException e) {
			LOGGER.error("ERROR generando expediente foliado: " + numExpPadre, e);
			throw new ISPACRuleException("ERROR generando expediente foliado: " + numExpPadre, e);
		}
		return "";
	}

	public void extraeDocs(File archivo, String extension, String nombreDoc, String descripcion, Date fechaDoc, Date fechaAprobacion, String idPlantilla, int idTipDoc, ArrayList<DocIncluir> resultado, IClientContext cct, String infoPag) throws Exception {

		DocIncluir docAIncluir = null;
		File getFile = null;

		// Hay que comprobar si el contenido es un archivo o un fichero
		// Si es archivo hay que ver si se trata de zip, rar o ya es un fichero
		// final

		if ("ZIP".equalsIgnoreCase(extension)) {
			try {
				descripcion += ".zip - " + nombreDoc;

				descripcion = descripcion.replace("(", "");
				descripcion = descripcion.replace(")", "");
				descripcion = descripcion.replace(".", "-");
				descripcion = descripcion.trim();

				ZipFile zipArchivo = new ZipFile(archivo);
				Enumeration<? extends ZipEntry> entradas = zipArchivo.entries();

				entradas = zipArchivo.entries();
				ZipEntry entrada;
				
				while (entradas.hasMoreElements()) {
					entrada = entradas.nextElement();

					if (!entrada.isDirectory()) {
						nombreDoc = StringUtils.substring( entrada.getName(), 0, StringUtils.lastIndexOf( entrada.getName(), '.'));

						nombreDoc = nombreDoc.replace("(", "");
						nombreDoc = nombreDoc.replace(")", "");
						nombreDoc = nombreDoc.trim();

						extension = FileUtils.getExtensionByNombreDoc(entrada.getName());
						if(StringUtils.isNotEmpty(extension)){
							extension = extension.replace("(", "");
							extension = extension.replace(")", "");
							extension = extension.trim();
						}

						InputStream in = zipArchivo.getInputStream(entrada);
						getFile = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + FileTemporaryManager.getInstance().newFileName("." + extension));

						FileOutputStream fos = new FileOutputStream(getFile);
						int leido;
						byte[] buffer = new byte[1024];
						
						if (in != null && fos != null) {
							while (0 < (leido = in.read(buffer))) {
								fos.write(buffer, 0, leido);
							}
							
							fos.flush();
							fos.close();
							in.close();

							extraeDocs(getFile, extension, nombreDoc, descripcion, fechaDoc, fechaAprobacion, idPlantilla, idTipDoc, resultado, cct, infoPag);
						}

					}
				}
				zipArchivo.close();
				if (archivo != null && archivo.exists()){
					archivo.delete();
				}
				
			} catch (ZipException e) {
				LOGGER.error( "Error al extraer los documentos del archivo ZIP: " + nombreDoc, e);
				erroresFW.write("\t - Error al extraer los documentos del archivo ZIP: " + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\n");
				hayErrores = true;
			} catch (IOException e) {
				LOGGER.error( "Error al extraer los documentos del archivo ZIP: " + nombreDoc, e);
				erroresFW.write("\t - Error al extraer los documentos del archivo ZIP: " + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\n");
				hayErrores = true;
			} catch (Exception e) {
				LOGGER.error( "Error al extraer los documentos del archivo ZIP: " + nombreDoc, e);
				erroresFW.write("\t - Error al extraer los documentos del archivo ZIP: " + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\n");
				hayErrores = true;
			}
		} else if ("RAR".equalsIgnoreCase(extension)) {
			try {
				descripcion += ".rar - " + nombreDoc;

				descripcion = descripcion.replace("(", "");
				descripcion = descripcion.replace(")", "");
				descripcion = descripcion.replace(".", "-");
				descripcion = descripcion.trim();

				Archive a = new Archive(archivo);
				List<FileHeader> headers = a.getFileHeaders();

				Iterator<FileHeader> fileHeaderIterator = headers.iterator();

				while (fileHeaderIterator.hasNext()) {
					FileHeader entrada = fileHeaderIterator.next();

					if (!entrada.isDirectory()) {
						getFile = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + entrada.getFileNameString());
						int cont = 0;
						
						while (getFile.exists()) {
							getFile = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + cont + entrada.getFileNameString());
							cont++;
						}
						FileOutputStream fos = new FileOutputStream(getFile);

						nombreDoc = StringUtils.substring( entrada.getFileNameString(), 0, StringUtils.lastIndexOf( entrada.getFileNameString(), '.'));

						nombreDoc = nombreDoc.replace("(", "");
						nombreDoc = nombreDoc.replace(")", "");
						nombreDoc = nombreDoc.trim();

						a.extractFile(entrada, fos);

						fos.flush();
						fos.close();

						extension = FileUtils.getExtensionByNombreDoc(entrada.getFileNameString());
						if(StringUtils.isNotEmpty(extension)){
							extension = extension.replace("(", "");
							extension = extension.replace(")", "");
							extension = extension.trim();
						}

						extraeDocs(getFile, extension, nombreDoc, descripcion, fechaDoc, fechaAprobacion, idPlantilla, idTipDoc, resultado, cct, infoPag);
					}
				}
				a.close();
				if (archivo != null && archivo.exists()){
					archivo.delete();
				}
			} catch (RarException e) {
				LOGGER.error("Error al contar los documentos del archivo RAR: " + nombreDoc, e);
				erroresFW.write("\t - Error al extraer los documentos del archivo RAR: " + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\n");
				hayErrores = true;
				
			} catch (IOException e) {
				LOGGER.error("Error al contar los documentos del archivo RAR: " + nombreDoc, e);
				erroresFW.write("\t - Error al extraer los documentos del archivo RAR: " + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\n");
				hayErrores = true;
				
			} catch (Exception e) {
				LOGGER.error("Error al contar los documentos del archivo RAR: " + nombreDoc, e);
				erroresFW.write("\t - Error al extraer los documentos del archivo RAR: " + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\n");
				hayErrores = true;
			}
		} else if (!"PDF".equalsIgnoreCase(extension)) {

			String docFilePath = "";

			docFilePath = convert2PDF(cct, archivo, extension);
			
			if (StringUtils.isNotEmpty(docFilePath)) {
				getFile = new File(docFilePath);

				nombreDoc = StringUtils.substring(nombreDoc, StringUtils.lastIndexOf(nombreDoc, '/') + 1);
				nombreDoc = StringUtils.substring(nombreDoc, StringUtils.lastIndexOf(nombreDoc, '\\') + 1);
				
				nombreDoc = nombreDoc.replace("(", "");
				nombreDoc = nombreDoc.replace(")", "");
				nombreDoc = nombreDoc.replace(".", "-");
				nombreDoc = nombreDoc.trim();

				docAIncluir = new DocIncluir(getFile, nombreDoc, extension,	descripcion, fechaDoc, fechaAprobacion, archivo, idPlantilla, idTipDoc);

				resultado.add(docAIncluir);
			}
		} else {
			// Si es PDF lo incluimos directamente
			nombreDoc = StringUtils.substring(nombreDoc, StringUtils.lastIndexOf(nombreDoc, '/') + 1);
			nombreDoc = StringUtils.substring(nombreDoc, StringUtils.lastIndexOf(nombreDoc, '\\') + 1);

			nombreDoc = nombreDoc.replace("(", "");
			nombreDoc = nombreDoc.replace(")", "");
			nombreDoc = nombreDoc.replace(".", "-");
			nombreDoc = nombreDoc.trim();

			docAIncluir = new DocIncluir(archivo, nombreDoc, extension, descripcion, fechaDoc, fechaAprobacion, archivo, idPlantilla, idTipDoc);

			resultado.add(docAIncluir);
		}
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<DocIncluir> getDocsDelExp( IItemCollection documentsCollection) throws ISPACException, IOException {

		ArrayList<DocIncluir> resultado = new ArrayList<DocIncluir>();

		if (documentsCollection != null && documentsCollection.next()) {
			// Creamos el nuevo documento
			Iterator documentos = documentsCollection.iterator();

			while (documentos.hasNext()) {
				IItem documento = (IItem) documentos.next();
				String infoPagRDE = documento.getString("INFOPAG_RDE");
				String extension = documento.getString("EXTENSION_RDE");
				
				if (StringUtils.isEmpty(infoPagRDE)) {
					infoPagRDE = documento.getString("INFOPAG");
					extension = documento.getString("EXTENSION");
				}
				
				String numExp = documento.getString("NUMEXP");
				String nombreDocumento = documento.getString("NOMBRE");
				String descripcionDocumento = documento.getString("DESCRIPCION");
				Date fechaDocumento = documento.getDate("FFIRMA");

				Date fechaAprobacion = documento.getDate("FAPROBACION");
				if (fechaAprobacion == null){
					fechaAprobacion = fechaDocumento;
				}

				String tipoRegistro = documento.getString("TP_REG");
				String nreg = documento.getString("NREG");
				String freg = documento.getString("FREG");
				String idPlantilla = documento.getString("ID_PLANTILLA");
				int idTipDoc = documento.getInt("ID_TPDOC");
				
				if ("NINGUNO".equalsIgnoreCase(tipoRegistro)){
					tipoRegistro = "";
				}
				
				if (nreg == null) {
					nreg = "";
					freg = "";
				}
				
				if (infoPagRDE != null) {
					DocIncluir doc = new DocIncluir(infoPagRDE, numExp, tipoRegistro, nreg, freg, normalizar(nombreDocumento), extension, descripcionDocumento, fechaDocumento,	fechaAprobacion, idPlantilla, idTipDoc);
					resultado.add(doc);
				} else {
					LOGGER.error("Error no existe el documento '" + nombreDocumento + "' del expediente: '" + numExp + "'");
					erroresFW.write("\t - Error no existe el documento '" + nombreDocumento + "'\n\t\t'" + descripcionDocumento + "." + extension.trim() + "'\n\t\tExpediente: '" + numExp + "'\n\n");
					hayErrores = true;
				}
			}
		}
		return resultado;
	}

	@SuppressWarnings("rawtypes")
	public String getConsultaDocumentos(ArrayList<String> expedientes, IRuleContext rulectx, IItem expedienteOriginal) throws ISPACException {

		IClientContext cct = rulectx.getClientContext();
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();

		String nombre = "";
		StringBuilder nombreSinDni = new StringBuilder("");
		String[] identidad = null;
		String nif = "";

		if (expedienteOriginal.getString("IDENTIDADTITULAR") != null){
			nombre = expedienteOriginal.getString("IDENTIDADTITULAR");
		}
		if (expedienteOriginal.getString("NIFCIFTITULAR") != null){
			nif = expedienteOriginal.getString("NIFCIFTITULAR");
		}

		if (StringUtils.isEmpty(nif)) {
			if (StringUtils.isNotEmpty(nombre)) {
				identidad = nombre.split(" ");
				nombreSinDni.append(identidad[1]);

				for (int i = 2; i < identidad.length; i++) {
					nombreSinDni.append(" " + identidad[i]);
				}
			}
		} else {
			nombreSinDni = new StringBuilder(nombre);
		}

		String consultaDocumentos = "";
		String consultaDecre = "";
		String consultaSecre = "";
		String consultaBOP = "";
		String consultaTablon = "";
		String consultaElse = "";

		// Comprobamos si es de secretaría, si lo es, buscamos la propuesta
		String expedientePropuesta = getExpedientePropuesta(expedientes, entitiesAPI, rulectx);

		// Recorremos todos los expedientes en función del expediente u
		// obtenemos todos los firmados y las entradas
		// o únicamente los certificados y las notificaciones
		for (int i = 0; i < expedientes.size(); i++) {
			IItem itemProc = ExpedientesUtil.getExpediente( rulectx.getClientContext(), expedientes.get(i));
			
			if (itemProc != null) {
				int idProcedimiento = itemProc.getInt("ID_PCD");
				// Comprobamos si el expediente es de los que no hay que sacar
				// documentos

				// No se obtienen documentos de Propuestas y Comisiones
				if (esProcTipo(entitiesAPI, idProcedimiento, "DECRETO")) {
					String id = "";
					String consultaDecretos = "";
					String orden = "";
					if (StringUtils.isNotEmpty(nombreSinDni.toString())) {
						// Es de tipo decreto recuperamos solo las
						// notificaciones y los acuses
						consultaDecretos = " (UPPER(DESCRIPCION) LIKE UPPER('NOTIFICA%"
								+ nombreSinDni.toString()
								+ "%') OR (UPPER(DESCRIPCION) LIKE UPPER('NOTIFICA%"
								+ nombreSinDni.toString()
								+ "%') AND (UPPER(EXTENSION) ='PDF' OR UPPER(EXTENSION_RDE) ='PDF'))) ";

						consultaDecretos += " OR (UPPER(DESCRIPCION) LIKE UPPER('ACUSE%"
								+ nombreSinDni.toString()
								+ "%') AND (UPPER(EXTENSION) ='PDF' OR UPPER(EXTENSION_RDE) ='PDF')) ";
						consultaDecretos += " OR (ESTADOFIRMA IN ('02','03') AND UPPER(DESCRIPCION) LIKE UPPER('%DECRETO%'))";
						consultaDecretos += " OR (TP_REG = 'ENTRADA' AND (UPPER(EXTENSION) = 'PDF' OR UPPER(EXTENSION_RDE) ='PDF'))";

						orden = "CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION DESC";
					} else {// si no tiene interesado principal toma el propio
							// decreto
						consultaDecretos = "UPPER(NOMBRE) = 'DECRETO'";
						orden = "ID DESC LIMIT 1";
					}

					IItemCollection icDoc = DocumentosUtil.getDocumentos(cct, expedientes.get(i), consultaDecretos, orden);
					Iterator docNotificaciones;
					
					if (icDoc.next()) {
						docNotificaciones = icDoc.iterator();
						
						List<String> elementos = new ArrayList<String>();
						while (docNotificaciones.hasNext()) {
							elementos.add(((IItem) docNotificaciones.next()).getString("ID"));
						}
						
						if(null != elementos && !elementos.isEmpty()){
							id += StringUtils.join(elementos, ",");
						}
					}

					if (StringUtils.isNotEmpty(id)) {
						if (StringUtils.isNotEmpty(consultaDecre)){
							consultaDecre += " OR (ID IN (" + id + "))";
						} else {
							consultaDecre = " (ID IN (" + id + "))";
						}
					}
				} else if (esProcTipo(entitiesAPI, idProcedimiento, "SECRETAR") && !expedientes.get(i).equals(expedientePropuesta)) {
					
					String id = "";
					IItemCollection icDoc = DocumentosUtil.getDocumentos(cct, expedientes.get(i), "DESCRIPCION LIKE '%" + expedientePropuesta + "%'", "");
					Iterator docNotificaciones;
					
					if (icDoc.next()) {
						String tipoPropuesta = "";
						String numeroPropuesta = "";
						String esUrgencia = "";
						String esUrgencia2 = "";

						docNotificaciones = icDoc.iterator();
						
						if (docNotificaciones.hasNext()) {
							String descripcion = ((IItem) docNotificaciones.next()).getString("DESCRIPCION");
							String[] vectorDescripcion = descripcion.split(" . ");
							
							if (vectorDescripcion.length > 1) {
								tipoPropuesta = vectorDescripcion[0];
								numeroPropuesta = vectorDescripcion[1];
							}
							if ("PROPUESTA URGENCIA".equalsIgnoreCase(tipoPropuesta)) {
								esUrgencia = ".- Urgencia Certificado";
								esUrgencia2 = "Urgencia";
							} else{
								esUrgencia = ".-Certificado";
							}

							String consulta = "((UPPER(DESCRIPCION) LIKE UPPER('" + numeroPropuesta + esUrgencia + "%')";

							if (StringUtils.isNotEmpty(nombre)){
								consulta += "OR UPPER(DESCRIPCION) LIKE UPPER('"
										+ numeroPropuesta
										+ "%"
										+ esUrgencia2
										+ "%" + nombre + "%')";
							}

							consulta += ") AND ESTADOFIRMA IN ('02','03')) OR (TP_REG = 'ENTRADA' AND (UPPER(EXTENSION) = 'PDF' OR UPPER(EXTENSION_RDE) ='PDF'))";
							IItemCollection icDoc2 = DocumentosUtil.getDocumentos( cct, expedientes.get(i), consulta, " CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION DESC");
							Iterator it2 = icDoc2.iterator();
							
							List<String> elementos = new ArrayList<String>();
							while (it2.hasNext()) {
								elementos.add(((IItem) it2.next()).getString("ID"));
							}
							
							if(null != elementos && !elementos.isEmpty()){
								id += StringUtils.join(elementos, ",");
							}
						}

					}
					if (StringUtils.isNotEmpty(id)) {
						if (StringUtils.isNotEmpty(consultaSecre)){
							consultaSecre += " OR (ID IN (" + id + "))";
						} else {
							consultaSecre += " (ID IN (" + id + "))";
						}
					}
				} else if (esProcTipo(entitiesAPI, idProcedimiento, "BOP")) {
					String idPropuestaAnuncio = "";
					String idAnuncioPublicado = "";

					IItemCollection solBopCollection = entitiesAPI.getEntities( "BOP_SOLICITUD", expedientes.get(i));
					Iterator solBopIterator = solBopCollection.iterator();
					
					if (solBopIterator.hasNext()) {
						IItem solBop = (IItem) solBopIterator.next();
						Date fechaPublicacion = solBop.getDate("FECHA_PUBLICACION");
						String numAnuncio = solBop.getString("NUM_ANUNCIO_BOP");

						if (fechaPublicacion != null) {
							IItemCollection publicacionBOPCollection = entitiesAPI.queryEntities("BOP_PUBLICACION", " WHERE FECHA = '" + fechaPublicacion + "'");
							Iterator publicacionBOPIterator = publicacionBOPCollection.iterator();
							
							if (publicacionBOPIterator.hasNext()) {
								IItem publicacionBOP = (IItem) publicacionBOPIterator.next();
								String numexpPublicacion = publicacionBOP.getString("NUMEXP");

								IItemCollection documentosPublicacionBOPCollection = DocumentosUtil.getDocumentos(cct, numexpPublicacion, " DESCRIPCION LIKE '" + numAnuncio + " -%'", "");
								Iterator documentosPublicacionBOPIterator = documentosPublicacionBOPCollection.iterator();
								
								if (documentosPublicacionBOPIterator.hasNext()) {
									IItem documentosPublicacion = (IItem) documentosPublicacionBOPIterator.next();
									idAnuncioPublicado = documentosPublicacion.getString("ID");
								}
							}
						}
					}
					if (StringUtils.isEmpty(idAnuncioPublicado)) {
						IItemCollection docPropAnuncioCollection = DocumentosUtil.getDocumentos( cct, expedientes.get(i), "", " CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION ASC");
						Iterator docPropAnuncioIterator = docPropAnuncioCollection .iterator();
						if (docPropAnuncioIterator.hasNext()) {
							IItem docPropAnuncio = (IItem) docPropAnuncioIterator.next();
							idPropuestaAnuncio = docPropAnuncio.getString("ID");
						}
					}

					if (StringUtils.isNotEmpty(idAnuncioPublicado)) {
						if (StringUtils.isNotEmpty(consultaBOP)){
							consultaBOP += " OR (ID IN (" + idAnuncioPublicado + "))";
						} else {
							consultaBOP += " (ID IN (" + idAnuncioPublicado + "))";
						}
					} else if (StringUtils.isNotEmpty(idPropuestaAnuncio)) {
						if (StringUtils.isNotEmpty(consultaBOP)){
							consultaBOP += " OR (ID IN (" + idPropuestaAnuncio + "))";
						} else{
							consultaBOP += " (ID IN (" + idPropuestaAnuncio + "))";
						}
					}

				} else if (esProcTipo(entitiesAPI, idProcedimiento, "ETABLÓN")) {
					String idDocTablon = "";

					IItemCollection docTablonCollection = DocumentosUtil.getDocumentos( cct, expedientes.get(i), " UPPER(NOMBRE) LIKE 'ETABLON - DILIGENCIA%'", "");
					Iterator docTabloniIterator = docTablonCollection.iterator();
					
					if (docTabloniIterator.hasNext()) {
						IItem docTablon = (IItem) docTabloniIterator.next();
						idDocTablon = docTablon.getString("ID");
					}
					
					if (StringUtils.isNotEmpty(idDocTablon)) {
						if (StringUtils.isNotEmpty(consultaTablon)){
							consultaTablon += " OR (ID IN (" + idDocTablon + "))";
						} else{
							consultaTablon += " (ID IN (" + idDocTablon + "))";
						}
					}
				} else {
					String idFoliado = "";
					IItemCollection docFoliadoCollection = DocumentosUtil.getDocumentos( cct, expedientes.get(i), " UPPER(NOMBRE) LIKE 'EXPEDIENTE FOLIADO%' AND UPPER(DESCRIPCION) NOT LIKE 'DOCUMENTOS NO INCLUIDOS'", "");
					Iterator docFoliadoiIterator = docFoliadoCollection.iterator();
					
					if (docFoliadoiIterator.hasNext()) {
						IItem docFoliado = (IItem) docFoliadoiIterator.next();
						idFoliado = docFoliado.getString("ID");
					}

					if (StringUtils.isNotEmpty(idFoliado)) {
						if (StringUtils.isNotEmpty(consultaElse)){
							consultaElse += " OR (ID IN (" + idFoliado + "))";
						} else {
							consultaElse += " (ID IN (" + idFoliado + "))";
						}
					}
				}
			}
		}
		// Finalmente tomamos todos los documentos del expediente en el que nos
		// encontramos
		String idDocsExpOriginal = "";
		IItemCollection docsExpCollection = DocumentosUtil.getDocumentos(cct, expedienteOriginal.getString("NUMEXP"));
		Iterator docsExpIterator = docsExpCollection.iterator();
		
		while (docsExpIterator.hasNext()) {
			IItem docExp = (IItem) docsExpIterator.next();
			if (!"DOCUMENTACIÓN DE PROPUESTA".equalsIgnoreCase(docExp.getString("NOMBRE")) && !"Propuesta Anuncio".equalsIgnoreCase(docExp.getString("NOMBRE")) && !StringUtils.containsIgnoreCase(docExp.getString("NOMBRE"), "EXPEDIENTE FOLIADO")) {
				if (StringUtils.isEmpty(nif)) {
					if (!StringUtils.containsIgnoreCase(docExp.getString("NOMBRE"), "NOTIFICACI") && !StringUtils.containsIgnoreCase(docExp.getString("NOMBRE"), "ACUSE")){
						if (StringUtils.isEmpty(idDocsExpOriginal)){
							idDocsExpOriginal = docExp.getString("ID");
						} else {
							idDocsExpOriginal += ", " + docExp.getString("ID");
						}
					}
				} else {
					if (StringUtils.isEmpty(idDocsExpOriginal)){
						idDocsExpOriginal = docExp.getString("ID");
					} else {
						idDocsExpOriginal += ", " + docExp.getString("ID");
					}
				}
			}
		}
		if (StringUtils.isNotEmpty(idDocsExpOriginal)) {
			consultaDocumentos += " (ID IN (" + idDocsExpOriginal + "))";
		}

		if (StringUtils.isNotEmpty(consultaDecre)){
			consultaDocumentos += " OR " + consultaDecre;
		}

		if (StringUtils.isNotEmpty(consultaSecre)){
			consultaDocumentos += " OR " + consultaSecre;
		}

		if (StringUtils.isNotEmpty(consultaBOP)){
			consultaDocumentos += " OR " + consultaBOP;
		}

		if (StringUtils.isNotEmpty(consultaTablon)){
			consultaDocumentos += " OR " + consultaTablon;
		}

		if (StringUtils.isNotEmpty(consultaElse)){
			consultaDocumentos += " OR " + consultaElse;
		}

		if (StringUtils.isNotEmpty(consultaDocumentos)) {
			consultaDocumentos += " ORDER BY CASE WHEN FAPROBACION IS NULL THEN FFIRMA ELSE FAPROBACION END, DESCRIPCION ASC";

			consultaDocumentos = " WHERE " + consultaDocumentos;
		} else{
			consultaDocumentos = " WHERE 1=2 ";
		}

		return consultaDocumentos;
	}

	public String getExpedientePropuesta(ArrayList<String> expedientes, IEntitiesAPI entitiesAPI, IRuleContext rulectx) throws ISPACException {
		
		String resultado = "";
		String expedientePropuesta = "";
		
		for (int i = 0; i < expedientes.size(); i++) {
			expedientePropuesta = expedientes.get(i);
			IItem itemProc = ExpedientesUtil.getExpediente( rulectx.getClientContext(), expedientes.get(i));
			
			if (itemProc != null) {
				String nombreProcedimiento = itemProc.getString("NOMBREPROCEDIMIENTO");
				
				if (nombreProcedimiento.toUpperCase().indexOf("PROPUESTA") > -1) {
					resultado = expedientePropuesta;
				}
			}
		}
		return resultado;
	}

	public boolean esProcTipo(IEntitiesAPI entitiesAPI, int idProcedimiento, String tipo) {

		boolean resultado = false;
		
		try {
			IItem catalogo = entitiesAPI.getEntity( SpacEntities.SPAC_CT_PROCEDIMIENTOS, idProcedimiento);

			int idPadre = catalogo.getInt("ID_PADRE");
			
			if (idPadre != -1) {
				resultado = esProcTipo(entitiesAPI, idPadre, tipo);
			} else {
				String nomPcd = "";
				
				if (catalogo.getString("NOMBRE") != null) {
					nomPcd = catalogo.getString("NOMBRE");
					
					if (nomPcd.toUpperCase().indexOf(tipo.toUpperCase()) >= 0) {
						resultado = true;
					}
				}
			}
		} catch (ISPACException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return resultado;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public File generarPdf(ArrayList<DocIncluir> docsDelExp, String filePathNotificaciones, IEntitiesAPI entitiesAPI, ClientContext cct, IRuleContext rulectx) {

		File resultado = null;
		PdfReader reader = null;

		File resultadoDoc = null;
		FileOutputStream resultadoDocFO = null;
		Document documentDoc = null;
		PdfWriter writerDoc = null;

		String infoPag = "";
		String numexp = "";
		String nombreDoc = "";
		String extension = "";
		String descripcion = "";
		Date fechaDoc = null;
		Date fechaAprobacion = null;
		File archivoOriginal = null;
		ArrayList<DocIncluir> docsResultado = null;
		String idPlantilla = "";
		int idTipDoc = 0;

		try {
			resultado = new File(filePathNotificaciones);

			ArrayList<Integer> numPagsDocs = new ArrayList<Integer>();

			String filePathNotificacionesDoc = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + FileTemporaryManager.getInstance().newFileName(".pdf");

			boolean primero = true;// Indica si es el primer fichero (sobre el
									// que se concatenarán el resto de ficheros)

			int nPagActual = 0;
			int contadorDocumentos = 0;

			int numeroTotalDeDoc = cuentaDocs(cct, docsDelExp);

			if (docsDelExp.size() != 0) {

				Iterator<DocIncluir> docsDelExpIterator = docsDelExp.iterator();
				while (docsDelExpIterator.hasNext()) {

					DocIncluir documento1 = docsDelExpIterator.next();

					infoPag = documento1.infoPagRDE;
					numexp = documento1.numExp;
					nombreDoc = documento1.nombreDoc;

					nombreDoc = nombreDoc.replace("(", "");
					nombreDoc = nombreDoc.replace(")", "");
					nombreDoc = nombreDoc.replace(".", "-");
					nombreDoc = nombreDoc.trim();

					extension = documento1.extension;

					extension = extension.replace("(", "");
					extension = extension.replace(")", "");
					extension = extension.trim();

					descripcion = documento1.descripcion;

					descripcion = descripcion.replace("(", "");
					descripcion = descripcion.replace(")", "");
					descripcion = descripcion.replace(".", "-");
					descripcion = descripcion.trim();

					fechaDoc = documento1.fechaDoc;
					fechaAprobacion = documento1.fechaAprobacion;
					idPlantilla = documento1.idPlantilla;
					idTipDoc = documento1.idTipDoc;

					docsResultado = new ArrayList<DocIncluir>();

					try {
						archivoOriginal = DocumentosUtil.getFile(cct, infoPag, null, extension.trim());
					} catch (ISPACException e) {
						if (archivoOriginal != null && archivoOriginal.exists()) {
							archivoOriginal.delete();
							archivoOriginal = null;
						}
						LOGGER.error("Error al recuperar el documento '" + nombreDoc + "' del expediente: '" + numexp + "'", e);
						erroresFW.write("\t - Error al recuperar el documento '" + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\t\tExpediente: '" + numexp + "'\n\n");
						hayErrores = true;
						
					} catch (Exception e) {
						if (archivoOriginal != null && archivoOriginal.exists()) {
							archivoOriginal.delete();
							archivoOriginal = null;
						}
						
						LOGGER.error("Error al recuperar el documento '" + nombreDoc + "' del expediente: '" + numexp + "'", e);
						erroresFW.write("\t - Error al recuperar el documento '" + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\t\tExpediente: '" + numexp + "'\n\n");
						hayErrores = true;
					}

					try {
						if (archivoOriginal != null && archivoOriginal.exists()){
							extraeDocs(archivoOriginal, extension, nombreDoc, descripcion, fechaDoc, fechaAprobacion, idPlantilla, idTipDoc, docsResultado, cct, infoPag);
						}
					} catch (ISPACException e) {
						if (archivoOriginal != null && archivoOriginal.exists()) {
							archivoOriginal.delete();
							archivoOriginal = null;
						}
						
						LOGGER.error("Error al extraer el documento '" + nombreDoc + "' del expediente: '" + numexp + "'", e);
						erroresFW.write("\t - Error al extraer el documento '" + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\t\tExpediente: '" + numexp + "'\n\n");
						hayErrores = true;
					} catch (Exception e) {
						if (archivoOriginal != null && archivoOriginal.exists()) {
							archivoOriginal.delete();
							archivoOriginal = null;
						}
						
						LOGGER.error("Error al extraer el documento '" + nombreDoc + "' del expediente: '" + numexp + "'", e);
						erroresFW.write("\t - Error al extraer el documento '" + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\t\tExpediente: '" + numexp + "'\n\n");
						hayErrores = true;
					}

					if (docsResultado != null && !docsResultado.isEmpty()) {
						Iterator docsResultadoIterator = docsResultado.iterator();
						
						while (docsResultadoIterator.hasNext()) {
							DocIncluir documento = (DocIncluir) docsResultadoIterator.next();

							File archivo = documento.docPdf;
							nombreDoc = documento.nombreDoc;

							nombreDoc = nombreDoc.replace("(", "");
							nombreDoc = nombreDoc.replace(")", "");
							nombreDoc = nombreDoc.trim();

							extension = documento.extension;

							extension = extension.replace("(", "");
							extension = extension.replace(")", "");
							extension = extension.trim();

							descripcion = documento.descripcion;

							descripcion = descripcion.replace("(", "");
							descripcion = descripcion.replace(")", "");
							descripcion = descripcion.replace(".", "-");
							descripcion = descripcion.trim();

							File archivoSinConvertir = documento.docOriginal;

							FileInputStream file = new FileInputStream(archivo);

							String rutaOriginal = archivo.getAbsolutePath();
							reader = null;

							try {
								reader = new PdfReader((InputStream) file);
							} catch (IOException e) {
								try {
									LOGGER.error("Error al crear el PdfReader para el documento: " + archivo.getName(), e);
									String docFilePath = convert2PDF(cct, archivo, "");
									
									if (StringUtils.isNotEmpty(docFilePath)) {
										archivo = new File(docFilePath);

										file.close();
										file = new FileInputStream(archivo);

										rutaOriginal = archivo.getAbsolutePath();

										reader = new PdfReader((InputStream) file);
									}
								} catch (Exception e1) {
									LOGGER.error("Error al volver a intentar crear el PdfReader para el documento: " + archivo.getName(), e1);
									erroresFW.write("\t - Error al recuperar el documento '" + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\t\tExpediente: '" + numexp + "'\n\n");
									hayErrores = true;
								}
							}

							if (reader != null) {

								reader.consolidateNamedDestinations();
								int numPag = reader.getNumberOfPages();

								numPagsDocs.add(numPag);

								if (primero) {
									primero = false;
									documentDoc = new Document(reader.getPageSize(1));

									documentDoc.setMargins(100, documentDoc.rightMargin(), documentDoc.topMargin(), 27);

									Font fuentePagina = new Font( Font.TIMES_ROMAN);
									fuentePagina.setStyle(Font.BOLD);
									fuentePagina.setSize(13);

									if (colorNumPag == null){
										colorNumPag = new Color(255, 255, 255);
									}
									fuentePagina.setColor(colorNumPag);

									HeaderFooter nPaginaFooterDoc = new HeaderFooter( new Phrase("", fuentePagina), true);
									nPaginaFooterDoc.setAlignment(Element.ALIGN_RIGHT);
									nPaginaFooterDoc.setBorder(0);
									nPaginaFooterDoc.setRight(0);
									nPaginaFooterDoc.setBottom(0);

									// Como es la primera página añadimos la
									// portada
									resultadoDoc = new File(filePathNotificacionesDoc);
									resultadoDocFO = new FileOutputStream(resultadoDoc, true);

									writerDoc = PdfWriter.getInstance(documentDoc, resultadoDocFO);
									writerDoc.setPdfVersion(PdfWriter.VERSION_1_4);
									writerDoc.setViewerPreferences(PdfWriter.PageModeUseOutlines);

									if (añadeIndice) {
										numPagsIndice = (numeroTotalDeDoc / DOCUMENTOS_POR_PAGINA) + 1;
									} else {
										numPagsIndice = 0;
									}
									if (añadePortada) {
										numPagsIndice++;
										numPagsIndiceReales++;
									}

									nPagActual = numPagsIndice;

									if (añadePortada) {
										nPaginaFooterDoc.setPageNumber(nPagActual - 1);
										documentDoc.setFooter(nPaginaFooterDoc);
										documentDoc.setPageCount(nPagActual - 1);
										documentDoc.open();
									} else {
										nPaginaFooterDoc.setPageNumber(nPagActual);
										documentDoc.setFooter(nPaginaFooterDoc);
										documentDoc.setPageCount(nPagActual);
										documentDoc.open();
									}
									nPagActual++;

									if (añadeIndice) {
										HashMap<String, Object> map = new HashMap<String, Object>();

										map = new HashMap<String, Object>();
										map.put("Title", "Índice");
										map.put("Action", "GoTo");

										if (añadePortada){
											map.put("Page", "2");
										} else {
											map.put("Page", "1");
										}

										marcas.add(map);
									}
								}

								String numDocMostrar = "";
								if (numeroTotalDeDoc < 10){
									numDocMostrar = "" + (contadorDocumentos + 1);
								}else if (numeroTotalDeDoc < 100) {
									if (contadorDocumentos < 9){
										numDocMostrar = "0" + (contadorDocumentos + 1);
									} else{
										numDocMostrar = "" + (contadorDocumentos + 1);
									}
								} else if (numeroTotalDeDoc < 1000) {
									if (contadorDocumentos < 9){
										numDocMostrar = "00" + (contadorDocumentos + 1);
									} else if (contadorDocumentos < 99){
										numDocMostrar = "0" + (contadorDocumentos + 1);
									} else {
										numDocMostrar = "" + (contadorDocumentos + 1);
									}
								}

								else if (numeroTotalDeDoc < 10000) {
									if (contadorDocumentos < 9){
										numDocMostrar = "000" + (contadorDocumentos + 1);
									} else if (contadorDocumentos < 99) {
										numDocMostrar = "00" + (contadorDocumentos + 1);
									} else if (contadorDocumentos < 999){
										numDocMostrar = "0" + (contadorDocumentos + 1);
									} else {
										numDocMostrar = "" + (contadorDocumentos + 1);
									}
								}

								String[] entrada;
								SimpleDateFormat format = new SimpleDateFormat( "dd/MM/yyyy");
								if (añadePortada){
									entrada = new String[] { numDocMostrar, descripcion, format.format(fechaAprobacion), "" + numPag, "" + (nPagActual - 1) };
								} else {
									entrada = new String[] { numDocMostrar, descripcion, format.format(fechaAprobacion), "" + numPag, "" + nPagActual };
								}
								camposIndice.add(entrada);

								LOGGER.info("Se añade el documento: " + numDocMostrar + " - " + descripcion + "." + extension);

								añadeDocumento(reader, documentDoc, writerDoc, rutaOriginal, numDocMostrar + " - " + descripcion + "." + extension, null, descripcion + "-" + nombreDoc, nPagActual, archivoSinConvertir);
								nPagActual += numPag;

								contadorDocumentos++;

								reader.close();

								if (file != null){
									file.close();
								}
								if (archivo != null && archivoSinConvertir.exists()){
									archivo.delete();
								}
								if (archivoSinConvertir != null && archivoSinConvertir.exists()){
									archivoSinConvertir.delete();
								}
							}// while docsResultadoIterator
						}
					}
					if (archivoOriginal != null && archivoOriginal.exists()) {
						archivoOriginal.delete();
						archivoOriginal = null;
					}
				}// while
				if (añadeContraportada){
					añadeContraPortada(documentDoc);
				}

				if (documentDoc != null){
					documentDoc.close();
				}
				if (writerDoc != null){
					writerDoc.close();
				}
				if (resultadoDocFO != null){
					resultadoDocFO.close();
				}

				if (resultadoDoc != null && resultadoDoc.exists()) {
					ArrayList<File> listFicheros = new ArrayList<File>();
					listFicheros.add(resultadoDoc);

					concatenarArchivos(listFicheros, entitiesAPI, resultado, rulectx);

					resultadoDoc.delete();
				}
			} else {
				documentDoc = new Document(PageSize.A4, 50, 50, 100, 0);
				// Si no hay documentos creamos uno indicándolo
				resultado = new File(filePathNotificaciones);
				resultadoDocFO = new FileOutputStream(resultado);
				writerDoc = PdfWriter.getInstance(documentDoc, resultadoDocFO);

				Font fuentePagina = new Font(Font.TIMES_ROMAN);
				fuentePagina.setStyle(Font.BOLD);
				fuentePagina.setSize(13);

				if (colorNumPag == null){
					colorNumPag = new Color(255, 255, 255);
				}
				fuentePagina.setColor(colorNumPag);

				HeaderFooter nPaginaFooterDoc = new HeaderFooter(new Phrase("", fuentePagina), true);
				nPaginaFooterDoc.setAlignment(Element.ALIGN_RIGHT);
				nPaginaFooterDoc.setBorder(0);
				nPaginaFooterDoc.setRight(0);
				nPaginaFooterDoc.setBottom(0);
				nPaginaFooterDoc.setPageNumber(1);

				documentDoc.open();

				documentDoc.setFooter(nPaginaFooterDoc);
				documentDoc.setPageCount(0);

				// Como es la primera página añadimos la portada
				if (añadePortada){
					añadePortada(documentDoc, entitiesAPI, rulectx);
				}

				nuevaPagina(documentDoc, true, true, true, PageSize.A4);

				Font fuenteNoIncluir = new Font(Font.TIMES_ROMAN);
				fuenteNoIncluir.setStyle(Font.BOLD);
				fuenteNoIncluir.setSize(20);

				String texto = "\n";
				texto += "\n";
				texto += "\n";
				texto += "\n";
				texto += "El expediente no tiene documentos a incluir";
				Phrase frase = new Phrase(texto, fuenteNoIncluir);
				Paragraph parrafo = new Paragraph();
				parrafo.add(frase);
				documentDoc.add(parrafo);

				if (añadeContraportada){
					añadeContraPortada(documentDoc);
				}

				if (documentDoc != null){
					documentDoc.close();
				}
				if (writerDoc != null){
					writerDoc.close();
				}
				if (resultadoDocFO != null){
					resultadoDocFO.close();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error al concatenar los PDF's: " + e.getMessage(), e);
		} finally {
			if (documentDoc != null){
				documentDoc.close();
			}
			if (writerDoc != null){
				writerDoc.close();
			}
			if (resultadoDocFO != null)
				try {
					resultadoDocFO.close();
				} catch (IOException e) {
					LOGGER.error( "ERROR al concatenar los PDF's: " + e.getMessage(), e);
				}
		}
		return resultado;
	}

	public int cuentaDocs(IClientContext cct, ArrayList<DocIncluir> docsDelExp) {
		int resultado = 0;
		try {
			if (docsDelExp.size() != 0) {
				Iterator<DocIncluir> docsDelExpIterator = docsDelExp.iterator();
				while (docsDelExpIterator.hasNext()) {

					DocIncluir documento1 = docsDelExpIterator.next();

					String infoPag = documento1.infoPagRDE;
					String numexp = documento1.numExp;
					String nombreDoc = documento1.nombreDoc;
					String extension = documento1.extension;
					String descripcion = documento1.descripcion;

					File getFile = null;
					File archivo = null;

					try {
						archivo = DocumentosUtil.getFile(cct, infoPag, null, extension.trim());
					} catch (ISPACException e) {
						LOGGER.error("Error al recuperar el documento '" + nombreDoc + "' del expediente: '" + numexp + "'", e);
						erroresFW.write("\t - Error al recuperar el documento '" + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\t\tExpediente: '" + numexp + "'\n\n");
						hayErrores = true;
					} catch (Exception e) {
						LOGGER.error("Error al recuperar el documento '" + nombreDoc + "' del expediente: '" + numexp + "'", e);
						erroresFW.write("\t - Error al recuperar el documento '" + nombreDoc + "'\n\t\t'" + descripcion + "." + extension.trim() + "'\n\t\tExpediente: '" + numexp + "'\n\n");
						hayErrores = true;
					}

					if (archivo != null) {
						if ("ZIP".equalsIgnoreCase(extension)) {
							try {
								ZipFile zipArchivo = new ZipFile(archivo);
								Enumeration<? extends ZipEntry> entradas = zipArchivo.entries();

								entradas = zipArchivo.entries();

								ZipEntry entrada;

								while (entradas.hasMoreElements()) {
									entrada = entradas.nextElement();

									if (!entrada.isDirectory()) {

										extension = FileUtils.getExtensionByNombreDoc(entrada.getName());

										InputStream in = zipArchivo.getInputStream(entrada);

										getFile = new File( FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + FileTemporaryManager.getInstance().newFileName( "." + extension));

										FileOutputStream fos = new FileOutputStream( getFile);
										int leido;
										byte[] buffer = new byte[1024];
										
										if (in != null && fos != null) {
											while (0 < (leido = in.read(buffer))) {
												fos.write(buffer, 0, leido);
											}
											
											fos.flush();
											fos.close();
											in.close();

											resultado += cuentaDocs(getFile, extension);
											
											if (getFile != null && getFile.exists()){
												getFile.delete();
											}
										}
									}
								}
								zipArchivo.close();
							} catch (ZipException e) {
								LOGGER.error( "Error al contar los documentos del archivo ZIP: " + nombreDoc, e);
							} catch (IOException e) {
								LOGGER.error( "Error al contar los documentos del archivo ZIP: " + nombreDoc, e);
							} catch (Exception e) {
								LOGGER.error( "Error al contar los documentos del archivo ZIP: " + nombreDoc, e);
							}
						} else if ("RAR".equalsIgnoreCase(extension)) {
							try {
								Archive a = new Archive(archivo);
								List<FileHeader> headers = a.getFileHeaders();

								Iterator<FileHeader> fileHeaderIterator = headers.iterator();

								while (fileHeaderIterator.hasNext()) {
									FileHeader entrada = fileHeaderIterator.next();

									if (!entrada.isDirectory()) {
										getFile = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + entrada.getFileNameString());
										
										int cont = 0;
										
										while (getFile.exists()) {
											getFile = new File( FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + cont + entrada .getFileNameString());
											cont++;
										}
										
										FileOutputStream fos = new FileOutputStream(getFile);

										a.extractFile(entrada, fos);

										fos.flush();
										fos.close();

										extension = FileUtils.getExtensionByNombreDoc(entrada.getFileNameString());

										resultado += cuentaDocs(getFile, extension);
										
										if (getFile != null && getFile.exists()){
											getFile.delete();
										}
									}
								}
								a.close();
							} catch (RarException e) {
								LOGGER.error( "Error al contar los documentos del archivo RAR: " + nombreDoc, e);
							} catch (IOException e) {
								LOGGER.error( "Error al contar los documentos del archivo RAR: " + nombreDoc, e);
							} catch (Exception e) {
								LOGGER.error( "Error al contar los documentos del archivo RAR: " + nombreDoc, e);
							}
						} else {
							resultado++;
						}
						if (archivo != null && archivo.exists()){
							archivo.delete();
						}
						if (getFile != null && getFile.exists()){
							getFile.delete();
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error al contar los documentos", e);
		}

		return resultado;
	}

	public int cuentaDocs(File archivo, String extension) {
		int resultado = 0;
		File getFile = null;
		
		try {
			if ("ZIP".equalsIgnoreCase(extension)) {
				try {
					ZipFile zipArchivo;
					zipArchivo = new ZipFile(archivo);
					Enumeration<? extends ZipEntry> entradas = zipArchivo.entries();
					entradas = zipArchivo.entries();

					ZipEntry entrada;

					while (entradas.hasMoreElements()) {
						entrada = entradas.nextElement();

						if (!entrada.isDirectory()) {
							extension = FileUtils.getExtensionByNombreDoc(entrada.getName());

							if (!"ZIP".equalsIgnoreCase(extension) && !"RAR".equalsIgnoreCase(extension)) {
								resultado = 1;
							} else {
								InputStream in = zipArchivo.getInputStream(entrada);

								getFile = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + FileTemporaryManager.getInstance().newFileName("." + extension));

								FileOutputStream fos = new FileOutputStream(getFile);
								int leido;
								byte[] buffer = new byte[1024];
								
								if (in != null && fos != null) {
									while (0 < (leido = in.read(buffer))) {
										fos.write(buffer, 0, leido);
									}
									fos.flush();
									fos.close();
									in.close();

									resultado += cuentaDocs(getFile, extension);
									if (getFile != null && getFile.exists()){
										getFile.delete();
									}
								}
							}
						}
						zipArchivo.close();
					}
				} catch (ZipException e) {
					LOGGER.error( "Error al contar los documentos del archivo ZIP", e);
				} catch (IOException e) {
					LOGGER.error( "Error al contar los documentos del archivo ZIP", e);
				} catch (Exception e) {
					LOGGER.error( "Error al contar los documentos del archivo ZIP", e);
				}
			} else if ("RAR".equalsIgnoreCase(extension)) {
				try {

					Archive a = new Archive(archivo);
					List<FileHeader> headers = a.getFileHeaders();

					Iterator<FileHeader> fileHeaderIterator = headers.iterator();

					while (fileHeaderIterator.hasNext()) {

						FileHeader entrada = fileHeaderIterator.next();

						if (!entrada.isDirectory()) {
							extension = FileUtils.getExtensionByNombreDoc(entrada.getFileNameString());

							if (!"ZIP".equalsIgnoreCase(extension) && !"RAR".equalsIgnoreCase(extension)) {
								resultado = 1;
							} else {
								getFile = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + entrada.getFileNameString());
								int cont = 0;
								while (getFile.exists()) {
									getFile = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + cont + entrada.getFileNameString());
									cont++;
								}
								FileOutputStream fos = new FileOutputStream( getFile);

								a.extractFile(entrada, fos);

								fos.flush();
								fos.close();

								resultado += cuentaDocs(getFile, extension);
								if (getFile != null && getFile.exists()){
									getFile.delete();
								}
							}
						}
					}
					a.close();
				} catch (RarException e) {
					LOGGER.error( "Error al contar los documentos del archivo RAR", e);
				} catch (IOException e) {
					LOGGER.error( "Error al contar los documentos del archivo RAR", e);
				} catch (Exception e) {
					LOGGER.error( "Error al contar los documentos del archivo RAR", e);
				}
			} else {
				resultado = 1;
			}
			if (getFile != null && getFile.exists()){
				getFile.delete();
			}
		} catch (Exception e) {
			LOGGER.error("ERROR al contar los documentos", e);
		}

		return resultado;
	}

	public void concatenarArchivos(ArrayList<File> listFicheros, IEntitiesAPI entitiesAPI, File resultado, IRuleContext rulectx) throws ISPACException {

		PdfReader reader = null;
		File file = null;
		InputStream is = null;

		Document document = null;
		PdfWriter copy = null;
		PdfImportedPage page = null;

		PdfDictionary root = null;
		PdfDictionary names = null;
		PdfDictionary files = null;
		PdfArray filespecs = null;
		PdfDictionary refs = null;
		PRStream stream = null;
		PdfFileSpecification pfs = null;

		int desfaseIndice = 0;

		try {
			for (int i = 0; i < listFicheros.size(); i++) {
				file = (File) listFicheros.get(i);
				is = (InputStream) new FileInputStream(file);
				reader = new PdfReader(is);

				if (i == 0) {
					// Creamos un objeto Document
					document = new Document( PageSize.A4);
					document.setMargins(document.leftMargin(), document.rightMargin(), document.topMargin(), 27);

					copy = PdfWriter.getInstance( document, new FileOutputStream(resultado));

					copy.setPdfVersion(PdfCopy.VERSION_1_4);
					copy.setViewerPreferences(PdfCopy.PageModeUseOutlines);

					document.open();
					document.setPageCount(0);

					if (añadePortada){
						document = añadePortada2(document, entitiesAPI, rulectx);
					}
					if (añadeIndice){
						document = añadeIndice(document, copy);
					}

					desfaseIndice = numPagsIndiceReales - numPagsIndice;
				}

				// Añadimos el contenido
				reader.consolidateNamedDestinations();
				int n = reader.getNumberOfPages();

				int cont = 0;
				for (int j = 1; j <= n; j++) {
					document.setPageSize(reader.getPageSizeWithRotation(j));
					document.newPage();
					page = copy.getImportedPage(reader, j);
					Image img = Image.getInstance(page);

					img.setAbsolutePosition(0.0F, 0.0F);

					if (cont < camposIndice.size()) {
						String nPagActual = camposIndice.get(cont)[4];
						int pagActual = Integer.parseInt(nPagActual);
						if (añadePortada){
							pagActual++;
						}
						// if(j == pagActual-numPagsIndice){
						if (j == pagActual - numPagsIndiceReales) {
							// Si estamos en la página que debe tener destino, lo añadimos
							document.add(new Chunk(" ").setLocalDestination(nPagActual));
							cont++;
						}
					}
					document.add(img);
				}

				// Añadimos los adjuntos
				root = reader.getCatalog();
				names = root.getAsDict(PdfName.NAMES);
				
				if (names != null) {
					files = names.getAsDict(PdfName.EMBEDDEDFILES);
					
					if (files != null) {
						filespecs = files.getAsArray(PdfName.NAMES);

						String descripcionAdjunto = "";
						
						if (filespecs != null) {
							for (int k = 0; k < filespecs.size(); k++) {
								PdfDictionary temp = filespecs.getAsDict(k);
								
								if (temp != null) {
									refs = temp.getAsDict(PdfName.EF);									
									for (Object key : refs.getKeys()) {
										stream = (PRStream) PdfReader.getPdfObject(refs.getAsIndirectObject((PdfName) key));

										String descripcionAdjuntoAux = filespecs.getAsDict(k).getAsString((PdfName) key).toString();
										descripcionAdjuntoAux = descripcionAdjuntoAux.replaceAll("/", "-");
										descripcionAdjuntoAux = descripcionAdjuntoAux.replaceAll(":", "-");
										
										if(!descripcionAdjunto.equals(descripcionAdjuntoAux)){
											pfs = PdfFileSpecification.fileEmbedded(copy,descripcionAdjuntoAux,descripcionAdjuntoAux,PdfReader.getStreamBytes(stream));
											if (pfs != null)
												copy.addFileAttachment(descripcionAdjuntoAux, pfs);
										}
										descripcionAdjunto = descripcionAdjuntoAux;
									}
								}
							}
						}
					}
				}
			}

			for (int i = 0; i < marcas.size(); i++) {
				((HashMap<String, Object>) marcas.get(i)).put("Page", (Integer.parseInt((String) marcas.get(i).get("Page")) + desfaseIndice)+ "");
				newBookmarks.add(i, marcas.get(i));
			}

			// Copiamos los marcadores
			PdfDictionary top = new PdfDictionary();
			PdfIndirectReference topRef = copy
					.getPdfIndirectReference();
			Object[] kids = SimpleBookmark.iterateOutlines(copy, topRef, newBookmarks, false);

			top.put(PdfName.FIRST, (PdfIndirectReference) kids[0]);
			top.put(PdfName.LAST, (PdfIndirectReference) kids[1]);
			top.put(PdfName.COUNT, new PdfNumber(((Integer) kids[2]).intValue()));
			copy.addToBody(top, topRef);
			copy.getExtraCatalog().put(PdfName.OUTLINES, topRef);

		} catch (Exception e) {
			LOGGER.error("ERROR al concatenar los PDF's " + e.getMessage(), e);
			throw new ISPACException( "Error al concatenar los archivos del documento. " + e.getMessage(), e);
		} finally {
			// Cerramos todo
			if (document != null){
				document.close();
			}
			if (copy != null){
				copy.close();
			}
			if (file != null){
				file = null;
			}
			if (is != null){
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			if (reader != null){
				reader.close();
			}
		}
	}

	public Document añadeIndice( Document document, PdfWriter writer) throws ISPACException, DocumentException, MalformedURLException, IOException, DocumentException {

		String texto = "";
		Phrase frase = null;
		PdfPCell celda = null;
		PdfPTable tabla = null;
		Paragraph parrafo = null;

		Font fuentePagina = new Font( Font.TIMES_ROMAN);
		fuentePagina.setStyle(Font.BOLD);
		fuentePagina.setSize(13);
		
		if (bColorNumPag == null){
			bColorNumPag = Color.WHITE;
		}
		
		fuentePagina.setColor(bColorNumPag);

		// Fuentes de los textos
		Font fuente = new Font( Font.TIMES_ROMAN);
		fuente.setStyle(Font.BOLD);
		fuente.setSize(30);

		Font fuente2 = new Font( Font.TIMES_ROMAN);
		fuente2.setStyle(Font.BOLD);
		fuente2.setSize(13);

		Font fuenteNoIncluir = new Font( Font.TIMES_ROMAN);
		fuenteNoIncluir.setStyle(Font.BOLD);
		fuenteNoIncluir.setSize(20);

		Font fuente3 = new Font( Font.TIMES_ROMAN);
		fuente3.setStyle(Font.NORMAL);
		
		/**
		 * [Teresa#1255#SIGEM#3SP] Cambiar el tamaño de indice en el foliado.
		 * **/
		// fuente3.setSize(13);
		fuente3.setSize(9);

		if (parrafo == null){
			parrafo = new Paragraph();
		}
		boolean primeraPag = true;
		int docIndice = 0;
		int contadorDocs = 0;
		int numPag = 0;
		
		// MQE Añadimos la Información del documento
		if (camposIndice != null && camposIndice.size() > 0) {
			while (contadorDocs < camposIndice.size()) {
				String numDoc = camposIndice.get(contadorDocs)[0];
				String doc = camposIndice.get(contadorDocs)[1];
				String fechaDoc = camposIndice.get(contadorDocs)[2];
				String numPags = camposIndice.get(contadorDocs)[3];
				String nPagActual = camposIndice.get(contadorDocs)[4];

				contadorDocs++;
				docIndice++;

				// Añadimos el cuerpo frase a frase
				parrafo.setAlignment(Element.ALIGN_CENTER);
				parrafo.setExtraParagraphSpace(5);
				if (primeraPag) {
					primeraPag = false;

					nuevaPagina2(document, true, true, true,
							PageSize.A4);
					numPag++;

					texto = "\n";
					texto += "\n";
					texto += "\n";
					texto += "\n";
					texto += "\n";
					texto += "\n";
					texto += "Índice de contenidos";
					frase = new Phrase(texto, fuente);

					parrafo.add(frase);

					// Tabla columnas y filas
					tabla = inicializaTabla(fuente2);
					numPagsIndiceReales++;
					if (añadePortada){
						document.add(new Chunk(" ").setLocalDestination("" + 1));
					}
				}

				if (tabla == null){
					tabla = inicializaTabla(fuente2);
				}

				texto = "";

				if (DOCUMENTOS_POR_PAGINA <= docIndice) {
					parrafo.add(tabla);
					document.add(parrafo);

					// Añadimos el número de página a la página anterior
					Rectangle rect = document.getPageSize();
					ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("" + numPag, fuentePagina), rect.getWidth() - 35, 27, 0);

					nuevaPagina2(document, true, true, true, PageSize.A4);
					numPag++;

					parrafo = new Paragraph();
					parrafo.add(new Phrase("\n"));
					document.add(parrafo);
					document.add(parrafo);
					document.add(parrafo);
					document.add(parrafo);
					document.add(parrafo);
					document.add(parrafo);

					tabla = inicializaTabla(fuente2);
					numPagsIndiceReales++;

					parrafo = new Paragraph();
					parrafo.setAlignment(Element.ALIGN_CENTER);

					parrafo.setExtraParagraphSpace(5);
					docIndice = 0;
				}

				// Descripción del documento
				Paragraph parrafoAux = new Paragraph();
				frase = new Phrase(numDoc + " ", fuente3);
				parrafoAux.add(frase);

				if (doc.length() > 243){
					docIndice += 3;
				} else if (doc.length() > 162){
					docIndice += 2;
				} else if (doc.length() > 81){
					docIndice++;
				}

				if (doc.length() > 45) {
					Font fuente4 = new Font( Font.TIMES_ROMAN);
					fuente4.setStyle(Font.NORMAL);
					fuente4.setSize(9);
					frase = new Phrase(doc, fuente4);
				} else if (doc.length() > 40) {
					Font fuente5 = new Font( Font.TIMES_ROMAN);
					fuente5.setStyle(Font.NORMAL);
					
					/**
					 * [Teresa#1255#SIGEM#3SP] Cambiar el tamaño de indice en el
					 * foliado.
					 * **/
					// fuente3.setSize(11);
					fuente3.setSize(9);
					frase = new Phrase(doc, fuente5);
				} else if (doc.length() > 35) {
					Font fuente6 = new Font( Font.TIMES_ROMAN);
					fuente6.setStyle(Font.NORMAL);
					
					/**
					 * [Teresa#1255#SIGEM#3SP] Cambiar el tamaño de indice en el
					 * foliado.
					 * **/
					// fuente3.setSize(13);
					fuente3.setSize(9);
					frase = new Phrase(doc, fuente6);
				} else {
					frase = new Phrase(doc, fuente3);
				}

				parrafoAux.add(frase);
				celda = new PdfPCell(parrafoAux);
				celda.setBorder(0);
				celda.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
				tabla.addCell(celda);

				frase = new Phrase(fechaDoc, fuente3);
				celda = new PdfPCell(frase);
				celda.setBorder(0);
				celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
				tabla.addCell(celda);

				frase = new Phrase("" + numPags, fuente3);
				celda = new PdfPCell(frase);
				celda.setBorder(0);
				celda.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				tabla.addCell(celda);

				celda = new PdfPCell( new Paragraph( new Chunk(nPagActual, fuente3).setLocalGoto(nPagActual)));
				celda.setBorder(0);
				celda.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
				tabla.addCell(celda);
			}// fin while
		} else {
			texto = "\n";
			texto += "\n";
			texto += "\n";
			texto += "\n";
			texto += "El expediente no tiene documentos a incluir";
			frase = new Phrase(texto, fuenteNoIncluir);
			parrafo = new Paragraph(frase);
		}
		parrafo.add(tabla);
		document.add(parrafo);

		// Añadimos el número de página
		Rectangle rect = document.getPageSize();
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("" + numPag, fuentePagina), rect.getWidth() - 35, 27, 0);

		return document;
	}

	public void añadeDocumento(PdfReader reader, Document document, PdfWriter writer, String rutaOriginal, String nombreDocumento, Anchor referenciaIndice, String descripcionAdjunto, int nPagActual, File archivoSinConvertir) throws ISPACException, DocumentException, MalformedURLException, IOException {

		PdfImportedPage page;

		Rectangle rectangulo = document.getPageSize();

		HashMap<String, Object> map = new HashMap<String, Object>();

		map = new HashMap<String, Object>();
		map.put("Title", "Documento " + nombreDocumento);
		map.put("Action", "GoTo");
		map.put("Page", "" + nPagActual);

		marcas.add(map);

		int n = reader.getNumberOfPages();
		for (int i = 0; i < n;) {
			i++;
			// Rectangle dimensiones = reader.getPageSizeWithRotation(i);
			Rectangle dimensiones = reader.getPageSize(i);

			nuevaPagina(document, false, true, true, dimensiones);
			document.add(new Chunk(" ").setLocalDestination("" + nPagActual));

			page = writer.getImportedPage(reader, i);
			Image imagen = Image.getInstance(page);

			imagen.setAbsolutePosition(50, 50);
			imagen.scaleAbsoluteWidth(rectangulo.getWidth());
			imagen.scaleAbsoluteHeight(rectangulo.getHeight());
			imagen.setBorder(2);
			imagen.setBorderWidthTop((float) 0.2);
			imagen.setBorderWidthLeft((float) 0.2);
			imagen.setBorderWidthRight((float) 0.2);
			imagen.setBorderWidthBottom((float) 0.2);
			imagen.scalePercent(90);

			document.add(imagen);
		}

		PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer, archivoSinConvertir.getAbsolutePath(), nombreDocumento, null);
		
		if (pfs != null){
			writer.addFileAttachment(descripcionAdjunto, pfs);
		}
	}

	public PdfPTable inicializaTabla( Font fuente2) throws DocumentException {
		
		PdfPTable tabla = null;
		tabla = new PdfPTable(4);
		tabla.setWidthPercentage(100);

		float[] anchoColumnas = new float[] { 60, 20, 10, 10 };
		tabla.setWidths(anchoColumnas);

		Phrase frase = new Phrase( "Nombre Documento", fuente2);
		PdfPCell celda;
		celda = new PdfPCell(frase);
		celda.setBorder(0);
		celda.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tabla.addCell(celda);

		frase = new Phrase("Fecha Documento", fuente2);
		celda = new PdfPCell(frase);
		celda.setBorder(0);
		celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		tabla.addCell(celda);

		frase = new Phrase("Nº de páginas", fuente2);
		celda = new PdfPCell(frase);
		celda.setBorder(0);
		celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		tabla.addCell(celda);

		frase = new Phrase("Página", fuente2);
		celda = new PdfPCell(frase);
		celda.setBorder(0);
		celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		tabla.addCell(celda);

		return tabla;
	}

	public void nuevaPagina2(Document document, boolean imgCabecera, boolean hayFondo, boolean hayPie, Rectangle dimensiones) throws MalformedURLException, IOException, DocumentException, DocumentException, ISPACException {
		
		// Añadimos el logotipo de la diputación
		document.setMargins(document.leftMargin() + 20, document.rightMargin(), document.topMargin(), document.bottomMargin());
		document.setPageSize(dimensiones);
		document.newPage();

		Image imagen = null;
		if (imgCabecera) {
			try {
				imagen = Image.getInstance(IMG_LOGO_CABECERA);
				imagen.setAbsolutePosition(50, document.getPageSize().getHeight() - 100);
				imagen.scalePercent(50);
				document.add(imagen);
			} catch (Exception e) {
				LOGGER.error( "ERROR no se ha encontrado la imagen de logo de la entidad: " + IMG_LOGO_CABECERA + ". " + e.getMessage(), e);
				throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de logo de la entidad: " + IMG_LOGO_CABECERA + ". " + e.getMessage(), e);
			}
		}

		// Añadimos la imagen del fondo
		if (hayFondo) {
			try {
				imagen = Image.getInstance(IMG_FONDO);
				imagen.setAbsolutePosition(250, 50);
				imagen.scalePercent(70);
				document.add(imagen);
			} catch (Exception e) {
				LOGGER.error("ERROR no se ha encontrado la imagen de fondo: " + IMG_FONDO + ". " + e.getMessage(), e);
				throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de fondo: " + IMG_FONDO + ". " + e.getMessage(), e);
			}
		}

		// Añadimos el pie de página de la diputación
		if (hayPie) {
			try {
				imagen = Image.getInstance(IMG_PIE);
				imagen.setAbsolutePosition( document.getPageSize().getWidth() - 550, 15);
				imagen.scalePercent(80);
				document.add(imagen);
			} catch (Exception e) {
				LOGGER.error( "ERROR no se ha encontrado la imagen de pie de página: " + IMG_PIE + ". " + e.getMessage(), e);
				throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de pie de página: " + IMG_PIE + ". " + e.getMessage(), e);
			}
		}
		document.setMargins(document.leftMargin() - 20, document.rightMargin(), document.topMargin(), document.bottomMargin());
	}

	public void nuevaPagina(Document document, boolean hayCabecera, boolean hayFondo, boolean hayPie, Rectangle dimensiones) throws MalformedURLException, IOException, DocumentException, ISPACException {
		// Añadimos el logotipo de la diputación
		document.setPageSize(dimensiones);
		document.newPage();
		Image imagen = null;
		
		if (hayCabecera) {
			try {
				imagen = Image.getInstance(IMG_LOGO_CABECERA);
				imagen.setAbsolutePosition(50, document.getPageSize() .getHeight() - 100);
				imagen.scalePercent(50);
				document.add(imagen);
			} catch (Exception e) {
				LOGGER.error( "ERROR no se ha encontrado la imagen de logo de la entidad: " + IMG_LOGO_CABECERA + ". " + e.getMessage(), e);
				throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de logo de la entidad: " + IMG_LOGO_CABECERA + ". " + e.getMessage(), e);
			}
		}

		// Añadimos la imagen del fondo
		if (hayFondo) {
			try {
				imagen = Image.getInstance(IMG_FONDO);
				imagen.setAbsolutePosition(250, 50);
				imagen.scalePercent(70);
				document.add(imagen);
			} catch (Exception e) {
				LOGGER.error("ERROR no se ha encontrado la imagen de fondo: " + IMG_FONDO + ". " + e.getMessage(), e);
				throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de fondo: " + IMG_FONDO + ". " + e.getMessage(), e);
			}
		}

		// Añadimos el pie de página de la diputación
		if (hayPie) {
			try {
				imagen = Image.getInstance(IMG_PIE);
				imagen.setAbsolutePosition( document.getPageSize().getWidth() - 550, 15);
				imagen.scalePercent(80);
				document.add(imagen);
			} catch (Exception e) {
				LOGGER.error( "ERROR no se ha encontrado la imagen de pie de página: " + IMG_PIE + ". " + e.getMessage(), e);
				throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de pie de página: " + IMG_PIE + ". " + e.getMessage(), e);
			}
		}
	}

	public void añadePortada(Document document, IEntitiesAPI entitiesAPI, IRuleContext rulectx) throws ISPACException, DocumentException, MalformedURLException, IOException {

		IItem expediente = ExpedientesUtil.getExpediente( rulectx.getClientContext(), numExpPadre);
		String cadena = numExpPadre;
		
		if (expediente != null) {
			if (StringUtils.isNotEmpty(expediente.getString("ASUNTO"))){
				cadena += " - " + expediente.getString("ASUNTO");
			}
		}

		document.newPage();
		// Añadimos el fondo
		try {
			Image imagenFondo = Image.getInstance(IMG_PORTADA);
			imagenFondo.scaleAbsolute(document.getPageSize().getWidth(), document.getPageSize().getHeight());
			imagenFondo.setAbsolutePosition(0, 0);
			document.add(imagenFondo);
		} catch (Exception e) {
			LOGGER.error("ERROR no se ha encontrado la imagen de portada: " + IMG_PORTADA + ". " + e.getMessage(), e);
			throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de portada: " + IMG_PORTADA + ". " + e.getMessage(), e);
		}

		// Añadimos el logotipo de la diputación
		try {
			Image imagen = Image.getInstance(IMG_LOGO_PORTADA);
			imagen.setAbsolutePosition(50, 250);
			imagen.scalePercent(50);
			document.add(imagen);
		} catch (Exception e) {
			LOGGER.error( "ERROR no se ha encontrado la imagen logotipo de la entidad: " + IMG_LOGO_PORTADA + ". " + e.getMessage(), e);
			throw new ISPACRuleException( "ERROR no se ha encontrado la imagen logotipo de la entidad: " + IMG_LOGO_PORTADA + ". " + e.getMessage(), e);
		}

		// Fuentes de los textos
		Font fuente = new Font(Font.TIMES_ROMAN);
		fuente.setStyle(Font.BOLD);

		if (colorAsunto == null){
			colorAsunto = new Color(153, 0, 0, 240);
		}

		fuente.setColor(colorAsunto);
		fuente.setSize(15);

		// Añadimos el cuerpo frase a frase
		Paragraph parrafo = new Paragraph();
		parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

		StringBuilder texto = new StringBuilder("");
		com.lowagie.text.Phrase frase = null;

		fuente.setSize(20);
		// texto = cadena;
		texto.append("\n\n\n\n\n\n\n");
		StringBuilder cadenaAux = new StringBuilder(cadena);
		StringBuilder cadena2 = new StringBuilder("");
		String[] cadenaSplit;
		
		while (cadenaAux.toString().length() > 20) {
			cadena2 = new StringBuilder("");
			cadenaSplit = cadenaAux.toString().split(" ");
			cadena2.append(cadenaSplit[0]);
			cadena2.append(" ");
			int j = 1;
			
			while (j < cadenaSplit.length && cadena2.toString().length() < 20) {
				cadena2.append(cadenaSplit[j]);
				cadena2.append(" ");
				j++;
			}
			
			texto.append("\n\t");
			texto.append(cadena2);
			texto.append("\n");
			cadenaAux = new StringBuilder("");
			
			for (int i = j; i < cadenaSplit.length; i++) {
				cadenaAux.append(cadenaSplit[i]);
				cadenaAux.append(" ");
			}
		}
		texto.append("\n");
		texto.append("\n\t");
		texto.append(cadenaAux);
		texto.append("\n");

		frase = new Phrase(texto.toString(), fuente);
		parrafo.add(frase);
		document.add(parrafo);
		// Fin de la portada
	}

	public Document añadePortada2( Document document, IEntitiesAPI entitiesAPI, IRuleContext rulectx) throws ISPACException, DocumentException, MalformedURLException, IOException, DocumentException {

		IItem expediente = ExpedientesUtil.getExpediente( rulectx.getClientContext(), numExpPadre);
		String cadena = numExpPadre;
		
		if (null != expediente) {
			if (StringUtils.isNotEmpty(expediente.getString("ASUNTO"))){
				cadena += " - " + expediente.getString("ASUNTO");
			}
		}

		document.newPage();
		document.setPageSize(PageSize.A4);
		// Añadimos el fondo
		try {
			Image imagenFondo = Image .getInstance(IMG_PORTADA);
			imagenFondo.scaleAbsolute(document.getPageSize().getWidth(), document.getPageSize().getHeight());
			imagenFondo.setAbsolutePosition(0, 0);
			document.add(imagenFondo);
			
		} catch (Exception e) {
			LOGGER.error("ERROR no se ha encontrado la imagen de portada: " + IMG_PORTADA + ". " + e.getMessage(), e);
			throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de portada: " + IMG_PORTADA + ". " + e.getMessage(), e);
		}

		// Añadimos el logotipo de la diputación
		try {
			Image imagen = Image.getInstance(IMG_LOGO_PORTADA);
			imagen.setAbsolutePosition(50, 250);
			imagen.scalePercent(50);
			document.add(imagen);
		} catch (Exception e) {
			LOGGER.error( "ERROR no se ha encontrado la imagen de logo de la entidad: " + IMG_LOGO_PORTADA + ". " + e.getMessage(), e);
			throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de logo de la entidad: " + IMG_LOGO_PORTADA + ". " + e.getMessage(), e);
		}

		// Fuentes de los textos
		Font fuente = new Font( Font.TIMES_ROMAN);
		fuente.setStyle(Font.BOLD);

		if (bColorAsunto == null){
			bColorAsunto = new Color(153, 0, 0, 240);
		}
		fuente.setColor(bColorAsunto);
		fuente.setSize(15);

		// Añadimos el cuerpo frase a frase
		Paragraph parrafo = new Paragraph();
		parrafo.setAlignment(Element.ALIGN_JUSTIFIED);

		StringBuilder texto = new StringBuilder("");
		Phrase frase = null;

		fuente.setSize(20);
		texto.append("\n\n\n\n\n\n\n");
		StringBuilder cadenaAux = new StringBuilder(cadena);
		StringBuilder cadena2 = new StringBuilder("");
		String[] cadenaSplit;
		
		while (cadenaAux.toString().length() > 20) {
			cadena2 = new StringBuilder("");
			cadenaSplit = cadenaAux.toString().split(" ");
			cadena2.append(cadenaSplit[0]);
			cadena2.append(" ");
			int j = 1;
		
			while (j < cadenaSplit.length && cadena2.toString().length() < 20) {
				cadena2.append(cadenaSplit[j]);
				cadena2.append(" ");
				j++;
			}
			
			texto.append("\n");
			texto.append(cadena2);
			texto.append("\n");
			cadenaAux = new StringBuilder("");
			
			for (int i = j; i < cadenaSplit.length; i++) {
				cadenaAux.append(cadenaSplit[i]);
				cadenaAux.append(" ");
			}
		}
		texto.append("\n");
		texto.append("\n\t");
		texto.append(cadenaAux);
		texto.append("\n");

		frase = new Phrase(texto.toString(), fuente);

		parrafo.setAlignment(Paragraph.ALIGN_RIGHT);
		parrafo.add(frase);

		document.add(parrafo);

		return document;
		// Fin de la portada
	}

	public void añadeContraPortada(Document document) throws ISPACException, DocumentException, MalformedURLException, IOException {

		document.resetFooter();
		// Añadimos el fondo
		document.setPageSize(PageSize.A4);
		document.newPage();
		
		try {
			Image imagenFondo = Image.getInstance(IMG_CONTRAPORTADA);
			imagenFondo.scaleAbsolute(document.getPageSize().getWidth(), document.getPageSize().getHeight());
			imagenFondo.setAbsolutePosition(0, 0);

			// Quitamos el número de página

			document.add(imagenFondo);

			// Fin de la portada
		} catch (Exception e) {
			LOGGER.error( "ERROR no se ha encontrado la imagen de contraportada: " + IMG_CONTRAPORTADA + ". " + e.getMessage(), e);
			throw new ISPACRuleException( "ERROR no se ha encontrado la imagen de contraportada: " + IMG_CONTRAPORTADA + ". " + e.getMessage(), e);
		}
	}

	public static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		name = StringUtils.replace(name, "(", "");
		name = StringUtils.replace(name, ")", "");
		name = StringUtils.replace(name, ".", "-");
		name = StringUtils.replace(name, ":", " ");
		name = name.trim();
		return name;
	}

	protected String convert2PDF(Object connectorSession, File archivo, String extension) throws ISPACException, IOException {
		String resultado = "";
		File temporal = null;

		String extensionOrig = extension;
		FileReader fr = null;
		BufferedReader in = null;
		PrintWriter out = null;
		String sourceFileURL = "";
		OpenOfficeHelper ooHelper;

		try {
			FileTemporaryManager fileTmpMgr = FileTemporaryManager.getInstance();

			// Si es xml el OpenOfficeHelper da error
			// Si es xml se crea un archivo temporal txt que es el que se parsea
			// y se incluye el original en el pdf

			if ("XML".equalsIgnoreCase(extension.trim()) || "TXT".equalsIgnoreCase(extension.trim())) {
				extension = "txt";
				temporal = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/tempXML.txt");
				
				try {
					fr = new FileReader(archivo);
					in = new BufferedReader(fr);
					out = new PrintWriter(temporal);

					String linea = "";
					while ((linea = in.readLine()) != null){
						out.println(linea);
					}

					sourceFileURL = new StringBuilder().append("file:///").append(temporal.getPath()).toString();
					
				} catch (Exception e) {
					LOGGER.error("ERROR al convertir fichero con extensión: " + extensionOrig, e);

					extension = extensionOrig;
					sourceFileURL = new StringBuilder().append("file:///").append(temporal.getPath()).toString();
					
				} finally {
					if (in != null){
						in.close();
					}
					if (out != null){
						out.close();
					}
					if (fr != null){
						fr.close();
					}
				}
			} else {
				sourceFileURL = new StringBuilder().append("file:///").append(archivo.getPath()).toString();
			}

			String mime = MimetypeMapping.getMimeType(extension);
			checkSourceMimeType(mime);
			
			try {
				EXTS_TO_PDF.load(DocumentConverter.class.getClassLoader().getResourceAsStream("ieci/tdw/ispac/ispaclib/gendoc/converter/ConversorToGeneratePdf.properties"));
			} catch (IOException e) {
				LOGGER.error("Error loading internal ieci/tdw/ispac/ispaclib/gendoc/converter/ConversorToGeneratePdf.properties", e);
			}

			if (EXTS_TO_PDF.containsKey(extension)) {
				extension = EXTS_TO_PDF.getProperty(extension);
			}

			// Comprobar si los formatos de origen y destino son iguales
			if (!"pdf".equalsIgnoreCase(extension) && !"XML".equalsIgnoreCase(extension.trim())) {
				// Path del fichero destino
				File finalFile = fileTmpMgr.newFile(".pdf");
				@SuppressWarnings("deprecation")
				String finalFileURL = finalFile.toURL().toString();

				// Llama a OpenOffice para convertir el fichero a PDF
				try {
					ooHelper = OpenOfficeHelper.getInstance();
					ooHelper.load_and_Convert(sourceFileURL, finalFileURL, extension, "pdf");
					ooHelper.dispose();
				} catch (Exception e) {
					LOGGER.error( "ERROR al convertir el documento a PDF, vamos a intentar reconectar", e);
					ooHelper = OpenOfficeHelper.getInstance();
					ooHelper.load_and_Convert(sourceFileURL, finalFileURL, extension, "pdf");
					ooHelper.dispose();
				}

				if (finalFile == null || !finalFile.exists() || finalFile.length() == 0) {
					erroresFW.write("\t - ERROR al convertir el documento a PDF '" + archivo.getAbsolutePath() + "'\n\t\t'" + extension + "'\n\n");
					if (finalFile != null && finalFile.exists()){
						finalFile.delete();
					}
				} else {
					resultado = finalFile.getAbsolutePath();
				}
			} else {
				// Devolvemos el fichero pdf, pues no hace falta convertirlo
				resultado = archivo.getAbsolutePath();
			}
		} catch (ISPACInfo e) {
			LOGGER.error("Extension " + extension + " no permitida para convertir a pdf", e);
			throw new ISPACInfo(e, false);
		} catch (Exception e) {
			LOGGER.error("Error al convertir el documento a PDF: guid=[" + archivo.getAbsolutePath() + "], extension=[" + extension + "]", e);
			throw new ISPACException(e);
		} finally {
			if (temporal != null && temporal.exists()){
				temporal.delete();
			}
		}
		return resultado;
	}

	protected static void checkSourceMimeType(String mimeType) throws ISPACInfo {
		if (StringUtils.isBlank(mimeType)
				|| !(mimeType.equalsIgnoreCase("application/msword")
				|| StringUtils.containsIgnoreCase(mimeType, "application/excel")
				|| StringUtils.containsIgnoreCase(mimeType, "application/x-excel") // INICIO [eCenpri-Felipe #449]
				|| StringUtils.containsIgnoreCase(mimeType, "application/x-msexcel") // Metemos todos los alias posibles del Excel. El último es el oficial
				|| StringUtils.containsIgnoreCase(mimeType, "application/vndms-excel")
				|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.ms-excel") // FIN [eCenpri-Felipe #449]
				|| StringUtils.containsIgnoreCase(mimeType, "application/pdf")
				|| StringUtils.containsIgnoreCase(mimeType, "application/rtf")
				|| StringUtils.containsIgnoreCase(mimeType, "application/powerpoint")
				|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.text")
				|| mimeType.equalsIgnoreCase("application/vnd.oasis.opendocument.text-template") // odt, sxw, stw, sdw, ott, oth, odm
				|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.spreadsheet")
				|| mimeType.equalsIgnoreCase("application/vnd.oasis.opendocument.spreadsheet-template") // sxc, stc, sdc, ods, ots
				|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.chart") // *.odc
				|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.presentation")
				|| mimeType.equalsIgnoreCase("application/vnd.oasis.opendocument.presentation-template") // sxi, sti, sdd, sdp, odp, otp
				|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.graphics") // *.odG
				|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.oasis.opendocument.formula") // *.odf
				|| mimeType.equalsIgnoreCase("application/vnd.oasis.opendocument.image")
				|| mimeType.equalsIgnoreCase("application/vnd.oasis.opendocument.graphics-template")
				|| StringUtils.containsIgnoreCase(mimeType, "image/jpeg")
				|| StringUtils.containsIgnoreCase(mimeType, "image/gif")
				|| StringUtils.containsIgnoreCase(mimeType, "image/dib")
				|| StringUtils.containsIgnoreCase(mimeType, "image/x-xbitmap")
				|| StringUtils.containsIgnoreCase(mimeType, "image/tiff")
				|| StringUtils.containsIgnoreCase(mimeType, "image/tif")
				|| StringUtils.containsIgnoreCase(mimeType, "image/png")
				|| StringUtils.containsIgnoreCase(mimeType, "image/bmp")
				|| StringUtils.containsIgnoreCase(mimeType, "text/plain")
				|| StringUtils.containsIgnoreCase(mimeType, "text/xml")
				|| StringUtils.containsIgnoreCase(mimeType, "text/html")
				|| StringUtils.containsIgnoreCase(mimeType, "text/rtf")
				|| StringUtils.containsIgnoreCase(mimeType, "application/xml")
				|| StringUtils.containsIgnoreCase(mimeType, "application/x-rtf")
				|| StringUtils.containsIgnoreCase(mimeType, "application/vndms-powerpoint")
				|| StringUtils.containsIgnoreCase(mimeType, "image/x-windows-bmp")
				|| StringUtils.containsIgnoreCase(mimeType, "application/octet-stream")
				|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
				|| StringUtils.containsIgnoreCase(mimeType, "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
			
			throw new ISPACInfo( "El tipo del documento a convertir no es correcto '" + mimeType + "'", false);
		}
	}
}
