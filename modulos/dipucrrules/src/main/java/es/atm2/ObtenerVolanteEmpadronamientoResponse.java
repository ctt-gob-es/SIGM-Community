/**
 * ObtenerVolanteEmpadronamientoResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.atm2;

public class ObtenerVolanteEmpadronamientoResponse  implements java.io.Serializable {
    private es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult obtenerVolanteEmpadronamientoResult;

    public ObtenerVolanteEmpadronamientoResponse() {
    }

    public ObtenerVolanteEmpadronamientoResponse(
           es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult obtenerVolanteEmpadronamientoResult) {
           this.obtenerVolanteEmpadronamientoResult = obtenerVolanteEmpadronamientoResult;
    }


    /**
     * Gets the obtenerVolanteEmpadronamientoResult value for this ObtenerVolanteEmpadronamientoResponse.
     * 
     * @return obtenerVolanteEmpadronamientoResult
     */
    public es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult getObtenerVolanteEmpadronamientoResult() {
        return obtenerVolanteEmpadronamientoResult;
    }


    /**
     * Sets the obtenerVolanteEmpadronamientoResult value for this ObtenerVolanteEmpadronamientoResponse.
     * 
     * @param obtenerVolanteEmpadronamientoResult
     */
    public void setObtenerVolanteEmpadronamientoResult(es.atm2.ObtenerVolanteEmpadronamientoResponseObtenerVolanteEmpadronamientoResult obtenerVolanteEmpadronamientoResult) {
        this.obtenerVolanteEmpadronamientoResult = obtenerVolanteEmpadronamientoResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObtenerVolanteEmpadronamientoResponse)) return false;
        ObtenerVolanteEmpadronamientoResponse other = (ObtenerVolanteEmpadronamientoResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.obtenerVolanteEmpadronamientoResult==null && other.getObtenerVolanteEmpadronamientoResult()==null) || 
             (this.obtenerVolanteEmpadronamientoResult!=null &&
              this.obtenerVolanteEmpadronamientoResult.equals(other.getObtenerVolanteEmpadronamientoResult())));
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
        if (getObtenerVolanteEmpadronamientoResult() != null) {
            _hashCode += getObtenerVolanteEmpadronamientoResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ObtenerVolanteEmpadronamientoResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerVolanteEmpadronamientoResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obtenerVolanteEmpadronamientoResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerVolanteEmpadronamientoResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerVolanteEmpadronamientoResponse>ObtenerVolanteEmpadronamientoResult"));
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
