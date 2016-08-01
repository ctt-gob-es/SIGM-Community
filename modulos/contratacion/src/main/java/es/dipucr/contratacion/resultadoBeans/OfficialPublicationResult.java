/**
 * OfficialPublicationResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.resultadoBeans;

public class OfficialPublicationResult  implements java.io.Serializable {
    private java.lang.String boeConfirmationURL;

    private java.lang.String publishAgency;

    private java.lang.String publishDate;

    private java.lang.String publishURL;

    private java.lang.String returnCode;

    public OfficialPublicationResult() {
    }

    public OfficialPublicationResult(
           java.lang.String boeConfirmationURL,
           java.lang.String publishAgency,
           java.lang.String publishDate,
           java.lang.String publishURL,
           java.lang.String returnCode) {
           this.boeConfirmationURL = boeConfirmationURL;
           this.publishAgency = publishAgency;
           this.publishDate = publishDate;
           this.publishURL = publishURL;
           this.returnCode = returnCode;
    }


    /**
     * Gets the boeConfirmationURL value for this OfficialPublicationResult.
     * 
     * @return boeConfirmationURL
     */
    public java.lang.String getBoeConfirmationURL() {
        return boeConfirmationURL;
    }


    /**
     * Sets the boeConfirmationURL value for this OfficialPublicationResult.
     * 
     * @param boeConfirmationURL
     */
    public void setBoeConfirmationURL(java.lang.String boeConfirmationURL) {
        this.boeConfirmationURL = boeConfirmationURL;
    }


    /**
     * Gets the publishAgency value for this OfficialPublicationResult.
     * 
     * @return publishAgency
     */
    public java.lang.String getPublishAgency() {
        return publishAgency;
    }


    /**
     * Sets the publishAgency value for this OfficialPublicationResult.
     * 
     * @param publishAgency
     */
    public void setPublishAgency(java.lang.String publishAgency) {
        this.publishAgency = publishAgency;
    }


    /**
     * Gets the publishDate value for this OfficialPublicationResult.
     * 
     * @return publishDate
     */
    public java.lang.String getPublishDate() {
        return publishDate;
    }


    /**
     * Sets the publishDate value for this OfficialPublicationResult.
     * 
     * @param publishDate
     */
    public void setPublishDate(java.lang.String publishDate) {
        this.publishDate = publishDate;
    }


    /**
     * Gets the publishURL value for this OfficialPublicationResult.
     * 
     * @return publishURL
     */
    public java.lang.String getPublishURL() {
        return publishURL;
    }


    /**
     * Sets the publishURL value for this OfficialPublicationResult.
     * 
     * @param publishURL
     */
    public void setPublishURL(java.lang.String publishURL) {
        this.publishURL = publishURL;
    }


    /**
     * Gets the returnCode value for this OfficialPublicationResult.
     * 
     * @return returnCode
     */
    public java.lang.String getReturnCode() {
        return returnCode;
    }


    /**
     * Sets the returnCode value for this OfficialPublicationResult.
     * 
     * @param returnCode
     */
    public void setReturnCode(java.lang.String returnCode) {
        this.returnCode = returnCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OfficialPublicationResult)) return false;
        OfficialPublicationResult other = (OfficialPublicationResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.boeConfirmationURL==null && other.getBoeConfirmationURL()==null) || 
             (this.boeConfirmationURL!=null &&
              this.boeConfirmationURL.equals(other.getBoeConfirmationURL()))) &&
            ((this.publishAgency==null && other.getPublishAgency()==null) || 
             (this.publishAgency!=null &&
              this.publishAgency.equals(other.getPublishAgency()))) &&
            ((this.publishDate==null && other.getPublishDate()==null) || 
             (this.publishDate!=null &&
              this.publishDate.equals(other.getPublishDate()))) &&
            ((this.publishURL==null && other.getPublishURL()==null) || 
             (this.publishURL!=null &&
              this.publishURL.equals(other.getPublishURL()))) &&
            ((this.returnCode==null && other.getReturnCode()==null) || 
             (this.returnCode!=null &&
              this.returnCode.equals(other.getReturnCode())));
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
        if (getBoeConfirmationURL() != null) {
            _hashCode += getBoeConfirmationURL().hashCode();
        }
        if (getPublishAgency() != null) {
            _hashCode += getPublishAgency().hashCode();
        }
        if (getPublishDate() != null) {
            _hashCode += getPublishDate().hashCode();
        }
        if (getPublishURL() != null) {
            _hashCode += getPublishURL().hashCode();
        }
        if (getReturnCode() != null) {
            _hashCode += getReturnCode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OfficialPublicationResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "OfficialPublicationResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("boeConfirmationURL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "boeConfirmationURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publishAgency");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "publishAgency"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publishDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "publishDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publishURL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "publishURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "returnCode"));
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
