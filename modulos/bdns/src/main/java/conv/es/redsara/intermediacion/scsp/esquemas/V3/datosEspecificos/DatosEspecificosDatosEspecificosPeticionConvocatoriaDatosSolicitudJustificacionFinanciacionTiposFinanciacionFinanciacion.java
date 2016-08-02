/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion  implements java.io.Serializable {
    private java.lang.String tipoFinanciacion;

    private java.math.BigDecimal importeFinanciacion;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion(
           java.lang.String tipoFinanciacion,
           java.math.BigDecimal importeFinanciacion) {
           this.tipoFinanciacion = tipoFinanciacion;
           this.importeFinanciacion = importeFinanciacion;
    }


    /**
     * Gets the tipoFinanciacion value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion.
     * 
     * @return tipoFinanciacion
     */
    public java.lang.String getTipoFinanciacion() {
        return tipoFinanciacion;
    }


    /**
     * Sets the tipoFinanciacion value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion.
     * 
     * @param tipoFinanciacion
     */
    public void setTipoFinanciacion(java.lang.String tipoFinanciacion) {
        this.tipoFinanciacion = tipoFinanciacion;
    }


    /**
     * Gets the importeFinanciacion value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion.
     * 
     * @return importeFinanciacion
     */
    public java.math.BigDecimal getImporteFinanciacion() {
        return importeFinanciacion;
    }


    /**
     * Sets the importeFinanciacion value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion.
     * 
     * @param importeFinanciacion
     */
    public void setImporteFinanciacion(java.math.BigDecimal importeFinanciacion) {
        this.importeFinanciacion = importeFinanciacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tipoFinanciacion==null && other.getTipoFinanciacion()==null) || 
             (this.tipoFinanciacion!=null &&
              this.tipoFinanciacion.equals(other.getTipoFinanciacion()))) &&
            ((this.importeFinanciacion==null && other.getImporteFinanciacion()==null) || 
             (this.importeFinanciacion!=null &&
              this.importeFinanciacion.equals(other.getImporteFinanciacion())));
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
        if (getTipoFinanciacion() != null) {
            _hashCode += getTipoFinanciacion().hashCode();
        }
        if (getImporteFinanciacion() != null) {
            _hashCode += getImporteFinanciacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionTiposFinanciacionFinanciacion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>DatosSolicitudJustificacionFinanciacion>TiposFinanciacion>Financiacion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoFinanciacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "TipoFinanciacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importeFinanciacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ImporteFinanciacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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
