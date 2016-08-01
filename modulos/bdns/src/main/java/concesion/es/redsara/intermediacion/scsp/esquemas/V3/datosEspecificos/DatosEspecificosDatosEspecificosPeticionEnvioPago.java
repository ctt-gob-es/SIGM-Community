/**
 * DatosEspecificosDatosEspecificosPeticionEnvioPago.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionEnvioPago  implements java.io.Serializable {
    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdPago idPago;

    private java.util.Date fechaPago;

    private java.math.BigDecimal importePagado;

    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioPagoRetencion retencion;

    public DatosEspecificosDatosEspecificosPeticionEnvioPago() {
    }

    public DatosEspecificosDatosEspecificosPeticionEnvioPago(
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdPago idPago,
           java.util.Date fechaPago,
           java.math.BigDecimal importePagado,
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioPagoRetencion retencion) {
           this.idPago = idPago;
           this.fechaPago = fechaPago;
           this.importePagado = importePagado;
           this.retencion = retencion;
    }


    /**
     * Gets the idPago value for this DatosEspecificosDatosEspecificosPeticionEnvioPago.
     * 
     * @return idPago
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdPago getIdPago() {
        return idPago;
    }


    /**
     * Sets the idPago value for this DatosEspecificosDatosEspecificosPeticionEnvioPago.
     * 
     * @param idPago
     */
    public void setIdPago(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.IdPago idPago) {
        this.idPago = idPago;
    }


    /**
     * Gets the fechaPago value for this DatosEspecificosDatosEspecificosPeticionEnvioPago.
     * 
     * @return fechaPago
     */
    public java.util.Date getFechaPago() {
        return fechaPago;
    }


    /**
     * Sets the fechaPago value for this DatosEspecificosDatosEspecificosPeticionEnvioPago.
     * 
     * @param fechaPago
     */
    public void setFechaPago(java.util.Date fechaPago) {
        this.fechaPago = fechaPago;
    }


    /**
     * Gets the importePagado value for this DatosEspecificosDatosEspecificosPeticionEnvioPago.
     * 
     * @return importePagado
     */
    public java.math.BigDecimal getImportePagado() {
        return importePagado;
    }


    /**
     * Sets the importePagado value for this DatosEspecificosDatosEspecificosPeticionEnvioPago.
     * 
     * @param importePagado
     */
    public void setImportePagado(java.math.BigDecimal importePagado) {
        this.importePagado = importePagado;
    }


    /**
     * Gets the retencion value for this DatosEspecificosDatosEspecificosPeticionEnvioPago.
     * 
     * @return retencion
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioPagoRetencion getRetencion() {
        return retencion;
    }


    /**
     * Sets the retencion value for this DatosEspecificosDatosEspecificosPeticionEnvioPago.
     * 
     * @param retencion
     */
    public void setRetencion(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionEnvioPagoRetencion retencion) {
        this.retencion = retencion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionEnvioPago)) return false;
        DatosEspecificosDatosEspecificosPeticionEnvioPago other = (DatosEspecificosDatosEspecificosPeticionEnvioPago) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idPago==null && other.getIdPago()==null) || 
             (this.idPago!=null &&
              this.idPago.equals(other.getIdPago()))) &&
            ((this.fechaPago==null && other.getFechaPago()==null) || 
             (this.fechaPago!=null &&
              this.fechaPago.equals(other.getFechaPago()))) &&
            ((this.importePagado==null && other.getImportePagado()==null) || 
             (this.importePagado!=null &&
              this.importePagado.equals(other.getImportePagado()))) &&
            ((this.retencion==null && other.getRetencion()==null) || 
             (this.retencion!=null &&
              this.retencion.equals(other.getRetencion())));
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
        if (getIdPago() != null) {
            _hashCode += getIdPago().hashCode();
        }
        if (getFechaPago() != null) {
            _hashCode += getFechaPago().hashCode();
        }
        if (getImportePagado() != null) {
            _hashCode += getImportePagado().hashCode();
        }
        if (getRetencion() != null) {
            _hashCode += getRetencion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionEnvioPago.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>Envio>Pago"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "IdPago"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">IdPago"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaPago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "FechaPago"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importePagado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ImportePagado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retencion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Retencion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Envio>Pago>Retencion"));
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
