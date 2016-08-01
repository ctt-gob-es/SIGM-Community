/**
 * Solicitudes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion;

public class Solicitudes  implements java.io.Serializable {
    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision[] solicitudTransmision;

    private java.lang.String id;  // attribute

    public Solicitudes() {
    }

    public Solicitudes(
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision[] solicitudTransmision,
           java.lang.String id) {
           this.solicitudTransmision = solicitudTransmision;
           this.id = id;
    }


    /**
     * Gets the solicitudTransmision value for this Solicitudes.
     * 
     * @return solicitudTransmision
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision[] getSolicitudTransmision() {
        return solicitudTransmision;
    }


    /**
     * Sets the solicitudTransmision value for this Solicitudes.
     * 
     * @param solicitudTransmision
     */
    public void setSolicitudTransmision(concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision[] solicitudTransmision) {
        this.solicitudTransmision = solicitudTransmision;
    }

    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision getSolicitudTransmision(int i) {
        return this.solicitudTransmision[i];
    }

    public void setSolicitudTransmision(int i, concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion.SolicitudTransmision _value) {
        this.solicitudTransmision[i] = _value;
    }


    /**
     * Gets the id value for this Solicitudes.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Solicitudes.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Solicitudes)) return false;
        Solicitudes other = (Solicitudes) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.solicitudTransmision==null && other.getSolicitudTransmision()==null) || 
             (this.solicitudTransmision!=null &&
              java.util.Arrays.equals(this.solicitudTransmision, other.getSolicitudTransmision()))) &&
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
        if (getSolicitudTransmision() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSolicitudTransmision());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSolicitudTransmision(), i);
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
        new org.apache.axis.description.TypeDesc(Solicitudes.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/peticion", ">Solicitudes"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("solicitudTransmision");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/peticion", "SolicitudTransmision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/peticion", "SolicitudTransmision"));
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
