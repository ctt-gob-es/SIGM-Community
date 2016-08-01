/**
 * Documento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class Documento  implements java.io.Serializable {
    private java.lang.String buyerProfileId;

    private byte[] contenido;

    private java.lang.String descripcion;

    private java.lang.String expedientNumber;

    private java.util.Calendar fechaFirma;

    private java.lang.String idTypeDoc;

    private java.lang.String mimeCode;

    private java.lang.String nameDoc;

    private java.lang.String publicationId;

    private java.lang.String typeDoc;

    private java.lang.String urlDocument;

    public Documento() {
    }

    public Documento(
           java.lang.String buyerProfileId,
           byte[] contenido,
           java.lang.String descripcion,
           java.lang.String expedientNumber,
           java.util.Calendar fechaFirma,
           java.lang.String idTypeDoc,
           java.lang.String mimeCode,
           java.lang.String nameDoc,
           java.lang.String publicationId,
           java.lang.String typeDoc,
           java.lang.String urlDocument) {
           this.buyerProfileId = buyerProfileId;
           this.contenido = contenido;
           this.descripcion = descripcion;
           this.expedientNumber = expedientNumber;
           this.fechaFirma = fechaFirma;
           this.idTypeDoc = idTypeDoc;
           this.mimeCode = mimeCode;
           this.nameDoc = nameDoc;
           this.publicationId = publicationId;
           this.typeDoc = typeDoc;
           this.urlDocument = urlDocument;
    }


    /**
     * Gets the buyerProfileId value for this Documento.
     * 
     * @return buyerProfileId
     */
    public java.lang.String getBuyerProfileId() {
        return buyerProfileId;
    }


    /**
     * Sets the buyerProfileId value for this Documento.
     * 
     * @param buyerProfileId
     */
    public void setBuyerProfileId(java.lang.String buyerProfileId) {
        this.buyerProfileId = buyerProfileId;
    }


    /**
     * Gets the contenido value for this Documento.
     * 
     * @return contenido
     */
    public byte[] getContenido() {
        return contenido;
    }


    /**
     * Sets the contenido value for this Documento.
     * 
     * @param contenido
     */
    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }


    /**
     * Gets the descripcion value for this Documento.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this Documento.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the expedientNumber value for this Documento.
     * 
     * @return expedientNumber
     */
    public java.lang.String getExpedientNumber() {
        return expedientNumber;
    }


    /**
     * Sets the expedientNumber value for this Documento.
     * 
     * @param expedientNumber
     */
    public void setExpedientNumber(java.lang.String expedientNumber) {
        this.expedientNumber = expedientNumber;
    }


    /**
     * Gets the fechaFirma value for this Documento.
     * 
     * @return fechaFirma
     */
    public java.util.Calendar getFechaFirma() {
        return fechaFirma;
    }


    /**
     * Sets the fechaFirma value for this Documento.
     * 
     * @param fechaFirma
     */
    public void setFechaFirma(java.util.Calendar fechaFirma) {
        this.fechaFirma = fechaFirma;
    }


    /**
     * Gets the idTypeDoc value for this Documento.
     * 
     * @return idTypeDoc
     */
    public java.lang.String getIdTypeDoc() {
        return idTypeDoc;
    }


    /**
     * Sets the idTypeDoc value for this Documento.
     * 
     * @param idTypeDoc
     */
    public void setIdTypeDoc(java.lang.String idTypeDoc) {
        this.idTypeDoc = idTypeDoc;
    }


    /**
     * Gets the mimeCode value for this Documento.
     * 
     * @return mimeCode
     */
    public java.lang.String getMimeCode() {
        return mimeCode;
    }


    /**
     * Sets the mimeCode value for this Documento.
     * 
     * @param mimeCode
     */
    public void setMimeCode(java.lang.String mimeCode) {
        this.mimeCode = mimeCode;
    }


    /**
     * Gets the nameDoc value for this Documento.
     * 
     * @return nameDoc
     */
    public java.lang.String getNameDoc() {
        return nameDoc;
    }


    /**
     * Sets the nameDoc value for this Documento.
     * 
     * @param nameDoc
     */
    public void setNameDoc(java.lang.String nameDoc) {
        this.nameDoc = nameDoc;
    }


    /**
     * Gets the publicationId value for this Documento.
     * 
     * @return publicationId
     */
    public java.lang.String getPublicationId() {
        return publicationId;
    }


    /**
     * Sets the publicationId value for this Documento.
     * 
     * @param publicationId
     */
    public void setPublicationId(java.lang.String publicationId) {
        this.publicationId = publicationId;
    }


    /**
     * Gets the typeDoc value for this Documento.
     * 
     * @return typeDoc
     */
    public java.lang.String getTypeDoc() {
        return typeDoc;
    }


    /**
     * Sets the typeDoc value for this Documento.
     * 
     * @param typeDoc
     */
    public void setTypeDoc(java.lang.String typeDoc) {
        this.typeDoc = typeDoc;
    }


    /**
     * Gets the urlDocument value for this Documento.
     * 
     * @return urlDocument
     */
    public java.lang.String getUrlDocument() {
        return urlDocument;
    }


    /**
     * Sets the urlDocument value for this Documento.
     * 
     * @param urlDocument
     */
    public void setUrlDocument(java.lang.String urlDocument) {
        this.urlDocument = urlDocument;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Documento)) return false;
        Documento other = (Documento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.buyerProfileId==null && other.getBuyerProfileId()==null) || 
             (this.buyerProfileId!=null &&
              this.buyerProfileId.equals(other.getBuyerProfileId()))) &&
            ((this.contenido==null && other.getContenido()==null) || 
             (this.contenido!=null &&
              java.util.Arrays.equals(this.contenido, other.getContenido()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              this.descripcion.equals(other.getDescripcion()))) &&
            ((this.expedientNumber==null && other.getExpedientNumber()==null) || 
             (this.expedientNumber!=null &&
              this.expedientNumber.equals(other.getExpedientNumber()))) &&
            ((this.fechaFirma==null && other.getFechaFirma()==null) || 
             (this.fechaFirma!=null &&
              this.fechaFirma.equals(other.getFechaFirma()))) &&
            ((this.idTypeDoc==null && other.getIdTypeDoc()==null) || 
             (this.idTypeDoc!=null &&
              this.idTypeDoc.equals(other.getIdTypeDoc()))) &&
            ((this.mimeCode==null && other.getMimeCode()==null) || 
             (this.mimeCode!=null &&
              this.mimeCode.equals(other.getMimeCode()))) &&
            ((this.nameDoc==null && other.getNameDoc()==null) || 
             (this.nameDoc!=null &&
              this.nameDoc.equals(other.getNameDoc()))) &&
            ((this.publicationId==null && other.getPublicationId()==null) || 
             (this.publicationId!=null &&
              this.publicationId.equals(other.getPublicationId()))) &&
            ((this.typeDoc==null && other.getTypeDoc()==null) || 
             (this.typeDoc!=null &&
              this.typeDoc.equals(other.getTypeDoc()))) &&
            ((this.urlDocument==null && other.getUrlDocument()==null) || 
             (this.urlDocument!=null &&
              this.urlDocument.equals(other.getUrlDocument())));
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
        if (getBuyerProfileId() != null) {
            _hashCode += getBuyerProfileId().hashCode();
        }
        if (getContenido() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContenido());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContenido(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDescripcion() != null) {
            _hashCode += getDescripcion().hashCode();
        }
        if (getExpedientNumber() != null) {
            _hashCode += getExpedientNumber().hashCode();
        }
        if (getFechaFirma() != null) {
            _hashCode += getFechaFirma().hashCode();
        }
        if (getIdTypeDoc() != null) {
            _hashCode += getIdTypeDoc().hashCode();
        }
        if (getMimeCode() != null) {
            _hashCode += getMimeCode().hashCode();
        }
        if (getNameDoc() != null) {
            _hashCode += getNameDoc().hashCode();
        }
        if (getPublicationId() != null) {
            _hashCode += getPublicationId().hashCode();
        }
        if (getTypeDoc() != null) {
            _hashCode += getTypeDoc().hashCode();
        }
        if (getUrlDocument() != null) {
            _hashCode += getUrlDocument().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Documento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Documento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("buyerProfileId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "buyerProfileId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contenido");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "contenido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expedientNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "expedientNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFirma");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fechaFirma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTypeDoc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "idTypeDoc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mimeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "mimeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nameDoc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "nameDoc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publicationId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "publicationId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeDoc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "typeDoc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urlDocument");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "urlDocument"));
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
