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
import iaik.pkcs.pkcs11.MechanismInfo;
import iaik.pkcs.pkcs11.Module;
import iaik.pkcs.pkcs11.Session;
import iaik.pkcs.pkcs11.Slot;
import iaik.pkcs.pkcs11.Token;
import iaik.pkcs.pkcs11.TokenException;
import iaik.pkcs.pkcs11.objects.Attribute;
import iaik.pkcs.pkcs11.objects.CharArrayAttribute;
import iaik.pkcs.pkcs11.objects.Key;
import iaik.pkcs.pkcs11.objects.KeyPair;
import iaik.pkcs.pkcs11.objects.RSAPrivateKey;
import iaik.pkcs.pkcs11.objects.RSAPublicKey;
import iaik.pkcs.pkcs11.objects.X509PublicKeyCertificate;
import iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE;
import iaik.pkcs.pkcs11.wrapper.PKCS11;
import iaik.pkcs.pkcs11.wrapper.PKCS11Constants;
import iaik.pkcs.pkcs11.wrapper.PKCS11Exception;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.device.CertificateInvalidException;
import es.accv.arangi.base.exception.device.DeletingObjectException;
import es.accv.arangi.base.exception.device.DeviceFullException;
import es.accv.arangi.base.exception.device.KeyPairException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.SavingObjectException;
import es.accv.arangi.base.exception.device.SearchingException;

/**
 * Utilidades varias para las operaciones con PKCS11
 * 
 * @author faparicio
 * 
 */
public class Pkcs11Util {

	  /**
	   * Loggger de clase
	   */
	  private static Logger logger = Logger.getLogger(Pkcs11Util.class);
	  
	  /**
	   * Número máximo de certificados que podemos almacenar en la tarjeta.
	   */
	  public static final int GBL_MAX_CERTIFICATES = 4;
	  
	  /**
	 * Obtiene el ID del token
	 * 
	 * @param pkcs11Module
	 * @return ID del slot donde se encuentra la tarjeta
	 * @throws TokenException
	 */
	public static long getUserTokenId(Module pkcs11Module) throws TokenException {
		logger.debug("IN");

		long tokenId = -1;

		Slot[] slotsWithToken = pkcs11Module
				.getSlotList(Module.SlotRequirement.TOKEN_PRESENT);
		List supportedAvailableTokenList = new ArrayList();

		// -- Cargamos los tokens
		for (int i = 0; i < slotsWithToken.length; i++) {

			Token token = slotsWithToken[i].getToken();

			try {
				// -- Filtramos por los tokens disponibles para el modulo
				// cargado
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.debug("ID: " + token.getTokenID() + " NAME: "
						+ token.getTokenInfo().getLabel());

				supportedAvailableTokenList.add(token);
				logger.debug("Asignado token a la lista de tokens disponibles ["
								+ token.getTokenID() + "].");

			} catch (PKCS11Exception pkcs11ex) {
				logger.info(pkcs11ex.getMessage());
			}
		}

		if (supportedAvailableTokenList != null
				&& !supportedAvailableTokenList.isEmpty()) {
			tokenId = ((Token) supportedAvailableTokenList.get(0)).getTokenID();
		} else {
			throw new TokenException("No se ha encontrado ninguna tarjeta soportada por el módulo criptográfico.");
		}

		logger.debug("El primer token por defecto (" + tokenId + ")");
		return tokenId;
	}

	  
	  /**
		 * Elimina del PKCS#11 los objetos cuyo LABEL coincida con el proporcionado
		 */
		public static int deleteObjectsByLabel(Session session, String label) throws DeletingObjectException {
			return deleteObjectsByLabel(session, label, true);
		}
		
