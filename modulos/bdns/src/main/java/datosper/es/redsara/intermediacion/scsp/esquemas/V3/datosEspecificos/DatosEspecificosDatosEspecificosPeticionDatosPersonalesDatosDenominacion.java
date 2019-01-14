/**
 * DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion  implements java.io.Serializable {
    private datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PersonaFisica personaFisica;

    private datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PersonaJuridica personaJuridica;

    public DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion() {
    }

    public DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion(
           datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PersonaFisica personaFisica,
           datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PersonaJuridica personaJuridica) {
           this.personaFisica = personaFisica;
           this.personaJuridica = personaJuridica;
    }


    /**
     * Gets the personaFisica value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion.
     * 
     * @return personaFisica
     */
    public datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PersonaFisica getPersonaFisica() {
        return personaFisica;
    }


    /**
     * Sets the personaFisica value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion.
     * 
     * @param personaFisica
     */
    public void setPersonaFisica(datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PersonaFisica personaFisica) {
        this.personaFisica = personaFisica;
    }


    /**
     * Gets the personaJuridica value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion.
     * 
     * @return personaJuridica
     */
    public datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PersonaJuridica getPersonaJuridica() {
        return personaJuridica;
    }


    /**
     * Sets the personaJuridica value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion.
     * 
     * @param personaJuridica
     */
    public void setPersonaJuridica(datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.PersonaJuridica personaJuridica) {
        this.personaJuridica = personaJuridica;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion)) return false;
        DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion other = (DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.personaFisica==null && other.getPersonaFisica()==null) || 
             (this.personaFisica!=null &&
              this.personaFisica.equals(other.getPersonaFisica()))) &&
            ((this.personaJuridica==null && other.getPersonaJuridica()==null) || 
             (this.personaJuridica!=null &&
              this.personaJuridica.equals(other.getPersonaJuridica())));
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
        if (getPersonaFisica() != null) {
            _hashCode += getPersonaFisica().hashCode();
        }
        if (getPersonaJuridica() != null) {
            _hashCode += getPersonaJuridica().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDenominacion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>DatosPersonales>DatosDenominacion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personaFisica");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "PersonaFisica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">PersonaFisica"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personaJuridica");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "PersonaJuridica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">PersonaJuridica"));
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
