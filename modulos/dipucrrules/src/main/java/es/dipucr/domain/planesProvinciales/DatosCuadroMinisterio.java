/**
 * DatosCuadroMinisterio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.domain.planesProvinciales;

public class DatosCuadroMinisterio  implements java.io.Serializable {
    private double ayuntamiento;

    private int codigo;

    private java.lang.String denoobra;

    private double diputacion;

    private double estado;

    private java.lang.String municipio;

    private double presupuesto;

    public DatosCuadroMinisterio() {
    }

    public DatosCuadroMinisterio(
           double ayuntamiento,
           int codigo,
           java.lang.String denoobra,
           double diputacion,
           double estado,
           java.lang.String municipio,
           double presupuesto) {
           this.ayuntamiento = ayuntamiento;
           this.codigo = codigo;
           this.denoobra = denoobra;
           this.diputacion = diputacion;
           this.estado = estado;
           this.municipio = municipio;
           this.presupuesto = presupuesto;
    }


    /**
     * Gets the ayuntamiento value for this DatosCuadroMinisterio.
     * 
     * @return ayuntamiento
     */
    public double getAyuntamiento() {
        return ayuntamiento;
    }


    /**
     * Sets the ayuntamiento value for this DatosCuadroMinisterio.
     * 
     * @param ayuntamiento
     */
    public void setAyuntamiento(double ayuntamiento) {
        this.ayuntamiento = ayuntamiento;
    }


    /**
     * Gets the codigo value for this DatosCuadroMinisterio.
     * 
     * @return codigo
     */
    public int getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this DatosCuadroMinisterio.
     * 
     * @param codigo
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the denoobra value for this DatosCuadroMinisterio.
     * 
     * @return denoobra
     */
    public java.lang.String getDenoobra() {
        return denoobra;
    }


    /**
     * Sets the denoobra value for this DatosCuadroMinisterio.
     * 
     * @param denoobra
     */
    public void setDenoobra(java.lang.String denoobra) {
        this.denoobra = denoobra;
    }


    /**
     * Gets the diputacion value for this DatosCuadroMinisterio.
     * 
     * @return diputacion
     */
    public double getDiputacion() {
        return diputacion;
    }


    /**
     * Sets the diputacion value for this DatosCuadroMinisterio.
     * 
     * @param diputacion
     */
    public void setDiputacion(double diputacion) {
        this.diputacion = diputacion;
    }


    /**
     * Gets the estado value for this DatosCuadroMinisterio.
     * 
     * @return estado
     */
    public double getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this DatosCuadroMinisterio.
     * 
     * @param estado
     */
    public void setEstado(double estado) {
        this.estado = estado;
    }


    /**
     * Gets the municipio value for this DatosCuadroMinisterio.
     * 
     * @return municipio
     */
    public java.lang.String getMunicipio() {
        return municipio;
    }


    /**
     * Sets the municipio value for this DatosCuadroMinisterio.
     * 
     * @param municipio
     */
    public void setMunicipio(java.lang.String municipio) {
        this.municipio = municipio;
    }


    /**
     * Gets the presupuesto value for this DatosCuadroMinisterio.
     * 
     * @return presupuesto
     */
    public double getPresupuesto() {
        return presupuesto;
    }


    /**
     * Sets the presupuesto value for this DatosCuadroMinisterio.
     * 
     * @param presupuesto
     */
    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosCuadroMinisterio)) return false;
        DatosCuadroMinisterio other = (DatosCuadroMinisterio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.ayuntamiento == other.getAyuntamiento() &&
            this.codigo == other.getCodigo() &&
            ((this.denoobra==null && other.getDenoobra()==null) || 
             (this.denoobra!=null &&
              this.denoobra.equals(other.getDenoobra()))) &&
            this.diputacion == other.getDiputacion() &&
            this.estado == other.getEstado() &&
            ((this.municipio==null && other.getMunicipio()==null) || 
             (this.municipio!=null &&
              this.municipio.equals(other.getMunicipio()))) &&
            this.presupuesto == other.getPresupuesto();
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
        _hashCode += getCodigo();
        if (getDenoobra() != null) {
            _hashCode += getDenoobra().hashCode();
        }
        _hashCode += new Double(getDiputacion()).hashCode();
        _hashCode += new Double(getEstado()).hashCode();
        if (getMunicipio() != null) {
            _hashCode += getMunicipio().hashCode();
        }
        _hashCode += new Double(getPresupuesto()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosCuadroMinisterio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "DatosCuadroMinisterio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ayuntamiento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ayuntamiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denoobra");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "denoobra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
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
        elemField.setFieldName("municipio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "municipio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presupuesto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "presupuesto"));
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
