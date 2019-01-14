/**
 * DatosGeograficosManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.verifdatos.model.dao;

public class DatosGeograficosManager  implements java.io.Serializable {
    private java.lang.String COD_ESPANIA;

    private java.lang.String ESPANIA;

    private es.dipucr.verifdatos.model.dao.Generico[] comunidades;

    private es.dipucr.verifdatos.model.dao.Provincias[] municipios;

    private es.dipucr.verifdatos.model.dao.Generico[] paises;

    private es.dipucr.verifdatos.model.dao.Comunidades[] provincias;

    public DatosGeograficosManager() {
    }

    public DatosGeograficosManager(
           java.lang.String COD_ESPANIA,
           java.lang.String ESPANIA,
           es.dipucr.verifdatos.model.dao.Generico[] comunidades,
           es.dipucr.verifdatos.model.dao.Provincias[] municipios,
           es.dipucr.verifdatos.model.dao.Generico[] paises,
           es.dipucr.verifdatos.model.dao.Comunidades[] provincias) {
           this.COD_ESPANIA = COD_ESPANIA;
           this.ESPANIA = ESPANIA;
           this.comunidades = comunidades;
           this.municipios = municipios;
           this.paises = paises;
           this.provincias = provincias;
    }


    /**
     * Gets the COD_ESPANIA value for this DatosGeograficosManager.
     * 
     * @return COD_ESPANIA
     */
    public java.lang.String getCOD_ESPANIA() {
        return COD_ESPANIA;
    }


    /**
     * Sets the COD_ESPANIA value for this DatosGeograficosManager.
     * 
     * @param COD_ESPANIA
     */
    public void setCOD_ESPANIA(java.lang.String COD_ESPANIA) {
        this.COD_ESPANIA = COD_ESPANIA;
    }


    /**
     * Gets the ESPANIA value for this DatosGeograficosManager.
     * 
     * @return ESPANIA
     */
    public java.lang.String getESPANIA() {
        return ESPANIA;
    }


    /**
     * Sets the ESPANIA value for this DatosGeograficosManager.
     * 
     * @param ESPANIA
     */
    public void setESPANIA(java.lang.String ESPANIA) {
        this.ESPANIA = ESPANIA;
    }


    /**
     * Gets the comunidades value for this DatosGeograficosManager.
     * 
     * @return comunidades
     */
    public es.dipucr.verifdatos.model.dao.Generico[] getComunidades() {
        return comunidades;
    }


    /**
     * Sets the comunidades value for this DatosGeograficosManager.
     * 
     * @param comunidades
     */
    public void setComunidades(es.dipucr.verifdatos.model.dao.Generico[] comunidades) {
        this.comunidades = comunidades;
    }


    /**
     * Gets the municipios value for this DatosGeograficosManager.
     * 
     * @return municipios
     */
    public es.dipucr.verifdatos.model.dao.Provincias[] getMunicipios() {
        return municipios;
    }


    /**
     * Sets the municipios value for this DatosGeograficosManager.
     * 
     * @param municipios
     */
    public void setMunicipios(es.dipucr.verifdatos.model.dao.Provincias[] municipios) {
        this.municipios = municipios;
    }


    /**
     * Gets the paises value for this DatosGeograficosManager.
     * 
     * @return paises
     */
    public es.dipucr.verifdatos.model.dao.Generico[] getPaises() {
        return paises;
    }


    /**
     * Sets the paises value for this DatosGeograficosManager.
     * 
     * @param paises
     */
    public void setPaises(es.dipucr.verifdatos.model.dao.Generico[] paises) {
        this.paises = paises;
    }


    /**
     * Gets the provincias value for this DatosGeograficosManager.
     * 
     * @return provincias
     */
    public es.dipucr.verifdatos.model.dao.Comunidades[] getProvincias() {
        return provincias;
    }


    /**
     * Sets the provincias value for this DatosGeograficosManager.
     * 
     * @param provincias
     */
    public void setProvincias(es.dipucr.verifdatos.model.dao.Comunidades[] provincias) {
        this.provincias = provincias;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosGeograficosManager)) return false;
        DatosGeograficosManager other = (DatosGeograficosManager) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.COD_ESPANIA==null && other.getCOD_ESPANIA()==null) || 
             (this.COD_ESPANIA!=null &&
              this.COD_ESPANIA.equals(other.getCOD_ESPANIA()))) &&
            ((this.ESPANIA==null && other.getESPANIA()==null) || 
             (this.ESPANIA!=null &&
              this.ESPANIA.equals(other.getESPANIA()))) &&
            ((this.comunidades==null && other.getComunidades()==null) || 
             (this.comunidades!=null &&
              java.util.Arrays.equals(this.comunidades, other.getComunidades()))) &&
            ((this.municipios==null && other.getMunicipios()==null) || 
             (this.municipios!=null &&
              java.util.Arrays.equals(this.municipios, other.getMunicipios()))) &&
            ((this.paises==null && other.getPaises()==null) || 
             (this.paises!=null &&
              java.util.Arrays.equals(this.paises, other.getPaises()))) &&
            ((this.provincias==null && other.getProvincias()==null) || 
             (this.provincias!=null &&
              java.util.Arrays.equals(this.provincias, other.getProvincias())));
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
        if (getCOD_ESPANIA() != null) {
            _hashCode += getCOD_ESPANIA().hashCode();
        }
        if (getESPANIA() != null) {
            _hashCode += getESPANIA().hashCode();
        }
        if (getComunidades() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getComunidades());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getComunidades(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMunicipios() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMunicipios());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMunicipios(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPaises() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPaises());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPaises(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getProvincias() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProvincias());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProvincias(), i);
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
        new org.apache.axis.description.TypeDesc(DatosGeograficosManager.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "DatosGeograficosManager"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("COD_ESPANIA");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "COD_ESPANIA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ESPANIA");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "ESPANIA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comunidades");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "comunidades"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "Generico"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.verifdatos.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("municipios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "municipios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "Provincias"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.verifdatos.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paises");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "paises"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "Generico"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.verifdatos.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("provincias");
        elemField.setXmlName(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "provincias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://dao.model.verifdatos.dipucr.es", "Comunidades"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.verifdatos.dipucr.es", "item"));
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
