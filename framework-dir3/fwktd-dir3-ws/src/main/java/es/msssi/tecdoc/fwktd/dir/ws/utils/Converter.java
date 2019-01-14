package es.msssi.tecdoc.fwktd.dir.ws.utils;

import java.util.ArrayList;
import java.util.List;
import es.ieci.tecdoc.fwktd.dir3.core.type.CriterioUnidadOrganicaEnum;
import es.ieci.tecdoc.fwktd.dir3.core.type.OperadorCriterioEnum;
import es.ieci.tecdoc.fwktd.dir3.core.vo.Criterio;
import es.ieci.tecdoc.fwktd.dir3.core.vo.Criterios;
import es.ieci.tecdoc.fwktd.dir3.core.vo.DatosBasicosUnidadOrganica;
import es.ieci.tecdoc.fwktd.server.pagination.PageInfo;
import es.msssi.tecdoc.fwktd.dir.ws.object.CriterioUO;
import es.msssi.tecdoc.fwktd.dir.ws.object.CriterioUOEnum;
import es.msssi.tecdoc.fwktd.dir.ws.object.CriteriosUO;
import es.msssi.tecdoc.fwktd.dir.ws.object.DatosUnidadOrganica;

/**
 * Clase que implementa métodos de conversión de los bean que recibimos a los bean de
 * sigem.
 * 
 * @author cmorenog
 * 
 */
public class Converter {
	
	/**
	 * Convierte una lista de DatosBasicosUnidadOrganica del API en objetos de los WS.
	 * 
	 * @param listUOAPI
	 *            lista de objetos API.
	 * @return List<DatosUnidadOrganica>
	 *         lista de Unidades orgánicas de WS.
	 */
	public static List<DatosUnidadOrganica> UOAPItoUOWS(
			List<DatosBasicosUnidadOrganica> listUOAPI) {
		List<DatosUnidadOrganica> listUOWS = null;
		DatosUnidadOrganica datosWS;
		if (listUOAPI != null && !listUOAPI.isEmpty()) {
			listUOWS = new ArrayList<DatosUnidadOrganica>();
		}
		for (DatosBasicosUnidadOrganica datosAPI : listUOAPI) {
			datosWS = new DatosUnidadOrganica();
			datosWS.setDescripcionEstado(datosAPI.getDescripcionEstado());
			datosWS.setDescripcionNivelAdministracion(datosAPI
					.getDescripcionNivelAdministracion());
			datosWS.setEstado(datosAPI.getEstado());
			datosWS.setFechaAltaOficial(datosAPI.getFechaAltaOficial());
			datosWS.setFechaAnulacion(datosAPI.getFechaAnulacion());
			datosWS.setFechaBajaOficial(datosAPI.getFechaBajaOficial());
			datosWS.setFechaExtincion(datosAPI.getFechaExtincion());
			datosWS.setId(datosAPI.getId());
			datosWS.setIdExternoFuente(datosAPI.getIdExternoFuente());
			datosWS.setIdUnidadOrganicaEntidadDerechoPublico(datosAPI
					.getIdUnidadOrganicaEntidadDerechoPublico());
			datosWS.setIdUnidadOrganicaPrincipal(datosAPI
					.getIdUnidadOrganicaPrincipal());
			datosWS.setIdUnidadOrganicaSuperior(datosAPI
					.getIdUnidadOrganicaSuperior());
			datosWS.setIndicadorEntidadDerechoPublico(datosAPI
					.getIndicadorEntidadDerechoPublico());
			datosWS.setNivelAdministracion(datosAPI.getNivelAdministracion());
			datosWS.setNivelJerarquico(datosAPI.getNivelJerarquico());
			datosWS.setNombre(datosAPI.getNombre());
			datosWS.setNombreUnidadOrganicaEntidadDerechoPublico(datosAPI
					.getNombreUnidadOrganicaEntidadDerechoPublico());
			datosWS.setNombreUnidadOrganicaPrincipal(datosAPI
					.getNombreUnidadOrganicaPrincipal());
			datosWS.setNombreUnidadOrganicaSuperior(datosAPI
					.getNombreUnidadOrganicaSuperior());
			listUOWS.add(datosWS);
		}
		return listUOWS;
	}
	
	/**
	 * Convierte los criterios del WS en criterios del API.
	 * 
	 * @param criteriosWS
	 *            criterios provenientes del WS.
	 * @return Criterios<CriterioUnidadOrganicaEnum>
	 *         criterios del API.
	 */
	public static Criterios<CriterioUnidadOrganicaEnum>
			CriteriosUOWStoCriteriosAPIUO(CriteriosUO criteriosWS) {
		Criterios<CriterioUnidadOrganicaEnum> criteriosAPI = null;
		if (criteriosWS != null) {
			criteriosAPI = new Criterios<CriterioUnidadOrganicaEnum>();
			
			if (criteriosWS.getPageInfo() != null) {
				PageInfo pageInfo = new PageInfo();
				pageInfo.setMaxNumItems(criteriosWS.getPageInfo()
						.getMaxNumItems());
				pageInfo.setObjectsPerPage(criteriosWS.getPageInfo()
						.getObjectsPerPage());
				pageInfo.setPageNumber(criteriosWS.getPageInfo()
						.getPageNumber());
				criteriosAPI.setPageInfo(pageInfo);
			}
			if (criteriosWS.getOrderBy() != null) {
				List<CriterioUnidadOrganicaEnum> orderBy =
						new ArrayList<CriterioUnidadOrganicaEnum>();
				for (CriterioUOEnum enumE : criteriosWS.getOrderBy()) {
					orderBy.add(CriterioUnidadOrganicaEnum.getCriterio(enumE
							.getValue()));
				}
				criteriosAPI.setOrderBy(orderBy);
			}
			if (criteriosWS.getCriterio() != null) {
				List<Criterio<CriterioUnidadOrganicaEnum>> criterios =
						new ArrayList<Criterio<CriterioUnidadOrganicaEnum>>();
				for (CriterioUO criterioWS : criteriosWS.getCriterio()) {
					Criterio<CriterioUnidadOrganicaEnum> criterioAPI =
							new Criterio<CriterioUnidadOrganicaEnum>(
									CriterioUnidadOrganicaEnum.getCriterio(criterioWS
											.getNombre().getValue()),
									OperadorCriterioEnum
											.getOperadorCriterio(criterioWS
													.getOperador().getValue()),
									criterioWS.getValor());
					criterios.add(criterioAPI);
				}
				criteriosAPI.setCriterios(criterios);
			}
		}
		return criteriosAPI;
	}
}
