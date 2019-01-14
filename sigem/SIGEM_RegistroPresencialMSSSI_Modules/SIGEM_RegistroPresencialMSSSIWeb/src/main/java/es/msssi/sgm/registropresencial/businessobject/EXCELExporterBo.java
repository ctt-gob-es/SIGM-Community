/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.businessobject;

import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.primefaces.component.api.DynamicColumn;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.ExcelExporter;

import com.ieci.tecdoc.common.invesicres.ScrRegstate;

import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.msssi.sgm.registropresencial.beans.ItemBean;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPExportErrorCode;
import es.msssi.sgm.registropresencial.errors.RPExportException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.UtilExporter;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase utilizada para exportar a un libro EXCEL.
 * 
 * @author cmorenog
 * */
public class EXCELExporterBo extends ExcelExporter implements Serializable {

	private static final Logger LOG = Logger.getLogger(EXCELExporterBo.class.getName());
	
	private static final long serialVersionUID = 1L;
	private static final int COUNTCOLUMN = 8;
	private static final int FCHCELL = 7;
	/** Fecha del documento. */
	private String fecha;
	/** Estilo por defecto de las celdas. */
	private HSSFCellStyle cellStyleCell;

	/**
	 * Constructor.
	 */
	public EXCELExporterBo() {
		super();
		fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
	}

