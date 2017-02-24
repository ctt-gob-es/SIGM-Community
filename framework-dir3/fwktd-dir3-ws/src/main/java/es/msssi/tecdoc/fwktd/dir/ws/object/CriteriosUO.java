package es.msssi.tecdoc.fwktd.dir.ws.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Información de los criterios de búsqueda.
 *
 * @author cmorenog
 *
 */
public class CriteriosUO implements Serializable {
	private static final long serialVersionUID = -508373998996142816L;
	
	/**
	 * Lista de criterios.
	 */

	private List<CriterioUO> criterio = null;

	/**
	 * Criterios por los que se van a ordenar los resultados
	 */
	private List<CriterioUOEnum> orderBy = null;

	/**
	 * Información de la página para la paginación de resultados
	 */
	private PageInfoUO pageInfo = null;


	/**
	 * Constructor.
	 */
	public CriteriosUO() {
		super();
	}

	/**
	 * Constructor.
	 *
	 * @param criterios
	 *            Lista de criterios de búsqueda.
	 */
	public CriteriosUO(List<CriterioUO> criterios) {
		setCriterio(criterios);
	}

	/**
	 * Obtiene el valor del parámetro criterio.
	 * 
	 * @return criterio valor del campo a obtener.
	 *
	 */
	public List<CriterioUO> getCriterio() {
		return criterio;
	}
	
	/**
	 * Guarda el valor del parámetro criterio.
	 * 
	 * @param criterio
	 *            valor del campo a guardar.
	 */
	public void setCriterio(List<CriterioUO> criterio) {
		this.criterio = new ArrayList<CriterioUO>();
		if (criterio != null) {
			this.criterio.addAll(criterio);
		}
	}
	
	/**
	 * Añade un valor a la lista criterio.
	 * 
	 * @param criterio
	 *            valor del campo a guardar.
	 */
	public void addCriterio(CriterioUO criterio) {
		if (criterio != null) {
			this.criterio.add(criterio);
		}
	}
	
	/**
	 * Obtiene el valor del parámetro orderBy.
	 * 
	 * @return orderBy valor del campo a obtener.
	 *
	 */
	public List<CriterioUOEnum> getOrderBy() {
		return orderBy;
	}
	
	/**
	 * Guarda el valor del parámetro orderBy.
	 * 
	 * @param orderBy
	 *            valor del campo a guardar.
	 */
	public void setOrderBy(List<CriterioUOEnum> orderBy) {
		this.orderBy = new ArrayList<CriterioUOEnum>();
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
	public void addOrderBy(CriterioUOEnum order) {
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
	public PageInfoUO getPageInfo() {
		return pageInfo;
	}
	
	/**
	 * Guarda el valor del parámetro pageInfo.
	 * 
	 * @param pageInfo
	 *            valor del campo a guardar.
	 */
	public void setPageInfo(PageInfoUO pageInfo) {
		this.pageInfo = pageInfo;
	}

	
}
