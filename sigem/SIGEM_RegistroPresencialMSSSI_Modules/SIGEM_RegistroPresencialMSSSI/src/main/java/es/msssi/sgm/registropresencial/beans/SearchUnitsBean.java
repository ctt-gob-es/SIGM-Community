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
 * Bean con el formulario de búsqueda de unidades.
 * 
 * @author cmorenog
 * */
public class SearchUnitsBean extends GenericBean {

	private static final long serialVersionUID = 1L;

	/** Nivel de búsqueda. */
	private int searchType = 1;
	/** texto de búsqueda. */
	private String searchText = "";
	/** ccaa de búsqueda. */
	private String ccaaId = null;
	/** provincia de búsqueda. */
	private Integer provId = null;
	/** código del tipo de unidad a buscar. */	
	private String codeTipoUnidad = "";

	/**
	 * Obtiene el valor del parámetro searchType.
	 * 
	 * @return searchType valor del campo a obtener.
	 */
	public int getSearchType() {
		return searchType;
	}

	/**
	 * Guarda el valor del parámetro searchType.
	 * 
	 * @param searchType
	 *            valor del campo a guardar.
	 */
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	/**
	 * Obtiene el valor del parámetro searchText.
	 * 
	 * @return searchText valor del campo a obtener.
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * Guarda el valor del parámetro searchText.
	 * 
	 * @param searchText
	 *            valor del campo a guardar.
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	/**
	 * Obtiene el valor del parámetro ccaaId.
	 * 
	 * @return ccaaId valor del campo a obtener.
	 */
	public String getCcaaId() {
		return ccaaId;
	}

	/**
	 * Guarda el valor del parámetro ccaaId.
	 * 
	 * @param ccaaId
	 *            valor del campo a guardar.
	 */
	public void setCcaaId(String ccaaId) {
		this.ccaaId = ccaaId;
	}

	/**
	 * Obtiene el valor del parámetro provId.
	 * 
	 * @return provId valor del campo a obtener.
	 */
	public Integer getProvId() {
		return provId;
	}

	/**
	 * Guarda el valor del parámetro provId.
	 * 
	 * @param provId
	 *            valor del campo a guardar.
	 */
	public void setProvId(Integer provId) {
		this.provId = provId;
	}

	/**
	 * Obtiene el valor del parámetro codeTipoUnidad
	 * 
	 * @return codeTipoUnidad valor del codigo del tipo de unidad
	 */
	public String getCodeTipoUnidad() {
		return codeTipoUnidad;
	}
	
	/**
	 * Guarda el valor del parámetro codeTipoUnidad
	 * 
	 * @param codeTipoUnidad valor del codigo del tipo de unidad
	 */
	public void setCodeTipoUnidad(String codeTipoUnidad) {
		this.codeTipoUnidad = codeTipoUnidad;		
	}

}