	/**
	 * Elimina del PKCS#11 los objetos cuyo LABEL coincida con el proporcionado
	 */
	public static int deleteObjectsByLabel(Session session, String label, boolean deleteOrphans) throws DeletingObjectException {
		logger.debug("Borrando objetos por etiqueta...");
		logger.debug("Label: " + label);
		iaik.pkcs.pkcs11.objects.GenericTemplate template = new iaik.pkcs.pkcs11.objects.GenericTemplate();
		CharArrayAttribute ckLabel = new CharArrayAttribute(Attribute.LABEL);
		ckLabel.setCharArrayValue(label.toCharArray());
		template.addAttribute(ckLabel);
		int deleted = 0;
		try {
			List foundObjects = Pkcs11Util.findAllObjects(session, template);
			
			logger.debug("Buscados ``" + label +"´´. Encontrados: " + foundObjects.size());			
			if (!foundObjects.isEmpty()){
				Iterator it = foundObjects.iterator();
				while (it.hasNext()) {
					iaik.pkcs.pkcs11.objects.Object iaikObject = (iaik.pkcs.pkcs11.objects.Object)it.next();
					logger.debug("Borrando objeto: " + iaik.pkcs.pkcs11.objects.Object.getObjectClassName(iaikObject.getObjectClass().getLongValue()) + "...");
					session.destroyObject(iaikObject);
					logger.debug("Borrando objeto: " + iaik.pkcs.pkcs11.objects.Object.getObjectClassName(iaikObject.getObjectClass().getLongValue()) + "...OK");
					deleted++;
				}
			}
		} catch (Exception e) {
			logger.debug("Borrando objetos por etiqueta...ERROR", e);
			throw new DeletingObjectException("Error durante del grupo de objetos de label ``" + label + "´´", e);
		}
		logger.debug("Eliminados: " + deleted);
		
		if (deleteOrphans) {
	    	//-- Eliminar huerfanos y objetos extraños (GyD)
	    	Pkcs11Util.deleteOrphans(session);
		}

		logger.debug("Borrando objetos por etiqueta...OK");
		return deleted;
	}
	
	/**
	 * @param session
	 * @param template
	 * @return Lista de objetos encontrados
	 * @throws OperatingException 
	 */
	  public static List<Object> findAllObjects(Session session, iaik.pkcs.pkcs11.objects.Object template) throws SearchingException {

	    List<Object> objects = new ArrayList<Object>();
	    try {
		    // -- Buscamos todos los objetos que cumplen la plantilla
		    // -- o todos si la plantilla es null
		    session.findObjectsInit(template);
		    Object[] matchingKeys;
		    while ((matchingKeys = session.findObjects(1)).length > 0) {
		    	Object objTmp = matchingKeys[0];
		    	
		    	//-- Sólo los objetos correctos (certificados y claves)
		    	if (objTmp instanceof iaik.pkcs.pkcs11.objects.PrivateKey ||
		    			objTmp instanceof iaik.pkcs.pkcs11.objects.PublicKey ||
		    			objTmp instanceof iaik.pkcs.pkcs11.objects.X509PublicKeyCertificate) {
		    		
		    		objects.add(objTmp);
		    		
		    	}
		    }

		    // -- Liberamos busqueda
		    session.findObjectsFinal();
	    } catch (TokenException e){
			throw new SearchingException("Error durante la búsqueda",e);
		}
	    logger.debug("retorna: Num. obj. en la tarjeta: " + objects.size());
	    return objects;
	  }

	  /**
	   * @param session
	   * @param label
	 * @return Lista de objetos encontrados
	 * @throws SearchingException 
	   * @throws TokenException
	   */
	  public static List findPrivateKeysByLabel(Session session, String label) throws SearchingException {

	    logger.debug("IN: label: " + label);

	    List keyList = new ArrayList();

	    // -- Plantilla de busqueda de claves privadas por etiqueta
	    Key keyTemplate = new iaik.pkcs.pkcs11.objects.PrivateKey();
	    if (label != null) {
	      keyTemplate.getLabel().setCharArrayValue(label.toCharArray());
	    }

	    // -- Buscamos claves privadas
	    keyList = findAllObjects(session, keyTemplate);
	    logger.debug("retorna: keyList: " + keyList.size());
	    return keyList;

	  }

	  /**
	   * Si el numero de intentos es = o mayor que 3 deja de intentar el borrado.
	   * 
	   * @param session
	   * @param obj
	   * @param numRetry
	   */
	  public static void deleteObject(Session session, Object obj, int numRetry) {

	    try {

	      // -- Intentamos el borrado
	      session.destroyObject((iaik.pkcs.pkcs11.objects.Object) obj);

	    } catch (PKCS11Exception e) {

	      if (numRetry < 3) {
	        deleteObject(session, obj, numRetry++);
	      }
	      else {
	        logger.info("Agotado el numero de reintentos.");
	      }

	    } catch (TokenException e) {
	      logger.info("Error en el borrado.", e);
	    }

	  }
	  
