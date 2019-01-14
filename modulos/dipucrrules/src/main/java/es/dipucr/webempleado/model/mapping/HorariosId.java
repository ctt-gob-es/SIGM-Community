/**
 * HorariosId.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.webempleado.model.mapping;

public class HorariosId  implements java.io.Serializable {
    private int ano;

    private double corta;

    private java.lang.String descripcion;

    private double jornada;

    private double manana;

    private double noche;

    private short num;

    private java.lang.String recuperable;

    private java.lang.String reduccion;

    private double tarde;

    private java.lang.String turnos;

    private java.lang.String zona;

    public HorariosId() {
    }

    public HorariosId(
           int ano,
           double corta,
           java.lang.String descripcion,
           double jornada,
           double manana,
           double noche,
           short num,
           java.lang.String recuperable,
           java.lang.String reduccion,
           double tarde,
           java.lang.String turnos,
           java.lang.String zona) {
           this.ano = ano;
           this.corta = corta;
           this.descripcion = descripcion;
           this.jornada = jornada;
           this.manana = manana;
           this.noche = noche;
           this.num = num;
           this.recuperable = recuperable;
           this.reduccion = reduccion;
           this.tarde = tarde;
           this.turnos = turnos;
           this.zona = zona;
    }


    /**
     * Gets the ano value for this HorariosId.
     * 
     * @return ano
     */
    public int getAno() {
        return ano;
    }


    /**
     * Sets the ano value for this HorariosId.
     * 
     * @param ano
     */
    public void setAno(int ano) {
        this.ano = ano;
    }


    /**
     * Gets the corta value for this HorariosId.
     * 
     * @return corta
     */
    public double getCorta() {
        return corta;
    }


    /**
     * Sets the corta value for this HorariosId.
     * 
     * @param corta
     */
    public void setCorta(double corta) {
        this.corta = corta;
    }


    /**
     * Gets the descripcion value for this HorariosId.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this HorariosId.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the jornada value for this HorariosId.
     * 
     * @return jornada
     */
    public double getJornada() {
        return jornada;
    }


    /**
     * Sets the jornada value for this HorariosId.
     * 
     * @param jornada
     */
    public void setJornada(double jornada) {
        this.jornada = jornada;
    }


    /**
     * Gets the manana value for this HorariosId.
     * 
     * @return manana
     */
    public double getManana() {
        return manana;
    }


    /**
     * Sets the manana value for this HorariosId.
     * 
     * @param manana
     */
    public void setManana(double manana) {
        this.manana = manana;
    }


    /**
     * Gets the noche value for this HorariosId.
     * 
     * @return noche
     */
    public double getNoche() {
        return noche;
    }


    /**
     * Sets the noche value for this HorariosId.
     * 
     * @param noche
     */
    public void setNoche(double noche) {
        this.noche = noche;
    }


    /**
     * Gets the num value for this HorariosId.
     * 
     * @return num
     */
    public short getNum() {
        return num;
    }


    /**
     * Sets the num value for this HorariosId.
     * 
     * @param num
     */
    public void setNum(short num) {
        this.num = num;
    }


    /**
     * Gets the recuperable value for this HorariosId.
     * 
     * @return recuperable
     */
    public java.lang.String getRecuperable() {
        return recuperable;
    }


    /**
     * Sets the recuperable value for this HorariosId.
     * 
     * @param recuperable
     */
    public void setRecuperable(java.lang.String recuperable) {
        this.recuperable = recuperable;
    }


    /**
     * Gets the reduccion value for this HorariosId.
     * 
     * @return reduccion
     */
    public java.lang.String getReduccion() {
        return reduccion;
    }


    /**
     * Sets the reduccion value for this HorariosId.
     * 
     * @param reduccion
     */
    public void setReduccion(java.lang.String reduccion) {
        this.reduccion = reduccion;
    }


    /**
     * Gets the tarde value for this HorariosId.
     * 
     * @return tarde
     */
    public double getTarde() {
        return tarde;
    }


    /**
     * Sets the tarde value for this HorariosId.
     * 
     * @param tarde
     */
    public void setTarde(double tarde) {
        this.tarde = tarde;
    }


    /**
     * Gets the turnos value for this HorariosId.
     * 
     * @return turnos
     */
    public java.lang.String getTurnos() {
        return turnos;
    }


    /**
     * Sets the turnos value for this HorariosId.
     * 
     * @param turnos
     */
    public void setTurnos(java.lang.String turnos) {
        this.turnos = turnos;
    }


    /**
     * Gets the zona value for this HorariosId.
     * 
     * @return zona
     */
    public java.lang.String getZona() {
        return zona;
    }


    /**
     * Sets the zona value for this HorariosId.
     * 
     * @param zona
     */
    public void setZona(java.lang.String zona) {
        this.zona = zona;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HorariosId)) return false;
        HorariosId other = (HorariosId) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.ano == other.getAno() &&
            this.corta == other.getCorta() &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              this.descripcion.equals(other.getDescripcion()))) &&
            this.jornada == other.getJornada() &&
            this.manana == other.getManana() &&
            this.noche == other.getNoche() &&
            this.num == other.getNum() &&
            ((this.recuperable==null && other.getRecuperable()==null) || 
             (this.recuperable!=null &&
              this.recuperable.equals(other.getRecuperable()))) &&
            ((this.reduccion==null && other.getReduccion()==null) || 
             (this.reduccion!=null &&
              this.reduccion.equals(other.getReduccion()))) &&
            this.tarde == other.getTarde() &&
            ((this.turnos==null && other.getTurnos()==null) || 
             (this.turnos!=null &&
              this.turnos.equals(other.getTurnos()))) &&
            ((this.zona==null && other.getZona()==null) || 
             (this.zona!=null &&
              this.zona.equals(other.getZona())));
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
        _hashCode += getAno();
        _hashCode += new Double(getCorta()).hashCode();
        if (getDescripcion() != null) {
            _hashCode += getDescripcion().hashCode();
        }
        _hashCode += new Double(getJornada()).hashCode();
        _hashCode += new Double(getManana()).hashCode();
        _hashCode += new Double(getNoche()).hashCode();
        _hashCode += getNum();
        if (getRecuperable() != null) {
            _hashCode += getRecuperable().hashCode();
        }
        if (getReduccion() != null) {
            _hashCode += getReduccion().hashCode();
        }
        _hashCode += new Double(getTarde()).hashCode();
        if (getTurnos() != null) {
            _hashCode += getTurnos().hashCode();
        }
        if (getZona() != null) {
            _hashCode += getZona().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HorariosId.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "HorariosId"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ano");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "ano"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("corta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "corta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jornada");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "jornada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("manana");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "manana"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noche");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "noche"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "num"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recuperable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "recuperable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reduccion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "reduccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tarde");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "tarde"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("turnos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "turnos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("zona");
        elemField.setXmlName(new javax.xml.namespace.QName("http://mapping.model.webempleado.dipucr.es", "zona"));
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
