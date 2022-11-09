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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}Document"/>
 *           &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}TransformedData"/>
 *           &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}DocumentHash"/>
 *           &lt;element name="Other" type="{urn:oasis:names:tc:dss:1.0:core:schema}AnyType"/>
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
@XmlType(name = "", propOrder = {
    "documentOrTransformedDataOrDocumentHash"
})
@XmlRootElement(name = "InputDocuments")
public class InputDocuments {

    @XmlElements({
        @XmlElement(name = "DocumentHash", type = DocumentHash.class),
        @XmlElement(name = "Document", type = DocumentType.class),
        @XmlElement(name = "TransformedData", type = TransformedData.class),
        @XmlElement(name = "Other", type = AnyType.class)
    })
    protected List<Object> documentOrTransformedDataOrDocumentHash;

    /**
     * Gets the value of the documentOrTransformedDataOrDocumentHash property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentOrTransformedDataOrDocumentHash property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentOrTransformedDataOrDocumentHash().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentHash }
     * {@link DocumentType }
     * {@link TransformedData }
     * {@link AnyType }
     * 
     * 
     */
    public List<Object> getDocumentOrTransformedDataOrDocumentHash() {
        if (documentOrTransformedDataOrDocumentHash == null) {
            documentOrTransformedDataOrDocumentHash = new ArrayList<Object>();
        }
        return this.documentOrTransformedDataOrDocumentHash;
    }

}
