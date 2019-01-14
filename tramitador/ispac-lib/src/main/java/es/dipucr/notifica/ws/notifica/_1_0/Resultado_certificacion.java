/**
 * Resultado_certificacion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Resultado_certificacion  implements java.io.Serializable {
    private es.dipucr.notifica.ws.notifica._1_0.Certificacion_envio_respuesta certificacion;

    private java.lang.String codigo_respuesta;

    private java.lang.String descripcion_respuesta;

    public Resultado_certificacion() {
    }

    public Resultado_certificacion(
           es.dipucr.notifica.ws.notifica._1_0.Certificacion_envio_respuesta certificacion,
           java.lang.String codigo_respuesta,
           java.lang.String descripcion_respuesta) {
           this.certificacion = certificacion;
           this.codigo_respuesta = codigo_respuesta;
           this.descripcion_respuesta = descripcion_respuesta;
    }


    /**
     * Gets the certificacion value for this Resultado_certificacion.
     * 
     * @return certificacion
     */
    public es.dipucr.notifica.ws.notifica._1_0.Certificacion_envio_respuesta getCertificacion() {
        return certificacion;
    }


    /**
     * Sets the certificacion value for this Resultado_certificacion.
     * 
     * @param certificacion
     */
    public void setCertificacion(es.dipucr.notifica.ws.notifica._1_0.Certificacion_envio_respuesta certificacion) {
        this.certificacion = certificacion;
    }


    /**
     * Gets the codigo_respuesta value for this Resultado_certificacion.
     * 
     * @return codigo_respuesta
     */
    public java.lang.String getCodigo_respuesta() {
        return codigo_respuesta;
    }


    /**
     * Sets the codigo_respuesta value for this Resultado_certificacion.
     * 
     * @param codigo_respuesta
     */
    public void setCodigo_respuesta(java.lang.String codigo_respuesta) {
        this.codigo_respuesta = codigo_respuesta;
    }


    /**
     * Gets the descripcion_respuesta value for this Resultado_certificacion.
     * 
     * @return descripcion_respuesta
     */
    public java.lang.String getDescripcion_respuesta() {
        return descripcion_respuesta;
    }


    /**
     * Sets the descripcion_respuesta value for this Resultado_certificacion.
     * 
     * @param descripcion_respuesta
     */
    public void setDescripcion_respuesta(java.lang.String descripcion_respuesta) {
        this.descripcion_respuesta = descripcion_respuesta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Resultado_certificacion)) return false;
        Resultado_certificacion other = (Resultado_certificacion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.certificacion==null && other.getCertificacion()==null) || 
             (this.certificacion!=null &&
              this.certificacion.equals(other.getCertificacion()))) &&
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
        if (getCertificacion() != null) {
            _hashCode += getCertificacion().hashCode();
        }
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
        new org.apache.axis.description.TypeDesc(Resultado_certificacion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_certificacion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "certificacion_envio_respuesta"));
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
