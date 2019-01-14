/**
 * DatosEspecificosDatosEspecificosPeticionEnvioReintegro.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionEnvioReintegro  implements java.io.Serializable {
    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdReintegro idReintegro;

    private java.util.Date fechaReint;

    private java.math.BigDecimal importePrincipalReint;

    private java.math.BigDecimal importeInteresesReint;

    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint causasReint;

    public DatosEspecificosDatosEspecificosPeticionEnvioReintegro() {
    }

    public DatosEspecificosDatosEspecificosPeticionEnvioReintegro(
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdReintegro idReintegro,
           java.util.Date fechaReint,
           java.math.BigDecimal importePrincipalReint,
           java.math.BigDecimal importeInteresesReint,
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint causasReint) {
           this.idReintegro = idReintegro;
           this.fechaReint = fechaReint;
           this.importePrincipalReint = importePrincipalReint;
           this.importeInteresesReint = importeInteresesReint;
           this.causasReint = causasReint;
    }


    /**
     * Gets the idReintegro value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegro.
     * 
     * @return idReintegro
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdReintegro getIdReintegro() {
        return idReintegro;
    }


    /**
     * Sets the idReintegro value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegro.
     * 
     * @param idReintegro
     */
    public void setIdReintegro(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdReintegro idReintegro) {
        this.idReintegro = idReintegro;
    }


    /**
     * Gets the fechaReint value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegro.
     * 
     * @return fechaReint
     */
    public java.util.Date getFechaReint() {
        return fechaReint;
    }


    /**
     * Sets the fechaReint value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegro.
     * 
     * @param fechaReint
     */
    public void setFechaReint(java.util.Date fechaReint) {
        this.fechaReint = fechaReint;
    }


    /**
     * Gets the importePrincipalReint value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegro.
     * 
     * @return importePrincipalReint
     */
    public java.math.BigDecimal getImportePrincipalReint() {
        return importePrincipalReint;
    }


    /**
     * Sets the importePrincipalReint value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegro.
     * 
     * @param importePrincipalReint
     */
    public void setImportePrincipalReint(java.math.BigDecimal importePrincipalReint) {
        this.importePrincipalReint = importePrincipalReint;
    }


    /**
     * Gets the importeInteresesReint value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegro.
     * 
     * @return importeInteresesReint
     */
    public java.math.BigDecimal getImporteInteresesReint() {
        return importeInteresesReint;
    }


    /**
     * Sets the importeInteresesReint value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegro.
     * 
     * @param importeInteresesReint
     */
    public void setImporteInteresesReint(java.math.BigDecimal importeInteresesReint) {
        this.importeInteresesReint = importeInteresesReint;
    }


    /**
     * Gets the causasReint value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegro.
     * 
     * @return causasReint
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint getCausasReint() {
        return causasReint;
    }


    /**
     * Sets the causasReint value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegro.
     * 
     * @param causasReint
     */
    public void setCausasReint(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint causasReint) {
        this.causasReint = causasReint;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionEnvioReintegro)) return false;
        DatosEspecificosDatosEspecificosPeticionEnvioReintegro other = (DatosEspecificosDatosEspecificosPeticionEnvioReintegro) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idReintegro==null && other.getIdReintegro()==null) || 
             (this.idReintegro!=null &&
              this.idReintegro.equals(other.getIdReintegro()))) &&
            ((this.fechaReint==null && other.getFechaReint()==null) || 
             (this.fechaReint!=null &&
              this.fechaReint.equals(other.getFechaReint()))) &&
            ((this.importePrincipalReint==null && other.getImportePrincipalReint()==null) || 
             (this.importePrincipalReint!=null &&
              this.importePrincipalReint.equals(other.getImportePrincipalReint()))) &&
            ((this.importeInteresesReint==null && other.getImporteInteresesReint()==null) || 
             (this.importeInteresesReint!=null &&
              this.importeInteresesReint.equals(other.getImporteInteresesReint()))) &&
            ((this.causasReint==null && other.getCausasReint()==null) || 
             (this.causasReint!=null &&
              this.causasReint.equals(other.getCausasReint())));
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
        if (getIdReintegro() != null) {
            _hashCode += getIdReintegro().hashCode();
        }
        if (getFechaReint() != null) {
            _hashCode += getFechaReint().hashCode();
        }
        if (getImportePrincipalReint() != null) {
            _hashCode += getImportePrincipalReint().hashCode();
        }
        if (getImporteInteresesReint() != null) {
            _hashCode += getImporteInteresesReint().hashCode();
        }
        if (getCausasReint() != null) {
            _hashCode += getCausasReint().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionEnvioReintegro.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>Envio>Reintegro"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idReintegro");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdReintegro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdReintegro"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaReint");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "FechaReint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importePrincipalReint");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ImportePrincipalReint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importeInteresesReint");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ImporteInteresesReint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("causasReint");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "CausasReint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Envio>Reintegro>CausasReint"));
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
