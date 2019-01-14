/**
 * DatosEspecificosDatosEspecificosPeticionDatosPersonales.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionDatosPersonales  implements java.io.Serializable {
    private datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosIdentificacion datosIdentificacion;

    private datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion datosDenominacion;

    private datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio datosDomicilio;

    private datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.ActividadEconomica actividadEconomica;

    public DatosEspecificosDatosEspecificosPeticionDatosPersonales() {
    }

    public DatosEspecificosDatosEspecificosPeticionDatosPersonales(
           datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosIdentificacion datosIdentificacion,
           datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion datosDenominacion,
           datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio datosDomicilio,
           datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.ActividadEconomica actividadEconomica) {
           this.datosIdentificacion = datosIdentificacion;
           this.datosDenominacion = datosDenominacion;
           this.datosDomicilio = datosDomicilio;
           this.actividadEconomica = actividadEconomica;
    }


    /**
     * Gets the datosIdentificacion value for this DatosEspecificosDatosEspecificosPeticionDatosPersonales.
     * 
     * @return datosIdentificacion
     */
    public datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosIdentificacion getDatosIdentificacion() {
        return datosIdentificacion;
    }


    /**
     * Sets the datosIdentificacion value for this DatosEspecificosDatosEspecificosPeticionDatosPersonales.
     * 
     * @param datosIdentificacion
     */
    public void setDatosIdentificacion(datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosIdentificacion datosIdentificacion) {
        this.datosIdentificacion = datosIdentificacion;
    }


    /**
     * Gets the datosDenominacion value for this DatosEspecificosDatosEspecificosPeticionDatosPersonales.
     * 
     * @return datosDenominacion
     */
    public datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion getDatosDenominacion() {
        return datosDenominacion;
    }


    /**
     * Sets the datosDenominacion value for this DatosEspecificosDatosEspecificosPeticionDatosPersonales.
     * 
     * @param datosDenominacion
     */
    public void setDatosDenominacion(datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion datosDenominacion) {
        this.datosDenominacion = datosDenominacion;
    }


    /**
     * Gets the datosDomicilio value for this DatosEspecificosDatosEspecificosPeticionDatosPersonales.
     * 
     * @return datosDomicilio
     */
    public datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio getDatosDomicilio() {
        return datosDomicilio;
    }


    /**
     * Sets the datosDomicilio value for this DatosEspecificosDatosEspecificosPeticionDatosPersonales.
     * 
     * @param datosDomicilio
     */
    public void setDatosDomicilio(datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio datosDomicilio) {
        this.datosDomicilio = datosDomicilio;
    }


    /**
     * Gets the actividadEconomica value for this DatosEspecificosDatosEspecificosPeticionDatosPersonales.
     * 
     * @return actividadEconomica
     */
    public datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.ActividadEconomica getActividadEconomica() {
        return actividadEconomica;
    }


    /**
     * Sets the actividadEconomica value for this DatosEspecificosDatosEspecificosPeticionDatosPersonales.
     * 
     * @param actividadEconomica
     */
    public void setActividadEconomica(datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.ActividadEconomica actividadEconomica) {
        this.actividadEconomica = actividadEconomica;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionDatosPersonales)) return false;
        DatosEspecificosDatosEspecificosPeticionDatosPersonales other = (DatosEspecificosDatosEspecificosPeticionDatosPersonales) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.datosIdentificacion==null && other.getDatosIdentificacion()==null) || 
             (this.datosIdentificacion!=null &&
              this.datosIdentificacion.equals(other.getDatosIdentificacion()))) &&
            ((this.datosDenominacion==null && other.getDatosDenominacion()==null) || 
             (this.datosDenominacion!=null &&
              this.datosDenominacion.equals(other.getDatosDenominacion()))) &&
            ((this.datosDomicilio==null && other.getDatosDomicilio()==null) || 
             (this.datosDomicilio!=null &&
              this.datosDomicilio.equals(other.getDatosDomicilio()))) &&
            ((this.actividadEconomica==null && other.getActividadEconomica()==null) || 
             (this.actividadEconomica!=null &&
              this.actividadEconomica.equals(other.getActividadEconomica())));
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
        if (getDatosIdentificacion() != null) {
            _hashCode += getDatosIdentificacion().hashCode();
        }
        if (getDatosDenominacion() != null) {
            _hashCode += getDatosDenominacion().hashCode();
        }
        if (getDatosDomicilio() != null) {
            _hashCode += getDatosDomicilio().hashCode();
        }
        if (getActividadEconomica() != null) {
            _hashCode += getActividadEconomica().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionDatosPersonales.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>DatosEspecificos>DatosEspecificosPeticion>DatosPersonales"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosIdentificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DatosIdentificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">DatosIdentificacion"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosDenominacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DatosDenominacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>DatosPersonales>DatosDenominacion"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosDomicilio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "DatosDomicilio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>DatosPersonales>DatosDomicilio"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actividadEconomica");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ActividadEconomica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">ActividadEconomica"));
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
