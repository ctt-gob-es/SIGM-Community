/**
 * LICENCIA LGPL:
 * 
 * Esta librería es Software Libre; Usted puede redistribuirla y/o modificarla
 * bajo los términos de la GNU Lesser General Public License (LGPL) tal y como 
 * ha sido publicada por la Free Software Foundation; o bien la versión 2.1 de 
 * la Licencia, o (a su elección) cualquier versión posterior.
 * 
 * Esta librería se distribuye con la esperanza de que sea útil, pero SIN 
 * NINGUNA GARANTÍA; tampoco las implícitas garantías de MERCANTILIDAD o 
 * ADECUACIÓN A UN PROPÓSITO PARTICULAR. Consulte la GNU Lesser General Public 
 * License (LGPL) para más detalles
 * 
 * Usted debe recibir una copia de la GNU Lesser General Public License (LGPL) 
 * junto con esta librería; si no es así, escriba a la Free Software Foundation 
 * Inc. 51 Franklin Street, 5º Piso, Boston, MA 02110-1301, USA o consulte
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.document;

import java.io.InputStream;

import es.accv.arangi.base.exception.document.HashingException;


/**
 * Interfaz que representa a un documento que va a ser firmado.<br><br>
 * Los procesos de firma requieren que el documento pueda resumirse en un hash,
 * que no siempre representa al documento completo: caso de PDF o firma en XML.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public interface IDocument {

	/**
	 * Método que obtiene el hash del documento con el algoritmo de hashing
	 * por defecto: SHA-1.
	 * 
	 * @return Resultado de aplicar el algoritmo SHA-1 al contenido del documento.
	 * @throws HashingException Error en el proceso de hashing
	 */
	public byte[] getHash () throws HashingException;
	
	/**
	 * Método que obtiene el hash del documento con el algoritmo de hashing
	 * pasado como parámetro.
	 * 
	 * @param hashingAlgorithm Algoritmo de hashing empleado para obtener el hash. Pueden
	 * encontrarse las constantes de dichos algoritmos en la clase {@link es.accv.arangi.base.algorithm.HashingAlgorithm HashingAlgorithm}
	 * @return Resultado de aplicar el algoritmo indicado al contenido del documento.
	 * @throws HashingException Error en el proceso de hashing
	 */
	public byte[] getHash (String hashingAlgorithm) throws HashingException;
	
	/**
	 * Método que devuelve un stream de lectura al contenido del documento.
	 * 
	 * @return Stream de lectura. En alguna situación se puede obtener un valor nulo si
	 * es imposible obtener el stream de lectura.
	 */
	public InputStream getInputStream ();

}
