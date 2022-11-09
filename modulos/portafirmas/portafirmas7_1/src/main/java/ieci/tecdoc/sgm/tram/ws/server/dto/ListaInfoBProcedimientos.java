
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.core.services.dto.RetornoServicio;
import ieci.tecdoc.sgm.tram.ws.server.sigem.ArrayOfTns1InfoBProcedimiento;


/**
 * <p>Clase Java para ListaInfoBProcedimientos complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ListaInfoBProcedimientos">
 *   &lt;complexContent>
 *     &lt;extension base="{}RetornoServicio">
 *       &lt;sequence>
 *         &lt;element name="procedimientos" type="{}ArrayOf_tns1_InfoBProcedimiento"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaInfoBProcedimientos", propOrder = {
    "procedimientos"
})
public class ListaInfoBProcedimientos
    extends RetornoServicio
{

    @XmlElement(required = true, nillable = true)
    protected ArrayOfTns1InfoBProcedimiento procedimientos;

    /**
     * Obtiene el valor de la propiedad procedimientos.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTns1InfoBProcedimiento }
     *     
     */
    public ArrayOfTns1InfoBProcedimiento getProcedimientos() {
        return procedimientos;
    }

    /**
     * Define el valor de la propiedad procedimientos.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTns1InfoBProcedimiento }
     *     
     */
    public void setProcedimientos(ArrayOfTns1InfoBProcedimiento value) {
        this.procedimientos = value;
    }

}
