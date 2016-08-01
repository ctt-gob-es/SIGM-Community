/**
 * DatosEspecificosDatosEspecificosPeticion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

import es.dipucr.bdns.common.DatosEspecificosDatosEspecificosPeticionAbstracta;

public class DatosEspecificosDatosEspecificosPeticion extends DatosEspecificosDatosEspecificosPeticionAbstracta  implements java.io.Serializable {
    private conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosGenerales datosGenerales;

    private conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoria convocatoria;

    public DatosEspecificosDatosEspecificosPeticion() {
    }

    public DatosEspecificosDatosEspecificosPeticion(
           conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosGenerales datosGenerales,
           conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoria convocatoria) {
           this.datosGenerales = datosGenerales;
           this.convocatoria = convocatoria;
    }


    /**
     * Gets the datosGenerales value for this DatosEspecificosDatosEspecificosPeticion.
     * 
     * @return datosGenerales
     */
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosGenerales getDatosGenerales() {
        return datosGenerales;
    }


    /**
     * Sets the datosGenerales value for this DatosEspecificosDatosEspecificosPeticion.
     * 
     * @param datosGenerales
     */
    public void setDatosGenerales(conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosGenerales datosGenerales) {
        this.datosGenerales = datosGenerales;
    }


    /**
     * Gets the convocatoria value for this DatosEspecificosDatosEspecificosPeticion.
     * 
     * @return convocatoria
     */
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoria getConvocatoria() {
        return convocatoria;
    }


    /**
     * Sets the convocatoria value for this DatosEspecificosDatosEspecificosPeticion.
     * 
     * @param convocatoria
     */
    public void setConvocatoria(conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoria convocatoria) {
        this.convocatoria = convocatoria;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticion)) return false;
        DatosEspecificosDatosEspecificosPeticion other = (DatosEspecificosDatosEspecificosPeticion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.datosGenerales==null && other.getDatosGenerales()==null) || 
             (this.datosGenerales!=null &&
              this.datosGenerales.equals(other.getDatosGenerales()))) &&
            ((this.convocatoria==null && other.getConvocatoria()==null) || 
             (this.convocatoria!=null &&
              this.convocatoria.equals(other.getConvocatoria())));
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
        if (getDatosGenerales() != null) {
            _hashCode += getDatosGenerales().hashCode();
        }
        if (getConvocatoria() != null) {
            _hashCode += getConvocatoria().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">DatosEspecificos>DatosEspecificosPeticion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosGenerales");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DatosGenerales"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>DatosEspecificos>DatosEspecificosPeticion>DatosGenerales"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("convocatoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Convocatoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>DatosEspecificos>DatosEspecificosPeticion>Convocatoria"));
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
