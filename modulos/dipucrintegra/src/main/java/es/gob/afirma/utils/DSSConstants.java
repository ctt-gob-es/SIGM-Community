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
 * <b>File:</b><p>es.gob.afirma.utils.DSSContants.java</p>
 * <b>Description:</b><p>Class that represents constants used in the dss xml type.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @author Gobierno de España.
 * @version 07/02/2011.
 */
package es.gob.afirma.utils;

/**
 * <p>Class that represents constants used in the dss xml type.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 07/02/2011.
 */
public final class DSSConstants {

    /**
     * Constructor method for the class DSSContants.java.
     */
    private DSSConstants() {
    }

    /**
     * Attribute that represents 'dss' prefix.
     */
    public static final String PREFIX = "dss";

    /**
     * Attribute that represents the name space for oasis core.
     */
    public static final String OASIS_CORE_1_0_NS = "urn:oasis:names:tc:dss:1.0:core:schema";

    /**
     * <p>Class represents constants that contains xpaths of tag of @Firma dss services' xml request .</p>
     * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
     * certificates and electronic signature.</p>
     * @version 1.0, 17/03/2011.
     */
    public final class DSSTagsRequest {

	/**
	 * Constructor method for the class DSSContants.java.
	 */
	private DSSTagsRequest() {
	}

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:OptionalInputs</code>.
	 */
	private static final String OPTIONAL_INPUT = "dss:OptionalInputs";

	/**
	 * Constant attribute that represents the xpath for 'requestId'(a signRequest attribute).
	 */
	public static final String SIGNREQUEST_ATR_REQUEST_ID = "@RequestID";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:Document</code>.
	 */
	private static final String DOCUMENT = "dss:InputDocuments/dss:Document";

	/**
	 * Constant attribute that represents the xpath for the attribute 'id' of tag <code>dss:Document</code>.
	 */
	public static final String DOCUMENT_ATR_ID = DOCUMENT + "@ID";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:Base64Data</code>.
	 */
	public static final String BASE64DATA = DOCUMENT + "/dss:Base64Data";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:Base64XML</code>.
	 */
	public static final String BASE64XML = DOCUMENT + "/dss:Base64XML";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:DocumentHash</code>.
	 */
	public static final String DOCUMENTHASH = "dss:InputDocuments/dss:DocumentHash";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:InlineXML</code>.
	 */
	public static final String INLINEXML = DOCUMENT + "/dss:InlineXML";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:EscapedXML</code>.
	 */
	public static final String ESCAPEDXML = DOCUMENT + "/dss:EscapedXML";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:TransformedData</code>.
	 */
	public static final String TRANSFORMEDDATA = "dss:InputDocuments/dss:TransformedData";
	
	/**
	 * Constant attribute that represents the xpath for the attribute 'Algorithm' of tag <code>dss:DocumentHash/ds:DigestMethod</code>.
	 */
	public static final String DIGEST_METHOD_ATR_ALGORITHM = "dss:InputDocuments/dss:DocumentHash/ds:DigestMethod@Algorithm";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>ds:DigestMethod</code>.
	 */
	public static final String DIGEST_METHOD = "dss:InputDocuments/dss:DocumentHash/ds:DigestMethod";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>ds:DigestMethod</code>.
	 */
	public static final String DOCUMENT_HASH_TRANSFORM_ATR_ALGORITHM = "dss:InputDocuments/dss:DocumentHash/ds:Transforms/ds:Transform@Algorithm";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>ds:DigestMethod</code>.
	 */
	public static final String TRANSFORMED_DATA_BASE64DATA = TRANSFORMEDDATA + "/dss:Base64Data";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>ds:DigestMethod</code>.
	 */
	public static final String TRANSFORMED_DATA_TRANSFORM_ATR_ALGORITHM = TRANSFORMEDDATA + "/ds:Transforms/ds:Transform@Algorithm";

