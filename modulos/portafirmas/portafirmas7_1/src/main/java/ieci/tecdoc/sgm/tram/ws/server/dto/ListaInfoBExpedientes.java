
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.core.services.dto.RetornoServicio;
import ieci.tecdoc.sgm.tram.ws.server.sigem.ArrayOfTns1InfoBExpediente;


/**
 * <p>Clase Java para ListaInfoBExpedientes complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ListaInfoBExpedientes">
 *   &lt;complexContent>
 *     &lt;extension base="{}RetornoServicio">
 *       &lt;sequence>
 *         &lt;element name="expedientes" type="{}ArrayOf_tns1_InfoBExpediente"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaInfoBExpedientes", propOrder = {
    "expedientes"
})
public class ListaInfoBExpedientes
    extends RetornoServicio
{

    @XmlElement(required = true, nillable = true)
    protected ArrayOfTns1InfoBExpediente expedientes;

    /**
     * Obtiene el valor de la propiedad expedientes.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTns1InfoBExpediente }
     *     
     */
    public ArrayOfTns1InfoBExpediente getExpedientes() {
        return expedientes;
    }

    /**
     * Define el valor de la propiedad expedientes.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTns1InfoBExpediente }
     *     
     */
    public void setExpedientes(ArrayOfTns1InfoBExpediente value) {
        this.expedientes = value;
    }

}
