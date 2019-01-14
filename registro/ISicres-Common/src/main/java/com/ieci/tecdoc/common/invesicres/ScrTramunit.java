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
 *         table="SCR_TRAMUNIT"
 *     
*/
public class ScrTramunit implements Serializable{
    // Id de la Unidad Tramitadora
    private Integer id;
    private String code_tramunit;
    private String name_tramunit;
    private String code_entity;
    private String name_entity;
    private int id_orgs;
    
    /** full constructor */
    public ScrTramunit(Integer id, String code_tramunit, String name_tramunit, String code_entity, String name_entity, int id_orgs) {
        this.id = id;
        this.code_tramunit = code_tramunit;
        this.name_tramunit = name_tramunit;
        this.code_entity = code_entity;
        this.name_entity = name_entity;
        this.id_orgs = id_orgs;
    }

    /** default constructor */
    public ScrTramunit() {
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
     *             column="CODE_TRAMUNIT"
     *             length="21"
     *             not-null="true"
     *         
     */
	public String getCode_tramunit() {
		return code_tramunit;
	}

	public void setCode_tramunit(String code_tramunit) {
		this.code_tramunit = code_tramunit;
	}

	/** 
     *            @hibernate.property
     *             column="NAME_TRAMUNIT"
     *             length="80"
     *             not-null="true"
     *         
     */
	public String getName_tramunit() {
		return name_tramunit;
	}

	public void setName_tramunit(String name_tramunit) {
		this.name_tramunit = name_tramunit;
	}

	/** 
     *            @hibernate.property
     *             column="CODE_ENTITY"
     *             length="21"
     *             not-null="true"
     *         
     */
	public String getCode_entity() {
		return code_entity;
	}

	public void setCode_entity(String code_entity) {
		this.code_entity = code_entity;
	}
	
	/** 
     *            @hibernate.property
     *             column="NAME_ENTITY"
     *             length="80"
     *             not-null="true"
     *         
     */
	public String getName_entity() {
		return name_entity;
	}

	public void setName_entity(String name_entity) {
		this.name_entity = name_entity;
	}

	/** 
     *            @hibernate.property
     *             column="ID_ORGS"
     *             length="10"
     *             not-null="true"
     *         
     */
	public int getId_orgs() {
		return id_orgs;
	}

	public void setId_orgs(int id_orgs) {
		this.id_orgs = id_orgs;
	}
    
    public String toString() {
    	return new ToStringBuilder(this).append("id", getId()).toString();
    }
    
    public boolean equals(Object other) {
    	if ( !(other instanceof ScrTramunit) ) return false;
    	ScrTramunit castOther = (ScrTramunit) other;
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