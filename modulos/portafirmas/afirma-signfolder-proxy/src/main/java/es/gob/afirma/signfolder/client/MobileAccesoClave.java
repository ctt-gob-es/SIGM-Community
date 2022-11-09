
package es.gob.afirma.signfolder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mobileAccesoClave complex type.
 *
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 *
 * <pre>
 * &lt;complexType name="mobileAccesoClave">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="claveServiceUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="excludedIdPList" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="forcedIdP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="samlRequest" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileAccesoClave", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
    "claveServiceUrl",
    "excludedIdPList",
    "forcedIdP",
    "samlRequest"
})
public class MobileAccesoClave {

    protected String claveServiceUrl;
    protected String excludedIdPList;
    protected String forcedIdP;
    protected String samlRequest;

    /**
     * Obtiene el valor de la propiedad claveServiceUrl.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getClaveServiceUrl() {
        return this.claveServiceUrl;
    }

    /**
     * Define el valor de la propiedad claveServiceUrl.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setClaveServiceUrl(final String value) {
        this.claveServiceUrl = value;
    }

    /**
     * Obtiene el valor de la propiedad excludedIdPList.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getExcludedIdPList() {
        return this.excludedIdPList;
    }

    /**
     * Define el valor de la propiedad excludedIdPList.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setExcludedIdPList(final String value) {
        this.excludedIdPList = value;
    }

    /**
     * Obtiene el valor de la propiedad forcedIdP.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getForcedIdP() {
        return this.forcedIdP;
    }

    /**
     * Define el valor de la propiedad forcedIdP.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setForcedIdP(final String value) {
        this.forcedIdP = value;
    }

    /**
     * Obtiene el valor de la propiedad samlRequest.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSamlRequest() {
        return this.samlRequest;
    }

    /**
     * Define el valor de la propiedad samlRequest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSamlRequest(final String value) {
        this.samlRequest = value;
    }

}
