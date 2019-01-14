/**
 * Propuesta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class Propuesta  implements java.io.Serializable {
    private java.util.Calendar FPropuesta;

    private java.lang.String estado;

    private int idPropuesta;

    private java.lang.String tipoAyuda;

    private java.lang.String tipoRegimen;

    private java.lang.String tipoSolicitud;

    public Propuesta() {
    }

    public Propuesta(
           java.util.Calendar FPropuesta,
           java.lang.String estado,
           int idPropuesta,
           java.lang.String tipoAyuda,
           java.lang.String tipoRegimen,
           java.lang.String tipoSolicitud) {
           this.FPropuesta = FPropuesta;
           this.estado = estado;
           this.idPropuesta = idPropuesta;
           this.tipoAyuda = tipoAyuda;
           this.tipoRegimen = tipoRegimen;
           this.tipoSolicitud = tipoSolicitud;
    }


    /**
     * Gets the FPropuesta value for this Propuesta.
     * 
     * @return FPropuesta
     */
    public java.util.Calendar getFPropuesta() {
        return FPropuesta;
    }


    /**
     * Sets the FPropuesta value for this Propuesta.
     * 
     * @param FPropuesta
     */
    public void setFPropuesta(java.util.Calendar FPropuesta) {
        this.FPropuesta = FPropuesta;
    }


    /**
     * Gets the estado value for this Propuesta.
     * 
     * @return estado
     */
    public java.lang.String getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this Propuesta.
     * 
     * @param estado
     */
    public void setEstado(java.lang.String estado) {
        this.estado = estado;
    }


    /**
     * Gets the idPropuesta value for this Propuesta.
     * 
     * @return idPropuesta
     */
    public int getIdPropuesta() {
        return idPropuesta;
    }


    /**
     * Sets the idPropuesta value for this Propuesta.
     * 
     * @param idPropuesta
     */
    public void setIdPropuesta(int idPropuesta) {
        this.idPropuesta = idPropuesta;
    }


    /**
     * Gets the tipoAyuda value for this Propuesta.
     * 
     * @return tipoAyuda
     */
    public java.lang.String getTipoAyuda() {
        return tipoAyuda;
    }


    /**
     * Sets the tipoAyuda value for this Propuesta.
     * 
     * @param tipoAyuda
     */
    public void setTipoAyuda(java.lang.String tipoAyuda) {
        this.tipoAyuda = tipoAyuda;
    }


    /**
     * Gets the tipoRegimen value for this Propuesta.
     * 
     * @return tipoRegimen
     */
    public java.lang.String getTipoRegimen() {
        return tipoRegimen;
    }


    /**
     * Sets the tipoRegimen value for this Propuesta.
     * 
     * @param tipoRegimen
     */
    public void setTipoRegimen(java.lang.String tipoRegimen) {
        this.tipoRegimen = tipoRegimen;
    }


    /**
     * Gets the tipoSolicitud value for this Propuesta.
     * 
     * @return tipoSolicitud
     */
    public java.lang.String getTipoSolicitud() {
        return tipoSolicitud;
    }


    /**
     * Sets the tipoSolicitud value for this Propuesta.
     * 
     * @param tipoSolicitud
     */
    public void setTipoSolicitud(java.lang.String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Propuesta)) return false;
        Propuesta other = (Propuesta) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.FPropuesta==null && other.getFPropuesta()==null) || 
             (this.FPropuesta!=null &&
              this.FPropuesta.equals(other.getFPropuesta()))) &&
            ((this.estado==null && other.getEstado()==null) || 
             (this.estado!=null &&
              this.estado.equals(other.getEstado()))) &&
            this.idPropuesta == other.getIdPropuesta() &&
            ((this.tipoAyuda==null && other.getTipoAyuda()==null) || 
             (this.tipoAyuda!=null &&
              this.tipoAyuda.equals(other.getTipoAyuda()))) &&
            ((this.tipoRegimen==null && other.getTipoRegimen()==null) || 
             (this.tipoRegimen!=null &&
              this.tipoRegimen.equals(other.getTipoRegimen()))) &&
            ((this.tipoSolicitud==null && other.getTipoSolicitud()==null) || 
             (this.tipoSolicitud!=null &&
              this.tipoSolicitud.equals(other.getTipoSolicitud())));
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
        if (getFPropuesta() != null) {
            _hashCode += getFPropuesta().hashCode();
        }
        if (getEstado() != null) {
            _hashCode += getEstado().hashCode();
        }
        _hashCode += getIdPropuesta();
        if (getTipoAyuda() != null) {
            _hashCode += getTipoAyuda().hashCode();
        }
        if (getTipoRegimen() != null) {
            _hashCode += getTipoRegimen().hashCode();
        }
        if (getTipoSolicitud() != null) {
            _hashCode += getTipoSolicitud().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Propuesta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Propuesta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FPropuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "FPropuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPropuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "idPropuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoAyuda");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "tipoAyuda"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoRegimen");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "tipoRegimen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoSolicitud");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "tipoSolicitud"));
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
