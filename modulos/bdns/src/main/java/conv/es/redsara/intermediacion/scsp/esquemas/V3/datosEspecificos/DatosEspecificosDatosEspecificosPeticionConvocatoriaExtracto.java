/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto  implements java.io.Serializable {
    private java.lang.String diarioOficial;

    private conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoCastellano extractoCastellano;

    private conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua extractoOtraLengua;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto(
           java.lang.String diarioOficial,
           conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoCastellano extractoCastellano,
           conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua extractoOtraLengua) {
           this.diarioOficial = diarioOficial;
           this.extractoCastellano = extractoCastellano;
           this.extractoOtraLengua = extractoOtraLengua;
    }


    /**
     * Gets the diarioOficial value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto.
     * 
     * @return diarioOficial
     */
    public java.lang.String getDiarioOficial() {
        return diarioOficial;
    }


    /**
     * Sets the diarioOficial value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto.
     * 
     * @param diarioOficial
     */
    public void setDiarioOficial(java.lang.String diarioOficial) {
        this.diarioOficial = diarioOficial;
    }


    /**
     * Gets the extractoCastellano value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto.
     * 
     * @return extractoCastellano
     */
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoCastellano getExtractoCastellano() {
        return extractoCastellano;
    }


    /**
     * Sets the extractoCastellano value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto.
     * 
     * @param extractoCastellano
     */
    public void setExtractoCastellano(conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoCastellano extractoCastellano) {
        this.extractoCastellano = extractoCastellano;
    }


    /**
     * Gets the extractoOtraLengua value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto.
     * 
     * @return extractoOtraLengua
     */
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua getExtractoOtraLengua() {
        return extractoOtraLengua;
    }


    /**
     * Sets the extractoOtraLengua value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto.
     * 
     * @param extractoOtraLengua
     */
    public void setExtractoOtraLengua(conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua extractoOtraLengua) {
        this.extractoOtraLengua = extractoOtraLengua;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.diarioOficial==null && other.getDiarioOficial()==null) || 
             (this.diarioOficial!=null &&
              this.diarioOficial.equals(other.getDiarioOficial()))) &&
            ((this.extractoCastellano==null && other.getExtractoCastellano()==null) || 
             (this.extractoCastellano!=null &&
              this.extractoCastellano.equals(other.getExtractoCastellano()))) &&
            ((this.extractoOtraLengua==null && other.getExtractoOtraLengua()==null) || 
             (this.extractoOtraLengua!=null &&
              this.extractoOtraLengua.equals(other.getExtractoOtraLengua())));
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
        if (getDiarioOficial() != null) {
            _hashCode += getDiarioOficial().hashCode();
        }
        if (getExtractoCastellano() != null) {
            _hashCode += getExtractoCastellano().hashCode();
        }
        if (getExtractoOtraLengua() != null) {
            _hashCode += getExtractoOtraLengua().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaExtracto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>Extracto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("diarioOficial");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DiarioOficial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extractoCastellano");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ExtractoCastellano"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>Extracto>ExtractoCastellano"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extractoOtraLengua");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ExtractoOtraLengua"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>Extracto>ExtractoOtraLengua"));
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
