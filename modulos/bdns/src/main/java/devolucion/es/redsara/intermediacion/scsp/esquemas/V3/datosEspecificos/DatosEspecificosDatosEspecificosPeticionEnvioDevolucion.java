/**
 * DatosEspecificosDatosEspecificosPeticionEnvioDevolucion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionEnvioDevolucion  implements java.io.Serializable {
    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdDevolucion idDevolucion;

    private java.util.Date fechaDev;

    private java.math.BigDecimal importePrincipalDev;

    private java.math.BigDecimal importeInteresesDev;

    public DatosEspecificosDatosEspecificosPeticionEnvioDevolucion() {
    }

    public DatosEspecificosDatosEspecificosPeticionEnvioDevolucion(
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdDevolucion idDevolucion,
           java.util.Date fechaDev,
           java.math.BigDecimal importePrincipalDev,
           java.math.BigDecimal importeInteresesDev) {
           this.idDevolucion = idDevolucion;
           this.fechaDev = fechaDev;
           this.importePrincipalDev = importePrincipalDev;
           this.importeInteresesDev = importeInteresesDev;
    }


    /**
     * Gets the idDevolucion value for this DatosEspecificosDatosEspecificosPeticionEnvioDevolucion.
     * 
     * @return idDevolucion
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdDevolucion getIdDevolucion() {
        return idDevolucion;
    }


    /**
     * Sets the idDevolucion value for this DatosEspecificosDatosEspecificosPeticionEnvioDevolucion.
     * 
     * @param idDevolucion
     */
    public void setIdDevolucion(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdDevolucion idDevolucion) {
        this.idDevolucion = idDevolucion;
    }


    /**
     * Gets the fechaDev value for this DatosEspecificosDatosEspecificosPeticionEnvioDevolucion.
     * 
     * @return fechaDev
     */
    public java.util.Date getFechaDev() {
        return fechaDev;
    }


    /**
     * Sets the fechaDev value for this DatosEspecificosDatosEspecificosPeticionEnvioDevolucion.
     * 
     * @param fechaDev
     */
    public void setFechaDev(java.util.Date fechaDev) {
        this.fechaDev = fechaDev;
    }


    /**
     * Gets the importePrincipalDev value for this DatosEspecificosDatosEspecificosPeticionEnvioDevolucion.
     * 
     * @return importePrincipalDev
     */
    public java.math.BigDecimal getImportePrincipalDev() {
        return importePrincipalDev;
    }


    /**
     * Sets the importePrincipalDev value for this DatosEspecificosDatosEspecificosPeticionEnvioDevolucion.
     * 
     * @param importePrincipalDev
     */
    public void setImportePrincipalDev(java.math.BigDecimal importePrincipalDev) {
        this.importePrincipalDev = importePrincipalDev;
    }


    /**
     * Gets the importeInteresesDev value for this DatosEspecificosDatosEspecificosPeticionEnvioDevolucion.
     * 
     * @return importeInteresesDev
     */
    public java.math.BigDecimal getImporteInteresesDev() {
        return importeInteresesDev;
    }


    /**
     * Sets the importeInteresesDev value for this DatosEspecificosDatosEspecificosPeticionEnvioDevolucion.
     * 
     * @param importeInteresesDev
     */
    public void setImporteInteresesDev(java.math.BigDecimal importeInteresesDev) {
        this.importeInteresesDev = importeInteresesDev;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionEnvioDevolucion)) return false;
        DatosEspecificosDatosEspecificosPeticionEnvioDevolucion other = (DatosEspecificosDatosEspecificosPeticionEnvioDevolucion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idDevolucion==null && other.getIdDevolucion()==null) || 
             (this.idDevolucion!=null &&
              this.idDevolucion.equals(other.getIdDevolucion()))) &&
            ((this.fechaDev==null && other.getFechaDev()==null) || 
             (this.fechaDev!=null &&
              this.fechaDev.equals(other.getFechaDev()))) &&
            ((this.importePrincipalDev==null && other.getImportePrincipalDev()==null) || 
             (this.importePrincipalDev!=null &&
              this.importePrincipalDev.equals(other.getImportePrincipalDev()))) &&
            ((this.importeInteresesDev==null && other.getImporteInteresesDev()==null) || 
             (this.importeInteresesDev!=null &&
              this.importeInteresesDev.equals(other.getImporteInteresesDev())));
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
        if (getIdDevolucion() != null) {
            _hashCode += getIdDevolucion().hashCode();
        }
        if (getFechaDev() != null) {
            _hashCode += getFechaDev().hashCode();
        }
        if (getImportePrincipalDev() != null) {
            _hashCode += getImportePrincipalDev().hashCode();
        }
        if (getImporteInteresesDev() != null) {
            _hashCode += getImporteInteresesDev().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionEnvioDevolucion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>Envio>Devolucion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idDevolucion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdDevolucion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdDevolucion"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaDev");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "FechaDev"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importePrincipalDev");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ImportePrincipalDev"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importeInteresesDev");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ImporteInteresesDev"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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
