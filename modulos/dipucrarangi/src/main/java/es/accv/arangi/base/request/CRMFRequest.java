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
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

import sun.security.rsa.RSAPublicKeyImpl;

import com.novosec.pkix.asn1.crmf.CertRequest;

import es.accv.arangi.base.exception.CertificateRequestException;
import es.accv.arangi.base.util.Util;

/**
 * Representación de una petición de certificado en formato Certification Request
 * Message Format (CRMF) según la <a href="http://www.ietf.org/rfc/rfc4211.txt">
 * RFC-4211</a>.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 *
 */
public class CRMFRequest extends CertificateRequest {
	
	/*
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(CRMFRequest.class);
	
	CertRequest crmfRequest;
	
	byte[] crmfRequestBytes; 
	
	//-- Constructor
	
	/**
	 * Constructor al que se le pasa el fichero CRMF en base64
	 * 
	 * @param crmfBase64 CRMF en base64
	 * @throws IOException Excepción si el CRMF no tiene la estructura
	 * 	correcta
	 */
	public CRMFRequest (String crmfBase64) throws IOException {
		super();
		
	    logger.debug("[PKCS10Request]::Entrada::" + crmfBase64);
	    
	    if ( crmfBase64 == null || crmfBase64.equals("") ) {
	      throw new IOException("El CRMF está vacío");
	    }
	    
	    try {
		    this.crmfRequestBytes = Util.decodeBase64(crmfBase64);
		    
		    ASN1InputStream in = new ASN1InputStream(this.crmfRequestBytes);
		    ASN1Sequence crmfSeq = (ASN1Sequence) in.readObject();
		    ASN1Sequence reqSeq = (ASN1Sequence) ((ASN1Sequence) crmfSeq.getObjectAt(0)).getObjectAt(0);
		    crmfRequest = new CertRequest(reqSeq);
		    logger.debug("[PKCS10Request]::Obtenido el CRMF"); 
	    } catch (Exception e) {
	    	throw new IOException("No se puede parsear el CRMF: " + e.getMessage());
	    }

	}
	
	//-- Implementación de la clase abstracta
	
	@Override
	public PublicKey getPublicKey() throws CertificateRequestException {
		SubjectPublicKeyInfo keyInfo = crmfRequest.getCertTemplate().getPublicKey();
		try {
			return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyInfo.getEncoded(ASN1Encoding.DER)));
		} catch (InvalidKeySpecException e) {
			logger.info("[CRMFRequest.getPublicKey]::Error por especificaciones de la clave pública no válidas");
			throw new CertificateRequestException ("Error por especificaciones de la clave pública no válidas", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[CRMFRequest.getPublicKey]::No existe el algoritmo de la clave pública");
			throw new CertificateRequestException ("No existe el algoritmo de la clave pública", e);
		} catch (IOException e) {
			logger.info("[CRMFRequest.getPublicKey]::No ha sido posible obtener el array de bytes de la clave pública");
			throw new CertificateRequestException ("No ha sido posible obtener el array de bytes de la clave pública", e);
		}
	}

	@Override
	public int getPublicKeyLength() throws CertificateRequestException {
		return ((RSAPublicKeyImpl)getPublicKey()).getModulus().bitLength();
	}
	
	@Override
	public byte[] getEncoded() {
		return this.crmfRequestBytes;
	}


}
