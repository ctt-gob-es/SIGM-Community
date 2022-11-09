import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.junit.Test;

import es.gob.afirma.core.misc.Base64;
import es.gob.afirma.core.misc.http.UrlHttpManager;
import es.gob.afirma.core.misc.http.UrlHttpManagerFactory;
import es.gob.afirma.core.misc.http.UrlHttpMethod;
import es.gob.afirma.core.signers.AOPkcs1Signer;
import junit.framework.Assert;

/**
 * Pruebas de inicio de sesi&oacute;n.
 */
public class TestLogin {

	private static final String CERT_PATH = "ANCERTCCP_FIRMA.p12"; //$NON-NLS-1$
	private static final char[] CERT_PASS = "1111".toCharArray(); //$NON-NLS-1$
	private static final String CERT_ALIAS = "juan ejemplo español"; //$NON-NLS-1$

	private static final String CERT_PATH2 = "ANCERTCCP_AUTH.p12"; //$NON-NLS-1$
	private static final char[] CERT_PASS2 = "1111".toCharArray(); //$NON-NLS-1$
	private static final String CERT_ALIAS2 = "juan ejemplo español"; //$NON-NLS-1$

	private static final String URL_BASE = "http://localhost:8080/afirma-signfolder-proxy/pf?"; //$NON-NLS-1$

