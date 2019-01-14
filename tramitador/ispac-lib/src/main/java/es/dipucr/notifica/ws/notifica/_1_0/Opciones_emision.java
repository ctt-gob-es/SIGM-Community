/**
 * Opciones_emision.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Opciones_emision  implements java.io.Serializable {
    private java.lang.Integer retardo_postal_deh;

    private java.lang.String caducidad;

    public Opciones_emision() {
    }

    public Opciones_emision(
           java.lang.Integer retardo_postal_deh,
           java.lang.String caducidad) {
           this.retardo_postal_deh = retardo_postal_deh;
           this.caducidad = caducidad;
    }


    /**
     * Gets the retardo_postal_deh value for this Opciones_emision.
     * 
     * @return retardo_postal_deh
     */
    public java.lang.Integer getRetardo_postal_deh() {
        return retardo_postal_deh;
    }


    /**
     * Sets the retardo_postal_deh value for this Opciones_emision.
     * 
     * @param retardo_postal_deh
     */
    public void setRetardo_postal_deh(java.lang.Integer retardo_postal_deh) {
        this.retardo_postal_deh = retardo_postal_deh;
    }


    /**
     * Gets the caducidad value for this Opciones_emision.
     * 
     * @return caducidad
     */
    public java.lang.String getCaducidad() {
        return caducidad;
    }


    /**
     * Sets the caducidad value for this Opciones_emision.
     * 
     * @param caducidad
     */
    public void setCaducidad(java.lang.String caducidad) {
        this.caducidad = caducidad;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Opciones_emision)) return false;
        Opciones_emision other = (Opciones_emision) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.retardo_postal_deh==null && other.getRetardo_postal_deh()==null) || 
             (this.retardo_postal_deh!=null &&
              this.retardo_postal_deh.equals(other.getRetardo_postal_deh()))) &&
            ((this.caducidad==null && other.getCaducidad()==null) || 
             (this.caducidad!=null &&
              this.caducidad.equals(other.getCaducidad())));
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
        if (getRetardo_postal_deh() != null) {
            _hashCode += getRetardo_postal_deh().hashCode();
        }
        if (getCaducidad() != null) {
            _hashCode += getCaducidad().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Opciones_emision.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "opciones_emision"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retardo_postal_deh");
        elemField.setXmlName(new javax.xml.namespace.QName("", "retardo_postal_deh"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("caducidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "caducidad"));
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
    	
    	result = result.concat("\n");
    	result = result.concat("dias en carpeta ciudadana antes de pasar a la deh (retardo_postal_deh): ");
    	result = result.concat(String.valueOf(retardo_postal_deh));
    	result = result.concat("\n");
    	result = result.concat("fecha de caducidad: ");
    	result = result.concat(caducidad);
    	
    	return result;
    	
    }

}
