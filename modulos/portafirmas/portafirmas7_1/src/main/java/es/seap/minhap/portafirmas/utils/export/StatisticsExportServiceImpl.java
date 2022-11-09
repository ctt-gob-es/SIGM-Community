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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.displaytag.export.ExcelView;
import org.displaytag.model.Cell;
import org.displaytag.model.HeaderCell;
import org.displaytag.model.Row;
import org.displaytag.model.TableModel;
import org.displaytag.properties.TableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;


@Component
public class StatisticsExportServiceImpl implements ExportService, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory
			.getLogger(StatisticsExportServiceImpl.class);
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
	public void export(HttpServletResponse response, TableModel table, String type) {
		try {
			if (type != null) {
				response.setHeader("Content-disposition",
						"attachment; filename=" + "export_" + type + ".xls");
				ExcelView excel = new ExcelView();
				excel.setParameters(table, true, true, true);
				response.setContentType(Constants.EXPORT_XLS_MIME);
				excel.doExport(response.getWriter());
			}
		} catch (Exception e) {
			log.error("Error in export", e);
			throw new RuntimeException("Error in export", e);
		}
	}

	/**
	 * @see es.juntadeandalucia.cice.pfirma.utils.export.ExportService#loadRows(TableModel, String[], ListDataModel, int, int)
	 */
	public void loadRows(TableModel table, String[] headers,
			List<AbstractBaseDTO> rows, int pageSize, int pageActual) {
		// En este caso usar el otro LoadRows
	}
	/**
	 * @see es.juntadeandalucia.cice.pfirma.utils.export.ExportService#loadHeaders(TableModel, String[])
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
		try {
			int counterRows = 1;
			for (Object[] fila : rows) {
				Row row = new Row(counterRows + "", counterRows);
				for (int i = 0; i < fila.length; i++) {
					String resultado = "";
					if (fila[i] instanceof String) {
						resultado = fila[i] == null ? "-" : (String) fila[i];
					}
					else if (fila[i] instanceof Date) {
						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						resultado = formatter.format((Date) fila[i]);
					}
					else {
						resultado = fila[i] == null ? "-" : fila[i].toString();
					}
					row.addCell(new Cell(Util.getInstance().filterCell(resultado)));
				}
				table.addRow(row);
				counterRows++;
			}
		} catch (Exception e) {
			log.error("Error in load rows", e);
			throw new RuntimeException("Error in load rows", e);
		}
		
	}
	@Override
	public void loadRows(TableModel table, List<Object[]> rows) {
		// TODO Auto-generated method stub
		
	}

}
