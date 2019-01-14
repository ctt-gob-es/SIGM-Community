/**
 * DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package devolucion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint  implements java.io.Serializable {
    private java.lang.String[] causas;

    public DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint() {
    }

    public DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint(
           java.lang.String[] causas) {
           this.causas = causas;
    }


    /**
     * Gets the causas value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint.
     * 
     * @return causas
     */
    public java.lang.String[] getCausas() {
        return causas;
    }


    /**
     * Sets the causas value for this DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint.
     * 
     * @param causas
     */
    public void setCausas(java.lang.String[] causas) {
        this.causas = causas;
    }

    public java.lang.String getCausas(int i) {
        return this.causas[i];
    }

    public void setCausas(int i, java.lang.String _value) {
        this.causas[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint)) return false;
        DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint other = (DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.causas==null && other.getCausas()==null) || 
             (this.causas!=null &&
              java.util.Arrays.equals(this.causas, other.getCausas())));
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
        if (getCausas() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCausas());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCausas(), i);
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
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionEnvioReintegroCausasReint.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Envio>Reintegro>CausasReint"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("causas");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Causas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>>DatosEspecificos>DatosEspecificosPeticion>Envio>Reintegro>CausasReint>Causas"));
        elemField.setNillable(false);
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
