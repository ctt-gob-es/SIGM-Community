/**
 * PersonalContacto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.dipucr.contratacion.client.beans;

public class PersonalContacto  implements java.io.Serializable {
    private java.lang.String calle;

    private java.lang.String ciudad;

    private es.dipucr.contratacion.client.beans.Campo codFormatoDirec;

    private java.lang.String cp;

    private java.lang.String email;

    private es.dipucr.contratacion.client.beans.Campo localizacionGeografica;

    private java.lang.String nombreContacto;

    private es.dipucr.contratacion.client.beans.Campo pais;

    private java.lang.String provincia;

    private java.lang.String telefono;

    public PersonalContacto() {
    }

    public PersonalContacto(
           java.lang.String calle,
           java.lang.String ciudad,
           es.dipucr.contratacion.client.beans.Campo codFormatoDirec,
           java.lang.String cp,
           java.lang.String email,
           es.dipucr.contratacion.client.beans.Campo localizacionGeografica,
           java.lang.String nombreContacto,
           es.dipucr.contratacion.client.beans.Campo pais,
           java.lang.String provincia,
           java.lang.String telefono) {
           this.calle = calle;
           this.ciudad = ciudad;
           this.codFormatoDirec = codFormatoDirec;
           this.cp = cp;
           this.email = email;
           this.localizacionGeografica = localizacionGeografica;
           this.nombreContacto = nombreContacto;
           this.pais = pais;
           this.provincia = provincia;
           this.telefono = telefono;
    }


    /**
     * Gets the calle value for this PersonalContacto.
     * 
     * @return calle
     */
    public java.lang.String getCalle() {
        return calle;
    }


    /**
     * Sets the calle value for this PersonalContacto.
     * 
     * @param calle
     */
    public void setCalle(java.lang.String calle) {
        this.calle = calle;
    }


    /**
     * Gets the ciudad value for this PersonalContacto.
     * 
     * @return ciudad
     */
    public java.lang.String getCiudad() {
        return ciudad;
    }


    /**
     * Sets the ciudad value for this PersonalContacto.
     * 
     * @param ciudad
     */
    public void setCiudad(java.lang.String ciudad) {
        this.ciudad = ciudad;
    }


    /**
     * Gets the codFormatoDirec value for this PersonalContacto.
     * 
     * @return codFormatoDirec
     */
    public es.dipucr.contratacion.client.beans.Campo getCodFormatoDirec() {
        return codFormatoDirec;
    }


    /**
     * Sets the codFormatoDirec value for this PersonalContacto.
     * 
     * @param codFormatoDirec
     */
    public void setCodFormatoDirec(es.dipucr.contratacion.client.beans.Campo codFormatoDirec) {
        this.codFormatoDirec = codFormatoDirec;
    }


    /**
     * Gets the cp value for this PersonalContacto.
     * 
     * @return cp
     */
    public java.lang.String getCp() {
        return cp;
    }


    /**
     * Sets the cp value for this PersonalContacto.
     * 
     * @param cp
     */
    public void setCp(java.lang.String cp) {
        this.cp = cp;
    }


    /**
     * Gets the email value for this PersonalContacto.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this PersonalContacto.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the localizacionGeografica value for this PersonalContacto.
     * 
     * @return localizacionGeografica
     */
    public es.dipucr.contratacion.client.beans.Campo getLocalizacionGeografica() {
        return localizacionGeografica;
    }


    /**
     * Sets the localizacionGeografica value for this PersonalContacto.
     * 
     * @param localizacionGeografica
     */
    public void setLocalizacionGeografica(es.dipucr.contratacion.client.beans.Campo localizacionGeografica) {
        this.localizacionGeografica = localizacionGeografica;
    }


    /**
     * Gets the nombreContacto value for this PersonalContacto.
     * 
     * @return nombreContacto
     */
    public java.lang.String getNombreContacto() {
        return nombreContacto;
    }


    /**
     * Sets the nombreContacto value for this PersonalContacto.
     * 
     * @param nombreContacto
     */
    public void setNombreContacto(java.lang.String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }


    /**
     * Gets the pais value for this PersonalContacto.
     * 
     * @return pais
     */
    public es.dipucr.contratacion.client.beans.Campo getPais() {
        return pais;
    }


    /**
     * Sets the pais value for this PersonalContacto.
     * 
     * @param pais
     */
    public void setPais(es.dipucr.contratacion.client.beans.Campo pais) {
        this.pais = pais;
    }


    /**
     * Gets the provincia value for this PersonalContacto.
     * 
     * @return provincia
     */
    public java.lang.String getProvincia() {
        return provincia;
    }


    /**
     * Sets the provincia value for this PersonalContacto.
     * 
     * @param provincia
     */
    public void setProvincia(java.lang.String provincia) {
        this.provincia = provincia;
    }


    /**
     * Gets the telefono value for this PersonalContacto.
     * 
     * @return telefono
     */
    public java.lang.String getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this PersonalContacto.
     * 
     * @param telefono
     */
    public void setTelefono(java.lang.String telefono) {
        this.telefono = telefono;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PersonalContacto)) return false;
        PersonalContacto other = (PersonalContacto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.calle==null && other.getCalle()==null) || 
             (this.calle!=null &&
              this.calle.equals(other.getCalle()))) &&
            ((this.ciudad==null && other.getCiudad()==null) || 
             (this.ciudad!=null &&
              this.ciudad.equals(other.getCiudad()))) &&
            ((this.codFormatoDirec==null && other.getCodFormatoDirec()==null) || 
             (this.codFormatoDirec!=null &&
              this.codFormatoDirec.equals(other.getCodFormatoDirec()))) &&
            ((this.cp==null && other.getCp()==null) || 
             (this.cp!=null &&
              this.cp.equals(other.getCp()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.localizacionGeografica==null && other.getLocalizacionGeografica()==null) || 
             (this.localizacionGeografica!=null &&
              this.localizacionGeografica.equals(other.getLocalizacionGeografica()))) &&
            ((this.nombreContacto==null && other.getNombreContacto()==null) || 
             (this.nombreContacto!=null &&
              this.nombreContacto.equals(other.getNombreContacto()))) &&
            ((this.pais==null && other.getPais()==null) || 
             (this.pais!=null &&
              this.pais.equals(other.getPais()))) &&
            ((this.provincia==null && other.getProvincia()==null) || 
             (this.provincia!=null &&
              this.provincia.equals(other.getProvincia()))) &&
            ((this.telefono==null && other.getTelefono()==null) || 
             (this.telefono!=null &&
              this.telefono.equals(other.getTelefono())));
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
        if (getCalle() != null) {
            _hashCode += getCalle().hashCode();
        }
        if (getCiudad() != null) {
            _hashCode += getCiudad().hashCode();
        }
        if (getCodFormatoDirec() != null) {
            _hashCode += getCodFormatoDirec().hashCode();
        }
        if (getCp() != null) {
            _hashCode += getCp().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getLocalizacionGeografica() != null) {
            _hashCode += getLocalizacionGeografica().hashCode();
        }
        if (getNombreContacto() != null) {
            _hashCode += getNombreContacto().hashCode();
        }
        if (getPais() != null) {
            _hashCode += getPais().hashCode();
        }
        if (getProvincia() != null) {
            _hashCode += getProvincia().hashCode();
        }
        if (getTelefono() != null) {
            _hashCode += getTelefono().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PersonalContacto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "PersonalContacto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("calle");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "calle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ciudad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "ciudad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codFormatoDirec");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "codFormatoDirec"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "cp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localizacionGeografica");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "localizacionGeografica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreContacto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "nombreContacto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pais");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "pais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "Campo"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("provincia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "provincia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefono");
        elemField.setXmlName(new javax.xml.namespace.QName("http://beans.client.contratacion.dipucr.es", "telefono"));
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
