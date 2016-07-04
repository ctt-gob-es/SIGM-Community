/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Información básica.
 * 
 * @author cmorenog
 * 
 */
public class Entity implements Serializable {

    private static final long serialVersionUID = 5515426564073999982L;

    private String id;

    /**
     * Obtiene el valor del parámetro id.
     * 
     * @return id valor del campo a obtener.
     */
    public String getId() {
	return id;
    }

    /**
     * Obtiene el valor del parámetro id.
     * 
     * @return id valor del campo a obtener.
     */
    public Long getIdAsLong() {
	return Long.parseLong(id);
    }

    /**
     * Obtiene el valor del parámetro id.
     * 
     * @return id valor del campo a obtener.
     */
    public Integer getIdAsInteger() {
	return Integer.parseInt(id);
    }

    /**
     * Guarda el valor del parámetro id.
     * 
     * @param id
     *            valor del campo a guardar.
     */
    public void setId(
	String anId) {
	this.id = anId;
    }

    /**
     * Implementación comparando identificadores.
     * 
     * @param o
     *            objeto a comparar.
     * @return boolean true si son iguales.
     * 
     */
    @Override
    public boolean equals(
	Object o) {
	if ((this == o))
	    return true;
	if (!(o instanceof Entity))
	    return false;

	Entity otherEntity = (Entity) o;

	return new EqualsBuilder().append(
	    otherEntity.getId(), this.getId()).isEquals();
    }

    /**
     * Devuelve el hash code del objeto.
     * 
     * @return int el hash code.
     */
    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(
	    id).toHashCode();
    }

    /**
     * Devuelve el id en un string.
     * 
     * @return String el id en un string..
     */

    @Override
    public String toString() {
	return "Entity con ID: " +
	    getId();
    }

}
