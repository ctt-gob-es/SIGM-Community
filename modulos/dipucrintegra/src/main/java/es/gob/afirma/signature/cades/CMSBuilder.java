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
 * <b>File:</b><p>es.gob.afirma.signature.cades.SignedDataBuilder.java.</p>
 * <b>Description:</b><p>Class contains funcionalities for build objects that represents elements
 *  used in CMS.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>13/09/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 13/09/2011.
 */
package es.gob.afirma.signature.cades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.BERSet;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.cms.SignerIdentifier;
import org.bouncycastle.asn1.cms.SignerInfo;
import org.bouncycastle.asn1.esf.OtherHashAlgAndValue;
import org.bouncycastle.asn1.esf.SigPolicyQualifierInfo;
import org.bouncycastle.asn1.esf.SigPolicyQualifiers;
import org.bouncycastle.asn1.esf.SignaturePolicyId;
import org.bouncycastle.asn1.esf.SignaturePolicyIdentifier;
import org.bouncycastle.asn1.ess.ContentHints;
import org.bouncycastle.asn1.ess.ESSCertID;
import org.bouncycastle.asn1.ess.ESSCertIDv2;
import org.bouncycastle.asn1.ess.SigningCertificate;
import org.bouncycastle.asn1.ess.SigningCertificateV2;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.util.Store;
import org.ietf.jgss.Oid;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.signature.SignatureConstants;
import es.gob.afirma.signature.SignatureProperties;
import es.gob.afirma.signature.SigningException;
import es.gob.afirma.transformers.TransformersException;
import es.gob.afirma.utils.Base64Coder;
import es.gob.afirma.utils.CryptoUtil;
import es.gob.afirma.utils.GenericUtils;

/**
 * <p>Class contains funcionalities for build objects that represents elements
 *  used in CMS (defined in the
 * <a href="http://tools.ietf.org/html/rfc3852">rfc3852</a>)</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 13/09/2011.
 */
public final class CMSBuilder {

    /**
     * Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(CMSBuilder.class);

    /**
     * <p>Enumeration class that represents diferent types of SignerInfo objects.</p>
     * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
     * certificates and electronic signature.</p>
     * @version 1.0, 27/01/2012.
     */
    enum SignerInfoTypes {
	/**Enum that represents type used for countersignatures (signatures in parallels).*/
	COUNTERSIGNATURE,
	/**Enum that represents type used for cosignatures (signatures in serial).*/
	COSIGNATURE
    };

    /**
     * Attribute that represents the oid for RFC-5126 id-spq-ets-uri element.
     */
    private static final String ID_SPQ_ETS_SQT_URI = "1.2.840.113549.1.9.16.5.1";

