/**
 * PublicacionAnuncio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package BDNS.publicacionAnuncio;

public class PublicacionAnuncio  implements java.io.Serializable {
    private java.lang.String idPeticion;

    private java.lang.String timeStamp;

    private BDNS.publicacionAnuncio.PublicacionAnuncioAnunciosAnuncio[] anuncios;

    public PublicacionAnuncio() {
    }

    public PublicacionAnuncio(
           java.lang.String idPeticion,
           java.lang.String timeStamp,
           BDNS.publicacionAnuncio.PublicacionAnuncioAnunciosAnuncio[] anuncios) {
           this.idPeticion = idPeticion;
           this.timeStamp = timeStamp;
           this.anuncios = anuncios;
    }


    /**
     * Gets the idPeticion value for this PublicacionAnuncio.
     * 
     * @return idPeticion
     */
    public java.lang.String getIdPeticion() {
        return idPeticion;
    }


    /**
     * Sets the idPeticion value for this PublicacionAnuncio.
     * 
     * @param idPeticion
     */
    public void setIdPeticion(java.lang.String idPeticion) {
        this.idPeticion = idPeticion;
    }


    /**
     * Gets the timeStamp value for this PublicacionAnuncio.
     * 
     * @return timeStamp
     */
    public java.lang.String getTimeStamp() {
        return timeStamp;
    }


    /**
     * Sets the timeStamp value for this PublicacionAnuncio.
     * 
     * @param timeStamp
     */
    public void setTimeStamp(java.lang.String timeStamp) {
        this.timeStamp = timeStamp;
    }


    /**
     * Gets the anuncios value for this PublicacionAnuncio.
     * 
     * @return anuncios
     */
    public BDNS.publicacionAnuncio.PublicacionAnuncioAnunciosAnuncio[] getAnuncios() {
        return anuncios;
    }


    /**
     * Sets the anuncios value for this PublicacionAnuncio.
     * 
     * @param anuncios
     */
    public void setAnuncios(BDNS.publicacionAnuncio.PublicacionAnuncioAnunciosAnuncio[] anuncios) {
        this.anuncios = anuncios;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PublicacionAnuncio)) return false;
        PublicacionAnuncio other = (PublicacionAnuncio) obj;
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
            ((this.anuncios==null && other.getAnuncios()==null) || 
             (this.anuncios!=null &&
              java.util.Arrays.equals(this.anuncios, other.getAnuncios())));
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
        if (getAnuncios() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAnuncios());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAnuncios(), i);
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
        new org.apache.axis.description.TypeDesc(PublicacionAnuncio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", ">PublicacionAnuncio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPeticion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", "IdPeticion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", ">IdPeticion"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeStamp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", "TimeStamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", ">TimeStamp"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anuncios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", "Anuncios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", ">>>PublicacionAnuncio>Anuncios>Anuncio"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://BDNS/publicacionAnuncio", "Anuncio"));
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
