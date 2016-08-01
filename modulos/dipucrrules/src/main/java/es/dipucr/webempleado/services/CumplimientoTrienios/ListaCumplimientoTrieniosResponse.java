/**
 * ListaCumplimientoTrieniosResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.services.CumplimientoTrienios;

public class ListaCumplimientoTrieniosResponse  implements java.io.Serializable {
    private java.lang.Object[] clistaCumplimientoTrienios;

    public ListaCumplimientoTrieniosResponse() {
    }

    public ListaCumplimientoTrieniosResponse(
           java.lang.Object[] clistaCumplimientoTrienios) {
           this.clistaCumplimientoTrienios = clistaCumplimientoTrienios;
    }


    /**
     * Gets the clistaCumplimientoTrienios value for this ListaCumplimientoTrieniosResponse.
     * 
     * @return clistaCumplimientoTrienios
     */
    public java.lang.Object[] getClistaCumplimientoTrienios() {
        return clistaCumplimientoTrienios;
    }


    /**
     * Sets the clistaCumplimientoTrienios value for this ListaCumplimientoTrieniosResponse.
     * 
     * @param clistaCumplimientoTrienios
     */
    public void setClistaCumplimientoTrienios(java.lang.Object[] clistaCumplimientoTrienios) {
        this.clistaCumplimientoTrienios = clistaCumplimientoTrienios;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListaCumplimientoTrieniosResponse)) return false;
        ListaCumplimientoTrieniosResponse other = (ListaCumplimientoTrieniosResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.clistaCumplimientoTrienios==null && other.getClistaCumplimientoTrienios()==null) || 
             (this.clistaCumplimientoTrienios!=null &&
              java.util.Arrays.equals(this.clistaCumplimientoTrienios, other.getClistaCumplimientoTrienios())));
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
        if (getClistaCumplimientoTrienios() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getClistaCumplimientoTrienios());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getClistaCumplimientoTrienios(), i);
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
        new org.apache.axis.description.TypeDesc(ListaCumplimientoTrieniosResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", ">listaCumplimientoTrieniosResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clistaCumplimientoTrienios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", "clistaCumplimientoTrienios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://CumplimientoTrienios.services.webempleado.dipucr.es", "item"));
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
