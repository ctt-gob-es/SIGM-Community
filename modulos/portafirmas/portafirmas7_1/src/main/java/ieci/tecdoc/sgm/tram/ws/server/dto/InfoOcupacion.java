
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.core.services.dto.RetornoServicio;


/**
 * <p>Clase Java para InfoOcupacion complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="InfoOcupacion">
 *   &lt;complexContent>
 *     &lt;extension base="{}RetornoServicio">
 *       &lt;sequence>
 *         &lt;element name="espacioOcupado" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="espacioTotal" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="numeroFicheros" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoOcupacion", propOrder = {
    "espacioOcupado",
    "espacioTotal",
    "numeroFicheros"
})
public class InfoOcupacion
    extends RetornoServicio
{

    protected long espacioOcupado;
    protected long espacioTotal;
    protected long numeroFicheros;

    /**
     * Obtiene el valor de la propiedad espacioOcupado.
     * 
     */
    public long getEspacioOcupado() {
        return espacioOcupado;
    }

    /**
     * Define el valor de la propiedad espacioOcupado.
     * 
     */
    public void setEspacioOcupado(long value) {
        this.espacioOcupado = value;
    }

    /**
     * Obtiene el valor de la propiedad espacioTotal.
     * 
     */
    public long getEspacioTotal() {
        return espacioTotal;
    }

    /**
     * Define el valor de la propiedad espacioTotal.
     * 
     */
    public void setEspacioTotal(long value) {
        this.espacioTotal = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroFicheros.
     * 
     */
    public long getNumeroFicheros() {
        return numeroFicheros;
    }

    /**
     * Define el valor de la propiedad numeroFicheros.
     * 
     */
    public void setNumeroFicheros(long value) {
        this.numeroFicheros = value;
    }

}
