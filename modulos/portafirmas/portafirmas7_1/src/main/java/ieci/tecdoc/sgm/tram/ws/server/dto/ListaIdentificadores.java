
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.core.services.dto.RetornoServicio;
import ieci.tecdoc.sgm.tram.ws.server.sigem.ArrayOfXsdString;


/**
 * <p>Clase Java para ListaIdentificadores complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ListaIdentificadores">
 *   &lt;complexContent>
 *     &lt;extension base="{}RetornoServicio">
 *       &lt;sequence>
 *         &lt;element name="identificadores" type="{}ArrayOf_xsd_string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaIdentificadores", propOrder = {
    "identificadores"
})
public class ListaIdentificadores
    extends RetornoServicio
{

    @XmlElement(required = true, nillable = true)
    protected ArrayOfXsdString identificadores;

    /**
     * Obtiene el valor de la propiedad identificadores.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfXsdString }
     *     
     */
    public ArrayOfXsdString getIdentificadores() {
        return identificadores;
    }

    /**
     * Define el valor de la propiedad identificadores.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfXsdString }
     *     
     */
    public void setIdentificadores(ArrayOfXsdString value) {
        this.identificadores = value;
    }

}
