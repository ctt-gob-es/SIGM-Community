/**
 * AdministradorWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notificador.model;

public class AdministradorWS  implements java.io.Serializable {
    private java.lang.Object[] aplicaciones;

    private java.lang.String clave;

    private java.util.Calendar fecAlta;

    private java.util.Calendar fecBaja;

    private int idAdministrador;

    private java.lang.String usuario;

    public AdministradorWS() {
    }

    public AdministradorWS(
           java.lang.Object[] aplicaciones,
           java.lang.String clave,
           java.util.Calendar fecAlta,
           java.util.Calendar fecBaja,
           int idAdministrador,
           java.lang.String usuario) {
           this.aplicaciones = aplicaciones;
           this.clave = clave;
           this.fecAlta = fecAlta;
           this.fecBaja = fecBaja;
           this.idAdministrador = idAdministrador;
           this.usuario = usuario;
    }


    /**
     * Gets the aplicaciones value for this AdministradorWS.
     * 
     * @return aplicaciones
     */
    public java.lang.Object[] getAplicaciones() {
        return aplicaciones;
    }


    /**
     * Sets the aplicaciones value for this AdministradorWS.
     * 
     * @param aplicaciones
     */
    public void setAplicaciones(java.lang.Object[] aplicaciones) {
        this.aplicaciones = aplicaciones;
    }


    /**
     * Gets the clave value for this AdministradorWS.
     * 
     * @return clave
     */
    public java.lang.String getClave() {
        return clave;
    }


    /**
     * Sets the clave value for this AdministradorWS.
     * 
     * @param clave
     */
    public void setClave(java.lang.String clave) {
        this.clave = clave;
    }


    /**
     * Gets the fecAlta value for this AdministradorWS.
     * 
     * @return fecAlta
     */
    public java.util.Calendar getFecAlta() {
        return fecAlta;
    }


    /**
     * Sets the fecAlta value for this AdministradorWS.
     * 
     * @param fecAlta
     */
    public void setFecAlta(java.util.Calendar fecAlta) {
        this.fecAlta = fecAlta;
    }


    /**
     * Gets the fecBaja value for this AdministradorWS.
     * 
     * @return fecBaja
     */
    public java.util.Calendar getFecBaja() {
        return fecBaja;
    }


    /**
     * Sets the fecBaja value for this AdministradorWS.
     * 
     * @param fecBaja
     */
    public void setFecBaja(java.util.Calendar fecBaja) {
        this.fecBaja = fecBaja;
    }


    /**
     * Gets the idAdministrador value for this AdministradorWS.
     * 
     * @return idAdministrador
     */
    public int getIdAdministrador() {
        return idAdministrador;
    }


    /**
     * Sets the idAdministrador value for this AdministradorWS.
     * 
     * @param idAdministrador
     */
    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }


    /**
     * Gets the usuario value for this AdministradorWS.
     * 
     * @return usuario
     */
    public java.lang.String getUsuario() {
        return usuario;
    }


    /**
     * Sets the usuario value for this AdministradorWS.
     * 
     * @param usuario
     */
    public void setUsuario(java.lang.String usuario) {
        this.usuario = usuario;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AdministradorWS)) return false;
        AdministradorWS other = (AdministradorWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.aplicaciones==null && other.getAplicaciones()==null) || 
             (this.aplicaciones!=null &&
              java.util.Arrays.equals(this.aplicaciones, other.getAplicaciones()))) &&
            ((this.clave==null && other.getClave()==null) || 
             (this.clave!=null &&
              this.clave.equals(other.getClave()))) &&
            ((this.fecAlta==null && other.getFecAlta()==null) || 
             (this.fecAlta!=null &&
              this.fecAlta.equals(other.getFecAlta()))) &&
            ((this.fecBaja==null && other.getFecBaja()==null) || 
             (this.fecBaja!=null &&
              this.fecBaja.equals(other.getFecBaja()))) &&
            this.idAdministrador == other.getIdAdministrador() &&
            ((this.usuario==null && other.getUsuario()==null) || 
             (this.usuario!=null &&
              this.usuario.equals(other.getUsuario())));
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
        if (getAplicaciones() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAplicaciones());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAplicaciones(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getClave() != null) {
            _hashCode += getClave().hashCode();
        }
        if (getFecAlta() != null) {
            _hashCode += getFecAlta().hashCode();
        }
        if (getFecBaja() != null) {
            _hashCode += getFecBaja().hashCode();
        }
        _hashCode += getIdAdministrador();
        if (getUsuario() != null) {
            _hashCode += getUsuario().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AdministradorWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "AdministradorWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aplicaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "aplicaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clave");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "clave"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecAlta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "fecAlta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecBaja");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "fecBaja"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAdministrador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "idAdministrador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usuario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "usuario"));
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
