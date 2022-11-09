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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.saml._1_0.assertion;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;



/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the oasis.names.tc.saml._1_0.assertion package. 
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

    private final static QName _Attribute_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Attribute");
    private final static QName _AttributeStatement_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "AttributeStatement");
    private final static QName _Condition_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Condition");
    private final static QName _SubjectConfirmation_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "SubjectConfirmation");
    private final static QName _AudienceRestrictionCondition_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "AudienceRestrictionCondition");
    private final static QName _AssertionIDReference_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "AssertionIDReference");
    private final static QName _Subject_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Subject");
    private final static QName _Advice_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Advice");
    private final static QName _Action_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Action");
    private final static QName _Audience_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Audience");
    private final static QName _ConfirmationMethod_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "ConfirmationMethod");
    private final static QName _SubjectConfirmationData_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "SubjectConfirmationData");
    private final static QName _SubjectStatement_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "SubjectStatement");
    private final static QName _Assertion_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Assertion");
    private final static QName _NameIdentifier_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "NameIdentifier");
    private final static QName _AuthorizationDecisionStatement_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "AuthorizationDecisionStatement");
    private final static QName _AttributeDesignator_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "AttributeDesignator");
    private final static QName _AuthorityBinding_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "AuthorityBinding");
    private final static QName _Evidence_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Evidence");
    private final static QName _SubjectLocality_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "SubjectLocality");
    private final static QName _AuthenticationStatement_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "AuthenticationStatement");
    private final static QName _Conditions_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Conditions");
    private final static QName _DoNotCacheCondition_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "DoNotCacheCondition");
    private final static QName _Statement_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "Statement");
    private final static QName _AttributeValue_QNAME = new QName("urn:oasis:names:tc:SAML:1.0:assertion", "AttributeValue");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: oasis.names.tc.saml._1_0.assertion
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConditionsType }
     * 
     */
    public ConditionsType createConditionsType() {
        return new ConditionsType();
    }

    /**
     * Create an instance of {@link DoNotCacheConditionType }
     * 
     */
    public DoNotCacheConditionType createDoNotCacheConditionType() {
        return new DoNotCacheConditionType();
    }

    /**
     * Create an instance of {@link SubjectConfirmationType }
     * 
     */
    public SubjectConfirmationType createSubjectConfirmationType() {
        return new SubjectConfirmationType();
    }

    /**
     * Create an instance of {@link ActionType }
     * 
     */
    public ActionType createActionType() {
        return new ActionType();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link AuthenticationStatementType }
     * 
     */
    public AuthenticationStatementType createAuthenticationStatementType() {
        return new AuthenticationStatementType();
    }

    /**
     * Create an instance of {@link AttributeStatementType }
     * 
     */
    public AttributeStatementType createAttributeStatementType() {
        return new AttributeStatementType();
    }

    /**
     * Create an instance of {@link AudienceRestrictionConditionType }
     * 
     */
    public AudienceRestrictionConditionType createAudienceRestrictionConditionType() {
        return new AudienceRestrictionConditionType();
    }

    /**
     * Create an instance of {@link AuthorizationDecisionStatementType }
     * 
     */
    public AuthorizationDecisionStatementType createAuthorizationDecisionStatementType() {
        return new AuthorizationDecisionStatementType();
    }

    /**
     * Create an instance of {@link AttributeDesignatorType }
     * 
     */
    public AttributeDesignatorType createAttributeDesignatorType() {
        return new AttributeDesignatorType();
    }

    /**
     * Create an instance of {@link SubjectType }
     * 
     */
    public SubjectType createSubjectType() {
        return new SubjectType();
    }

    /**
     * Create an instance of {@link SubjectLocalityType }
     * 
     */
    public SubjectLocalityType createSubjectLocalityType() {
        return new SubjectLocalityType();
    }

    /**
     * Create an instance of {@link AdviceType }
     * 
     */
    public AdviceType createAdviceType() {
        return new AdviceType();
    }

    /**
     * Create an instance of {@link AssertionType }
     * 
     */
    public AssertionType createAssertionType() {
        return new AssertionType();
    }

    /**
     * Create an instance of {@link AuthorityBindingType }
     * 
     */
    public AuthorityBindingType createAuthorityBindingType() {
        return new AuthorityBindingType();
    }

    /**
     * Create an instance of {@link NameIdentifierType }
     * 
     */
    public NameIdentifierType createNameIdentifierType() {
        return new NameIdentifierType();
    }

    /**
     * Create an instance of {@link EvidenceType }
     * 
     */
    public EvidenceType createEvidenceType() {
        return new EvidenceType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "Attribute")
    public JAXBElement<AttributeType> createAttribute(AttributeType value) {
        return new JAXBElement<AttributeType>(_Attribute_QNAME, AttributeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeStatementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "AttributeStatement")
    public JAXBElement<AttributeStatementType> createAttributeStatement(AttributeStatementType value) {
        return new JAXBElement<AttributeStatementType>(_AttributeStatement_QNAME, AttributeStatementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConditionAbstractType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "Condition")
    public JAXBElement<ConditionAbstractType> createCondition(ConditionAbstractType value) {
        return new JAXBElement<ConditionAbstractType>(_Condition_QNAME, ConditionAbstractType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubjectConfirmationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "SubjectConfirmation")
    public JAXBElement<SubjectConfirmationType> createSubjectConfirmation(SubjectConfirmationType value) {
        return new JAXBElement<SubjectConfirmationType>(_SubjectConfirmation_QNAME, SubjectConfirmationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AudienceRestrictionConditionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "AudienceRestrictionCondition")
    public JAXBElement<AudienceRestrictionConditionType> createAudienceRestrictionCondition(AudienceRestrictionConditionType value) {
        return new JAXBElement<AudienceRestrictionConditionType>(_AudienceRestrictionCondition_QNAME, AudienceRestrictionConditionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "AssertionIDReference")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAssertionIDReference(String value) {
        return new JAXBElement<String>(_AssertionIDReference_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubjectType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "Subject")
    public JAXBElement<SubjectType> createSubject(SubjectType value) {
        return new JAXBElement<SubjectType>(_Subject_QNAME, SubjectType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdviceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "Advice")
    public JAXBElement<AdviceType> createAdvice(AdviceType value) {
        return new JAXBElement<AdviceType>(_Advice_QNAME, AdviceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "Action")
    public JAXBElement<ActionType> createAction(ActionType value) {
        return new JAXBElement<ActionType>(_Action_QNAME, ActionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "Audience")
    public JAXBElement<String> createAudience(String value) {
        return new JAXBElement<String>(_Audience_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "ConfirmationMethod")
    public JAXBElement<String> createConfirmationMethod(String value) {
        return new JAXBElement<String>(_ConfirmationMethod_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "SubjectConfirmationData")
    public JAXBElement<Object> createSubjectConfirmationData(Object value) {
        return new JAXBElement<Object>(_SubjectConfirmationData_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubjectStatementAbstractType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "SubjectStatement")
    public JAXBElement<SubjectStatementAbstractType> createSubjectStatement(SubjectStatementAbstractType value) {
        return new JAXBElement<SubjectStatementAbstractType>(_SubjectStatement_QNAME, SubjectStatementAbstractType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssertionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "Assertion")
    public JAXBElement<AssertionType> createAssertion(AssertionType value) {
        return new JAXBElement<AssertionType>(_Assertion_QNAME, AssertionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NameIdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "NameIdentifier")
    public JAXBElement<NameIdentifierType> createNameIdentifier(NameIdentifierType value) {
        return new JAXBElement<NameIdentifierType>(_NameIdentifier_QNAME, NameIdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthorizationDecisionStatementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "AuthorizationDecisionStatement")
    public JAXBElement<AuthorizationDecisionStatementType> createAuthorizationDecisionStatement(AuthorizationDecisionStatementType value) {
        return new JAXBElement<AuthorizationDecisionStatementType>(_AuthorizationDecisionStatement_QNAME, AuthorizationDecisionStatementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeDesignatorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "AttributeDesignator")
    public JAXBElement<AttributeDesignatorType> createAttributeDesignator(AttributeDesignatorType value) {
        return new JAXBElement<AttributeDesignatorType>(_AttributeDesignator_QNAME, AttributeDesignatorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthorityBindingType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "AuthorityBinding")
    public JAXBElement<AuthorityBindingType> createAuthorityBinding(AuthorityBindingType value) {
        return new JAXBElement<AuthorityBindingType>(_AuthorityBinding_QNAME, AuthorityBindingType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "Evidence")
    public JAXBElement<EvidenceType> createEvidence(EvidenceType value) {
        return new JAXBElement<EvidenceType>(_Evidence_QNAME, EvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubjectLocalityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "SubjectLocality")
    public JAXBElement<SubjectLocalityType> createSubjectLocality(SubjectLocalityType value) {
        return new JAXBElement<SubjectLocalityType>(_SubjectLocality_QNAME, SubjectLocalityType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticationStatementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "AuthenticationStatement")
    public JAXBElement<AuthenticationStatementType> createAuthenticationStatement(AuthenticationStatementType value) {
        return new JAXBElement<AuthenticationStatementType>(_AuthenticationStatement_QNAME, AuthenticationStatementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConditionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "Conditions")
    public JAXBElement<ConditionsType> createConditions(ConditionsType value) {
        return new JAXBElement<ConditionsType>(_Conditions_QNAME, ConditionsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DoNotCacheConditionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "DoNotCacheCondition")
    public JAXBElement<DoNotCacheConditionType> createDoNotCacheCondition(DoNotCacheConditionType value) {
        return new JAXBElement<DoNotCacheConditionType>(_DoNotCacheCondition_QNAME, DoNotCacheConditionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatementAbstractType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "Statement")
    public JAXBElement<StatementAbstractType> createStatement(StatementAbstractType value) {
        return new JAXBElement<StatementAbstractType>(_Statement_QNAME, StatementAbstractType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:1.0:assertion", name = "AttributeValue")
    public JAXBElement<Object> createAttributeValue(Object value) {
        return new JAXBElement<Object>(_AttributeValue_QNAME, Object.class, null, value);
    }

}