	  /**
	   * 
	   * 
	   * @throws TokenException
	   */
	  public static void deleteOrphans(Session session) {

		//-- Primera pasada: eliminar las claves públicas que no tengan certificado
	    iaik.pkcs.pkcs11.objects.PublicKey publicKeyTemplate = new iaik.pkcs.pkcs11.objects.PublicKey();
	    List publicKeys;
		try {
			publicKeys = Pkcs11Util.findAllObjects(session, publicKeyTemplate);
		} catch (SearchingException e) {
			logger.debug ("[Pkcs11Util.deleteOrphans]::No se han podido obtener todas las claves públicas");
			return;
		}

	    Hashtable ht;
		try {
			ht = createKeyCertificateTable(session, publicKeys);
		} catch (SearchingException e) {
			logger.debug ("[Pkcs11Util.deleteOrphans]::" + e.getMessage());
			return;
		}

	    Iterator itPub = publicKeys.iterator();
	    while (itPub.hasNext()) {
	      iaik.pkcs.pkcs11.objects.Object pubKey = (iaik.pkcs.pkcs11.objects.Object) itPub.next();
	      X509PublicKeyCertificate x509PublicKeyCertificate = (X509PublicKeyCertificate) ht.get(pubKey);

	      if (x509PublicKeyCertificate == null) {
	        deleteObject(session, pubKey, 0);
	      }
	    }
	    logger.debug("Claves publicas borradas.");


		//-- Segunda pasada: eliminar las claves privadas que no tengan certificado
	    List keys;
		try {
			keys = Pkcs11Util.findAllPrivateKeys(session);
		} catch (SearchingException e) {
			logger.debug ("[Pkcs11Util.deleteOrphans]::No se han podido obtener las claves privadas");
			return;
		}

		try {
			ht = createKeyCertificateTable(session, keys);
		} catch (SearchingException e) {
			logger.debug ("[Pkcs11Util.deleteOrphans]::" + e.getMessage());
			return;
		}

	    Iterator itPriv = keys.iterator();
	    while (itPriv.hasNext()) {

	      Key privKey = (Key) itPriv.next();
	      X509PublicKeyCertificate x509PublicKeyCertificate = (X509PublicKeyCertificate) ht.get(privKey);

	      if (x509PublicKeyCertificate == null) {
	        deleteObject(session, privKey, 0);
	      }

	    }
	    logger.debug("Claves privadas huerfanas borradas.");

		//-- Tercera pasada: en algunos casos (GyD) los elementos borrados quedan como elementos zombie
	    //-- Hay que eliminarlos porque si no dan múltiples problemas
	    
	    List lObjects;
		try {
			lObjects = findAllObjects(session, new iaik.pkcs.pkcs11.objects.Object ());
		} catch (SearchingException e1) {
			logger.debug ("[Pkcs11Util.deleteOrphans]::No se han podido obtener todos los objetos");
			return;
		}
	    Iterator itObject = lObjects.iterator();
	    while (itObject.hasNext()) {
			iaik.pkcs.pkcs11.objects.Object object = (iaik.pkcs.pkcs11.objects.Object) itObject.next();
			if (!(object instanceof iaik.pkcs.pkcs11.objects.PrivateKey ||
					object instanceof iaik.pkcs.pkcs11.objects.PublicKey ||
					object instanceof iaik.pkcs.pkcs11.objects.X509PublicKeyCertificate)) {
				try {
					session.destroyObject (object);
				} catch (TokenException e) {
					logger.info ("[Pkcs11Util.deleteOrphans]::No se ha podido eliminar el objeto de tipo '" + object.getClass() + "'.", e);
				}
				continue;
			}

		}
	  }

	/**
	   * Busca todas las claves privadas de la tarjeta.
	   * 
	   * @param session
	   * @return
	 * @throws SearchingException 
	   * @throws TokenException
	   */
	  private static List findAllPrivateKeys(Session session) throws SearchingException {
	
	    logger.debug("IN");
	
	    List privateKeys = findPrivateKeysByLabel(session, null);
	
	    logger.debug("privateKeys: " + privateKeys.size());
	    return privateKeys;
	
	  }

