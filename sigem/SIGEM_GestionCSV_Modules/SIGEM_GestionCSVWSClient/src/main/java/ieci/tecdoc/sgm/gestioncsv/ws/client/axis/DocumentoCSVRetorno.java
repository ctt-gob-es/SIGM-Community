/**
 * DocumentoCSVRetorno.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.gestioncsv.ws.client.axis;

public class DocumentoCSVRetorno  extends ieci.tecdoc.sgm.gestioncsv.ws.client.axis.InfoDocumentoCSVRetorno  implements java.io.Serializable {
    private byte[] contenido;

    public DocumentoCSVRetorno() {
    }

    public DocumentoCSVRetorno(
           java.lang.String errorCode,
           java.lang.String returnCode,
           java.lang.String codigoAplicacion,
           java.lang.String csv,
           boolean disponible,
           java.util.Calendar fechaCSV,
           java.util.Calendar fechaCaducidad,
           java.util.Calendar fechaCreacion,
           java.lang.String id,
           java.lang.String nombre,
           java.lang.String nombreAplicacion,
           java.lang.String tipoMime,
           byte[] contenido) {
        super(
            errorCode,
            returnCode,
            codigoAplicacion,
            csv,
            disponible,
            fechaCSV,
            fechaCaducidad,
            fechaCreacion,
            id,
            nombre,
            nombreAplicacion,
            tipoMime);
        this.contenido = contenido;
    }


    /**
     * Gets the contenido value for this DocumentoCSVRetorno.
     * 
     * @return contenido
     */
    public byte[] getContenido() {
        return contenido;
    }


    /**
     * Sets the contenido value for this DocumentoCSVRetorno.
     * 
     * @param contenido
     */
    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DocumentoCSVRetorno)) return false;
        DocumentoCSVRetorno other = (DocumentoCSVRetorno) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.contenido==null && other.getContenido()==null) || 
             (this.contenido!=null &&
              java.util.Arrays.equals(this.contenido, other.getContenido())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getContenido() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContenido());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContenido(), i);
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
        new org.apache.axis.description.TypeDesc(DocumentoCSVRetorno.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dto.server.ws.gestioncsv.sgm.tecdoc.ieci", "DocumentoCSVRetorno"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contenido");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dto.server.ws.gestioncsv.sgm.tecdoc.ieci", "contenido"));
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
