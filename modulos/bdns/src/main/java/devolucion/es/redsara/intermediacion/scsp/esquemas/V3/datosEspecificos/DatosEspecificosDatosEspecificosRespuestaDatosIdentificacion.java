/**
 * DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion  implements java.io.Serializable {
    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdDevolucion idDevolucion;

    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdReintegro idReintegro;

    public DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion() {
    }

    public DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion(
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdDevolucion idDevolucion,
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdReintegro idReintegro) {
           this.idDevolucion = idDevolucion;
           this.idReintegro = idReintegro;
    }


    /**
     * Gets the idDevolucion value for this DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.
     * 
     * @return idDevolucion
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdDevolucion getIdDevolucion() {
        return idDevolucion;
    }


    /**
     * Sets the idDevolucion value for this DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.
     * 
     * @param idDevolucion
     */
    public void setIdDevolucion(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdDevolucion idDevolucion) {
        this.idDevolucion = idDevolucion;
    }


    /**
     * Gets the idReintegro value for this DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.
     * 
     * @return idReintegro
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdReintegro getIdReintegro() {
        return idReintegro;
    }


    /**
     * Sets the idReintegro value for this DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.
     * 
     * @param idReintegro
     */
    public void setIdReintegro(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdReintegro idReintegro) {
        this.idReintegro = idReintegro;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion)) return false;
        DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion other = (DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idDevolucion==null && other.getIdDevolucion()==null) || 
             (this.idDevolucion!=null &&
              this.idDevolucion.equals(other.getIdDevolucion()))) &&
            ((this.idReintegro==null && other.getIdReintegro()==null) || 
             (this.idReintegro!=null &&
              this.idReintegro.equals(other.getIdReintegro())));
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
        if (getIdDevolucion() != null) {
            _hashCode += getIdDevolucion().hashCode();
        }
        if (getIdReintegro() != null) {
            _hashCode += getIdReintegro().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>DatosEspecificos>DatosEspecificosRespuesta>DatosIdentificacion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idDevolucion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdDevolucion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdDevolucion"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idReintegro");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdReintegro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdReintegro"));
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
