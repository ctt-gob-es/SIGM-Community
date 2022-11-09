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


package es.seap.minhap.portafirmas.business.ws.clients.autentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para AdminUser complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="AdminUser"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="adminName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="adminOrganicalCodeDir3" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdminUser", namespace = "http://obj.dir4.ws.sag.es", propOrder = {
    "adminName",
    "adminOrganicalCodeDir3"
})
public class AdminUser {

    @XmlElement(required = true, nillable = true)
    protected String adminName;
    @XmlElement(required = true, nillable = true)
    protected String adminOrganicalCodeDir3;

    /**
     * Obtiene el valor de la propiedad adminName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdminName() {
        return adminName;
    }

    /**
     * Define el valor de la propiedad adminName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdminName(String value) {
        this.adminName = value;
    }

    /**
     * Obtiene el valor de la propiedad adminOrganicalCodeDir3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdminOrganicalCodeDir3() {
        return adminOrganicalCodeDir3;
    }

    /**
     * Define el valor de la propiedad adminOrganicalCodeDir3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdminOrganicalCodeDir3(String value) {
        this.adminOrganicalCodeDir3 = value;
    }

}
