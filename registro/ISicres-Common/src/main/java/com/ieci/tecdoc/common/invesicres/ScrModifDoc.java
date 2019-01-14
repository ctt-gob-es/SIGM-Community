package com.ieci.tecdoc.common.invesicres;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="SCR_MODIFDOC"
 *     
*/
public class ScrModifDoc implements Serializable {

	/** identifier field */
    private Integer id;

    /** persistent field */
    private String usr;

    /** persistent field */
    private Date modifDate;    

    /** persistent field */
    private String numReg;

    /** persistent field */
    private int idArch;

    /** persistent field */
    private int tipoDoc;

    /** persistent field */
    private String nombreDoc;
    
    /** persistent field */
    private String nombreDocNuevo;
    
    /** persistent field */
    private int accion;

    /** full constructor */
    public ScrModifDoc(Integer id, String usr, Date modifDate, String numReg, int idArch, int tipoDoc, String nombreDoc, String nombreDocNuevo, int accion) {
        this.id = id;
        this.usr = usr;
        this.modifDate = modifDate;
        this.numReg = numReg;
        this.idArch = idArch;
        this.tipoDoc = tipoDoc;
        this.nombreDoc = nombreDoc;
        this.nombreDocNuevo = nombreDocNuevo;
        this.accion = accion;
    }

    /** default constructor */
    public ScrModifDoc() {
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
     *             column="USR"
     *             length="32"
     *             not-null="true"
     *         
     */
    public String getUsr() {
        return this.usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    /** 
     *            @hibernate.property
     *             column="MODIF_DATE"
     *             length="7"
     *             not-null="true"
     *         
     */
    public Date getModifDate() {
        return this.modifDate;
    }

    public void setModifDate(Date modifDate) {
        this.modifDate = modifDate;
    }

    /** 
     *            @hibernate.property
     *             column="NUM_REG"
     *             length="20"
     *             not-null="true"
     *         
     */
    public String getNumReg() {
        return this.numReg;
    }

    public void setNumReg(String numReg) {
        this.numReg = numReg;
    }

    /** 
     *            @hibernate.property
     *             column="ID_ARCH"
     *             length="10"
     *             not-null="true"
     *         
     */
    public int getIdArch() {
        return this.idArch;
    }

    public void setIdArch(int idArch) {
        this.idArch = idArch;
    }

    /** 
     *            @hibernate.property
     *             column="TIPODOC"
     *             length="10"
     *             not-null="true"
     *         
     */
    public int getTipoDoc() {
        return this.tipoDoc;
    }

    public void setTipoDoc(int tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

	/** 
     *            @hibernate.property
     *             column="NOMBREDOC"
     *             length="255"
     *             not-null="true"
     *         
     */
    public String getNombreDoc() {
        return this.nombreDoc;
    }

    public void setNombreDoc(String nombreDoc) {
        this.nombreDoc = nombreDoc;
    }
    
    /** 
     *            @hibernate.property
     *             column="NOMBREDOCNUEVO"
     *             length="255"
     *             not-null="false"
     *         
     */
    public String getNombreDocNuevo() {
        return this.nombreDocNuevo;
    }

    public void setNombreDocNuevo(String nombreDocNuevo) {
        this.nombreDocNuevo = nombreDocNuevo;
    }
    
    /** 
     *            @hibernate.property
     *             column="ACCION"
     *             length="10"
     *             not-null="true"
     *         
     */
    public int getAccion() {
        return this.accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ScrModifDoc) ) return false;
        ScrModifDoc castOther = (ScrModifDoc) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
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
      
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

}
