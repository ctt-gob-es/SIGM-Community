/**
 * GetPlanProvincialResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.services.server.planesProvinciales;

public class GetPlanProvincialResponse  implements java.io.Serializable {
    private es.dipucr.domain.planesProvinciales.PlanProvincial getPlanProvincialReturn;

    public GetPlanProvincialResponse() {
    }

    public GetPlanProvincialResponse(
           es.dipucr.domain.planesProvinciales.PlanProvincial getPlanProvincialReturn) {
           this.getPlanProvincialReturn = getPlanProvincialReturn;
    }


    /**
     * Gets the getPlanProvincialReturn value for this GetPlanProvincialResponse.
     * 
     * @return getPlanProvincialReturn
     */
    public es.dipucr.domain.planesProvinciales.PlanProvincial getGetPlanProvincialReturn() {
        return getPlanProvincialReturn;
    }


    /**
     * Sets the getPlanProvincialReturn value for this GetPlanProvincialResponse.
     * 
     * @param getPlanProvincialReturn
     */
    public void setGetPlanProvincialReturn(es.dipucr.domain.planesProvinciales.PlanProvincial getPlanProvincialReturn) {
        this.getPlanProvincialReturn = getPlanProvincialReturn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetPlanProvincialResponse)) return false;
        GetPlanProvincialResponse other = (GetPlanProvincialResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getPlanProvincialReturn==null && other.getGetPlanProvincialReturn()==null) || 
             (this.getPlanProvincialReturn!=null &&
              this.getPlanProvincialReturn.equals(other.getGetPlanProvincialReturn())));
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
        if (getGetPlanProvincialReturn() != null) {
            _hashCode += getGetPlanProvincialReturn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetPlanProvincialResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", ">getPlanProvincialResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getPlanProvincialReturn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://planesProvinciales.server.services.dipucr.es", "getPlanProvincialReturn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://planesProvinciales.domain.dipucr.es", "PlanProvincial"));
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
