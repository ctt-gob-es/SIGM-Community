/**
 * AnuncioBOP.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.sede.services.bop.enviarBOPSedeWS;

public class AnuncioBOP  implements java.io.Serializable {
    private int idDocumento;

    private int idBop;

    private java.lang.String sumario;

    private int num_anuncio;

    private int num_pagina;

    private java.lang.String clasificacion;

    private java.lang.String entidad;

    private java.lang.String categoria;

    public AnuncioBOP() {
    }

    public AnuncioBOP(
           int idDocumento,
           int idBop,
           java.lang.String sumario,
           int num_anuncio,
           int num_pagina,
           java.lang.String clasificacion,
           java.lang.String entidad,
           java.lang.String categoria) {
           this.idDocumento = idDocumento;
           this.idBop = idBop;
           this.sumario = sumario;
           this.num_anuncio = num_anuncio;
           this.num_pagina = num_pagina;
           this.clasificacion = clasificacion;
           this.entidad = entidad;
           this.categoria = categoria;
    }


    /**
     * Gets the idDocumento value for this AnuncioBOP.
     * 
     * @return idDocumento
     */
    public int getIdDocumento() {
        return idDocumento;
    }


    /**
     * Sets the idDocumento value for this AnuncioBOP.
     * 
     * @param idDocumento
     */
    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }


    /**
     * Gets the idBop value for this AnuncioBOP.
     * 
     * @return idBop
     */
    public int getIdBop() {
        return idBop;
    }


    /**
     * Sets the idBop value for this AnuncioBOP.
     * 
     * @param idBop
     */
    public void setIdBop(int idBop) {
        this.idBop = idBop;
    }


    /**
     * Gets the sumario value for this AnuncioBOP.
     * 
     * @return sumario
     */
    public java.lang.String getSumario() {
        return sumario;
    }


    /**
     * Sets the sumario value for this AnuncioBOP.
     * 
     * @param sumario
     */
    public void setSumario(java.lang.String sumario) {
        this.sumario = sumario;
    }


    /**
     * Gets the num_anuncio value for this AnuncioBOP.
     * 
     * @return num_anuncio
     */
    public int getNum_anuncio() {
        return num_anuncio;
    }


    /**
     * Sets the num_anuncio value for this AnuncioBOP.
     * 
     * @param num_anuncio
     */
    public void setNum_anuncio(int num_anuncio) {
        this.num_anuncio = num_anuncio;
    }


    /**
     * Gets the num_pagina value for this AnuncioBOP.
     * 
     * @return num_pagina
     */
    public int getNum_pagina() {
        return num_pagina;
    }


    /**
     * Sets the num_pagina value for this AnuncioBOP.
     * 
     * @param num_pagina
     */
    public void setNum_pagina(int num_pagina) {
        this.num_pagina = num_pagina;
    }


    /**
     * Gets the clasificacion value for this AnuncioBOP.
     * 
     * @return clasificacion
     */
    public java.lang.String getClasificacion() {
        return clasificacion;
    }


    /**
     * Sets the clasificacion value for this AnuncioBOP.
     * 
     * @param clasificacion
     */
    public void setClasificacion(java.lang.String clasificacion) {
        this.clasificacion = clasificacion;
    }


    /**
     * Gets the entidad value for this AnuncioBOP.
     * 
     * @return entidad
     */
    public java.lang.String getEntidad() {
        return entidad;
    }


    /**
     * Sets the entidad value for this AnuncioBOP.
     * 
     * @param entidad
     */
    public void setEntidad(java.lang.String entidad) {
        this.entidad = entidad;
    }


    /**
     * Gets the categoria value for this AnuncioBOP.
     * 
     * @return categoria
     */
    public java.lang.String getCategoria() {
        return categoria;
    }


    /**
     * Sets the categoria value for this AnuncioBOP.
     * 
     * @param categoria
     */
    public void setCategoria(java.lang.String categoria) {
        this.categoria = categoria;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AnuncioBOP)) return false;
        AnuncioBOP other = (AnuncioBOP) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.idDocumento == other.getIdDocumento() &&
            this.idBop == other.getIdBop() &&
            ((this.sumario==null && other.getSumario()==null) || 
             (this.sumario!=null &&
              this.sumario.equals(other.getSumario()))) &&
            this.num_anuncio == other.getNum_anuncio() &&
            this.num_pagina == other.getNum_pagina() &&
            ((this.clasificacion==null && other.getClasificacion()==null) || 
             (this.clasificacion!=null &&
              this.clasificacion.equals(other.getClasificacion()))) &&
            ((this.entidad==null && other.getEntidad()==null) || 
             (this.entidad!=null &&
              this.entidad.equals(other.getEntidad()))) &&
            ((this.categoria==null && other.getCategoria()==null) || 
             (this.categoria!=null &&
              this.categoria.equals(other.getCategoria())));
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
        _hashCode += getIdDocumento();
        _hashCode += getIdBop();
        if (getSumario() != null) {
            _hashCode += getSumario().hashCode();
        }
        _hashCode += getNum_anuncio();
        _hashCode += getNum_pagina();
        if (getClasificacion() != null) {
            _hashCode += getClasificacion().hashCode();
        }
        if (getEntidad() != null) {
            _hashCode += getEntidad().hashCode();
        }
        if (getCategoria() != null) {
            _hashCode += getCategoria().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AnuncioBOP.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://enviarBOPSedeWS.bop.services.sede.dipucr.es", "anuncioBOP"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idBop");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idBop"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sumario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sumario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_anuncio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "num_anuncio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_pagina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "num_pagina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clasificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "clasificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoria");
        elemField.setXmlName(new javax.xml.namespace.QName("", "categoria"));
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
