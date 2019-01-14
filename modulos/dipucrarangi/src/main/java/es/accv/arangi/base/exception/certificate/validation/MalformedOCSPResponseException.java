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
package es.accv.arangi.base.exception.certificate.validation;

import es.accv.arangi.base.exception.CertificateException;

/**
 * El objeto que se intenta pasar como respuesta OCSP no lo es o es una respuesta
 * OCSP que no se corresponde a la <a href="http://www.ietf.org/rfc/rfc2560.txt" target="rfc">RFC-2560</a>.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class MalformedOCSPResponseException extends CertificateException {

	public MalformedOCSPResponseException() {
		super();
	}

	public MalformedOCSPResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	public MalformedOCSPResponseException(String message) {
		super(message);
	}

	public MalformedOCSPResponseException(Throwable cause) {
		super(cause);
	}

}
