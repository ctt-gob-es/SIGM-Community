/**
 * ResumenPlanPorAyuntamiento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.domain.planesProvinciales;

public class ResumenPlanPorAyuntamiento  implements java.io.Serializable {
    private int anioact;

    private int anioant;

    private double aportayuntamiento;

    private int codigo;

    private double dipumptact;

    private double dipumptant;

    private java.lang.String habitantes;

    private java.lang.String nombreayuntamiento;

    private double total;

    public ResumenPlanPorAyuntamiento() {
    }

    public ResumenPlanPorAyuntamiento(
           int anioact,
           int anioant,
           double aportayuntamiento,
           int codigo,
           double dipumptact,
           double dipumptant,
           java.lang.String habitantes,
           java.lang.String nombreayuntamiento,
           double total) {
           this.anioact = anioact;
           this.anioant = anioant;
           this.aportayuntamiento = aportayuntamiento;
           this.codigo = codigo;
           this.dipumptact = dipumptact;
           this.dipumptant = dipumptant;
           this.habitantes = habitantes;
           this.nombreayuntamiento = nombreayuntamiento;
           this.total = total;
    }


    /**
     * Gets the anioact value for this ResumenPlanPorAyuntamiento.
     * 
     * @return anioact
     */
    public int getAnioact() {
        return anioact;
    }


    /**
     * Sets the anioact value for this ResumenPlanPorAyuntamiento.
     * 
     * @param anioact
     */
    public void setAnioact(int anioact) {
        this.anioact = anioact;
    }


    /**
     * Gets the anioant value for this ResumenPlanPorAyuntamiento.
     * 
     * @return anioant
     */
    public int getAnioant() {
        return anioant;
    }


    /**
     * Sets the anioant value for this ResumenPlanPorAyuntamiento.
     * 
     * @param anioant
     */
    public void setAnioant(int anioant) {
        this.anioant = anioant;
    }


    /**
     * Gets the aportayuntamiento value for this ResumenPlanPorAyuntamiento.
     * 
     * @return aportayuntamiento
     */
    public double getAportayuntamiento() {
        return aportayuntamiento;
    }


    /**
     * Sets the aportayuntamiento value for this ResumenPlanPorAyuntamiento.
     * 
     * @param aportayuntamiento
     */
    public void setAportayuntamiento(double aportayuntamiento) {
        this.aportayuntamiento = aportayuntamiento;
    }


    /**
     * Gets the codigo value for this ResumenPlanPorAyuntamiento.
     * 
     * @return codigo
     */
    public int getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this ResumenPlanPorAyuntamiento.
     * 
     * @param codigo
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the dipumptact value for this ResumenPlanPorAyuntamiento.
     * 
     * @return dipumptact
     */
    public double getDipumptact() {
        return dipumptact;
    }


    /**
     * Sets the dipumptact value for this ResumenPlanPorAyuntamiento.
     * 
     * @param dipumptact
     */
    public void setDipumptact(double dipumptact) {
        this.dipumptact = dipumptact;
    }


    /**
     * Gets the dipumptant value for this ResumenPlanPorAyuntamiento.
     * 
     * @return dipumptant
     */
    public double getDipumptant() {
        return dipumptant;
    }


    /**
     * Sets the dipumptant value for this ResumenPlanPorAyuntamiento.
     * 
     * @param dipumptant
     */
    public void setDipumptant(double dipumptant) {
        this.dipumptant = dipumptant;
    }


    /**
     * Gets the habitantes value for this ResumenPlanPorAyuntamiento.
     * 
     * @return habitantes
     */
    public java.lang.String getHabitantes() {
        return habitantes;
    }


    /**
     * Sets the habitantes value for this ResumenPlanPorAyuntamiento.
     * 
     * @param habitantes
     */
    public void setHabitantes(java.lang.String habitantes) {
        this.habitantes = habitantes;
    }


    /**
     * Gets the nombreayuntamiento value for this ResumenPlanPorAyuntamiento.
     * 
     * @return nombreayuntamiento
     */
    public java.lang.String getNombreayuntamiento() {
        return nombreayuntamiento;
    }


    /**
     * Sets the nombreayuntamiento value for this ResumenPlanPorAyuntamiento.
     * 
     * @param nombreayuntamiento
     */
    public void setNombreayuntamiento(java.lang.String nombreayuntamiento) {
        this.nombreayuntamiento = nombreayuntamiento;
    }


    /**
     * Gets the total value for this ResumenPlanPorAyuntamiento.
     * 
     * @return total
     */
    public double getTotal() {
        return total;
    }


    /**
     * Sets the total value for this ResumenPlanPorAyuntamiento.
     * 
     * @param total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResumenPlanPorAyuntamiento)) return false;
        ResumenPlanPorAyuntamiento other = (ResumenPlanPorAyuntamiento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.anioact == other.getAnioact() &&
            this.anioant == other.getAnioant() &&
            this.aportayuntamiento == other.getAportayuntamiento() &&
            this.codigo == other.getCodigo() &&
            this.dipumptact == other.getDipumptact() &&
            this.dipumptant == other.getDipumptant() &&
            ((this.habitantes==null && other.getHabitantes()==null) || 
             (this.habitantes!=null &&
              this.habitantes.equals(other.getHabitantes()))) &&
            ((this.nombreayuntamiento==null && other.getNombreayuntamiento()==null) || 
             (this.nombreayuntamiento!=null &&
              this.nombreayuntamiento.equals(other.getNombreayuntamiento()))) &&
            this.total == other.getTotal();
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
        _hashCode += getAnioact();
        _hashCode += getAnioant();
        _hashCode += new Double(getAportayuntamiento()).hashCode();
        _hashCode += getCodigo();
        _hashCode += new Double(getDipumptact()).hashCode();
        _hashCode += new Double(getDipumptant()).hashCode();
        if (getHabitantes() != null) {
            _hashCode += getHabitantes().hashCode();
        }
        if (getNombreayuntamiento() != null) {
            _hashCode += getNombreayuntamiento().hashCode();
        }
        _hashCode += new Double(getTotal()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResumenPlanPorAyuntamiento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "ResumenPlanPorAyuntamiento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anioact");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "anioact"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anioant");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "anioant"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aportayuntamiento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "aportayuntamiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dipumptact");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "dipumptact"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dipumptant");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "dipumptant"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("habitantes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "habitantes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreayuntamiento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "nombreayuntamiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("total");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "total"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
