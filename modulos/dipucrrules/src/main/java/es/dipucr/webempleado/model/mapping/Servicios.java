/**
 * Servicios.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class Servicios  implements java.io.Serializable {
    private java.lang.String alias;

    private java.lang.String codServ;

    private java.lang.String direccion;

    private java.lang.String nomServ;

    private java.lang.String numFunc;

    public Servicios() {
    }

    public Servicios(
           java.lang.String alias,
           java.lang.String codServ,
           java.lang.String direccion,
           java.lang.String nomServ,
           java.lang.String numFunc) {
           this.alias = alias;
           this.codServ = codServ;
           this.direccion = direccion;
           this.nomServ = nomServ;
           this.numFunc = numFunc;
    }


    /**
     * Gets the alias value for this Servicios.
     * 
     * @return alias
     */
    public java.lang.String getAlias() {
        return alias;
    }


    /**
     * Sets the alias value for this Servicios.
     * 
     * @param alias
     */
    public void setAlias(java.lang.String alias) {
        this.alias = alias;
    }


    /**
     * Gets the codServ value for this Servicios.
     * 
     * @return codServ
     */
    public java.lang.String getCodServ() {
        return codServ;
    }


    /**
     * Sets the codServ value for this Servicios.
     * 
     * @param codServ
     */
    public void setCodServ(java.lang.String codServ) {
        this.codServ = codServ;
    }


    /**
     * Gets the direccion value for this Servicios.
     * 
     * @return direccion
     */
    public java.lang.String getDireccion() {
        return direccion;
    }


    /**
     * Sets the direccion value for this Servicios.
     * 
     * @param direccion
     */
    public void setDireccion(java.lang.String direccion) {
        this.direccion = direccion;
    }


    /**
     * Gets the nomServ value for this Servicios.
     * 
     * @return nomServ
     */
    public java.lang.String getNomServ() {
        return nomServ;
    }


    /**
     * Sets the nomServ value for this Servicios.
     * 
     * @param nomServ
     */
    public void setNomServ(java.lang.String nomServ) {
        this.nomServ = nomServ;
    }


    /**
     * Gets the numFunc value for this Servicios.
     * 
     * @return numFunc
     */
    public java.lang.String getNumFunc() {
        return numFunc;
    }


    /**
     * Sets the numFunc value for this Servicios.
     * 
     * @param numFunc
     */
    public void setNumFunc(java.lang.String numFunc) {
        this.numFunc = numFunc;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Servicios)) return false;
        Servicios other = (Servicios) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.alias==null && other.getAlias()==null) || 
             (this.alias!=null &&
              this.alias.equals(other.getAlias()))) &&
            ((this.codServ==null && other.getCodServ()==null) || 
             (this.codServ!=null &&
              this.codServ.equals(other.getCodServ()))) &&
            ((this.direccion==null && other.getDireccion()==null) || 
             (this.direccion!=null &&
              this.direccion.equals(other.getDireccion()))) &&
            ((this.nomServ==null && other.getNomServ()==null) || 
             (this.nomServ!=null &&
              this.nomServ.equals(other.getNomServ()))) &&
            ((this.numFunc==null && other.getNumFunc()==null) || 
             (this.numFunc!=null &&
              this.numFunc.equals(other.getNumFunc())));
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
        if (getAlias() != null) {
            _hashCode += getAlias().hashCode();
        }
        if (getCodServ() != null) {
            _hashCode += getCodServ().hashCode();
        }
        if (getDireccion() != null) {
            _hashCode += getDireccion().hashCode();
        }
        if (getNomServ() != null) {
            _hashCode += getNomServ().hashCode();
        }
        if (getNumFunc() != null) {
            _hashCode += getNumFunc().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Servicios.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Servicios"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alias");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "alias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codServ");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "codServ"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("direccion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "direccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomServ");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "nomServ"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numFunc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "numFunc"));
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
