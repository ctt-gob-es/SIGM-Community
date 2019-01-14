/**
 * Escalas.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class Escalas  implements java.io.Serializable {
    private java.lang.String CEsc;

    private java.lang.String NEsc;

    public Escalas() {
    }

    public Escalas(
           java.lang.String CEsc,
           java.lang.String NEsc) {
           this.CEsc = CEsc;
           this.NEsc = NEsc;
    }


    /**
     * Gets the CEsc value for this Escalas.
     * 
     * @return CEsc
     */
    public java.lang.String getCEsc() {
        return CEsc;
    }


    /**
     * Sets the CEsc value for this Escalas.
     * 
     * @param CEsc
     */
    public void setCEsc(java.lang.String CEsc) {
        this.CEsc = CEsc;
    }


    /**
     * Gets the NEsc value for this Escalas.
     * 
     * @return NEsc
     */
    public java.lang.String getNEsc() {
        return NEsc;
    }


    /**
     * Sets the NEsc value for this Escalas.
     * 
     * @param NEsc
     */
    public void setNEsc(java.lang.String NEsc) {
        this.NEsc = NEsc;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Escalas)) return false;
        Escalas other = (Escalas) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CEsc==null && other.getCEsc()==null) || 
             (this.CEsc!=null &&
              this.CEsc.equals(other.getCEsc()))) &&
            ((this.NEsc==null && other.getNEsc()==null) || 
             (this.NEsc!=null &&
              this.NEsc.equals(other.getNEsc())));
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
        if (getCEsc() != null) {
            _hashCode += getCEsc().hashCode();
        }
        if (getNEsc() != null) {
            _hashCode += getNEsc().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Escalas.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Escalas"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CEsc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "CEsc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NEsc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "NEsc"));
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