	/**
	 * Constant attribute that represents the xpath for the tag <code>ds:DigestValue</code>.
	 */
	public static final String DIGEST_VALUE = "dss:InputDocuments/dss:DocumentHash/ds:DigestValue";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:Other</code>.
	 */
	private static final String DOCUMENT_OTHER = "dss:InputDocuments/dss:Other";

	/**
	 * Constant attribute that represents the xpath for the tag <code>afxp:DocumentArchiveId</code>.
	 */
	public static final String DOCUMENT_ARCHIVE_ID = DOCUMENT_OTHER + "/afxp:DocumentArchiveId";

	/**
	 * Constant attribute that represents the xpath for the attribute 'id' of tag <code>afxp:DocumentArchiveId</code>.
	 */
	public static final String DOCUMENT_ARCHIVE_ID_ATR_ID = DOCUMENT_OTHER + "/afxp:DocumentArchiveId@ID";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:InputDocuments/dss:Other/cmism:getContentStream/cmism:repositoryId</code>.
	 */
	public static final String INPUTDOC_GETCONTENTSTREAM_REPOID = DOCUMENT_OTHER + "/cmism:getContentStream/cmism:repositoryId";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:InputDocuments/dss:Other/cmism:getContentStream/cmism:objectId</code>.
	 */
	public static final String INPUTDOC_GETCONTENTSTREAM_OBJECTID = DOCUMENT_OTHER + "/cmism:getContentStream/cmism:objectId";

	/**
	 * Constant attribute that represents the xpath for the tag <code>ClaimedIdentity/dss:Name</code>.
	 */
	public static final String CLAIMED_IDENTITY = OPTIONAL_INPUT + "/dss:ClaimedIdentity/dss:Name";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>ClaimedIdentity/dss:idAplicacion</code>.
	 */
	public static final String CLAIMED_IDENTITY_TSA = OPTIONAL_INPUT + "/dss:ClaimedIdentity/dss:idAplicacion";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:KeySelector/ds:KeyInfo/ds:KeyName</code>.
	 */
	public static final String KEY_SELECTOR = OPTIONAL_INPUT + "/dss:KeySelector/ds:KeyInfo/ds:KeyName";

	/**
	 * Constant attribute that represents the xpath for the tag <code>afxp:ReferenceId</code>.
	 */
	public static final String AFXP_REFERENCEID = OPTIONAL_INPUT + "/afxp:ReferenceId";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:SignatureType</code>.
	 */
	public static final String SIGNATURE_TYPE = OPTIONAL_INPUT + "/dss:SignatureType";
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:SignatureForm</code>.
	 */
	public static final String SIGNATURE_FORM = OPTIONAL_INPUT + "/ades:SignatureForm";
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:HashAlgorithm</code>.
	 */
	public static final String HASH_ALGORITHM = OPTIONAL_INPUT + "/afxp:HashAlgorithm";

	/**
	 * Constant attribute that represents the xpath for the tag <code>afxp:AdditionalDocumentInfo</code>.
	 */
	private static final String ADDITIONAL_DOCUMENT_INFO = OPTIONAL_INPUT + "/afxp:AdditionalDocumentInfo";
	/**
	 * Constant attribute that represents the xpath for the tag <code>afxp:AdditionalDocumentInfo/afxp:DocumentName</code>.
	 */
	public static final String ADDITIONAL_DOCUMENT_NAME = ADDITIONAL_DOCUMENT_INFO + "/afxp:DocumentName";
	/**
	 * Constant attribute that represents the xpath for the tag <code>afxp:AdditionalDocumentInfo/afxp:DocumentType</code>.
	 */
	public static final String ADDITIONAL_DOCUMENT_TYPE = ADDITIONAL_DOCUMENT_INFO + "/afxp:DocumentType";
	/**
	 * Constant attribute that represents the xpath for the attribute 'WhichDocument' of tag <code>afxp:AdditionalDocumentInfo</code>.
	 */
	public static final String ADDITIONAL_DOCUMENT_ATR_WHICH = ADDITIONAL_DOCUMENT_INFO + "@WhichDocument";
	/**
	 * Constant attribute that represents the xpath for the tag <code>afxp:XMLSignatureMode</code>.
	 */
	public static final String XML_SIGNATURE_MODE = OPTIONAL_INPUT + "/afxp:XMLSignatureMode";
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:IncludeEContent</code>.
	 */
	public static final String INCLUDE_E_CONTENT = OPTIONAL_INPUT + "/dss:IncludeEContent";
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:SignatureType</code>.
	 */
	public static final String SIGNATURE_POLICY_IDENTIFIER = OPTIONAL_INPUT + "/sigpol:SignaturePolicyIdentifier";
	/**
	 * Constant attribute that represents the xpath for the tag <code>afxp:IgnoreGracePeriod </code>.
	 */
	public static final String IGNORE_GRACE_PERIOD = OPTIONAL_INPUT + "/afxp:IgnoreGracePeriod";

