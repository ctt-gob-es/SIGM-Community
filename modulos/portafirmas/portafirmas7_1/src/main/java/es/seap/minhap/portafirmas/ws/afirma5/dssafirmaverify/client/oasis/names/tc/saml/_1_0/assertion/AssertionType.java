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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.w3._2000._09.xmldsig.SignatureType;



/**
 * <p>Java class for AssertionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AssertionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}Conditions" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}Advice" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}Statement"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}SubjectStatement"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}AuthenticationStatement"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}AuthorizationDecisionStatement"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}AttributeStatement"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="MajorVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="MinorVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="AssertionID" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="Issuer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="IssueInstant" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssertionType", propOrder = {
    "conditions",
    "advice",
    "statementOrSubjectStatementOrAuthenticationStatement",
    "signature"
})
public class AssertionType {

    @XmlElement(name = "Conditions")
    protected ConditionsType conditions;
    @XmlElement(name = "Advice")
    protected AdviceType advice;
    @XmlElements({
        @XmlElement(name = "AuthenticationStatement", type = AuthenticationStatementType.class),
        @XmlElement(name = "Statement"),
        @XmlElement(name = "AttributeStatement", type = AttributeStatementType.class),
        @XmlElement(name = "SubjectStatement", type = SubjectStatementAbstractType.class),
        @XmlElement(name = "AuthorizationDecisionStatement", type = AuthorizationDecisionStatementType.class)
    })
    protected List<StatementAbstractType> statementOrSubjectStatementOrAuthenticationStatement;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;
    @XmlAttribute(name = "MajorVersion", required = true)
    protected BigInteger majorVersion;
    @XmlAttribute(name = "MinorVersion", required = true)
    protected BigInteger minorVersion;
    @XmlAttribute(name = "AssertionID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String assertionID;
    @XmlAttribute(name = "Issuer", required = true)
    protected String issuer;
    @XmlAttribute(name = "IssueInstant", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar issueInstant;

    /**
     * Gets the value of the conditions property.
     * 
     * @return
     *     possible object is
     *     {@link ConditionsType }
     *     
     */
    public ConditionsType getConditions() {
        return conditions;
    }

    /**
     * Sets the value of the conditions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConditionsType }
     *     
     */
    public void setConditions(ConditionsType value) {
        this.conditions = value;
    }

    /**
     * Gets the value of the advice property.
     * 
     * @return
     *     possible object is
     *     {@link AdviceType }
     *     
     */
    public AdviceType getAdvice() {
        return advice;
    }

    /**
     * Sets the value of the advice property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdviceType }
     *     
     */
    public void setAdvice(AdviceType value) {
        this.advice = value;
    }

    /**
     * Gets the value of the statementOrSubjectStatementOrAuthenticationStatement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statementOrSubjectStatementOrAuthenticationStatement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatementOrSubjectStatementOrAuthenticationStatement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AuthenticationStatementType }
     * {@link StatementAbstractType }
     * {@link AttributeStatementType }
     * {@link SubjectStatementAbstractType }
     * {@link AuthorizationDecisionStatementType }
     * 
     * 
     */
    public List<StatementAbstractType> getStatementOrSubjectStatementOrAuthenticationStatement() {
        if (statementOrSubjectStatementOrAuthenticationStatement == null) {
            statementOrSubjectStatementOrAuthenticationStatement = new ArrayList<StatementAbstractType>();
        }
        return this.statementOrSubjectStatementOrAuthenticationStatement;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    /**
     * Gets the value of the majorVersion property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMajorVersion() {
        return majorVersion;
    }

    /**
     * Sets the value of the majorVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMajorVersion(BigInteger value) {
        this.majorVersion = value;
    }

    /**
     * Gets the value of the minorVersion property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMinorVersion() {
        return minorVersion;
    }

    /**
     * Sets the value of the minorVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMinorVersion(BigInteger value) {
        this.minorVersion = value;
    }

    /**
     * Gets the value of the assertionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssertionID() {
        return assertionID;
    }

    /**
     * Sets the value of the assertionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssertionID(String value) {
        this.assertionID = value;
    }

    /**
     * Gets the value of the issuer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the value of the issuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuer(String value) {
        this.issuer = value;
    }

    /**
     * Gets the value of the issueInstant property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIssueInstant() {
        return issueInstant;
    }

    /**
     * Sets the value of the issueInstant property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIssueInstant(XMLGregorianCalendar value) {
        this.issueInstant = value;
    }

}
