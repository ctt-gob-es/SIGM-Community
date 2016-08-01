package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.lowagie.text.Anchor;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrGenerarRemisionDocumentosFoliadoRule implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrGenerarRemisionDocumentosFoliadoRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		boolean resultado = true;
		String numexp = "";
		try {
			logger.info("INICIO - " + this.getClass().getName());

			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			// ----------------------------------------------------------------------------------------------
			Object connectorSession = null;
			ArrayList<String[]> arrayInfoPagsRDE = new ArrayList<String[]>();
				
			numexp = rulectx.getNumExp();
			String consultaDocumentos = getConsultaDocumentos(entitiesAPI, rulectx);
			
			IItemCollection documentsCollection = DocumentosUtil.queryDocumentos(cct, consultaDocumentos);
			getArrayInfoPagsRDE(documentsCollection, arrayInfoPagsRDE);
			
			FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();
			File documentoResumen = ftMgr.newFile(".pdf");

			// Creamos el documento
			documentoResumen = generarPdf(arrayInfoPagsRDE,documentoResumen.getPath(), genDocAPI, rulectx.getNumExp(), entitiesAPI, cct);
			
			
			int idTypeDocument = DocumentosUtil.getTipoDoc(cct, "EXPEDIENTE FOLIADO CON ÍNDICE", DocumentosUtil.BUSQUEDA_LIKE, true);
			if (idTypeDocument == Integer.MIN_VALUE) {
				throw new ISPACInfo("Error al obtener el tipo de documento 'EXPEDIENTE FOLIADO CON ÍNDICE' del expediente " + numexp);
			}

			String sExtension = "pdf";
			String sMimeType = MimetypeMapping.getMimeType(sExtension);
			if (sMimeType == null) {
				throw new ISPACInfo("Error al obtener el tipo Mime");
			}
			
			// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, rulectx.getNumExp(),  "ROL != 'TRAS' OR ROL IS NULL",  "ID");
			
			// 2. Comprobar que hay algún participante para el cual generar su notificación
			if (participantes!=null && participantes.toList().size()>=1) {
				// 4. Para cada participante generar una notificación
				for (int i=0;i<participantes.toList().size();i++){
					try {
						connectorSession = genDocAPI.createConnectorSession();
						IItem participante = (IItem) participantes.toList().get(i);
						// Abrir transacción para que no se pueda generar un documento sin fichero
				        cct.beginTX();
				        if (participante!=null){
					        String nombre = "";
					        String id_ext = "";
							// Añadir a la session los datos para poder utilizar <ispactag sessionvar='var'> en la plantilla
				        	if ((String)participante.get("NOMBRE")!=null){
				        		nombre = (String)participante.get("NOMBRE");
				        	}else{
				        		nombre = "";
				        	}
				        	if ((String)participante.get("ID_EXT")!=null){
				        		id_ext = (String)participante.get("ID_EXT");
				        	}else{
				        		id_ext = "";
				        	}
				        	
							String descripcion = "Remisión de Documentos"+ " - " + nombre;
							

							IItem entityDocument = DocumentosUtil.generaYAnexaDocumento(rulectx, idTypeDocument, descripcion, documentoResumen, "pdf");
							
							entityDocument.set("NOMBRE", "Remisión de Documentos");
							entityDocument.set("DESTINO", nombre);
							entityDocument.set("DESTINO_ID", id_ext);
							entityDocument.store(cct);
				        }
					}catch (Throwable e) {
						
						// Si se produce algún error se hace rollback de la transacción
						cct.endTX(false);
						
						String message = "exception.documents.generate";
						String extraInfo = null;
						Throwable eCause = e.getCause();
						
						if (eCause instanceof ISPACException) {
							
							if (eCause.getCause() instanceof NoConnectException) {
								extraInfo = "exception.extrainfo.documents.openoffice.off"; 
							}
							else {
								extraInfo = eCause.getCause().getMessage();
							}
						}
						else if (eCause instanceof DisposedException) {
							extraInfo = "exception.extrainfo.documents.openoffice.stop";
						}
						else {
							extraInfo = e.getMessage();
						}
						logger.error(e.getMessage(), e);
						throw new ISPACInfo(message, extraInfo);
						
					}finally {
						cct.endTX(true);
						if (connectorSession != null) {
							genDocAPI.closeConnectorSession(connectorSession);
						}
					}
				}
			}
		} catch (ISPACException e) {
			logger.error("Expediente: " +numexp + ", " + e.getMessage(), e);
		}
		
		logger.info("FIN - " + this.getClass().getName());
		
		return new Boolean(resultado);
	}
	
	@SuppressWarnings("rawtypes")
	private File generarPdf(ArrayList<String[]> inputStreamNotificaciones,
			String filePathNotificaciones, IGenDocAPI genDocAPI,
			String numExpPadre, IEntitiesAPI entitiesAPI, ClientContext cct) {

		File resultado = null;
		
		try {
			// Creamos un reader para el documento
			PdfReader reader = null;

			Document document = null;
			PdfWriter writer = null;
			boolean primero = true;// Indica si es el primer fichero (sobre el
									// que se concatenarán el resto de ficheros)

			int nPagActual = 0;
			ArrayList<String[]> marcas = new ArrayList<String[]>();
			int contadorDocumentos = 0;
			FileOutputStream resultadoFO = null;
			if (inputStreamNotificaciones.size() != 0) {
				Iterator inputStreamNotificacionesIterator = inputStreamNotificaciones
						.iterator();
				while (inputStreamNotificacionesIterator.hasNext()) {
					String[] documento = (String[]) inputStreamNotificacionesIterator
							.next();
					FileInputStream file = null;
					String rutaOriginal = "";
					if (!documento[7].toUpperCase().equals("PDF")) {
						rutaOriginal = DocumentosUtil.getFile(cct,	documento[0], null, documento[7]).getAbsolutePath();
						String docFilePath = DocumentConverter.convert2PDF(cct
								.getAPI(), documento[0], documento[7]);
						File archivo = new File(docFilePath);
						file = new FileInputStream(archivo);
					} else {
						rutaOriginal = DocumentosUtil.getFile(cct,	documento[0], null, documento[7]).getAbsolutePath();
						file = new FileInputStream(DocumentosUtil.getFile(cct, documento[0], null, documento[7]));
					}
					reader = new PdfReader((InputStream) file);

					reader.consolidateNamedDestinations();

					if (primero) {
						primero = false;
						// Creamos un objeto Document
						document = new Document(reader
								.getPageSizeWithRotation(1));

						document.setMargins(100, document.rightMargin(),
								document.topMargin(), document.bottomMargin());
						Font fuentePagina = new Font(Font.TIMES_ROMAN);
						fuentePagina.setStyle(Font.BOLD);
						fuentePagina.setSize(13);
						fuentePagina.setColor(255, 255, 255);

						HeaderFooter nPaginaFooter = new HeaderFooter(
								new Phrase("", fuentePagina), true);
						nPaginaFooter.setAlignment(Element.ALIGN_RIGHT);
						nPaginaFooter.setBorder(0);
						nPaginaFooter.setRight(0);
						nPaginaFooter.setBottom(0);
						nPaginaFooter.setPageNumber(0);

						// Como es la primera página añadimos la portada
						resultado = new File(filePathNotificaciones);
						resultadoFO = new FileOutputStream(resultado, true);

						writer = PdfWriter.getInstance(document, resultadoFO);
						writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);

						document.open();

						document.setFooter(nPaginaFooter);
						document.setPageCount(0);
						// Añadimos el índice
						nPagActual = añadeIndice(inputStreamNotificaciones,document, writer, genDocAPI, marcas, cct);
					}

					// Concatenamos los documentos
					nPagActual = añadeDocumento(reader, document, writer,nPagActual, rutaOriginal, (contadorDocumentos + 1)+"-" + documento[2] + "." + 
							documento[7],null);

					contadorDocumentos++;
					reader.close();

					file.close();
				}// while

				// Añadimos los marcadores
				List<HashMap<String, String>> newBookmarks = new ArrayList<HashMap<String, String>>();
				for (int i = 0; i < marcas.size(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("Title", "" + marcas.get(i)[0]);
					map.put("Action", "GoTo");
					map.put("Page", marcas.get(i)[1]);
					newBookmarks.add(i, map);
				}
			} else {
				document = new Document(PageSize.A4, 50, 50, 100, 32);
				// Si no hay documentos creamos uno indicándolo
				resultado = new File(filePathNotificaciones);
				resultadoFO = new FileOutputStream(resultado);
				writer = PdfWriter.getInstance(document, resultadoFO);

				document.open();

				nPagActual = añadeIndice(inputStreamNotificaciones, document,
						writer, genDocAPI, marcas, cct);
			}

			writer.close();
			document.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return resultado;
	}
	
	private int añadeDocumento(PdfReader reader, Document document,
			PdfWriter writer, int nPagActual, String rutaOriginal,
			String nombreDocumento, Anchor referenciaIndice)
			throws ISPACException, DocumentException, MalformedURLException,
			IOException {

		PdfImportedPage page;

		Rectangle rectangulo = document.getPageSize();

		int n = reader.getNumberOfPages();

		for (int i = 0; i < n;) {
			i++;
			document.newPage();

			nPagActual++;

			page = writer.getImportedPage(reader, i);
			Image imagen = Image.getInstance(page);

			imagen.setAbsolutePosition(50, 70);
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

		PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer,
				rutaOriginal, nombreDocumento, null);
		if (pfs != null)
			writer.addFileAttachment("", pfs);
		return nPagActual;
	}
	
	@SuppressWarnings("rawtypes")
	private int añadeIndice(ArrayList<String[]> inputStreamNotificaciones,
			Document document, PdfWriter writer, IGenDocAPI genDocAPI,
			ArrayList<String[]> marcas, ClientContext cct)
			throws ISPACException, DocumentException, MalformedURLException,
			IOException {

		String[] marca = new String[2];

		int nDocumento = 0;

		int nPaginaDocumentos = 0;
		Iterator inputStreamNotificacionesIterator = inputStreamNotificaciones
				.iterator();
		while (inputStreamNotificacionesIterator.hasNext()) {
			nDocumento++;
			
			String[] documento = (String[]) inputStreamNotificacionesIterator.next();

			FileInputStream file = null;
			if (!documento[7].toUpperCase().equals("PDF")) {
				String docFilePath = DocumentConverter.convert2PDF(cct.getAPI(), documento[0], documento[7]);
				File archivo = new File(docFilePath);
				file = new FileInputStream(archivo);
			} else
				file = new FileInputStream(DocumentosUtil.getFile(cct, documento[0], null, documento[7]));

			PdfReader reader = new PdfReader((InputStream) file);

			reader.consolidateNamedDestinations();
			int n = reader.getNumberOfPages();

			marca = new String[2];
			marca[0] = "Documento " + nDocumento+ " " + documento[2];
			marca[1] = "" + (nPaginaDocumentos + 1);
			marcas.add(marca);
			nPaginaDocumentos += n;
		}
		return 0;
	}

	
	@SuppressWarnings("rawtypes")
	private String getConsultaDocumentos(IEntitiesAPI entitiesAPI, IRuleContext rulectx)
			throws ISPACException {

		String consultaDocumentos = "";
		String consultaDecre = "";
		String id = "";

		String STR_queryDocumentos = "ID_FASE_PCD = "+rulectx.getStageProcedureId()+" AND ESTADOFIRMA = '02'";
		IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), STR_queryDocumentos, "FFIRMA ASC");
		
		Iterator docNotificaciones;
		if (documentsCollection.next()) {
			docNotificaciones = documentsCollection.iterator();
			while (docNotificaciones.hasNext()) {
				id += ((IItem) docNotificaciones.next()).getString("ID");
				if (docNotificaciones.hasNext())
					id += ",";
			}
		}
		if (!id.equals("")) {
			if (!consultaDecre.equals(""))
				consultaDecre += " OR (ID IN (" + id + "))";
			else
				consultaDecre = " (ID IN (" + id + "))";
		}

		if (!consultaDecre.equals("")) {

			if (consultaDecre.equals(""))
				consultaDocumentos += "";
			else
				consultaDocumentos += "(" + consultaDecre + ")";

			consultaDocumentos += " ORDER BY FFIRMA";

			consultaDocumentos = " WHERE " + consultaDocumentos;
		} else
			consultaDocumentos = " WHERE 1=2 ";

		return consultaDocumentos;
	}
	
	@SuppressWarnings("rawtypes")
	private void getArrayInfoPagsRDE(IItemCollection documentsCollection,
			ArrayList<String[]> arrayInfoPagsRDE) throws ISPACException {

		if (documentsCollection != null && documentsCollection.next()) {
			// Creamos el nuevo documento
			Iterator documentos = documentsCollection.iterator();

			while (documentos.hasNext()) {
				IItem documento = (IItem) documentos.next();
				String infoPagRDE = documento.getString("INFOPAG_RDE");
				String extension = documento.getString("EXTENSION_RDE");
				if (infoPagRDE == null || infoPagRDE.equals("")) {
					infoPagRDE = documento.getString("INFOPAG");
					extension = documento.getString("EXTENSION");
				}
				String numExp = documento.getString("NUMEXP");
				String nombreDocumento = documento.getString("NOMBRE");
				String fechaDocumento = documento.getString("FFIRMA");
				String tipoRegistro = documento.getString("TP_REG");
				String nreg = documento.getString("NREG");
				String freg = documento.getString("FREG");
				if (tipoRegistro == null
						|| tipoRegistro.toUpperCase().equals("NINGUNO"))
					tipoRegistro = "";
				if (nreg == null) {
					nreg = "";
					freg = "";
				}
				String[] arrayDocumento = { infoPagRDE, numExp,
						normalizar(nombreDocumento), fechaDocumento, tipoRegistro, nreg,
						freg, extension };
				arrayInfoPagsRDE.add(arrayDocumento);
			}
		}
	}
	
	private static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}


	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		boolean resultado = true;
		return resultado;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean resultado = true;
		return resultado;
	}

}
