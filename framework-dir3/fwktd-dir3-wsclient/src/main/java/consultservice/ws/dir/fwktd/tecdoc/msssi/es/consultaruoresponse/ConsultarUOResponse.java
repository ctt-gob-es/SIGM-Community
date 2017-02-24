
package consultservice.ws.dir.fwktd.tecdoc.msssi.es.consultaruoresponse;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import es.msssi.tecdoc.fwktd.dir.ws.CommonResponse;
import es.msssi.tecdoc.fwktd.dir.ws.DatosUnidadOrganica;


/**
 * <p>Java class for consultarUOResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarUOResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.dir.fwktd.tecdoc.msssi.es/}commonResponse">
 *       &lt;sequence>
 *         &lt;element name="unidades" type="{http://ws.dir.fwktd.tecdoc.msssi.es/}datosUnidadOrganica" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarUOResponse", propOrder = {
    "unidades"
})
public class ConsultarUOResponse
    extends CommonResponse
{

    @XmlElement(nillable = true)
    protected List<DatosUnidadOrganica> unidades;

    /**
     * Gets the value of the unidades property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the unidades property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUnidades().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DatosUnidadOrganica }
     * 
     * 
     */
    public List<DatosUnidadOrganica> getUnidades() {
        if (unidades == null) {
            unidades = new ArrayList<DatosUnidadOrganica>();
        }
        return this.unidades;
    }

}
