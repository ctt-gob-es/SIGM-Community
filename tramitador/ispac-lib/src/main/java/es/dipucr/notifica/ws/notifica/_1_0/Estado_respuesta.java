/**
 * Estado_respuesta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Estado_respuesta  implements java.io.Serializable {
    private java.lang.String identificador_envio;

    private java.lang.String estado;

    private java.lang.String ncc_id_externo;

    public Estado_respuesta() {
    }

    public Estado_respuesta(
           java.lang.String identificador_envio,
           java.lang.String estado,
           java.lang.String ncc_id_externo) {
           this.identificador_envio = identificador_envio;
           this.estado = estado;
           this.ncc_id_externo = ncc_id_externo;
    }


    /**
     * Gets the identificador_envio value for this Estado_respuesta.
     * 
     * @return identificador_envio
     */
    public java.lang.String getIdentificador_envio() {
        return identificador_envio;
    }


    /**
     * Sets the identificador_envio value for this Estado_respuesta.
     * 
     * @param identificador_envio
     */
    public void setIdentificador_envio(java.lang.String identificador_envio) {
        this.identificador_envio = identificador_envio;
    }


    /**
     * Gets the estado value for this Estado_respuesta.
     * 
     * @return estado
     */
    public java.lang.String getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this Estado_respuesta.
     * 
     * @param estado
     */
    public void setEstado(java.lang.String estado) {
        this.estado = estado;
    }


    /**
     * Gets the ncc_id_externo value for this Estado_respuesta.
     * 
     * @return ncc_id_externo
     */
    public java.lang.String getNcc_id_externo() {
        return ncc_id_externo;
    }


    /**
     * Sets the ncc_id_externo value for this Estado_respuesta.
     * 
     * @param ncc_id_externo
     */
    public void setNcc_id_externo(java.lang.String ncc_id_externo) {
        this.ncc_id_externo = ncc_id_externo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Estado_respuesta)) return false;
        Estado_respuesta other = (Estado_respuesta) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.identificador_envio==null && other.getIdentificador_envio()==null) || 
             (this.identificador_envio!=null &&
              this.identificador_envio.equals(other.getIdentificador_envio()))) &&
            ((this.estado==null && other.getEstado()==null) || 
             (this.estado!=null &&
              this.estado.equals(other.getEstado()))) &&
            ((this.ncc_id_externo==null && other.getNcc_id_externo()==null) || 
             (this.ncc_id_externo!=null &&
              this.ncc_id_externo.equals(other.getNcc_id_externo())));
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
        if (getIdentificador_envio() != null) {
            _hashCode += getIdentificador_envio().hashCode();
        }
        if (getEstado() != null) {
            _hashCode += getEstado().hashCode();
        }
        if (getNcc_id_externo() != null) {
            _hashCode += getNcc_id_externo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Estado_respuesta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "estado_respuesta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificador_envio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identificador_envio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ncc_id_externo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ncc_id_externo"));
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
