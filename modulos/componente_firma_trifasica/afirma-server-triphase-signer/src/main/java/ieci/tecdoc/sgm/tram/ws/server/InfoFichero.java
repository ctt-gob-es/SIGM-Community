/**
 * InfoFichero.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class InfoFichero  extends ieci.tecdoc.sgm.core.services.dto.RetornoServicio  implements java.io.Serializable {
    private java.util.Calendar fechaAlta;

    private ieci.tecdoc.sgm.tram.ws.server.Firma[] firmas;

    private java.lang.String nombre;

    public InfoFichero() {
    }

    public InfoFichero(
           java.lang.String errorCode,
           java.lang.String returnCode,
           java.util.Calendar fechaAlta,
           ieci.tecdoc.sgm.tram.ws.server.Firma[] firmas,
           java.lang.String nombre) {
        super(
            errorCode,
            returnCode);
        this.fechaAlta = fechaAlta;
        this.firmas = firmas;
        this.nombre = nombre;
    }


    /**
     * Gets the fechaAlta value for this InfoFichero.
     * 
     * @return fechaAlta
     */
    public java.util.Calendar getFechaAlta() {
        return fechaAlta;
    }


    /**
     * Sets the fechaAlta value for this InfoFichero.
     * 
     * @param fechaAlta
     */
    public void setFechaAlta(java.util.Calendar fechaAlta) {
        this.fechaAlta = fechaAlta;
    }


    /**
     * Gets the firmas value for this InfoFichero.
     * 
     * @return firmas
     */
    public ieci.tecdoc.sgm.tram.ws.server.Firma[] getFirmas() {
        return firmas;
    }


    /**
     * Sets the firmas value for this InfoFichero.
     * 
     * @param firmas
     */
    public void setFirmas(ieci.tecdoc.sgm.tram.ws.server.Firma[] firmas) {
        this.firmas = firmas;
    }


    /**
     * Gets the nombre value for this InfoFichero.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this InfoFichero.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InfoFichero)) return false;
        InfoFichero other = (InfoFichero) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.fechaAlta==null && other.getFechaAlta()==null) || 
             (this.fechaAlta!=null &&
              this.fechaAlta.equals(other.getFechaAlta()))) &&
            ((this.firmas==null && other.getFirmas()==null) || 
             (this.firmas!=null &&
              java.util.Arrays.equals(this.firmas, other.getFirmas()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre())));
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
        if (getFechaAlta() != null) {
            _hashCode += getFechaAlta().hashCode();
        }
        if (getFirmas() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFirmas());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFirmas(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoFichero.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoFichero"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaAlta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "fechaAlta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firmas");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "firmas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Firma"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
