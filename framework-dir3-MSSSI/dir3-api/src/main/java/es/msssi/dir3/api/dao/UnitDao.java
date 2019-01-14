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

import es.msssi.dir3.api.type.UOCriterionEnum;
import es.msssi.dir3.api.vo.BasicDataUnitVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.UnitVO;

/**
 * Interfaz de los DAOs de datos básicos de unidades orgánicas.
 * 
 * @author cmorenog
 * 
 */
public interface UnitDao extends BaseDao<UnitVO, String, UOCriterionEnum> {



    /**
     * Método genérico para recuperar todas las entidades de un mismo tipo y que
     * cumplan los criterios introducidos.
     * 
     * @param criterios
     *            Criterios de búsqueda de tipo C.
     * @return la lista de objetos.
     * @throws SQLException
     */
    public List<UnitVO> find(
	Criteria<UOCriterionEnum> criteria)
	throws SQLException;

    /**
     * Método  para recuperar los datos básicos de todas las unidades
     *  que cumplan los criterios introducidos.
     * 
     * @param criterios
     *            Criterios de búsqueda .
     * @return la lista de unidades.
     * @throws SQLException
     */
    public List<BasicDataUnitVO> findBasicData(
	Criteria<UOCriterionEnum> criteria)
	throws SQLException;

    /**
     * Devuelve el número de entidades de unidades existentes que cumplan los
     * criterios introducidos.
     * 
     * @param criterios
     *            Criterios de búsqueda.
     * @return número de unidades.
     * @throws SQLException
     */
    public int count(
	List<Criterion<UOCriterionEnum>> criteria)
	throws SQLException;

}
