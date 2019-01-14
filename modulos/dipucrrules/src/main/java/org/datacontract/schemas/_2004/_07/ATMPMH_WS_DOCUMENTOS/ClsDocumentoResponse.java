/**
 * ClsDocumentoResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS;

public class ClsDocumentoResponse  implements java.io.Serializable {
    private java.lang.String CODIGO_RESULTADO;

    private java.lang.Boolean CORRECTO;

    private java.lang.String DESCRIPCION_ERROR;

    private byte[] DOCUMENTO;

    public ClsDocumentoResponse() {
    }

    public ClsDocumentoResponse(
           java.lang.String CODIGO_RESULTADO,
           java.lang.Boolean CORRECTO,
           java.lang.String DESCRIPCION_ERROR,
           byte[] DOCUMENTO) {
           this.CODIGO_RESULTADO = CODIGO_RESULTADO;
           this.CORRECTO = CORRECTO;
           this.DESCRIPCION_ERROR = DESCRIPCION_ERROR;
           this.DOCUMENTO = DOCUMENTO;
    }


    /**
     * Gets the CODIGO_RESULTADO value for this ClsDocumentoResponse.
     * 
     * @return CODIGO_RESULTADO
     */
    public java.lang.String getCODIGO_RESULTADO() {
        return CODIGO_RESULTADO;
    }


    /**
     * Sets the CODIGO_RESULTADO value for this ClsDocumentoResponse.
     * 
     * @param CODIGO_RESULTADO
     */
    public void setCODIGO_RESULTADO(java.lang.String CODIGO_RESULTADO) {
        this.CODIGO_RESULTADO = CODIGO_RESULTADO;
    }


    /**
     * Gets the CORRECTO value for this ClsDocumentoResponse.
     * 
     * @return CORRECTO
     */
    public java.lang.Boolean getCORRECTO() {
        return CORRECTO;
    }


    /**
     * Sets the CORRECTO value for this ClsDocumentoResponse.
     * 
     * @param CORRECTO
     */
    public void setCORRECTO(java.lang.Boolean CORRECTO) {
        this.CORRECTO = CORRECTO;
    }


    /**
     * Gets the DESCRIPCION_ERROR value for this ClsDocumentoResponse.
     * 
     * @return DESCRIPCION_ERROR
     */
    public java.lang.String getDESCRIPCION_ERROR() {
        return DESCRIPCION_ERROR;
    }


    /**
     * Sets the DESCRIPCION_ERROR value for this ClsDocumentoResponse.
     * 
     * @param DESCRIPCION_ERROR
     */
    public void setDESCRIPCION_ERROR(java.lang.String DESCRIPCION_ERROR) {
        this.DESCRIPCION_ERROR = DESCRIPCION_ERROR;
    }


    /**
     * Gets the DOCUMENTO value for this ClsDocumentoResponse.
     * 
     * @return DOCUMENTO
     */
    public byte[] getDOCUMENTO() {
        return DOCUMENTO;
    }


    /**
     * Sets the DOCUMENTO value for this ClsDocumentoResponse.
     * 
     * @param DOCUMENTO
     */
    public void setDOCUMENTO(byte[] DOCUMENTO) {
        this.DOCUMENTO = DOCUMENTO;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ClsDocumentoResponse)) return false;
        ClsDocumentoResponse other = (ClsDocumentoResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CODIGO_RESULTADO==null && other.getCODIGO_RESULTADO()==null) || 
             (this.CODIGO_RESULTADO!=null &&
              this.CODIGO_RESULTADO.equals(other.getCODIGO_RESULTADO()))) &&
            ((this.CORRECTO==null && other.getCORRECTO()==null) || 
             (this.CORRECTO!=null &&
              this.CORRECTO.equals(other.getCORRECTO()))) &&
            ((this.DESCRIPCION_ERROR==null && other.getDESCRIPCION_ERROR()==null) || 
             (this.DESCRIPCION_ERROR!=null &&
              this.DESCRIPCION_ERROR.equals(other.getDESCRIPCION_ERROR()))) &&
            ((this.DOCUMENTO==null && other.getDOCUMENTO()==null) || 
             (this.DOCUMENTO!=null &&
              java.util.Arrays.equals(this.DOCUMENTO, other.getDOCUMENTO())));
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
        if (getCODIGO_RESULTADO() != null) {
            _hashCode += getCODIGO_RESULTADO().hashCode();
        }
        if (getCORRECTO() != null) {
            _hashCode += getCORRECTO().hashCode();
        }
        if (getDESCRIPCION_ERROR() != null) {
            _hashCode += getDESCRIPCION_ERROR().hashCode();
        }
        if (getDOCUMENTO() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDOCUMENTO());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDOCUMENTO(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ClsDocumentoResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ATMPMH_WS_DOCUMENTOS", "clsDocumentoResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CODIGO_RESULTADO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ATMPMH_WS_DOCUMENTOS", "CODIGO_RESULTADO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CORRECTO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ATMPMH_WS_DOCUMENTOS", "CORRECTO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DESCRIPCION_ERROR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ATMPMH_WS_DOCUMENTOS", "DESCRIPCION_ERROR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DOCUMENTO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ATMPMH_WS_DOCUMENTOS", "DOCUMENTO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
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
