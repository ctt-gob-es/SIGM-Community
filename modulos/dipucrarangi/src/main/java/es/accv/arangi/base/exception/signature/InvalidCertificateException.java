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
package es.accv.arangi.base.exception.signature;

/**
 * Se trata de realizar una firma longeva con un certificado revocado o caducado. 
 * La firma longeva incluye información de validación, y ésta no puede indicar que 
 * el certificado está revocado.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class InvalidCertificateException extends es.accv.arangi.base.exception.SignatureException {

	private static final long serialVersionUID = 1L;
	
	private int errorNumber; 

	public InvalidCertificateException(int errorNumber) {
		super();
		this.errorNumber = errorNumber;
	}

	public InvalidCertificateException(String message, int errorNumber, Throwable cause) {
		super(message, cause);
		this.errorNumber = errorNumber;
	}

	public InvalidCertificateException(String message, int errorNumber) {
		super(message);
		this.errorNumber = errorNumber;
	}

	public InvalidCertificateException(Throwable cause, int errorNumber) {
		super(cause);
		this.errorNumber = errorNumber;
	}

	/**
	 * Obtiene el número del error que ocasionó la excepción. Las constantes que 
	 * determinan el resultado de una validación se encuentran en
	 * {@link es.accv.arangi.base.util.validation.ValidationResult ValidationResult}
	 * @return número del error
	 */
	public int getErrorNumber() {
		return errorNumber;
	}

}
