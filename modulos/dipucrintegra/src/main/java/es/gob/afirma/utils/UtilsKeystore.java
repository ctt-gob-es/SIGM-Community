/*
 * Este fichero forma parte de la plataforma TS@.
 * La plataforma TS@ es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2013-,2014 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.utils.UtilsKeystore.java.</p>
 * <b>Description:</b><p>Class that manages operations related with the management of keystores.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>14/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 14/01/2014.
 */
package es.gob.afirma.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 * <p>Class that manages operations related with the management of keystores.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 14/01/2014.
 */
public final class UtilsKeystore {

    /**
     * Attribute that represents the PKCS#12 keystore type.
     */
    public static final String PKCS12 = "PKCS12";

    /**
     * Attribute that represents the JCEKS keystore type.
     */
    public static final String JCEKS = "JCEKS";

    /**
     * Attribute that represents the Java Key Store keystore type.
     */
    public static final String JKS = "JKS";

    /**
     * Constructor method for the class UtilsKeystore.java.
     */
    private UtilsKeystore() {
    }

    /**
     * Method that loads a keystore.
     * @param path Parameter that represents the path where is located the keystore.
     * @param password Parameter that represents the password of the keystore.
     * @param type Parameter that represents the keystore type.
     * @return an object that represents the loaded keystore.
     * @throws KeyStoreException If the method fails.
     * @throws NoSuchAlgorithmException If the method fails.
     * @throws CertificateException If the method fails.
     * @throws IOException If the method fails.
     */
    public static KeyStore loadKeystore(String path, String password, String type) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
	InputStream bais = null;
	KeyStore ks = null;
	try {
	    ks = KeyStore.getInstance(type);
	    bais = new FileInputStream(path);
	    ks.load(bais, password.toCharArray());
	} finally {
	    UtilsResources.safeCloseInputStream(bais);
	}
	return ks;
    }

    /**
     * Method that obtains a certificate stored inside of a keystore.
     * @param keystore Parameter that represents the keystore.
     * @param keystoreDecodedPass Parameter that represents the keystore password.
     * @param alias Parameter that represents the alias of the certificate to obtain.
     * @param keystoreType Parameter that represents the keystore type.
     * @return the found certificate.
     * @throws KeyStoreException If the method fails.
     * @throws NoSuchAlgorithmException If the method fails.
     * @throws CertificateException If the method fails.
     * @throws IOException If the method fails.
     */
    public static byte[ ] getCertificateEntry(byte[ ] keystore, String keystoreDecodedPass, String alias, String keystoreType) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
	ByteArrayInputStream bais = null;
	byte[ ] certBytes = null;
	try {
	    KeyStore ks = KeyStore.getInstance(keystoreType);
	    bais = new ByteArrayInputStream(keystore);
	    ks.load(bais, keystoreDecodedPass.toCharArray());
	    Certificate cert = ks.getCertificate(alias);
	    certBytes = cert.getEncoded();
	} finally {
	    UtilsResources.safeCloseInputStream(bais);
	}
	return certBytes;
    }

    /**
     * Method that obtains a private key stored inside of a keystore as a bytes array.
     * @param keystore Parameter that represents the keystore.
     * @param keystoreDecodedPass Parameter that represents the keystore password.
     * @param alias Parameter that represents the alias of the certificate to obtain.
     * @param keystoreType Parameter that represents the keystore type.
     * @param privateKeyDecodedPass Parameter that represents the private key password.
     * @return the found private key.
     * @throws KeyStoreException If the method fails.
     * @throws NoSuchAlgorithmException If the method fails.
     * @throws CertificateException If the method fails.
     * @throws IOException If the method fails.
     * @throws UnrecoverableKeyException If the method fails.
     */
    public static byte[ ] getPrivateKeyEntryBytes(byte[ ] keystore, String keystoreDecodedPass, String alias, String keystoreType, String privateKeyDecodedPass) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
	return getPrivateKeyEntry(keystore, keystoreDecodedPass, alias, keystoreType, privateKeyDecodedPass).getEncoded();
    }

    /**
     * Method that obtains a private key stored inside of a keystore.
     * @param keystore Parameter that represents the keystore.
     * @param keystoreDecodedPass Parameter that represents the keystore password.
     * @param alias Parameter that represents the alias of the certificate to obtain.
     * @param keystoreType Parameter that represents the keystore type.
     * @param privateKeyDecodedPass Parameter that represents the private key password.
     * @return the found private key.
     * @throws KeyStoreException If the method fails.
     * @throws NoSuchAlgorithmException If the method fails.
     * @throws CertificateException If the method fails.
     * @throws IOException If the method fails.
     * @throws UnrecoverableKeyException If the method fails.
     */
    public static PrivateKey getPrivateKeyEntry(byte[ ] keystore, String keystoreDecodedPass, String alias, String keystoreType, String privateKeyDecodedPass) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
	ByteArrayInputStream bais = null;
	PrivateKey pk = null;
	try {
	    KeyStore ks = KeyStore.getInstance(keystoreType);
	    bais = new ByteArrayInputStream(keystore);
	    ks.load(bais, keystoreDecodedPass.toCharArray());
	    pk = (PrivateKey) ks.getKey(alias, privateKeyDecodedPass.toCharArray());
	} finally {
	    UtilsResources.safeCloseInputStream(bais);
	}
	return pk;
    }
}
