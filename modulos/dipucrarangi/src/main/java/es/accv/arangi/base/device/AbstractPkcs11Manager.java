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
package es.accv.arangi.base.device;

import iaik.pkcs.pkcs11.Mechanism;
import iaik.pkcs.pkcs11.MechanismInfo;
import iaik.pkcs.pkcs11.Session;
import iaik.pkcs.pkcs11.Token;
import iaik.pkcs.pkcs11.TokenException;
import iaik.pkcs.pkcs11.objects.PrivateKey;
import iaik.pkcs.pkcs11.objects.PublicKey;
import iaik.pkcs.pkcs11.objects.RSAPrivateKey;
import iaik.pkcs.pkcs11.objects.RSAPublicKey;
import iaik.pkcs.pkcs11.objects.X509PublicKeyCertificate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.X509CertificateObject;

import sun.security.util.ObjectIdentifier;
import es.accv.arangi.base.algorithm.CipherAlgorithm;
import es.accv.arangi.base.algorithm.DigitalSignatureAlgorithm;
import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.device.model.Pkcs11Device;
import es.accv.arangi.base.device.model.Pkcs11Manufacturer;
import es.accv.arangi.base.device.util.CSRUtil;
import es.accv.arangi.base.device.util.DeviceUtil;
import es.accv.arangi.base.device.util.pkcs11.Pkcs11Util;
import es.accv.arangi.base.device.util.pkcs11.SignUtil;
import es.accv.arangi.base.device.util.pkcs11.TokenPrivateKey;
import es.accv.arangi.base.device.util.pkcs11.TokenPublicKey;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.document.InputStreamDocument;
import es.accv.arangi.base.exception.device.AliasNotFoundException;
import es.accv.arangi.base.exception.device.AliasNotFreeException;
import es.accv.arangi.base.exception.device.CertificateInvalidException;
import es.accv.arangi.base.exception.device.CipherException;
import es.accv.arangi.base.exception.device.DeletingObjectException;
import es.accv.arangi.base.exception.device.DeviceFullException;
import es.accv.arangi.base.exception.device.DeviceNotFoundException;
import es.accv.arangi.base.exception.device.FormatException;
import es.accv.arangi.base.exception.device.IAIKDLLNotFoundException;
import es.accv.arangi.base.exception.device.IncorrectPINException;
import es.accv.arangi.base.exception.device.IncorrectPUKException;
import es.accv.arangi.base.exception.device.InitializeProviderException;
import es.accv.arangi.base.exception.device.KeyPairException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.LockedPINException;
import es.accv.arangi.base.exception.device.ModuleNotFoundException;
import es.accv.arangi.base.exception.device.OpeningDeviceException;
import es.accv.arangi.base.exception.device.ReadingStreamException;
import es.accv.arangi.base.exception.device.SavingObjectException;
import es.accv.arangi.base.exception.device.SearchingException;
import es.accv.arangi.base.exception.device.UnlockPINException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.document.InitDocumentException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.util.Util;

