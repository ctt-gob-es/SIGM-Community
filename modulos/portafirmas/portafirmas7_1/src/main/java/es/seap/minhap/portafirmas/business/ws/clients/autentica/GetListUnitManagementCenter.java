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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="webUser" type="{http://obj.dir4.ws.sag.es}WebUser"/&gt;
 *         &lt;element name="adminUser" type="{http://obj.dir4.ws.sag.es}AdminUser"/&gt;
 *         &lt;element name="unitOrganization" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "webUser",
    "adminUser",
    "unitOrganization"
})
@XmlRootElement(name = "getListUnitManagementCenter")
public class GetListUnitManagementCenter {

    @XmlElement(required = true)
    protected WebUser webUser;
    @XmlElement(required = true)
    protected AdminUser adminUser;
    @XmlElement(required = true)
    protected String unitOrganization;

    /**
     * Obtiene el valor de la propiedad webUser.
     * 
     * @return
     *     possible object is
     *     {@link WebUser }
     *     
     */
    public WebUser getWebUser() {
        return webUser;
    }

    /**
     * Define el valor de la propiedad webUser.
     * 
     * @param value
     *     allowed object is
     *     {@link WebUser }
     *     
     */
    public void setWebUser(WebUser value) {
        this.webUser = value;
    }

    /**
     * Obtiene el valor de la propiedad adminUser.
     * 
     * @return
     *     possible object is
     *     {@link AdminUser }
     *     
     */
    public AdminUser getAdminUser() {
        return adminUser;
    }

    /**
     * Define el valor de la propiedad adminUser.
     * 
     * @param value
     *     allowed object is
     *     {@link AdminUser }
     *     
     */
    public void setAdminUser(AdminUser value) {
        this.adminUser = value;
    }

    /**
     * Obtiene el valor de la propiedad unitOrganization.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOrganization() {
        return unitOrganization;
    }

    /**
     * Define el valor de la propiedad unitOrganization.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOrganization(String value) {
        this.unitOrganization = value;
    }

}
