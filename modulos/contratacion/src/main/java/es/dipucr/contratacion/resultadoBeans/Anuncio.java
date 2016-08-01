/**
 * Anuncio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.resultadoBeans;

public class Anuncio  implements java.io.Serializable {
    private java.lang.String identifier;

    private java.lang.String issueDate;

    private es.dipucr.contratacion.resultadoBeans.OfficialPublicationResults officialPublicationResults;

    private java.lang.String state;

    private byte[] timeStamping;

    private java.lang.String type;

    private java.lang.String urlHTML;

    private java.lang.String urlPDF;

    private java.lang.String urlXML;

    public Anuncio() {
    }

    public Anuncio(
           java.lang.String identifier,
           java.lang.String issueDate,
           es.dipucr.contratacion.resultadoBeans.OfficialPublicationResults officialPublicationResults,
           java.lang.String state,
           byte[] timeStamping,
           java.lang.String type,
           java.lang.String urlHTML,
           java.lang.String urlPDF,
           java.lang.String urlXML) {
           this.identifier = identifier;
           this.issueDate = issueDate;
           this.officialPublicationResults = officialPublicationResults;
           this.state = state;
           this.timeStamping = timeStamping;
           this.type = type;
           this.urlHTML = urlHTML;
           this.urlPDF = urlPDF;
           this.urlXML = urlXML;
    }


    /**
     * Gets the identifier value for this Anuncio.
     * 
     * @return identifier
     */
    public java.lang.String getIdentifier() {
        return identifier;
    }


    /**
     * Sets the identifier value for this Anuncio.
     * 
     * @param identifier
     */
    public void setIdentifier(java.lang.String identifier) {
        this.identifier = identifier;
    }


    /**
     * Gets the issueDate value for this Anuncio.
     * 
     * @return issueDate
     */
    public java.lang.String getIssueDate() {
        return issueDate;
    }


    /**
     * Sets the issueDate value for this Anuncio.
     * 
     * @param issueDate
     */
    public void setIssueDate(java.lang.String issueDate) {
        this.issueDate = issueDate;
    }


    /**
     * Gets the officialPublicationResults value for this Anuncio.
     * 
     * @return officialPublicationResults
     */
    public es.dipucr.contratacion.resultadoBeans.OfficialPublicationResults getOfficialPublicationResults() {
        return officialPublicationResults;
    }


    /**
     * Sets the officialPublicationResults value for this Anuncio.
     * 
     * @param officialPublicationResults
     */
    public void setOfficialPublicationResults(es.dipucr.contratacion.resultadoBeans.OfficialPublicationResults officialPublicationResults) {
        this.officialPublicationResults = officialPublicationResults;
    }


    /**
     * Gets the state value for this Anuncio.
     * 
     * @return state
     */
    public java.lang.String getState() {
        return state;
    }


    /**
     * Sets the state value for this Anuncio.
     * 
     * @param state
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }


    /**
     * Gets the timeStamping value for this Anuncio.
     * 
     * @return timeStamping
     */
    public byte[] getTimeStamping() {
        return timeStamping;
    }


    /**
     * Sets the timeStamping value for this Anuncio.
     * 
     * @param timeStamping
     */
    public void setTimeStamping(byte[] timeStamping) {
        this.timeStamping = timeStamping;
    }


    /**
     * Gets the type value for this Anuncio.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this Anuncio.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the urlHTML value for this Anuncio.
     * 
     * @return urlHTML
     */
    public java.lang.String getUrlHTML() {
        return urlHTML;
    }


    /**
     * Sets the urlHTML value for this Anuncio.
     * 
     * @param urlHTML
     */
    public void setUrlHTML(java.lang.String urlHTML) {
        this.urlHTML = urlHTML;
    }


    /**
     * Gets the urlPDF value for this Anuncio.
     * 
     * @return urlPDF
     */
    public java.lang.String getUrlPDF() {
        return urlPDF;
    }


    /**
     * Sets the urlPDF value for this Anuncio.
     * 
     * @param urlPDF
     */
    public void setUrlPDF(java.lang.String urlPDF) {
        this.urlPDF = urlPDF;
    }


    /**
     * Gets the urlXML value for this Anuncio.
     * 
     * @return urlXML
     */
    public java.lang.String getUrlXML() {
        return urlXML;
    }


    /**
     * Sets the urlXML value for this Anuncio.
     * 
     * @param urlXML
     */
    public void setUrlXML(java.lang.String urlXML) {
        this.urlXML = urlXML;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Anuncio)) return false;
        Anuncio other = (Anuncio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.identifier==null && other.getIdentifier()==null) || 
             (this.identifier!=null &&
              this.identifier.equals(other.getIdentifier()))) &&
            ((this.issueDate==null && other.getIssueDate()==null) || 
             (this.issueDate!=null &&
              this.issueDate.equals(other.getIssueDate()))) &&
            ((this.officialPublicationResults==null && other.getOfficialPublicationResults()==null) || 
             (this.officialPublicationResults!=null &&
              this.officialPublicationResults.equals(other.getOfficialPublicationResults()))) &&
            ((this.state==null && other.getState()==null) || 
             (this.state!=null &&
              this.state.equals(other.getState()))) &&
            ((this.timeStamping==null && other.getTimeStamping()==null) || 
             (this.timeStamping!=null &&
              java.util.Arrays.equals(this.timeStamping, other.getTimeStamping()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.urlHTML==null && other.getUrlHTML()==null) || 
             (this.urlHTML!=null &&
              this.urlHTML.equals(other.getUrlHTML()))) &&
            ((this.urlPDF==null && other.getUrlPDF()==null) || 
             (this.urlPDF!=null &&
              this.urlPDF.equals(other.getUrlPDF()))) &&
            ((this.urlXML==null && other.getUrlXML()==null) || 
             (this.urlXML!=null &&
              this.urlXML.equals(other.getUrlXML())));
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
        if (getIdentifier() != null) {
            _hashCode += getIdentifier().hashCode();
        }
        if (getIssueDate() != null) {
            _hashCode += getIssueDate().hashCode();
        }
        if (getOfficialPublicationResults() != null) {
            _hashCode += getOfficialPublicationResults().hashCode();
        }
        if (getState() != null) {
            _hashCode += getState().hashCode();
        }
        if (getTimeStamping() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTimeStamping());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTimeStamping(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getUrlHTML() != null) {
            _hashCode += getUrlHTML().hashCode();
        }
        if (getUrlPDF() != null) {
            _hashCode += getUrlPDF().hashCode();
        }
        if (getUrlXML() != null) {
            _hashCode += getUrlXML().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Anuncio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Anuncio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "identifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("issueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "issueDate"));
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
        elemField.setFieldName("state");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "state"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeStamping");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "timeStamping"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urlHTML");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "urlHTML"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urlPDF");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "urlPDF"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urlXML");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "urlXML"));
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
