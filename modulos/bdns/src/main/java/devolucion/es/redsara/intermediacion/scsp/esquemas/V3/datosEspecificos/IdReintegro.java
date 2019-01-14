/**
 * IdReintegro.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class IdReintegro  implements java.io.Serializable {
    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion;

    private java.lang.String discriminadorReint;

    public IdReintegro() {
    }

    public IdReintegro(
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion,
           java.lang.String discriminadorReint) {
           this.idConcesion = idConcesion;
           this.discriminadorReint = discriminadorReint;
    }


    /**
     * Gets the idConcesion value for this IdReintegro.
     * 
     * @return idConcesion
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion getIdConcesion() {
        return idConcesion;
    }


    /**
     * Sets the idConcesion value for this IdReintegro.
     * 
     * @param idConcesion
     */
    public void setIdConcesion(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion) {
        this.idConcesion = idConcesion;
    }


    /**
     * Gets the discriminadorReint value for this IdReintegro.
     * 
     * @return discriminadorReint
     */
    public java.lang.String getDiscriminadorReint() {
        return discriminadorReint;
    }


    /**
     * Sets the discriminadorReint value for this IdReintegro.
     * 
     * @param discriminadorReint
     */
    public void setDiscriminadorReint(java.lang.String discriminadorReint) {
        this.discriminadorReint = discriminadorReint;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdReintegro)) return false;
        IdReintegro other = (IdReintegro) obj;
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
            ((this.discriminadorReint==null && other.getDiscriminadorReint()==null) || 
             (this.discriminadorReint!=null &&
              this.discriminadorReint.equals(other.getDiscriminadorReint())));
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
        if (getDiscriminadorReint() != null) {
            _hashCode += getDiscriminadorReint().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdReintegro.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdReintegro"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idConcesion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdConcesion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdConcesion"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("discriminadorReint");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DiscriminadorReint"));
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
