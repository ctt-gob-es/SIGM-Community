/**
 * RespuestaAnuncioAnunciosAnuncio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package BDNS.respuestaAnuncio;

public class RespuestaAnuncioAnunciosAnuncio  implements java.io.Serializable {
    private BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioCabecera cabecera;

    private BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtracto extracto;

    public RespuestaAnuncioAnunciosAnuncio() {
    }

    public RespuestaAnuncioAnunciosAnuncio(
           BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioCabecera cabecera,
           BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtracto extracto) {
           this.cabecera = cabecera;
           this.extracto = extracto;
    }


    /**
     * Gets the cabecera value for this RespuestaAnuncioAnunciosAnuncio.
     * 
     * @return cabecera
     */
    public BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioCabecera getCabecera() {
        return cabecera;
    }


    /**
     * Sets the cabecera value for this RespuestaAnuncioAnunciosAnuncio.
     * 
     * @param cabecera
     */
    public void setCabecera(BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioCabecera cabecera) {
        this.cabecera = cabecera;
    }


    /**
     * Gets the extracto value for this RespuestaAnuncioAnunciosAnuncio.
     * 
     * @return extracto
     */
    public BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtracto getExtracto() {
        return extracto;
    }


    /**
     * Sets the extracto value for this RespuestaAnuncioAnunciosAnuncio.
     * 
     * @param extracto
     */
    public void setExtracto(BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtracto extracto) {
        this.extracto = extracto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespuestaAnuncioAnunciosAnuncio)) return false;
        RespuestaAnuncioAnunciosAnuncio other = (RespuestaAnuncioAnunciosAnuncio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cabecera==null && other.getCabecera()==null) || 
             (this.cabecera!=null &&
              this.cabecera.equals(other.getCabecera()))) &&
            ((this.extracto==null && other.getExtracto()==null) || 
             (this.extracto!=null &&
              this.extracto.equals(other.getExtracto())));
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
        if (getCabecera() != null) {
            _hashCode += getCabecera().hashCode();
        }
        if (getExtracto() != null) {
            _hashCode += getExtracto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RespuestaAnuncioAnunciosAnuncio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>RespuestaAnuncio>Anuncios>Anuncio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cabecera");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "Cabecera"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>RespuestaAnuncio>Anuncios>Anuncio>Cabecera"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extracto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "Extracto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>RespuestaAnuncio>Anuncios>Anuncio>Extracto"));
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
