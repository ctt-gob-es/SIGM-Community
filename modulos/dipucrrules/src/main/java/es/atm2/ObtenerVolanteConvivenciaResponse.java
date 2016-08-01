/**
 * ObtenerVolanteConvivenciaResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.atm2;

public class ObtenerVolanteConvivenciaResponse  implements java.io.Serializable {
    private es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult obtenerVolanteConvivenciaResult;

    public ObtenerVolanteConvivenciaResponse() {
    }

    public ObtenerVolanteConvivenciaResponse(
           es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult obtenerVolanteConvivenciaResult) {
           this.obtenerVolanteConvivenciaResult = obtenerVolanteConvivenciaResult;
    }


    /**
     * Gets the obtenerVolanteConvivenciaResult value for this ObtenerVolanteConvivenciaResponse.
     * 
     * @return obtenerVolanteConvivenciaResult
     */
    public es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult getObtenerVolanteConvivenciaResult() {
        return obtenerVolanteConvivenciaResult;
    }


    /**
     * Sets the obtenerVolanteConvivenciaResult value for this ObtenerVolanteConvivenciaResponse.
     * 
     * @param obtenerVolanteConvivenciaResult
     */
    public void setObtenerVolanteConvivenciaResult(es.atm2.ObtenerVolanteConvivenciaResponseObtenerVolanteConvivenciaResult obtenerVolanteConvivenciaResult) {
        this.obtenerVolanteConvivenciaResult = obtenerVolanteConvivenciaResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObtenerVolanteConvivenciaResponse)) return false;
        ObtenerVolanteConvivenciaResponse other = (ObtenerVolanteConvivenciaResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.obtenerVolanteConvivenciaResult==null && other.getObtenerVolanteConvivenciaResult()==null) || 
             (this.obtenerVolanteConvivenciaResult!=null &&
              this.obtenerVolanteConvivenciaResult.equals(other.getObtenerVolanteConvivenciaResult())));
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
        if (getObtenerVolanteConvivenciaResult() != null) {
            _hashCode += getObtenerVolanteConvivenciaResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ObtenerVolanteConvivenciaResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerVolanteConvivenciaResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obtenerVolanteConvivenciaResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerVolanteConvivenciaResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerVolanteConvivenciaResponse>ObtenerVolanteConvivenciaResult"));
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
