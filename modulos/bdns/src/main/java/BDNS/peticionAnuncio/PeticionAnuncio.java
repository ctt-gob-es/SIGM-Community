/**
 * PeticionAnuncio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package BDNS.peticionAnuncio;

public class PeticionAnuncio  implements java.io.Serializable {
    private java.lang.String idPeticion;

    private java.lang.String timeStamp;

    private java.lang.Integer idAnuncio;

    public PeticionAnuncio() {
    }

    public PeticionAnuncio(
           java.lang.String idPeticion,
           java.lang.String timeStamp,
           java.lang.Integer idAnuncio) {
           this.idPeticion = idPeticion;
           this.timeStamp = timeStamp;
           this.idAnuncio = idAnuncio;
    }


    /**
     * Gets the idPeticion value for this PeticionAnuncio.
     * 
     * @return idPeticion
     */
    public java.lang.String getIdPeticion() {
        return idPeticion;
    }


    /**
     * Sets the idPeticion value for this PeticionAnuncio.
     * 
     * @param idPeticion
     */
    public void setIdPeticion(java.lang.String idPeticion) {
        this.idPeticion = idPeticion;
    }


    /**
     * Gets the timeStamp value for this PeticionAnuncio.
     * 
     * @return timeStamp
     */
    public java.lang.String getTimeStamp() {
        return timeStamp;
    }


    /**
     * Sets the timeStamp value for this PeticionAnuncio.
     * 
     * @param timeStamp
     */
    public void setTimeStamp(java.lang.String timeStamp) {
        this.timeStamp = timeStamp;
    }


    /**
     * Gets the idAnuncio value for this PeticionAnuncio.
     * 
     * @return idAnuncio
     */
    public java.lang.Integer getIdAnuncio() {
        return idAnuncio;
    }


    /**
     * Sets the idAnuncio value for this PeticionAnuncio.
     * 
     * @param idAnuncio
     */
    public void setIdAnuncio(java.lang.Integer idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PeticionAnuncio)) return false;
        PeticionAnuncio other = (PeticionAnuncio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idPeticion==null && other.getIdPeticion()==null) || 
             (this.idPeticion!=null &&
              this.idPeticion.equals(other.getIdPeticion()))) &&
            ((this.timeStamp==null && other.getTimeStamp()==null) || 
             (this.timeStamp!=null &&
              this.timeStamp.equals(other.getTimeStamp()))) &&
            ((this.idAnuncio==null && other.getIdAnuncio()==null) || 
             (this.idAnuncio!=null &&
              this.idAnuncio.equals(other.getIdAnuncio())));
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
        if (getIdPeticion() != null) {
            _hashCode += getIdPeticion().hashCode();
        }
        if (getTimeStamp() != null) {
            _hashCode += getTimeStamp().hashCode();
        }
        if (getIdAnuncio() != null) {
            _hashCode += getIdAnuncio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PeticionAnuncio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://BDNS/peticionAnuncio", ">PeticionAnuncio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPeticion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/peticionAnuncio", "IdPeticion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/peticionAnuncio", ">IdPeticion"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeStamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/peticionAnuncio", "TimeStamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/peticionAnuncio", ">TimeStamp"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idAnuncio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/peticionAnuncio", "IdAnuncio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
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
