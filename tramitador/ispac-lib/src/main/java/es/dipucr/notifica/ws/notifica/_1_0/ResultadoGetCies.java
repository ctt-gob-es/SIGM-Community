/**
 * ResultadoGetCies.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class ResultadoGetCies  implements java.io.Serializable {
    private es.dipucr.notifica.ws.notifica._1_0.ArrayOfCie cies;

    private java.lang.String codigo_respuesta;

    private java.lang.String descripcion_respuesta;

    public ResultadoGetCies() {
    }

    public ResultadoGetCies(
           es.dipucr.notifica.ws.notifica._1_0.ArrayOfCie cies,
           java.lang.String codigo_respuesta,
           java.lang.String descripcion_respuesta) {
           this.cies = cies;
           this.codigo_respuesta = codigo_respuesta;
           this.descripcion_respuesta = descripcion_respuesta;
    }


    /**
     * Gets the cies value for this ResultadoGetCies.
     * 
     * @return cies
     */
    public es.dipucr.notifica.ws.notifica._1_0.ArrayOfCie getCies() {
        return cies;
    }


    /**
     * Sets the cies value for this ResultadoGetCies.
     * 
     * @param cies
     */
    public void setCies(es.dipucr.notifica.ws.notifica._1_0.ArrayOfCie cies) {
        this.cies = cies;
    }


    /**
     * Gets the codigo_respuesta value for this ResultadoGetCies.
     * 
     * @return codigo_respuesta
     */
    public java.lang.String getCodigo_respuesta() {
        return codigo_respuesta;
    }


    /**
     * Sets the codigo_respuesta value for this ResultadoGetCies.
     * 
     * @param codigo_respuesta
     */
    public void setCodigo_respuesta(java.lang.String codigo_respuesta) {
        this.codigo_respuesta = codigo_respuesta;
    }


    /**
     * Gets the descripcion_respuesta value for this ResultadoGetCies.
     * 
     * @return descripcion_respuesta
     */
    public java.lang.String getDescripcion_respuesta() {
        return descripcion_respuesta;
    }


    /**
     * Sets the descripcion_respuesta value for this ResultadoGetCies.
     * 
     * @param descripcion_respuesta
     */
    public void setDescripcion_respuesta(java.lang.String descripcion_respuesta) {
        this.descripcion_respuesta = descripcion_respuesta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResultadoGetCies)) return false;
        ResultadoGetCies other = (ResultadoGetCies) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cies==null && other.getCies()==null) || 
             (this.cies!=null &&
              this.cies.equals(other.getCies()))) &&
            ((this.codigo_respuesta==null && other.getCodigo_respuesta()==null) || 
             (this.codigo_respuesta!=null &&
              this.codigo_respuesta.equals(other.getCodigo_respuesta()))) &&
            ((this.descripcion_respuesta==null && other.getDescripcion_respuesta()==null) || 
             (this.descripcion_respuesta!=null &&
              this.descripcion_respuesta.equals(other.getDescripcion_respuesta())));
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
        if (getCies() != null) {
            _hashCode += getCies().hashCode();
        }
        if (getCodigo_respuesta() != null) {
            _hashCode += getCodigo_respuesta().hashCode();
        }
        if (getDescripcion_respuesta() != null) {
            _hashCode += getDescripcion_respuesta().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResultadoGetCies.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultadoGetCies"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cies");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cies"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "ArrayOfCie"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_respuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_respuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion_respuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcion_respuesta"));
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
