/**
 * PieFirmaExtracto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class PieFirmaExtracto  implements java.io.Serializable {
    private java.util.Date fechaFirma;

    private java.lang.String lugarFirma;

    private java.lang.String firmante;

    public PieFirmaExtracto() {
    }

    public PieFirmaExtracto(
           java.util.Date fechaFirma,
           java.lang.String lugarFirma,
           java.lang.String firmante) {
           this.fechaFirma = fechaFirma;
           this.lugarFirma = lugarFirma;
           this.firmante = firmante;
    }


    /**
     * Gets the fechaFirma value for this PieFirmaExtracto.
     * 
     * @return fechaFirma
     */
    public java.util.Date getFechaFirma() {
        return fechaFirma;
    }


    /**
     * Sets the fechaFirma value for this PieFirmaExtracto.
     * 
     * @param fechaFirma
     */
    public void setFechaFirma(java.util.Date fechaFirma) {
        this.fechaFirma = fechaFirma;
    }


    /**
     * Gets the lugarFirma value for this PieFirmaExtracto.
     * 
     * @return lugarFirma
     */
    public java.lang.String getLugarFirma() {
        return lugarFirma;
    }


    /**
     * Sets the lugarFirma value for this PieFirmaExtracto.
     * 
     * @param lugarFirma
     */
    public void setLugarFirma(java.lang.String lugarFirma) {
        this.lugarFirma = lugarFirma;
    }


    /**
     * Gets the firmante value for this PieFirmaExtracto.
     * 
     * @return firmante
     */
    public java.lang.String getFirmante() {
        return firmante;
    }


    /**
     * Sets the firmante value for this PieFirmaExtracto.
     * 
     * @param firmante
     */
    public void setFirmante(java.lang.String firmante) {
        this.firmante = firmante;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PieFirmaExtracto)) return false;
        PieFirmaExtracto other = (PieFirmaExtracto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fechaFirma==null && other.getFechaFirma()==null) || 
             (this.fechaFirma!=null &&
              this.fechaFirma.equals(other.getFechaFirma()))) &&
            ((this.lugarFirma==null && other.getLugarFirma()==null) || 
             (this.lugarFirma!=null &&
              this.lugarFirma.equals(other.getLugarFirma()))) &&
            ((this.firmante==null && other.getFirmante()==null) || 
             (this.firmante!=null &&
              this.firmante.equals(other.getFirmante())));
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
        if (getFechaFirma() != null) {
            _hashCode += getFechaFirma().hashCode();
        }
        if (getLugarFirma() != null) {
            _hashCode += getLugarFirma().hashCode();
        }
        if (getFirmante() != null) {
            _hashCode += getFirmante().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PieFirmaExtracto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">PieFirmaExtracto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFirma");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "FechaFirma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lugarFirma");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "LugarFirma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firmante");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Firmante"));
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
