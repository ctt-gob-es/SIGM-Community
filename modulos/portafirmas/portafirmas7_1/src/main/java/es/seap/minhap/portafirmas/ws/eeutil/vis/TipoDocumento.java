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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para TipoDocumento complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoDocumento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/contenido}contenido"/>
 *         &lt;element ref="{https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos}metadatos"/>
 *         &lt;element ref="{https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma}firmas" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoDocumento", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento", propOrder = {
    "contenido",
    "metadatos",
    "firmas"
})
public class TipoDocumento {

    @XmlElement(namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/contenido", required = true)
    protected TipoContenido contenido;
    @XmlElement(namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/documento/metadatos", required = true)
    protected TipoMetadatos metadatos;
    @XmlElement(namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma")
    protected Firmas firmas;

    /**
     * Obtiene el valor de la propiedad contenido.
     * 
     * @return
     *     possible object is
     *     {@link TipoContenido }
     *     
     */
    public TipoContenido getContenido() {
        return contenido;
    }

    /**
     * Define el valor de la propiedad contenido.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoContenido }
     *     
     */
    public void setContenido(TipoContenido value) {
        this.contenido = value;
    }

    /**
     * Obtiene el valor de la propiedad metadatos.
     * 
     * @return
     *     possible object is
     *     {@link TipoMetadatos }
     *     
     */
    public TipoMetadatos getMetadatos() {
        return metadatos;
    }

    /**
     * Define el valor de la propiedad metadatos.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoMetadatos }
     *     
     */
    public void setMetadatos(TipoMetadatos value) {
        this.metadatos = value;
    }

    /**
     * Obtiene el valor de la propiedad firmas.
     * 
     * @return
     *     possible object is
     *     {@link Firmas }
     *     
     */
    public Firmas getFirmas() {
        return firmas;
    }

    /**
     * Define el valor de la propiedad firmas.
     * 
     * @param value
     *     allowed object is
     *     {@link Firmas }
     *     
     */
    public void setFirmas(Firmas value) {
        this.firmas = value;
    }

}
