/**
 * Documento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Documento  implements java.io.Serializable {
    private java.lang.String contenido;

    private java.lang.String hash_sha1;

    private java.lang.String normalizado;

    private java.lang.String generar_csv;

    public Documento() {
    }

    public Documento(
           java.lang.String contenido,
           java.lang.String hash_sha1,
           java.lang.String normalizado,
           java.lang.String generar_csv) {
           this.contenido = contenido;
           this.hash_sha1 = hash_sha1;
           this.normalizado = normalizado;
           this.generar_csv = generar_csv;
    }


    /**
     * Gets the contenido value for this Documento.
     * 
     * @return contenido
     */
    public java.lang.String getContenido() {
        return contenido;
    }


    /**
     * Sets the contenido value for this Documento.
     * 
     * @param contenido
     */
    public void setContenido(java.lang.String contenido) {
        this.contenido = contenido;
    }


    /**
     * Gets the hash_sha1 value for this Documento.
     * 
     * @return hash_sha1
     */
    public java.lang.String getHash_sha1() {
        return hash_sha1;
    }


    /**
     * Sets the hash_sha1 value for this Documento.
     * 
     * @param hash_sha1
     */
    public void setHash_sha1(java.lang.String hash_sha1) {
        this.hash_sha1 = hash_sha1;
    }


    /**
     * Gets the normalizado value for this Documento.
     * 
     * @return normalizado
     */
    public java.lang.String getNormalizado() {
        return normalizado;
    }


    /**
     * Sets the normalizado value for this Documento.
     * 
     * @param normalizado
     */
    public void setNormalizado(java.lang.String normalizado) {
        this.normalizado = normalizado;
    }


    /**
     * Gets the generar_csv value for this Documento.
     * 
     * @return generar_csv
     */
    public java.lang.String getGenerar_csv() {
        return generar_csv;
    }


    /**
     * Sets the generar_csv value for this Documento.
     * 
     * @param generar_csv
     */
    public void setGenerar_csv(java.lang.String generar_csv) {
        this.generar_csv = generar_csv;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Documento)) return false;
        Documento other = (Documento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.contenido==null && other.getContenido()==null) || 
             (this.contenido!=null &&
              this.contenido.equals(other.getContenido()))) &&
            ((this.hash_sha1==null && other.getHash_sha1()==null) || 
             (this.hash_sha1!=null &&
              this.hash_sha1.equals(other.getHash_sha1()))) &&
            ((this.normalizado==null && other.getNormalizado()==null) || 
             (this.normalizado!=null &&
              this.normalizado.equals(other.getNormalizado()))) &&
            ((this.generar_csv==null && other.getGenerar_csv()==null) || 
             (this.generar_csv!=null &&
              this.generar_csv.equals(other.getGenerar_csv())));
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
        if (getContenido() != null) {
            _hashCode += getContenido().hashCode();
        }
        if (getHash_sha1() != null) {
            _hashCode += getHash_sha1().hashCode();
        }
        if (getNormalizado() != null) {
            _hashCode += getNormalizado().hashCode();
        }
        if (getGenerar_csv() != null) {
            _hashCode += getGenerar_csv().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Documento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "documento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contenido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contenido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hash_sha1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hash_sha1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("normalizado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "normalizado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("generar_csv");
        elemField.setXmlName(new javax.xml.namespace.QName("", "generar_csv"));
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
    	
    	result = result.concat("contenido length: ");
    	result = result.concat(String.valueOf(contenido.length()));
    	result = result.concat("\n");
    	result = result.concat("hash_sha1: ");
    	result = result.concat(hash_sha1);
    	result = result.concat("\n");
    	result = result.concat("normalizado: ");
    	result = result.concat(normalizado);
    	result = result.concat("\n");
    	result = result.concat("generar_csv: ");
    	result = result.concat(generar_csv);
    	
    	return result;
    	
    }

}
