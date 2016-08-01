/**
 * Transmisiones.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta;

public class Transmisiones  implements java.io.Serializable {
    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.TransmisionDatos[] transmisionDatos;

    private java.lang.String id;  // attribute

    public Transmisiones() {
    }

    public Transmisiones(
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.TransmisionDatos[] transmisionDatos,
           java.lang.String id) {
           this.transmisionDatos = transmisionDatos;
           this.id = id;
    }


    /**
     * Gets the transmisionDatos value for this Transmisiones.
     * 
     * @return transmisionDatos
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.TransmisionDatos[] getTransmisionDatos() {
        return transmisionDatos;
    }


    /**
     * Sets the transmisionDatos value for this Transmisiones.
     * 
     * @param transmisionDatos
     */
    public void setTransmisionDatos(concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.TransmisionDatos[] transmisionDatos) {
        this.transmisionDatos = transmisionDatos;
    }

    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.TransmisionDatos getTransmisionDatos(int i) {
        return this.transmisionDatos[i];
    }

    public void setTransmisionDatos(int i, concesion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.TransmisionDatos _value) {
        this.transmisionDatos[i] = _value;
    }


    /**
     * Gets the id value for this Transmisiones.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Transmisiones.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Transmisiones)) return false;
        Transmisiones other = (Transmisiones) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.transmisionDatos==null && other.getTransmisionDatos()==null) || 
             (this.transmisionDatos!=null &&
              java.util.Arrays.equals(this.transmisionDatos, other.getTransmisionDatos()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId())));
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
        if (getTransmisionDatos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTransmisionDatos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTransmisionDatos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Transmisiones.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", ">Transmisiones"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transmisionDatos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", "TransmisionDatos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", "TransmisionDatos"));
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
