/**
 * DatosGenericos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta;

public class DatosGenericos  implements java.io.Serializable {
    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Emisor emisor;

    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Solicitante solicitante;

    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Titular titular;

    private devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Transmision transmision;

    public DatosGenericos() {
    }

    public DatosGenericos(
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Emisor emisor,
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Solicitante solicitante,
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Titular titular,
           devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Transmision transmision) {
           this.emisor = emisor;
           this.solicitante = solicitante;
           this.titular = titular;
           this.transmision = transmision;
    }


    /**
     * Gets the emisor value for this DatosGenericos.
     * 
     * @return emisor
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Emisor getEmisor() {
        return emisor;
    }


    /**
     * Sets the emisor value for this DatosGenericos.
     * 
     * @param emisor
     */
    public void setEmisor(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Emisor emisor) {
        this.emisor = emisor;
    }


    /**
     * Gets the solicitante value for this DatosGenericos.
     * 
     * @return solicitante
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Solicitante getSolicitante() {
        return solicitante;
    }


    /**
     * Sets the solicitante value for this DatosGenericos.
     * 
     * @param solicitante
     */
    public void setSolicitante(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Solicitante solicitante) {
        this.solicitante = solicitante;
    }


    /**
     * Gets the titular value for this DatosGenericos.
     * 
     * @return titular
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Titular getTitular() {
        return titular;
    }


    /**
     * Sets the titular value for this DatosGenericos.
     * 
     * @param titular
     */
    public void setTitular(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Titular titular) {
        this.titular = titular;
    }


    /**
     * Gets the transmision value for this DatosGenericos.
     * 
     * @return transmision
     */
    public devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Transmision getTransmision() {
        return transmision;
    }


    /**
     * Sets the transmision value for this DatosGenericos.
     * 
     * @param transmision
     */
    public void setTransmision(devolucion.es.redsara.intermediacion.scsp.esquemas.V3.respuesta.Transmision transmision) {
        this.transmision = transmision;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosGenericos)) return false;
        DatosGenericos other = (DatosGenericos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.emisor==null && other.getEmisor()==null) || 
             (this.emisor!=null &&
              this.emisor.equals(other.getEmisor()))) &&
            ((this.solicitante==null && other.getSolicitante()==null) || 
             (this.solicitante!=null &&
              this.solicitante.equals(other.getSolicitante()))) &&
            ((this.titular==null && other.getTitular()==null) || 
             (this.titular!=null &&
              this.titular.equals(other.getTitular()))) &&
            ((this.transmision==null && other.getTransmision()==null) || 
             (this.transmision!=null &&
              this.transmision.equals(other.getTransmision())));
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
        if (getEmisor() != null) {
            _hashCode += getEmisor().hashCode();
        }
        if (getSolicitante() != null) {
            _hashCode += getSolicitante().hashCode();
        }
        if (getTitular() != null) {
            _hashCode += getTitular().hashCode();
        }
        if (getTransmision() != null) {
            _hashCode += getTransmision().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosGenericos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", ">DatosGenericos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("emisor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", "Emisor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", ">Emisor"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("solicitante");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", "Solicitante"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", ">Solicitante"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("titular");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", "Titular"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", ">Titular"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transmision");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", "Transmision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/respuesta", ">Transmision"));
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
