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
 * <b>File:</b><p>es.gob.afirma.signature.cades.CadesSigner.java.</p>
 * <b>Description:</b><p>Class that signs documents with CAdES signature format.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>28/06/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 28/06/2011.
 */
package es.gob.afirma.signature.cades;

import java.io.IOException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.cms.SignerInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignerDigestMismatchException;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.signature.SignatureConstants;
import es.gob.afirma.signature.Signer;
import es.gob.afirma.signature.SigningException;
import es.gob.afirma.signature.cades.CMSBuilder.SignerInfoTypes;
import es.gob.afirma.utils.CryptoUtil;
import es.gob.afirma.utils.GenericUtils;

/**
 * <p>Class that signs documents with CAdES signature format.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 28/06/2011.
 */
public final class CadesSigner implements Signer {

    /**
     *  Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(CadesSigner.class);

    /**
     * Builder for create elements used in PKCS7/CMS.
     */
    private CMSBuilder cmsBuilder = new CMSBuilder();

    /**
     * {@inheritDoc} <br>
     * Optional parameters can be:
     * <ul>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#CADES_POLICY_DIGESTVALUE_PROP
     * 	SignatureProperties.CADES_POLICY_DIGESTVALUE_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#CADES_POLICY_IDENTIFIER_PROP
     * 	SignatureProperties.CADES_POLICY_IDENTIFIER_PROP}.</li>
     * </ul>
     * @see es.gob.afirma.signature.Signer#sign(byte[ ], java.lang.String, java.security.KeyStore.PrivateKeyEntry, String, Properties)
     */
    public byte[ ] sign(byte[ ] data, String algorithm, String signatureFormat, PrivateKeyEntry privateKey, Properties extraParams) throws SigningException {
	LOGGER.debug(Language.getResIntegra(ILogConstantKeys.CS_LOG004));
	// Validación de los parámetros de entrada
	checkInputParam(algorithm, data, privateKey);
	if (data == null || !GenericUtils.assertStringValue(algorithm) || privateKey == null) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.CS_LOG003);
	    LOGGER.error(errorMsg);
	    throw new IllegalArgumentException(errorMsg);
	}
	if (!SignatureConstants.SIGN_ALGORITHMS_SUPPORT_CADES.containsKey(algorithm)) {
	    String msg = Language.getFormatResIntegra(ILogConstantKeys.CS_LOG005, new Object[ ] { algorithm });
	    LOGGER.error(msg);
	    throw new SigningException(msg);
	}
	Properties externalParams = extraParams;
	if (externalParams == null) {
	    externalParams = new Properties();
	}
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.CS_LOG006, new Object[ ] { algorithm, signatureFormat, extraParams }));
	}
	final P7ContentSignerParameters csp = new P7ContentSignerParameters(data, algorithm, privateKey);
	try {
	    // oid para elemento EncapsulatedContentInfo
	    Oid dataType = new Oid(PKCSObjectIdentifiers.data.getId());

	    final String mode = signatureFormat == null ? SignatureConstants.DEFAULT_SIGN_MODE : signatureFormat;

	    // verificación de la inclusión contenido en la firma
	    boolean includeContent = true;
	    if (mode.equals(SignatureConstants.SIGN_MODE_EXPLICIT)) {
		includeContent = false;
	    }
	    // generación del elemento SignedData
	    byte[ ] result = cmsBuilder.generateSignedData(csp, includeContent, dataType, externalParams);
	    return GenericUtils.printResult(result, LOGGER);
	} catch (GSSException e) {
	    LOGGER.error(Language.getResIntegra(ILogConstantKeys.CS_LOG001), e);
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CS_LOG001), e);
	}

    }

    /**
     * Builds a co-signature (signature with signers in parallel) according to CMS standard (<a href="http://tools.ietf.org/html/rfc3852">IETF RFC 3852</a>).
     * @param signature signature data.
     * @param document data used to sign in original signature.
     * @param algorithm signature algorithm.
     * @param privateKey entry with private key and certificates chain.
     * @param extraParams optional parameters that can be:
     * <ul>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#CADES_POLICY_DIGESTVALUE_PROP
     * 	SignatureProperties.CADES_POLICY_DIGESTVALUE_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#CADES_POLICY_IDENTIFIER_PROP
     * 	SignatureProperties.CADES_POLICY_IDENTIFIER_PROP}.</li>
     * </ul>
     * @return  a DER byte array of co-signature.
     * @throws SigningException if any error ocurrs in the signing process.
     * @see es.gob.afirma.signature.Signer#coSign(byte[], byte[], java.lang.String, java.security.KeyStore.PrivateKeyEntry, java.util.Properties).
     */
    public byte[ ] coSign(byte[ ] signature, byte[ ] document, String algorithm, PrivateKeyEntry privateKey, Properties extraParams) throws SigningException {
	LOGGER.debug(Language.getResIntegra(ILogConstantKeys.CS_LOG007));

	// Verificación de los parámetros de entrada
	checkInputParam(algorithm, signature, document, privateKey);
	Properties externalParams = extraParams;
	if (externalParams == null) {
	    externalParams = new Properties();
	}
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.CS_LOG008, new Object[ ] { algorithm, extraParams }));
	CMSSignedData signedData = null;
	// Creación del objeto que representa el mensaje PKCS7-Signature.
	try {
	    signedData = new CMSSignedData(signature);
	} catch (CMSException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.CS_LOG002);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	}
	// Obtención de los certificados de la firma original junto al nuevo
	// certificado firmante.
	Store allCerts = addCertificateToStore(signedData.getCertificates(), (X509Certificate) privateKey.getCertificate());

	SignerInformationStore signerInformations = signedData.getSignerInfos();

	// Extracción el hash de cualquier firmante (signerInfo) para compararlo
	// con el hash calculado del documento de entrada
	SignerInformation signerInformation = (SignerInformation) signerInformations.getSigners().iterator().next();
	ASN1Set attValues = signerInformation.getSignedAttributes().get(CMSAttributes.messageDigest).getAttrValues();
	byte[ ] digestSignature = attValues.getObjects().hasMoreElements() ? ((DEROctetString) attValues.getObjects().nextElement()).getOctets() : new byte[0];

	// Obtención del algoritmo de hash del firmante original
	String digestAlgName = CryptoUtil.translateAlgorithmIdentifier(signerInformation.getDigestAlgorithmID());
	if (digestAlgName == null) {
	    throw new SigningException(Language.getFormatResIntegra(ILogConstantKeys.CS_LOG009, new Object[ ] { signerInformation.getDigestAlgorithmID() }));
	}
	// calculo del hash del documento original y comparación con el hash del
	// firmante original.
	byte[ ] digestDoc = CryptoUtil.digest(digestAlgName, document);
	if (!CryptoUtil.equalHashes(digestSignature, digestDoc)) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CS_LOG010));
	}
	P7ContentSignerParameters pkcs7Params = new P7ContentSignerParameters(document, algorithm, privateKey, externalParams);

	// Creación del objeto SignerInfo para la cofirma.
	SignerInfo newSignerInfo = cmsBuilder.generateSignerInfo(pkcs7Params, SignerInfoTypes.COSIGNATURE);

	// Creación de un set que incluyan todos los firmantes (incluyendo el
	// nuevo signerInfo)
	ASN1Set newSigners = cmsBuilder.convertToASN1Set(signerInformations);
	newSigners = cmsBuilder.addElementToASN1Set(newSigners, newSignerInfo);

	// Construcción del nuevo objeto SignedData.
	byte[ ] result = cmsBuilder.generateSignedData(signedData, newSignerInfo.getDigestAlgorithm(), allCerts, newSigners);
	return GenericUtils.printResult(result, LOGGER);
    }

    /**
     * Makes a counter-signature (signature with signers in serial) according to CMS standard (<a href="http://tools.ietf.org/html/rfc3852">IETF RFC 3852</a>).
     * @param signature signature data
     * @param algorithm signature algorithm.
     * @param privateKey entry with private key and certificates chain.
     * @param extraParams optional parameters that can be:
     * <ul>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#CADES_POLICY_DIGESTVALUE_PROP
     * 	SignatureProperties.CADES_POLICY_DIGESTVALUE_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#CADES_POLICY_IDENTIFIER_PROP
     * 	SignatureProperties.CADES_POLICY_IDENTIFIER_PROP}.</li>
     * </ul>
     * @return a DER byte array of countersignature.
     * @throws SigningException if any error ocurrs in the signing process.
     * @see es.gob.afirma.signature.Signer#counterSign(byte[], java.lang.String, java.security.KeyStore.PrivateKeyEntry, java.util.Properties).
     */
    public byte[ ] counterSign(byte[ ] signature, String algorithm, PrivateKeyEntry privateKey, Properties extraParams) throws SigningException {
	LOGGER.debug(Language.getResIntegra(ILogConstantKeys.CS_LOG011));
	// verificamos parámetros de entrada
	checkInputParam(algorithm, privateKey, signature);
	Properties externalParams = extraParams;
	if (externalParams == null) {
	    externalParams = new Properties();
	}
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.CS_LOG008, new Object[ ] { algorithm, extraParams }));
	CMSSignedData oldSignedData = null;
	// Creación del objeto que representa el mensaje PKCS7-Signature.
	try {
	    oldSignedData = new CMSSignedData(signature);
	} catch (CMSException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.CS_LOG002);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	}
	// obtención de todos los firmantes de la firma CMS
	SignerInformationStore oldSignerInfos = oldSignedData.getSignerInfos();

	// Obtención de los certificados de la firma original junto al nuevo
	// certificado firmante.
	Store allCerts = addCertificateToStore(oldSignedData.getCertificates(), (X509Certificate) privateKey.getCertificate());

	// Búsqueda de todos las hojas (últimas cofirmas/contrafirmas) para
	// realizar la contrafirma.
	P7ContentSignerParameters params = new P7ContentSignerParameters(algorithm, privateKey, externalParams);
	SignerInformationStore newSignerInfomations = counterSignLeaf(oldSignerInfos, params);

	// Creación de un set que incluyan todos los firmantes.
	ASN1Set newSigners = cmsBuilder.convertToASN1Set(newSignerInfomations);

	// Construcción del objeto SignedData
	String digestAlgorithm = SignatureConstants.SIGN_ALGORITHMS_SUPPORT_CADES.get(algorithm);
	AlgorithmIdentifier digestAlgorithmId = cmsBuilder.makeDigestAlgorithmId(digestAlgorithm);
	byte[ ] result = cmsBuilder.generateSignedData(oldSignedData, digestAlgorithmId, allCerts, newSigners);

	return GenericUtils.printResult(result, LOGGER);
    }

    /**
     * Adds a certificate a store certificates.
     * @param certificates store with certificates (instances of X509CertificateHolder).
     * @param certificate certificate to add.
     * @return a store with all certificates.
     * @throws SigningException if certificate given is wrong.
     */
    @SuppressWarnings("unchecked")
    private Store addCertificateToStore(Store certificates, X509Certificate certificate) throws SigningException {
	try {
	    Collection<X509CertificateHolder> certAuxCol = certificates.getMatches(null);
	    certAuxCol.add(new X509CertificateHolder(certificate.getEncoded()));
	    return new JcaCertStore(certAuxCol);
	} catch (CertificateEncodingException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.CS_LOG001);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	} catch (IOException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.CS_LOG001);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	}
    }

    /**
     * Signs the leave of each signer (end node of each signerInfo).
     * @param signerInfos store with all SignerInfomation to sign.
     * @param parameters countersigner parameters.
     * @return a store with all instance of SignerInformation signed.
     * @throws SigningException Used in error case building news SignedInfo instances.
     */
    private SignerInformationStore counterSignLeaf(SignerInformationStore signerInfos, P7ContentSignerParameters parameters) throws SigningException {
	List<SignerInformation> newSignerInfos = new ArrayList<SignerInformation>();
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.CS_LOG012, new Object[ ] { signerInfos.getSigners().size() }));
	// se busca el último firmante o contrafirmante para realizar la
	// contrafirma
	for (Iterator<?> iterator = signerInfos.getSigners().iterator(); iterator.hasNext();) {

	    SignerInformation signerInfoOld = (SignerInformation) iterator.next();
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.CS_LOG013, new Object[ ] { signerInfoOld.getSID().getIssuerAsString(), signerInfoOld.getSID().getSerialNumber() }));
	    SignerInformationStore counterSignsStore = signerInfoOld.getCounterSignatures();
	    AttributeTable counterAttributes = null;
	    if (counterSignsStore.getSigners().isEmpty()) { // si no tiene
		// ninguna
		// contrafirma,
		// contrafirmamos la
		// hoja.
		LOGGER.debug(Language.getResIntegra(ILogConstantKeys.CS_LOG014));
		// incluimos el valor de la firma del signerInfo padre) en los
		// parámetros de entrada.
		parameters.setContent(signerInfoOld.toASN1Structure().getEncryptedDigest().getOctets());
		// creamos un objeto countersignature (de tipo signerInfo)
		SignerInfo counterSignature = cmsBuilder.generateSignerInfo(parameters, SignerInfoTypes.COUNTERSIGNATURE);
		// incluimos el objeto countersignature como atributo no firmado
		Attribute counterAttribute = new Attribute(CMSAttributes.counterSignature, new DERSet(counterSignature));
		counterAttributes = new AttributeTable(new DERSet(counterAttribute));

	    } else { // si tiene contrafirmas buscamos dentro la hoja a firmar
		// (última firma/contrafirma)
		LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.CS_LOG015, new Object[ ] { counterSignsStore.size() }));
		SignerInformationStore counterSignatures = counterSignLeaf(counterSignsStore, parameters);
		Attribute unsignedAttribute = new Attribute(CMSAttributes.counterSignature, cmsBuilder.convertToASN1Set(counterSignatures));
		counterAttributes = new AttributeTable(new DERSet(unsignedAttribute));
	    }
	    // creamos un nuevo signerInfo con los datos originales pero
	    // incluyendo como atributo no firmado, el/los ojeto/s
	    // countersignature/s.
	    SignerInformation newSignerInfo = SignerInformation.replaceUnsignedAttributes(signerInfoOld, counterAttributes);
	    // newSignerInfo = newSignerInfo(signerInfoOld.toASN1Structure(),
	    // counterAttributes);
	    newSignerInfos.add(newSignerInfo);
	}
	return new SignerInformationStore(newSignerInfos);
    }

    /**
     * Verifies a CAdES signature.
     * @param eSignature signature data.
     * @param document document source (before it was signed). Optional for implicit signatures.
     * @return true if signature is verified and false otherwise.
     * @throws SigningException in error case.
     */
    public boolean verifySignature(byte[ ] eSignature, byte[ ] document) throws SigningException {
	if (eSignature == null) {
	    throw new IllegalArgumentException(Language.getResIntegra(ILogConstantKeys.CS_LOG003));
	}

	try {
	    // Creación del objeto que representa el mensaje PKCS7-Signature.
	    CMSSignedData signedData = null;
	    if (document != null) {
		signedData = new CMSSignedData(new CMSProcessableByteArray(document), eSignature);
	    } else {
		signedData = new CMSSignedData(eSignature);
		if (signedData.getSignedContent() == null) {
		    // Detached -- Se exige que se suministre el documento
		    throw new IllegalArgumentException(Language.getResIntegra(ILogConstantKeys.CS_LOG003));
		}
	    }

	    // validación de todos los firmantes que contiene la firma.
	    return verifySignerInfos(signedData.getSignerInfos(), signedData.getCertificates());

	} catch (CMSException e) {
	    LOGGER.error(e);
	    throw new SigningException(e);
	}
    }

    /**
     * Verifies a collection of signers.
     * @param signerInfos collection of SignerInformation instances.
     * @param certificates collection of certificates used by signers.
     * @return true if the signer information is verified, false otherwise.
     * @throws SigningException in error case.
     */
    private boolean verifySignerInfos(SignerInformationStore signerInfos, Store certificates) throws SigningException {
	boolean isValid = false;
	try {
	    for (Iterator<?> iterator = signerInfos.getSigners().iterator(); iterator.hasNext();) {
		SignerInformation signerInformation = (SignerInformation) iterator.next();
		X509CertificateHolder x509certificate = getCertificateBySignerId(certificates, signerInformation.getSID());
		if (x509certificate == null) {
		    LOGGER.warn(Language.getFormatResIntegra(ILogConstantKeys.CS_LOG016, new Object[ ] { signerInformation.getSID().toString() }));
		    return false;
		}
		SignerInformationVerifier signerInformationVerifier;

		signerInformationVerifier = new JcaSimpleSignerInfoVerifierBuilder().setProvider(SignatureConstants.BC_PROVIDER).build(x509certificate);

		isValid = signerInformation.verify(signerInformationVerifier);
		if (!isValid) {
		    return false;
		}
		// validación de todas las contrafirmas.
		SignerInformationStore counterSignatures = signerInformation.getCounterSignatures();
		if (!counterSignatures.getSigners().isEmpty()) {
		    isValid = verifySignerInfos(counterSignatures, certificates);
		}
	    }
	} catch (CMSSignerDigestMismatchException e) {
	    LOGGER.warn(e.toString());
	} catch (CMSException e) {
	    LOGGER.error(e);
	    throw new SigningException(e);
	} catch (OperatorCreationException e) {
	    LOGGER.error(e);
	    throw new SigningException(e);
	} catch (CertificateException e) {
	    LOGGER.error(e);
	    throw new SigningException(e);
	}
	return isValid;
    }

    /**
     * Gets a certificate from a certificate store.
     * @param certificates store with a collection of certificates.
     * @param signerId signer identifier.
     * @return the searched certificate
     */
    private X509CertificateHolder getCertificateBySignerId(Store certificates, SignerId signerId) {
	if (certificates != null && certificates.getMatches(null) != null && signerId != null) {
	    for (Iterator<?> iterator = certificates.getMatches(null).iterator(); iterator.hasNext();) {
		X509CertificateHolder cert = (X509CertificateHolder) iterator.next();
		if (signerId.match(cert)) {
		    return cert;
		}
	    }
	}
	return null;
    }

    /**
     * Checks if the values of input parameters (signature algorithm and a set of values) are corrects.
     * @param algorithm signature algorithm.
     * @param inputParams any value to check if are null.
     * @throws SigningException if signature algorithmn isn't support.
     */
    private void checkInputParam(String algorithm, Object... inputParams) throws SigningException {
	if (GenericUtils.checkNullValues(inputParams)) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.CS_LOG003);
	    LOGGER.error(errorMsg);
	    throw new IllegalArgumentException(errorMsg);
	}
	if (!SignatureConstants.SIGN_ALGORITHMS_SUPPORT_CADES.containsKey(algorithm)) {
	    String msg = Language.getFormatResIntegra(ILogConstantKeys.CS_LOG005, new Object[ ] { algorithm });
	    LOGGER.error(msg);
	    throw new SigningException(msg);
	}
    }

}
