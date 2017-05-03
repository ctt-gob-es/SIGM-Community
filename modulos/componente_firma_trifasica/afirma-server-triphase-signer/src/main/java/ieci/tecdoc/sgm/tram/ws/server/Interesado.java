/**
 * Interesado.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class Interesado  implements java.io.Serializable {
    private java.lang.String idEnTerceros;

    private boolean interesadoPrincipal;

    private java.lang.String nombre;

    private java.lang.String numIdentidad;

    private java.lang.String rol;

    public Interesado() {
    }

    public Interesado(
           java.lang.String idEnTerceros,
           boolean interesadoPrincipal,
           java.lang.String nombre,
           java.lang.String numIdentidad,
           java.lang.String rol) {
           this.idEnTerceros = idEnTerceros;
           this.interesadoPrincipal = interesadoPrincipal;
           this.nombre = nombre;
           this.numIdentidad = numIdentidad;
           this.rol = rol;
    }


    /**
     * Gets the idEnTerceros value for this Interesado.
     * 
     * @return idEnTerceros
     */
    public java.lang.String getIdEnTerceros() {
        return idEnTerceros;
    }


    /**
     * Sets the idEnTerceros value for this Interesado.
     * 
     * @param idEnTerceros
     */
    public void setIdEnTerceros(java.lang.String idEnTerceros) {
        this.idEnTerceros = idEnTerceros;
    }


    /**
     * Gets the interesadoPrincipal value for this Interesado.
     * 
     * @return interesadoPrincipal
     */
    public boolean isInteresadoPrincipal() {
        return interesadoPrincipal;
    }


    /**
     * Sets the interesadoPrincipal value for this Interesado.
     * 
     * @param interesadoPrincipal
     */
    public void setInteresadoPrincipal(boolean interesadoPrincipal) {
        this.interesadoPrincipal = interesadoPrincipal;
    }


    /**
     * Gets the nombre value for this Interesado.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this Interesado.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the numIdentidad value for this Interesado.
     * 
     * @return numIdentidad
     */
    public java.lang.String getNumIdentidad() {
        return numIdentidad;
    }


    /**
     * Sets the numIdentidad value for this Interesado.
     * 
     * @param numIdentidad
     */
    public void setNumIdentidad(java.lang.String numIdentidad) {
        this.numIdentidad = numIdentidad;
    }


    /**
     * Gets the rol value for this Interesado.
     * 
     * @return rol
     */
    public java.lang.String getRol() {
        return rol;
    }


    /**
     * Sets the rol value for this Interesado.
     * 
     * @param rol
     */
    public void setRol(java.lang.String rol) {
        this.rol = rol;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Interesado)) return false;
        Interesado other = (Interesado) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idEnTerceros==null && other.getIdEnTerceros()==null) || 
             (this.idEnTerceros!=null &&
              this.idEnTerceros.equals(other.getIdEnTerceros()))) &&
            this.interesadoPrincipal == other.isInteresadoPrincipal() &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.numIdentidad==null && other.getNumIdentidad()==null) || 
             (this.numIdentidad!=null &&
              this.numIdentidad.equals(other.getNumIdentidad()))) &&
            ((this.rol==null && other.getRol()==null) || 
             (this.rol!=null &&
              this.rol.equals(other.getRol())));
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
        if (getIdEnTerceros() != null) {
            _hashCode += getIdEnTerceros().hashCode();
        }
        _hashCode += (isInteresadoPrincipal() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getNumIdentidad() != null) {
            _hashCode += getNumIdentidad().hashCode();
        }
        if (getRol() != null) {
            _hashCode += getRol().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Interesado.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "Interesado"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idEnTerceros");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "idEnTerceros"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interesadoPrincipal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "interesadoPrincipal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numIdentidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "numIdentidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rol");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "rol"));
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
