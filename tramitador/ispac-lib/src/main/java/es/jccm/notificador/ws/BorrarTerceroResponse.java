/**
 * BorrarTerceroResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.jccm.notificador.ws;

public class BorrarTerceroResponse  implements java.io.Serializable {
    private boolean borrarTerceroReturn;

    public BorrarTerceroResponse() {
    }

    public BorrarTerceroResponse(
           boolean borrarTerceroReturn) {
           this.borrarTerceroReturn = borrarTerceroReturn;
    }


    /**
     * Gets the borrarTerceroReturn value for this BorrarTerceroResponse.
     * 
     * @return borrarTerceroReturn
     */
    public boolean isBorrarTerceroReturn() {
        return borrarTerceroReturn;
    }


    /**
     * Sets the borrarTerceroReturn value for this BorrarTerceroResponse.
     * 
     * @param borrarTerceroReturn
     */
    public void setBorrarTerceroReturn(boolean borrarTerceroReturn) {
        this.borrarTerceroReturn = borrarTerceroReturn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BorrarTerceroResponse)) return false;
        BorrarTerceroResponse other = (BorrarTerceroResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.borrarTerceroReturn == other.isBorrarTerceroReturn();
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
        _hashCode += (isBorrarTerceroReturn() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BorrarTerceroResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", ">borrarTerceroResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("borrarTerceroReturn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "borrarTerceroReturn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
