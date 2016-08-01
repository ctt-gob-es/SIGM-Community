/**
 * RecuperarEstadosNotificacionesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.jccm.notificador.ws;

public class RecuperarEstadosNotificacionesResponse  implements java.io.Serializable {
    private es.dipucr.notificador.model.NotificacionWS recuperarEstadosNotificacionesReturn;

    public RecuperarEstadosNotificacionesResponse() {
    }

    public RecuperarEstadosNotificacionesResponse(
           es.dipucr.notificador.model.NotificacionWS recuperarEstadosNotificacionesReturn) {
           this.recuperarEstadosNotificacionesReturn = recuperarEstadosNotificacionesReturn;
    }


    /**
     * Gets the recuperarEstadosNotificacionesReturn value for this RecuperarEstadosNotificacionesResponse.
     * 
     * @return recuperarEstadosNotificacionesReturn
     */
    public es.dipucr.notificador.model.NotificacionWS getRecuperarEstadosNotificacionesReturn() {
        return recuperarEstadosNotificacionesReturn;
    }


    /**
     * Sets the recuperarEstadosNotificacionesReturn value for this RecuperarEstadosNotificacionesResponse.
     * 
     * @param recuperarEstadosNotificacionesReturn
     */
    public void setRecuperarEstadosNotificacionesReturn(es.dipucr.notificador.model.NotificacionWS recuperarEstadosNotificacionesReturn) {
        this.recuperarEstadosNotificacionesReturn = recuperarEstadosNotificacionesReturn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RecuperarEstadosNotificacionesResponse)) return false;
        RecuperarEstadosNotificacionesResponse other = (RecuperarEstadosNotificacionesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.recuperarEstadosNotificacionesReturn==null && other.getRecuperarEstadosNotificacionesReturn()==null) || 
             (this.recuperarEstadosNotificacionesReturn!=null &&
              this.recuperarEstadosNotificacionesReturn.equals(other.getRecuperarEstadosNotificacionesReturn())));
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
        if (getRecuperarEstadosNotificacionesReturn() != null) {
            _hashCode += getRecuperarEstadosNotificacionesReturn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RecuperarEstadosNotificacionesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", ">recuperarEstadosNotificacionesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recuperarEstadosNotificacionesReturn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "recuperarEstadosNotificacionesReturn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "NotificacionWS"));
        elemField.setNillable(false);
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