    /**
     *<p> Builds a signedData object used in CAdES signatures. SignedData is defined in the
     * <a href="http://tools.ietf.org/html/rfc3852">rfc3852</a>:<br>
     * <code><pre>
     * id-signedData OBJECT IDENTIFIER ::= { iso(1) member-body(2)
         us(840) rsadsi(113549) pkcs(1) pkcs7(7) 2 }

      SignedData ::= SEQUENCE {
        version CMSVersion,
        digestAlgorithms DigestAlgorithmIdentifiers,
        encapContentInfo EncapsulatedContentInfo,
        certificates [0] IMPLICIT CertificateSet OPTIONAL,
        crls [1] IMPLICIT RevocationInfoChoices OPTIONAL,
        signerInfos SignerInfos }

    </pre></code></p>
     * @param parameters parameters used in the signature.
     * @param includeContent indicates whether the document content is included in the signature or is only referenced.
     * @param dataType type of content to sign.
     * @param extraParams optional parameters.
     * @return a sinedData object.
     * @throws SigningException in error case.
     */
    public byte[ ] generateSignedData(final P7ContentSignerParameters parameters, final boolean includeContent, final Oid dataType, Properties extraParams) throws SigningException {
	LOGGER.debug(Language.getResIntegra(ILogConstantKeys.CMSB_LOG001));
	if (GenericUtils.checkNullValues(parameters, dataType)) {
	    throw new IllegalArgumentException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG002));
	}
	Properties optionalParams = extraParams;
	if (optionalParams == null) {
	    optionalParams = new Properties();
	}

	try {
	    // 1. VERSION
	    // la version se mete en el constructor del signedData y es 1

	    // 2. DIGESTALGORITM
	    // buscamos que tipo de algoritmo de digest es y lo codificamos con
	    // su OID

	    final ASN1EncodableVector digestAlgs = new ASN1EncodableVector();

	    String digestAlgorithm = SignatureConstants.SIGN_ALGORITHMS_SUPPORT_CADES.get(parameters.getSignatureAlgorithm());

	    final sun.security.x509.AlgorithmId digestAlgorithmId = sun.security.x509.AlgorithmId.get(digestAlgorithm);

	    AlgorithmIdentifier digAlgId = makeAlgId(digestAlgorithmId.getOID().toString(), digestAlgorithmId.getEncodedParams());

	    digestAlgs.add(digAlgId);

	    // 3. CONTENTINFO
	    // si se introduce el contenido o no

	    ContentInfo encInfo = null;
	    ASN1ObjectIdentifier contentTypeOID = new ASN1ObjectIdentifier(dataType.toString());

	    if (includeContent) {
		LOGGER.debug(Language.getResIntegra(ILogConstantKeys.CMSB_LOG003));
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		byte[ ] content2 = parameters.getContent();
		CMSProcessable msg = new CMSProcessableByteArray(content2);
		try {
		    msg.write(bOut);
		} catch (IOException ex) {
		    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG004), ex);
		} catch (CMSException e) {
		    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG004), e);
		}
		encInfo = new ContentInfo(contentTypeOID, new DEROctetString(bOut.toByteArray()));
	    } else {
		LOGGER.debug(Language.getResIntegra(ILogConstantKeys.CMSB_LOG005));
		encInfo = new ContentInfo(contentTypeOID, null);
	    }

	    // 4. CERTIFICADOS
	    // obtenemos la lista de certificados e incluimos el certificado
	    // firmante

	    X509Certificate signerCertificate = (X509Certificate) parameters.getPrivateKey().getCertificate();
	    ASN1Set certificates = createBerSetFromList(X509CertificateStructure.getInstance(ASN1Object.fromByteArray(signerCertificate.getEncoded())));

	    ASN1Set certrevlist = null;

	    // 5. SIGNERINFO
	    // raiz de la secuencia de SignerInfo
	    ASN1EncodableVector signerInfos = new ASN1EncodableVector();

	    TBSCertificateStructure tbs = TBSCertificateStructure.getInstance(ASN1Object.fromByteArray(signerCertificate.getTBSCertificate()));
	    IssuerAndSerialNumber encSid = new IssuerAndSerialNumber(X500Name.getInstance(tbs.getIssuer()), tbs.getSerialNumber().getValue());

	    SignerIdentifier identifier = new SignerIdentifier(encSid);

	    // AlgorithmIdentifier
	    digAlgId = new AlgorithmIdentifier(new DERObjectIdentifier(digestAlgorithmId.getOID().toString()), new DERNull());

	    ASN1Set signedAttr = generateSignedAttr(parameters, digestAlgorithmId, digAlgId, digestAlgorithm, dataType, optionalParams);

	    // digEncryptionAlgorithm
	    AlgorithmIdentifier encAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find(parameters.getSignatureAlgorithm());
	    ASN1OctetString sign2 = sign(parameters.getSignatureAlgorithm(), parameters.getPrivateKey(), signedAttr);

	    signerInfos.add(new SignerInfo(identifier, digAlgId, signedAttr, encAlgId, sign2, null));

	    // construimos el Signed Data y lo devolvemos
	    return new ContentInfo(PKCSObjectIdentifiers.signedData, new SignedData(new DERSet(digestAlgs), encInfo, certificates, certrevlist, new DERSet(signerInfos))).getDEREncoded();
	} catch (CertificateException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG006), e);
	} catch (NoSuchAlgorithmException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG007), e);
	} catch (IOException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG008), e);
	}

    }

    /**
     * Generates a set of signed attributes used in CMS messages defined in <a href="http://tools.ietf.org/html/rfc3852">rfc3852</a>.
     * Definition:
     * <pre>
     SignedAttributes ::= SET SIZE (1..MAX) OF Attribute

      Attribute ::= SEQUENCE {
        attrType OBJECT IDENTIFIER,
        attrValues SET OF AttributeValue }

      AttributeValue ::= ANY
      </pre>
     * @param parameters parameters for signature process.
     * @param digestAlgorithmId digest algorithm identificator.
     * @param algId algorithm identificator.
     * @param digestAlgorithm digest algorithm name.
     * @param dataType data type oid identificator(Universal Object Identifiers) to sign.
     * @param extraParams optional parameters
     * @return a SignerInfo object.
     * @throws SigningException throws in error case.
     */
    private ASN1Set generateSignedAttr(P7ContentSignerParameters parameters, sun.security.x509.AlgorithmId digestAlgorithmId, AlgorithmIdentifier algId, String digestAlgorithm, Oid dataType, Properties extraParams) throws SigningException {

	try {
	    boolean isPadesSigner = extraParams.get(SignatureConstants.SIGN_FORMAT_PADES) == null ? false : true;
	    X509Certificate cert = (X509Certificate) parameters.getPrivateKey().getCertificate();

	    // // ATRIBUTOS

	    // authenticatedAttributes
	    ASN1EncodableVector contexExpecific = new ASN1EncodableVector();

	    // tipo de contenido
	    contexExpecific.add(new Attribute(CMSAttributes.contentType, new DERSet(new DERObjectIdentifier(dataType.toString()))));

	    // fecha de firma
	    if (!isPadesSigner) {
		contexExpecific.add(new Attribute(CMSAttributes.signingTime, new DERSet(new DERUTCTime(new Date()))));
	    }

	    // Política de la firma --> elemento SignaturePolicyId
	    addPolicy(contexExpecific, extraParams, isPadesSigner);

	    // Digest del documento
	    byte[ ] messageDigest = null;
	    // Si el valor del digest viene externo lo incluimos directamente en
	    // los atributos.
	    if (parameters.getDigestValue() != null) {
		messageDigest = parameters.getDigestValue();
	    } else { // si no lo calculamos a partir de los datos del documento
		// original.
		messageDigest = CryptoUtil.digest(digestAlgorithm, parameters.getContent());
	    }
	    contexExpecific.add(new Attribute(CMSAttributes.messageDigest, new DERSet(new DEROctetString(messageDigest))));

	    if (!digestAlgorithm.equals(CryptoUtil.HASH_ALGORITHM_SHA1)) {

		// INICIO SIGNING CERTIFICATE-V2

		/**
		 * IssuerSerial ::= SEQUENCE {
		 *   issuer                   GeneralNames,
		 *   serialNumber             CertificateSerialNumber
		 *
		 */

		TBSCertificateStructure tbs = TBSCertificateStructure.getInstance(ASN1Object.fromByteArray(cert.getTBSCertificate()));
		GeneralName gn = new GeneralName(tbs.getIssuer());
		GeneralNames gns = new GeneralNames(gn);

		IssuerSerial isuerSerial = new IssuerSerial(gns, tbs.getSerialNumber());

		/**
		 * ESSCertIDv2 ::=  SEQUENCE {
		 *       hashAlgorithm           AlgorithmIdentifier  DEFAULT {algorithm id-sha256},
		 *       certHash                 Hash,
		 *       issuerSerial             IssuerSerial OPTIONAL
		 *   }
		 *
		 *   Hash ::= OCTET STRING
		 */

		MessageDigest md = MessageDigest.getInstance(CryptoUtil.getDigestAlgorithmName(digestAlgorithmId.getName()));
		byte[ ] certHash = md.digest(cert.getEncoded());
		ESSCertIDv2[ ] essCertIDv2 = { new ESSCertIDv2(algId, certHash, isuerSerial) };

		/**
		 * PolicyInformation ::= SEQUENCE {
		 *           policyIdentifier   CertPolicyId,
		 *           policyQualifiers   SEQUENCE SIZE (1..MAX) OF
		 *                                  PolicyQualifierInfo OPTIONAL }
		 *
		 *      CertPolicyId ::= OBJECT IDENTIFIER
		 *
		 *      PolicyQualifierInfo ::= SEQUENCE {
		 *           policyQualifierId  PolicyQualifierId,
		 *           qualifier          ANY DEFINED BY policyQualifierId }
		 *
		 */

		SigningCertificateV2 scv2 = null;
		// if (qualifier != null) {
		//
		// DERObjectIdentifier oidQualifier = new
		// DERObjectIdentifier(qualifier.toString());
		// if (politica.equals("")) {
		// pI = new PolicyInformation[ ] { new
		// PolicyInformation(oidQualifier) };
		// } else {
		// PolicyQualifierInfo pqInfo = new
		// PolicyQualifierInfo(politica);
		// pI = new PolicyInformation[ ] { new
		// PolicyInformation(oidQualifier, new DERSequence(pqInfo)) };
		// }
		//
		// /**
		// * SigningCertificateV2 ::= SEQUENCE {
		// * certs SEQUENCE OF ESSCertIDv2,
		// * policies SEQUENCE OF PolicyInformation OPTIONAL
		// * }
		// *
		// */
		// scv2 = new SigningCertificateV2(essCertIDv2, pI); // con
		// // politica
		// } else {
		scv2 = new SigningCertificateV2(essCertIDv2); // Sin
		// politica
		// }

		// Secuencia con singningCertificate
		contexExpecific.add(new Attribute(PKCSObjectIdentifiers.id_aa_signingCertificateV2, new DERSet(scv2)));

		// FIN SINGING CERTIFICATE-V2

	    } else {

		// INICIO SINGNING CERTIFICATE

		/**
		 *	IssuerSerial ::= SEQUENCE {
		 *	     issuer                   GeneralNames,
		 *	     serialNumber             CertificateSerialNumber
		 *	}
		 */

		TBSCertificateStructure tbs = TBSCertificateStructure.getInstance(ASN1Object.fromByteArray(cert.getTBSCertificate()));
		GeneralName gn = new GeneralName(tbs.getIssuer());
		GeneralNames gns = new GeneralNames(gn);

		IssuerSerial isuerSerial = new IssuerSerial(gns, tbs.getSerialNumber());

		/**
		 *	ESSCertID ::=  SEQUENCE {
		 *   certHash                 Hash,
		 *   issuerSerial             IssuerSerial OPTIONAL
		 *	}
		 * 
		 *	Hash ::= OCTET STRING -- SHA1 hash of entire certificate
		 */
		// MessageDigest
		String digestAlgorithmName = CryptoUtil.getDigestAlgorithmName(digestAlgorithmId.getName());
		MessageDigest md = MessageDigest.getInstance(digestAlgorithmName);
		byte[ ] certHash = md.digest(cert.getEncoded());
		ESSCertID essCertID = new ESSCertID(certHash, isuerSerial);

		/**
		 * PolicyInformation ::= SEQUENCE {
		 *           policyIdentifier   CertPolicyId,
		 *           policyQualifiers   SEQUENCE SIZE (1..MAX) OF
		 *                                  PolicyQualifierInfo OPTIONAL }
		 *
		 *      CertPolicyId ::= OBJECT IDENTIFIER
		 *
		 *      PolicyQualifierInfo ::= SEQUENCE {
		 *           policyQualifierId  PolicyQualifierId,
		 *           qualifier          ANY DEFINED BY policyQualifierId }
		 *
		 */

		SigningCertificate scv = new SigningCertificate(essCertID); // Sin
		// politica

		/**
		 * id-aa-signingCertificate OBJECT IDENTIFIER ::= { iso(1)
		 *   member-body(2) us(840) rsadsi(113549) pkcs(1) pkcs9(9)
		 *   smime(16) id-aa(2) 12 }
		 */
		// Secuencia con singningCertificate
		contexExpecific.add(new Attribute(PKCSObjectIdentifiers.id_aa_signingCertificate, new DERSet(scv)));
	    }

	    // INICIO SIGPOLICYID ATTRIBUTE

	    // if (qualifier != null) {
	    // /*
	    // * SigPolicyQualifierInfo ::= SEQUENCE {
	    // * SigPolicyQualifierId SigPolicyQualifierId,
	    // * SigQualifier ANY DEFINED BY policyQualifierId }
	    // *
	    // * SignaturePolicyId ::= SEQUENCE {
	    // * sigPolicyId SigPolicyId,
	    // * sigPolicyHash SigPolicyHash,
	    // * sigPolicyQualifiers SEQUENCE SIZE (1..MAX) OF
	    // * SigPolicyQualifierInfo OPTIONAL}
	    //
	    // * SigPolicyId ::= OBJECT IDENTIFIER
	    // *
	    // * OtherHashAlgAndValue ::= SEQUENCE {
	    // * hashAlgorithm AlgorithmIdentifier,
	    // * hashValue OCTET STRING }
	    // *
	    // */
	    //
	    // DERObjectIdentifier DOISigPolicyId = new
	    // DERObjectIdentifier(qualifier.toString());
	    // byte[ ] hashed = CryptoUtil.digest(digestAlgorithm,
	    // "hashPolitica".getBytes());
	    // DigestInfo OtherHashAlgAndValue = new DigestInfo(digAlgId,
	    // hashed);
	    //
	    // SigPolicyQualifierInfo spqInfo = new
	    // SigPolicyQualifierInfo(politica);
	    //
	    // ASN1EncodableVector v = new ASN1EncodableVector();
	    // // sigPolicyId
	    // v.add(DOISigPolicyId);
	    // // sigPolicyHash
	    // v.add(OtherHashAlgAndValue.toASN1Object()); // como sequence
	    // // sigPolicyQualifiers
	    // v.add(spqInfo.toASN1Object());
	    // DERSequence ds = new DERSequence(v);
	    //
	    // // Secuencia con singningCertificate
	    // contexExpecific.add(new
	    // Attribute(PKCSObjectIdentifiers.id_aa_ets_sigPolicyId, new
	    // DERSet(ds.toASN1Object())));
	    // // FIN SIGPOLICYID ATTRIBUTE
	    // }
	    return getAttributeSet(new AttributeTable(contexExpecific));

	} catch (CertificateException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG006), e);
	} catch (NoSuchAlgorithmException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG007), e);
	} catch (IOException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG008), e);
	}
    }

    /**
     * Adds a SignaturePolicyId to SignedInfo element.
     * This is a mandatory attribute for EPES format:
     * <pre>
     * SignaturePolicyId ::= SEQUENCE {
    		sigPolicyId SigPolicyId,
    		sigPolicyHash SigPolicyHash,
    		sigPolicyQualifiers SEQUENCE SIZE (1..MAX) OF SigPolicyQualifierInfo OPTIONAL}*
     * </pre>
     * @param contexExpecific SignedInfo attributes to adds  SignaturePolicyId element.
     * @param extraParams user optional params
     * @param isPadesSigner indicates if is a PAdES signature.
     * @throws SigningException throws in error case.
     */
    private void addPolicy(ASN1EncodableVector contexExpecific, Properties extraParams, boolean isPadesSigner) throws SigningException {
	String policyDigestValue = extraParams.getProperty(SignatureProperties.CADES_POLICY_DIGESTVALUE_PROP);
	String policyDigestAlg = extraParams.getProperty(SignatureProperties.CADES_POLICY_DIGEST_ALGORITHM_PROP);
	String policyIdentifier = extraParams.getProperty(SignatureProperties.CADES_POLICY_IDENTIFIER_PROP);
	String policyQualifiers = extraParams.getProperty(SignatureProperties.CADES_POLICY_QUALIFIER_PROP);

	// comprobamos si tiene parámetros para la política de firma.
	if (GenericUtils.assertStringValue(policyDigestValue) && GenericUtils.assertStringValue(policyIdentifier)) {

	    // extraemos el valor del digest de la política (en base64)
	    byte[ ] digestValueBytes = null;
	    try {
		digestValueBytes = Base64Coder.decodeBase64(extraParams.getProperty(SignatureProperties.CADES_POLICY_DIGESTVALUE_PROP).getBytes());
	    } catch (TransformersException e) {
		throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG009), e);
	    }

	    // extraemos el algoritmo de la política y se crea su identificador
	    // correspondiente
	    sun.security.x509.AlgorithmId policyAlgorithmId = null;
	    try {
		if (GenericUtils.assertStringValue(policyDigestAlg)) {
		    policyAlgorithmId = sun.security.x509.AlgorithmId.get(policyDigestAlg);
		} else {
		    policyAlgorithmId = sun.security.x509.AlgorithmId.get(CryptoUtil.HASH_ALGORITHM_SHA1);
		}
	    } catch (NoSuchAlgorithmException e) {
		// si se produce algún error en la obtención del oid del
		// algoritmo, se indica el oid para SHA1.
		policyAlgorithmId = new sun.security.x509.AlgorithmId(sun.security.x509.AlgorithmId.SHA_oid);
	    }
	    AlgorithmIdentifier policyAlgorithm = new AlgorithmIdentifier(new DERObjectIdentifier(policyAlgorithmId.getOID().toString()), new DERNull());

	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.CMSB_LOG010, new Object[ ] { policyIdentifier, policyDigestValue, policyAlgorithm.getAlgorithm().getId(), policyQualifiers }));

	    SigPolicyQualifierInfo sigPolicyQualifierInfo[] = new SigPolicyQualifierInfo[1];
	    SigPolicyQualifiers sigPolicyQualifiers = null;

	    if (GenericUtils.assertStringValue(policyQualifiers)) {
		sigPolicyQualifierInfo[0] = new SigPolicyQualifierInfo(new DERObjectIdentifier(ID_SPQ_ETS_SQT_URI), new DERIA5String(policyQualifiers));
		sigPolicyQualifiers = new SigPolicyQualifiers(sigPolicyQualifierInfo);
	    }

	    contexExpecific.add(new Attribute(PKCSObjectIdentifiers.id_aa_ets_sigPolicyId, new DERSet(new SignaturePolicyIdentifier(new SignaturePolicyId(new DERObjectIdentifier(policyIdentifier), new OtherHashAlgAndValue(policyAlgorithm, new DEROctetString(digestValueBytes)), sigPolicyQualifiers)))));

	    // si la firma no es PADES se incluye además el atributo
	    // content-hints

	    /**ContentHints ::= SEQUENCE {
	    	  contentDescription UTF8String (SIZE (1..MAX)) OPTIONAL,
	    	  contentType ContentType }*/
	    if (!isPadesSigner) {
		contexExpecific.add(new Attribute(CMSAttributes.contentHint, new DERSet(new ContentHints(PKCSObjectIdentifiers.data))));
	    }
	}
    }

    /**
     * Performs signature of signed attributes.
     * @param signatureAlgorithm signature algorithm
     * @param keyEntry private key.
     * @param signedAttributes signed attributes of SignedInfo.
     * @return signature in ASN1OctetString format.
     * @throws SigningException in error case.
     */
    private ASN1OctetString sign(String signatureAlgorithm, PrivateKeyEntry keyEntry, ASN1Set signedAttributes) throws SigningException {

	Signature sig = null;
	try {
	    sig = Signature.getInstance(signatureAlgorithm);
	} catch (Exception e) {
	    throw new SigningException(Language.getFormatResIntegra(ILogConstantKeys.CMSB_LOG011, new Object[ ] { signatureAlgorithm }), e);
	}

	byte[ ] tmp = null;

	try {
	    tmp = signedAttributes.getEncoded(ASN1Encodable.DER);
	} catch (IOException ex) {
	    LOGGER.error(ex);
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG012), ex);
	}

	// Indicar clave privada para la firma
	try {
	    sig.initSign(keyEntry.getPrivateKey());
	} catch (InvalidKeyException e) {
	    throw new SigningException(Language.getFormatResIntegra(ILogConstantKeys.CMSB_LOG013, new Object[ ] { signatureAlgorithm }), e);
	}

	// Actualizamos la configuracion de firma
	try {
	    sig.update(tmp);
	} catch (SignatureException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG014), e);
	}

	// firmamos.
	byte[ ] realSig = null;
	try {
	    realSig = sig.sign();
	} catch (SignatureException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG015), e);
	}

	ASN1OctetString encDigest = new DEROctetString(realSig);

	return encDigest;

    }

    /**
     * Builds a SignerInfo object used in CAdES. SignerInfo is defined in the
     * <a href="http://tools.ietf.org/html/rfc3852">RFC 3852</a>:<br>
     *<pre>
     *SignerInfo ::= SEQUENCE {
        version CMSVersion,
        sid SignerIdentifier,
        digestAlgorithm DigestAlgorithmIdentifier,
        signedAttrs [0] IMPLICIT SignedAttributes OPTIONAL,
        signatureAlgorithm SignatureAlgorithmIdentifier,
        signature SignatureValue,
        unsignedAttrs [1] IMPLICIT UnsignedAttributes OPTIONAL }

      SignerIdentifier ::= CHOICE {
        issuerAndSerialNumber IssuerAndSerialNumber,
        subjectKeyIdentifier [0] SubjectKeyIdentifier }

      SignedAttributes ::= SET SIZE (1..MAX) OF Attribute

      UnsignedAttributes ::= SET SIZE (1..MAX) OF Attribute

      Attribute ::= SEQUENCE {
        attrType OBJECT IDENTIFIER,
        attrValues SET OF AttributeValue }

      AttributeValue ::= ANY

      SignatureValue ::= OCTET STRING
      </pre>
     * @param parameters object that includes all data to generate SignerInfo object.
     * @param signType SignerInfo type to generate.
     * @return a new {@link SignerInfo} instance.
     * @throws SigningException in error case.
     */
    SignerInfo generateSignerInfo(P7ContentSignerParameters parameters, SignerInfoTypes signType) throws SigningException {
	try {
	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.CMSB_LOG016));
	    // obtención del certificado firmante
	    PrivateKeyEntry privateKey = parameters.getPrivateKey();
	    X509CertificateHolder cert = new X509CertificateHolder(privateKey.getCertificate().getEncoded());
	    IssuerAndSerialNumber issuerAndSerial = cert.getIssuerAndSerialNumber();

	    // SignerIdentifier
	    SignerIdentifier signerIdentifier = new SignerIdentifier(issuerAndSerial);

	    // DigestAlgorithmIdentifier
	    String digestAlgorithm = SignatureConstants.SIGN_ALGORITHMS_SUPPORT_CADES.get(parameters.getSignatureAlgorithm());
	    AlgorithmIdentifier digestAlgorithmId = makeDigestAlgorithmId(digestAlgorithm);

	    // ATRIBUTOS FIRMADOS
	    // =====================================================================================
	    ASN1EncodableVector signedAttributes = new ASN1EncodableVector();

	    // ContentType (no se incluye en counterSignature CMS por
	    // especificación de RFC 3852)
	    if (!signType.equals(SignerInfoTypes.COUNTERSIGNATURE)) {
		signedAttributes.add(new Attribute(CMSAttributes.contentType, new DERSet(PKCSObjectIdentifiers.data)));
	    }
	    // SigningTime (fecha de firma)
	    signedAttributes.add(new Attribute(CMSAttributes.signingTime, new DERSet(new DERUTCTime(new Date()))));

	    // Política de la firma --> elemento SignaturePolicyId
	    addPolicy(signedAttributes, parameters.getOptionalParams(), false);

	    // MessageDigest
	    // si es countersignature el digest se crea a partir del campo
	    // signatureValue del SignerInfo padre.
	    // si es cosign el digest se crea a partir del contenido pasado como
	    // argumento.
	    byte[ ] messageDigest = CryptoUtil.digest(digestAlgorithm, parameters.getContent());
	    signedAttributes.add(new Attribute(CMSAttributes.messageDigest, new DERSet(new DEROctetString(messageDigest))));

	    // Signing Certificate Attributes
	    signedAttributes.add(generateSigningCertAttr(cert, digestAlgorithm, digestAlgorithmId));

	    ASN1Set signedAttrSet = getAttributeSet(new AttributeTable(signedAttributes));
	    // ========================================================================================================

	    // SignatureAlgorithmIdentifier
	    AlgorithmIdentifier signAlgorithmId = new DefaultSignatureAlgorithmIdentifierFinder().find(parameters.getSignatureAlgorithm());

	    // SignatureValue (cálculo de la firma de los atributos firmados)
	    ASN1OctetString signatureValue = sign(parameters.getSignatureAlgorithm(), privateKey, signedAttrSet);

	    return new SignerInfo(signerIdentifier, // Identificador del
	                          // certificado firmante
	                          digestAlgorithmId, // algoritmo para el hash o digest de datos
	                          signedAttrSet, // atributos firmados
	                          signAlgorithmId, // algoritmo de encriptación la firma
	                          signatureValue, // valor de la firma
	                          null); // atributos no firmados

	} catch (CertificateException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG006), e);
	} catch (IOException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG008), e);
	}

    }

    // generación del atributo firmado signingcertificate:
    /**
     * Generates a SigningCertificate attribute (version 1 or 2 depending of type digest algorithm). Definition:
     *<pre>
    SigningCertificate ::=  SEQUENCE {
           certs        SEQUENCE OF ESSCertID,
           policies     SEQUENCE OF PolicyInformation OPTIONAL
       }
     	ESSCertID ::=  SEQUENCE {
        	certHash                 Hash,
        	issuerSerial             IssuerSerial OPTIONAL
     	}

     	Hash ::= OCTET STRING -- SHA1 hash of entire certificate
     	 -------------------------
     SigningCertificateV2 ::=  SEQUENCE {
            certs        SEQUENCE OF ESSCertIDv2,
            policies     SEQUENCE OF PolicyInformation OPTIONAL
        }
    	ESSCertIDv2 ::=  SEQUENCE {
            hashAlgorithm   	AlgorithmIdentifier DEFAULT {algorithm id-sha256},
            certHash            Hash,
            issuerSerial        IssuerSerial OPTIONAL
        }

    	Hash ::= OCTET STRING
     	</pre>
     * 
     * @param cert certificate object.
     * @param digestAlgorithm digest algorithm.
     * @param digestAlgorithmId digest algorithm identification.
     * @return a SigningCertificate attribute.
     * @throws SigningException in error case.
     * @throws IOException if cetificate is wrong.
     */
    private Attribute generateSigningCertAttr(X509CertificateHolder cert, String digestAlgorithm, AlgorithmIdentifier digestAlgorithmId) throws SigningException, IOException {
	X500Name x500Name = cert.getIssuerAndSerialNumber().getName();
	DERInteger serialNumber = cert.getIssuerAndSerialNumber().getSerialNumber();

	/**
	 IssuerSerial ::= SEQUENCE {
	        issuer                   GeneralNames,
	        serialNumber             CertificateSerialNumber
	   }
	 */
	IssuerSerial issuerSerial = new IssuerSerial(new GeneralNames(new GeneralName(x500Name)), serialNumber);

	byte[ ] certHash = CryptoUtil.digest(digestAlgorithm, cert.getEncoded());

	// Comprobación si se emplea SigningCertificate ó
	// SigningCertificate-V2 (sha1 o demás sha2 respectivamente)
	if (CryptoUtil.HASH_ALGORITHM_SHA1.equals(digestAlgorithm)) {
	    // INICIO SigningCertificate
	    /**
	    	SigningCertificate ::=  SEQUENCE {
	           certs        SEQUENCE OF ESSCertID,
	           policies     SEQUENCE OF PolicyInformation OPTIONAL
	       }
	     	ESSCertID ::=  SEQUENCE {
	        	certHash                 Hash,
	        	issuerSerial             IssuerSerial OPTIONAL
	     	}

	     	Hash ::= OCTET STRING -- SHA1 hash of entire certificate
	     */

	    // creación objeto ESSCertID
	    ESSCertID essCertID = new ESSCertID(certHash, issuerSerial);
	    // creación objeto SigningCertificate
	    SigningCertificate scv = new SigningCertificate(essCertID); // SigningCertificate
	    // sin
	    // politica.

	    // Secuencia con singningCertificate
	    return new Attribute(PKCSObjectIdentifiers.id_aa_signingCertificate, new DERSet(scv));

	} else {
	    // INICIO SigningCertificateV2
	    /**
	     SigningCertificateV2 ::=  SEQUENCE {
	            certs        SEQUENCE OF ESSCertIDv2,
	            policies     SEQUENCE OF PolicyInformation OPTIONAL
	        }
	    	ESSCertIDv2 ::=  SEQUENCE {
	            hashAlgorithm   	AlgorithmIdentifier DEFAULT {algorithm id-sha256},
	            certHash            Hash,
	            issuerSerial        IssuerSerial OPTIONAL
	        }

	    	Hash ::= OCTET STRING

	     */
	    ESSCertIDv2[ ] essCertIDv2 = { new ESSCertIDv2(digestAlgorithmId, certHash, issuerSerial) };

	    SigningCertificateV2 scv2 = new SigningCertificateV2(essCertIDv2); // SigningCertificateV2
	    // sin
	    // política

	    return new Attribute(PKCSObjectIdentifiers.id_aa_signingCertificateV2, new DERSet(scv2));
	}
    }

    /**
     * Generates a new CMSData instance.
     * @param oldSignedData original signedData object.
     * @param certificates store with all signer certificates (list of  X509CertificateHolder objects).
     * @param signerInfos collection of new SignerInformation objects.
     * @return a new CMSData instance.
     * @throws SigningException in error case.
     */
    CMSSignedData generateCMSData(CMSSignedData oldSignedData, Store certificates, SignerInformationStore signerInfos) throws SigningException {
	try {
	    CMSSignedDataGenerator cmsGenerator = new CMSSignedDataGenerator();
	    cmsGenerator.addCertificates(certificates);
	    cmsGenerator.addSigners(signerInfos);
	    return cmsGenerator.generate(oldSignedData.getSignedContent(), new BouncyCastleProvider());
	} catch (CMSException e) {
	    LOGGER.error(e);
	    throw new SigningException(e);
	} catch (NoSuchAlgorithmException e) {
	    LOGGER.error(e);
	    throw new SigningException(e);
	}
    }

    /**
     * Generates a signature object.
     * @param cmsSignedData object that contains all data of original signature.
     * @param digestAlgId digest algorithm OID.
     * @param certificates list of certificates to include in the signature.
     * @param signerInfos list of signers to include in the signature.
     * @return a DER byte array of signature object (SignedData object).
     * @throws SigningException if happens a error in the
     */
    byte[ ] generateSignedData(CMSSignedData cmsSignedData, AlgorithmIdentifier digestAlgId, Store certificates, ASN1Set signerInfos) throws SigningException {
	// Obtención del contentInfo de la firma original.
	SignedData originalSignedData = SignedData.getInstance(cmsSignedData.getContentInfo().getContent());
	ASN1Set digestAlgorithms = originalSignedData.getDigestAlgorithms();
	digestAlgorithms = addElementToASN1Set(digestAlgorithms, digestAlgId.getDERObject());
	SignedData newSignedData = new SignedData(digestAlgorithms, originalSignedData.getEncapContentInfo(), convertCertStoreToASN1Set(certificates), null, signerInfos);
	return new ContentInfo(PKCSObjectIdentifiers.signedData, newSignedData).getDEREncoded();
    }

    // ///////////////////// SIGN UTILS /////////// /////////////////////

    /**
     * Converts a {@link SignerInformation} store to a set of {@link SignerInfo}.
     * @param signerInfos store with a collection of SignatureInformation objects.
     * @return a set of {@link SignerInfo} objects.
     */
    ASN1Set convertToASN1Set(SignerInformationStore signerInfos) {
	ASN1EncodableVector result = new ASN1EncodableVector();
	for (Object signerInformation: signerInfos.getSigners()) {
	    result.add(((SignerInformation) signerInformation).toASN1Structure());
	}
	return new DERSet(result);
    }

    /**
     * Converts a certificate store to a set of {@link org.bouncycastle.asn1.x509.X509CertificateStructure X509CertificateStructure}.
     * @param store store with a collection of {@link X509CertificateHolder}
     * @return a ASN1Set with a collection of certificates(X509CertificateStructure objects).
     */
    ASN1Set convertCertStoreToASN1Set(Store store) {
	ASN1EncodableVector asn1Vector = new ASN1EncodableVector();
	for (Object element: store.getMatches(null)) {
	    if (element instanceof X509CertificateHolder) {
		asn1Vector.add(((X509CertificateHolder) element).toASN1Structure());
	    }
	}
	return new DERSet(asn1Vector);
    }

    /**
     * Adds a new element to a ASN1Set list.
     * @param set list of ASN1Encodable elements.
     * @param element ASN1Encodable object to add.
     * @return a new ASN1Set with element included.
     */
    ASN1Set addElementToASN1Set(ASN1Set set, ASN1Encodable element) {
	ASN1Encodable[ ] arrayTmp = set.toArray();
	ASN1Encodable[ ] newArray = new ASN1Encodable[arrayTmp.length + 1];
	System.arraycopy(arrayTmp, 0, newArray, 0, arrayTmp.length);
	newArray[newArray.length - 1] = element;
	return new DERSet(newArray);
    }

    /**
     * Obtains the OID identifier associated to digest algorithm given.
     * @param digestAlg digest algorithm.
     * @return the OID identifier associated to digest algorithm.
     * @throws SigningException if algorithm given is wrong.
     */
    AlgorithmIdentifier makeDigestAlgorithmId(String digestAlg) throws SigningException {
	try {
	    sun.security.x509.AlgorithmId digestAlgorithmId = sun.security.x509.AlgorithmId.get(digestAlg);
	    return makeAlgId(digestAlgorithmId.getOID().toString(), digestAlgorithmId.getEncodedParams());
	} catch (NoSuchAlgorithmException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG007), e);
	} catch (IOException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.CMSB_LOG007), e);
	}
    }

    /**
     * M&eacute;todo que devuelve el Identificador del algoritmo.
     *
     * @param oid OID del algoritmo a idenfiticar
     * @param params par&aacute;metros que identifican el algoritmo en si
     * @return El identificador del algoritmo formateado y listo para introducir
     * en el cms.
     * @throws java.io.IOException in error case.
     */
    private AlgorithmIdentifier makeAlgId(String oid, byte[ ] params) throws IOException {
	if (params != null) {
	    return new AlgorithmIdentifier(new DERObjectIdentifier(oid), makeObj(params));
	}
	return new AlgorithmIdentifier(new DERObjectIdentifier(oid), new DERNull());
    }

    /**
     * Genera un objeto formateado de tipo ASN1 especial para insertarlo en el CMS.
     * Devuelve <code>null</code> si le llega una codificaci&oacute;n nula
     * @param encoding  Lo codificado
     * @return  Un objeto formateado de tipo DER
     * @throws java.io.IOException in error case.
     */
    private DERObject makeObj(byte[ ] encoding) throws IOException {
	if (encoding == null) {
	    LOGGER.warn(Language.getResIntegra(ILogConstantKeys.CMSB_LOG017));
	    return null;
	}
	return new ASN1InputStream(new ByteArrayInputStream(encoding)).readObject();
    }

    /**
     * Genera un atributo de un SET en formato DER.
     * @param attr Atributo a formatear.
     * @return SET en formato DER del atributo.
     */
    private ASN1Set getAttributeSet(AttributeTable attr) {
	if (attr != null) {
	    return new DERSet(attr.toASN1EncodableVector());
	}
	LOGGER.warn(Language.getResIntegra(ILogConstantKeys.CMSB_LOG018));
	return null;
    }

    /**
     * Genera un estructura de tipo SET de formato ASN1.
     * @param derObject a DEREncodable
     * @return  A list of ASN1 objects
     */
    private ASN1Set createBerSetFromList(DEREncodable derObject) {
	ASN1EncodableVector v = new ASN1EncodableVector();
	v.add(derObject);
	return new BERSet(v);
    }

}
