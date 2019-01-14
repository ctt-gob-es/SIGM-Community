/**
 * Datado_envio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Datado_envio  implements java.io.Serializable {
    private es.dipucr.notifica.ws.notifica._1_0.Identificador_envio identificador_envio;

    private es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipoIntento datado;

    private java.lang.String estado_actual;

    private java.lang.String descripcion_estado_actual;

    private java.util.Calendar fecha_actualizacion;

    private java.lang.String ncc_id_externo;

    public Datado_envio() {
    }

    public Datado_envio(
           es.dipucr.notifica.ws.notifica._1_0.Identificador_envio identificador_envio,
           es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipoIntento datado,
           java.lang.String estado_actual,
           java.lang.String descripcion_estado_actual,
           java.util.Calendar fecha_actualizacion,
           java.lang.String ncc_id_externo) {
           this.identificador_envio = identificador_envio;
           this.datado = datado;
           this.estado_actual = estado_actual;
           this.descripcion_estado_actual = descripcion_estado_actual;
           this.fecha_actualizacion = fecha_actualizacion;
           this.ncc_id_externo = ncc_id_externo;
    }


    /**
     * Gets the identificador_envio value for this Datado_envio.
     * 
     * @return identificador_envio
     */
    public es.dipucr.notifica.ws.notifica._1_0.Identificador_envio getIdentificador_envio() {
        return identificador_envio;
    }


    /**
     * Sets the identificador_envio value for this Datado_envio.
     * 
     * @param identificador_envio
     */
    public void setIdentificador_envio(es.dipucr.notifica.ws.notifica._1_0.Identificador_envio identificador_envio) {
        this.identificador_envio = identificador_envio;
    }


    /**
     * Gets the datado value for this Datado_envio.
     * 
     * @return datado
     */
    public es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipoIntento getDatado() {
        return datado;
    }


    /**
     * Sets the datado value for this Datado_envio.
     * 
     * @param datado
     */
    public void setDatado(es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipoIntento datado) {
        this.datado = datado;
    }


    /**
     * Gets the estado_actual value for this Datado_envio.
     * 
     * @return estado_actual
     */
    public java.lang.String getEstado_actual() {
        return estado_actual;
    }


    /**
     * Sets the estado_actual value for this Datado_envio.
     * 
     * @param estado_actual
     */
    public void setEstado_actual(java.lang.String estado_actual) {
        this.estado_actual = estado_actual;
    }


    /**
     * Gets the descripcion_estado_actual value for this Datado_envio.
     * 
     * @return descripcion_estado_actual
     */
    public java.lang.String getDescripcion_estado_actual() {
        return descripcion_estado_actual;
    }


    /**
     * Sets the descripcion_estado_actual value for this Datado_envio.
     * 
     * @param descripcion_estado_actual
     */
    public void setDescripcion_estado_actual(java.lang.String descripcion_estado_actual) {
        this.descripcion_estado_actual = descripcion_estado_actual;
    }


    /**
     * Gets the fecha_actualizacion value for this Datado_envio.
     * 
     * @return fecha_actualizacion
     */
    public java.util.Calendar getFecha_actualizacion() {
        return fecha_actualizacion;
    }


    /**
     * Sets the fecha_actualizacion value for this Datado_envio.
     * 
     * @param fecha_actualizacion
     */
    public void setFecha_actualizacion(java.util.Calendar fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }


    /**
     * Gets the ncc_id_externo value for this Datado_envio.
     * 
     * @return ncc_id_externo
     */
    public java.lang.String getNcc_id_externo() {
        return ncc_id_externo;
    }


    /**
     * Sets the ncc_id_externo value for this Datado_envio.
     * 
     * @param ncc_id_externo
     */
    public void setNcc_id_externo(java.lang.String ncc_id_externo) {
        this.ncc_id_externo = ncc_id_externo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Datado_envio)) return false;
        Datado_envio other = (Datado_envio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.identificador_envio==null && other.getIdentificador_envio()==null) || 
             (this.identificador_envio!=null &&
              this.identificador_envio.equals(other.getIdentificador_envio()))) &&
            ((this.datado==null && other.getDatado()==null) || 
             (this.datado!=null &&
              this.datado.equals(other.getDatado()))) &&
            ((this.estado_actual==null && other.getEstado_actual()==null) || 
             (this.estado_actual!=null &&
              this.estado_actual.equals(other.getEstado_actual()))) &&
            ((this.descripcion_estado_actual==null && other.getDescripcion_estado_actual()==null) || 
             (this.descripcion_estado_actual!=null &&
              this.descripcion_estado_actual.equals(other.getDescripcion_estado_actual()))) &&
            ((this.fecha_actualizacion==null && other.getFecha_actualizacion()==null) || 
             (this.fecha_actualizacion!=null &&
              this.fecha_actualizacion.equals(other.getFecha_actualizacion()))) &&
            ((this.ncc_id_externo==null && other.getNcc_id_externo()==null) || 
             (this.ncc_id_externo!=null &&
              this.ncc_id_externo.equals(other.getNcc_id_externo())));
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
        if (getIdentificador_envio() != null) {
            _hashCode += getIdentificador_envio().hashCode();
        }
        if (getDatado() != null) {
            _hashCode += getDatado().hashCode();
        }
        if (getEstado_actual() != null) {
            _hashCode += getEstado_actual().hashCode();
        }
        if (getDescripcion_estado_actual() != null) {
            _hashCode += getDescripcion_estado_actual().hashCode();
        }
        if (getFecha_actualizacion() != null) {
            _hashCode += getFecha_actualizacion().hashCode();
        }
        if (getNcc_id_externo() != null) {
            _hashCode += getNcc_id_externo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Datado_envio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "datado_envio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificador_envio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identificador_envio"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "identificador_envio"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "datado"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "ArrayOfTipoIntento"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado_actual");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estado_actual"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion_estado_actual");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcion_estado_actual"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_actualizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_actualizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ncc_id_externo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ncc_id_externo"));
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
