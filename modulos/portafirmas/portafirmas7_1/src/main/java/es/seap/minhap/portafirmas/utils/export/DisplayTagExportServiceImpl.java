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

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.displaytag.export.CsvView;
import org.displaytag.export.ExcelView;
import org.displaytag.export.XmlView;
import org.displaytag.model.Cell;
import org.displaytag.model.HeaderCell;
import org.displaytag.model.Row;
import org.displaytag.model.TableModel;
import org.displaytag.properties.TableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;


@Component
public class DisplayTagExportServiceImpl implements ExportService, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory
			.getLogger(DisplayTagExportServiceImpl.class);
	/**
	 * @see es.juntadeandalucia.cice.pfirma.utils.export.ExportService#createTableModel(TableProperties, String)
	 */
	public TableModel createTableModel(TableProperties props, String encoding) {
		try {
			TableModel table = new TableModel(props, encoding, null);
			return table;
		} catch (Exception e) {
			log.error("Error in create table", e);
			throw new RuntimeException("Error in create table", e);
		}
	}
	/**
	 * @see es.juntadeandalucia.cice.pfirma.utils.export.ExportService#export(HttpServletResponse, TableModel, String)
	 */
	public void export(HttpServletResponse response, TableModel table,
			String type) {
		try {
			if (type != null) {
				// TODO: Cambiar nombre de fichero ...
				response.setHeader("Content-disposition",
						"attachment; filename=" + "export." + type);
				if (type.equals(Constants.EXPORT_PDF_TYPE)) {
					//PdfView pdf = new PdfView();
					PdfViewiTextv2 pdf = new PdfViewiTextv2();
					pdf.setParameters(table, true, true, false);
					response.setContentType(Constants.EXPORT_PDF_MIME);
					pdf.doExport(response.getOutputStream());
				} else if (type.equals(Constants.EXPORT_XML_TYPE)) {
					XmlView xml = new XmlView();
					xml.setParameters(table, true, true, false);
					response.setContentType(Constants.EXPORT_XML_MIME);
					xml.doExport(response.getWriter());
				} else if (type.equals(Constants.EXPORT_XLS_TYPE)) {
					ExcelView excel = new ExcelView();
					excel.setParameters(table, true, true, false);
					response.setContentType(Constants.EXPORT_XLS_MIME);
					excel.doExport(response.getWriter());
				} else if (type.equals(Constants.EXPORT_CSV_TYPE)) {
					CsvView csv = new CsvView();
					csv.setParameters(table, true, true, false);
					response.setContentType(Constants.EXPORT_CSV_MIME);
					csv.doExport(response.getWriter());
				}
			}
		} catch (Exception e) {
			log.error("Error in export", e);
			throw new RuntimeException("Error in export", e);
		}
	}
	
	public void loadRows (TableModel table, List<Object[]> rowsList) {
		try {
			int counterRows = 1;			
			for (int i=0; i < rowsList.size(); i++) {
				Object[] currentRow = rowsList.get(i);
				Row row = new Row(counterRows + "", counterRows);
				for (int j=0; j<currentRow.length; j++) {
					Object valor = currentRow[j];
					if (Util.esVacioONulo(valor)) {
						row.addCell(new Cell(""));
					} else {
						row.addCell(new Cell(valor.toString()));
					}
				}
				table.addRow(row);
				counterRows++;
			}
		} catch (Exception e) {
			log.error("Error in load rows", e);
			throw new RuntimeException("Error in load rows", e);
		}
	}

	//public void loadRows(TableModel table, String[] headers, List<AbstractBaseDTO> rowsList, int pageSize, int pageActual) {
	/*public void loadRows(TableModel table, String[] headers, List<Object[]> rowsList, int pageSize, int pageActual) {
		try {
			int counterRows = 1;
			int initPage = (pageActual - 1) * pageSize;
			int endPage = initPage + pageSize;
			for (int j = initPage; j < endPage && j < rowsList.size(); j++) {
				Object rowElement = rowsList.get(j);
				Row row = new Row(counterRows + "", counterRows);
				for (int i = 0; i < headers.length; i++) {
					Method method = rowElement.getClass().getMethod(
							"get" + headers[i]);
					String resultado = "";
					if (method.invoke(rowElement) instanceof String) {
						resultado = (String) method.invoke(rowElement);
					} else if (method.invoke(rowElement) instanceof java.util.Date) {
						java.util.Date fRequest = (Date) method
								.invoke(rowElement);
						SimpleDateFormat formatter = new SimpleDateFormat(
								"dd/MM/yyyy HH:mm:ss");
						resultado = formatter.format(fRequest);
					} else {
						resultado = method.invoke(rowElement) == null ? "-"
								: method.invoke(rowElement).toString();
					}
					row.addCell(new Cell(resultado));
				}
				table.addRow(row);
				counterRows++;

			}
		} catch (Exception e) {
			log.error("Error in load rows", e);
			throw new RuntimeException("Error in load rows", e);
		}
			
	}*/
	/**
	 * @see es.juntadeandalucia.cice.pfirma.utils.export.ExportService#loadRows(TableModel, String[], ListDataModel, int, int)
	 */
	/*public void loadRows(TableModel table, String[] headers,
			List<AbstractBaseDTO> rows, int pageSize, int pageActual) {
		try {
			int counterRows = 1;
			List<AbstractBaseDTO> rowsList = (List<AbstractBaseDTO>) rows
					.getWrappedData();

			int initPage = (pageActual - 1) * pageSize;
			int endPage = initPage + pageSize;
			for (int j = initPage; j < endPage && j < rowsList.size(); j++) {
				Object rowElement = rowsList.get(j);
				Row row = new Row(counterRows + "", counterRows);
				for (int i = 0; i < headers.length; i++) {
					Method method = rowElement.getClass().getMethod(
							"get" + headers[i]);
					String resultado = "";
					if (method.invoke(rowElement) instanceof String) {
						resultado = (String) method.invoke(rowElement);
					} else if (method.invoke(rowElement) instanceof java.util.Date) {
						java.util.Date fRequest = (Date) method
								.invoke(rowElement);
						SimpleDateFormat formatter = new SimpleDateFormat(
								"dd/MM/yyyy HH:mm:ss");
						resultado = formatter.format(fRequest);
					} else {
						resultado = method.invoke(rowElement) == null ? "-"
								: method.invoke(rowElement).toString();
					}
					row.addCell(new Cell(resultado));
				}
				table.addRow(row);
				counterRows++;

			}
		} catch (Exception e) {
			log.error("Error in load rows", e);
			throw new RuntimeException("Error in load rows", e);
		}
	}*/
	/**
	 * @see es.juntadeandalucia.cice.pfirma.utils.export.ExportService#lodHeaders(TableModel, String[])
	 */
	public void loadHeaders(TableModel table, String[] headers) {
		try {
			for (int i = 0; i < headers.length; i++) {
				HeaderCell cabecera = new HeaderCell();
				cabecera.setBeanPropertyName(headers[i]);
				table.addColumnHeader(cabecera);
			}
		} catch (Exception e) {
			log.error("Error in load headers", e);
			throw new RuntimeException("Error in load headers", e);
		}
	}

	@Override
	public void loadRowsStatistics(TableModel table, List<Object[]> rows) {
		// En este caso usar el otro loadRows
	}

}
