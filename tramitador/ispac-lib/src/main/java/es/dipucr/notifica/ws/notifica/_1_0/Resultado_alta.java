/**
 * Resultado_alta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Resultado_alta  implements java.io.Serializable {
    private es.dipucr.notifica.ws.notifica._1_0.ArrayOfIdentificador_envio identificadores;

    private java.lang.String codigo_csv;

    private java.lang.String codigo_respuesta;

    private java.lang.String descripcion_respuesta;

    public Resultado_alta() {
    }

    public Resultado_alta(
           es.dipucr.notifica.ws.notifica._1_0.ArrayOfIdentificador_envio identificadores,
           java.lang.String codigo_csv,
           java.lang.String codigo_respuesta,
           java.lang.String descripcion_respuesta) {
           this.identificadores = identificadores;
           this.codigo_csv = codigo_csv;
           this.codigo_respuesta = codigo_respuesta;
           this.descripcion_respuesta = descripcion_respuesta;
    }


    /**
     * Gets the identificadores value for this Resultado_alta.
     * 
     * @return identificadores
     */
    public es.dipucr.notifica.ws.notifica._1_0.ArrayOfIdentificador_envio getIdentificadores() {
        return identificadores;
    }


    /**
     * Sets the identificadores value for this Resultado_alta.
     * 
     * @param identificadores
     */
    public void setIdentificadores(es.dipucr.notifica.ws.notifica._1_0.ArrayOfIdentificador_envio identificadores) {
        this.identificadores = identificadores;
    }


    /**
     * Gets the codigo_csv value for this Resultado_alta.
     * 
     * @return codigo_csv
     */
    public java.lang.String getCodigo_csv() {
        return codigo_csv;
    }


    /**
     * Sets the codigo_csv value for this Resultado_alta.
     * 
     * @param codigo_csv
     */
    public void setCodigo_csv(java.lang.String codigo_csv) {
        this.codigo_csv = codigo_csv;
    }


    /**
     * Gets the codigo_respuesta value for this Resultado_alta.
     * 
     * @return codigo_respuesta
     */
    public java.lang.String getCodigo_respuesta() {
        return codigo_respuesta;
    }


    /**
     * Sets the codigo_respuesta value for this Resultado_alta.
     * 
     * @param codigo_respuesta
     */
    public void setCodigo_respuesta(java.lang.String codigo_respuesta) {
        this.codigo_respuesta = codigo_respuesta;
    }


    /**
     * Gets the descripcion_respuesta value for this Resultado_alta.
     * 
     * @return descripcion_respuesta
     */
    public java.lang.String getDescripcion_respuesta() {
        return descripcion_respuesta;
    }


    /**
     * Sets the descripcion_respuesta value for this Resultado_alta.
     * 
     * @param descripcion_respuesta
     */
    public void setDescripcion_respuesta(java.lang.String descripcion_respuesta) {
        this.descripcion_respuesta = descripcion_respuesta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Resultado_alta)) return false;
        Resultado_alta other = (Resultado_alta) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.identificadores==null && other.getIdentificadores()==null) || 
             (this.identificadores!=null &&
              this.identificadores.equals(other.getIdentificadores()))) &&
            ((this.codigo_csv==null && other.getCodigo_csv()==null) || 
             (this.codigo_csv!=null &&
              this.codigo_csv.equals(other.getCodigo_csv()))) &&
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
        if (getIdentificadores() != null) {
            _hashCode += getIdentificadores().hashCode();
        }
        if (getCodigo_csv() != null) {
            _hashCode += getCodigo_csv().hashCode();
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
        new org.apache.axis.description.TypeDesc(Resultado_alta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "resultado_alta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificadores");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identificadores"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "ArrayOfIdentificador_envio"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_csv");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_csv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
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
    
    public String toString(){

    	String result="";
    	
    	result = result.concat("\n");
    	result = result.concat("codigo_respuesta: ");
    	result = result.concat(codigo_respuesta);
    	result = result.concat("\n");
    	result = result.concat("descripcion_respuesta: ");
    	result = result.concat(descripcion_respuesta);
    	result = result.concat("\n");
    	result = result.concat("descripcion_respuesta: ");
    	result = result.concat(descripcion_respuesta);
    	result = result.concat("\n");
    	result = result.concat("identificadores: ");
    	result = result.concat(identificadores.toString());
    	
    	return result;
    	
    }

}
