/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores  implements java.io.Serializable {
    private java.lang.String[] sector;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores(
           java.lang.String[] sector) {
           this.sector = sector;
    }


    /**
     * Gets the sector value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores.
     * 
     * @return sector
     */
    public java.lang.String[] getSector() {
        return sector;
    }


    /**
     * Sets the sector value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores.
     * 
     * @param sector
     */
    public void setSector(java.lang.String[] sector) {
        this.sector = sector;
    }

    public java.lang.String getSector(int i) {
        return this.sector[i];
    }

    public void setSector(int i, java.lang.String _value) {
        this.sector[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sector==null && other.getSector()==null) || 
             (this.sector!=null &&
              java.util.Arrays.equals(this.sector, other.getSector())));
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
        if (getSector() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSector());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSector(), i);
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
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosSectores.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>OtrosDatos>Sectores"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sector");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Sector"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>OtrosDatos>Sectores>Sector"));
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
