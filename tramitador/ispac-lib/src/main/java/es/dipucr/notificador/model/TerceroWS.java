/**
 * TerceroWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notificador.model;

public class TerceroWS  implements java.io.Serializable {
    private java.lang.String dni;

    private java.lang.String email;

    private java.util.Calendar fecAlta;

    private java.util.Calendar fecBaja;

    private java.util.Calendar fecModificacion;

    private int idTercero;

    private java.lang.Object[] notificaciones;

    private java.lang.Integer telefono;

    public TerceroWS() {
    }

    public TerceroWS(
           java.lang.String dni,
           java.lang.String email,
           java.util.Calendar fecAlta,
           java.util.Calendar fecBaja,
           java.util.Calendar fecModificacion,
           int idTercero,
           java.lang.Object[] notificaciones,
           java.lang.Integer telefono) {
           this.dni = dni;
           this.email = email;
           this.fecAlta = fecAlta;
           this.fecBaja = fecBaja;
           this.fecModificacion = fecModificacion;
           this.idTercero = idTercero;
           this.notificaciones = notificaciones;
           this.telefono = telefono;
    }


    /**
     * Gets the dni value for this TerceroWS.
     * 
     * @return dni
     */
    public java.lang.String getDni() {
        return dni;
    }


    /**
     * Sets the dni value for this TerceroWS.
     * 
     * @param dni
     */
    public void setDni(java.lang.String dni) {
        this.dni = dni;
    }


    /**
     * Gets the email value for this TerceroWS.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this TerceroWS.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the fecAlta value for this TerceroWS.
     * 
     * @return fecAlta
     */
    public java.util.Calendar getFecAlta() {
        return fecAlta;
    }


    /**
     * Sets the fecAlta value for this TerceroWS.
     * 
     * @param fecAlta
     */
    public void setFecAlta(java.util.Calendar fecAlta) {
        this.fecAlta = fecAlta;
    }


    /**
     * Gets the fecBaja value for this TerceroWS.
     * 
     * @return fecBaja
     */
    public java.util.Calendar getFecBaja() {
        return fecBaja;
    }


    /**
     * Sets the fecBaja value for this TerceroWS.
     * 
     * @param fecBaja
     */
    public void setFecBaja(java.util.Calendar fecBaja) {
        this.fecBaja = fecBaja;
    }


    /**
     * Gets the fecModificacion value for this TerceroWS.
     * 
     * @return fecModificacion
     */
    public java.util.Calendar getFecModificacion() {
        return fecModificacion;
    }


    /**
     * Sets the fecModificacion value for this TerceroWS.
     * 
     * @param fecModificacion
     */
    public void setFecModificacion(java.util.Calendar fecModificacion) {
        this.fecModificacion = fecModificacion;
    }


    /**
     * Gets the idTercero value for this TerceroWS.
     * 
     * @return idTercero
     */
    public int getIdTercero() {
        return idTercero;
    }


    /**
     * Sets the idTercero value for this TerceroWS.
     * 
     * @param idTercero
     */
    public void setIdTercero(int idTercero) {
        this.idTercero = idTercero;
    }


    /**
     * Gets the notificaciones value for this TerceroWS.
     * 
     * @return notificaciones
     */
    public java.lang.Object[] getNotificaciones() {
        return notificaciones;
    }


    /**
     * Sets the notificaciones value for this TerceroWS.
     * 
     * @param notificaciones
     */
    public void setNotificaciones(java.lang.Object[] notificaciones) {
        this.notificaciones = notificaciones;
    }


    /**
     * Gets the telefono value for this TerceroWS.
     * 
     * @return telefono
     */
    public java.lang.Integer getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this TerceroWS.
     * 
     * @param telefono
     */
    public void setTelefono(java.lang.Integer telefono) {
        this.telefono = telefono;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TerceroWS)) return false;
        TerceroWS other = (TerceroWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dni==null && other.getDni()==null) || 
             (this.dni!=null &&
              this.dni.equals(other.getDni()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.fecAlta==null && other.getFecAlta()==null) || 
             (this.fecAlta!=null &&
              this.fecAlta.equals(other.getFecAlta()))) &&
            ((this.fecBaja==null && other.getFecBaja()==null) || 
             (this.fecBaja!=null &&
              this.fecBaja.equals(other.getFecBaja()))) &&
            ((this.fecModificacion==null && other.getFecModificacion()==null) || 
             (this.fecModificacion!=null &&
              this.fecModificacion.equals(other.getFecModificacion()))) &&
            this.idTercero == other.getIdTercero() &&
            ((this.notificaciones==null && other.getNotificaciones()==null) || 
             (this.notificaciones!=null &&
              java.util.Arrays.equals(this.notificaciones, other.getNotificaciones()))) &&
            ((this.telefono==null && other.getTelefono()==null) || 
             (this.telefono!=null &&
              this.telefono.equals(other.getTelefono())));
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
        if (getDni() != null) {
            _hashCode += getDni().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getFecAlta() != null) {
            _hashCode += getFecAlta().hashCode();
        }
        if (getFecBaja() != null) {
            _hashCode += getFecBaja().hashCode();
        }
        if (getFecModificacion() != null) {
            _hashCode += getFecModificacion().hashCode();
        }
        _hashCode += getIdTercero();
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
        if (getTelefono() != null) {
            _hashCode += getTelefono().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TerceroWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "TerceroWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dni");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "dni"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "email"));
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
        elemField.setFieldName("fecModificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "fecModificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTercero");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "idTercero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "notificaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefono");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "telefono"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
