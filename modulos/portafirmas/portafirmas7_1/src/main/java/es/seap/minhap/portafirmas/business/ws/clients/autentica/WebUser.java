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
 * <p>Clase Java para WebUser complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="WebUser"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="webName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="webPass" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WebUser", namespace = "http://obj.dir4.ws.sag.es", propOrder = {
    "webName",
    "webPass"
})
public class WebUser {

    @XmlElement(required = true, nillable = true)
    protected String webName;
    @XmlElement(required = true, nillable = true)
    protected String webPass;

    /**
     * Obtiene el valor de la propiedad webName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebName() {
        return webName;
    }

    /**
     * Define el valor de la propiedad webName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebName(String value) {
        this.webName = value;
    }

    /**
     * Obtiene el valor de la propiedad webPass.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebPass() {
        return webPass;
    }

    /**
     * Define el valor de la propiedad webPass.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebPass(String value) {
        this.webPass = value;
    }

}
