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
package es.accv.arangi.base.exception.timestamp;

import es.accv.arangi.base.exception.TimeStampException;

/**
 * El servidor de sello de tiempos devuelve un error en la respuesta.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class ResponseTimeStampException extends TimeStampException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResponseTimeStampException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseTimeStampException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ResponseTimeStampException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ResponseTimeStampException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
