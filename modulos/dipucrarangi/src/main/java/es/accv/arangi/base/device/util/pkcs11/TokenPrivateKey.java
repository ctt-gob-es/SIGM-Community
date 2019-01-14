package es.accv.arangi.base.device.util.pkcs11;


import iaik.pkcs.pkcs11.objects.PrivateKey;
import iaik.pkcs.pkcs11.objects.RSAPrivateKey;

import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;




/**
 * This is an adapter class that allows to use token keys as JCA private keys.
 * An application can use this class whereever an interface requires the
 * application to pass an JCA private key; e.g. for signing.
 *
 * @author <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
 * @version 0.1
 */
public class TokenPrivateKey implements java.security.interfaces.RSAPrivateKey {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 8716752388375049462L;
  
  
  /**
   * Loggger de clase
   */
  private static Logger logger = Logger.getLogger(TokenPrivateKey.class);
  
  
  
  
  /**
   * The PKCS#11 private key of this object.
   */
  protected RSAPrivateKey tokenPrivateKey_;
  
  
  
  private java.security.interfaces.RSAPrivateKey sunPrivateKey = null;
  

  /**
   * Create a new JCA private key that uses the given PKCS#11 private key
   * internally.
   *
   * @param key The PKCS#11 private key that this object refers to.
   */
  public TokenPrivateKey(RSAPrivateKey key) {
    tokenPrivateKey_ = key;
    this.sunPrivateKey = convertToSunKey( (RSAPrivateKey) key );
  }
  
  
  

  
  public java.security.PrivateKey getSunPrivateKey(){
    return this.sunPrivateKey;
  }
  
  
  private java.security.interfaces.RSAPrivateKey convertToSunKey( RSAPrivateKey pk ){
    
    logger.debug("IN");
    
    BigInteger modulo     = new BigInteger( pk.getModulus().toString(false), 16 );
    System.out.println ("modulo: " + modulo.toString(16));
    
    BigInteger moduloi     = new BigInteger(1, pk.getModulus().getByteArrayValue() );
    System.out.println ("moduloi: " + moduloi.toString(16));
    
    BigInteger modulox     = new BigInteger( pk.getModulus().getByteArrayValue() );
    System.out.println ("modulox: " + modulox.toString(16));
    
    BigInteger exponentePublico  = new BigInteger(pk.getPublicExponent().getByteArrayValue() );
    System.out.println ("exponentePublico: " + exponentePublico);

    BigInteger exponentePublicoi  = new BigInteger(1, pk.getPublicExponent().getByteArrayValue() );
    System.out.println ("exponentePublicoi: " + exponentePublicoi);
    
    pk.getPrivateExponent().setSensitive(false);
//    BigInteger exponentePrivado  = new BigInteger( pk.getPrivateExponent().toString(false), 16 );
//    System.out.println ("exponentePrivado: " + exponentePrivado);
    pk.getPrivateExponent().setSensitive(true);
    
//    BigInteger primo1  = new BigInteger( pk.getPrime1().toString(false), 16 );
//    System.out.println ("primo1: " + primo1);
//    
//    BigInteger primo2  = new BigInteger( pk.getPrime2().toString(false), 16 );
//    System.out.println ("primo2: " + primo2);
//    
//    BigInteger exponentePrimo1  = new BigInteger( pk.getExponent1().toString(false), 16 );
//    System.out.println ("exponentePrimo1: " + exponentePrimo1);
//    
//    BigInteger exponentePrimo2  = new BigInteger( pk.getExponent2().toString(false), 16 );
//    System.out.println ("exponentePrimo2: " + exponentePrimo2);
//    
//    BigInteger crtCoefficient  = new BigInteger( pk.getCoefficient().toString(false), 16 );
//    System.out.println ("crtCoefficient: " + crtCoefficient);
//    
//    
//    RSAPrivateCrtKeySpec privateSpec = new RSAPrivateCrtKeySpec(modulo, exponentePublico, exponentePrivado, primo1, primo2, exponentePrimo1, exponentePrimo2, crtCoefficient);  
//
//    java.security.PrivateKey sunPK = null;
//    try {
//      
//      KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
//      sunPK = keyFactory.generatePrivate(privateSpec);
//      
//    } catch (NoSuchAlgorithmException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (InvalidKeySpecException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (NoSuchProviderException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
    logger.debug("Retornamos la misma instancia");
    java.security.interfaces.RSAPrivateKey sunPK = this;
    return sunPK;
    
  }




	public BigInteger getPrivateExponent() {
		
		System.out.println ("Exponent: " + tokenPrivateKey_.getPrivateExponent());
		
		return new BigInteger(1, tokenPrivateKey_.getPrivateExponent().getByteArrayValue() );
	}
	
	
	
	
	public BigInteger getModulus() {
		
		System.out.println ("Modulo: " + tokenPrivateKey_.getModulus().getByteArrayValue().length);
		
		return new BigInteger(1, tokenPrivateKey_.getModulus().getByteArrayValue() );
	}

	  /**
	   * Just returns null.
	   *
	   * @return null.
	   */
	  public String getAlgorithm() {
		  
	    return new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption.getId()).toString();
	  }

	  /**
	   * Just returns null.
	   *
	   * @return null.
	   */
	  public String getFormat() {
	    return null;
	  }

	  /**
	   * Just returns null.
	   *
	   * @return null.
	   */
	  public byte[] getEncoded() {
	    return null;
	  }

	  /**
	   * Returns the PKCS#11 private key object that this object refers to.
	   *
	   * @return The KCS#11 private key object that this object refers to.
	   */
	  public PrivateKey getTokenPrivateKey() {
	    return tokenPrivateKey_ ;
	  }
	  

}
