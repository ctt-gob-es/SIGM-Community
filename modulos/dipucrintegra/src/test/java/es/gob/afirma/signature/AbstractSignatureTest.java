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
 * <b>File:</b><p>es.gob.afirma.signature.AbstractSignatureTest.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>08/08/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 08/08/2011.
 */
package es.gob.afirma.signature;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import junit.framework.TestCase;

import org.junit.Ignore;

import es.gob.afirma.utils.UtilsFileSystem;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 08/08/2011.
 */
@Ignore
public class AbstractSignatureTest extends TestCase {
	
	protected static final String ERROR_EXCEPTION_NOT_THROWED = "No se ha lanzado la excepción esperada";
	
	private static final String  XML_DOCUMENT_PATH = "ficheroAfirmar.xml";
	
	private static final String  TEXT_DOCUMENT_PATH = "ficheroAfirmar.txt";
	
	private static final String  PDF_DOCUMENT_PATH = "pdfToSign.pdf";
	
	private static final String CADES_SIGN_PATH = "signatures/CAdES-SHA512.p7s";
	
	private static PrivateKeyEntry certificatePrivateKey;
	
	private static byte[] xmlDocument;
	
	private static byte[] textDocument;
	
	private static byte[] pdfDocument;
	
	private static byte[] cadesSignature;
	
	protected PrivateKeyEntry getCertificatePrivateKey() {
		if (certificatePrivateKey == null) {
			KeyStore.Entry key = null;
			try {
				InputStream is = new FileInputStream(ClassLoader.getSystemResource("keyStoreJCEKS.jks").getFile());
				KeyStore ks = KeyStore.getInstance("JCEKS");
				char [] password = "12345".toCharArray();
				ks.load(is, password);
				key = ks.getEntry("ServerSigner", new KeyStore.PasswordProtection(password));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			} catch (UnrecoverableEntryException e) {
				e.printStackTrace();
			}
			certificatePrivateKey = (KeyStore.PrivateKeyEntry) key;
		}
		return certificatePrivateKey;
	}
	
	protected byte[ ] getTextDocument() {
		if (textDocument == null) {
			textDocument = UtilsFileSystem.readFile(TEXT_DOCUMENT_PATH, true);
		}
		return textDocument;
	}
	
	protected byte[ ] getXmlDocument() {
		if (xmlDocument == null) {
			xmlDocument = UtilsFileSystem.readFile(XML_DOCUMENT_PATH, true);
		} 
		return xmlDocument;
	}
	
	protected byte[ ] getPdfDocument() {
		if (pdfDocument == null) {
			pdfDocument = UtilsFileSystem.readFile(PDF_DOCUMENT_PATH, true);
		} 
		return pdfDocument;
	}
	
	protected byte[] getCadesSignature() {
		if (cadesSignature == null) {
			cadesSignature = UtilsFileSystem.readFile(CADES_SIGN_PATH, true);
		}
		return cadesSignature;
	}
}
