/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.dao;

import java.sql.SQLException;
import java.util.List;

import es.msssi.dir3.api.type.OfficeCriterionEnum;
import es.msssi.dir3.api.vo.BasicDataOfficeVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.OfficeVO;

/**
 * Interfaz de los DAOs de datos básicos de oficinas.
 * 
 * @author cmorenog
 * 
 */
public interface OfficeDao extends BaseDao<OfficeVO, String, OfficeCriterionEnum> {


    /**
     * Método genérico para recuperar todas las oficinas y que
     * cumplan los criterios introducidos.
     * 
     * @param criteria
     *            Criterios de búsqueda.
     * @return la lista de oficinas.
     * @throws SQLException .
     */
    public List<OfficeVO> find(
	Criteria<OfficeCriterionEnum> criteria)
	throws SQLException;

    /**
     * Método genérico para recuperar los datos básicos de todas las oficinas y que
     * cumplan los criterios introducidos.
     * 
     * @param criteria
     *            Criterios de búsqueda.
     * @return la lista de datos básicos de oficinas.
     * @throws SQLException .
     */
    public List<BasicDataOfficeVO> findBasicData(
	Criteria<OfficeCriterionEnum> criteria)
	throws SQLException;

    /**
     * Devuelve el número de oficinas existentes que cumplan los
     * criterios introducidos.
     * 
     * @param criteria
     *            Criterios de búsqueda de tipo C.
     * @return número de entidades.
     * @throws SQLException .
     */
    public int count(
	List<Criterion<OfficeCriterionEnum>> criteria)
	throws SQLException;

}
