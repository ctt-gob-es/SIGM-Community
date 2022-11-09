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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para TipoOpcionesVisualizacionDocumento complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoOpcionesVisualizacionDocumento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EstamparImagen" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="EstamparNombreOrganismo" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="EstamparPie" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="TextoPie" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FilasNombreOrganismo" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Fila" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoOpcionesVisualizacionDocumento", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/visualizacion/documento-e", propOrder = {
    "estamparImagen",
    "estamparNombreOrganismo",
    "estamparPie",
    "textoPie",
    "filasNombreOrganismo"
})
public class TipoOpcionesVisualizacionDocumento {

    @XmlElement(name = "EstamparImagen")
    protected boolean estamparImagen;
    @XmlElement(name = "EstamparNombreOrganismo")
    protected boolean estamparNombreOrganismo;
    @XmlElement(name = "EstamparPie")
    protected boolean estamparPie;
    @XmlElement(name = "TextoPie", required = true)
    protected String textoPie;
    @XmlElement(name = "FilasNombreOrganismo")
    protected TipoOpcionesVisualizacionDocumento.FilasNombreOrganismo filasNombreOrganismo;

    /**
     * Obtiene el valor de la propiedad estamparImagen.
     * 
     */
    public boolean isEstamparImagen() {
        return estamparImagen;
    }

    /**
     * Define el valor de la propiedad estamparImagen.
     * 
     */
    public void setEstamparImagen(boolean value) {
        this.estamparImagen = value;
    }

    /**
     * Obtiene el valor de la propiedad estamparNombreOrganismo.
     * 
     */
    public boolean isEstamparNombreOrganismo() {
        return estamparNombreOrganismo;
    }

    /**
     * Define el valor de la propiedad estamparNombreOrganismo.
     * 
     */
    public void setEstamparNombreOrganismo(boolean value) {
        this.estamparNombreOrganismo = value;
    }

    /**
     * Obtiene el valor de la propiedad estamparPie.
     * 
     */
    public boolean isEstamparPie() {
        return estamparPie;
    }

    /**
     * Define el valor de la propiedad estamparPie.
     * 
     */
    public void setEstamparPie(boolean value) {
        this.estamparPie = value;
    }

    /**
     * Obtiene el valor de la propiedad textoPie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextoPie() {
        return textoPie;
    }

    /**
     * Define el valor de la propiedad textoPie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextoPie(String value) {
        this.textoPie = value;
    }

    /**
     * Obtiene el valor de la propiedad filasNombreOrganismo.
     * 
     * @return
     *     possible object is
     *     {@link TipoOpcionesVisualizacionDocumento.FilasNombreOrganismo }
     *     
     */
    public TipoOpcionesVisualizacionDocumento.FilasNombreOrganismo getFilasNombreOrganismo() {
        return filasNombreOrganismo;
    }

    /**
     * Define el valor de la propiedad filasNombreOrganismo.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoOpcionesVisualizacionDocumento.FilasNombreOrganismo }
     *     
     */
    public void setFilasNombreOrganismo(TipoOpcionesVisualizacionDocumento.FilasNombreOrganismo value) {
        this.filasNombreOrganismo = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Fila" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
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
        "fila"
    })
    public static class FilasNombreOrganismo {

        @XmlElement(name = "Fila", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/visualizacion/documento-e", required = true)
        protected List<String> fila;

        /**
         * Gets the value of the fila property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the fila property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFila().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getFila() {
            if (fila == null) {
                fila = new ArrayList<String>();
            }
            return this.fila;
        }

    }

}
