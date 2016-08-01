/**
 * InfoTrienios.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class InfoTrienios  implements java.io.Serializable {
    private java.lang.String categoria;

    private java.lang.String dni;

    private java.util.Calendar fechaAbsoluta;

    private java.util.Calendar fechaTrienio;

    private java.lang.String grupo;

    private java.lang.String nombre_completo;

    private int numTrienios;

    public InfoTrienios() {
    }

    public InfoTrienios(
           java.lang.String categoria,
           java.lang.String dni,
           java.util.Calendar fechaAbsoluta,
           java.util.Calendar fechaTrienio,
           java.lang.String grupo,
           java.lang.String nombre_completo,
           int numTrienios) {
           this.categoria = categoria;
           this.dni = dni;
           this.fechaAbsoluta = fechaAbsoluta;
           this.fechaTrienio = fechaTrienio;
           this.grupo = grupo;
           this.nombre_completo = nombre_completo;
           this.numTrienios = numTrienios;
    }


    /**
     * Gets the categoria value for this InfoTrienios.
     * 
     * @return categoria
     */
    public java.lang.String getCategoria() {
        return categoria;
    }


    /**
     * Sets the categoria value for this InfoTrienios.
     * 
     * @param categoria
     */
    public void setCategoria(java.lang.String categoria) {
        this.categoria = categoria;
    }


    /**
     * Gets the dni value for this InfoTrienios.
     * 
     * @return dni
     */
    public java.lang.String getDni() {
        return dni;
    }


    /**
     * Sets the dni value for this InfoTrienios.
     * 
     * @param dni
     */
    public void setDni(java.lang.String dni) {
        this.dni = dni;
    }


    /**
     * Gets the fechaAbsoluta value for this InfoTrienios.
     * 
     * @return fechaAbsoluta
     */
    public java.util.Calendar getFechaAbsoluta() {
        return fechaAbsoluta;
    }


    /**
     * Sets the fechaAbsoluta value for this InfoTrienios.
     * 
     * @param fechaAbsoluta
     */
    public void setFechaAbsoluta(java.util.Calendar fechaAbsoluta) {
        this.fechaAbsoluta = fechaAbsoluta;
    }


    /**
     * Gets the fechaTrienio value for this InfoTrienios.
     * 
     * @return fechaTrienio
     */
    public java.util.Calendar getFechaTrienio() {
        return fechaTrienio;
    }


    /**
     * Sets the fechaTrienio value for this InfoTrienios.
     * 
     * @param fechaTrienio
     */
    public void setFechaTrienio(java.util.Calendar fechaTrienio) {
        this.fechaTrienio = fechaTrienio;
    }


    /**
     * Gets the grupo value for this InfoTrienios.
     * 
     * @return grupo
     */
    public java.lang.String getGrupo() {
        return grupo;
    }


    /**
     * Sets the grupo value for this InfoTrienios.
     * 
     * @param grupo
     */
    public void setGrupo(java.lang.String grupo) {
        this.grupo = grupo;
    }


    /**
     * Gets the nombre_completo value for this InfoTrienios.
     * 
     * @return nombre_completo
     */
    public java.lang.String getNombre_completo() {
        return nombre_completo;
    }


    /**
     * Sets the nombre_completo value for this InfoTrienios.
     * 
     * @param nombre_completo
     */
    public void setNombre_completo(java.lang.String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }


    /**
     * Gets the numTrienios value for this InfoTrienios.
     * 
     * @return numTrienios
     */
    public int getNumTrienios() {
        return numTrienios;
    }


    /**
     * Sets the numTrienios value for this InfoTrienios.
     * 
     * @param numTrienios
     */
    public void setNumTrienios(int numTrienios) {
        this.numTrienios = numTrienios;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InfoTrienios)) return false;
        InfoTrienios other = (InfoTrienios) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.categoria==null && other.getCategoria()==null) || 
             (this.categoria!=null &&
              this.categoria.equals(other.getCategoria()))) &&
            ((this.dni==null && other.getDni()==null) || 
             (this.dni!=null &&
              this.dni.equals(other.getDni()))) &&
            ((this.fechaAbsoluta==null && other.getFechaAbsoluta()==null) || 
             (this.fechaAbsoluta!=null &&
              this.fechaAbsoluta.equals(other.getFechaAbsoluta()))) &&
            ((this.fechaTrienio==null && other.getFechaTrienio()==null) || 
             (this.fechaTrienio!=null &&
              this.fechaTrienio.equals(other.getFechaTrienio()))) &&
            ((this.grupo==null && other.getGrupo()==null) || 
             (this.grupo!=null &&
              this.grupo.equals(other.getGrupo()))) &&
            ((this.nombre_completo==null && other.getNombre_completo()==null) || 
             (this.nombre_completo!=null &&
              this.nombre_completo.equals(other.getNombre_completo()))) &&
            this.numTrienios == other.getNumTrienios();
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
        if (getCategoria() != null) {
            _hashCode += getCategoria().hashCode();
        }
        if (getDni() != null) {
            _hashCode += getDni().hashCode();
        }
        if (getFechaAbsoluta() != null) {
            _hashCode += getFechaAbsoluta().hashCode();
        }
        if (getFechaTrienio() != null) {
            _hashCode += getFechaTrienio().hashCode();
        }
        if (getGrupo() != null) {
            _hashCode += getGrupo().hashCode();
        }
        if (getNombre_completo() != null) {
            _hashCode += getNombre_completo().hashCode();
        }
        _hashCode += getNumTrienios();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoTrienios.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "InfoTrienios"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "categoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dni");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "dni"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaAbsoluta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "fechaAbsoluta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaTrienio");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "fechaTrienio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("grupo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "grupo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre_completo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "nombre_completo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numTrienios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "numTrienios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
