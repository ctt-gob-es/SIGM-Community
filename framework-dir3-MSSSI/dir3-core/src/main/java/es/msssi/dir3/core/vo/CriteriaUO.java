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

import es.msssi.dir3.core.type.UOCriterionEnum;

/**
 * Información de los criterios de búsqueda de unidades orgánicas.
 * 
 * @author cmorenog
 * 
 */
public class CriteriaUO implements Serializable {
    private static final long serialVersionUID = -508373998996142816L;

    /**
     * Lista de criterios.
     */

    private List<CriterionUO> criteria = null;

    /**
     * Criterios por los que se van a ordenar los resultados.
     */
    private List<UOCriterionEnum> orderBy = null;

    /**
     * Información de la página para la paginación de resultados.
     */
    private PageInfo pageInfo = null;

    /**
     * Constructor.
     */
    public CriteriaUO() {
	super();
    }

    /**
     * Constructor.
     * 
     * @param criteria
     *            Lista de criterios de búsqueda.
     */
    public CriteriaUO(List<CriterionUO> criteria) {
	setCriteria(criteria);
    }

    /**
     * Obtiene el valor del parámetro criteria.
     * 
     * @return criteria valor del campo a obtener.
     * 
     */
    public List<CriterionUO> getCriteria() {
	return criteria;
    }

    /**
     * Guarda el valor del parámetro criteria.
     * 
     * @param criteria
     *            valor del campo a guardar.
     */
    public void setCriteria(
	List<CriterionUO> criteria) {
	this.criteria = new ArrayList<CriterionUO>();
	if (criteria != null) {
	    this.criteria.addAll(criteria);
	}
    }

    /**
     * Añade un valor a la lista criterion.
     * 
     * @param criterion
     *            valor del campo a guardar.
     */
    public void addCriterion(
	CriterionUO criterion) {
	if (criterion != null) {
	    this.criteria.add(criterion);
	}
    }

    /**
     * Obtiene el valor del parámetro orderBy.
     * 
     * @return orderBy valor del campo a obtener.
     * 
     */
    public List<UOCriterionEnum> getOrderBy() {
	return orderBy;
    }

    /**
     * Guarda el valor del parámetro orderBy.
     * 
     * @param orderBy
     *            valor del campo a guardar.
     */
    public void setOrderBy(
	List<UOCriterionEnum> orderBy) {
	this.orderBy = new ArrayList<UOCriterionEnum>();
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
	UOCriterionEnum order) {
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
