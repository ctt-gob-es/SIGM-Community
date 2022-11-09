/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

/*

 Empresa desarrolladora: GuadalTEL S.A.

 Autor: Junta de Andaluc&iacute;a

 Derechos de explotaci&oacute;n propiedad de la Junta de Andaluc&iacute;a.

 Éste programa es software libre: usted tiene derecho a redistribuirlo y/o modificarlo bajo los t&eacute;rminos de la Licencia EUPL European Public License publicada 
 por el organismo IDABC de la Comisi&oacute;n Europea, en su versi&oacute;n 1.0. o posteriores.

 Éste programa se distribuye de buena fe, pero SIN NINGUNA GARANTÍA, incluso sin las presuntas garant&iacute;as impl&iacute;citas de USABILIDAD o ADECUACIÓN ZipUtil PROPÓSITO 
 CONCRETO. Para mas informaci&oacute;n consulte la Licencia EUPL European Public License.

 Usted recibe una copia de la Licencia EUPL European Public License junto con este programa, si por alg&uacute;n motivo no le es posible visualizarla, puede 
 consultarla en la siguiente URL: http://ec.europa.eu/idabc/servlets/Doc?id=31099

 You should have received a copy of the EUPL European Public License along with this program. If not, see http://ec.europa.eu/idabc/servlets/Doc?id=31096

 Vous devez avoir reçu une copie de la EUPL European Public License avec ce programme. Si non, voir http://ec.europa.eu/idabc/servlets/Doc?id=31205

 Sie sollten eine Kopie der EUPL European Public License zusammen mit diesem Programm. Wenn nicht, finden Sie da 
 http://ec.europa.eu/idabc/servlets/Doc?id=29919

 */

package es.seap.minhap.portafirmas.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;


/**
 * Provides methods to compress files into a ZIP file.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
public class ZipUtil {

	private final byte[] buffer = new byte[8192];

	/**
	 * Creates a new Zip file from received {@link File} object with
	 * Deflater.DEFAULT_COMPRESSION compression level.
	 * 
	 * @param file
	 *            {@link File} where ZIP file will be created.
	 * @return {@link ZipOutputStream} with ZIP file data.
	 * @throws IOException
	 *             If any error occurs.
	 */
	public ZipOutputStream createZip(String fileName) throws IOException {
		ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(fileName));
		// Set compression level to default
		zip.setLevel(Deflater.DEFAULT_COMPRESSION);
		return zip;
	}

	/**
	 * Add files to zip.
	 * 
	 * @param fileToAdd
	 *            {@link File} which will be added to ZIP file.
	 * @param fileName
	 *            Name of the file which will be added to ZIP file.
	 * @param zos
	 *            {@link ZipOutputStream} with ZIP file data.
	 * @throws IOException
	 *             If any error occurs.
	 */
	public void addFile(InputStream fileToAdd, String fileName,
			ZipOutputStream zos) throws IOException {
		CRC32 crc = new CRC32();
		crc.reset();
		crc.update(buffer);
		ZipEntry entry = new ZipEntry(fileName);
		entry.setCrc(crc.getValue());
		// Add ZIP entry to output stream
		zos.putNextEntry(entry);

		// Transfer bytes from the current file to the ZIP file
		IOUtils.copy(fileToAdd, zos);
		//Flush and close the current entry
		zos.flush();
		zos.closeEntry();
		fileToAdd.close();
	}

	/**
	 * Ends ZIP file creation and adding process.
	 * 
	 * @param zip
	 *            {@link ZipOutputStream} with ZIP file data.
	 * @throws IOException
	 *             If any error occurs.
	 */
	public void closeZip(ZipOutputStream zip) throws IOException {
		zip.finish();
		zip.close();
	}
}
