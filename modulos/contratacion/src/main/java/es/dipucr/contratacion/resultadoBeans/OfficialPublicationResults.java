/**
 * OfficialPublicationResults.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.resultadoBeans;

public class OfficialPublicationResults  implements java.io.Serializable {
    private es.dipucr.contratacion.resultadoBeans.OfficialPublicationResult[] officialPublicationResult;

    public OfficialPublicationResults() {
    }

    public OfficialPublicationResults(
           es.dipucr.contratacion.resultadoBeans.OfficialPublicationResult[] officialPublicationResult) {
           this.officialPublicationResult = officialPublicationResult;
    }


    /**
     * Gets the officialPublicationResult value for this OfficialPublicationResults.
     * 
     * @return officialPublicationResult
     */
    public es.dipucr.contratacion.resultadoBeans.OfficialPublicationResult[] getOfficialPublicationResult() {
        return officialPublicationResult;
    }


    /**
     * Sets the officialPublicationResult value for this OfficialPublicationResults.
     * 
     * @param officialPublicationResult
     */
    public void setOfficialPublicationResult(es.dipucr.contratacion.resultadoBeans.OfficialPublicationResult[] officialPublicationResult) {
        this.officialPublicationResult = officialPublicationResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OfficialPublicationResults)) return false;
        OfficialPublicationResults other = (OfficialPublicationResults) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.officialPublicationResult==null && other.getOfficialPublicationResult()==null) || 
             (this.officialPublicationResult!=null &&
              java.util.Arrays.equals(this.officialPublicationResult, other.getOfficialPublicationResult())));
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
        if (getOfficialPublicationResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOfficialPublicationResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOfficialPublicationResult(), i);
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
        new org.apache.axis.description.TypeDesc(OfficialPublicationResults.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "OfficialPublicationResults"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("officialPublicationResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "officialPublicationResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "OfficialPublicationResult"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
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
