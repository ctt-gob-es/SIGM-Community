/**
 * DatosAnualidades.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosAnualidades  implements java.io.Serializable {
    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidades[] anualidades;

    public DatosAnualidades() {
    }

    public DatosAnualidades(
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidades[] anualidades) {
           this.anualidades = anualidades;
    }


    /**
     * Gets the anualidades value for this DatosAnualidades.
     * 
     * @return anualidades
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidades[] getAnualidades() {
        return anualidades;
    }


    /**
     * Sets the anualidades value for this DatosAnualidades.
     * 
     * @param anualidades
     */
    public void setAnualidades(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidades[] anualidades) {
        this.anualidades = anualidades;
    }

    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidades getAnualidades(int i) {
        return this.anualidades[i];
    }

    public void setAnualidades(int i, concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidades _value) {
        this.anualidades[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosAnualidades)) return false;
        DatosAnualidades other = (DatosAnualidades) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.anualidades==null && other.getAnualidades()==null) || 
             (this.anualidades!=null &&
              java.util.Arrays.equals(this.anualidades, other.getAnualidades())));
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
        if (getAnualidades() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAnualidades());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAnualidades(), i);
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
        new org.apache.axis.description.TypeDesc(DatosAnualidades.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">DatosAnualidades"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anualidades");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Anualidades"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>DatosAnualidades>Anualidades"));
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
