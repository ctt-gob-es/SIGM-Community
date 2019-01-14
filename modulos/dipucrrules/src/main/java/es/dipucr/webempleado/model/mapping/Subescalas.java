/**
 * Subescalas.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class Subescalas  implements java.io.Serializable {
    private java.lang.String CSesc;

    private java.lang.String NSesc;

    public Subescalas() {
    }

    public Subescalas(
           java.lang.String CSesc,
           java.lang.String NSesc) {
           this.CSesc = CSesc;
           this.NSesc = NSesc;
    }


    /**
     * Gets the CSesc value for this Subescalas.
     * 
     * @return CSesc
     */
    public java.lang.String getCSesc() {
        return CSesc;
    }


    /**
     * Sets the CSesc value for this Subescalas.
     * 
     * @param CSesc
     */
    public void setCSesc(java.lang.String CSesc) {
        this.CSesc = CSesc;
    }


    /**
     * Gets the NSesc value for this Subescalas.
     * 
     * @return NSesc
     */
    public java.lang.String getNSesc() {
        return NSesc;
    }


    /**
     * Sets the NSesc value for this Subescalas.
     * 
     * @param NSesc
     */
    public void setNSesc(java.lang.String NSesc) {
        this.NSesc = NSesc;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Subescalas)) return false;
        Subescalas other = (Subescalas) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CSesc==null && other.getCSesc()==null) || 
             (this.CSesc!=null &&
              this.CSesc.equals(other.getCSesc()))) &&
            ((this.NSesc==null && other.getNSesc()==null) || 
             (this.NSesc!=null &&
              this.NSesc.equals(other.getNSesc())));
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
        if (getCSesc() != null) {
            _hashCode += getCSesc().hashCode();
        }
        if (getNSesc() != null) {
            _hashCode += getNSesc().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Subescalas.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Subescalas"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CSesc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "CSesc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NSesc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "NSesc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
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
