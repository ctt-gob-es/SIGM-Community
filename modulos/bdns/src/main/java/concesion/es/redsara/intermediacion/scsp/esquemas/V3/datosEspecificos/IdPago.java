/**
 * IdPago.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class IdPago  implements java.io.Serializable {
    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion;

    private java.lang.String discriminadorPago;

    public IdPago() {
    }

    public IdPago(
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion,
           java.lang.String discriminadorPago) {
           this.idConcesion = idConcesion;
           this.discriminadorPago = discriminadorPago;
    }


    /**
     * Gets the idConcesion value for this IdPago.
     * 
     * @return idConcesion
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion getIdConcesion() {
        return idConcesion;
    }


    /**
     * Sets the idConcesion value for this IdPago.
     * 
     * @param idConcesion
     */
    public void setIdConcesion(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion) {
        this.idConcesion = idConcesion;
    }


    /**
     * Gets the discriminadorPago value for this IdPago.
     * 
     * @return discriminadorPago
     */
    public java.lang.String getDiscriminadorPago() {
        return discriminadorPago;
    }


    /**
     * Sets the discriminadorPago value for this IdPago.
     * 
     * @param discriminadorPago
     */
    public void setDiscriminadorPago(java.lang.String discriminadorPago) {
        this.discriminadorPago = discriminadorPago;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdPago)) return false;
        IdPago other = (IdPago) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idConcesion==null && other.getIdConcesion()==null) || 
             (this.idConcesion!=null &&
              this.idConcesion.equals(other.getIdConcesion()))) &&
            ((this.discriminadorPago==null && other.getDiscriminadorPago()==null) || 
             (this.discriminadorPago!=null &&
              this.discriminadorPago.equals(other.getDiscriminadorPago())));
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
        if (getIdConcesion() != null) {
            _hashCode += getIdConcesion().hashCode();
        }
        if (getDiscriminadorPago() != null) {
            _hashCode += getDiscriminadorPago().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdPago.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdPago"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idConcesion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdConcesion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdConcesion"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("discriminadorPago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DiscriminadorPago"));
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
