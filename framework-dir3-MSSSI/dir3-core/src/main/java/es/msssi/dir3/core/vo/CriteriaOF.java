/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.msssi.dir3.core.type.OFCriterionEnum;

/**
 * Información de los criterios de búsqueda de oficinas.
 * 
 * @author cmorenog
 * 
 */
public class CriteriaOF implements Serializable {
    private static final long serialVersionUID = -508373998996142816L;

    /**
     * Lista de criterios.
     */

    private List<CriterionOF> criteria = null;

    /**
     * Criterios por los que se van a ordenar los resultados.
     */
    private List<OFCriterionEnum> orderBy = null;

    /**
     * Información de la página para la paginación de resultados.
     */
    private PageInfo pageInfo = null;

    /**
     * Constructor.
     */
    public CriteriaOF() {
	super();
    }

    /**
     * Constructor.
     * 
     * @param criteria
     *            Lista de criterios de búsqueda.
     */
    public CriteriaOF(List<CriterionOF> criteria) {
	setCriteria(criteria);
    }

    /**
     * Obtiene el valor del parámetro criteria.
     * 
     * @return criteria valor del campo a obtener.
     * 
     */
    public List<CriterionOF> getCriteria() {
	return criteria;
    }

    /**
     * Guarda el valor del parámetro criteria.
     * 
     * @param criteria
     *            valor del campo a guardar.
     */
    public void setCriteria(
	List<CriterionOF> criteria) {
	this.criteria = new ArrayList<CriterionOF>();
	if (criteria != null) {
	    this.criteria.addAll(criteria);
	}
    }

    /**
     * Añade un valor a la lista criterio.
     * 
     * @param criterio
     *            valor del campo a guardar.
     */
    public void addCriterion(
	CriterionOF criterio) {
	if (criterio != null) {
	    this.criteria.add(criterio);
	}
    }

    /**
     * Obtiene el valor del parámetro orderBy.
     * 
     * @return orderBy valor del campo a obtener.
     * 
     */
    public List<OFCriterionEnum> getOrderBy() {
	return orderBy;
    }

    /**
     * Guarda el valor del parámetro orderBy.
     * 
     * @param orderBy
     *            valor del campo a guardar.
     */
    public void setOrderBy(
	List<OFCriterionEnum> orderBy) {
	this.orderBy = new ArrayList<OFCriterionEnum>();
	if (orderBy != null) {
	    this.orderBy.addAll(orderBy);
	}
    }

    /**
     * Añade un valor a la lista orderBy.
     * 
     * @param order
     *            valor del campo a guardar.
     */
    public void addOrderBy(
	OFCriterionEnum order) {
	if (orderBy != null) {
	    orderBy.add(order);
	}
    }

    /**
     * Obtiene el valor del parámetro pageInfo.
     * 
     * @return pageInfo valor del campo a obtener.
     * 
     */
    public PageInfo getPageInfo() {
	return pageInfo;
    }

    /**
     * Guarda el valor del parámetro pageInfo.
     * 
     * @param pageInfo
     *            valor del campo a guardar.
     */
    public void setPageInfo(
	PageInfo pageInfo) {
	this.pageInfo = pageInfo;
    }

}
