import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.Signature;

import es.gob.afirma.core.signers.AOPkcs1Signer;

public class TestVerifyPkcs1 {


	private static final String CERT_PATH = "ANCERTCCP_FIRMA.p12"; //$NON-NLS-1$
	private static final char[] CERT_PASS = "1111".toCharArray(); //$NON-NLS-1$
	private static final String CERT_ALIAS = "juan ejemplo español"; //$NON-NLS-1$


	public static void main(String[] args) throws Exception {

		final String SIGNATURE_ALGORITHM = "SHA256withRSA";
		final byte[] DATA = "Hola Mundo!!".getBytes();


		// Cargamos el certificado
		final KeyStore ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
		ks.load(ClassLoader.getSystemResourceAsStream(CERT_PATH), CERT_PASS);
		final PrivateKeyEntry pke = (PrivateKeyEntry) ks.getEntry(CERT_ALIAS, new PasswordProtection(CERT_PASS));


		final AOPkcs1Signer signer = new AOPkcs1Signer();
		final byte[] signature = signer.sign(DATA, SIGNATURE_ALGORITHM, pke.getPrivateKey(), pke.getCertificateChain(), null);

		final Signature verifier = Signature.getInstance(SIGNATURE_ALGORITHM);
		verifier.initVerify(pke.getCertificate().getPublicKey());
		verifier.update(DATA);
		verifier.verify(signature);

		System.out.println("OK");
	}
}
