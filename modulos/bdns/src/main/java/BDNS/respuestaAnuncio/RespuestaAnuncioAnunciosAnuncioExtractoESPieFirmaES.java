/**
 * RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package BDNS.respuestaAnuncio;

public class RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES  implements java.io.Serializable {
    private java.lang.String lugarFirmaES;

    private java.util.Date fechaFirmaES;

    private java.lang.String firmanteES;

    public RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES() {
    }

    public RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES(
           java.lang.String lugarFirmaES,
           java.util.Date fechaFirmaES,
           java.lang.String firmanteES) {
           this.lugarFirmaES = lugarFirmaES;
           this.fechaFirmaES = fechaFirmaES;
           this.firmanteES = firmanteES;
    }


    /**
     * Gets the lugarFirmaES value for this RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES.
     * 
     * @return lugarFirmaES
     */
    public java.lang.String getLugarFirmaES() {
        return lugarFirmaES;
    }


    /**
     * Sets the lugarFirmaES value for this RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES.
     * 
     * @param lugarFirmaES
     */
    public void setLugarFirmaES(java.lang.String lugarFirmaES) {
        this.lugarFirmaES = lugarFirmaES;
    }


    /**
     * Gets the fechaFirmaES value for this RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES.
     * 
     * @return fechaFirmaES
     */
    public java.util.Date getFechaFirmaES() {
        return fechaFirmaES;
    }


    /**
     * Sets the fechaFirmaES value for this RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES.
     * 
     * @param fechaFirmaES
     */
    public void setFechaFirmaES(java.util.Date fechaFirmaES) {
        this.fechaFirmaES = fechaFirmaES;
    }


    /**
     * Gets the firmanteES value for this RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES.
     * 
     * @return firmanteES
     */
    public java.lang.String getFirmanteES() {
        return firmanteES;
    }


    /**
     * Sets the firmanteES value for this RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES.
     * 
     * @param firmanteES
     */
    public void setFirmanteES(java.lang.String firmanteES) {
        this.firmanteES = firmanteES;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES)) return false;
        RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES other = (RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lugarFirmaES==null && other.getLugarFirmaES()==null) || 
             (this.lugarFirmaES!=null &&
              this.lugarFirmaES.equals(other.getLugarFirmaES()))) &&
            ((this.fechaFirmaES==null && other.getFechaFirmaES()==null) || 
             (this.fechaFirmaES!=null &&
              this.fechaFirmaES.equals(other.getFechaFirmaES()))) &&
            ((this.firmanteES==null && other.getFirmanteES()==null) || 
             (this.firmanteES!=null &&
              this.firmanteES.equals(other.getFirmanteES())));
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
        if (getLugarFirmaES() != null) {
            _hashCode += getLugarFirmaES().hashCode();
        }
        if (getFechaFirmaES() != null) {
            _hashCode += getFechaFirmaES().hashCode();
        }
        if (getFirmanteES() != null) {
            _hashCode += getFirmanteES().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>>>RespuestaAnuncio>Anuncios>Anuncio>Extracto>ES>PieFirmaES"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lugarFirmaES");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "LugarFirmaES"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFirmaES");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "FechaFirmaES"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firmanteES");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "FirmanteES"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
