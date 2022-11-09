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


package es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client;

import java.util.ArrayList;
import java.util.List;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://afirmaws/ws/firma}idTransaccionBloque" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}bloqueOrigen"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}firmaElectronica"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}documentosSeleccionados"/>
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
    "idTransaccionBloque",
    "bloqueOrigen",
    "firmaElectronica",
    "documentosSeleccionados"
})
@XmlRootElement(name = "bloqueSeleccionado")
public class BloqueSeleccionado {

    @XmlElement(required = true)
    protected List<String> idTransaccionBloque;
    @XmlElement(required = true)
    protected BloqueOrigen bloqueOrigen;
    @XmlElement(required = true)
    protected byte[] firmaElectronica;
    @XmlElement(required = true)
    protected DocumentosSeleccionados documentosSeleccionados;

    /**
     * Gets the value of the idTransaccionBloque property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idTransaccionBloque property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdTransaccionBloque().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getIdTransaccionBloque() {
        if (idTransaccionBloque == null) {
            idTransaccionBloque = new ArrayList<String>();
        }
        return this.idTransaccionBloque;
    }

    /**
     * Gets the value of the bloqueOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link BloqueOrigen }
     *     
     */
    public BloqueOrigen getBloqueOrigen() {
        return bloqueOrigen;
    }

    /**
     * Sets the value of the bloqueOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link BloqueOrigen }
     *     
     */
    public void setBloqueOrigen(BloqueOrigen value) {
        this.bloqueOrigen = value;
    }

    /**
     * Gets the value of the firmaElectronica property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getFirmaElectronica() {
        return firmaElectronica;
    }

    /**
     * Sets the value of the firmaElectronica property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setFirmaElectronica(byte[] value) {
        this.firmaElectronica = ((byte[]) value);
    }

    /**
     * Gets the value of the documentosSeleccionados property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentosSeleccionados }
     *     
     */
    public DocumentosSeleccionados getDocumentosSeleccionados() {
        return documentosSeleccionados;
    }

    /**
     * Sets the value of the documentosSeleccionados property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentosSeleccionados }
     *     
     */
    public void setDocumentosSeleccionados(DocumentosSeleccionados value) {
        this.documentosSeleccionados = value;
    }

}
