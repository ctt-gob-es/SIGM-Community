package com.ieci.tecdoc.common.invesdoc;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="IUSERDATA"
 *     
*/
public class Iuserdata implements Serializable {

    /** identifier field */
    private Integer id;

    /** persistent field */
    private String cargo;

    /** persistent field */
    private String tfno_movil;
    
    /** persistent field */
    private String id_certificado;
    
    /** persistent field */
    private String email;
    
    /** persistent field */
    private String nombre;
    
    /** persistent field */
    private String apellidos;
    
    /** persistent field */
    private String dni;
    
    private Iuseruserhdr iuseruserhdr;


    /** full constructor */
    public Iuserdata(Integer id, String cargo, String tfno_movil, String id_certificado, String email, String nombre, String apellidos, String dni) {
        this.id = id;
        this.cargo = cargo;
        this.tfno_movil = tfno_movil;
        this.id_certificado = id_certificado;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
    }

    /** default constructor */
    public Iuserdata() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.Integer"
     *             column="ID"
     *         
     */
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
	/** 
     *            @hibernate.property
     *             column="CARGO"
     *             length="256"
     *             not-null="true"
     *         
     */	
	public String getCargo() {
		return cargo;
	}
	
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	/** 
     *            @hibernate.property
     *             column="TFNO_MOVIL"
     *             length="16"
     *             not-null="true"
     *         
     */
	public String getTfno_movil() {
		return tfno_movil;
	}
	
	public void setTfno_movil(String tfno_movil) {
		this.tfno_movil = tfno_movil;
	}
	
	/** 
     *            @hibernate.property
     *             column="ID_CERTIFICADO"
     *             length="256"
     *             not-null="true"
     *         
     */
	public String getId_certificado() {
		return id_certificado;
	}
	
	public void setId_certificado(String id_certificado) {
		this.id_certificado = id_certificado;
	}
	
	/** 
     *            @hibernate.property
     *             column="EMAIL"
     *             length="256"
     *             not-null="true"
     *         
     */
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	/** 
     *            @hibernate.property
     *             column="NOMBRE"
     *             length="256"
     *             not-null="true"
     *         
     */
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/** 
     *            @hibernate.property
     *             column="APELLIDO"
     *             length="256"
     *             not-null="true"
     *         
     */
	public String getApellidos() {
		return apellidos;
	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	/** 
     *            @hibernate.property
     *             column="DNI"
     *             length="20"
     *             not-null="true"
     *         
     */
	public String getDni() {
		return dni;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}

	public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Iuseruserhdr) ) return false;
        
        Iuseruserhdr castOther = (Iuseruserhdr) other;
        
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    //************************************
    // Incluido pos ISicres-Common Oracle 9i
    public String toXML() {
       String className = getClass().getName();
       className = className.substring(className.lastIndexOf(".") + 1, className.length()).toUpperCase();
       StringBuffer buffer = new StringBuffer();
       buffer.append("<");
       buffer.append(className);
       buffer.append(">");
       try {
           java.lang.reflect.Field[] fields = getClass().getDeclaredFields();
           java.lang.reflect.Field field = null;
           String name = null;
           int size = fields.length;
           for (int i = 0; i < size; i++) {
               field = fields[i];
               name = field.getName();
               buffer.append("<");
               buffer.append(name.toUpperCase());
               buffer.append(">");
               if (field.get(this) != null) {
                   buffer.append(field.get(this));
               }
               buffer.append("</");
               buffer.append(name.toUpperCase());
               buffer.append(">");
           }
       } catch (Exception e) {
           e.printStackTrace(System.err);
       }
       buffer.append("</");
       buffer.append(className);
       buffer.append(">");
       return buffer.toString();
	}
                               
	//************************************  
	                                                                                                                                                                   
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	public Iuseruserhdr getIuseruserhdr() {
		return iuseruserhdr;
	}

	public void setIuseruserhdr(Iuseruserhdr iuseruserhdr) {
		this.iuseruserhdr = iuseruserhdr;
	}
}
