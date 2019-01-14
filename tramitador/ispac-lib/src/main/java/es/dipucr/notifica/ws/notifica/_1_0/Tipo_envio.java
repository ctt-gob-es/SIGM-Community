/**
 * Tipo_envio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class Tipo_envio  implements java.io.Serializable {
    private es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoEmisor organismo_emisor;

    private es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCorreos organismo_pagador_correos;

    private es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCIE organismo_pagador_cie;

    private es.dipucr.notifica.ws.notifica._1_0.Documento documento;

    private java.lang.String tipo_envio;

    private java.util.Calendar fecha_envio_programado;

    private java.lang.String concepto;

    private es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipo_destinatario destinatarios;

    private es.dipucr.notifica.ws.notifica._1_0.Tipo_procedimiento procedimiento;

    public Tipo_envio() {
    }

    public Tipo_envio(
           es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoEmisor organismo_emisor,
           es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCorreos organismo_pagador_correos,
           es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCIE organismo_pagador_cie,
           es.dipucr.notifica.ws.notifica._1_0.Documento documento,
           java.lang.String tipo_envio,
           java.util.Calendar fecha_envio_programado,
           java.lang.String concepto,
           es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipo_destinatario destinatarios,
           es.dipucr.notifica.ws.notifica._1_0.Tipo_procedimiento procedimiento) {
           this.organismo_emisor = organismo_emisor;
           this.organismo_pagador_correos = organismo_pagador_correos;
           this.organismo_pagador_cie = organismo_pagador_cie;
           this.documento = documento;
           this.tipo_envio = tipo_envio;
           this.fecha_envio_programado = fecha_envio_programado;
           this.concepto = concepto;
           this.destinatarios = destinatarios;
           this.procedimiento = procedimiento;
    }


    /**
     * Gets the organismo_emisor value for this Tipo_envio.
     * 
     * @return organismo_emisor
     */
    public es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoEmisor getOrganismo_emisor() {
        return organismo_emisor;
    }


    /**
     * Sets the organismo_emisor value for this Tipo_envio.
     * 
     * @param organismo_emisor
     */
    public void setOrganismo_emisor(es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoEmisor organismo_emisor) {
        this.organismo_emisor = organismo_emisor;
    }


    /**
     * Gets the organismo_pagador_correos value for this Tipo_envio.
     * 
     * @return organismo_pagador_correos
     */
    public es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCorreos getOrganismo_pagador_correos() {
        return organismo_pagador_correos;
    }


    /**
     * Sets the organismo_pagador_correos value for this Tipo_envio.
     * 
     * @param organismo_pagador_correos
     */
    public void setOrganismo_pagador_correos(es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCorreos organismo_pagador_correos) {
        this.organismo_pagador_correos = organismo_pagador_correos;
    }


    /**
     * Gets the organismo_pagador_cie value for this Tipo_envio.
     * 
     * @return organismo_pagador_cie
     */
    public es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCIE getOrganismo_pagador_cie() {
        return organismo_pagador_cie;
    }


    /**
     * Sets the organismo_pagador_cie value for this Tipo_envio.
     * 
     * @param organismo_pagador_cie
     */
    public void setOrganismo_pagador_cie(es.dipucr.notifica.ws.notifica._1_0.TipoOrganismoPagadorCIE organismo_pagador_cie) {
        this.organismo_pagador_cie = organismo_pagador_cie;
    }


    /**
     * Gets the documento value for this Tipo_envio.
     * 
     * @return documento
     */
    public es.dipucr.notifica.ws.notifica._1_0.Documento getDocumento() {
        return documento;
    }


    /**
     * Sets the documento value for this Tipo_envio.
     * 
     * @param documento
     */
    public void setDocumento(es.dipucr.notifica.ws.notifica._1_0.Documento documento) {
        this.documento = documento;
    }


    /**
     * Gets the tipo_envio value for this Tipo_envio.
     * 
     * @return tipo_envio
     */
    public java.lang.String getTipo_envio() {
        return tipo_envio;
    }


    /**
     * Sets the tipo_envio value for this Tipo_envio.
     * 
     * @param tipo_envio
     */
    public void setTipo_envio(java.lang.String tipo_envio) {
        this.tipo_envio = tipo_envio;
    }


    /**
     * Gets the fecha_envio_programado value for this Tipo_envio.
     * 
     * @return fecha_envio_programado
     */
    public java.util.Calendar getFecha_envio_programado() {
        return fecha_envio_programado;
    }


    /**
     * Sets the fecha_envio_programado value for this Tipo_envio.
     * 
     * @param fecha_envio_programado
     */
    public void setFecha_envio_programado(java.util.Calendar fecha_envio_programado) {
        this.fecha_envio_programado = fecha_envio_programado;
    }


    /**
     * Gets the concepto value for this Tipo_envio.
     * 
     * @return concepto
     */
    public java.lang.String getConcepto() {
        return concepto;
    }


    /**
     * Sets the concepto value for this Tipo_envio.
     * 
     * @param concepto
     */
    public void setConcepto(java.lang.String concepto) {
        this.concepto = concepto;
    }


    /**
     * Gets the destinatarios value for this Tipo_envio.
     * 
     * @return destinatarios
     */
    public es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipo_destinatario getDestinatarios() {
        return destinatarios;
    }


    /**
     * Sets the destinatarios value for this Tipo_envio.
     * 
     * @param destinatarios
     */
    public void setDestinatarios(es.dipucr.notifica.ws.notifica._1_0.ArrayOfTipo_destinatario destinatarios) {
        this.destinatarios = destinatarios;
    }


    /**
     * Gets the procedimiento value for this Tipo_envio.
     * 
     * @return procedimiento
     */
    public es.dipucr.notifica.ws.notifica._1_0.Tipo_procedimiento getProcedimiento() {
        return procedimiento;
    }


    /**
     * Sets the procedimiento value for this Tipo_envio.
     * 
     * @param procedimiento
     */
    public void setProcedimiento(es.dipucr.notifica.ws.notifica._1_0.Tipo_procedimiento procedimiento) {
        this.procedimiento = procedimiento;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Tipo_envio)) return false;
        Tipo_envio other = (Tipo_envio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.organismo_emisor==null && other.getOrganismo_emisor()==null) || 
             (this.organismo_emisor!=null &&
              this.organismo_emisor.equals(other.getOrganismo_emisor()))) &&
            ((this.organismo_pagador_correos==null && other.getOrganismo_pagador_correos()==null) || 
             (this.organismo_pagador_correos!=null &&
              this.organismo_pagador_correos.equals(other.getOrganismo_pagador_correos()))) &&
            ((this.organismo_pagador_cie==null && other.getOrganismo_pagador_cie()==null) || 
             (this.organismo_pagador_cie!=null &&
              this.organismo_pagador_cie.equals(other.getOrganismo_pagador_cie()))) &&
            ((this.documento==null && other.getDocumento()==null) || 
             (this.documento!=null &&
              this.documento.equals(other.getDocumento()))) &&
            ((this.tipo_envio==null && other.getTipo_envio()==null) || 
             (this.tipo_envio!=null &&
              this.tipo_envio.equals(other.getTipo_envio()))) &&
            ((this.fecha_envio_programado==null && other.getFecha_envio_programado()==null) || 
             (this.fecha_envio_programado!=null &&
              this.fecha_envio_programado.equals(other.getFecha_envio_programado()))) &&
            ((this.concepto==null && other.getConcepto()==null) || 
             (this.concepto!=null &&
              this.concepto.equals(other.getConcepto()))) &&
            ((this.destinatarios==null && other.getDestinatarios()==null) || 
             (this.destinatarios!=null &&
              this.destinatarios.equals(other.getDestinatarios()))) &&
            ((this.procedimiento==null && other.getProcedimiento()==null) || 
             (this.procedimiento!=null &&
              this.procedimiento.equals(other.getProcedimiento())));
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
        if (getOrganismo_emisor() != null) {
            _hashCode += getOrganismo_emisor().hashCode();
        }
        if (getOrganismo_pagador_correos() != null) {
            _hashCode += getOrganismo_pagador_correos().hashCode();
        }
        if (getOrganismo_pagador_cie() != null) {
            _hashCode += getOrganismo_pagador_cie().hashCode();
        }
        if (getDocumento() != null) {
            _hashCode += getDocumento().hashCode();
        }
        if (getTipo_envio() != null) {
            _hashCode += getTipo_envio().hashCode();
        }
        if (getFecha_envio_programado() != null) {
            _hashCode += getFecha_envio_programado().hashCode();
        }
        if (getConcepto() != null) {
            _hashCode += getConcepto().hashCode();
        }
        if (getDestinatarios() != null) {
            _hashCode += getDestinatarios().hashCode();
        }
        if (getProcedimiento() != null) {
            _hashCode += getProcedimiento().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Tipo_envio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_envio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organismo_emisor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "organismo_emisor"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipoOrganismoEmisor"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organismo_pagador_correos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "organismo_pagador_correos"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipoOrganismoPagadorCorreos"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organismo_pagador_cie");
        elemField.setXmlName(new javax.xml.namespace.QName("", "organismo_pagador_cie"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipoOrganismoPagadorCIE"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "documento"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "documento"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo_envio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo_envio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_envio_programado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_envio_programado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("concepto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "concepto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatarios");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinatarios"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "ArrayOfTipo_destinatario"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procedimiento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "procedimiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_procedimiento"));
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
    	
    	result=result.concat("\n");
    	result=result.concat("\n");
    	result=result.concat("************************************************************************************************************");
    	result=result.concat("\n");
    	result=result.concat("*************************************** ENVÍO A NOTIFIC@ ***************************************************");
    	result=result.concat("\n");
    	result=result.concat("************************************************************************************************************");
    	result=result.concat("\n");
    	result=result.concat("########### CONCEPTO ########################");
    	result=result.concat("\n");
    	result=result.concat(this.concepto);
    	result=result.concat("\n");
    	result=result.concat("\n");
    	result=result.concat("########### ORGANISMO EMISOR ################");
    	result=result.concat("\n");
    	result=result.concat(this.organismo_emisor.toString());
    	result=result.concat("\n");
    	result=result.concat("########### DESTINATARIOS ###################");
    	result=result.concat("\n");
    	result=result.concat(this.destinatarios.toString());
    	result=result.concat("\n");
    	result=result.concat("########### PROCEDIMIENTO SIA ###############");
    	result=result.concat("\n");
    	result=result.concat(this.procedimiento.toString());
    	result=result.concat("\n");
    	result=result.concat("########### FECHA ENVIO PROGRAMADO ##########");
    	result=result.concat("\n");
    	if(null != this.fecha_envio_programado)
    		result=result.concat(this.fecha_envio_programado.getTime().toString());
    	result=result.concat("\n");
    	result=result.concat("\n");
    	result=result.concat("########### TIPO ENVIO ######################");
    	result=result.concat("\n");
    	result=result.concat(this.tipo_envio.toString());
    	result=result.concat("\n");
    	result=result.concat("\n");
    	result=result.concat("########### DOCUMENTO #######################");
    	result=result.concat("\n");
    	result=result.concat(this.documento.toString());
    	result=result.concat("\n");
    	result=result.concat("************************************************************************************************************");
    	result=result.concat("\n");
    	result=result.concat("************************************* FIN ENVÍO A NOTIFIC@ *************************************************");
    	result=result.concat("\n");
    	result=result.concat("************************************************************************************************************");
    	result=result.concat("\n");
    	result=result.concat("\n");

    	return result;
    	
    }

}
