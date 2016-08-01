/**
 * FundacionPrograma.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class FundacionPrograma  implements java.io.Serializable {
    private java.lang.String programa;

    private es.dipucr.contratacion.client.beans.Campo programasFinanciacionCode;

    public FundacionPrograma() {
    }

    public FundacionPrograma(
           java.lang.String programa,
           es.dipucr.contratacion.client.beans.Campo programasFinanciacionCode) {
           this.programa = programa;
           this.programasFinanciacionCode = programasFinanciacionCode;
    }


    /**
     * Gets the programa value for this FundacionPrograma.
     * 
     * @return programa
     */
    public java.lang.String getPrograma() {
        return programa;
    }


    /**
     * Sets the programa value for this FundacionPrograma.
     * 
     * @param programa
     */
    public void setPrograma(java.lang.String programa) {
        this.programa = programa;
    }


    /**
     * Gets the programasFinanciacionCode value for this FundacionPrograma.
     * 
     * @return programasFinanciacionCode
     */
    public es.dipucr.contratacion.client.beans.Campo getProgramasFinanciacionCode() {
        return programasFinanciacionCode;
    }


    /**
     * Sets the programasFinanciacionCode value for this FundacionPrograma.
     * 
     * @param programasFinanciacionCode
     */
    public void setProgramasFinanciacionCode(es.dipucr.contratacion.client.beans.Campo programasFinanciacionCode) {
        this.programasFinanciacionCode = programasFinanciacionCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FundacionPrograma)) return false;
        FundacionPrograma other = (FundacionPrograma) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.programa==null && other.getPrograma()==null) || 
             (this.programa!=null &&
              this.programa.equals(other.getPrograma()))) &&
            ((this.programasFinanciacionCode==null && other.getProgramasFinanciacionCode()==null) || 
             (this.programasFinanciacionCode!=null &&
              this.programasFinanciacionCode.equals(other.getProgramasFinanciacionCode())));
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
        if (getPrograma() != null) {
            _hashCode += getPrograma().hashCode();
        }
        if (getProgramasFinanciacionCode() != null) {
            _hashCode += getProgramasFinanciacionCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FundacionPrograma.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "FundacionPrograma"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("programa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "programa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("programasFinanciacionCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "programasFinanciacionCode"));
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
