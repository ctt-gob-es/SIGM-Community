/**
 * IdConcesion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class IdConcesion  implements java.io.Serializable {
    private java.lang.String idConvocatoria;

    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesionIdBeneficiario idBeneficiario;

    private java.lang.String discriminadorConcesion;

    public IdConcesion() {
    }

    public IdConcesion(
           java.lang.String idConvocatoria,
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesionIdBeneficiario idBeneficiario,
           java.lang.String discriminadorConcesion) {
           this.idConvocatoria = idConvocatoria;
           this.idBeneficiario = idBeneficiario;
           this.discriminadorConcesion = discriminadorConcesion;
    }


    /**
     * Gets the idConvocatoria value for this IdConcesion.
     * 
     * @return idConvocatoria
     */
    public java.lang.String getIdConvocatoria() {
        return idConvocatoria;
    }


    /**
     * Sets the idConvocatoria value for this IdConcesion.
     * 
     * @param idConvocatoria
     */
    public void setIdConvocatoria(java.lang.String idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }


    /**
     * Gets the idBeneficiario value for this IdConcesion.
     * 
     * @return idBeneficiario
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesionIdBeneficiario getIdBeneficiario() {
        return idBeneficiario;
    }


    /**
     * Sets the idBeneficiario value for this IdConcesion.
     * 
     * @param idBeneficiario
     */
    public void setIdBeneficiario(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesionIdBeneficiario idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }


    /**
     * Gets the discriminadorConcesion value for this IdConcesion.
     * 
     * @return discriminadorConcesion
     */
    public java.lang.String getDiscriminadorConcesion() {
        return discriminadorConcesion;
    }


    /**
     * Sets the discriminadorConcesion value for this IdConcesion.
     * 
     * @param discriminadorConcesion
     */
    public void setDiscriminadorConcesion(java.lang.String discriminadorConcesion) {
        this.discriminadorConcesion = discriminadorConcesion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdConcesion)) return false;
        IdConcesion other = (IdConcesion) obj;
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
            ((this.idBeneficiario==null && other.getIdBeneficiario()==null) || 
             (this.idBeneficiario!=null &&
              this.idBeneficiario.equals(other.getIdBeneficiario()))) &&
            ((this.discriminadorConcesion==null && other.getDiscriminadorConcesion()==null) || 
             (this.discriminadorConcesion!=null &&
              this.discriminadorConcesion.equals(other.getDiscriminadorConcesion())));
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
        if (getIdBeneficiario() != null) {
            _hashCode += getIdBeneficiario().hashCode();
        }
        if (getDiscriminadorConcesion() != null) {
            _hashCode += getDiscriminadorConcesion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdConcesion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdConcesion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idConvocatoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdConvocatoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdConvocatoria"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idBeneficiario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdBeneficiario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>IdConcesion>IdBeneficiario"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("discriminadorConcesion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DiscriminadorConcesion"));
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
