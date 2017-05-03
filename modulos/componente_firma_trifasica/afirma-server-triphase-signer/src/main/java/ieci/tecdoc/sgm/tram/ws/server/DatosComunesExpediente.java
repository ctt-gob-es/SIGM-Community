/**
 * DatosComunesExpediente.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class DatosComunesExpediente  implements java.io.Serializable {
    private java.util.Calendar fechaRegistro;

    private java.lang.String idOrganismo;

    private ieci.tecdoc.sgm.tram.ws.server.InteresadoExpediente[] interesados;

    private java.lang.String numeroRegistro;

    private java.lang.String tipoAsunto;

    public DatosComunesExpediente() {
    }

    public DatosComunesExpediente(
           java.util.Calendar fechaRegistro,
           java.lang.String idOrganismo,
           ieci.tecdoc.sgm.tram.ws.server.InteresadoExpediente[] interesados,
           java.lang.String numeroRegistro,
           java.lang.String tipoAsunto) {
           this.fechaRegistro = fechaRegistro;
           this.idOrganismo = idOrganismo;
           this.interesados = interesados;
           this.numeroRegistro = numeroRegistro;
           this.tipoAsunto = tipoAsunto;
    }


    /**
     * Gets the fechaRegistro value for this DatosComunesExpediente.
     * 
     * @return fechaRegistro
     */
    public java.util.Calendar getFechaRegistro() {
        return fechaRegistro;
    }


    /**
     * Sets the fechaRegistro value for this DatosComunesExpediente.
     * 
     * @param fechaRegistro
     */
    public void setFechaRegistro(java.util.Calendar fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


    /**
     * Gets the idOrganismo value for this DatosComunesExpediente.
     * 
     * @return idOrganismo
     */
    public java.lang.String getIdOrganismo() {
        return idOrganismo;
    }


    /**
     * Sets the idOrganismo value for this DatosComunesExpediente.
     * 
     * @param idOrganismo
     */
    public void setIdOrganismo(java.lang.String idOrganismo) {
        this.idOrganismo = idOrganismo;
    }


    /**
     * Gets the interesados value for this DatosComunesExpediente.
     * 
     * @return interesados
     */
    public ieci.tecdoc.sgm.tram.ws.server.InteresadoExpediente[] getInteresados() {
        return interesados;
    }


    /**
     * Sets the interesados value for this DatosComunesExpediente.
     * 
     * @param interesados
     */
    public void setInteresados(ieci.tecdoc.sgm.tram.ws.server.InteresadoExpediente[] interesados) {
        this.interesados = interesados;
    }


    /**
     * Gets the numeroRegistro value for this DatosComunesExpediente.
     * 
     * @return numeroRegistro
     */
    public java.lang.String getNumeroRegistro() {
        return numeroRegistro;
    }


    /**
     * Sets the numeroRegistro value for this DatosComunesExpediente.
     * 
     * @param numeroRegistro
     */
    public void setNumeroRegistro(java.lang.String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }


    /**
     * Gets the tipoAsunto value for this DatosComunesExpediente.
     * 
     * @return tipoAsunto
     */
    public java.lang.String getTipoAsunto() {
        return tipoAsunto;
    }


    /**
     * Sets the tipoAsunto value for this DatosComunesExpediente.
     * 
     * @param tipoAsunto
     */
    public void setTipoAsunto(java.lang.String tipoAsunto) {
        this.tipoAsunto = tipoAsunto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosComunesExpediente)) return false;
        DatosComunesExpediente other = (DatosComunesExpediente) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fechaRegistro==null && other.getFechaRegistro()==null) || 
             (this.fechaRegistro!=null &&
              this.fechaRegistro.equals(other.getFechaRegistro()))) &&
            ((this.idOrganismo==null && other.getIdOrganismo()==null) || 
             (this.idOrganismo!=null &&
              this.idOrganismo.equals(other.getIdOrganismo()))) &&
            ((this.interesados==null && other.getInteresados()==null) || 
             (this.interesados!=null &&
              java.util.Arrays.equals(this.interesados, other.getInteresados()))) &&
            ((this.numeroRegistro==null && other.getNumeroRegistro()==null) || 
             (this.numeroRegistro!=null &&
              this.numeroRegistro.equals(other.getNumeroRegistro()))) &&
            ((this.tipoAsunto==null && other.getTipoAsunto()==null) || 
             (this.tipoAsunto!=null &&
              this.tipoAsunto.equals(other.getTipoAsunto())));
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
        if (getFechaRegistro() != null) {
            _hashCode += getFechaRegistro().hashCode();
        }
        if (getIdOrganismo() != null) {
            _hashCode += getIdOrganismo().hashCode();
        }
        if (getInteresados() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInteresados());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInteresados(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNumeroRegistro() != null) {
            _hashCode += getNumeroRegistro().hashCode();
        }
        if (getTipoAsunto() != null) {
            _hashCode += getTipoAsunto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosComunesExpediente.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DatosComunesExpediente"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "fechaRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idOrganismo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idOrganismo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interesados");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "interesados"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InteresadoExpediente"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "numeroRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoAsunto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "tipoAsunto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
