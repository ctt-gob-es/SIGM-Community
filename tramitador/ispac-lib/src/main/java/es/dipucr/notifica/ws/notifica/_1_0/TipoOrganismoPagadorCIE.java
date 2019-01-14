/**
 * TipoOrganismoPagadorCIE.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class TipoOrganismoPagadorCIE  implements java.io.Serializable {
    private java.lang.String codigo_dir3;

    private java.util.Date fecha_vigencia;

    public TipoOrganismoPagadorCIE() {
    }

    public TipoOrganismoPagadorCIE(
           java.lang.String codigo_dir3,
           java.util.Date fecha_vigencia) {
           this.codigo_dir3 = codigo_dir3;
           this.fecha_vigencia = fecha_vigencia;
    }


    /**
     * Gets the codigo_dir3 value for this TipoOrganismoPagadorCIE.
     * 
     * @return codigo_dir3
     */
    public java.lang.String getCodigo_dir3() {
        return codigo_dir3;
    }


    /**
     * Sets the codigo_dir3 value for this TipoOrganismoPagadorCIE.
     * 
     * @param codigo_dir3
     */
    public void setCodigo_dir3(java.lang.String codigo_dir3) {
        this.codigo_dir3 = codigo_dir3;
    }


    /**
     * Gets the fecha_vigencia value for this TipoOrganismoPagadorCIE.
     * 
     * @return fecha_vigencia
     */
    public java.util.Date getFecha_vigencia() {
        return fecha_vigencia;
    }


    /**
     * Sets the fecha_vigencia value for this TipoOrganismoPagadorCIE.
     * 
     * @param fecha_vigencia
     */
    public void setFecha_vigencia(java.util.Date fecha_vigencia) {
        this.fecha_vigencia = fecha_vigencia;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TipoOrganismoPagadorCIE)) return false;
        TipoOrganismoPagadorCIE other = (TipoOrganismoPagadorCIE) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo_dir3==null && other.getCodigo_dir3()==null) || 
             (this.codigo_dir3!=null &&
              this.codigo_dir3.equals(other.getCodigo_dir3()))) &&
            ((this.fecha_vigencia==null && other.getFecha_vigencia()==null) || 
             (this.fecha_vigencia!=null &&
              this.fecha_vigencia.equals(other.getFecha_vigencia())));
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
        if (getCodigo_dir3() != null) {
            _hashCode += getCodigo_dir3().hashCode();
        }
        if (getFecha_vigencia() != null) {
            _hashCode += getFecha_vigencia().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TipoOrganismoPagadorCIE.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipoOrganismoPagadorCIE"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_dir3");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_dir3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_vigencia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_vigencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
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
