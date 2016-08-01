/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua  implements java.io.Serializable {
    private java.lang.String tituloExtracto;

    private conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TextoExtracto textoExtracto;

    private conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PieFirmaExtracto pieFirmaExtracto;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua(
           java.lang.String tituloExtracto,
           conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TextoExtracto textoExtracto,
           conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PieFirmaExtracto pieFirmaExtracto) {
           this.tituloExtracto = tituloExtracto;
           this.textoExtracto = textoExtracto;
           this.pieFirmaExtracto = pieFirmaExtracto;
    }


    /**
     * Gets the tituloExtracto value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua.
     * 
     * @return tituloExtracto
     */
    public java.lang.String getTituloExtracto() {
        return tituloExtracto;
    }


    /**
     * Sets the tituloExtracto value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua.
     * 
     * @param tituloExtracto
     */
    public void setTituloExtracto(java.lang.String tituloExtracto) {
        this.tituloExtracto = tituloExtracto;
    }


    /**
     * Gets the textoExtracto value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua.
     * 
     * @return textoExtracto
     */
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TextoExtracto getTextoExtracto() {
        return textoExtracto;
    }


    /**
     * Sets the textoExtracto value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua.
     * 
     * @param textoExtracto
     */
    public void setTextoExtracto(conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TextoExtracto textoExtracto) {
        this.textoExtracto = textoExtracto;
    }


    /**
     * Gets the pieFirmaExtracto value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua.
     * 
     * @return pieFirmaExtracto
     */
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PieFirmaExtracto getPieFirmaExtracto() {
        return pieFirmaExtracto;
    }


    /**
     * Sets the pieFirmaExtracto value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua.
     * 
     * @param pieFirmaExtracto
     */
    public void setPieFirmaExtracto(conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PieFirmaExtracto pieFirmaExtracto) {
        this.pieFirmaExtracto = pieFirmaExtracto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tituloExtracto==null && other.getTituloExtracto()==null) || 
             (this.tituloExtracto!=null &&
              this.tituloExtracto.equals(other.getTituloExtracto()))) &&
            ((this.textoExtracto==null && other.getTextoExtracto()==null) || 
             (this.textoExtracto!=null &&
              this.textoExtracto.equals(other.getTextoExtracto()))) &&
            ((this.pieFirmaExtracto==null && other.getPieFirmaExtracto()==null) || 
             (this.pieFirmaExtracto!=null &&
              this.pieFirmaExtracto.equals(other.getPieFirmaExtracto())));
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
        if (getTituloExtracto() != null) {
            _hashCode += getTituloExtracto().hashCode();
        }
        if (getTextoExtracto() != null) {
            _hashCode += getTextoExtracto().hashCode();
        }
        if (getPieFirmaExtracto() != null) {
            _hashCode += getPieFirmaExtracto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaExtractoExtractoOtraLengua.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>Extracto>ExtractoOtraLengua"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tituloExtracto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "TituloExtracto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">TituloExtracto"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("textoExtracto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "TextoExtracto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">TextoExtracto"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pieFirmaExtracto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "PieFirmaExtracto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">PieFirmaExtracto"));
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
