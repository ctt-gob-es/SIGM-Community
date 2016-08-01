/**
 * Titular.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.peticion;

public class Titular  implements java.io.Serializable {
    private java.lang.String tipoDocumentacion;

    private java.lang.String documentacion;

    public Titular() {
    }

    public Titular(
           java.lang.String tipoDocumentacion,
           java.lang.String documentacion) {
           this.tipoDocumentacion = tipoDocumentacion;
           this.documentacion = documentacion;
    }


    /**
     * Gets the tipoDocumentacion value for this Titular.
     * 
     * @return tipoDocumentacion
     */
    public java.lang.String getTipoDocumentacion() {
        return tipoDocumentacion;
    }


    /**
     * Sets the tipoDocumentacion value for this Titular.
     * 
     * @param tipoDocumentacion
     */
    public void setTipoDocumentacion(java.lang.String tipoDocumentacion) {
        this.tipoDocumentacion = tipoDocumentacion;
    }


    /**
     * Gets the documentacion value for this Titular.
     * 
     * @return documentacion
     */
    public java.lang.String getDocumentacion() {
        return documentacion;
    }


    /**
     * Sets the documentacion value for this Titular.
     * 
     * @param documentacion
     */
    public void setDocumentacion(java.lang.String documentacion) {
        this.documentacion = documentacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Titular)) return false;
        Titular other = (Titular) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tipoDocumentacion==null && other.getTipoDocumentacion()==null) || 
             (this.tipoDocumentacion!=null &&
              this.tipoDocumentacion.equals(other.getTipoDocumentacion()))) &&
            ((this.documentacion==null && other.getDocumentacion()==null) || 
             (this.documentacion!=null &&
              this.documentacion.equals(other.getDocumentacion())));
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
        if (getTipoDocumentacion() != null) {
            _hashCode += getTipoDocumentacion().hashCode();
        }
        if (getDocumentacion() != null) {
            _hashCode += getDocumentacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Titular.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/peticion", ">Titular"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoDocumentacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/peticion", "TipoDocumentacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/peticion", "Documentacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
