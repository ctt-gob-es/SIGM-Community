/**
 * InfoBProcedimiento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.tram.ws.server;

public class InfoBProcedimiento  implements java.io.Serializable {
    private java.lang.String codSistProductor;

    private java.lang.String codigo;

    private java.lang.String id;

    private java.lang.String nombre;

    private java.lang.String nombreSistProductor;

    public InfoBProcedimiento() {
    }

    public InfoBProcedimiento(
           java.lang.String codSistProductor,
           java.lang.String codigo,
           java.lang.String id,
           java.lang.String nombre,
           java.lang.String nombreSistProductor) {
           this.codSistProductor = codSistProductor;
           this.codigo = codigo;
           this.id = id;
           this.nombre = nombre;
           this.nombreSistProductor = nombreSistProductor;
    }


    /**
     * Gets the codSistProductor value for this InfoBProcedimiento.
     * 
     * @return codSistProductor
     */
    public java.lang.String getCodSistProductor() {
        return codSistProductor;
    }


    /**
     * Sets the codSistProductor value for this InfoBProcedimiento.
     * 
     * @param codSistProductor
     */
    public void setCodSistProductor(java.lang.String codSistProductor) {
        this.codSistProductor = codSistProductor;
    }


    /**
     * Gets the codigo value for this InfoBProcedimiento.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this InfoBProcedimiento.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the id value for this InfoBProcedimiento.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this InfoBProcedimiento.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the nombre value for this InfoBProcedimiento.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this InfoBProcedimiento.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the nombreSistProductor value for this InfoBProcedimiento.
     * 
     * @return nombreSistProductor
     */
    public java.lang.String getNombreSistProductor() {
        return nombreSistProductor;
    }


    /**
     * Sets the nombreSistProductor value for this InfoBProcedimiento.
     * 
     * @param nombreSistProductor
     */
    public void setNombreSistProductor(java.lang.String nombreSistProductor) {
        this.nombreSistProductor = nombreSistProductor;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InfoBProcedimiento)) return false;
        InfoBProcedimiento other = (InfoBProcedimiento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codSistProductor==null && other.getCodSistProductor()==null) || 
             (this.codSistProductor!=null &&
              this.codSistProductor.equals(other.getCodSistProductor()))) &&
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.nombreSistProductor==null && other.getNombreSistProductor()==null) || 
             (this.nombreSistProductor!=null &&
              this.nombreSistProductor.equals(other.getNombreSistProductor())));
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
        if (getCodSistProductor() != null) {
            _hashCode += getCodSistProductor().hashCode();
        }
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getNombreSistProductor() != null) {
            _hashCode += getNombreSistProductor().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoBProcedimiento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "InfoBProcedimiento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codSistProductor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "codSistProductor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreSistProductor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.tram.sgm.tecdoc.ieci", "nombreSistProductor"));
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
