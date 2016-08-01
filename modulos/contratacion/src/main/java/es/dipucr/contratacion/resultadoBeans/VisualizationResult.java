/**
 * VisualizationResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.resultadoBeans;

public class VisualizationResult  implements java.io.Serializable {
    private org.dgpe.codice.common.cbclib.EmbeddedDocumentBinaryObjectType document;

    private java.lang.String resultCode;

    private java.lang.String resultCodeDescription;

    private es.dipucr.contratacion.resultadoBeans.GeneralErrorDetails viewErrorDetails;

    public VisualizationResult() {
    }

    public VisualizationResult(
           org.dgpe.codice.common.cbclib.EmbeddedDocumentBinaryObjectType document,
           java.lang.String resultCode,
           java.lang.String resultCodeDescription,
           es.dipucr.contratacion.resultadoBeans.GeneralErrorDetails viewErrorDetails) {
           this.document = document;
           this.resultCode = resultCode;
           this.resultCodeDescription = resultCodeDescription;
           this.viewErrorDetails = viewErrorDetails;
    }


    /**
     * Gets the document value for this VisualizationResult.
     * 
     * @return document
     */
    public org.dgpe.codice.common.cbclib.EmbeddedDocumentBinaryObjectType getDocument() {
        return document;
    }


    /**
     * Sets the document value for this VisualizationResult.
     * 
     * @param document
     */
    public void setDocument(org.dgpe.codice.common.cbclib.EmbeddedDocumentBinaryObjectType document) {
        this.document = document;
    }


    /**
     * Gets the resultCode value for this VisualizationResult.
     * 
     * @return resultCode
     */
    public java.lang.String getResultCode() {
        return resultCode;
    }


    /**
     * Sets the resultCode value for this VisualizationResult.
     * 
     * @param resultCode
     */
    public void setResultCode(java.lang.String resultCode) {
        this.resultCode = resultCode;
    }


    /**
     * Gets the resultCodeDescription value for this VisualizationResult.
     * 
     * @return resultCodeDescription
     */
    public java.lang.String getResultCodeDescription() {
        return resultCodeDescription;
    }


    /**
     * Sets the resultCodeDescription value for this VisualizationResult.
     * 
     * @param resultCodeDescription
     */
    public void setResultCodeDescription(java.lang.String resultCodeDescription) {
        this.resultCodeDescription = resultCodeDescription;
    }


    /**
     * Gets the viewErrorDetails value for this VisualizationResult.
     * 
     * @return viewErrorDetails
     */
    public es.dipucr.contratacion.resultadoBeans.GeneralErrorDetails getViewErrorDetails() {
        return viewErrorDetails;
    }


    /**
     * Sets the viewErrorDetails value for this VisualizationResult.
     * 
     * @param viewErrorDetails
     */
    public void setViewErrorDetails(es.dipucr.contratacion.resultadoBeans.GeneralErrorDetails viewErrorDetails) {
        this.viewErrorDetails = viewErrorDetails;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VisualizationResult)) return false;
        VisualizationResult other = (VisualizationResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.document==null && other.getDocument()==null) || 
             (this.document!=null &&
              this.document.equals(other.getDocument()))) &&
            ((this.resultCode==null && other.getResultCode()==null) || 
             (this.resultCode!=null &&
              this.resultCode.equals(other.getResultCode()))) &&
            ((this.resultCodeDescription==null && other.getResultCodeDescription()==null) || 
             (this.resultCodeDescription!=null &&
              this.resultCodeDescription.equals(other.getResultCodeDescription()))) &&
            ((this.viewErrorDetails==null && other.getViewErrorDetails()==null) || 
             (this.viewErrorDetails!=null &&
              this.viewErrorDetails.equals(other.getViewErrorDetails())));
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
        if (getDocument() != null) {
            _hashCode += getDocument().hashCode();
        }
        if (getResultCode() != null) {
            _hashCode += getResultCode().hashCode();
        }
        if (getResultCodeDescription() != null) {
            _hashCode += getResultCodeDescription().hashCode();
        }
        if (getViewErrorDetails() != null) {
            _hashCode += getViewErrorDetails().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VisualizationResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "VisualizationResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("document");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "document"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://cbclib.common.codice.dgpe.org", "EmbeddedDocumentBinaryObjectType"));
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
        elemField.setFieldName("viewErrorDetails");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "viewErrorDetails"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "GeneralErrorDetails"));
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
