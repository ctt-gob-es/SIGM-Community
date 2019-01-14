/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.manager;

import es.msssi.dir3.api.type.CriterionEnum;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.core.vo.Entity;

/**
 * Interfaz para los managers.
 * 
 * @author cmorenog
 * 
 * @param <T>
 *            tipo de entidad a gestionar.
 * @param <tId>
 *            tipo de la clave primaria de la entidad.
 * @param <C>
 *            tipo de criterio para la búsqueda.
 * 
 */
public interface BaseManager<T extends Entity, tId, C extends CriterionEnum> {
    /**
     * Método genérico para recuperar un objeto basándonos en su identificador.
     * 
     * @param id
     *            identificador del objeto a recuperar.
     * @return el objeto recuperado.
     * @throws DIR3Exception .
     */
    public T get(
	tId anId)
	throws DIR3Exception;

    /**
     * Comprueba la existencia de un objeto de tipo Entity con el identificador
     * dado.
     * 
     * @param id
     *            identificador del objeto
     * @return <code>true</code> si el objeto existe, <code>false</code> en caso
     *         contrario
     * @throws DIR3Exception .
     */
    public boolean exists(
	tId id)
	throws DIR3Exception;

    /**
     * Método genérico para guardar un objeto. Maneja tanto la inserción como la
     * actualización.
     * 
     * @param entity
     * @throws DIR3Exception .
     */
    public void save(
	T entity)
	throws DIR3Exception;

    /**
     * Método genérico para actualizar un objeto de la clase dada.
     * 
     * @param entity
     *            objeto a actualizar
     * @throws DIR3Exception .
     */
    public void update(
	T entity)
	throws DIR3Exception;
}