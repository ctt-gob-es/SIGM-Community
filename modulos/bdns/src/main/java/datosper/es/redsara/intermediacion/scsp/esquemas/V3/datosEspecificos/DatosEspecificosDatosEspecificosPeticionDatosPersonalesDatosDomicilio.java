/**
 * DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio  implements java.io.Serializable {
    private java.lang.String paisDom;

    private java.lang.String domicilio;

    private java.lang.String codigoPostal;

    private java.lang.String codProvincia;

    private java.lang.String codMunicipio;

    private java.lang.String municipio;

    public DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio() {
    }

    public DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio(
           java.lang.String paisDom,
           java.lang.String domicilio,
           java.lang.String codigoPostal,
           java.lang.String codProvincia,
           java.lang.String codMunicipio,
           java.lang.String municipio) {
           this.paisDom = paisDom;
           this.domicilio = domicilio;
           this.codigoPostal = codigoPostal;
           this.codProvincia = codProvincia;
           this.codMunicipio = codMunicipio;
           this.municipio = municipio;
    }


    /**
     * Gets the paisDom value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @return paisDom
     */
    public java.lang.String getPaisDom() {
        return paisDom;
    }


    /**
     * Sets the paisDom value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @param paisDom
     */
    public void setPaisDom(java.lang.String paisDom) {
        this.paisDom = paisDom;
    }


    /**
     * Gets the domicilio value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @return domicilio
     */
    public java.lang.String getDomicilio() {
        return domicilio;
    }


    /**
     * Sets the domicilio value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @param domicilio
     */
    public void setDomicilio(java.lang.String domicilio) {
        this.domicilio = domicilio;
    }


    /**
     * Gets the codigoPostal value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @return codigoPostal
     */
    public java.lang.String getCodigoPostal() {
        return codigoPostal;
    }


    /**
     * Sets the codigoPostal value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @param codigoPostal
     */
    public void setCodigoPostal(java.lang.String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }


    /**
     * Gets the codProvincia value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @return codProvincia
     */
    public java.lang.String getCodProvincia() {
        return codProvincia;
    }


    /**
     * Sets the codProvincia value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @param codProvincia
     */
    public void setCodProvincia(java.lang.String codProvincia) {
        this.codProvincia = codProvincia;
    }


    /**
     * Gets the codMunicipio value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @return codMunicipio
     */
    public java.lang.String getCodMunicipio() {
        return codMunicipio;
    }


    /**
     * Sets the codMunicipio value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @param codMunicipio
     */
    public void setCodMunicipio(java.lang.String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }


    /**
     * Gets the municipio value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @return municipio
     */
    public java.lang.String getMunicipio() {
        return municipio;
    }


    /**
     * Sets the municipio value for this DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.
     * 
     * @param municipio
     */
    public void setMunicipio(java.lang.String municipio) {
        this.municipio = municipio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio)) return false;
        DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio other = (DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.paisDom==null && other.getPaisDom()==null) || 
             (this.paisDom!=null &&
              this.paisDom.equals(other.getPaisDom()))) &&
            ((this.domicilio==null && other.getDomicilio()==null) || 
             (this.domicilio!=null &&
              this.domicilio.equals(other.getDomicilio()))) &&
            ((this.codigoPostal==null && other.getCodigoPostal()==null) || 
             (this.codigoPostal!=null &&
              this.codigoPostal.equals(other.getCodigoPostal()))) &&
            ((this.codProvincia==null && other.getCodProvincia()==null) || 
             (this.codProvincia!=null &&
              this.codProvincia.equals(other.getCodProvincia()))) &&
            ((this.codMunicipio==null && other.getCodMunicipio()==null) || 
             (this.codMunicipio!=null &&
              this.codMunicipio.equals(other.getCodMunicipio()))) &&
            ((this.municipio==null && other.getMunicipio()==null) || 
             (this.municipio!=null &&
              this.municipio.equals(other.getMunicipio())));
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
        if (getPaisDom() != null) {
            _hashCode += getPaisDom().hashCode();
        }
        if (getDomicilio() != null) {
            _hashCode += getDomicilio().hashCode();
        }
        if (getCodigoPostal() != null) {
            _hashCode += getCodigoPostal().hashCode();
        }
        if (getCodProvincia() != null) {
            _hashCode += getCodProvincia().hashCode();
        }
        if (getCodMunicipio() != null) {
            _hashCode += getCodMunicipio().hashCode();
        }
        if (getMunicipio() != null) {
            _hashCode += getMunicipio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionDatosPersonalesDatosDomicilio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosEspecificos>DatosEspecificosPeticion>DatosPersonales>DatosDomicilio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paisDom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "PaisDom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domicilio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Domicilio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">Domicilio"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoPostal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "CodigoPostal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">CodigoPostal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codProvincia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "CodProvincia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codMunicipio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "CodMunicipio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("municipio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Municipio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">Municipio"));
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
