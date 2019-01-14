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
 * Bean correspondiente a la búsqueda de registros asociados.
 * 
 * @author cmorenog
 */
public class AsocRegisterBean extends GenericBean {
	private static final long serialVersionUID = 1L;
	
	// número de registro
	private String fld1;
	private Integer idLibro;
	
	/**
	 * Obtiene el valor del parámetro fld1.
	 * 
	 * @return fld1 valor del campo a obtener.
	 */
	public String getFld1() {
		return fld1;
	}
	
	/**
	 * Guarda el valor del parámetro fld1.
	 * 
	 * @param fld1
	 *            valor del campo a guardar.
	 */
	public void setFld1(String fld1) {
		this.fld1 = fld1;
	}
	
	/**
	 * Obtiene el valor del parámetro idLibro.
	 * 
	 * @return idLibro valor del campo a obtener.
	 */
	public Integer getIdLibro() {
		return idLibro;
	}
	
	/**
	 * Guarda el valor del parámetro idLibro.
	 * 
	 * @param idLibro
	 *            valor del campo a guardar.
	 */
	public void setIdLibro(Integer idLibro) {
		this.idLibro = idLibro;
	}
	
}