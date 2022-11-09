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

/*

 Empresa desarrolladora: GuadalTEL S.A.

 Autor: Junta de AndalucÃ­a

 Derechos de explotaciÃ³n propiedad de la Junta de AndalucÃ­a.

 Ã‰ste programa es software libre: usted tiene derecho a redistribuirlo y/o modificarlo bajo los tÃ©rminos de la Licencia EUPL European Public License publicada 
 por el organismo IDABC de la ComisiÃ³n Europea, en su versiÃ³n 1.0. o posteriores.

 Ã‰ste programa se distribuye de buena fe, pero SIN NINGUNA GARANTÃ�A, incluso sin las presuntas garantÃ­as implÃ­citas de USABILIDAD o ADECUACIÃ“N A PROPÃ“SITO 
 CONCRETO. Para mas informaciÃ³n consulte la Licencia EUPL European Public License.

 Usted recibe una copia de la Licencia EUPL European Public License junto con este programa, si por algÃºn motivo no le es posible visualizarla, puede 
 consultarla en la siguiente URL: http://ec.europa.eu/idabc/servlets/Doc?id=31099

 You should have received a copy of the EUPL European Public License along with this program. If not, see http://ec.europa.eu/idabc/servlets/Doc?id=31096

 Vous devez avoir reÃ§u une copie de la EUPL European Public License avec ce programme. Si non, voir http://ec.europa.eu/idabc/servlets/Doc?id=31205

 Sie sollten eine Kopie der EUPL European Public License zusammen mit diesem Programm. Wenn nicht, finden Sie da 
 http://ec.europa.eu/idabc/servlets/Doc?id=29919

 */

package es.seap.minhap.portafirmas.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a list backed by a JPA Query, but only loading the results of the
 * query one page at a time. It loads one page *ahead* for each fetch miss, so
 * if you are iterating through query results backwards you will get poor
 * performance.
 */
public class RequestListDTO extends ArrayList<AbstractBaseDTO> {

	private static final long serialVersionUID = 1L;
	
	/** total number of results expected */
	private long numResults;
	private int pageSize;

	public RequestListDTO(List<AbstractBaseDTO> list, long numResults,
			int pageSize) {
		super(list);
		this.numResults = numResults;
		this.pageSize = pageSize;
	}

	/**
	 * Return the total number of items in the list. This is done by using an
	 * equivalent COUNT query for the backed query.
	 */
	@Override
	public int size() {
		return (int) numResults;
	}

	/** update the number of results expected in this list */
	public void setNumResults(long numResults) {
		this.numResults = numResults;
	}

	@Override
	public AbstractBaseDTO get(int index) {
		return super.get(index % pageSize);
	}
	/**
	 * Obtiene el n&uacute;mero de peticiones a mostrar por p&aacute;gina en la paginaci&oacute;n
	 * @return n&uacute;mero de peticiones a mostrar por p&aacute;gina
	 */
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}