	/**
	 * Constant attribute that represents the xpath for the tag <code>xss:ParallelSignature </code>.
	 */
	public static final String PARALLEL_SIGNATURE = OPTIONAL_INPUT + "/xss:ParallelSignature";
	/**
	 * Constant attribute that represents the xpath for the tag <code>xss:CounterSignature </code>.
	 */
	public static final String COUNTER_SIGNATURE = OPTIONAL_INPUT + "/xss:CounterSignature";
	/**
	 * Constant attribute that represents the xpath for the attribute 'WhichDocument' of tag <code>xss:CounterSignature</code>.
	 */
	public static final String COUNTER_SIGNATURE_ATR_WHICH = OPTIONAL_INPUT + "/xss:CounterSignature@WhichDocument";

	/********************	VERIFYREQUEST	********************************/

	/**
	 * Constant attribute that represents the xpath for the tag <code>vr:ReturnVerificationReport/vr:ReportOptions</code>.
	 */
	private static final String REPORT_OPTIONS = OPTIONAL_INPUT + "/vr:ReturnVerificationReport/vr:ReportOptions";

	/**
	 * Constant attribute that represents the xpath for the tag <code>vr:IncludeCertificateValues</code>.
	 */
	public static final String INCLUDE_CERTIFICATE = REPORT_OPTIONS + "/vr:IncludeCertificateValues";

	/**
	 * Constant attribute that represents the xpath for the tag <code>vr:IncludeRevocationValues</code>.
	 */
	public static final String INCLUDE_REVOCATION = REPORT_OPTIONS + "/vr:IncludeRevocationValues";

	/**
	 * Constant attribute that represents the xpath for the tag <code>vr:ReportDetailLevel</code>.
	 */
	public static final String REPORT_DETAIL_LEVEL = REPORT_OPTIONS + "/vr:ReportDetailLevel";

	/**
	 * Constant attribute that represents the xpath for the tag <code>/vr:ReturnVerificationReport/vr:CheckOptions/vr:CheckCertificateStatus</code>.
	 */
	public static final String CHECK_CERTIFICATE_STATUS = OPTIONAL_INPUT + "/vr:ReturnVerificationReport/vr:CheckOptions/vr:CheckCertificateStatus";

	/**
	 * Constant attribute that represents the xpath for the tag <code>/vr:ReturnVerificationReport/vr:CheckOptions/vr:VerifyManifest</code>.
	 */
	public static final String VERIFIY_MANIFEST = OPTIONAL_INPUT + "/vr:ReturnVerificationReport/vr:CheckOptions/vr:VerifyManifest";

	/**
	 * Constant attribute that represents the xpath for the tag <code>afxp:ReturnReadableCertificateInfo</code>.
	 */
	public static final String RETURN_READABLE_CERT_INFO = OPTIONAL_INPUT + "/afxp:ReturnReadableCertificateInfo";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:ReturnProcessingDetails</code>.
	 */
	public static final String RETURN_PROCESSING_DETAILS = OPTIONAL_INPUT + "/dss:ReturnProcessingDetails";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:ReturnUpdatedSignature</code>.
	 */
	private static final String RETURN_UPDATED_SIGNATURE = OPTIONAL_INPUT + "/dss:ReturnUpdatedSignature";