	/**
	   * Construye una hastable con clave valor en la que la clave es un elemento de
	   * la lista de claves y el valor el certificado asociado.
	   * 
	   * @param session
	   * @param keys
	   * @return
	 * @throws SearchingException 
	 * @throws OperatingException 
	   */
	  private static Hashtable createKeyCertificateTable(Session session, List keys) throws SearchingException  {
	
	    logger.debug("IN: " + keys.size());
	
	    Hashtable ht = new Hashtable();
	
	    // for each private signature key try to find a public key certificate
	    // with the same ID
	    Iterator it = keys.iterator();
	
	    while (it.hasNext()) {
	      Key key = (Key) it.next();
	      byte[] keyID = key.getId().getByteArrayValue();
	
	      // this is the implementation that uses a concrete object class
	      // (X509PublicKeyCertificate) for searching
	      X509PublicKeyCertificate certificateSearchTemplate = new X509PublicKeyCertificate();
	      certificateSearchTemplate.getId().setByteArrayValue(keyID);
	      try {
		      session.findObjectsInit(certificateSearchTemplate);
		
		      Object[] foundCertificateObjects;
		      if ((foundCertificateObjects = session.findObjects(1)).length > 0) {
		        ht.put(key, foundCertificateObjects[0]);
		      }
		
		      session.findObjectsFinal();
	      } catch (TokenException e){
	    	  throw new SearchingException("Error creando HashTable de pares Clave-Certificado", e);
	      }
	    }
	
	    logger.debug("retorna: " + ht.size());
	    return ht;
	
	  }

	private static PrivateKey getJavaSecurityPrivateKey(RSAPrivateKey exportableRsaPrivateKey) {
	//    logger.debug("IN: exportableRsaPrivateKey: " + exportableRsaPrivateKey);
	    TokenPrivateKey key = new TokenPrivateKey(exportableRsaPrivateKey); 
	    return key;
	  }

