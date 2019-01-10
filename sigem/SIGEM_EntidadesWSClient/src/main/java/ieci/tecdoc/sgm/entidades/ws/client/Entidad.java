/**
 * Entidad.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.entidades.ws.client;


public class Entidad  extends ieci.tecdoc.sgm.entidades.ws.client.RetornoServicio  implements java.io.Serializable {
	
    private java.lang.String codigo_ine;
    private java.lang.String identificador;
    private java.lang.String nombreCorto;
    private java.lang.String nombreLargo;
	private String cif;
	private String password_entidad;
	private String dir3;
	private String sia;
	private String deh;

    public Entidad() {
    }

    public Entidad(
           java.lang.String errorCode,
           java.lang.String returnCode,
           java.lang.String codigo_ine,
           java.lang.String identificador,
           java.lang.String nombreCorto,
           java.lang.String nombreLargo) {
        super(
            errorCode,
            returnCode);
        this.codigo_ine = codigo_ine;
        this.identificador = identificador;
        this.nombreCorto = nombreCorto;
        this.nombreLargo = nombreLargo;
    }
    
       
    


    public java.lang.String getCodigo_ine() {
		return codigo_ine;
	}

	public void setCodigo_ine(java.lang.String codigo_ine) {
		this.codigo_ine = codigo_ine;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getPassword_entidad() {
		return password_entidad;
	}

	public void setPassword_entidad(String password_entidad) {
		this.password_entidad = password_entidad;
	}

	public String getDir3() {
		return dir3;
	}

	public void setDir3(String dir3) {
		this.dir3 = dir3;
	}

	public String getSia() {
		return sia;
	}

	public void setSia(String sia) {
		this.sia = sia;
	}

	public String getDeh() {
		return deh;
	}

	public void setDeh(String deh) {
		this.deh = deh;
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
        _equals = super.equals(obj) && 
            ((this.codigo_ine==null && other.getCodigo_ine()==null) || 
             (this.codigo_ine!=null &&
              this.codigo_ine.equals(other.getCodigo_ine()))) &&
            ((this.identificador==null && other.getIdentificador()==null) || 
             (this.identificador!=null &&
              this.identificador.equals(other.getIdentificador()))) &&
            ((this.nombreCorto==null && other.getNombreCorto()==null) || 
             (this.nombreCorto!=null &&
              this.nombreCorto.equals(other.getNombreCorto()))) &&
            ((this.nombreLargo==null && other.getNombreLargo()==null) || 
             (this.nombreLargo!=null &&
              this.nombreLargo.equals(other.getNombreLargo())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getCodigo_ine() != null) {
            _hashCode += getCodigo_ine().hashCode();
        }
        if (getIdentificador() != null) {
            _hashCode += getIdentificador().hashCode();
        }
        if (getNombreCorto() != null) {
            _hashCode += getNombreCorto().hashCode();
        }
        if (getNombreLargo() != null) {
            _hashCode += getNombreLargo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Entidad.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.entidades.sgm.tecdoc.ieci", "Entidad"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoINE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.entidades.sgm.tecdoc.ieci", "codigoINE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.entidades.sgm.tecdoc.ieci", "identificador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreCorto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.entidades.sgm.tecdoc.ieci", "nombreCorto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreLargo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.entidades.sgm.tecdoc.ieci", "nombreLargo"));
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
