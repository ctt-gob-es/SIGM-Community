/**
 * DuracionContratoBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class DuracionContratoBean  implements java.io.Serializable {
    private java.lang.String duracion;

    private es.dipucr.contratacion.client.beans.Campo durationMeasure;

    private java.util.Calendar fechaFinal;

    private java.util.Calendar fechaInicio;

    public DuracionContratoBean() {
    }

    public DuracionContratoBean(
           java.lang.String duracion,
           es.dipucr.contratacion.client.beans.Campo durationMeasure,
           java.util.Calendar fechaFinal,
           java.util.Calendar fechaInicio) {
           this.duracion = duracion;
           this.durationMeasure = durationMeasure;
           this.fechaFinal = fechaFinal;
           this.fechaInicio = fechaInicio;
    }


    /**
     * Gets the duracion value for this DuracionContratoBean.
     * 
     * @return duracion
     */
    public java.lang.String getDuracion() {
        return duracion;
    }


    /**
     * Sets the duracion value for this DuracionContratoBean.
     * 
     * @param duracion
     */
    public void setDuracion(java.lang.String duracion) {
        this.duracion = duracion;
    }


    /**
     * Gets the durationMeasure value for this DuracionContratoBean.
     * 
     * @return durationMeasure
     */
    public es.dipucr.contratacion.client.beans.Campo getDurationMeasure() {
        return durationMeasure;
    }


    /**
     * Sets the durationMeasure value for this DuracionContratoBean.
     * 
     * @param durationMeasure
     */
    public void setDurationMeasure(es.dipucr.contratacion.client.beans.Campo durationMeasure) {
        this.durationMeasure = durationMeasure;
    }


    /**
     * Gets the fechaFinal value for this DuracionContratoBean.
     * 
     * @return fechaFinal
     */
    public java.util.Calendar getFechaFinal() {
        return fechaFinal;
    }


    /**
     * Sets the fechaFinal value for this DuracionContratoBean.
     * 
     * @param fechaFinal
     */
    public void setFechaFinal(java.util.Calendar fechaFinal) {
        this.fechaFinal = fechaFinal;
    }


    /**
     * Gets the fechaInicio value for this DuracionContratoBean.
     * 
     * @return fechaInicio
     */
    public java.util.Calendar getFechaInicio() {
        return fechaInicio;
    }


    /**
     * Sets the fechaInicio value for this DuracionContratoBean.
     * 
     * @param fechaInicio
     */
    public void setFechaInicio(java.util.Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DuracionContratoBean)) return false;
        DuracionContratoBean other = (DuracionContratoBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.duracion==null && other.getDuracion()==null) || 
             (this.duracion!=null &&
              this.duracion.equals(other.getDuracion()))) &&
            ((this.durationMeasure==null && other.getDurationMeasure()==null) || 
             (this.durationMeasure!=null &&
              this.durationMeasure.equals(other.getDurationMeasure()))) &&
            ((this.fechaFinal==null && other.getFechaFinal()==null) || 
             (this.fechaFinal!=null &&
              this.fechaFinal.equals(other.getFechaFinal()))) &&
            ((this.fechaInicio==null && other.getFechaInicio()==null) || 
             (this.fechaInicio!=null &&
              this.fechaInicio.equals(other.getFechaInicio())));
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
        if (getDuracion() != null) {
            _hashCode += getDuracion().hashCode();
        }
        if (getDurationMeasure() != null) {
            _hashCode += getDurationMeasure().hashCode();
        }
        if (getFechaFinal() != null) {
            _hashCode += getFechaFinal().hashCode();
        }
        if (getFechaInicio() != null) {
            _hashCode += getFechaInicio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DuracionContratoBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "DuracionContratoBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("duracion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "duracion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("durationMeasure");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "durationMeasure"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFinal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fechaFinal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaInicio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fechaInicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
