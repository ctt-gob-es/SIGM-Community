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
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.apache.log4j.Logger;

import es.accv.arangi.base.exception.device.InitializeProviderException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.OpeningDeviceException;

/**
 * Clase utilizada para manejar almacenes de claves PKCS#11 desde el manager
 * de Mozilla. 
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class MozillaPkcs11Manager {

	/*
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(MozillaPkcs11Manager.class);
	
	/*
	 * Nombre del proveedor PKCS#11
	 */
	String providerName;
	
	/*
	 * Proveedor PKCS#11
	 */
	Provider provider;
	
	/*
	 * Almacén de claves PKCS#11
	 */
	KeyStore ks;
	
	/*
	 * PIN
	 */
	char[] pin;
	
	/**
	 * Constructor
	 * 
	 * @param providerName Nombre del proveedor (coincidirá con el nombre del módulo del 
	 * 	dispositivo de seguridad definido en Mozilla)
	 * @param pkcs11Library Librería PKCS#11 (coincidirá con el archivo del módulo del
	 * 	dispositivo de seguridad definido en Mozilla)
	 * @throws InitializeProviderException No se ha podido inicializar el proveedor
	 * @throws OpeningDeviceException No se ha podido abrir el PKCS#11
	 */
	public MozillaPkcs11Manager (String providerName, File pkcs11Library) throws InitializeProviderException, OpeningDeviceException {
		logger.debug("[MozillaPkcs11Manager]::Entrada::" + Arrays.asList(new Object[] { providerName, pkcs11Library }));
		
		//-- Cargar campo
		this.providerName = providerName;
		
		//-- Obtener el proveedor
		logger.debug("[MozillaPkcs11Manager]::Crear el proveedor::" + providerName);
		
		String configFile = "name=" + providerName +
			"\nlibrary=" + pkcs11Library.getAbsolutePath() + 
			"\nshowInfo=true\n"; 
		logger.debug("[MozillaPkcs11Manager]::Configuración::" + configFile);

		//-- Iniciando proveedor
		try {
			provider = new sun.security.pkcs11.SunPKCS11(new ByteArrayInputStream(configFile.getBytes()));
			if (Security.getProvider(provider.getName()) != null) {
				Security.removeProvider(provider.getName());
			}
			Security.addProvider(provider);
		} catch (ProviderException e) {
			logger.debug ("[MozillaPkcs11Manager]::No se ha podido iniciar el provider del módulo " + pkcs11Library.getAbsolutePath(), e);
			throw new InitializeProviderException("No ha sido posible iniciar el proveedor de SUN", e);
		}
		
		//-- Obtener el keystore
	    try {
		    ks = KeyStore.getInstance("PKCS11", provider);
			logger.debug("[MozillaPkcs11Manager]::Obtenido keystore. Abriéndolo ...");
		} catch (KeyStoreException e1) {
			logger.info("[Pkcs11Manager.getKeyStore]::Error de keystore", e1);
			throw new OpeningDeviceException ("Error de keystore", e1);
		} catch (Exception e1) {
			logger.info("[Pkcs11Manager.getKeyStore]::Error desconocido creando y abriendo el keystore PKCS11", e1);
			throw new OpeningDeviceException ("Error desconocido creando el keystore PKCS11", e1);
		}
		
		//-- Abrir el keystore
		boolean exit = false;
		while (!exit) {
		    try {
		    	char[] pin = getPassword();
		    	if (pin == null) {
		    		throw new OpeningDeviceException ("Operación cancelada por el usuario");
		    	}
			    ks.load(null, pin); 
			    exit = true;
			    this.pin = pin;
				logger.debug("[MozillaPkcs11Manager]::Keystore abierto");
		    } catch (NoSuchAlgorithmException e1) {
				logger.info("[Pkcs11Manager.getKeyStore]::No existe el algoritmo que comprueba la integridad del dispositivo", e1);
				throw new OpeningDeviceException ("No existe el algoritmo que comprueba la integridad del dispositivo", e1);
			} catch (CertificateException e1) {
				logger.info("[Pkcs11Manager.getKeyStore]::Alguno de los certificados del dispositivo no puede ser guardado", e1);
				throw new OpeningDeviceException ("Alguno de los certificados del dispositivo no puede ser guardado", e1);
			} catch (IOException e1) {
				logger.info("[Pkcs11Manager.getKeyStore]::El PIN introducido no es correcto", e1);
				if (JOptionPane.showConfirmDialog(null, "El PIN no es correcto. Recuerde que si se equivoca varias veces bloqueará la tarjeta. " +
						"¿Desea volver a intentarlo?", "PIN incorrecto", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) != JOptionPane.YES_OPTION) {
					logger.debug("[MozillaPkcs11Manager.getKeystore]::Operación cancelada por el usuario");
					exit = true;
				}
			} 
		}
		
	}
	
	//-- Métodos públicos
	
	public String getProviderName() {
		return providerName;
	}
	
	/**
	 * Obtiene la lista de alias del PKCS#11. El alias es el nombre del manager seguido de "_" y
	 * el alias que se encuentra en la tarjeta.
	 * 
	 * @return Lista de alias de la tarjeta
	 */
	public List<String> getAliasNamesList() {
		logger.debug("[MozillaPkcs11Manager.getAliasNamesList]::Entrada");
		
		Enumeration<String> enumAlias = null;
		try {
			enumAlias = ks.aliases();
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.getAliasNamesList]::Keystore no inicializado", e);
		}
		List<String> lAlias = new ArrayList<String> ();
		while (enumAlias != null && enumAlias.hasMoreElements()) {
			String alias = (String) enumAlias.nextElement();
			lAlias.add(providerName + "_" + alias);
		}
		
		return lAlias;
	}

	/**
	 * Obtiene el certificado en el alias
	 * 
	 * @param alias Alias (incluye el nombre del proveedor)
	 * @return Certificado o null si no existe el alias
	 */
	public X509Certificate getCertificate(String alias) {
		logger.debug("[MozillaPkcs11Manager.getCertificate]::Entrada");
		
		alias = alias.substring (alias.lastIndexOf("_") + 1);
		Enumeration<String> enumAlias = null;
		try {
			enumAlias = ks.aliases();
			while (enumAlias != null && enumAlias.hasMoreElements()) {
				String ksAlias = enumAlias.nextElement();
				if (ksAlias.equals(alias)) {
					X509Certificate certificate = (X509Certificate) ks.getCertificate(ksAlias);
					logger.debug ("[MozillaKeyStoreManager.getCertificate]::Obtenido certificado::" + certificate);
					return certificate;
				}
			}
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[MozillaPkcs11Manager.getCertificate]::Keystore no inicializado", e);
		}
		
		logger.debug("[MozillaPkcs11Manager.getPrivateKey]::No se ha encontrado certificado de alias '" + alias + "' en '" + providerName + "'");
		return null;
	}

	/**
	 * Obtiene la clave privada en el alias
	 * 
	 * @param alias Alias (incluye el nombre del proveedor)
	 * @return Clave privada o null si no existe el alias
	 */
	public PrivateKey getPrivateKey(String alias) throws LoadingObjectException {
		logger.debug("[MozillaPkcs11Manager.getPrivateKey]::Entrada");
		
		alias = alias.substring (alias.lastIndexOf("_") + 1);
		Enumeration<String> enumAlias = null;
		try {
			enumAlias = ks.aliases();
			while (enumAlias != null && enumAlias.hasMoreElements()) {
				String ksAlias = enumAlias.nextElement();
				if (ksAlias.equals(alias) && ks.isKeyEntry(ksAlias)) {
					PrivateKey pk = (PrivateKey) ks.getKey(ksAlias, this.pin);
					logger.debug ("[MozillaKeyStoreManager.getCertificate]::Obtenida clave privada::" + pk);
					return pk;
				}
			}
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[MozillaPkcs11Manager.getPrivateKey]::Keystore no inicializado", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[MozillaPkcs11Manager.getPrivateKey]::No se halla presente el algoritmo de la clave privada", e);
			throw new LoadingObjectException("No se halla presente el algoritmo de la clave privada", e);
		} catch (UnrecoverableKeyException e) {
			logger.info("[MozillaPkcs11Manager.getPrivateKey]::La clave privada del alias '" + alias + "' no ha podido ser recuperada. " +
					"Compruebe que su password coincide con el PIN del dispositivo.", e);
			throw new LoadingObjectException("La clave privada del alias '" + alias + "' no ha podido ser recuperada. Compruebe " +
					"que su password coincide con el PIN del dispositivo.", e);
		}
		
		logger.debug("[MozillaPkcs11Manager.getPrivateKey]::No se ha encontrado la clave privada de alias '" + alias + "' en '" + providerName + "'");
		return null;
	}

	/**
	 * Cierra el proveedor PKCS#11
	 */
	public void close() {
		logger.debug ("Cerrando Keystore...");

		ks = null;
		
		//-- Eliminar provider de SUN
		logger.debug("[CLOSE]::Eliminando el provider de SUN");
		Security.removeProvider(this.provider.getName());
		logger.debug("[CLOSE]::Eliminado el provider de SUN");
	}

	//-- Metodos privados

	private char[] getPassword() {
		JPasswordField pinField = new JPasswordField(10);
		JLabel pinLabel = new JLabel("Password [" + providerName + "]'");
		pinLabel.setLabelFor(pinField);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(pinLabel);
		panel.add(pinField);
		int response = JOptionPane.showConfirmDialog(null, panel, "PIN", JOptionPane.OK_CANCEL_OPTION);
		if(response != JOptionPane.OK_OPTION) {
			if (JOptionPane.showConfirmDialog(null, "Si no introduce el PIN no se utilizarán los certificados de esta tarjeta. " +
					"¿Desea volver a intentarlo?", "Sin PIN", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) != JOptionPane.YES_OPTION) {
				logger.debug("[MozillaPkcs11Manager.getPassword]::Operación cancelada por el usuario");
				return null;
			}
		}

		return pinField.getPassword();
	}


}
