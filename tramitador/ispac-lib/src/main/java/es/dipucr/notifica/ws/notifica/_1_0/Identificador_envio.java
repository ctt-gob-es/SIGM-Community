/**
 * Identificador_envio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Identificador_envio  implements java.io.Serializable {
    private java.lang.String identificador;

    private java.lang.String referencia_emisor;

    private java.lang.String nif_titular;

    public Identificador_envio() {
    }

    public Identificador_envio(
           java.lang.String identificador,
           java.lang.String referencia_emisor,
           java.lang.String nif_titular) {
           this.identificador = identificador;
           this.referencia_emisor = referencia_emisor;
           this.nif_titular = nif_titular;
    }


    /**
     * Gets the identificador value for this Identificador_envio.
     * 
     * @return identificador
     */
    public java.lang.String getIdentificador() {
        return identificador;
    }


    /**
     * Sets the identificador value for this Identificador_envio.
     * 
     * @param identificador
     */
    public void setIdentificador(java.lang.String identificador) {
        this.identificador = identificador;
    }


    /**
     * Gets the referencia_emisor value for this Identificador_envio.
     * 
     * @return referencia_emisor
     */
    public java.lang.String getReferencia_emisor() {
        return referencia_emisor;
    }


    /**
     * Sets the referencia_emisor value for this Identificador_envio.
     * 
     * @param referencia_emisor
     */
    public void setReferencia_emisor(java.lang.String referencia_emisor) {
        this.referencia_emisor = referencia_emisor;
    }


    /**
     * Gets the nif_titular value for this Identificador_envio.
     * 
     * @return nif_titular
     */
    public java.lang.String getNif_titular() {
        return nif_titular;
    }


    /**
     * Sets the nif_titular value for this Identificador_envio.
     * 
     * @param nif_titular
     */
    public void setNif_titular(java.lang.String nif_titular) {
        this.nif_titular = nif_titular;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Identificador_envio)) return false;
        Identificador_envio other = (Identificador_envio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.identificador==null && other.getIdentificador()==null) || 
             (this.identificador!=null &&
              this.identificador.equals(other.getIdentificador()))) &&
            ((this.referencia_emisor==null && other.getReferencia_emisor()==null) || 
             (this.referencia_emisor!=null &&
              this.referencia_emisor.equals(other.getReferencia_emisor()))) &&
            ((this.nif_titular==null && other.getNif_titular()==null) || 
             (this.nif_titular!=null &&
              this.nif_titular.equals(other.getNif_titular())));
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
        if (getIdentificador() != null) {
            _hashCode += getIdentificador().hashCode();
        }
        if (getReferencia_emisor() != null) {
            _hashCode += getReferencia_emisor().hashCode();
        }
        if (getNif_titular() != null) {
            _hashCode += getNif_titular().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Identificador_envio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "identificador_envio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificador");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identificador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referencia_emisor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "referencia_emisor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nif_titular");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nif_titular"));
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

}
