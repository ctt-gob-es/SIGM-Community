/**
 * CriteriosAdjudicacion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class CriteriosAdjudicacion  implements java.io.Serializable {
    private es.dipucr.contratacion.client.beans.Campo codigoAlgoritmo;

    private es.dipucr.contratacion.client.beans.CriterioAdjudicacionMultCrit[] critAdjudicacionMultCrit;

    private java.lang.String[] descripcion;

    private java.lang.String[] descripcionBajaTemeraria;

    private java.lang.String[] descripcionComiteTecnico;

    private es.dipucr.contratacion.client.beans.PersonaComite[] persComite;

    private es.dipucr.contratacion.client.beans.Campo tipoAdjudicacion;

    private es.dipucr.contratacion.client.beans.Campo valoracionTipoOferta;

    public CriteriosAdjudicacion() {
    }

    public CriteriosAdjudicacion(
           es.dipucr.contratacion.client.beans.Campo codigoAlgoritmo,
           es.dipucr.contratacion.client.beans.CriterioAdjudicacionMultCrit[] critAdjudicacionMultCrit,
           java.lang.String[] descripcion,
           java.lang.String[] descripcionBajaTemeraria,
           java.lang.String[] descripcionComiteTecnico,
           es.dipucr.contratacion.client.beans.PersonaComite[] persComite,
           es.dipucr.contratacion.client.beans.Campo tipoAdjudicacion,
           es.dipucr.contratacion.client.beans.Campo valoracionTipoOferta) {
           this.codigoAlgoritmo = codigoAlgoritmo;
           this.critAdjudicacionMultCrit = critAdjudicacionMultCrit;
           this.descripcion = descripcion;
           this.descripcionBajaTemeraria = descripcionBajaTemeraria;
           this.descripcionComiteTecnico = descripcionComiteTecnico;
           this.persComite = persComite;
           this.tipoAdjudicacion = tipoAdjudicacion;
           this.valoracionTipoOferta = valoracionTipoOferta;
    }


    /**
     * Gets the codigoAlgoritmo value for this CriteriosAdjudicacion.
     * 
     * @return codigoAlgoritmo
     */
    public es.dipucr.contratacion.client.beans.Campo getCodigoAlgoritmo() {
        return codigoAlgoritmo;
    }


    /**
     * Sets the codigoAlgoritmo value for this CriteriosAdjudicacion.
     * 
     * @param codigoAlgoritmo
     */
    public void setCodigoAlgoritmo(es.dipucr.contratacion.client.beans.Campo codigoAlgoritmo) {
        this.codigoAlgoritmo = codigoAlgoritmo;
    }


    /**
     * Gets the critAdjudicacionMultCrit value for this CriteriosAdjudicacion.
     * 
     * @return critAdjudicacionMultCrit
     */
    public es.dipucr.contratacion.client.beans.CriterioAdjudicacionMultCrit[] getCritAdjudicacionMultCrit() {
        return critAdjudicacionMultCrit;
    }


    /**
     * Sets the critAdjudicacionMultCrit value for this CriteriosAdjudicacion.
     * 
     * @param critAdjudicacionMultCrit
     */
    public void setCritAdjudicacionMultCrit(es.dipucr.contratacion.client.beans.CriterioAdjudicacionMultCrit[] critAdjudicacionMultCrit) {
        this.critAdjudicacionMultCrit = critAdjudicacionMultCrit;
    }


    /**
     * Gets the descripcion value for this CriteriosAdjudicacion.
     * 
     * @return descripcion
     */
    public java.lang.String[] getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this CriteriosAdjudicacion.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String[] descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the descripcionBajaTemeraria value for this CriteriosAdjudicacion.
     * 
     * @return descripcionBajaTemeraria
     */
    public java.lang.String[] getDescripcionBajaTemeraria() {
        return descripcionBajaTemeraria;
    }


    /**
     * Sets the descripcionBajaTemeraria value for this CriteriosAdjudicacion.
     * 
     * @param descripcionBajaTemeraria
     */
    public void setDescripcionBajaTemeraria(java.lang.String[] descripcionBajaTemeraria) {
        this.descripcionBajaTemeraria = descripcionBajaTemeraria;
    }


    /**
     * Gets the descripcionComiteTecnico value for this CriteriosAdjudicacion.
     * 
     * @return descripcionComiteTecnico
     */
    public java.lang.String[] getDescripcionComiteTecnico() {
        return descripcionComiteTecnico;
    }


    /**
     * Sets the descripcionComiteTecnico value for this CriteriosAdjudicacion.
     * 
     * @param descripcionComiteTecnico
     */
    public void setDescripcionComiteTecnico(java.lang.String[] descripcionComiteTecnico) {
        this.descripcionComiteTecnico = descripcionComiteTecnico;
    }


    /**
     * Gets the persComite value for this CriteriosAdjudicacion.
     * 
     * @return persComite
     */
    public es.dipucr.contratacion.client.beans.PersonaComite[] getPersComite() {
        return persComite;
    }


    /**
     * Sets the persComite value for this CriteriosAdjudicacion.
     * 
     * @param persComite
     */
    public void setPersComite(es.dipucr.contratacion.client.beans.PersonaComite[] persComite) {
        this.persComite = persComite;
    }


    /**
     * Gets the tipoAdjudicacion value for this CriteriosAdjudicacion.
     * 
     * @return tipoAdjudicacion
     */
    public es.dipucr.contratacion.client.beans.Campo getTipoAdjudicacion() {
        return tipoAdjudicacion;
    }


    /**
     * Sets the tipoAdjudicacion value for this CriteriosAdjudicacion.
     * 
     * @param tipoAdjudicacion
     */
    public void setTipoAdjudicacion(es.dipucr.contratacion.client.beans.Campo tipoAdjudicacion) {
        this.tipoAdjudicacion = tipoAdjudicacion;
    }


    /**
     * Gets the valoracionTipoOferta value for this CriteriosAdjudicacion.
     * 
     * @return valoracionTipoOferta
     */
    public es.dipucr.contratacion.client.beans.Campo getValoracionTipoOferta() {
        return valoracionTipoOferta;
    }


    /**
     * Sets the valoracionTipoOferta value for this CriteriosAdjudicacion.
     * 
     * @param valoracionTipoOferta
     */
    public void setValoracionTipoOferta(es.dipucr.contratacion.client.beans.Campo valoracionTipoOferta) {
        this.valoracionTipoOferta = valoracionTipoOferta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CriteriosAdjudicacion)) return false;
        CriteriosAdjudicacion other = (CriteriosAdjudicacion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoAlgoritmo==null && other.getCodigoAlgoritmo()==null) || 
             (this.codigoAlgoritmo!=null &&
              this.codigoAlgoritmo.equals(other.getCodigoAlgoritmo()))) &&
            ((this.critAdjudicacionMultCrit==null && other.getCritAdjudicacionMultCrit()==null) || 
             (this.critAdjudicacionMultCrit!=null &&
              java.util.Arrays.equals(this.critAdjudicacionMultCrit, other.getCritAdjudicacionMultCrit()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              java.util.Arrays.equals(this.descripcion, other.getDescripcion()))) &&
            ((this.descripcionBajaTemeraria==null && other.getDescripcionBajaTemeraria()==null) || 
             (this.descripcionBajaTemeraria!=null &&
              java.util.Arrays.equals(this.descripcionBajaTemeraria, other.getDescripcionBajaTemeraria()))) &&
            ((this.descripcionComiteTecnico==null && other.getDescripcionComiteTecnico()==null) || 
             (this.descripcionComiteTecnico!=null &&
              java.util.Arrays.equals(this.descripcionComiteTecnico, other.getDescripcionComiteTecnico()))) &&
            ((this.persComite==null && other.getPersComite()==null) || 
             (this.persComite!=null &&
              java.util.Arrays.equals(this.persComite, other.getPersComite()))) &&
            ((this.tipoAdjudicacion==null && other.getTipoAdjudicacion()==null) || 
             (this.tipoAdjudicacion!=null &&
              this.tipoAdjudicacion.equals(other.getTipoAdjudicacion()))) &&
            ((this.valoracionTipoOferta==null && other.getValoracionTipoOferta()==null) || 
             (this.valoracionTipoOferta!=null &&
              this.valoracionTipoOferta.equals(other.getValoracionTipoOferta())));
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
        if (getCodigoAlgoritmo() != null) {
            _hashCode += getCodigoAlgoritmo().hashCode();
        }
        if (getCritAdjudicacionMultCrit() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCritAdjudicacionMultCrit());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCritAdjudicacionMultCrit(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
        if (getDescripcionBajaTemeraria() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescripcionBajaTemeraria());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescripcionBajaTemeraria(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDescripcionComiteTecnico() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescripcionComiteTecnico());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescripcionComiteTecnico(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPersComite() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPersComite());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPersComite(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTipoAdjudicacion() != null) {
            _hashCode += getTipoAdjudicacion().hashCode();
        }
        if (getValoracionTipoOferta() != null) {
            _hashCode += getValoracionTipoOferta().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CriteriosAdjudicacion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "CriteriosAdjudicacion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoAlgoritmo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "codigoAlgoritmo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("critAdjudicacionMultCrit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "critAdjudicacionMultCrit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "CriterioAdjudicacionMultCrit"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionBajaTemeraria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descripcionBajaTemeraria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionComiteTecnico");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descripcionComiteTecnico"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("persComite");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "persComite"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PersonaComite"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoAdjudicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "tipoAdjudicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valoracionTipoOferta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "valoracionTipoOferta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
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
