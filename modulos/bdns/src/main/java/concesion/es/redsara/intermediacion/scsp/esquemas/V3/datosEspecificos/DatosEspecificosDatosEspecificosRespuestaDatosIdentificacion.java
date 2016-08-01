/**
 * DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion  implements java.io.Serializable {
    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion;

    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdPago idPago;

    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdProyecto idProyecto;

    public DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion() {
    }

    public DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion(
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion,
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdPago idPago,
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdProyecto idProyecto) {
           this.idConcesion = idConcesion;
           this.idPago = idPago;
           this.idProyecto = idProyecto;
    }


    /**
     * Gets the idConcesion value for this DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.
     * 
     * @return idConcesion
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion getIdConcesion() {
        return idConcesion;
    }


    /**
     * Sets the idConcesion value for this DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.
     * 
     * @param idConcesion
     */
    public void setIdConcesion(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdConcesion idConcesion) {
        this.idConcesion = idConcesion;
    }


    /**
     * Gets the idPago value for this DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.
     * 
     * @return idPago
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdPago getIdPago() {
        return idPago;
    }


    /**
     * Sets the idPago value for this DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.
     * 
     * @param idPago
     */
    public void setIdPago(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdPago idPago) {
        this.idPago = idPago;
    }


    /**
     * Gets the idProyecto value for this DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.
     * 
     * @return idProyecto
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdProyecto getIdProyecto() {
        return idProyecto;
    }


    /**
     * Sets the idProyecto value for this DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.
     * 
     * @param idProyecto
     */
    public void setIdProyecto(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdProyecto idProyecto) {
        this.idProyecto = idProyecto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion)) return false;
        DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion other = (DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idConcesion==null && other.getIdConcesion()==null) || 
             (this.idConcesion!=null &&
              this.idConcesion.equals(other.getIdConcesion()))) &&
            ((this.idPago==null && other.getIdPago()==null) || 
             (this.idPago!=null &&
              this.idPago.equals(other.getIdPago()))) &&
            ((this.idProyecto==null && other.getIdProyecto()==null) || 
             (this.idProyecto!=null &&
              this.idProyecto.equals(other.getIdProyecto())));
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
        if (getIdConcesion() != null) {
            _hashCode += getIdConcesion().hashCode();
        }
        if (getIdPago() != null) {
            _hashCode += getIdPago().hashCode();
        }
        if (getIdProyecto() != null) {
            _hashCode += getIdProyecto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosRespuestaDatosIdentificacion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>DatosEspecificos>DatosEspecificosRespuesta>DatosIdentificacion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idConcesion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdConcesion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdConcesion"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdPago"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdPago"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProyecto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdProyecto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdProyecto"));
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
