package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
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
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.lowagie.text.Anchor;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
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
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class CopyOfExtraerTodosValAnomDespInfContPdfRule   implements IRule{
	
	private static final Logger logger = Logger.getLogger(CopyOfExtraerTodosValAnomDespInfContPdfRule.class);
	
	protected boolean conExpRelacionadosHijos = false;
	protected boolean conExpRelacionadosPadres = false;
	protected boolean muestraPortada = false;
	protected boolean muestracontraPortada = false;
	protected boolean muestraIndice = true;
	protected String expedienteOrigen = "";
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();      
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();		
	        IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        logger.warn("INICIO ExtraerTodosValAnomDespInfContPdfRule");
	        
	        expedienteOrigen = rulectx.getNumExp();
	        
			ArrayList<String[]> arrayInfoPagsRDE = new ArrayList<String[]>();
			ArrayList<String> expedientes = new ArrayList<String>();
			
	        //Obtenemos la lista de los números de expediente relacionados (hijos) con el expediente actual
			expedientes.add(expedienteOrigen);
	        
	        //Recuperamos todos los documentos firmados de los expedientes relacionandos, ordenados por fecha
	        
	        if (expedientes != null && expedientes.size() > 0) {

				ResultSet documentsCollection = obtenerInformeContratacion(cct, rulectx);
				getArrayInfoPagsRDE(documentsCollection, arrayInfoPagsRDE);

				FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();
				File documentoResumen = ftMgr.newFile(".pdf");

				// Creamos el documento
				documentoResumen = generarPdf(arrayInfoPagsRDE, documentoResumen.getPath(), genDocAPI, expedienteOrigen, entitiesAPI, cct);

				// Obtenemos el tipo de documento
				// Obtenemos el id del tipo de documento de
				// "Contenido de la propuesta" para
				// ponerle el nombre del documento al zip
				String strQuery = "WHERE UPPER(NOMBRE) LIKE '%INFORME DE VALORES ANORMALES O DESPROPORCIONADOS%'";
				IItemCollection typesDocumentCollection = entitiesAPI
						.queryEntities(SpacEntities.SPAC_CT_TPDOC, strQuery);
				int idTypeDocument = 0;
				if (typesDocumentCollection != null
						&& typesDocumentCollection.next()) {
					idTypeDocument = ((IItem) typesDocumentCollection
							.iterator().next()).getInt("ID");
				}
				if (idTypeDocument == 0) {
					throw new ISPACInfo(
							"Error al obtener el tipo de documento.");
				}

				String sExtension = "pdf";
				String sName = "Expediente: " + expedienteOrigen;// Campo
																		// descripción
																		// del
																		// documento
				String sMimeType = MimetypeMapping.getMimeType(sExtension);
				if (sMimeType == null) {
					throw new ISPACInfo("Error al obtener el tipo Mime");
				}
				int taskId = rulectx.getTaskId();

				IItem entityDocument = genDocAPI.createTaskDocument(taskId,
						idTypeDocument);
				int keyId = entityDocument.getKeyInt();
				// Establecer la extensión del documento para mostrar
				// un icono descriptivo del tipo de documento en la lista de
				// documentos
				entityDocument.set("NOMBRE", "Informe de Valores Anormales o Desproporcionados");
				entityDocument.set("EXTENSION", sExtension);
				entityDocument.store(cct);

				InputStream in = new FileInputStream(documentoResumen);
				int length = (int) documentoResumen.length();
				Object connectorSession = null;
				try {
					connectorSession = genDocAPI.createConnectorSession();
					genDocAPI.attachTaskInputStream(connectorSession,
							taskId, keyId, in, length, sMimeType, sName);
				} finally {
					if (connectorSession != null) {
						genDocAPI.closeConnectorSession(connectorSession);
					}
				}
				documentoResumen.delete();
			}
			logger.warn("FIN ExtraerTodosValAnomDespInfContPdfRule");     
        } catch (ISPACException e) {		
        	logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return null;
	}

	private ResultSet obtenerInformeContratacion(ClientContext cct, IRuleContext rulectx) throws ISPACRuleException {
		ResultSet pertenece = null;

		try {
			String consulta = "select * from spac_dt_documentos where numexp IN " +
					"	(select numexp_hijo from spac_exp_relacionados where numexp_padre IN " +
					"		(select NUMEXP_HIJO from spac_exp_relacionados where NUMEXP_PADRE='"+rulectx.getNumExp()+"' AND RELACION='Plica' ORDER BY ID ASC) " +
					"	AND relacion='Val. anormales o despropor') " +
					"AND nombre LIKE '%Informe de Contratación%'";
			pertenece = cct.getConnection().executeQuery(consulta).getResultSet();
			
		} catch (ISPACRuleException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		
		
		
		return pertenece;
	}

	private void getArrayInfoPagsRDE(ResultSet documentsCollection,
			ArrayList<String[]> arrayInfoPagsRDE) throws ISPACException, SQLException {

		if (documentsCollection != null) {

			while (documentsCollection.next()) {
				String infoPagRDE = documentsCollection.getString("INFOPAG_RDE");
				String extension = documentsCollection.getString("EXTENSION_RDE");
				if (infoPagRDE == null || infoPagRDE.equals("")) {
					infoPagRDE = documentsCollection.getString("INFOPAG");
					extension = documentsCollection.getString("EXTENSION");
				}
				String numExp = documentsCollection.getString("NUMEXP");
				String nombreDocumento = documentsCollection.getString("NOMBRE");
				String descripcionDocumento = documentsCollection.getString("DESCRIPCION");
				String fechaDocumento = documentsCollection.getString("FFIRMA");
				String tipoRegistro = documentsCollection.getString("TP_REG");
				String nreg = documentsCollection.getString("NREG");
				String freg = documentsCollection.getString("FREG");
				if (tipoRegistro == null
						|| tipoRegistro.toUpperCase().equals("NINGUNO"))
					tipoRegistro = "";
				if (nreg == null) {
					nreg = "";
					freg = "";
				}
				String[] arrayDocumento = { infoPagRDE, numExp,
						normalizar(nombreDocumento), fechaDocumento, tipoRegistro, nreg,
						freg, extension, descripcionDocumento};
				arrayInfoPagsRDE.add(arrayDocumento);
			}
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		expedienteOrigen = rulectx.getNumExp();
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	private File generarPdf(ArrayList<String[]> inputStreamNotificaciones,
			String filePathNotificaciones, IGenDocAPI genDocAPI,
			String numExpPadre, IEntitiesAPI entitiesAPI, ClientContext cct) throws ISPACRuleException {

		File resultado = null;

		try {
			// Creamos un reader para el documento
			PdfReader reader = null;

			Document document = null;
			PdfWriter writer = null;
			boolean primero = true;// Indica si es el primer fichero (sobre el
									// que se concatenarán el resto de ficheros)

			int nPagActual = 1;
			ArrayList<String[]> marcas = new ArrayList<String[]>();
			int contadorDocumentos = 0;
			FileOutputStream resultadoFO = null;
			if (inputStreamNotificaciones.size() != 0) {
				Iterator<String[]> inputStreamNotificacionesIterator = inputStreamNotificaciones.iterator();
				while (inputStreamNotificacionesIterator.hasNext()) {
					String[] documento = (String[]) inputStreamNotificacionesIterator.next();
					FileInputStream file = null;
					File archivo = null;
					File getFile = null;
					String rutaOriginal = "";
					if (!documento[7].toUpperCase().equals("PDF")) {
						getFile = DocumentosUtil.getFile(cct,documento[0], null, null);
						rutaOriginal = getFile.getAbsolutePath();
						String docFilePath = DocumentConverter.convert2PDF(cct
								.getAPI(), documento[0], documento[7]);
						archivo = new File(docFilePath);
						file = new FileInputStream(archivo);						
					} else {
						archivo = DocumentosUtil.getFile(cct,documento[0], null, null);
						rutaOriginal = archivo.getAbsolutePath();
						file = new FileInputStream(archivo);
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
						
						logger.warn("TCG npagactual al entrar: "+nPagActual);
						if(muestraPortada){
							nPagActual = añadePortada(numExpPadre, document, entitiesAPI, nPagActual);
							logger.warn("TCG npagactual: "+nPagActual);
						}
						logger.warn("TCG npagactual al salir: "+nPagActual);
						
						// Añadimos el índice
						if(muestraIndice){
							nPagActual = añadeIndice(inputStreamNotificaciones, document, writer, genDocAPI, marcas, cct, nPagActual);
						}
						logger.warn("TCG npagactual tras el índice: "+nPagActual);
						document.setFooter(nPaginaFooter);
						document.setPageCount(0);
					}
					
					//Descripción del documento
					nPagActual = añadeDocumento(reader, document, writer,
							nPagActual, rutaOriginal, ((contadorDocumentos + 1)<10?"0"+(contadorDocumentos + 1):(contadorDocumentos + 1))
									+ "-" + documento[8] + "." + documento[7],
							null, documento[2]);

					contadorDocumentos++;
					reader.close();
									
					if(archivo != null)
						archivo.delete();
					if(getFile != null) 
						getFile.delete();
					file.close();
				}// while
				
				logger.warn("TCG npagina antes de entrar: "+nPagActual);
				
				if(muestracontraPortada){
					logger.warn("TCG entramos en la contraportada");
					nPagActual = añadeContraPortada(document, nPagActual);
				}
				else {
					document.resetFooter();
					document.newPage();
				}
				
				logger.warn("TCG npagina antes al salir: "+nPagActual);

				// Añadimos los marcadores
				List<HashMap<String, String>> newBookmarks = new ArrayList<HashMap<String, String>>();
				for (int i = 0; i < marcas.size(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("Title", "" + marcas.get(i)[0]);
					map.put("Action", "GoTo");
					map.put("Page", marcas.get(i)[1]);
					newBookmarks.add(i, map);
				}

				PdfDictionary top = new PdfDictionary();
				PdfIndirectReference topRef = writer.getPdfIndirectReference();
				Object[] kids = SimpleBookmark.iterateOutlines(writer, topRef,
						newBookmarks, false);
				top.put(PdfName.FIRST, (PdfIndirectReference) kids[0]);
				top.put(PdfName.LAST, (PdfIndirectReference) kids[1]);
				top.put(PdfName.COUNT, new PdfNumber(((Integer) kids[2])
						.intValue()));
				writer.addToBody(top, topRef);
				writer.getExtraCatalog().put(PdfName.OUTLINES, topRef);

			} else {
				document = new Document(PageSize.A4, 50, 50, 100, 32);
				// Si no hay documentos creamos uno indicándolo
				resultado = new File(filePathNotificaciones);
				resultadoFO = new FileOutputStream(resultado);
				writer = PdfWriter.getInstance(document, resultadoFO);

				document.open();
				// Como es la primera página añadimos la portada
				if(muestraPortada){
					nPagActual = añadePortada(numExpPadre, document, entitiesAPI, nPagActual);
				}
				// Añadimos el índice
				if(muestraIndice){
					nPagActual = añadeIndice(inputStreamNotificaciones, document,
							writer, genDocAPI, marcas, cct, nPagActual);
				}
				
				if(muestracontraPortada){
					nPagActual = añadeContraPortada(document, nPagActual);
				}
			}

			writer.close();
			document.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return resultado;
	}

	private int añadeDocumento(PdfReader reader, Document document,
			PdfWriter writer, int nPagActual, String rutaOriginal,
			String nombreDocumento, Anchor referenciaIndice, String descripcionAdjunto)
			throws ISPACException, DocumentException, MalformedURLException,
			IOException {

		PdfImportedPage page;

		Rectangle rectangulo = document.getPageSize();

		int n = reader.getNumberOfPages();

		for (int i = 0; i < n;) {
			i++;
			document.newPage();
			
			// Añadimos el pie de página de la diputación
			Image imagen = Image
					.getInstance("/sigem/app/SIGEM/temp/reports/images/logoPie.jpg");
			imagen.setAbsolutePosition(40, 25);
			imagen.scalePercent(78);
			document.add(imagen);

			if(muestraIndice)
				document.add(new Chunk(" ").setLocalDestination("" + nPagActual));
			logger.warn("TCG localdestination: "+nPagActual+", documento: "+nombreDocumento);

			page = writer.getImportedPage(reader, i);
			imagen = Image.getInstance(page);

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
			nPagActual++;
		}

		PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer,
				rutaOriginal, nombreDocumento, null);
		if (pfs != null)
			writer.addFileAttachment(descripcionAdjunto, pfs);
		return nPagActual;
	}

	private int añadeIndice(ArrayList<String[]> inputStreamNotificaciones,
			Document document, PdfWriter writer, IGenDocAPI genDocAPI,
			ArrayList<String[]> marcas, ClientContext cct, int nPaginaActual)
			throws ISPACException, DocumentException, MalformedURLException,
			IOException {

		int documentosPorPagina = 13;

		String[] marca = new String[2];
		marca[0] = "Índice";
		marca[1] = ""+(nPaginaActual);
//		marca[1] = "1";
		marcas.add(marca);
				
		PdfReader reader = null;
		int nPagina =  nPaginaActual;
		nPaginaActual++;
		int nPaginaDocumentos = nPaginaActual;		
		int nPaginaDocumentosImprimir = 1;
		String texto = "";
		Phrase frase = null;
		Paragraph parrafo = null;
		Cell celda = null;

		// Fuentes de los textos
		Font fuente = new Font(Font.TIMES_ROMAN);
		fuente.setStyle(Font.BOLD);
		fuente.setSize(30);

		Font fuente2 = new Font(Font.TIMES_ROMAN);
		fuente2.setStyle(Font.BOLD);
		fuente2.setSize(13);

		Font fuenteNoIncluir = new Font(Font.TIMES_ROMAN);
		fuenteNoIncluir.setStyle(Font.BOLD);
		fuenteNoIncluir.setSize(20);
		
		Font fuente3 = new Font(Font.TIMES_ROMAN);
		fuente3.setStyle(Font.NORMAL);
		fuente3.setSize(13);

		// TCG Añadimos la Información del documento

		document.newPage();
		añadeImagenes(document);

		if (inputStreamNotificaciones.size() != 0) {

			nPagina++;

			// Añadimos el cuerpo frase a frase
			parrafo = new Paragraph();
			parrafo.setAlignment(Element.ALIGN_CENTER);
			parrafo.setExtraParagraphSpace(5);
			parrafo.setSpacingAfter(100);

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
			Table tabla = inicializaTabla(fuente2);
			
			tabla.setCellsFitPage(true);
			
			// Creamos el índice
			// Creamos un contador, cada x documentos insertamos una nueva
			// página
			int contador = 0, nDocumento = 0;
			texto = "";
			nPaginaDocumentos += (int) inputStreamNotificaciones.size()
					/ documentosPorPagina;
			Iterator<String[]> inputStreamNotificacionesIterator = inputStreamNotificaciones
					.iterator();
			while (inputStreamNotificacionesIterator.hasNext()) {
				nDocumento++;
				if (contador == documentosPorPagina) {
					parrafo.add(tabla);
					document.add(parrafo);

					document.newPage();

					añadeImagenes(document);

					nPagina++;
					parrafo = new Paragraph();
					parrafo.add(new Phrase("\n"));
					document.add(parrafo);
					document.add(parrafo);
					document.add(parrafo);
					document.add(parrafo);
					document.add(parrafo);
					document.add(parrafo);
					
					tabla = inicializaTabla(fuente2);			

					parrafo = new Paragraph();
					parrafo.setAlignment(Element.ALIGN_CENTER);
					parrafo.setSpacingAfter(50);
					parrafo.setExtraParagraphSpace(5);
					contador = 0;
				}
				/**
				 * [0]-> infoPagRDE
				 * [1]-> numExp
				 * [2]-> normalizar(nombreDocumento)
				 * [3]-> fechaDocumento
				 * [4]-> tipoRegistro
				 * [5]-> nreg
				 * [6]-> freg
				 * [7]-> extension
				 * [8]-> descripcionDocumento};
				 * **/
				String[] documento = (String[]) inputStreamNotificacionesIterator
						.next();

				FileInputStream file = null;
				File archivo = null;
				if (!documento[7].toUpperCase().equals("PDF")) {
					String docFilePath = DocumentConverter.convert2PDF(cct
							.getAPI(), documento[0], documento[7]);
					archivo = new File(docFilePath);
					file = new FileInputStream(archivo);
				} else{
					archivo = DocumentosUtil.getFile(cct, documento[0], null, null);
					file = new FileInputStream(archivo);
				}

				reader = new PdfReader((InputStream) file);

				reader.consolidateNamedDestinations();
				int n = reader.getNumberOfPages();
				
				String nombreDoc = nombreDoc(cct, documento[1]);

				//Descripción del documento
				//texto = (nDocumento<10?"0"+nDocumento:nDocumento) + " " + documento[8];
				texto = (nDocumento<10?"0"+nDocumento:nDocumento) + " " + documento[8]+ "-" +nombreDoc;
				
				Font fuente6 = new Font(Font.TIMES_ROMAN);
				fuente6.setStyle(Font.NORMAL);
				fuente6.setSize(10);
				tabla.setBorder(0);
				frase = new Phrase(texto, fuente6);

				
				celda = new Cell(frase);
				celda.setBorder(0);
				celda.setWidth("88%");
				tabla.addCell(celda);				

				texto = documento[3];
				frase = new Phrase(texto, fuente3);
				celda = new Cell(frase);
				celda.setBorder(0);
				celda.setWidth("10%");
				tabla.addCell(celda);

				texto = "" + n;
				frase = new Phrase(texto, fuente3);
				celda = new Cell(frase);
				celda.setBorder(0);
				celda.setWidth("1%");
				tabla.addCell(celda);

				//quitamos 1 página al nº de página que se imprime ya que no contamos el índice
				if(muestraPortada){
					texto = "" + nPaginaDocumentosImprimir;
				}else{
					texto = "" + nPaginaDocumentosImprimir;
				}
				
				frase = new Phrase(texto, fuente3);
				celda = new Cell(new Paragraph(new Chunk(texto, fuente3)
						.setLocalGoto("" + nPaginaDocumentos)));
				logger.warn("TCG localgoto: "+nPaginaDocumentos+", documento: "+documento[8]+ "-" +nombreDoc);

				celda.setBorder(0);
				celda.setWidth("1%");
				tabla.addCell(celda);

				marca = new String[2];
				//Nombre del documento				
				//marca[0] = "Documento " + nDocumento+ " " + documento[2];
				
				//Descripción del documento
				//marca[0] = "Documento " + (nDocumento<10?"0"+nDocumento:nDocumento) + " " + documento[8];
				marca[0] = "Documento " + (nDocumento<10?"0"+nDocumento:nDocumento) + " " + documento[8]+ "-" +nombreDoc;
				marca[1] = "" + (nPaginaDocumentos);
				marcas.add(marca);
				
				logger.warn("TCG marca[0]: "+(nPaginaDocumentos)+",  "+marca[0]);
				nPaginaDocumentos += n;				
				nPaginaDocumentosImprimir += n;
				
				contador++;
				
				if(archivo != null)
					archivo.delete();

			}
			parrafo.add(tabla);
			document.add(parrafo);
		} else {
			nPagina++;
			texto = "\n";
			texto += "\n";
			texto += "\n";
			texto += "\n";
			texto += "El expediente no tiene documentos a incluir";
			frase = new Phrase(texto, fuenteNoIncluir);
			parrafo = new Paragraph();
			parrafo.add(frase);
			document.add(parrafo);
		}
		return nPagina;
	}

	private String nombreDoc(ClientContext cct, String numexp) throws ISPACRuleException {
		String nombreTitular = "";
		String consulta = "select identidadtitular from spac_expedientes where numexp in " +
				"(select numexp_padre from spac_exp_relacionados where numexp_hijo='"+numexp+"') and fcierre is null";
		logger.warn("[TCG]consulta "+consulta);
		try {
			ResultSet titular = cct.getConnection().executeQuery(consulta).getResultSet();
			logger.warn("[TCG]titular "+titular);
			if(titular != null){
				while(titular.next()){
					nombreTitular = titular.getString("identidadtitular");
				}
			}
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return nombreTitular;
	}

	private Table inicializaTabla (Font fuente2) throws ISPACRuleException {
		Table tabla = null;
		try {			
			tabla = new Table(4);
			tabla.setBorder(0);
			tabla.setWidth(120);
			
			Phrase frase = new Phrase("Nombre Documento", fuente2);
			Cell celda;
			celda = new Cell(frase);
			celda.setBorder(0);
			celda.setWidth("88%");
			tabla.addCell(celda);
	
			frase = new Phrase("Fecha Documento", fuente2);
			celda = new Cell(frase);
			celda.setBorder(0);
			celda.setWidth("10%");
			tabla.addCell(celda);
			tabla.endHeaders();
	
			frase = new Phrase("Nº de páginas", fuente2);
			celda = new Cell(frase);
			celda.setBorder(0);
			celda.setWidth("1%");
			tabla.addCell(celda);
			tabla.endHeaders();
	
			frase = new Phrase("Página", fuente2);
			celda = new Cell(frase);
			celda.setBorder(0);
			celda.setWidth("1%");
			tabla.addCell(celda);
			tabla.endHeaders();
				
			tabla.setBorder(0);
			
		} catch (BadElementException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return tabla;
	}
	private void añadeImagenes(Document document) throws MalformedURLException,
			IOException, DocumentException {
		// Añadimos el logotipo de la diputación
		Image imagen = Image
				.getInstance("/sigem/app/SIGEM/temp/reports/images/logoCabecera.gif");
		imagen.setAbsolutePosition(50, document.getPageSize().getHeight() - 100);
		imagen.scalePercent(50);
		document.add(imagen);
		// Añadimos la imagen del fondo
		imagen = Image
				.getInstance("/sigem/app/SIGEM/temp/reports/images/logodipuFondo.png");
		imagen.setAbsolutePosition(250, 50);
		imagen.scalePercent(70);
		document.add(imagen);

		// Añadimos el pie de página de la diputación
		imagen = Image
				.getInstance("/sigem/app/SIGEM/temp/reports/images/logoPie.jpg");
		imagen.setAbsolutePosition(40, 25);
		imagen.scalePercent(80);
		document.add(imagen);
	}

	/*private void añadeInfoDocumento(String[] documento, Document document,
			PdfWriter writer) throws ISPACException, DocumentException,
			IOException {

		// TCG Añadimos la Información del documento
		// {infoPagRDE,numExp,nombreDocumento,fechaDocumento,tipoRegistro,nreg}

		// Añadimos el logotipo de la diputación
		Image imagen = Image
				.getInstance("/sigem/app/SIGEM/temp/reports/images/logoCabecera.gif");
		imagen.setAbsolutePosition(50, document.getPageSize().height() - 100);
		imagen.scalePercent(50);
		document.add(imagen);
		// Añadimos la imagen del fondo
		imagen = Image
				.getInstance("/sigem/app/SIGEM/temp/reports/images/logodipuFondo.png");
		imagen.setAbsolutePosition(250, 50);
		imagen.scalePercent(70);
		document.add(imagen);
		// Añadimos el pie de página de la diputación
		imagen = Image
				.getInstance("/sigem/app/SIGEM/temp/reports/images/logoPie.jpg");
		imagen.setAbsolutePosition(30, 25);
		imagen.scalePercent(80);
		document.add(imagen);

		// Fuentes de los textos
		Font fuente = new Font(Font.TIMES_ROMAN);
		fuente.setStyle(Font.BOLD);
		Font fuente2 = new Font(Font.TIMES_ROMAN);
		fuente2.setStyle(Font.NORMAL);

		// Añadimos el cuerpo frase a frase
		Paragraph parrafo = new Paragraph();
		parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
		parrafo.setExtraParagraphSpace(5);
		parrafo.setSpacingAfter(100);

		String texto = "";
		texto += "\n";
		texto += "\n";
		texto += "\n";
		texto += "\n";
		texto += "\t\t\t\t\t\t" + "Número de expediente: ";
		Phrase frase = new Phrase(texto, fuente2);
		parrafo.add(frase);

		texto = documento[1];
		frase = new Phrase(texto, fuente);
		parrafo.add(frase);

		texto = "\n";
		texto += "\t\t\t\t\t\t" + "Nombre del documento: ";
		frase = new Phrase(texto, fuente2);
		parrafo.add(frase);

		//Nombre del documento
//		texto = documento[2];
//		frase = new Phrase(texto, fuente);
//		parrafo.add(frase);
		
		//Descripción del documento
		texto = documento[8];
		frase = new Phrase(texto, fuente);
		parrafo.add(frase);

		texto = "\n";
		texto += "\t\t\t\t\t\t" + "Fecha de creación: ";
		frase = new Phrase(texto, fuente2);
		parrafo.add(frase);

		texto = documento[3];
		frase = new Phrase(texto, fuente);
		parrafo.add(frase);

		texto = "\n";
		if (!documento[4].equals("")) {
			texto += "\t\t\t\t\t\t" + "Tipo de registro: ";
			frase = new Phrase(texto, fuente2);
			parrafo.add(frase);
			texto = documento[4];
			frase = new Phrase(texto, fuente);
			parrafo.add(frase);
			texto = "\n";
		}
		if (!documento[5].equals("")) {
			texto += "\t\t\t\t\t\t" + "Número de registro: ";
			frase = new Phrase(texto, fuente2);
			parrafo.add(frase);
			texto = documento[5];
			frase = new Phrase(texto, fuente);
			parrafo.add(frase);

			texto = "\n\n\n\n" + "Fecha del registro:";
			frase = new Phrase(texto, fuente2);
			parrafo.add(frase);
			texto = documento[6];
			frase = new Phrase(texto, fuente);
			parrafo.add(frase);
		}

		document.add(parrafo);
	}*/

	@SuppressWarnings("unchecked")
	private int añadePortada(String numExpPadre, Document document,
			IEntitiesAPI entitiesAPI, int pagActual) throws ISPACException, DocumentException,
			MalformedURLException, IOException {

		IItemCollection iColExpediente = entitiesAPI.getEntities(
				"SPAC_EXPEDIENTES", numExpPadre);
		String cadena = numExpPadre;
		if (iColExpediente != null && iColExpediente.next()) {
			Iterator<IItem> itExpediente = iColExpediente.iterator();
			IItem expediente = (IItem) itExpediente.next();
			if (expediente.getString("ASUNTO") != null
					&& !expediente.getString("ASUNTO").equals(""))
				cadena = expediente.getString("ASUNTO");
		}

		document.newPage();
		pagActual++;
		// Añadimos el fondo
		Image imagenFondo = Image
				.getInstance("/sigem/app/SIGEM/temp/reports/images/portada2.png");
		imagenFondo.scaleAbsolute(document.getPageSize().getWidth(), document
				.getPageSize().getHeight());
		imagenFondo.setAbsolutePosition(0, 0);
		document.add(imagenFondo);

		// Añadimos el logotipo de la diputación
		Image imagen = Image
				.getInstance("/sigem/app/SIGEM/temp/reports/images/logo2.png");
		imagen.setAbsolutePosition(50, 250);
		imagen.scalePercent(50);
		document.add(imagen);

		// Fuentes de los textos
		Font fuente = new Font(Font.TIMES_ROMAN);
		fuente.setStyle(Font.BOLD);
		Color color = new Color(153, 0, 0, 240);
		fuente.setColor(color);
		fuente.setSize(15);

		// Añadimos el cuerpo frase a frase
		Paragraph parrafo = new Paragraph();
		parrafo.setAlignment(Element.ALIGN_JUSTIFIED);
		parrafo.setExtraParagraphSpace(50);
		parrafo.setSpacingAfter(250);

		StringBuffer texto = new StringBuffer("");
		com.lowagie.text.Phrase frase = null;

		fuente.setSize(20);
		// texto = cadena;
		texto.append("\n\n\n\n\n\n\n");
		StringBuffer cadenaAux = new StringBuffer(cadena);
		StringBuffer cadena2 = new StringBuffer("");
		String[] cadenaSplit;
		while (cadenaAux.toString().length() > 20) {
			cadena2 = new StringBuffer("");
			cadenaSplit = cadenaAux.toString().split(" ");
			cadena2.append(cadenaSplit[0]);
			cadena2.append(" ");
			int j = 1;
			while (j < cadenaSplit.length && cadena2.toString().length() < 20) {
				cadena2.append(cadenaSplit[j]);
				cadena2.append(" ");
				j++;
			}
			texto
					.append("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
			texto.append(cadena2);
			texto.append("\n");
			cadenaAux = new StringBuffer("");
			;
			for (int i = j; i < cadenaSplit.length; i++) {
				cadenaAux.append(cadenaSplit[i]);
				cadenaAux.append(" ");
			}
		}
		texto.append("\n");
		texto
				.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
		texto.append(cadenaAux);
		texto.append("\n");

		frase = new Phrase(texto.toString(), fuente);
		parrafo.add(frase);
		document.add(parrafo);
		// Fin de la portada
		return pagActual;
	}

	private int añadeContraPortada(Document document, int pagActual) throws ISPACException,
			DocumentException, MalformedURLException, IOException {

		document.resetFooter();
		// Añadimos el fondo
		document.newPage();
		pagActual++;
		Image imagenFondo = Image
				.getInstance("/sigem/app/SIGEM/temp/reports/images/contraPortada.png");
		imagenFondo.scaleAbsolute(document.getPageSize().getWidth(), document
				.getPageSize().getHeight());
		imagenFondo.setAbsolutePosition(0, 0);
		// Quitamos el número de página

		document.add(imagenFondo);
		
		return pagActual;

		// Fin de la portada
	}
	
	private static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}}
