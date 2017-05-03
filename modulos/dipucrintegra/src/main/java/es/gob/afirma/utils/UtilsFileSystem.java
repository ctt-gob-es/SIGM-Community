// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/*
 * Este fichero forma parte de la plataforma de @firma.
 * La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2009-,2011 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.utils.DSSParseTransformer.UtilsFileSystem.java.</p>
 * <b>Description:</b><p>Utility class for reading and processing files.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>23/02/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 23/02/2011.
 */
package es.gob.afirma.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;

/**
 * <p>Utility class for reading and processing files.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 18/03/2011.
 */
public final class UtilsFileSystem {

    /**
     * Constructor method for the class UtilsFileSystem.java.
     */
    private UtilsFileSystem() {
    }

    /**
     *  Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(UtilsFileSystem.class);

    /**
     * Retrieves content of a given file.
     * 
     * @param filePathToRead file path.
     * @return file content.
     */
    private static synchronized byte[ ] readFileFromFileSystem(String filePathToRead) {
	FileInputStream fileReader = null;
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	byte[ ] result = null;
	if (filePathToRead != null) {
	    try {
		fileReader = new FileInputStream(new File(filePathToRead));

		byte[ ] bs = new byte[NumberConstants.INT_1024];
		int numRead;
		while ((numRead = fileReader.read(bs, 0, bs.length)) >= 0) {
		    baos.write(bs, 0, numRead);
		}
		result = baos.toByteArray();
	    } catch (IOException ioe) {
		LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.UFS_LOG002, new Object[ ] { filePathToRead }), ioe);
		return null;
	    } catch (Exception e) {
		LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.UFS_LOG003, new Object[ ] { filePathToRead }), e);
		return null;
	    } finally {
		try {
		    baos.close();
		} catch (Exception e) {}

		try {
		    fileReader.close();
		} catch (Exception e) {}
	    }
	}
	return result;
    }

    /**
     * Retrieves encoded Base64 file content. If file would be encoded it'll be returned without encoded.
     * @param filePathToRead absolute file path.
     * @return Base64 file content.
     */
    private static synchronized byte[ ] readFileFromFileSystemBase64Encoded(String filePathToRead) {
	if (filePathToRead != null) {
	    try {
		byte[ ] content = readFileFromFileSystem(filePathToRead);

		if (!Base64Coder.isBase64Encoded(content)) {
		    return Base64Coder.encodeBase64(content);
		} else {
		    return content;
		}
	    } catch (Exception e) {
		LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.UFS_LOG004, new Object[ ] { filePathToRead }), e);
	    }
	}
	return null;
    }

    /**
     * Gets a file content by a given path.
     * @param relativePath relative path used for gets file.
     * @return gets a file content.
     */
    private static synchronized String getFilePath(String relativePath) {
	URL url = ClassLoader.getSystemResource(relativePath);
	if (url == null) {
	    LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.UFS_LOG005, new Object[ ] { relativePath }));
	    return null;
	}
	return url.getFile();
    }

    /**
     * Reads a file from system by given path (absolute or relative to classpath) and encodes to Base64 format.
     * @param path file path.
     * @param isRelativePath true if path is relative to classpath and false if path is absolute.
     * @return content file encoded in base64. Returns a null value if an error happens
     */
    public static synchronized String readFileBase64Encoded(String path, boolean isRelativePath) {
	String fileContent = null;
	if (GenericUtils.assertStringValue(path)) {
	    byte[ ] content = UtilsFileSystem.readFileFromFileSystemBase64Encoded(isRelativePath ? getFilePath(path) : path);
	    if (content == null) {
		return null;
	    }
	    fileContent = new String(content);
	} else {
	    throw new IllegalArgumentException(Language.getResIntegra(ILogConstantKeys.UFS_LOG001));
	}
	return fileContent;
    }

    /**
     * Reads a file from system by given path (absolute or relative to classpath).
     * @param path file path.
     * @param isRelativePath true if path is relative to classpath and false if path is absolute.
     * @return content file. Returns a null value if an error happens
     */
    public static synchronized byte[ ] readFile(String path, boolean isRelativePath) {
	if (GenericUtils.assertStringValue(path)) {
	    return UtilsFileSystem.readFileFromFileSystem(isRelativePath ? getFilePath(path) : path);
	} else {
	    throw new IllegalArgumentException(Language.getResIntegra(ILogConstantKeys.UFS_LOG001));
	}
    }

    /**
     * Write data into a file. If file doesn't exist, it is created.
     * @param data information to include into file.
     * @param filename name of file to record.
     * @throws IOException if a error happens accessing to file.
     */
    public static void writeFile(byte[ ] data, String filename) throws IOException {
	if (data != null && GenericUtils.assertStringValue(filename)) {
	    FileOutputStream fos = null;
	    try {
		fos = new FileOutputStream(new File(filename));
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		byte[ ] buffer = new byte[NumberConstants.INT_1024];
		int bytesReaded = 0;
		while ((bytesReaded = bais.read(buffer)) >= 0) {
		    fos.write(buffer, 0, bytesReaded);
		}
	    } finally {
		if (fos != null) {
		    fos.flush();
		    fos.close();
		}
	    }
	} else {
	    throw new IllegalArgumentException(Language.getResIntegra(ILogConstantKeys.UFS_LOG001));
	}
    }
}