	/**
	 * Abre sesi&oacute;n y comprueba que est&aacute; establecida realizando una operaci&oacute;n.
	 * @throws Exception Cuando ocurre cualquier error no esperado.
	 */
	@Test
	public void testLoginOk() throws Exception {

		final UrlHttpManager urlManager = UrlHttpManagerFactory.getInstalledManager();

		// --------------------------
		// Llamada al metodo de login
		// --------------------------

		String xml = "<lgnrq />"; //$NON-NLS-1$

		String url = URL_BASE + createUrlParams("10", xml); //$NON-NLS-1$

		byte[] data = urlManager.readUrl(url, UrlHttpMethod.POST);

		String xmlResponse = new String(data);

		System.out.println("Respuesta a la peticion de login:\n" + new String(xmlResponse)); //$NON-NLS-1$

		final String tokenB64 = xmlResponse.substring(xmlResponse.indexOf("'>") + "'>".length(), xmlResponse.indexOf("</lgnrq>")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// --------------------------
		// Firma PKCS#1 del token
		// --------------------------

		// Cargamos el certificado
		final KeyStore ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
		ks.load(ClassLoader.getSystemResourceAsStream(CERT_PATH), CERT_PASS);
		final PrivateKeyEntry pke = (PrivateKeyEntry) ks.getEntry(CERT_ALIAS, new PasswordProtection(CERT_PASS));

		final X509Certificate cert = (X509Certificate) ks.getCertificate(CERT_ALIAS);
		final String certB64 = Base64.encode(cert.getEncoded());

		// Realizamos la firma
		final AOPkcs1Signer signer = new AOPkcs1Signer();
		final byte[] signature = signer.sign(Base64.decode(tokenB64), "SHA256withRSA", pke.getPrivateKey(), pke.getCertificateChain(), null); //$NON-NLS-1$

		// --------------------------
		// Llamada al metodo de validar login
		// --------------------------

		xml = "<rqtvl><cert>" + certB64 + "</cert><pkcs1>" + Base64.encode(signature) + "</pkcs1></rqtvl>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		url = URL_BASE + createUrlParams("11", xml); //$NON-NLS-1$

		data = urlManager.readUrl(url, UrlHttpMethod.POST);

		xmlResponse = new String(data);

		System.out.println("Respuesta a la peticion de validacion de login:\n" + xmlResponse); //$NON-NLS-1$

		Assert.assertTrue("La validacion del login devolvio un error", xmlResponse.indexOf("ok='true'") != -1); //$NON-NLS-1$ //$NON-NLS-2$

		// --------------------------
		// Llamada a un metodo de operacion para comprobar que funciona
		// --------------------------

		xml = "<rqtconf><cert>" + Base64.encode(cert.getEncoded()) + "</cert></rqtconf>"; //$NON-NLS-1$ //$NON-NLS-2$

		url = URL_BASE + createUrlParams("6", xml); //$NON-NLS-1$

		data = urlManager.readUrl(url, UrlHttpMethod.POST);

		xmlResponse = new String(data);

		System.out.println("Respuesta a la peticion de datos de configuracion:\n" + xmlResponse); //$NON-NLS-1$

		Assert.assertNotSame(
				"Se obtuvo el mensaje de error en la autenticacion", //$NON-NLS-1$
				"<err cd=\"ERR-11\">Error en la autenticacion de la peticion</err>", //$NON-NLS-1$
				xmlResponse);
	}

	/**
	 * Intenta abrir una sesi&oacute;n firmando un token distinto al que proporcione el servicio.
	 * @throws Exception Cuando ocurre cualquier error no esperado.
	 */
	@Test
	public void testWrongToken() throws Exception {

		final UrlHttpManager urlManager = UrlHttpManagerFactory.getInstalledManager();

		// --------------------------
		// Llamada al metodo de login
		// --------------------------

		String xml = "<lgnrq />"; //$NON-NLS-1$

		String url = URL_BASE + createUrlParams("10", xml); //$NON-NLS-1$

		byte[] data = urlManager.readUrl(url, UrlHttpMethod.POST);

		String xmlResponse = new String(data);

		System.out.println("Respuesta a la peticion de login:\n" + new String(xmlResponse)); //$NON-NLS-1$

		// --------------------------
		// Firma PKCS#1 del token
		// --------------------------

		// Cargamos el certificado
		final KeyStore ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
		ks.load(ClassLoader.getSystemResourceAsStream(CERT_PATH), CERT_PASS);
		final PrivateKeyEntry pke = (PrivateKeyEntry) ks.getEntry(CERT_ALIAS, new PasswordProtection(CERT_PASS));

		// Firmamos un token distinto
		final X509Certificate cert = (X509Certificate) ks.getCertificate(CERT_ALIAS);
		final String certB64 = Base64.encode(cert.getEncoded());

		final AOPkcs1Signer signer = new AOPkcs1Signer();
		final byte[] pkcs1 = signer.sign("Hola Mundo!!".getBytes(), "SHA256withRSA", pke.getPrivateKey(), pke.getCertificateChain(), null); //$NON-NLS-1$ //$NON-NLS-2$

		// --------------------------
		// Llamada al metodo de validar login
		// --------------------------

		xml = "<rqtvl><cert>" + certB64 + "</cert><pkcs1>" + Base64.encode(pkcs1) + "</pkcs1></rqtvl>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		url = URL_BASE + createUrlParams("11", xml); //$NON-NLS-1$

		data = urlManager.readUrl(url, UrlHttpMethod.POST);

		xmlResponse = new String(data);

		System.out.println("Respuesta a la peticion de validacion de login:\n" + xmlResponse); //$NON-NLS-1$

		Assert.assertTrue("La validacion del login devolvio un error", xmlResponse.indexOf("ok='false'") != -1); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Intenta abrir una sesi&oacute;n proporcionando un certificado distinto al utilizado para
	 * firmar el token.
	 * @throws Exception Cuando ocurre cualquier error no esperado.
	 */
	@Test
	public void testWrongCertificate() throws Exception {

		final UrlHttpManager urlManager = UrlHttpManagerFactory.getInstalledManager();

		// --------------------------
		// Llamada al metodo de login
		// --------------------------

		String xml = "<lgnrq />"; //$NON-NLS-1$

		String url = URL_BASE + createUrlParams("10", xml); //$NON-NLS-1$

		byte[] data = urlManager.readUrl(url, UrlHttpMethod.POST);

		String xmlResponse = new String(data);

		System.out.println("Respuesta a la peticion de login:\n" + new String(xmlResponse)); //$NON-NLS-1$

		final String tokenB64 = xmlResponse.substring(xmlResponse.indexOf("'>") + "'>".length(), xmlResponse.indexOf("</lgnrq>")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// --------------------------
		// Firma PKCS#1 del token
		// --------------------------

		// Cargamos el certificado
		final KeyStore ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
		ks.load(ClassLoader.getSystemResourceAsStream(CERT_PATH), CERT_PASS);
		final PrivateKeyEntry pke = (PrivateKeyEntry) ks.getEntry(CERT_ALIAS, new PasswordProtection(CERT_PASS));

		// Firmamos el token
		final AOPkcs1Signer signer = new AOPkcs1Signer();
		final byte[] pkcs1 = signer.sign(Base64.decode(tokenB64), "SHA256withRSA", pke.getPrivateKey(), pke.getCertificateChain(), null); //$NON-NLS-1$

		// Cargamos otro almacen para declarar un certificado distinto al usado
		final KeyStore ks2 = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
		ks2.load(ClassLoader.getSystemResourceAsStream(CERT_PATH2), CERT_PASS2);
		final Certificate wrongCert = ks2.getCertificate(CERT_ALIAS2);

		final String wrongCertB64 = Base64.encode(wrongCert.getEncoded());

		// --------------------------
		// Llamada al metodo de validar login
		// --------------------------

		xml = "<rqtvl><cert>" + wrongCertB64 + "</cert><pkcs1>" + Base64.encode(pkcs1) + "</pkcs1></rqtvl>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		url = URL_BASE + createUrlParams("11", xml); //$NON-NLS-1$

		data = urlManager.readUrl(url, UrlHttpMethod.POST);

		xmlResponse = new String(data);

		System.out.println("Respuesta a la peticion de validacion de login:\n" + xmlResponse); //$NON-NLS-1$

		Assert.assertTrue("La validacion del login devolvio un error", xmlResponse.indexOf("ok='false'") != -1); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Intenta llamar a una operaci&oacute;n sin realizar el proceso de login.
	 * @throws Exception Cuando ocurre cualquier error no esperado.
	 */
	@Test
	public void testNoLogin() throws Exception {

		final UrlHttpManager urlManager = UrlHttpManagerFactory.getInstalledManager();

		// Cargamos el certificado
		final KeyStore ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
		ks.load(ClassLoader.getSystemResourceAsStream(CERT_PATH), CERT_PASS);
		final X509Certificate cert = (X509Certificate) ks.getCertificate(CERT_ALIAS);

		// --------------------------
		// Llamada a un metodo de operacion
		// --------------------------

		final String xml = "<rqtconf><cert>" + Base64.encode(cert.getEncoded()) + "</cert></rqtconf>"; //$NON-NLS-1$ //$NON-NLS-2$

		final String url = URL_BASE + createUrlParams("6", xml); //$NON-NLS-1$

		final byte[] data = urlManager.readUrl(url, UrlHttpMethod.POST);

		final String xmlResponse = new String(data);

		System.out.println("Respuesta a la peticion de datos de configuracion:\n" + xmlResponse); //$NON-NLS-1$

		Assert.assertEquals(
				"No se obtuvo el mensaje de error en la autenticacion", //$NON-NLS-1$
				"<err cd=\"ERR-11\">Error en la autenticacion de la peticion</err>", //$NON-NLS-1$
				xmlResponse);
	}

	/**
	 * Abre sesi&oacute;n, comprueba que est&aacute; establecida realizando una operaci&oacute;n, la cierra y
	 * comprueba que ya no se pueden realizar nuevas operaciones.
	 * @throws Exception Cuando ocurre cualquier error no esperado.
	 */
	@Test
	public void testCloseSession() throws Exception {

		final UrlHttpManager urlManager = UrlHttpManagerFactory.getInstalledManager();

		// --------------------------
		// Llamada al metodo de login
		// --------------------------

		String xml = "<lgnrq />"; //$NON-NLS-1$

		String url = URL_BASE + createUrlParams("10", xml); //$NON-NLS-1$

		byte[] data = urlManager.readUrl(url, UrlHttpMethod.POST);

		String xmlResponse = new String(data);

		System.out.println("Respuesta a la peticion de login:\n" + new String(xmlResponse)); //$NON-NLS-1$

		final String tokenB64 = xmlResponse.substring(xmlResponse.indexOf("'>") + "'>".length(), xmlResponse.indexOf("</lgnrq>")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// --------------------------
		// Firma PKCS#1 del token
		// --------------------------

		// Cargamos el certificado
		final KeyStore ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
		ks.load(ClassLoader.getSystemResourceAsStream(CERT_PATH), CERT_PASS);
		final PrivateKeyEntry pke = (PrivateKeyEntry) ks.getEntry(CERT_ALIAS, new PasswordProtection(CERT_PASS));

		final X509Certificate cert = (X509Certificate) ks.getCertificate(CERT_ALIAS);
		final String certB64 = Base64.encode(cert.getEncoded());

		// Firmamos el token
		final AOPkcs1Signer signer = new AOPkcs1Signer();
		final byte[] signature = signer.sign(Base64.decode(tokenB64), "SHA256withRSA", pke.getPrivateKey(), pke.getCertificateChain(), null); //$NON-NLS-1$


		// --------------------------
		// Llamada al metodo de validar login
		// --------------------------

		xml = "<rqtvl><cert>" + certB64 + "</cert><pkcs1>" + Base64.encode(signature) + "</pkcs1></rqtvl>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		url = URL_BASE + createUrlParams("11", xml); //$NON-NLS-1$

		data = urlManager.readUrl(url, UrlHttpMethod.POST);

		xmlResponse = new String(data);

		System.out.println("Respuesta a la peticion de validacion de login:\n" + xmlResponse); //$NON-NLS-1$

		Assert.assertTrue("La validacion del login devolvio un error", xmlResponse.indexOf("ok='true'") != -1); //$NON-NLS-1$ //$NON-NLS-2$

		// --------------------------
		// Llamada a un metodo de operacion
		// --------------------------

		xml = "<rqtconf><cert>" + Base64.encode(cert.getEncoded()) + "</cert></rqtconf>"; //$NON-NLS-1$ //$NON-NLS-2$

		url = URL_BASE + createUrlParams("6", xml); //$NON-NLS-1$

		data = urlManager.readUrl(url, UrlHttpMethod.POST);

		xmlResponse = new String(data);

		Assert.assertNotSame(
				"Se obtuvo el mensaje de error en la autenticacion", //$NON-NLS-1$
				"<err cd=\"ERR-11\">Error en la autenticacion de la peticion</err>", //$NON-NLS-1$
				xmlResponse);

		// --------------------------
		// Llamada al metodo de cierre de sesion
		// --------------------------

		 xml = "<lgorq/>"; //$NON-NLS-1$

		url = URL_BASE + createUrlParams("12", xml); //$NON-NLS-1$

		data = urlManager.readUrl(url, UrlHttpMethod.POST);

		xmlResponse = new String(data);

		Assert.assertEquals(
				"No se obtuvo la respuesta de cierre de sesion", //$NON-NLS-1$
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><lgorq/>", //$NON-NLS-1$
				xmlResponse);

		// --------------------------
		// Llamada a un metodo de operacion
		// --------------------------

		xml = "<rqtconf><cert>" + Base64.encode(cert.getEncoded()) + "</cert></rqtconf>"; //$NON-NLS-1$ //$NON-NLS-2$

		url = URL_BASE + createUrlParams("6", xml); //$NON-NLS-1$

		data = urlManager.readUrl(url, UrlHttpMethod.POST);

		xmlResponse = new String(data);

		Assert.assertEquals(
				"El servicio no notifico un error de autenticacion", //$NON-NLS-1$
				"<err cd=\"ERR-11\">Error en la autenticacion de la peticion</err>", //$NON-NLS-1$
				xmlResponse);
	}

	/**
	 * Crea los parametros de una URL para la llamada al servicio del proxy.
	 * @param op Identificador de operaci&oacute;n.
	 * @param data XML con los datos para configurar la operaci&oacute;n.
	 * @return Cadena con los parametros de la URL.
	 */
	private static String createUrlParams(final String op, final String data) {
		return "op=" + op + "&dat=" + Base64.encode(data.getBytes(), true); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
