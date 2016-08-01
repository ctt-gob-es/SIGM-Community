/**
 * VariantesOfertas.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class VariantesOfertas  implements java.io.Serializable {
    private java.lang.String[] descVariantes;

    private java.lang.String numMaxVar;

    private boolean varianteOferta;

    public VariantesOfertas() {
    }

    public VariantesOfertas(
           java.lang.String[] descVariantes,
           java.lang.String numMaxVar,
           boolean varianteOferta) {
           this.descVariantes = descVariantes;
           this.numMaxVar = numMaxVar;
           this.varianteOferta = varianteOferta;
    }


    /**
     * Gets the descVariantes value for this VariantesOfertas.
     * 
     * @return descVariantes
     */
    public java.lang.String[] getDescVariantes() {
        return descVariantes;
    }


    /**
     * Sets the descVariantes value for this VariantesOfertas.
     * 
     * @param descVariantes
     */
    public void setDescVariantes(java.lang.String[] descVariantes) {
        this.descVariantes = descVariantes;
    }


    /**
     * Gets the numMaxVar value for this VariantesOfertas.
     * 
     * @return numMaxVar
     */
    public java.lang.String getNumMaxVar() {
        return numMaxVar;
    }


    /**
     * Sets the numMaxVar value for this VariantesOfertas.
     * 
     * @param numMaxVar
     */
    public void setNumMaxVar(java.lang.String numMaxVar) {
        this.numMaxVar = numMaxVar;
    }


    /**
     * Gets the varianteOferta value for this VariantesOfertas.
     * 
     * @return varianteOferta
     */
    public boolean isVarianteOferta() {
        return varianteOferta;
    }


    /**
     * Sets the varianteOferta value for this VariantesOfertas.
     * 
     * @param varianteOferta
     */
    public void setVarianteOferta(boolean varianteOferta) {
        this.varianteOferta = varianteOferta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VariantesOfertas)) return false;
        VariantesOfertas other = (VariantesOfertas) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.descVariantes==null && other.getDescVariantes()==null) || 
             (this.descVariantes!=null &&
              java.util.Arrays.equals(this.descVariantes, other.getDescVariantes()))) &&
            ((this.numMaxVar==null && other.getNumMaxVar()==null) || 
             (this.numMaxVar!=null &&
              this.numMaxVar.equals(other.getNumMaxVar()))) &&
            this.varianteOferta == other.isVarianteOferta();
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
        if (getDescVariantes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescVariantes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescVariantes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNumMaxVar() != null) {
            _hashCode += getNumMaxVar().hashCode();
        }
        _hashCode += (isVarianteOferta() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VariantesOfertas.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "VariantesOfertas"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descVariantes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descVariantes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numMaxVar");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "numMaxVar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("varianteOferta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "varianteOferta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
