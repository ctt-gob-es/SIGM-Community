/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora  implements java.io.Serializable {
    private java.lang.String nomenclatura;

    private java.lang.String diarioOficialBR;

    private java.lang.String descripcionBR;

    private java.lang.String URLEspBR;

    private java.lang.String URLengBR;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora(
           java.lang.String nomenclatura,
           java.lang.String diarioOficialBR,
           java.lang.String descripcionBR,
           java.lang.String URLEspBR,
           java.lang.String URLengBR) {
           this.nomenclatura = nomenclatura;
           this.diarioOficialBR = diarioOficialBR;
           this.descripcionBR = descripcionBR;
           this.URLEspBR = URLEspBR;
           this.URLengBR = URLengBR;
    }


    /**
     * Gets the nomenclatura value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.
     * 
     * @return nomenclatura
     */
    public java.lang.String getNomenclatura() {
        return nomenclatura;
    }


    /**
     * Sets the nomenclatura value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.
     * 
     * @param nomenclatura
     */
    public void setNomenclatura(java.lang.String nomenclatura) {
        this.nomenclatura = nomenclatura;
    }


    /**
     * Gets the diarioOficialBR value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.
     * 
     * @return diarioOficialBR
     */
    public java.lang.String getDiarioOficialBR() {
        return diarioOficialBR;
    }


    /**
     * Sets the diarioOficialBR value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.
     * 
     * @param diarioOficialBR
     */
    public void setDiarioOficialBR(java.lang.String diarioOficialBR) {
        this.diarioOficialBR = diarioOficialBR;
    }


    /**
     * Gets the descripcionBR value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.
     * 
     * @return descripcionBR
     */
    public java.lang.String getDescripcionBR() {
        return descripcionBR;
    }


    /**
     * Sets the descripcionBR value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.
     * 
     * @param descripcionBR
     */
    public void setDescripcionBR(java.lang.String descripcionBR) {
        this.descripcionBR = descripcionBR;
    }


    /**
     * Gets the URLEspBR value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.
     * 
     * @return URLEspBR
     */
    public java.lang.String getURLEspBR() {
        return URLEspBR;
    }


    /**
     * Sets the URLEspBR value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.
     * 
     * @param URLEspBR
     */
    public void setURLEspBR(java.lang.String URLEspBR) {
        this.URLEspBR = URLEspBR;
    }


    /**
     * Gets the URLengBR value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.
     * 
     * @return URLengBR
     */
    public java.lang.String getURLengBR() {
        return URLengBR;
    }


    /**
     * Sets the URLengBR value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.
     * 
     * @param URLengBR
     */
    public void setURLengBR(java.lang.String URLengBR) {
        this.URLengBR = URLengBR;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nomenclatura==null && other.getNomenclatura()==null) || 
             (this.nomenclatura!=null &&
              this.nomenclatura.equals(other.getNomenclatura()))) &&
            ((this.diarioOficialBR==null && other.getDiarioOficialBR()==null) || 
             (this.diarioOficialBR!=null &&
              this.diarioOficialBR.equals(other.getDiarioOficialBR()))) &&
            ((this.descripcionBR==null && other.getDescripcionBR()==null) || 
             (this.descripcionBR!=null &&
              this.descripcionBR.equals(other.getDescripcionBR()))) &&
            ((this.URLEspBR==null && other.getURLEspBR()==null) || 
             (this.URLEspBR!=null &&
              this.URLEspBR.equals(other.getURLEspBR()))) &&
            ((this.URLengBR==null && other.getURLengBR()==null) || 
             (this.URLengBR!=null &&
              this.URLengBR.equals(other.getURLengBR())));
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
        if (getNomenclatura() != null) {
            _hashCode += getNomenclatura().hashCode();
        }
        if (getDiarioOficialBR() != null) {
            _hashCode += getDiarioOficialBR().hashCode();
        }
        if (getDescripcionBR() != null) {
            _hashCode += getDescripcionBR().hashCode();
        }
        if (getURLEspBR() != null) {
            _hashCode += getURLEspBR().hashCode();
        }
        if (getURLengBR() != null) {
            _hashCode += getURLengBR().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosBaseReguladora.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>DatosBaseReguladora"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomenclatura");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Nomenclatura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("diarioOficialBR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DiarioOficialBR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionBR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DescripcionBR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("URLEspBR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "URLEspBR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("URLengBR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "URLengBR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
