/**
 * Puestos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class Puestos  implements java.io.Serializable {
    private java.lang.String CPto;

    private java.lang.String NAbrevPto;

    private java.lang.String NCompletoPto;

    private java.lang.String NPto;

    private java.lang.String colegio;

    public Puestos() {
    }

    public Puestos(
           java.lang.String CPto,
           java.lang.String NAbrevPto,
           java.lang.String NCompletoPto,
           java.lang.String NPto,
           java.lang.String colegio) {
           this.CPto = CPto;
           this.NAbrevPto = NAbrevPto;
           this.NCompletoPto = NCompletoPto;
           this.NPto = NPto;
           this.colegio = colegio;
    }


    /**
     * Gets the CPto value for this Puestos.
     * 
     * @return CPto
     */
    public java.lang.String getCPto() {
        return CPto;
    }


    /**
     * Sets the CPto value for this Puestos.
     * 
     * @param CPto
     */
    public void setCPto(java.lang.String CPto) {
        this.CPto = CPto;
    }


    /**
     * Gets the NAbrevPto value for this Puestos.
     * 
     * @return NAbrevPto
     */
    public java.lang.String getNAbrevPto() {
        return NAbrevPto;
    }


    /**
     * Sets the NAbrevPto value for this Puestos.
     * 
     * @param NAbrevPto
     */
    public void setNAbrevPto(java.lang.String NAbrevPto) {
        this.NAbrevPto = NAbrevPto;
    }


    /**
     * Gets the NCompletoPto value for this Puestos.
     * 
     * @return NCompletoPto
     */
    public java.lang.String getNCompletoPto() {
        return NCompletoPto;
    }


    /**
     * Sets the NCompletoPto value for this Puestos.
     * 
     * @param NCompletoPto
     */
    public void setNCompletoPto(java.lang.String NCompletoPto) {
        this.NCompletoPto = NCompletoPto;
    }


    /**
     * Gets the NPto value for this Puestos.
     * 
     * @return NPto
     */
    public java.lang.String getNPto() {
        return NPto;
    }


    /**
     * Sets the NPto value for this Puestos.
     * 
     * @param NPto
     */
    public void setNPto(java.lang.String NPto) {
        this.NPto = NPto;
    }


    /**
     * Gets the colegio value for this Puestos.
     * 
     * @return colegio
     */
    public java.lang.String getColegio() {
        return colegio;
    }


    /**
     * Sets the colegio value for this Puestos.
     * 
     * @param colegio
     */
    public void setColegio(java.lang.String colegio) {
        this.colegio = colegio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Puestos)) return false;
        Puestos other = (Puestos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CPto==null && other.getCPto()==null) || 
             (this.CPto!=null &&
              this.CPto.equals(other.getCPto()))) &&
            ((this.NAbrevPto==null && other.getNAbrevPto()==null) || 
             (this.NAbrevPto!=null &&
              this.NAbrevPto.equals(other.getNAbrevPto()))) &&
            ((this.NCompletoPto==null && other.getNCompletoPto()==null) || 
             (this.NCompletoPto!=null &&
              this.NCompletoPto.equals(other.getNCompletoPto()))) &&
            ((this.NPto==null && other.getNPto()==null) || 
             (this.NPto!=null &&
              this.NPto.equals(other.getNPto()))) &&
            ((this.colegio==null && other.getColegio()==null) || 
             (this.colegio!=null &&
              this.colegio.equals(other.getColegio())));
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
        if (getCPto() != null) {
            _hashCode += getCPto().hashCode();
        }
        if (getNAbrevPto() != null) {
            _hashCode += getNAbrevPto().hashCode();
        }
        if (getNCompletoPto() != null) {
            _hashCode += getNCompletoPto().hashCode();
        }
        if (getNPto() != null) {
            _hashCode += getNPto().hashCode();
        }
        if (getColegio() != null) {
            _hashCode += getColegio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Puestos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "Puestos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CPto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "CPto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NAbrevPto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "NAbrevPto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NCompletoPto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "NCompletoPto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NPto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "NPto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("colegio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "colegio"));
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
