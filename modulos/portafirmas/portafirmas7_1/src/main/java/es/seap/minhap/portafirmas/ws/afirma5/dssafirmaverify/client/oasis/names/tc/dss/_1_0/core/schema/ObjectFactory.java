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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.saml._1_0.assertion.NameIdentifierType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the oasis.names.tc.dss._1_0.core.schema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ReturnVerificationTimeInfo_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "ReturnVerificationTimeInfo");
    private final static QName _ReturnSigningTimeInfo_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "ReturnSigningTimeInfo");
    private final static QName _UseVerificationTime_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "UseVerificationTime");
    private final static QName _SignatureType_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "SignatureType");
    private final static QName _VerifyResponse_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "VerifyResponse");
    private final static QName _Schemas_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "Schemas");
    private final static QName _AdditionalProfile_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "AdditionalProfile");
    private final static QName _AddTimestamp_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "AddTimestamp");
    private final static QName _ReturnProcessingDetails_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "ReturnProcessingDetails");
    private final static QName _SigningTimeInfo_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "SigningTimeInfo");
    private final static QName _Schema_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "Schema");
    private final static QName _ReturnSignerIdentity_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "ReturnSignerIdentity");
    private final static QName _Response_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "Response");
    private final static QName _Language_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "Language");
    private final static QName _ReturnTimestampedSignature_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "ReturnTimestampedSignature");
    private final static QName _AttachmentReference_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "AttachmentReference");
    private final static QName _AdditionalTimeInfo_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "AdditionalTimeInfo");
    private final static QName _ServicePolicy_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "ServicePolicy");
    private final static QName _SignerIdentity_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "SignerIdentity");
    private final static QName _UpdatedSignature_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "UpdatedSignature");
    private final static QName _OptionalInputs_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "OptionalInputs");
    private final static QName _TimestampedSignature_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "TimestampedSignature");
    private final static QName _VerifyManifestResults_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "VerifyManifestResults");
    private final static QName _VerificationTimeInfo_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "VerificationTimeInfo");
    private final static QName _Document_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "Document");
    private final static QName _OptionalOutputs_QNAME = new QName("urn:oasis:names:tc:dss:1.0:core:schema", "OptionalOutputs");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: oasis.names.tc.dss._1_0.core.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SigningTimeInfoType }
     * 
     */
    public SigningTimeInfoType createSigningTimeInfoType() {
        return new SigningTimeInfoType();
    }

    /**
     * Create an instance of {@link UpdateSignatureInstructionType }
     * 
     */
    public UpdateSignatureInstructionType createUpdateSignatureInstructionType() {
        return new UpdateSignatureInstructionType();
    }

    /**
     * Create an instance of {@link SignRequest }
     * 
     */
    public SignRequest createSignRequest() {
        return new SignRequest();
    }

    /**
     * Create an instance of {@link RequestBaseType }
     * 
     */
    public RequestBaseType createRequestBaseType() {
        return new RequestBaseType();
    }

    /**
     * Create an instance of {@link AnyType }
     * 
     */
    public AnyType createAnyType() {
        return new AnyType();
    }

    /**
     * Create an instance of {@link InputDocuments }
     * 
     */
    public InputDocuments createInputDocuments() {
        return new InputDocuments();
    }

    /**
     * Create an instance of {@link DocumentType }
     * 
     */
    public DocumentType createDocumentType() {
        return new DocumentType();
    }

    /**
     * Create an instance of {@link TransformedData }
     * 
     */
    public TransformedData createTransformedData() {
        return new TransformedData();
    }

    /**
     * Create an instance of {@link Base64Data }
     * 
     */
    public Base64Data createBase64Data() {
        return new Base64Data();
    }

    /**
     * Create an instance of {@link DocumentHash }
     * 
     */
    public DocumentHash createDocumentHash() {
        return new DocumentHash();
    }

    /**
     * Create an instance of {@link AdditionalKeyInfo }
     * 
     */
    public AdditionalKeyInfo createAdditionalKeyInfo() {
        return new AdditionalKeyInfo();
    }

    /**
     * Create an instance of {@link AdditionalTimeInfoType }
     * 
     */
    public AdditionalTimeInfoType createAdditionalTimeInfoType() {
        return new AdditionalTimeInfoType();
    }

    /**
     * Create an instance of {@link AttachmentReferenceType }
     * 
     */
    public AttachmentReferenceType createAttachmentReferenceType() {
        return new AttachmentReferenceType();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link KeySelector }
     * 
     */
    public KeySelector createKeySelector() {
        return new KeySelector();
    }

    /**
     * Create an instance of {@link SignaturePlacement }
     * 
     */
    public SignaturePlacement createSignaturePlacement() {
        return new SignaturePlacement();
    }

    /**
     * Create an instance of {@link SignedReference }
     * 
     */
    public SignedReference createSignedReference() {
        return new SignedReference();
    }

    /**
     * Create an instance of {@link Result }
     * 
     */
    public Result createResult() {
        return new Result();
    }

    /**
     * Create an instance of {@link InternationalStringType }
     * 
     */
    public InternationalStringType createInternationalStringType() {
        return new InternationalStringType();
    }

    /**
     * Create an instance of {@link RequesterIdentity }
     * 
     */
    public RequesterIdentity createRequesterIdentity() {
        return new RequesterIdentity();
    }

    /**
     * Create an instance of {@link TstInfo }
     * 
     */
    public TstInfo createTstInfo() {
        return new TstInfo();
    }

    /**
     * Create an instance of {@link IncludeObject }
     * 
     */
    public IncludeObject createIncludeObject() {
        return new IncludeObject();
    }

    /**
     * Create an instance of {@link DocumentWithSignature }
     * 
     */
    public DocumentWithSignature createDocumentWithSignature() {
        return new DocumentWithSignature();
    }

    /**
     * Create an instance of {@link UpdatedSignatureType }
     * 
     */
    public UpdatedSignatureType createUpdatedSignatureType() {
        return new UpdatedSignatureType();
    }

    /**
     * Create an instance of {@link ManifestResult }
     * 
     */
    public ManifestResult createManifestResult() {
        return new ManifestResult();
    }

    /**
     * Create an instance of {@link SchemasType }
     * 
     */
    public SchemasType createSchemasType() {
        return new SchemasType();
    }

    /**
     * Create an instance of {@link ResponseBaseType }
     * 
     */
    public ResponseBaseType createResponseBaseType() {
        return new ResponseBaseType();
    }

    /**
     * Create an instance of {@link IntendedAudience }
     * 
     */
    public IntendedAudience createIntendedAudience() {
        return new IntendedAudience();
    }

    /**
     * Create an instance of {@link ReturnUpdatedSignature }
     * 
     */
    public ReturnUpdatedSignature createReturnUpdatedSignature() {
        return new ReturnUpdatedSignature();
    }

    /**
     * Create an instance of {@link SignResponse }
     * 
     */
    public SignResponse createSignResponse() {
        return new SignResponse();
    }

    /**
     * Create an instance of {@link SignatureObject }
     * 
     */
    public SignatureObject createSignatureObject() {
        return new SignatureObject();
    }

    /**
     * Create an instance of {@link Timestamp }
     * 
     */
    public Timestamp createTimestamp() {
        return new Timestamp();
    }

    /**
     * Create an instance of {@link Base64Signature }
     * 
     */
    public Base64Signature createBase64Signature() {
        return new Base64Signature();
    }

    /**
     * Create an instance of {@link SignaturePtr }
     * 
     */
    public SignaturePtr createSignaturePtr() {
        return new SignaturePtr();
    }

    /**
     * Create an instance of {@link ReturnTransformedDocument }
     * 
     */
    public ReturnTransformedDocument createReturnTransformedDocument() {
        return new ReturnTransformedDocument();
    }

    /**
     * Create an instance of {@link Properties }
     * 
     */
    public Properties createProperties() {
        return new Properties();
    }

    /**
     * Create an instance of {@link PropertiesType }
     * 
     */
    public PropertiesType createPropertiesType() {
        return new PropertiesType();
    }

    /**
     * Create an instance of {@link VerifyManifestResultsType }
     * 
     */
    public VerifyManifestResultsType createVerifyManifestResultsType() {
        return new VerifyManifestResultsType();
    }

    /**
     * Create an instance of {@link ProcessingDetails }
     * 
     */
    public ProcessingDetails createProcessingDetails() {
        return new ProcessingDetails();
    }

    /**
     * Create an instance of {@link DetailType }
     * 
     */
    public DetailType createDetailType() {
        return new DetailType();
    }

    /**
     * Create an instance of {@link VerificationTimeInfoType }
     * 
     */
    public VerificationTimeInfoType createVerificationTimeInfoType() {
        return new VerificationTimeInfoType();
    }

    /**
     * Create an instance of {@link VerifyRequest }
     * 
     */
    public VerifyRequest createVerifyRequest() {
        return new VerifyRequest();
    }

    /**
     * Create an instance of {@link TransformedDocument }
     * 
     */
    public TransformedDocument createTransformedDocument() {
        return new TransformedDocument();
    }

    /**
     * Create an instance of {@link ClaimedIdentity }
     * 
     */
    public ClaimedIdentity createClaimedIdentity() {
        return new ClaimedIdentity();
    }

    /**
     * Create an instance of {@link SignedReferences }
     * 
     */
    public SignedReferences createSignedReferences() {
        return new SignedReferences();
    }

    /**
     * Create an instance of {@link InlineXMLType }
     * 
     */
    public InlineXMLType createInlineXMLType() {
        return new InlineXMLType();
    }

    /**
     * Create an instance of {@link UseVerificationTimeType }
     * 
     */
    public UseVerificationTimeType createUseVerificationTimeType() {
        return new UseVerificationTimeType();
    }

    /**
     * Create an instance of {@link TimeSignatureInstructionType }
     * 
     */
    public TimeSignatureInstructionType createTimeSignatureInstructionType() {
        return new TimeSignatureInstructionType();
    }

    /**
     * Create an instance of {@link SigningTimeInfoType.SigningTimeBoundaries }
     * 
     */
    public SigningTimeInfoType.SigningTimeBoundaries createSigningTimeInfoTypeSigningTimeBoundaries() {
        return new SigningTimeInfoType.SigningTimeBoundaries();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "ReturnVerificationTimeInfo")
    public JAXBElement<Object> createReturnVerificationTimeInfo(Object value) {
        return new JAXBElement<Object>(_ReturnVerificationTimeInfo_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "ReturnSigningTimeInfo")
    public JAXBElement<Object> createReturnSigningTimeInfo(Object value) {
        return new JAXBElement<Object>(_ReturnSigningTimeInfo_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "UseVerificationTime")
    public JAXBElement<Object> createUseVerificationTime(Object value) {
        return new JAXBElement<Object>(_UseVerificationTime_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "SignatureType")
    public JAXBElement<String> createSignatureType(String value) {
        return new JAXBElement<String>(_SignatureType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResponseBaseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "VerifyResponse")
    public JAXBElement<ResponseBaseType> createVerifyResponse(ResponseBaseType value) {
        return new JAXBElement<ResponseBaseType>(_VerifyResponse_QNAME, ResponseBaseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SchemasType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "Schemas")
    public JAXBElement<SchemasType> createSchemas(SchemasType value) {
        return new JAXBElement<SchemasType>(_Schemas_QNAME, SchemasType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "AdditionalProfile")
    public JAXBElement<String> createAdditionalProfile(String value) {
        return new JAXBElement<String>(_AdditionalProfile_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSignatureInstructionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "AddTimestamp")
    public JAXBElement<UpdateSignatureInstructionType> createAddTimestamp(UpdateSignatureInstructionType value) {
        return new JAXBElement<UpdateSignatureInstructionType>(_AddTimestamp_QNAME, UpdateSignatureInstructionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "ReturnProcessingDetails")
    public JAXBElement<Object> createReturnProcessingDetails(Object value) {
        return new JAXBElement<Object>(_ReturnProcessingDetails_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SigningTimeInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "SigningTimeInfo")
    public JAXBElement<SigningTimeInfoType> createSigningTimeInfo(SigningTimeInfoType value) {
        return new JAXBElement<SigningTimeInfoType>(_SigningTimeInfo_QNAME, SigningTimeInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "Schema")
    public JAXBElement<DocumentType> createSchema(DocumentType value) {
        return new JAXBElement<DocumentType>(_Schema_QNAME, DocumentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "ReturnSignerIdentity")
    public JAXBElement<Object> createReturnSignerIdentity(Object value) {
        return new JAXBElement<Object>(_ReturnSignerIdentity_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResponseBaseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "Response")
    public JAXBElement<ResponseBaseType> createResponse(ResponseBaseType value) {
        return new JAXBElement<ResponseBaseType>(_Response_QNAME, ResponseBaseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "Language")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createLanguage(String value) {
        return new JAXBElement<String>(_Language_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateSignatureInstructionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "ReturnTimestampedSignature")
    public JAXBElement<UpdateSignatureInstructionType> createReturnTimestampedSignature(UpdateSignatureInstructionType value) {
        return new JAXBElement<UpdateSignatureInstructionType>(_ReturnTimestampedSignature_QNAME, UpdateSignatureInstructionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttachmentReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "AttachmentReference")
    public JAXBElement<AttachmentReferenceType> createAttachmentReference(AttachmentReferenceType value) {
        return new JAXBElement<AttachmentReferenceType>(_AttachmentReference_QNAME, AttachmentReferenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdditionalTimeInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "AdditionalTimeInfo")
    public JAXBElement<AdditionalTimeInfoType> createAdditionalTimeInfo(AdditionalTimeInfoType value) {
        return new JAXBElement<AdditionalTimeInfoType>(_AdditionalTimeInfo_QNAME, AdditionalTimeInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "ServicePolicy")
    public JAXBElement<String> createServicePolicy(String value) {
        return new JAXBElement<String>(_ServicePolicy_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NameIdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "SignerIdentity")
    public JAXBElement<NameIdentifierType> createSignerIdentity(NameIdentifierType value) {
        return new JAXBElement<NameIdentifierType>(_SignerIdentity_QNAME, NameIdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdatedSignatureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "UpdatedSignature")
    public JAXBElement<UpdatedSignatureType> createUpdatedSignature(UpdatedSignatureType value) {
        return new JAXBElement<UpdatedSignatureType>(_UpdatedSignature_QNAME, UpdatedSignatureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "OptionalInputs")
    public JAXBElement<AnyType> createOptionalInputs(AnyType value) {
        return new JAXBElement<AnyType>(_OptionalInputs_QNAME, AnyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdatedSignatureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "TimestampedSignature")
    public JAXBElement<UpdatedSignatureType> createTimestampedSignature(UpdatedSignatureType value) {
        return new JAXBElement<UpdatedSignatureType>(_TimestampedSignature_QNAME, UpdatedSignatureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyManifestResultsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "VerifyManifestResults")
    public JAXBElement<VerifyManifestResultsType> createVerifyManifestResults(VerifyManifestResultsType value) {
        return new JAXBElement<VerifyManifestResultsType>(_VerifyManifestResults_QNAME, VerifyManifestResultsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerificationTimeInfoType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "VerificationTimeInfo")
    public JAXBElement<VerificationTimeInfoType> createVerificationTimeInfo(VerificationTimeInfoType value) {
        return new JAXBElement<VerificationTimeInfoType>(_VerificationTimeInfo_QNAME, VerificationTimeInfoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "Document")
    public JAXBElement<DocumentType> createDocument(DocumentType value) {
        return new JAXBElement<DocumentType>(_Document_QNAME, DocumentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:core:schema", name = "OptionalOutputs")
    public JAXBElement<AnyType> createOptionalOutputs(AnyType value) {
        return new JAXBElement<AnyType>(_OptionalOutputs_QNAME, AnyType.class, null, value);
    }

}