	/**
	 * Constant attribute that represents the xpath for the attribute 'Type' of tag <code>dss:ReturnUpdatedSignature</code>.
	 */
	public static final String RETURN_UPDATED_SIGNATURE_ATR_TYPE = RETURN_UPDATED_SIGNATURE + "@Type";
	/**
	 * Constant attribute that represents the xpath for the tag <code>afxp:TargetSigner</code>.
	 */
	public static final String TARGET_SIGNER = OPTIONAL_INPUT + "/afxp:TargetSigner";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:SignatureObject</code>.
	 */
	public static final String SIGNATURE_OBJECT = "dss:SignatureObject";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:SignaturePtr</code>.
	 */
	public static final String SIGNATURE_PTR = SIGNATURE_OBJECT + "/dss:SignaturePtr";

	/**
	 * Constant attribute that represents the xpath for the attribute 'WhichDocument' of tag <code>dss:SignaturePtr</code>.
	 */
	public static final String SIGNATURE_PTR_ATR_WHICH = SIGNATURE_OBJECT + "/dss:SignaturePtr@WhichDocument";

	/**
	 * Constant attribute that represents the xpath for the tag <code>ds:Signature</code>.
	 */
	public static final String SIGNATURE_DS = SIGNATURE_OBJECT + "/ds:Signature";
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:Base64Signature</code>.
	 */
	public static final String SIGNATURE_BASE64 = SIGNATURE_OBJECT + "/dss:Base64Signature";
	/**
	 * Constant attribute that represents the xpath for the attribute 'Type' of tag <code>dss:Base64Signature</code>.
	 */
	public static final String SIGNATURE_BASE64_ATR_TYPE = SIGNATURE_BASE64 + "@Type";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:SignatureObject/dss:Other/cmism:getContentStream/cmism:repositoryId</code>.
	 */
	public static final String SIGNATURE_OTHER_GETCONTENTSTREAM_REPOID = SIGNATURE_OBJECT + "/dss:Other/cmism:getContentStream/cmism:repositoryId";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:SignatureObject/dss:Other/cmism:getContentStream/cmism:objectId</code>.
	 */
	public static final String SIGNATURE_OTHER_GETCONTENTSTREAM_OBJECTID = SIGNATURE_OBJECT + "/dss:Other/cmism:getContentStream/cmism:objectId";

	/**
	 * Constant attribute that represents the xpath for the tag <code>afxp:SignatureArchiveId</code>.
	 */
	public static final String SIGNATURE_ARCHIVE_ID = SIGNATURE_OBJECT + "/dss:Other/afxp:SignatureArchiveId";
	/**
	 * Constant attribute that represents the xpath for the attribute 'ID' of tag <code>afxp:SignatureArchiveId</code>.
	 */
	public static final String SIGNATURE_ARCHIVE_ID_ATR_ID = SIGNATURE_OBJECT + "/dss:Other/afxp:SignatureArchiveId@ID";

	/**
	 * Constant attribute that represents the xpath for the attribute that returns 'SignatureTimestamp' values.
	 */
	public static final String ADDICIONAL_REPORT_OPT_SIGNATURE_TIMESTAMP = OPTIONAL_INPUT + "/afxp:AdditionalReportOption/afxp:IncludeProperties/afxp:IncludeProperty@Type";

	/**
	 * Constant attribute that represents the xpath for the tag <code>ds:X509Certificate</code>.
	 */
	public static final String X509_CERTIFICATE = SIGNATURE_OBJECT + "/dss:Other/ds:X509Data/ds:X509Certificate";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:RFC3161TimeStampToken</code>.
	 */
	public static final String TIMESTAMP_RFC3161_TIMESTAMPTOKEN = SIGNATURE_OBJECT + "/dss:Timestamp/dss:RFC3161TimeStampToken";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>ds:Signature</code>.
	 */
	public static final String TIMESTAMP_XML_TIMESTAMPTOKEN = SIGNATURE_OBJECT + "/dss:Timestamp";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>ds:Signature</code>.
	 */
	public static final String TIMESTAMP_PREVIOUS_RFC3161_TIMESTAMPTOKEN = "/dst:RenewTimestamp/dst:PreviousTimestamp/dss:Timestamp/dss:RFC3161TimeStampToken";
	
