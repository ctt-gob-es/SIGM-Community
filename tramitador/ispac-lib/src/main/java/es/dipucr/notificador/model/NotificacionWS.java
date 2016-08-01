/**
 * NotificacionWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.notificador.model;

public class NotificacionWS  implements java.io.Serializable {
    private java.lang.Byte acceso;

    private es.dipucr.notificador.model.AplicacionWS aplicacion;

    private java.lang.String cve;

    private java.lang.String descripcion;

    private java.lang.String dni;

    private java.lang.String documento;

    private int estado;

    private java.lang.String expediente;

    private java.util.Calendar fecAlta;

    private java.util.Calendar fecCaducidad;

    private java.util.Calendar fecLectura;

    private java.lang.String idHashAcuse;

    private java.lang.String idHashNotificacion;

    private int idNotificacion;

    private int idNotificacionRepresentante;

    private java.lang.String idTransaccion;

    private java.lang.String procedimiento;

    private es.dipucr.notificador.model.TerceroWS tercero;

    public NotificacionWS() {
    }

    public NotificacionWS(
           java.lang.Byte acceso,
           es.dipucr.notificador.model.AplicacionWS aplicacion,
           java.lang.String cve,
           java.lang.String descripcion,
           java.lang.String dni,
           java.lang.String documento,
           int estado,
           java.lang.String expediente,
           java.util.Calendar fecAlta,
           java.util.Calendar fecCaducidad,
           java.util.Calendar fecLectura,
           java.lang.String idHashAcuse,
           java.lang.String idHashNotificacion,
           int idNotificacion,
           int idNotificacionRepresentante,
           java.lang.String idTransaccion,
           java.lang.String procedimiento,
           es.dipucr.notificador.model.TerceroWS tercero) {
           this.acceso = acceso;
           this.aplicacion = aplicacion;
           this.cve = cve;
           this.descripcion = descripcion;
           this.dni = dni;
           this.documento = documento;
           this.estado = estado;
           this.expediente = expediente;
           this.fecAlta = fecAlta;
           this.fecCaducidad = fecCaducidad;
           this.fecLectura = fecLectura;
           this.idHashAcuse = idHashAcuse;
           this.idHashNotificacion = idHashNotificacion;
           this.idNotificacion = idNotificacion;
           this.idNotificacionRepresentante = idNotificacionRepresentante;
           this.idTransaccion = idTransaccion;
           this.procedimiento = procedimiento;
           this.tercero = tercero;
    }


    /**
     * Gets the acceso value for this NotificacionWS.
     * 
     * @return acceso
     */
    public java.lang.Byte getAcceso() {
        return acceso;
    }


    /**
     * Sets the acceso value for this NotificacionWS.
     * 
     * @param acceso
     */
    public void setAcceso(java.lang.Byte acceso) {
        this.acceso = acceso;
    }


    /**
     * Gets the aplicacion value for this NotificacionWS.
     * 
     * @return aplicacion
     */
    public es.dipucr.notificador.model.AplicacionWS getAplicacion() {
        return aplicacion;
    }


    /**
     * Sets the aplicacion value for this NotificacionWS.
     * 
     * @param aplicacion
     */
    public void setAplicacion(es.dipucr.notificador.model.AplicacionWS aplicacion) {
        this.aplicacion = aplicacion;
    }


    /**
     * Gets the cve value for this NotificacionWS.
     * 
     * @return cve
     */
    public java.lang.String getCve() {
        return cve;
    }


    /**
     * Sets the cve value for this NotificacionWS.
     * 
     * @param cve
     */
    public void setCve(java.lang.String cve) {
        this.cve = cve;
    }


    /**
     * Gets the descripcion value for this NotificacionWS.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this NotificacionWS.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the dni value for this NotificacionWS.
     * 
     * @return dni
     */
    public java.lang.String getDni() {
        return dni;
    }


    /**
     * Sets the dni value for this NotificacionWS.
     * 
     * @param dni
     */
    public void setDni(java.lang.String dni) {
        this.dni = dni;
    }


    /**
     * Gets the documento value for this NotificacionWS.
     * 
     * @return documento
     */
    public java.lang.String getDocumento() {
        return documento;
    }


    /**
     * Sets the documento value for this NotificacionWS.
     * 
     * @param documento
     */
    public void setDocumento(java.lang.String documento) {
        this.documento = documento;
    }


    /**
     * Gets the estado value for this NotificacionWS.
     * 
     * @return estado
     */
    public int getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this NotificacionWS.
     * 
     * @param estado
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }


    /**
     * Gets the expediente value for this NotificacionWS.
     * 
     * @return expediente
     */
    public java.lang.String getExpediente() {
        return expediente;
    }


    /**
     * Sets the expediente value for this NotificacionWS.
     * 
     * @param expediente
     */
    public void setExpediente(java.lang.String expediente) {
        this.expediente = expediente;
    }


    /**
     * Gets the fecAlta value for this NotificacionWS.
     * 
     * @return fecAlta
     */
    public java.util.Calendar getFecAlta() {
        return fecAlta;
    }


    /**
     * Sets the fecAlta value for this NotificacionWS.
     * 
     * @param fecAlta
     */
    public void setFecAlta(java.util.Calendar fecAlta) {
        this.fecAlta = fecAlta;
    }


    /**
     * Gets the fecCaducidad value for this NotificacionWS.
     * 
     * @return fecCaducidad
     */
    public java.util.Calendar getFecCaducidad() {
        return fecCaducidad;
    }


    /**
     * Sets the fecCaducidad value for this NotificacionWS.
     * 
     * @param fecCaducidad
     */
    public void setFecCaducidad(java.util.Calendar fecCaducidad) {
        this.fecCaducidad = fecCaducidad;
    }


    /**
     * Gets the fecLectura value for this NotificacionWS.
     * 
     * @return fecLectura
     */
    public java.util.Calendar getFecLectura() {
        return fecLectura;
    }


    /**
     * Sets the fecLectura value for this NotificacionWS.
     * 
     * @param fecLectura
     */
    public void setFecLectura(java.util.Calendar fecLectura) {
        this.fecLectura = fecLectura;
    }


    /**
     * Gets the idHashAcuse value for this NotificacionWS.
     * 
     * @return idHashAcuse
     */
    public java.lang.String getIdHashAcuse() {
        return idHashAcuse;
    }


    /**
     * Sets the idHashAcuse value for this NotificacionWS.
     * 
     * @param idHashAcuse
     */
    public void setIdHashAcuse(java.lang.String idHashAcuse) {
        this.idHashAcuse = idHashAcuse;
    }


    /**
     * Gets the idHashNotificacion value for this NotificacionWS.
     * 
     * @return idHashNotificacion
     */
    public java.lang.String getIdHashNotificacion() {
        return idHashNotificacion;
    }


    /**
     * Sets the idHashNotificacion value for this NotificacionWS.
     * 
     * @param idHashNotificacion
     */
    public void setIdHashNotificacion(java.lang.String idHashNotificacion) {
        this.idHashNotificacion = idHashNotificacion;
    }


    /**
     * Gets the idNotificacion value for this NotificacionWS.
     * 
     * @return idNotificacion
     */
    public int getIdNotificacion() {
        return idNotificacion;
    }


    /**
     * Sets the idNotificacion value for this NotificacionWS.
     * 
     * @param idNotificacion
     */
    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }


    /**
     * Gets the idNotificacionRepresentante value for this NotificacionWS.
     * 
     * @return idNotificacionRepresentante
     */
    public int getIdNotificacionRepresentante() {
        return idNotificacionRepresentante;
    }


    /**
     * Sets the idNotificacionRepresentante value for this NotificacionWS.
     * 
     * @param idNotificacionRepresentante
     */
    public void setIdNotificacionRepresentante(int idNotificacionRepresentante) {
        this.idNotificacionRepresentante = idNotificacionRepresentante;
    }


    /**
     * Gets the idTransaccion value for this NotificacionWS.
     * 
     * @return idTransaccion
     */
    public java.lang.String getIdTransaccion() {
        return idTransaccion;
    }


    /**
     * Sets the idTransaccion value for this NotificacionWS.
     * 
     * @param idTransaccion
     */
    public void setIdTransaccion(java.lang.String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }


    /**
     * Gets the procedimiento value for this NotificacionWS.
     * 
     * @return procedimiento
     */
    public java.lang.String getProcedimiento() {
        return procedimiento;
    }


    /**
     * Sets the procedimiento value for this NotificacionWS.
     * 
     * @param procedimiento
     */
    public void setProcedimiento(java.lang.String procedimiento) {
        this.procedimiento = procedimiento;
    }


    /**
     * Gets the tercero value for this NotificacionWS.
     * 
     * @return tercero
     */
    public es.dipucr.notificador.model.TerceroWS getTercero() {
        return tercero;
    }


    /**
     * Sets the tercero value for this NotificacionWS.
     * 
     * @param tercero
     */
    public void setTercero(es.dipucr.notificador.model.TerceroWS tercero) {
        this.tercero = tercero;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NotificacionWS)) return false;
        NotificacionWS other = (NotificacionWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.acceso==null && other.getAcceso()==null) || 
             (this.acceso!=null &&
              this.acceso.equals(other.getAcceso()))) &&
            ((this.aplicacion==null && other.getAplicacion()==null) || 
             (this.aplicacion!=null &&
              this.aplicacion.equals(other.getAplicacion()))) &&
            ((this.cve==null && other.getCve()==null) || 
             (this.cve!=null &&
              this.cve.equals(other.getCve()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              this.descripcion.equals(other.getDescripcion()))) &&
            ((this.dni==null && other.getDni()==null) || 
             (this.dni!=null &&
              this.dni.equals(other.getDni()))) &&
            ((this.documento==null && other.getDocumento()==null) || 
             (this.documento!=null &&
              this.documento.equals(other.getDocumento()))) &&
            this.estado == other.getEstado() &&
            ((this.expediente==null && other.getExpediente()==null) || 
             (this.expediente!=null &&
              this.expediente.equals(other.getExpediente()))) &&
            ((this.fecAlta==null && other.getFecAlta()==null) || 
             (this.fecAlta!=null &&
              this.fecAlta.equals(other.getFecAlta()))) &&
            ((this.fecCaducidad==null && other.getFecCaducidad()==null) || 
             (this.fecCaducidad!=null &&
              this.fecCaducidad.equals(other.getFecCaducidad()))) &&
            ((this.fecLectura==null && other.getFecLectura()==null) || 
             (this.fecLectura!=null &&
              this.fecLectura.equals(other.getFecLectura()))) &&
            ((this.idHashAcuse==null && other.getIdHashAcuse()==null) || 
             (this.idHashAcuse!=null &&
              this.idHashAcuse.equals(other.getIdHashAcuse()))) &&
            ((this.idHashNotificacion==null && other.getIdHashNotificacion()==null) || 
             (this.idHashNotificacion!=null &&
              this.idHashNotificacion.equals(other.getIdHashNotificacion()))) &&
            this.idNotificacion == other.getIdNotificacion() &&
            this.idNotificacionRepresentante == other.getIdNotificacionRepresentante() &&
            ((this.idTransaccion==null && other.getIdTransaccion()==null) || 
             (this.idTransaccion!=null &&
              this.idTransaccion.equals(other.getIdTransaccion()))) &&
            ((this.procedimiento==null && other.getProcedimiento()==null) || 
             (this.procedimiento!=null &&
              this.procedimiento.equals(other.getProcedimiento()))) &&
            ((this.tercero==null && other.getTercero()==null) || 
             (this.tercero!=null &&
              this.tercero.equals(other.getTercero())));
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
        if (getAcceso() != null) {
            _hashCode += getAcceso().hashCode();
        }
        if (getAplicacion() != null) {
            _hashCode += getAplicacion().hashCode();
        }
        if (getCve() != null) {
            _hashCode += getCve().hashCode();
        }
        if (getDescripcion() != null) {
            _hashCode += getDescripcion().hashCode();
        }
        if (getDni() != null) {
            _hashCode += getDni().hashCode();
        }
        if (getDocumento() != null) {
            _hashCode += getDocumento().hashCode();
        }
        _hashCode += getEstado();
        if (getExpediente() != null) {
            _hashCode += getExpediente().hashCode();
        }
        if (getFecAlta() != null) {
            _hashCode += getFecAlta().hashCode();
        }
        if (getFecCaducidad() != null) {
            _hashCode += getFecCaducidad().hashCode();
        }
        if (getFecLectura() != null) {
            _hashCode += getFecLectura().hashCode();
        }
        if (getIdHashAcuse() != null) {
            _hashCode += getIdHashAcuse().hashCode();
        }
        if (getIdHashNotificacion() != null) {
            _hashCode += getIdHashNotificacion().hashCode();
        }
        _hashCode += getIdNotificacion();
        _hashCode += getIdNotificacionRepresentante();
        if (getIdTransaccion() != null) {
            _hashCode += getIdTransaccion().hashCode();
        }
        if (getProcedimiento() != null) {
            _hashCode += getProcedimiento().hashCode();
        }
        if (getTercero() != null) {
            _hashCode += getTercero().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NotificacionWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "NotificacionWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acceso");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "acceso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "byte"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "aplicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "AplicacionWS"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cve");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "cve"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dni");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "dni"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "documento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expediente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "expediente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecAlta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "fecAlta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecCaducidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "fecCaducidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecLectura");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "fecLectura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idHashAcuse");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "idHashAcuse"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idHashNotificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "idHashNotificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idNotificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "idNotificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idNotificacionRepresentante");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "idNotificacionRepresentante"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTransaccion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "idTransaccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procedimiento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "procedimiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tercero");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "tercero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://model.notificador.dipucr.es", "TerceroWS"));
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
