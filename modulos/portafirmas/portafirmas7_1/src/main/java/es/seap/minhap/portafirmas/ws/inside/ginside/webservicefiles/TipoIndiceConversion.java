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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para TipoIndiceConversion complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoIndiceConversion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FechaIndiceElectronico" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="DocumentoIndizado" type="{https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/conversion}TipoDocumentoIndizadoConversion"/>
 *           &lt;element name="ExpedienteIndizado" type="{https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/conversion}TipoIndiceConversion"/>
 *           &lt;element name="CarpetaIndizada" type="{https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/conversion}TipoCarpetaIndizadaConversion"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoIndiceConversion", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/expediente-e/conversion", propOrder = {
    "fechaIndiceElectronico",
    "documentoIndizadoOrExpedienteIndizadoOrCarpetaIndizada"
})
public class TipoIndiceConversion {

    @XmlElement(name = "FechaIndiceElectronico", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaIndiceElectronico;
    @XmlElements({
        @XmlElement(name = "DocumentoIndizado", type = TipoDocumentoIndizadoConversion.class),
        @XmlElement(name = "ExpedienteIndizado", type = TipoIndiceConversion.class),
        @XmlElement(name = "CarpetaIndizada", type = TipoCarpetaIndizadaConversion.class)
    })
    protected List<Object> documentoIndizadoOrExpedienteIndizadoOrCarpetaIndizada;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Obtiene el valor de la propiedad fechaIndiceElectronico.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaIndiceElectronico() {
        return fechaIndiceElectronico;
    }

    /**
     * Define el valor de la propiedad fechaIndiceElectronico.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaIndiceElectronico(XMLGregorianCalendar value) {
        this.fechaIndiceElectronico = value;
    }

    /**
     * Gets the value of the documentoIndizadoOrExpedienteIndizadoOrCarpetaIndizada property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentoIndizadoOrExpedienteIndizadoOrCarpetaIndizada property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentoIndizadoOrExpedienteIndizadoOrCarpetaIndizada().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoDocumentoIndizadoConversion }
     * {@link TipoIndiceConversion }
     * {@link TipoCarpetaIndizadaConversion }
     * 
     * 
     */
    public List<Object> getDocumentoIndizadoOrExpedienteIndizadoOrCarpetaIndizada() {
        if (documentoIndizadoOrExpedienteIndizadoOrCarpetaIndizada == null) {
            documentoIndizadoOrExpedienteIndizadoOrCarpetaIndizada = new ArrayList<Object>();
        }
        return this.documentoIndizadoOrExpedienteIndizadoOrCarpetaIndizada;
    }

    /**
     * Obtiene el valor de la propiedad id.
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
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
