/**
 * ExpedientStateData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.resultadoBeans;

public class ExpedientStateData  implements java.io.Serializable {
    private es.dipucr.contratacion.resultadoBeans.Anuncio[] anuncios;

    public ExpedientStateData() {
    }

    public ExpedientStateData(
           es.dipucr.contratacion.resultadoBeans.Anuncio[] anuncios) {
           this.anuncios = anuncios;
    }


    /**
     * Gets the anuncios value for this ExpedientStateData.
     * 
     * @return anuncios
     */
    public es.dipucr.contratacion.resultadoBeans.Anuncio[] getAnuncios() {
        return anuncios;
    }


    /**
     * Sets the anuncios value for this ExpedientStateData.
     * 
     * @param anuncios
     */
    public void setAnuncios(es.dipucr.contratacion.resultadoBeans.Anuncio[] anuncios) {
        this.anuncios = anuncios;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExpedientStateData)) return false;
        ExpedientStateData other = (ExpedientStateData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.anuncios==null && other.getAnuncios()==null) || 
             (this.anuncios!=null &&
              java.util.Arrays.equals(this.anuncios, other.getAnuncios())));
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
        if (getAnuncios() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAnuncios());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAnuncios(), i);
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
        new org.apache.axis.description.TypeDesc(ExpedientStateData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "ExpedientStateData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anuncios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "anuncios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Anuncio"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
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
