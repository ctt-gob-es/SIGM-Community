/**
 * ListaIdentificadores.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class ListaIdentificadores  extends ieci.tecdoc.sgm.core.services.dto.RetornoServicio  implements java.io.Serializable {
    private java.lang.String[] identificadores;

    public ListaIdentificadores() {
    }

    public ListaIdentificadores(
           java.lang.String errorCode,
           java.lang.String returnCode,
           java.lang.String[] identificadores) {
        super(
            errorCode,
            returnCode);
        this.identificadores = identificadores;
    }


    /**
     * Gets the identificadores value for this ListaIdentificadores.
     * 
     * @return identificadores
     */
    public java.lang.String[] getIdentificadores() {
        return identificadores;
    }


    /**
     * Sets the identificadores value for this ListaIdentificadores.
     * 
     * @param identificadores
     */
    public void setIdentificadores(java.lang.String[] identificadores) {
        this.identificadores = identificadores;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListaIdentificadores)) return false;
        ListaIdentificadores other = (ListaIdentificadores) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.identificadores==null && other.getIdentificadores()==null) || 
             (this.identificadores!=null &&
              java.util.Arrays.equals(this.identificadores, other.getIdentificadores())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getIdentificadores() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdentificadores());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdentificadores(), i);
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
        new org.apache.axis.description.TypeDesc(ListaIdentificadores.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "ListaIdentificadores"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificadores");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "identificadores"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item"));
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
