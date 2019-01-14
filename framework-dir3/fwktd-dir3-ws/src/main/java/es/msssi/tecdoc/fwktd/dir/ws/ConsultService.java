package es.msssi.tecdoc.fwktd.dir.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import es.ieci.tecdoc.fwktd.dir3.api.service.impl.ServicioConsultaDirectorioComunImpl;
import es.ieci.tecdoc.fwktd.dir3.core.type.CriterioUnidadOrganicaEnum;
import es.ieci.tecdoc.fwktd.dir3.core.vo.Criterios;
import es.ieci.tecdoc.fwktd.dir3.core.vo.DatosBasicosUnidadOrganica;
import es.msssi.tecdoc.fwktd.dir.config.Dir3WSSpringApplicationContext;
import es.msssi.tecdoc.fwktd.dir.errors.Dir3WSErrorConstants;
import es.msssi.tecdoc.fwktd.dir.errors.Dir3WSException;
import es.msssi.tecdoc.fwktd.dir.ws.object.ConsultarUORequest;
import es.msssi.tecdoc.fwktd.dir.ws.object.ConsultarUOResponse;
import es.msssi.tecdoc.fwktd.dir.ws.object.DatosUnidadOrganica;
import es.msssi.tecdoc.fwktd.dir.ws.utils.Converter;

/**
 * Clase que implementa los servicios web de consulta de dir3.
 * 
 * @author cmorenog
 * 
 */

@WebService
public class ConsultService extends BaseService {
	private static final Logger LOG = Logger.getLogger(ConsultService.class);
	private static ApplicationContext contexto = Dir3WSSpringApplicationContext
			.getInstance().getApplicationContext();
	private ServicioConsultaDirectorioComunImpl fwktd_dir3_api_servicioConsultaDirectorioComunImpl;
	
	private ServicioConsultaDirectorioComunImpl
			getServicioConsultaDirectorioComun() {
		if (fwktd_dir3_api_servicioConsultaDirectorioComunImpl == null) {
			fwktd_dir3_api_servicioConsultaDirectorioComunImpl =
					(ServicioConsultaDirectorioComunImpl) contexto
							.getBean("fwktd_dir3_api_servicioConsultaDirectorioComunImpl");
		}
		return fwktd_dir3_api_servicioConsultaDirectorioComunImpl;
	}
	
	/**
	 * Retorna las unidades orgánicas que cumplan los criterios de búsqueda.
	 * 
	 * @param request
	 *            objeto con los criterios de búsqueda.
	 * @return ConsultarUOResponse
	 *         lista de Unidades orgánicas que cumplen los criterios de búsqueda.
	 * @throws Dir3WSException 
	 */
	@WebMethod
	public ConsultarUOResponse consultarUnidadesOrganicas(@WebParam(
			name = "request") ConsultarUORequest request) throws Dir3WSException {
		try{
			LOG.debug("INIT consultarUnidadesOrganicas");
			ConsultarUOResponse result = new ConsultarUOResponse();
			Criterios<CriterioUnidadOrganicaEnum> criterios =
					Converter.CriteriosUOWStoCriteriosAPIUO(request.getCriterios());
			List<DatosBasicosUnidadOrganica> unidades =
					getServicioConsultaDirectorioComun().findUnidadesOrganicas(
							criterios);
			List<DatosUnidadOrganica> UOWS;
			UOWS = Converter.UOAPItoUOWS(unidades);
			result.setUnidades(UOWS);
			LOG.debug("END consultarUnidadesOrganicas");
			return result;
		} catch (Exception e) {
			LOG.error(Dir3WSErrorConstants.FIND_UO_MESSAGE, e);
			throw new Dir3WSException(Dir3WSErrorConstants.FIND_UO_MESSAGE,e.getMessage(), e.getCause());
		}
	}
}
