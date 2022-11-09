
package es.gob.afirma.signfolder.client;

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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="solicitudAccesoClaveRespuesta" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileAccesoClave"/>
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
    "solicitudAccesoClaveRespuesta"
})
@XmlRootElement(name = "solicitudAccesoClaveResponse")
public class SolicitudAccesoClaveResponse {

    @XmlElement(required = true)
    protected MobileAccesoClave solicitudAccesoClaveRespuesta;

    /**
     * Obtiene el valor de la propiedad solicitudAccesoClaveRespuesta.
     * 
     * @return
     *     possible object is
     *     {@link MobileAccesoClave }
     *     
     */
    public MobileAccesoClave getSolicitudAccesoClaveRespuesta() {
        return solicitudAccesoClaveRespuesta;
    }

    /**
     * Define el valor de la propiedad solicitudAccesoClaveRespuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileAccesoClave }
     *     
     */
    public void setSolicitudAccesoClaveRespuesta(MobileAccesoClave value) {
        this.solicitudAccesoClaveRespuesta = value;
    }

}
