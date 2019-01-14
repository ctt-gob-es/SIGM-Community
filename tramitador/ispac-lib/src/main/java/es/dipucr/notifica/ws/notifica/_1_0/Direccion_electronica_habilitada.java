/**
 * Direccion_electronica_habilitada.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Direccion_electronica_habilitada  implements java.io.Serializable {
    private boolean obligado;

    private java.lang.String nif;

    private java.lang.String codigo_procedimiento;

    public Direccion_electronica_habilitada() {
    }

    public Direccion_electronica_habilitada(
           boolean obligado,
           java.lang.String nif,
           java.lang.String codigo_procedimiento) {
           this.obligado = obligado;
           this.nif = nif;
           this.codigo_procedimiento = codigo_procedimiento;
    }


    /**
     * Gets the obligado value for this Direccion_electronica_habilitada.
     * 
     * @return obligado
     */
    public boolean isObligado() {
        return obligado;
    }


    /**
     * Sets the obligado value for this Direccion_electronica_habilitada.
     * 
     * @param obligado
     */
    public void setObligado(boolean obligado) {
        this.obligado = obligado;
    }


    /**
     * Gets the nif value for this Direccion_electronica_habilitada.
     * 
     * @return nif
     */
    public java.lang.String getNif() {
        return nif;
    }


    /**
     * Sets the nif value for this Direccion_electronica_habilitada.
     * 
     * @param nif
     */
    public void setNif(java.lang.String nif) {
        this.nif = nif;
    }


    /**
     * Gets the codigo_procedimiento value for this Direccion_electronica_habilitada.
     * 
     * @return codigo_procedimiento
     */
    public java.lang.String getCodigo_procedimiento() {
        return codigo_procedimiento;
    }


    /**
     * Sets the codigo_procedimiento value for this Direccion_electronica_habilitada.
     * 
     * @param codigo_procedimiento
     */
    public void setCodigo_procedimiento(java.lang.String codigo_procedimiento) {
        this.codigo_procedimiento = codigo_procedimiento;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Direccion_electronica_habilitada)) return false;
        Direccion_electronica_habilitada other = (Direccion_electronica_habilitada) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.obligado == other.isObligado() &&
            ((this.nif==null && other.getNif()==null) || 
             (this.nif!=null &&
              this.nif.equals(other.getNif()))) &&
            ((this.codigo_procedimiento==null && other.getCodigo_procedimiento()==null) || 
             (this.codigo_procedimiento!=null &&
              this.codigo_procedimiento.equals(other.getCodigo_procedimiento())));
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
        _hashCode += (isObligado() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getNif() != null) {
            _hashCode += getNif().hashCode();
        }
        if (getCodigo_procedimiento() != null) {
            _hashCode += getCodigo_procedimiento().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Direccion_electronica_habilitada.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "direccion_electronica_habilitada"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obligado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "obligado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nif");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nif"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_procedimiento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_procedimiento"));
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
    	
    	String result = "";
    	
    	result = result.concat("\n");
    	result = result.concat("obligado (true/false): ");
    	result = result.concat(String.valueOf(obligado));
    	result = result.concat("\n");
    	result = result.concat("nif: ");
    	result = result.concat(nif);
    	result = result.concat("\n");
    	result = result.concat("codigo_procedimiento_DEH: ");
    	result = result.concat(codigo_procedimiento);
    	
    	return result; 
    	
    }

}
