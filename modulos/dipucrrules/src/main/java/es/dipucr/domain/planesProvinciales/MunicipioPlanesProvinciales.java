/**
 * MunicipioPlanesProvinciales.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.domain.planesProvinciales;

public class MunicipioPlanesProvinciales  implements java.io.Serializable {
    private java.lang.String codigo;

    private java.lang.String nombreMunicipio;

    public MunicipioPlanesProvinciales() {
    }

    public MunicipioPlanesProvinciales(
           java.lang.String codigo,
           java.lang.String nombreMunicipio) {
           this.codigo = codigo;
           this.nombreMunicipio = nombreMunicipio;
    }


    /**
     * Gets the codigo value for this MunicipioPlanesProvinciales.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this MunicipioPlanesProvinciales.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the nombreMunicipio value for this MunicipioPlanesProvinciales.
     * 
     * @return nombreMunicipio
     */
    public java.lang.String getNombreMunicipio() {
        return nombreMunicipio;
    }


    /**
     * Sets the nombreMunicipio value for this MunicipioPlanesProvinciales.
     * 
     * @param nombreMunicipio
     */
    public void setNombreMunicipio(java.lang.String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MunicipioPlanesProvinciales)) return false;
        MunicipioPlanesProvinciales other = (MunicipioPlanesProvinciales) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.nombreMunicipio==null && other.getNombreMunicipio()==null) || 
             (this.nombreMunicipio!=null &&
              this.nombreMunicipio.equals(other.getNombreMunicipio())));
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
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getNombreMunicipio() != null) {
            _hashCode += getNombreMunicipio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MunicipioPlanesProvinciales.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "MunicipioPlanesProvinciales"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreMunicipio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "nombreMunicipio"));
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
