package es.gob.afirma.signfolder.server.proxy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementaci&oacute;n de un compresor de datos.
 * @author Carlos Gamuci
 */
public class GzipCompressorImpl {

	private static final int BUFFER_SIZE = 1024;

	private static final Logger LOGGER = LoggerFactory.getLogger(GzipCompressorImpl.class);

	/**
	 * Comprime datos en un GZIP.
	 * @param data Datos.
	 * @return Datos comprimidos o los originales si se produjo alg&uacute;n error durante el proceso.
	 */
	public static byte[] gzip(final byte[] data) {

		final byte[] compressedData;
		try (
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final GZIPOutputStream gzipOs = new GZIPOutputStream(baos);
				) {
			gzipOs.write(data);
			gzipOs.finish();

			compressedData = baos.toByteArray();
		} catch (final IOException e) {
			// Este error no deberia ocurrir nunca
			LOGGER.warn("Error al comprimir los datos. Se devuelven los datos originales: " + e); //$NON-NLS-1$
			return data;
		}

		return compressedData;
	}

	/**
	 * Descomprime un GZIP.
	 * @param compressedData Datos comprimidos.
	 * @return Datos descomprimidos.
	 * @throws IOException Cuando ocurre alg&uacute;n error en la descompresi&oacute;n.
	 */
	public static byte[] gunzip(final byte[] compressedData) throws IOException {

		int n = 0;
		byte[] data;
		final byte[] buffer = new byte[BUFFER_SIZE];
		try (	ByteArrayOutputStream baos = new ByteArrayOutputStream();
				GZIPInputStream gzipIs = new GZIPInputStream(new ByteArrayInputStream(compressedData));
				) {
			while ((n = gzipIs.read(buffer)) > 0) {
				baos.write(buffer, 0, n);
			}
			data = baos.toByteArray();
		}
		return data;
	}
}
