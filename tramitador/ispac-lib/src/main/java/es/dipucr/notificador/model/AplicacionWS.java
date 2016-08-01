/**
 * AplicacionWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notificador.model;

public class AplicacionWS  implements java.io.Serializable {
    private es.dipucr.notificador.model.AdministradorWS administrador;

    private java.lang.String clave;

    private java.util.Calendar fecAlta;

    private java.util.Calendar fecBaja;

    private int idAplicacion;

    private java.lang.String nombre;

    private java.lang.Object[] notificaciones;

    private java.lang.String usuario;

    public AplicacionWS() {
    }

    public AplicacionWS(
           es.dipucr.notificador.model.AdministradorWS administrador,
           java.lang.String clave,
           java.util.Calendar fecAlta,
           java.util.Calendar fecBaja,
           int idAplicacion,
           java.lang.String nombre,
           java.lang.Object[] notificaciones,
           java.lang.String usuario) {
           this.administrador = administrador;
           this.clave = clave;
           this.fecAlta = fecAlta;
           this.fecBaja = fecBaja;
           this.idAplicacion = idAplicacion;
           this.nombre = nombre;
           this.notificaciones = notificaciones;
           this.usuario = usuario;
    }


    /**
     * Gets the administrador value for this AplicacionWS.
     * 
     * @return administrador
     */
    public es.dipucr.notificador.model.AdministradorWS getAdministrador() {
        return administrador;
    }


    /**
     * Sets the administrador value for this AplicacionWS.
     * 
     * @param administrador
     */
    public void setAdministrador(es.dipucr.notificador.model.AdministradorWS administrador) {
        this.administrador = administrador;
    }


    /**
     * Gets the clave value for this AplicacionWS.
     * 
     * @return clave
     */
    public java.lang.String getClave() {
        return clave;
    }


    /**
     * Sets the clave value for this AplicacionWS.
     * 
     * @param clave
     */
    public void setClave(java.lang.String clave) {
        this.clave = clave;
    }


    /**
     * Gets the fecAlta value for this AplicacionWS.
     * 
     * @return fecAlta
     */
    public java.util.Calendar getFecAlta() {
        return fecAlta;
    }


    /**
     * Sets the fecAlta value for this AplicacionWS.
     * 
     * @param fecAlta
     */
    public void setFecAlta(java.util.Calendar fecAlta) {
        this.fecAlta = fecAlta;
    }


    /**
     * Gets the fecBaja value for this AplicacionWS.
     * 
     * @return fecBaja
     */
    public java.util.Calendar getFecBaja() {
        return fecBaja;
    }


    /**
     * Sets the fecBaja value for this AplicacionWS.
     * 
     * @param fecBaja
     */
    public void setFecBaja(java.util.Calendar fecBaja) {
        this.fecBaja = fecBaja;
    }


    /**
     * Gets the idAplicacion value for this AplicacionWS.
     * 
     * @return idAplicacion
     */
    public int getIdAplicacion() {
        return idAplicacion;
    }


    /**
     * Sets the idAplicacion value for this AplicacionWS.
     * 
     * @param idAplicacion
     */
    public void setIdAplicacion(int idAplicacion) {
        this.idAplicacion = idAplicacion;
    }


    /**
     * Gets the nombre value for this AplicacionWS.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this AplicacionWS.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the notificaciones value for this AplicacionWS.
     * 
     * @return notificaciones
     */
    public java.lang.Object[] getNotificaciones() {
        return notificaciones;
    }


    /**
     * Sets the notificaciones value for this AplicacionWS.
     * 
     * @param notificaciones
     */
    public void setNotificaciones(java.lang.Object[] notificaciones) {
        this.notificaciones = notificaciones;
    }


    /**
     * Gets the usuario value for this AplicacionWS.
     * 
     * @return usuario
     */
    public java.lang.String getUsuario() {
        return usuario;
    }


    /**
     * Sets the usuario value for this AplicacionWS.
     * 
     * @param usuario
     */
    public void setUsuario(java.lang.String usuario) {
        this.usuario = usuario;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AplicacionWS)) return false;
        AplicacionWS other = (AplicacionWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.administrador==null && other.getAdministrador()==null) || 
             (this.administrador!=null &&
              this.administrador.equals(other.getAdministrador()))) &&
            ((this.clave==null && other.getClave()==null) || 
             (this.clave!=null &&
              this.clave.equals(other.getClave()))) &&
            ((this.fecAlta==null && other.getFecAlta()==null) || 
             (this.fecAlta!=null &&
              this.fecAlta.equals(other.getFecAlta()))) &&
            ((this.fecBaja==null && other.getFecBaja()==null) || 
             (this.fecBaja!=null &&
              this.fecBaja.equals(other.getFecBaja()))) &&
            this.idAplicacion == other.getIdAplicacion() &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.notificaciones==null && other.getNotificaciones()==null) || 
             (this.notificaciones!=null &&
              java.util.Arrays.equals(this.notificaciones, other.getNotificaciones()))) &&
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
        if (getAdministrador() != null) {
            _hashCode += getAdministrador().hashCode();
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
        _hashCode += getIdAplicacion();
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getNotificaciones() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNotificaciones());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNotificaciones(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getUsuario() != null) {
            _hashCode += getUsuario().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AplicacionWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "AplicacionWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("administrador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "administrador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "AdministradorWS"));
        elemField.setNillable(true);
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
        elemField.setFieldName("idAplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "idAplicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "notificaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "item"));
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