	private static PublicKey getJavaSecurityPublicKey(RSAPublicKey exportableRsaPublicKey) {
	    TokenPublicKey key = new TokenPublicKey(exportableRsaPublicKey);
	    return key;
	  }


/**
   * Generacion de un par de claves.
   * 
   * @param session
   * @param token
   * @return Par de claves
 * @throws KeyPairException 
   */
  public static KeyPair generateKeyPair(Session session, Token token, int keySize, int numRetry, String label) throws KeyPairException {
	  
	  logger.debug("IN: " + Arrays.asList(new Object[] { "<session>", "<token>", String.valueOf(keySize), String.valueOf(numRetry), label }));
	  MechanismInfo signatureMechanismInfo;
	  try {
		  // first check out what attributes of the keys we may set
		  HashSet supportedMechanisms = new HashSet(Arrays.asList(token.getMechanismList()));
		  
		  if (supportedMechanisms.contains(Mechanism.RSA_PKCS)) {
			  signatureMechanismInfo = token.getMechanismInfo(Mechanism.RSA_PKCS);
			  
		  } else if (supportedMechanisms.contains(Mechanism.RSA_X_509)) {
			  signatureMechanismInfo = token.getMechanismInfo(Mechanism.RSA_X_509);
			  
		  } else if (supportedMechanisms.contains(Mechanism.RSA_9796)) {
			  signatureMechanismInfo = token.getMechanismInfo(Mechanism.RSA_9796);
			  
		  } else if (supportedMechanisms.contains(Mechanism.RSA_PKCS_OAEP)) {
			  signatureMechanismInfo = token.getMechanismInfo(Mechanism.RSA_PKCS_OAEP);
			  
		  } else {
			  signatureMechanismInfo = null;
		  }
	  } catch (TokenException e1) {
			logger.info("[Pkcs11Util.generateKeyPair]::No se pueden obtener los mecanismos de cifrado del dispositivo", e1);
			throw new KeyPairException("No se pueden obtener los mecanismos de cifrado del dispositivo", e1);
	  }
	  Mechanism keyPairGenerationMechanism = Mechanism.RSA_PKCS_KEY_PAIR_GEN;
	  RSAPublicKey rsaPublicKeyTemplate = new RSAPublicKey();
	  RSAPrivateKey rsaPrivateKeyTemplate = new RSAPrivateKey();
	  
	  // set the general attributes for the public key
	  rsaPublicKeyTemplate.getModulusBits().setLongValue(new Long(keySize));
	  byte[] publicExponentBytes = { 0x01, 0x00, 0x01 }; // 2^16 + 1
	  rsaPublicKeyTemplate.getPublicExponent().setByteArrayValue(publicExponentBytes);
	  rsaPublicKeyTemplate.getToken().setBooleanValue(Boolean.TRUE);
	  byte[] id = new byte[20];
	  new Random().nextBytes(id);
	  rsaPublicKeyTemplate.getId().setByteArrayValue(id);
	  
	  if (label != null){
		  rsaPublicKeyTemplate.getLabel().setCharArrayValue(label.toCharArray());
	  }
	  
	  rsaPrivateKeyTemplate.getSensitive().setBooleanValue(Boolean.TRUE);
	  rsaPrivateKeyTemplate.getToken().setBooleanValue(Boolean.TRUE);
	  rsaPrivateKeyTemplate.getPrivate().setBooleanValue(Boolean.TRUE);
	  rsaPrivateKeyTemplate.getModifiable().setBooleanValue(Boolean.TRUE);
	  rsaPrivateKeyTemplate.getId().setByteArrayValue(id);
	  // byte[] subject = args[1].getBytes();
	  // rsaPrivateKeyTemplate.getSubject().setByteArrayValue(subject);
	  if (label != null){
		  rsaPrivateKeyTemplate.getLabel().setCharArrayValue(label.toCharArray());
	  }
	  
	  // set the attributes in a way netscape does, this should work with most
	  // tokens
	  if (signatureMechanismInfo != null) {
		  
		  rsaPublicKeyTemplate.getVerify().setBooleanValue(new Boolean(signatureMechanismInfo.isVerify()));
		  rsaPublicKeyTemplate.getVerifyRecover().setBooleanValue(new Boolean(signatureMechanismInfo.isVerifyRecover()));
		  rsaPublicKeyTemplate.getEncrypt().setBooleanValue(new Boolean(signatureMechanismInfo.isEncrypt()));
		  rsaPublicKeyTemplate.getDerive().setBooleanValue(new Boolean(signatureMechanismInfo.isDerive()));
		  
		  rsaPrivateKeyTemplate.getSign().setBooleanValue(new Boolean(signatureMechanismInfo.isSign()));
		  rsaPrivateKeyTemplate.getSignRecover().setBooleanValue(new Boolean(signatureMechanismInfo.isSignRecover()));
		  rsaPrivateKeyTemplate.getDecrypt().setBooleanValue(new Boolean(signatureMechanismInfo.isDecrypt()));
		  rsaPrivateKeyTemplate.getDerive().setBooleanValue(new Boolean(signatureMechanismInfo.isDerive()));
		  
	  } else {
		  
		  // if we have no information we assume these attributes
		  rsaPrivateKeyTemplate.getSign().setBooleanValue(Boolean.TRUE);
		  rsaPrivateKeyTemplate.getDecrypt().setBooleanValue(Boolean.TRUE);
		  
		  rsaPublicKeyTemplate.getVerify().setBooleanValue(Boolean.TRUE);
		  rsaPublicKeyTemplate.getEncrypt().setBooleanValue(Boolean.TRUE);
		  
	  }
	  
	  rsaPublicKeyTemplate.getWrap().setBooleanValue(Boolean.TRUE);
	  rsaPrivateKeyTemplate.getUnwrap().setBooleanValue(Boolean.TRUE); // Sin esto no van los certificados de logon

	  // netscape does not set these attribute, so we do no either
	  rsaPublicKeyTemplate.getKeyType().setPresent(false);
	  rsaPublicKeyTemplate.getObjectClass().setPresent(false);
	  
	  rsaPrivateKeyTemplate.getKeyType().setPresent(false);
	  rsaPrivateKeyTemplate.getObjectClass().setPresent(false);
	  
	  logger.debug("Plantillas de generacion de claves creadas");
	  
	  KeyPair generatedKeyPair = null;
	  
	  try {
		  
		  // -- Intentamos generar las claves
		  generatedKeyPair = session.generateKeyPair(keyPairGenerationMechanism, rsaPublicKeyTemplate, rsaPrivateKeyTemplate);
		  
	  } catch (TokenException pkcsex) {
		  logger.info("Excepción en la operación con el PKCS11.\n Iniciando el borrado de elementos huerfanos.", pkcsex);
		  Pkcs11Util.deleteOrphans(session);
		  logger.debug("Fin del proceso de eliminación de elementos huerfanos.");
		  
		  if (numRetry < 3) {
			  logger.debug("Reintentamdo la generacion...");
			  numRetry++;
			  generatedKeyPair = Pkcs11Util.generateKeyPair(session, token, keySize, numRetry, label);
			  
		  } else {
			  logger.info("[Pkcs11Util.generateKeyPair]::No se ha podido generar el par de claves", pkcsex);
			  throw new KeyPairException("No se ha podido generar el par de claves", pkcsex);
		  }
		  
	  }
		  
	  return generatedKeyPair;
  }

/**
   * @param rsaKeyPair
   * @return Par de claves
   */
  public static java.security.KeyPair iakKeyPairToSun(KeyPair rsaKeyPair) {

//    logger.debug("PublicKey:\n" + rsaKeyPair.getPublicKey().toString());
//    logger.debug("PrivateKey:\n" + rsaKeyPair.getPrivateKey().toString());

//    java.security.PublicKey publicKey = getJavaSecurityPublicKey((RSAPublicKey) rsaKeyPair.getPublicKey());
    TokenPublicKey tpk = new TokenPublicKey(rsaKeyPair.getPublicKey());
//    java.security.PublicKey publicKey = tpk.getSunPublicKey();
    java.security.PublicKey publicKey = tpk;
    
    logger.debug("publicKey: " + publicKey.toString());

    java.security.PrivateKey privateKey = getJavaSecurityPrivateKey((RSAPrivateKey) rsaKeyPair.getPrivateKey());
    logger.debug("privateKey: " + privateKey.toString());

    java.security.KeyPair kp = new java.security.KeyPair(publicKey, privateKey);
//    logger.debug("SUN PublicKey:\n" + kp.getPublic().getEncoded());
//    logger.debug("SUN KeyPair:\n" + kp);

    return kp;
  }
  
