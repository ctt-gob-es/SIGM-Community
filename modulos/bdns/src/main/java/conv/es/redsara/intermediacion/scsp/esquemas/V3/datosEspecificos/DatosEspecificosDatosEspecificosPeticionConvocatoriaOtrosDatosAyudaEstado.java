/**
 * DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos;

public class DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado  implements java.io.Serializable {
    private java.lang.String autorizacionADE;

    private java.lang.String referenciaUE;

    private java.lang.String reglamento;

    private conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos objetivos;

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado() {
    }

    public DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado(
           java.lang.String autorizacionADE,
           java.lang.String referenciaUE,
           java.lang.String reglamento,
           conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos objetivos) {
           this.autorizacionADE = autorizacionADE;
           this.referenciaUE = referenciaUE;
           this.reglamento = reglamento;
           this.objetivos = objetivos;
    }


    /**
     * Gets the autorizacionADE value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado.
     * 
     * @return autorizacionADE
     */
    public java.lang.String getAutorizacionADE() {
        return autorizacionADE;
    }


    /**
     * Sets the autorizacionADE value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado.
     * 
     * @param autorizacionADE
     */
    public void setAutorizacionADE(java.lang.String autorizacionADE) {
        this.autorizacionADE = autorizacionADE;
    }


    /**
     * Gets the referenciaUE value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado.
     * 
     * @return referenciaUE
     */
    public java.lang.String getReferenciaUE() {
        return referenciaUE;
    }


    /**
     * Sets the referenciaUE value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado.
     * 
     * @param referenciaUE
     */
    public void setReferenciaUE(java.lang.String referenciaUE) {
        this.referenciaUE = referenciaUE;
    }


    /**
     * Gets the reglamento value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado.
     * 
     * @return reglamento
     */
    public java.lang.String getReglamento() {
        return reglamento;
    }


    /**
     * Sets the reglamento value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado.
     * 
     * @param reglamento
     */
    public void setReglamento(java.lang.String reglamento) {
        this.reglamento = reglamento;
    }


    /**
     * Gets the objetivos value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado.
     * 
     * @return objetivos
     */
    public conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos getObjetivos() {
        return objetivos;
    }


    /**
     * Sets the objetivos value for this DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado.
     * 
     * @param objetivos
     */
    public void setObjetivos(conv.es.redsara.intermediacion.scsp.esquemas.V3.datosEspecificos.DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstadoObjetivos objetivos) {
        this.objetivos = objetivos;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado)) return false;
        DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado other = (DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.autorizacionADE==null && other.getAutorizacionADE()==null) || 
             (this.autorizacionADE!=null &&
              this.autorizacionADE.equals(other.getAutorizacionADE()))) &&
            ((this.referenciaUE==null && other.getReferenciaUE()==null) || 
             (this.referenciaUE!=null &&
              this.referenciaUE.equals(other.getReferenciaUE()))) &&
            ((this.reglamento==null && other.getReglamento()==null) || 
             (this.reglamento!=null &&
              this.reglamento.equals(other.getReglamento()))) &&
            ((this.objetivos==null && other.getObjetivos()==null) || 
             (this.objetivos!=null &&
              this.objetivos.equals(other.getObjetivos())));
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
        if (getAutorizacionADE() != null) {
            _hashCode += getAutorizacionADE().hashCode();
        }
        if (getReferenciaUE() != null) {
            _hashCode += getReferenciaUE().hashCode();
        }
        if (getReglamento() != null) {
            _hashCode += getReglamento().hashCode();
        }
        if (getObjetivos() != null) {
            _hashCode += getObjetivos().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosEspecificosDatosEspecificosPeticionConvocatoriaOtrosDatosAyudaEstado.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>OtrosDatos>AyudaEstado"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("autorizacionADE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "AutorizacionADE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referenciaUE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "ReferenciaUE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reglamento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Reglamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objetivos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", "Objetivos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://intermediacion.redsara.es/scsp/esquemas/V3/datosEspecificos", ">>>>>DatosEspecificos>DatosEspecificosPeticion>Convocatoria>OtrosDatos>AyudaEstado>Objetivos"));
        elemField.setMinOccurs(0);
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
