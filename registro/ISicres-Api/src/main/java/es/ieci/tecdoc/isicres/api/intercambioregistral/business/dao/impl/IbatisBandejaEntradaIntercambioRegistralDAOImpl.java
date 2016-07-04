package es.ieci.tecdoc.isicres.api.intercambioregistral.business.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;

import es.ieci.tecdoc.fwktd.server.pagination.PageInfo;
import es.ieci.tecdoc.fwktd.server.pagination.PaginatedArrayList;
import es.ieci.tecdoc.fwktd.sir.core.vo.AsientoRegistralVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.CriteriosVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.dao.BandejaEntradaIntercambioRegistralDAO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaEntradaItemVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.IntercambioRegistralEntradaVO;

public class IbatisBandejaEntradaIntercambioRegistralDAOImpl implements
		BandejaEntradaIntercambioRegistralDAO {

	private SqlMapClientTemplate sqlMapClientTemplate = new SqlMapClientTemplate();
	private static final Logger logger = Logger.getLogger(IbatisBandejaEntradaIntercambioRegistralDAOImpl.class);
	private static final String SAVE_INTERCAMBIO_REGISTRAL_ENTRADA = "IntercambioRegistralEntradaVO.addIntercambioRegistralEntradaVO";
	private static final String GET_BANDEJA_ENTRADA_BY_ESTADO = "IntercambioRegistralEntradaVO.getBandejaEntradaByEstado";
	private static final String GET_BANDEJA_ENTRADA_ITEM_REGISTRO = "IntercambioRegistralEntradaVO.getBandejaEntradaItemRegistro";
	private static final String GET_INFO_ESTADO_BY_REGISTRO = "IntercambioRegistralEntradaVO.getBandejaEntradaByRegistro";
	private static final String COUNT_BANDEJA_ENTRADA_BY_ESTADO = "IntercambioRegistralEntradaVO.getCountBandejaEntradaByEstado";



	public void save(IntercambioRegistralEntradaVO intercambioRegistralEntrada) {
		try{

			getSqlMapClientTemplate().insert(SAVE_INTERCAMBIO_REGISTRAL_ENTRADA,intercambioRegistralEntrada);
		}
		catch (DataAccessException e) {
			logger.error("Error en la insercción de un intercambio registral de entrada", e);

			throw new RuntimeException(e);
		}
	}


	public List<IntercambioRegistralEntradaVO> getInfoEstado(
			IntercambioRegistralEntradaVO intecambioRegistralEntrada) {
		try{
			List<IntercambioRegistralEntradaVO> result = null;

			result = (List<IntercambioRegistralEntradaVO>) getSqlMapClientTemplate()
					.queryForList(GET_INFO_ESTADO_BY_REGISTRO,
							intecambioRegistralEntrada);

			return result;
		}catch (DataAccessException e) {
			logger.error("Error al obtener la información del estado del Intercambio Registral por registro", e);

			throw new RuntimeException(e);
		}
	}


	public List<BandejaEntradaItemVO> getBandejaEntradaByEstado(Integer estado, Integer idOficina) {
		try{
			HashMap<String, Integer> params = new HashMap<String, Integer>();
			params.put("estado", estado);
			params.put("idOfic", idOficina);

			List<BandejaEntradaItemVO> bandejaEntrada = (List<BandejaEntradaItemVO>)getSqlMapClientTemplate().queryForList(GET_BANDEJA_ENTRADA_BY_ESTADO,params);
			
			return bandejaEntrada;
		}
		catch (DataAccessException e) {
			logger.error("Error en la busqueda de bandeja de entrada por estado y oficina", e);

			throw new RuntimeException(e);
		}
	}

	public List<BandejaEntradaItemVO> getBandejaEntradaByEstado(Integer estado, Integer idOficina, CriteriosVO criterios) {
		try{
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("estado", estado);
			params.put("idOfic", idOficina);
			if (criterios.getOrderByString() != null){
			    params.put("ordenString", criterios.getOrderByString());
			}
			// Comprobar si hay que paginar los resultados
			PageInfo pageInfo = criterios.getPageInfo();
			if (pageInfo != null) {
			    // Número de resultados a ignorar
			    int skipResults = SqlExecutor.NO_SKIPPED_RESULTS;

			    // Número máximo de resultados.
			    int maxResults = SqlExecutor.NO_MAXIMUM_RESULTS;

			    if ((pageInfo.getPageNumber() > 0) && (pageInfo.getObjectsPerPage() > 0)) {
				skipResults = (pageInfo.getPageNumber() - 1) * pageInfo.getObjectsPerPage();
				maxResults = pageInfo.getObjectsPerPage();
			    } else if (pageInfo.getMaxNumItems() > 0) {
				maxResults = pageInfo.getMaxNumItems();
			    }

				// Obtener los resultados a mostrar en la página
				List<BandejaEntradaItemVO> bandejaEntrada = (List<BandejaEntradaItemVO>) getSqlMapClientTemplate().queryForList(GET_BANDEJA_ENTRADA_BY_ESTADO, params,
						skipResults, maxResults);

				return bandejaEntrada;
			}
			else {
				return  (List<BandejaEntradaItemVO>) getSqlMapClientTemplate().queryForList(GET_BANDEJA_ENTRADA_BY_ESTADO, params);
			}

		}
		catch (DataAccessException e) {
			logger.error("Error en la busqueda de bandeja de entrada por estado y oficina", e);

			throw new RuntimeException(e);
		}
	}
	/**
	 * Obtiene el número de registros de la bandeja de entrada para el siguiente <code>estado</code>
	 * @param estado
	 * @param CriteriosVO criterios
	 * @return
	 */
	public int getCountBandejaEntradaByEstado(int aceptadoValue, Integer idOficina,
		CriteriosVO criterios) {
	    try{
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("estado", aceptadoValue);
		params.put("idOfic", idOficina);
		int result = 0;
		result = (Integer)getSqlMapClientTemplate().queryForObject(COUNT_BANDEJA_ENTRADA_BY_ESTADO, params);
		return result;
	    }
		catch (DataAccessException e) {
			logger.error("Error en la busqueda de bandeja de entrada por estado y oficina", e);

			throw new RuntimeException(e);
		}
	}
	
	public BandejaEntradaItemVO completarBandejaEntradaItem(BandejaEntradaItemVO bandejaEntradaItemVO) {
		try{

			BandejaEntradaItemVO bandejaEntrada = (BandejaEntradaItemVO)getSqlMapClientTemplate().queryForObject(GET_BANDEJA_ENTRADA_ITEM_REGISTRO,bandejaEntradaItemVO);
			bandejaEntradaItemVO.setNumeroRegistro(bandejaEntrada.getNumeroRegistro());
			bandejaEntradaItemVO.setOrigen(bandejaEntrada.getOrigen());
			bandejaEntradaItemVO.setFechaRegistro(bandejaEntrada.getFechaRegistro());
			bandejaEntradaItemVO.setOrigenName(bandejaEntrada.getOrigenName());
			return bandejaEntradaItemVO;
		}
		catch (DataAccessException e) {
			logger.error("Error en la completacion de un elemento de la bandeja de entrada", e);

			throw new RuntimeException(e);
		}
	}

	public final void setSqlMapClient(SqlMapClient aSqlMapClient) {
		this.sqlMapClientTemplate.setSqlMapClient(aSqlMapClient);
	}

	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	public void setSqlMapClientTemplate(SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}



}