  /**
	* Importa al PKCS11 un certificado a partir de un certificado X509.
	* 
	* @param session
	* @param token
	* @param cert
	* @param theLabel
	* @throws CertificateInvalidException 
	* @throws DeviceFullException 
	* @throws SavingObjectException 
	*/
	public static void importX509Cert(Session session, Token token,
			X509Certificate cert, String theLabel, boolean deletePublicKey) throws CertificateInvalidException, DeviceFullException, SavingObjectException  {

		Collection certificateChain = null;
		try {
			//-- Parseo del certificado
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");
			certificateChain = certificateFactory.generateCertificates(new ByteArrayInputStream(cert.getEncoded()));
			if (certificateChain.size() < 1) {
				logger.info("Did not find any certificate in the given input file. Finished.");
				throw new CertificateInvalidException("El objeto X509 proporcionado no contiene ningún certificado");
			}
			// -- Quitamos el certificado de la cadena de confianza
			certificateChain.remove(cert);
		} catch (CertificateException e) {
			logger.info("Error criptográfico manipulando el Certificado X509 proporcionado", e);
			throw new CertificateInvalidException("Error criptográfico manipulando el Certificado X509 proporcionado", e);
		} catch (NoSuchProviderException e) {
			logger.info("El certificado proporcionado no puede ser manipulado por el proveedor de Bouncy Castle", e);
			throw new CertificateInvalidException("El certificado proporcionado no puede ser manipulado por el proveedor de Bouncy Castle", e);
		}

		logger.debug("Searching for corresponding private key on token.");

		PublicKey publicKey = cert.getPublicKey();

		iaik.pkcs.pkcs11.objects.Object searchTemplate = null;
		if (publicKey.getAlgorithm().equalsIgnoreCase("RSA")) {
			java.security.interfaces.RSAPublicKey rsaPublicKey = (java.security.interfaces.RSAPublicKey) publicKey;
			RSAPrivateKey rsaPrivateKeySearchTemplate = new RSAPrivateKey();
			byte[] modulus = iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(rsaPublicKey.getModulus());
			rsaPrivateKeySearchTemplate.getModulus().setByteArrayValue(modulus);
			searchTemplate = rsaPrivateKeySearchTemplate;
		}

		byte[] objectID = null;
		try {
			if (searchTemplate != null) {
				session.findObjectsInit(searchTemplate);
				Object[] foundKeyObjects = session.findObjects(1);
				if (foundKeyObjects.length > 0) {
					Key foundKey = (Key) foundKeyObjects[0];
					objectID = foundKey.getId().getByteArrayValue();
					logger.debug("found a correponding key on the token: ");
					logger.debug(foundKey);
				} else {
					logger.debug("found no correponding key on the token.");
				}
				session.findObjectsFinal();
			} else {
				logger.debug("public key is neither RSA, DSA nor DH.");
			}
		} catch (TokenException e) {
			logger.debug("Error buscando clave privada", e);
		}
		
		logger.debug("Create certificate object(s) on token.");

		X509Certificate currentCertificate = cert; // start with user cert
		boolean importedCompleteChain = false;
		while (!importedCompleteChain) {
			// create certificate object template
			X509PublicKeyCertificate pkcs11X509PublicKeyCertificate = new X509PublicKeyCertificate();
			char[] label = theLabel.toCharArray();
			byte[] newObjectID;
			try {
				// if we need a new object ID, create one
				if (objectID == null) {
					if (publicKey instanceof java.security.interfaces.RSAPublicKey) {
						newObjectID = ((java.security.interfaces.RSAPublicKey) publicKey).getModulus().toByteArray();
						MessageDigest digest = MessageDigest.getInstance("SHA-1");
						newObjectID = digest.digest(newObjectID);
					} else if (publicKey instanceof java.security.interfaces.DSAPublicKey) {
						newObjectID = ((java.security.interfaces.DSAPublicKey) publicKey).getY().toByteArray();
						MessageDigest digest = MessageDigest.getInstance("SHA-1");
						newObjectID = digest.digest(newObjectID);
					} else {
						Certificate certificate = null;
						try {
							certificate = new Certificate (currentCertificate);
						} catch (NormalizeCertificateException e) {
							logger.info("No se puede obtener el certificado de Arangi", e);
						}
						newObjectID = certificate.getFingerPrint().getBytes();
//						newObjectID = org.ejbca.util.CertTools.getFingerprintAsString(currentCertificate).getBytes();
					}
				} else {
					// we already got one from a corresponding private key before
					newObjectID = objectID;
				}
			} catch (NoSuchAlgorithmException e) {
				logger.info ("[Pkcs11Util.importX509Cert]::No se puede obtener un nuevo ID en el dispositivo debido a que el algoritmo criptográfico SHA-1 no soportado.", e);
				throw new SavingObjectException ("No se puede obtener un nuevo ID en el dispositivo debido a que el algoritmo criptográfico SHA-1 no soportado.", e);
			}	
			// byte[] encodedSubject = ((Name)
			// currentCertificate.getSubjectDN()).getEncoded();
			// byte[] encodedIssuer = ((Name)
			// currentCertificate.getIssuerDN()).getEncoded();
			byte[] encodedSubject = currentCertificate.getSubjectX500Principal().getEncoded();
			byte[] encodedIssuer = currentCertificate.getIssuerX500Principal().getEncoded();

			// serial number should be an DER encoded ASN.1 integer
			/*
			 * INTEGER asn1Integer = new
			 * INTEGER(userCertificate.getSerialNumber()); ByteArrayOutputStream
			 * buffer = new ByteArrayOutputStream();
			 * DerCoder.encodeTo(asn1Integer, buffer);
			 * pkcs11X509PublicKeyCertificate.getSerialNumber().setByteArrayValue(buffer.toByteArray());
			 */
			// Netscape deviates from the standard here, for use with Netscape
			// rather use
//			byte[] serialNumber = currentCertificate.getSerialNumber().toByteArray();

			pkcs11X509PublicKeyCertificate.getToken().setBooleanValue(Boolean.TRUE);
			pkcs11X509PublicKeyCertificate.getPrivate().setBooleanValue(Boolean.FALSE);
			pkcs11X509PublicKeyCertificate.getLabel().setCharArrayValue(label);
			pkcs11X509PublicKeyCertificate.getId().setByteArrayValue(newObjectID);
			pkcs11X509PublicKeyCertificate.getSubject().setByteArrayValue(encodedSubject);
			pkcs11X509PublicKeyCertificate.getIssuer().setByteArrayValue(encodedIssuer);
//			pkcs11X509PublicKeyCertificate.getSerialNumber().setByteArrayValue(serialNumber);
			try {
				pkcs11X509PublicKeyCertificate.getValue().setByteArrayValue(currentCertificate.getEncoded());
			} catch (CertificateEncodingException e) {
				logger.info ("[Pkcs11Util.importX509Cert]::La codificación del certificado es incorrecta", e);
				throw new CertificateInvalidException("La codificación del certificado es incorrecta", e);
			}
			logger.debug(pkcs11X509PublicKeyCertificate);
			try{
				session.createObject(pkcs11X509PublicKeyCertificate);
			} catch (TokenException e) {
				logger.info ("[Pkcs11Util.importX509Cert]::Error creando clave pública del certificado en el dispositivo", e);
				throw new SavingObjectException("Error creando clave pública del certificado en el dispositivo", e);
			}

			if (certificateChain.size() > 0) {
				currentCertificate = (X509Certificate) certificateChain.iterator().next();
				certificateChain.remove(currentCertificate);
				objectID = null; // do not use the same ID for other
									// certificates
			} else {
				importedCompleteChain = true;
			}
		}

		if (deletePublicKey) {
			// -- Borramos ahora la clave pública
			logger.debug("Borramos clave pública...");
		    iaik.pkcs.pkcs11.objects.PublicKey publicKeyTemplate = new iaik.pkcs.pkcs11.objects.PublicKey();
			publicKeyTemplate.getLabel().setCharArrayValue(theLabel.toCharArray());
			List<Object> lPKs;
			try {
				lPKs = findAllObjects(session, publicKeyTemplate);
				if (!lPKs.isEmpty()) {
					deleteObject(session, lPKs.get(0), 0);
					logger.debug("Clave pública borrada.");
				}
			} catch (SearchingException e) {
				logger.info("No se ha borrado la clave pública porque no existe");
			}
		}

		//-- Borramos huerfanos
		deleteOrphans(session);

	}

