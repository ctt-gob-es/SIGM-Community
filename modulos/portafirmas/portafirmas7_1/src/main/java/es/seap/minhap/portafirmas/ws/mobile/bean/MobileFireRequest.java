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


package es.seap.minhap.portafirmas.ws.mobile.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mobileFireRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mobileFireRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asunto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documentos" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileFireDocumentList" minOccurs="0"/>
 *         &lt;element name="errorPeticion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remitters" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileFireRequest", propOrder = {
    "id",
    "asunto",
    "documentos",
    "errorPeticion"
})
public class MobileFireRequest {

    protected String id;
    protected String asunto;
    protected MobileFireDocumentList documentos;
    protected String errorPeticion;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
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
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the asunto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Sets the value of the asunto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsunto(String value) {
        this.asunto = value;
    }

    /**
     * Gets the value of the documentos property.
     * 
     * @return
     *     possible object is
     *     {@link MobileFireDocumentList }
     *     
     */
    public MobileFireDocumentList getDocumentos() {
        return documentos;
    }

    /**
     * Sets the value of the documentos property.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileFireDocumentList }
     *     
     */
    public void setDocumentos(MobileFireDocumentList value) {
        this.documentos = value;
    }

    /**
     * Gets the value of the errorPeticion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorPeticion() {
        return errorPeticion;
    }

    /**
     * Sets the value of the errorPeticion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorPeticion(String value) {
        this.errorPeticion = value;
    }

}
