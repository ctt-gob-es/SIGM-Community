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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ListaPropiedades complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ListaPropiedades">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="propiedades">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="propiedades" type="{http://service.ws.inside.dsic.mpt.es/}propiedad" maxOccurs="unbounded"/>
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
@XmlType(name = "ListaPropiedades", propOrder = {
    "propiedades"
})
public class ListaPropiedades {

    @XmlElement(required = true)
    protected ListaPropiedades.Propiedades propiedades;

    /**
     * Obtiene el valor de la propiedad propiedades.
     * 
     * @return
     *     possible object is
     *     {@link ListaPropiedades.Propiedades }
     *     
     */
    public ListaPropiedades.Propiedades getPropiedades() {
        return propiedades;
    }

    /**
     * Define el valor de la propiedad propiedades.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaPropiedades.Propiedades }
     *     
     */
    public void setPropiedades(ListaPropiedades.Propiedades value) {
        this.propiedades = value;
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
     *         &lt;element name="propiedades" type="{http://service.ws.inside.dsic.mpt.es/}propiedad" maxOccurs="unbounded"/>
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
        "propiedades"
    })
    public static class Propiedades {

        @XmlElement(required = true)
        protected List<Propiedad> propiedades;

        /**
         * Gets the value of the propiedades property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the propiedades property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPropiedades().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Propiedad }
         * 
         * 
         */
        public List<Propiedad> getPropiedades() {
            if (propiedades == null) {
                propiedades = new ArrayList<Propiedad>();
            }
            return this.propiedades;
        }

    }

}
