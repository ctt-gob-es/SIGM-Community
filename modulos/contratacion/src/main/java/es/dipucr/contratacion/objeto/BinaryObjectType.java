/**
 * BinaryObjectType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.objeto;

public class BinaryObjectType  implements java.io.Serializable {
    private java.lang.String characterSetCode;

    private java.lang.String encodingCode;

    private java.lang.String filename;

    private java.lang.String format;

    private java.lang.String mimeCode;

    private java.lang.String uri;

    private byte[] value;

    public BinaryObjectType() {
    }

    public BinaryObjectType(
           java.lang.String characterSetCode,
           java.lang.String encodingCode,
           java.lang.String filename,
           java.lang.String format,
           java.lang.String mimeCode,
           java.lang.String uri,
           byte[] value) {
           this.characterSetCode = characterSetCode;
           this.encodingCode = encodingCode;
           this.filename = filename;
           this.format = format;
           this.mimeCode = mimeCode;
           this.uri = uri;
           this.value = value;
    }


    /**
     * Gets the characterSetCode value for this BinaryObjectType.
     * 
     * @return characterSetCode
     */
    public java.lang.String getCharacterSetCode() {
        return characterSetCode;
    }


    /**
     * Sets the characterSetCode value for this BinaryObjectType.
     * 
     * @param characterSetCode
     */
    public void setCharacterSetCode(java.lang.String characterSetCode) {
        this.characterSetCode = characterSetCode;
    }


    /**
     * Gets the encodingCode value for this BinaryObjectType.
     * 
     * @return encodingCode
     */
    public java.lang.String getEncodingCode() {
        return encodingCode;
    }


    /**
     * Sets the encodingCode value for this BinaryObjectType.
     * 
     * @param encodingCode
     */
    public void setEncodingCode(java.lang.String encodingCode) {
        this.encodingCode = encodingCode;
    }


    /**
     * Gets the filename value for this BinaryObjectType.
     * 
     * @return filename
     */
    public java.lang.String getFilename() {
        return filename;
    }


    /**
     * Sets the filename value for this BinaryObjectType.
     * 
     * @param filename
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }


    /**
     * Gets the format value for this BinaryObjectType.
     * 
     * @return format
     */
    public java.lang.String getFormat() {
        return format;
    }


    /**
     * Sets the format value for this BinaryObjectType.
     * 
     * @param format
     */
    public void setFormat(java.lang.String format) {
        this.format = format;
    }


    /**
     * Gets the mimeCode value for this BinaryObjectType.
     * 
     * @return mimeCode
     */
    public java.lang.String getMimeCode() {
        return mimeCode;
    }


    /**
     * Sets the mimeCode value for this BinaryObjectType.
     * 
     * @param mimeCode
     */
    public void setMimeCode(java.lang.String mimeCode) {
        this.mimeCode = mimeCode;
    }


    /**
     * Gets the uri value for this BinaryObjectType.
     * 
     * @return uri
     */
    public java.lang.String getUri() {
        return uri;
    }


    /**
     * Sets the uri value for this BinaryObjectType.
     * 
     * @param uri
     */
    public void setUri(java.lang.String uri) {
        this.uri = uri;
    }


    /**
     * Gets the value value for this BinaryObjectType.
     * 
     * @return value
     */
    public byte[] getValue() {
        return value;
    }


    /**
     * Sets the value value for this BinaryObjectType.
     * 
     * @param value
     */
    public void setValue(byte[] value) {
        this.value = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BinaryObjectType)) return false;
        BinaryObjectType other = (BinaryObjectType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.characterSetCode==null && other.getCharacterSetCode()==null) || 
             (this.characterSetCode!=null &&
              this.characterSetCode.equals(other.getCharacterSetCode()))) &&
            ((this.encodingCode==null && other.getEncodingCode()==null) || 
             (this.encodingCode!=null &&
              this.encodingCode.equals(other.getEncodingCode()))) &&
            ((this.filename==null && other.getFilename()==null) || 
             (this.filename!=null &&
              this.filename.equals(other.getFilename()))) &&
            ((this.format==null && other.getFormat()==null) || 
             (this.format!=null &&
              this.format.equals(other.getFormat()))) &&
            ((this.mimeCode==null && other.getMimeCode()==null) || 
             (this.mimeCode!=null &&
              this.mimeCode.equals(other.getMimeCode()))) &&
            ((this.uri==null && other.getUri()==null) || 
             (this.uri!=null &&
              this.uri.equals(other.getUri()))) &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              java.util.Arrays.equals(this.value, other.getValue())));
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
        if (getCharacterSetCode() != null) {
            _hashCode += getCharacterSetCode().hashCode();
        }
        if (getEncodingCode() != null) {
            _hashCode += getEncodingCode().hashCode();
        }
        if (getFilename() != null) {
            _hashCode += getFilename().hashCode();
        }
        if (getFormat() != null) {
            _hashCode += getFormat().hashCode();
        }
        if (getMimeCode() != null) {
            _hashCode += getMimeCode().hashCode();
        }
        if (getUri() != null) {
            _hashCode += getUri().hashCode();
        }
        if (getValue() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getValue());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getValue(), i);
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
        new org.apache.axis.description.TypeDesc(BinaryObjectType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://udt.common.ubl.oasis.org", "BinaryObjectType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("characterSetCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://udt.common.ubl.oasis.org", "characterSetCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encodingCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://udt.common.ubl.oasis.org", "encodingCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filename");
        elemField.setXmlName(new javax.xml.namespace.QName("http://udt.common.ubl.oasis.org", "filename"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("format");
        elemField.setXmlName(new javax.xml.namespace.QName("http://udt.common.ubl.oasis.org", "format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mimeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://udt.common.ubl.oasis.org", "mimeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uri");
        elemField.setXmlName(new javax.xml.namespace.QName("http://udt.common.ubl.oasis.org", "uri"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("http://udt.common.ubl.oasis.org", "value"));
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