	/**
	 * Añade formato a la cabecera de la tabla.
	 * 
	 * @param workbook
	 *            libro que se está construyendo.
	 */
	private void addHeader(HSSFWorkbook workbook) {
		LOG.trace("Entrando en EXCELExporterBo.addHeader()");
		
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFColor colorFondo = palette.findSimilarColor( UtilExporter.EXCELCOLORFONDOCABECERATABLE.getRed(), UtilExporter.EXCELCOLORFONDOCABECERATABLE.getGreen(), UtilExporter.EXCELCOLORFONDOCABECERATABLE.getBlue());

		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow header = sheet.getRow(sheet.getLastRowNum());

		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(colorFondo.getIndex());
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
		cellStyle.setFont(formatFont(workbook.createFont(), (short) UtilExporter.EXCELHEIGHTHEADERTABLE, UtilExporter.EXCELWEIGHTHEADERTABLE, UtilExporter.EXCELFONTHEADERTABLE));

		for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
			HSSFCell cell = header.getCell(i);
			cell.setCellStyle(cellStyle);
		}
	}

	/**
	 * Añade la cabecera del documento.
	 * 
	 * @param workbook
	 *            libro que se está construyendo.
	 * @param typeBook
	 *            tipo de libro
	 * @throws RPExportException
	 *             si se ha producido algún error al exportar el documento.
	 */
	private void addTitle(HSSFWorkbook workbook, int typeBook) throws RPExportException {
		LOG.trace("Entrando en EXCELExporterBo.addTitle()");
		
		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = sheet.createRow(0);
		row.setHeightInPoints(UtilExporter.EXCELHEIGHTROW);
		// titulo
		HSSFCellStyle cellStyleTitle = workbook.createCellStyle();
		cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyleTitle.setFont(formatFont(workbook.createFont(), (short) UtilExporter.EXCELHEIGHTTITLE, UtilExporter.EXCELWEIGHTTITLE, UtilExporter.EXCELFONTHEADERTABLE));

		// fecha
		HSSFCellStyle cellStyleDate = workbook.createCellStyle();
		cellStyleDate.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyleDate.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyleDate.setFont(formatFont(workbook.createFont(), (short) UtilExporter.EXCELHEIGHTDATE, UtilExporter.EXCELWEIGHTDATE, UtilExporter.EXCELFONTHEADERTABLE));

		for (int i = 0; i < COUNTCOLUMN; i++) {
			row.createCell(i);
		}
		// IMAGEN
		byte[] imagen;
		try {
			imagen = getImageCabecera();
			int pictureIdx = workbook.addPicture(imagen, Workbook.PICTURE_TYPE_PNG);

			CreationHelper helper = workbook.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = helper.createClientAnchor();
			anchor.setCol1(0);
			anchor.setRow1(0);
			Picture pict = drawing.createPicture(anchor, pictureIdx);
			pict.resize(UtilExporter.EXCELIMAGE);
			
		} catch (IOException ioException) {
			LOG.error(ErrorConstants.EXCEL_DOCUMENT_EXPORT_ERROR_MESSAGE, ioException);
			throw new RPExportException( RPExportErrorCode.GET_HEADER_IMAGE_ERROR, ErrorConstants.GET_HEADER_IMAGE_ERROR_MESSAGE, ioException);
		}

		// FECHA
		HSSFCell cellTitle = row.getCell(FCHCELL);
		cellTitle.setCellValue(fecha);
		cellTitle.setCellStyle(cellStyleDate);

		sheet.addMergedRegion(CellRangeAddress.valueOf("A1:B1"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("C1:G1"));
		// TITULO
		cellTitle = row.getCell(2);
		
		if (typeBook == 1) {
			cellTitle.setCellValue(UtilExporter.TITULO_BUSQ_RENTRADA);
			
		} else {
			cellTitle.setCellValue(UtilExporter.TITULO_BUSQ_RSALIDA);
		}
		
		cellTitle.setCellStyle(cellStyleTitle);
	}

	/**
	 * Recoge los criterios de búsqueda y los añade al excel.
	 * 
	 * @param workbook
	 *            documento que estamos formando.
	 * @param typeBook
	 *            tipo de libro
	 * @throws RPGenericException
	 *             si se ha producido un error genérico en el proceso.
	 */
	private void addCriterios(HSSFWorkbook workbook, int typeBook) throws RPGenericException {
		LOG.trace("Entrando en EXCELExporterBo.addCriterios()");
		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
		
		for (int i = 0; i < COUNTCOLUMN; i++) {
			row.createCell(i);
		}
		
		HSSFCell cell1 = row.createCell(7);
		cell1.setCellValue("Carácter Restringido");

		List<ItemBean> listaCriterios;
		
		if (typeBook == 1) {
			listaCriterios = UtilExporter.putInputSearchCriteria();
			
		} else {
			listaCriterios = UtilExporter.putOutputSearchCriteria();
		}
		
		for (ItemBean criterio : listaCriterios) {
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			HSSFCell cell = row.createCell(0);
			cell.setCellValue(criterio.getClave() + ": " + criterio.getValor());
		}
		
		if (listaCriterios != null && listaCriterios.size() > 0) {
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			row.createCell(0);
		}
	}

	/**
	 * Exporta la tabla a exportar a un excel.
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
	public void export(FacesContext context, DataTable table, String filename, boolean pageOnly, boolean selectionOnly, String encodingType, MethodExpression preProcessor, MethodExpression postProcessor) {
		LOG.trace("Entrando en EXCELExporterBo.export()");
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		sheet.setDefaultColumnWidth(UtilExporter.EXCELWEIGHCELL);
		// style cell
		cellStyleCell = wb.createCellStyle();
		cellStyleCell.setBorderBottom((short) 1);
		cellStyleCell.setBorderTop((short) 1);
		cellStyleCell.setBorderLeft((short) 1);
		cellStyleCell.setBorderRight((short) 1);
		cellStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyleCell.setFont(formatFont(wb.createFont(), (short) UtilExporter.EXCELHEIGHTCELL, UtilExporter.EXCELWEIGHTCELL, UtilExporter.EXCELFONTHEADERTABLE));
		cellStyleCell.setWrapText(true);

		if (preProcessor != null) {
			preProcessor.invoke(context.getELContext(), new Object[] { wb });
		}

		try {
			ScrRegstate book = (ScrRegstate) context.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
			addTitle(wb, book.getIdocarchhdr().getType());
			addCriterios(wb, book.getIdocarchhdr().getType());
			
		} catch (RPGenericException rpGenericException) {
			LOG.error(ErrorConstants.EXCEL_DOCUMENT_EXPORT_ERROR_MESSAGE + " . Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " + rpGenericException.getShortMessage());
			Utils.redirectToErrorPage(rpGenericException, null, null);
			
		} catch (RPExportException rpExporterException) {
			LOG.error(ErrorConstants.EXCEL_DOCUMENT_EXPORT_ERROR_MESSAGE + " . Código: " + rpExporterException.getCode().getCode() + " . Mensaje: " + rpExporterException.getShortMessage());
			Utils.redirectToErrorPage(rpExporterException, null, null);
		}
		
		addColumnFacets(table, sheet, ColumnType.HEADER);
		addHeader(wb);
		
		if (pageOnly) {
			exportPageOnly(context, table, sheet);
			
		} else if (selectionOnly) {
			exportSelectionOnly(context, table, sheet);
			
		} else {
			exportAll(context, table, sheet);
		}

		if (table.hasFooterColumn()) {
			addColumnFacets(table, sheet, ColumnType.FOOTER);
		}

		table.setRowIndex(-1);

		if (postProcessor != null) {
			postProcessor.invoke(context.getELContext(), new Object[] { wb });
		}

		try {
			writeExcelToResponse(context.getExternalContext(), wb, filename);
			
		} catch (IOException ioException) {
			LOG.error(ErrorConstants.EXCEL_DOCUMENT_EXPORT_ERROR_MESSAGE, ioException);
			Utils.redirectToErrorPage(null, null, ioException);
		}
	}

	/**
	 * Añade la cabecera o el pie de la tabla del documento.
	 * 
	 * @param table
	 *            tabla que se está exportando.
	 * @param sheet
	 *            hoja del excel que se está construyendo.
	 * @param columnType
	 *            si la columna es cabecera o pie.
	 */
	@Override
	protected void addColumnFacets(DataTable table, Sheet sheet, ColumnType columnType) {		
		LOG.trace("Entrando en EXCELExporterBo.addColumnFacets()");
		
		int sheetRowIndex = sheet.getLastRowNum() + 1;
		Row rowHeader = sheet.createRow(sheetRowIndex);

		for (UIColumn col : table.getColumns()) {
			if (col.isRendered()) {
				if (col instanceof DynamicColumn) {
					((DynamicColumn) col).applyModel();
				}

				if (col.isExportable()) {
					addColumnValue(rowHeader, col.getFacet(columnType.facet()));
				}
			}
		}
	}

	/**
	 * Añade la celda a la cabecera o pie con el texto de la cabecera de la
	 * tabla que se está exportando.
	 * 
	 * @param row
	 *            fila que se añade al excel.
	 * @param components
	 *            Lista de componentes UI de Faces.
	 */
	@Override
	protected void addColumnValue(Row row, List<UIComponent> components) {
		LOG.trace("Entrando en EXCELExporterBo.addColumnValue()");
		
		int cellIndex = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();
		Cell cell = row.createCell(cellIndex);
		StringBuilder builder = new StringBuilder();
		FacesContext context = FacesContext.getCurrentInstance();

		for (UIComponent component : components) {
			if (component.isRendered()) {
				String value = exportValue(context, component);
				if (value != null) {
					builder.append(value);
				}
			}
		}
		cell.setCellValue(new HSSFRichTextString(builder.toString()));
		cell.setCellStyle(cellStyleCell);
	}

	/**
	 * Devuelve la imagen de la cabecera en un array de byte.
	 * 
	 * @return array de byte con la imagen de cabecera.
	 * 
	 * @throws IOException
	 *             Si ha ocurrido un error de entrada/salida.
	 */
	private byte[] getImageCabecera() throws IOException {
		LOG.trace("Entrando en EXCELExporterBo.getImageCabecera()");
		String logo = SigemConfigFilePathResolver.getInstance().resolveFullPath( "skinEntidad_" + MultiEntityContextHolder.getEntity(), "/SIGEM_RegistroPresencialMSSSIWeb") + UtilExporter.LOGO;
		
		BufferedImage originalImage = ImageIO.read(new File(logo));
		if (originalImage.getHeight() > 80) {
			Image imagenEscalada = originalImage.getScaledInstance(-1, 80, Image.SCALE_SMOOTH);
			originalImage = new BufferedImage(imagenEscalada.getWidth(null), imagenEscalada.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g = originalImage.getGraphics();
			g.drawImage(imagenEscalada, 0, 0, null);
			g.dispose();
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, "png", baos);
		baos.flush();
		byte[] bytes = baos.toByteArray();
		baos.close();

		return bytes;
	}

	/**
	 * Formatea la fuente del libro.
	 * 
	 * @param font
	 *            fuente a formatear.
	 * @param fontHeight
	 *            Tamaño de la fuente.
	 * @param boldweight
	 *            Estilo de la fuente.
	 * @param fontName
	 *            Tipo de letra.
	 * 
	 * @return la fuente formateada.
	 */
	private HSSFFont formatFont(HSSFFont font, short fontHeight, short boldweight, String fontName) {
		LOG.trace("Entrando en EXCELExporterBo.formatFont()");
		
		HSSFFont fontReturn = font;
		font.setFontHeightInPoints(fontHeight);
		font.setBoldweight(boldweight);
		font.setFontName(fontName);
		return fontReturn;
	}
}