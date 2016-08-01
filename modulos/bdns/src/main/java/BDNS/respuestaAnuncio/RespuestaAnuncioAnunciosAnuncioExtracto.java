/**
 * RespuestaAnuncioAnunciosAnuncioExtracto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package BDNS.respuestaAnuncio;

public class RespuestaAnuncioAnunciosAnuncioExtracto  implements java.io.Serializable {
    private BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoES ES;

    private BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoOL OL;

    public RespuestaAnuncioAnunciosAnuncioExtracto() {
    }

    public RespuestaAnuncioAnunciosAnuncioExtracto(
           BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoES ES,
           BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoOL OL) {
           this.ES = ES;
           this.OL = OL;
    }


    /**
     * Gets the ES value for this RespuestaAnuncioAnunciosAnuncioExtracto.
     * 
     * @return ES
     */
    public BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoES getES() {
        return ES;
    }


    /**
     * Sets the ES value for this RespuestaAnuncioAnunciosAnuncioExtracto.
     * 
     * @param ES
     */
    public void setES(BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoES ES) {
        this.ES = ES;
    }


    /**
     * Gets the OL value for this RespuestaAnuncioAnunciosAnuncioExtracto.
     * 
     * @return OL
     */
    public BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoOL getOL() {
        return OL;
    }


    /**
     * Sets the OL value for this RespuestaAnuncioAnunciosAnuncioExtracto.
     * 
     * @param OL
     */
    public void setOL(BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoOL OL) {
        this.OL = OL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespuestaAnuncioAnunciosAnuncioExtracto)) return false;
        RespuestaAnuncioAnunciosAnuncioExtracto other = (RespuestaAnuncioAnunciosAnuncioExtracto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ES==null && other.getES()==null) || 
             (this.ES!=null &&
              this.ES.equals(other.getES()))) &&
            ((this.OL==null && other.getOL()==null) || 
             (this.OL!=null &&
              this.OL.equals(other.getOL())));
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
        if (getES() != null) {
            _hashCode += getES().hashCode();
        }
        if (getOL() != null) {
            _hashCode += getOL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RespuestaAnuncioAnunciosAnuncioExtracto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>RespuestaAnuncio>Anuncios>Anuncio>Extracto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ES");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "ES"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>>RespuestaAnuncio>Anuncios>Anuncio>Extracto>ES"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("OL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "OL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>>RespuestaAnuncio>Anuncios>Anuncio>Extracto>OL"));
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
