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
 * <p>Clase Java para visualizar complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="visualizar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="aplicacionInfo" type="{http://service.ws.inside.dsic.mpt.es/}applicationLogin"/>
 *         &lt;element name="item" type="{http://service.ws.inside.dsic.mpt.es/}item"/>
 *         &lt;element name="opcionesVisualizacion" type="{http://service.ws.inside.dsic.mpt.es/}opcionesVisualizacion"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "visualizar", propOrder = {
    "aplicacionInfo",
    "item",
    "opcionesVisualizacion"
})
public class Visualizar {

    @XmlElement(required = true)
    protected ApplicationLogin aplicacionInfo;
    @XmlElement(required = true)
    protected Item item;
    @XmlElement(required = true)
    protected OpcionesVisualizacion opcionesVisualizacion;

    /**
     * Obtiene el valor de la propiedad aplicacionInfo.
     * 
     * @return
     *     possible object is
     *     {@link ApplicationLogin }
     *     
     */
    public ApplicationLogin getAplicacionInfo() {
        return aplicacionInfo;
    }

    /**
     * Define el valor de la propiedad aplicacionInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplicationLogin }
     *     
     */
    public void setAplicacionInfo(ApplicationLogin value) {
        this.aplicacionInfo = value;
    }

    /**
     * Obtiene el valor de la propiedad item.
     * 
     * @return
     *     possible object is
     *     {@link Item }
     *     
     */
    public Item getItem() {
        return item;
    }

    /**
     * Define el valor de la propiedad item.
     * 
     * @param value
     *     allowed object is
     *     {@link Item }
     *     
     */
    public void setItem(Item value) {
        this.item = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesVisualizacion.
     * 
     * @return
     *     possible object is
     *     {@link OpcionesVisualizacion }
     *     
     */
    public OpcionesVisualizacion getOpcionesVisualizacion() {
        return opcionesVisualizacion;
    }

    /**
     * Define el valor de la propiedad opcionesVisualizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link OpcionesVisualizacion }
     *     
     */
    public void setOpcionesVisualizacion(OpcionesVisualizacion value) {
        this.opcionesVisualizacion = value;
    }

}
