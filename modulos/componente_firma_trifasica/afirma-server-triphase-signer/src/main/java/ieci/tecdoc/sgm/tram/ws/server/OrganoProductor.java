/**
 * OrganoProductor.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class OrganoProductor  implements java.io.Serializable {
    private java.lang.String id;

    private java.util.Calendar inicioProduccion;

    public OrganoProductor() {
    }

    public OrganoProductor(
           java.lang.String id,
           java.util.Calendar inicioProduccion) {
           this.id = id;
           this.inicioProduccion = inicioProduccion;
    }


    /**
     * Gets the id value for this OrganoProductor.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this OrganoProductor.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the inicioProduccion value for this OrganoProductor.
     * 
     * @return inicioProduccion
     */
    public java.util.Calendar getInicioProduccion() {
        return inicioProduccion;
    }


    /**
     * Sets the inicioProduccion value for this OrganoProductor.
     * 
     * @param inicioProduccion
     */
    public void setInicioProduccion(java.util.Calendar inicioProduccion) {
        this.inicioProduccion = inicioProduccion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OrganoProductor)) return false;
        OrganoProductor other = (OrganoProductor) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.inicioProduccion==null && other.getInicioProduccion()==null) || 
             (this.inicioProduccion!=null &&
              this.inicioProduccion.equals(other.getInicioProduccion())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getInicioProduccion() != null) {
            _hashCode += getInicioProduccion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OrganoProductor.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "OrganoProductor"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inicioProduccion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "inicioProduccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
