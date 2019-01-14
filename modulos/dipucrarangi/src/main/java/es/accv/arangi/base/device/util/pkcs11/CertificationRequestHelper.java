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
package es.accv.arangi.base.device.util.pkcs11;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.device.util.CSRUtil;
import es.accv.arangi.base.util.Util;


public class CertificationRequestHelper extends ArangiObject{


  /**
   * Loggger de clase
   */
  private static Logger logger = Logger.getLogger(CertificationRequestHelper.class);
  
  
  private static Hashtable<String,ASN1ObjectIdentifier> algorithms = new Hashtable<String,ASN1ObjectIdentifier>();
  private static Hashtable<ASN1ObjectIdentifier,String> oids = new Hashtable<ASN1ObjectIdentifier,String>();

  static {
      algorithms.put("MD2WITHRSAENCRYPTION", new ASN1ObjectIdentifier("1.2.840.113549.1.1.2"));
      algorithms.put("MD2WITHRSA", new ASN1ObjectIdentifier("1.2.840.113549.1.1.2"));
      algorithms.put("MD5WITHRSAENCRYPTION", new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"));
      algorithms.put("MD5WITHRSA", new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"));
      algorithms.put("RSAWITHMD5", new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"));
      algorithms.put("SHA1WITHRSAENCRYPTION", new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"));
      algorithms.put("SHA1WITHRSA", new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"));
      algorithms.put("SHA224WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha224WithRSAEncryption);
      algorithms.put("SHA224WITHRSA", PKCSObjectIdentifiers.sha224WithRSAEncryption);
      algorithms.put("SHA256WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha256WithRSAEncryption);
      algorithms.put("SHA256WITHRSA", PKCSObjectIdentifiers.sha256WithRSAEncryption);
      algorithms.put("SHA384WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha384WithRSAEncryption);
      algorithms.put("SHA384WITHRSA", PKCSObjectIdentifiers.sha384WithRSAEncryption);
      algorithms.put("SHA512WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha512WithRSAEncryption);
      algorithms.put("SHA512WITHRSA", PKCSObjectIdentifiers.sha512WithRSAEncryption);
      algorithms.put("RSAWITHSHA1", new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"));
      algorithms.put("RIPEMD160WITHRSAENCRYPTION", new ASN1ObjectIdentifier("1.3.36.3.3.1.2"));
      algorithms.put("RIPEMD160WITHRSA", new ASN1ObjectIdentifier("1.3.36.3.3.1.2"));
      algorithms.put("SHA1WITHDSA", new ASN1ObjectIdentifier("1.2.840.10040.4.3"));
      algorithms.put("DSAWITHSHA1", new ASN1ObjectIdentifier("1.2.840.10040.4.3"));
      algorithms.put("SHA1WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA1);
      algorithms.put("ECDSAWITHSHA1", X9ObjectIdentifiers.ecdsa_with_SHA1);

      //
      // reverse mappings
      //
      oids.put(new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"), "SHA1WITHRSA");
      oids.put(PKCSObjectIdentifiers.sha224WithRSAEncryption, "SHA224WITHRSA");
      oids.put(PKCSObjectIdentifiers.sha256WithRSAEncryption, "SHA256WITHRSA");
      oids.put(PKCSObjectIdentifiers.sha384WithRSAEncryption, "SHA384WITHRSA");
      oids.put(PKCSObjectIdentifiers.sha512WithRSAEncryption, "SHA512WITHRSA");

      oids.put(new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"), "MD5WITHRSA");
      oids.put(new ASN1ObjectIdentifier("1.2.840.113549.1.1.2"), "MD2WITHRSA");
      oids.put(new ASN1ObjectIdentifier("1.2.840.10040.4.3"), "DSAWITHSHA1");
      oids.put(X9ObjectIdentifiers.ecdsa_with_SHA1, "DSAWITHSHA1");
      
  }
  



  /**
   * Obtiene una peticion de certificado asociada a partir de los parametros.
   * 
   * @param signatureAlgorithmString
   * @param rsaKeys
   * @param subjectDN
   * @param subjectAlternativeDN
   * @return PKCS#10
   * @throws Exception 
   */
  public static PKCS10CertificationRequest getCertificationRequest(String signatureAlgorithmString, KeyPair rsaKeys, String subjectDN, String subjectAlternativeDN) throws Exception {

    PrivateKey privateKey = rsaKeys.getPrivate();
    PublicKey publicKey = rsaKeys.getPublic();
    
    // -- Obtenemos el nombre estandar
    X500Name x500Name = es.accv.arangi.base.certificate.Certificate.stringToBcX500Name( subjectDN );
    logger.debug("x500Name: " + x500Name);
      
    //-- Parametros para la creacion del bloque de extensiones
    ExtensionsGenerator extGen = new ExtensionsGenerator();
    extGen.addExtension(Extension.subjectAlternativeName, false, CSRUtil.getGeneralNamesFromAltName(subjectAlternativeDN));
    
    // Hacer el publicKeyInfo
    SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(
            new ByteArrayInputStream(publicKey.getEncoded())).readObject());

    // Generate PKCS10 certificate request builder
    PKCS10CertificationRequestBuilder builder = new PKCS10CertificationRequestBuilder(x500Name, spki);
    builder.addAttribute(Extension.subjectAlternativeName, extGen.generate());
    
    // Hace el contentSigner
    ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider(CRYPTOGRAPHIC_PROVIDER_NAME).build(privateKey);
    
    // Obtener el pkcs10
    PKCS10CertificationRequest req = builder.build(signer);
    logger.info("req: " + req);

    return req;

  }

  /**
   * Obtiene la cadena en base64 asociada al P12 del certificado creado.
   * 
   * @param rsaKeys
   * @param cert
   * @param alias
   * @param pwd
   * @return PKCS#12 en base64
   * @throws Exception
   */
  public static String getP12Base64(KeyPair rsaKeys, X509Certificate cert, String alias, String pwd) throws Exception {

    if (cert == null) {
      throw new Exception("Recibido certificado nulo.");
    }

    // Generamos la cadena de certificados.
    Certificate[] chain = new Certificate[1];
    chain[0] = cert;

    // Generamos el KeyStore
    KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
    ks.load(null, pwd.toCharArray());

    // Importamos la clave y el certificado.
    ks.setKeyEntry(alias, rsaKeys.getPrivate(), null, chain);

    // Creamos el objeto P12
    ByteArrayOutputStream baosPKCS12 = new ByteArrayOutputStream();
    ks.store(baosPKCS12, pwd.toCharArray());
    baosPKCS12.close();

    byte[] abPKCS12 = baosPKCS12.toByteArray();
    String pkcs12B64 = new String(Util.encodeBase64(abPKCS12));

    // Para evitar el error del InvalidKey, es necesario esto:
    ks.load(new ByteArrayInputStream(abPKCS12), pwd.toCharArray());

    return pkcs12B64;

  }
  
  
  
  /**
   * Obtiene el identificador del algoritmo a parir del nombre.
   * 
   * @param algorithm
   * @return Identificador algoritmo
   */
  public static AlgorithmIdentifier getAlgorithmId( String algorithm ){
    
    logger.info("IN: " + Arrays.asList(new Object[] { algorithm }));
    
    AlgorithmIdentifier algId = null;
    
    if ( algorithm != null && !"".equals(algorithm) ){
      ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) algorithms.get(algorithm.toUpperCase());
      algId = new AlgorithmIdentifier(oid);
    }
    
    logger.info("retorna: " + algId);
    
    return algId;
  } 

}