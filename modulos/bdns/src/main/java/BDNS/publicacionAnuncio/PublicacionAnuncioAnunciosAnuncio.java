/**
 * PublicacionAnuncioAnunciosAnuncio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package BDNS.publicacionAnuncio;

public class PublicacionAnuncioAnunciosAnuncio  implements java.io.Serializable {
    private int idAnuncio;

    private java.lang.String idAnuncioDiarioOficial;

    private BDNS.publicacionAnuncio.PublicacionAnuncioAnunciosAnuncioEstadoPublicacion estadoPublicacion;

    private java.util.Date fechaPublicacion;

    private java.lang.String CVE;

    private java.lang.String URL;

    private java.lang.String observaciones;

    public PublicacionAnuncioAnunciosAnuncio() {
    }

    public PublicacionAnuncioAnunciosAnuncio(
           int idAnuncio,
           java.lang.String idAnuncioDiarioOficial,
           BDNS.publicacionAnuncio.PublicacionAnuncioAnunciosAnuncioEstadoPublicacion estadoPublicacion,
           java.util.Date fechaPublicacion,
           java.lang.String CVE,
           java.lang.String URL,
           java.lang.String observaciones) {
           this.idAnuncio = idAnuncio;
           this.idAnuncioDiarioOficial = idAnuncioDiarioOficial;
           this.estadoPublicacion = estadoPublicacion;
           this.fechaPublicacion = fechaPublicacion;
           this.CVE = CVE;
           this.URL = URL;
           this.observaciones = observaciones;
    }


    /**
     * Gets the idAnuncio value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @return idAnuncio
     */
    public int getIdAnuncio() {
        return idAnuncio;
    }


    /**
     * Sets the idAnuncio value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @param idAnuncio
     */
    public void setIdAnuncio(int idAnuncio) {
        this.idAnuncio = idAnuncio;
    }


    /**
     * Gets the idAnuncioDiarioOficial value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @return idAnuncioDiarioOficial
     */
    public java.lang.String getIdAnuncioDiarioOficial() {
        return idAnuncioDiarioOficial;
    }


    /**
     * Sets the idAnuncioDiarioOficial value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @param idAnuncioDiarioOficial
     */
    public void setIdAnuncioDiarioOficial(java.lang.String idAnuncioDiarioOficial) {
        this.idAnuncioDiarioOficial = idAnuncioDiarioOficial;
    }


    /**
     * Gets the estadoPublicacion value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @return estadoPublicacion
     */
    public BDNS.publicacionAnuncio.PublicacionAnuncioAnunciosAnuncioEstadoPublicacion getEstadoPublicacion() {
        return estadoPublicacion;
    }


    /**
     * Sets the estadoPublicacion value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @param estadoPublicacion
     */
    public void setEstadoPublicacion(BDNS.publicacionAnuncio.PublicacionAnuncioAnunciosAnuncioEstadoPublicacion estadoPublicacion) {
        this.estadoPublicacion = estadoPublicacion;
    }


    /**
     * Gets the fechaPublicacion value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @return fechaPublicacion
     */
    public java.util.Date getFechaPublicacion() {
        return fechaPublicacion;
    }


    /**
     * Sets the fechaPublicacion value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @param fechaPublicacion
     */
    public void setFechaPublicacion(java.util.Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }


    /**
     * Gets the CVE value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @return CVE
     */
    public java.lang.String getCVE() {
        return CVE;
    }


    /**
     * Sets the CVE value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @param CVE
     */
    public void setCVE(java.lang.String CVE) {
        this.CVE = CVE;
    }


    /**
     * Gets the URL value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @return URL
     */
    public java.lang.String getURL() {
        return URL;
    }


    /**
     * Sets the URL value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @param URL
     */
    public void setURL(java.lang.String URL) {
        this.URL = URL;
    }


    /**
     * Gets the observaciones value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @return observaciones
     */
    public java.lang.String getObservaciones() {
        return observaciones;
    }


    /**
     * Sets the observaciones value for this PublicacionAnuncioAnunciosAnuncio.
     * 
     * @param observaciones
     */
    public void setObservaciones(java.lang.String observaciones) {
        this.observaciones = observaciones;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PublicacionAnuncioAnunciosAnuncio)) return false;
        PublicacionAnuncioAnunciosAnuncio other = (PublicacionAnuncioAnunciosAnuncio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.idAnuncio == other.getIdAnuncio() &&
            ((this.idAnuncioDiarioOficial==null && other.getIdAnuncioDiarioOficial()==null) || 
             (this.idAnuncioDiarioOficial!=null &&
              this.idAnuncioDiarioOficial.equals(other.getIdAnuncioDiarioOficial()))) &&
            ((this.estadoPublicacion==null && other.getEstadoPublicacion()==null) || 
             (this.estadoPublicacion!=null &&
              this.estadoPublicacion.equals(other.getEstadoPublicacion()))) &&
            ((this.fechaPublicacion==null && other.getFechaPublicacion()==null) || 
             (this.fechaPublicacion!=null &&
              this.fechaPublicacion.equals(other.getFechaPublicacion()))) &&
            ((this.CVE==null && other.getCVE()==null) || 
             (this.CVE!=null &&
              this.CVE.equals(other.getCVE()))) &&
            ((this.URL==null && other.getURL()==null) || 
             (this.URL!=null &&
              this.URL.equals(other.getURL()))) &&
            ((this.observaciones==null && other.getObservaciones()==null) || 
             (this.observaciones!=null &&
              this.observaciones.equals(other.getObservaciones())));
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
        _hashCode += getIdAnuncio();
        if (getIdAnuncioDiarioOficial() != null) {
            _hashCode += getIdAnuncioDiarioOficial().hashCode();
        }
        if (getEstadoPublicacion() != null) {
            _hashCode += getEstadoPublicacion().hashCode();
        }
        if (getFechaPublicacion() != null) {
            _hashCode += getFechaPublicacion().hashCode();
        }
        if (getCVE() != null) {
            _hashCode += getCVE().hashCode();
        }
        if (getURL() != null) {
            _hashCode += getURL().hashCode();
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PublicacionAnuncioAnunciosAnuncio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", ">>>PublicacionAnuncio>Anuncios>Anuncio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAnuncio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", "IdAnuncio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAnuncioDiarioOficial");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", "IdAnuncioDiarioOficial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estadoPublicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", "EstadoPublicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", ">>>>PublicacionAnuncio>Anuncios>Anuncio>EstadoPublicacion"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaPublicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", "FechaPublicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CVE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", "CVE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("URL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", "URL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", "Observaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
