/**
 * SobreElectronico.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class SobreElectronico  implements java.io.Serializable {
    private es.dipucr.contratacion.client.beans.Campo codOferta;

    private java.lang.String[] descripcion;

    private es.dipucr.contratacion.client.beans.DatoDocumento[] doc;

    private es.dipucr.contratacion.client.beans.EventoAperturaBean eventoApertura;

    private java.lang.String[] idLote;

    private java.lang.String idSobre;

    public SobreElectronico() {
    }

    public SobreElectronico(
           es.dipucr.contratacion.client.beans.Campo codOferta,
           java.lang.String[] descripcion,
           es.dipucr.contratacion.client.beans.DatoDocumento[] doc,
           es.dipucr.contratacion.client.beans.EventoAperturaBean eventoApertura,
           java.lang.String[] idLote,
           java.lang.String idSobre) {
           this.codOferta = codOferta;
           this.descripcion = descripcion;
           this.doc = doc;
           this.eventoApertura = eventoApertura;
           this.idLote = idLote;
           this.idSobre = idSobre;
    }


    /**
     * Gets the codOferta value for this SobreElectronico.
     * 
     * @return codOferta
     */
    public es.dipucr.contratacion.client.beans.Campo getCodOferta() {
        return codOferta;
    }


    /**
     * Sets the codOferta value for this SobreElectronico.
     * 
     * @param codOferta
     */
    public void setCodOferta(es.dipucr.contratacion.client.beans.Campo codOferta) {
        this.codOferta = codOferta;
    }


    /**
     * Gets the descripcion value for this SobreElectronico.
     * 
     * @return descripcion
     */
    public java.lang.String[] getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this SobreElectronico.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String[] descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the doc value for this SobreElectronico.
     * 
     * @return doc
     */
    public es.dipucr.contratacion.client.beans.DatoDocumento[] getDoc() {
        return doc;
    }


    /**
     * Sets the doc value for this SobreElectronico.
     * 
     * @param doc
     */
    public void setDoc(es.dipucr.contratacion.client.beans.DatoDocumento[] doc) {
        this.doc = doc;
    }


    /**
     * Gets the eventoApertura value for this SobreElectronico.
     * 
     * @return eventoApertura
     */
    public es.dipucr.contratacion.client.beans.EventoAperturaBean getEventoApertura() {
        return eventoApertura;
    }


    /**
     * Sets the eventoApertura value for this SobreElectronico.
     * 
     * @param eventoApertura
     */
    public void setEventoApertura(es.dipucr.contratacion.client.beans.EventoAperturaBean eventoApertura) {
        this.eventoApertura = eventoApertura;
    }


    /**
     * Gets the idLote value for this SobreElectronico.
     * 
     * @return idLote
     */
    public java.lang.String[] getIdLote() {
        return idLote;
    }


    /**
     * Sets the idLote value for this SobreElectronico.
     * 
     * @param idLote
     */
    public void setIdLote(java.lang.String[] idLote) {
        this.idLote = idLote;
    }


    /**
     * Gets the idSobre value for this SobreElectronico.
     * 
     * @return idSobre
     */
    public java.lang.String getIdSobre() {
        return idSobre;
    }


    /**
     * Sets the idSobre value for this SobreElectronico.
     * 
     * @param idSobre
     */
    public void setIdSobre(java.lang.String idSobre) {
        this.idSobre = idSobre;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SobreElectronico)) return false;
        SobreElectronico other = (SobreElectronico) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codOferta==null && other.getCodOferta()==null) || 
             (this.codOferta!=null &&
              this.codOferta.equals(other.getCodOferta()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              java.util.Arrays.equals(this.descripcion, other.getDescripcion()))) &&
            ((this.doc==null && other.getDoc()==null) || 
             (this.doc!=null &&
              java.util.Arrays.equals(this.doc, other.getDoc()))) &&
            ((this.eventoApertura==null && other.getEventoApertura()==null) || 
             (this.eventoApertura!=null &&
              this.eventoApertura.equals(other.getEventoApertura()))) &&
            ((this.idLote==null && other.getIdLote()==null) || 
             (this.idLote!=null &&
              java.util.Arrays.equals(this.idLote, other.getIdLote()))) &&
            ((this.idSobre==null && other.getIdSobre()==null) || 
             (this.idSobre!=null &&
              this.idSobre.equals(other.getIdSobre())));
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
        if (getCodOferta() != null) {
            _hashCode += getCodOferta().hashCode();
        }
        if (getDescripcion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescripcion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescripcion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDoc() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDoc());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDoc(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEventoApertura() != null) {
            _hashCode += getEventoApertura().hashCode();
        }
        if (getIdLote() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdLote());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdLote(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdSobre() != null) {
            _hashCode += getIdSobre().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SobreElectronico.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "SobreElectronico"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codOferta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "codOferta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("doc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "doc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "DatoDocumento"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eventoApertura");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "eventoApertura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "EventoAperturaBean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idLote");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "idLote"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSobre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "idSobre"));
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
