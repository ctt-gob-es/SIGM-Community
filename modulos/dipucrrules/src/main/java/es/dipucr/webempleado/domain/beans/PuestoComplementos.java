/**
 * PuestoComplementos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.domain.beans;

public class PuestoComplementos  implements java.io.Serializable {
    private double compDestino;

    private es.dipucr.webempleado.model.mapping.Puestos datosPuesto;

    private es.dipucr.webempleado.model.mapping.Fper020 empleado;

    private es.dipucr.webempleado.model.mapping.HorPersonal horario;

    private es.dipucr.webempleado.model.mapping.Fper210 puesto;

    public PuestoComplementos() {
    }

    public PuestoComplementos(
           double compDestino,
           es.dipucr.webempleado.model.mapping.Puestos datosPuesto,
           es.dipucr.webempleado.model.mapping.Fper020 empleado,
           es.dipucr.webempleado.model.mapping.HorPersonal horario,
           es.dipucr.webempleado.model.mapping.Fper210 puesto) {
           this.compDestino = compDestino;
           this.datosPuesto = datosPuesto;
           this.empleado = empleado;
           this.horario = horario;
           this.puesto = puesto;
    }


    /**
     * Gets the compDestino value for this PuestoComplementos.
     * 
     * @return compDestino
     */
    public double getCompDestino() {
        return compDestino;
    }


    /**
     * Sets the compDestino value for this PuestoComplementos.
     * 
     * @param compDestino
     */
    public void setCompDestino(double compDestino) {
        this.compDestino = compDestino;
    }


    /**
     * Gets the datosPuesto value for this PuestoComplementos.
     * 
     * @return datosPuesto
     */
    public es.dipucr.webempleado.model.mapping.Puestos getDatosPuesto() {
        return datosPuesto;
    }


    /**
     * Sets the datosPuesto value for this PuestoComplementos.
     * 
     * @param datosPuesto
     */
    public void setDatosPuesto(es.dipucr.webempleado.model.mapping.Puestos datosPuesto) {
        this.datosPuesto = datosPuesto;
    }


    /**
     * Gets the empleado value for this PuestoComplementos.
     * 
     * @return empleado
     */
    public es.dipucr.webempleado.model.mapping.Fper020 getEmpleado() {
        return empleado;
    }


    /**
     * Sets the empleado value for this PuestoComplementos.
     * 
     * @param empleado
     */
    public void setEmpleado(es.dipucr.webempleado.model.mapping.Fper020 empleado) {
        this.empleado = empleado;
    }


    /**
     * Gets the horario value for this PuestoComplementos.
     * 
     * @return horario
     */
    public es.dipucr.webempleado.model.mapping.HorPersonal getHorario() {
        return horario;
    }


    /**
     * Sets the horario value for this PuestoComplementos.
     * 
     * @param horario
     */
    public void setHorario(es.dipucr.webempleado.model.mapping.HorPersonal horario) {
        this.horario = horario;
    }


    /**
     * Gets the puesto value for this PuestoComplementos.
     * 
     * @return puesto
     */
    public es.dipucr.webempleado.model.mapping.Fper210 getPuesto() {
        return puesto;
    }


    /**
     * Sets the puesto value for this PuestoComplementos.
     * 
     * @param puesto
     */
    public void setPuesto(es.dipucr.webempleado.model.mapping.Fper210 puesto) {
        this.puesto = puesto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PuestoComplementos)) return false;
        PuestoComplementos other = (PuestoComplementos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.compDestino == other.getCompDestino() &&
            ((this.datosPuesto==null && other.getDatosPuesto()==null) || 
             (this.datosPuesto!=null &&
              this.datosPuesto.equals(other.getDatosPuesto()))) &&
            ((this.empleado==null && other.getEmpleado()==null) || 
             (this.empleado!=null &&
              this.empleado.equals(other.getEmpleado()))) &&
            ((this.horario==null && other.getHorario()==null) || 
             (this.horario!=null &&
              this.horario.equals(other.getHorario()))) &&
            ((this.puesto==null && other.getPuesto()==null) || 
             (this.puesto!=null &&
              this.puesto.equals(other.getPuesto())));
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
        _hashCode += new Double(getCompDestino()).hashCode();
        if (getDatosPuesto() != null) {
            _hashCode += getDatosPuesto().hashCode();
        }
        if (getEmpleado() != null) {
            _hashCode += getEmpleado().hashCode();
        }
        if (getHorario() != null) {
            _hashCode += getHorario().hashCode();
        }
        if (getPuesto() != null) {
            _hashCode += getPuesto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PuestoComplementos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "PuestoComplementos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("compDestino");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "compDestino"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datosPuesto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "datosPuesto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Puestos"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empleado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "empleado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Fper020"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("horario");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "horario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "HorPersonal"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("puesto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.domain.webempleado.dipucr.es", "puesto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Fper210"));
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
