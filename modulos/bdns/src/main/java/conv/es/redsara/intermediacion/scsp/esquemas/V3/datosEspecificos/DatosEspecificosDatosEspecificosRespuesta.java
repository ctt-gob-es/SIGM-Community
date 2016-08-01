/**
 * DatosEspecificosDatosEspecificosRespuesta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

import es.dipucr.bdns.common.DatosEspecificosDatosEspecificosRespuestaAbstracta;

public class DatosEspecificosDatosEspecificosRespuesta extends DatosEspecificosDatosEspecificosRespuestaAbstracta  implements java.io.Serializable {
    private java.lang.String idConvocatoria;

    private java.lang.String codigoEstadoSo;

    private java.lang.String codigoEstadoSecundarioSo;

    private java.lang.String literalErrorSo;

    public DatosEspecificosDatosEspecificosRespuesta() {
    }

    public DatosEspecificosDatosEspecificosRespuesta(
           java.lang.String idConvocatoria,
           java.lang.String codigoEstadoSo,
           java.lang.String codigoEstadoSecundarioSo,
           java.lang.String literalErrorSo) {
           this.idConvocatoria = idConvocatoria;
           this.codigoEstadoSo = codigoEstadoSo;
           this.codigoEstadoSecundarioSo = codigoEstadoSecundarioSo;
           this.literalErrorSo = literalErrorSo;
    }


    /**
     * Gets the idConvocatoria value for this DatosEspecificosDatosEspecificosRespuesta.
     * 
     * @return idConvocatoria
     */
    public java.lang.String getIdConvocatoria() {
        return idConvocatoria;
    }


    /**
     * Sets the idConvocatoria value for this DatosEspecificosDatosEspecificosRespuesta.
     * 
     * @param idConvocatoria
     */
    public void setIdConvocatoria(java.lang.String idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }


    /**
     * Gets the codigoEstadoSo value for this DatosEspecificosDatosEspecificosRespuesta.
     * 
     * @return codigoEstadoSo
     */
    public java.lang.String getCodigoEstadoSo() {
        return codigoEstadoSo;
    }


    /**
     * Sets the codigoEstadoSo value for this DatosEspecificosDatosEspecificosRespuesta.
     * 
     * @param codigoEstadoSo
     */
    public void setCodigoEstadoSo(java.lang.String codigoEstadoSo) {
        this.codigoEstadoSo = codigoEstadoSo;
    }


    /**
     * Gets the codigoEstadoSecundarioSo value for this DatosEspecificosDatosEspecificosRespuesta.
     * 
     * @return codigoEstadoSecundarioSo
     */
    public java.lang.String getCodigoEstadoSecundarioSo() {
        return codigoEstadoSecundarioSo;
    }


    /**
     * Sets the codigoEstadoSecundarioSo value for this DatosEspecificosDatosEspecificosRespuesta.
     * 
     * @param codigoEstadoSecundarioSo
     */
    public void setCodigoEstadoSecundarioSo(java.lang.String codigoEstadoSecundarioSo) {
        this.codigoEstadoSecundarioSo = codigoEstadoSecundarioSo;
    }


    /**
     * Gets the literalErrorSo value for this DatosEspecificosDatosEspecificosRespuesta.
     * 
     * @return literalErrorSo
     */
    public java.lang.String getLiteralErrorSo() {
        return literalErrorSo;
    }


    /**
     * Sets the literalErrorSo value for this DatosEspecificosDatosEspecificosRespuesta.
     * 
     * @param literalErrorSo
     */
    public void setLiteralErrorSo(java.lang.String literalErrorSo) {
        this.literalErrorSo = literalErrorSo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosRespuesta)) return false;
        DatosEspecificosDatosEspecificosRespuesta other = (DatosEspecificosDatosEspecificosRespuesta) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idConvocatoria==null && other.getIdConvocatoria()==null) || 
             (this.idConvocatoria!=null &&
              this.idConvocatoria.equals(other.getIdConvocatoria()))) &&
            ((this.codigoEstadoSo==null && other.getCodigoEstadoSo()==null) || 
             (this.codigoEstadoSo!=null &&
              this.codigoEstadoSo.equals(other.getCodigoEstadoSo()))) &&
            ((this.codigoEstadoSecundarioSo==null && other.getCodigoEstadoSecundarioSo()==null) || 
             (this.codigoEstadoSecundarioSo!=null &&
              this.codigoEstadoSecundarioSo.equals(other.getCodigoEstadoSecundarioSo()))) &&
            ((this.literalErrorSo==null && other.getLiteralErrorSo()==null) || 
             (this.literalErrorSo!=null &&
              this.literalErrorSo.equals(other.getLiteralErrorSo())));
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
        if (getIdConvocatoria() != null) {
            _hashCode += getIdConvocatoria().hashCode();
        }
        if (getCodigoEstadoSo() != null) {
            _hashCode += getCodigoEstadoSo().hashCode();
        }
        if (getCodigoEstadoSecundarioSo() != null) {
            _hashCode += getCodigoEstadoSecundarioSo().hashCode();
        }
        if (getLiteralErrorSo() != null) {
            _hashCode += getLiteralErrorSo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosRespuesta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">DatosEspecificos>DatosEspecificosRespuesta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idConvocatoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdConvocatoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdConvocatoria"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoEstadoSo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "CodigoEstadoSo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoEstadoSecundarioSo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "CodigoEstadoSecundarioSo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">CodigoEstadoSecundarioSo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("literalErrorSo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "LiteralErrorSo"));
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
