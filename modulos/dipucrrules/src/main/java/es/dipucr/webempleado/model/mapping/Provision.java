/**
 * Provision.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class Provision  implements java.io.Serializable {
    private java.lang.String CProvi;

    private java.lang.String NProvi;

    public Provision() {
    }

    public Provision(
           java.lang.String CProvi,
           java.lang.String NProvi) {
           this.CProvi = CProvi;
           this.NProvi = NProvi;
    }


    /**
     * Gets the CProvi value for this Provision.
     * 
     * @return CProvi
     */
    public java.lang.String getCProvi() {
        return CProvi;
    }


    /**
     * Sets the CProvi value for this Provision.
     * 
     * @param CProvi
     */
    public void setCProvi(java.lang.String CProvi) {
        this.CProvi = CProvi;
    }


    /**
     * Gets the NProvi value for this Provision.
     * 
     * @return NProvi
     */
    public java.lang.String getNProvi() {
        return NProvi;
    }


    /**
     * Sets the NProvi value for this Provision.
     * 
     * @param NProvi
     */
    public void setNProvi(java.lang.String NProvi) {
        this.NProvi = NProvi;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Provision)) return false;
        Provision other = (Provision) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CProvi==null && other.getCProvi()==null) || 
             (this.CProvi!=null &&
              this.CProvi.equals(other.getCProvi()))) &&
            ((this.NProvi==null && other.getNProvi()==null) || 
             (this.NProvi!=null &&
              this.NProvi.equals(other.getNProvi())));
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
        if (getCProvi() != null) {
            _hashCode += getCProvi().hashCode();
        }
        if (getNProvi() != null) {
            _hashCode += getNProvi().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Provision.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Provision"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CProvi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "CProvi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NProvi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "NProvi"));
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
