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


package es.seap.minhap.portafirmas.ws.eeutil.vis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para TipoMetadatos complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoMetadatos">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VersionNTI" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="Identificador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Organo" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="FechaCaptura" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="OrigenCiudadanoAdministracion" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="EstadoElaboracion" type="{https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos}TipoEstadoElaboracion"/>
 *         &lt;element name="TipoDocumental" type="{https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos}tipoDocumental"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoMetadatos", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos", propOrder = {
    "versionNTI",
    "identificador",
    "organo",
    "fechaCaptura",
    "origenCiudadanoAdministracion",
    "estadoElaboracion",
    "tipoDocumental"
})
public class TipoMetadatos {

    @XmlElement(name = "VersionNTI", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String versionNTI;
    @XmlElement(name = "Identificador", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos", required = true)
    protected String identificador;
    @XmlElement(name = "Organo", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos", required = true)
    protected List<String> organo;
    @XmlElement(name = "FechaCaptura", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCaptura;
    @XmlElement(name = "OrigenCiudadanoAdministracion", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos")
    protected boolean origenCiudadanoAdministracion;
    @XmlElement(name = "EstadoElaboracion", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos", required = true)
    protected TipoEstadoElaboracion estadoElaboracion;
    @XmlElement(name = "TipoDocumental", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos", required = true)
    @XmlSchemaType(name = "string")
    protected TipoDocumental tipoDocumental;

    /**
     * Obtiene el valor de la propiedad versionNTI.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionNTI() {
        return versionNTI;
    }

    /**
     * Define el valor de la propiedad versionNTI.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionNTI(String value) {
        this.versionNTI = value;
    }

    /**
     * Obtiene el valor de la propiedad identificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Define el valor de la propiedad identificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificador(String value) {
        this.identificador = value;
    }

    /**
     * Gets the value of the organo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the organo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrgano().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOrgano() {
        if (organo == null) {
            organo = new ArrayList<String>();
        }
        return this.organo;
    }

    /**
     * Obtiene el valor de la propiedad fechaCaptura.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCaptura() {
        return fechaCaptura;
    }

    /**
     * Define el valor de la propiedad fechaCaptura.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCaptura(XMLGregorianCalendar value) {
        this.fechaCaptura = value;
    }

    /**
     * Obtiene el valor de la propiedad origenCiudadanoAdministracion.
     * 
     */
    public boolean isOrigenCiudadanoAdministracion() {
        return origenCiudadanoAdministracion;
    }

    /**
     * Define el valor de la propiedad origenCiudadanoAdministracion.
     * 
     */
    public void setOrigenCiudadanoAdministracion(boolean value) {
        this.origenCiudadanoAdministracion = value;
    }

    /**
     * Obtiene el valor de la propiedad estadoElaboracion.
     * 
     * @return
     *     possible object is
     *     {@link TipoEstadoElaboracion }
     *     
     */
    public TipoEstadoElaboracion getEstadoElaboracion() {
        return estadoElaboracion;
    }

    /**
     * Define el valor de la propiedad estadoElaboracion.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoEstadoElaboracion }
     *     
     */
    public void setEstadoElaboracion(TipoEstadoElaboracion value) {
        this.estadoElaboracion = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoDocumental.
     * 
     * @return
     *     possible object is
     *     {@link TipoDocumental }
     *     
     */
    public TipoDocumental getTipoDocumental() {
        return tipoDocumental;
    }

    /**
     * Define el valor de la propiedad tipoDocumental.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoDocumental }
     *     
     */
    public void setTipoDocumental(TipoDocumental value) {
        this.tipoDocumental = value;
    }

}
