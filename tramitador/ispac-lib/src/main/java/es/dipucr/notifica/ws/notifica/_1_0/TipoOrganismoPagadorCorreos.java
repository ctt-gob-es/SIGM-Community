/**
 * TipoOrganismoPagadorCorreos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notifica.ws.notifica._1_0;

public class TipoOrganismoPagadorCorreos  implements java.io.Serializable {
    private java.lang.String codigo_dir3;

    private java.lang.String numero_contrato_correos;

    private java.lang.String codigo_cliente_facturacion_correos;

    private java.util.Date fecha_vigencia;

    public TipoOrganismoPagadorCorreos() {
    }

    public TipoOrganismoPagadorCorreos(
           java.lang.String codigo_dir3,
           java.lang.String numero_contrato_correos,
           java.lang.String codigo_cliente_facturacion_correos,
           java.util.Date fecha_vigencia) {
           this.codigo_dir3 = codigo_dir3;
           this.numero_contrato_correos = numero_contrato_correos;
           this.codigo_cliente_facturacion_correos = codigo_cliente_facturacion_correos;
           this.fecha_vigencia = fecha_vigencia;
    }


    /**
     * Gets the codigo_dir3 value for this TipoOrganismoPagadorCorreos.
     * 
     * @return codigo_dir3
     */
    public java.lang.String getCodigo_dir3() {
        return codigo_dir3;
    }


    /**
     * Sets the codigo_dir3 value for this TipoOrganismoPagadorCorreos.
     * 
     * @param codigo_dir3
     */
    public void setCodigo_dir3(java.lang.String codigo_dir3) {
        this.codigo_dir3 = codigo_dir3;
    }


    /**
     * Gets the numero_contrato_correos value for this TipoOrganismoPagadorCorreos.
     * 
     * @return numero_contrato_correos
     */
    public java.lang.String getNumero_contrato_correos() {
        return numero_contrato_correos;
    }


    /**
     * Sets the numero_contrato_correos value for this TipoOrganismoPagadorCorreos.
     * 
     * @param numero_contrato_correos
     */
    public void setNumero_contrato_correos(java.lang.String numero_contrato_correos) {
        this.numero_contrato_correos = numero_contrato_correos;
    }


    /**
     * Gets the codigo_cliente_facturacion_correos value for this TipoOrganismoPagadorCorreos.
     * 
     * @return codigo_cliente_facturacion_correos
     */
    public java.lang.String getCodigo_cliente_facturacion_correos() {
        return codigo_cliente_facturacion_correos;
    }


    /**
     * Sets the codigo_cliente_facturacion_correos value for this TipoOrganismoPagadorCorreos.
     * 
     * @param codigo_cliente_facturacion_correos
     */
    public void setCodigo_cliente_facturacion_correos(java.lang.String codigo_cliente_facturacion_correos) {
        this.codigo_cliente_facturacion_correos = codigo_cliente_facturacion_correos;
    }


    /**
     * Gets the fecha_vigencia value for this TipoOrganismoPagadorCorreos.
     * 
     * @return fecha_vigencia
     */
    public java.util.Date getFecha_vigencia() {
        return fecha_vigencia;
    }


    /**
     * Sets the fecha_vigencia value for this TipoOrganismoPagadorCorreos.
     * 
     * @param fecha_vigencia
     */
    public void setFecha_vigencia(java.util.Date fecha_vigencia) {
        this.fecha_vigencia = fecha_vigencia;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TipoOrganismoPagadorCorreos)) return false;
        TipoOrganismoPagadorCorreos other = (TipoOrganismoPagadorCorreos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo_dir3==null && other.getCodigo_dir3()==null) || 
             (this.codigo_dir3!=null &&
              this.codigo_dir3.equals(other.getCodigo_dir3()))) &&
            ((this.numero_contrato_correos==null && other.getNumero_contrato_correos()==null) || 
             (this.numero_contrato_correos!=null &&
              this.numero_contrato_correos.equals(other.getNumero_contrato_correos()))) &&
            ((this.codigo_cliente_facturacion_correos==null && other.getCodigo_cliente_facturacion_correos()==null) || 
             (this.codigo_cliente_facturacion_correos!=null &&
              this.codigo_cliente_facturacion_correos.equals(other.getCodigo_cliente_facturacion_correos()))) &&
            ((this.fecha_vigencia==null && other.getFecha_vigencia()==null) || 
             (this.fecha_vigencia!=null &&
              this.fecha_vigencia.equals(other.getFecha_vigencia())));
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
        if (getCodigo_dir3() != null) {
            _hashCode += getCodigo_dir3().hashCode();
        }
        if (getNumero_contrato_correos() != null) {
            _hashCode += getNumero_contrato_correos().hashCode();
        }
        if (getCodigo_cliente_facturacion_correos() != null) {
            _hashCode += getCodigo_cliente_facturacion_correos().hashCode();
        }
        if (getFecha_vigencia() != null) {
            _hashCode += getFecha_vigencia().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TipoOrganismoPagadorCorreos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "tipoOrganismoPagadorCorreos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_dir3");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_dir3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero_contrato_correos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numero_contrato_correos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_cliente_facturacion_correos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_cliente_facturacion_correos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_vigencia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_vigencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
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
