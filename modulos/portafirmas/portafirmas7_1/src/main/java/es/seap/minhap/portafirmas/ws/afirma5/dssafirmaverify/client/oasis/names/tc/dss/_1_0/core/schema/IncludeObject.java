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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="WhichDocument" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="hasObjectTagsAndAttributesSet" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="ObjId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="createReference" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "IncludeObject")
public class IncludeObject {

    @XmlAttribute(name = "WhichDocument")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object whichDocument;
    @XmlAttribute(name = "hasObjectTagsAndAttributesSet")
    protected Boolean hasObjectTagsAndAttributesSet;
    @XmlAttribute(name = "ObjId")
    protected String objId;
    @XmlAttribute(name = "createReference")
    protected Boolean createReference;

    /**
     * Gets the value of the whichDocument property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getWhichDocument() {
        return whichDocument;
    }

    /**
     * Sets the value of the whichDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setWhichDocument(Object value) {
        this.whichDocument = value;
    }

    /**
     * Gets the value of the hasObjectTagsAndAttributesSet property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isHasObjectTagsAndAttributesSet() {
        if (hasObjectTagsAndAttributesSet == null) {
            return false;
        } else {
            return hasObjectTagsAndAttributesSet;
        }
    }

    /**
     * Sets the value of the hasObjectTagsAndAttributesSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasObjectTagsAndAttributesSet(Boolean value) {
        this.hasObjectTagsAndAttributesSet = value;
    }

    /**
     * Gets the value of the objId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjId() {
        return objId;
    }

    /**
     * Sets the value of the objId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjId(String value) {
        this.objId = value;
    }

    /**
     * Gets the value of the createReference property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCreateReference() {
        if (createReference == null) {
            return true;
        } else {
            return createReference;
        }
    }

    /**
     * Sets the value of the createReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCreateReference(Boolean value) {
        this.createReference = value;
    }

}
