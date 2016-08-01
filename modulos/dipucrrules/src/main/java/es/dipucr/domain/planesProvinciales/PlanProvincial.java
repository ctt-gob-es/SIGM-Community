/**
 * PlanProvincial.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.domain.planesProvinciales;

public class PlanProvincial  implements java.io.Serializable {
    private double aportacionAyto;

    private java.lang.String denominacion;

    private java.lang.String nombreMunicipio;

    private double porcentaje;

    private double presupuesto;

    public PlanProvincial() {
    }

    public PlanProvincial(
           double aportacionAyto,
           java.lang.String denominacion,
           java.lang.String nombreMunicipio,
           double porcentaje,
           double presupuesto) {
           this.aportacionAyto = aportacionAyto;
           this.denominacion = denominacion;
           this.nombreMunicipio = nombreMunicipio;
           this.porcentaje = porcentaje;
           this.presupuesto = presupuesto;
    }


    /**
     * Gets the aportacionAyto value for this PlanProvincial.
     * 
     * @return aportacionAyto
     */
    public double getAportacionAyto() {
        return aportacionAyto;
    }


    /**
     * Sets the aportacionAyto value for this PlanProvincial.
     * 
     * @param aportacionAyto
     */
    public void setAportacionAyto(double aportacionAyto) {
        this.aportacionAyto = aportacionAyto;
    }


    /**
     * Gets the denominacion value for this PlanProvincial.
     * 
     * @return denominacion
     */
    public java.lang.String getDenominacion() {
        return denominacion;
    }


    /**
     * Sets the denominacion value for this PlanProvincial.
     * 
     * @param denominacion
     */
    public void setDenominacion(java.lang.String denominacion) {
        this.denominacion = denominacion;
    }


    /**
     * Gets the nombreMunicipio value for this PlanProvincial.
     * 
     * @return nombreMunicipio
     */
    public java.lang.String getNombreMunicipio() {
        return nombreMunicipio;
    }


    /**
     * Sets the nombreMunicipio value for this PlanProvincial.
     * 
     * @param nombreMunicipio
     */
    public void setNombreMunicipio(java.lang.String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }


    /**
     * Gets the porcentaje value for this PlanProvincial.
     * 
     * @return porcentaje
     */
    public double getPorcentaje() {
        return porcentaje;
    }


    /**
     * Sets the porcentaje value for this PlanProvincial.
     * 
     * @param porcentaje
     */
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }


    /**
     * Gets the presupuesto value for this PlanProvincial.
     * 
     * @return presupuesto
     */
    public double getPresupuesto() {
        return presupuesto;
    }


    /**
     * Sets the presupuesto value for this PlanProvincial.
     * 
     * @param presupuesto
     */
    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PlanProvincial)) return false;
        PlanProvincial other = (PlanProvincial) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.aportacionAyto == other.getAportacionAyto() &&
            ((this.denominacion==null && other.getDenominacion()==null) || 
             (this.denominacion!=null &&
              this.denominacion.equals(other.getDenominacion()))) &&
            ((this.nombreMunicipio==null && other.getNombreMunicipio()==null) || 
             (this.nombreMunicipio!=null &&
              this.nombreMunicipio.equals(other.getNombreMunicipio()))) &&
            this.porcentaje == other.getPorcentaje() &&
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
        _hashCode += new Double(getAportacionAyto()).hashCode();
        if (getDenominacion() != null) {
            _hashCode += getDenominacion().hashCode();
        }
        if (getNombreMunicipio() != null) {
            _hashCode += getNombreMunicipio().hashCode();
        }
        _hashCode += new Double(getPorcentaje()).hashCode();
        _hashCode += new Double(getPresupuesto()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PlanProvincial.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "PlanProvincial"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aportacionAyto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "aportacionAyto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "denominacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreMunicipio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "nombreMunicipio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("porcentaje");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "porcentaje"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
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
