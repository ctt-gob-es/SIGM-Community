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
 *         &lt;element ref="{http://afirmaws/ws/firma}idDocumentos"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}idDocumentosMultifirmados"/>
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
    "idDocumentos",
    "idDocumentosMultifirmados"
})
@XmlRootElement(name = "idDocumentosBloque")
public class IdDocumentosBloque {

    @XmlElement(required = true)
    protected IdDocumentos idDocumentos;
    @XmlElement(required = true)
    protected IdDocumentosMultifirmados idDocumentosMultifirmados;

    /**
     * Gets the value of the idDocumentos property.
     * 
     * @return
     *     possible object is
     *     {@link IdDocumentos }
     *     
     */
    public IdDocumentos getIdDocumentos() {
        return idDocumentos;
    }

    /**
     * Sets the value of the idDocumentos property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdDocumentos }
     *     
     */
    public void setIdDocumentos(IdDocumentos value) {
        this.idDocumentos = value;
    }

    /**
     * Gets the value of the idDocumentosMultifirmados property.
     * 
     * @return
     *     possible object is
     *     {@link IdDocumentosMultifirmados }
     *     
     */
    public IdDocumentosMultifirmados getIdDocumentosMultifirmados() {
        return idDocumentosMultifirmados;
    }

    /**
     * Sets the value of the idDocumentosMultifirmados property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdDocumentosMultifirmados }
     *     
     */
    public void setIdDocumentosMultifirmados(IdDocumentosMultifirmados value) {
        this.idDocumentosMultifirmados = value;
    }

}
