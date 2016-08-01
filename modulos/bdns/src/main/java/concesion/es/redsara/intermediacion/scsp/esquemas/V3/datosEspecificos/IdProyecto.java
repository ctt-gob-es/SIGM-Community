/**
 * IdProyecto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class IdProyecto  implements java.io.Serializable {
    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion;

    private java.lang.String discriminadorProyecto;

    public IdProyecto() {
    }

    public IdProyecto(
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion,
           java.lang.String discriminadorProyecto) {
           this.idConcesion = idConcesion;
           this.discriminadorProyecto = discriminadorProyecto;
    }


    /**
     * Gets the idConcesion value for this IdProyecto.
     * 
     * @return idConcesion
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion getIdConcesion() {
        return idConcesion;
    }


    /**
     * Sets the idConcesion value for this IdProyecto.
     * 
     * @param idConcesion
     */
    public void setIdConcesion(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion) {
        this.idConcesion = idConcesion;
    }


    /**
     * Gets the discriminadorProyecto value for this IdProyecto.
     * 
     * @return discriminadorProyecto
     */
    public java.lang.String getDiscriminadorProyecto() {
        return discriminadorProyecto;
    }


    /**
     * Sets the discriminadorProyecto value for this IdProyecto.
     * 
     * @param discriminadorProyecto
     */
    public void setDiscriminadorProyecto(java.lang.String discriminadorProyecto) {
        this.discriminadorProyecto = discriminadorProyecto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdProyecto)) return false;
        IdProyecto other = (IdProyecto) obj;
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
            ((this.discriminadorProyecto==null && other.getDiscriminadorProyecto()==null) || 
             (this.discriminadorProyecto!=null &&
              this.discriminadorProyecto.equals(other.getDiscriminadorProyecto())));
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
        if (getDiscriminadorProyecto() != null) {
            _hashCode += getDiscriminadorProyecto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdProyecto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdProyecto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idConcesion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdConcesion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdConcesion"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("discriminadorProyecto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DiscriminadorProyecto"));
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
