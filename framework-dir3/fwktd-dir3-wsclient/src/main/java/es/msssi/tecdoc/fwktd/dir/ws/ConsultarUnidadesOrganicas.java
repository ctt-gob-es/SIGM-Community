
package es.msssi.tecdoc.fwktd.dir.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import consultservice.ws.dir.fwktd.tecdoc.msssi.es.consultaruorequest.ConsultarUORequest;


/**
 * <p>Java class for consultarUnidadesOrganicas complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarUnidadesOrganicas">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="request" type="{http://es.msssi.tecdoc.fwktd.dir.ws.ConsultService/consultarUORequest}consultarUORequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarUnidadesOrganicas", propOrder = {
    "request"
})
public class ConsultarUnidadesOrganicas {

    protected ConsultarUORequest request;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link ConsultarUORequest }
     *     
     */
    public ConsultarUORequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsultarUORequest }
     *     
     */
    public void setRequest(ConsultarUORequest value) {
        this.request = value;
    }

}
