/**
 * PublicacionesOficialesBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class PublicacionesOficialesBean  implements java.io.Serializable {
    private boolean enviarBOE;

    private boolean enviarDOUE;

    private java.util.Calendar fechaEnvioBOE;

    private java.util.Calendar fechaEnvioDOUE;

    private java.util.Calendar fechaPubDOUE;

    private java.util.Calendar fechaPubOtrosDiarios;

    private java.lang.String nombreOtrosDiarios;

    private java.lang.String publishURLOtrosDiarios;

    private boolean yaEnviadoBOE;

    private boolean yaEnviadoDOUE;

    public PublicacionesOficialesBean() {
    }

    public PublicacionesOficialesBean(
           boolean enviarBOE,
           boolean enviarDOUE,
           java.util.Calendar fechaEnvioBOE,
           java.util.Calendar fechaEnvioDOUE,
           java.util.Calendar fechaPubDOUE,
           java.util.Calendar fechaPubOtrosDiarios,
           java.lang.String nombreOtrosDiarios,
           java.lang.String publishURLOtrosDiarios,
           boolean yaEnviadoBOE,
           boolean yaEnviadoDOUE) {
           this.enviarBOE = enviarBOE;
           this.enviarDOUE = enviarDOUE;
           this.fechaEnvioBOE = fechaEnvioBOE;
           this.fechaEnvioDOUE = fechaEnvioDOUE;
           this.fechaPubDOUE = fechaPubDOUE;
           this.fechaPubOtrosDiarios = fechaPubOtrosDiarios;
           this.nombreOtrosDiarios = nombreOtrosDiarios;
           this.publishURLOtrosDiarios = publishURLOtrosDiarios;
           this.yaEnviadoBOE = yaEnviadoBOE;
           this.yaEnviadoDOUE = yaEnviadoDOUE;
    }


    /**
     * Gets the enviarBOE value for this PublicacionesOficialesBean.
     * 
     * @return enviarBOE
     */
    public boolean isEnviarBOE() {
        return enviarBOE;
    }


    /**
     * Sets the enviarBOE value for this PublicacionesOficialesBean.
     * 
     * @param enviarBOE
     */
    public void setEnviarBOE(boolean enviarBOE) {
        this.enviarBOE = enviarBOE;
    }


    /**
     * Gets the enviarDOUE value for this PublicacionesOficialesBean.
     * 
     * @return enviarDOUE
     */
    public boolean isEnviarDOUE() {
        return enviarDOUE;
    }


    /**
     * Sets the enviarDOUE value for this PublicacionesOficialesBean.
     * 
     * @param enviarDOUE
     */
    public void setEnviarDOUE(boolean enviarDOUE) {
        this.enviarDOUE = enviarDOUE;
    }


    /**
     * Gets the fechaEnvioBOE value for this PublicacionesOficialesBean.
     * 
     * @return fechaEnvioBOE
     */
    public java.util.Calendar getFechaEnvioBOE() {
        return fechaEnvioBOE;
    }


    /**
     * Sets the fechaEnvioBOE value for this PublicacionesOficialesBean.
     * 
     * @param fechaEnvioBOE
     */
    public void setFechaEnvioBOE(java.util.Calendar fechaEnvioBOE) {
        this.fechaEnvioBOE = fechaEnvioBOE;
    }


    /**
     * Gets the fechaEnvioDOUE value for this PublicacionesOficialesBean.
     * 
     * @return fechaEnvioDOUE
     */
    public java.util.Calendar getFechaEnvioDOUE() {
        return fechaEnvioDOUE;
    }


    /**
     * Sets the fechaEnvioDOUE value for this PublicacionesOficialesBean.
     * 
     * @param fechaEnvioDOUE
     */
    public void setFechaEnvioDOUE(java.util.Calendar fechaEnvioDOUE) {
        this.fechaEnvioDOUE = fechaEnvioDOUE;
    }


    /**
     * Gets the fechaPubDOUE value for this PublicacionesOficialesBean.
     * 
     * @return fechaPubDOUE
     */
    public java.util.Calendar getFechaPubDOUE() {
        return fechaPubDOUE;
    }


    /**
     * Sets the fechaPubDOUE value for this PublicacionesOficialesBean.
     * 
     * @param fechaPubDOUE
     */
    public void setFechaPubDOUE(java.util.Calendar fechaPubDOUE) {
        this.fechaPubDOUE = fechaPubDOUE;
    }


    /**
     * Gets the fechaPubOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @return fechaPubOtrosDiarios
     */
    public java.util.Calendar getFechaPubOtrosDiarios() {
        return fechaPubOtrosDiarios;
    }


    /**
     * Sets the fechaPubOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @param fechaPubOtrosDiarios
     */
    public void setFechaPubOtrosDiarios(java.util.Calendar fechaPubOtrosDiarios) {
        this.fechaPubOtrosDiarios = fechaPubOtrosDiarios;
    }


    /**
     * Gets the nombreOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @return nombreOtrosDiarios
     */
    public java.lang.String getNombreOtrosDiarios() {
        return nombreOtrosDiarios;
    }


    /**
     * Sets the nombreOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @param nombreOtrosDiarios
     */
    public void setNombreOtrosDiarios(java.lang.String nombreOtrosDiarios) {
        this.nombreOtrosDiarios = nombreOtrosDiarios;
    }


    /**
     * Gets the publishURLOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @return publishURLOtrosDiarios
     */
    public java.lang.String getPublishURLOtrosDiarios() {
        return publishURLOtrosDiarios;
    }


    /**
     * Sets the publishURLOtrosDiarios value for this PublicacionesOficialesBean.
     * 
     * @param publishURLOtrosDiarios
     */
    public void setPublishURLOtrosDiarios(java.lang.String publishURLOtrosDiarios) {
        this.publishURLOtrosDiarios = publishURLOtrosDiarios;
    }


    /**
     * Gets the yaEnviadoBOE value for this PublicacionesOficialesBean.
     * 
     * @return yaEnviadoBOE
     */
    public boolean isYaEnviadoBOE() {
        return yaEnviadoBOE;
    }


    /**
     * Sets the yaEnviadoBOE value for this PublicacionesOficialesBean.
     * 
     * @param yaEnviadoBOE
     */
    public void setYaEnviadoBOE(boolean yaEnviadoBOE) {
        this.yaEnviadoBOE = yaEnviadoBOE;
    }


    /**
     * Gets the yaEnviadoDOUE value for this PublicacionesOficialesBean.
     * 
     * @return yaEnviadoDOUE
     */
    public boolean isYaEnviadoDOUE() {
        return yaEnviadoDOUE;
    }


    /**
     * Sets the yaEnviadoDOUE value for this PublicacionesOficialesBean.
     * 
     * @param yaEnviadoDOUE
     */
    public void setYaEnviadoDOUE(boolean yaEnviadoDOUE) {
        this.yaEnviadoDOUE = yaEnviadoDOUE;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PublicacionesOficialesBean)) return false;
        PublicacionesOficialesBean other = (PublicacionesOficialesBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.enviarBOE == other.isEnviarBOE() &&
            this.enviarDOUE == other.isEnviarDOUE() &&
            ((this.fechaEnvioBOE==null && other.getFechaEnvioBOE()==null) || 
             (this.fechaEnvioBOE!=null &&
              this.fechaEnvioBOE.equals(other.getFechaEnvioBOE()))) &&
            ((this.fechaEnvioDOUE==null && other.getFechaEnvioDOUE()==null) || 
             (this.fechaEnvioDOUE!=null &&
              this.fechaEnvioDOUE.equals(other.getFechaEnvioDOUE()))) &&
            ((this.fechaPubDOUE==null && other.getFechaPubDOUE()==null) || 
             (this.fechaPubDOUE!=null &&
              this.fechaPubDOUE.equals(other.getFechaPubDOUE()))) &&
            ((this.fechaPubOtrosDiarios==null && other.getFechaPubOtrosDiarios()==null) || 
             (this.fechaPubOtrosDiarios!=null &&
              this.fechaPubOtrosDiarios.equals(other.getFechaPubOtrosDiarios()))) &&
            ((this.nombreOtrosDiarios==null && other.getNombreOtrosDiarios()==null) || 
             (this.nombreOtrosDiarios!=null &&
              this.nombreOtrosDiarios.equals(other.getNombreOtrosDiarios()))) &&
            ((this.publishURLOtrosDiarios==null && other.getPublishURLOtrosDiarios()==null) || 
             (this.publishURLOtrosDiarios!=null &&
              this.publishURLOtrosDiarios.equals(other.getPublishURLOtrosDiarios()))) &&
            this.yaEnviadoBOE == other.isYaEnviadoBOE() &&
            this.yaEnviadoDOUE == other.isYaEnviadoDOUE();
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
        _hashCode += (isEnviarBOE() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isEnviarDOUE() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getFechaEnvioBOE() != null) {
            _hashCode += getFechaEnvioBOE().hashCode();
        }
        if (getFechaEnvioDOUE() != null) {
            _hashCode += getFechaEnvioDOUE().hashCode();
        }
        if (getFechaPubDOUE() != null) {
            _hashCode += getFechaPubDOUE().hashCode();
        }
        if (getFechaPubOtrosDiarios() != null) {
            _hashCode += getFechaPubOtrosDiarios().hashCode();
        }
        if (getNombreOtrosDiarios() != null) {
            _hashCode += getNombreOtrosDiarios().hashCode();
        }
        if (getPublishURLOtrosDiarios() != null) {
            _hashCode += getPublishURLOtrosDiarios().hashCode();
        }
        _hashCode += (isYaEnviadoBOE() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isYaEnviadoDOUE() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PublicacionesOficialesBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PublicacionesOficialesBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enviarBOE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "enviarBOE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("enviarDOUE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "enviarDOUE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaEnvioBOE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fechaEnvioBOE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaEnvioDOUE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fechaEnvioDOUE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaPubDOUE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fechaPubDOUE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaPubOtrosDiarios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fechaPubOtrosDiarios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreOtrosDiarios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "nombreOtrosDiarios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publishURLOtrosDiarios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "publishURLOtrosDiarios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("yaEnviadoBOE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "yaEnviadoBOE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("yaEnviadoDOUE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "yaEnviadoDOUE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
