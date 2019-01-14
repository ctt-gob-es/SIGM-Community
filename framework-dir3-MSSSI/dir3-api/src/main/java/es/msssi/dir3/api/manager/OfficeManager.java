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

import java.util.List;

import es.msssi.dir3.api.type.OfficeCriterionEnum;
import es.msssi.dir3.api.vo.BasicDataOfficeVO;
import es.msssi.dir3.api.vo.ContactsOFIVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.HistoriesVO;
import es.msssi.dir3.api.vo.OfficeVO;
import es.msssi.dir3.api.vo.OfficesVO;
import es.msssi.dir3.api.vo.ServicesVO;
import es.msssi.dir3.core.errors.DIR3Exception;

/**
 * Interfaz para los managers de datos básicos de oficinas.
 * 
 * @author cmorenog
 * 
 */
public interface OfficeManager extends BaseManager<OfficeVO, String, OfficeCriterionEnum> {

    /**
     * Actualiza o inserta los datos de las oficinas obtenidas del DCO.
     * 
     * @param officesDCO
     *            listado de oficinas a actualizar.
     * @throws DIR3Exception .
     */
    public void insertUpdateOffices(
	List<OfficeVO> officesDCO)
	throws DIR3Exception;

    /**
     * Actualiza los datos de las oficinas obtenidas del DCO.
     * 
     * @param officesDCO
     *            listado de oficinas a actualizar.
     * @param contactsDCO
     *            listado de contactos.
     * @param servicesDCO
     *            listado de servicios.
     * @param historiesDCO
     *            listado de históricos.
     * @throws DIR3Exception .
     */
    public void updateOffices(
	OfficesVO officesDCO, ContactsOFIVO contactsDCO, ServicesVO servicesDCO,
	HistoriesVO historiesDCO)
	throws DIR3Exception;

    /**
     * Método para recuperar todas las entidades de un mismo tipo.
     * Viene a ser lo mismo que recuperar todas las filas de una tabla de base
     * de datos.
     * 
     * @return la lista de objetos recuperados.
     * @throws DIR3Exception .
     */
    public List<OfficeVO> getAll()
	throws DIR3Exception;

    /**
     * Método para recuperar todas las entidades de un mismo tipo y que
     * cumplan con los criterios establecidos. Si criterios es nulo viene a ser
     * lo mismo que recuperar todas las filas de una tabla de base de datos.
     * 
     * @param criteria
     *            criterios de búsqueda.
     * @return la lista de objetos recuperados
     * @throws DIR3Exception .
     */
    public List<OfficeVO> find(
	Criteria<OfficeCriterionEnum> criteria)
	throws DIR3Exception;

    /**
     * Método  para recuperar los datos básicos de todas las entidades
     * de un mismo tipo y que cumplan con los criterios establecidos. Si
     * criterios es nulo viene a ser lo mismo que recuperar todas las filas de
     * una tabla de base de datos.
     * 
     * @param criteria
     *            criterios de búsqueda.
     * @return la lista de objetos recuperados
     * @throws DIR3Exception .
     */
    public List<BasicDataOfficeVO> findBasicData(
	Criteria<OfficeCriterionEnum> criteria)
	throws DIR3Exception;

    /**
     * Devuelve el número de entidades de tipo Entity existentes. Para obtener
     * el resultado delega en el dao asociado.
     * 
     * @return int el número de entidades.
     * @throws DIR3Exception .
     */
    public int count()
	throws DIR3Exception;

    /**
     * Devuelve el número de entidades de tipo Entity existentes y que cumplan
     * los criterios establecidos.
     * 
     * @param criterios
     *            criterios de búsqueda.
     * @return int el número de entidades.
     * @throws DIR3Exception .
     */
    public int count(
	List<Criterion<OfficeCriterionEnum>> criterios)
	throws DIR3Exception;

}
