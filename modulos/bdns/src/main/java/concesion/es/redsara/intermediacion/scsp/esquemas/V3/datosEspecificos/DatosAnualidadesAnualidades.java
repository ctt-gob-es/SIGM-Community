/**
 * DatosAnualidadesAnualidades.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosAnualidadesAnualidades  implements java.io.Serializable {
    private concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidadesTipoAnualidad tipoAnualidad;

    private org.apache.axis.types.PositiveInteger anualidad;

    private java.lang.String aplicacion;

    private java.math.BigDecimal importeAnualporApli;

    public DatosAnualidadesAnualidades() {
    }

    public DatosAnualidadesAnualidades(
           concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidadesTipoAnualidad tipoAnualidad,
           org.apache.axis.types.PositiveInteger anualidad,
           java.lang.String aplicacion,
           java.math.BigDecimal importeAnualporApli) {
           this.tipoAnualidad = tipoAnualidad;
           this.anualidad = anualidad;
           this.aplicacion = aplicacion;
           this.importeAnualporApli = importeAnualporApli;
    }


    /**
     * Gets the tipoAnualidad value for this DatosAnualidadesAnualidades.
     * 
     * @return tipoAnualidad
     */
    public concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidadesTipoAnualidad getTipoAnualidad() {
        return tipoAnualidad;
    }


    /**
     * Sets the tipoAnualidad value for this DatosAnualidadesAnualidades.
     * 
     * @param tipoAnualidad
     */
    public void setTipoAnualidad(concesion.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosAnualidadesAnualidadesTipoAnualidad tipoAnualidad) {
        this.tipoAnualidad = tipoAnualidad;
    }


    /**
     * Gets the anualidad value for this DatosAnualidadesAnualidades.
     * 
     * @return anualidad
     */
    public org.apache.axis.types.PositiveInteger getAnualidad() {
        return anualidad;
    }


    /**
     * Sets the anualidad value for this DatosAnualidadesAnualidades.
     * 
     * @param anualidad
     */
    public void setAnualidad(org.apache.axis.types.PositiveInteger anualidad) {
        this.anualidad = anualidad;
    }


    /**
     * Gets the aplicacion value for this DatosAnualidadesAnualidades.
     * 
     * @return aplicacion
     */
    public java.lang.String getAplicacion() {
        return aplicacion;
    }


    /**
     * Sets the aplicacion value for this DatosAnualidadesAnualidades.
     * 
     * @param aplicacion
     */
    public void setAplicacion(java.lang.String aplicacion) {
        this.aplicacion = aplicacion;
    }


    /**
     * Gets the importeAnualporApli value for this DatosAnualidadesAnualidades.
     * 
     * @return importeAnualporApli
     */
    public java.math.BigDecimal getImporteAnualporApli() {
        return importeAnualporApli;
    }


    /**
     * Sets the importeAnualporApli value for this DatosAnualidadesAnualidades.
     * 
     * @param importeAnualporApli
     */
    public void setImporteAnualporApli(java.math.BigDecimal importeAnualporApli) {
        this.importeAnualporApli = importeAnualporApli;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosAnualidadesAnualidades)) return false;
        DatosAnualidadesAnualidades other = (DatosAnualidadesAnualidades) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tipoAnualidad==null && other.getTipoAnualidad()==null) || 
             (this.tipoAnualidad!=null &&
              this.tipoAnualidad.equals(other.getTipoAnualidad()))) &&
            ((this.anualidad==null && other.getAnualidad()==null) || 
             (this.anualidad!=null &&
              this.anualidad.equals(other.getAnualidad()))) &&
            ((this.aplicacion==null && other.getAplicacion()==null) || 
             (this.aplicacion!=null &&
              this.aplicacion.equals(other.getAplicacion()))) &&
            ((this.importeAnualporApli==null && other.getImporteAnualporApli()==null) || 
             (this.importeAnualporApli!=null &&
              this.importeAnualporApli.equals(other.getImporteAnualporApli())));
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
        if (getTipoAnualidad() != null) {
            _hashCode += getTipoAnualidad().hashCode();
        }
        if (getAnualidad() != null) {
            _hashCode += getAnualidad().hashCode();
        }
        if (getAplicacion() != null) {
            _hashCode += getAplicacion().hashCode();
        }
        if (getImporteAnualporApli() != null) {
            _hashCode += getImporteAnualporApli().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosAnualidadesAnualidades.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>DatosAnualidades>Anualidades"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoAnualidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "TipoAnualidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>DatosAnualidades>Anualidades>TipoAnualidad"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anualidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Anualidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "positiveInteger"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Aplicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importeAnualporApli");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ImporteAnualporApli"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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
