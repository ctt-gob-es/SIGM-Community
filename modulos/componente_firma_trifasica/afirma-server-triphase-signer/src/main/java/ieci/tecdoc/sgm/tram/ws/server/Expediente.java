/**
 * Expediente.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class Expediente  extends ieci.tecdoc.sgm.core.services.dto.RetornoServicio  implements java.io.Serializable {
    private java.lang.String asunto;

    private ieci.tecdoc.sgm.tram.ws.server.DocElectronico[] documentosElectronicos;

    private ieci.tecdoc.sgm.tram.ws.server.DocFisico[] documentosFisicos;

    private ieci.tecdoc.sgm.tram.ws.server.Emplazamiento[] emplazamientos;

    private java.util.Calendar fechaFinalizacion;

    private java.util.Calendar fechaInicio;

    private java.lang.String idOrgProductor;

    private ieci.tecdoc.sgm.tram.ws.server.InfoBExpediente informacionBasica;

    private ieci.tecdoc.sgm.tram.ws.server.Interesado[] interesados;

    private java.lang.String nombreOrgProductor;

    public Expediente() {
    }

    public Expediente(
           java.lang.String errorCode,
           java.lang.String returnCode,
           java.lang.String asunto,
           ieci.tecdoc.sgm.tram.ws.server.DocElectronico[] documentosElectronicos,
           ieci.tecdoc.sgm.tram.ws.server.DocFisico[] documentosFisicos,
           ieci.tecdoc.sgm.tram.ws.server.Emplazamiento[] emplazamientos,
           java.util.Calendar fechaFinalizacion,
           java.util.Calendar fechaInicio,
           java.lang.String idOrgProductor,
           ieci.tecdoc.sgm.tram.ws.server.InfoBExpediente informacionBasica,
           ieci.tecdoc.sgm.tram.ws.server.Interesado[] interesados,
           java.lang.String nombreOrgProductor) {
        super(
            errorCode,
            returnCode);
        this.asunto = asunto;
        this.documentosElectronicos = documentosElectronicos;
        this.documentosFisicos = documentosFisicos;
        this.emplazamientos = emplazamientos;
        this.fechaFinalizacion = fechaFinalizacion;
        this.fechaInicio = fechaInicio;
        this.idOrgProductor = idOrgProductor;
        this.informacionBasica = informacionBasica;
        this.interesados = interesados;
        this.nombreOrgProductor = nombreOrgProductor;
    }


    /**
     * Gets the asunto value for this Expediente.
     * 
     * @return asunto
     */
    public java.lang.String getAsunto() {
        return asunto;
    }


    /**
     * Sets the asunto value for this Expediente.
     * 
     * @param asunto
     */
    public void setAsunto(java.lang.String asunto) {
        this.asunto = asunto;
    }


    /**
     * Gets the documentosElectronicos value for this Expediente.
     * 
     * @return documentosElectronicos
     */
    public ieci.tecdoc.sgm.tram.ws.server.DocElectronico[] getDocumentosElectronicos() {
        return documentosElectronicos;
    }


    /**
     * Sets the documentosElectronicos value for this Expediente.
     * 
     * @param documentosElectronicos
     */
    public void setDocumentosElectronicos(ieci.tecdoc.sgm.tram.ws.server.DocElectronico[] documentosElectronicos) {
        this.documentosElectronicos = documentosElectronicos;
    }


    /**
     * Gets the documentosFisicos value for this Expediente.
     * 
     * @return documentosFisicos
     */
    public ieci.tecdoc.sgm.tram.ws.server.DocFisico[] getDocumentosFisicos() {
        return documentosFisicos;
    }


    /**
     * Sets the documentosFisicos value for this Expediente.
     * 
     * @param documentosFisicos
     */
    public void setDocumentosFisicos(ieci.tecdoc.sgm.tram.ws.server.DocFisico[] documentosFisicos) {
        this.documentosFisicos = documentosFisicos;
    }


    /**
     * Gets the emplazamientos value for this Expediente.
     * 
     * @return emplazamientos
     */
    public ieci.tecdoc.sgm.tram.ws.server.Emplazamiento[] getEmplazamientos() {
        return emplazamientos;
    }


    /**
     * Sets the emplazamientos value for this Expediente.
     * 
     * @param emplazamientos
     */
    public void setEmplazamientos(ieci.tecdoc.sgm.tram.ws.server.Emplazamiento[] emplazamientos) {
        this.emplazamientos = emplazamientos;
    }


    /**
     * Gets the fechaFinalizacion value for this Expediente.
     * 
     * @return fechaFinalizacion
     */
    public java.util.Calendar getFechaFinalizacion() {
        return fechaFinalizacion;
    }


    /**
     * Sets the fechaFinalizacion value for this Expediente.
     * 
     * @param fechaFinalizacion
     */
    public void setFechaFinalizacion(java.util.Calendar fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }


    /**
     * Gets the fechaInicio value for this Expediente.
     * 
     * @return fechaInicio
     */
    public java.util.Calendar getFechaInicio() {
        return fechaInicio;
    }


    /**
     * Sets the fechaInicio value for this Expediente.
     * 
     * @param fechaInicio
     */
    public void setFechaInicio(java.util.Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }


    /**
     * Gets the idOrgProductor value for this Expediente.
     * 
     * @return idOrgProductor
     */
    public java.lang.String getIdOrgProductor() {
        return idOrgProductor;
    }


    /**
     * Sets the idOrgProductor value for this Expediente.
     * 
     * @param idOrgProductor
     */
    public void setIdOrgProductor(java.lang.String idOrgProductor) {
        this.idOrgProductor = idOrgProductor;
    }


    /**
     * Gets the informacionBasica value for this Expediente.
     * 
     * @return informacionBasica
     */
    public ieci.tecdoc.sgm.tram.ws.server.InfoBExpediente getInformacionBasica() {
        return informacionBasica;
    }


    /**
     * Sets the informacionBasica value for this Expediente.
     * 
     * @param informacionBasica
     */
    public void setInformacionBasica(ieci.tecdoc.sgm.tram.ws.server.InfoBExpediente informacionBasica) {
        this.informacionBasica = informacionBasica;
    }


    /**
     * Gets the interesados value for this Expediente.
     * 
     * @return interesados
     */
    public ieci.tecdoc.sgm.tram.ws.server.Interesado[] getInteresados() {
        return interesados;
    }


    /**
     * Sets the interesados value for this Expediente.
     * 
     * @param interesados
     */
    public void setInteresados(ieci.tecdoc.sgm.tram.ws.server.Interesado[] interesados) {
        this.interesados = interesados;
    }


    /**
     * Gets the nombreOrgProductor value for this Expediente.
     * 
     * @return nombreOrgProductor
     */
    public java.lang.String getNombreOrgProductor() {
        return nombreOrgProductor;
    }


    /**
     * Sets the nombreOrgProductor value for this Expediente.
     * 
     * @param nombreOrgProductor
     */
    public void setNombreOrgProductor(java.lang.String nombreOrgProductor) {
        this.nombreOrgProductor = nombreOrgProductor;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Expediente)) return false;
        Expediente other = (Expediente) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.asunto==null && other.getAsunto()==null) || 
             (this.asunto!=null &&
              this.asunto.equals(other.getAsunto()))) &&
            ((this.documentosElectronicos==null && other.getDocumentosElectronicos()==null) || 
             (this.documentosElectronicos!=null &&
              java.util.Arrays.equals(this.documentosElectronicos, other.getDocumentosElectronicos()))) &&
            ((this.documentosFisicos==null && other.getDocumentosFisicos()==null) || 
             (this.documentosFisicos!=null &&
              java.util.Arrays.equals(this.documentosFisicos, other.getDocumentosFisicos()))) &&
            ((this.emplazamientos==null && other.getEmplazamientos()==null) || 
             (this.emplazamientos!=null &&
              java.util.Arrays.equals(this.emplazamientos, other.getEmplazamientos()))) &&
            ((this.fechaFinalizacion==null && other.getFechaFinalizacion()==null) || 
             (this.fechaFinalizacion!=null &&
              this.fechaFinalizacion.equals(other.getFechaFinalizacion()))) &&
            ((this.fechaInicio==null && other.getFechaInicio()==null) || 
             (this.fechaInicio!=null &&
              this.fechaInicio.equals(other.getFechaInicio()))) &&
            ((this.idOrgProductor==null && other.getIdOrgProductor()==null) || 
             (this.idOrgProductor!=null &&
              this.idOrgProductor.equals(other.getIdOrgProductor()))) &&
            ((this.informacionBasica==null && other.getInformacionBasica()==null) || 
             (this.informacionBasica!=null &&
              this.informacionBasica.equals(other.getInformacionBasica()))) &&
            ((this.interesados==null && other.getInteresados()==null) || 
             (this.interesados!=null &&
              java.util.Arrays.equals(this.interesados, other.getInteresados()))) &&
            ((this.nombreOrgProductor==null && other.getNombreOrgProductor()==null) || 
             (this.nombreOrgProductor!=null &&
              this.nombreOrgProductor.equals(other.getNombreOrgProductor())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getAsunto() != null) {
            _hashCode += getAsunto().hashCode();
        }
        if (getDocumentosElectronicos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDocumentosElectronicos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDocumentosElectronicos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDocumentosFisicos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDocumentosFisicos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDocumentosFisicos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEmplazamientos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEmplazamientos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEmplazamientos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getFechaFinalizacion() != null) {
            _hashCode += getFechaFinalizacion().hashCode();
        }
        if (getFechaInicio() != null) {
            _hashCode += getFechaInicio().hashCode();
        }
        if (getIdOrgProductor() != null) {
            _hashCode += getIdOrgProductor().hashCode();
        }
        if (getInformacionBasica() != null) {
            _hashCode += getInformacionBasica().hashCode();
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
        if (getNombreOrgProductor() != null) {
            _hashCode += getNombreOrgProductor().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Expediente.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Expediente"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("asunto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "asunto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentosElectronicos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "documentosElectronicos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DocElectronico"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentosFisicos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "documentosFisicos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DocFisico"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("emplazamientos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "emplazamientos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Emplazamiento"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFinalizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "fechaFinalizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaInicio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "fechaInicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idOrgProductor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idOrgProductor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("informacionBasica");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "informacionBasica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoBExpediente"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interesados");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "interesados"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Interesado"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreOrgProductor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "nombreOrgProductor"));
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
