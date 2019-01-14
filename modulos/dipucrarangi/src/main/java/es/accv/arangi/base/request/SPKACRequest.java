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
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.mozilla.SignedPublicKeyAndChallenge;

import es.accv.arangi.base.exception.CertificateRequestException;
import es.accv.arangi.base.util.Util;

/**
 * Representación de una petición de certificado en formato Signed Public Key 
 * and Challenge.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 *
 */
public class SPKACRequest extends CertificateRequest {
	
	/*
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(SPKACRequest.class);
	
	SignedPublicKeyAndChallenge spkacRequest;
	
	//-- Constructor
	
	/**
	 * Constructor al que se le pasa el fichero SPKAC en base64.
	 * 
	 * @param spkacB64 SPKAC en base 64
	 * @throws IOException Excepción si el SPKAC no tiene la estructura
	 * 	correcta
	 */
	public SPKACRequest (String spkacB64) throws IOException {
		super();
		
	    logger.debug("[SPKACRequest]::Entrada::" + spkacB64);
	    
	    if ( spkacB64 == null || spkacB64.equals("") ) {
	      throw new IOException("El fichero SPKAC es nulo o está vacío");
	    }
	    
	    try {
	    	spkacRequest = new SignedPublicKeyAndChallenge(Util.decodeBase64(spkacB64));
	    } catch (Exception e) {
	    	logger.info("No es un SPKAC válido", e);
	    	throw new IOException("El fichero no parece un SPKAC válido");
	    }

	}

	//-- Implementación de la clase abstracta
	
	@Override
	public PublicKey getPublicKey() throws CertificateRequestException {
		try {
			return spkacRequest.getPublicKey(CRYPTOGRAPHIC_PROVIDER_NAME);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[SPKACRequest.getPublicKey]::No existe el algoritmo de la clave pública", e);
			throw new CertificateRequestException ("No existe el algoritmo de la clave pública", e);
		} catch (InvalidKeyException e) {
			logger.info("[SPKACRequest.getPublicKey]::La clave pública no es válida", e);
			throw new CertificateRequestException ("La clave pública no es válida", e);
		} catch (NoSuchProviderException e) {
			//-- no se va a dar
			logger.info("[SPKACRequest.getPublicKey]::No existe el proveedor criptográfico BC", e);
			throw new CertificateRequestException ("No existe el proveedor criptográfico BC", e);
		}
	}
	
	@Override
	public int getPublicKeyLength () throws CertificateRequestException {
		return ((RSAPublicKey)getPublicKey()).getModulus().bitLength();
	}

	@Override
	public byte[] getEncoded() {
		try {
			return spkacRequest.getEncoded();
		} catch (IOException e) {
			logger.info("[SPKACRequest.getEncoded]::No se puede pasar el SPKAC a array de bytes", e);
			return null;
		}
	}

}
