/**
 * Garantia.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class Garantia  implements java.io.Serializable {
    private java.lang.String amountRate;

    private java.util.Calendar constitutionPeriod;

    private java.lang.String[] descripcion;

    private java.lang.String importeGarantia;

    private java.lang.String periodoGarantia;

    private es.dipucr.contratacion.client.beans.Campo tipoGarantia;

    private es.dipucr.contratacion.client.beans.Campo tipoPeriodo;

    public Garantia() {
    }

    public Garantia(
           java.lang.String amountRate,
           java.util.Calendar constitutionPeriod,
           java.lang.String[] descripcion,
           java.lang.String importeGarantia,
           java.lang.String periodoGarantia,
           es.dipucr.contratacion.client.beans.Campo tipoGarantia,
           es.dipucr.contratacion.client.beans.Campo tipoPeriodo) {
           this.amountRate = amountRate;
           this.constitutionPeriod = constitutionPeriod;
           this.descripcion = descripcion;
           this.importeGarantia = importeGarantia;
           this.periodoGarantia = periodoGarantia;
           this.tipoGarantia = tipoGarantia;
           this.tipoPeriodo = tipoPeriodo;
    }


    /**
     * Gets the amountRate value for this Garantia.
     * 
     * @return amountRate
     */
    public java.lang.String getAmountRate() {
        return amountRate;
    }


    /**
     * Sets the amountRate value for this Garantia.
     * 
     * @param amountRate
     */
    public void setAmountRate(java.lang.String amountRate) {
        this.amountRate = amountRate;
    }


    /**
     * Gets the constitutionPeriod value for this Garantia.
     * 
     * @return constitutionPeriod
     */
    public java.util.Calendar getConstitutionPeriod() {
        return constitutionPeriod;
    }


    /**
     * Sets the constitutionPeriod value for this Garantia.
     * 
     * @param constitutionPeriod
     */
    public void setConstitutionPeriod(java.util.Calendar constitutionPeriod) {
        this.constitutionPeriod = constitutionPeriod;
    }


    /**
     * Gets the descripcion value for this Garantia.
     * 
     * @return descripcion
     */
    public java.lang.String[] getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this Garantia.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String[] descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the importeGarantia value for this Garantia.
     * 
     * @return importeGarantia
     */
    public java.lang.String getImporteGarantia() {
        return importeGarantia;
    }


    /**
     * Sets the importeGarantia value for this Garantia.
     * 
     * @param importeGarantia
     */
    public void setImporteGarantia(java.lang.String importeGarantia) {
        this.importeGarantia = importeGarantia;
    }


    /**
     * Gets the periodoGarantia value for this Garantia.
     * 
     * @return periodoGarantia
     */
    public java.lang.String getPeriodoGarantia() {
        return periodoGarantia;
    }


    /**
     * Sets the periodoGarantia value for this Garantia.
     * 
     * @param periodoGarantia
     */
    public void setPeriodoGarantia(java.lang.String periodoGarantia) {
        this.periodoGarantia = periodoGarantia;
    }


    /**
     * Gets the tipoGarantia value for this Garantia.
     * 
     * @return tipoGarantia
     */
    public es.dipucr.contratacion.client.beans.Campo getTipoGarantia() {
        return tipoGarantia;
    }


    /**
     * Sets the tipoGarantia value for this Garantia.
     * 
     * @param tipoGarantia
     */
    public void setTipoGarantia(es.dipucr.contratacion.client.beans.Campo tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
    }


    /**
     * Gets the tipoPeriodo value for this Garantia.
     * 
     * @return tipoPeriodo
     */
    public es.dipucr.contratacion.client.beans.Campo getTipoPeriodo() {
        return tipoPeriodo;
    }


    /**
     * Sets the tipoPeriodo value for this Garantia.
     * 
     * @param tipoPeriodo
     */
    public void setTipoPeriodo(es.dipucr.contratacion.client.beans.Campo tipoPeriodo) {
        this.tipoPeriodo = tipoPeriodo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Garantia)) return false;
        Garantia other = (Garantia) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.amountRate==null && other.getAmountRate()==null) || 
             (this.amountRate!=null &&
              this.amountRate.equals(other.getAmountRate()))) &&
            ((this.constitutionPeriod==null && other.getConstitutionPeriod()==null) || 
             (this.constitutionPeriod!=null &&
              this.constitutionPeriod.equals(other.getConstitutionPeriod()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              java.util.Arrays.equals(this.descripcion, other.getDescripcion()))) &&
            ((this.importeGarantia==null && other.getImporteGarantia()==null) || 
             (this.importeGarantia!=null &&
              this.importeGarantia.equals(other.getImporteGarantia()))) &&
            ((this.periodoGarantia==null && other.getPeriodoGarantia()==null) || 
             (this.periodoGarantia!=null &&
              this.periodoGarantia.equals(other.getPeriodoGarantia()))) &&
            ((this.tipoGarantia==null && other.getTipoGarantia()==null) || 
             (this.tipoGarantia!=null &&
              this.tipoGarantia.equals(other.getTipoGarantia()))) &&
            ((this.tipoPeriodo==null && other.getTipoPeriodo()==null) || 
             (this.tipoPeriodo!=null &&
              this.tipoPeriodo.equals(other.getTipoPeriodo())));
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
        if (getAmountRate() != null) {
            _hashCode += getAmountRate().hashCode();
        }
        if (getConstitutionPeriod() != null) {
            _hashCode += getConstitutionPeriod().hashCode();
        }
        if (getDescripcion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescripcion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescripcion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getImporteGarantia() != null) {
            _hashCode += getImporteGarantia().hashCode();
        }
        if (getPeriodoGarantia() != null) {
            _hashCode += getPeriodoGarantia().hashCode();
        }
        if (getTipoGarantia() != null) {
            _hashCode += getTipoGarantia().hashCode();
        }
        if (getTipoPeriodo() != null) {
            _hashCode += getTipoPeriodo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Garantia.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Garantia"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amountRate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "amountRate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("constitutionPeriod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "constitutionPeriod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importeGarantia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "importeGarantia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("periodoGarantia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "periodoGarantia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoGarantia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "tipoGarantia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoPeriodo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "tipoPeriodo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
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
