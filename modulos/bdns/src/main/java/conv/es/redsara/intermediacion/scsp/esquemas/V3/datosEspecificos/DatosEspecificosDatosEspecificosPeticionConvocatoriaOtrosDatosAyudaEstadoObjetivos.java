/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos  implements java.io.Serializable {
    private java.lang.String[] objetivo;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos(
           java.lang.String[] objetivo) {
           this.objetivo = objetivo;
    }


    /**
     * Gets the objetivo value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos.
     * 
     * @return objetivo
     */
    public java.lang.String[] getObjetivo() {
        return objetivo;
    }


    /**
     * Sets the objetivo value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos.
     * 
     * @param objetivo
     */
    public void setObjetivo(java.lang.String[] objetivo) {
        this.objetivo = objetivo;
    }

    public java.lang.String getObjetivo(int i) {
        return this.objetivo[i];
    }

    public void setObjetivo(int i, java.lang.String _value) {
        this.objetivo[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.objetivo==null && other.getObjetivo()==null) || 
             (this.objetivo!=null &&
              java.util.Arrays.equals(this.objetivo, other.getObjetivo())));
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
        if (getObjetivo() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getObjetivo());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getObjetivo(), i);
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
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>OtrosDatos>AyudaEstado>Objetivos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objetivo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Objetivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>OtrosDatos>AyudaEstado>Objetivos>Objetivo"));
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
