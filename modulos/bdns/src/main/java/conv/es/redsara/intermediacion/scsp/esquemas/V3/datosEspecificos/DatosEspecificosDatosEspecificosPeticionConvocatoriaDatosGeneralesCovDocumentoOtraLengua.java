/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua  implements java.io.Serializable {
    private java.lang.String nombre;

    private byte[] fichero;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua(
           java.lang.String nombre,
           byte[] fichero) {
           this.nombre = nombre;
           this.fichero = fichero;
    }


    /**
     * Gets the nombre value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the fichero value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua.
     * 
     * @return fichero
     */
    public byte[] getFichero() {
        return fichero;
    }


    /**
     * Sets the fichero value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua.
     * 
     * @param fichero
     */
    public void setFichero(byte[] fichero) {
        this.fichero = fichero;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.fichero==null && other.getFichero()==null) || 
             (this.fichero!=null &&
              java.util.Arrays.equals(this.fichero, other.getFichero())));
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
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getFichero() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFichero());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFichero(), i);
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
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaDatosGeneralesCovDocumentoOtraLengua.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>DatosGeneralesCov>DocumentoOtraLengua"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">Nombre"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fichero");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Fichero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
