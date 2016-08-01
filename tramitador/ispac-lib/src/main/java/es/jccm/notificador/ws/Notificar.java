/**
 * Notificar.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.jccm.notificador.ws;

public class Notificar  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String[] dnis;

    private java.lang.String procedimiento;

    private java.lang.String expediente;

    private java.lang.String descripcion;

    private int diaCaducidad;

    private int mesCaducidad;

    private int anyoCaducidad;

    private javax.activation.DataHandler notificacion;

    private java.lang.String nombre;

    private java.lang.String extension;

    private java.lang.String idHashNotificacion;

    private java.lang.String idCve;

    public Notificar() {
    }

    public Notificar(
           java.lang.String id,
           java.lang.String[] dnis,
           java.lang.String procedimiento,
           java.lang.String expediente,
           java.lang.String descripcion,
           int diaCaducidad,
           int mesCaducidad,
           int anyoCaducidad,
           javax.activation.DataHandler notificacion,
           java.lang.String nombre,
           java.lang.String extension,
           java.lang.String idHashNotificacion,
           java.lang.String idCve) {
           this.id = id;
           this.dnis = dnis;
           this.procedimiento = procedimiento;
           this.expediente = expediente;
           this.descripcion = descripcion;
           this.diaCaducidad = diaCaducidad;
           this.mesCaducidad = mesCaducidad;
           this.anyoCaducidad = anyoCaducidad;
           this.notificacion = notificacion;
           this.nombre = nombre;
           this.extension = extension;
           this.idHashNotificacion = idHashNotificacion;
           this.idCve = idCve;
    }


    /**
     * Gets the id value for this Notificar.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Notificar.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the dnis value for this Notificar.
     * 
     * @return dnis
     */
    public java.lang.String[] getDnis() {
        return dnis;
    }


    /**
     * Sets the dnis value for this Notificar.
     * 
     * @param dnis
     */
    public void setDnis(java.lang.String[] dnis) {
        this.dnis = dnis;
    }

    public java.lang.String getDnis(int i) {
        return this.dnis[i];
    }

    public void setDnis(int i, java.lang.String _value) {
        this.dnis[i] = _value;
    }


    /**
     * Gets the procedimiento value for this Notificar.
     * 
     * @return procedimiento
     */
    public java.lang.String getProcedimiento() {
        return procedimiento;
    }


    /**
     * Sets the procedimiento value for this Notificar.
     * 
     * @param procedimiento
     */
    public void setProcedimiento(java.lang.String procedimiento) {
        this.procedimiento = procedimiento;
    }


    /**
     * Gets the expediente value for this Notificar.
     * 
     * @return expediente
     */
    public java.lang.String getExpediente() {
        return expediente;
    }


    /**
     * Sets the expediente value for this Notificar.
     * 
     * @param expediente
     */
    public void setExpediente(java.lang.String expediente) {
        this.expediente = expediente;
    }


    /**
     * Gets the descripcion value for this Notificar.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this Notificar.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the diaCaducidad value for this Notificar.
     * 
     * @return diaCaducidad
     */
    public int getDiaCaducidad() {
        return diaCaducidad;
    }


    /**
     * Sets the diaCaducidad value for this Notificar.
     * 
     * @param diaCaducidad
     */
    public void setDiaCaducidad(int diaCaducidad) {
        this.diaCaducidad = diaCaducidad;
    }


    /**
     * Gets the mesCaducidad value for this Notificar.
     * 
     * @return mesCaducidad
     */
    public int getMesCaducidad() {
        return mesCaducidad;
    }


    /**
     * Sets the mesCaducidad value for this Notificar.
     * 
     * @param mesCaducidad
     */
    public void setMesCaducidad(int mesCaducidad) {
        this.mesCaducidad = mesCaducidad;
    }


    /**
     * Gets the anyoCaducidad value for this Notificar.
     * 
     * @return anyoCaducidad
     */
    public int getAnyoCaducidad() {
        return anyoCaducidad;
    }


    /**
     * Sets the anyoCaducidad value for this Notificar.
     * 
     * @param anyoCaducidad
     */
    public void setAnyoCaducidad(int anyoCaducidad) {
        this.anyoCaducidad = anyoCaducidad;
    }


    /**
     * Gets the notificacion value for this Notificar.
     * 
     * @return notificacion
     */
    public javax.activation.DataHandler getNotificacion() {
        return notificacion;
    }


    /**
     * Sets the notificacion value for this Notificar.
     * 
     * @param notificacion
     */
    public void setNotificacion(javax.activation.DataHandler notificacion) {
        this.notificacion = notificacion;
    }


    /**
     * Gets the nombre value for this Notificar.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this Notificar.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the extension value for this Notificar.
     * 
     * @return extension
     */
    public java.lang.String getExtension() {
        return extension;
    }


    /**
     * Sets the extension value for this Notificar.
     * 
     * @param extension
     */
    public void setExtension(java.lang.String extension) {
        this.extension = extension;
    }


    /**
     * Gets the idHashNotificacion value for this Notificar.
     * 
     * @return idHashNotificacion
     */
    public java.lang.String getIdHashNotificacion() {
        return idHashNotificacion;
    }


    /**
     * Sets the idHashNotificacion value for this Notificar.
     * 
     * @param idHashNotificacion
     */
    public void setIdHashNotificacion(java.lang.String idHashNotificacion) {
        this.idHashNotificacion = idHashNotificacion;
    }


    /**
     * Gets the idCve value for this Notificar.
     * 
     * @return idCve
     */
    public java.lang.String getIdCve() {
        return idCve;
    }


    /**
     * Sets the idCve value for this Notificar.
     * 
     * @param idCve
     */
    public void setIdCve(java.lang.String idCve) {
        this.idCve = idCve;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Notificar)) return false;
        Notificar other = (Notificar) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.dnis==null && other.getDnis()==null) || 
             (this.dnis!=null &&
              java.util.Arrays.equals(this.dnis, other.getDnis()))) &&
            ((this.procedimiento==null && other.getProcedimiento()==null) || 
             (this.procedimiento!=null &&
              this.procedimiento.equals(other.getProcedimiento()))) &&
            ((this.expediente==null && other.getExpediente()==null) || 
             (this.expediente!=null &&
              this.expediente.equals(other.getExpediente()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              this.descripcion.equals(other.getDescripcion()))) &&
            this.diaCaducidad == other.getDiaCaducidad() &&
            this.mesCaducidad == other.getMesCaducidad() &&
            this.anyoCaducidad == other.getAnyoCaducidad() &&
            ((this.notificacion==null && other.getNotificacion()==null) || 
             (this.notificacion!=null &&
              this.notificacion.equals(other.getNotificacion()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.extension==null && other.getExtension()==null) || 
             (this.extension!=null &&
              this.extension.equals(other.getExtension()))) &&
            ((this.idHashNotificacion==null && other.getIdHashNotificacion()==null) || 
             (this.idHashNotificacion!=null &&
              this.idHashNotificacion.equals(other.getIdHashNotificacion()))) &&
            ((this.idCve==null && other.getIdCve()==null) || 
             (this.idCve!=null &&
              this.idCve.equals(other.getIdCve())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getDnis() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDnis());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDnis(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getProcedimiento() != null) {
            _hashCode += getProcedimiento().hashCode();
        }
        if (getExpediente() != null) {
            _hashCode += getExpediente().hashCode();
        }
        if (getDescripcion() != null) {
            _hashCode += getDescripcion().hashCode();
        }
        _hashCode += getDiaCaducidad();
        _hashCode += getMesCaducidad();
        _hashCode += getAnyoCaducidad();
        if (getNotificacion() != null) {
            _hashCode += getNotificacion().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getExtension() != null) {
            _hashCode += getExtension().hashCode();
        }
        if (getIdHashNotificacion() != null) {
            _hashCode += getIdHashNotificacion().hashCode();
        }
        if (getIdCve() != null) {
            _hashCode += getIdCve().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Notificar.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", ">notificar"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dnis");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "dnis"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procedimiento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "procedimiento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expediente");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "expediente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("diaCaducidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "diaCaducidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mesCaducidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "mesCaducidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anyoCaducidad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "anyoCaducidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "notificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "DataHandler"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extension");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "extension"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idHashNotificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "idHashNotificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCve");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ws.notificador.jccm.es", "idCve"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
