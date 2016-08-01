/**
 * ListaAvisos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.boe.www.ServicioNotificaciones;


/**
 * Lista de avisos que se han producido durante el proceso de un anuncio
 */
public class ListaAvisos  implements java.io.Serializable {
    private es.boe.www.ServicioNotificaciones.Aviso[] aviso;

    public ListaAvisos() {
    }

    public ListaAvisos(
           es.boe.www.ServicioNotificaciones.Aviso[] aviso) {
           this.aviso = aviso;
    }


    /**
     * Gets the aviso value for this ListaAvisos.
     * 
     * @return aviso
     */
    public es.boe.www.ServicioNotificaciones.Aviso[] getAviso() {
        return aviso;
    }


    /**
     * Sets the aviso value for this ListaAvisos.
     * 
     * @param aviso
     */
    public void setAviso(es.boe.www.ServicioNotificaciones.Aviso[] aviso) {
        this.aviso = aviso;
    }

    public es.boe.www.ServicioNotificaciones.Aviso getAviso(int i) {
        return this.aviso[i];
    }

    public void setAviso(int i, es.boe.www.ServicioNotificaciones.Aviso _value) {
        this.aviso[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListaAvisos)) return false;
        ListaAvisos other = (ListaAvisos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.aviso==null && other.getAviso()==null) || 
             (this.aviso!=null &&
              java.util.Arrays.equals(this.aviso, other.getAviso())));
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
        if (getAviso() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAviso());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAviso(), i);
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
        new org.apache.axis.description.TypeDesc(ListaAvisos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.boe.es/ServicioNotificaciones/", "ListaAvisos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aviso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "aviso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.boe.es/ServicioNotificaciones/", "Aviso"));
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
