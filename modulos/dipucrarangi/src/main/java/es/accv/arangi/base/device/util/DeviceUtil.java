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
package es.accv.arangi.base.device.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.jce.provider.X509CertificateObject;

import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class DeviceUtil {

	private static Logger logger = Logger.getLogger(DeviceUtil.class);

	//-- Posiciones en el array del KeyUsage
	public static int KEYUSAGE_DIGITALSIGNATURE = 0;
	public static int KEYUSAGE_NONREPUDIATION   = 1;
	public static int KEYUSAGE_KEYENCIPHERMENT  = 2;
	public static int KEYUSAGE_DATAENCIPHERMENT = 3;
	public static int KEYUSAGE_KEYAGREEMENT     = 4;
	public static int KEYUSAGE_KEYCERTSIGN      = 5;
	public static int KEYUSAGE_CRLSIGN          = 6;
	public static int KEYUSAGE_ENCIPHERONLY     = 7;
	public static int KEYUSAGE_DECIPHERONLY     = 8;

	public static String normalizeString(String s) {
		String retorno = null;
		if (s != null && !"".equals(s)){
			s = s.replaceAll("[àáäâ]","a");
			s = s.replaceAll("[ìíïî]","i");
			s = s.replaceAll("[òóöô]","o");
			s = s.replaceAll("[èéëê]","e");
			s = s.replaceAll("[ùúüû]","u");
			
			s = s.replaceAll("[ÀÁÄÂ]","A");
			s = s.replaceAll("[ÌÍÏÎ]","I");
			s = s.replaceAll("[ÒÓÖÔ]","O");
			s = s.replaceAll("[ÈÉËÊ]","E");
			s = s.replaceAll("[ÙÚÜÛ]","U");
			retorno = s.toUpperCase();
		}
		return retorno;
	}
	
	public static String generateRandomPin(int pinLenght){
		String pin = "";
		for (int i = 0; i < pinLenght; i++) {
			pin = pin + (Math.round((Math.random() * 9)));
		}
		return pin;
	}
	
	public static StringBuffer getFullStackTrace (Exception exception){ 
		StringBuffer traza = new StringBuffer();
		traza.append("\n\t" + exception.getClass().getName() + ": " + exception.getLocalizedMessage() + "\n");
		StackTraceElement[] st = exception.getStackTrace();
		for (int i = 0; i < st.length; i++) {
			traza.append('\t' + ((StackTraceElement) st[i]).toString() + '\n');
		}
		
		//-- Y las trazas de todas las causas que haya
		while ((exception = (Exception)exception.getCause()) != null){
			traza.append("Caused by: " + exception + ": " + exception.getLocalizedMessage() + "\n");
			st = exception.getStackTrace();
			for (int i = 0; i < st.length; i++) {
				traza.append('\t' + ((StackTraceElement) st[i]).toString() + '\n');
			}
		}
		return traza;
	}

	public static X509Certificate readCertPKCS12(byte[] bytesCert, String password, String alias) throws Exception{
		CertificateFactory cf = null;
		X509Certificate x509cert = null;
		try {
			cf = CertificateFactory.getInstance("PKCS12", "BC");
			x509cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(bytesCert));
			return x509cert;
		} catch (NoSuchProviderException e) {
			logger.info("NoSuchProvider: ", e);
			throw new Exception("Error ensamblando certificado", e);
		} catch (CertificateException e) {
			logger.info("CertificateException: ", e);
			throw new Exception("Error ensamblando certificado", e);
		}
	}
	
	/**
	 * Realiza la transformación del hash previa a su cifrado mediante RSA para
	 * completar la firma
	 * 
	 * @param algOid OID del algoritmo de hashing
	 * @param digest Hash
	 * @return Hash formateado
	 * @throws IOException
	 */
	public static byte[] getSignData(ObjectIdentifier algOid, byte[] digest) throws IOException {
	  
	  DerOutputStream out = new DerOutputStream();
	  DerOutputStream algS = new DerOutputStream();
	  DerOutputStream oidS = new DerOutputStream();
	  oidS.putOID(algOid);
	
	  DerOutputStream nullS = new DerOutputStream();
	  nullS.putNull();
	
	  algS.putSequence(new DerValue[] { new DerValue(oidS.toByteArray()), new DerValue(nullS.toByteArray()) });
	
	  DerOutputStream digestS = new DerOutputStream();
	  digestS.putOctetString(digest);
	  
	  DerValue[] dv = new DerValue[] { new DerValue(algS.toByteArray()), new DerValue(digestS.toByteArray()) };
	  out.putSequence(dv);
	  
	  return out.toByteArray();
	}

	  /**
	   * Obtiene un X509CertificateObject a partir de un certificado.
	   * 
	   * @param x509cer Certificado
	   * @return objeto X509CertificateObject
	   */
	  public static X509CertificateObject getX509CertificateObject( X509Certificate x509cer ){
	    
	    X509CertificateObject x509co = null;
	    ASN1InputStream asn1Is = null;
	    ASN1Sequence seq = null; 
	    
	    try {
	      
	      asn1Is = new ASN1InputStream(new ByteArrayInputStream(x509cer.getEncoded()));
	      ASN1Primitive derObject = asn1Is.readObject();
	      seq = ASN1Sequence.getInstance(derObject);
	      
	    } catch (IOException e) {
	      logger.info("WARN: ", e);
	      
	    } catch (CertificateEncodingException e) {
	      logger.info("WARN: ", e);
	    } finally {
	    	if (asn1Is != null) {
	    		try {
					asn1Is.close();
				} catch (IOException e) {
					logger.info("WARN: ", e);
				}
	    	}
	    }
	    
	    org.bouncycastle.asn1.x509.Certificate x509cs = org.bouncycastle.asn1.x509.Certificate.getInstance( seq );
	    x509co = null;
	    try {
			x509co = new X509CertificateObject(x509cs);
		} catch (CertificateParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return x509co;
	  }

	  /**
	   * Obtiene un Map a partir de la cadena de campos (clave=valor) del DN
	   * 
	   * @param dn DN
	   * @return Map con las entradas del DN
	   */
	  public static Map getMapFromDN( String dn ){
	    
	    Map map = new HashMap();
	    
	    if ( dn != null && !"".equals(dn) ){
	      String[] dnParts = dn.split(",");
	      for (int i = 0; i < dnParts.length; i++) {
	        int posSep = dnParts[i].indexOf("=");
	        String field = dnParts[i].substring(0, posSep);
	        String value = dnParts[i].substring(posSep+1);
	        logger.debug("field: " + field + "\tvalue: " + value);
	        
	        //-- Añadimos el valor del campo
	        map.put(field, value);
	        
	      }
	    }
	    
	    return map;
	    
	  }

}
