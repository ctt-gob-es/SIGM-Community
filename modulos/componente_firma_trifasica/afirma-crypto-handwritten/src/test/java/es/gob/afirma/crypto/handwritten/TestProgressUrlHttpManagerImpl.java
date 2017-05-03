package es.gob.afirma.crypto.handwritten;

import org.junit.Test;

import es.gob.afirma.core.misc.http.UrlHttpMethod;
import es.gob.afirma.crypto.handwritten.net.ProgressUrlHttpManagerImpl;

/** Clase para probar la descarga de documentos PDF.
 * @author Astrid Idoate **/
public class TestProgressUrlHttpManagerImpl {

	/** Prueba simple de descarga de PDF.*/
	@SuppressWarnings("static-method")
	@Test
	public void testSimpleProgressUrlHttpManager () {
		try {
			final ProgressUrlHttpManagerImpl puhmi = new ProgressUrlHttpManagerImpl();
			puhmi.readUrl(
				"http://technology.nasa.gov/NASA_Software_Catalog_2014.pdf", //$NON-NLS-1$
				UrlHttpMethod.GET
			);
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
		for(;;) {
			// Vacio
		}
	}
}
