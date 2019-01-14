/**
 * ResultadoInfoEnvio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class ResultadoInfoEnvio  implements java.io.Serializable {
    private es.dipucr.notifica.ws.notifica._1_0.Tipo_envio infoEnvio;

    private boolean certificada;

    private java.lang.String codigo_respuesta;

    private java.lang.String descripcion_respuesta;

    public ResultadoInfoEnvio() {
    }

    public ResultadoInfoEnvio(
           es.dipucr.notifica.ws.notifica._1_0.Tipo_envio infoEnvio,
           boolean certificada,
           java.lang.String codigo_respuesta,
           java.lang.String descripcion_respuesta) {
           this.infoEnvio = infoEnvio;
           this.certificada = certificada;
           this.codigo_respuesta = codigo_respuesta;
           this.descripcion_respuesta = descripcion_respuesta;
    }


    /**
     * Gets the infoEnvio value for this ResultadoInfoEnvio.
     * 
     * @return infoEnvio
     */
    public es.dipucr.notifica.ws.notifica._1_0.Tipo_envio getInfoEnvio() {
        return infoEnvio;
    }


    /**
     * Sets the infoEnvio value for this ResultadoInfoEnvio.
     * 
     * @param infoEnvio
     */
    public void setInfoEnvio(es.dipucr.notifica.ws.notifica._1_0.Tipo_envio infoEnvio) {
        this.infoEnvio = infoEnvio;
    }


    /**
     * Gets the certificada value for this ResultadoInfoEnvio.
     * 
     * @return certificada
     */
    public boolean isCertificada() {
        return certificada;
    }


    /**
     * Sets the certificada value for this ResultadoInfoEnvio.
     * 
     * @param certificada
     */
    public void setCertificada(boolean certificada) {
        this.certificada = certificada;
    }


    /**
     * Gets the codigo_respuesta value for this ResultadoInfoEnvio.
     * 
     * @return codigo_respuesta
     */
    public java.lang.String getCodigo_respuesta() {
        return codigo_respuesta;
    }


    /**
     * Sets the codigo_respuesta value for this ResultadoInfoEnvio.
     * 
     * @param codigo_respuesta
     */
    public void setCodigo_respuesta(java.lang.String codigo_respuesta) {
        this.codigo_respuesta = codigo_respuesta;
    }


    /**
     * Gets the descripcion_respuesta value for this ResultadoInfoEnvio.
     * 
     * @return descripcion_respuesta
     */
    public java.lang.String getDescripcion_respuesta() {
        return descripcion_respuesta;
    }


    /**
     * Sets the descripcion_respuesta value for this ResultadoInfoEnvio.
     * 
     * @param descripcion_respuesta
     */
    public void setDescripcion_respuesta(java.lang.String descripcion_respuesta) {
        this.descripcion_respuesta = descripcion_respuesta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResultadoInfoEnvio)) return false;
        ResultadoInfoEnvio other = (ResultadoInfoEnvio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.infoEnvio==null && other.getInfoEnvio()==null) || 
             (this.infoEnvio!=null &&
              this.infoEnvio.equals(other.getInfoEnvio()))) &&
            this.certificada == other.isCertificada() &&
            ((this.codigo_respuesta==null && other.getCodigo_respuesta()==null) || 
             (this.codigo_respuesta!=null &&
              this.codigo_respuesta.equals(other.getCodigo_respuesta()))) &&
            ((this.descripcion_respuesta==null && other.getDescripcion_respuesta()==null) || 
             (this.descripcion_respuesta!=null &&
              this.descripcion_respuesta.equals(other.getDescripcion_respuesta())));
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
        if (getInfoEnvio() != null) {
            _hashCode += getInfoEnvio().hashCode();
        }
        _hashCode += (isCertificada() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getCodigo_respuesta() != null) {
            _hashCode += getCodigo_respuesta().hashCode();
        }
        if (getDescripcion_respuesta() != null) {
            _hashCode += getDescripcion_respuesta().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResultadoInfoEnvio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultadoInfoEnvio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("infoEnvio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "infoEnvio"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_envio"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certificada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_respuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_respuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion_respuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcion_respuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
