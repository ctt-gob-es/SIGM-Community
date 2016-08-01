/**
 * RespuestaAnuncio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package BDNS.respuestaAnuncio;

public class RespuestaAnuncio  implements java.io.Serializable {
    private java.lang.String idPeticion;

    private java.lang.String timestamp;

    private java.lang.String codigoEstado;

    private java.lang.String literalError;

    private BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncio[] anuncios;

    public RespuestaAnuncio() {
    }

    public RespuestaAnuncio(
           java.lang.String idPeticion,
           java.lang.String timestamp,
           java.lang.String codigoEstado,
           java.lang.String literalError,
           BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncio[] anuncios) {
           this.idPeticion = idPeticion;
           this.timestamp = timestamp;
           this.codigoEstado = codigoEstado;
           this.literalError = literalError;
           this.anuncios = anuncios;
    }


    /**
     * Gets the idPeticion value for this RespuestaAnuncio.
     * 
     * @return idPeticion
     */
    public java.lang.String getIdPeticion() {
        return idPeticion;
    }


    /**
     * Sets the idPeticion value for this RespuestaAnuncio.
     * 
     * @param idPeticion
     */
    public void setIdPeticion(java.lang.String idPeticion) {
        this.idPeticion = idPeticion;
    }


    /**
     * Gets the timestamp value for this RespuestaAnuncio.
     * 
     * @return timestamp
     */
    public java.lang.String getTimestamp() {
        return timestamp;
    }


    /**
     * Sets the timestamp value for this RespuestaAnuncio.
     * 
     * @param timestamp
     */
    public void setTimestamp(java.lang.String timestamp) {
        this.timestamp = timestamp;
    }


    /**
     * Gets the codigoEstado value for this RespuestaAnuncio.
     * 
     * @return codigoEstado
     */
    public java.lang.String getCodigoEstado() {
        return codigoEstado;
    }


    /**
     * Sets the codigoEstado value for this RespuestaAnuncio.
     * 
     * @param codigoEstado
     */
    public void setCodigoEstado(java.lang.String codigoEstado) {
        this.codigoEstado = codigoEstado;
    }


    /**
     * Gets the literalError value for this RespuestaAnuncio.
     * 
     * @return literalError
     */
    public java.lang.String getLiteralError() {
        return literalError;
    }


    /**
     * Sets the literalError value for this RespuestaAnuncio.
     * 
     * @param literalError
     */
    public void setLiteralError(java.lang.String literalError) {
        this.literalError = literalError;
    }


    /**
     * Gets the anuncios value for this RespuestaAnuncio.
     * 
     * @return anuncios
     */
    public BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncio[] getAnuncios() {
        return anuncios;
    }


    /**
     * Sets the anuncios value for this RespuestaAnuncio.
     * 
     * @param anuncios
     */
    public void setAnuncios(BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncio[] anuncios) {
        this.anuncios = anuncios;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespuestaAnuncio)) return false;
        RespuestaAnuncio other = (RespuestaAnuncio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idPeticion==null && other.getIdPeticion()==null) || 
             (this.idPeticion!=null &&
              this.idPeticion.equals(other.getIdPeticion()))) &&
            ((this.timestamp==null && other.getTimestamp()==null) || 
             (this.timestamp!=null &&
              this.timestamp.equals(other.getTimestamp()))) &&
            ((this.codigoEstado==null && other.getCodigoEstado()==null) || 
             (this.codigoEstado!=null &&
              this.codigoEstado.equals(other.getCodigoEstado()))) &&
            ((this.literalError==null && other.getLiteralError()==null) || 
             (this.literalError!=null &&
              this.literalError.equals(other.getLiteralError()))) &&
            ((this.anuncios==null && other.getAnuncios()==null) || 
             (this.anuncios!=null &&
              java.util.Arrays.equals(this.anuncios, other.getAnuncios())));
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
        if (getIdPeticion() != null) {
            _hashCode += getIdPeticion().hashCode();
        }
        if (getTimestamp() != null) {
            _hashCode += getTimestamp().hashCode();
        }
        if (getCodigoEstado() != null) {
            _hashCode += getCodigoEstado().hashCode();
        }
        if (getLiteralError() != null) {
            _hashCode += getLiteralError().hashCode();
        }
        if (getAnuncios() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAnuncios());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAnuncios(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RespuestaAnuncio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">RespuestaAnuncio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPeticion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "IdPeticion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timestamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "Timestamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoEstado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "CodigoEstado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">CodigoEstado"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("literalError");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "LiteralError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">LiteralError"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anuncios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "Anuncios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>RespuestaAnuncio>Anuncios>Anuncio"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "Anuncio"));
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
