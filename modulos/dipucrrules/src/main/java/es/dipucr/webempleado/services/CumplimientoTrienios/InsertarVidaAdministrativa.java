/**
 * InsertarVidaAdministrativa.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.CumplimientoTrienios;

public class InsertarVidaAdministrativa  implements java.io.Serializable {
    private java.lang.String dni;

    private java.lang.String fecha_decreto;

    private java.lang.String num_trienio;

    private java.lang.String fecha_trienio;

    private java.lang.String no_expediente;

    private java.lang.String grupo;

    public InsertarVidaAdministrativa() {
    }

    public InsertarVidaAdministrativa(
           java.lang.String dni,
           java.lang.String fecha_decreto,
           java.lang.String num_trienio,
           java.lang.String fecha_trienio,
           java.lang.String no_expediente,
           java.lang.String grupo) {
           this.dni = dni;
           this.fecha_decreto = fecha_decreto;
           this.num_trienio = num_trienio;
           this.fecha_trienio = fecha_trienio;
           this.no_expediente = no_expediente;
           this.grupo = grupo;
    }


    /**
     * Gets the dni value for this InsertarVidaAdministrativa.
     * 
     * @return dni
     */
    public java.lang.String getDni() {
        return dni;
    }


    /**
     * Sets the dni value for this InsertarVidaAdministrativa.
     * 
     * @param dni
     */
    public void setDni(java.lang.String dni) {
        this.dni = dni;
    }


    /**
     * Gets the fecha_decreto value for this InsertarVidaAdministrativa.
     * 
     * @return fecha_decreto
     */
    public java.lang.String getFecha_decreto() {
        return fecha_decreto;
    }


    /**
     * Sets the fecha_decreto value for this InsertarVidaAdministrativa.
     * 
     * @param fecha_decreto
     */
    public void setFecha_decreto(java.lang.String fecha_decreto) {
        this.fecha_decreto = fecha_decreto;
    }


    /**
     * Gets the num_trienio value for this InsertarVidaAdministrativa.
     * 
     * @return num_trienio
     */
    public java.lang.String getNum_trienio() {
        return num_trienio;
    }


    /**
     * Sets the num_trienio value for this InsertarVidaAdministrativa.
     * 
     * @param num_trienio
     */
    public void setNum_trienio(java.lang.String num_trienio) {
        this.num_trienio = num_trienio;
    }


    /**
     * Gets the fecha_trienio value for this InsertarVidaAdministrativa.
     * 
     * @return fecha_trienio
     */
    public java.lang.String getFecha_trienio() {
        return fecha_trienio;
    }


    /**
     * Sets the fecha_trienio value for this InsertarVidaAdministrativa.
     * 
     * @param fecha_trienio
     */
    public void setFecha_trienio(java.lang.String fecha_trienio) {
        this.fecha_trienio = fecha_trienio;
    }


    /**
     * Gets the no_expediente value for this InsertarVidaAdministrativa.
     * 
     * @return no_expediente
     */
    public java.lang.String getNo_expediente() {
        return no_expediente;
    }


    /**
     * Sets the no_expediente value for this InsertarVidaAdministrativa.
     * 
     * @param no_expediente
     */
    public void setNo_expediente(java.lang.String no_expediente) {
        this.no_expediente = no_expediente;
    }


    /**
     * Gets the grupo value for this InsertarVidaAdministrativa.
     * 
     * @return grupo
     */
    public java.lang.String getGrupo() {
        return grupo;
    }


    /**
     * Sets the grupo value for this InsertarVidaAdministrativa.
     * 
     * @param grupo
     */
    public void setGrupo(java.lang.String grupo) {
        this.grupo = grupo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InsertarVidaAdministrativa)) return false;
        InsertarVidaAdministrativa other = (InsertarVidaAdministrativa) obj;
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
            ((this.fecha_decreto==null && other.getFecha_decreto()==null) || 
             (this.fecha_decreto!=null &&
              this.fecha_decreto.equals(other.getFecha_decreto()))) &&
            ((this.num_trienio==null && other.getNum_trienio()==null) || 
             (this.num_trienio!=null &&
              this.num_trienio.equals(other.getNum_trienio()))) &&
            ((this.fecha_trienio==null && other.getFecha_trienio()==null) || 
             (this.fecha_trienio!=null &&
              this.fecha_trienio.equals(other.getFecha_trienio()))) &&
            ((this.no_expediente==null && other.getNo_expediente()==null) || 
             (this.no_expediente!=null &&
              this.no_expediente.equals(other.getNo_expediente()))) &&
            ((this.grupo==null && other.getGrupo()==null) || 
             (this.grupo!=null &&
              this.grupo.equals(other.getGrupo())));
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
        if (getFecha_decreto() != null) {
            _hashCode += getFecha_decreto().hashCode();
        }
        if (getNum_trienio() != null) {
            _hashCode += getNum_trienio().hashCode();
        }
        if (getFecha_trienio() != null) {
            _hashCode += getFecha_trienio().hashCode();
        }
        if (getNo_expediente() != null) {
            _hashCode += getNo_expediente().hashCode();
        }
        if (getGrupo() != null) {
            _hashCode += getGrupo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InsertarVidaAdministrativa.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", ">insertarVidaAdministrativa"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dni");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", "dni"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_decreto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", "fecha_decreto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_trienio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", "num_trienio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_trienio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", "fecha_trienio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("no_expediente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", "no_expediente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("grupo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", "grupo"));
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

}
