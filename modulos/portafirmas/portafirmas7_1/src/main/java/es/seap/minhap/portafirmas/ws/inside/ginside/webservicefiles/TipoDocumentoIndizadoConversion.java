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


package es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para TipoDocumentoIndizadoConversion complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoDocumentoIndizadoConversion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdentificadorDocumento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ValorHuella" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FuncionResumen" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FechaIncorporacionExpediente" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="OrdenDocumentoExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoDocumentoIndizadoConversion", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/conversion", propOrder = {
    "identificadorDocumento",
    "valorHuella",
    "funcionResumen",
    "fechaIncorporacionExpediente",
    "ordenDocumentoExpediente"
})
public class TipoDocumentoIndizadoConversion {

    @XmlElement(name = "IdentificadorDocumento", required = true)
    protected String identificadorDocumento;
    @XmlElement(name = "ValorHuella", required = true)
    protected String valorHuella;
    @XmlElement(name = "FuncionResumen", required = true)
    protected String funcionResumen;
    @XmlElement(name = "FechaIncorporacionExpediente")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaIncorporacionExpediente;
    @XmlElement(name = "OrdenDocumentoExpediente")
    protected String ordenDocumentoExpediente;

    /**
     * Obtiene el valor de la propiedad identificadorDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorDocumento() {
        return identificadorDocumento;
    }

    /**
     * Define el valor de la propiedad identificadorDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorDocumento(String value) {
        this.identificadorDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad valorHuella.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorHuella() {
        return valorHuella;
    }

    /**
     * Define el valor de la propiedad valorHuella.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorHuella(String value) {
        this.valorHuella = value;
    }

    /**
     * Obtiene el valor de la propiedad funcionResumen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuncionResumen() {
        return funcionResumen;
    }

    /**
     * Define el valor de la propiedad funcionResumen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuncionResumen(String value) {
        this.funcionResumen = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaIncorporacionExpediente.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaIncorporacionExpediente() {
        return fechaIncorporacionExpediente;
    }

    /**
     * Define el valor de la propiedad fechaIncorporacionExpediente.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaIncorporacionExpediente(XMLGregorianCalendar value) {
        this.fechaIncorporacionExpediente = value;
    }

    /**
     * Obtiene el valor de la propiedad ordenDocumentoExpediente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrdenDocumentoExpediente() {
        return ordenDocumentoExpediente;
    }

    /**
     * Define el valor de la propiedad ordenDocumentoExpediente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrdenDocumentoExpediente(String value) {
        this.ordenDocumentoExpediente = value;
    }

}
