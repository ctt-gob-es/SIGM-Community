/*
 * Plan Avanza Local Soluciones – e-fácil – Copyright © 2011 – Ministerio de Industria, Turismo y 
 * Comercio.
 *
 * Este programa es software libre, por ello está permitido redistribuirlo y/o modificarlo bajo los 
 * términos de la GNU General Public License, en su versión 3, publicada por la Free Software 
 * Foundation.
 *
 * Junto con este programa debe haber recibido una copia de la GNU General Public License, en 
 * caso contrario puede consultarla en <http://www.gnu.org/licenses/>.
 *
 * El presente programa posee las siguientes cláusulas particulares de licencia GPL v3:
 *
 *  - Queda restringido su uso a Administraciones Públicas, en el ámbito de sus 
 * competencias legalmente establecidas y con la finalidad de utilidad pública e interés 
 * social;
 *  - La distribución y el uso del software tienen carácter gratuito;
 *  - Ni el software ni ninguno de los Módulos que lo componen serán usados ni 
 * sublicenciados, bajo ninguna circunstancia, con fines o intereses comerciales, bien sea 
 * directa o indirectamente, por parte de la Administración Pública, sus funcionarios, 
 * empleados o cualquier otra persona u organización dentro o fuera de la 
 * administración.
 *
 */

/**
 * ConformarFacturaResponseData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.fi2net.efacil.conformacion.ws.client;


/**
 * Contenedor de la respuesta de conformacion de facturas.
 */
public class ConformarFacturaResponseData  implements java.io.Serializable {
    /* Indica el resultado de la operaciÃ³n.
     *                             0: OK
     *                             1: Factura no existente
     *                            -1: Error desconocido */
    private int result;

    /* Mensaje del servidor en el que se indica el error producido. */
    private java.lang.String resultMsg;

    /**
     * Constructor.
     */
    public ConformarFacturaResponseData() {
    }

    public ConformarFacturaResponseData(
           int result,
           java.lang.String resultMsg) {
           this.result = result;
           this.resultMsg = resultMsg;
    }


    /**
     * Gets the result value for this ConformarFacturaResponseData.
     * 
     * @return result   * Indica el resultado de la operaciÃ³n.
     *                             0: OK
     *                             1: Factura no existente
     *                            -1: Error desconocido
     */
    public int getResult() {
        return result;
    }


    /**
     * Sets the result value for this ConformarFacturaResponseData.
     * 
     * @param result   * Indica el resultado de la operaciÃ³n.
     *                             0: OK
     *                             1: Factura no existente
     *                            -1: Error desconocido
     */
    public void setResult(int result) {
        this.result = result;
    }


    /**
     * Gets the resultMsg value for this ConformarFacturaResponseData.
     * 
     * @return resultMsg   * Mensaje del servidor en el que se indica el error producido.
     */
    public java.lang.String getResultMsg() {
        return resultMsg;
    }


    /**
     * Sets the resultMsg value for this ConformarFacturaResponseData.
     * 
     * @param resultMsg   * Mensaje del servidor en el que se indica el error producido.
     */
    public void setResultMsg(java.lang.String resultMsg) {
        this.resultMsg = resultMsg;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConformarFacturaResponseData)) return false;
        ConformarFacturaResponseData other = (ConformarFacturaResponseData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.result == other.getResult() &&
            ((this.resultMsg==null && other.getResultMsg()==null) || 
             (this.resultMsg!=null &&
              this.resultMsg.equals(other.getResultMsg())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getResult();
        if (getResultMsg() != null) {
            _hashCode += getResultMsg().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConformarFacturaResponseData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "ConformarFacturaResponseData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultMsg");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mityc.es/eFacil/ConformacionFactura/1.0", "resultMsg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
