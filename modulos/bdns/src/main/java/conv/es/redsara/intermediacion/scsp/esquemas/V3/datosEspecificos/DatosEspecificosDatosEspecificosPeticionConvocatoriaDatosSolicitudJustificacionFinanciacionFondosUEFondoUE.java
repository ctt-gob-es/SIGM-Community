/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE  implements java.io.Serializable {
    private java.lang.String tipoFondo;

    private java.math.BigDecimal importeFondo;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE(
           java.lang.String tipoFondo,
           java.math.BigDecimal importeFondo) {
           this.tipoFondo = tipoFondo;
           this.importeFondo = importeFondo;
    }


    /**
     * Gets the tipoFondo value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE.
     * 
     * @return tipoFondo
     */
    public java.lang.String getTipoFondo() {
        return tipoFondo;
    }


    /**
     * Sets the tipoFondo value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE.
     * 
     * @param tipoFondo
     */
    public void setTipoFondo(java.lang.String tipoFondo) {
        this.tipoFondo = tipoFondo;
    }


    /**
     * Gets the importeFondo value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE.
     * 
     * @return importeFondo
     */
    public java.math.BigDecimal getImporteFondo() {
        return importeFondo;
    }


    /**
     * Sets the importeFondo value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE.
     * 
     * @param importeFondo
     */
    public void setImporteFondo(java.math.BigDecimal importeFondo) {
        this.importeFondo = importeFondo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tipoFondo==null && other.getTipoFondo()==null) || 
             (this.tipoFondo!=null &&
              this.tipoFondo.equals(other.getTipoFondo()))) &&
            ((this.importeFondo==null && other.getImporteFondo()==null) || 
             (this.importeFondo!=null &&
              this.importeFondo.equals(other.getImporteFondo())));
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
        if (getTipoFondo() != null) {
            _hashCode += getTipoFondo().hashCode();
        }
        if (getImporteFondo() != null) {
            _hashCode += getImporteFondo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>DatosSolicitudJustificacionFinanciacion>FondosUE>FondoUE"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoFondo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "TipoFondo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importeFondo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ImporteFondo"));
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
