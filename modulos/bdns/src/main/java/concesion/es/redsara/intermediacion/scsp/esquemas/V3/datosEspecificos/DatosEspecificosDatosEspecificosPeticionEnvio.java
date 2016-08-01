/**
 * DatosEspecificosDatosEspecificosPeticionEnvio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionEnvio  implements java.io.Serializable {
    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioConcesion concesion;

    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioPago pago;

    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioProyecto proyecto;

    public DatosEspecificosDatosEspecificosPeticionEnvio() {
    }

    public DatosEspecificosDatosEspecificosPeticionEnvio(
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioConcesion concesion,
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioPago pago,
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioProyecto proyecto) {
           this.concesion = concesion;
           this.pago = pago;
           this.proyecto = proyecto;
    }


    /**
     * Gets the concesion value for this DatosEspecificosDatosEspecificosPeticionEnvio.
     * 
     * @return concesion
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioConcesion getConcesion() {
        return concesion;
    }


    /**
     * Sets the concesion value for this DatosEspecificosDatosEspecificosPeticionEnvio.
     * 
     * @param concesion
     */
    public void setConcesion(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioConcesion concesion) {
        this.concesion = concesion;
    }


    /**
     * Gets the pago value for this DatosEspecificosDatosEspecificosPeticionEnvio.
     * 
     * @return pago
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioPago getPago() {
        return pago;
    }


    /**
     * Sets the pago value for this DatosEspecificosDatosEspecificosPeticionEnvio.
     * 
     * @param pago
     */
    public void setPago(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioPago pago) {
        this.pago = pago;
    }


    /**
     * Gets the proyecto value for this DatosEspecificosDatosEspecificosPeticionEnvio.
     * 
     * @return proyecto
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioProyecto getProyecto() {
        return proyecto;
    }


    /**
     * Sets the proyecto value for this DatosEspecificosDatosEspecificosPeticionEnvio.
     * 
     * @param proyecto
     */
    public void setProyecto(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioProyecto proyecto) {
        this.proyecto = proyecto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionEnvio)) return false;
        DatosEspecificosDatosEspecificosPeticionEnvio other = (DatosEspecificosDatosEspecificosPeticionEnvio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.concesion==null && other.getConcesion()==null) || 
             (this.concesion!=null &&
              this.concesion.equals(other.getConcesion()))) &&
            ((this.pago==null && other.getPago()==null) || 
             (this.pago!=null &&
              this.pago.equals(other.getPago()))) &&
            ((this.proyecto==null && other.getProyecto()==null) || 
             (this.proyecto!=null &&
              this.proyecto.equals(other.getProyecto())));
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
        if (getConcesion() != null) {
            _hashCode += getConcesion().hashCode();
        }
        if (getPago() != null) {
            _hashCode += getPago().hashCode();
        }
        if (getProyecto() != null) {
            _hashCode += getProyecto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionEnvio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>DatosEspecificos>DatosEspecificosPeticion>Envio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("concesion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Concesion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>Envio>Concesion"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Pago"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>Envio>Pago"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("proyecto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Proyecto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>Envio>Proyecto"));
        elemField.setMinOccurs(0);
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
