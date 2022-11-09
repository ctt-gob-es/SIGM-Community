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


package es.seap.minhap.portafirmas.ws.csvstorage.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Clase Java para PGPDataType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="PGPDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="PGPKeyID" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *           &lt;element name="PGPKeyPacket" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *           &lt;any processContents='lax' namespace='##other'/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PGPDataType", namespace = "http://www.w3.org/2000/09/xmldsig#", propOrder = {
    "pgpKeyIDOrPGPKeyPacketOrAny"
})
public class PGPDataType {

    @XmlElementRefs({
        @XmlElementRef(name = "PGPKeyPacket", namespace = "http://www.w3.org/2000/09/xmldsig#", type = JAXBElement.class),
        @XmlElementRef(name = "PGPKeyID", namespace = "http://www.w3.org/2000/09/xmldsig#", type = JAXBElement.class)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> pgpKeyIDOrPGPKeyPacketOrAny;

    /**
     * Gets the value of the pgpKeyIDOrPGPKeyPacketOrAny property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pgpKeyIDOrPGPKeyPacketOrAny property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPGPKeyIDOrPGPKeyPacketOrAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * {@link Element }
     * {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     * {@link Object }
     * 
     * 
     */
    public List<Object> getPGPKeyIDOrPGPKeyPacketOrAny() {
        if (pgpKeyIDOrPGPKeyPacketOrAny == null) {
            pgpKeyIDOrPGPKeyPacketOrAny = new ArrayList<Object>();
        }
        return this.pgpKeyIDOrPGPKeyPacketOrAny;
    }

}
