/**
 * RecuperarAcuseFirmadoResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.jccm.notificador.ws;

public class RecuperarAcuseFirmadoResponse  implements java.io.Serializable {
    private javax.activation.DataHandler recuperarAcuseFirmadoReturn;

    public RecuperarAcuseFirmadoResponse() {
    }

    public RecuperarAcuseFirmadoResponse(
           javax.activation.DataHandler recuperarAcuseFirmadoReturn) {
           this.recuperarAcuseFirmadoReturn = recuperarAcuseFirmadoReturn;
    }


    /**
     * Gets the recuperarAcuseFirmadoReturn value for this RecuperarAcuseFirmadoResponse.
     * 
     * @return recuperarAcuseFirmadoReturn
     */
    public javax.activation.DataHandler getRecuperarAcuseFirmadoReturn() {
        return recuperarAcuseFirmadoReturn;
    }


    /**
     * Sets the recuperarAcuseFirmadoReturn value for this RecuperarAcuseFirmadoResponse.
     * 
     * @param recuperarAcuseFirmadoReturn
     */
    public void setRecuperarAcuseFirmadoReturn(javax.activation.DataHandler recuperarAcuseFirmadoReturn) {
        this.recuperarAcuseFirmadoReturn = recuperarAcuseFirmadoReturn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RecuperarAcuseFirmadoResponse)) return false;
        RecuperarAcuseFirmadoResponse other = (RecuperarAcuseFirmadoResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.recuperarAcuseFirmadoReturn==null && other.getRecuperarAcuseFirmadoReturn()==null) || 
             (this.recuperarAcuseFirmadoReturn!=null &&
              this.recuperarAcuseFirmadoReturn.equals(other.getRecuperarAcuseFirmadoReturn())));
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
        if (getRecuperarAcuseFirmadoReturn() != null) {
            _hashCode += getRecuperarAcuseFirmadoReturn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RecuperarAcuseFirmadoResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", ">recuperarAcuseFirmadoResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recuperarAcuseFirmadoReturn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "recuperarAcuseFirmadoReturn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "DataHandler"));
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
