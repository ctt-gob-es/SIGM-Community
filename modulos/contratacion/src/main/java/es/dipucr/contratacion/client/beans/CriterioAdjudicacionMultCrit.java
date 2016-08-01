/**
 * CriterioAdjudicacionMultCrit.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class CriterioAdjudicacionMultCrit  implements java.io.Serializable {
    private java.lang.String cantMax;

    private java.lang.String cantMin;

    private es.dipucr.contratacion.client.beans.Campo codCritAdj;

    private java.lang.String[] descPonderacion;

    private java.lang.String[] descpCalculoExp;

    private java.lang.String[] descripcion;

    private java.lang.String[] descripcionPujaMinSubastaElect;

    private es.dipucr.contratacion.client.beans.Campo expresionCalc;

    private java.lang.String id;

    private java.lang.String impMax;

    private java.lang.String impMin;

    private java.lang.String ponderacion;

    public CriterioAdjudicacionMultCrit() {
    }

    public CriterioAdjudicacionMultCrit(
           java.lang.String cantMax,
           java.lang.String cantMin,
           es.dipucr.contratacion.client.beans.Campo codCritAdj,
           java.lang.String[] descPonderacion,
           java.lang.String[] descpCalculoExp,
           java.lang.String[] descripcion,
           java.lang.String[] descripcionPujaMinSubastaElect,
           es.dipucr.contratacion.client.beans.Campo expresionCalc,
           java.lang.String id,
           java.lang.String impMax,
           java.lang.String impMin,
           java.lang.String ponderacion) {
           this.cantMax = cantMax;
           this.cantMin = cantMin;
           this.codCritAdj = codCritAdj;
           this.descPonderacion = descPonderacion;
           this.descpCalculoExp = descpCalculoExp;
           this.descripcion = descripcion;
           this.descripcionPujaMinSubastaElect = descripcionPujaMinSubastaElect;
           this.expresionCalc = expresionCalc;
           this.id = id;
           this.impMax = impMax;
           this.impMin = impMin;
           this.ponderacion = ponderacion;
    }


    /**
     * Gets the cantMax value for this CriterioAdjudicacionMultCrit.
     * 
     * @return cantMax
     */
    public java.lang.String getCantMax() {
        return cantMax;
    }


    /**
     * Sets the cantMax value for this CriterioAdjudicacionMultCrit.
     * 
     * @param cantMax
     */
    public void setCantMax(java.lang.String cantMax) {
        this.cantMax = cantMax;
    }


    /**
     * Gets the cantMin value for this CriterioAdjudicacionMultCrit.
     * 
     * @return cantMin
     */
    public java.lang.String getCantMin() {
        return cantMin;
    }


    /**
     * Sets the cantMin value for this CriterioAdjudicacionMultCrit.
     * 
     * @param cantMin
     */
    public void setCantMin(java.lang.String cantMin) {
        this.cantMin = cantMin;
    }


    /**
     * Gets the codCritAdj value for this CriterioAdjudicacionMultCrit.
     * 
     * @return codCritAdj
     */
    public es.dipucr.contratacion.client.beans.Campo getCodCritAdj() {
        return codCritAdj;
    }


    /**
     * Sets the codCritAdj value for this CriterioAdjudicacionMultCrit.
     * 
     * @param codCritAdj
     */
    public void setCodCritAdj(es.dipucr.contratacion.client.beans.Campo codCritAdj) {
        this.codCritAdj = codCritAdj;
    }


    /**
     * Gets the descPonderacion value for this CriterioAdjudicacionMultCrit.
     * 
     * @return descPonderacion
     */
    public java.lang.String[] getDescPonderacion() {
        return descPonderacion;
    }


    /**
     * Sets the descPonderacion value for this CriterioAdjudicacionMultCrit.
     * 
     * @param descPonderacion
     */
    public void setDescPonderacion(java.lang.String[] descPonderacion) {
        this.descPonderacion = descPonderacion;
    }


    /**
     * Gets the descpCalculoExp value for this CriterioAdjudicacionMultCrit.
     * 
     * @return descpCalculoExp
     */
    public java.lang.String[] getDescpCalculoExp() {
        return descpCalculoExp;
    }


    /**
     * Sets the descpCalculoExp value for this CriterioAdjudicacionMultCrit.
     * 
     * @param descpCalculoExp
     */
    public void setDescpCalculoExp(java.lang.String[] descpCalculoExp) {
        this.descpCalculoExp = descpCalculoExp;
    }


    /**
     * Gets the descripcion value for this CriterioAdjudicacionMultCrit.
     * 
     * @return descripcion
     */
    public java.lang.String[] getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this CriterioAdjudicacionMultCrit.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String[] descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the descripcionPujaMinSubastaElect value for this CriterioAdjudicacionMultCrit.
     * 
     * @return descripcionPujaMinSubastaElect
     */
    public java.lang.String[] getDescripcionPujaMinSubastaElect() {
        return descripcionPujaMinSubastaElect;
    }


    /**
     * Sets the descripcionPujaMinSubastaElect value for this CriterioAdjudicacionMultCrit.
     * 
     * @param descripcionPujaMinSubastaElect
     */
    public void setDescripcionPujaMinSubastaElect(java.lang.String[] descripcionPujaMinSubastaElect) {
        this.descripcionPujaMinSubastaElect = descripcionPujaMinSubastaElect;
    }


    /**
     * Gets the expresionCalc value for this CriterioAdjudicacionMultCrit.
     * 
     * @return expresionCalc
     */
    public es.dipucr.contratacion.client.beans.Campo getExpresionCalc() {
        return expresionCalc;
    }


    /**
     * Sets the expresionCalc value for this CriterioAdjudicacionMultCrit.
     * 
     * @param expresionCalc
     */
    public void setExpresionCalc(es.dipucr.contratacion.client.beans.Campo expresionCalc) {
        this.expresionCalc = expresionCalc;
    }


    /**
     * Gets the id value for this CriterioAdjudicacionMultCrit.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this CriterioAdjudicacionMultCrit.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the impMax value for this CriterioAdjudicacionMultCrit.
     * 
     * @return impMax
     */
    public java.lang.String getImpMax() {
        return impMax;
    }


    /**
     * Sets the impMax value for this CriterioAdjudicacionMultCrit.
     * 
     * @param impMax
     */
    public void setImpMax(java.lang.String impMax) {
        this.impMax = impMax;
    }


    /**
     * Gets the impMin value for this CriterioAdjudicacionMultCrit.
     * 
     * @return impMin
     */
    public java.lang.String getImpMin() {
        return impMin;
    }


    /**
     * Sets the impMin value for this CriterioAdjudicacionMultCrit.
     * 
     * @param impMin
     */
    public void setImpMin(java.lang.String impMin) {
        this.impMin = impMin;
    }


    /**
     * Gets the ponderacion value for this CriterioAdjudicacionMultCrit.
     * 
     * @return ponderacion
     */
    public java.lang.String getPonderacion() {
        return ponderacion;
    }


    /**
     * Sets the ponderacion value for this CriterioAdjudicacionMultCrit.
     * 
     * @param ponderacion
     */
    public void setPonderacion(java.lang.String ponderacion) {
        this.ponderacion = ponderacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CriterioAdjudicacionMultCrit)) return false;
        CriterioAdjudicacionMultCrit other = (CriterioAdjudicacionMultCrit) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cantMax==null && other.getCantMax()==null) || 
             (this.cantMax!=null &&
              this.cantMax.equals(other.getCantMax()))) &&
            ((this.cantMin==null && other.getCantMin()==null) || 
             (this.cantMin!=null &&
              this.cantMin.equals(other.getCantMin()))) &&
            ((this.codCritAdj==null && other.getCodCritAdj()==null) || 
             (this.codCritAdj!=null &&
              this.codCritAdj.equals(other.getCodCritAdj()))) &&
            ((this.descPonderacion==null && other.getDescPonderacion()==null) || 
             (this.descPonderacion!=null &&
              java.util.Arrays.equals(this.descPonderacion, other.getDescPonderacion()))) &&
            ((this.descpCalculoExp==null && other.getDescpCalculoExp()==null) || 
             (this.descpCalculoExp!=null &&
              java.util.Arrays.equals(this.descpCalculoExp, other.getDescpCalculoExp()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              java.util.Arrays.equals(this.descripcion, other.getDescripcion()))) &&
            ((this.descripcionPujaMinSubastaElect==null && other.getDescripcionPujaMinSubastaElect()==null) || 
             (this.descripcionPujaMinSubastaElect!=null &&
              java.util.Arrays.equals(this.descripcionPujaMinSubastaElect, other.getDescripcionPujaMinSubastaElect()))) &&
            ((this.expresionCalc==null && other.getExpresionCalc()==null) || 
             (this.expresionCalc!=null &&
              this.expresionCalc.equals(other.getExpresionCalc()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.impMax==null && other.getImpMax()==null) || 
             (this.impMax!=null &&
              this.impMax.equals(other.getImpMax()))) &&
            ((this.impMin==null && other.getImpMin()==null) || 
             (this.impMin!=null &&
              this.impMin.equals(other.getImpMin()))) &&
            ((this.ponderacion==null && other.getPonderacion()==null) || 
             (this.ponderacion!=null &&
              this.ponderacion.equals(other.getPonderacion())));
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
        if (getCantMax() != null) {
            _hashCode += getCantMax().hashCode();
        }
        if (getCantMin() != null) {
            _hashCode += getCantMin().hashCode();
        }
        if (getCodCritAdj() != null) {
            _hashCode += getCodCritAdj().hashCode();
        }
        if (getDescPonderacion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescPonderacion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescPonderacion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDescpCalculoExp() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescpCalculoExp());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescpCalculoExp(), i);
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
        if (getDescripcionPujaMinSubastaElect() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescripcionPujaMinSubastaElect());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescripcionPujaMinSubastaElect(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getExpresionCalc() != null) {
            _hashCode += getExpresionCalc().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getImpMax() != null) {
            _hashCode += getImpMax().hashCode();
        }
        if (getImpMin() != null) {
            _hashCode += getImpMin().hashCode();
        }
        if (getPonderacion() != null) {
            _hashCode += getPonderacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CriterioAdjudicacionMultCrit.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "CriterioAdjudicacionMultCrit"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cantMax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "cantMax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cantMin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "cantMin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codCritAdj");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "codCritAdj"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descPonderacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descPonderacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descpCalculoExp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descpCalculoExp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("descripcionPujaMinSubastaElect");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "descripcionPujaMinSubastaElect"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expresionCalc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "expresionCalc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("impMax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "impMax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("impMin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "impMin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ponderacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "ponderacion"));
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
