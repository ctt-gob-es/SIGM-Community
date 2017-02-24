/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.businessobject;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.PDFExporter;

import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import es.msssi.sgm.registropresencial.beans.ItemBean;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.UtilExporter;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase que utilizamos para exportar PDF. Extendemos de la original porque no
 * podemos customizar luego el PDF.
 * 
 * @author cmorenog
 * */
public class PDFExporterBo extends PDFExporter {
    private static final Logger LOG = Logger.getLogger(PDFExporterBo.class);

    /**
     * Constructor.
     */
    public PDFExporterBo() {
	super();
    }

    /**
     * Añade la cabecera o el pie de la tabla del documento.
     * 
     * @param table
     *            tabla que se está exportando.
     * @param pdfTable
     *            tabla pdf que se está construyendo.
     * @param columnType
     *            si la columna es cabecera o pie.
     */
    @Override
    protected void addColumnFacets(
	DataTable table, PdfPTable pdfTable, ColumnType columnType) {
	LOG.trace("Entrando en PDFExporterBo.addColumnFacets()");
	if (table.getRowCount() != 0) {
	    pdfTable.setWidthPercentage(UtilExporter.PDFWIDTHPERCENTAGE);

	    // cargar cabecera de tabla
	    for (UIColumn col : table.getColumns()) {
		if (col.isRendered()) {
		    if (col instanceof DynamicColumn) {
			((DynamicColumn) col).applyModel();
		    }

		    if (col.isExportable()) {
			addHeaderValue(
			    pdfTable, col.getFacet(columnType.facet()),
			    UtilExporter.PDFFUENTECABECERATABLE);
		    }
		}
	    }
	}
    }

    /**
     * Añade la celda a la cabecera o pie con el texto de la cabecera de la
     * tabla que se está exportando.
     * 
     * @param pdfTable
     *            tabla que se está exportando.
     * @param component
     *            la cabecera o el pie a añadir.
     * @param font
     *            cuente de la celda.
     */
    protected void addHeaderValue(
	PdfPTable pdfTable, UIComponent component, Font font) {
	LOG.trace("Entrando en PDFExporterBo.addHeaderValue()");
	String value = component == null
	    ? "" : exportValue(
		FacesContext.getCurrentInstance(), component);

	PdfPCell cell = new PdfPCell(
	    new Paragraph(
		value, font));
	// dar formato a celda
	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

	cell.setBackgroundColor(UtilExporter.PDFCOLORFONDOCABECERATABLE);
	cell.setFixedHeight(UtilExporter.PDFHEADERCELLFIXEDHEIGHT);
	// agregar celda
	pdfTable.addCell(cell);
    }

    /**
     * Exporta todas las celdas de la tabla a exportar en la tabla del pdf que
     * se está construyendo.
     * 
     * @param table
     *            tabla que se está exportando.
     * @param pdfTable
     *            tabla que se está exportando.
     */
    @Override
    protected void exportCells(
	DataTable table, Object pdfTable) {
	LOG.trace("Entrando en PDFExporterBo.exportCells()");
	PdfPTable tablePDF = (PdfPTable) pdfTable;
	tablePDF.setWidthPercentage(UtilExporter.PDFWIDTHPERCENTAGE);
	for (UIColumn col : table.getColumns()) {
	    if (col.isRendered()) {
		if (col instanceof DynamicColumn) {
		    ((DynamicColumn) col).applyModel();
		}
		// si es exportable
		if (col.isExportable()) {
		    addCellValue(
			tablePDF, col.getChildren(), UtilExporter.PDFFUENTECELDA);
		}
	    }
	}
    }

    /**
     * Añade el valor a la celda.
     * 
     * @param pdfTable
     *            tabla que se está exportando.
     * @param components
     *            contenido a añadir en la celda.
     * @param font
     *            fuente de la celda.
     * @param fondocelda
     *            color de fondo de la celda.
     */
    protected void addCellValue(
	PdfPTable pdfTable, List<UIComponent> components, Font font, Color fondocelda) {
	LOG.trace("Entrando en PDFExporterBo.addCellValue() sin fondo de celda");
	StringBuilder builder = new StringBuilder();
	FacesContext context = FacesContext.getCurrentInstance();
	for (UIComponent component : components) {
	    if (component.isRendered()) {
		String value = exportValue(
		    context, component);
		if (value != null) {
		    builder.append(value);
		}
	    }
	}

	PdfPCell cell = new PdfPCell(
	    new Paragraph(
		builder.toString(), font));
	cell.setBorder(1);
	cell.setBackgroundColor(fondocelda);
	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	pdfTable.addCell(cell);
    }

