/**
 * EnvioWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notificador.model;

public class EnvioWS  implements java.io.Serializable {
    private java.lang.String dni;

    private boolean email;

    private int idNotificacion;

    private boolean notificacion;

    private boolean sms;

    public EnvioWS() {
    }

    public EnvioWS(
           java.lang.String dni,
           boolean email,
           int idNotificacion,
           boolean notificacion,
           boolean sms) {
           this.dni = dni;
           this.email = email;
           this.idNotificacion = idNotificacion;
           this.notificacion = notificacion;
           this.sms = sms;
    }


    /**
     * Gets the dni value for this EnvioWS.
     * 
     * @return dni
     */
    public java.lang.String getDni() {
        return dni;
    }


    /**
     * Sets the dni value for this EnvioWS.
     * 
     * @param dni
     */
    public void setDni(java.lang.String dni) {
        this.dni = dni;
    }


    /**
     * Gets the email value for this EnvioWS.
     * 
     * @return email
     */
    public boolean isEmail() {
        return email;
    }


    /**
     * Sets the email value for this EnvioWS.
     * 
     * @param email
     */
    public void setEmail(boolean email) {
        this.email = email;
    }


    /**
     * Gets the idNotificacion value for this EnvioWS.
     * 
     * @return idNotificacion
     */
    public int getIdNotificacion() {
        return idNotificacion;
    }


    /**
     * Sets the idNotificacion value for this EnvioWS.
     * 
     * @param idNotificacion
     */
    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }


    /**
     * Gets the notificacion value for this EnvioWS.
     * 
     * @return notificacion
     */
    public boolean isNotificacion() {
        return notificacion;
    }


    /**
     * Sets the notificacion value for this EnvioWS.
     * 
     * @param notificacion
     */
    public void setNotificacion(boolean notificacion) {
        this.notificacion = notificacion;
    }


    /**
     * Gets the sms value for this EnvioWS.
     * 
     * @return sms
     */
    public boolean isSms() {
        return sms;
    }


    /**
     * Sets the sms value for this EnvioWS.
     * 
     * @param sms
     */
    public void setSms(boolean sms) {
        this.sms = sms;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EnvioWS)) return false;
        EnvioWS other = (EnvioWS) obj;
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
            this.email == other.isEmail() &&
            this.idNotificacion == other.getIdNotificacion() &&
            this.notificacion == other.isNotificacion() &&
            this.sms == other.isSms();
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
        _hashCode += (isEmail() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getIdNotificacion();
        _hashCode += (isNotificacion() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isSms() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EnvioWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "EnvioWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dni");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "dni"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idNotificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "idNotificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "notificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sms");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "sms"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

}
