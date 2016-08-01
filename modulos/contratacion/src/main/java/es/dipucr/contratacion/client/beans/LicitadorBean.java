/**
 * LicitadorBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class LicitadorBean  implements java.io.Serializable {
    private java.lang.String calle;

    private java.lang.String cp;

    private java.util.Calendar fechaAdjudicacion;

    private java.util.Calendar fechaFinFormalizacion;

    private java.lang.String identificador;

    private java.lang.String importeConImpuestos;

    private java.lang.String importeSinImpuestos;

    private java.lang.String justificacionDescripcion;

    private es.dipucr.contratacion.client.beans.Campo justificacionProceso;

    private java.lang.String motivacion;

    private java.lang.String nombre;

    private java.lang.String tipoIdentificador;

    public LicitadorBean() {
    }

    public LicitadorBean(
           java.lang.String calle,
           java.lang.String cp,
           java.util.Calendar fechaAdjudicacion,
           java.util.Calendar fechaFinFormalizacion,
           java.lang.String identificador,
           java.lang.String importeConImpuestos,
           java.lang.String importeSinImpuestos,
           java.lang.String justificacionDescripcion,
           es.dipucr.contratacion.client.beans.Campo justificacionProceso,
           java.lang.String motivacion,
           java.lang.String nombre,
           java.lang.String tipoIdentificador) {
           this.calle = calle;
           this.cp = cp;
           this.fechaAdjudicacion = fechaAdjudicacion;
           this.fechaFinFormalizacion = fechaFinFormalizacion;
           this.identificador = identificador;
           this.importeConImpuestos = importeConImpuestos;
           this.importeSinImpuestos = importeSinImpuestos;
           this.justificacionDescripcion = justificacionDescripcion;
           this.justificacionProceso = justificacionProceso;
           this.motivacion = motivacion;
           this.nombre = nombre;
           this.tipoIdentificador = tipoIdentificador;
    }


    /**
     * Gets the calle value for this LicitadorBean.
     * 
     * @return calle
     */
    public java.lang.String getCalle() {
        return calle;
    }


    /**
     * Sets the calle value for this LicitadorBean.
     * 
     * @param calle
     */
    public void setCalle(java.lang.String calle) {
        this.calle = calle;
    }


    /**
     * Gets the cp value for this LicitadorBean.
     * 
     * @return cp
     */
    public java.lang.String getCp() {
        return cp;
    }


    /**
     * Sets the cp value for this LicitadorBean.
     * 
     * @param cp
     */
    public void setCp(java.lang.String cp) {
        this.cp = cp;
    }


    /**
     * Gets the fechaAdjudicacion value for this LicitadorBean.
     * 
     * @return fechaAdjudicacion
     */
    public java.util.Calendar getFechaAdjudicacion() {
        return fechaAdjudicacion;
    }


    /**
     * Sets the fechaAdjudicacion value for this LicitadorBean.
     * 
     * @param fechaAdjudicacion
     */
    public void setFechaAdjudicacion(java.util.Calendar fechaAdjudicacion) {
        this.fechaAdjudicacion = fechaAdjudicacion;
    }


    /**
     * Gets the fechaFinFormalizacion value for this LicitadorBean.
     * 
     * @return fechaFinFormalizacion
     */
    public java.util.Calendar getFechaFinFormalizacion() {
        return fechaFinFormalizacion;
    }


    /**
     * Sets the fechaFinFormalizacion value for this LicitadorBean.
     * 
     * @param fechaFinFormalizacion
     */
    public void setFechaFinFormalizacion(java.util.Calendar fechaFinFormalizacion) {
        this.fechaFinFormalizacion = fechaFinFormalizacion;
    }


    /**
     * Gets the identificador value for this LicitadorBean.
     * 
     * @return identificador
     */
    public java.lang.String getIdentificador() {
        return identificador;
    }


    /**
     * Sets the identificador value for this LicitadorBean.
     * 
     * @param identificador
     */
    public void setIdentificador(java.lang.String identificador) {
        this.identificador = identificador;
    }


    /**
     * Gets the importeConImpuestos value for this LicitadorBean.
     * 
     * @return importeConImpuestos
     */
    public java.lang.String getImporteConImpuestos() {
        return importeConImpuestos;
    }


    /**
     * Sets the importeConImpuestos value for this LicitadorBean.
     * 
     * @param importeConImpuestos
     */
    public void setImporteConImpuestos(java.lang.String importeConImpuestos) {
        this.importeConImpuestos = importeConImpuestos;
    }


    /**
     * Gets the importeSinImpuestos value for this LicitadorBean.
     * 
     * @return importeSinImpuestos
     */
    public java.lang.String getImporteSinImpuestos() {
        return importeSinImpuestos;
    }


    /**
     * Sets the importeSinImpuestos value for this LicitadorBean.
     * 
     * @param importeSinImpuestos
     */
    public void setImporteSinImpuestos(java.lang.String importeSinImpuestos) {
        this.importeSinImpuestos = importeSinImpuestos;
    }


    /**
     * Gets the justificacionDescripcion value for this LicitadorBean.
     * 
     * @return justificacionDescripcion
     */
    public java.lang.String getJustificacionDescripcion() {
        return justificacionDescripcion;
    }


    /**
     * Sets the justificacionDescripcion value for this LicitadorBean.
     * 
     * @param justificacionDescripcion
     */
    public void setJustificacionDescripcion(java.lang.String justificacionDescripcion) {
        this.justificacionDescripcion = justificacionDescripcion;
    }


    /**
     * Gets the justificacionProceso value for this LicitadorBean.
     * 
     * @return justificacionProceso
     */
    public es.dipucr.contratacion.client.beans.Campo getJustificacionProceso() {
        return justificacionProceso;
    }


    /**
     * Sets the justificacionProceso value for this LicitadorBean.
     * 
     * @param justificacionProceso
     */
    public void setJustificacionProceso(es.dipucr.contratacion.client.beans.Campo justificacionProceso) {
        this.justificacionProceso = justificacionProceso;
    }


    /**
     * Gets the motivacion value for this LicitadorBean.
     * 
     * @return motivacion
     */
    public java.lang.String getMotivacion() {
        return motivacion;
    }


    /**
     * Sets the motivacion value for this LicitadorBean.
     * 
     * @param motivacion
     */
    public void setMotivacion(java.lang.String motivacion) {
        this.motivacion = motivacion;
    }


    /**
     * Gets the nombre value for this LicitadorBean.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this LicitadorBean.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the tipoIdentificador value for this LicitadorBean.
     * 
     * @return tipoIdentificador
     */
    public java.lang.String getTipoIdentificador() {
        return tipoIdentificador;
    }


    /**
     * Sets the tipoIdentificador value for this LicitadorBean.
     * 
     * @param tipoIdentificador
     */
    public void setTipoIdentificador(java.lang.String tipoIdentificador) {
        this.tipoIdentificador = tipoIdentificador;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LicitadorBean)) return false;
        LicitadorBean other = (LicitadorBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.calle==null && other.getCalle()==null) || 
             (this.calle!=null &&
              this.calle.equals(other.getCalle()))) &&
            ((this.cp==null && other.getCp()==null) || 
             (this.cp!=null &&
              this.cp.equals(other.getCp()))) &&
            ((this.fechaAdjudicacion==null && other.getFechaAdjudicacion()==null) || 
             (this.fechaAdjudicacion!=null &&
              this.fechaAdjudicacion.equals(other.getFechaAdjudicacion()))) &&
            ((this.fechaFinFormalizacion==null && other.getFechaFinFormalizacion()==null) || 
             (this.fechaFinFormalizacion!=null &&
              this.fechaFinFormalizacion.equals(other.getFechaFinFormalizacion()))) &&
            ((this.identificador==null && other.getIdentificador()==null) || 
             (this.identificador!=null &&
              this.identificador.equals(other.getIdentificador()))) &&
            ((this.importeConImpuestos==null && other.getImporteConImpuestos()==null) || 
             (this.importeConImpuestos!=null &&
              this.importeConImpuestos.equals(other.getImporteConImpuestos()))) &&
            ((this.importeSinImpuestos==null && other.getImporteSinImpuestos()==null) || 
             (this.importeSinImpuestos!=null &&
              this.importeSinImpuestos.equals(other.getImporteSinImpuestos()))) &&
            ((this.justificacionDescripcion==null && other.getJustificacionDescripcion()==null) || 
             (this.justificacionDescripcion!=null &&
              this.justificacionDescripcion.equals(other.getJustificacionDescripcion()))) &&
            ((this.justificacionProceso==null && other.getJustificacionProceso()==null) || 
             (this.justificacionProceso!=null &&
              this.justificacionProceso.equals(other.getJustificacionProceso()))) &&
            ((this.motivacion==null && other.getMotivacion()==null) || 
             (this.motivacion!=null &&
              this.motivacion.equals(other.getMotivacion()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.tipoIdentificador==null && other.getTipoIdentificador()==null) || 
             (this.tipoIdentificador!=null &&
              this.tipoIdentificador.equals(other.getTipoIdentificador())));
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
        if (getCalle() != null) {
            _hashCode += getCalle().hashCode();
        }
        if (getCp() != null) {
            _hashCode += getCp().hashCode();
        }
        if (getFechaAdjudicacion() != null) {
            _hashCode += getFechaAdjudicacion().hashCode();
        }
        if (getFechaFinFormalizacion() != null) {
            _hashCode += getFechaFinFormalizacion().hashCode();
        }
        if (getIdentificador() != null) {
            _hashCode += getIdentificador().hashCode();
        }
        if (getImporteConImpuestos() != null) {
            _hashCode += getImporteConImpuestos().hashCode();
        }
        if (getImporteSinImpuestos() != null) {
            _hashCode += getImporteSinImpuestos().hashCode();
        }
        if (getJustificacionDescripcion() != null) {
            _hashCode += getJustificacionDescripcion().hashCode();
        }
        if (getJustificacionProceso() != null) {
            _hashCode += getJustificacionProceso().hashCode();
        }
        if (getMotivacion() != null) {
            _hashCode += getMotivacion().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getTipoIdentificador() != null) {
            _hashCode += getTipoIdentificador().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LicitadorBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "LicitadorBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("calle");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "calle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "cp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaAdjudicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fechaAdjudicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFinFormalizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "fechaFinFormalizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "identificador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importeConImpuestos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "importeConImpuestos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importeSinImpuestos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "importeSinImpuestos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("justificacionDescripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "justificacionDescripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("justificacionProceso");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "justificacionProceso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motivacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "motivacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoIdentificador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "tipoIdentificador"));
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
