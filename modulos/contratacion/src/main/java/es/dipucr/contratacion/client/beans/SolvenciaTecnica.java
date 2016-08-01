/**
 * SolvenciaTecnica.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class SolvenciaTecnica  implements java.io.Serializable {
    private es.dipucr.contratacion.client.beans.Campo criterioSolvencia;

    private java.lang.String[] descripcion;

    private es.dipucr.contratacion.client.beans.Campo expresEvaluarCriterioEvalucion;

    private java.util.Calendar periodoDuracion;

    private java.lang.String valorUmbralImporte;

    private java.lang.String valorUmbralNoImporte;

    public SolvenciaTecnica() {
    }

    public SolvenciaTecnica(
           es.dipucr.contratacion.client.beans.Campo criterioSolvencia,
           java.lang.String[] descripcion,
           es.dipucr.contratacion.client.beans.Campo expresEvaluarCriterioEvalucion,
           java.util.Calendar periodoDuracion,
           java.lang.String valorUmbralImporte,
           java.lang.String valorUmbralNoImporte) {
           this.criterioSolvencia = criterioSolvencia;
           this.descripcion = descripcion;
           this.expresEvaluarCriterioEvalucion = expresEvaluarCriterioEvalucion;
           this.periodoDuracion = periodoDuracion;
           this.valorUmbralImporte = valorUmbralImporte;
           this.valorUmbralNoImporte = valorUmbralNoImporte;
    }


    /**
     * Gets the criterioSolvencia value for this SolvenciaTecnica.
     * 
     * @return criterioSolvencia
     */
    public es.dipucr.contratacion.client.beans.Campo getCriterioSolvencia() {
        return criterioSolvencia;
    }


    /**
     * Sets the criterioSolvencia value for this SolvenciaTecnica.
     * 
     * @param criterioSolvencia
     */
    public void setCriterioSolvencia(es.dipucr.contratacion.client.beans.Campo criterioSolvencia) {
        this.criterioSolvencia = criterioSolvencia;
    }


    /**
     * Gets the descripcion value for this SolvenciaTecnica.
     * 
     * @return descripcion
     */
    public java.lang.String[] getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this SolvenciaTecnica.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String[] descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the expresEvaluarCriterioEvalucion value for this SolvenciaTecnica.
     * 
     * @return expresEvaluarCriterioEvalucion
     */
    public es.dipucr.contratacion.client.beans.Campo getExpresEvaluarCriterioEvalucion() {
        return expresEvaluarCriterioEvalucion;
    }


    /**
     * Sets the expresEvaluarCriterioEvalucion value for this SolvenciaTecnica.
     * 
     * @param expresEvaluarCriterioEvalucion
     */
    public void setExpresEvaluarCriterioEvalucion(es.dipucr.contratacion.client.beans.Campo expresEvaluarCriterioEvalucion) {
        this.expresEvaluarCriterioEvalucion = expresEvaluarCriterioEvalucion;
    }


    /**
     * Gets the periodoDuracion value for this SolvenciaTecnica.
     * 
     * @return periodoDuracion
     */
    public java.util.Calendar getPeriodoDuracion() {
        return periodoDuracion;
    }


    /**
     * Sets the periodoDuracion value for this SolvenciaTecnica.
     * 
     * @param periodoDuracion
     */
    public void setPeriodoDuracion(java.util.Calendar periodoDuracion) {
        this.periodoDuracion = periodoDuracion;
    }


    /**
     * Gets the valorUmbralImporte value for this SolvenciaTecnica.
     * 
     * @return valorUmbralImporte
     */
    public java.lang.String getValorUmbralImporte() {
        return valorUmbralImporte;
    }


    /**
     * Sets the valorUmbralImporte value for this SolvenciaTecnica.
     * 
     * @param valorUmbralImporte
     */
    public void setValorUmbralImporte(java.lang.String valorUmbralImporte) {
        this.valorUmbralImporte = valorUmbralImporte;
    }


    /**
     * Gets the valorUmbralNoImporte value for this SolvenciaTecnica.
     * 
     * @return valorUmbralNoImporte
     */
    public java.lang.String getValorUmbralNoImporte() {
        return valorUmbralNoImporte;
    }


    /**
     * Sets the valorUmbralNoImporte value for this SolvenciaTecnica.
     * 
     * @param valorUmbralNoImporte
     */
    public void setValorUmbralNoImporte(java.lang.String valorUmbralNoImporte) {
        this.valorUmbralNoImporte = valorUmbralNoImporte;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SolvenciaTecnica)) return false;
        SolvenciaTecnica other = (SolvenciaTecnica) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.criterioSolvencia==null && other.getCriterioSolvencia()==null) || 
             (this.criterioSolvencia!=null &&
              this.criterioSolvencia.equals(other.getCriterioSolvencia()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              java.util.Arrays.equals(this.descripcion, other.getDescripcion()))) &&
            ((this.expresEvaluarCriterioEvalucion==null && other.getExpresEvaluarCriterioEvalucion()==null) || 
             (this.expresEvaluarCriterioEvalucion!=null &&
              this.expresEvaluarCriterioEvalucion.equals(other.getExpresEvaluarCriterioEvalucion()))) &&
            ((this.periodoDuracion==null && other.getPeriodoDuracion()==null) || 
             (this.periodoDuracion!=null &&
              this.periodoDuracion.equals(other.getPeriodoDuracion()))) &&
            ((this.valorUmbralImporte==null && other.getValorUmbralImporte()==null) || 
             (this.valorUmbralImporte!=null &&
              this.valorUmbralImporte.equals(other.getValorUmbralImporte()))) &&
            ((this.valorUmbralNoImporte==null && other.getValorUmbralNoImporte()==null) || 
             (this.valorUmbralNoImporte!=null &&
              this.valorUmbralNoImporte.equals(other.getValorUmbralNoImporte())));
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
        if (getCriterioSolvencia() != null) {
            _hashCode += getCriterioSolvencia().hashCode();
        }
        if (getDescripcion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescripcion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescripcion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getExpresEvaluarCriterioEvalucion() != null) {
            _hashCode += getExpresEvaluarCriterioEvalucion().hashCode();
        }
        if (getPeriodoDuracion() != null) {
            _hashCode += getPeriodoDuracion().hashCode();
        }
        if (getValorUmbralImporte() != null) {
            _hashCode += getValorUmbralImporte().hashCode();
        }
        if (getValorUmbralNoImporte() != null) {
            _hashCode += getValorUmbralNoImporte().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SolvenciaTecnica.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "SolvenciaTecnica"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("criterioSolvencia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "criterioSolvencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expresEvaluarCriterioEvalucion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "expresEvaluarCriterioEvalucion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("periodoDuracion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "periodoDuracion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valorUmbralImporte");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "valorUmbralImporte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valorUmbralNoImporte");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "valorUmbralNoImporte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
