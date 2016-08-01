/**
 * CondicionesLicitadores.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class CondicionesLicitadores  implements java.io.Serializable {
    private java.lang.String anosExperiencia;

    private java.lang.String[] descripcion;

    private java.lang.String[] formaLegal;

    private java.lang.String numMinEmpleados;

    private java.lang.String[] situacionPersonal;

    public CondicionesLicitadores() {
    }

    public CondicionesLicitadores(
           java.lang.String anosExperiencia,
           java.lang.String[] descripcion,
           java.lang.String[] formaLegal,
           java.lang.String numMinEmpleados,
           java.lang.String[] situacionPersonal) {
           this.anosExperiencia = anosExperiencia;
           this.descripcion = descripcion;
           this.formaLegal = formaLegal;
           this.numMinEmpleados = numMinEmpleados;
           this.situacionPersonal = situacionPersonal;
    }


    /**
     * Gets the anosExperiencia value for this CondicionesLicitadores.
     * 
     * @return anosExperiencia
     */
    public java.lang.String getAnosExperiencia() {
        return anosExperiencia;
    }


    /**
     * Sets the anosExperiencia value for this CondicionesLicitadores.
     * 
     * @param anosExperiencia
     */
    public void setAnosExperiencia(java.lang.String anosExperiencia) {
        this.anosExperiencia = anosExperiencia;
    }


    /**
     * Gets the descripcion value for this CondicionesLicitadores.
     * 
     * @return descripcion
     */
    public java.lang.String[] getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this CondicionesLicitadores.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String[] descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the formaLegal value for this CondicionesLicitadores.
     * 
     * @return formaLegal
     */
    public java.lang.String[] getFormaLegal() {
        return formaLegal;
    }


    /**
     * Sets the formaLegal value for this CondicionesLicitadores.
     * 
     * @param formaLegal
     */
    public void setFormaLegal(java.lang.String[] formaLegal) {
        this.formaLegal = formaLegal;
    }


    /**
     * Gets the numMinEmpleados value for this CondicionesLicitadores.
     * 
     * @return numMinEmpleados
     */
    public java.lang.String getNumMinEmpleados() {
        return numMinEmpleados;
    }


    /**
     * Sets the numMinEmpleados value for this CondicionesLicitadores.
     * 
     * @param numMinEmpleados
     */
    public void setNumMinEmpleados(java.lang.String numMinEmpleados) {
        this.numMinEmpleados = numMinEmpleados;
    }


    /**
     * Gets the situacionPersonal value for this CondicionesLicitadores.
     * 
     * @return situacionPersonal
     */
    public java.lang.String[] getSituacionPersonal() {
        return situacionPersonal;
    }


    /**
     * Sets the situacionPersonal value for this CondicionesLicitadores.
     * 
     * @param situacionPersonal
     */
    public void setSituacionPersonal(java.lang.String[] situacionPersonal) {
        this.situacionPersonal = situacionPersonal;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CondicionesLicitadores)) return false;
        CondicionesLicitadores other = (CondicionesLicitadores) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.anosExperiencia==null && other.getAnosExperiencia()==null) || 
             (this.anosExperiencia!=null &&
              this.anosExperiencia.equals(other.getAnosExperiencia()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              java.util.Arrays.equals(this.descripcion, other.getDescripcion()))) &&
            ((this.formaLegal==null && other.getFormaLegal()==null) || 
             (this.formaLegal!=null &&
              java.util.Arrays.equals(this.formaLegal, other.getFormaLegal()))) &&
            ((this.numMinEmpleados==null && other.getNumMinEmpleados()==null) || 
             (this.numMinEmpleados!=null &&
              this.numMinEmpleados.equals(other.getNumMinEmpleados()))) &&
            ((this.situacionPersonal==null && other.getSituacionPersonal()==null) || 
             (this.situacionPersonal!=null &&
              java.util.Arrays.equals(this.situacionPersonal, other.getSituacionPersonal())));
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
        if (getAnosExperiencia() != null) {
            _hashCode += getAnosExperiencia().hashCode();
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
        if (getFormaLegal() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFormaLegal());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFormaLegal(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNumMinEmpleados() != null) {
            _hashCode += getNumMinEmpleados().hashCode();
        }
        if (getSituacionPersonal() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSituacionPersonal());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSituacionPersonal(), i);
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
        new org.apache.axis.description.TypeDesc(CondicionesLicitadores.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "CondicionesLicitadores"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anosExperiencia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "anosExperiencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("formaLegal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "formaLegal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numMinEmpleados");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "numMinEmpleados"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("situacionPersonal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "situacionPersonal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
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
