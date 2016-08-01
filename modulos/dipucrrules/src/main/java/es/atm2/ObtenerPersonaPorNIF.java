/**
 * ObtenerPersonaPorNIF.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.atm2;

public class ObtenerPersonaPorNIF  implements java.io.Serializable {
    private java.lang.String p_sCodInstitucion;

    private java.lang.String p_sPersonaFisica_o_Juridica;

    private java.lang.String p_sDocumentoIdentidad;

    public ObtenerPersonaPorNIF() {
    }

    public ObtenerPersonaPorNIF(
           java.lang.String p_sCodInstitucion,
           java.lang.String p_sPersonaFisica_o_Juridica,
           java.lang.String p_sDocumentoIdentidad) {
           this.p_sCodInstitucion = p_sCodInstitucion;
           this.p_sPersonaFisica_o_Juridica = p_sPersonaFisica_o_Juridica;
           this.p_sDocumentoIdentidad = p_sDocumentoIdentidad;
    }


    /**
     * Gets the p_sCodInstitucion value for this ObtenerPersonaPorNIF.
     * 
     * @return p_sCodInstitucion
     */
    public java.lang.String getP_sCodInstitucion() {
        return p_sCodInstitucion;
    }


    /**
     * Sets the p_sCodInstitucion value for this ObtenerPersonaPorNIF.
     * 
     * @param p_sCodInstitucion
     */
    public void setP_sCodInstitucion(java.lang.String p_sCodInstitucion) {
        this.p_sCodInstitucion = p_sCodInstitucion;
    }


    /**
     * Gets the p_sPersonaFisica_o_Juridica value for this ObtenerPersonaPorNIF.
     * 
     * @return p_sPersonaFisica_o_Juridica
     */
    public java.lang.String getP_sPersonaFisica_o_Juridica() {
        return p_sPersonaFisica_o_Juridica;
    }


    /**
     * Sets the p_sPersonaFisica_o_Juridica value for this ObtenerPersonaPorNIF.
     * 
     * @param p_sPersonaFisica_o_Juridica
     */
    public void setP_sPersonaFisica_o_Juridica(java.lang.String p_sPersonaFisica_o_Juridica) {
        this.p_sPersonaFisica_o_Juridica = p_sPersonaFisica_o_Juridica;
    }


    /**
     * Gets the p_sDocumentoIdentidad value for this ObtenerPersonaPorNIF.
     * 
     * @return p_sDocumentoIdentidad
     */
    public java.lang.String getP_sDocumentoIdentidad() {
        return p_sDocumentoIdentidad;
    }


    /**
     * Sets the p_sDocumentoIdentidad value for this ObtenerPersonaPorNIF.
     * 
     * @param p_sDocumentoIdentidad
     */
    public void setP_sDocumentoIdentidad(java.lang.String p_sDocumentoIdentidad) {
        this.p_sDocumentoIdentidad = p_sDocumentoIdentidad;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObtenerPersonaPorNIF)) return false;
        ObtenerPersonaPorNIF other = (ObtenerPersonaPorNIF) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.p_sCodInstitucion==null && other.getP_sCodInstitucion()==null) || 
             (this.p_sCodInstitucion!=null &&
              this.p_sCodInstitucion.equals(other.getP_sCodInstitucion()))) &&
            ((this.p_sPersonaFisica_o_Juridica==null && other.getP_sPersonaFisica_o_Juridica()==null) || 
             (this.p_sPersonaFisica_o_Juridica!=null &&
              this.p_sPersonaFisica_o_Juridica.equals(other.getP_sPersonaFisica_o_Juridica()))) &&
            ((this.p_sDocumentoIdentidad==null && other.getP_sDocumentoIdentidad()==null) || 
             (this.p_sDocumentoIdentidad!=null &&
              this.p_sDocumentoIdentidad.equals(other.getP_sDocumentoIdentidad())));
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
        if (getP_sCodInstitucion() != null) {
            _hashCode += getP_sCodInstitucion().hashCode();
        }
        if (getP_sPersonaFisica_o_Juridica() != null) {
            _hashCode += getP_sPersonaFisica_o_Juridica().hashCode();
        }
        if (getP_sDocumentoIdentidad() != null) {
            _hashCode += getP_sDocumentoIdentidad().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ObtenerPersonaPorNIF.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerPersonaPorNIF"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_sCodInstitucion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atm2.es/", "p_sCodInstitucion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_sPersonaFisica_o_Juridica");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atm2.es/", "p_sPersonaFisica_o_Juridica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p_sDocumentoIdentidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atm2.es/", "p_sDocumentoIdentidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
