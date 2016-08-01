/**
 * AnalisisPrevio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.objeto;

public class AnalisisPrevio  implements java.io.Serializable {
    private es.dipucr.contratacion.objeto.Campo[] cpv;

    private es.dipucr.contratacion.objeto.PublicacionesOficialesBean diarios;

    private java.util.Calendar f_term_pazo_presen_prop;

    private java.lang.String numexp;

    private java.lang.String objetoContrato;

    private java.lang.String presupuestoConIva;

    private java.lang.String presupuestoSinIva;

    private es.dipucr.contratacion.objeto.Campo procContratacion;

    private es.dipucr.contratacion.objeto.Campo subTipoContrato;

    private es.dipucr.contratacion.objeto.Campo tipoContrato;

    private es.dipucr.contratacion.objeto.Campo tipoPresentacionOferta;

    private es.dipucr.contratacion.objeto.Campo tipoTramitacion;

    private es.dipucr.contratacion.objeto.Campo tramitacionGasto;

    public AnalisisPrevio() {
    }

    public AnalisisPrevio(
           es.dipucr.contratacion.objeto.Campo[] cpv,
           es.dipucr.contratacion.objeto.PublicacionesOficialesBean diarios,
           java.util.Calendar f_term_pazo_presen_prop,
           java.lang.String numexp,
           java.lang.String objetoContrato,
           java.lang.String presupuestoConIva,
           java.lang.String presupuestoSinIva,
           es.dipucr.contratacion.objeto.Campo procContratacion,
           es.dipucr.contratacion.objeto.Campo subTipoContrato,
           es.dipucr.contratacion.objeto.Campo tipoContrato,
           es.dipucr.contratacion.objeto.Campo tipoPresentacionOferta,
           es.dipucr.contratacion.objeto.Campo tipoTramitacion,
           es.dipucr.contratacion.objeto.Campo tramitacionGasto) {
           this.cpv = cpv;
           this.diarios = diarios;
           this.f_term_pazo_presen_prop = f_term_pazo_presen_prop;
           this.numexp = numexp;
           this.objetoContrato = objetoContrato;
           this.presupuestoConIva = presupuestoConIva;
           this.presupuestoSinIva = presupuestoSinIva;
           this.procContratacion = procContratacion;
           this.subTipoContrato = subTipoContrato;
           this.tipoContrato = tipoContrato;
           this.tipoPresentacionOferta = tipoPresentacionOferta;
           this.tipoTramitacion = tipoTramitacion;
           this.tramitacionGasto = tramitacionGasto;
    }


    /**
     * Gets the cpv value for this AnalisisPrevio.
     * 
     * @return cpv
     */
    public es.dipucr.contratacion.objeto.Campo[] getCpv() {
        return cpv;
    }


    /**
     * Sets the cpv value for this AnalisisPrevio.
     * 
     * @param cpv
     */
    public void setCpv(es.dipucr.contratacion.objeto.Campo[] cpv) {
        this.cpv = cpv;
    }


    /**
     * Gets the diarios value for this AnalisisPrevio.
     * 
     * @return diarios
     */
    public es.dipucr.contratacion.objeto.PublicacionesOficialesBean getDiarios() {
        return diarios;
    }


    /**
     * Sets the diarios value for this AnalisisPrevio.
     * 
     * @param diarios
     */
    public void setDiarios(es.dipucr.contratacion.objeto.PublicacionesOficialesBean diarios) {
        this.diarios = diarios;
    }


    /**
     * Gets the f_term_pazo_presen_prop value for this AnalisisPrevio.
     * 
     * @return f_term_pazo_presen_prop
     */
    public java.util.Calendar getF_term_pazo_presen_prop() {
        return f_term_pazo_presen_prop;
    }


    /**
     * Sets the f_term_pazo_presen_prop value for this AnalisisPrevio.
     * 
     * @param f_term_pazo_presen_prop
     */
    public void setF_term_pazo_presen_prop(java.util.Calendar f_term_pazo_presen_prop) {
        this.f_term_pazo_presen_prop = f_term_pazo_presen_prop;
    }


    /**
     * Gets the numexp value for this AnalisisPrevio.
     * 
     * @return numexp
     */
    public java.lang.String getNumexp() {
        return numexp;
    }


    /**
     * Sets the numexp value for this AnalisisPrevio.
     * 
     * @param numexp
     */
    public void setNumexp(java.lang.String numexp) {
        this.numexp = numexp;
    }


    /**
     * Gets the objetoContrato value for this AnalisisPrevio.
     * 
     * @return objetoContrato
     */
    public java.lang.String getObjetoContrato() {
        return objetoContrato;
    }


    /**
     * Sets the objetoContrato value for this AnalisisPrevio.
     * 
     * @param objetoContrato
     */
    public void setObjetoContrato(java.lang.String objetoContrato) {
        this.objetoContrato = objetoContrato;
    }


    /**
     * Gets the presupuestoConIva value for this AnalisisPrevio.
     * 
     * @return presupuestoConIva
     */
    public java.lang.String getPresupuestoConIva() {
        return presupuestoConIva;
    }


    /**
     * Sets the presupuestoConIva value for this AnalisisPrevio.
     * 
     * @param presupuestoConIva
     */
    public void setPresupuestoConIva(java.lang.String presupuestoConIva) {
        this.presupuestoConIva = presupuestoConIva;
    }


    /**
     * Gets the presupuestoSinIva value for this AnalisisPrevio.
     * 
     * @return presupuestoSinIva
     */
    public java.lang.String getPresupuestoSinIva() {
        return presupuestoSinIva;
    }


    /**
     * Sets the presupuestoSinIva value for this AnalisisPrevio.
     * 
     * @param presupuestoSinIva
     */
    public void setPresupuestoSinIva(java.lang.String presupuestoSinIva) {
        this.presupuestoSinIva = presupuestoSinIva;
    }


    /**
     * Gets the procContratacion value for this AnalisisPrevio.
     * 
     * @return procContratacion
     */
    public es.dipucr.contratacion.objeto.Campo getProcContratacion() {
        return procContratacion;
    }


    /**
     * Sets the procContratacion value for this AnalisisPrevio.
     * 
     * @param procContratacion
     */
    public void setProcContratacion(es.dipucr.contratacion.objeto.Campo procContratacion) {
        this.procContratacion = procContratacion;
    }


    /**
     * Gets the subTipoContrato value for this AnalisisPrevio.
     * 
     * @return subTipoContrato
     */
    public es.dipucr.contratacion.objeto.Campo getSubTipoContrato() {
        return subTipoContrato;
    }


    /**
     * Sets the subTipoContrato value for this AnalisisPrevio.
     * 
     * @param subTipoContrato
     */
    public void setSubTipoContrato(es.dipucr.contratacion.objeto.Campo subTipoContrato) {
        this.subTipoContrato = subTipoContrato;
    }


    /**
     * Gets the tipoContrato value for this AnalisisPrevio.
     * 
     * @return tipoContrato
     */
    public es.dipucr.contratacion.objeto.Campo getTipoContrato() {
        return tipoContrato;
    }


    /**
     * Sets the tipoContrato value for this AnalisisPrevio.
     * 
     * @param tipoContrato
     */
    public void setTipoContrato(es.dipucr.contratacion.objeto.Campo tipoContrato) {
        this.tipoContrato = tipoContrato;
    }


    /**
     * Gets the tipoPresentacionOferta value for this AnalisisPrevio.
     * 
     * @return tipoPresentacionOferta
     */
    public es.dipucr.contratacion.objeto.Campo getTipoPresentacionOferta() {
        return tipoPresentacionOferta;
    }


    /**
     * Sets the tipoPresentacionOferta value for this AnalisisPrevio.
     * 
     * @param tipoPresentacionOferta
     */
    public void setTipoPresentacionOferta(es.dipucr.contratacion.objeto.Campo tipoPresentacionOferta) {
        this.tipoPresentacionOferta = tipoPresentacionOferta;
    }


    /**
     * Gets the tipoTramitacion value for this AnalisisPrevio.
     * 
     * @return tipoTramitacion
     */
    public es.dipucr.contratacion.objeto.Campo getTipoTramitacion() {
        return tipoTramitacion;
    }


    /**
     * Sets the tipoTramitacion value for this AnalisisPrevio.
     * 
     * @param tipoTramitacion
     */
    public void setTipoTramitacion(es.dipucr.contratacion.objeto.Campo tipoTramitacion) {
        this.tipoTramitacion = tipoTramitacion;
    }


    /**
     * Gets the tramitacionGasto value for this AnalisisPrevio.
     * 
     * @return tramitacionGasto
     */
    public es.dipucr.contratacion.objeto.Campo getTramitacionGasto() {
        return tramitacionGasto;
    }


    /**
     * Sets the tramitacionGasto value for this AnalisisPrevio.
     * 
     * @param tramitacionGasto
     */
    public void setTramitacionGasto(es.dipucr.contratacion.objeto.Campo tramitacionGasto) {
        this.tramitacionGasto = tramitacionGasto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AnalisisPrevio)) return false;
        AnalisisPrevio other = (AnalisisPrevio) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cpv==null && other.getCpv()==null) || 
             (this.cpv!=null &&
              java.util.Arrays.equals(this.cpv, other.getCpv()))) &&
            ((this.diarios==null && other.getDiarios()==null) || 
             (this.diarios!=null &&
              this.diarios.equals(other.getDiarios()))) &&
            ((this.f_term_pazo_presen_prop==null && other.getF_term_pazo_presen_prop()==null) || 
             (this.f_term_pazo_presen_prop!=null &&
              this.f_term_pazo_presen_prop.equals(other.getF_term_pazo_presen_prop()))) &&
            ((this.numexp==null && other.getNumexp()==null) || 
             (this.numexp!=null &&
              this.numexp.equals(other.getNumexp()))) &&
            ((this.objetoContrato==null && other.getObjetoContrato()==null) || 
             (this.objetoContrato!=null &&
              this.objetoContrato.equals(other.getObjetoContrato()))) &&
            ((this.presupuestoConIva==null && other.getPresupuestoConIva()==null) || 
             (this.presupuestoConIva!=null &&
              this.presupuestoConIva.equals(other.getPresupuestoConIva()))) &&
            ((this.presupuestoSinIva==null && other.getPresupuestoSinIva()==null) || 
             (this.presupuestoSinIva!=null &&
              this.presupuestoSinIva.equals(other.getPresupuestoSinIva()))) &&
            ((this.procContratacion==null && other.getProcContratacion()==null) || 
             (this.procContratacion!=null &&
              this.procContratacion.equals(other.getProcContratacion()))) &&
            ((this.subTipoContrato==null && other.getSubTipoContrato()==null) || 
             (this.subTipoContrato!=null &&
              this.subTipoContrato.equals(other.getSubTipoContrato()))) &&
            ((this.tipoContrato==null && other.getTipoContrato()==null) || 
             (this.tipoContrato!=null &&
              this.tipoContrato.equals(other.getTipoContrato()))) &&
            ((this.tipoPresentacionOferta==null && other.getTipoPresentacionOferta()==null) || 
             (this.tipoPresentacionOferta!=null &&
              this.tipoPresentacionOferta.equals(other.getTipoPresentacionOferta()))) &&
            ((this.tipoTramitacion==null && other.getTipoTramitacion()==null) || 
             (this.tipoTramitacion!=null &&
              this.tipoTramitacion.equals(other.getTipoTramitacion()))) &&
            ((this.tramitacionGasto==null && other.getTramitacionGasto()==null) || 
             (this.tramitacionGasto!=null &&
              this.tramitacionGasto.equals(other.getTramitacionGasto())));
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
        if (getCpv() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCpv());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCpv(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDiarios() != null) {
            _hashCode += getDiarios().hashCode();
        }
        if (getF_term_pazo_presen_prop() != null) {
            _hashCode += getF_term_pazo_presen_prop().hashCode();
        }
        if (getNumexp() != null) {
            _hashCode += getNumexp().hashCode();
        }
        if (getObjetoContrato() != null) {
            _hashCode += getObjetoContrato().hashCode();
        }
        if (getPresupuestoConIva() != null) {
            _hashCode += getPresupuestoConIva().hashCode();
        }
        if (getPresupuestoSinIva() != null) {
            _hashCode += getPresupuestoSinIva().hashCode();
        }
        if (getProcContratacion() != null) {
            _hashCode += getProcContratacion().hashCode();
        }
        if (getSubTipoContrato() != null) {
            _hashCode += getSubTipoContrato().hashCode();
        }
        if (getTipoContrato() != null) {
            _hashCode += getTipoContrato().hashCode();
        }
        if (getTipoPresentacionOferta() != null) {
            _hashCode += getTipoPresentacionOferta().hashCode();
        }
        if (getTipoTramitacion() != null) {
            _hashCode += getTipoTramitacion().hashCode();
        }
        if (getTramitacionGasto() != null) {
            _hashCode += getTramitacionGasto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AnalisisPrevio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "AnalisisPrevio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cpv");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "cpv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://services.contratacion.dipucr.es", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("diarios");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "diarios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.place.dgpe.es", "PublicacionesOficialesBean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("f_term_pazo_presen_prop");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "f_term_pazo_presen_prop"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numexp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "numexp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objetoContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "objetoContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presupuestoConIva");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "presupuestoConIva"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presupuestoSinIva");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "presupuestoSinIva"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procContratacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "procContratacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subTipoContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "subTipoContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoContrato");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "tipoContrato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoPresentacionOferta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "tipoPresentacionOferta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoTramitacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "tipoTramitacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tramitacionGasto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "tramitacionGasto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://objeto.contratacion.dipucr.es", "Campo"));
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