	/**
	 * Constant attribute that represents the xpath for the tag <code>ds:Signature</code>.
	 */
	public static final String TIMESTAMP_PREVIOUS_XML_TIMESTAMPTOKEN = "/dst:RenewTimestamp/dst:PreviousTimestamp/dss:Timestamp";

	/********************	DSSBATCH_VERIFY ********************************/

	/**
	 * Constant attribute that represents the xpath for the attribute 'Type' of tag <code>afxp:BatchRequest</code>.
	 */
	public static final String BATCH_REQUEST_ATTR_TYPE = "@Type";

	/**
	 * Constant attribute that represents the xpath for the tag <code>afxp:Requests</code>.
	 */
	private static final String BATCH_REQUEST = "afxp:Requests";

	/**
	 * Constant attribute that represents the xpath for the tag <code>dss:VerifyRequest</code>.
	 */
	public static final String VERIFY_REQUEST = BATCH_REQUEST + "/dss:VerifyRequest";

	/**
	 * Constant attribute that represents the xpath for the attribute 'RequestID' of tag <code>dss:VerifyRequest</code>.
	 */
	public static final String VERIFY_REQUEST_ATTR_REQUEST_ID = "@RequestID";

	/**
	 * Constant attribute that represents type used for Verify Signature Batch Request.
	 */
	public static final String BATCH_VERIFY_SIGN_TYPE = "urn:afirma:dss:1.0:profile:XSS:BatchProtocol:VerifySignatureType";

	/**
	 * Constant attribute that represents type used for Verify Certificate Batch Request.
	 */
	public static final String BATCH_VERIFY_CERT_TYPE = "urn:afirma:dss:1.0:profile:XSS:BatchProtocol:VerifyCertificateType";

	/**
	 * Constant attribute that represents xpath for the tag <code>async:ResponseID</code>.
	 */
	public static final String ASYNC_RESPONSE_ID = OPTIONAL_INPUT + "/async:ResponseID";
    }

    /**
     * <p>Class that represents signature types identificators.</p>
     * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
     * certificates and electronic signature.</p>
     * @version 1.0, 07/02/2011.
     */
    public final class SignTypesURIs {

	/**
	 * Constructor method for the class DSSContants.java.
	 */
	private SignTypesURIs() {
	}

	/** */
	/**
	 * Attribute that represents identificator for XADES signature 1.3.2. version.
	 */
	public static final String XADES_V_1_3_2 = "http://uri.etsi.org/01903/v1.3.2#";

	/**
	 * Attribute that represents identificator for XADES signature 1.2.2. version.
	 */
	public static final String XADES_V_1_2_2 = "http://uri.etsi.org/01903/v1.2.2#";

	/**
	 * Attribute that represents identificator for XADES signature 1.1.1. version.
	 */
	public static final String XADES_V_1_1_1 = "http://uri.etsi.org/01903/v1.1.1#";

	/**
	 * Attribute that represents identificator for CADES.
	 */
	public static final String CADES = "http://uri.etsi.org/01733/v1.7.3#";

	/**
	 * Attribute that represents identificator for XML_DSIG.
	 */
	public static final String XML_DSIG = "urn:ietf:rfc:3275";

	/**
	 * Attribute that represents identificator for CMS.
	 */
	public static final String CMS = "urn:ietf:rfc:3369";

	/**
	 * Attribute that represents identificator for CMS(TST).
	 */
	public static final String CMS_TST = "urn:afirma:dss:1.0:profile:XSS:forms:CMSWithTST";

	/**
	 * Attribute that represents identificator for PKCS7.
	 */
	public static final String PKCS7 = "urn:ietf:rfc:2315";

