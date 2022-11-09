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


package es.seap.minhap.portafirmas.ws.eeutil.utilfirma;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CopiaInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CopiaInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idAplicacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="csv" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fecha" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="expediente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="urlSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contenido" type="{http://service.ws.inside.dsic.mpt.es/}ContenidoInfo"/>
 *         &lt;element name="tituloAplicacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloCSV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloFecha" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloNif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estamparLogo" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="lateral" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="urlQR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="opcionesPagina" type="{http://service.ws.inside.dsic.mpt.es/}opcionesPagina" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CopiaInfo", propOrder = {
    "idAplicacion",
    "csv",
    "fecha",
    "expediente",
    "nif",
    "urlSede",
    "contenido",
    "tituloAplicacion",
    "tituloCSV",
    "tituloFecha",
    "tituloExpediente",
    "tituloNif",
    "tituloURL",
    "estamparLogo",
    "lateral",
    "urlQR",
    "opcionesPagina"
})
public class CopiaInfo {

    @XmlElement(required = true)
    protected String idAplicacion;
    @XmlElement(required = true)
    protected String csv;
    @XmlElement(required = true)
    protected String fecha;
    @XmlElement(required = true)
    protected String expediente;
    protected String nif;
    @XmlElement(defaultValue = "https://sede.administracionespublicas.gob.es/valida")
    protected String urlSede;
    @XmlElement(required = true)
    protected ContenidoInfo contenido;
    protected String tituloAplicacion;
    protected String tituloCSV;
    protected String tituloFecha;
    protected String tituloExpediente;
    protected String tituloNif;
    protected String tituloURL;
    protected boolean estamparLogo;
    protected String lateral;
    protected String urlQR;
    protected OpcionesPagina opcionesPagina;

    /**
     * Gets the value of the idAplicacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAplicacion() {
        return idAplicacion;
    }

    /**
     * Sets the value of the idAplicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAplicacion(String value) {
        this.idAplicacion = value;
    }

    /**
     * Gets the value of the csv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsv() {
        return csv;
    }

    /**
     * Sets the value of the csv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsv(String value) {
        this.csv = value;
    }

    /**
     * Gets the value of the fecha property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Sets the value of the fecha property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFecha(String value) {
        this.fecha = value;
    }

    /**
     * Gets the value of the expediente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpediente() {
        return expediente;
    }

    /**
     * Sets the value of the expediente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpediente(String value) {
        this.expediente = value;
    }

    /**
     * Gets the value of the nif property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNif() {
        return nif;
    }

    /**
     * Sets the value of the nif property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNif(String value) {
        this.nif = value;
    }

    /**
     * Gets the value of the urlSede property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlSede() {
        return urlSede;
    }

    /**
     * Sets the value of the urlSede property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlSede(String value) {
        this.urlSede = value;
    }

    /**
     * Gets the value of the contenido property.
     * 
     * @return
     *     possible object is
     *     {@link ContenidoInfo }
     *     
     */
    public ContenidoInfo getContenido() {
        return contenido;
    }

    /**
     * Sets the value of the contenido property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContenidoInfo }
     *     
     */
    public void setContenido(ContenidoInfo value) {
        this.contenido = value;
    }

    /**
     * Gets the value of the tituloAplicacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTituloAplicacion() {
        return tituloAplicacion;
    }

    /**
     * Sets the value of the tituloAplicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTituloAplicacion(String value) {
        this.tituloAplicacion = value;
    }

    /**
     * Gets the value of the tituloCSV property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTituloCSV() {
        return tituloCSV;
    }

    /**
     * Sets the value of the tituloCSV property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTituloCSV(String value) {
        this.tituloCSV = value;
    }

    /**
     * Gets the value of the tituloFecha property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTituloFecha() {
        return tituloFecha;
    }

    /**
     * Sets the value of the tituloFecha property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTituloFecha(String value) {
        this.tituloFecha = value;
    }

    /**
     * Gets the value of the tituloExpediente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTituloExpediente() {
        return tituloExpediente;
    }

    /**
     * Sets the value of the tituloExpediente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTituloExpediente(String value) {
        this.tituloExpediente = value;
    }

    /**
     * Gets the value of the tituloNif property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTituloNif() {
        return tituloNif;
    }

    /**
     * Sets the value of the tituloNif property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTituloNif(String value) {
        this.tituloNif = value;
    }

    /**
     * Gets the value of the tituloURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTituloURL() {
        return tituloURL;
    }

    /**
     * Sets the value of the tituloURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTituloURL(String value) {
        this.tituloURL = value;
    }

    /**
     * Gets the value of the estamparLogo property.
     * 
     */
    public boolean isEstamparLogo() {
        return estamparLogo;
    }

    /**
     * Sets the value of the estamparLogo property.
     * 
     */
    public void setEstamparLogo(boolean value) {
        this.estamparLogo = value;
    }

    /**
     * Gets the value of the lateral property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLateral() {
        return lateral;
    }

    /**
     * Sets the value of the lateral property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLateral(String value) {
        this.lateral = value;
    }

    /**
     * Gets the value of the urlQR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlQR() {
        return urlQR;
    }

    /**
     * Sets the value of the urlQR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlQR(String value) {
        this.urlQR = value;
    }

    /**
     * Gets the value of the opcionesPagina property.
     * 
     * @return
     *     possible object is
     *     {@link OpcionesPagina }
     *     
     */
    public OpcionesPagina getOpcionesPagina() {
        return opcionesPagina;
    }

    /**
     * Sets the value of the opcionesPagina property.
     * 
     * @param value
     *     allowed object is
     *     {@link OpcionesPagina }
     *     
     */
    public void setOpcionesPagina(OpcionesPagina value) {
        this.opcionesPagina = value;
    }

}
