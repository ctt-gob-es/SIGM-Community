/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento  implements java.io.Serializable {
    private java.lang.String descripcionOtro;

    private java.lang.String nombre;

    private byte[] fichero;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento(
           java.lang.String descripcionOtro,
           java.lang.String nombre,
           byte[] fichero) {
           this.descripcionOtro = descripcionOtro;
           this.nombre = nombre;
           this.fichero = fichero;
    }


    /**
     * Gets the descripcionOtro value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento.
     * 
     * @return descripcionOtro
     */
    public java.lang.String getDescripcionOtro() {
        return descripcionOtro;
    }


    /**
     * Sets the descripcionOtro value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento.
     * 
     * @param descripcionOtro
     */
    public void setDescripcionOtro(java.lang.String descripcionOtro) {
        this.descripcionOtro = descripcionOtro;
    }


    /**
     * Gets the nombre value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the fichero value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento.
     * 
     * @return fichero
     */
    public byte[] getFichero() {
        return fichero;
    }


    /**
     * Sets the fichero value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento.
     * 
     * @param fichero
     */
    public void setFichero(byte[] fichero) {
        this.fichero = fichero;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.descripcionOtro==null && other.getDescripcionOtro()==null) || 
             (this.descripcionOtro!=null &&
              this.descripcionOtro.equals(other.getDescripcionOtro()))) &&
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
        if (getDescripcionOtro() != null) {
            _hashCode += getDescripcionOtro().hashCode();
        }
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
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDocumentosOtroDocumento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>OtrosDocumentos>OtroDocumento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionOtro");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DescripcionOtro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
