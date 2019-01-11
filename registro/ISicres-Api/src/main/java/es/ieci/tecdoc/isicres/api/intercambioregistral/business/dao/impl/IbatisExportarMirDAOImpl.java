package es.ieci.tecdoc.isicres.api.intercambioregistral.business.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Component;

import com.ibatis.sqlmap.client.SqlMapClient;

import es.ieci.tecdoc.isicres.api.intercambioregistral.business.dao.ExportacionMirDAO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaEntradaItemVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.InfoRegistroPageRepositoryVO;


public class IbatisExportarMirDAOImpl implements ExportacionMirDAO {


	protected SqlMapClientTemplate sqlMapClientTemplate = new SqlMapClientTemplate();
	
	private static final Logger logger = Logger.getLogger(IbatisExportarMirDAOImpl.class);
	private static final String GET_DOCUMENTOS_REGISTRO_BY_ID_FDRID = "ExportacionMirVO.getDocumentos";
	private static final String GET_ASIENTOS_MIR = "ExportacionMirVO.getAsientos";

	@SuppressWarnings("unchecked")
	@Override
	public List<BandejaEntradaItemVO> getRegistrosLibro(Integer idLibro) {
		try{
			List<BandejaEntradaItemVO> result = null;
			result = (List<BandejaEntradaItemVO>) getSqlMapClientTemplate().queryForList(GET_ASIENTOS_MIR,idLibro);
			return result;
		}catch (DataAccessException e) {
			logger.error("Error al obtener la información del libro MIR con codigo " + Integer.toString(idLibro) , e);
			throw new RuntimeException(e);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<InfoRegistroPageRepositoryVO> getDocumentos( Integer idRegistro,Integer idBook) {
		try{
			HashMap<String, Integer> params = new HashMap<String, Integer>();		
			params.put("idRegistro", idRegistro);
			params.put("idLibro", idBook);
			List<InfoRegistroPageRepositoryVO> documentos = (List<InfoRegistroPageRepositoryVO>)getSqlMapClientTemplate().queryForList(GET_DOCUMENTOS_REGISTRO_BY_ID_FDRID,params);
			return documentos;
		}
		catch (DataAccessException e) {
			logger.error("Error en la busqueda de documentos "+ idRegistro.toString(), e);

			throw new RuntimeException(e);
		}
	}
	


	public SqlMapClientTemplate getSqlMapClientTemplate() {
		return sqlMapClientTemplate;
	}

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	public final void setSqlMapClient(
			SqlMapClient aSqlMapClient) {
		this.sqlMapClientTemplate.setSqlMapClient(aSqlMapClient);
	}
	

}
