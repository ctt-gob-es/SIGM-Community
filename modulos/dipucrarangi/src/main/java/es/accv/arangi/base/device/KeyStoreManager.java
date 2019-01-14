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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.exception.device.AliasNotFoundException;
import es.accv.arangi.base.exception.device.ClosingStreamException;
import es.accv.arangi.base.exception.device.DeviceNotFoundException;
import es.accv.arangi.base.exception.device.IncorrectPINException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.OpeningDeviceException;
import es.accv.arangi.base.exception.device.ReadingStreamException;
import es.accv.arangi.base.exception.device.SaveDeviceException;
import es.accv.arangi.base.exception.device.SavingObjectException;

/**
 * Clase para tratar almacenes de claves en formato software. Los tipos que se
 * pueden tratar con esta clase son PKCS#12 y JKS.<br><br>
 * 
 * Ejemplo de uso:<br><br>
 * 
 * <code>
 *  IDocument document = new FileDocument(new File ("/documento.txt"));<br>
 * 	KeyStoreManager manager = new KeyStoreManager (new File ("/keystores/ks.pk12"), "1234");<br>
 * 	String aliases = manager.getAliasNamesList();<br>
 * 	for (int i=0;i<aliases.length;i++) {<br>
 *  &nbsp;&nbsp;System.out.println ("Certificate: " + manager.getCertificate(aliases[i]));<br>
 * 	&nbsp;&nbsp;System.out.println ("Firma: " + manager.signDocument(document, aliases[i])); // Firma con la clave privada del alias<br>
 * 	}<br>
 * </code><br>
 * 
 * A la hora de usar los métodos hay que tener en cuenta las particularidades de 
 * cada almacén de claves. Por ejemplo, el formato PKCS#12 no permite que existan
 * certificados solitarios en un alias, necesariamente deben ir acompañados de
 * su correspondiente clave privada.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class KeyStoreManager extends AbstractKeyStoreManager {
	
	/** Logger de clase */
	private static Logger logger = Logger.getLogger(KeyStoreManager.class);
	
	//---- CONSTRUCTORES -----//
	
	/**
	 * Inicializa un gestor de KeyStores para clases hijas. No
	 * usar con keystores en fichero.
	 * 
	 * @throws DeviceManagerException
	 */
	protected KeyStoreManager() {
		super();
		logger.debug("Creando Keystore de clase hija ...OK");
	}

	/**
 	 * Inicializa un gestor de almacenes de claves software.
 	 * 
	 * @param ksFile Fichero keystore (PKCS#12 o JKS)
	 * @param pin PIN del dispositivo
	 * @throws DeviceNotFoundException El fichero no existe
	 * @throws ClosingStreamException No se ha podido cerrar correctamente el stream de lectura
	 *  del fichero
	 * @throws OpeningDeviceException Error no controlado abriendo el dispositivo
	 * @throws IncorrectPINException El PIN proporcionado para abrir el dispositivo no es correcto
	 */
	public KeyStoreManager(File ksFile, String pin) throws DeviceNotFoundException, ClosingStreamException, OpeningDeviceException, IncorrectPINException {
    	logger.debug("[KeyStoreManager]::ksPath: " + ksFile);
    	
    	//-- Cargar el keystore
		ksFilePath = ksFile.getAbsolutePath();
		if (ksFilePath != null){
			//-- Lectura de disco del keystore existente
			logger.debug("Leyendo Keystore existente...");
			InputStream is = null;
			try {
				is = new FileInputStream(ksFile);
				this.contenido = readStream (is);
			} catch (Exception e) {
				logger.info("[KeyStoreManager]::Excepción accediendo a ``" + ksFilePath + "´´: ", e);
				throw new DeviceNotFoundException("Ha fallado la carga del Keystore ``" + ksFilePath + "´´", e);			
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						logger.info("[KeyStoreManager]::Excepción cerrando el estream de lectura al fichero", e);
						throw new ClosingStreamException("Ha fallado el cierre del stream de lectura del fichero", e);			
					}
				}
			}
			logger.debug("Leyendo Keystore existente...OK");
		}
		
		//-- Abrir el keystore
		open(pin);
	}

	/**
 	 * Inicializa un gestor de almacenes de claves software mediante un stream de lectura.
 	 * 
	 * @param is Stream de lectura al keystore (PKCS#12 o JKS)
	 * @param pin PIN del dispositivo
	 * @throws DeviceNotFoundException El stream de lectura pasado como parámetro es nulo
	 * @throws ReadingStreamException Error leyendo el stream de lectura
	 * @throws OpeningDeviceException Error no controlado abriendo el dispositivo
	 * @throws IncorrectPINException El PIN proporcionado para abrir el dispositivo no es correcto
	 */
	public KeyStoreManager(InputStream is, String pin) throws DeviceNotFoundException, ReadingStreamException, OpeningDeviceException, IncorrectPINException {
    	logger.debug("[KeyStoreManager]::is: " + is);
		if (is == null){
			logger.info("[KeyStoreManager]::El stream de lectura es nulo");
			throw new DeviceNotFoundException("El stream de lectura es nulo");			
		}
		
		try {
			this.contenido = readStream (is);
		} catch (IOException e) {
			logger.info("[KeyStoreManager]::No ha sido posible leer el stream de lectura", e);
			throw new ReadingStreamException("No ha sido posible leer el stream de lectura", e);			
		}
		
		//-- Abrir el keystore
		open(pin);

	}

	//---- IMPLEMENTACION DE MÉTODOS DE IDeviceManager -----//

	/**
	 * Si el cierre se produce en un keystore en memoria, todos los cambios se perderán a no ser
	 * que se haya persistido antes en disco. Si este objeto se inicializó con un fichero, en el
	 * momento de cerrar se persistirán los cambios en él.
	 */
	public void close() {
		logger.debug ("Cerrando Keystore...");
		
		//-- Si el keystore se creo en disco se hace persistir con los cambios
		//-- producidos
		if (this.ksFilePath != null) {
			try {
				save (new File (this.ksFilePath));
			} catch (SaveDeviceException e) {
				logger.info ("[KeyStoreManager.close]::No se ha podido guardar el keystore antes de cerrar", e);
			}
		}
		
		try {
			pin = null;
			ks = null;
			ksType = null;
		} catch (Throwable e) {
			logger.info("Excepción durante cierre de dispositivo. Ignorando...", e);
		} 
		logger.debug("Cerrando Keystore...OK");
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#importAliasFromKeystore(java.io.InputStream, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void importAliasFromKeystore(InputStream is, String password, String labelOrigen, String labelDestino) 
		throws DeviceNotFoundException, ReadingStreamException, AliasNotFoundException, LoadingObjectException, SavingObjectException, OpeningDeviceException, IncorrectPINException {
		
	    logger.debug("[KeyStoreManager.importAliasFromKeystore]::Entrada::" + Arrays.asList(new Object[] {is, password, labelOrigen, labelDestino}));
		
		//-- Obtener el manager del keystore
    	KeyStoreManager keystoreManager;
		keystoreManager = new KeyStoreManager (is, password);
    	
		//-- Llamar al método
		importAliasFromKeystore(keystoreManager, labelOrigen, labelDestino);
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#isAliasFree(java.lang.String)
	 */
	public boolean isAliasFree(String alias){
		
		logger.debug ("[KeyStoreManager.isAliasFree]::Entrada::" + alias);
		
		boolean isFree = false;
		try {
			isFree = !ks.containsAlias(alias);
		} catch (KeyStoreException e) {
			logger.info("[KeyStoreManager.isAliasFree]::Error comprobando si existe el alias '" + alias + "'", e);
		}
		return isFree;
	}
	
	//---- METODOS PÚBLICOS ----//

	/**
	   * Crea un fichero PKCS#12 que contiene en un alias el certificado y el par de claves
	   * que se pasan como parámetro. El método devuelve el fichero codificado en base64
	   * 
	   * @param rsaKeys Par de claves o null si sólo se quiere añadir el certificado
	   * @param certificate Certificado 
	   * @param alias Alias donde se guardará el certificado y el par de claves
	   * @param pin Contraseña del keystore
	   * @return Fichero keystore creado en formato base64
	   * @throws Exception Excepciones tratando y serializando el keystore
	   */
	public static String getP12Base64(KeyPair rsaKeys, X509Certificate certificate, String alias, String pin) throws Exception {

	    if (certificate == null) {
	      throw new Exception("Recibido certificado nulo.");
	    }

	    // Generamos la cadena de certificados.
	    Certificate[] chain = new Certificate[1];
	    chain[0] = certificate;

	    // Generamos el KeyStore
	    KeyStore ks = KeyStore.getInstance("PKCS12", ArangiObject.CRYPTOGRAPHIC_PROVIDER_NAME);
	    ks.load(null, pin.toCharArray());

	    // Importamos la clave y el certificado.
	    if (rsaKeys != null) {
	    	ks.setKeyEntry(alias, rsaKeys.getPrivate(), null, chain);
	    }

	    // Creamos el objeto P12
	    ByteArrayOutputStream baosPKCS12 = new ByteArrayOutputStream();
	    ks.store(baosPKCS12, pin.toCharArray());
	    baosPKCS12.close();

	    byte[] abPKCS12 = baosPKCS12.toByteArray();
	    String pkcs12B64 = new String(Base64.encode(abPKCS12));

	    // Para evitar el error del InvalidKey, es necesario esto:
	    ks.load(new ByteArrayInputStream(abPKCS12), pin.toCharArray());

	    return pkcs12B64;

	}

	/**
	   * Crea un fichero PKCS#12 vacío
	   * 
	   * @param storeType Tipo de keystore ({@link AbstractKeyStoreManager#STORE_TYPE_JKS} o {@link AbstractKeyStoreManager#STORE_TYPE_PKCS12})
	   * @param pin Contraseña del keystore
	   * @return Manager al keystore recien creado
	   * @throws OpeningDeviceException Excepciones creando y serializando el keystore
	   */
	public static KeyStoreManager getEmptyKeyStore(String storeType, String pin)throws OpeningDeviceException {

		logger.info("[KeyStoreManager.getEmptyKeyStore]::Entrada::" + Arrays.asList (new Object[] { storeType }));
		
		try {
		    // Generamos el KeyStore
			KeyStore ks;
			if (storeType.equals(KeyStoreManager.STORE_TYPE_JKS)) {
				ks = KeyStore.getInstance(storeType);
			} else {
				ks = KeyStore.getInstance(storeType, ArangiObject.CRYPTOGRAPHIC_PROVIDER);
			}
		    ks.load(null, pin.toCharArray());
	
		    // Creamos el objeto 
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ks.store(baos, pin.toCharArray());
		    baos.close();
		    
		    //-- Creamos el manager
		    return new KeyStoreManager(new ByteArrayInputStream(baos.toByteArray()), pin);
		    
		} catch (KeyStoreException e) {
			logger.info("[KeyStoreManager.getEmptyKeyStore]::Error de keystore creando uno vacío de tipo '" + storeType + "'", e);
			throw new OpeningDeviceException("Error de keystore creando uno vacío de tipo '" + storeType + "'", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.getEmptyKeyStore]::El algoritmo usado para verificar la integridad del nuevo keystore de tipo '" + storeType + "' no existe", e);
			throw new OpeningDeviceException("El algoritmo usado para verificar la integridad del nuevo keystore de tipo '" + storeType + "' no existe", e);
		} catch (Exception e) {
			logger.info("[KeyStoreManager.getEmptyKeyStore]::Error creando un keystore vacío de tipo '" + storeType + "'", e);
			throw new OpeningDeviceException("Error creando un keystore vacío de tipo '" + storeType + "'", e);
		} 

	}

	/**
	 * Guarda en formato PKCS#12 el keystore que se está tratando. 
	 * 
	 * @param destination Fichero destino
	 * @throws SaveDeviceException No es posible escribir en el fichero o no es posible
	 * 	guardar alguno de los elementos del dispositivo
	 * @throws SaveDeviceException Error guardando el dispositivo en fichero.
	 * @throws LoadingObjectException No se pueden cargar ciertos elementos del almacén de
	 * 	claves origen para pasarlos al destino
	 */
	public void saveToPkcs12(File destination) throws SaveDeviceException, LoadingObjectException {
		logger.debug("Guardando como PKCS12...");
		//-- Si es un PKCS12, guardamos tal cual
		//-- Si es un JKS, guardamos cambiando de StoreType
		if (ksType.equals(STORE_TYPE_PKCS12)){
			save(destination);
		} else {
			saveSwitchingStoreType(destination);
		}
		logger.debug("Guardando como PKCS12...OK");
	}
	
	/**
	 * Guarda en formato JKS el keystore que se está tratando. 
	 * 
	 * @param destination Fichero destino
	 * @throws SaveDeviceException No es posible escribir en el fichero o no es posible
	 * 	guardar alguno de los elementos del dispositivo
	 * @throws SaveDeviceException Error guardando el dispositivo en fichero.
	 * @throws LoadingObjectException No se pueden cargar ciertos elementos del almacén de
	 * 	claves origen para pasarlos al destino
	 */
	public void saveToJks(File destination) throws SaveDeviceException, LoadingObjectException {
		logger.debug("Guardando como JKS...");
		//-- Si es un JKS, guardamos tal cual
		//-- Si es un PKCS12, guardamos cambiando de StoreType
		if (ksType.equals(STORE_TYPE_PKCS12)){
			saveSwitchingStoreType(destination);
		} else {
			save(destination);
		}
		logger.debug("Guardando como JKS...OK");
	}

	/**
	 * Cambia el formato a PKCS#12. 
	 * 
	 * @return Contenido del fichero PKCS#12
	 * @throws SaveDeviceException No es posible escribir en el fichero o no es posible
	 * 	guardar alguno de los elementos del dispositivo
	 * @throws SaveDeviceException Error guardando el dispositivo en fichero.
	 * @throws LoadingObjectException No se pueden cargar ciertos elementos del almacén de
	 * 	claves origen para pasarlos al destino
	 */
	public byte[] changeToPkcs12() throws SaveDeviceException, LoadingObjectException {
		logger.debug("Cambiando a PKCS#12...");
		//-- Si es un PKCS12, no cambiamos nada
		//-- Si es un JKS, cambiamos
		if (ksType.equals(STORE_TYPE_PKCS12)){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			save(baos);
			return baos.toByteArray();
		} else {
			return changeStoreType();
		}
	}

	/**
	 * Cambia el formato a JKS. 
	 * 
	 * @return Contenido del fichero JKS
	 * @throws SaveDeviceException No es posible escribir en el fichero o no es posible
	 * 	guardar alguno de los elementos del dispositivo
	 * @throws SaveDeviceException Error guardando el dispositivo en fichero.
	 * @throws LoadingObjectException No se pueden cargar ciertos elementos del almacén de
	 * 	claves origen para pasarlos al destino
	 */
	public byte[] changeToJks() throws SaveDeviceException, LoadingObjectException {
		logger.debug("Cambiando a JKS...");
		//-- Si es un JKS, no cambiamos nada
		//-- Si es un PKCS12, cambiamos
		if (ksType.equals(STORE_TYPE_JKS)){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			save(baos);
			return baos.toByteArray();
		} else {
			return changeStoreType();
		}
	}

	/**
	 * Guarda el keystore con el que se está trabajando en un stream de salida.
	 * 
	 * @param out Stream de salida donde se guardará el keystore
	 * @throws SaveDeviceException No es posible escribir en el stream de salida o no es posible
	 * 	guardar alguno de los elementos del dispositivo
	 */
	public void save (OutputStream out) throws SaveDeviceException {
		
		logger.debug ("[KeyStoreManager.save]::Entrada::" + out);
		
		try {
			ks.store(out, pin.toCharArray());
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.save]::Keystore no inicializado", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.save]::No existe el algoritmo que comprueba la integridad del dispositivo", e);
			throw new SaveDeviceException ("No existe el algoritmo que comprueba la integridad del dispositivo", e);
		} catch (CertificateException e) {
			logger.info("[KeyStoreManager.save]::Alguno de los certificados del dispositivo no puede ser guardado", e);
			throw new SaveDeviceException ("Alguno de los certificados del dispositivo no puede ser guardado", e);
		} catch (IOException e) {
			logger.info("[KeyStoreManager.save]::Se ha producido un error en entrada/salida guardando el dispositivo", e);
			throw new SaveDeviceException ("Se ha producido un error en entrada/salida guardando el dispositivo", e);
		}
	}
	
	/**
	 * Guarda el keystore con el que se está trabajando en disco.
	 * 
	 * @param file Fichero donde se guardará el keystore
	 * @throws SaveDeviceException No es posible escribir en el fichero o no es posible
	 * 	guardar alguno de los elementos del dispositivo
	 */
	public void save (File file) throws SaveDeviceException {
		logger.debug ("[KeyStoreManager.save]::Entrada::" + file);
		
		FileOutputStream fos = null;
		try {
			try {
				fos = new FileOutputStream (file);
			} catch (FileNotFoundException e) {
				logger.info("[KeyStoreManager.save]::No se puede abrir el fichero en la ruta '" + file + 
						"'. Compruebe que no es un directorio y que tiene permisos para escribir en él.", e);
				throw new SaveDeviceException("No se puede abrir el fichero en la ruta '" + file + 
						"'. Compruebe que no es un directorio y que tiene permisos para escribir en él.", e);
			}
			
			//-- Guardar
			save (fos);
			
		} finally {
			if (fos != null) { try {
				fos.close();
			} catch (IOException e) {
				logger.info("[KeyStoreManager.save]::No es posible cerrar el stream de escritura al disco", e);
				throw new SaveDeviceException("No es posible cerrar el stream de escritura al disco", e);
			} }
		}
	}
	
	@Override
	public void initialize() throws OpeningDeviceException, IncorrectPINException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ks.store(out, pin.toCharArray());
			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
			contenido = readStream(in);
			open(pin);
		} catch (KeyStoreException e) {
			logger.error("[KeyStoreManager.initialize]:: No es posible reinicializar el KeyStore", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("[KeyStoreManager.initialize]:: No es posible reinicializar el KeyStore", e);
		} catch (CertificateException e) {
			logger.error("[KeyStoreManager.initialize]:: No es posible reinicializar el KeyStore", e);
		} catch (IOException e) {
			logger.error("[KeyStoreManager.initialize]:: No es posible reinicializar el KeyStore", e);
		}
		
	}
	
	//---- METODOS PRIVADOS ----//
	
	/*
	 * Guarda el keystore actual a disco cambiando su formato (de PKCS#12 a JKS o de
	 * JKS a PKCS#12)
	 */
	private void saveSwitchingStoreType(File destination) throws SaveDeviceException, LoadingObjectException {
		logger.debug("Guardando con cambio de StoreType...");
		logger.debug("jksPath: '" + destination + "'...");
		//-- Creamos un keystore en memoria, de distinto tipo al original
		KeyStore jks = null;
		//-- Si era un PKCS, entonces el destino será un JKS y viceversa
		try {
			if (ksType.equals(STORE_TYPE_PKCS12)){
			    jks = KeyStore.getInstance(STORE_TYPE_JKS);
			} else {
				jks = KeyStore.getInstance(STORE_TYPE_PKCS12, "BC");
			}
			jks.load(null, pin.toCharArray());

		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se el tipo del keystore no es entendido por
			//-- el proveedor. Como nos aseguramos que el proveedor es BC nunca se debería dar
			//-- esta excepción
			logger.debug("[KeyStoreManager.saveSwitchingStoreType]::El tipo del nuevo keystore no es tratable por el proveedor", e);
		} catch (NoSuchProviderException e) {
			//-- Nos aseguramos de que el proveedor es BC. Si no es posible cargarlo no habrá
			//-- sido posible iniciar este objeto.
			logger.debug("[KeyStoreManager.saveSwitchingStoreType]::No es posible cargar el proveedor BC", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::No existe el algoritmo que comprueba la integridad del dispositivo", e);
			throw new SaveDeviceException ("No existe el algoritmo que comprueba la integridad del dispositivo", e);
		} catch (CertificateException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::Alguno de los certificados del dispositivo no puede ser guardado", e);
			throw new SaveDeviceException ("Alguno de los certificados del dispositivo no puede ser guardado", e);
		} catch (IOException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::Se ha producido un error en entrada/salida guardando el dispositivo", e);
			throw new SaveDeviceException ("Se ha producido un error en entrada/salida guardando el dispositivo", e);
		}

		FileOutputStream fos = null;
		try {
			//-- Creamos la ruta hasta el destino, si no existe
			File parentDir = destination.getParentFile();
			if (!parentDir.exists()){
				parentDir.mkdirs();
			}
	
		    //-- Importamos los items del ks original
		    logger.debug("Exportando elementos...");
		    Enumeration items = ks.aliases();
		    while (items.hasMoreElements()) {
				String alias = (String) items.nextElement();
				if (ks.isKeyEntry(alias)) {
					logger.debug("Añadiendo certificado y clave con alias '" + alias + "'");
					Key key = ks.getKey(alias, pin.toCharArray());
					Certificate[] chain = ks.getCertificateChain(alias);
					jks.setKeyEntry(alias, key, pin.toCharArray(), chain);
				} else if (ks.isCertificateEntry(alias)) {
					logger.debug("Añadiendo certificado con alias '" + alias + "'");
					Certificate cert = ks.getCertificate(alias);
					jks.setCertificateEntry(alias, cert);
				}
			}
		    logger.debug("Exportando elementos...OK");		    

			//-- Guardamos en disco
		    fos = new FileOutputStream(destination);
			jks.store(fos, pin.toCharArray());
			 logger.debug("Guardando con cambio de StoreType...OK");
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se el tipo del keystore no es entendido por
			//-- el proveedor. Como nos aseguramos que el proveedor es BC nunca se debería dar
			//-- esta excepción
			logger.debug("[KeyStoreManager.saveSwitchingStoreType]::El tipo del nuevo keystore no es tratable por el proveedor", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::No existe el algoritmo que comprueba la integridad del dispositivo", e);
			throw new SaveDeviceException ("No existe el algoritmo que comprueba la integridad del dispositivo", e);
		} catch (CertificateException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::Alguno de los certificados del dispositivo no puede ser guardado", e);
			throw new SaveDeviceException ("Alguno de los certificados del dispositivo no puede ser guardado", e);
		} catch (IOException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::Se ha producido un error en entrada/salida guardando el dispositivo", e);
			throw new SaveDeviceException ("Se ha producido un error en entrada/salida guardando el dispositivo", e);
		} catch (UnrecoverableKeyException e) {
			throw new LoadingObjectException("Error recuperando clave en memoria al guardar con cambio de StoreType", e);
		} finally {
			closeStreamSilently(fos, "Error cerrando FileOutputStream sobre ``" + destination + "´´");
		}
	}
	/*
	 * Guarda el keystore actual a disco cambiando su formato (de PKCS#12 a JKS o de
	 * JKS a PKCS#12)
	 */
	private byte[] changeStoreType() throws SaveDeviceException, LoadingObjectException {
		logger.debug("Cambiando el tipo...");

		//-- Creamos un keystore en memoria, de distinto tipo al original
		KeyStore keystoreNuevo = null;
		//-- Si era un PKCS, entonces el destino será un JKS y viceversa
		try {
			if (ksType.equals(STORE_TYPE_PKCS12)){
				keystoreNuevo = KeyStore.getInstance(STORE_TYPE_JKS);
			} else {
				keystoreNuevo = KeyStore.getInstance(STORE_TYPE_PKCS12, "BC");
			}
			keystoreNuevo.load(null, pin.toCharArray());

		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se el tipo del keystore no es entendido por
			//-- el proveedor. Como nos aseguramos que el proveedor es BC nunca se debería dar
			//-- esta excepción
			logger.debug("[KeyStoreManager.saveSwitchingStoreType]::El tipo del nuevo keystore no es tratable por el proveedor", e);
		} catch (NoSuchProviderException e) {
			//-- Nos aseguramos de que el proveedor es BC. Si no es posible cargarlo no habrá
			//-- sido posible iniciar este objeto.
			logger.debug("[KeyStoreManager.saveSwitchingStoreType]::No es posible cargar el proveedor BC", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::No existe el algoritmo que comprueba la integridad del dispositivo", e);
			throw new SaveDeviceException ("No existe el algoritmo que comprueba la integridad del dispositivo", e);
		} catch (CertificateException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::Alguno de los certificados del dispositivo no puede ser guardado", e);
			throw new SaveDeviceException ("Alguno de los certificados del dispositivo no puede ser guardado", e);
		} catch (IOException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::Se ha producido un error en entrada/salida guardando el dispositivo", e);
			throw new SaveDeviceException ("Se ha producido un error en entrada/salida guardando el dispositivo", e);
		}

		ByteArrayOutputStream os = null;
		try {
		    //-- Importamos los items del ks original
		    logger.debug("Exportando elementos...");
		    Enumeration items = ks.aliases();
		    while (items.hasMoreElements()) {
				String alias = (String) items.nextElement();
				if (ks.isKeyEntry(alias)) {
					logger.debug("Añadiendo certificado y clave con alias '" + alias + "'");
					Key key = ks.getKey(alias, pin.toCharArray());
					Certificate[] chain = ks.getCertificateChain(alias);
					keystoreNuevo.setKeyEntry(alias, key, pin.toCharArray(), chain);
				} else if (ks.isCertificateEntry(alias)) {
					logger.debug("Añadiendo certificado con alias '" + alias + "'");
					Certificate cert = ks.getCertificate(alias);
					keystoreNuevo.setCertificateEntry(alias, cert);
				}
			}
		    logger.debug("Exportando elementos...OK");		    

			//-- Guardamos en disco
		    os = new ByteArrayOutputStream();
		    keystoreNuevo.store(os, pin.toCharArray());
			logger.debug("Cambiado de tipo...OK");
			
			return os.toByteArray();
			
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se el tipo del keystore no es entendido por
			//-- el proveedor. Como nos aseguramos que el proveedor es BC nunca se debería dar
			//-- esta excepción
			logger.debug("[KeyStoreManager.saveSwitchingStoreType]::El tipo del nuevo keystore no es tratable por el proveedor", e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::No existe el algoritmo que comprueba la integridad del dispositivo", e);
			throw new SaveDeviceException ("No existe el algoritmo que comprueba la integridad del dispositivo", e);
		} catch (CertificateException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::Alguno de los certificados del dispositivo no puede ser guardado", e);
			throw new SaveDeviceException ("Alguno de los certificados del dispositivo no puede ser guardado", e);
		} catch (IOException e) {
			logger.info("[KeyStoreManager.saveSwitchingStoreType]::Se ha producido un error en entrada/salida guardando el dispositivo", e);
			throw new SaveDeviceException ("Se ha producido un error en entrada/salida guardando el dispositivo", e);
		} catch (UnrecoverableKeyException e) {
			throw new LoadingObjectException("Error recuperando clave en memoria al guardar con cambio de StoreType", e);
		} finally {
			closeStreamSilently(os, "Error cerrando outputStream");
		}
	}


	/*
	 * Cierra el stream. Si salta una excepción la recoge en los logs.
	 */
	private void closeStreamSilently(OutputStream os, String mensaje){
		if (os != null){
			try {
				os.close();
			} catch (Exception e) {/* Glurp*/
				if (mensaje != null){
					logger.info(mensaje);
				}
			}
		}
	}
	
	/*
	 * Obtiene un array de bytes leyendo el stream de lectura
	 */
	private byte[] readStream(InputStream is) throws IOException {
		
    	byte[] buffer = new byte [1024];
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        while ((len = is.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
		return baos.toByteArray();
	}

	/*
	 * Abre un dispositivo criptográfico y lo deja preparado para ser manipulado
	 * 
	 * @param pin PIN del dispositivo
	 */
	private void open(String pin) throws OpeningDeviceException, IncorrectPINException {
		logger.debug("Abriendo KeyStore...");
	    //-- Inicializmos la sesion del usuario
	    try {
		    ks = KeyStore.getInstance(STORE_TYPE_PKCS12, CRYPTOGRAPHIC_PROVIDER);
		    ks.load(new ByteArrayInputStream(contenido), pin.toCharArray());
			logger.debug("Keystore abierto. Tipo: PKCS#12");
			this.pin = pin;
			ksType = STORE_TYPE_PKCS12;
		} catch (Exception e) {
			logger.debug("No ha podido abrirse como PKCS#12. Reintentando como JKS...", e);
		    try {
				ks = KeyStore.getInstance(STORE_TYPE_JKS);
			} catch (KeyStoreException e1) {
				logger.info("[KeyStoreManager.open]::No se puede obtener una instancia de keystore PKCS#12", e);
				logger.info("[KeyStoreManager.open]::No se puede obtener una instancia de keystore JKS", e1);
				throw new OpeningDeviceException ("No se puede obtener una instancia de keystore PKCS#12 ni JKS ", e1);
			} 
		    //-- Cargamos el inputstream nuevamente para forzar un reset
		    try {
				ks.load(new ByteArrayInputStream(contenido), pin.toCharArray());
			} catch (NoSuchAlgorithmException e1) {
				logger.info("[KeyStoreManager.open]::No existe el algoritmo que comprueba la integridad del dispositivo", e1);
				throw new OpeningDeviceException ("No existe el algoritmo que comprueba la integridad del dispositivo", e1);
			} catch (CertificateException e1) {
				logger.info("[KeyStoreManager.open]::Alguno de los certificados del dispositivo no puede ser cargado", e1);
				throw new OpeningDeviceException ("Alguno de los certificados del dispositivo no puede ser cargado", e1);
			} catch (IOException e1) {
				logger.info("[KeyStoreManager.open]::El PIN introducido no es correcto", e1);
				throw new IncorrectPINException ("El PIN introducido no es correcto", e1);
			}
			logger.debug("Keystore abierto. Tipo: JKS");
			this.pin = pin;
			ksType = STORE_TYPE_JKS;
		}
		
		contenido = null; // Para que el Garbage Collector elimine el objeto
		logger.debug("Abriendo KeyStore...OK");
	}

    /*
     * Genera un par de claves
     *
     * @param keysize tamaño de la clave a generar, 1024 es el valor más habitual para
     * claves RSA
     *
     * @return KeyPair Par de claves generado
     */
    protected static KeyPair genKeys(int keysize)
        throws NoSuchAlgorithmException, NoSuchProviderException {

        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA", ArangiObject.CRYPTOGRAPHIC_PROVIDER);
        keygen.initialize(keysize);

        KeyPair rsaKeys = keygen.generateKeyPair();

        logger.debug("Generated " + rsaKeys.getPublic().getAlgorithm() + " keys with length " +
            ((RSAPrivateKey) rsaKeys.getPrivate()).getPrivateExponent().bitLength());

        return rsaKeys;
    } // genKeys

	
    public static void main(String[] args) {
		
    	try {
			KeyStoreManager manager = new KeyStoreManager(ClassLoader.getSystemResourceAsStream("device/pkcs12/uactivo951v_firma.p12"), "1234");
			byte[] contenido = manager.contenido;
			System.out.println("Contenido: " + contenido.length);
		} catch (DeviceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadingStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OpeningDeviceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectPINException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
    
}

