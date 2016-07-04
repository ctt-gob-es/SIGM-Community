
package es.msssi.tecdoc.fwktd.dir.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import consultservice.ws.dir.fwktd.tecdoc.msssi.es.consultaruoresponse.ConsultarUOResponse;


/**
 * <p>Java class for consultarUnidadesOrganicasResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarUnidadesOrganicasResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://es.msssi.tecdoc.fwktd.dir.ws.ConsultService/consultarUOResponse}consultarUOResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarUnidadesOrganicasResponse", propOrder = {
    "_return"
})
public class ConsultarUnidadesOrganicasResponse {

    @XmlElement(name = "return")
    protected ConsultarUOResponse _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link ConsultarUOResponse }
     *     
     */
    public ConsultarUOResponse getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsultarUOResponse }
     *     
     */
    public void setReturn(ConsultarUOResponse value) {
        this._return = value;
    }

}
