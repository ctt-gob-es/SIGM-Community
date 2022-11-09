/**
 * Entidad.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.gestioncsv.ws.client.axis;

public class Entidad  implements java.io.Serializable {
    private java.lang.String codigoINE;

    private java.lang.String deh;

    private java.lang.String dir3;

    private java.lang.String identificador;

    private java.lang.String nombre;

    private java.lang.String nombreCorto;

    private java.lang.String nombreLargo;

    private java.lang.String sia;

    public Entidad() {
    }

    public Entidad(
           java.lang.String codigoINE,
           java.lang.String deh,
           java.lang.String dir3,
           java.lang.String identificador,
           java.lang.String nombre,
           java.lang.String nombreCorto,
           java.lang.String nombreLargo,
           java.lang.String sia) {
           this.codigoINE = codigoINE;
           this.deh = deh;
           this.dir3 = dir3;
           this.identificador = identificador;
           this.nombre = nombre;
           this.nombreCorto = nombreCorto;
           this.nombreLargo = nombreLargo;
           this.sia = sia;
    }


    /**
     * Gets the codigoINE value for this Entidad.
     * 
     * @return codigoINE
     */
    public java.lang.String getCodigoINE() {
        return codigoINE;
    }


    /**
     * Sets the codigoINE value for this Entidad.
     * 
     * @param codigoINE
     */
    public void setCodigoINE(java.lang.String codigoINE) {
        this.codigoINE = codigoINE;
    }


    /**
     * Gets the deh value for this Entidad.
     * 
     * @return deh
     */
    public java.lang.String getDeh() {
        return deh;
    }


    /**
     * Sets the deh value for this Entidad.
     * 
     * @param deh
     */
    public void setDeh(java.lang.String deh) {
        this.deh = deh;
    }


    /**
     * Gets the dir3 value for this Entidad.
     * 
     * @return dir3
     */
    public java.lang.String getDir3() {
        return dir3;
    }


    /**
     * Sets the dir3 value for this Entidad.
     * 
     * @param dir3
     */
    public void setDir3(java.lang.String dir3) {
        this.dir3 = dir3;
    }


    /**
     * Gets the identificador value for this Entidad.
     * 
     * @return identificador
     */
    public java.lang.String getIdentificador() {
        return identificador;
    }


    /**
     * Sets the identificador value for this Entidad.
     * 
     * @param identificador
     */
    public void setIdentificador(java.lang.String identificador) {
        this.identificador = identificador;
    }


    /**
     * Gets the nombre value for this Entidad.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this Entidad.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the nombreCorto value for this Entidad.
     * 
     * @return nombreCorto
     */
    public java.lang.String getNombreCorto() {
        return nombreCorto;
    }


    /**
     * Sets the nombreCorto value for this Entidad.
     * 
     * @param nombreCorto
     */
    public void setNombreCorto(java.lang.String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }


    /**
     * Gets the nombreLargo value for this Entidad.
     * 
     * @return nombreLargo
     */
    public java.lang.String getNombreLargo() {
        return nombreLargo;
    }


    /**
     * Sets the nombreLargo value for this Entidad.
     * 
     * @param nombreLargo
     */
    public void setNombreLargo(java.lang.String nombreLargo) {
        this.nombreLargo = nombreLargo;
    }


    /**
     * Gets the sia value for this Entidad.
     * 
     * @return sia
     */
    public java.lang.String getSia() {
        return sia;
    }


    /**
     * Sets the sia value for this Entidad.
     * 
     * @param sia
     */
    public void setSia(java.lang.String sia) {
        this.sia = sia;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Entidad)) return false;
        Entidad other = (Entidad) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoINE==null && other.getCodigoINE()==null) || 
             (this.codigoINE!=null &&
              this.codigoINE.equals(other.getCodigoINE()))) &&
            ((this.deh==null && other.getDeh()==null) || 
             (this.deh!=null &&
              this.deh.equals(other.getDeh()))) &&
            ((this.dir3==null && other.getDir3()==null) || 
             (this.dir3!=null &&
              this.dir3.equals(other.getDir3()))) &&
            ((this.identificador==null && other.getIdentificador()==null) || 
             (this.identificador!=null &&
              this.identificador.equals(other.getIdentificador()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.nombreCorto==null && other.getNombreCorto()==null) || 
             (this.nombreCorto!=null &&
              this.nombreCorto.equals(other.getNombreCorto()))) &&
            ((this.nombreLargo==null && other.getNombreLargo()==null) || 
             (this.nombreLargo!=null &&
              this.nombreLargo.equals(other.getNombreLargo()))) &&
            ((this.sia==null && other.getSia()==null) || 
             (this.sia!=null &&
              this.sia.equals(other.getSia())));
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
        if (getCodigoINE() != null) {
            _hashCode += getCodigoINE().hashCode();
        }
        if (getDeh() != null) {
            _hashCode += getDeh().hashCode();
        }
        if (getDir3() != null) {
            _hashCode += getDir3().hashCode();
        }
        if (getIdentificador() != null) {
            _hashCode += getIdentificador().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getNombreCorto() != null) {
            _hashCode += getNombreCorto().hashCode();
        }
        if (getNombreLargo() != null) {
            _hashCode += getNombreLargo().hashCode();
        }
        if (getSia() != null) {
            _hashCode += getSia().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Entidad.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dto.services.core.sgm.tecdoc.ieci", "Entidad"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoINE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dto.services.core.sgm.tecdoc.ieci", "codigoINE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deh");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dto.services.core.sgm.tecdoc.ieci", "deh"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dir3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dto.services.core.sgm.tecdoc.ieci", "dir3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dto.services.core.sgm.tecdoc.ieci", "identificador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dto.services.core.sgm.tecdoc.ieci", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreCorto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dto.services.core.sgm.tecdoc.ieci", "nombreCorto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreLargo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dto.services.core.sgm.tecdoc.ieci", "nombreLargo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dto.services.core.sgm.tecdoc.ieci", "sia"));
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
