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

import iaik.pkcs.pkcs11.objects.PublicKey;
import iaik.pkcs.pkcs11.objects.RSAPublicKey;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.util.ASN1Dump;

import es.accv.arangi.base.device.util.pkcs11.CertificationRequestHelper;
import es.accv.arangi.base.device.util.pkcs11.SignUtil;
import es.accv.arangi.base.device.util.pkcs11.TokenPublicKey;

public class TokenPublicKey implements java.security.PublicKey {
  
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -7878441804218507259L;
  
  
  /**
   * Loggger de clase
   */
  private static Logger logger = Logger.getLogger(TokenPublicKey.class);
  
  
  
  private PublicKey publicKey = null;
  private java.security.PublicKey sunPublicKey = null;
  
  
  
  
  public TokenPublicKey( PublicKey key ){
    this.publicKey = key;
    this.sunPublicKey = convertToSunKey( (RSAPublicKey) key );
  }
  
  

  public String getAlgorithm() {
    return CertificationRequestHelper.getAlgorithmId(SignUtil.SHA1WITH_RSA.toUpperCase()).toString();
  }

  
  
  public byte[] getEncoded() {
    
    logger.info("IN:");
    logger.debug("getPublicKey():\n" + getPublicKey());
    
    byte[] ab = sunPublicKey.getEncoded();
    logger.info("OUT: " + ASN1Dump.dumpAsString(sunPublicKey) );
    SignUtil.logArray(ab);
    
    return ab;
    
  }
  

  public String getFormat() {
    // TODO Auto-generated method stub
    return null;
  }
  
  
  private java.security.PublicKey convertToSunKey( RSAPublicKey pk ){
    
//    BigInteger modulo     = new BigInteger( pk.getModulus().toString(false), 16 );
//    BigInteger exponente  = new BigInteger( pk.getPublicExponent().toString(false), 16 );
    BigInteger modulo     = new BigInteger( 1, pk.getModulus().getByteArrayValue() );
    BigInteger exponente  = new BigInteger( 1, pk.getPublicExponent().getByteArrayValue() );
    
    RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modulo, exponente);

    java.security.PublicKey sunPK = null;
    try {
      
      KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
      sunPK = keyFactory.generatePublic(publicSpec);
      
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchProviderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return sunPK;
    
  }
  
  public RSAPublicKey getPublicKey(){
    return (RSAPublicKey) this.publicKey;
  }
  
  public java.security.PublicKey getSunPublicKey(){
    return this.sunPublicKey;
  }
  
  public String toString(){
    return getPublicKey().toString();
    
  }

}
