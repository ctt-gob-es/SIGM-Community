/**
 * IdConcesionIdBeneficiario.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class IdConcesionIdBeneficiario  implements java.io.Serializable {
    private java.lang.String paisBen;

    private java.lang.String idPersonaBen;

    public IdConcesionIdBeneficiario() {
    }

    public IdConcesionIdBeneficiario(
           java.lang.String paisBen,
           java.lang.String idPersonaBen) {
           this.paisBen = paisBen;
           this.idPersonaBen = idPersonaBen;
    }


    /**
     * Gets the paisBen value for this IdConcesionIdBeneficiario.
     * 
     * @return paisBen
     */
    public java.lang.String getPaisBen() {
        return paisBen;
    }


    /**
     * Sets the paisBen value for this IdConcesionIdBeneficiario.
     * 
     * @param paisBen
     */
    public void setPaisBen(java.lang.String paisBen) {
        this.paisBen = paisBen;
    }


    /**
     * Gets the idPersonaBen value for this IdConcesionIdBeneficiario.
     * 
     * @return idPersonaBen
     */
    public java.lang.String getIdPersonaBen() {
        return idPersonaBen;
    }


    /**
     * Sets the idPersonaBen value for this IdConcesionIdBeneficiario.
     * 
     * @param idPersonaBen
     */
    public void setIdPersonaBen(java.lang.String idPersonaBen) {
        this.idPersonaBen = idPersonaBen;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdConcesionIdBeneficiario)) return false;
        IdConcesionIdBeneficiario other = (IdConcesionIdBeneficiario) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.paisBen==null && other.getPaisBen()==null) || 
             (this.paisBen!=null &&
              this.paisBen.equals(other.getPaisBen()))) &&
            ((this.idPersonaBen==null && other.getIdPersonaBen()==null) || 
             (this.idPersonaBen!=null &&
              this.idPersonaBen.equals(other.getIdPersonaBen())));
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
        if (getPaisBen() != null) {
            _hashCode += getPaisBen().hashCode();
        }
        if (getIdPersonaBen() != null) {
            _hashCode += getIdPersonaBen().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdConcesionIdBeneficiario.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>IdConcesion>IdBeneficiario"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paisBen");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "PaisBen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">PaisBen"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPersonaBen");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdPersonaBen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdPersonaBen"));
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
