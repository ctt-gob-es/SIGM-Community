/**
 * Info_envio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Info_envio  implements java.io.Serializable {
    private java.lang.String envio_destinatario;

    public Info_envio() {
    }

    public Info_envio(
           java.lang.String envio_destinatario) {
           this.envio_destinatario = envio_destinatario;
    }


    /**
     * Gets the envio_destinatario value for this Info_envio.
     * 
     * @return envio_destinatario
     */
    public java.lang.String getEnvio_destinatario() {
        return envio_destinatario;
    }


    /**
     * Sets the envio_destinatario value for this Info_envio.
     * 
     * @param envio_destinatario
     */
    public void setEnvio_destinatario(java.lang.String envio_destinatario) {
        this.envio_destinatario = envio_destinatario;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Info_envio)) return false;
        Info_envio other = (Info_envio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.envio_destinatario==null && other.getEnvio_destinatario()==null) || 
             (this.envio_destinatario!=null &&
              this.envio_destinatario.equals(other.getEnvio_destinatario())));
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
        if (getEnvio_destinatario() != null) {
            _hashCode += getEnvio_destinatario().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Info_envio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "info_envio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("envio_destinatario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "envio_destinatario"));
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
