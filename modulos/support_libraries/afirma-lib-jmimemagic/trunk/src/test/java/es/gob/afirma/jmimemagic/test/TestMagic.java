package es.gob.afirma.jmimemagic.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jmimemagic.Magic;

import org.junit.Assert;
import org.junit.Test;

/** Prueba directa de JMIMEMagic. */
public class TestMagic {

	/** Prueba simple de detacci&oacute;n de un PDF.
	 * @throws Exception */
	@SuppressWarnings("static-method")
	@Test
	public void testMagic() throws Exception {
		final byte[] data = getDataFromInputStream(ClassLoader.getSystemResourceAsStream("TEST_PDF.pdf")); //$NON-NLS-1$
		Assert.assertEquals("application/pdf", Magic.getMagicMatch(data).getMimeType()); //$NON-NLS-1$
	}

    private static byte[] getDataFromInputStream(final InputStream input) throws IOException {
        if (input == null) {
            return new byte[0];
        }
        int nBytes = 0;
        final byte[] buffer = new byte[1024];
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((nBytes = input.read(buffer)) != -1) {
            baos.write(buffer, 0, nBytes);
        }
        return baos.toByteArray();
    }
}
