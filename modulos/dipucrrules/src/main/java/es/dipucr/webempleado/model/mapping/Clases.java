/**
 * Clases.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class Clases  implements java.io.Serializable {
    private java.lang.String CClase;

    private java.lang.String NClase;

    public Clases() {
    }

    public Clases(
           java.lang.String CClase,
           java.lang.String NClase) {
           this.CClase = CClase;
           this.NClase = NClase;
    }


    /**
     * Gets the CClase value for this Clases.
     * 
     * @return CClase
     */
    public java.lang.String getCClase() {
        return CClase;
    }


    /**
     * Sets the CClase value for this Clases.
     * 
     * @param CClase
     */
    public void setCClase(java.lang.String CClase) {
        this.CClase = CClase;
    }


    /**
     * Gets the NClase value for this Clases.
     * 
     * @return NClase
     */
    public java.lang.String getNClase() {
        return NClase;
    }


    /**
     * Sets the NClase value for this Clases.
     * 
     * @param NClase
     */
    public void setNClase(java.lang.String NClase) {
        this.NClase = NClase;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Clases)) return false;
        Clases other = (Clases) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CClase==null && other.getCClase()==null) || 
             (this.CClase!=null &&
              this.CClase.equals(other.getCClase()))) &&
            ((this.NClase==null && other.getNClase()==null) || 
             (this.NClase!=null &&
              this.NClase.equals(other.getNClase())));
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
        if (getCClase() != null) {
            _hashCode += getCClase().hashCode();
        }
        if (getNClase() != null) {
            _hashCode += getNClase().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Clases.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Clases"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CClase");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "CClase"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NClase");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "NClase"));
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
