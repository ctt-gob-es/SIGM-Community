/**
 * Tipo_procedimiento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Tipo_procedimiento  implements java.io.Serializable {
    private java.lang.String codigo_sia;

    private java.lang.String descripcion_sia;

    public Tipo_procedimiento() {
    }

    public Tipo_procedimiento(
           java.lang.String codigo_sia,
           java.lang.String descripcion_sia) {
           this.codigo_sia = codigo_sia;
           this.descripcion_sia = descripcion_sia;
    }


    /**
     * Gets the codigo_sia value for this Tipo_procedimiento.
     * 
     * @return codigo_sia
     */
    public java.lang.String getCodigo_sia() {
        return codigo_sia;
    }


    /**
     * Sets the codigo_sia value for this Tipo_procedimiento.
     * 
     * @param codigo_sia
     */
    public void setCodigo_sia(java.lang.String codigo_sia) {
        this.codigo_sia = codigo_sia;
    }


    /**
     * Gets the descripcion_sia value for this Tipo_procedimiento.
     * 
     * @return descripcion_sia
     */
    public java.lang.String getDescripcion_sia() {
        return descripcion_sia;
    }


    /**
     * Sets the descripcion_sia value for this Tipo_procedimiento.
     * 
     * @param descripcion_sia
     */
    public void setDescripcion_sia(java.lang.String descripcion_sia) {
        this.descripcion_sia = descripcion_sia;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Tipo_procedimiento)) return false;
        Tipo_procedimiento other = (Tipo_procedimiento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo_sia==null && other.getCodigo_sia()==null) || 
             (this.codigo_sia!=null &&
              this.codigo_sia.equals(other.getCodigo_sia()))) &&
            ((this.descripcion_sia==null && other.getDescripcion_sia()==null) || 
             (this.descripcion_sia!=null &&
              this.descripcion_sia.equals(other.getDescripcion_sia())));
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
        if (getCodigo_sia() != null) {
            _hashCode += getCodigo_sia().hashCode();
        }
        if (getDescripcion_sia() != null) {
            _hashCode += getDescripcion_sia().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Tipo_procedimiento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_procedimiento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_sia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_sia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion_sia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcion_sia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
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
    
    public String toString(){
    	
    	String result="";
    	
    	result = result.concat("codigo_sia: ");
    	result = result.concat(codigo_sia);
    	result = result.concat("\n");
    	result = result.concat("descripcion_sia: ");
    	result = result.concat(descripcion_sia);
    	result = result.concat("\n");
    	
    	return result;
    	
    }

}
