/**
 * DocumentoExpediente.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class DocumentoExpediente  implements java.io.Serializable {
    private java.lang.String code;

    private byte[] content;

    private java.lang.String extension;

    private java.lang.String id;

    private int lenght;

    private java.lang.String name;

    public DocumentoExpediente() {
    }

    public DocumentoExpediente(
           java.lang.String code,
           byte[] content,
           java.lang.String extension,
           java.lang.String id,
           int lenght,
           java.lang.String name) {
           this.code = code;
           this.content = content;
           this.extension = extension;
           this.id = id;
           this.lenght = lenght;
           this.name = name;
    }


    /**
     * Gets the code value for this DocumentoExpediente.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this DocumentoExpediente.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the content value for this DocumentoExpediente.
     * 
     * @return content
     */
    public byte[] getContent() {
        return content;
    }


    /**
     * Sets the content value for this DocumentoExpediente.
     * 
     * @param content
     */
    public void setContent(byte[] content) {
        this.content = content;
    }


    /**
     * Gets the extension value for this DocumentoExpediente.
     * 
     * @return extension
     */
    public java.lang.String getExtension() {
        return extension;
    }


    /**
     * Sets the extension value for this DocumentoExpediente.
     * 
     * @param extension
     */
    public void setExtension(java.lang.String extension) {
        this.extension = extension;
    }


    /**
     * Gets the id value for this DocumentoExpediente.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this DocumentoExpediente.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the lenght value for this DocumentoExpediente.
     * 
     * @return lenght
     */
    public int getLenght() {
        return lenght;
    }


    /**
     * Sets the lenght value for this DocumentoExpediente.
     * 
     * @param lenght
     */
    public void setLenght(int lenght) {
        this.lenght = lenght;
    }


    /**
     * Gets the name value for this DocumentoExpediente.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this DocumentoExpediente.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DocumentoExpediente)) return false;
        DocumentoExpediente other = (DocumentoExpediente) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.code==null && other.getCode()==null) || 
             (this.code!=null &&
              this.code.equals(other.getCode()))) &&
            ((this.content==null && other.getContent()==null) || 
             (this.content!=null &&
              java.util.Arrays.equals(this.content, other.getContent()))) &&
            ((this.extension==null && other.getExtension()==null) || 
             (this.extension!=null &&
              this.extension.equals(other.getExtension()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            this.lenght == other.getLenght() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName())));
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
        if (getCode() != null) {
            _hashCode += getCode().hashCode();
        }
        if (getContent() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContent());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContent(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getExtension() != null) {
            _hashCode += getExtension().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        _hashCode += getLenght();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentoExpediente.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "DocumentoExpediente"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("content");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extension");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "extension"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lenght");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "lenght"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "name"));
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
