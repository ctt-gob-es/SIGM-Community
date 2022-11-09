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

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.displaytag.model.TableModel;
import org.displaytag.properties.TableProperties;



public interface ExportService {
	
	/**
	 * Exporta un documento que contiene el objeto response, el formato del documento viene
	 * especificado en el par&aacute;metro type
	 * @param response el objeto http de respuesta
	 * @param table el modelo de tabla de displayTag
	 * @param type el tipo de documento
	 * @see es.juntadeandalucia.cice.pfirma.utils.Constants#EXPORT_PDF_TYPE
	 * @see es.juntadeandalucia.cice.pfirma.utils.Constants#EXPORT_XML_TYPE
	 * @see es.juntadeandalucia.cice.pfirma.utils.Constants#EXPORT_XLS_TYPE
	 * @see es.juntadeandalucia.cice.pfirma.utils.Constants#EXPORT_CSV_TYPE
	 */
	public void export(HttpServletResponse response, TableModel table, String type);
	/**
	 * Carga con los datos pasados en el par&aacute;metro 'rows' las filas de una tabla
	 * @param table el modelo de tabla
	 * @param headers las cabeceras de la tabla
	 * @param rows los datos para rellenar las filas de la tabla
	 * @param pageSize el tama&ntilde;o de p&aacute;gina de la tabla
	 * @param pageActual la p&aacute;gina actual
	 */
	//public void loadRows(TableModel table, String[] headers, List<Object[]> rows, int pageSize, int pageActual);
	public void loadRows(TableModel table, List<Object[]> rows);
	
	
	/**
	 * Carga en el modelo de tabla las cabeceras pasadas como par&aacute;metro
	 * @param el modelo de tabla
	 * @param las cabeceras que se van a a&ntilde;adir al modelo
	 */
	public void loadHeaders(TableModel table, String[] headers);
	
	/**
	 * Crea el modelo de tabla para los par&aacute;metros especificados
	 * @param props las propiedades de la tabla
	 * @param encoding el tipo de codificaci&oacute;n de caractares
	 */
	public TableModel createTableModel(TableProperties props, String encoding);

	/**
	 * Carga con los datos pasados en el par&aacute;metro 'rows' las filas de una tabla
	 * @param table el modelo de tabla
	 * @param rows los datos para rellenar las filas de la tabla
	 * @param pageSize el tama&ntilde;o de p&aacute;gina de la tabla
	 * @param pageActual la p&aacute;gina actual
	 */
	public void loadRowsStatistics(TableModel table, List<Object[]> rows);
}
