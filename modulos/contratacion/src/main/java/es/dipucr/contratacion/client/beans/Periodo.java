/**
 * Periodo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class Periodo  implements java.io.Serializable {
    private java.lang.String[] description;

    private es.dipucr.contratacion.client.beans.Campo[] descriptionCode;

    private java.lang.String duracion;

    private es.dipucr.contratacion.client.beans.Campo durationMeasure;

    private java.util.Calendar endDate;

    private java.util.Calendar startDate;

    private es.dipucr.contratacion.client.beans.Campo tipoDuracion;

    public Periodo() {
    }

    public Periodo(
           java.lang.String[] description,
           es.dipucr.contratacion.client.beans.Campo[] descriptionCode,
           java.lang.String duracion,
           es.dipucr.contratacion.client.beans.Campo durationMeasure,
           java.util.Calendar endDate,
           java.util.Calendar startDate,
           es.dipucr.contratacion.client.beans.Campo tipoDuracion) {
           this.description = description;
           this.descriptionCode = descriptionCode;
           this.duracion = duracion;
           this.durationMeasure = durationMeasure;
           this.endDate = endDate;
           this.startDate = startDate;
           this.tipoDuracion = tipoDuracion;
    }


    /**
     * Gets the description value for this Periodo.
     * 
     * @return description
     */
    public java.lang.String[] getDescription() {
        return description;
    }


    /**
     * Sets the description value for this Periodo.
     * 
     * @param description
     */
    public void setDescription(java.lang.String[] description) {
        this.description = description;
    }


    /**
     * Gets the descriptionCode value for this Periodo.
     * 
     * @return descriptionCode
     */
    public es.dipucr.contratacion.client.beans.Campo[] getDescriptionCode() {
        return descriptionCode;
    }


    /**
     * Sets the descriptionCode value for this Periodo.
     * 
     * @param descriptionCode
     */
    public void setDescriptionCode(es.dipucr.contratacion.client.beans.Campo[] descriptionCode) {
        this.descriptionCode = descriptionCode;
    }


    /**
     * Gets the duracion value for this Periodo.
     * 
     * @return duracion
     */
    public java.lang.String getDuracion() {
        return duracion;
    }


    /**
     * Sets the duracion value for this Periodo.
     * 
     * @param duracion
     */
    public void setDuracion(java.lang.String duracion) {
        this.duracion = duracion;
    }


    /**
     * Gets the durationMeasure value for this Periodo.
     * 
     * @return durationMeasure
     */
    public es.dipucr.contratacion.client.beans.Campo getDurationMeasure() {
        return durationMeasure;
    }


    /**
     * Sets the durationMeasure value for this Periodo.
     * 
     * @param durationMeasure
     */
    public void setDurationMeasure(es.dipucr.contratacion.client.beans.Campo durationMeasure) {
        this.durationMeasure = durationMeasure;
    }


    /**
     * Gets the endDate value for this Periodo.
     * 
     * @return endDate
     */
    public java.util.Calendar getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this Periodo.
     * 
     * @param endDate
     */
    public void setEndDate(java.util.Calendar endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets the startDate value for this Periodo.
     * 
     * @return startDate
     */
    public java.util.Calendar getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this Periodo.
     * 
     * @param startDate
     */
    public void setStartDate(java.util.Calendar startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the tipoDuracion value for this Periodo.
     * 
     * @return tipoDuracion
     */
    public es.dipucr.contratacion.client.beans.Campo getTipoDuracion() {
        return tipoDuracion;
    }


    /**
     * Sets the tipoDuracion value for this Periodo.
     * 
     * @param tipoDuracion
     */
    public void setTipoDuracion(es.dipucr.contratacion.client.beans.Campo tipoDuracion) {
        this.tipoDuracion = tipoDuracion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Periodo)) return false;
        Periodo other = (Periodo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              java.util.Arrays.equals(this.description, other.getDescription()))) &&
            ((this.descriptionCode==null && other.getDescriptionCode()==null) || 
             (this.descriptionCode!=null &&
              java.util.Arrays.equals(this.descriptionCode, other.getDescriptionCode()))) &&
            ((this.duracion==null && other.getDuracion()==null) || 
             (this.duracion!=null &&
              this.duracion.equals(other.getDuracion()))) &&
            ((this.durationMeasure==null && other.getDurationMeasure()==null) || 
             (this.durationMeasure!=null &&
              this.durationMeasure.equals(other.getDurationMeasure()))) &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate()))) &&
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            ((this.tipoDuracion==null && other.getTipoDuracion()==null) || 
             (this.tipoDuracion!=null &&
              this.tipoDuracion.equals(other.getTipoDuracion())));
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
        if (getDescription() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescription());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescription(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDescriptionCode() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescriptionCode());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescriptionCode(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDuracion() != null) {
            _hashCode += getDuracion().hashCode();
        }
        if (getDurationMeasure() != null) {
            _hashCode += getDurationMeasure().hashCode();
        }
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        if (getTipoDuracion() != null) {
            _hashCode += getTipoDuracion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Periodo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Periodo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descriptionCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descriptionCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("duracion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "duracion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("durationMeasure");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "durationMeasure"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "endDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "startDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoDuracion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "tipoDuracion"));
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
