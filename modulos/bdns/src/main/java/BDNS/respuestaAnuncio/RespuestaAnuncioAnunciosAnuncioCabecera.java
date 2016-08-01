/**
 * RespuestaAnuncioAnunciosAnuncioCabecera.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package BDNS.respuestaAnuncio;

public class RespuestaAnuncioAnunciosAnuncioCabecera  implements java.io.Serializable {
    private java.lang.String adminPublica;

    private java.lang.String codAdminPublica;

    private java.lang.String organo;

    private java.lang.String codOrgano;

    private int idAnuncio;

    private java.lang.String codigoConvocatoria;

    private java.lang.String refConvocatoria;

    private java.lang.String desConvocatoria;

    public RespuestaAnuncioAnunciosAnuncioCabecera() {
    }

    public RespuestaAnuncioAnunciosAnuncioCabecera(
           java.lang.String adminPublica,
           java.lang.String codAdminPublica,
           java.lang.String organo,
           java.lang.String codOrgano,
           int idAnuncio,
           java.lang.String codigoConvocatoria,
           java.lang.String refConvocatoria,
           java.lang.String desConvocatoria) {
           this.adminPublica = adminPublica;
           this.codAdminPublica = codAdminPublica;
           this.organo = organo;
           this.codOrgano = codOrgano;
           this.idAnuncio = idAnuncio;
           this.codigoConvocatoria = codigoConvocatoria;
           this.refConvocatoria = refConvocatoria;
           this.desConvocatoria = desConvocatoria;
    }


    /**
     * Gets the adminPublica value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @return adminPublica
     */
    public java.lang.String getAdminPublica() {
        return adminPublica;
    }


    /**
     * Sets the adminPublica value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @param adminPublica
     */
    public void setAdminPublica(java.lang.String adminPublica) {
        this.adminPublica = adminPublica;
    }


    /**
     * Gets the codAdminPublica value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @return codAdminPublica
     */
    public java.lang.String getCodAdminPublica() {
        return codAdminPublica;
    }


    /**
     * Sets the codAdminPublica value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @param codAdminPublica
     */
    public void setCodAdminPublica(java.lang.String codAdminPublica) {
        this.codAdminPublica = codAdminPublica;
    }


    /**
     * Gets the organo value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @return organo
     */
    public java.lang.String getOrgano() {
        return organo;
    }


    /**
     * Sets the organo value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @param organo
     */
    public void setOrgano(java.lang.String organo) {
        this.organo = organo;
    }


    /**
     * Gets the codOrgano value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @return codOrgano
     */
    public java.lang.String getCodOrgano() {
        return codOrgano;
    }


    /**
     * Sets the codOrgano value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @param codOrgano
     */
    public void setCodOrgano(java.lang.String codOrgano) {
        this.codOrgano = codOrgano;
    }


    /**
     * Gets the idAnuncio value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @return idAnuncio
     */
    public int getIdAnuncio() {
        return idAnuncio;
    }


    /**
     * Sets the idAnuncio value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @param idAnuncio
     */
    public void setIdAnuncio(int idAnuncio) {
        this.idAnuncio = idAnuncio;
    }


    /**
     * Gets the codigoConvocatoria value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @return codigoConvocatoria
     */
    public java.lang.String getCodigoConvocatoria() {
        return codigoConvocatoria;
    }


    /**
     * Sets the codigoConvocatoria value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @param codigoConvocatoria
     */
    public void setCodigoConvocatoria(java.lang.String codigoConvocatoria) {
        this.codigoConvocatoria = codigoConvocatoria;
    }


    /**
     * Gets the refConvocatoria value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @return refConvocatoria
     */
    public java.lang.String getRefConvocatoria() {
        return refConvocatoria;
    }


    /**
     * Sets the refConvocatoria value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @param refConvocatoria
     */
    public void setRefConvocatoria(java.lang.String refConvocatoria) {
        this.refConvocatoria = refConvocatoria;
    }


    /**
     * Gets the desConvocatoria value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @return desConvocatoria
     */
    public java.lang.String getDesConvocatoria() {
        return desConvocatoria;
    }


    /**
     * Sets the desConvocatoria value for this RespuestaAnuncioAnunciosAnuncioCabecera.
     * 
     * @param desConvocatoria
     */
    public void setDesConvocatoria(java.lang.String desConvocatoria) {
        this.desConvocatoria = desConvocatoria;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespuestaAnuncioAnunciosAnuncioCabecera)) return false;
        RespuestaAnuncioAnunciosAnuncioCabecera other = (RespuestaAnuncioAnunciosAnuncioCabecera) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.adminPublica==null && other.getAdminPublica()==null) || 
             (this.adminPublica!=null &&
              this.adminPublica.equals(other.getAdminPublica()))) &&
            ((this.codAdminPublica==null && other.getCodAdminPublica()==null) || 
             (this.codAdminPublica!=null &&
              this.codAdminPublica.equals(other.getCodAdminPublica()))) &&
            ((this.organo==null && other.getOrgano()==null) || 
             (this.organo!=null &&
              this.organo.equals(other.getOrgano()))) &&
            ((this.codOrgano==null && other.getCodOrgano()==null) || 
             (this.codOrgano!=null &&
              this.codOrgano.equals(other.getCodOrgano()))) &&
            this.idAnuncio == other.getIdAnuncio() &&
            ((this.codigoConvocatoria==null && other.getCodigoConvocatoria()==null) || 
             (this.codigoConvocatoria!=null &&
              this.codigoConvocatoria.equals(other.getCodigoConvocatoria()))) &&
            ((this.refConvocatoria==null && other.getRefConvocatoria()==null) || 
             (this.refConvocatoria!=null &&
              this.refConvocatoria.equals(other.getRefConvocatoria()))) &&
            ((this.desConvocatoria==null && other.getDesConvocatoria()==null) || 
             (this.desConvocatoria!=null &&
              this.desConvocatoria.equals(other.getDesConvocatoria())));
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
        if (getAdminPublica() != null) {
            _hashCode += getAdminPublica().hashCode();
        }
        if (getCodAdminPublica() != null) {
            _hashCode += getCodAdminPublica().hashCode();
        }
        if (getOrgano() != null) {
            _hashCode += getOrgano().hashCode();
        }
        if (getCodOrgano() != null) {
            _hashCode += getCodOrgano().hashCode();
        }
        _hashCode += getIdAnuncio();
        if (getCodigoConvocatoria() != null) {
            _hashCode += getCodigoConvocatoria().hashCode();
        }
        if (getRefConvocatoria() != null) {
            _hashCode += getRefConvocatoria().hashCode();
        }
        if (getDesConvocatoria() != null) {
            _hashCode += getDesConvocatoria().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RespuestaAnuncioAnunciosAnuncioCabecera.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>RespuestaAnuncio>Anuncios>Anuncio>Cabecera"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adminPublica");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "AdminPublica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codAdminPublica");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "CodAdminPublica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "Organo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codOrgano");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "CodOrgano"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAnuncio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "IdAnuncio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoConvocatoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "CodigoConvocatoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refConvocatoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "RefConvocatoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desConvocatoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "DesConvocatoria"));
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
