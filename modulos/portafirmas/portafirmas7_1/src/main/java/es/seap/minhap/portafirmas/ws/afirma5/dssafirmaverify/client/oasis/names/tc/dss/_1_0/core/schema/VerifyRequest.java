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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:dss:1.0:core:schema}RequestBaseType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}SignatureObject" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "signatureObject"
})
@XmlRootElement(name = "VerifyRequest")
public class VerifyRequest
    extends RequestBaseType
{

    @XmlElement(name = "SignatureObject")
    protected SignatureObject signatureObject;

    /**
     * Gets the value of the signatureObject property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureObject }
     *     
     */
    public SignatureObject getSignatureObject() {
        return signatureObject;
    }

    /**
     * Sets the value of the signatureObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureObject }
     *     
     */
    public void setSignatureObject(SignatureObject value) {
        this.signatureObject = value;
    }

}
