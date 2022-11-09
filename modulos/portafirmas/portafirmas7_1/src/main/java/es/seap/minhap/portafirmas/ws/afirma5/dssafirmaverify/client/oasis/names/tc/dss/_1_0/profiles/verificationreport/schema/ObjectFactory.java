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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.profiles.verificationreport.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._01903.v1_3.CompleteCertificateRefsType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._01903.v1_3.CompleteRevocationRefsType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the oasis.names.tc.dss._1_0.profiles.verificationreport.schema package. 
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

    private final static QName _VerificationReport_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "VerificationReport");
    private final static QName _DetailedReport_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "DetailedReport");
    private final static QName _UnsignedSignaturePropertiesTypeSigAndRefsTimeStamp_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "SigAndRefsTimeStamp");
    private final static QName _UnsignedSignaturePropertiesTypeAttributeCertificateRefs_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "AttributeCertificateRefs");
    private final static QName _UnsignedSignaturePropertiesTypeCounterSignature_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "CounterSignature");
    private final static QName _UnsignedSignaturePropertiesTypeCompleteRevocationRefs_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "CompleteRevocationRefs");
    private final static QName _UnsignedSignaturePropertiesTypeCompleteCertificateRefs_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "CompleteCertificateRefs");
    private final static QName _UnsignedSignaturePropertiesTypeAttributeRevocationValues_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "AttributeRevocationValues");
    private final static QName _UnsignedSignaturePropertiesTypeAttributeRevocationRefs_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "AttributeRevocationRefs");
    private final static QName _UnsignedSignaturePropertiesTypeRevocationValues_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "RevocationValues");
    private final static QName _UnsignedSignaturePropertiesTypeSignatureTimeStamp_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "SignatureTimeStamp");
    private final static QName _UnsignedSignaturePropertiesTypeRefsOnlyTimeStamp_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "RefsOnlyTimeStamp");
    private final static QName _UnsignedSignaturePropertiesTypeCertificateValues_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "CertificateValues");
    private final static QName _UnsignedSignaturePropertiesTypeAttrAuthoritiesCertValues_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "AttrAuthoritiesCertValues");
    private final static QName _UnsignedSignaturePropertiesTypeArchiveTimeStamp_QNAME = new QName("urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", "ArchiveTimeStamp");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: oasis.names.tc.dss._1_0.profiles.verificationreport.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CRLContentType }
     * 
     */
    public CRLContentType createCRLContentType() {
        return new CRLContentType();
    }

    /**
     * Create an instance of {@link AttributeCertificateContentType }
     * 
     */
    public AttributeCertificateContentType createAttributeCertificateContentType() {
        return new AttributeCertificateContentType();
    }

    /**
     * Create an instance of {@link CertificateStatusType }
     * 
     */
    public CertificateStatusType createCertificateStatusType() {
        return new CertificateStatusType();
    }

    /**
     * Create an instance of {@link RevocationValuesType }
     * 
     */
    public RevocationValuesType createRevocationValuesType() {
        return new RevocationValuesType();
    }

    /**
     * Create an instance of {@link OCSPContentType }
     * 
     */
    public OCSPContentType createOCSPContentType() {
        return new OCSPContentType();
    }

    /**
     * Create an instance of {@link SingleResponseType }
     * 
     */
    public SingleResponseType createSingleResponseType() {
        return new SingleResponseType();
    }

    /**
     * Create an instance of {@link DetailedReportType }
     * 
     */
    public DetailedReportType createDetailedReportType() {
        return new DetailedReportType();
    }

    /**
     * Create an instance of {@link VerificationReportType }
     * 
     */
    public VerificationReportType createVerificationReportType() {
        return new VerificationReportType();
    }

    /**
     * Create an instance of {@link ReturnVerificationReport }
     * 
     */
    public ReturnVerificationReport createReturnVerificationReport() {
        return new ReturnVerificationReport();
    }

    /**
     * Create an instance of {@link CheckOptionsType }
     * 
     */
    public CheckOptionsType createCheckOptionsType() {
        return new CheckOptionsType();
    }

    /**
     * Create an instance of {@link ReportOptionsType }
     * 
     */
    public ReportOptionsType createReportOptionsType() {
        return new ReportOptionsType();
    }

    /**
     * Create an instance of {@link CertificatePathValidityType }
     * 
     */
    public CertificatePathValidityType createCertificatePathValidityType() {
        return new CertificatePathValidityType();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link SignedSignaturePropertiesType }
     * 
     */
    public SignedSignaturePropertiesType createSignedSignaturePropertiesType() {
        return new SignedSignaturePropertiesType();
    }

    /**
     * Create an instance of {@link SignatureValidityType }
     * 
     */
    public SignatureValidityType createSignatureValidityType() {
        return new SignatureValidityType();
    }

    /**
     * Create an instance of {@link CRLValidityType }
     * 
     */
    public CRLValidityType createCRLValidityType() {
        return new CRLValidityType();
    }

    /**
     * Create an instance of {@link SignedPropertiesType }
     * 
     */
    public SignedPropertiesType createSignedPropertiesType() {
        return new SignedPropertiesType();
    }

    /**
     * Create an instance of {@link CertifiedRolesListType }
     * 
     */
    public CertifiedRolesListType createCertifiedRolesListType() {
        return new CertifiedRolesListType();
    }

    /**
     * Create an instance of {@link SignerRoleType }
     * 
     */
    public SignerRoleType createSignerRoleType() {
        return new SignerRoleType();
    }

    /**
     * Create an instance of {@link PropertiesType }
     * 
     */
    public PropertiesType createPropertiesType() {
        return new PropertiesType();
    }

    /**
     * Create an instance of {@link CertificateValuesType }
     * 
     */
    public CertificateValuesType createCertificateValuesType() {
        return new CertificateValuesType();
    }

    /**
     * Create an instance of {@link UnsignedPropertiesType }
     * 
     */
    public UnsignedPropertiesType createUnsignedPropertiesType() {
        return new UnsignedPropertiesType();
    }

    /**
     * Create an instance of {@link CertificateValidityType }
     * 
     */
    public CertificateValidityType createCertificateValidityType() {
        return new CertificateValidityType();
    }

    /**
     * Create an instance of {@link OCSPValidityType }
     * 
     */
    public OCSPValidityType createOCSPValidityType() {
        return new OCSPValidityType();
    }

    /**
     * Create an instance of {@link AttrCertIDType }
     * 
     */
    public AttrCertIDType createAttrCertIDType() {
        return new AttrCertIDType();
    }

    /**
     * Create an instance of {@link TimeStampValidityType }
     * 
     */
    public TimeStampValidityType createTimeStampValidityType() {
        return new TimeStampValidityType();
    }

    /**
     * Create an instance of {@link UnsignedSignaturePropertiesType }
     * 
     */
    public UnsignedSignaturePropertiesType createUnsignedSignaturePropertiesType() {
        return new UnsignedSignaturePropertiesType();
    }

    /**
     * Create an instance of {@link TrustStatusListValidityType }
     * 
     */
    public TrustStatusListValidityType createTrustStatusListValidityType() {
        return new TrustStatusListValidityType();
    }

    /**
     * Create an instance of {@link CertificatePathValidityDetailType }
     * 
     */
    public CertificatePathValidityDetailType createCertificatePathValidityDetailType() {
        return new CertificatePathValidityDetailType();
    }

    /**
     * Create an instance of {@link AttributeCertificateValidityType }
     * 
     */
    public AttributeCertificateValidityType createAttributeCertificateValidityType() {
        return new AttributeCertificateValidityType();
    }

    /**
     * Create an instance of {@link SignedDataObjectPropertiesType }
     * 
     */
    public SignedDataObjectPropertiesType createSignedDataObjectPropertiesType() {
        return new SignedDataObjectPropertiesType();
    }

    /**
     * Create an instance of {@link ExtensionType }
     * 
     */
    public ExtensionType createExtensionType() {
        return new ExtensionType();
    }

    /**
     * Create an instance of {@link IndividualSignatureReportType }
     * 
     */
    public IndividualSignatureReportType createIndividualSignatureReportType() {
        return new IndividualSignatureReportType();
    }

    /**
     * Create an instance of {@link TstContentType }
     * 
     */
    public TstContentType createTstContentType() {
        return new TstContentType();
    }

    /**
     * Create an instance of {@link ExtensionsType }
     * 
     */
    public ExtensionsType createExtensionsType() {
        return new ExtensionsType();
    }

    /**
     * Create an instance of {@link EntityType }
     * 
     */
    public EntityType createEntityType() {
        return new EntityType();
    }

    /**
     * Create an instance of {@link CertificateContentType }
     * 
     */
    public CertificateContentType createCertificateContentType() {
        return new CertificateContentType();
    }

    /**
     * Create an instance of {@link ValidityType }
     * 
     */
    public ValidityType createValidityType() {
        return new ValidityType();
    }

    /**
     * Create an instance of {@link SignatureIdentifierType }
     * 
     */
    public SignatureIdentifierType createSignatureIdentifierType() {
        return new SignatureIdentifierType();
    }

    /**
     * Create an instance of {@link CRLContentType.RevokedCertificates }
     * 
     */
    public CRLContentType.RevokedCertificates createCRLContentTypeRevokedCertificates() {
        return new CRLContentType.RevokedCertificates();
    }

    /**
     * Create an instance of {@link AttributeCertificateContentType.Attributes }
     * 
     */
    public AttributeCertificateContentType.Attributes createAttributeCertificateContentTypeAttributes() {
        return new AttributeCertificateContentType.Attributes();
    }

    /**
     * Create an instance of {@link CertificateStatusType.RevocationInfo }
     * 
     */
    public CertificateStatusType.RevocationInfo createCertificateStatusTypeRevocationInfo() {
        return new CertificateStatusType.RevocationInfo();
    }

    /**
     * Create an instance of {@link CertificateStatusType.RevocationEvidence }
     * 
     */
    public CertificateStatusType.RevocationEvidence createCertificateStatusTypeRevocationEvidence() {
        return new CertificateStatusType.RevocationEvidence();
    }

    /**
     * Create an instance of {@link RevocationValuesType.CRLValues }
     * 
     */
    public RevocationValuesType.CRLValues createRevocationValuesTypeCRLValues() {
        return new RevocationValuesType.CRLValues();
    }

    /**
     * Create an instance of {@link RevocationValuesType.OCSPValues }
     * 
     */
    public RevocationValuesType.OCSPValues createRevocationValuesTypeOCSPValues() {
        return new RevocationValuesType.OCSPValues();
    }

    /**
     * Create an instance of {@link OCSPContentType.Responses }
     * 
     */
    public OCSPContentType.Responses createOCSPContentTypeResponses() {
        return new OCSPContentType.Responses();
    }

    /**
     * Create an instance of {@link SingleResponseType.CertID }
     * 
     */
    public SingleResponseType.CertID createSingleResponseTypeCertID() {
        return new SingleResponseType.CertID();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerificationReportType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "VerificationReport")
    public JAXBElement<VerificationReportType> createVerificationReport(VerificationReportType value) {
        return new JAXBElement<VerificationReportType>(_VerificationReport_QNAME, VerificationReportType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DetailedReportType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "DetailedReport")
    public JAXBElement<DetailedReportType> createDetailedReport(DetailedReportType value) {
        return new JAXBElement<DetailedReportType>(_DetailedReport_QNAME, DetailedReportType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TimeStampValidityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "SigAndRefsTimeStamp", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<TimeStampValidityType> createUnsignedSignaturePropertiesTypeSigAndRefsTimeStamp(TimeStampValidityType value) {
        return new JAXBElement<TimeStampValidityType>(_UnsignedSignaturePropertiesTypeSigAndRefsTimeStamp_QNAME, TimeStampValidityType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompleteCertificateRefsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "AttributeCertificateRefs", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<CompleteCertificateRefsType> createUnsignedSignaturePropertiesTypeAttributeCertificateRefs(CompleteCertificateRefsType value) {
        return new JAXBElement<CompleteCertificateRefsType>(_UnsignedSignaturePropertiesTypeAttributeCertificateRefs_QNAME, CompleteCertificateRefsType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureValidityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "CounterSignature", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<SignatureValidityType> createUnsignedSignaturePropertiesTypeCounterSignature(SignatureValidityType value) {
        return new JAXBElement<SignatureValidityType>(_UnsignedSignaturePropertiesTypeCounterSignature_QNAME, SignatureValidityType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompleteRevocationRefsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "CompleteRevocationRefs", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<CompleteRevocationRefsType> createUnsignedSignaturePropertiesTypeCompleteRevocationRefs(CompleteRevocationRefsType value) {
        return new JAXBElement<CompleteRevocationRefsType>(_UnsignedSignaturePropertiesTypeCompleteRevocationRefs_QNAME, CompleteRevocationRefsType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompleteCertificateRefsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "CompleteCertificateRefs", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<CompleteCertificateRefsType> createUnsignedSignaturePropertiesTypeCompleteCertificateRefs(CompleteCertificateRefsType value) {
        return new JAXBElement<CompleteCertificateRefsType>(_UnsignedSignaturePropertiesTypeCompleteCertificateRefs_QNAME, CompleteCertificateRefsType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RevocationValuesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "AttributeRevocationValues", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<RevocationValuesType> createUnsignedSignaturePropertiesTypeAttributeRevocationValues(RevocationValuesType value) {
        return new JAXBElement<RevocationValuesType>(_UnsignedSignaturePropertiesTypeAttributeRevocationValues_QNAME, RevocationValuesType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CompleteRevocationRefsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "AttributeRevocationRefs", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<CompleteRevocationRefsType> createUnsignedSignaturePropertiesTypeAttributeRevocationRefs(CompleteRevocationRefsType value) {
        return new JAXBElement<CompleteRevocationRefsType>(_UnsignedSignaturePropertiesTypeAttributeRevocationRefs_QNAME, CompleteRevocationRefsType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RevocationValuesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "RevocationValues", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<RevocationValuesType> createUnsignedSignaturePropertiesTypeRevocationValues(RevocationValuesType value) {
        return new JAXBElement<RevocationValuesType>(_UnsignedSignaturePropertiesTypeRevocationValues_QNAME, RevocationValuesType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TimeStampValidityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "SignatureTimeStamp", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<TimeStampValidityType> createUnsignedSignaturePropertiesTypeSignatureTimeStamp(TimeStampValidityType value) {
        return new JAXBElement<TimeStampValidityType>(_UnsignedSignaturePropertiesTypeSignatureTimeStamp_QNAME, TimeStampValidityType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TimeStampValidityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "RefsOnlyTimeStamp", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<TimeStampValidityType> createUnsignedSignaturePropertiesTypeRefsOnlyTimeStamp(TimeStampValidityType value) {
        return new JAXBElement<TimeStampValidityType>(_UnsignedSignaturePropertiesTypeRefsOnlyTimeStamp_QNAME, TimeStampValidityType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CertificateValuesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "CertificateValues", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<CertificateValuesType> createUnsignedSignaturePropertiesTypeCertificateValues(CertificateValuesType value) {
        return new JAXBElement<CertificateValuesType>(_UnsignedSignaturePropertiesTypeCertificateValues_QNAME, CertificateValuesType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CertificateValuesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "AttrAuthoritiesCertValues", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<CertificateValuesType> createUnsignedSignaturePropertiesTypeAttrAuthoritiesCertValues(CertificateValuesType value) {
        return new JAXBElement<CertificateValuesType>(_UnsignedSignaturePropertiesTypeAttrAuthoritiesCertValues_QNAME, CertificateValuesType.class, UnsignedSignaturePropertiesType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TimeStampValidityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#", name = "ArchiveTimeStamp", scope = UnsignedSignaturePropertiesType.class)
    public JAXBElement<TimeStampValidityType> createUnsignedSignaturePropertiesTypeArchiveTimeStamp(TimeStampValidityType value) {
        return new JAXBElement<TimeStampValidityType>(_UnsignedSignaturePropertiesTypeArchiveTimeStamp_QNAME, TimeStampValidityType.class, UnsignedSignaturePropertiesType.class, value);
    }

}
