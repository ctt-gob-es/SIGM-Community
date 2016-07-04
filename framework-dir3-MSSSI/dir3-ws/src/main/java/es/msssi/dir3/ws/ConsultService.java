/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import es.msssi.dir3.api.service.impl.ConsultServiceDCOImpl;
import es.msssi.dir3.config.Dir3WSSpringApplicationContext;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.core.vo.BasicDataOffice;
import es.msssi.dir3.core.vo.BasicDataUnit;
import es.msssi.dir3.core.vo.CriteriaOF;
import es.msssi.dir3.core.vo.CriteriaUO;
import es.msssi.dir3.core.vo.Office;
import es.msssi.dir3.core.vo.Unit;
import es.msssi.dir3.errors.Dir3WSErrorConstants;
import es.msssi.dir3.errors.Dir3WSException;
import es.msssi.dir3.ws.request.ConsultOFRequest;
import es.msssi.dir3.ws.request.ConsultUORequest;
import es.msssi.dir3.ws.request.GetOFRequest;
import es.msssi.dir3.ws.request.GetUORequest;
import es.msssi.dir3.ws.response.ConsultBasicDataOFResponse;
import es.msssi.dir3.ws.response.ConsultBasicDataUOResponse;
import es.msssi.dir3.ws.response.ConsultOFResponse;
import es.msssi.dir3.ws.response.ConsultUOResponse;
import es.msssi.dir3.ws.response.CountResponse;
import es.msssi.dir3.ws.response.GetOFResponse;
import es.msssi.dir3.ws.response.GetUOResponse;

/**
 * Clase que implementa los servicios web de consulta de dir3.
 * 
 * @author cmorenog
 * 
 */

@WebService
public class ConsultService extends BaseService {
    private static final Logger LOG = Logger.getLogger(ConsultService.class);
    private static ApplicationContext contexto = Dir3WSSpringApplicationContext.getInstance()
	.getApplicationContext();
    private ConsultServiceDCOImpl consultServiceDCO;

    /**
     * Devuelve el bean del servicio.
     * @return bean del servicio.
     */
    private ConsultServiceDCOImpl getConsultServiceDCO() {
	if (consultServiceDCO == null) {
	    consultServiceDCO =
		(ConsultServiceDCOImpl) contexto.getBean("consultServiceDCO");
	}
	return consultServiceDCO;
    }

    /**
     * Retorna las unidades orgánicas que cumplan los criterios de búsqueda.
     * 
     * @param request
     *            objeto con los criterios de búsqueda.
     * @return ConsultarUOResponse lista de Unidades orgánicas que cumplen los
     *         criterios de búsqueda.
     * @throws Dir3WSException .
     */
    @WebMethod
    public ConsultUOResponse findUnits(
	@WebParam(name = "request") ConsultUORequest request)
	throws Dir3WSException {
	try {
	    LOG.info("INIT findUnits");
	    ConsultUOResponse result = new ConsultUOResponse();
	    CriteriaUO criteria = new CriteriaUO();
	    criteria.setCriteria(request.getCriteria());
	    criteria.setOrderBy(request.getOrderBy());
	    criteria.setPageInfo(request.getPageInfo());
	    List<Unit> units = getConsultServiceDCO().findUnits(
		criteria);

	    result.setUnits(units);
	    LOG.info("END findUnits");
	    return result;
	}
	catch (DIR3Exception e) {
	    LOG.error(
		Dir3WSErrorConstants.FIND_UO_MESSAGE, e);
	    throw new Dir3WSException(
		Dir3WSErrorConstants.FIND_UO_MESSAGE, e.getMessage(), e.getCause());
	}
    }

