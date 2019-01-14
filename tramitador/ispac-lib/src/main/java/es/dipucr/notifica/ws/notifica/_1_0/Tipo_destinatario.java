/**
 * Tipo_destinatario.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

import ieci.tdw.ispac.api.errors.ISPACException;

public class Tipo_destinatario  implements java.io.Serializable {
    private java.lang.String referencia_emisor;

    private es.dipucr.notifica.ws.notifica._1_0.Tipo_persona_destinatario titular;

    private es.dipucr.notifica.ws.notifica._1_0.Tipo_persona_destinatario destinatario;

    private java.lang.String tipo_domicilio;

    private es.dipucr.notifica.ws.notifica._1_0.Tipo_domicilio domicilio;

    private java.lang.String servicio;

    private es.dipucr.notifica.ws.notifica._1_0.Direccion_electronica_habilitada direccion_electronica;

    private es.dipucr.notifica.ws.notifica._1_0.Opciones_emision opciones_emision;

    public Tipo_destinatario() {
    }

    public Tipo_destinatario(
           java.lang.String referencia_emisor,
           es.dipucr.notifica.ws.notifica._1_0.Tipo_persona_destinatario titular,
           es.dipucr.notifica.ws.notifica._1_0.Tipo_persona_destinatario destinatario,
           java.lang.String tipo_domicilio,
           es.dipucr.notifica.ws.notifica._1_0.Tipo_domicilio domicilio,
           java.lang.String servicio,
           es.dipucr.notifica.ws.notifica._1_0.Direccion_electronica_habilitada direccion_electronica,
           es.dipucr.notifica.ws.notifica._1_0.Opciones_emision opciones_emision) {
           this.referencia_emisor = referencia_emisor;
           this.titular = titular;
           this.destinatario = destinatario;
           this.tipo_domicilio = tipo_domicilio;
           this.domicilio = domicilio;
           this.servicio = servicio;
           this.direccion_electronica = direccion_electronica;
           this.opciones_emision = opciones_emision;
    }


    /**
     * Gets the referencia_emisor value for this Tipo_destinatario.
     * 
     * @return referencia_emisor
     */
    public java.lang.String getReferencia_emisor() {
        return referencia_emisor;
    }


    /**
     * Sets the referencia_emisor value for this Tipo_destinatario.
     * 
     * @param referencia_emisor
     */
    public void setReferencia_emisor(java.lang.String referencia_emisor) {
        this.referencia_emisor = referencia_emisor;
    }


    /**
     * Gets the titular value for this Tipo_destinatario.
     * 
     * @return titular
     */
    public es.dipucr.notifica.ws.notifica._1_0.Tipo_persona_destinatario getTitular() {
        return titular;
    }


    /**
     * Sets the titular value for this Tipo_destinatario.
     * 
     * @param titular
     */
    public void setTitular(es.dipucr.notifica.ws.notifica._1_0.Tipo_persona_destinatario titular) {
        this.titular = titular;
    }


    /**
     * Gets the destinatario value for this Tipo_destinatario.
     * 
     * @return destinatario
     */
    public es.dipucr.notifica.ws.notifica._1_0.Tipo_persona_destinatario getDestinatario() {
        return destinatario;
    }


    /**
     * Sets the destinatario value for this Tipo_destinatario.
     * 
     * @param destinatario
     */
    public void setDestinatario(es.dipucr.notifica.ws.notifica._1_0.Tipo_persona_destinatario destinatario) {
        this.destinatario = destinatario;
    }


    /**
     * Gets the tipo_domicilio value for this Tipo_destinatario.
     * 
     * @return tipo_domicilio
     */
    public java.lang.String getTipo_domicilio() {
        return tipo_domicilio;
    }


    /**
     * Sets the tipo_domicilio value for this Tipo_destinatario.
     * 
     * @param tipo_domicilio
     */
    public void setTipo_domicilio(java.lang.String tipo_domicilio) {
        this.tipo_domicilio = tipo_domicilio;
    }


    /**
     * Gets the domicilio value for this Tipo_destinatario.
     * 
     * @return domicilio
     */
    public es.dipucr.notifica.ws.notifica._1_0.Tipo_domicilio getDomicilio() {
        return domicilio;
    }


    /**
     * Sets the domicilio value for this Tipo_destinatario.
     * 
     * @param domicilio
     */
    public void setDomicilio(es.dipucr.notifica.ws.notifica._1_0.Tipo_domicilio domicilio) {
        this.domicilio = domicilio;
    }


    /**
     * Gets the servicio value for this Tipo_destinatario.
     * 
     * @return servicio
     */
    public java.lang.String getServicio() {
        return servicio;
    }


    /**
     * Sets the servicio value for this Tipo_destinatario.
     * 
     * @param servicio
     */
    public void setServicio(java.lang.String servicio) {
        this.servicio = servicio;
    }


    /**
     * Gets the direccion_electronica value for this Tipo_destinatario.
     * 
     * @return direccion_electronica
     */
    public es.dipucr.notifica.ws.notifica._1_0.Direccion_electronica_habilitada getDireccion_electronica() {
        return direccion_electronica;
    }


    /**
     * Sets the direccion_electronica value for this Tipo_destinatario.
     * 
     * @param direccion_electronica
     */
    public void setDireccion_electronica(es.dipucr.notifica.ws.notifica._1_0.Direccion_electronica_habilitada direccion_electronica) {
        this.direccion_electronica = direccion_electronica;
    }


    /**
     * Gets the opciones_emision value for this Tipo_destinatario.
     * 
     * @return opciones_emision
     */
    public es.dipucr.notifica.ws.notifica._1_0.Opciones_emision getOpciones_emision() {
        return opciones_emision;
    }


    /**
     * Sets the opciones_emision value for this Tipo_destinatario.
     * 
     * @param opciones_emision
     */
    public void setOpciones_emision(es.dipucr.notifica.ws.notifica._1_0.Opciones_emision opciones_emision) {
        this.opciones_emision = opciones_emision;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Tipo_destinatario)) return false;
        Tipo_destinatario other = (Tipo_destinatario) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.referencia_emisor==null && other.getReferencia_emisor()==null) || 
             (this.referencia_emisor!=null &&
              this.referencia_emisor.equals(other.getReferencia_emisor()))) &&
            ((this.titular==null && other.getTitular()==null) || 
             (this.titular!=null &&
              this.titular.equals(other.getTitular()))) &&
            ((this.destinatario==null && other.getDestinatario()==null) || 
             (this.destinatario!=null &&
              this.destinatario.equals(other.getDestinatario()))) &&
            ((this.tipo_domicilio==null && other.getTipo_domicilio()==null) || 
             (this.tipo_domicilio!=null &&
              this.tipo_domicilio.equals(other.getTipo_domicilio()))) &&
            ((this.domicilio==null && other.getDomicilio()==null) || 
             (this.domicilio!=null &&
              this.domicilio.equals(other.getDomicilio()))) &&
            ((this.servicio==null && other.getServicio()==null) || 
             (this.servicio!=null &&
              this.servicio.equals(other.getServicio()))) &&
            ((this.direccion_electronica==null && other.getDireccion_electronica()==null) || 
             (this.direccion_electronica!=null &&
              this.direccion_electronica.equals(other.getDireccion_electronica()))) &&
            ((this.opciones_emision==null && other.getOpciones_emision()==null) || 
             (this.opciones_emision!=null &&
              this.opciones_emision.equals(other.getOpciones_emision())));
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
        if (getReferencia_emisor() != null) {
            _hashCode += getReferencia_emisor().hashCode();
        }
        if (getTitular() != null) {
            _hashCode += getTitular().hashCode();
        }
        if (getDestinatario() != null) {
            _hashCode += getDestinatario().hashCode();
        }
        if (getTipo_domicilio() != null) {
            _hashCode += getTipo_domicilio().hashCode();
        }
        if (getDomicilio() != null) {
            _hashCode += getDomicilio().hashCode();
        }
        if (getServicio() != null) {
            _hashCode += getServicio().hashCode();
        }
        if (getDireccion_electronica() != null) {
            _hashCode += getDireccion_electronica().hashCode();
        }
        if (getOpciones_emision() != null) {
            _hashCode += getOpciones_emision().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Tipo_destinatario.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_destinatario"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referencia_emisor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "referencia_emisor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("titular");
        elemField.setXmlName(new javax.xml.namespace.QName("", "titular"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_persona_destinatario"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinatario"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_persona_destinatario"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo_domicilio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo_domicilio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domicilio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "domicilio"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipo_domicilio"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("servicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "servicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("direccion_electronica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "direccion_electronica"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "direccion_electronica_habilitada"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("opciones_emision");
        elemField.setXmlName(new javax.xml.namespace.QName("", "opciones_emision"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "opciones_emision"));
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
        
        result = result.concat("id notificacion referencia de sigem: ");
        result = result.concat(this.referencia_emisor);
        result = result.concat("\n");
        result = result.concat("---titular--");
        result = result.concat(this.titular.toString());
        result = result.concat("\n");
        result = result.concat("--destinatario--");
        result = result.concat(this.destinatario.toString());
        result = result.concat("\n");
        result = result.concat("tipo_domicilio (fiscal/concreto): ");
        result = result.concat(this.tipo_domicilio);
        result = result.concat("\n");
        result = result.concat("servicio (normal/urgente): ");
        result = result.concat(this.servicio);
        result = result.concat("\n");
        result = result.concat("--direccion_electronica habilitada (DEH)--");
        if(null!=this.direccion_electronica)
        	result = result.concat(this.direccion_electronica.toString());
        result = result.concat("\n");
        result = result.concat("--opciones_emision--");
        result = result.concat(this.opciones_emision.toString());
        result = result.concat("\n");
        
        return result;
    	
    }
    
    public boolean validarDestinatario() throws ISPACException{
    	
    	if(null == this.getTitular().getNif())
    		throw new ISPACException("Error en la validacion del participante para el envio a Notifica, revisa el participante del expediente con el nif nulo: "+ this.getTitular().getNombre());
    	if(null == this.getTitular().getNombre())
    		throw new ISPACException("Error en la validacion del participante para el envio a Notifica, revisa participante del expediente con el nombre nulo: "+ this.getTitular().getNombre());
    	//Se permite enviar la notificacion con el campo nulo
    	//if(null == this.getTitular().getEmail())
    	//	throw new ISPACException("Error en la validacion del participante para el envio a Notifica, revisa participante del expediente con el email nulo: "+ this.getTitular().getNombre());
    	
    	return true;
    	
    }

}
