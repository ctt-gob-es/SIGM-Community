/**
 * Anuncio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.boe.www.ServicioNotificaciones;


/**
 * Datos de un anuncio
 */
public class Anuncio  implements java.io.Serializable {
    private java.lang.String idBoe;

    private es.boe.www.ServicioNotificaciones.ListaAvisos[] avisos;

    private es.boe.www.ServicioNotificaciones.ListaErrores[] errores;

    private java.lang.String estadoBoe;

    private java.lang.String nbo;

    private java.lang.String cve;

    private org.apache.axis.types.URI url;

    private java.lang.String fechaPub;

    private java.lang.String id;  // attribute

    public Anuncio() {
    }

    public Anuncio(
           java.lang.String idBoe,
           es.boe.www.ServicioNotificaciones.ListaAvisos[] avisos,
           es.boe.www.ServicioNotificaciones.ListaErrores[] errores,
           java.lang.String estadoBoe,
           java.lang.String nbo,
           java.lang.String cve,
           org.apache.axis.types.URI url,
           java.lang.String fechaPub,
           java.lang.String id) {
           this.idBoe = idBoe;
           this.avisos = avisos;
           this.errores = errores;
           this.estadoBoe = estadoBoe;
           this.nbo = nbo;
           this.cve = cve;
           this.url = url;
           this.fechaPub = fechaPub;
           this.id = id;
    }


    /**
     * Gets the idBoe value for this Anuncio.
     * 
     * @return idBoe
     */
    public java.lang.String getIdBoe() {
        return idBoe;
    }


    /**
     * Sets the idBoe value for this Anuncio.
     * 
     * @param idBoe
     */
    public void setIdBoe(java.lang.String idBoe) {
        this.idBoe = idBoe;
    }


    /**
     * Gets the avisos value for this Anuncio.
     * 
     * @return avisos
     */
    public es.boe.www.ServicioNotificaciones.ListaAvisos[] getAvisos() {
        return avisos;
    }


    /**
     * Sets the avisos value for this Anuncio.
     * 
     * @param avisos
     */
    public void setAvisos(es.boe.www.ServicioNotificaciones.ListaAvisos[] avisos) {
        this.avisos = avisos;
    }

    public es.boe.www.ServicioNotificaciones.ListaAvisos getAvisos(int i) {
        return this.avisos[i];
    }

    public void setAvisos(int i, es.boe.www.ServicioNotificaciones.ListaAvisos _value) {
        this.avisos[i] = _value;
    }


    /**
     * Gets the errores value for this Anuncio.
     * 
     * @return errores
     */
    public es.boe.www.ServicioNotificaciones.ListaErrores[] getErrores() {
        return errores;
    }


    /**
     * Sets the errores value for this Anuncio.
     * 
     * @param errores
     */
    public void setErrores(es.boe.www.ServicioNotificaciones.ListaErrores[] errores) {
        this.errores = errores;
    }

    public es.boe.www.ServicioNotificaciones.ListaErrores getErrores(int i) {
        return this.errores[i];
    }

    public void setErrores(int i, es.boe.www.ServicioNotificaciones.ListaErrores _value) {
        this.errores[i] = _value;
    }


    /**
     * Gets the estadoBoe value for this Anuncio.
     * 
     * @return estadoBoe
     */
    public java.lang.String getEstadoBoe() {
        return estadoBoe;
    }


    /**
     * Sets the estadoBoe value for this Anuncio.
     * 
     * @param estadoBoe
     */
    public void setEstadoBoe(java.lang.String estadoBoe) {
        this.estadoBoe = estadoBoe;
    }


    /**
     * Gets the nbo value for this Anuncio.
     * 
     * @return nbo
     */
    public java.lang.String getNbo() {
        return nbo;
    }


    /**
     * Sets the nbo value for this Anuncio.
     * 
     * @param nbo
     */
    public void setNbo(java.lang.String nbo) {
        this.nbo = nbo;
    }


    /**
     * Gets the cve value for this Anuncio.
     * 
     * @return cve
     */
    public java.lang.String getCve() {
        return cve;
    }


    /**
     * Sets the cve value for this Anuncio.
     * 
     * @param cve
     */
    public void setCve(java.lang.String cve) {
        this.cve = cve;
    }


    /**
     * Gets the url value for this Anuncio.
     * 
     * @return url
     */
    public org.apache.axis.types.URI getUrl() {
        return url;
    }


    /**
     * Sets the url value for this Anuncio.
     * 
     * @param url
     */
    public void setUrl(org.apache.axis.types.URI url) {
        this.url = url;
    }


    /**
     * Gets the fechaPub value for this Anuncio.
     * 
     * @return fechaPub
     */
    public java.lang.String getFechaPub() {
        return fechaPub;
    }


    /**
     * Sets the fechaPub value for this Anuncio.
     * 
     * @param fechaPub
     */
    public void setFechaPub(java.lang.String fechaPub) {
        this.fechaPub = fechaPub;
    }


    /**
     * Gets the id value for this Anuncio.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Anuncio.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Anuncio)) return false;
        Anuncio other = (Anuncio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idBoe==null && other.getIdBoe()==null) || 
             (this.idBoe!=null &&
              this.idBoe.equals(other.getIdBoe()))) &&
            ((this.avisos==null && other.getAvisos()==null) || 
             (this.avisos!=null &&
              java.util.Arrays.equals(this.avisos, other.getAvisos()))) &&
            ((this.errores==null && other.getErrores()==null) || 
             (this.errores!=null &&
              java.util.Arrays.equals(this.errores, other.getErrores()))) &&
            ((this.estadoBoe==null && other.getEstadoBoe()==null) || 
             (this.estadoBoe!=null &&
              this.estadoBoe.equals(other.getEstadoBoe()))) &&
            ((this.nbo==null && other.getNbo()==null) || 
             (this.nbo!=null &&
              this.nbo.equals(other.getNbo()))) &&
            ((this.cve==null && other.getCve()==null) || 
             (this.cve!=null &&
              this.cve.equals(other.getCve()))) &&
            ((this.url==null && other.getUrl()==null) || 
             (this.url!=null &&
              this.url.equals(other.getUrl()))) &&
            ((this.fechaPub==null && other.getFechaPub()==null) || 
             (this.fechaPub!=null &&
              this.fechaPub.equals(other.getFechaPub()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId())));
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
        if (getIdBoe() != null) {
            _hashCode += getIdBoe().hashCode();
        }
        if (getAvisos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAvisos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAvisos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getErrores() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getErrores());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getErrores(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEstadoBoe() != null) {
            _hashCode += getEstadoBoe().hashCode();
        }
        if (getNbo() != null) {
            _hashCode += getNbo().hashCode();
        }
        if (getCve() != null) {
            _hashCode += getCve().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        if (getFechaPub() != null) {
            _hashCode += getFechaPub().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Anuncio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.boe.es/ServicioNotificaciones/", "Anuncio"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idBoe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idBoe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("avisos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "avisos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.boe.es/ServicioNotificaciones/", "ListaAvisos"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errores");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errores"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.boe.es/ServicioNotificaciones/", "ListaErrores"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estadoBoe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estadoBoe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nbo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nbo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cve");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cve"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("", "url"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaPub");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaPub"));
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
