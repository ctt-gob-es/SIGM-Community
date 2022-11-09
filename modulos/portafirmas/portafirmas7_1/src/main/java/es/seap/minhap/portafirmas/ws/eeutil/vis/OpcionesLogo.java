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
 * <p>Clase Java para opcionesLogo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="opcionesLogo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="estamparLogo" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="estamparNombreOrganismo" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="listaCadenasNombreOrganismo" type="{http://service.ws.inside.dsic.mpt.es/}listaCadenas" minOccurs="0"/>
 *         &lt;element name="posicion" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="estamparPie" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="textoPie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "opcionesLogo", propOrder = {
    "estamparLogo",
    "estamparNombreOrganismo",
    "listaCadenasNombreOrganismo",
    "posicion",
    "estamparPie",
    "textoPie"
})
public class OpcionesLogo {

    @XmlElement(defaultValue = "true")
    protected boolean estamparLogo;
    @XmlElement(defaultValue = "true")
    protected boolean estamparNombreOrganismo;
    protected ListaCadenas listaCadenasNombreOrganismo;
    protected Integer posicion;
    protected boolean estamparPie;
    protected String textoPie;

    /**
     * Obtiene el valor de la propiedad estamparLogo.
     * 
     */
    public boolean isEstamparLogo() {
        return estamparLogo;
    }

    /**
     * Define el valor de la propiedad estamparLogo.
     * 
     */
    public void setEstamparLogo(boolean value) {
        this.estamparLogo = value;
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
     * Obtiene el valor de la propiedad listaCadenasNombreOrganismo.
     * 
     * @return
     *     possible object is
     *     {@link ListaCadenas }
     *     
     */
    public ListaCadenas getListaCadenasNombreOrganismo() {
        return listaCadenasNombreOrganismo;
    }

    /**
     * Define el valor de la propiedad listaCadenasNombreOrganismo.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaCadenas }
     *     
     */
    public void setListaCadenasNombreOrganismo(ListaCadenas value) {
        this.listaCadenasNombreOrganismo = value;
    }

    /**
     * Obtiene el valor de la propiedad posicion.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPosicion() {
        return posicion;
    }

    /**
     * Define el valor de la propiedad posicion.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPosicion(Integer value) {
        this.posicion = value;
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

}
