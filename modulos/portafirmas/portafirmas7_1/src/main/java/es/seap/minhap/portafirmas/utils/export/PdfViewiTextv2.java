/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.utils.export;

import java.awt.Color;
import java.io.OutputStream;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.displaytag.exception.SeverityEnum;
import org.displaytag.export.BinaryExportView;
import org.displaytag.model.Column;
import org.displaytag.model.ColumnIterator;
import org.displaytag.model.HeaderCell;
import org.displaytag.model.Row;
import org.displaytag.model.RowIterator;
import org.displaytag.model.TableModel;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

public class PdfViewiTextv2  implements BinaryExportView {

	private TableModel model;
	private boolean exportFull;
	private boolean header;
	private boolean decorated;
	private Table tablePDF;
	private Font smallFont;
	//static Class class$org$displaytag$export$PdfView;

	public void setParameters(TableModel tableModel, boolean exportFullList, boolean includeHeader, boolean decorateValues) {

		this.model = tableModel;
		this.exportFull = exportFullList;
		this.header = includeHeader;
		this.decorated = decorateValues;
	}


	protected void initTable() throws BadElementException  {
		this.tablePDF = new Table(this.model.getNumberOfColumns());
		this.tablePDF.setAlignment(4);
		//this.tablePDF.setDefaultVerticalAlignment(4);
		this.tablePDF.setCellsFitPage(true);
		this.tablePDF.setWidth(100.0F);

		this.tablePDF.setPadding(2.0F);
		this.tablePDF.setSpacing(0.0F);

		this.smallFont = FontFactory.getFont("Helvetica", 7.0F, 0, new Color(0, 0, 0));
	}

	public String getMimeType()
	{
		return "application/pdf";
	}

	protected void generatePDFTable()
	throws JspException, BadElementException
	{
		if (this.header) {
			generateHeaders();
		}
		this.tablePDF.endHeaders();
		generateRows();
	}

	public void doExport(OutputStream out)
	throws JspException
	{
		try
		{
			initTable();

			Document document = new Document(PageSize.A4.rotate(), 60.0F, 60.0F, 40.0F, 40.0F);
			document.addCreationDate();
			HeaderFooter footer = new HeaderFooter(new Phrase("", this.smallFont), true);
			footer.setBorder(0);
			footer.setAlignment(1);

			PdfWriter.getInstance(document, out);

			generatePDFTable();
			document.open();
			document.setFooter(footer);
			document.add(this.tablePDF);
			document.close();
		}
		catch (Exception e)
		{
			throw new JspException("No se puede generar el PDF: ", e);
		}
	}

	protected void generateHeaders()
	throws BadElementException
	{
		Iterator<?> iterator = this.model.getHeaderCellList().iterator();

		while (iterator.hasNext())
		{
			HeaderCell headerCell = (HeaderCell)iterator.next();

			String columnHeader = headerCell.getTitle();

			if (columnHeader == null)
			{
				columnHeader = StringUtils.capitalize(headerCell.getBeanPropertyName());
			}

			Cell hdrCell = getCell(columnHeader);
			//hdrCell.setGrayFill(0.89999998F);
			//hdrCell.setGrayFill(0,89999998.0F);
			hdrCell.setBackgroundColor(new Color(200, 200, 200));
			hdrCell.setHeader(true);
			this.tablePDF.addCell(hdrCell);
		}
	}

	protected void generateRows()
	throws JspException, BadElementException
	{
		RowIterator rowIterator = this.model.getRowIterator(this.exportFull);

		while (rowIterator.hasNext())
		{
			Row row = rowIterator.next();

			ColumnIterator columnIterator = row.getColumnIterator(this.model.getHeaderCellList());

			while (columnIterator.hasNext())
			{
				Column column = columnIterator.nextColumn();

				Object value = column.getValue(this.decorated);

				Cell cell = getCell(ObjectUtils.toString(value));
				this.tablePDF.addCell(cell);
			}
		}
	}

	private Cell getCell(String value)
	throws BadElementException
	{
		Cell cell = new Cell(new Chunk(StringUtils.trimToEmpty(value), this.smallFont));
		cell.setVerticalAlignment(4);
		cell.setLeading(8.0F);
		return cell;
	}

	/*static Class class$(String x0)
	{
		try
		{
			return Class.forName(x0); } catch (ClassNotFoundException x1) { throw new NoClassDefFoundError().initCause(x1); }  } 
	static class PdfGenerationException extends BaseNestableJspTagException { private static final long serialVersionUID = 899149338534L;

	public PdfGenerationException(Throwable cause) { super((PdfView.class$org$displaytag$export$PdfView == null) ? (PdfView.class$org$displaytag$export$PdfView = PdfView.class$("org.displaytag.export.PdfView")) : PdfView.class$org$displaytag$export$PdfView, Messages.getString("PdfView.errorexporting"), cause);
	}*/

	public SeverityEnum getSeverity()
	{
		return SeverityEnum.ERROR;
	}
}

