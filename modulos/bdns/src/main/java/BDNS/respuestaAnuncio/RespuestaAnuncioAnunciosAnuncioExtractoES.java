/**
 * RespuestaAnuncioAnunciosAnuncioExtractoES.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package BDNS.respuestaAnuncio;

public class RespuestaAnuncioAnunciosAnuncioExtractoES  implements java.io.Serializable {
    private java.lang.String tituloES;

    private java.lang.String[] textoES;

    private BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES pieFirmaES;

    public RespuestaAnuncioAnunciosAnuncioExtractoES() {
    }

    public RespuestaAnuncioAnunciosAnuncioExtractoES(
           java.lang.String tituloES,
           java.lang.String[] textoES,
           BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES pieFirmaES) {
           this.tituloES = tituloES;
           this.textoES = textoES;
           this.pieFirmaES = pieFirmaES;
    }


    /**
     * Gets the tituloES value for this RespuestaAnuncioAnunciosAnuncioExtractoES.
     * 
     * @return tituloES
     */
    public java.lang.String getTituloES() {
        return tituloES;
    }


    /**
     * Sets the tituloES value for this RespuestaAnuncioAnunciosAnuncioExtractoES.
     * 
     * @param tituloES
     */
    public void setTituloES(java.lang.String tituloES) {
        this.tituloES = tituloES;
    }


    /**
     * Gets the textoES value for this RespuestaAnuncioAnunciosAnuncioExtractoES.
     * 
     * @return textoES
     */
    public java.lang.String[] getTextoES() {
        return textoES;
    }


    /**
     * Sets the textoES value for this RespuestaAnuncioAnunciosAnuncioExtractoES.
     * 
     * @param textoES
     */
    public void setTextoES(java.lang.String[] textoES) {
        this.textoES = textoES;
    }


    /**
     * Gets the pieFirmaES value for this RespuestaAnuncioAnunciosAnuncioExtractoES.
     * 
     * @return pieFirmaES
     */
    public BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES getPieFirmaES() {
        return pieFirmaES;
    }


    /**
     * Sets the pieFirmaES value for this RespuestaAnuncioAnunciosAnuncioExtractoES.
     * 
     * @param pieFirmaES
     */
    public void setPieFirmaES(BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoESPieFirmaES pieFirmaES) {
        this.pieFirmaES = pieFirmaES;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespuestaAnuncioAnunciosAnuncioExtractoES)) return false;
        RespuestaAnuncioAnunciosAnuncioExtractoES other = (RespuestaAnuncioAnunciosAnuncioExtractoES) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tituloES==null && other.getTituloES()==null) || 
             (this.tituloES!=null &&
              this.tituloES.equals(other.getTituloES()))) &&
            ((this.textoES==null && other.getTextoES()==null) || 
             (this.textoES!=null &&
              java.util.Arrays.equals(this.textoES, other.getTextoES()))) &&
            ((this.pieFirmaES==null && other.getPieFirmaES()==null) || 
             (this.pieFirmaES!=null &&
              this.pieFirmaES.equals(other.getPieFirmaES())));
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
        if (getTituloES() != null) {
            _hashCode += getTituloES().hashCode();
        }
        if (getTextoES() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTextoES());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTextoES(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPieFirmaES() != null) {
            _hashCode += getPieFirmaES().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RespuestaAnuncioAnunciosAnuncioExtractoES.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>>RespuestaAnuncio>Anuncios>Anuncio>Extracto>ES"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tituloES");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "TituloES"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("textoES");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "TextoES"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "p"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pieFirmaES");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "PieFirmaES"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>>>RespuestaAnuncio>Anuncios>Anuncio>Extracto>ES>PieFirmaES"));
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
