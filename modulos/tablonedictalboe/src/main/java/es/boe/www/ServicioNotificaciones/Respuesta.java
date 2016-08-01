/**
 * Respuesta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.boe.www.ServicioNotificaciones;


/**
 * Resultado de la operacion
 */
public class Respuesta  implements java.io.Serializable {
    private java.lang.String fecha;

    private es.boe.www.ServicioNotificaciones.Resultado resultado;

    private java.lang.String idEnvio;

    private es.boe.www.ServicioNotificaciones.ListaAnuncios anuncios;

    public Respuesta() {
    }

    public Respuesta(
           java.lang.String fecha,
           es.boe.www.ServicioNotificaciones.Resultado resultado,
           java.lang.String idEnvio,
           es.boe.www.ServicioNotificaciones.ListaAnuncios anuncios) {
           this.fecha = fecha;
           this.resultado = resultado;
           this.idEnvio = idEnvio;
           this.anuncios = anuncios;
    }


    /**
     * Gets the fecha value for this Respuesta.
     * 
     * @return fecha
     */
    public java.lang.String getFecha() {
        return fecha;
    }


    /**
     * Sets the fecha value for this Respuesta.
     * 
     * @param fecha
     */
    public void setFecha(java.lang.String fecha) {
        this.fecha = fecha;
    }


    /**
     * Gets the resultado value for this Respuesta.
     * 
     * @return resultado
     */
    public es.boe.www.ServicioNotificaciones.Resultado getResultado() {
        return resultado;
    }


    /**
     * Sets the resultado value for this Respuesta.
     * 
     * @param resultado
     */
    public void setResultado(es.boe.www.ServicioNotificaciones.Resultado resultado) {
        this.resultado = resultado;
    }


    /**
     * Gets the idEnvio value for this Respuesta.
     * 
     * @return idEnvio
     */
    public java.lang.String getIdEnvio() {
        return idEnvio;
    }


    /**
     * Sets the idEnvio value for this Respuesta.
     * 
     * @param idEnvio
     */
    public void setIdEnvio(java.lang.String idEnvio) {
        this.idEnvio = idEnvio;
    }


    /**
     * Gets the anuncios value for this Respuesta.
     * 
     * @return anuncios
     */
    public es.boe.www.ServicioNotificaciones.ListaAnuncios getAnuncios() {
        return anuncios;
    }


    /**
     * Sets the anuncios value for this Respuesta.
     * 
     * @param anuncios
     */
    public void setAnuncios(es.boe.www.ServicioNotificaciones.ListaAnuncios anuncios) {
        this.anuncios = anuncios;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Respuesta)) return false;
        Respuesta other = (Respuesta) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fecha==null && other.getFecha()==null) || 
             (this.fecha!=null &&
              this.fecha.equals(other.getFecha()))) &&
            ((this.resultado==null && other.getResultado()==null) || 
             (this.resultado!=null &&
              this.resultado.equals(other.getResultado()))) &&
            ((this.idEnvio==null && other.getIdEnvio()==null) || 
             (this.idEnvio!=null &&
              this.idEnvio.equals(other.getIdEnvio()))) &&
            ((this.anuncios==null && other.getAnuncios()==null) || 
             (this.anuncios!=null &&
              this.anuncios.equals(other.getAnuncios())));
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
        if (getFecha() != null) {
            _hashCode += getFecha().hashCode();
        }
        if (getResultado() != null) {
            _hashCode += getResultado().hashCode();
        }
        if (getIdEnvio() != null) {
            _hashCode += getIdEnvio().hashCode();
        }
        if (getAnuncios() != null) {
            _hashCode += getAnuncios().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Respuesta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.boe.es/ServicioNotificaciones/", "Respuesta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resultado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.boe.es/ServicioNotificaciones/", "Resultado"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idEnvio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idEnvio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anuncios");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anuncios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.boe.es/ServicioNotificaciones/", "ListaAnuncios"));
        elemField.setMinOccurs(0);
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
