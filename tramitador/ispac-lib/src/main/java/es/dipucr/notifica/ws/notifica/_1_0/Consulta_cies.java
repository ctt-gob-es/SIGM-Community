/**
 * Consulta_cies.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Consulta_cies  implements java.io.Serializable {
    private java.lang.String organismo_emisor;

    public Consulta_cies() {
    }

    public Consulta_cies(
           java.lang.String organismo_emisor) {
           this.organismo_emisor = organismo_emisor;
    }


    /**
     * Gets the organismo_emisor value for this Consulta_cies.
     * 
     * @return organismo_emisor
     */
    public java.lang.String getOrganismo_emisor() {
        return organismo_emisor;
    }


    /**
     * Sets the organismo_emisor value for this Consulta_cies.
     * 
     * @param organismo_emisor
     */
    public void setOrganismo_emisor(java.lang.String organismo_emisor) {
        this.organismo_emisor = organismo_emisor;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Consulta_cies)) return false;
        Consulta_cies other = (Consulta_cies) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.organismo_emisor==null && other.getOrganismo_emisor()==null) || 
             (this.organismo_emisor!=null &&
              this.organismo_emisor.equals(other.getOrganismo_emisor())));
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
        if (getOrganismo_emisor() != null) {
            _hashCode += getOrganismo_emisor().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Consulta_cies.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "consulta_cies"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organismo_emisor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "organismo_emisor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
