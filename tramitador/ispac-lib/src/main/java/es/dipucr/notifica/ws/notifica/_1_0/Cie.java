/**
 * Cie.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Cie  implements java.io.Serializable {
    private int id;

    private java.lang.String centro_impresion;

    private java.util.Date fecha_vigencia;

    private java.lang.String codigo_unidad_relacionada;

    private java.lang.String nombre_unidad_relacionada;

    private int orden;

    public Cie() {
    }

    public Cie(
           int id,
           java.lang.String centro_impresion,
           java.util.Date fecha_vigencia,
           java.lang.String codigo_unidad_relacionada,
           java.lang.String nombre_unidad_relacionada,
           int orden) {
           this.id = id;
           this.centro_impresion = centro_impresion;
           this.fecha_vigencia = fecha_vigencia;
           this.codigo_unidad_relacionada = codigo_unidad_relacionada;
           this.nombre_unidad_relacionada = nombre_unidad_relacionada;
           this.orden = orden;
    }


    /**
     * Gets the id value for this Cie.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this Cie.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the centro_impresion value for this Cie.
     * 
     * @return centro_impresion
     */
    public java.lang.String getCentro_impresion() {
        return centro_impresion;
    }


    /**
     * Sets the centro_impresion value for this Cie.
     * 
     * @param centro_impresion
     */
    public void setCentro_impresion(java.lang.String centro_impresion) {
        this.centro_impresion = centro_impresion;
    }


    /**
     * Gets the fecha_vigencia value for this Cie.
     * 
     * @return fecha_vigencia
     */
    public java.util.Date getFecha_vigencia() {
        return fecha_vigencia;
    }


    /**
     * Sets the fecha_vigencia value for this Cie.
     * 
     * @param fecha_vigencia
     */
    public void setFecha_vigencia(java.util.Date fecha_vigencia) {
        this.fecha_vigencia = fecha_vigencia;
    }


    /**
     * Gets the codigo_unidad_relacionada value for this Cie.
     * 
     * @return codigo_unidad_relacionada
     */
    public java.lang.String getCodigo_unidad_relacionada() {
        return codigo_unidad_relacionada;
    }


    /**
     * Sets the codigo_unidad_relacionada value for this Cie.
     * 
     * @param codigo_unidad_relacionada
     */
    public void setCodigo_unidad_relacionada(java.lang.String codigo_unidad_relacionada) {
        this.codigo_unidad_relacionada = codigo_unidad_relacionada;
    }


    /**
     * Gets the nombre_unidad_relacionada value for this Cie.
     * 
     * @return nombre_unidad_relacionada
     */
    public java.lang.String getNombre_unidad_relacionada() {
        return nombre_unidad_relacionada;
    }


    /**
     * Sets the nombre_unidad_relacionada value for this Cie.
     * 
     * @param nombre_unidad_relacionada
     */
    public void setNombre_unidad_relacionada(java.lang.String nombre_unidad_relacionada) {
        this.nombre_unidad_relacionada = nombre_unidad_relacionada;
    }


    /**
     * Gets the orden value for this Cie.
     * 
     * @return orden
     */
    public int getOrden() {
        return orden;
    }


    /**
     * Sets the orden value for this Cie.
     * 
     * @param orden
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Cie)) return false;
        Cie other = (Cie) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.id == other.getId() &&
            ((this.centro_impresion==null && other.getCentro_impresion()==null) || 
             (this.centro_impresion!=null &&
              this.centro_impresion.equals(other.getCentro_impresion()))) &&
            ((this.fecha_vigencia==null && other.getFecha_vigencia()==null) || 
             (this.fecha_vigencia!=null &&
              this.fecha_vigencia.equals(other.getFecha_vigencia()))) &&
            ((this.codigo_unidad_relacionada==null && other.getCodigo_unidad_relacionada()==null) || 
             (this.codigo_unidad_relacionada!=null &&
              this.codigo_unidad_relacionada.equals(other.getCodigo_unidad_relacionada()))) &&
            ((this.nombre_unidad_relacionada==null && other.getNombre_unidad_relacionada()==null) || 
             (this.nombre_unidad_relacionada!=null &&
              this.nombre_unidad_relacionada.equals(other.getNombre_unidad_relacionada()))) &&
            this.orden == other.getOrden();
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
        _hashCode += getId();
        if (getCentro_impresion() != null) {
            _hashCode += getCentro_impresion().hashCode();
        }
        if (getFecha_vigencia() != null) {
            _hashCode += getFecha_vigencia().hashCode();
        }
        if (getCodigo_unidad_relacionada() != null) {
            _hashCode += getCodigo_unidad_relacionada().hashCode();
        }
        if (getNombre_unidad_relacionada() != null) {
            _hashCode += getNombre_unidad_relacionada().hashCode();
        }
        _hashCode += getOrden();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Cie.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "cie"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("centro_impresion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "centro_impresion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_vigencia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_vigencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_unidad_relacionada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_unidad_relacionada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre_unidad_relacionada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre_unidad_relacionada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orden");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orden"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