    /**
     * Retorna los datos básicos de las unidades orgánicas que cumplan los
     * criterios de búsqueda.
     * 
     * @param request
     *            objeto con los criterios de búsqueda.
     * @return ConsultBasicDataUOResponse lista de Unidades orgánicas que
     *         cumplen los criterios de búsqueda.
     * @throws Dir3WSException .
     */
    @WebMethod
    public ConsultBasicDataUOResponse findBasicDataUnits(
	@WebParam(name = "request") ConsultUORequest request)
	throws Dir3WSException {
	try {
	    LOG.info("INIT findBasicDataUnits");
	    ConsultBasicDataUOResponse result = new ConsultBasicDataUOResponse();
	    CriteriaUO criteria = new CriteriaUO();
	    criteria.setCriteria(request.getCriteria());
	    criteria.setOrderBy(request.getOrderBy());
	    criteria.setPageInfo(request.getPageInfo());
	    List<BasicDataUnit> units = getConsultServiceDCO().findBasicDataUnits(
		criteria);

	    result.setUnits(units);
	    LOG.info("END findBasicDataUnits");
	    return result;
	}
	catch (DIR3Exception e) {
	    LOG.error(
		Dir3WSErrorConstants.FIND_UO_MESSAGE, e);
	    throw new Dir3WSException(
		Dir3WSErrorConstants.FIND_UO_MESSAGE, e.getMessage(), e.getCause());
	}
    }

    /**
     * Retorna las oficinas que cumplan los criterios de búsqueda.
     * 
     * @param request
     *            objeto con los criterios de búsqueda.
     * @return ConsultarOFResponse lista de oficinas que cumplen los criterios
     *         de búsqueda.
     * @throws Dir3WSException .
     */
    @WebMethod
    public ConsultOFResponse findOffices(
	@WebParam(name = "request") ConsultOFRequest request)
	throws Dir3WSException {
	try {
	    LOG.info("INIT findOffices");
	    ConsultOFResponse result = new ConsultOFResponse();
	    CriteriaOF criteria = new CriteriaOF();
	    criteria.setCriteria(request.getCriteria());
	    criteria.setOrderBy(request.getOrderBy());
	    criteria.setPageInfo(request.getPageInfo());
	    List<Office> offices = getConsultServiceDCO().findOffices(
		criteria);

	    result.setOffices(offices);
	    LOG.info("END findOffices");
	    return result;
	}
	catch (DIR3Exception e) {
	    LOG.error(
		Dir3WSErrorConstants.FIND_OF_MESSAGE, e);
	    throw new Dir3WSException(
		Dir3WSErrorConstants.FIND_OF_MESSAGE, e.getMessage(), e.getCause());
	}
    }

    /**
     * Retorna los datos básicos de las oficinas que cumplan los criterios de
     * búsqueda.
     * 
     * @param request
     *            objeto con los criterios de búsqueda.
     * @return ConsultBasicDataOFResponse lista de oficinas que cumplen los
     *         criterios de búsqueda.
     * @throws Dir3WSException .
     */
    @WebMethod
    public ConsultBasicDataOFResponse findBasicDataOffices(
	@WebParam(name = "request") ConsultOFRequest request)
	throws Dir3WSException {
	try {
	    LOG.info("INIT findOffices");
	    ConsultBasicDataOFResponse result = new ConsultBasicDataOFResponse();
	    CriteriaOF criteria = new CriteriaOF();
	    criteria.setCriteria(request.getCriteria());
	    criteria.setOrderBy(request.getOrderBy());
	    criteria.setPageInfo(request.getPageInfo());
	    List<BasicDataOffice> offices = getConsultServiceDCO().findBasicDataOffices(
		criteria);

	    result.setOffices(offices);
	    LOG.info("END findOffices");
	    return result;
	}
	catch (DIR3Exception e) {
	    LOG.error(
		Dir3WSErrorConstants.FIND_OF_MESSAGE, e);
	    throw new Dir3WSException(
		Dir3WSErrorConstants.FIND_OF_MESSAGE, e.getMessage(), e.getCause());
	}
    }

