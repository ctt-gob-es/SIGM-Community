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

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="updateUser" type="{http://obj.dir4.ws.sag.es}UpdateUser"/&gt;
 *         &lt;element name="ldapUser" type="{http://obj.dir4.ws.sag.es}LdapUser"/&gt;
 *         &lt;element name="aLdapPositionsWS" type="{http://obj.dir4.ws.sag.es}LdapPositionWS" maxOccurs="unbounded"/&gt;
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
    "updateUser",
    "ldapUser",
    "aLdapPositionsWS"
})
@XmlRootElement(name = "modifyUser")
public class ModifyUser {

    @XmlElement(required = true)
    protected WebUser webUser;
    @XmlElement(required = true)
    protected UpdateUser updateUser;
    @XmlElement(required = true)
    protected LdapUser ldapUser;
    @XmlElement(required = true)
    protected List<LdapPositionWS> aLdapPositionsWS;

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
     * Obtiene el valor de la propiedad updateUser.
     * 
     * @return
     *     possible object is
     *     {@link UpdateUser }
     *     
     */
    public UpdateUser getUpdateUser() {
        return updateUser;
    }

    /**
     * Define el valor de la propiedad updateUser.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateUser }
     *     
     */
    public void setUpdateUser(UpdateUser value) {
        this.updateUser = value;
    }

    /**
     * Obtiene el valor de la propiedad ldapUser.
     * 
     * @return
     *     possible object is
     *     {@link LdapUser }
     *     
     */
    public LdapUser getLdapUser() {
        return ldapUser;
    }

    /**
     * Define el valor de la propiedad ldapUser.
     * 
     * @param value
     *     allowed object is
     *     {@link LdapUser }
     *     
     */
    public void setLdapUser(LdapUser value) {
        this.ldapUser = value;
    }

    /**
     * Gets the value of the aLdapPositionsWS property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aLdapPositionsWS property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALdapPositionsWS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LdapPositionWS }
     * 
     * 
     */
    public List<LdapPositionWS> getALdapPositionsWS() {
        if (aLdapPositionsWS == null) {
            aLdapPositionsWS = new ArrayList<LdapPositionWS>();
        }
        return this.aLdapPositionsWS;
    }

}
