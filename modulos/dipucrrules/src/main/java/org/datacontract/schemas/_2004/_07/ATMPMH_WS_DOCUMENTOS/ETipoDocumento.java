/**
 * ETipoDocumento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.ATMPMH_WS_DOCUMENTOS;

public class ETipoDocumento implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ETipoDocumento(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _CERTIFICADO = "CERTIFICADO";
    public static final java.lang.String _VOLANTE = "VOLANTE";
    public static final java.lang.String _CERTIFICADO_EMPADRONAMIENTO_HISTORICO = "CERTIFICADO_EMPADRONAMIENTO_HISTORICO";
    public static final java.lang.String _CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS = "CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS";
    public static final java.lang.String _CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS_CON_CAMBIO_DOMICILIO = "CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS_CON_CAMBIO_DOMICILIO";
    public static final java.lang.String _CERTIFICADO_CONVIVENCIA = "CERTIFICADO_CONVIVENCIA";
    public static final ETipoDocumento CERTIFICADO = new ETipoDocumento(_CERTIFICADO);
    public static final ETipoDocumento VOLANTE = new ETipoDocumento(_VOLANTE);
    public static final ETipoDocumento CERTIFICADO_EMPADRONAMIENTO_HISTORICO = new ETipoDocumento(_CERTIFICADO_EMPADRONAMIENTO_HISTORICO);
    public static final ETipoDocumento CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS = new ETipoDocumento(_CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS);
    public static final ETipoDocumento CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS_CON_CAMBIO_DOMICILIO = new ETipoDocumento(_CERTIFICADO_HISTORICO_MOVIMIENTOS_ALTAS_Y_BAJAS_CON_CAMBIO_DOMICILIO);
    public static final ETipoDocumento CERTIFICADO_CONVIVENCIA = new ETipoDocumento(_CERTIFICADO_CONVIVENCIA);
    public java.lang.String getValue() { return _value_;}
    public static ETipoDocumento fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ETipoDocumento enumeration = (ETipoDocumento)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ETipoDocumento fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ETipoDocumento.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/ATMPMH_WS_DOCUMENTOS", "eTipoDocumento"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
