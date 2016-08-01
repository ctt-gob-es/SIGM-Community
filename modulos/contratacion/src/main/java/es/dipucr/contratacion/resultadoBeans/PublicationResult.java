/**
 * PublicationResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.resultadoBeans;

public class PublicationResult  implements java.io.Serializable {
    private java.lang.String expedientNumber;

    private java.lang.String noticeURL;

    private es.dipucr.contratacion.resultadoBeans.OfficialPublicationResults officialPublicationResults;

    private es.dipucr.contratacion.resultadoBeans.PublishErrorDetails publishErrorDetails;

    private java.lang.String resultCode;

    private java.lang.String resultCodeDescription;

    private java.lang.String state;

    private byte[] timestamping;

    public PublicationResult() {
    }

    public PublicationResult(
           java.lang.String expedientNumber,
           java.lang.String noticeURL,
           es.dipucr.contratacion.resultadoBeans.OfficialPublicationResults officialPublicationResults,
           es.dipucr.contratacion.resultadoBeans.PublishErrorDetails publishErrorDetails,
           java.lang.String resultCode,
           java.lang.String resultCodeDescription,
           java.lang.String state,
           byte[] timestamping) {
           this.expedientNumber = expedientNumber;
           this.noticeURL = noticeURL;
           this.officialPublicationResults = officialPublicationResults;
           this.publishErrorDetails = publishErrorDetails;
           this.resultCode = resultCode;
           this.resultCodeDescription = resultCodeDescription;
           this.state = state;
           this.timestamping = timestamping;
    }


    /**
     * Gets the expedientNumber value for this PublicationResult.
     * 
     * @return expedientNumber
     */
    public java.lang.String getExpedientNumber() {
        return expedientNumber;
    }


    /**
     * Sets the expedientNumber value for this PublicationResult.
     * 
     * @param expedientNumber
     */
    public void setExpedientNumber(java.lang.String expedientNumber) {
        this.expedientNumber = expedientNumber;
    }


    /**
     * Gets the noticeURL value for this PublicationResult.
     * 
     * @return noticeURL
     */
    public java.lang.String getNoticeURL() {
        return noticeURL;
    }


    /**
     * Sets the noticeURL value for this PublicationResult.
     * 
     * @param noticeURL
     */
    public void setNoticeURL(java.lang.String noticeURL) {
        this.noticeURL = noticeURL;
    }


    /**
     * Gets the officialPublicationResults value for this PublicationResult.
     * 
     * @return officialPublicationResults
     */
    public es.dipucr.contratacion.resultadoBeans.OfficialPublicationResults getOfficialPublicationResults() {
        return officialPublicationResults;
    }


    /**
     * Sets the officialPublicationResults value for this PublicationResult.
     * 
     * @param officialPublicationResults
     */
    public void setOfficialPublicationResults(es.dipucr.contratacion.resultadoBeans.OfficialPublicationResults officialPublicationResults) {
        this.officialPublicationResults = officialPublicationResults;
    }


    /**
     * Gets the publishErrorDetails value for this PublicationResult.
     * 
     * @return publishErrorDetails
     */
    public es.dipucr.contratacion.resultadoBeans.PublishErrorDetails getPublishErrorDetails() {
        return publishErrorDetails;
    }


    /**
     * Sets the publishErrorDetails value for this PublicationResult.
     * 
     * @param publishErrorDetails
     */
    public void setPublishErrorDetails(es.dipucr.contratacion.resultadoBeans.PublishErrorDetails publishErrorDetails) {
        this.publishErrorDetails = publishErrorDetails;
    }


    /**
     * Gets the resultCode value for this PublicationResult.
     * 
     * @return resultCode
     */
    public java.lang.String getResultCode() {
        return resultCode;
    }


    /**
     * Sets the resultCode value for this PublicationResult.
     * 
     * @param resultCode
     */
    public void setResultCode(java.lang.String resultCode) {
        this.resultCode = resultCode;
    }


    /**
     * Gets the resultCodeDescription value for this PublicationResult.
     * 
     * @return resultCodeDescription
     */
    public java.lang.String getResultCodeDescription() {
        return resultCodeDescription;
    }


    /**
     * Sets the resultCodeDescription value for this PublicationResult.
     * 
     * @param resultCodeDescription
     */
    public void setResultCodeDescription(java.lang.String resultCodeDescription) {
        this.resultCodeDescription = resultCodeDescription;
    }


    /**
     * Gets the state value for this PublicationResult.
     * 
     * @return state
     */
    public java.lang.String getState() {
        return state;
    }


    /**
     * Sets the state value for this PublicationResult.
     * 
     * @param state
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }


    /**
     * Gets the timestamping value for this PublicationResult.
     * 
     * @return timestamping
     */
    public byte[] getTimestamping() {
        return timestamping;
    }


    /**
     * Sets the timestamping value for this PublicationResult.
     * 
     * @param timestamping
     */
    public void setTimestamping(byte[] timestamping) {
        this.timestamping = timestamping;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PublicationResult)) return false;
        PublicationResult other = (PublicationResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.expedientNumber==null && other.getExpedientNumber()==null) || 
             (this.expedientNumber!=null &&
              this.expedientNumber.equals(other.getExpedientNumber()))) &&
            ((this.noticeURL==null && other.getNoticeURL()==null) || 
             (this.noticeURL!=null &&
              this.noticeURL.equals(other.getNoticeURL()))) &&
            ((this.officialPublicationResults==null && other.getOfficialPublicationResults()==null) || 
             (this.officialPublicationResults!=null &&
              this.officialPublicationResults.equals(other.getOfficialPublicationResults()))) &&
            ((this.publishErrorDetails==null && other.getPublishErrorDetails()==null) || 
             (this.publishErrorDetails!=null &&
              this.publishErrorDetails.equals(other.getPublishErrorDetails()))) &&
            ((this.resultCode==null && other.getResultCode()==null) || 
             (this.resultCode!=null &&
              this.resultCode.equals(other.getResultCode()))) &&
            ((this.resultCodeDescription==null && other.getResultCodeDescription()==null) || 
             (this.resultCodeDescription!=null &&
              this.resultCodeDescription.equals(other.getResultCodeDescription()))) &&
            ((this.state==null && other.getState()==null) || 
             (this.state!=null &&
              this.state.equals(other.getState()))) &&
            ((this.timestamping==null && other.getTimestamping()==null) || 
             (this.timestamping!=null &&
              java.util.Arrays.equals(this.timestamping, other.getTimestamping())));
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
        if (getExpedientNumber() != null) {
            _hashCode += getExpedientNumber().hashCode();
        }
        if (getNoticeURL() != null) {
            _hashCode += getNoticeURL().hashCode();
        }
        if (getOfficialPublicationResults() != null) {
            _hashCode += getOfficialPublicationResults().hashCode();
        }
        if (getPublishErrorDetails() != null) {
            _hashCode += getPublishErrorDetails().hashCode();
        }
        if (getResultCode() != null) {
            _hashCode += getResultCode().hashCode();
        }
        if (getResultCodeDescription() != null) {
            _hashCode += getResultCodeDescription().hashCode();
        }
        if (getState() != null) {
            _hashCode += getState().hashCode();
        }
        if (getTimestamping() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTimestamping());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTimestamping(), i);
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
        new org.apache.axis.description.TypeDesc(PublicationResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "PublicationResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expedientNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "expedientNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noticeURL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "noticeURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("officialPublicationResults");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "officialPublicationResults"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "OfficialPublicationResults"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publishErrorDetails");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "publishErrorDetails"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "PublishErrorDetails"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("state");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "state"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timestamping");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "timestamping"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
