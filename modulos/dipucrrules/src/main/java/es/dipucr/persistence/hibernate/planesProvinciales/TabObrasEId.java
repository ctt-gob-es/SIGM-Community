/**
 * TabObrasEId.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.persistence.hibernate.planesProvinciales;

public class TabObrasEId  implements java.io.Serializable {
    private short anooh;

    private short cmunoh;

    private short numoboh;

    private short planoh;

    private short subobrao;

    public TabObrasEId() {
    }

    public TabObrasEId(
           short anooh,
           short cmunoh,
           short numoboh,
           short planoh,
           short subobrao) {
           this.anooh = anooh;
           this.cmunoh = cmunoh;
           this.numoboh = numoboh;
           this.planoh = planoh;
           this.subobrao = subobrao;
    }


    /**
     * Gets the anooh value for this TabObrasEId.
     * 
     * @return anooh
     */
    public short getAnooh() {
        return anooh;
    }


    /**
     * Sets the anooh value for this TabObrasEId.
     * 
     * @param anooh
     */
    public void setAnooh(short anooh) {
        this.anooh = anooh;
    }


    /**
     * Gets the cmunoh value for this TabObrasEId.
     * 
     * @return cmunoh
     */
    public short getCmunoh() {
        return cmunoh;
    }


    /**
     * Sets the cmunoh value for this TabObrasEId.
     * 
     * @param cmunoh
     */
    public void setCmunoh(short cmunoh) {
        this.cmunoh = cmunoh;
    }


    /**
     * Gets the numoboh value for this TabObrasEId.
     * 
     * @return numoboh
     */
    public short getNumoboh() {
        return numoboh;
    }


    /**
     * Sets the numoboh value for this TabObrasEId.
     * 
     * @param numoboh
     */
    public void setNumoboh(short numoboh) {
        this.numoboh = numoboh;
    }


    /**
     * Gets the planoh value for this TabObrasEId.
     * 
     * @return planoh
     */
    public short getPlanoh() {
        return planoh;
    }


    /**
     * Sets the planoh value for this TabObrasEId.
     * 
     * @param planoh
     */
    public void setPlanoh(short planoh) {
        this.planoh = planoh;
    }


    /**
     * Gets the subobrao value for this TabObrasEId.
     * 
     * @return subobrao
     */
    public short getSubobrao() {
        return subobrao;
    }


    /**
     * Sets the subobrao value for this TabObrasEId.
     * 
     * @param subobrao
     */
    public void setSubobrao(short subobrao) {
        this.subobrao = subobrao;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TabObrasEId)) return false;
        TabObrasEId other = (TabObrasEId) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.anooh == other.getAnooh() &&
            this.cmunoh == other.getCmunoh() &&
            this.numoboh == other.getNumoboh() &&
            this.planoh == other.getPlanoh() &&
            this.subobrao == other.getSubobrao();
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
        _hashCode += getAnooh();
        _hashCode += getCmunoh();
        _hashCode += getNumoboh();
        _hashCode += getPlanoh();
        _hashCode += getSubobrao();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TabObrasEId.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://planesProvinciales.hibernate.persistence.dipucr.es", "TabObrasEId"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anooh");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.hibernate.persistence.dipucr.es", "anooh"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cmunoh");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.hibernate.persistence.dipucr.es", "cmunoh"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numoboh");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.hibernate.persistence.dipucr.es", "numoboh"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("planoh");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.hibernate.persistence.dipucr.es", "planoh"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subobrao");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.hibernate.persistence.dipucr.es", "subobrao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
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