    /**
     * Retorna el número de unidades orgánicas que cumplan los criterios de
     * búsqueda.
     * 
     * @param request
     *            objeto con los criterios de búsqueda.
     * @return CountResponse número de Unidades orgánicas que cumplen los
     *         criterios de búsqueda.
     * @throws Dir3WSException .
     */
    @WebMethod
    public CountResponse countUnits(
	@WebParam(name = "request") ConsultUORequest request)
	throws Dir3WSException {
	try {
	    LOG.info("INIT countUnits");
	    CountResponse result = new CountResponse();
	    int count = getConsultServiceDCO().countUnits(
		request.getCriteria());

	    result.setCount(count);
	    LOG.info("END findUnits");
	    return result;
	}
	catch (DIR3Exception e) {
	    LOG.error(
		Dir3WSErrorConstants.COUNT_UO_MESSAGE, e);
	    throw new Dir3WSException(
		Dir3WSErrorConstants.COUNT_UO_MESSAGE, e.getMessage(), e.getCause());
	}
    }

    /**
     * Retorna el número de oficinas que cumplan los criterios de búsqueda.
     * 
     * @param request
     *            objeto con los criterios de búsqueda.
     * @return CountResponse número de oficinas que cumplen los criterios de
     *         búsqueda.
     * @throws Dir3WSException .
     */
    @WebMethod
    public CountResponse countOffices(
	@WebParam(name = "request") ConsultOFRequest request)
	throws Dir3WSException {
	try {
	    LOG.info("INIT countOffices");
	    CountResponse result = new CountResponse();
	    int count = getConsultServiceDCO().countOffices(
		request.getCriteria());

	    result.setCount(count);
	    LOG.info("END countOffices");
	    return result;
	}
	catch (DIR3Exception e) {
	    LOG.error(
		Dir3WSErrorConstants.COUNT_OF_MESSAGE, e);
	    throw new Dir3WSException(
		Dir3WSErrorConstants.COUNT_OF_MESSAGE, e.getMessage(), e.getCause());
	}
    }

    /**
     * Retorna la oficina con el identificador dado.
     * 
     * @param request
     *            identificador de la oficina.
     * @return GetOFResponse la oficina.
     * @throws Dir3WSException .
     */
    @WebMethod
    public GetOFResponse getOffice(
	@WebParam(name = "request") GetOFRequest request)
	throws Dir3WSException {
	try {
	    LOG.info("INIT getOffice");
	    GetOFResponse result = new GetOFResponse();
	    Office office = getConsultServiceDCO().getOffice(
		request.getOfficeId());

	    result.setOffice(office);
	    LOG.info("END getOffice");
	    return result;
	}
	catch (DIR3Exception e) {
	    LOG.error(
		Dir3WSErrorConstants.GET_OF_MESSAGE, e);
	    throw new Dir3WSException(
		Dir3WSErrorConstants.GET_OF_MESSAGE, e.getMessage(), e.getCause());
	}
    }

    /**
     * Retorna la unidad orgánica con el identificador dado.
     * 
     * @param request
     *            identificador de la oficina.
     * @return GetOFResponse la oficina.
     * @throws Dir3WSException .
     */
    @WebMethod
    public GetUOResponse getUnit(
	@WebParam(name = "request") GetUORequest request)
	throws Dir3WSException {
	try {
	    LOG.info("INIT getUnit");
	    GetUOResponse result = new GetUOResponse();
	    Unit unit = getConsultServiceDCO().getUnit(
		request.getUnitId());

	    result.setUnit(unit);
	    LOG.info("END getUnit");
	    return result;
	}
	catch (DIR3Exception e) {
	    LOG.error(
		Dir3WSErrorConstants.GET_UO_MESSAGE, e);
	    throw new Dir3WSException(
		Dir3WSErrorConstants.GET_UO_MESSAGE, e.getMessage(), e.getCause());
	}
    }
}
