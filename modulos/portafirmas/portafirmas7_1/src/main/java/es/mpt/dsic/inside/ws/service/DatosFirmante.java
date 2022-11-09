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


package es.mpt.dsic.inside.ws.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para datosFirmante complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="datosFirmante">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombreFirmante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroIdentificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mailFirmante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadCertificadora" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datosFirmante", propOrder = {
    "nombreFirmante",
    "numeroIdentificacion",
    "mailFirmante",
    "entidadCertificadora"
})
public class DatosFirmante {

    protected String nombreFirmante;
    protected String numeroIdentificacion;
    protected String mailFirmante;
    protected String entidadCertificadora;

    /**
     * Obtiene el valor de la propiedad nombreFirmante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreFirmante() {
        return nombreFirmante;
    }

    /**
     * Define el valor de la propiedad nombreFirmante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreFirmante(String value) {
        this.nombreFirmante = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroIdentificacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    /**
     * Define el valor de la propiedad numeroIdentificacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroIdentificacion(String value) {
        this.numeroIdentificacion = value;
    }

    /**
     * Obtiene el valor de la propiedad mailFirmante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailFirmante() {
        return mailFirmante;
    }

    /**
     * Define el valor de la propiedad mailFirmante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailFirmante(String value) {
        this.mailFirmante = value;
    }

    /**
     * Obtiene el valor de la propiedad entidadCertificadora.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadCertificadora() {
        return entidadCertificadora;
    }

    /**
     * Define el valor de la propiedad entidadCertificadora.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadCertificadora(String value) {
        this.entidadCertificadora = value;
    }

}
