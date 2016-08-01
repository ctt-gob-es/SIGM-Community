/**
 * Resultado.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.resultadoBeans;

public class Resultado  implements java.io.Serializable {
    private es.dipucr.contratacion.resultadoBeans.PlaceAskResult placeAskResult;

    private es.dipucr.contratacion.resultadoBeans.PublicationResult publicacion;

    private es.dipucr.contratacion.resultadoBeans.VisualizationResult visualizacion;

    public Resultado() {
    }

    public Resultado(
           es.dipucr.contratacion.resultadoBeans.PlaceAskResult placeAskResult,
           es.dipucr.contratacion.resultadoBeans.PublicationResult publicacion,
           es.dipucr.contratacion.resultadoBeans.VisualizationResult visualizacion) {
           this.placeAskResult = placeAskResult;
           this.publicacion = publicacion;
           this.visualizacion = visualizacion;
    }


    /**
     * Gets the placeAskResult value for this Resultado.
     * 
     * @return placeAskResult
     */
    public es.dipucr.contratacion.resultadoBeans.PlaceAskResult getPlaceAskResult() {
        return placeAskResult;
    }


    /**
     * Sets the placeAskResult value for this Resultado.
     * 
     * @param placeAskResult
     */
    public void setPlaceAskResult(es.dipucr.contratacion.resultadoBeans.PlaceAskResult placeAskResult) {
        this.placeAskResult = placeAskResult;
    }


    /**
     * Gets the publicacion value for this Resultado.
     * 
     * @return publicacion
     */
    public es.dipucr.contratacion.resultadoBeans.PublicationResult getPublicacion() {
        return publicacion;
    }


    /**
     * Sets the publicacion value for this Resultado.
     * 
     * @param publicacion
     */
    public void setPublicacion(es.dipucr.contratacion.resultadoBeans.PublicationResult publicacion) {
        this.publicacion = publicacion;
    }


    /**
     * Gets the visualizacion value for this Resultado.
     * 
     * @return visualizacion
     */
    public es.dipucr.contratacion.resultadoBeans.VisualizationResult getVisualizacion() {
        return visualizacion;
    }


    /**
     * Sets the visualizacion value for this Resultado.
     * 
     * @param visualizacion
     */
    public void setVisualizacion(es.dipucr.contratacion.resultadoBeans.VisualizationResult visualizacion) {
        this.visualizacion = visualizacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Resultado)) return false;
        Resultado other = (Resultado) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.placeAskResult==null && other.getPlaceAskResult()==null) || 
             (this.placeAskResult!=null &&
              this.placeAskResult.equals(other.getPlaceAskResult()))) &&
            ((this.publicacion==null && other.getPublicacion()==null) || 
             (this.publicacion!=null &&
              this.publicacion.equals(other.getPublicacion()))) &&
            ((this.visualizacion==null && other.getVisualizacion()==null) || 
             (this.visualizacion!=null &&
              this.visualizacion.equals(other.getVisualizacion())));
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
        if (getPlaceAskResult() != null) {
            _hashCode += getPlaceAskResult().hashCode();
        }
        if (getPublicacion() != null) {
            _hashCode += getPublicacion().hashCode();
        }
        if (getVisualizacion() != null) {
            _hashCode += getVisualizacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Resultado.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "Resultado"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placeAskResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "placeAskResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "PlaceAskResult"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "publicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "PublicationResult"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("visualizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "visualizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resultadoBeans.contratacion.dipucr.es", "VisualizationResult"));
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
