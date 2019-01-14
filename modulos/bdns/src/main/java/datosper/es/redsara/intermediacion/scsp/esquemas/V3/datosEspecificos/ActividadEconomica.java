/**
 * ActividadEconomica.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package datosper.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class ActividadEconomica  implements java.io.Serializable {
    private java.lang.String region;

    private java.lang.String tipoBeneficiario;

    private java.lang.String[] sectorEconomico;

    public ActividadEconomica() {
    }

    public ActividadEconomica(
           java.lang.String region,
           java.lang.String tipoBeneficiario,
           java.lang.String[] sectorEconomico) {
           this.region = region;
           this.tipoBeneficiario = tipoBeneficiario;
           this.sectorEconomico = sectorEconomico;
    }


    /**
     * Gets the region value for this ActividadEconomica.
     * 
     * @return region
     */
    public java.lang.String getRegion() {
        return region;
    }


    /**
     * Sets the region value for this ActividadEconomica.
     * 
     * @param region
     */
    public void setRegion(java.lang.String region) {
        this.region = region;
    }


    /**
     * Gets the tipoBeneficiario value for this ActividadEconomica.
     * 
     * @return tipoBeneficiario
     */
    public java.lang.String getTipoBeneficiario() {
        return tipoBeneficiario;
    }


    /**
     * Sets the tipoBeneficiario value for this ActividadEconomica.
     * 
     * @param tipoBeneficiario
     */
    public void setTipoBeneficiario(java.lang.String tipoBeneficiario) {
        this.tipoBeneficiario = tipoBeneficiario;
    }


    /**
     * Gets the sectorEconomico value for this ActividadEconomica.
     * 
     * @return sectorEconomico
     */
    public java.lang.String[] getSectorEconomico() {
        return sectorEconomico;
    }


    /**
     * Sets the sectorEconomico value for this ActividadEconomica.
     * 
     * @param sectorEconomico
     */
    public void setSectorEconomico(java.lang.String[] sectorEconomico) {
        this.sectorEconomico = sectorEconomico;
    }

    public java.lang.String getSectorEconomico(int i) {
        return this.sectorEconomico[i];
    }

    public void setSectorEconomico(int i, java.lang.String _value) {
        this.sectorEconomico[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ActividadEconomica)) return false;
        ActividadEconomica other = (ActividadEconomica) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.region==null && other.getRegion()==null) || 
             (this.region!=null &&
              this.region.equals(other.getRegion()))) &&
            ((this.tipoBeneficiario==null && other.getTipoBeneficiario()==null) || 
             (this.tipoBeneficiario!=null &&
              this.tipoBeneficiario.equals(other.getTipoBeneficiario()))) &&
            ((this.sectorEconomico==null && other.getSectorEconomico()==null) || 
             (this.sectorEconomico!=null &&
              java.util.Arrays.equals(this.sectorEconomico, other.getSectorEconomico())));
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
        if (getRegion() != null) {
            _hashCode += getRegion().hashCode();
        }
        if (getTipoBeneficiario() != null) {
            _hashCode += getTipoBeneficiario().hashCode();
        }
        if (getSectorEconomico() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSectorEconomico());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSectorEconomico(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ActividadEconomica.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">ActividadEconomica"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Region"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoBeneficiario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "TipoBeneficiario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">TipoBeneficiario"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sectorEconomico");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "SectorEconomico"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "SectorEconomico"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
