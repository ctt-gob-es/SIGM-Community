/**
 * FormalizacionBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class FormalizacionBean  implements java.io.Serializable {
    private java.lang.String descripcionPeriodoFormalizacionContrato;

    private es.dipucr.contratacion.client.beans.Documento docContrato;

    private java.util.Calendar fechaContrato;

    private java.lang.String numContrato;

    private java.util.Calendar periodoValidezFinContrato;

    private java.util.Calendar periodoValidezInicioContrato;

    private java.lang.String porcentajeSubcontratacion;

    private java.lang.String textoAcuerdoFormalizacion;

    public FormalizacionBean() {
    }

    public FormalizacionBean(
           java.lang.String descripcionPeriodoFormalizacionContrato,
           es.dipucr.contratacion.client.beans.Documento docContrato,
           java.util.Calendar fechaContrato,
           java.lang.String numContrato,
           java.util.Calendar periodoValidezFinContrato,
           java.util.Calendar periodoValidezInicioContrato,
           java.lang.String porcentajeSubcontratacion,
           java.lang.String textoAcuerdoFormalizacion) {
           this.descripcionPeriodoFormalizacionContrato = descripcionPeriodoFormalizacionContrato;
           this.docContrato = docContrato;
           this.fechaContrato = fechaContrato;
           this.numContrato = numContrato;
           this.periodoValidezFinContrato = periodoValidezFinContrato;
           this.periodoValidezInicioContrato = periodoValidezInicioContrato;
           this.porcentajeSubcontratacion = porcentajeSubcontratacion;
           this.textoAcuerdoFormalizacion = textoAcuerdoFormalizacion;
    }


    /**
     * Gets the descripcionPeriodoFormalizacionContrato value for this FormalizacionBean.
     * 
     * @return descripcionPeriodoFormalizacionContrato
     */
    public java.lang.String getDescripcionPeriodoFormalizacionContrato() {
        return descripcionPeriodoFormalizacionContrato;
    }


    /**
     * Sets the descripcionPeriodoFormalizacionContrato value for this FormalizacionBean.
     * 
     * @param descripcionPeriodoFormalizacionContrato
     */
    public void setDescripcionPeriodoFormalizacionContrato(java.lang.String descripcionPeriodoFormalizacionContrato) {
        this.descripcionPeriodoFormalizacionContrato = descripcionPeriodoFormalizacionContrato;
    }


    /**
     * Gets the docContrato value for this FormalizacionBean.
     * 
     * @return docContrato
     */
    public es.dipucr.contratacion.client.beans.Documento getDocContrato() {
        return docContrato;
    }


    /**
     * Sets the docContrato value for this FormalizacionBean.
     * 
     * @param docContrato
     */
    public void setDocContrato(es.dipucr.contratacion.client.beans.Documento docContrato) {
        this.docContrato = docContrato;
    }


    /**
     * Gets the fechaContrato value for this FormalizacionBean.
     * 
     * @return fechaContrato
     */
    public java.util.Calendar getFechaContrato() {
        return fechaContrato;
    }


    /**
     * Sets the fechaContrato value for this FormalizacionBean.
     * 
     * @param fechaContrato
     */
    public void setFechaContrato(java.util.Calendar fechaContrato) {
        this.fechaContrato = fechaContrato;
    }


    /**
     * Gets the numContrato value for this FormalizacionBean.
     * 
     * @return numContrato
     */
    public java.lang.String getNumContrato() {
        return numContrato;
    }


    /**
     * Sets the numContrato value for this FormalizacionBean.
     * 
     * @param numContrato
     */
    public void setNumContrato(java.lang.String numContrato) {
        this.numContrato = numContrato;
    }


    /**
     * Gets the periodoValidezFinContrato value for this FormalizacionBean.
     * 
     * @return periodoValidezFinContrato
     */
    public java.util.Calendar getPeriodoValidezFinContrato() {
        return periodoValidezFinContrato;
    }


    /**
     * Sets the periodoValidezFinContrato value for this FormalizacionBean.
     * 
     * @param periodoValidezFinContrato
     */
    public void setPeriodoValidezFinContrato(java.util.Calendar periodoValidezFinContrato) {
        this.periodoValidezFinContrato = periodoValidezFinContrato;
    }


    /**
     * Gets the periodoValidezInicioContrato value for this FormalizacionBean.
     * 
     * @return periodoValidezInicioContrato
     */
    public java.util.Calendar getPeriodoValidezInicioContrato() {
        return periodoValidezInicioContrato;
    }


    /**
     * Sets the periodoValidezInicioContrato value for this FormalizacionBean.
     * 
     * @param periodoValidezInicioContrato
     */
    public void setPeriodoValidezInicioContrato(java.util.Calendar periodoValidezInicioContrato) {
        this.periodoValidezInicioContrato = periodoValidezInicioContrato;
    }


    /**
     * Gets the porcentajeSubcontratacion value for this FormalizacionBean.
     * 
     * @return porcentajeSubcontratacion
     */
    public java.lang.String getPorcentajeSubcontratacion() {
        return porcentajeSubcontratacion;
    }


    /**
     * Sets the porcentajeSubcontratacion value for this FormalizacionBean.
     * 
     * @param porcentajeSubcontratacion
     */
    public void setPorcentajeSubcontratacion(java.lang.String porcentajeSubcontratacion) {
        this.porcentajeSubcontratacion = porcentajeSubcontratacion;
    }


    /**
     * Gets the textoAcuerdoFormalizacion value for this FormalizacionBean.
     * 
     * @return textoAcuerdoFormalizacion
     */
    public java.lang.String getTextoAcuerdoFormalizacion() {
        return textoAcuerdoFormalizacion;
    }


    /**
     * Sets the textoAcuerdoFormalizacion value for this FormalizacionBean.
     * 
     * @param textoAcuerdoFormalizacion
     */
    public void setTextoAcuerdoFormalizacion(java.lang.String textoAcuerdoFormalizacion) {
        this.textoAcuerdoFormalizacion = textoAcuerdoFormalizacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FormalizacionBean)) return false;
        FormalizacionBean other = (FormalizacionBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.descripcionPeriodoFormalizacionContrato==null && other.getDescripcionPeriodoFormalizacionContrato()==null) || 
             (this.descripcionPeriodoFormalizacionContrato!=null &&
              this.descripcionPeriodoFormalizacionContrato.equals(other.getDescripcionPeriodoFormalizacionContrato()))) &&
            ((this.docContrato==null && other.getDocContrato()==null) || 
             (this.docContrato!=null &&
              this.docContrato.equals(other.getDocContrato()))) &&
            ((this.fechaContrato==null && other.getFechaContrato()==null) || 
             (this.fechaContrato!=null &&
              this.fechaContrato.equals(other.getFechaContrato()))) &&
            ((this.numContrato==null && other.getNumContrato()==null) || 
             (this.numContrato!=null &&
              this.numContrato.equals(other.getNumContrato()))) &&
            ((this.periodoValidezFinContrato==null && other.getPeriodoValidezFinContrato()==null) || 
             (this.periodoValidezFinContrato!=null &&
              this.periodoValidezFinContrato.equals(other.getPeriodoValidezFinContrato()))) &&
            ((this.periodoValidezInicioContrato==null && other.getPeriodoValidezInicioContrato()==null) || 
             (this.periodoValidezInicioContrato!=null &&
              this.periodoValidezInicioContrato.equals(other.getPeriodoValidezInicioContrato()))) &&
            ((this.porcentajeSubcontratacion==null && other.getPorcentajeSubcontratacion()==null) || 
             (this.porcentajeSubcontratacion!=null &&
              this.porcentajeSubcontratacion.equals(other.getPorcentajeSubcontratacion()))) &&
            ((this.textoAcuerdoFormalizacion==null && other.getTextoAcuerdoFormalizacion()==null) || 
             (this.textoAcuerdoFormalizacion!=null &&
              this.textoAcuerdoFormalizacion.equals(other.getTextoAcuerdoFormalizacion())));
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
        if (getDescripcionPeriodoFormalizacionContrato() != null) {
            _hashCode += getDescripcionPeriodoFormalizacionContrato().hashCode();
        }
        if (getDocContrato() != null) {
            _hashCode += getDocContrato().hashCode();
        }
        if (getFechaContrato() != null) {
            _hashCode += getFechaContrato().hashCode();
        }
        if (getNumContrato() != null) {
            _hashCode += getNumContrato().hashCode();
        }
        if (getPeriodoValidezFinContrato() != null) {
            _hashCode += getPeriodoValidezFinContrato().hashCode();
        }
        if (getPeriodoValidezInicioContrato() != null) {
            _hashCode += getPeriodoValidezInicioContrato().hashCode();
        }
        if (getPorcentajeSubcontratacion() != null) {
            _hashCode += getPorcentajeSubcontratacion().hashCode();
        }
        if (getTextoAcuerdoFormalizacion() != null) {
            _hashCode += getTextoAcuerdoFormalizacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FormalizacionBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "FormalizacionBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionPeriodoFormalizacionContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descripcionPeriodoFormalizacionContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "docContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Documento"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fechaContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "numContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("periodoValidezFinContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "periodoValidezFinContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("periodoValidezInicioContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "periodoValidezInicioContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("porcentajeSubcontratacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "porcentajeSubcontratacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("textoAcuerdoFormalizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "textoAcuerdoFormalizacion"));
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
