/**
 * AplicacionPresupuestaria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class AplicacionPresupuestaria  implements java.io.Serializable {
    private java.lang.String anualidad;

    private java.lang.String aplicPres;

    private java.lang.String importe;

    public AplicacionPresupuestaria() {
    }

    public AplicacionPresupuestaria(
           java.lang.String anualidad,
           java.lang.String aplicPres,
           java.lang.String importe) {
           this.anualidad = anualidad;
           this.aplicPres = aplicPres;
           this.importe = importe;
    }


    /**
     * Gets the anualidad value for this AplicacionPresupuestaria.
     * 
     * @return anualidad
     */
    public java.lang.String getAnualidad() {
        return anualidad;
    }


    /**
     * Sets the anualidad value for this AplicacionPresupuestaria.
     * 
     * @param anualidad
     */
    public void setAnualidad(java.lang.String anualidad) {
        this.anualidad = anualidad;
    }


    /**
     * Gets the aplicPres value for this AplicacionPresupuestaria.
     * 
     * @return aplicPres
     */
    public java.lang.String getAplicPres() {
        return aplicPres;
    }


    /**
     * Sets the aplicPres value for this AplicacionPresupuestaria.
     * 
     * @param aplicPres
     */
    public void setAplicPres(java.lang.String aplicPres) {
        this.aplicPres = aplicPres;
    }


    /**
     * Gets the importe value for this AplicacionPresupuestaria.
     * 
     * @return importe
     */
    public java.lang.String getImporte() {
        return importe;
    }


    /**
     * Sets the importe value for this AplicacionPresupuestaria.
     * 
     * @param importe
     */
    public void setImporte(java.lang.String importe) {
        this.importe = importe;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AplicacionPresupuestaria)) return false;
        AplicacionPresupuestaria other = (AplicacionPresupuestaria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.anualidad==null && other.getAnualidad()==null) || 
             (this.anualidad!=null &&
              this.anualidad.equals(other.getAnualidad()))) &&
            ((this.aplicPres==null && other.getAplicPres()==null) || 
             (this.aplicPres!=null &&
              this.aplicPres.equals(other.getAplicPres()))) &&
            ((this.importe==null && other.getImporte()==null) || 
             (this.importe!=null &&
              this.importe.equals(other.getImporte())));
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
        if (getAnualidad() != null) {
            _hashCode += getAnualidad().hashCode();
        }
        if (getAplicPres() != null) {
            _hashCode += getAplicPres().hashCode();
        }
        if (getImporte() != null) {
            _hashCode += getImporte().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AplicacionPresupuestaria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "AplicacionPresupuestaria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anualidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "anualidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aplicPres");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "aplicPres"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importe");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "importe"));
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
