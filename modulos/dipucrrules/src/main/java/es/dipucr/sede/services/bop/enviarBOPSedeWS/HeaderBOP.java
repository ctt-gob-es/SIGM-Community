/**
 * HeaderBOP.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.sede.services.bop.enviarBOPSedeWS;

public class HeaderBOP  implements java.io.Serializable {
    private int idDocumento;

    private java.lang.String fecha_boletin;

    private int num_boletin;

    private int num_paginas;

    public HeaderBOP() {
    }

    public HeaderBOP(
           int idDocumento,
           java.lang.String fecha_boletin,
           int num_boletin,
           int num_paginas) {
           this.idDocumento = idDocumento;
           this.fecha_boletin = fecha_boletin;
           this.num_boletin = num_boletin;
           this.num_paginas = num_paginas;
    }


    /**
     * Gets the idDocumento value for this HeaderBOP.
     * 
     * @return idDocumento
     */
    public int getIdDocumento() {
        return idDocumento;
    }


    /**
     * Sets the idDocumento value for this HeaderBOP.
     * 
     * @param idDocumento
     */
    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }


    /**
     * Gets the fecha_boletin value for this HeaderBOP.
     * 
     * @return fecha_boletin
     */
    public java.lang.String getFecha_boletin() {
        return fecha_boletin;
    }


    /**
     * Sets the fecha_boletin value for this HeaderBOP.
     * 
     * @param fecha_boletin
     */
    public void setFecha_boletin(java.lang.String fecha_boletin) {
        this.fecha_boletin = fecha_boletin;
    }


    /**
     * Gets the num_boletin value for this HeaderBOP.
     * 
     * @return num_boletin
     */
    public int getNum_boletin() {
        return num_boletin;
    }


    /**
     * Sets the num_boletin value for this HeaderBOP.
     * 
     * @param num_boletin
     */
    public void setNum_boletin(int num_boletin) {
        this.num_boletin = num_boletin;
    }


    /**
     * Gets the num_paginas value for this HeaderBOP.
     * 
     * @return num_paginas
     */
    public int getNum_paginas() {
        return num_paginas;
    }


    /**
     * Sets the num_paginas value for this HeaderBOP.
     * 
     * @param num_paginas
     */
    public void setNum_paginas(int num_paginas) {
        this.num_paginas = num_paginas;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HeaderBOP)) return false;
        HeaderBOP other = (HeaderBOP) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.idDocumento == other.getIdDocumento() &&
            ((this.fecha_boletin==null && other.getFecha_boletin()==null) || 
             (this.fecha_boletin!=null &&
              this.fecha_boletin.equals(other.getFecha_boletin()))) &&
            this.num_boletin == other.getNum_boletin() &&
            this.num_paginas == other.getNum_paginas();
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
        _hashCode += getIdDocumento();
        if (getFecha_boletin() != null) {
            _hashCode += getFecha_boletin().hashCode();
        }
        _hashCode += getNum_boletin();
        _hashCode += getNum_paginas();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HeaderBOP.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://enviarBOPSedeWS.bop.services.sede.dipucr.es", "headerBOP"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_boletin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_boletin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_boletin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "num_boletin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_paginas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "num_paginas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
