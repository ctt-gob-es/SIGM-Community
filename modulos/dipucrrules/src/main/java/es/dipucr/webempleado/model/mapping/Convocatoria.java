/**
 * Convocatoria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class Convocatoria  implements java.io.Serializable {
    private java.lang.String DConvocatoria;

    private java.util.Calendar FComision;

    private java.util.Calendar FFinConvocatoria;

    private java.util.Calendar FIniConvocatoria;

    private java.util.Calendar FResolucion;

    private java.math.BigDecimal dotacionEconomica;

    private java.lang.String estado;

    private int idConvocatoria;

    private java.math.BigDecimal importeDistribuido;

    private java.math.BigDecimal importePunto;

    private java.lang.String regComision;

    private java.lang.String regResolucion;

    private java.lang.Integer totalPuntos;

    public Convocatoria() {
    }

    public Convocatoria(
           java.lang.String DConvocatoria,
           java.util.Calendar FComision,
           java.util.Calendar FFinConvocatoria,
           java.util.Calendar FIniConvocatoria,
           java.util.Calendar FResolucion,
           java.math.BigDecimal dotacionEconomica,
           java.lang.String estado,
           int idConvocatoria,
           java.math.BigDecimal importeDistribuido,
           java.math.BigDecimal importePunto,
           java.lang.String regComision,
           java.lang.String regResolucion,
           java.lang.Integer totalPuntos) {
           this.DConvocatoria = DConvocatoria;
           this.FComision = FComision;
           this.FFinConvocatoria = FFinConvocatoria;
           this.FIniConvocatoria = FIniConvocatoria;
           this.FResolucion = FResolucion;
           this.dotacionEconomica = dotacionEconomica;
           this.estado = estado;
           this.idConvocatoria = idConvocatoria;
           this.importeDistribuido = importeDistribuido;
           this.importePunto = importePunto;
           this.regComision = regComision;
           this.regResolucion = regResolucion;
           this.totalPuntos = totalPuntos;
    }


    /**
     * Gets the DConvocatoria value for this Convocatoria.
     * 
     * @return DConvocatoria
     */
    public java.lang.String getDConvocatoria() {
        return DConvocatoria;
    }


    /**
     * Sets the DConvocatoria value for this Convocatoria.
     * 
     * @param DConvocatoria
     */
    public void setDConvocatoria(java.lang.String DConvocatoria) {
        this.DConvocatoria = DConvocatoria;
    }


    /**
     * Gets the FComision value for this Convocatoria.
     * 
     * @return FComision
     */
    public java.util.Calendar getFComision() {
        return FComision;
    }


    /**
     * Sets the FComision value for this Convocatoria.
     * 
     * @param FComision
     */
    public void setFComision(java.util.Calendar FComision) {
        this.FComision = FComision;
    }


    /**
     * Gets the FFinConvocatoria value for this Convocatoria.
     * 
     * @return FFinConvocatoria
     */
    public java.util.Calendar getFFinConvocatoria() {
        return FFinConvocatoria;
    }


    /**
     * Sets the FFinConvocatoria value for this Convocatoria.
     * 
     * @param FFinConvocatoria
     */
    public void setFFinConvocatoria(java.util.Calendar FFinConvocatoria) {
        this.FFinConvocatoria = FFinConvocatoria;
    }


    /**
     * Gets the FIniConvocatoria value for this Convocatoria.
     * 
     * @return FIniConvocatoria
     */
    public java.util.Calendar getFIniConvocatoria() {
        return FIniConvocatoria;
    }


    /**
     * Sets the FIniConvocatoria value for this Convocatoria.
     * 
     * @param FIniConvocatoria
     */
    public void setFIniConvocatoria(java.util.Calendar FIniConvocatoria) {
        this.FIniConvocatoria = FIniConvocatoria;
    }


    /**
     * Gets the FResolucion value for this Convocatoria.
     * 
     * @return FResolucion
     */
    public java.util.Calendar getFResolucion() {
        return FResolucion;
    }


    /**
     * Sets the FResolucion value for this Convocatoria.
     * 
     * @param FResolucion
     */
    public void setFResolucion(java.util.Calendar FResolucion) {
        this.FResolucion = FResolucion;
    }


    /**
     * Gets the dotacionEconomica value for this Convocatoria.
     * 
     * @return dotacionEconomica
     */
    public java.math.BigDecimal getDotacionEconomica() {
        return dotacionEconomica;
    }


    /**
     * Sets the dotacionEconomica value for this Convocatoria.
     * 
     * @param dotacionEconomica
     */
    public void setDotacionEconomica(java.math.BigDecimal dotacionEconomica) {
        this.dotacionEconomica = dotacionEconomica;
    }


    /**
     * Gets the estado value for this Convocatoria.
     * 
     * @return estado
     */
    public java.lang.String getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this Convocatoria.
     * 
     * @param estado
     */
    public void setEstado(java.lang.String estado) {
        this.estado = estado;
    }


    /**
     * Gets the idConvocatoria value for this Convocatoria.
     * 
     * @return idConvocatoria
     */
    public int getIdConvocatoria() {
        return idConvocatoria;
    }


    /**
     * Sets the idConvocatoria value for this Convocatoria.
     * 
     * @param idConvocatoria
     */
    public void setIdConvocatoria(int idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }


    /**
     * Gets the importeDistribuido value for this Convocatoria.
     * 
     * @return importeDistribuido
     */
    public java.math.BigDecimal getImporteDistribuido() {
        return importeDistribuido;
    }


    /**
     * Sets the importeDistribuido value for this Convocatoria.
     * 
     * @param importeDistribuido
     */
    public void setImporteDistribuido(java.math.BigDecimal importeDistribuido) {
        this.importeDistribuido = importeDistribuido;
    }


    /**
     * Gets the importePunto value for this Convocatoria.
     * 
     * @return importePunto
     */
    public java.math.BigDecimal getImportePunto() {
        return importePunto;
    }


    /**
     * Sets the importePunto value for this Convocatoria.
     * 
     * @param importePunto
     */
    public void setImportePunto(java.math.BigDecimal importePunto) {
        this.importePunto = importePunto;
    }


    /**
     * Gets the regComision value for this Convocatoria.
     * 
     * @return regComision
     */
    public java.lang.String getRegComision() {
        return regComision;
    }


    /**
     * Sets the regComision value for this Convocatoria.
     * 
     * @param regComision
     */
    public void setRegComision(java.lang.String regComision) {
        this.regComision = regComision;
    }


    /**
     * Gets the regResolucion value for this Convocatoria.
     * 
     * @return regResolucion
     */
    public java.lang.String getRegResolucion() {
        return regResolucion;
    }


    /**
     * Sets the regResolucion value for this Convocatoria.
     * 
     * @param regResolucion
     */
    public void setRegResolucion(java.lang.String regResolucion) {
        this.regResolucion = regResolucion;
    }


    /**
     * Gets the totalPuntos value for this Convocatoria.
     * 
     * @return totalPuntos
     */
    public java.lang.Integer getTotalPuntos() {
        return totalPuntos;
    }


    /**
     * Sets the totalPuntos value for this Convocatoria.
     * 
     * @param totalPuntos
     */
    public void setTotalPuntos(java.lang.Integer totalPuntos) {
        this.totalPuntos = totalPuntos;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Convocatoria)) return false;
        Convocatoria other = (Convocatoria) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.DConvocatoria==null && other.getDConvocatoria()==null) || 
             (this.DConvocatoria!=null &&
              this.DConvocatoria.equals(other.getDConvocatoria()))) &&
            ((this.FComision==null && other.getFComision()==null) || 
             (this.FComision!=null &&
              this.FComision.equals(other.getFComision()))) &&
            ((this.FFinConvocatoria==null && other.getFFinConvocatoria()==null) || 
             (this.FFinConvocatoria!=null &&
              this.FFinConvocatoria.equals(other.getFFinConvocatoria()))) &&
            ((this.FIniConvocatoria==null && other.getFIniConvocatoria()==null) || 
             (this.FIniConvocatoria!=null &&
              this.FIniConvocatoria.equals(other.getFIniConvocatoria()))) &&
            ((this.FResolucion==null && other.getFResolucion()==null) || 
             (this.FResolucion!=null &&
              this.FResolucion.equals(other.getFResolucion()))) &&
            ((this.dotacionEconomica==null && other.getDotacionEconomica()==null) || 
             (this.dotacionEconomica!=null &&
              this.dotacionEconomica.equals(other.getDotacionEconomica()))) &&
            ((this.estado==null && other.getEstado()==null) || 
             (this.estado!=null &&
              this.estado.equals(other.getEstado()))) &&
            this.idConvocatoria == other.getIdConvocatoria() &&
            ((this.importeDistribuido==null && other.getImporteDistribuido()==null) || 
             (this.importeDistribuido!=null &&
              this.importeDistribuido.equals(other.getImporteDistribuido()))) &&
            ((this.importePunto==null && other.getImportePunto()==null) || 
             (this.importePunto!=null &&
              this.importePunto.equals(other.getImportePunto()))) &&
            ((this.regComision==null && other.getRegComision()==null) || 
             (this.regComision!=null &&
              this.regComision.equals(other.getRegComision()))) &&
            ((this.regResolucion==null && other.getRegResolucion()==null) || 
             (this.regResolucion!=null &&
              this.regResolucion.equals(other.getRegResolucion()))) &&
            ((this.totalPuntos==null && other.getTotalPuntos()==null) || 
             (this.totalPuntos!=null &&
              this.totalPuntos.equals(other.getTotalPuntos())));
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
        if (getDConvocatoria() != null) {
            _hashCode += getDConvocatoria().hashCode();
        }
        if (getFComision() != null) {
            _hashCode += getFComision().hashCode();
        }
        if (getFFinConvocatoria() != null) {
            _hashCode += getFFinConvocatoria().hashCode();
        }
        if (getFIniConvocatoria() != null) {
            _hashCode += getFIniConvocatoria().hashCode();
        }
        if (getFResolucion() != null) {
            _hashCode += getFResolucion().hashCode();
        }
        if (getDotacionEconomica() != null) {
            _hashCode += getDotacionEconomica().hashCode();
        }
        if (getEstado() != null) {
            _hashCode += getEstado().hashCode();
        }
        _hashCode += getIdConvocatoria();
        if (getImporteDistribuido() != null) {
            _hashCode += getImporteDistribuido().hashCode();
        }
        if (getImportePunto() != null) {
            _hashCode += getImportePunto().hashCode();
        }
        if (getRegComision() != null) {
            _hashCode += getRegComision().hashCode();
        }
        if (getRegResolucion() != null) {
            _hashCode += getRegResolucion().hashCode();
        }
        if (getTotalPuntos() != null) {
            _hashCode += getTotalPuntos().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Convocatoria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Convocatoria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DConvocatoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "DConvocatoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FComision");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "FComision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FFinConvocatoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "FFinConvocatoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FIniConvocatoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "FIniConvocatoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FResolucion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "FResolucion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dotacionEconomica");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "dotacionEconomica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idConvocatoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "idConvocatoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importeDistribuido");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "importeDistribuido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importePunto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "importePunto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regComision");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "regComision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regResolucion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "regResolucion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalPuntos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "totalPuntos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
