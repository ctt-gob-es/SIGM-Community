/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import es.msssi.dir3.api.helper.OfficesHelper;
import es.msssi.dir3.api.helper.UnitsHelper;
import es.msssi.dir3.api.manager.OfficeManager;
import es.msssi.dir3.api.manager.UnitManager;
import es.msssi.dir3.api.type.OfficeCriterionEnum;
import es.msssi.dir3.api.type.UOCriterionEnum;
import es.msssi.dir3.api.vo.BasicDataOfficeVO;
import es.msssi.dir3.api.vo.BasicDataUnitVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.OfficeVO;
import es.msssi.dir3.api.vo.UnitVO;
import es.msssi.dir3.core.ConsultServiceDCO;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.core.vo.BasicDataOffice;
import es.msssi.dir3.core.vo.BasicDataUnit;
import es.msssi.dir3.core.vo.CriteriaOF;
import es.msssi.dir3.core.vo.CriteriaUO;
import es.msssi.dir3.core.vo.CriterionOF;
import es.msssi.dir3.core.vo.CriterionUO;
import es.msssi.dir3.core.vo.Office;
import es.msssi.dir3.core.vo.Unit;

/**
 * Implementación local del servicio de consulta del Directorio Común (DIR3).
 * 
 * @author cmorenog.
 * 
 */
public class ConsultServiceDCOImpl implements ConsultServiceDCO {

    /**
     * Logger de la clase.
     */
    private static final Logger logger = Logger.getLogger(ConsultServiceDCOImpl.class);

    /**
     * Gestor de oficinas.
     */
    private OfficeManager officeManager;

    /**
     * Gestor de unidades orgánicas.
     */
    private UnitManager unitManager;

    /**
     * Constructor.
     */
    public ConsultServiceDCOImpl() {
	super();
    }

    /**
     * Obtiene el valor del parámetro officeManager.
     * 
     * @return officeManager valor del campo a obtener.
     */
    public OfficeManager getOfficeManager() {
	return officeManager;
    }

    /**
     * Guarda el valor del parámetro officeManager.
     * 
     * @param officeManager
     *            valor del campo a guardar.
     */
    public void setOfficeManager(
	OfficeManager officeManager) {
	this.officeManager = officeManager;
    }

    /**
     * Obtiene el valor del parámetro unitManager.
     * 
     * @return unitManager valor del campo a obtener.
     */
    public UnitManager getUnitManager() {
	return unitManager;
    }

    /**
     * Guarda el valor del parámetro unitManager.
     * 
     * @param unitManager
     *            valor del campo a guardar.
     */
    public void setUnitManager(
	UnitManager unitManager) {
	this.unitManager = unitManager;
    }

    /**
     * Obtiene el número de oficinas que cumplan con los criterios indicados. Si
     * los criterios son nulos se devolverán el total de las oficinas.
     * 
     * @param criteria
     *            criterios de búsqueda.
     * @return int número de oficinas.
     * @throws DIR3Exception .
     */
    public int countOffices(
	List<CriterionOF> criteria)
	throws DIR3Exception {

	logger.info("Llamada a countOficinas: criterios=[{}]");
	List<Criterion<OfficeCriterionEnum>> criteriaAPI =
	    OfficesHelper.criterionListOFCoretoCriterionListAPIOF(criteria);
	// Obtener el número de oficinas que cumplan los criterios
	return getOfficeManager().count(
	    criteriaAPI);
    }

    /**
     * Obtiene la lista de oficinas que cumplan con los criterios indicados. Si
     * los criterios son nulos se devolverán todas las oficinas.
     * 
     * @param criteria
     *            criterios de búsqueda.
     * @return List lista de oficinas.
     * @throws DIR3Exception .
     */
    public List<Office> findOffices(
	CriteriaOF criteria)
	throws DIR3Exception {

	logger.info("Llamada a findOficinas: criterios=[{}]");
	Criteria<OfficeCriterionEnum> criteriaAPI =
	    OfficesHelper.criteriaOFCoretoCriteriaAPIOF(criteria);
	// Búsqueda de oficinas según los criterios
	return OfficesHelper.getOffices((List<OfficeVO>) getOfficeManager().find(
	    criteriaAPI));
    }