    /**
     * Añade el valor a la celda.
     * 
     * @param pdfTable
     *            tabla que se está exportando.
     * @param components
     *            contenido a añadir en la celda.
     * @param font
     *            fuente de la celda.
     */
    protected void addCellValue(
	PdfPTable pdfTable, List<UIComponent> components, Font font) {
	LOG.trace("Entrando en PDFExporterBo.addCellValue()");
	addCellValue(pdfTable, components, font,
	    new Color(
		UtilExporter.PDFCELLRGBCOLOR_R, UtilExporter.PDFCELLRGBCOLOR_G,
		UtilExporter.PDFCELLRGBCOLOR_B));
    }

    /**
     * Recoge los criterios de búsqueda y los añade al pdf.
     * 
     * @param document
     *            documento que estamos formando.
     * @param typeBook
     *            tipo de libro
     * @throws IOException
     *             Si se he producido un error de entrada/salida.
     * @throws DocumentException
     *             Si se ha producido un error con el documento.
     * */
    public void addCriterios(
	Document document, int typeBook)
	throws IOException, DocumentException {
	LOG.trace("Entrando en PDFExporterBo.addCriterios()");
	List<ItemBean> listaCriterios;
	if (typeBook == 1) {
	    listaCriterios = UtilExporter.putInputSearchCriteria();
	}
	else {
	    listaCriterios = UtilExporter.putOutputSearchCriteria();
	}
	for (ItemBean criterio : listaCriterios) {
	    document.add(new Paragraph(
		criterio.getClave() +
		    ": " + criterio.getValor(), UtilExporter.PDFFUENTECRITERIOS));
	}
	// linea en blanco
	document.add(new Paragraph(
	    " ", UtilExporter.PDFFUENTECRITERIOS));
    }

    /**
     * Exporta la tabla a exportar a un pdf.
     * 
     * @param context
     *            contexto de la aplicación.
     * @param table
     *            Tabla a exportar.
     * @param filename
     *            Nombre del fichero de salida.
     * @param pageOnly
     *            Solo se exporta una página de la tabla.
     * @param selectionOnly
     *            Solo se exporta los elementos seleccionados de la tabla.
     * @param encodingType
     *            Tipo de encoding.
     * @param preProcessor
     *            Método que se ejecuta antes de la exportación.
     * @param postProcessor
     *            Método que se ejecuta después de la exportación.
     */
    @Override
    public void export(
	FacesContext context, DataTable table, String filename, boolean pageOnly,
	boolean selectionOnly, String encodingType, MethodExpression preProcessor,
	MethodExpression postProcessor) {
	LOG.trace("Entrando en PDFExporterBo.export()");
	try {
	    Document document =
		new Document(
		    PageSize.A4.rotate(), UtilExporter.PDFROTATEDOCUMENTMARGINLEFT,
		    UtilExporter.PDFROTATEDOCUMENTMARGINRIGHT,
		    UtilExporter.PDFROTATEDOCUMENTMARGINTOP,
		    UtilExporter.PDFROTATEDOCUMENTMARGINBOTTOM);

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PdfWriter writer = PdfWriter.getInstance(
		document, baos);
	    ScrRegstate book = (ScrRegstate) context.getExternalContext().getSessionMap().get(
		KeysRP.J_BOOK);

	    // cabecera
	    PDFExporterTableHeader event;
	    if (book.getIdocarchhdr().getType() == 1) {
		event = new PDFExporterTableHeader(
		    UtilExporter.TITULO_BUSQ_RENTRADA);
	    }
	    else {
		event = new PDFExporterTableHeader(
		    UtilExporter.TITULO_BUSQ_RSALIDA);
	    }
	    writer.setPageEvent(event);

	    if (preProcessor != null) {
		preProcessor.invoke(
		    context.getELContext(), new Object[] { document });
	    }

	    if (!document.isOpen()) {
		document.open();
	    }

	    addCriterios(
		document, book.getIdocarchhdr().getType());

	    if (table.getRowCount() != 0) {
		Paragraph totalCeldas = new Paragraph(
		    "Número total de Registros: " +
			table.getRowCount(), UtilExporter.PDFFUENTECELDA);
		totalCeldas.setAlignment(2);
		totalCeldas.setSpacingAfter(2);
		document.add(totalCeldas);
	    }
	    document.add(exportPDFTable(
		context, table, pageOnly, selectionOnly, encodingType));

	    if (postProcessor != null) {
		postProcessor.invoke(
		    context.getELContext(), new Object[] { document });
	    }
	   
	    document.close();

	    writePDFToResponse(
		context.getExternalContext(), baos, filename);
	}
	catch (IOException ioException) {
	    LOG.error(
		ErrorConstants.PDF_DOCUMENT_EXPORT_ERROR_MESSAGE, ioException);
	    Utils.redirectToErrorPage(
		null, null, ioException);
	}
	catch (DocumentException documentException) {
	    LOG.error(
		ErrorConstants.PDF_DOCUMENT_EXPORT_ERROR_MESSAGE, documentException);
	    Utils.redirectToErrorPage(
		null, null, documentException);
	}
    }
}