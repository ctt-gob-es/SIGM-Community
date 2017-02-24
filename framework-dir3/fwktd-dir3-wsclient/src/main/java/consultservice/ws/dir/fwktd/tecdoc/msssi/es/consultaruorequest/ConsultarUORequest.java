
package consultservice.ws.dir.fwktd.tecdoc.msssi.es.consultaruorequest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import es.msssi.tecdoc.fwktd.dir.ws.CommonRequest;
import es.msssi.tecdoc.fwktd.dir.ws.CriteriosUO;


/**
 * <p>Java class for consultarUORequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarUORequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.dir.fwktd.tecdoc.msssi.es/}commonRequest">
 *       &lt;sequence>
 *         &lt;element name="criterios" type="{http://ws.dir.fwktd.tecdoc.msssi.es/}criteriosUO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarUORequest", propOrder = {
    "criterios"
})
public class ConsultarUORequest
    extends CommonRequest
{

    protected CriteriosUO criterios;

    /**
     * Gets the value of the criterios property.
     * 
     * @return
     *     possible object is
     *     {@link CriteriosUO }
     *     
     */
    public CriteriosUO getCriterios() {
        return criterios;
    }

    /**
     * Sets the value of the criterios property.
     * 
     * @param value
     *     allowed object is
     *     {@link CriteriosUO }
     *     
     */
    public void setCriterios(CriteriosUO value) {
        this.criterios = value;
    }

}
