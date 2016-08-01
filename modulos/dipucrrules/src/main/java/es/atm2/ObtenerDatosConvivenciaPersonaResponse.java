/**
 * ObtenerDatosConvivenciaPersonaResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.atm2;

public class ObtenerDatosConvivenciaPersonaResponse  implements java.io.Serializable {
    private es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult obtenerDatosConvivenciaPersonaResult;

    public ObtenerDatosConvivenciaPersonaResponse() {
    }

    public ObtenerDatosConvivenciaPersonaResponse(
           es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult obtenerDatosConvivenciaPersonaResult) {
           this.obtenerDatosConvivenciaPersonaResult = obtenerDatosConvivenciaPersonaResult;
    }


    /**
     * Gets the obtenerDatosConvivenciaPersonaResult value for this ObtenerDatosConvivenciaPersonaResponse.
     * 
     * @return obtenerDatosConvivenciaPersonaResult
     */
    public es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult getObtenerDatosConvivenciaPersonaResult() {
        return obtenerDatosConvivenciaPersonaResult;
    }


    /**
     * Sets the obtenerDatosConvivenciaPersonaResult value for this ObtenerDatosConvivenciaPersonaResponse.
     * 
     * @param obtenerDatosConvivenciaPersonaResult
     */
    public void setObtenerDatosConvivenciaPersonaResult(es.atm2.ObtenerDatosConvivenciaPersonaResponseObtenerDatosConvivenciaPersonaResult obtenerDatosConvivenciaPersonaResult) {
        this.obtenerDatosConvivenciaPersonaResult = obtenerDatosConvivenciaPersonaResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObtenerDatosConvivenciaPersonaResponse)) return false;
        ObtenerDatosConvivenciaPersonaResponse other = (ObtenerDatosConvivenciaPersonaResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.obtenerDatosConvivenciaPersonaResult==null && other.getObtenerDatosConvivenciaPersonaResult()==null) || 
             (this.obtenerDatosConvivenciaPersonaResult!=null &&
              this.obtenerDatosConvivenciaPersonaResult.equals(other.getObtenerDatosConvivenciaPersonaResult())));
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
        if (getObtenerDatosConvivenciaPersonaResult() != null) {
            _hashCode += getObtenerDatosConvivenciaPersonaResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ObtenerDatosConvivenciaPersonaResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerDatosConvivenciaPersonaResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obtenerDatosConvivenciaPersonaResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atm2.es/", "ObtenerDatosConvivenciaPersonaResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">>ObtenerDatosConvivenciaPersonaResponse>ObtenerDatosConvivenciaPersonaResult"));
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
