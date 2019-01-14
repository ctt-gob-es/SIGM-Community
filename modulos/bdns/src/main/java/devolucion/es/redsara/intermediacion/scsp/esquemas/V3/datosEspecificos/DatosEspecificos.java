/**
 * DatosEspecificos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificos  implements java.io.Serializable {
    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticion datosEspecificosPeticion;

    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosRespuesta datosEspecificosRespuesta;

    public DatosEspecificos() {
    }

    public DatosEspecificos(
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticion datosEspecificosPeticion,
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosRespuesta datosEspecificosRespuesta) {
           this.datosEspecificosPeticion = datosEspecificosPeticion;
           this.datosEspecificosRespuesta = datosEspecificosRespuesta;
    }


    /**
     * Gets the datosEspecificosPeticion value for this DatosEspecificos.
     * 
     * @return datosEspecificosPeticion
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticion getDatosEspecificosPeticion() {
        return datosEspecificosPeticion;
    }


    /**
     * Sets the datosEspecificosPeticion value for this DatosEspecificos.
     * 
     * @param datosEspecificosPeticion
     */
    public void setDatosEspecificosPeticion(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticion datosEspecificosPeticion) {
        this.datosEspecificosPeticion = datosEspecificosPeticion;
    }


    /**
     * Gets the datosEspecificosRespuesta value for this DatosEspecificos.
     * 
     * @return datosEspecificosRespuesta
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosRespuesta getDatosEspecificosRespuesta() {
        return datosEspecificosRespuesta;
    }


    /**
     * Sets the datosEspecificosRespuesta value for this DatosEspecificos.
     * 
     * @param datosEspecificosRespuesta
     */
    public void setDatosEspecificosRespuesta(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosRespuesta datosEspecificosRespuesta) {
        this.datosEspecificosRespuesta = datosEspecificosRespuesta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificos)) return false;
        DatosEspecificos other = (DatosEspecificos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.datosEspecificosPeticion==null && other.getDatosEspecificosPeticion()==null) || 
             (this.datosEspecificosPeticion!=null &&
              this.datosEspecificosPeticion.equals(other.getDatosEspecificosPeticion()))) &&
            ((this.datosEspecificosRespuesta==null && other.getDatosEspecificosRespuesta()==null) || 
             (this.datosEspecificosRespuesta!=null &&
              this.datosEspecificosRespuesta.equals(other.getDatosEspecificosRespuesta())));
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
        if (getDatosEspecificosPeticion() != null) {
            _hashCode += getDatosEspecificosPeticion().hashCode();
        }
        if (getDatosEspecificosRespuesta() != null) {
            _hashCode += getDatosEspecificosRespuesta().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DatosEspecificos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosEspecificosPeticion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DatosEspecificosPeticion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">DatosEspecificos>DatosEspecificosPeticion"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosEspecificosRespuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DatosEspecificosRespuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">DatosEspecificos>DatosEspecificosRespuesta"));
        elemField.setMinOccurs(0);
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
