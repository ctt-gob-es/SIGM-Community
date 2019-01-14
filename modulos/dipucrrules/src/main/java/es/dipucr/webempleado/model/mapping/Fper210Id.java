/**
 * Fper210Id.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class Fper210Id  implements java.io.Serializable {
    private java.lang.String p2Are;

    private java.lang.String p2Ord;

    private java.lang.String p2Ser;

    private java.lang.String p2Tip;

    public Fper210Id() {
    }

    public Fper210Id(
           java.lang.String p2Are,
           java.lang.String p2Ord,
           java.lang.String p2Ser,
           java.lang.String p2Tip) {
           this.p2Are = p2Are;
           this.p2Ord = p2Ord;
           this.p2Ser = p2Ser;
           this.p2Tip = p2Tip;
    }


    /**
     * Gets the p2Are value for this Fper210Id.
     * 
     * @return p2Are
     */
    public java.lang.String getP2Are() {
        return p2Are;
    }


    /**
     * Sets the p2Are value for this Fper210Id.
     * 
     * @param p2Are
     */
    public void setP2Are(java.lang.String p2Are) {
        this.p2Are = p2Are;
    }


    /**
     * Gets the p2Ord value for this Fper210Id.
     * 
     * @return p2Ord
     */
    public java.lang.String getP2Ord() {
        return p2Ord;
    }


    /**
     * Sets the p2Ord value for this Fper210Id.
     * 
     * @param p2Ord
     */
    public void setP2Ord(java.lang.String p2Ord) {
        this.p2Ord = p2Ord;
    }


    /**
     * Gets the p2Ser value for this Fper210Id.
     * 
     * @return p2Ser
     */
    public java.lang.String getP2Ser() {
        return p2Ser;
    }


    /**
     * Sets the p2Ser value for this Fper210Id.
     * 
     * @param p2Ser
     */
    public void setP2Ser(java.lang.String p2Ser) {
        this.p2Ser = p2Ser;
    }


    /**
     * Gets the p2Tip value for this Fper210Id.
     * 
     * @return p2Tip
     */
    public java.lang.String getP2Tip() {
        return p2Tip;
    }


    /**
     * Sets the p2Tip value for this Fper210Id.
     * 
     * @param p2Tip
     */
    public void setP2Tip(java.lang.String p2Tip) {
        this.p2Tip = p2Tip;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Fper210Id)) return false;
        Fper210Id other = (Fper210Id) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.p2Are==null && other.getP2Are()==null) || 
             (this.p2Are!=null &&
              this.p2Are.equals(other.getP2Are()))) &&
            ((this.p2Ord==null && other.getP2Ord()==null) || 
             (this.p2Ord!=null &&
              this.p2Ord.equals(other.getP2Ord()))) &&
            ((this.p2Ser==null && other.getP2Ser()==null) || 
             (this.p2Ser!=null &&
              this.p2Ser.equals(other.getP2Ser()))) &&
            ((this.p2Tip==null && other.getP2Tip()==null) || 
             (this.p2Tip!=null &&
              this.p2Tip.equals(other.getP2Tip())));
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
        if (getP2Are() != null) {
            _hashCode += getP2Are().hashCode();
        }
        if (getP2Ord() != null) {
            _hashCode += getP2Ord().hashCode();
        }
        if (getP2Ser() != null) {
            _hashCode += getP2Ser().hashCode();
        }
        if (getP2Tip() != null) {
            _hashCode += getP2Tip().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Fper210Id.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Fper210Id"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p2Are");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "p2Are"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p2Ord");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "p2Ord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p2Ser");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "p2Ser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p2Tip");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "p2Tip"));
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
