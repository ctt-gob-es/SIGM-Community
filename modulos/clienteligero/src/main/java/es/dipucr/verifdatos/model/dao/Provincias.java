/**
 * Provincias.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.verifdatos.model.dao;

public class Provincias  implements java.io.Serializable {
    private java.lang.String codigo;

    private es.dipucr.verifdatos.model.dao.Generico[] municipios;

    public Provincias() {
    }

    public Provincias(
           java.lang.String codigo,
           es.dipucr.verifdatos.model.dao.Generico[] municipios) {
           this.codigo = codigo;
           this.municipios = municipios;
    }


    /**
     * Gets the codigo value for this Provincias.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this Provincias.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the municipios value for this Provincias.
     * 
     * @return municipios
     */
    public es.dipucr.verifdatos.model.dao.Generico[] getMunicipios() {
        return municipios;
    }


    /**
     * Sets the municipios value for this Provincias.
     * 
     * @param municipios
     */
    public void setMunicipios(es.dipucr.verifdatos.model.dao.Generico[] municipios) {
        this.municipios = municipios;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Provincias)) return false;
        Provincias other = (Provincias) obj;
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
            ((this.municipios==null && other.getMunicipios()==null) || 
             (this.municipios!=null &&
              java.util.Arrays.equals(this.municipios, other.getMunicipios())));
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
        if (getMunicipios() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMunicipios());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMunicipios(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Provincias.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "Provincias"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("municipios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "municipios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "Generico"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.verifdatos.dipucr.es", "item"));
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
