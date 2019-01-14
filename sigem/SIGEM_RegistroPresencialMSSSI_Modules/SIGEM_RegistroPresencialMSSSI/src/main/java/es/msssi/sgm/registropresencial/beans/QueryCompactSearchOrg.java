/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.beans;

/**
 * Bean que guarda los datos de la query de la búsqueda rápida de organismos.
 * 
 * @author cmorenog
 */
public class QueryCompactSearchOrg extends GenericBean {

    private static final long serialVersionUID = 4907664550675804289L;
    /** where de la consulta. */
    private String where;
    /** orderby de la consulta. */
    private String orderBy;

    /**
     * Obtiene el valor del parámetro where.
     * 
     * @return where valor del campo a obtener.
     */
    public String getWhere() {
	return where;
    }

    /**
     * Guarda el valor del parámetro where.
     * 
     * @param where
     *            valor del campo a guardar.
     */
    public void setWhere(
	String where) {
	this.where = where;
    }

    /**
     * Obtiene el valor del parámetro orderBy.
     * 
     * @return orderBy valor del campo a obtener.
     */
    public String getOrderBy() {
	return orderBy;
    }

    /**
     * Guarda el valor del parámetro orderBy.
     * 
     * @param orderBy
     *            valor del campo a guardar.
     */
    public void setOrderBy(
	String orderBy) {
	this.orderBy = orderBy;
    }

}
