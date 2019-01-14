/**
 * Certificacion_envio_respuesta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Certificacion_envio_respuesta  implements java.io.Serializable {
    private es.dipucr.notifica.ws.notifica._1_0.Identificador_envio identificador_envio;

    private java.lang.String pdf_certificado;

    private java.lang.String xml_certificado;

    private java.lang.String certificacion;

    private java.util.Calendar fecha_actualizacion;

    private java.lang.String ncc_id_externo;

    public Certificacion_envio_respuesta() {
    }

    public Certificacion_envio_respuesta(
           es.dipucr.notifica.ws.notifica._1_0.Identificador_envio identificador_envio,
           java.lang.String pdf_certificado,
           java.lang.String xml_certificado,
           java.lang.String certificacion,
           java.util.Calendar fecha_actualizacion,
           java.lang.String ncc_id_externo) {
           this.identificador_envio = identificador_envio;
           this.pdf_certificado = pdf_certificado;
           this.xml_certificado = xml_certificado;
           this.certificacion = certificacion;
           this.fecha_actualizacion = fecha_actualizacion;
           this.ncc_id_externo = ncc_id_externo;
    }


    /**
     * Gets the identificador_envio value for this Certificacion_envio_respuesta.
     * 
     * @return identificador_envio
     */
    public es.dipucr.notifica.ws.notifica._1_0.Identificador_envio getIdentificador_envio() {
        return identificador_envio;
    }


    /**
     * Sets the identificador_envio value for this Certificacion_envio_respuesta.
     * 
     * @param identificador_envio
     */
    public void setIdentificador_envio(es.dipucr.notifica.ws.notifica._1_0.Identificador_envio identificador_envio) {
        this.identificador_envio = identificador_envio;
    }


    /**
     * Gets the pdf_certificado value for this Certificacion_envio_respuesta.
     * 
     * @return pdf_certificado
     */
    public java.lang.String getPdf_certificado() {
        return pdf_certificado;
    }


    /**
     * Sets the pdf_certificado value for this Certificacion_envio_respuesta.
     * 
     * @param pdf_certificado
     */
    public void setPdf_certificado(java.lang.String pdf_certificado) {
        this.pdf_certificado = pdf_certificado;
    }


    /**
     * Gets the xml_certificado value for this Certificacion_envio_respuesta.
     * 
     * @return xml_certificado
     */
    public java.lang.String getXml_certificado() {
        return xml_certificado;
    }


    /**
     * Sets the xml_certificado value for this Certificacion_envio_respuesta.
     * 
     * @param xml_certificado
     */
    public void setXml_certificado(java.lang.String xml_certificado) {
        this.xml_certificado = xml_certificado;
    }


    /**
     * Gets the certificacion value for this Certificacion_envio_respuesta.
     * 
     * @return certificacion
     */
    public java.lang.String getCertificacion() {
        return certificacion;
    }


    /**
     * Sets the certificacion value for this Certificacion_envio_respuesta.
     * 
     * @param certificacion
     */
    public void setCertificacion(java.lang.String certificacion) {
        this.certificacion = certificacion;
    }


    /**
     * Gets the fecha_actualizacion value for this Certificacion_envio_respuesta.
     * 
     * @return fecha_actualizacion
     */
    public java.util.Calendar getFecha_actualizacion() {
        return fecha_actualizacion;
    }


    /**
     * Sets the fecha_actualizacion value for this Certificacion_envio_respuesta.
     * 
     * @param fecha_actualizacion
     */
    public void setFecha_actualizacion(java.util.Calendar fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }


    /**
     * Gets the ncc_id_externo value for this Certificacion_envio_respuesta.
     * 
     * @return ncc_id_externo
     */
    public java.lang.String getNcc_id_externo() {
        return ncc_id_externo;
    }


    /**
     * Sets the ncc_id_externo value for this Certificacion_envio_respuesta.
     * 
     * @param ncc_id_externo
     */
    public void setNcc_id_externo(java.lang.String ncc_id_externo) {
        this.ncc_id_externo = ncc_id_externo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Certificacion_envio_respuesta)) return false;
        Certificacion_envio_respuesta other = (Certificacion_envio_respuesta) obj;
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
            ((this.pdf_certificado==null && other.getPdf_certificado()==null) || 
             (this.pdf_certificado!=null &&
              this.pdf_certificado.equals(other.getPdf_certificado()))) &&
            ((this.xml_certificado==null && other.getXml_certificado()==null) || 
             (this.xml_certificado!=null &&
              this.xml_certificado.equals(other.getXml_certificado()))) &&
            ((this.certificacion==null && other.getCertificacion()==null) || 
             (this.certificacion!=null &&
              this.certificacion.equals(other.getCertificacion()))) &&
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
        if (getPdf_certificado() != null) {
            _hashCode += getPdf_certificado().hashCode();
        }
        if (getXml_certificado() != null) {
            _hashCode += getXml_certificado().hashCode();
        }
        if (getCertificacion() != null) {
            _hashCode += getCertificacion().hashCode();
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
        new org.apache.axis.description.TypeDesc(Certificacion_envio_respuesta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "certificacion_envio_respuesta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificador_envio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identificador_envio"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "identificador_envio"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pdf_certificado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pdf_certificado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xml_certificado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xml_certificado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certificacion"));
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
