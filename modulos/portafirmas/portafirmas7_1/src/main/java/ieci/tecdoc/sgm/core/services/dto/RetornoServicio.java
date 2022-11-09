
package ieci.tecdoc.sgm.core.services.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.Binario;
import ieci.tecdoc.sgm.tram.ws.server.dto.Booleano;
import ieci.tecdoc.sgm.tram.ws.server.dto.Cadena;
import ieci.tecdoc.sgm.tram.ws.server.dto.Entero;
import ieci.tecdoc.sgm.tram.ws.server.dto.Expediente;
import ieci.tecdoc.sgm.tram.ws.server.dto.InfoFichero;
import ieci.tecdoc.sgm.tram.ws.server.dto.InfoOcupacion;
import ieci.tecdoc.sgm.tram.ws.server.dto.ListaIdentificadores;
import ieci.tecdoc.sgm.tram.ws.server.dto.ListaInfoBExpedientes;
import ieci.tecdoc.sgm.tram.ws.server.dto.ListaInfoBProcedimientos;
import ieci.tecdoc.sgm.tram.ws.server.dto.Procedimiento;


/**
 * <p>Clase Java para RetornoServicio complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RetornoServicio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="returnCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetornoServicio", propOrder = {
    "errorCode",
    "returnCode"
})
@XmlSeeAlso({
    Cadena.class,
    Booleano.class,
    InfoOcupacion.class,
    Expediente.class,
    Entero.class,
    Binario.class,
    ListaInfoBProcedimientos.class,
    InfoFichero.class,
    Procedimiento.class,
    ListaIdentificadores.class,
    ListaInfoBExpedientes.class
})
public class RetornoServicio {

    @XmlElement(required = true, nillable = true)
    protected String errorCode;
    @XmlElement(required = true, nillable = true)
    protected String returnCode;

    /**
     * Obtiene el valor de la propiedad errorCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Define el valor de la propiedad errorCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * Obtiene el valor de la propiedad returnCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnCode() {
        return returnCode;
    }

    /**
     * Define el valor de la propiedad returnCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnCode(String value) {
        this.returnCode = value;
    }

}
