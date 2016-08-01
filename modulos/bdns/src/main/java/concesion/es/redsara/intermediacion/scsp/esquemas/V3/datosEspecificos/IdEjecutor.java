/**
 * IdEjecutor.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class IdEjecutor  implements java.io.Serializable {
    private java.lang.String paisEjecutor;

    private java.lang.String idPersonaEjecutor;

    public IdEjecutor() {
    }

    public IdEjecutor(
           java.lang.String paisEjecutor,
           java.lang.String idPersonaEjecutor) {
           this.paisEjecutor = paisEjecutor;
           this.idPersonaEjecutor = idPersonaEjecutor;
    }


    /**
     * Gets the paisEjecutor value for this IdEjecutor.
     * 
     * @return paisEjecutor
     */
    public java.lang.String getPaisEjecutor() {
        return paisEjecutor;
    }


    /**
     * Sets the paisEjecutor value for this IdEjecutor.
     * 
     * @param paisEjecutor
     */
    public void setPaisEjecutor(java.lang.String paisEjecutor) {
        this.paisEjecutor = paisEjecutor;
    }


    /**
     * Gets the idPersonaEjecutor value for this IdEjecutor.
     * 
     * @return idPersonaEjecutor
     */
    public java.lang.String getIdPersonaEjecutor() {
        return idPersonaEjecutor;
    }


    /**
     * Sets the idPersonaEjecutor value for this IdEjecutor.
     * 
     * @param idPersonaEjecutor
     */
    public void setIdPersonaEjecutor(java.lang.String idPersonaEjecutor) {
        this.idPersonaEjecutor = idPersonaEjecutor;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdEjecutor)) return false;
        IdEjecutor other = (IdEjecutor) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.paisEjecutor==null && other.getPaisEjecutor()==null) || 
             (this.paisEjecutor!=null &&
              this.paisEjecutor.equals(other.getPaisEjecutor()))) &&
            ((this.idPersonaEjecutor==null && other.getIdPersonaEjecutor()==null) || 
             (this.idPersonaEjecutor!=null &&
              this.idPersonaEjecutor.equals(other.getIdPersonaEjecutor())));
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
        if (getPaisEjecutor() != null) {
            _hashCode += getPaisEjecutor().hashCode();
        }
        if (getIdPersonaEjecutor() != null) {
            _hashCode += getIdPersonaEjecutor().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdEjecutor.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdEjecutor"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paisEjecutor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "PaisEjecutor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">PaisEjecutor"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPersonaEjecutor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdPersonaEjecutor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdPersonaEjecutor"));
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
