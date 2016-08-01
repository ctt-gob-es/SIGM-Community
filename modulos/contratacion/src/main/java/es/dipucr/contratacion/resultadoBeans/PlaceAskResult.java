/**
 * PlaceAskResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.resultadoBeans;

public class PlaceAskResult  implements java.io.Serializable {
    private es.dipucr.contratacion.resultadoBeans.GeneralErrorDetails askErrorDetails;

    private es.dipucr.contratacion.resultadoBeans.ExpedientStateData expedientStateData;

    private java.lang.String resultCode;

    private java.lang.String resultCodeDescription;

    public PlaceAskResult() {
    }

    public PlaceAskResult(
           es.dipucr.contratacion.resultadoBeans.GeneralErrorDetails askErrorDetails,
           es.dipucr.contratacion.resultadoBeans.ExpedientStateData expedientStateData,
           java.lang.String resultCode,
           java.lang.String resultCodeDescription) {
           this.askErrorDetails = askErrorDetails;
           this.expedientStateData = expedientStateData;
           this.resultCode = resultCode;
           this.resultCodeDescription = resultCodeDescription;
    }


    /**
     * Gets the askErrorDetails value for this PlaceAskResult.
     * 
     * @return askErrorDetails
     */
    public es.dipucr.contratacion.resultadoBeans.GeneralErrorDetails getAskErrorDetails() {
        return askErrorDetails;
    }


    /**
     * Sets the askErrorDetails value for this PlaceAskResult.
     * 
     * @param askErrorDetails
     */
    public void setAskErrorDetails(es.dipucr.contratacion.resultadoBeans.GeneralErrorDetails askErrorDetails) {
        this.askErrorDetails = askErrorDetails;
    }


    /**
     * Gets the expedientStateData value for this PlaceAskResult.
     * 
     * @return expedientStateData
     */
    public es.dipucr.contratacion.resultadoBeans.ExpedientStateData getExpedientStateData() {
        return expedientStateData;
    }


    /**
     * Sets the expedientStateData value for this PlaceAskResult.
     * 
     * @param expedientStateData
     */
    public void setExpedientStateData(es.dipucr.contratacion.resultadoBeans.ExpedientStateData expedientStateData) {
        this.expedientStateData = expedientStateData;
    }


    /**
     * Gets the resultCode value for this PlaceAskResult.
     * 
     * @return resultCode
     */
    public java.lang.String getResultCode() {
        return resultCode;
    }


    /**
     * Sets the resultCode value for this PlaceAskResult.
     * 
     * @param resultCode
     */
    public void setResultCode(java.lang.String resultCode) {
        this.resultCode = resultCode;
    }


    /**
     * Gets the resultCodeDescription value for this PlaceAskResult.
     * 
     * @return resultCodeDescription
     */
    public java.lang.String getResultCodeDescription() {
        return resultCodeDescription;
    }


    /**
     * Sets the resultCodeDescription value for this PlaceAskResult.
     * 
     * @param resultCodeDescription
     */
    public void setResultCodeDescription(java.lang.String resultCodeDescription) {
        this.resultCodeDescription = resultCodeDescription;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PlaceAskResult)) return false;
        PlaceAskResult other = (PlaceAskResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.askErrorDetails==null && other.getAskErrorDetails()==null) || 
             (this.askErrorDetails!=null &&
              this.askErrorDetails.equals(other.getAskErrorDetails()))) &&
            ((this.expedientStateData==null && other.getExpedientStateData()==null) || 
             (this.expedientStateData!=null &&
              this.expedientStateData.equals(other.getExpedientStateData()))) &&
            ((this.resultCode==null && other.getResultCode()==null) || 
             (this.resultCode!=null &&
              this.resultCode.equals(other.getResultCode()))) &&
            ((this.resultCodeDescription==null && other.getResultCodeDescription()==null) || 
             (this.resultCodeDescription!=null &&
              this.resultCodeDescription.equals(other.getResultCodeDescription())));
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
        if (getAskErrorDetails() != null) {
            _hashCode += getAskErrorDetails().hashCode();
        }
        if (getExpedientStateData() != null) {
            _hashCode += getExpedientStateData().hashCode();
        }
        if (getResultCode() != null) {
            _hashCode += getResultCode().hashCode();
        }
        if (getResultCodeDescription() != null) {
            _hashCode += getResultCodeDescription().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PlaceAskResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "PlaceAskResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("askErrorDetails");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "askErrorDetails"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "GeneralErrorDetails"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expedientStateData");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "expedientStateData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "ExpedientStateData"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "resultCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultCodeDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "resultCodeDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
