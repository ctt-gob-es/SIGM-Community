package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.awt.Color;
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
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipuCrGenerarDocumentoUnicoExpediente implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipuCrGenerarDocumentoUnicoExpediente.class);
	
	public OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
	public String entityId = info.getOrganizationId();
	
	public String dir = "/sigem/app/SIGEM/conf/SIGEM_TramitacionWeb/skinEntidad_" + entityId + "/img_exp_fol/";

	public final String imgContraportada = dir + "contraPortada.png";
	public final String imgFondo = dir + "fondo.png";
	public final String imgLogoCabecera = dir + "logoCabecera.gif";
	public final String imgLogoPortada = dir + "logoPortada.png";
	public final String imgPie = dir + "pie.jpg";
	public final String imgPortada =  dir + "portada.png";
	
	protected boolean conExpRelacionadosHijos = true;
	protected boolean conExpRelacionadosPadres = true;
	protected boolean muestraPortada = true;
	protected boolean muestracontraPortada = true;
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
	        
	        logger.info("INICIO - " + this.getClass());
	        
			ArrayList<String[]> arrayInfoPagsRDE = new ArrayList<String[]>();
			ArrayList<String> expedientes = new ArrayList<String>();
			
	        //Obtenemos la lista de los números de expediente relacionados (hijos) con el expediente actual
			expedientes.add(expedienteOrigen);
			if(conExpRelacionadosHijos)	        
				expedientes.addAll(ExpedientesRelacionadosUtil.getProcedimientosRelacionadosHijos(expedienteOrigen, entitiesAPI));
			if(conExpRelacionadosPadres)
				expedientes.addAll(ExpedientesRelacionadosUtil.getProcedimientosRelacionadosPadres(expedienteOrigen, entitiesAPI,expedientes));
	        
	        //Recuperamos todos los documentos firmados de los expedientes relacionandos, ordenados por fecha
	        
	        if (expedientes != null && expedientes.size() > 0) {
				IItem expedienteOriginal = ExpedientesUtil.getExpediente(cct, expedienteOrigen);

				String consultaSolicitudRegistro = getConsultaSolicitudRegistro(expedientes, entitiesAPI, expedienteOriginal);
				IItemCollection solicitudRegistroCollection = DocumentosUtil.queryDocumentos(cct, consultaSolicitudRegistro);

				getArrayInfoPagsRDE(solicitudRegistroCollection,arrayInfoPagsRDE);

				String consultaDocumentos = getConsultaDocumentos(expedientes, entitiesAPI, expedienteOriginal, rulectx);
				IItemCollection documentsCollection = DocumentosUtil.queryDocumentos(cct, consultaDocumentos);
				getArrayInfoPagsRDE(documentsCollection, arrayInfoPagsRDE);

				FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();
				File documentoResumen = ftMgr.newFile(".pdf");

				// Creamos el documento
				documentoResumen = generarPdf(arrayInfoPagsRDE, documentoResumen.getPath(), genDocAPI, expedienteOrigen, entitiesAPI, cct);

				int idTypeDocument = DocumentosUtil.getTipoDoc(cct, "EXPEDIENTE FOLIADO CON ÍNDICE", DocumentosUtil.BUSQUEDA_LIKE, true);

				String sName = "Expediente: " + expedienteOrigen;// Campo descripción del documento
				
				DocumentosUtil.generaYAnexaDocumento(rulectx, idTypeDocument, sName, documentoResumen, "pdf");

				documentoResumen.delete();
			}
			logger.info("FIN - " + this.getClass());     
        } catch (ISPACException e) {	
        	logger.error("Error al generar el documento único. Expediente: " + expedienteOrigen + ". " + e.getMessage(), e);
        }
		return null;
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
				String descripcionDocumento = documento.getString("DESCRIPCION");
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
						freg, extension, descripcionDocumento};
				arrayInfoPagsRDE.add(arrayDocumento);
			}
		}
	}

	private String getConsultaSolicitudRegistro(ArrayList<String> expedientes,
			IEntitiesAPI entitiesAPI, IItem expedienteOriginal)
			throws ISPACException {

		String consultaDocumentos = "";
		for (int i = 0; i < expedientes.size(); i++) {
			if (!consultaDocumentos.equals(""))
				consultaDocumentos += " OR (UPPER(NOMBRE) = 'SOLICITUD REGISTRO' AND NUMEXP = '"
						+ expedientes.get(i) + "')";
			else
				consultaDocumentos += " (UPPER(NOMBRE) = 'SOLICITUD REGISTRO' AND NUMEXP = '"
						+ expedientes.get(i) + "')";
		}
		if (!consultaDocumentos.equals(""))
			consultaDocumentos = " WHERE " + consultaDocumentos;
		else
			consultaDocumentos = " WHERE 1=1 ";
		return consultaDocumentos;
	}

	@SuppressWarnings("rawtypes")
	protected String getConsultaDocumentos(ArrayList<String> expedientes,
			IEntitiesAPI entitiesAPI, IItem expedienteOriginal, IRuleContext rulectx)
			throws ISPACException {

		String nombre = "";
		StringBuffer nombreSinDni = new StringBuffer("");
		String[] identidad = null;
		String nif = "";
		
		if (expedienteOriginal.getString("NREG") != null) {
		}
		if (expedienteOriginal.getString("IDENTIDADTITULAR") != null)
			nombre = expedienteOriginal.getString("IDENTIDADTITULAR");
		if (expedienteOriginal.getString("NIFCIFTITULAR") != null)
			nif = expedienteOriginal.getString("NIFCIFTITULAR");
		
		if (nif == null || nif.equals("")) {
			if (nombre != null && !nombre.equals("")) {
				identidad = nombre.split(" ");
				nombreSinDni.append(identidad[1]);

				for (int i = 2; i < identidad.length; i++) {
					nombreSinDni.append(" " + identidad[i]);
				}
			}
		} else {
			nombreSinDni = new StringBuffer(nombre);
		}

		String consultaDocumentos = "";
		String consultaDecre = "";
		String consultaSecre = "";
		String consultaElse = "";

		//Comprobamos si es de secretaría, si lo es, buscamos la propuesta
		String expedientePropuesta = getExpedientePropuesta(expedientes, entitiesAPI, rulectx);
		
		// Recorremos todos los expedientes en función del expediente u
		// obtenemos todos los firmados y las entradas
		// o únicamente los certificados y las notificaciones
		for (int i = 0; i < expedientes.size(); i++) {
			IItem itemProc = ExpedientesUtil.getExpediente(rulectx.getClientContext(), expedientes.get(i));
			int idProcedimiento = itemProc.getInt("ID_PCD");
			// Comprobamos si el expediente es de los que no hay que sacar
			// documentos

			// No se obtienen documentos de Propuestas y Comisiones
			if (esProcTipo(entitiesAPI, idProcedimiento, "DECRETO")
					&& !nombreSinDni.toString().equals("")) {
				// Es de tipo decreto recuperamos solo las notificaciones y los
				// acuses
				String id = "";
				String consultaDecretos = " (UPPER(DESCRIPCION) LIKE UPPER('NOTIFICA%"
						+ nombreSinDni.toString()
						+ "%') OR (UPPER(DESCRIPCION) LIKE UPPER('NOTIFICA%"
						+ nombreSinDni.toString()
						+ "%') AND (UPPER(EXTENSION) ='PDF' OR UPPER(EXTENSION_RDE) ='PDF'))) ";
				consultaDecretos += " OR (UPPER(DESCRIPCION) LIKE UPPER('ACUSE%"
						+ nombreSinDni.toString()
						+ "%') AND (UPPER(EXTENSION) ='PDF' OR UPPER(EXTENSION_RDE) ='PDF')) ";
				consultaDecretos += " OR (ESTADOFIRMA IN ('02','03') AND UPPER(DESCRIPCION) LIKE UPPER('%DECRETO%'))";
				consultaDecretos += " OR (TP_REG = 'ENTRADA' AND (UPPER(EXTENSION) = 'PDF' OR UPPER(EXTENSION_RDE) ='PDF'))";
				consultaDecretos += " OR (UPPER(NOMBRE) = 'DOCUMENTOS SELLADOS' OR UPPER(NOMBRE) = 'PARTICIPANTES COMPARECE')";

				IItemCollection icDoc = entitiesAPI.getDocuments(expedientes
						.get(i), consultaDecretos, "FFIRMA, FDOC DESC");
				Iterator docNotificaciones;
				if (icDoc.next()) {
					docNotificaciones = icDoc.iterator();
					while (docNotificaciones.hasNext()) {
						id += ((IItem) docNotificaciones.next())
								.getString("ID");
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
			} else if (esProcTipo(entitiesAPI, idProcedimiento, "SECRETAR")
					&& !expedientes.get(i).equals(expedientePropuesta)){
				String id = "";
				IItemCollection icDoc = entitiesAPI.getDocuments(expedientes
						.get(i), "DESCRIPCION LIKE '%"
						+ expedientePropuesta + "%'", "");
				Iterator docNotificaciones;
				if (icDoc.next()) {
					String tipoPropuesta="";
					String numeroPropuesta = "";
					String esUrgencia = "";
					String esUrgencia2 = "";
				
					docNotificaciones = icDoc.iterator();
					if (docNotificaciones.hasNext()) {
						String descripcion = ((IItem) docNotificaciones.next())
								.getString("DESCRIPCION");
						String[] vectorDescripcion = descripcion.split(" . ");
						if (vectorDescripcion.length > 1) {
							tipoPropuesta = vectorDescripcion[0];
							numeroPropuesta = vectorDescripcion[1];
						}
						if (tipoPropuesta.toUpperCase().equals("PROPUESTA URGENCIA")){
							esUrgencia = ".- Urgencia Certificado";
							esUrgencia2= "Urgencia";
						}
						else esUrgencia = ".-Certificado";
						
						String consulta = "((UPPER(DESCRIPCION) LIKE UPPER('"+numeroPropuesta+esUrgencia+"%')";
						
						if(!nombre.equals("")) consulta += "OR UPPER(DESCRIPCION) LIKE UPPER('"+numeroPropuesta+"%"+esUrgencia2+"%"+nombre+"%')";
						
						consulta += ") AND ((ESTADOFIRMA IN ('02','03')) ";
						consulta +=	" OR (TP_REG = 'ENTRADA' AND (UPPER(EXTENSION) = 'PDF' OR UPPER(EXTENSION_RDE) ='PDF'))";
						consulta += " OR (UPPER(NOMBRE) = 'DOCUMENTOS SELLADOS' OR UPPER(NOMBRE) = 'PARTICIPANTES COMPARECE')))";

						IItemCollection icDoc2 = entitiesAPI.getDocuments(expedientes.get(i), consulta, "FFIRMA, FDOC DESC");
						Iterator it2 = icDoc2.iterator();
						while (it2.hasNext()){
							id += ((IItem) it2.next()).getString("ID");
							if (it2.hasNext())
								id += ",";	
						}
					}
				}				
				if (!id.equals("")) {
					if (!consultaSecre.equals(""))
						consultaSecre += " OR (ID IN (" + id + "))";
					else
						consultaSecre += " (ID IN (" + id + "))";
				}
			} else {
				if (!consultaElse.equals("")) {
					consultaElse += " OR (NUMEXP = '" + expedientes.get(i)
							+ "' AND ((ESTADOFIRMA IN ('02','03') ";
					consultaElse += "OR (TP_REG = 'ENTRADA' AND (UPPER(EXTENSION) = 'PDF' OR UPPER(EXTENSION_RDE) ='PDF')))";
					consultaElse += " OR (UPPER(NOMBRE) = 'DOCUMENTOS SELLADOS' OR UPPER(NOMBRE) = 'PARTICIPANTES COMPARECE')))";
				} else {
					consultaElse += " (NUMEXP = '" + expedientes.get(i)
							+ "' AND ((ESTADOFIRMA IN ('02','03') ";
					consultaElse += "OR (TP_REG = 'ENTRADA' AND (UPPER(EXTENSION) = 'PDF' OR UPPER(EXTENSION_RDE) ='PDF')))";
					consultaElse += " OR (UPPER(NOMBRE) = 'DOCUMENTOS SELLADOS' OR UPPER(NOMBRE) = 'PARTICIPANTES COMPARECE')))";
				}
				logger.debug("consultaElse "+consultaElse);
			}
		}

		if (!consultaDecre.equals("") || !consultaSecre.equals("")
				|| !consultaElse.equals("")) {

			if (consultaDecre.equals(""))
				consultaDocumentos += "";
			else
				consultaDocumentos += "(" + consultaDecre + ")";

			if (consultaDocumentos.equals(""))
				if (consultaSecre.equals(""))
					consultaDocumentos += "";
				else
					consultaDocumentos += "(" + consultaSecre + ")";
			else if (consultaSecre.equals(""))
				consultaDocumentos += "";
			else
				consultaDocumentos += " OR (" + consultaSecre + ")";

			if (consultaDocumentos.equals(""))
				if (consultaElse.equals(""))
					consultaDocumentos += "";
				else
					consultaDocumentos += "(" + consultaElse + ")";
			else if (consultaElse.equals(""))
				consultaDocumentos += "";
			else
				consultaDocumentos += " OR (" + consultaElse + ")";

			consultaDocumentos += " ORDER BY FFIRMA, FAPROBACION, FDOC, DESCRIPCION";

			consultaDocumentos = " WHERE " + consultaDocumentos;
		} else
			consultaDocumentos = " WHERE 1=2 ";
			logger.debug("Consulta documentos: "+consultaDocumentos);
		return consultaDocumentos;
	}

	private String getExpedientePropuesta(ArrayList<String> expedientes, IEntitiesAPI entitiesAPI, IRuleContext rulectx) throws ISPACException {
		String resultado = "";
		String expedientePropuesta = "";
		for (int i =0; i<expedientes.size();i++){
			expedientePropuesta = expedientes.get(i);
			IItem itemProc = ExpedientesUtil.getExpediente(rulectx.getClientContext(), expedientes.get(i));
			String nombreProcedimiento = itemProc.getString("NOMBREPROCEDIMIENTO");
			if(nombreProcedimiento.toUpperCase().indexOf("PROPUESTA")>-1){
				resultado = expedientePropuesta;
			}			
		}
		return resultado;
	}

	private boolean esProcTipo(IEntitiesAPI entitiesAPI, int idProcedimiento, String tipo) {

		boolean resultado = false;
		try {
			IItem catalogo = entitiesAPI.getEntity(
					SpacEntities.SPAC_CT_PROCEDIMIENTOS, idProcedimiento);

			int id_padre = catalogo.getInt("ID_PADRE");
			if (id_padre != -1) {				
				resultado = esProcTipo(entitiesAPI, id_padre, tipo);
			} else {
				String nom_pcd = "";
				if (catalogo.getString("NOMBRE") != null){
					nom_pcd = catalogo.getString("NOMBRE");
					if (nom_pcd.toUpperCase().indexOf(tipo.toUpperCase()) >= 0) {
						resultado = true;					
					}
				}
			}
		} catch (ISPACException e) {
			logger.error("Error al comprobar si el procedimiento con id: " + idProcedimiento + " es de tipo : " + tipo + ". " + e.getMessage(), e);
		}
		return resultado;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		expedienteOrigen = rulectx.getNumExp();
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
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

			int nPagActual = 1;
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
					File archivo = null;
					File getFile = null;
					String rutaOriginal = "";
					if (!documento[7].toUpperCase().equals("PDF")) {
						getFile = DocumentosUtil.getFile(cct, documento[0], null, documento[7]);
						rutaOriginal = getFile.getAbsolutePath();
						String docFilePath = DocumentConverter.convert2PDF(cct
								.getAPI(), documento[0], documento[7]);
						archivo = new File(docFilePath);
						file = new FileInputStream(archivo);						
					} else {
						archivo = DocumentosUtil.getFile(cct, documento[0], null, documento[7]);
						rutaOriginal = archivo.getAbsolutePath();
						file = new FileInputStream(archivo);
					}
					reader = new PdfReader((InputStream) file);

					reader.consolidateNamedDestinations();

					if (primero) {
						primero = false;
						// Creamos un objeto Document
						document = new Document(reader.getPageSizeWithRotation(1));

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
						
						logger.debug("npagactual al entrar: "+nPagActual);
						if(muestraPortada){
							nPagActual = añadePortada(numExpPadre, document, entitiesAPI, nPagActual, cct);
							logger.debug("npagactual: "+nPagActual);
						}
						logger.debug("npagactual al salir: "+nPagActual);
						
						// Añadimos el índice
						if(muestraIndice){
							nPagActual = añadeIndice(inputStreamNotificaciones, document, writer, genDocAPI, marcas, cct, nPagActual);
						}
						logger.debug("npagactual tras el índice: "+nPagActual);
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
				
				logger.debug("npagina antes de entrar: "+nPagActual);
				
				if(muestracontraPortada){
					logger.debug("entramos en la contraportada");
					nPagActual = añadeContraPortada(document, nPagActual);
				}
				else {
					document.resetFooter();
					document.newPage();
				}
				
				logger.debug("npagina antes al salir: "+nPagActual);

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
				Object[] kids = SimpleBookmark.iterateOutlines(writer, topRef, newBookmarks, false);
				top.put(PdfName.FIRST, (PdfIndirectReference) kids[0]);
				top.put(PdfName.LAST, (PdfIndirectReference) kids[1]);
				top.put(PdfName.COUNT, new PdfNumber(((Integer) kids[2]).intValue()));
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
					nPagActual = añadePortada(numExpPadre, document, entitiesAPI, nPagActual, cct);
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
			logger.error("Error al generar el pdf único. Expediente: " + expedienteOrigen + ".  "+e.getMessage(), e);
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
			Image imagen = null;			
			try{
				imagen = Image.getInstance(imgPie);
				imagen.setAbsolutePosition(document.getPageSize().getWidth()-550, 15);
				imagen.setAbsolutePosition(40, 25);
				imagen.scalePercent(78);
				imagen.scalePercent(80);
				document.add(imagen);
			}
			catch(Exception e){
				logger.error("ERROR no se ha encontrado la imagen de pie de página: " + imgPie + ". " + e.getMessage(), e);
				throw new ISPACRuleException("ERROR no se ha encontrado la imagen de pie de página: " + imgPie + ". " + e.getMessage(), e);
			}

			if(muestraIndice)
				document.add(new Chunk(" ").setLocalDestination("" + nPagActual));
			logger.debug("localdestination: "+nPagActual+", documento: "+nombreDocumento);

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

		PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer, rutaOriginal, nombreDocumento, null);
		if (pfs != null)
			writer.addFileAttachment(descripcionAdjunto, pfs);
		return nPagActual;
	}

	@SuppressWarnings("rawtypes")
	private int añadeIndice(ArrayList<String[]> inputStreamNotificaciones,
			Document document, PdfWriter writer, IGenDocAPI genDocAPI,
			ArrayList<String[]> marcas, ClientContext cct, int nPaginaActual)
			throws ISPACException, DocumentException, MalformedURLException,
			IOException {

		int documentosPorPagina = 13;

		String[] marca = new String[2];
		marca[0] = "Índice";
		marca[1] = ""+(nPaginaActual);
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

		// MQE Añadimos la Información del documento

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
			Iterator inputStreamNotificacionesIterator = inputStreamNotificaciones
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
					archivo = DocumentosUtil.getFile(cct, documento[0], null, documento[7]);
					file = new FileInputStream(archivo);
				}

				reader = new PdfReader((InputStream) file);

				reader.consolidateNamedDestinations();
				int n = reader.getNumberOfPages();

				//Nombre del documento
				//Descripción del documento
				texto = (nDocumento<10?"0"+nDocumento:nDocumento) + " " + documento[8];
				
				Font fuente6 = new Font(Font.TIMES_ROMAN);
				fuente6.setStyle(Font.NORMAL);
				fuente6.setSize(10);
				tabla.setBorder(0);
				frase = new Phrase(texto, fuente6);
				
				celda = new Cell(frase);
				celda.setBorder(0);
				celda.setWidth("80%");
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
				celda.setWidth("5%");
				tabla.addCell(celda);

				//quitamos 1 página al nº de página que se imprime ya que no contamos el índice
				if(muestraPortada){
					texto = "" + nPaginaDocumentosImprimir;
				}else{
					texto = "" + nPaginaDocumentosImprimir;
				}
				
				frase = new Phrase(texto, fuente3);
				celda = new Cell(new Paragraph(new Chunk(texto, fuente3).setLocalGoto("" + nPaginaDocumentos)));
				logger.debug("localgoto: "+nPaginaDocumentos+", documento: "+documento[8]);

				celda.setBorder(0);
				celda.setWidth("5%");
				tabla.addCell(celda);

				marca = new String[2];
				
				//Descripción del documento
				marca[0] = "Documento " + (nDocumento<10?"0"+nDocumento:nDocumento) + " " + documento[8];
				marca[1] = "" + (nPaginaDocumentos);
				marcas.add(marca);
				
				logger.debug("marca[0]: "+(nPaginaDocumentos)+",  "+marca[0]);
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

	private Table inicializaTabla (Font fuente2) {
		Table tabla = null;
		try {			
			tabla = new Table(4);
			tabla.setBorder(0);
			tabla.setWidth(120);
			
			Phrase frase = new Phrase("Nombre Documento", fuente2);
			Cell celda;
			celda = new Cell(frase);
			celda.setBorder(0);
			celda.setWidth("80%");
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
			celda.setWidth("5%");
			tabla.addCell(celda);
			tabla.endHeaders();
	
			frase = new Phrase("Página", fuente2);
			celda = new Cell(frase);
			celda.setBorder(0);
			celda.setWidth("5%");
			tabla.addCell(celda);
			tabla.endHeaders();
				
			tabla.setBorder(0);
			
		} catch (BadElementException e) {
			logger.error("Error al inicializar la tabla. Expediente: " + expedienteOrigen + ". "+e.getMessage(), e);
		}
		return tabla;
	}

	private void añadeImagenes(Document document) throws MalformedURLException,	IOException, DocumentException, ISPACRuleException {
		
		Image imagen = null;
		try{
			imagen = Image.getInstance(imgLogoCabecera);
			imagen.setAbsolutePosition(50, document.getPageSize().getHeight() - 100);
			imagen.scalePercent(50);
			document.add(imagen);
		}
		catch(Exception e){
			logger.error("ERROR no se ha encontrado la imagen de logo de la entidad: " + imgLogoCabecera + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR no se ha encontrado la imagen de logo de la entidad: " + imgLogoCabecera + ". " + e.getMessage(), e);
		}

		// Añadimos la imagen del fondo
		try{
			imagen = Image.getInstance(imgFondo);
			imagen.setAbsolutePosition(250, 50);
			imagen.scalePercent(70);
			document.add(imagen);
		}
		catch(Exception e){
			logger.error("ERROR no se ha encontrado la imagen de fondo: " + imgFondo + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR no se ha encontrado la imagen de fondo: " + imgFondo + ". " + e.getMessage(), e);
		}
		
		// Añadimos el pie de página de la diputación
		try{
			imagen = Image.getInstance(imgPie);
			imagen.setAbsolutePosition(document.getPageSize().getWidth()-550, 15);
			imagen.scalePercent(80);
			document.add(imagen);
		}
		catch(Exception e){
			logger.error("ERROR no se ha encontrado la imagen de pie de página: " + imgPie + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR no se ha encontrado la imagen de pie de página: " + imgPie + ". " + e.getMessage(), e);
		}
	}

	private int añadePortada(String numExpPadre, Document document,	IEntitiesAPI entitiesAPI, int pagActual, IClientContext cct) throws ISPACException, DocumentException,
			MalformedURLException, IOException {

		IItem expediente = ExpedientesUtil.getExpediente(cct, numExpPadre);
		String cadena = numExpPadre;
		if (expediente != null) {
			if (StringUtils.isNotEmpty(expediente.getString("ASUNTO")))
				cadena = expediente.getString("ASUNTO");
		}

		document.newPage();
		pagActual++;
		// Añadimos el fondo
		try{
			Image imagenFondo = Image.getInstance(imgPortada);
			imagenFondo.scaleAbsolute(document.getPageSize().getWidth(), document
					.getPageSize().getHeight());
			imagenFondo.setAbsolutePosition(0, 0);
			document.add(imagenFondo);
		}
		catch(Exception e){
			logger.error("ERROR no se ha encontrado la imagen de portada: " + imgPortada + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR no se ha encontrado la imagen de portada: " + imgPortada + ". " + e.getMessage(), e);
		}

		// Añadimos el logotipo de la diputación
		try{
			Image imagen = Image.getInstance(imgLogoPortada);
			imagen.setAbsolutePosition(50, 250);
			imagen.scalePercent(50);
			document.add(imagen);
			document.add(imagen);
		}
		catch(Exception e){
			logger.error("ERROR no se ha encontrado la imagen logotipo de la entidad: " + imgLogoPortada + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR no se ha encontrado la imagen logotipo de la entidad: " + imgLogoPortada + ". " + e.getMessage(), e);
		}	

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
			texto.append("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
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
		texto.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
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
		
		try{
			Image imagenFondo = Image.getInstance(imgContraportada);
			imagenFondo.scaleAbsolute(document.getPageSize().getWidth(), document
				.getPageSize().getHeight());
			imagenFondo.setAbsolutePosition(0, 0);
			
			// Quitamos el número de página

			document.add(imagenFondo);

			// Fin de la portada
		}
		catch(Exception e){
			logger.error("ERROR no se ha encontrado la imagen de contraportada: " + imgContraportada + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR no se ha encontrado la imagen de contraportada: " + imgContraportada + ". " + e.getMessage(), e);
		}
		
		return pagActual;

		// Fin de la portada
	}
	
	private static String normalizar(String name) {
		name = StringUtils.replace(name, "/", "_");
		name = StringUtils.replace(name, "\\", "_");
		return name;
	}
}