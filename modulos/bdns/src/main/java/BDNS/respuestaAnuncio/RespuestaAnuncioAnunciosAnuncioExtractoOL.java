/**
 * RespuestaAnuncioAnunciosAnuncioExtractoOL.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package BDNS.respuestaAnuncio;

public class RespuestaAnuncioAnunciosAnuncioExtractoOL  implements java.io.Serializable {
    private java.lang.String tituloOL;

    private java.lang.String[] textoOL;

    private BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL pieFirmaOL;

    public RespuestaAnuncioAnunciosAnuncioExtractoOL() {
    }

    public RespuestaAnuncioAnunciosAnuncioExtractoOL(
           java.lang.String tituloOL,
           java.lang.String[] textoOL,
           BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL pieFirmaOL) {
           this.tituloOL = tituloOL;
           this.textoOL = textoOL;
           this.pieFirmaOL = pieFirmaOL;
    }


    /**
     * Gets the tituloOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOL.
     * 
     * @return tituloOL
     */
    public java.lang.String getTituloOL() {
        return tituloOL;
    }


    /**
     * Sets the tituloOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOL.
     * 
     * @param tituloOL
     */
    public void setTituloOL(java.lang.String tituloOL) {
        this.tituloOL = tituloOL;
    }


    /**
     * Gets the textoOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOL.
     * 
     * @return textoOL
     */
    public java.lang.String[] getTextoOL() {
        return textoOL;
    }


    /**
     * Sets the textoOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOL.
     * 
     * @param textoOL
     */
    public void setTextoOL(java.lang.String[] textoOL) {
        this.textoOL = textoOL;
    }


    /**
     * Gets the pieFirmaOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOL.
     * 
     * @return pieFirmaOL
     */
    public BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL getPieFirmaOL() {
        return pieFirmaOL;
    }


    /**
     * Sets the pieFirmaOL value for this RespuestaAnuncioAnunciosAnuncioExtractoOL.
     * 
     * @param pieFirmaOL
     */
    public void setPieFirmaOL(BDNS.respuestaAnuncio.RespuestaAnuncioAnunciosAnuncioExtractoOLPieFirmaOL pieFirmaOL) {
        this.pieFirmaOL = pieFirmaOL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespuestaAnuncioAnunciosAnuncioExtractoOL)) return false;
        RespuestaAnuncioAnunciosAnuncioExtractoOL other = (RespuestaAnuncioAnunciosAnuncioExtractoOL) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tituloOL==null && other.getTituloOL()==null) || 
             (this.tituloOL!=null &&
              this.tituloOL.equals(other.getTituloOL()))) &&
            ((this.textoOL==null && other.getTextoOL()==null) || 
             (this.textoOL!=null &&
              java.util.Arrays.equals(this.textoOL, other.getTextoOL()))) &&
            ((this.pieFirmaOL==null && other.getPieFirmaOL()==null) || 
             (this.pieFirmaOL!=null &&
              this.pieFirmaOL.equals(other.getPieFirmaOL())));
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
        if (getTituloOL() != null) {
            _hashCode += getTituloOL().hashCode();
        }
        if (getTextoOL() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTextoOL());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTextoOL(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPieFirmaOL() != null) {
            _hashCode += getPieFirmaOL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RespuestaAnuncioAnunciosAnuncioExtractoOL.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>>RespuestaAnuncio>Anuncios>Anuncio>Extracto>OL"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tituloOL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "TituloOL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("textoOL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "TextoOL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "p"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pieFirmaOL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", "PieFirmaOL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://BDNS/respuestaAnuncio", ">>>>>>RespuestaAnuncio>Anuncios>Anuncio>Extracto>OL>PieFirmaOL"));
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
