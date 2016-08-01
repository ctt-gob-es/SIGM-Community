/**
 * ObtenerCertificadoEmpadronamientoResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.atm2;

public class ObtenerCertificadoEmpadronamientoResponse  implements java.io.Serializable {
    private es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult obtenerCertificadoEmpadronamientoResult;

    public ObtenerCertificadoEmpadronamientoResponse() {
    }

    public ObtenerCertificadoEmpadronamientoResponse(
           es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult obtenerCertificadoEmpadronamientoResult) {
           this.obtenerCertificadoEmpadronamientoResult = obtenerCertificadoEmpadronamientoResult;
    }


    /**
     * Gets the obtenerCertificadoEmpadronamientoResult value for this ObtenerCertificadoEmpadronamientoResponse.
     * 
     * @return obtenerCertificadoEmpadronamientoResult
     */
    public es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult getObtenerCertificadoEmpadronamientoResult() {
        return obtenerCertificadoEmpadronamientoResult;
    }


    /**
     * Sets the obtenerCertificadoEmpadronamientoResult value for this ObtenerCertificadoEmpadronamientoResponse.
     * 
     * @param obtenerCertificadoEmpadronamientoResult
     */
    public void setObtenerCertificadoEmpadronamientoResult(es.atm2.ObtenerCertificadoEmpadronamientoResponseObtenerCertificadoEmpadronamientoResult obtenerCertificadoEmpadronamientoResult) {
        this.obtenerCertificadoEmpadronamientoResult = obtenerCertificadoEmpadronamientoResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObtenerCertificadoEmpadronamientoResponse)) return false;
        ObtenerCertificadoEmpadronamientoResponse other = (ObtenerCertificadoEmpadronamientoResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.obtenerCertificadoEmpadronamientoResult==null && other.getObtenerCertificadoEmpadronamientoResult()==null) || 
             (this.obtenerCertificadoEmpadronamientoResult!=null &&
              this.obtenerCertificadoEmpadronamientoResult.equals(other.getObtenerCertificadoEmpadronamientoResult())));
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
        if (getObtenerCertificadoEmpadronamientoResult() != null) {
            _hashCode += getObtenerCertificadoEmpadronamientoResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ObtenerCertificadoEmpadronamientoResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerCertificadoEmpadronamientoResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obtenerCertificadoEmpadronamientoResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerCertificadoEmpadronamientoResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerCertificadoEmpadronamientoResponse>ObtenerCertificadoEmpadronamientoResult"));
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
