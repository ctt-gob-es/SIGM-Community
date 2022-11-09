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


package es.seap.minhap.portafirmas.ws.eeutil.operfirma;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para infoCertificado complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="infoCertificado">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="validado" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="idUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroSerie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="detalleValidacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="informacionDetallada" type="{http://service.ws.inside.dsic.mpt.es/}InfoDetalladaCertificado" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "infoCertificado", propOrder = {
    "validado",
    "idUsuario",
    "numeroSerie",
    "detalleValidacion",
    "informacionDetallada"
})
public class InfoCertificado {

    protected boolean validado;
    protected String idUsuario;
    protected String numeroSerie;
    @XmlElement(required = true)
    protected String detalleValidacion;
    @XmlElement(required = true)
    protected List<InfoDetalladaCertificado> informacionDetallada;

    /**
     * Obtiene el valor de la propiedad validado.
     * 
     */
    public boolean isValidado() {
        return validado;
    }

    /**
     * Define el valor de la propiedad validado.
     * 
     */
    public void setValidado(boolean value) {
        this.validado = value;
    }

    /**
     * Obtiene el valor de la propiedad idUsuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * Define el valor de la propiedad idUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUsuario(String value) {
        this.idUsuario = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroSerie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroSerie() {
        return numeroSerie;
    }

    /**
     * Define el valor de la propiedad numeroSerie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroSerie(String value) {
        this.numeroSerie = value;
    }

    /**
     * Obtiene el valor de la propiedad detalleValidacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetalleValidacion() {
        return detalleValidacion;
    }

    /**
     * Define el valor de la propiedad detalleValidacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetalleValidacion(String value) {
        this.detalleValidacion = value;
    }

    /**
     * Gets the value of the informacionDetallada property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the informacionDetallada property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInformacionDetallada().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InfoDetalladaCertificado }
     * 
     * 
     */
    public List<InfoDetalladaCertificado> getInformacionDetallada() {
        if (informacionDetallada == null) {
            informacionDetallada = new ArrayList<InfoDetalladaCertificado>();
        }
        return this.informacionDetallada;
    }

}