    /**
     * Obtiene la lista de datos básicos de oficinas que cumplan con los
     * criterios indicados. Si los criterios son nulos se devolverán todas las
     * oficinas.
     * 
     * @param criteria
     *            criterios de búsqueda.
     * @return List lista de oficinas.
     * @throws DIR3Exception .
     */
    public List<BasicDataOffice> findBasicDataOffices(
	CriteriaOF criteria)
	throws DIR3Exception {

	logger.info("Llamada a findOficinas: criterios=[{}]");
	Criteria<OfficeCriterionEnum> criteriaAPI =
	    OfficesHelper.criteriaOFCoretoCriteriaAPIOF(criteria);
	// Búsqueda de oficinas según los criterios
	return UnitsHelper.getRelationships((List<BasicDataOfficeVO>) getOfficeManager()
	    .findBasicData(
		criteriaAPI));
    }

    /**
     * Obtiene la oficina con el código introducido.
     * 
     * @param id
     *            el código de la oficina a recuperar.
     * @return unidadorganica oficina.
     * @throws DIR3Exception .
     */
    public Office getOffice(
	String id)
	throws DIR3Exception {

	logger.info("Llamada a getOficina: id=[{}] " +
	    id);

	Assert.hasText(
	    id, "'id' must not be empty");

	// Obtener los datos básicos de la oficina
	return OfficesHelper.getOffice((OfficeVO) getOfficeManager().get(
	    id));
    }

    /**
     * Obtiene el número de unidades orgánicas que cumplan con los criterios
     * indicados. Si los criterios son nulos se devolverán el total de las
     * unidades orgánicas.
     * 
     * @param criterios
     *            criterios de búsqueda.
     * @return int número de unidades orgánicas.
     * @throws DIR3Exception . 
     */
    public int countUnits(
	List<CriterionUO> criteria)
	throws DIR3Exception {

	logger.info("Llamada a countUnidadesOrganicas: criterios=[{}]");
	List<Criterion<UOCriterionEnum>> criteriaAPI =
	    UnitsHelper.criterionListUOCoretoCriterionListAPIUO(criteria);
	// Obtener el número de unidades orgánicas que cumplan los criterios
	return getUnitManager().count(
	    criteriaAPI);
    }

    /**
     * Obtiene la lista de unidades orgánicas que cumplan con los criterios
     * indicados. Si los criterios son nulos se devolverán todas las unidades
     * orgánicas.
     * 
     * @param criteria
     *            criterios de búsqueda.
     * @return List lista de unidades orgánicas.
     * @throws DIR3Exception .
     */
    public List<Unit> findUnits(
	CriteriaUO criteria)
	throws DIR3Exception {

	logger.info("Llamada a findUnidadesOrganicas: criterios=[{}]");
	Criteria<UOCriterionEnum> criteriaAPI = UnitsHelper.criteriaUOCoretoCriteriaAPIUO(criteria);
	// Búsqueda de unidades orgánicas según los criterios
	return UnitsHelper.getUnits((List<UnitVO>) getUnitManager().find(
	    criteriaAPI));
    }

    /**
     * Obtiene la lista de los datos básicos de unidades orgánicas que cumplan
     * con los criterios indicados. Si los criterios son nulos se devolverán
     * todas las unidades orgánicas.
     * 
     * @param criteria
     *            criterios de búsqueda.
     * @return List lista de unidades orgánicas.
     * @throws DIR3Exception .
     */
    public List<BasicDataUnit> findBasicDataUnits(
	CriteriaUO criteria)
	throws DIR3Exception {

	logger.info("Llamada a findUnidadesOrganicas: criterios=[{}]");
	Criteria<UOCriterionEnum> criteriaAPI = UnitsHelper.criteriaUOCoretoCriteriaAPIUO(criteria);
	// Búsqueda de unidades orgánicas según los criterios
	return OfficesHelper.getRelationships((List<BasicDataUnitVO>) getUnitManager()
	    .findBasicData(
		criteriaAPI));
    }

    /**
     * Obtiene la unidad orgánica con el código introducido.
     * 
     * @param id
     *            el código de la unidad a recuperar.
     * @return unidadorganica unidad orgánica.
     * @throws DIR3Exception .
     */
    public Unit getUnit(
	String id)
	throws DIR3Exception {

	logger.info("Llamada a getDatosBasicosOficina: id=[{}] " +
	    id);

	Assert.hasText(
	    id, "'id' must not be empty");

	// Obtener los datos básicos de la unidad orgánica
	return UnitsHelper.getUnit((UnitVO) getUnitManager().get(
	    id));
    }

}
