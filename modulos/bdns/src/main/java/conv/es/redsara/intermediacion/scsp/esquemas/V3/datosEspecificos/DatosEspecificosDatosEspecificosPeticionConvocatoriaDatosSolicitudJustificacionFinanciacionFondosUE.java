/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE  implements java.io.Serializable {
    private conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE[] fondoUE;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE(
           conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE[] fondoUE) {
           this.fondoUE = fondoUE;
    }


    /**
     * Gets the fondoUE value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE.
     * 
     * @return fondoUE
     */
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE[] getFondoUE() {
        return fondoUE;
    }


    /**
     * Sets the fondoUE value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE.
     * 
     * @param fondoUE
     */
    public void setFondoUE(conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE[] fondoUE) {
        this.fondoUE = fondoUE;
    }

    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE getFondoUE(int i) {
        return this.fondoUE[i];
    }

    public void setFondoUE(int i, conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUEFondoUE _value) {
        this.fondoUE[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fondoUE==null && other.getFondoUE()==null) || 
             (this.fondoUE!=null &&
              java.util.Arrays.equals(this.fondoUE, other.getFondoUE())));
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
        if (getFondoUE() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFondoUE());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFondoUE(), i);
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
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosSolicitudJustificacionFinanciacionFondosUE.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>DatosSolicitudJustificacionFinanciacion>FondosUE"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fondoUE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "FondoUE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>DatosSolicitudJustificacionFinanciacion>FondosUE>FondoUE"));
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
