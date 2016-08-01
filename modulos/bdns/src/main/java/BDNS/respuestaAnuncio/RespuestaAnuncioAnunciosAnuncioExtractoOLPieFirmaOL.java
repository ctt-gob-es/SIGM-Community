/**
 * RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package BDNS.respuestaAnuncio;

public class RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL  implements java.io.Serializable {
    private java.lang.String lugarFirmaOL;

    private java.util.Date fechaFirmaOL;

    private java.lang.String firmanteOL;

    public RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL() {
    }

    public RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL(
           java.lang.String lugarFirmaOL,
           java.util.Date fechaFirmaOL,
           java.lang.String firmanteOL) {
           this.lugarFirmaOL = lugarFirmaOL;
           this.fechaFirmaOL = fechaFirmaOL;
           this.firmanteOL = firmanteOL;
    }


    /**
     * Gets the lugarFirmaOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL.
     * 
     * @return lugarFirmaOL
     */
    public java.lang.String getLugarFirmaOL() {
        return lugarFirmaOL;
    }


    /**
     * Sets the lugarFirmaOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL.
     * 
     * @param lugarFirmaOL
     */
    public void setLugarFirmaOL(java.lang.String lugarFirmaOL) {
        this.lugarFirmaOL = lugarFirmaOL;
    }


    /**
     * Gets the fechaFirmaOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL.
     * 
     * @return fechaFirmaOL
     */
    public java.util.Date getFechaFirmaOL() {
        return fechaFirmaOL;
    }


    /**
     * Sets the fechaFirmaOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL.
     * 
     * @param fechaFirmaOL
     */
    public void setFechaFirmaOL(java.util.Date fechaFirmaOL) {
        this.fechaFirmaOL = fechaFirmaOL;
    }


    /**
     * Gets the firmanteOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL.
     * 
     * @return firmanteOL
     */
    public java.lang.String getFirmanteOL() {
        return firmanteOL;
    }


    /**
     * Sets the firmanteOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL.
     * 
     * @param firmanteOL
     */
    public void setFirmanteOL(java.lang.String firmanteOL) {
        this.firmanteOL = firmanteOL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL)) return false;
        RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL other = (RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.lugarFirmaOL==null && other.getLugarFirmaOL()==null) || 
             (this.lugarFirmaOL!=null &&
              this.lugarFirmaOL.equals(other.getLugarFirmaOL()))) &&
            ((this.fechaFirmaOL==null && other.getFechaFirmaOL()==null) || 
             (this.fechaFirmaOL!=null &&
              this.fechaFirmaOL.equals(other.getFechaFirmaOL()))) &&
            ((this.firmanteOL==null && other.getFirmanteOL()==null) || 
             (this.firmanteOL!=null &&
              this.firmanteOL.equals(other.getFirmanteOL())));
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
        if (getLugarFirmaOL() != null) {
            _hashCode += getLugarFirmaOL().hashCode();
        }
        if (getFechaFirmaOL() != null) {
            _hashCode += getFechaFirmaOL().hashCode();
        }
        if (getFirmanteOL() != null) {
            _hashCode += getFirmanteOL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>>>RespuestaAnuncio>Anuncios>Anuncio>Extracto>OL>PieFirmaOL"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lugarFirmaOL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "LugarFirmaOL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaFirmaOL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "FechaFirmaOL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firmanteOL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "FirmanteOL"));
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
