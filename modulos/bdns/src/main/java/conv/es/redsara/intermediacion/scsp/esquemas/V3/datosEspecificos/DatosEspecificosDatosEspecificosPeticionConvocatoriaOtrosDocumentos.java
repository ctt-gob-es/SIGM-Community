/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos  implements java.io.Serializable {
    private conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento[] otroDocumento;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos(
           conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento[] otroDocumento) {
           this.otroDocumento = otroDocumento;
    }


    /**
     * Gets the otroDocumento value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos.
     * 
     * @return otroDocumento
     */
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento[] getOtroDocumento() {
        return otroDocumento;
    }


    /**
     * Sets the otroDocumento value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos.
     * 
     * @param otroDocumento
     */
    public void setOtroDocumento(conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento[] otroDocumento) {
        this.otroDocumento = otroDocumento;
    }

    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento getOtroDocumento(int i) {
        return this.otroDocumento[i];
    }

    public void setOtroDocumento(int i, conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento _value) {
        this.otroDocumento[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.otroDocumento==null && other.getOtroDocumento()==null) || 
             (this.otroDocumento!=null &&
              java.util.Arrays.equals(this.otroDocumento, other.getOtroDocumento())));
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
        if (getOtroDocumento() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOtroDocumento());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOtroDocumento(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>OtrosDocumentos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("otroDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "OtroDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>OtrosDocumentos>OtroDocumento"));
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
