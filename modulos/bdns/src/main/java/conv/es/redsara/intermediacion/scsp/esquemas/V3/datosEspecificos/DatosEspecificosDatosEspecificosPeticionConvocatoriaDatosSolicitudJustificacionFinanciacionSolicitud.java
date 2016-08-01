/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud  implements java.io.Serializable {
    private conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitudAbierto abierto;

    private java.lang.String inicioSolicitud;

    private java.util.Date fechaInicioSolicitud;

    private java.lang.String finSolicitud;

    private java.util.Date fechaFinSolicitud;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud(
           conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitudAbierto abierto,
           java.lang.String inicioSolicitud,
           java.util.Date fechaInicioSolicitud,
           java.lang.String finSolicitud,
           java.util.Date fechaFinSolicitud) {
           this.abierto = abierto;
           this.inicioSolicitud = inicioSolicitud;
           this.fechaInicioSolicitud = fechaInicioSolicitud;
           this.finSolicitud = finSolicitud;
           this.fechaFinSolicitud = fechaFinSolicitud;
    }


    /**
     * Gets the abierto value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.
     * 
     * @return abierto
     */
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitudAbierto getAbierto() {
        return abierto;
    }


    /**
     * Sets the abierto value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.
     * 
     * @param abierto
     */
    public void setAbierto(conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitudAbierto abierto) {
        this.abierto = abierto;
    }


    /**
     * Gets the inicioSolicitud value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.
     * 
     * @return inicioSolicitud
     */
    public java.lang.String getInicioSolicitud() {
        return inicioSolicitud;
    }


    /**
     * Sets the inicioSolicitud value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.
     * 
     * @param inicioSolicitud
     */
    public void setInicioSolicitud(java.lang.String inicioSolicitud) {
        this.inicioSolicitud = inicioSolicitud;
    }


    /**
     * Gets the fechaInicioSolicitud value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.
     * 
     * @return fechaInicioSolicitud
     */
    public java.util.Date getFechaInicioSolicitud() {
        return fechaInicioSolicitud;
    }


    /**
     * Sets the fechaInicioSolicitud value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.
     * 
     * @param fechaInicioSolicitud
     */
    public void setFechaInicioSolicitud(java.util.Date fechaInicioSolicitud) {
        this.fechaInicioSolicitud = fechaInicioSolicitud;
    }


    /**
     * Gets the finSolicitud value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.
     * 
     * @return finSolicitud
     */
    public java.lang.String getFinSolicitud() {
        return finSolicitud;
    }


    /**
     * Sets the finSolicitud value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.
     * 
     * @param finSolicitud
     */
    public void setFinSolicitud(java.lang.String finSolicitud) {
        this.finSolicitud = finSolicitud;
    }


    /**
     * Gets the fechaFinSolicitud value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.
     * 
     * @return fechaFinSolicitud
     */
    public java.util.Date getFechaFinSolicitud() {
        return fechaFinSolicitud;
    }


    /**
     * Sets the fechaFinSolicitud value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.
     * 
     * @param fechaFinSolicitud
     */
    public void setFechaFinSolicitud(java.util.Date fechaFinSolicitud) {
        this.fechaFinSolicitud = fechaFinSolicitud;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.abierto==null && other.getAbierto()==null) || 
             (this.abierto!=null &&
              this.abierto.equals(other.getAbierto()))) &&
            ((this.inicioSolicitud==null && other.getInicioSolicitud()==null) || 
             (this.inicioSolicitud!=null &&
              this.inicioSolicitud.equals(other.getInicioSolicitud()))) &&
            ((this.fechaInicioSolicitud==null && other.getFechaInicioSolicitud()==null) || 
             (this.fechaInicioSolicitud!=null &&
              this.fechaInicioSolicitud.equals(other.getFechaInicioSolicitud()))) &&
            ((this.finSolicitud==null && other.getFinSolicitud()==null) || 
             (this.finSolicitud!=null &&
              this.finSolicitud.equals(other.getFinSolicitud()))) &&
            ((this.fechaFinSolicitud==null && other.getFechaFinSolicitud()==null) || 
             (this.fechaFinSolicitud!=null &&
              this.fechaFinSolicitud.equals(other.getFechaFinSolicitud())));
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
        if (getAbierto() != null) {
            _hashCode += getAbierto().hashCode();
        }
        if (getInicioSolicitud() != null) {
            _hashCode += getInicioSolicitud().hashCode();
        }
        if (getFechaInicioSolicitud() != null) {
            _hashCode += getFechaInicioSolicitud().hashCode();
        }
        if (getFinSolicitud() != null) {
            _hashCode += getFinSolicitud().hashCode();
        }
        if (getFechaFinSolicitud() != null) {
            _hashCode += getFechaFinSolicitud().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionSolicitud.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>DatosSolicitudJustificacionFinanciacion>Solicitud"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("abierto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Abierto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>DatosSolicitudJustificacionFinanciacion>Solicitud>Abierto"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inicioSolicitud");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "InicioSolicitud"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaInicioSolicitud");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "FechaInicioSolicitud"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("finSolicitud");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "FinSolicitud"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFinSolicitud");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "FechaFinSolicitud"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
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
