/**
 * ObtenerVolanteConvivencia.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.atm2;

public class ObtenerVolanteConvivencia  implements java.io.Serializable {
    private java.lang.String codInstitucion;

    private java.lang.String tipoDocumento;

    private java.lang.String documentoIdentidad;

    public ObtenerVolanteConvivencia() {
    }

    public ObtenerVolanteConvivencia(
           java.lang.String codInstitucion,
           java.lang.String tipoDocumento,
           java.lang.String documentoIdentidad) {
           this.codInstitucion = codInstitucion;
           this.tipoDocumento = tipoDocumento;
           this.documentoIdentidad = documentoIdentidad;
    }


    /**
     * Gets the codInstitucion value for this ObtenerVolanteConvivencia.
     * 
     * @return codInstitucion
     */
    public java.lang.String getCodInstitucion() {
        return codInstitucion;
    }


    /**
     * Sets the codInstitucion value for this ObtenerVolanteConvivencia.
     * 
     * @param codInstitucion
     */
    public void setCodInstitucion(java.lang.String codInstitucion) {
        this.codInstitucion = codInstitucion;
    }


    /**
     * Gets the tipoDocumento value for this ObtenerVolanteConvivencia.
     * 
     * @return tipoDocumento
     */
    public java.lang.String getTipoDocumento() {
        return tipoDocumento;
    }


    /**
     * Sets the tipoDocumento value for this ObtenerVolanteConvivencia.
     * 
     * @param tipoDocumento
     */
    public void setTipoDocumento(java.lang.String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }


    /**
     * Gets the documentoIdentidad value for this ObtenerVolanteConvivencia.
     * 
     * @return documentoIdentidad
     */
    public java.lang.String getDocumentoIdentidad() {
        return documentoIdentidad;
    }


    /**
     * Sets the documentoIdentidad value for this ObtenerVolanteConvivencia.
     * 
     * @param documentoIdentidad
     */
    public void setDocumentoIdentidad(java.lang.String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObtenerVolanteConvivencia)) return false;
        ObtenerVolanteConvivencia other = (ObtenerVolanteConvivencia) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codInstitucion==null && other.getCodInstitucion()==null) || 
             (this.codInstitucion!=null &&
              this.codInstitucion.equals(other.getCodInstitucion()))) &&
            ((this.tipoDocumento==null && other.getTipoDocumento()==null) || 
             (this.tipoDocumento!=null &&
              this.tipoDocumento.equals(other.getTipoDocumento()))) &&
            ((this.documentoIdentidad==null && other.getDocumentoIdentidad()==null) || 
             (this.documentoIdentidad!=null &&
              this.documentoIdentidad.equals(other.getDocumentoIdentidad())));
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
        if (getCodInstitucion() != null) {
            _hashCode += getCodInstitucion().hashCode();
        }
        if (getTipoDocumento() != null) {
            _hashCode += getTipoDocumento().hashCode();
        }
        if (getDocumentoIdentidad() != null) {
            _hashCode += getDocumentoIdentidad().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ObtenerVolanteConvivencia.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atm2.es/", ">ObtenerVolanteConvivencia"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codInstitucion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atm2.es/", "codInstitucion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atm2.es/", "tipoDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentoIdentidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atm2.es/", "documentoIdentidad"));
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
