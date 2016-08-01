/**
 * OfertasRecibidas.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class OfertasRecibidas  implements java.io.Serializable {
    private java.lang.String numOfertasRecibidas;

    private java.lang.String ofertaMasAlta;

    private java.lang.String ofertaMasBaja;

    public OfertasRecibidas() {
    }

    public OfertasRecibidas(
           java.lang.String numOfertasRecibidas,
           java.lang.String ofertaMasAlta,
           java.lang.String ofertaMasBaja) {
           this.numOfertasRecibidas = numOfertasRecibidas;
           this.ofertaMasAlta = ofertaMasAlta;
           this.ofertaMasBaja = ofertaMasBaja;
    }


    /**
     * Gets the numOfertasRecibidas value for this OfertasRecibidas.
     * 
     * @return numOfertasRecibidas
     */
    public java.lang.String getNumOfertasRecibidas() {
        return numOfertasRecibidas;
    }


    /**
     * Sets the numOfertasRecibidas value for this OfertasRecibidas.
     * 
     * @param numOfertasRecibidas
     */
    public void setNumOfertasRecibidas(java.lang.String numOfertasRecibidas) {
        this.numOfertasRecibidas = numOfertasRecibidas;
    }


    /**
     * Gets the ofertaMasAlta value for this OfertasRecibidas.
     * 
     * @return ofertaMasAlta
     */
    public java.lang.String getOfertaMasAlta() {
        return ofertaMasAlta;
    }


    /**
     * Sets the ofertaMasAlta value for this OfertasRecibidas.
     * 
     * @param ofertaMasAlta
     */
    public void setOfertaMasAlta(java.lang.String ofertaMasAlta) {
        this.ofertaMasAlta = ofertaMasAlta;
    }


    /**
     * Gets the ofertaMasBaja value for this OfertasRecibidas.
     * 
     * @return ofertaMasBaja
     */
    public java.lang.String getOfertaMasBaja() {
        return ofertaMasBaja;
    }


    /**
     * Sets the ofertaMasBaja value for this OfertasRecibidas.
     * 
     * @param ofertaMasBaja
     */
    public void setOfertaMasBaja(java.lang.String ofertaMasBaja) {
        this.ofertaMasBaja = ofertaMasBaja;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OfertasRecibidas)) return false;
        OfertasRecibidas other = (OfertasRecibidas) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.numOfertasRecibidas==null && other.getNumOfertasRecibidas()==null) || 
             (this.numOfertasRecibidas!=null &&
              this.numOfertasRecibidas.equals(other.getNumOfertasRecibidas()))) &&
            ((this.ofertaMasAlta==null && other.getOfertaMasAlta()==null) || 
             (this.ofertaMasAlta!=null &&
              this.ofertaMasAlta.equals(other.getOfertaMasAlta()))) &&
            ((this.ofertaMasBaja==null && other.getOfertaMasBaja()==null) || 
             (this.ofertaMasBaja!=null &&
              this.ofertaMasBaja.equals(other.getOfertaMasBaja())));
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
        if (getNumOfertasRecibidas() != null) {
            _hashCode += getNumOfertasRecibidas().hashCode();
        }
        if (getOfertaMasAlta() != null) {
            _hashCode += getOfertaMasAlta().hashCode();
        }
        if (getOfertaMasBaja() != null) {
            _hashCode += getOfertaMasBaja().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OfertasRecibidas.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "OfertasRecibidas"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numOfertasRecibidas");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "numOfertasRecibidas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ofertaMasAlta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "ofertaMasAlta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ofertaMasBaja");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "ofertaMasBaja"));
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
