/**
 * Categorias.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class Categorias  implements java.io.Serializable {
    private java.lang.String CCateg;

    private java.lang.String NCateg;

    public Categorias() {
    }

    public Categorias(
           java.lang.String CCateg,
           java.lang.String NCateg) {
           this.CCateg = CCateg;
           this.NCateg = NCateg;
    }


    /**
     * Gets the CCateg value for this Categorias.
     * 
     * @return CCateg
     */
    public java.lang.String getCCateg() {
        return CCateg;
    }


    /**
     * Sets the CCateg value for this Categorias.
     * 
     * @param CCateg
     */
    public void setCCateg(java.lang.String CCateg) {
        this.CCateg = CCateg;
    }


    /**
     * Gets the NCateg value for this Categorias.
     * 
     * @return NCateg
     */
    public java.lang.String getNCateg() {
        return NCateg;
    }


    /**
     * Sets the NCateg value for this Categorias.
     * 
     * @param NCateg
     */
    public void setNCateg(java.lang.String NCateg) {
        this.NCateg = NCateg;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Categorias)) return false;
        Categorias other = (Categorias) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CCateg==null && other.getCCateg()==null) || 
             (this.CCateg!=null &&
              this.CCateg.equals(other.getCCateg()))) &&
            ((this.NCateg==null && other.getNCateg()==null) || 
             (this.NCateg!=null &&
              this.NCateg.equals(other.getNCateg())));
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
        if (getCCateg() != null) {
            _hashCode += getCCateg().hashCode();
        }
        if (getNCateg() != null) {
            _hashCode += getNCateg().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Categorias.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Categorias"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CCateg");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "CCateg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NCateg");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "NCateg"));
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
