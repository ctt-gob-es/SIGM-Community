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
package es.accv.arangi.base.request;

import java.io.IOException;
import java.security.PublicKey;

import org.apache.log4j.Logger;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.exception.CertificateRequestException;

/**
 * Clase con los métodos que debe cumplir una petición de certificado (CSR). Los 
 * tipos de CSR aceptados son:
 * <ul>
 * 	<li>PKCS#10: es el formato más utilizado y viene definido en la 
 * 	<a href="https://tools.ietf.org/html/rfc2986">RFC-2986</a>.</li>
 * 	<li>Certificate Request Message Format (CRMF): formato utilizado por las las 
 * 	librerías criptográficas javascript de Mozilla.</li>
 * 	<li>Signed Public Key And Challenge (SPKAC): formato generado por el tag
 * 	'keygen' de HTML5.</li>
 * </ul>
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 */
public abstract class CertificateRequest extends ArangiObject{

	/*
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(CertificateRequest.class);
	
	//-- Métodos estáticos
	
	/**
	 * Obtiene la instancia adecuada para el parámetro
	 * 
	 * @param certificateRequestPEMBase64 Petición de certificado en base64 o
	 * formato PEM (éste último formato lo utiliza PKCS#10)
	 * @return Instancia adecuada para el parámetro
	 * @throws IOException El fichero pasado no tiene la estructura de una 
	 * 	petición de certificado reconocida (PKCS#10 o CRMF)
	 */
	public static CertificateRequest getInstance (String certificateRequestPEMBase64) throws IOException {
		logger.debug("[CertificateRequest.getInstance]::Entrada::" + certificateRequestPEMBase64);
		
		String errorPkcs10, errorCrmf;
		
		//-- Intentar como PKCS#10
		PKCS10Request pkcs10;
		try {
			pkcs10 = new PKCS10Request(certificateRequestPEMBase64);
			return pkcs10;
		} catch (IOException e) {
			logger.info("[CertificateRequest.getInstance]::El fichero no parece un PKCS#10: " + e.getMessage());
			errorPkcs10 = e.getMessage();
		}
		
		//-- Intentar como CRMF
		try {
			CRMFRequest crmf = new CRMFRequest(certificateRequestPEMBase64);
			return crmf;
		} catch (IOException e) {
			logger.info("[CertificateRequest.getInstance]::El fichero no parece un CRMF: " + e.getMessage());
			errorCrmf = e.getMessage();
		}
		
		//-- Intentar como SPKAC
		try {
			SPKACRequest spkac = new SPKACRequest(certificateRequestPEMBase64);
			return spkac;
		} catch (IOException e) {
			logger.info("[CertificateRequest.getInstance]::El fichero no parece un SPKAC: " + e.getMessage());
			errorCrmf = e.getMessage();
		}
		
		//-- No tiene ninguno de los formatos -> lanzar una excepción
		logger.debug("[CertificateRequest.getInstance]::El fichero no tiene un formato de petición de certificado reconocible");
		throw new IOException("El fichero no tiene un formato de petición de certificado reconocible.\nPKCS#10: " + errorPkcs10 + "\nCRMF: " + errorCrmf);
		
	}
	
	//-- Métodos abstractos
	
	/**
	 * Obtiene la clave publica contenida en la petición
	 * 
	 * @return Clave Pública
	 * @throws CertificateRequestException Error obteniendo la clave pública. La 
	 * 	excepción que originó el error vendrá anidada en ésta.
	 */
	public abstract PublicKey getPublicKey() throws CertificateRequestException;
	
	/**
	 * Obtiene el tamaño (número de bits) de la clave pública de 
	 * la petición.
	 * 
	 * @return Tamaño de la clave pública 
	 * @throws CertificateRequestException Error obteniendo la clave pública. La 
	 * 	excepción que originó el error vendrá anidada en ésta.
	 */
	public abstract int getPublicKeyLength() throws CertificateRequestException;
	
	/**
	 * Obtiene el contenido de la petición del certificado
	 * 
	 * @return Contenido de la petición del certificado
	 */
	public abstract byte[] getEncoded();

}