	/**
	 * Attribute that represents identificator for XML_TST.
	 */
	public static final String XML_TST = "urn:oasis:names:tc:dss:1.0:core:schema:XMLTimeStampToken";

	/**
	 * Attribute that represents identificator for ODF.
	 */
	public static final String ODF = "urn:afirma:dss:1.0:profile:XSS:forms:ODF";

	/**
	 * Attribute that represents identificator for PDF.
	 */
	public static final String PDF = "urn:afirma:dss:1.0:profile:XSS:forms:PDF";

    }
    
    /** 
     * <p>Class that defines the TimeStampToken types.</p>
     * <b>Project:</b><p>Plataforma de Integración de Servicios.</p>
     * @version 1.0, 09/01/2014.
     */
    public final class TimestampForm {
	
	/**
	 * Constructor method for internal class TimestampForm.
	 */
	private TimestampForm(){}
	
	/**
	 * Constant attribute that identifies the URI of an XML timestamp containing an XML signature.
	 */
	public static final String XML = "urn:oasis:names:tc:dss:1.0:core:schema:XMLTimeStampToken";
	
	/**
	 * Constant attribute that identifies the URI of an XML timestamp containing an ASN.1 TimeStampToken.
	 */
	public static final String RFC_3161 = "urn:ietf:rfc:3161";
    }

    /**
     * <p>Class that represents signature form identificators.</p>
     * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
     * certificates and electronic signature.</p>
     * @version 1.0, 11/02/2011.
     */
    public final class SignatureForm {

	/**
	 * Constructor method for the class DSSContants.java.
	 */
	private SignatureForm() {
	}

	/**
	 * Attribute that represents BES identificator form.
	 */
	public static final String BES = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:BES";
	/**
	 * Attribute that represents EPES identificator form.
	 */
	public static final String EPES = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:EPES";
	/**
	 * Attribute that represents T identificator form.
	 */
	public static final String T = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-T";
	/**
	 * Attribute that represents C identificator form.
	 */
	public static final String C = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-C";
	/**
	 * Attribute that represents X identificator form.
	 */
	public static final String X = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X";
	/**
	 * Attribute that represents X_1 identificator form.
	 */
	public static final String X_1 = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-1";
	/**
	 * Attribute that represents X_2 identificator form.
	 */
	public static final String X_2 = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-2";
	/**
	 * Attribute that represents X_L identificator form.
	 */
	public static final String X_L = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-L";
	/**
	 * Attribute that represents X_L_1 identificator form.
	 */
	public static final String X_L_1 = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-L-1";
	/**
	 * Attribute that represents X_L_2 identificator form.
	 */
	public static final String X_L_2 = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-L-2";
	/**
	 * Attribute that represents A identificator form.
	 */
	public static final String A = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-A";
	
	public static final String LTV ="urn:afirma:dss:1.0:profile:XSS:PAdES:1.1.2:forms:LTV";

    }

    /**
     * <p>Class that represents the xml signature modes.</p>
     * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
     * certificates and electronic signature.</p>
     * @version 1.0, 11/02/2011.
     */
    public final class XmlSignatureMode {

	/**
	 * Constructor method for the class XmlSignatureMode.java.
	 */
	private XmlSignatureMode() {
	}

	/**
	 * Attribute that represents the ENVELOPING form.
	 */
	public static final String ENVELOPING = "urn:afirma:dss:1.0:profile:XSS:XMLSignatureMode:EnvelopingMode";

	/**
	 * Attribute that represents the EVELOPED form.
	 */
	public static final String ENVELOPED = "urn:afirma:dss:1.0:profile:XSS:XMLSignatureMode:EnvelopedMode";

	/**
	 * Attribute that represents the DETACHED form.
	 */
	public static final String DETACHED = "urn:afirma:dss:1.0:profile:XSS:XMLSignatureMode:DetachedMode";

    }

    /**
     * <p>Class that represents constants for algorithm types in calls DSS services.</p>
     * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
     * certificates and electronic signature.</p>
     * @version 1.0, 17/03/2011.
     */
    public final class AlgorithmTypes {

