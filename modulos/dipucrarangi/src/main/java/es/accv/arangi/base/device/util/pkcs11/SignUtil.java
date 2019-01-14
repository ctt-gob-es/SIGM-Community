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

import iaik.pkcs.pkcs11.Mechanism;
import iaik.pkcs.pkcs11.Session;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import org.apache.log4j.Logger;



/**
 * @author faparicio
 *
 */
public class SignUtil {
  
  
  /**
   * Loggger de clase
   */
  private static Logger logger = Logger.getLogger(SignUtil.class);

  
  public static final String SHA1WITH_RSA = "SHA1withRSA";
  
  
  /**
   * Firma una array de bytes.
   * 
   * @param session
   * @param privateKey
   * @param dataToSign
   * @return Firma
   */
  public static byte[] generateSignature( Session session, PrivateKey privateKey, byte[] dataToSign ) {
    
    logger.debug("IN");
    byte[] signature = null;
    Signature sig = null;
    
    try {
      
      sig = getSignatureEngine(session, privateKey);
      
      sig.initSign(privateKey);
      sig.update(dataToSign);
      
      signature = sig.sign();
      
      logger.debug("Antes de pasar a DER...");
      logArray(signature);

    
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchProviderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SignatureException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    logger.debug("OUT");
    return signature;
  }


  /**
   * Verifica la firma para un array de bytes.
   * 
   * @param session
   * @param publicKey
   * @param originalData
   * @param signature
   * @return Cierto si la firma es correcta
   */
  public static boolean verifySignature( Session session, PublicKey publicKey, byte[] originalData, byte[] signature ) {
//    logger.debug("IN: originalData:\n" + new String(Base64.encodeBase64(originalData)));
//    logArray(signature);
//    logArray(originalData);
    
    boolean isValid = false;
    try {
      
//      MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
//      DigestInfo digestInfoEngine = new DigestInfo( CertificationRequestHelper.getAlgorithmId("SHA1WithRSA"), md.digest(originalData) );
//      logger.debug("ASN1 digestInfoEngine: " + ASN1Dump.dumpAsString(digestInfoEngine));
//      logger.debug("digestInfoEngine.getDEREncoded(): ");
//      logArray(digestInfoEngine.getDEREncoded());
      
      Signature sig = getSignatureEngine(session, publicKey);
      
      sig.initVerify(publicKey);
      sig.update(originalData);
      
      isValid = sig.verify(signature);
      
    } catch (SignatureException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchProviderException e) {
      e.printStackTrace();
    }
    
    logger.debug("isValid: " + isValid);
    return isValid;

  }


  /**
   * @param ab
   */
  public static void logArray(byte[] ab) {
    StringBuffer sb = new StringBuffer();
    //-- Mostramos el array ----------------------------------------
    logger.debug("########################################\n{");
    for (int i = 0; i < ab.length; i++) {
      sb.append(ab[i]);
      if ( i+1 < ab.length ){ sb.append(", "); }
    }
    logger.debug(sb.toString());
    logger.debug("}\n########################################\n\n");
    //--------------------------------------------------------------

  }
  
  
  
  /**
   * Obtene una nstancia del motor de firma y verificacion.
   * 
   * @param session
   * @param key
   * @return
   * @throws NoSuchAlgorithmException
   * @throws NoSuchProviderException
   */
  private static Signature getSignatureEngine(Session session, Object key) throws NoSuchAlgorithmException, NoSuchProviderException {
    
    Signature sig;
    
    if ( key != null && (key instanceof TokenPrivateKey || key instanceof TokenPublicKey) ){
      logger.debug("Claves generadas en la tarjeta.");
      MessageDigest md = MessageDigest.getInstance("SHA1");
      sig = new SignatureEngine(SHA1WITH_RSA, session, Mechanism.RSA_PKCS, md);
      
    }else{
      logger.debug("Claves generadas fuera de la tarjeta.");
      sig = Signature.getInstance(SHA1WITH_RSA);
    }
    
    return sig;
    
  }
  


}
