/**
 * DatosEspecificosDatosEspecificosPeticionDatosGenerales.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionDatosGenerales  implements java.io.Serializable {
    private java.lang.String organoGestor;

    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TipoMovimiento tipoMovimiento;

    public DatosEspecificosDatosEspecificosPeticionDatosGenerales() {
    }

    public DatosEspecificosDatosEspecificosPeticionDatosGenerales(
           java.lang.String organoGestor,
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TipoMovimiento tipoMovimiento) {
           this.organoGestor = organoGestor;
           this.tipoMovimiento = tipoMovimiento;
    }


    /**
     * Gets the organoGestor value for this DatosEspecificosDatosEspecificosPeticionDatosGenerales.
     * 
     * @return organoGestor
     */
    public java.lang.String getOrganoGestor() {
        return organoGestor;
    }


    /**
     * Sets the organoGestor value for this DatosEspecificosDatosEspecificosPeticionDatosGenerales.
     * 
     * @param organoGestor
     */
    public void setOrganoGestor(java.lang.String organoGestor) {
        this.organoGestor = organoGestor;
    }


    /**
     * Gets the tipoMovimiento value for this DatosEspecificosDatosEspecificosPeticionDatosGenerales.
     * 
     * @return tipoMovimiento
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }


    /**
     * Sets the tipoMovimiento value for this DatosEspecificosDatosEspecificosPeticionDatosGenerales.
     * 
     * @param tipoMovimiento
     */
    public void setTipoMovimiento(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionDatosGenerales)) return false;
        DatosEspecificosDatosEspecificosPeticionDatosGenerales other = (DatosEspecificosDatosEspecificosPeticionDatosGenerales) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.organoGestor==null && other.getOrganoGestor()==null) || 
             (this.organoGestor!=null &&
              this.organoGestor.equals(other.getOrganoGestor()))) &&
            ((this.tipoMovimiento==null && other.getTipoMovimiento()==null) || 
             (this.tipoMovimiento!=null &&
              this.tipoMovimiento.equals(other.getTipoMovimiento())));
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
        if (getOrganoGestor() != null) {
            _hashCode += getOrganoGestor().hashCode();
        }
        if (getTipoMovimiento() != null) {
            _hashCode += getTipoMovimiento().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionDatosGenerales.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>DatosEspecificos>DatosEspecificosPeticion>DatosGenerales"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organoGestor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "OrganoGestor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">OrganoGestor"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoMovimiento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "TipoMovimiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">TipoMovimiento"));
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