	/**
	 * Constructor method for the class DSSContants.java.
	 */
	private AlgorithmTypes() {
	}

	/**
	 * Attribute that represents MD2 identificator of algorithm types.
	 */
	public static final String MD2 = "urn:ietf:rfc:1319";
	/**
	 * Attribute that represents MD5 identificator of algorithm types.
	 */
	public static final String MD5 = "http://www.w3.org/2001/04/xmldsig-more#md5";
	/**
	 * Attribute that represents SHA1 identificator of algorithm types.
	 */
	public static final String SHA1 = "http://www.w3.org/2000/09/xmldsig#sha1";
	/**
	 * Attribute that represents SHA256 identificator of algorithm types.
	 */
	public static final String SHA256 = "http://www.w3.org/2001/04/xmlenc#sha256";
	/**
	 * Attribute that represents SHA384 identificator of algorithm types.
	 */
	public static final String SHA384 = "http://www.w3.org/2001/04/xmldsig-more#sha384";
	/**
	 * Attribute that represents SHA512 identificator of algorithm types.
	 */
	public static final String SHA512 = "http://www.w3.org/2001/04/xmlenc#sha512";

    }

    /**
     * <p>Class that represents constants for report detail level in Dss services.</p>
     * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
     * certificates and electronic signature.</p>
     * @version 1.0, 17/03/2011.
     */
    public final class ReportDetailLevel {

	/**
	 * Constructor method for the class DSSContants.java.
	 */
	private ReportDetailLevel() {
	}

	/**
	 * Attribute that represents NO_DETAILS identificator for report detail level.
	 */
	public static final String NO_DETAILS = "urn:oasis:names:tc:dss:1.0:reportdetail:noDetails";

	/**
	 * Attribute that represents NO_PATH_DETAILS identificator for report detail level.
	 */
	public static final String NO_PATH_DETAILS = "urn:oasis:names:tc:dss:1.0:reportdetail:noPathDetails";

	/**
	 * Attribute that represents ALL_DETAILS identificator for report detail level.
	 */
	public static final String ALL_DETAILS = "urn:oasis:names:tc:dss:1.0:reportdetail:allDetails";

    }

    /**
     * <p>Class that represents constants for result process identificators in Dss services.</p>
     * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
     * certificates and electronic signature.</p>
     * @version 1.0, 17/03/2011.
     */
    public final class ResultProcessIds {

	/**
	 * Constructor method for the class ResultProcessIds.java.
	 */
	private ResultProcessIds() {
	}

	/**
	 * Attribute that represents success identificator.
	 */
	public static final String SUCESS = "urn:oasis:names:tc:dss:1.0:resultmajor:Success";

	/**
	 * Attribute that represents requester error identificator.
	 */
	public static final String REQUESTER_ERROR = "urn:oasis:names:tc:dss:1.0:resultmajor:RequesterError";

	/**
	 * Attribute that represents responder error identificator.
	 */
	public static final String RESPONDER_ERROR = "urn:oasis:names:tc:dss:1.0:resultmajor:ResponderError";

	/**
	 * Attribute that represents insufficient information identificator.
	 */
	public static final String INSUFFICIENT_INFORMATION = "urn:oasis:names:tc:dss:1.0:resultmajor:InsufficientInformation";

	/**
	 * Attribute that represents valid signature identificator.
	 */
	public static final String VALID_SIGNATURE = "urn:afirma:dss:1.0:profile:XSS:resultmajor:ValidSignature";

	/**
	 * Attribute that represents invalid signature identificator.
	 */
	public static final String INVALID_SIGNATURE = "urn:afirma:dss:1.0:profile:XSS:resultmajor:InvalidSignature";

	/**
	 * Attribute that represents warning identificator.
	 */
	public static final String WARNING = "urn:oasis:names:tc:dss:1.0:resultmajor:Warning";

	/**
	 * Attribute that represents pending identificator.
	 */
	public static final String PENDING = "urn:oasis:names:tc:dss:1.0:profiles:asynchronousprocessing:resultmajor:Pending";

    }

}
