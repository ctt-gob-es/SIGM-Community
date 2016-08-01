/**
 * InsertarApunteTrienioResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.CumplimientoTrienios;

public class InsertarApunteTrienioResponse  implements java.io.Serializable {
    private boolean insertarApunteTrienio;

    public InsertarApunteTrienioResponse() {
    }

    public InsertarApunteTrienioResponse(
           boolean insertarApunteTrienio) {
           this.insertarApunteTrienio = insertarApunteTrienio;
    }


    /**
     * Gets the insertarApunteTrienio value for this InsertarApunteTrienioResponse.
     * 
     * @return insertarApunteTrienio
     */
    public boolean isInsertarApunteTrienio() {
        return insertarApunteTrienio;
    }


    /**
     * Sets the insertarApunteTrienio value for this InsertarApunteTrienioResponse.
     * 
     * @param insertarApunteTrienio
     */
    public void setInsertarApunteTrienio(boolean insertarApunteTrienio) {
        this.insertarApunteTrienio = insertarApunteTrienio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InsertarApunteTrienioResponse)) return false;
        InsertarApunteTrienioResponse other = (InsertarApunteTrienioResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.insertarApunteTrienio == other.isInsertarApunteTrienio();
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
        _hashCode += (isInsertarApunteTrienio() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InsertarApunteTrienioResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", ">insertarApunteTrienioResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("insertarApunteTrienio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", "insertarApunteTrienio"));
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
