/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package com.ieci.tecdoc.common.invesicres;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="SCR_CCAA"
 *     
*/
public class ScrCCAA implements Serializable{
    // Id de la CCAA
    private Integer id;
    // Nombre de la CCAA
    private String name;
    /** persistent field */
    private Date tmstamp;
    
    /** full constructor */
    public ScrCCAA(Integer id, Date tmstamp, String name) {
        this.id = id;
        this.tmstamp = tmstamp;
        this.name = name;
    }

    /** default constructor */
    public ScrCCAA() {
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
     *             column="TMSTAMP"
     *             length="7"
     *             not-null="true"
     *         
     */
    public Date getTmstamp() {
        return this.tmstamp;
    }

    public void setTmstamp(Date tmstamp) {
        this.tmstamp = tmstamp;
    } 

    /** 
     *            @hibernate.property
     *             column="NAME"
     *             length="50"
     *             not-null="true"
     *         
     */
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
    	return new ToStringBuilder(this).append("id", getId()).toString();
    }
    
    public boolean equals(Object other) {
    	if ( !(other instanceof ScrCCAA) ) return false;
    	ScrCCAA castOther = (ScrCCAA) other;
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
    			if (null != field.get(this)) {
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
}