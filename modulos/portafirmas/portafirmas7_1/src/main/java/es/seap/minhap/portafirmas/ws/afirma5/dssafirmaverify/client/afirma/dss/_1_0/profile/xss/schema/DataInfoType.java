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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.DocumentHash;


/**
 * <p>Java class for DataInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="ContentData" type="{urn:afirma:dss:1.0:profile:XSS:schema}ContentDataType"/>
 *         &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}DocumentHash"/>
 *         &lt;element name="SignedDataRefs" type="{urn:afirma:dss:1.0:profile:XSS:schema}SignedDataRefsType"/>
 *       &lt;/choice>
 *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataInfoType", propOrder = {
    "contentData",
    "documentHash",
    "signedDataRefs"
})
public class DataInfoType {

    @XmlElement(name = "ContentData")
    protected ContentDataType contentData;
    @XmlElement(name = "DocumentHash", namespace = "urn:oasis:names:tc:dss:1.0:core:schema")
    protected DocumentHash documentHash;
    @XmlElement(name = "SignedDataRefs")
    protected SignedDataRefsType signedDataRefs;
    @XmlAttribute(name = "ID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the contentData property.
     * 
     * @return
     *     possible object is
     *     {@link ContentDataType }
     *     
     */
    public ContentDataType getContentData() {
        return contentData;
    }

    /**
     * Sets the value of the contentData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentDataType }
     *     
     */
    public void setContentData(ContentDataType value) {
        this.contentData = value;
    }

    /**
     * Gets the value of the documentHash property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentHash }
     *     
     */
    public DocumentHash getDocumentHash() {
        return documentHash;
    }

    /**
     * Sets the value of the documentHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentHash }
     *     
     */
    public void setDocumentHash(DocumentHash value) {
        this.documentHash = value;
    }

    /**
     * Gets the value of the signedDataRefs property.
     * 
     * @return
     *     possible object is
     *     {@link SignedDataRefsType }
     *     
     */
    public SignedDataRefsType getSignedDataRefs() {
        return signedDataRefs;
    }

    /**
     * Sets the value of the signedDataRefs property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignedDataRefsType }
     *     
     */
    public void setSignedDataRefs(SignedDataRefsType value) {
        this.signedDataRefs = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

}
