package es.accv.arangi.base.device.util.pkcs11;

import iaik.pkcs.pkcs11.Mechanism;
import iaik.pkcs.pkcs11.Session;
import iaik.pkcs.pkcs11.TokenException;
import iaik.pkcs.pkcs11.objects.Key;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;

import es.accv.arangi.base.device.util.pkcs11.SignatureEngine;
import es.accv.arangi.base.device.util.pkcs11.TokenPrivateKey;
import es.accv.arangi.base.device.util.pkcs11.TokenPublicKey;


/**
 * This is an implementation of a JCA Signature class that uses the PKCS#11
 * wrapper to create the signature. This implementation hashes outside the token
 * (i.e. in software) and support only signing but not verification.
 * 
 * @author <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
 * @version 0.1
 */
public class SignatureEngine extends Signature {
  
  /**
   * Loggger de clase
   */
  private static Logger logger = Logger.getLogger(SignatureEngine.class);
  
  
  

  /**
   * The session that this object uses for signing with the token.
   */
  protected Session session_;

  /**
   * The mechanism that this object uses for signing with the token.
   */
  protected Mechanism signatureMechanism_;

  /**
   * The PKCS#11 key that this object uses for signing with the token.
   */
  protected Key signatureKey_;
  
  /**
   * The PKCS#11 key that this object uses for verify with the token.
   */
  protected Key verifyKey_;

  /**
   * The digest engine used to hash the data.
   */
  protected MessageDigest digestEngine_;
  
//  protected byte[] tmp = null;
  


  /**
   * Creates a new signature engine that uses the given parameters to create the
   * signature on the PKCS#11 token.
   * 
   * @param algorithmName
   *          The name of the signature algorithm. This class does not interpret
   *          this name; it uses it as is.
   * @param session
   *          The PKCS#11 session to use for signing. It must have the
   *          permissions to sign with the used private key; e.g. it may require
   *          a user session.
   * @param signatureMechanism
   *          The PKCS#11 mechanism to use for signing; e.g. Mechanism.RSA_PKCS.
   * @param md
   *          The hash 
   * @exception NoSuchAlgorithmException
   *              If the hash algorithm is not available.
   */
  public SignatureEngine(String algorithmName, Session session, Mechanism signatureMechanism, MessageDigest md) throws NoSuchAlgorithmException {
    
    super(algorithmName);
    
    session_ = session;
    signatureMechanism_ = signatureMechanism;

    // we do digesting outside the card, because some cards do not support
    // on-card hashing
    digestEngine_ = md;
    
  }


  /**
   * SPI: see documentation of java.security.Signature.
   */
  protected java.lang.Object engineGetParameter(String name) throws InvalidParameterException {
    throw new UnsupportedOperationException();
  }


  /**
   * SPI: see documentation of java.security.Signature.
   */
  protected void engineSetParameter(String param, java.lang.Object value) throws InvalidParameterException {
    throw new UnsupportedOperationException();
  }


  /**
   * SPI: see documentation of java.security.Signature.
   */
  protected void engineInitSign(java.security.PrivateKey privateKey) throws InvalidKeyException {
    if (!(privateKey instanceof TokenPrivateKey)) {
      throw new InvalidKeyException("Private key must be an instance of TokenPrivateKey");
    }
    signatureKey_ = ((TokenPrivateKey) privateKey).getTokenPrivateKey();
  }


  /**
   * SPI: see documentation of java.security.Signature.
   */
  protected byte[] engineSign() throws SignatureException {
    
    // According to PKCS#11 building the DigestInfo structure must be done
    // off-card
    DigestInfo digestInfoEngine = new DigestInfo( new AlgorithmIdentifier(new ASN1ObjectIdentifier("1.3.14.3.2.26")), digestEngine_.digest() );
    byte[] toBeEncrypted;
	try {
		toBeEncrypted = digestInfoEngine.getEncoded(ASN1Encoding.DER);
	} catch (IOException e) {
		throw new SignatureException(e);
	}
    
    byte[] signatureValue = null;
    try {

      // -- Inicializamos para firmar
      session_.signInit(signatureMechanism_, signatureKey_);
      
      // -- Firmamos los datos
      signatureValue = session_.sign(toBeEncrypted);
      
    } catch (TokenException ex) {
      ex.printStackTrace();
      throw new SignatureException(ex.toString());
    }
    
    return signatureValue;
  }


  /**
   * SPI: see documentation of java.security.Signature.
   */
  protected void engineInitVerify(java.security.PublicKey publicKey) throws InvalidKeyException {
    if (!(publicKey instanceof TokenPublicKey)) {
      throw new InvalidKeyException("Private key must be an instance of TokenPublicKey");
    }
    
    this.verifyKey_ = ((TokenPublicKey)publicKey).getPublicKey();
  }
  

  /**
   * SPI: see documentation of java.security.Signature.
   */
  protected boolean engineVerify(byte[] signatureValue) throws SignatureException {
    
    // According to PKCS#11 building the DigestInfo structure must be done
    // off-card
    DigestInfo digestInfoEngine = new DigestInfo( new AlgorithmIdentifier(new ASN1ObjectIdentifier("1.3.14.3.2.26")), digestEngine_.digest() );
    byte[] hash;
	try {
		hash = digestInfoEngine.getEncoded(ASN1Encoding.DER);
	} catch (IOException e) {
		throw new SignatureException(e);
	}

    try {
      
      //-- Inicializa para verificacion
      session_.verifyInit(signatureMechanism_, verifyKey_);
      
      //-- Verifica los datos firmados
      session_.verify( hash, signatureValue );
      
      //-- Si no hay excepcion...
      return true;
      
    } catch (TokenException ex) {
      ex.printStackTrace();
      throw new SignatureException(ex.toString());
    }

  }


  
  /**
   * SPI: see documentation of java.security.Signature.
   */
  protected void engineUpdate(byte dataByte) throws SignatureException {
    
    logger.debug("IN (byte)");
    digestEngine_.update(dataByte);
    
  }

  
  

  /**
   * SPI: see documentation of java.security.Signature.
   */
  protected void engineUpdate(byte[] data, int offset, int length) throws SignatureException {
    
    logger.debug("IN (byte[], offset, length): (" + data + ", " + offset + ", " + length + ")");
//    logger.debug("data.length: " + data.length);
//    tmp = new byte[length];
//    System.arraycopy(data, offset, tmp, offset, offset+length);
    
    digestEngine_.update(data, offset, length);
    
  }

}
