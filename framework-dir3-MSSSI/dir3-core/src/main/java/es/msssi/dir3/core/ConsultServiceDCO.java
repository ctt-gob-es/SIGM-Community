/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.core;

import java.util.List;

import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.core.vo.CriteriaOF;
import es.msssi.dir3.core.vo.CriteriaUO;
import es.msssi.dir3.core.vo.CriterionOF;
import es.msssi.dir3.core.vo.CriterionUO;
import es.msssi.dir3.core.vo.Office;
import es.msssi.dir3.core.vo.Unit;

/**
 * Interfaz del servicio de consulta del Directorio Común (DIR3).
 * 
 * @author cmorenog
 * 
 */
public interface ConsultServiceDCO {

    /**
     * Obtiene el número de oficinas encontradas según los criterios de
     * búsqueda.
     * 
     * @param criteria
     *            Criterios de búsqueda.
     * @return Número de oficinas encontradas.
     * @throws DIR3Exception .
     */
    public int countOffices(
	List<CriterionOF> criteria)
	throws DIR3Exception;

    /**
     * Realiza una búsqueda de oficinas que cumplan con los criterios
     * establecidos.
     * 
     * @param criteria
     *            Criterios de búsqueda.
     * @return Oficinas encontradas.
     * @throws DIR3Exception .
     */
    public List<Office> findOffices(
	CriteriaOF criteria)
	throws DIR3Exception;

    /**
     * Obtiene los datos básicos de una oficina.
     * 
     * @param id
     *            Identificador de la oficina.
     * @return Datos básicos de la oficina.
     * @throws DIR3Exception .
     */
    public Office getOffice(
	String id)
	throws DIR3Exception;

    /**
     * Obtiene el número de unidades orgánicas encontradas según los criterios
     * de búsqueda.
     * 
     * @param criteria
     *            Criterios de búsqueda.
     * @return Número de unidades Oorgánicas encontradas.
     * @throws DIR3Exception .
     */
    public int countUnits(
	List<CriterionUO> criteria)
	throws DIR3Exception;

    /**
     * Realiza una búsqueda de unidades orgánicas que cumplan con los criterios
     * establecidos.
     * 
     * @param criteria
     *            Criterios de búsqueda.
     * @return Unidades orgánicas encontradas.
     * @throws DIR3Exception .
     */
    public List<Unit> findUnits(
	CriteriaUO criteria)
	throws DIR3Exception;

    /**
     * Obtiene los datos básicos de una unidad orgánica.
     * 
     * @param id
     *            Identificador de la unidad orgánica.
     * @return Datos básicos de la unidad orgánica.
     * @throws DIR3Exception .
     */
    public Unit getUnit(
	String id)
	throws DIR3Exception;

}
