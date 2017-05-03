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
 * <b>File:</b><p>es.gob.afirma.signature.pades.PadesSigner.java.</p>
 * <b>Description:</b><p>Class that signs documents with PAdES signature format.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>13/09/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 13/09/2011.
 */
package es.gob.afirma.signature.pades;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfPKCS7;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignature;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.signature.SignatureConstants;
import es.gob.afirma.signature.SignatureProperties;
import es.gob.afirma.signature.Signer;
import es.gob.afirma.signature.SigningException;
import es.gob.afirma.signature.cades.CMSBuilder;
import es.gob.afirma.signature.cades.P7ContentSignerParameters;
import es.gob.afirma.utils.CryptoUtil;
import es.gob.afirma.utils.GenericUtils;

/**
 * <p>Class that signs documents with PAdES signature format.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 13/09/2011.
 */
public final class PadesSigner implements Signer {

    /**
     * Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(PadesSigner.class);

    /**
     * Attribute that represents space reserved for content data.
     */
    private static final int CONTENT_RESERVED_SIZE = 8000;

    /**
     * {@inheritDoc} <br>
     * Optional parameters can be:
     * <ul>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#CADES_POLICY_DIGESTVALUE_PROP
     * 	SignatureProperties.CADES_POLICY_DIGESTVALUE_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#CADES_POLICY_IDENTIFIER_PROP
     * 	SignatureProperties.CADES_POLICY_IDENTIFIER_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#CADES_POLICY_DIGEST_ALGORITHM_PROP
     * 	SignatureProperties.POLICY_DIGEST_ALGORITHM_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#PADES_REASON_PROP
     * 	SignatureProperties.PADES_REASON_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#PADES_LOCATION_PROP
     * 	SignatureProperties.PADES_LOCATION_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#PADES_CONTACT_PROP
     * 	SignatureProperties.PADES_CONTACT_PROP}.</li>
     * </ul>
     * @see es.gob.afirma.signature.Signer#sign(byte[], java.lang.String, java.lang.String, java.security.KeyStore.PrivateKeyEntry, java.util.Properties)
     */
    public byte[ ] sign(byte[ ] data, String algorithm, String signatureFormat, PrivateKeyEntry privateKey, Properties extraParams) throws SigningException {
	LOGGER.debug("Iniciando la firma PAdES...");
	// Comprobación parámetros de entrada
	if (GenericUtils.checkNullValues(data, privateKey) || !GenericUtils.assertStringValue(algorithm)) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.PS_LOG001);
	    LOGGER.error(errorMsg);
	    throw new IllegalArgumentException(errorMsg);
	}
	if (!SignatureConstants.SIGN_ALGORITHMS_SUPPORT_CADES.containsKey(algorithm)) {
	    String errorMsg = Language.getFormatResIntegra(ILogConstantKeys.PS_LOG002, new Object[ ] { algorithm });
	    LOGGER.error(errorMsg);
	    throw new SigningException(errorMsg);
	}
	Properties externalParams = extraParams;
	if (externalParams == null) {
	    externalParams = new Properties();
	}

	if (SignatureConstants.SIGN_MODE_EXPLICIT.equals(signatureFormat)) {
	    LOGGER.warn(Language.getResIntegra(ILogConstantKeys.PS_LOG003));
	}

	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.PS_LOG004, new Object[ ] { algorithm, extraParams }));
	//
	ByteArrayOutputStream bytesResult = new ByteArrayOutputStream();

	try {

	    Certificate[ ] certificateChain = privateKey.getCertificateChain();
	    // leemos el pdf original
	    PdfReader reader = new PdfReader(data);

	    // creamos el contenido de la firma
	    PdfStamper stp = PdfStamper.createSignature(reader, bytesResult, '\0', null, true);
	    PdfSignatureAppearance signatureAparece = stp.getSignatureAppearance();
	    signatureAparece.setSignDate(new GregorianCalendar());
	    signatureAparece.setCrypto(null, certificateChain, null, PdfName.ADOBE_PPKLITE);
	    PdfSignature signDictionary = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName(SignatureConstants.PADES_SUBFILTER_VALUE));
	    signDictionary.setDate(new PdfDate(signatureAparece.getSignDate()));
	    signDictionary.setName(PdfPKCS7.getSubjectFields((X509Certificate) certificateChain[0]).getField("CN"));

	    // incluimos propiedades de la firma (si existen)
	    addPropertyToDictionary(externalParams, signDictionary);

	    // Comentamos inclusión entrada 'Cert' para firmas PADES
	    // signDictionary.setCert(((X509Certificate)certificateChain[0]).getEncoded());
	    signatureAparece.setCryptoDictionary(signDictionary);
	    // Reservamos espacio para el contenido de la clave /Contents
	    // 2229 <- CamerFirma Demo SHA-1 (1024)
	    // 5123 <- Firma Profesional Demo SHA-1 (1024)
	    // 5031 <- DNIe SHA-2 (2048)
	    int csize = CONTENT_RESERVED_SIZE;
	    HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
	    exc.put(PdfName.CONTENTS, Integer.valueOf(csize * 2 + 2));
	    signatureAparece.preClose(exc);

	    /**	********************************
	     * Creación del objeto SignedData*
	     ***********************************/
	    byte[ ] signedData = null;
	    P7ContentSignerParameters csp = new P7ContentSignerParameters(data, algorithm, privateKey);
	    Oid dataType = new Oid(PKCSObjectIdentifiers.data.getId());
	    // cálculo del digest del documento pdf original
	    String digestAlgorithm = CryptoUtil.getDigestAlgorithmName(algorithm);

	    byte[ ] messageDigest = CryptoUtil.digest(digestAlgorithm, GenericUtils.getDataFromInputStream(signatureAparece.getRangeStream()));
	    csp.setDigestValue(messageDigest);

	    // incluimos en los parámetros opcionales el formato de la firma
	    // pades para crear signedData (específico para PAdES)
	    externalParams.put(SignatureConstants.SIGN_FORMAT_PADES, true);
	    signedData = new CMSBuilder().generateSignedData(csp, false, dataType, externalParams);

	    /***********************************/
	    byte[ ] outc = new byte[csize];
	    PdfDictionary dic2 = new PdfDictionary();
	    System.arraycopy(signedData, 0, outc, 0, signedData.length);
	    dic2.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
	    signatureAparece.close(dic2);
	} catch (DocumentException e) {
	    LOGGER.error(e);
	    throw new SigningException(e);
	} catch (IOException e) {
	    LOGGER.error(e);
	    throw new SigningException(e);
	} catch (GSSException e) {
	    LOGGER.error(e);
	    throw new SigningException(e);
	}
	byte[ ] result = bytesResult.toByteArray();
	return GenericUtils.printResult(result, LOGGER);
    }

    /**
     * Adds properties to signature dictionary.
     * @param externalParams optional parameters
     * @param signDictionary signature dictionary
     */
    private void addPropertyToDictionary(Properties externalParams, PdfSignature signDictionary) {
	String reason = externalParams.getProperty(SignatureProperties.PADES_REASON_PROP);
	String contact = externalParams.getProperty(SignatureProperties.PADES_CONTACT_PROP);
	String location = externalParams.getProperty(SignatureProperties.PADES_LOCATION_PROP);
	if (GenericUtils.assertStringValue(reason)) {
	    signDictionary.setReason(reason);
	}
	// Localización donde se produce la firma
	if (GenericUtils.assertStringValue(location)) {
	    signDictionary.setLocation(location);
	}

	// Contacto del firmante
	if (GenericUtils.assertStringValue(contact)) {
	    signDictionary.setContact(contact);
	}

    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.signature.Signer#coSign(byte[], byte[], java.lang.String, java.security.KeyStore.PrivateKeyEntry, java.util.Properties)
     */
    public byte[ ] coSign(byte[ ] signature, byte[ ] document, String algorithm, PrivateKeyEntry privateKey, Properties extraParams) throws SigningException {
	throw new SigningException(Language.getResIntegra(ILogConstantKeys.PS_LOG005));
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.signature.Signer#counterSign(byte[], java.lang.String, java.security.KeyStore.PrivateKeyEntry, java.util.Properties)
     */
    public byte[ ] counterSign(byte[ ] signature, String algorithm, PrivateKeyEntry privateKey, Properties optionalParams) throws SigningException {
	throw new SigningException(Language.getResIntegra(ILogConstantKeys.PS_LOG005));
    }

}
