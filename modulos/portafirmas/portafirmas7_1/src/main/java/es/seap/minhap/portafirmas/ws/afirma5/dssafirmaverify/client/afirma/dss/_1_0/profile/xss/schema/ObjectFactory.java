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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the afirma.dss._1_0.profile.xss.schema package. 
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

    private final static QName _ReturnSignedDataInfo_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", "ReturnSignedDataInfo");
    private final static QName _SignatureArchiveId_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", "SignatureArchiveId");
    private final static QName _ReferenceId_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", "ReferenceId");
    private final static QName _HashAlgorithm_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", "HashAlgorithm");
    private final static QName _XMLSignatureMode_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", "XMLSignatureMode");
    private final static QName _ResponseTime_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", "ResponseTime");
    private final static QName _TargetSigner_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", "TargetSigner");
    private final static QName _DocumentArchiveId_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", "DocumentArchiveId");
    private final static QName _ReturnReadableCertificateInfo_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", "ReturnReadableCertificateInfo");
    private final static QName _BatchResponse_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", "BatchResponse");
    private final static QName __0020UpdatedSignatureMode_QNAME = new QName("urn:afirma:dss:1.0:profile:XSS:schema", " UpdatedSignatureMode");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: afirma.dss._1_0.profile.xss.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AdditionalReportOption }
     * 
     */
    public AdditionalReportOption createAdditionalReportOption() {
        return new AdditionalReportOption();
    }

    /**
     * Create an instance of {@link IncludeProperties }
     * 
     */
    public IncludeProperties createIncludeProperties() {
        return new IncludeProperties();
    }

    /**
     * Create an instance of {@link IncludePropertyType }
     * 
     */
    public IncludePropertyType createIncludePropertyType() {
        return new IncludePropertyType();
    }

    /**
     * Create an instance of {@link ReadableCertificateInfo }
     * 
     */
    public ReadableCertificateInfo createReadableCertificateInfo() {
        return new ReadableCertificateInfo();
    }

    /**
     * Create an instance of {@link ReadableFieldType }
     * 
     */
    public ReadableFieldType createReadableFieldType() {
        return new ReadableFieldType();
    }

    /**
     * Create an instance of {@link ReturnSigPolicyDocument }
     * 
     */
    public ReturnSigPolicyDocument createReturnSigPolicyDocument() {
        return new ReturnSigPolicyDocument();
    }

    /**
     * Create an instance of {@link Responses }
     * 
     */
    public Responses createResponses() {
        return new Responses();
    }

    /**
     * Create an instance of {@link BatchResponseType }
     * 
     */
    public BatchResponseType createBatchResponseType() {
        return new BatchResponseType();
    }

    /**
     * Create an instance of {@link BatchRequest }
     * 
     */
    public BatchRequest createBatchRequest() {
        return new BatchRequest();
    }

    /**
     * Create an instance of {@link Requests }
     * 
     */
    public Requests createRequests() {
        return new Requests();
    }

    /**
     * Create an instance of {@link AdditionalDocumentInfo }
     * 
     */
    public AdditionalDocumentInfo createAdditionalDocumentInfo() {
        return new AdditionalDocumentInfo();
    }

    /**
     * Create an instance of {@link SigPolicyDocument }
     * 
     */
    public SigPolicyDocument createSigPolicyDocument() {
        return new SigPolicyDocument();
    }

    /**
     * Create an instance of {@link SignedDataInfo }
     * 
     */
    public SignedDataInfo createSignedDataInfo() {
        return new SignedDataInfo();
    }

    /**
     * Create an instance of {@link DataInfoType }
     * 
     */
    public DataInfoType createDataInfoType() {
        return new DataInfoType();
    }

    /**
     * Create an instance of {@link DataInfoRef }
     * 
     */
    public DataInfoRef createDataInfoRef() {
        return new DataInfoRef();
    }

    /**
     * Create an instance of {@link ArchiveIdType }
     * 
     */
    public ArchiveIdType createArchiveIdType() {
        return new ArchiveIdType();
    }

    /**
     * Create an instance of {@link SignedDataRefsType }
     * 
     */
    public SignedDataRefsType createSignedDataRefsType() {
        return new SignedDataRefsType();
    }

    /**
     * Create an instance of {@link ContentDataType }
     * 
     */
    public ContentDataType createContentDataType() {
        return new ContentDataType();
    }

    /**
     * Create an instance of {@link SignedDataRefType }
     * 
     */
    public SignedDataRefType createSignedDataRefType() {
        return new SignedDataRefType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = "ReturnSignedDataInfo")
    public JAXBElement<Object> createReturnSignedDataInfo(Object value) {
        return new JAXBElement<Object>(_ReturnSignedDataInfo_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArchiveIdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = "SignatureArchiveId")
    public JAXBElement<ArchiveIdType> createSignatureArchiveId(ArchiveIdType value) {
        return new JAXBElement<ArchiveIdType>(_SignatureArchiveId_QNAME, ArchiveIdType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = "ReferenceId")
    public JAXBElement<String> createReferenceId(String value) {
        return new JAXBElement<String>(_ReferenceId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = "HashAlgorithm")
    public JAXBElement<String> createHashAlgorithm(String value) {
        return new JAXBElement<String>(_HashAlgorithm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = "XMLSignatureMode")
    public JAXBElement<String> createXMLSignatureMode(String value) {
        return new JAXBElement<String>(_XMLSignatureMode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = "ResponseTime")
    public JAXBElement<XMLGregorianCalendar> createResponseTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ResponseTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = "TargetSigner")
    public JAXBElement<byte[]> createTargetSigner(byte[] value) {
        return new JAXBElement<byte[]>(_TargetSigner_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArchiveIdType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = "DocumentArchiveId")
    public JAXBElement<ArchiveIdType> createDocumentArchiveId(ArchiveIdType value) {
        return new JAXBElement<ArchiveIdType>(_DocumentArchiveId_QNAME, ArchiveIdType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = "ReturnReadableCertificateInfo")
    public JAXBElement<Object> createReturnReadableCertificateInfo(Object value) {
        return new JAXBElement<Object>(_ReturnReadableCertificateInfo_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BatchResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = "BatchResponse")
    public JAXBElement<BatchResponseType> createBatchResponse(BatchResponseType value) {
        return new JAXBElement<BatchResponseType>(_BatchResponse_QNAME, BatchResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:afirma:dss:1.0:profile:XSS:schema", name = " UpdatedSignatureMode")
    public JAXBElement<String> create_0020UpdatedSignatureMode(String value) {
        return new JAXBElement<String>(__0020UpdatedSignatureMode_QNAME, String.class, null, value);
    }

}