	  /**
	   * Retorna el número de claves privadas en el PKCS11.
	   * 
	   * @param session
	   * @return Número de claves privadas en el PKCS11
	 * @throws SearchingException 
	   * @throws TokenException
	   */
	  public static int getNumPrivateKeysInPKCS11(Session session) throws SearchingException {
		  
		  int iRet = 0;
		  
		  List vKeys = findPrivateKeysByLabel(session, null);
		  if (vKeys != null) {
			  iRet = vKeys.size();
		  }
		  
		  return iRet;
	  }

	  /**
	 * Cambia la etiqueta de uno o varios objetos por la indicada
	 * 
	 * @param session
	 * @throws SearchingException 
	 * @throws SavingObjectException 
	 * @throws Exception 
	 */
	public static void changeLabel(Module module, Session session, String label, String newLabel) throws SearchingException, SavingObjectException {
		
		//-- Buscamos todos los objetos con label temporal
		logger.debug("Buscando certificados con LABEL '" + label + "'. Cambio a '" + newLabel + "'");
		iaik.pkcs.pkcs11.objects.GenericTemplate template = new iaik.pkcs.pkcs11.objects.GenericTemplate();
		CharArrayAttribute ckLabel = new CharArrayAttribute(Attribute.LABEL);
		ckLabel.setCharArrayValue(label.toCharArray());
		template.addAttribute(ckLabel);
		List foundObjects = findAllObjects(session, template);
		logger.debug("Encontrados: " + foundObjects.size());

		//-- Para cada objeto, obtenemos su label
		for (int i = 0; i < foundObjects.size(); i++) {
			iaik.pkcs.pkcs11.objects.Object object  = (iaik.pkcs.pkcs11.objects.Object)foundObjects.get(i);
			logger.debug(object + "\n\n");
			
			logger.debug("Atributos: " + object.getAttributeTable());			
			PKCS11 pkcs11Module = module.getPKCS11Module();
			long hSession = session.getSessionHandle();
			long hObject = object.getObjectHandle();
			
			iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE[] attributes;
			try {
				attributes = iaik.pkcs.pkcs11.objects.Object.getSetAttributes(object);
			} catch (PKCS11Exception e) {
				logger.info ("[Pkcs11Util.changeLabel]::Error obteniendo los atributos de uno de los objetos del alias '" +
						label + "'.", e);
				throw new SavingObjectException ("Error obteniendo los atributos de uno de los objetos del alias '" +
						label + "'.", e);
			}
			CK_ATTRIBUTE ck_label = null; 
			for (int j = 0; j < attributes.length; j++) {
				iaik.pkcs.pkcs11.wrapper.CK_ATTRIBUTE ckAtt = attributes[j];
				if (ckAtt.type == PKCS11Constants.CKA_LABEL) {
					ckAtt.pValue = newLabel.toCharArray();
					ck_label = ckAtt;
				}
			}
			
			CK_ATTRIBUTE[] pTemplate = new CK_ATTRIBUTE[1];
			pTemplate[0] = ck_label;
			try {
				pkcs11Module.C_SetAttributeValue(hSession, hObject, pTemplate, true);
			} catch (PKCS11Exception e) {
				logger.info ("[Pkcs11Util.changeLabel]::Error cambiando el atributo 'label' de uno de los objetos del alias '" +
						label + "'.", e);
				throw new SavingObjectException ("Error cambiando el atributo 'label' de uno de los objetos del alias '" +
						label + "'.", e);
			}
			logger.debug("Cambiada label del objeto '" + hObject + "' en la sesion '" + hSession + "'");
		}
	}



	
}
