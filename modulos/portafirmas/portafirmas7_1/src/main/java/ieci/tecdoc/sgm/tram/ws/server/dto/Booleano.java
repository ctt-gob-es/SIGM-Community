
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.core.services.dto.RetornoServicio;


/**
 * <p>Clase Java para Booleano complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Booleano">
 *   &lt;complexContent>
 *     &lt;extension base="{}RetornoServicio">
 *       &lt;sequence>
 *         &lt;element name="valor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Booleano", propOrder = {
    "valor"
})
public class Booleano
    extends RetornoServicio
{

    protected boolean valor;

    /**
     * Obtiene el valor de la propiedad valor.
     * 
     */
    public boolean isValor() {
        return valor;
    }

    /**
     * Define el valor de la propiedad valor.
     * 
     */
    public void setValor(boolean value) {
        this.valor = value;
    }

}
