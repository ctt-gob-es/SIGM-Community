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

/**
 * Información de una página del listado.
 * 
 * @author cmorenog
 * 
 */
public class PageInfo implements Serializable {

    private static final long serialVersionUID = 3453986001873009595L;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_OBJECTS_PER_PAGE = 0;
    private static final int DEFAULT_MAX_NUM_ITEMS = 0;

    /**
     * Número de página.
     */
    private int pageNumber = DEFAULT_PAGE_NUMBER;

    /**
     * Número de registros por página.
     */
    private int objectsPerPage = DEFAULT_OBJECTS_PER_PAGE;

    /**
     * Número máximo de resultados.
     */
    private int maxNumItems = DEFAULT_MAX_NUM_ITEMS;

    /**
     * Constructor.
     */
    public PageInfo() {
	super();
    }

    /**
     * Constructor.
     * 
     * @param maxNumItems
     *            Número máximo de resultados.
     */
    public PageInfo(int maxNumItems) {
	super();
	this.maxNumItems = maxNumItems;
    }

    /**
     * Constructor.
     * 
     * @param pageNumber
     *            Número de página.
     * @param objectsPerPage
     *            Número de registros por página.
     */
    public PageInfo(int pageNumber, int objectsPerPage) {
	super();
	this.pageNumber = pageNumber;
	this.objectsPerPage = objectsPerPage;
    }

    /**
     * Obtiene el valor del parámetro pageNumber.
     * 
     * @return pageNumber valor del campo a obtener.
     */
    public int getPageNumber() {
	return pageNumber;
    }

    /**
     * Guarda el valor del parámetro pageNumber.
     * 
     * @param pageNumber
     *            valor del campo a guardar.
     */
    public void setPageNumber(
	int pageNumber) {
	this.pageNumber = pageNumber;
    }

    /**
     * Obtiene el valor del parámetro objectsPerPage.
     * 
     * @return objectsPerPage valor del campo a obtener.
     */
    public int getObjectsPerPage() {
	return objectsPerPage;
    }

    /**
     * Guarda el valor del parámetro objectsPerPage.
     * 
     * @param objectsPerPage
     *            valor del campo a guardar.
     */
    public void setObjectsPerPage(
	int objectsPerPage) {
	this.objectsPerPage = objectsPerPage;
    }

    /**
     * Obtiene el valor del parámetro maxNumItems.
     * 
     * @return maxNumItems valor del campo a obtener.
     */
    public int getMaxNumItems() {
	return maxNumItems;
    }

    /**
     * Guarda el valor del parámetro maxNumItems.
     * 
     * @param maxNumItems
     *            valor del campo a guardar.
     */
    public void setMaxNumItems(
	int maxNumItems) {
	this.maxNumItems = maxNumItems;
    }

}
