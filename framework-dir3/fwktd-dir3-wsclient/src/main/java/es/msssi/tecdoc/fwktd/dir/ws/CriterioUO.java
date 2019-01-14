
package es.msssi.tecdoc.fwktd.dir.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for criterioUO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="criterioUO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombre" type="{http://ws.dir.fwktd.tecdoc.msssi.es/}CriterioUnidadOrganicaUOEnum" minOccurs="0"/>
 *         &lt;element name="operador" type="{http://ws.dir.fwktd.tecdoc.msssi.es/}OperadorCriterioUOEnum" minOccurs="0"/>
 *         &lt;element name="valor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "criterioUO", propOrder = {
    "nombre",
    "operador",
    "valor"
})
public class CriterioUO {

    protected CriterioUnidadOrganicaUOEnum nombre;
    protected OperadorCriterioUOEnum operador;
    protected String valor;

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link CriterioUnidadOrganicaUOEnum }
     *     
     */
    public CriterioUnidadOrganicaUOEnum getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link CriterioUnidadOrganicaUOEnum }
     *     
     */
    public void setNombre(CriterioUnidadOrganicaUOEnum value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the operador property.
     * 
     * @return
     *     possible object is
     *     {@link OperadorCriterioUOEnum }
     *     
     */
    public OperadorCriterioUOEnum getOperador() {
        return operador;
    }

    /**
     * Sets the value of the operador property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperadorCriterioUOEnum }
     *     
     */
    public void setOperador(OperadorCriterioUOEnum value) {
        this.operador = value;
    }

    /**
     * Gets the value of the valor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValor() {
        return valor;
    }

    /**
     * Sets the value of the valor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValor(String value) {
        this.valor = value;
    }

}
