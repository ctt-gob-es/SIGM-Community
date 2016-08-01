/**
 * ResumePlanProvincial.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.domain.planesProvinciales;

public class ResumePlanProvincial  implements java.io.Serializable {
    private double ayuntamiento;

    private double diputacion;

    private double estado;

    private java.lang.String finalidad;

    private double obras;

    private int tipoObra;

    private double total;

    public ResumePlanProvincial() {
    }

    public ResumePlanProvincial(
           double ayuntamiento,
           double diputacion,
           double estado,
           java.lang.String finalidad,
           double obras,
           int tipoObra,
           double total) {
           this.ayuntamiento = ayuntamiento;
           this.diputacion = diputacion;
           this.estado = estado;
           this.finalidad = finalidad;
           this.obras = obras;
           this.tipoObra = tipoObra;
           this.total = total;
    }


    /**
     * Gets the ayuntamiento value for this ResumePlanProvincial.
     * 
     * @return ayuntamiento
     */
    public double getAyuntamiento() {
        return ayuntamiento;
    }


    /**
     * Sets the ayuntamiento value for this ResumePlanProvincial.
     * 
     * @param ayuntamiento
     */
    public void setAyuntamiento(double ayuntamiento) {
        this.ayuntamiento = ayuntamiento;
    }


    /**
     * Gets the diputacion value for this ResumePlanProvincial.
     * 
     * @return diputacion
     */
    public double getDiputacion() {
        return diputacion;
    }


    /**
     * Sets the diputacion value for this ResumePlanProvincial.
     * 
     * @param diputacion
     */
    public void setDiputacion(double diputacion) {
        this.diputacion = diputacion;
    }


    /**
     * Gets the estado value for this ResumePlanProvincial.
     * 
     * @return estado
     */
    public double getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this ResumePlanProvincial.
     * 
     * @param estado
     */
    public void setEstado(double estado) {
        this.estado = estado;
    }


    /**
     * Gets the finalidad value for this ResumePlanProvincial.
     * 
     * @return finalidad
     */
    public java.lang.String getFinalidad() {
        return finalidad;
    }


    /**
     * Sets the finalidad value for this ResumePlanProvincial.
     * 
     * @param finalidad
     */
    public void setFinalidad(java.lang.String finalidad) {
        this.finalidad = finalidad;
    }


    /**
     * Gets the obras value for this ResumePlanProvincial.
     * 
     * @return obras
     */
    public double getObras() {
        return obras;
    }


    /**
     * Sets the obras value for this ResumePlanProvincial.
     * 
     * @param obras
     */
    public void setObras(double obras) {
        this.obras = obras;
    }


    /**
     * Gets the tipoObra value for this ResumePlanProvincial.
     * 
     * @return tipoObra
     */
    public int getTipoObra() {
        return tipoObra;
    }


    /**
     * Sets the tipoObra value for this ResumePlanProvincial.
     * 
     * @param tipoObra
     */
    public void setTipoObra(int tipoObra) {
        this.tipoObra = tipoObra;
    }


    /**
     * Gets the total value for this ResumePlanProvincial.
     * 
     * @return total
     */
    public double getTotal() {
        return total;
    }


    /**
     * Sets the total value for this ResumePlanProvincial.
     * 
     * @param total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResumePlanProvincial)) return false;
        ResumePlanProvincial other = (ResumePlanProvincial) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.ayuntamiento == other.getAyuntamiento() &&
            this.diputacion == other.getDiputacion() &&
            this.estado == other.getEstado() &&
            ((this.finalidad==null && other.getFinalidad()==null) || 
             (this.finalidad!=null &&
              this.finalidad.equals(other.getFinalidad()))) &&
            this.obras == other.getObras() &&
            this.tipoObra == other.getTipoObra() &&
            this.total == other.getTotal();
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
        _hashCode += new Double(getAyuntamiento()).hashCode();
        _hashCode += new Double(getDiputacion()).hashCode();
        _hashCode += new Double(getEstado()).hashCode();
        if (getFinalidad() != null) {
            _hashCode += getFinalidad().hashCode();
        }
        _hashCode += new Double(getObras()).hashCode();
        _hashCode += getTipoObra();
        _hashCode += new Double(getTotal()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResumePlanProvincial.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ResumePlanProvincial"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ayuntamiento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ayuntamiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("diputacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "diputacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("finalidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "finalidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obras");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "obras"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoObra");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "tipoObra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("total");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "total"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
