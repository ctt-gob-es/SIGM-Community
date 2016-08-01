/**
 * ObjetoCertificadoDataHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.certificadosPersonal;

public class ObjetoCertificadoDataHandler  implements java.io.Serializable {
    private javax.activation.DataHandler certificado;

    private java.lang.String nombreCertificado;

    public ObjetoCertificadoDataHandler() {
    }

    public ObjetoCertificadoDataHandler(
           javax.activation.DataHandler certificado,
           java.lang.String nombreCertificado) {
           this.certificado = certificado;
           this.nombreCertificado = nombreCertificado;
    }


    /**
     * Gets the certificado value for this ObjetoCertificadoDataHandler.
     * 
     * @return certificado
     */
    public javax.activation.DataHandler getCertificado() {
        return certificado;
    }


    /**
     * Sets the certificado value for this ObjetoCertificadoDataHandler.
     * 
     * @param certificado
     */
    public void setCertificado(javax.activation.DataHandler certificado) {
        this.certificado = certificado;
    }


    /**
     * Gets the nombreCertificado value for this ObjetoCertificadoDataHandler.
     * 
     * @return nombreCertificado
     */
    public java.lang.String getNombreCertificado() {
        return nombreCertificado;
    }


    /**
     * Sets the nombreCertificado value for this ObjetoCertificadoDataHandler.
     * 
     * @param nombreCertificado
     */
    public void setNombreCertificado(java.lang.String nombreCertificado) {
        this.nombreCertificado = nombreCertificado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObjetoCertificadoDataHandler)) return false;
        ObjetoCertificadoDataHandler other = (ObjetoCertificadoDataHandler) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.certificado==null && other.getCertificado()==null) || 
             (this.certificado!=null &&
              this.certificado.equals(other.getCertificado()))) &&
            ((this.nombreCertificado==null && other.getNombreCertificado()==null) || 
             (this.nombreCertificado!=null &&
              this.nombreCertificado.equals(other.getNombreCertificado())));
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
        if (getCertificado() != null) {
            _hashCode += getCertificado().hashCode();
        }
        if (getNombreCertificado() != null) {
            _hashCode += getNombreCertificado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ObjetoCertificadoDataHandler.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://certificadosPersonal.services.webempleado.dipucr.es", "ObjetoCertificadoDataHandler"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://certificadosPersonal.services.webempleado.dipucr.es", "certificado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "DataHandler"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreCertificado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://certificadosPersonal.services.webempleado.dipucr.es", "nombreCertificado"));
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
