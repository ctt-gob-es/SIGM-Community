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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *       &lt;sequence minOccurs="0">
 *         &lt;element ref="{http://afirmaws/ws/firma}documentosBloque" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}bloques" minOccurs="0"/>
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
    "documentosBloque",
    "bloques"
})
@XmlRootElement(name = "bloque")
public class Bloque {

    protected DocumentosBloque documentosBloque;
    protected Bloques bloques;

    /**
     * Gets the value of the documentosBloque property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentosBloque }
     *     
     */
    public DocumentosBloque getDocumentosBloque() {
        return documentosBloque;
    }

    /**
     * Sets the value of the documentosBloque property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentosBloque }
     *     
     */
    public void setDocumentosBloque(DocumentosBloque value) {
        this.documentosBloque = value;
    }

    /**
     * Gets the value of the bloques property.
     * 
     * @return
     *     possible object is
     *     {@link Bloques }
     *     
     */
    public Bloques getBloques() {
        return bloques;
    }

    /**
     * Sets the value of the bloques property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bloques }
     *     
     */
    public void setBloques(Bloques value) {
        this.bloques = value;
    }

}
