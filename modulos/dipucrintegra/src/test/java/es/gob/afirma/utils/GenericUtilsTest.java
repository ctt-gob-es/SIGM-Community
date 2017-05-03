// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/* 
* Este fichero forma parte de la plataforma de @firma. 
* La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
* y descargado desde http://forja-ctt.administracionelectronica.gob.es
*
* Copyright 2009-,2011 Gobierno de España
* Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
* condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este 
* fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
*/

/** 
 * <b>File:</b><p>es.gob.afirma.utils.GeneralUtilsTest.java.</p>
 * <b>Description:</b><p>Class tests GeneralUtils class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>25/03/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 25/03/2011.
 */
package es.gob.afirma.utils;

import java.util.HashMap;
import java.util.Map;

import es.gob.afirma.utils.GenericUtilsTest;

import junit.framework.TestCase;

/** 
 * <p>Class tests GenericUtilsTest class.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 25/03/2011.
 */
public class GenericUtilsTest extends TestCase {

	public void testGetValueFromMapsTree() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> data1 = new HashMap<String, Object>();
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("conclusion", "Firma correcta");
		data1.put("descripcion", data2);
		data.put("respuesta", data1);
		data.put("respuesta2", "data");

		// valores nulos
		assertNull(GenericUtils.getValueFromMapsTree(null, null));

		// valores no válidos
		assertNull(GenericUtils.getValueFromMapsTree("", null));
		assertNull(GenericUtils.getValueFromMapsTree("novalido", new HashMap<String, Object>()));
		// rutas erróneas
		assertNull(GenericUtils.getValueFromMapsTree("respuesta2/descripcion/conclusion", data));

		// valores válidos
		assertEquals("Firma correcta", GenericUtils.getValueFromMapsTree("respuesta/descripcion/conclusion", data));
		assertEquals("data", GenericUtils.getValueFromMapsTree("respuesta2", data));
	}

}
