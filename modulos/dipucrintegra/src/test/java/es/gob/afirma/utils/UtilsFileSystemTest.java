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
 * <b>File:</b><p>es.gob.afirma.utils.UtilsFileSystemTest.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>18/03/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 18/03/2011.
 */
package es.gob.afirma.utils;

import junit.framework.TestCase;
import es.gob.afirma.utils.UtilsFileSystem;


/** 
 * <p>Class tests UtilsFileSystem class.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 18/03/2011.
 */
public class UtilsFileSystemTest extends TestCase {

    
    public void testReadFileBase64Encoded() throws Exception {
	//prueba con valor nulo.
    try{
	assertNull(UtilsFileSystem.readFileBase64Encoded(null, false));
    } catch (IllegalArgumentException e) {}
	
    //prueba con valor no válido.
	assertNull(UtilsFileSystem.readFileBase64Encoded("/s", false));
	
	//prueba con valor válido.
	assertEquals(13252, UtilsFileSystem.readFileBase64Encoded("ficheroAfirmar.txt", true).length());
    }
    
    public void testReadFile() throws Exception {
    //prueba con valor nulo.
    try{
	assertNull(UtilsFileSystem.readFile(null, false));
    } catch (IllegalArgumentException e) {}
	
//	//prueba con valor no válido.
	assertNull(UtilsFileSystem.readFile("/s", false));
	
	//prueba con valor válido.
	assertEquals(9810, UtilsFileSystem.readFile("ficheroAfirmar.txt", true).length);
    }
    
	public void testWriteFile() throws Exception {

		// prueba con valores nulos
		try {
			UtilsFileSystem.writeFile(null, null);
			fail("No se ha lanzado la excepción esperada");
		} catch (IllegalArgumentException e) {}

		// prueba con valores no válidos
		try {
			UtilsFileSystem.writeFile(new byte[0], "");
			fail("No se ha lanzado la excepción esperada");
		} catch (IllegalArgumentException e) {}

	}
}