/**
 * Clase abstracta que utilizarán los managers que sigan el estándar PKCS#11.<br><br>
 * 
 * Arangí se encargará de instalar los módulos para trabajar con los
 * dispositivos PKCS#11. Para ello necesita un fichero XML que le indique
 * dónde se encuentran los fichero de los módulos. Existen dos opciones:<br>
 * <ul>
 * 	<li>Primero Arangí buscará en la raíz del classpath un fichero con el
 * 		nombre 'pkcs11_device_modules.xml'.</li>
 * 	<li>Si no lo encuentra lo intentará en la URL de una web. Por defecto
 * 		irá a http://???. Si se quiere que vaya a otra URL se tendría que
 * 		arrancar la JVM mediante el siguiente parámetro -Darangi.pkcs11.device.url</li>
 * </ul><br><br>
 * 
 * El fichero XML tendrá el siguiente formato: <br><br>
 * 
 * &lt;modules&gt;<br>
 * &nbsp;&nbsp;&lt;version&gt;...&lt;version&gt;<br>
 * &nbsp;&nbsp;&lt;module name="..."&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;classpath&gt;...&lt;/classpath&gt;<br>
 * &nbsp;&nbsp;&lt;/module&gt;<br>
 * &nbsp;&nbsp;&lt;module name="..."&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;url&gt;...&lt;/url&gt;<br>
 * &nbsp;&nbsp;&lt;/module&gt;<br>
 * &lt;/modules&gt;<br><br>
 * 
 * Estos ficheros se dejarán en la {@link #getArangiTemporalFolder() carpeta temporal de Arangí}
 * siempre que en dicha carpeta no exista un fichero de nombre 'pkcs11_device_modules.xml'
 * o, si existe, su versión es menor que la del fichero que se está usando ahora.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public abstract class AbstractPkcs11Manager extends DeviceManager {
	
	/** Logger de clase */
	private static Logger logger = Logger.getLogger(AbstractPkcs11Manager.class);
	
	/*
	 * Nombre de la DLL de IAIK para 32 bits
	 */
	protected static final String IAIK_WINX86_DLL_NAME	= "pkcs11wrapper_3_x86.dll";
	
	/*
	 * Nombre de la DLL de IAIK para 64 bits
	 */
	protected static final String IAIK_WINX64_DLL_NAME	= "pkcs11wrapper_3_x64.dll";
	
	/*
	 * Path dentro del classpath de la dll de IAIK para 32 bits
	 */
	protected static final String IAIK_WINX86_DLL_CLASSPATH_PATH	= "es/accv/arangi/base/device/" + IAIK_WINX86_DLL_NAME;
	
	/*
	 * Path dentro del classpath de la dll de IAIK para 64 bits
	 */
	protected static final String IAIK_WINX64_DLL_CLASSPATH_PATH	= "es/accv/arangi/base/device/" + IAIK_WINX64_DLL_NAME;
	
	protected static File iaikDLLFile;
	
	protected String pin;
	
	protected boolean openWithPuk = false;

	protected Pkcs11Device device;
	
	protected KeyStore keystore;
	
	protected String sunProviderName;
	
	protected boolean deleteOrphans = true;
	
	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#close()
	 */
	public void close() {
		logger.debug("[CLOSE]::Cerrando dispositivo...");
		try {
			if (device.getSession() != null){
				device.getSession().logout();
				device.getSession().closeSession();
				logger.debug("[CLOSE]::Sesión cerrada");
			}
			
			if (device.getToken() != null){
				//-- TODO Ojo que cerrando todas las sesiones da problemas al 
				//-- matar la sesion SSL del navegador 
				//token.closeAllSessions();
				logger.debug("[CLOSE]::Resto de sesiones cerradas");
			}
	
			if (device.getModule() != null){
				device.getModule().finalize(); 
				logger.debug("[CLOSE]::Modulo PKCS11 descargado (" + device.getModuleName() + ")");
			}
			
			//-- Eliminar provider de SUN
			if (keystore != null) {
				logger.debug("[CLOSE]::Eliminando el provider de SUN");
				keystore = null;
//				((sun.security.pkcs11.SunPKCS11)Security.getProvider(sunProviderName)).logout();
				Security.removeProvider(sunProviderName);
				logger.debug("[CLOSE]::Eliminado el provider de SUN");
			}
			
			logger.debug("Dispositivo cerrado correctamente");
		} catch (Throwable e) {
			logger.info("Excepción durante cierre de dispositivo. Ignorando...", e);
		}
		logger.debug("Cerrando dispositivo...OK");
	}
	
	/**
	 * Método que obtiene información de los dispositivos conectados para una fabricante PKCS#11.
	 * 
	 * @param manufacturer Fabricante del dispositivo
	 * 
	 * @return Lista de objetos {@link es.accv.arangi.base.device.model.Pkcs11Device Pkcs11Device} 
	 * 	con	información de cada uno de los dispositivos encontrados
	 * @throws OpeningDeviceException Errores accediendo a los dispositivos
	 * @throws LoadingObjectException Errores obteniendo la información propia del dispositivo
	 * @throws SearchingException Errores obteniendo los certificados de los dispositivos
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 */
	public static List<Pkcs11Device> getConnectedDevices (Pkcs11Manufacturer manufacturer) throws OpeningDeviceException, LoadingObjectException, SearchingException, IAIKDLLNotFoundException {
		
		logger.debug("[Pkcs11Manager.getConnectedDevices]::Entrada::" + manufacturer.getManufacturerName());
		
		//-- Llamada al método que obtiene la información
		return getConnectedDevices(new Pkcs11Manufacturer[] { manufacturer });
		
	}
	
	/**
	 * Cambia el PIN de un dispositivo PKCS#11
	 * 
	 * @param manager Manager del dispositivo PKCS#11
	 * @param newpin Nuevo PIN
	 * @throws SavingObjectException Error cambiando el PIN
	 */
	public static void changePin(AbstractPkcs11Manager manager, String newpin) throws SavingObjectException {
		logger.debug("Cambiando pin...");
		
		//-- Comprobar que el PIN no es nulo
		if (newpin == null || newpin.equals("")) {
			logger.info("[AbstractPkcs11Manager.changePin]::El PIN es nulo o está vacío");
			throw new SavingObjectException ("El PIN es nulo o está vacío");
		}
		
		//-- Cambiamos pin
		try {
			manager.getDevice().getSession().setPIN(manager.pin.toCharArray(), newpin.toCharArray());
		} catch (TokenException e) {
			logger.info ("[AbstractPkcs11Manager.changePin]::No es posible cambiar el PIN", e);
			throw new SavingObjectException ("No es posible cambiar el PIN", e);
		} 
		logger.debug("PIN cambiado");
		
	}
	
	/**
	 * Realiza un formateado del dispositivo
	 * 
	 * @param device Dispositivo a formatear
	 * @param puk PUK del dispositivo
	 * @param newPin Nuevo pin del dispositivo tras la inicialización
	 * @param newLabel Nueva etiqueta del dispositivo formateado
	 * @throws FormatException Errores durante la inicialización del dispositivo
	 */
	public static void format (Pkcs11Device device, String puk, String newPin, String newLabel)  throws FormatException {

		logger.debug ("[Pkcs11Manager.format]::Entrada");
		
		//-- Cerrar las sesiones abiertas
		logger.debug ("[Pkcs11Manager.format]::Cerrando las sesiones");
		try {
			device.getToken().closeAllSessions();
			logger.debug ("[Pkcs11Manager.format]::Sesiones cerradas");
		} catch (TokenException e) {
			logger.info ("[Pkcs11Manager.format]::Error durante el proceso de cerrado previo de las sesiones", e);
			throw new FormatException("Error durante el proceso de cerrado previo de las sesiones", e);
		}

		//-- Formatear
		logger.debug ("[Pkcs11Manager.format]::Iniciando inicialización");
		try {
			device.getToken().initToken(puk.toCharArray(), newLabel);
			logger.debug ("[Pkcs11Manager.format]::Dispositivo inicializado");
		} catch (TokenException e) {
			logger.info ("[Pkcs11Manager.format]::Error durante el proceso de inicialización del dispositivo", e);
			throw new FormatException("Error durante el proceso de inicialización del dispositivo", e);
		} 
		
		//-- Inicializar el PIN
		try {
			
	        device.setSession(device.getToken().openSession(Token.SessionType.SERIAL_SESSION, Token.SessionReadWriteBehavior.RW_SESSION, null, null));
	        device.getSession().login(Session.UserType.SO, puk.toCharArray());
	        device.getSession().initPIN(newPin.toCharArray());
	        device.getToken().closeAllSessions();
		} catch (TokenException e) {
			logger.info ("[Pkcs11Manager.format]::Error durante el proceso de inicialización del PIN tras el formateo del dispositivo", e);
			throw new FormatException("Error durante el proceso de inicialización del PIN tras el formateo del dispositivo", e);
		} 
		
		//-- Finalizar el módulo
		if (device.getModule() != null){
			try {
				device.getModule().finalize();
			} catch (Throwable e) {
				logger.info ("[Pkcs11Manager.format]::Error durante el proceso de finalización del módulo", e);
			} 
		}
	}
	
	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#isAliasFree(java.lang.String)
	 */
	public boolean isAliasFree(String alias) throws SearchingException{
		boolean isFree = false;
		X509Certificate cert = getCertificate(alias);
		java.security.PrivateKey privateKey;
		try {
			privateKey = getPrivateKey(alias);
		} catch (LoadingObjectException e) {
			logger.info("[KeyStoreManager.isAliasFree]::No se puede determinar si existe una clave privada en el alias '" + alias + "' del dispositivo.", e);
			throw new SearchingException ("No se puede determinar si existe una clave privada en el alias '" + alias + "' del dispositivo.", e);
		}
		java.security.PublicKey publicKey;
		try {
			publicKey = getPublicKey(alias);
		} catch (LoadingObjectException e) {
			logger.info("[KeyStoreManager.isAliasFree]::No se puede determinar si existe una clave pública en el alias '" + alias + "' del dispositivo.", e);
			throw new SearchingException ("No se puede determinar si existe una clave pública en el alias '" + alias + "' del dispositivo.", e);
		}
		if (cert == null && privateKey == null && publicKey == null) isFree = true;

		return isFree;
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#generateKeyPair(java.lang.String)
	 */
	public KeyPair generateKeyPair(String label) throws KeyPairException, AliasNotFreeException {
		return generateKeyPair(label, Util.DEFAULT_KEY_LENGTH);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#generateKeyPair(java.lang.String, int)
	 */
	public KeyPair generateKeyPair(String label, int keySize) throws KeyPairException, AliasNotFreeException {
		logger.debug("Generando par de claves...");

		//-- Comprobamos si existen objetos con el label 'label"
		try {
			if (!isAliasFree(label)){
				logger.debug("El alias '" + label + "' ya existe en el KeyStore y no puede ser utilizado");
				throw new AliasNotFreeException("El alias '" + label + "' ya existe en el KeyStore y no puede ser utilizado");
			}
		} catch (SearchingException e) {
			logger.info("[KeyStoreManager.generateKeyPair]::No se puede determinar si ya existe un alias '" + label + "' en el dispositivo.", e);
			throw new AliasNotFreeException ("No se puede determinar si ya existe un alias '" + label + "' en el dispositivo.", e);
		}
		
		//-- Generamos el par de claves
		iaik.pkcs.pkcs11.objects.KeyPair kp = Pkcs11Util.generateKeyPair(device.getSession(), device.getToken(), keySize, NUM_RETRIES, label);
		logger.debug("Generando par...OK");
		logger.debug("Pasando par de claves de IAIK a SUN...");
		KeyPair rsaKeyPair = Pkcs11Util.iakKeyPairToSun(kp);
		logger.debug("Pasando par de claves de IAIK a SUN...OK");

		logger.debug("Generando par de claves...OK");
		return rsaKeyPair;
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#generatePKCS10(java.lang.String, java.lang.String, java.security.KeyPair)
	 */
	public byte[] generatePKCS10(String subjectDN, String subjectAlternativeDN, KeyPair rsaKeyPair) {
		logger.debug("Generando PKCS#10...");
		
		//-- Construimos la instancia de CertificationRequest
		CertificationRequest cr = getCertificationRequest(device.getSession(), rsaKeyPair, subjectDN, subjectAlternativeDN);
		logger.debug("\n\n=========" + ASN1Dump.dumpAsString(cr) + "\n=========\n\n");
		logger.debug("CertificationRequest creada. dn: " + cr.getCertificationRequestInfo().getSubject().toString());
		logger.debug("Generando PKCS#10...OK");
		try {
			return cr.getEncoded(ASN1Encoding.DER);
		} catch (IOException e) {
			logger.info("[KeyStoreManager.generatePKCS10]::Error codificando el objeto PKCS#10", e);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#changeLabel(java.lang.String, java.lang.String)
	 */
	public void changeAlias(String oldLabel, String newLabel) throws SearchingException, AliasNotFoundException, AliasNotFreeException, SavingObjectException{
		logger.debug("Renombrando objetos ``" + oldLabel + " ´´ a ``" + newLabel + " ´´...");
		
		//-- Comprobamos si existen objetos con el label ``oldLabel" 
		try {
			if (isAliasFree (oldLabel)){
				logger.debug("El alias '" + oldLabel + "' no existe en el KeyStore");
				throw new AliasNotFoundException ("El alias '" + oldLabel + "' no existe en el KeyStore");
			}
		} catch (SearchingException e) {
			logger.info("[KeyStoreManager.changeAlias]::No se puede determinar si existe un alias '" + oldLabel + "' en el dispositivo.", e);
			throw new AliasNotFoundException ("No se puede determinar si existe un alias '" + oldLabel + "' en el dispositivo.", e);
		}
		
		//-- Comprobamos si existen objetos con el label ``newLabel"
		try {
			if (!isAliasFree(newLabel)){
				logger.debug("El alias '" + newLabel + "' ya existe en el KeyStore y no puede ser utilizado");
				throw new AliasNotFreeException("El alias '" + newLabel + "' ya existe en el KeyStore y no puede ser utilizado");
			}
		} catch (SearchingException e) {
			logger.info("[KeyStoreManager.changeAlias]::No se puede determinar si ya existe un alias '" + newLabel + "' en el dispositivo.", e);
			throw new AliasNotFreeException ("No se puede determinar si ya existe un alias '" + newLabel + "' en el dispositivo.", e);
		}
		
		Pkcs11Util.changeLabel(device.getModule(), device.getSession(), oldLabel, newLabel);
		logger.debug("Renombrando objetos...OK");
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#changePin(java.lang.String)
	 */
	public void changePin(String newpin) throws SavingObjectException, OpeningDeviceException {
		logger.debug("Cambiando pin...");
		
		//-- Cambiar el PIN
		AbstractPkcs11Manager.changePin(this, newpin);
		
		//-- Cerramos las sesiones abiertas dispositivo
		try {
			if (device.getSession() != null){
				device.getSession().closeSession();
				logger.debug("Sesión cerrada");
			}
			
			if (device.getToken() != null){
				device.getToken().closeAllSessions();
				logger.debug("Resto de sesiones cerradas");
			}
			
		} catch (TokenException e) {
			logger.info ("[Pkcs11Manager.changePin]::No se han podido cerrar las sesiones abiertas en el dispositivo tras el " +
					"cambio de PIN", e);
			throw new SavingObjectException("No se han podido cerrar las sesiones abiertas en el dispositivo tras el " +
					"cambio de PIN", e);
		} 
		
		//-- Lo abrimos con el nuevo pin
		try {
			this.device = this.device.getManufacturer().open(this.device, newpin, this.openWithPuk);
		} catch (IncorrectPINException e) {
			//-- No se dará porque acabamos de cambiar el PIN
			logger.info ("[Pkcs11Manager.changePin]::No se ha podido abrir una nueva sesión con el nuevo PIN", e);
		} catch (LockedPINException e) {
			//-- No se dará porque acabamos de cambiar el PIN y no estaba bloqueado
			logger.info ("[Pkcs11Manager.changePin]::El nuevo PIN está bloqueado en el dispositivo", e);
		} catch (IncorrectPUKException e) {
			//-- No se va a dar el caso ya que no se abre con PUK
		}
		
		logger.debug("Cambiando pin...OK");
	}
	
	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#getCertificates(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<X509Certificate> getCertificates(String findInAlias, String findInSubjectDn, String findInIssuerDn, String findSerialNumber) throws SearchingException {

		logger.debug("[Pkcs11Manager.getCertificates]::Entrada::" + Arrays.asList(new Object [] { findInAlias, findInSubjectDn, findInIssuerDn, findSerialNumber }));
		
		List<Object> objectList = new ArrayList<Object>();
		List<X509Certificate> result = new ArrayList<X509Certificate>();
		X509PublicKeyCertificate template = new X509PublicKeyCertificate();
		try {
			objectList = Pkcs11Util.findAllObjects(device.getSession(), template);
			logger.debug("Nº certificados en tarjeta: " + objectList.size());
		} catch (SearchingException e) {
			logger.info("[Pkcs11Manager.getCertificates]::ERROR::" + e.getMessage(), e);
			throw e;
		}
		
		//-- Cogemos todos los certificados y vamos quitando los que no cumplan
		//-- alguno de los criterios proporcionados
		X509PublicKeyCertificate iaikCert = null;
		X509Certificate x509cert = null;
		for (int i = 0; i < objectList.size(); i++) {
			iaikCert = (X509PublicKeyCertificate)objectList.get(i);
			try {
				x509cert = Util.getCertificate(iaikCert.getValue().getByteArrayValue());
			} catch (Exception e) {
				continue;
			}

			//-- Búsqueda en Label
			if (findInAlias != null){
				String label = iaikCert.getLabel().toString();
				if (label != null && label.indexOf(findInAlias) == -1){
					logger.debug("Quitando cert de label:" + label);
					continue;
				}
			}
	
			//-- Búsqueda en SubjectDN
			if (findInSubjectDn != null){
				String subjectDn = x509cert.getSubjectDN().toString();
				if (subjectDn != null && subjectDn.indexOf(findInSubjectDn) == -1){
					logger.debug("Quitando cert de SubjectDN:" + subjectDn);
					continue;
				}
			}
			
			//-- Búsqueda en IssuerDN
			if (findInIssuerDn != null){
				String issuerDn = x509cert.getIssuerDN().getName();
				logger.debug("Encontrado issuerDn: " + issuerDn);
				if (issuerDn != null && issuerDn.indexOf(findInIssuerDn) == -1){
					logger.debug("Quitando cert de IssuerDN:" + issuerDn);
					continue;
				}
			}

			//-- Búsqueda en Nº de serie
			if (findSerialNumber != null){
				String serialNumber = x509cert.getSerialNumber().toString(16);
				if (serialNumber != null && !serialNumber.equalsIgnoreCase(findSerialNumber)){
					logger.debug("Quitando cert con Nº serie:" + serialNumber);
					continue;
				}
			}
			
			//-- >Todo ok -> añado el certificado a la lista
			result.add(x509cert);
		}
		logger.debug("Nº certificados en tarjeta COINCIDENTES: " + result.size());
		logger.debug("getCertificates...OK");
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#getCertificate(java.lang.String)
	 */
	public X509Certificate getCertificate(String alias) throws SearchingException {
		logger.debug ("[Pkcs11Manager.getCertificate]::Obteniendo el certificado en '" + alias + "'");

		//-- Obtener el certificado
		X509PublicKeyCertificate iaikCert;
		try {
			iaikCert = getIAIKCertificate(alias);
		} catch (LoadingObjectException e) {
			logger.info("[Pkcs11Manager.getCertificate]::Error buscando el certificado para el alias '" + alias + "'", e);
			throw new SearchingException ("Error buscando el certificado para el alias '" + alias + "'", e);
		}
		if (iaikCert != null) {
			//-- Pasamos a X509Certificate de Sun
			try {
				logger.debug("Se va a devolver una clave pública para el alias '" + alias + "'");
				return Util.getCertificate(iaikCert.getValue().getByteArrayValue());
			} catch (Exception e) {
				logger.info("[Pkcs11Manager.getCertificate]::Error transformando el certificado de IAIK a X509Certificate de Sun", e);
				throw new SearchingException ("Error transformando el certificado de IAIK a X509Certificate de Sun", e);
			}
		}
		
		//-- Si estamos aquí es porque no se ha obtenido ningún certificado
		logger.debug("No se ha encontrado el certificado para el alias '" + alias + "'");
		return null;
	}


	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#importCertificate(java.security.cert.X509Certificate, java.lang.String)
	 */
	public void importCertificate(X509Certificate cert, String theLabel) throws DeviceFullException, CertificateInvalidException, SavingObjectException  {
		importCertificate(cert, theLabel, true);
	}

	/**
	 * Importa un certificado en la tarjeta.
	 * 
	 * @param cert Certificado a importar
	 * @param theLabel Alias donde se realizará la importación
	 * @param deletePublicKey Tras la importación se elimina o no la clave pública
	 * @throws DeviceFullException Dispositivo lleno
	 * @throws CertificateInvalidException Certificado no válido
	 * @throws SavingObjectException Error importando el certificado
	 */
	public void importCertificate(X509Certificate cert, String theLabel, boolean deletePublicKey) throws DeviceFullException, CertificateInvalidException, SavingObjectException  {
		logger.debug("Importando certificado...");
		logger.debug("Certificado: " + theLabel);

	    //-- Validacion de espacio en tarjeta
	    if (!validateFreeMemory (device.getFreeMemory())) {
	    	logger.info ("[Pkcs11Manager.importCertificate]::La memoria de la tarjeta está llena.");
	    	throw new DeviceFullException("La memoria de la tarjeta está llena.");
	    }
		
	    //-- Importar
		Pkcs11Util.importX509Cert(device.getSession(), device.getToken(), cert, theLabel, deletePublicKey);
		logger.debug("Importando certificado...OK");
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#importAliasFromKeystore(java.io.InputStream, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void importAliasFromKeystore(InputStream is, String password, String labelOrigen, String labelDestino) 
		throws DeviceFullException, DeviceNotFoundException, ReadingStreamException, AliasNotFoundException, LoadingObjectException, SavingObjectException, OpeningDeviceException, IncorrectPINException {
		
	    logger.debug("[Pkcs11Manager.importAliasFromKeystore]::Entrada::" + Arrays.asList(new Object[] {is, password, labelOrigen, labelDestino}));
		
		//-- Obtener el manager del keystore
    	KeyStoreManager keystoreManager;
		keystoreManager = new KeyStoreManager (is, password);
    	
		//-- Llamar al método
		importAliasFromKeystore(keystoreManager, labelOrigen, labelDestino);
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#importFromKeystore(es.accv.arangi.base.device.DeviceManager)
	 */
	public void importFromKeystore(DeviceManager deviceManager) throws DeviceFullException, DeviceNotFoundException, LoadingObjectException, SavingObjectException  {
		
	    logger.debug("[Pkcs11Manager.importAliasFromKeystore]::Entrada::" + Arrays.asList(new Object[] { deviceManager }));

		//-- Llamar al método
		try {
			importAliasFromKeystore(deviceManager, null, null);
		} catch (AliasNotFoundException e) {
			//-- No se dará ya que no se hace la llamada con alias
		}
	    
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#importAliasFromKeystore(es.accv.arangi.base.device.DeviceManager, java.lang.String, java.lang.String)
	 */
	public void importAliasFromKeystore(DeviceManager deviceManager, String labelOrigen, String labelDestino)  
	throws DeviceFullException, DeviceNotFoundException, AliasNotFoundException, LoadingObjectException, SavingObjectException {
	    logger.debug("[Pkcs11Manager.importAliasFromKeystore]::Entrada::" + Arrays.asList(new Object[] {deviceManager, labelOrigen, labelDestino}));

		//-- Verificaciones
	    if (deviceManager == null) {
	    	logger.info("[Pkcs11Manager.importAliasFromKeystore]::El dispositivo origen es nulo");
	    	throw new DeviceNotFoundException("El dispositivo origen es nulo.");
	    }
	    
	    // Comprobar que el label a importar existe en el dispositivo
	    try {
			if (labelOrigen != null && deviceManager.isAliasFree(labelOrigen)) {
				logger.info ("[Pkcs11Manager.importAliasFromKeystore]::El alias '" + labelOrigen + "' no existe en el dispositivo origen de la importación.");
				throw new AliasNotFoundException("El alias '" + labelOrigen + "' no existe en el dispositivo origen de la importación.");
			}
		} catch (SearchingException e) {
			logger.info("[KeyStoreManager.importAliasFromKeystore]::No se puede determinar si existe un alias '" + labelOrigen + "' en el dispositivo origen.", e);
			throw new AliasNotFoundException ("No se puede determinar si existe un alias '" + labelOrigen + "' en el dispositivo origen.", e);
		}
	    
	    // -- Validacion de espacio en tarjeta
	    if (!validateFreeMemory (device.getFreeMemory())) {
	    	logger.info ("[Pkcs11Manager.importAliasFromKeystore]::La memoria de la tarjeta está llena.");
	    	throw new DeviceFullException("La memoria de la tarjeta está llena.");
	    }

	    //-- Iniciamos el proceso de importacion: si labelOrigen es nulo importamos todo	    
	    String[] aliasToImport;
	    if (labelOrigen != null) {
	    	aliasToImport = new String [] {labelOrigen};
	    } else {
	    	try {
				aliasToImport = deviceManager.getAliasNamesList();
			} catch (SearchingException e) {
		    	logger.info ("[Pkcs11Manager.importAliasFromKeystore]::No se ha podido leer la información del dispositivo origen.", e);
		    	throw new LoadingObjectException("No se ha podido leer la información del dispositivo origen.", e);
			}
	    }
    	
	    //-- Recorrer todos los elementos e ir importando
    	for (int i = 0; i < aliasToImport.length; i++) {
			String alias = aliasToImport [i];
			
			//-- Buscar alias destino
			String nombreEnDestino = labelDestino;
			if (labelOrigen != null && labelDestino == null) {
				nombreEnDestino = labelOrigen;
			}
			if (labelOrigen == null) {
				nombreEnDestino = alias;
			}
			
			//-- Si el alias ya existe buscar otro nombre
			int aliasNumber = 0;
			while (true) {
				try {
					if (isAliasFree(nombreEnDestino)) {
						break;
					}
				} catch (SearchingException e) {
					logger.debug("[KeyStoreManager.importAliasFromKeystore]::No se puede determinar si existe un alias '" + nombreEnDestino + "' en el dispositivo.", e);
				}
				nombreEnDestino += aliasNumber;
				aliasNumber++;
			}
			
		    try {
		    	
				//-- Obtener el certificado
				X509CertificateObject userCertificate = DeviceUtil.getX509CertificateObject(deviceManager.getCertificate (alias));
				
				//-- Obtener el objectID que identificará tanto a la clave privada como al certificado en la tarjeta
				byte[] newObjectID = getObjectID (userCertificate);
				
				//-- Obtener clave privada, si existe, y comprobar que sea RSA
				java.security.PrivateKey jcaPrivateKey = (java.security.PrivateKey) deviceManager.getPrivateKey(alias);
			    if (jcaPrivateKey != null && jcaPrivateKey.getAlgorithm().equals("RSA")) {
			    	
			    	//-- Importar la clave al dispositivo
					logger.debug("Creando el objeto clave privada en la tarjeta... ");
			    	PrivateKey iaikPrivateKey = getIaikPrivateKey (jcaPrivateKey, nombreEnDestino, newObjectID, userCertificate, ((java.security.interfaces.RSAPublicKey) userCertificate.getPublicKey()).getPublicExponent());
			    	device.getSession().createObject(iaikPrivateKey);
					logger.debug("Creado el objeto clave privada en la tarjeta... ");
			    	
			    }

				logger.debug("Creando el objeto certificado en la tarjeta... ");

				// create certificate object template
				X509PublicKeyCertificate pkcs11X509PublicKeyCertificate = new X509PublicKeyCertificate();

				pkcs11X509PublicKeyCertificate.getToken().setBooleanValue(Boolean.TRUE);
				pkcs11X509PublicKeyCertificate.getPrivate().setBooleanValue(Boolean.FALSE);
				pkcs11X509PublicKeyCertificate.getLabel().setCharArrayValue(nombreEnDestino.toCharArray());
				pkcs11X509PublicKeyCertificate.getSubject().setByteArrayValue(userCertificate.getSubjectX500Principal().getEncoded());
				pkcs11X509PublicKeyCertificate.getId().setByteArrayValue(newObjectID);
				pkcs11X509PublicKeyCertificate.getIssuer().setByteArrayValue(userCertificate.getIssuerX500Principal().getEncoded());

				// Netscape deviates from the standard here, for use with Netscape rather
				// use
//				pkcs11X509PublicKeyCertificate.getSerialNumber().setByteArrayValue(userCertificate.getSerialNumber().toByteArray());
				pkcs11X509PublicKeyCertificate.getValue().setByteArrayValue(userCertificate.getEncoded());

				// logger.debug(pkcs11X509PublicKeyCertificate);
				device.getSession().createObject(pkcs11X509PublicKeyCertificate);

		    } catch (SearchingException e) {
		    	logger.info ("[Pkcs11Manager.importAliasFromKeystore]::Error al obtener el certificado para el alias '" + alias + "'", e);
		    	throw new SavingObjectException("Error al obtener el certificado para el alias '" + alias + "'", e);
			} catch (CertificateEncodingException e) {
		    	logger.info ("[Pkcs11Manager.importAliasFromKeystore]::Ha ocurrido una excepción manejando el certificado", e);
		    	throw new SavingObjectException("Ha ocurrido una excepción manejando el certificado", e);
			} catch (InitDocumentException e) {
		    	logger.info ("[Pkcs11Manager.importAliasFromKeystore]::No se puede obtener un ID para el dispositivo en base al certificado", e);
		    	throw new SavingObjectException("No se puede obtener un ID para el dispositivo en base al certificado", e);
			} catch (HashingException e) {
		    	logger.info ("[Pkcs11Manager.importAliasFromKeystore]::No se puede obtener un ID para el dispositivo en base al certificado", e);
		    	throw new SavingObjectException("No se puede obtener un ID para el dispositivo en base al certificado", e);
			} catch (LoadingObjectException e) {
		    	logger.info ("[Pkcs11Manager.importAliasFromKeystore]::Error obteniendo la clave privada del alias '" + alias + "'", e);
		    	throw new SavingObjectException("Error obteniendo la clave privada del alias '" + alias + "'", e);
			} catch (TokenException e) {
		    	logger.info ("[Pkcs11Manager.importAliasFromKeystore]::No se puede guardar el certificado o no se puede asociar a una clave privada existente " +
		    			"para el alias '" + alias + "'", e);
		    	throw new SavingObjectException("No se puede guardar el certificado o no se puede asociar a una clave privada existente " +
		    			"para el alias '" + alias + "'", e);
			} 
		}
    	
    	if (this.deleteOrphans) {
	    	//-- Eliminar huerfanos y objetos extraños (GyD)
	    	Pkcs11Util.deleteOrphans(device.getSession());
    	}
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#decrypt(byte[], java.lang.String, java.lang.String)
	 */
	public byte[] decrypt(byte[] encryptedDocument, String label, String cipherAlgorithm) throws AliasNotFoundException, LoadingObjectException, CipherException {
		
		logger.debug("[Pkcs11Manager.decrypt]::Entrada:: " + Arrays.asList(new Object[] { encryptedDocument, label, cipherAlgorithm }));
		
		//-- Comprobar que la etiqueta existe
		try {
			if (isAliasFree(label)) {
				logger.info("[Pkcs11Manager.decrypt]::La etiqueta '" + label + "' no se encuentra en el dispositivo");
				throw new AliasNotFoundException ("La etiqueta '" + label + "' no se encuentra en el dispositivo");
			}
		} catch (SearchingException e) {
			logger.debug("[Pkcs11Manager.decrypt]::No se puede determinar si existe un alias '" + label + "' en el dispositivo.", e);		
			throw new AliasNotFoundException ("No se puede determinar si existe un alias '" + label + "' en el dispositivo.", e);
		}
		
		//-- Comprobar que existe el mecanismo
		Mechanism cipherMechanism;
		try {
			cipherMechanism = getMechanism (cipherAlgorithm);
			if (cipherMechanism == null) {
				logger.info("[Pkcs11Manager.decrypt]::Arangi no acepta el algoritmo " + cipherAlgorithm + " para cifrar en el dispositivo");
				throw new CipherException("Arangi no acepta el algoritmo " + cipherAlgorithm + " para cifrar en el dispositivo");
			}
			List supportedMechanisms = Arrays.asList(device.getToken().getMechanismList());
			if (!supportedMechanisms.contains(cipherMechanism)) {
				logger.info("[Pkcs11Manager.decrypt]::El dispositivo no acepta el encriptado " + cipherAlgorithm);
				throw new CipherException("El dispositivo no acepta el encriptado " + cipherAlgorithm);
			} else {
				MechanismInfo rsaMechanismInfo = device.getToken().getMechanismInfo(cipherMechanism);
				if (!rsaMechanismInfo.isSign()) {
					logger.info("[Pkcs11Manager.decrypt]::El mechanismo " + cipherAlgorithm + " no es aceptado por el dispositivo para cifrar");
					throw new CipherException( "El mechanismo " + cipherAlgorithm + " no es aceptado por el dispositivo para cifrar");
				}
			}
		} catch (TokenException e1) {
			logger.info("[Pkcs11Manager.decrypt]::No se pueden obtener los mecanismos de cifrado del dispositivo", e1);
			throw new CipherException("No se pueden obtener los mecanismos de cifrado del dispositivo", e1);
		}
		
		//-- Obtener la clave privada
		java.security.PrivateKey privateKey = getPrivateKey(label);
		if (privateKey == null) {
			logger.info("[Pkcs11Manager.decrypt]::No existe una clave privada asociada al alias '" + label + "'");
			throw new LoadingObjectException("No existe una clave privada asociada al alias '" + label + "'");
		}
		
	    //-- Desencriptar
		byte[] desencriptado = null;
	    try {
	    	Cipher cipher = Cipher.getInstance(cipherAlgorithm, keystore.getProvider());
	    	cipher.init(Cipher.DECRYPT_MODE, privateKey);
	    	desencriptado = cipher.doFinal(encryptedDocument);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.encrypt]::No existe el algoritmo de cifrado '" + cipherAlgorithm + "'", e);
			throw new CipherException ("No existe el algoritmo de cifrado '" + cipherAlgorithm + "'", e);
		} catch (InvalidKeyException e) {
			logger.info("[KeyStoreManager.encrypt]::La clave privada no es válida", e);
			throw new CipherException ("La clave privada no es válida", e);
		} catch (IllegalBlockSizeException e) {
			logger.info("[KeyStoreManager.encrypt]::El tamaño de la información de un bloque de cifrado es incorrecta", e);
			throw new CipherException ("El tamaño de la información de un bloque de cifrado es incorrecta", e);
		} catch (BadPaddingException e) {
			logger.info("[KeyStoreManager.encrypt]::El relleno de la información no es el adecuado para el algoritmo de relleno", e);
			throw new CipherException ("El relleno de la información no es el adecuado para el algoritmo de relleno", e);
		} catch (NoSuchPaddingException e) {
			logger.info("[KeyStoreManager.encrypt]::No existe el algoritmo de relleno", e);
			throw new CipherException ("No existe el algoritmo de relleno", e);
		} 
    	
		logger.debug("Desencriptando...OK");
    	return desencriptado;
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#encrypt(byte[], java.lang.String, java.lang.String)
	 */
	public byte[] encrypt(byte[] document, String label, String cipherAlgorithm) throws AliasNotFoundException, LoadingObjectException, CipherException {
		
		logger.debug("Encriptando:: " + Arrays.asList(new Object[] { document, label, cipherAlgorithm }));
		
		//-- Comprobar que la etiqueta existe
		try {
			if (isAliasFree(label)) {
				logger.info("[Pkcs11Manager.encrypt]::La etiqueta '" + label + "' no se encuentra en el dispositivo");
				throw new AliasNotFoundException ("La etiqueta '" + label + "' no se encuentra en el dispositivo");
			}
		} catch (SearchingException e) {
			logger.debug("[KeyStoreManager.encrypt]::No se puede determinar si existe un alias '" + label + "' en el dispositivo.", e);		
			throw new AliasNotFoundException ("No se puede determinar si existe un alias '" + label + "' en el dispositivo.", e);
		}
		
		//-- Comprobar que existe el mecanismo
		Mechanism cipherMechanism;
		try {
			cipherMechanism = getMechanism (cipherAlgorithm);
			if (cipherMechanism == null) {
				logger.info("[Pkcs11Manager.encrypt]::Arangi no acepta el algoritmo " + cipherAlgorithm + " para cifrar en el dispositivo");
				throw new CipherException("Arangi no acepta el algoritmo " + cipherAlgorithm + " para cifrar en el dispositivo");
			}
			List supportedMechanisms = Arrays.asList(device.getToken().getMechanismList());
			if (!supportedMechanisms.contains(cipherMechanism)) {
				logger.info("[Pkcs11Manager.encrypt]::El dispositivo no acepta el encriptado " + cipherAlgorithm);
				throw new CipherException("El dispositivo no acepta el encriptado " + cipherAlgorithm);
			} else {
				MechanismInfo rsaMechanismInfo = device.getToken().getMechanismInfo(cipherMechanism);
				if (!rsaMechanismInfo.isSign()) {
					logger.info("[Pkcs11Manager.encrypt]::El mechanismo " + cipherAlgorithm + " no es aceptado por el dispositivo para cifrar");
					throw new CipherException( "El mechanismo " + cipherAlgorithm + " no es aceptado por el dispositivo para cifrar");
				}
			}
		} catch (TokenException e1) {
			logger.info("[Pkcs11Manager.encrypt]::No se pueden obtener los mecanismos de cifrado del dispositivo", e1);
			throw new CipherException("No se pueden obtener los mecanismos de cifrado del dispositivo", e1);
		}
		
		//-- Obtener la clave
		java.security.PublicKey publicKey = getPublicKey(label);
		
		//-- Encriptar
		return encrypt(document, publicKey, cipherAlgorithm);
		
	}

	/**
	 * Cifra un documento con la clave privada pasada como parámetro.
	 * 
	 * @param document Documento a cifrar
	 * @param key Clave privada que cifrará el documento
	 * @param cipherAlgorithm Algoritmo de cifrado 
	 * @return Documento cifrado
	 * @throws CipherException Error durante el cifrado
	 */
	public byte[] encrypt(byte[] document, java.security.PrivateKey key, String cipherAlgorithm) throws CipherException {
		logger.debug ("[KeyStoreManager.encrypt]::Entrada::" + Arrays.asList(new Object[] { document, key, cipherAlgorithm }));
		
		//-- Cifrar
		try {
			byte[] bytesSignature = null;
        	int i=0;
        	while (i<=NUM_RETRIES && bytesSignature==null) {
        		try {
					Cipher cipher = Cipher.getInstance(cipherAlgorithm, keystore.getProvider());
					cipher.init(Cipher.ENCRYPT_MODE, key);
					bytesSignature = cipher.doFinal(document);
					logger.debug("[KeyStoreManager.encrypt]::Se ha completado el cifrado");
        		} catch (RuntimeException e) {
        			//-- Error al firmar, volver a intentar (probablemente problema con el provider de SUN)
        			if (i == NUM_RETRIES) {
        				logger.info("[XAdESSignature.signXAdES]::Runtime error realizando el cifrado", e);
        				throw new CipherException ("Error interno del dispositivo durante el proceso de cifrado", e);
        			}
        		}
        		i++;
        	}
        	
	        return bytesSignature;
	        
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.encrypt]::No existe el algoritmo de cifrado '" + cipherAlgorithm + "'", e);
			throw new CipherException ("No existe el algoritmo de cifrado '" + cipherAlgorithm + "'", e);
		} catch (InvalidKeyException e) {
			logger.info("[KeyStoreManager.encrypt]::La clave privada no es válida", e);
			throw new CipherException ("La clave privada no es válida", e);
		} catch (IllegalBlockSizeException e) {
			logger.info("[KeyStoreManager.encrypt]::El tamaño de la información de un bloque de cifrado es incorrecta", e);
			throw new CipherException ("El tamaño de la información de un bloque de cifrado es incorrecta", e);
		} catch (BadPaddingException e) {
			logger.info("[KeyStoreManager.encrypt]::El relleno de la información no es el adecuado para el algoritmo de relleno", e);
			throw new CipherException ("El relleno de la información no es el adecuado para el algoritmo de relleno", e);
		} catch (NoSuchPaddingException e) {
			logger.info("[KeyStoreManager.encrypt]::No existe el algoritmo de relleno", e);
			throw new CipherException ("No existe el algoritmo de relleno", e);
		} 
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#signDocument(java.io.InputStream, java.lang.String)
	 */
	public byte[] signDocument(InputStream document, String label) throws AliasNotFoundException, HashingException, LoadingObjectException, SignatureException {
		return signDocument(document, label, DigitalSignatureAlgorithm.SHA1_RSA);
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#signDocument(java.io.InputStream, java.lang.String, java.lang.String)
	 */
	public byte[] signDocument(InputStream document, String label,
			String signatureAlgorithm) throws AliasNotFoundException, HashingException, LoadingObjectException, SignatureException {
		
		return signDocument (new InputStreamDocument (document), label, signatureAlgorithm);
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#signDocument(es.accv.arangi.base.document.IDocument, java.lang.String)
	 */
	public byte[] signDocument(IDocument document, String label) throws AliasNotFoundException, HashingException, LoadingObjectException, SignatureException {
		return signDocument(document, label, DigitalSignatureAlgorithm.SHA1_RSA);
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#signDocument(es.accv.arangi.base.document.IDocument, java.lang.String, java.lang.String)
	 */
	public byte[] signDocument(IDocument document, String label, String signatureAlgorithm) 
		throws AliasNotFoundException, HashingException, LoadingObjectException, SignatureException {
		
		logger.debug("[Pkcs11Manager.signDocument]::Entrada::" + Arrays.asList(new Object[] { document, label, signatureAlgorithm }));
		
		// Se obtiene el hash y se cifra. Se podría firmar directamente mediante un mecanismo, pero
		// si el documento es grande la tarjeta acaba saturándose.
		
		//-- Obtener los altoritmos de hashing y firma
		String hashingAlgorithm;
		String encryptAlgorithm;
		try {
			hashingAlgorithm = DigitalSignatureAlgorithm.getHashingAlgorithm(signatureAlgorithm);
			encryptAlgorithm = DigitalSignatureAlgorithm.getCipherAlgorithm(signatureAlgorithm);
			logger.debug("[Pkcs11Manager.signDocument]::Algorimo de hashing: " + hashingAlgorithm + "::Algoritmo" +
					" de encriptado: " + encryptAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[Pkcs11Manager.signDocument]::El algoritmo de firma no existe en Arangi", e);
			throw new SignatureException ("El algoritmo de firma no existe en Arangi", e);
		}
		
		//-- Obtener el hash
		byte[] hash = document.getHash(hashingAlgorithm);
		
		//-- Cifrar el hash
		try {
			return signBytesHash (hash, label, encryptAlgorithm);
		} catch (CipherException e) {
			logger.info("[Pkcs11Manager.signDocument]::No ha sido posible cifrar el hash para obtener la firma", e);
			throw new SignatureException ("No ha sido posible cifrar el hash para obtener la firma", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#signHash(byte[], java.lang.String, java.lang.String)
	 */
	public byte[] signBytesHash(byte[] hash, String label) throws HashingException, AliasNotFoundException, LoadingObjectException, CipherException {
		return signBytesHash(hash, label, CipherAlgorithm.RSA);
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#signHash(byte[], java.lang.String, java.lang.String, java.lang.String)
	 */
	public byte[] signBytesHash(byte[] hash, String label, String cipherAlgorithm) 
			throws HashingException, AliasNotFoundException, LoadingObjectException, CipherException {
		
		logger.debug("Firmando hash (=cifrando hash):: " + Arrays.asList(new Object[] { hash, label, cipherAlgorithm }));
		
		//-- Algoritmo de hashing
		String hashingAlgorithm = HashingAlgorithm.getAlgorithmNameFromHash(hash);
		
		//-- Obtener el hash a cifrar
		try {
			hash = DeviceUtil.getSignData(new ObjectIdentifier(HashingAlgorithm.getOID(hashingAlgorithm)), hash);
		} catch (IOException e) {
			logger.info("[KeyStoreManager.signHash]::No es posible obtener el objeto DER que contiene el hash", e);
			throw new HashingException ("No es posible obtener el objeto DER que contiene el hash", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.signHash]::El algoritmo de hashing '" + hashingAlgorithm + "' provoca una excepción", e);
			throw new HashingException ("El algoritmo de hashing '" + hashingAlgorithm + "' provoca una excepción", e);
		}
		
		//-- Comprobar que la etiqueta existe
		try {
			if (isAliasFree(label)) {
				logger.info("[KeyStoreManager.signHash]::La etiqueta '" + label + "' no se encuentra en el dispositivo");
				throw new AliasNotFoundException ("La etiqueta '" + label + "' no se encuentra en el dispositivo");
			}
		} catch (SearchingException e) {
			logger.debug("[KeyStoreManager.signHash]::No se puede determinar si existe un alias '" + label + "' en el dispositivo.", e);		
			throw new AliasNotFoundException ("No se puede determinar si existe un alias '" + label + "' en el dispositivo.", e);
		}
		
		//-- Comprobar que existe el mecanismo
		Mechanism cipherMechanism;
		try {
			cipherMechanism = getMechanism (cipherAlgorithm);
			if (cipherMechanism == null) {
				logger.info("[KeyStoreManager.signHash]::Arangi no acepta el algoritmo " + cipherAlgorithm + " para cifrar en el dispositivo");
				throw new CipherException("Arangi no acepta el algoritmo " + cipherAlgorithm + " para cifrar en el dispositivo");
			}
			List supportedMechanisms = Arrays.asList(device.getToken().getMechanismList());
			if (!supportedMechanisms.contains(cipherMechanism)) {
				logger.info("[KeyStoreManager.signHash]::El dispositivo no acepta el encriptado " + cipherAlgorithm);
				throw new CipherException("El dispositivo no acepta el encriptado " + cipherAlgorithm);
			} else {
				MechanismInfo rsaMechanismInfo = device.getToken().getMechanismInfo(cipherMechanism);
				if (!rsaMechanismInfo.isSign()) {
					logger.info("[KeyStoreManager.signHash]::El mechanismo " + cipherAlgorithm + " no es aceptado por el dispositivo para cifrar");
					throw new CipherException( "El mechanismo " + cipherAlgorithm + " no es aceptado por el dispositivo para cifrar");
				}
			}
		} catch (TokenException e1) {
			logger.info("[KeyStoreManager.signHash]::No se pueden obtener los mecanismos de cifrado del dispositivo", e1);
			throw new CipherException("No se pueden obtener los mecanismos de cifrado del dispositivo", e1);
		}
		
		//-- Obtener la clave privada
		java.security.PrivateKey privateKey = getPrivateKey(label);
		if (privateKey == null) {
			logger.info("[KeyStoreManager.signHash]::No existe una clave privada asociada al alias '" + label + "'");
			throw new LoadingObjectException("No existe una clave privada asociada al alias '" + label + "'");
		}
		
	    //-- Firmar (cifrar)
		byte[] firma = encrypt(hash, privateKey, cipherAlgorithm);
    	
		logger.debug("Firmando hash...OK");
    	return firma;
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#deleteObjects(java.lang.String)
	 */
	public int deleteObjects(String label) throws DeletingObjectException {
		logger.debug("Borrando objetos...");
		int deleted = 0;
		logger.debug("Label: " + label);
		deleted = Pkcs11Util.deleteObjectsByLabel(device.getSession(), label, this.deleteOrphans);
		logger.debug("Borrando objetos...OK");
		return deleted;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#getAliasList()
	 */
	public String[] getAliasNamesList() throws SearchingException {
		
		logger.debug("Obteniendo lista de alias...");
		
		List<String> result = new ArrayList<String>();
		List<Object> lObjects = Pkcs11Util.findAllObjects(device.getSession(), null);
		for (Iterator<Object> iterator = lObjects.iterator(); iterator.hasNext();) {
			iaik.pkcs.pkcs11.objects.Storage iaikObject = (iaik.pkcs.pkcs11.objects.Storage) iterator.next();
			if (!result.contains(iaikObject.getLabel().toString())) {
				result.add(iaikObject.getLabel().toString());
			}
		}
		
		return (String[]) result.toArray(new String[0]);
			
	}
	 
	/**
	 * Obtiene la clave privada asociada al alias. El password para recuperar la
	 * clave será el PIN del dispositivo (caso por defecto).La clave se obtiene utilizando el 
	 * provider PKCS#11 de Sun.<br><br>
	 * 
	 * Importante: no funciona con pares de claves sueltas, es decir necesita que las claves 
	 * estén asociadas a un certificado.
	 * 
	 * @param alias Alias donde se encuentra la clave
	 * @return Clave privada asociada al alias o null si no existe el alias o éste
	 * no contiene una clave privada
	 * 
	 * @throws LoadingObjectException No se puede obtener la clave. Una posible causa 
	 * podría ser que el password de la misma no coincide con el PIN del dispositivo.
	 */
	public java.security.PrivateKey getPrivateKey(String alias) throws LoadingObjectException {
		return getPrivateKey (alias, pin);
	}

	/**
	 * Busca la clave privada asociada al alias. La clave se obtiene utilizando el 
	 * provider PKCS#11 de Sun.<br><br>
	 * 
	 * Importante: no funciona con pares de claves sueltas, es decir necesita que las claves 
	 * estén asociadas a un certificado.
	 * 
	 * @param alias Alias donde se encuentra la clave
	 * @param keyPassword Password de la clave privada
	 * @return Clave privada asociada al alias o null si no existe el alias o éste
	 * 	no contiene una clave privada
	 * 
	 * @throws LoadingObjectException No se puede obtener la clave. Probáblemente el 
	 * 	password pasado como parámetro no sea correcto.
	 */
	public java.security.PrivateKey getPrivateKey(String alias,	String keyPassword) throws LoadingObjectException {
		logger.debug("getPrivateKeys...");
		logger.debug("findInAlias: " + alias);
		
		//-- Obtener la clave privada
		logger.debug("[AbstractPkcs11Manager.getPrivateKey]::Entrada::" + Arrays.asList(new Object [] { alias }));
		
		//-- Lo normal será abrir la clave privada de SUN para firmar. Si no hay keystore
		//-- se obtiene una clave privada en base a IAIK que sirve para que no aparezcan
		//-- NullPointerExceptions
		if (this.keystore != null) {
			try {
				//-- Devolver la clave privada con el proveedor de sun
				logger.debug("[AbstractPkcs11Manager.getPrivateKey]::Encontrada clave privada con alias '" + alias + "'");
				
				java.security.PrivateKey privateKey = (java.security.PrivateKey) keystore.getKey(alias, keyPassword.toCharArray());
				if (privateKey == null) {
					logger.debug("No se ha encontrado la clave privada para el alias '" + alias + "'");
				}
				
				return privateKey;
		
			} catch (KeyStoreException e) {
				//-- Este error sólo se produce si no se ha inicializado la clase, lo que
				//-- ya se comprueba y no puede ocurrir
				logger.debug("[AbstractPkcs11Manager.getPrivateKey]::Keystore no inicializado", e);
				return null;
			} catch (NoSuchAlgorithmException e) {
				//-- Este error sólo se produce si no se ha cargado Bouncy, con lo que
				//-- el error se habría producido mucho antes
				logger.debug("[AbstractPkcs11Manager.getPrivateKey]::No se encuentra el algoritmo para alguno de los certificados de la cadena", e);
				return null;
			} catch (UnrecoverableKeyException e) {
				logger.info("[AbstractPkcs11Manager.getPrivateKey]::La clave privada del alias '" + alias + "' no ha podido ser recuperada. " +
						"Compruebe que su password coincide con el PIN del dispositivo.", e);
				throw new LoadingObjectException("La clave privada del alias '" + alias + "' no ha podido ser recuperada. Compruebe " +
						"que su password coincide con el PIN del dispositivo.", e);
			} 
		} else {
			//-- Sin keystore -> devolver clave basada en IAIK
			//-- Obtener la clave privada
			RSAPrivateKey iaikKey = (RSAPrivateKey) getIAIKPrivateKey(alias);
			if (iaikKey == null) {
				logger.debug("No se ha encontrado la clave privada para el alias '" + alias + "'");
				return null;
			}
			
			//-- Pasamos a PrivateKey de Sun
			try {
				logger.debug("Se va a devolver una clave privada para el alias '" + alias + "'");
				return new TokenPrivateKey(iaikKey).getSunPrivateKey();
			} catch (Exception e) {
				logger.info("Error transformando clave privada a RSAPrivateKey de Sun...");
				throw new LoadingObjectException ("Error transformando clave privada a RSAPrivateKey de Sun", e);
			}

		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#getPublicKey(java.lang.String)
	 */
	public java.security.PublicKey getPublicKey(String alias) throws LoadingObjectException {
		logger.debug("getPublicKey...");

		//-- Obtener la clave pública en un par de claves
		PublicKey iaikKey = getIAIKPublicKey(alias);
		if (iaikKey != null) {
			//-- Pasamos a PublicKey de Sun
			try {
				logger.debug("Se va a devolver una clave pública para el alias '" + alias + "'");
				return new TokenPublicKey(iaikKey).getSunPublicKey();
			} catch (Exception e) {
				logger.info("Error transformando clave publica a RSAPublicKey de Sun...", e);
				throw new LoadingObjectException ("Error transformando clave publica a RSAPublicKey de Sun", e);
			}
		}
		
		//-- Obtener la clave pública dentro de un certificado
		X509PublicKeyCertificate iaikCert = getIAIKCertificate(alias);
		if (iaikCert != null) {
			//-- Pasamos a PublicKey de Sun
			try {
				logger.debug("Se va a devolver una clave pública para el alias '" + alias + "'");
				X509Certificate certificate = Util.getCertificate(iaikCert.getValue().getByteArrayValue());
				return certificate.getPublicKey();
			} catch (Exception e) {
				logger.info("Error transformando clave publica a RSAPublicKey de Sun...", e);
				throw new LoadingObjectException ("Error transformando clave publica a RSAPublicKey de Sun", e);
			}
		}
		
		//-- Si estamos aquí es porque no se ha obtenido ninguna clave pública
		logger.debug("No se ha encontrado la clave pública para el alias '" + alias + "'");
		return null;
		
	}
	

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#deletePrivateKey(java.lang.String)
	 */
	public int deletePrivateKey(String alias) throws DeletingObjectException {
		logger.debug("[Pkcs11Manager.deletePrivateKey]::Entrada::" + alias);
		int deleted = 0;
		try {
			//-- Obtener la clave privada
			RSAPrivateKey iaikKey = (RSAPrivateKey) getIAIKPrivateKey(alias);
			if (iaikKey == null) {
				logger.debug("[Pkcs11Manager.deletePrivateKey]::No se ha encontrado una clave privada para el alias '" + alias + "'");
				return 0;
			}
			
			//-- Eliminar la clave privada
			device.getSession().destroyObject(iaikKey);
			
		} catch (Exception e) {
			logger.info("Borrando objetos...ERROR", e);
			throw new DeletingObjectException("Error borrando objetos con la etiqueta ``" + alias + "´´", e);
		}
		logger.debug("Borrando objetos...OK");
		return deleted;
	}

	public int deleteCertificate (String alias) throws DeletingObjectException {
		logger.debug("[Pkcs11Manager.deletePrivateKey]::Entrada::" + alias);
		int deleted = 0;
		try {
			//-- Obtener el certificado
			X509PublicKeyCertificate iaikCert;
			try {
				iaikCert = getIAIKCertificate(alias);
			} catch (LoadingObjectException e) {
				logger.info("[Pkcs11Manager.getCertificate]::Error buscando el certificado para el alias '" + alias + "'", e);
				throw new SearchingException ("Error buscando el certificado para el alias '" + alias + "'", e);
			}
			if (iaikCert != null) {
				//-- Eliminar la clave privada
				device.getSession().destroyObject(iaikCert);
				deleted++;
			}
			
		} catch (Exception e) {
			logger.info("Borrando objetos...ERROR", e);
			throw new DeletingObjectException("Error borrando objetos con la etiqueta ``" + alias + "´´", e);
		}
		logger.debug("Borrando objetos...OK");
		return deleted;
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#format()
	 */
	public void clear() throws DeletingObjectException {

		logger.debug ("[Pkcs11Manager.clear]::Entrada");

		//-- Recorrer todos los alias y eliminar los objetos que hay en ellos
		int counter = 0;
		List<Object> lObjects;
		try {
			lObjects = Pkcs11Util.findAllObjects(device.getSession(), null);
		} catch (SearchingException e) {
			logger.info("[Pkcs11Manager.clear]::No se pueden obtener los elementos a formatear", e);
			throw new DeletingObjectException ("No se pueden obtener los elementos a formatear", e);
		}
		
		try {
			for (Iterator<Object> iterator = lObjects.iterator(); iterator.hasNext();) {
				iaik.pkcs.pkcs11.objects.Object iaikObject = (iaik.pkcs.pkcs11.objects.Object) iterator.next();
				logger.debug ("[Pkcs11Manager.clear]::Procediendo a eliminar objeto");
				device.getSession().destroyObject(iaikObject);
				logger.debug ("[Pkcs11Manager.clear]::Objeto eliminado");
				counter++;
			}
		} catch (TokenException e) {
			logger.info("[Pkcs11Manager.clear]::No se ha podido eliminar un objeto", e);
			
			//-- Tratar de no dejar huerfanos
			logger.debug("[Pkcs11Manager.clear]::Tratando de eliminar huérfanos tras fracaso en el formateo");
			Pkcs11Util.deleteOrphans(device.getSession());
			logger.debug("[Pkcs11Manager.clear]::Huérfanos eliminados");
			
			logger.info("[Pkcs11Manager.clear]::Error eliminando un elemento del dispositivo", e);
			throw new DeletingObjectException ("Error eliminando un elemento del dispositivo", e);
		}

		logger.debug ("[Pkcs11Manager.clear]::Fin::Se han eliminado " + counter + " objetos");

	}

	/**
	 * Realiza un formateado del dispositivo
	 * 
	 * @param puk PUK del dispositivo
	 * @param newPin Nuevo pin del dispositivo tras la inicialización
	 * @param newLabel Nueva etiqueta del dispositivo formateado
	 * @throws FormatException Errores durante la inicialización del dispositivo
	 */
	public void format (String puk, String newPin, String newLabel)  throws FormatException {

		logger.debug ("[Pkcs11Manager.format]::Entrada");
		
		//-- Cerrar las sesiones abiertas
		logger.debug ("[Pkcs11Manager.format]::Cerrando las sesiones");
		try {
			device.getToken().closeAllSessions();
			logger.debug ("[Pkcs11Manager.format]::Sesiones cerradas");
		} catch (TokenException e) {
			logger.info ("[Pkcs11Manager.format]::Error durante el proceso de cerrado previo de las sesiones", e);
			throw new FormatException("Error durante el proceso de cerrado previo de las sesiones", e);
		}

		//-- Formatear
		logger.debug ("[Pkcs11Manager.format]::Iniciando inicialización");
		try {
			this.device.getToken().initToken(puk.toCharArray(), newLabel);
			logger.debug ("[Pkcs11Manager.format]::Dispositivo inicializado");
		} catch (TokenException e) {
			logger.info ("[Pkcs11Manager.format]::Error durante el proceso de inicialización del dispositivo", e);
			throw new FormatException("Error durante el proceso de inicialización del dispositivo", e);
		} 
		
		//-- Inicializar el PIN
		try {
			
	        device.setSession(device.getToken().openSession(Token.SessionType.SERIAL_SESSION, Token.SessionReadWriteBehavior.RW_SESSION, null, null));
	        device.getSession().login(Session.UserType.SO, puk.toCharArray());
	        device.getSession().initPIN(newPin.toCharArray());
	        device.getToken().closeAllSessions();
	        this.pin = newPin;
		} catch (TokenException e) {
			logger.info ("[Pkcs11Manager.format]::Error durante el proceso de inicialización del PIN tras el formateo del dispositivo", e);
			throw new FormatException("Error durante el proceso de inicialización del PIN tras el formateo del dispositivo", e);
		} 
		
		//-- Volver a inicializar el manager
		try {
			initialize(pin, false, this.device.getManufacturer());
		} catch (Exception e) {
			logger.info ("[Pkcs11Manager.format]::No ha sido posible volver a abrir el dispositivo tras la inicialización", e);
			throw new FormatException("No ha sido posible volver a abrir el dispositivo tras la inicialización", e);
		}
	}
	
	/**
	 * Obtiene el objeto dispositivo que hay tras el manager.
	 * 
	 * @return Dispositivo
	 */
	public Pkcs11Device getDevice() {
		return device;
	}

	/**
	 * Obtiene el nombre del módulo que está gestionando el dispositivo
	 * 
	 * @return Nombre de módulo de gestión PKCS#11
	 */
	public String getDeviceModuleName(){
		logger.debug("getDeviceModuleName(): " + device.getModuleName());
		return this.device.getModuleName();
	}
	
	/**
	 * Obtiene el nombre del fabricante del dispositivo
	 * 
	 * @return Nombre del fabricante
	 */
	public String getManufacturerName () {
		logger.debug("getManufacturerName(): " + device.getManufacturer().getManufacturerName());
		return this.device.getManufacturer().getManufacturerName();
	}
	
	/**
	 * Obtiene el nombre del fabricante del dispositivo tal y como aparece
	 * en los campos del propio dispositivo
	 * 
	 * @return ID del fabricante
	 */
	public String getManufacturerID () {
		logger.debug("getManufacturerID(): " + device.getManufacturerId());
		return this.device.getManufacturerId();
	}
	
	/**
	 * Obtiene el identificador del dispositivo.
	 * 
	 * @return ID del dispositivo
	 */
	public long getId() {
		logger.debug("getId(): " + device.getId());
		return this.device.getId();
	}
	
	/**
	 * Obtiene la etiqueta del dispositivo, normalmente relacionada con el módulo
	 * con el que se abrió.
	 * 
	 * @return Etiqueta del dispositivo
	 */
	public String getLabel() {
		logger.debug("getLabel(): " + device.getLabel());
		return this.device.getLabel();
	}
	
	/**
	 * Obtiene el modelo del dispositivo
	 * 
	 * @return Modelo del dispostivo
	 */
	public String getModel() {
		logger.debug("getModel(): " + device.getModel());
		return this.device.getModel();
	}
	
	/**
	 * Obtiene el número de serie del dispositivo
	 * 
	 * @return Número de serie del dispositivo
	 */
	public String getSerialNumber() {
		logger.debug("getSerialNumber(): " + device.getSerialNumber());
		return this.device.getSerialNumber();
	}
	
	/**
	 * Obtiene el tamaño total de la memoria de la tarjeta
	 * 
	 * @return Tamaño total de la memoria de la tarjeta
	 */
	public long getTotalMemory () {
		logger.debug("getTotalMemory(): " + device.getTotalMemory());
		return this.device.getTotalMemory();
	}
	
	/**
	 * Obtiene la memoria que queda libre en la tarjeta
	 * 
	 * @return Tamaño de la memoria que queda libre en la tarjeta
	 */
	public long getFreeMemory () {
		logger.debug("getAvailableMemory(): " + device.getFreeMemory());
		return this.device.getFreeMemory();
	}
	
	/**
	 * Obtiene el objeto fabricante que abrió el dispositivo
	 * 
	 * @return Objeto fabricante que abrió el dispositivo
	 */
	public Pkcs11Manufacturer getManufacturer() {
		logger.debug("getManufacturer(): " + device.getManufacturer());
		return this.device.getManufacturer();
	}
	
	/**
	 * Desbloquea el PIN de la tarjeta. Para entrar habrá que hacerlo con el PUK
	 * 
	 * @param newPIN Nuevo PIN
	 * @throws UnlockPINException No ha sido posible desbloquear el PIN
	 */
	public void unlockPIN (String newPIN) throws UnlockPINException {
		logger.debug("[AbstractPKCS11Manager.unlockPIN]::Entrada");
		try {
			device.getSession().initPIN(newPIN.toCharArray());
		} catch (TokenException e) {
			logger.info("[AbstractPKCS11Manager.unlockPIN]::No se tienen permisos para desbloquear el PIN del dispositivo", e);
			throw new UnlockPINException ("No se tienen permisos para desbloquear el PIN del dispositivo", e);
		}
	}

	/**
	 * Carga la dll de IAIK en la carpeta temporal de Arangi y devuelve
	 * el fichero.
	 */
	public static File loadIAIKDllFile () throws IAIKDLLNotFoundException {
		//-- Obtener valores dependiendo de si estamos en un JRE de 32 o 64 bits
		String dllName = IAIK_WINX86_DLL_NAME;
		String dllClasspath = IAIK_WINX86_DLL_CLASSPATH_PATH;
		if (System.getProperty("os.arch").indexOf("64") > -1) {
			dllName = IAIK_WINX64_DLL_NAME;
			dllClasspath = IAIK_WINX64_DLL_CLASSPATH_PATH;
		}
		
		//-- Comprobar si la dll ya está en la carpeta temporal
		File file = new File (getArangiTemporalFolder(), dllName);
		if (!file.exists() || file.length() == 0) {
			//-- No está, así que guardarla
			try {
				saveToArangiTemporalFolder(new Util().getClass().getClassLoader().getResourceAsStream(dllClasspath), dllName);
			} catch (IOException e) {
				logger.debug("[AbstractPkcs11Manager.loadIAIKDllFile]::No ha sido posible guardar la DLL de IAIK en el path: " + file.getAbsolutePath(), e);
				
				//-- No se ha podido guardar. Comprobar si ya existe en el path (windows/system32). Si es así devolver null
				//-- para que no se utilice un fichero en una ubicación específica.
				try {
					System.loadLibrary(dllName);
					return null;
				} catch (Throwable t) {				
					logger.info("[AbstractPkcs11Manager.loadIAIKDllFile]::No existe la DLL de IAIK en el sistema y no puede ser guardada en el path: " + file.getAbsolutePath(), e);
					throw new IAIKDLLNotFoundException ("No existe la DLL de IAIK en el sistema", t);
				}
			}
		}
		
		//-- devolver el fichero
		return file;
	}
	
	/**
	 * Da valor al flag que indica si se quiere que se borren las
	 * claves huerfanas (claves sin certificado)
	 * 
	 * @param deleteOrphans Flag
	 */
	public void setDeleteOrphans (boolean deleteOrphans) {
		this.deleteOrphans = deleteOrphans;
	}

	//-- Métodos privados
	
	/*
	 * Obtiene la clave privada en formato IAIK
	 */
	private PrivateKey getIAIKPrivateKey(String alias) throws LoadingObjectException {
		logger.debug("getIAIKPrivateKey...");
		logger.debug("alias: " + alias);

		List<Object> lPKs = new ArrayList<Object>();
		PrivateKey privateKeyTemplate = new iaik.pkcs.pkcs11.objects.PrivateKey();
		privateKeyTemplate.getLabel().setCharArrayValue(alias.toCharArray());
		try {
			lPKs = Pkcs11Util.findAllObjects(device.getSession(), privateKeyTemplate);
		} catch (SearchingException e) {
			throw new LoadingObjectException ("Se ha producido un error buscando la clave privada en el dispositivo", e);
		}
		
		if (lPKs.isEmpty()) {
			logger.info("El alias '" + alias + "' no contiene una clave privada");
			return null;
		}
		
		return (RSAPrivateKey) lPKs.get(0);
	}

	/*
	 * Obtiene la clave pública en formato IAIK 
	 */
	private PublicKey getIAIKPublicKey(String alias) throws LoadingObjectException {
		logger.debug("getIAIKPublicKey...");
		logger.debug("alias: " + alias);

		List<Object> lPKs = new ArrayList<Object>();
		PublicKey publicKeyTemplate = new iaik.pkcs.pkcs11.objects.PublicKey();
		publicKeyTemplate.getLabel().setCharArrayValue(alias.toCharArray());
		try {
			lPKs = Pkcs11Util.findAllObjects(device.getSession(), publicKeyTemplate);
		} catch (SearchingException e) {
			throw new LoadingObjectException ("Se ha producido un error buscando la clave pública en el dispositivo", e);
		}
		
		if (lPKs.isEmpty()) {
			logger.info("El alias '" + alias + "' no contiene una clave pública");
			return null;
		}
		
		return (PublicKey) lPKs.get(0);
	}
	
	/*
	 * Obtiene el Certificado en formato IAIK 
	 */
	private X509PublicKeyCertificate getIAIKCertificate (String alias) throws LoadingObjectException {
		logger.debug("getIAIKCertificate...");
		logger.debug("alias: " + alias);

		List<Object> lCerts = new ArrayList<Object>();
		X509PublicKeyCertificate certificateTemplate = new X509PublicKeyCertificate();
		certificateTemplate.getLabel().setCharArrayValue(alias.toCharArray());
		try {
			lCerts = Pkcs11Util.findAllObjects(device.getSession(), certificateTemplate);
		} catch (SearchingException e) {
			throw new LoadingObjectException ("Se ha producido un error buscando el certificado en el dispositivo", e);
		}
		
		if (lCerts.isEmpty()) {
			logger.info("El alias '" + alias + "' no contiene certificados");
			return null;
		}
		
		return (X509PublicKeyCertificate) lCerts.get(0);
	}

	/*
	 * Devuelve el mecanismo que se corresponde con el algoritmo
	 */
	private static Mechanism getMechanism(String algorithm) {
		
		if (algorithm.equals (CipherAlgorithm.RSA)) {
			return Mechanism.RSA_PKCS;
		}
		if (algorithm.equals (CipherAlgorithm.DSA)) {
			return Mechanism.DSA;
		}
		
		return null;
	}
	
	/*
	 * Valida si hay espacio suficiente en la tarjeta (<5kb)
	 */
	private boolean validateFreeMemory(long freeMemory) {
		return freeMemory > 5120;
	}

	  /*
	   * Obtiene una peticion de certificado (PKCS#10) asociada a partir de los parametros.
	   * 
	   * @param session Sesión en el dispositivo
	   * @param kp Clave privada con la que se firmará el PKCS#10
	   * @param subjectDN Nombre del sujeto
	   * @param subjectAlternativeDN Nombre alternativo del sujeto
	   * @return Objeto que representa el PKCS#10
	   * 
	   * @throws Exception Error durante el proceso 
	   */
	  private static CertificationRequest getCertificationRequest(Session session, KeyPair kp, String subjectDN, String subjectAlternativeDN) {

	    logger.debug("[Pkcs11Manager.getCertificationRequest]::" + Arrays.asList(new Object[] { "<rsaKeys>", subjectDN, subjectAlternativeDN }));
	    
	    if (subjectDN == null) {
	      throw new IllegalArgumentException("El campo SubjectDN no puede ser nulo.");
	    }
	    
	    if (kp == null) {
	      throw new IllegalArgumentException("El par de claves no puede ser nulo.");
	    }
	    
	    try {
			    // -- Obtenemos el nombre estandar
			    X500Name x509Name = Certificate.stringToBcX500Name( subjectDN );
			    logger.debug("[Pkcs11Manager.getCertificationRequest]::::x509Name: " + x509Name);
			      
				    //-- Parametros para la creacion del bloque de extensiones
		    Map<String,String> extensionsMap = new HashMap<String,String>();
		    extensionsMap.put("subjectAlternativeNameDN", subjectAlternativeDN);
	
		    //-- Generamos el set de extensiones del certificado
		    ASN1Set pkcs10AttributeSet = CSRUtil.getPKCS10Set(kp.getPublic(), kp.getPublic(), extensionsMap);
	
		    // Generate PKCS10 certificate request
		    byte[]                  bytes = kp.getPublic().getEncoded();
		    ByteArrayInputStream    bIn = new ByteArrayInputStream(bytes);
		    ASN1InputStream         dIn = new ASN1InputStream(bIn);
	
		    CertificationRequestInfo cri = null;
	        logger.debug("[Pkcs11Manager.getCertificationRequest]::" + ASN1Dump.dumpAsString(pkcs10AttributeSet) + "\n");
	        
	        SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo((ASN1Sequence)dIn.readObject());
	        cri = new CertificationRequestInfo(x509Name, spki, pkcs10AttributeSet);
		      
		    logger.debug("[Pkcs11Manager.getCertificationRequest]::CertificationRequestInfo creado.");
		    
		    //-- Creacion del buffer de datos a firmar
		    byte[] buffer = cri.getEncoded(ASN1Encoding.DER);
	
		    ASN1ObjectIdentifier sigOID = new ASN1ObjectIdentifier(DigitalSignatureAlgorithm.getOID(DigitalSignatureAlgorithm.SHA1_RSA));
		    AlgorithmIdentifier sigAlgId = new AlgorithmIdentifier(sigOID);
		    
		    logger.debug("[Pkcs11Manager.getCertificationRequest]::Array a firmar: " + buffer.length);
		    byte[] abFirma = null;
		    int i=0;
		    while (i<=NUM_RETRIES && abFirma==null) {
		    	try {
		    		abFirma = SignUtil.generateSignature(session, kp.getPrivate(), buffer);
		    	} catch (Exception e) {
		    		//-- Si es el último intento lanzar la excepción
		    		if (i==NUM_RETRIES) {
		    			logger.debug ("[Pkcs11Manager.getCertificationRequest]::No ha sido posible firmar la petición", e);
		    			throw e;
		    		}
		    	}
		    	i++;
		    }
		    
		    logger.debug("[Pkcs11Manager.getCertificationRequest]::firma length: " + abFirma.length );
		    
		    DERBitString derBitStr = new DERBitString(abFirma);
		    
		    //-- Creamos la certification request
		    return new CertificationRequest(cri, sigAlgId, derBitStr);
		    
		} catch (Exception e) {
			// No se llega
			return null;
		}
		    
	  }
	  
	  /*
	   * Método que obtiene el identificador que tendrá un certificado y su clave privada asociada
	   */
	  protected byte[] getObjectID(X509CertificateObject userCertificate) throws CertificateEncodingException, InitDocumentException, HashingException {
		  InputStreamDocument isDocument = new InputStreamDocument (new ByteArrayInputStream (userCertificate.getEncoded()));
		  byte[] certificateFingerprint = isDocument.getHash();
		  boolean[] keyUsage = userCertificate.getKeyUsage();
		  logger.debug("keyUsage: " + keyUsage);
		  
		  byte[] subjectKeyIdentifier = userCertificate.getExtensionValue(Extension.subjectKeyIdentifier.getId());
		  
		  logger.debug("Obtenido certificado de usuario.");
		  logger.debug("Creando la clave privada en la tarjeta... ");
		  
		  //-- Obtener el ID que asocia clave y certificado
		  if (subjectKeyIdentifier != null) {
			  // we take the key identifier from the certificate
			  return subjectKeyIdentifier;
		  } else {
			  // then we simply take the fingerprint of the certificate
			  return certificateFingerprint;
		  }
	  }

	 /**
	 * Obtiene una clave privada IAIK en base a la clave privada de SUN pasada como
	 * parámetro
	 * 
	 * @param jcaPrivateKey 
	 * @param nombreEnDestino 
	 * @param newObjectID 
	 * @param userCertificate 
	 * @return Clave privada de IAIK
	 * @throws TokenException 
	   */
	  protected PrivateKey getIaikPrivateKey (java.security.PrivateKey jcaPrivateKey, String nombreEnDestino, byte[] newObjectID, 
			  X509Certificate userCertificate, BigInteger publicExponent) throws TokenException {
	    	
	    	java.security.interfaces.RSAPrivateKey jcaRsaPrivateKey = (java.security.interfaces.RSAPrivateKey)jcaPrivateKey;
		
			// create private key object template
			RSAPrivateKey pkcs11RsaPrivateKey = new RSAPrivateKey();

			pkcs11RsaPrivateKey.getSensitive().setBooleanValue(Boolean.TRUE);
			pkcs11RsaPrivateKey.getToken().setBooleanValue(Boolean.TRUE);
			pkcs11RsaPrivateKey.getPrivate().setBooleanValue(Boolean.TRUE);
			pkcs11RsaPrivateKey.getLabel().setCharArrayValue(nombreEnDestino.toCharArray());

			pkcs11RsaPrivateKey.getId().setByteArrayValue(newObjectID);
			
			//-- Obtener el mecanismo de firma
			MechanismInfo signatureMechanismInfo = getSignatureMechanismInfo (device.getToken());
			
			//-- Obtener keyUsage
			boolean[] keyUsage = null;
			if (userCertificate != null) {
				pkcs11RsaPrivateKey.getSubject().setByteArrayValue(userCertificate.getSubjectX500Principal().getEncoded());
				keyUsage = userCertificate.getKeyUsage();
			}
			
			if (keyUsage != null) {

				logger.debug("Tenemos KeyUsage");
	        
				// set the attributes in a way netscape does, this should work with most
				// tokens
				if (signatureMechanismInfo != null) {
					
					logger.debug("Tenemos signatureMechanismInfo");
					logger.debug("KeyUsage.dataEncipherment : " + keyUsage[DeviceUtil.KEYUSAGE_DATAENCIPHERMENT]);
					logger.debug("KeyUsage.keyCertSign      : " + keyUsage[DeviceUtil.KEYUSAGE_KEYCERTSIGN]);
					logger.debug("KeyUsage.encipherOnly      : " + keyUsage[DeviceUtil.KEYUSAGE_ENCIPHERONLY]);
					logger.debug("KeyUsage.digitalSignature: " + keyUsage[DeviceUtil.KEYUSAGE_DIGITALSIGNATURE]);
					logger.debug("KeyUsage.cRLSign          : " + keyUsage[DeviceUtil.KEYUSAGE_CRLSIGN]);
					logger.debug("KeyUsage.nonRepudiation  : " + keyUsage[DeviceUtil.KEYUSAGE_NONREPUDIATION]);
					logger.debug("KeyUsage.keyAgreement    : " + keyUsage[DeviceUtil.KEYUSAGE_KEYAGREEMENT]);
					logger.debug("KeyUsage.keyEncipherment  : " + keyUsage[DeviceUtil.KEYUSAGE_KEYENCIPHERMENT]);
					logger.debug("signatureMechanismInfo.isDecrypt(): " + signatureMechanismInfo.isDecrypt());
					logger.debug("signatureMechanismInfo.isSign(): " + signatureMechanismInfo.isSign());
					logger.debug("signatureMechanismInfo.isSignRecover(): " + signatureMechanismInfo.isSignRecover());
					logger.debug("signatureMechanismInfo.isDerive(): " + signatureMechanismInfo.isDerive());
					logger.debug("signatureMechanismInfo.isUnwrap(): " + signatureMechanismInfo.isUnwrap());

	          
					//-- Para evitar problemas CKA_DECRYPT = TRUE
					pkcs11RsaPrivateKey.getDecrypt().setBooleanValue(Boolean.TRUE);
	          
					pkcs11RsaPrivateKey.getSign().setBooleanValue(
							new Boolean((keyUsage[DeviceUtil.KEYUSAGE_DIGITALSIGNATURE] || keyUsage[DeviceUtil.KEYUSAGE_KEYCERTSIGN]
	                           || keyUsage[DeviceUtil.KEYUSAGE_CRLSIGN] || keyUsage[DeviceUtil.KEYUSAGE_NONREPUDIATION])
	                          && signatureMechanismInfo.isSign()));

					pkcs11RsaPrivateKey.getSignRecover().setBooleanValue(
							new Boolean((keyUsage[DeviceUtil.KEYUSAGE_DIGITALSIGNATURE] || keyUsage[DeviceUtil.KEYUSAGE_KEYCERTSIGN]
	                           || keyUsage[DeviceUtil.KEYUSAGE_CRLSIGN] || keyUsage[DeviceUtil.KEYUSAGE_NONREPUDIATION])
	                          && signatureMechanismInfo.isSignRecover()));

					pkcs11RsaPrivateKey.getDerive().setBooleanValue(new Boolean(keyUsage[DeviceUtil.KEYUSAGE_KEYAGREEMENT] && signatureMechanismInfo.isDerive()));
					pkcs11RsaPrivateKey.getUnwrap().setBooleanValue(new Boolean(keyUsage[DeviceUtil.KEYUSAGE_KEYENCIPHERMENT] && signatureMechanismInfo.isUnwrap()));

				} else {

					// if we have no mechanism information, we try to set the flags
					// according to the key usage only
					pkcs11RsaPrivateKey.getDecrypt().setBooleanValue(
							new Boolean(keyUsage[DeviceUtil.KEYUSAGE_DATAENCIPHERMENT] || keyUsage[DeviceUtil.KEYUSAGE_KEYCERTSIGN]));

					pkcs11RsaPrivateKey.getSign().setBooleanValue(
							new Boolean(keyUsage[DeviceUtil.KEYUSAGE_DIGITALSIGNATURE] || keyUsage[DeviceUtil.KEYUSAGE_KEYCERTSIGN]
	                          || keyUsage[DeviceUtil.KEYUSAGE_CRLSIGN] || keyUsage[DeviceUtil.KEYUSAGE_NONREPUDIATION]));

					pkcs11RsaPrivateKey.getSignRecover().setBooleanValue(
							new Boolean(keyUsage[DeviceUtil.KEYUSAGE_DIGITALSIGNATURE] || keyUsage[DeviceUtil.KEYUSAGE_KEYCERTSIGN]
	                          || keyUsage[DeviceUtil.KEYUSAGE_CRLSIGN] || keyUsage[DeviceUtil.KEYUSAGE_NONREPUDIATION]));

					pkcs11RsaPrivateKey.getDerive().setBooleanValue(new Boolean(keyUsage[DeviceUtil.KEYUSAGE_KEYAGREEMENT]));
					pkcs11RsaPrivateKey.getUnwrap().setBooleanValue(new Boolean(keyUsage[DeviceUtil.KEYUSAGE_KEYENCIPHERMENT]));

				}
				
			} else {
	        
				logger.debug("NO tenemos KeyUsage");

				// if there is no keyusage extension in the certificate, try to set all
				// flags according to the mechanism info
				if (signatureMechanismInfo != null) {

					pkcs11RsaPrivateKey.getSign().setBooleanValue(new Boolean(signatureMechanismInfo.isSign()));
					pkcs11RsaPrivateKey.getSignRecover().setBooleanValue(new Boolean(signatureMechanismInfo.isSignRecover()));
					pkcs11RsaPrivateKey.getDecrypt().setBooleanValue(new Boolean(signatureMechanismInfo.isDecrypt()));
					pkcs11RsaPrivateKey.getDerive().setBooleanValue(new Boolean(signatureMechanismInfo.isDerive()));
					pkcs11RsaPrivateKey.getUnwrap().setBooleanValue(new Boolean(signatureMechanismInfo.isUnwrap()));

				} else {

					// if we have neither mechanism info nor key usage we just try all
					pkcs11RsaPrivateKey.getSign().setBooleanValue(Boolean.TRUE);
					pkcs11RsaPrivateKey.getSignRecover().setBooleanValue(Boolean.TRUE);
					pkcs11RsaPrivateKey.getDecrypt().setBooleanValue(Boolean.TRUE);
					pkcs11RsaPrivateKey.getDerive().setBooleanValue(Boolean.TRUE);
					pkcs11RsaPrivateKey.getUnwrap().setBooleanValue(Boolean.TRUE);
				}

			}

			pkcs11RsaPrivateKey.getModulus().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(jcaRsaPrivateKey.getModulus()));
			pkcs11RsaPrivateKey.getPrivateExponent().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(jcaRsaPrivateKey.getPrivateExponent()));
			pkcs11RsaPrivateKey.getPublicExponent().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(publicExponent));

			if (jcaRsaPrivateKey instanceof java.security.interfaces.RSAPrivateCrtKey) {

				// if we have the CRT field, we write it to the card
				// e.g. gemsafe seems to need it
				java.security.interfaces.RSAPrivateCrtKey crtKey = (java.security.interfaces.RSAPrivateCrtKey) jcaRsaPrivateKey;
				pkcs11RsaPrivateKey.getPrime1().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(crtKey.getPrimeP()));
				pkcs11RsaPrivateKey.getPrime2().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(crtKey.getPrimeQ()));
				pkcs11RsaPrivateKey.getExponent1().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(crtKey.getPrimeExponentP()));
				pkcs11RsaPrivateKey.getExponent2().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(crtKey.getPrimeExponentQ()));
				pkcs11RsaPrivateKey.getCoefficient().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray(crtKey.getCrtCoefficient()));

			}

			return pkcs11RsaPrivateKey;
	  }
	  
	 /**
	 * Obtiene una clave publica IAIK en base a la clave pública de SUN pasada como
	 * parámetro
	 * 
	 * @param jcaPrivateKey 
	 * @param nombreEnDestino 
	 * @param newObjectID 
	 * @param userCertificate 
	 * @return Clave privada de IAIK
	 * @throws TokenException 
	   */
	  protected PublicKey getIaikPublicKey (java.security.PublicKey jcaPublicKey, String nombreEnDestino, byte[] newObjectID) throws TokenException {
		  
		  //-- Obtener el mecanismo de firma
		  MechanismInfo signatureMechanismInfo = getSignatureMechanismInfo (device.getToken());
		  
		  //-- Obtener la clave pública
		  RSAPublicKey pkcs11RsaPublicKey = new RSAPublicKey();
		  pkcs11RsaPublicKey.getId().setByteArrayValue(newObjectID);
		  pkcs11RsaPublicKey.getLabel().setCharArrayValue(nombreEnDestino.toCharArray());
		  pkcs11RsaPublicKey.getToken().setBooleanValue(Boolean.TRUE);
		  pkcs11RsaPublicKey.getPrivate().setBooleanValue(Boolean.FALSE);
		  pkcs11RsaPublicKey.getModifiable().setBooleanValue(Boolean.TRUE);
		  if (signatureMechanismInfo != null) {
			  pkcs11RsaPublicKey.getVerify().setBooleanValue(new Boolean(signatureMechanismInfo.isVerify()));
			  pkcs11RsaPublicKey.getVerifyRecover().setBooleanValue(new Boolean(signatureMechanismInfo.isVerifyRecover()));
			  pkcs11RsaPublicKey.getEncrypt().setBooleanValue(new Boolean(signatureMechanismInfo.isEncrypt()));
			  pkcs11RsaPublicKey.getDerive().setBooleanValue(new Boolean(signatureMechanismInfo.isDerive()));
			  pkcs11RsaPublicKey.getWrap().setBooleanValue(new Boolean(signatureMechanismInfo.isWrap()));
		  } else {
			  pkcs11RsaPublicKey.getVerify().setBooleanValue(Boolean.TRUE);
			  pkcs11RsaPublicKey.getEncrypt().setBooleanValue(Boolean.TRUE);
		  }
//		  pkcs11RsaPublicKey.getKeyType().setPresent(false);
//		  pkcs11RsaPublicKey.getObjectClass().setPresent(false);
		  
		  //-- Añadir valores de la clave pública de SUN
		  java.security.interfaces.RSAPublicKey sunRsaPublicKey = (java.security.interfaces.RSAPublicKey)jcaPublicKey;
		  pkcs11RsaPublicKey.getPublicExponent().setByteArrayValue(iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray (sunRsaPublicKey.getPublicExponent()));
		  byte [] modulus = iaik.pkcs.pkcs11.Util.unsignedBigIntergerToByteArray (sunRsaPublicKey.getModulus());
		  pkcs11RsaPublicKey.getModulusBits().setLongValue(new Long (modulus.length * 8));
		  pkcs11RsaPublicKey.getModulus().setByteArrayValue(modulus);
		  
		  return pkcs11RsaPublicKey;
	  }
	  
	/*
	 * Método que obtiene información de los dispositivos conectados para una
	 * serie de modulos PKCS#11 asociados a sus fabricantes
	 * 
	 * @param manufacturers Fabricantes de dispositivos
	 * @return Lista de objetos de tipo Pkcs11Device
	 * @throws IAIKDLLNotFoundException No es posible cargar la DLL de IAIK, por 
	 * 	lo que no se puede trabajar con dispositivos PKCS#11
	 */
	protected static List<Pkcs11Device> getConnectedDevices (Pkcs11Manufacturer[] manufacturers) throws IAIKDLLNotFoundException   {
		
		logger.debug("[Pkcs11Manager.getConnectedDevices]::Entrada::" + manufacturers);
		
		//-- Al ser estático hay que mirar que exista la dll de IAIK
		loadIAIKDllFile();
		
		//-- Buscar elementos para cada fabricante
		List<Pkcs11Device> lDevices = new ArrayList<Pkcs11Device>();
		for (int i=0;i<manufacturers.length;i++) {
			try {
				lDevices.addAll(manufacturers[i].getConnectedDevices());
			} catch (Throwable e) {
				logger.debug ("[Pkcs11Manager.getConnectedDevices]::No se han podido obtener los dispositivos para el " +
						"fabricante " + manufacturers[i].getManufacturerName() +": " + e.getMessage());
			} 
		}
			
		//-- Devolver tabla
		return lDevices;
			
	}

	protected static MechanismInfo getSignatureMechanismInfo (Token token) throws TokenException  {
		// check out what attributes of the keys we may set using the mechanism
		// info
		HashSet supportedMechanisms = new HashSet(Arrays.asList(token.getMechanismList()));

		if (supportedMechanisms.contains(Mechanism.RSA_PKCS)) {
			logger.debug("Mechanism.RSA_PKCS");
			return token.getMechanismInfo(Mechanism.RSA_PKCS);
      
		} else if (supportedMechanisms.contains(Mechanism.RSA_X_509)) {
			logger.debug("Mechanism.RSA_X_509");
			return token.getMechanismInfo(Mechanism.RSA_X_509);
      
		} else if (supportedMechanisms.contains(Mechanism.RSA_9796)) {
			logger.debug("Mechanism.RSA_9796");
			return token.getMechanismInfo(Mechanism.RSA_9796);
      
		} else if (supportedMechanisms.contains(Mechanism.RSA_PKCS_OAEP)) {
			logger.debug("Mechanism.RSA_PKCS_OAEP");
			return token.getMechanismInfo(Mechanism.RSA_PKCS_OAEP);
      
		} else {
			logger.debug("Mechanism --> null");
			return null;
		}
		

	}
	
	/**
	 * Obtiene un keystore con el proveedor PKCS#11 de Sun para tratar con
	 * el mismo dispositivo que está gestionando este manager.
	 * 
	 * @return Keystore Keystore PKCS#11
	 * @throws InitializeProviderException No es posible inicializar el proveedor PKCS#11
	 * @throws OpeningDeviceException Error abriendo el dispositivo con el proveedor PKCS11
	 * @throws IncorrectPINException PIN incorrecto
	 */
	protected KeyStore getKeyStore () throws InitializeProviderException, OpeningDeviceException, IncorrectPINException {
		logger.debug("[Pkcs11Manager.getKeyStore]::Entrada");
		
		//-- Nombre del provider de SUN
		sunProviderName = this.device.getModuleName() + "-" + this.device.getToken().getSlot().getSlotID();
		
		//-- Obtener el path al fichero de la librería
		int javaVersion = 6;
		if (System.getProperty("java.version").indexOf(".") > -1) {
			try {
				javaVersion = Integer.parseInt(System.getProperty("java.version").split("\\.")[1]);
			} catch (Exception e) {
				logger.info("No se puede determinar la versión de Java: " + System.getProperty("java.version"));
			}
		}

		String pathLibrary = new File (this.device.getManufacturer().getPkcs11LibPath()).getAbsolutePath();
		if (javaVersion > 7) {
			pathLibrary = "\"" + new File (this.device.getManufacturer().getPkcs11LibPath()).getAbsolutePath().replace('\\', '/') + "\"";
		}
		
		//-- Obtener la configuración del provider de SUN
		String configFile = "name=" + sunProviderName +
			"\nlibrary=" + pathLibrary + 
			"\nslot=" + this.device.getToken().getSlot().getSlotID() +
			"\nshowInfo=true\n"; 
		logger.debug("[Pkcs11Manager.getKeyStore]::Configuración::" + configFile);

		//-- Iniciando proveedor
		Provider provider;
		try {
			Class sunClass = Class.forName("sun.security.pkcs11.SunPKCS11");
			Constructor constructor = sunClass.getConstructor(new Class[] { InputStream.class });
			provider = (Provider)constructor.newInstance(new ByteArrayInputStream(configFile.getBytes()));
			
//			provider = new sun.security.pkcs11.SunPKCS11(new ByteArrayInputStream(configFile.getBytes()));
			sunProviderName = provider.getName();
			if (Security.getProvider(provider.getName()) != null) {
				Security.removeProvider(provider.getName());
			}
			Security.addProvider(provider);
		} catch (Exception e) {
			logger.info ("[Pkcs11Manager.getKeyStore]::No se ha podido iniciar el provider del módulo " + this.device.getModuleName() + ". Versión Java: " + System.getProperty("java.version") + "\nConfigFile:\n" + configFile, e);
			throw new InitializeProviderException("No ha sido posible iniciar el proveedor de SUN", e);
		} 
		
		//-- Abriendo el keystore
	    try {
		    KeyStore ks = KeyStore.getInstance("PKCS11", provider);
		    ks.load(null, pin.toCharArray()); 
			logger.debug("[Pkcs11Manager.getKeyStore]::Keystore abierto");
			
			return ks;
		} catch (NoSuchAlgorithmException e1) {
			logger.info("[Pkcs11Manager.getKeyStore]::No existe el algoritmo que comprueba la integridad del dispositivo", e1);
			throw new OpeningDeviceException ("No existe el algoritmo que comprueba la integridad del dispositivo", e1);
		} catch (CertificateException e1) {
			logger.info("[Pkcs11Manager.getKeyStore]::Alguno de los certificados del dispositivo no puede ser guardado", e1);
			throw new OpeningDeviceException ("Alguno de los certificados del dispositivo no puede ser guardado", e1);
		} catch (IOException e1) {
			logger.info("[Pkcs11Manager.getKeyStore]::El PIN introducido no es correcto", e1);
			throw new IncorrectPINException ("El PIN introducido no es correcto", e1);
		} catch (KeyStoreException e1) {
			logger.info("[Pkcs11Manager.getKeyStore]::Error de keystore", e1);
			throw new OpeningDeviceException ("Error de keystore", e1);
		} catch (Exception e1) {
			logger.info("[Pkcs11Manager.getKeyStore]::Error desconocido creando y abriendo el keystore PKCS11", e1);
			throw new OpeningDeviceException ("Error desconocido creando y abriendo el keystore PKCS11", e1);
		}

	}

	/*
	 * Inicializa un gestor de PKCS#11 realizando previamente un proceso de 
	 * autodetección de la tarjeta insertada el lector. En caso de que
	 * hayan varios dispositivos conectados se elegirá el primero de ellos.
	 * Este método se puede usar para el caso más habitual: que sólo exista
	 * un dispositivo PKCS#11 conectado.
	 * 
	 * @param deviceType Lista de tipos de dispositivo a detectar por Arangi.
	 * @throws ModuleNotFoundException No se ha encontrado ningún módulo PKCS#11
	 */
	protected void initialize(String pin, boolean isPUK, Pkcs11Manufacturer[] manufacturers) throws ModuleNotFoundException, IAIKDLLNotFoundException, InitializeProviderException, OpeningDeviceException, IncorrectPINException, IncorrectPUKException, LockedPINException {
		initialize(pin, isPUK, manufacturers, true);
	}
	
	/*
	 * Inicializa un gestor de PKCS#11 realizando previamente un proceso de 
	 * autodetección de la tarjeta insertada el lector. En caso de que
	 * hayan varios dispositivos conectados se elegirá el primero de ellos.
	 * Este método se puede usar para el caso más habitual: que sólo exista
	 * un dispositivo PKCS#11 conectado.
	 * 
	 * El parámetro withKeystore indica si se quiere cargar el keystore de
	 * SUN. Si no se va a utilizar funciones como la firma no es necesario
	 * cargarlo.
	 * 
	 * @param deviceType Lista de tipos de dispositivo a detectar por Arangi.
	 * @throws ModuleNotFoundException No se ha encontrado ningún módulo PKCS#11
	 */
	protected void initialize(String pin, boolean isPUK, Pkcs11Manufacturer[] manufacturers, boolean withKeyStore) throws ModuleNotFoundException, IAIKDLLNotFoundException, InitializeProviderException, OpeningDeviceException, IncorrectPINException, IncorrectPUKException, LockedPINException {

		//-- Autodetección de PKCS#11
		logger.debug("Autodetectando fabricantes PKCS11...");
		List<String> lManufacturers = new ArrayList<String> ();
		List<Throwable> lExceptions = new ArrayList<Throwable> ();
		for (int i = 0; i < manufacturers.length; i++) {
			try {
				logger.debug("Probando con " + manufacturers[i].getManufacturerName() + "...");
				
				this.device = manufacturers[i].getInstance(pin, isPUK);
				
				logger.debug("Probando con " + manufacturers[i].getManufacturerName() + "... OK.");
				break;
			} catch (ModuleNotFoundException e) {
				lManufacturers.add(manufacturers[i].getManufacturerName());
				lExceptions.add (e);
			} catch (DeviceNotFoundException e) {
				lManufacturers.add(manufacturers[i].getManufacturerName());
				lExceptions.add (e);
			} catch (IncorrectPINException e) {
				logger.info("PIN Incorrecto", e);
				throw e;
			} catch (IncorrectPUKException e) {
				logger.info("PUK Incorrecto", e);
				throw e;
			} catch (LockedPINException e) {
				logger.info("PIN bloqueado", e);
				throw e;
			} catch (Throwable t) {
				// Probablemente no estemos en Windows y no se pueda linkar la dll de IAIK
				lManufacturers.add(manufacturers[i].getManufacturerName());
				lExceptions.add (t);
			}
		}
		if (this.device == null){
			for (int i=0;i<lManufacturers.size();i++) {
				logger.debug("Excepción cargando módulo del fabricante '" + lManufacturers.get(i) + "'", (Throwable) lExceptions.get(i));
			}
			logger.info("Token nulo tras autodetección. ¡ERROR! ¡Ningún fabricante de la lista por defecto ha sido encontrado!");
			throw new ModuleNotFoundException("Ha fallado la la autodetección de un módulo PKCS#11 conectado al equipo");
		}
		
		this.pin = pin;
		this.openWithPuk = isPUK;

		logger.debug("Autodetectando fabricantes PKCS11...OK");
		
		//-- Obtener el keystore
		if (!isPUK && withKeyStore) {
			this.keystore = getKeyStore();
		}

	}
	
	/*
	 * Inicializa un gestor de PKCS#11 usando la implementación del PKCS#11 
 	 * indicado. En caso de que hayan varios dispositivos conectados se 
 	 * elegirá el primero de ellos. Este método se puede usar para el caso 
 	 * más habitual: que sólo exista un dispositivo PKCS#11 conectado.
	 * 
	 * @param pkcs11LibPath Path a la librería PKCS#11
	 * 
	 * @throws DeviceNotFoundException No existen dispositivos para la 
	 * 	libería PKCS#11 o no existe un dispositivo para el valor de 'tokenID'.
	 * @throws ModuleNotFoundException No se ha encontrado ningún módulo PKCS#11
	 * 	de la lista.
	 */
	protected void initialize (String pin, boolean isPUK, Pkcs11Manufacturer manufacturer) throws DeviceNotFoundException, ModuleNotFoundException, IAIKDLLNotFoundException, IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException, InitializeProviderException {
		initialize(pin, isPUK, manufacturer, true);
	}
	
	/*
	 * Inicializa un gestor de PKCS#11 usando la implementación del PKCS#11 
 	 * indicado. En caso de que hayan varios dispositivos conectados se 
 	 * elegirá el primero de ellos. Este método se puede usar para el caso 
 	 * más habitual: que sólo exista un dispositivo PKCS#11 conectado.
	 * 
	 * El parámetro withKeystore indica si se quiere cargar el keystore de
	 * SUN. Si no se va a utilizar funciones como la firma no es necesario
	 * cargarlo.
	 * 
	 * @param pkcs11LibPath Path a la librería PKCS#11
	 * 
	 * @throws DeviceNotFoundException No existen dispositivos para la 
	 * 	libería PKCS#11 o no existe un dispositivo para el valor de 'tokenID'.
	 * @throws ModuleNotFoundException No se ha encontrado ningún módulo PKCS#11
	 * 	de la lista.
	 */
	protected void initialize (String pin, boolean isPUK, Pkcs11Manufacturer manufacturer, boolean withKeyStore) throws DeviceNotFoundException, ModuleNotFoundException, IAIKDLLNotFoundException, IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException, InitializeProviderException {

		//-- Carga explícita
		this.device = manufacturer.getInstance(pin, isPUK);
		
		this.pin = pin;
		this.openWithPuk = isPUK;
		logger.debug("Cargando módulo para el fabricante PKCS#11 ``" + manufacturer + "´´... OK");
		
		//-- Obtener el keystore si se accede con PIN. Con PUK no se puede firmar, así
		//-- que no se necesita acceder a claves privadas
		if (!isPUK && withKeyStore) {
			this.keystore = getKeyStore();
		}
		
	}

	/*
	 * Inicializa un gestor de PKCS#11 usando un dispositivo concero. 
	 * 
	 * @param device Dispositivo a inicializar
	 * @param password
	 * @param isPUK Cierto si se abre con el PUK
	 * 
	 * @throws DeviceNotFoundException No existen dispositivos para la 
	 * 	libería PKCS#11 o no existe un dispositivo para el valor de 'tokenID'.
	 * @throws ModuleNotFoundException No se ha encontrado ningún módulo PKCS#11
	 * 	de la lista.
	 */
	protected void initialize (Pkcs11Device device, String password, boolean isPUK) throws DeviceNotFoundException, ModuleNotFoundException, IAIKDLLNotFoundException, IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException, InitializeProviderException {
		initialize(device, password, isPUK, true);
	}
	
	/*
	 * Inicializa un gestor de PKCS#11 usando un dispositivo concero. 
	 * 
	 * El parámetro withKeystore indica si se quiere cargar el keystore de
	 * SUN. Si no se va a utilizar funciones como la firma no es necesario
	 * cargarlo.
	 * 
	 * @param device Dispositivo a inicializar
	 * @param password
	 * @param isPUK Cierto si se abre con el PUK
	 * 
	 * @throws DeviceNotFoundException No existen dispositivos para la 
	 * 	libería PKCS#11 o no existe un dispositivo para el valor de 'tokenID'.
	 * @throws ModuleNotFoundException No se ha encontrado ningún módulo PKCS#11
	 * 	de la lista.
	 */
	protected void initialize (Pkcs11Device device, String password, boolean isPUK, boolean withKeyStore) throws DeviceNotFoundException, ModuleNotFoundException, IAIKDLLNotFoundException, IncorrectPINException, IncorrectPUKException, LockedPINException, OpeningDeviceException, InitializeProviderException {

		//-- Abrir el dispositivo
		this.device = device.getManufacturer().getInstance(device.getId(), password, isPUK);
		this.pin = password;
		this.openWithPuk = isPUK;
		if (!isPUK && withKeyStore) {
			this.keystore = getKeyStore();
		}
		
	}

}
