/**
 * TipoOrganismoEmisor.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class TipoOrganismoEmisor  implements java.io.Serializable {
    private java.lang.String codigo_dir3;

    private java.lang.String nombre;

    public TipoOrganismoEmisor() {
    }

    public TipoOrganismoEmisor(
           java.lang.String codigo_dir3,
           java.lang.String nombre) {
           this.codigo_dir3 = codigo_dir3;
           this.nombre = nombre;
    }


    /**
     * Gets the codigo_dir3 value for this TipoOrganismoEmisor.
     * 
     * @return codigo_dir3
     */
    public java.lang.String getCodigo_dir3() {
        return codigo_dir3;
    }


    /**
     * Sets the codigo_dir3 value for this TipoOrganismoEmisor.
     * 
     * @param codigo_dir3
     */
    public void setCodigo_dir3(java.lang.String codigo_dir3) {
        this.codigo_dir3 = codigo_dir3;
    }


    /**
     * Gets the nombre value for this TipoOrganismoEmisor.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this TipoOrganismoEmisor.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TipoOrganismoEmisor)) return false;
        TipoOrganismoEmisor other = (TipoOrganismoEmisor) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo_dir3==null && other.getCodigo_dir3()==null) || 
             (this.codigo_dir3!=null &&
              this.codigo_dir3.equals(other.getCodigo_dir3()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre())));
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
        if (getCodigo_dir3() != null) {
            _hashCode += getCodigo_dir3().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TipoOrganismoEmisor.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipoOrganismoEmisor"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_dir3");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_dir3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre"));
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
    	
        result=result.concat("DIR3: ");
   	    result=result.concat(this.codigo_dir3);
   	    result=result.concat("\n");
   	    result=result.concat("nombre: ");
   	    result=result.concat(this.nombre);
   	    result=result.concat("\n");

    	return result;
    	
    }

}
