/**
 * ObtenerPersonaPorNIFResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.atm2;

public class ObtenerPersonaPorNIFResponse  implements java.io.Serializable {
    private es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult obtenerPersonaPorNIFResult;

    public ObtenerPersonaPorNIFResponse() {
    }

    public ObtenerPersonaPorNIFResponse(
           es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult obtenerPersonaPorNIFResult) {
           this.obtenerPersonaPorNIFResult = obtenerPersonaPorNIFResult;
    }


    /**
     * Gets the obtenerPersonaPorNIFResult value for this ObtenerPersonaPorNIFResponse.
     * 
     * @return obtenerPersonaPorNIFResult
     */
    public es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult getObtenerPersonaPorNIFResult() {
        return obtenerPersonaPorNIFResult;
    }


    /**
     * Sets the obtenerPersonaPorNIFResult value for this ObtenerPersonaPorNIFResponse.
     * 
     * @param obtenerPersonaPorNIFResult
     */
    public void setObtenerPersonaPorNIFResult(es.atm2.ObtenerPersonaPorNIFResponseObtenerPersonaPorNIFResult obtenerPersonaPorNIFResult) {
        this.obtenerPersonaPorNIFResult = obtenerPersonaPorNIFResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObtenerPersonaPorNIFResponse)) return false;
        ObtenerPersonaPorNIFResponse other = (ObtenerPersonaPorNIFResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.obtenerPersonaPorNIFResult==null && other.getObtenerPersonaPorNIFResult()==null) || 
             (this.obtenerPersonaPorNIFResult!=null &&
              this.obtenerPersonaPorNIFResult.equals(other.getObtenerPersonaPorNIFResult())));
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
        if (getObtenerPersonaPorNIFResult() != null) {
            _hashCode += getObtenerPersonaPorNIFResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ObtenerPersonaPorNIFResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerPersonaPorNIFResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obtenerPersonaPorNIFResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerPersonaPorNIFResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerPersonaPorNIFResponse>ObtenerPersonaPorNIFResult"));
        elemField.setMinOccurs(0);
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
