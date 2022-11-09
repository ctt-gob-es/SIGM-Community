package ieci.tecdoc.sgm.ws.csv.connector.server;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.extension.StringClobType;
import com.ieci.tecdoc.common.repository.helper.ISRepositoryDocumentHelper;
import com.ieci.tecdoc.common.repository.vo.ISRepositoryRetrieveDocumentVO;
import com.ieci.tecdoc.common.utils.BBDDUtils;
import com.ieci.tecdoc.isicres.repository.RepositoryFactory;

import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.utils.KeysRP;

/**
 * @author [Dipucr-Manu]
 * @version $Revision$
 *
 */
public class AplicacionExternaCSVConnectorWS {
	
	/**
	 * Logger de la clase.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AplicacionExternaCSVConnectorWS.class);
	
	protected static String FDRID_FIELD = "fdrid";

	public boolean existeDocumento(java.lang.String csv, String entidad) throws java.rmi.RemoteException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("existeDocumento(java.lang.String) - start. CSV: [{}]",csv);
		}

		boolean existe = false;

		try {
			String[] doc = recuperaInfoDoc(entidad, csv);
			existe = StringUtils.isNotEmpty(doc);
		} catch (Exception e) {
			LOGGER.error("existeDocumento(java.lang.String, String)", e);
		}
		
		return existe;
	}
	
	/**
	 * De momento no se implementa la opción de documento original firmado para el registro presencial
	 */
	public boolean existeDocumentoOriginal(java.lang.String csv, String entidad) throws java.rmi.RemoteException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("existeDocumentoOriginal(java.lang.String) - start. CSV: [{}]",csv);
		}

		return false;
	}
		
	public byte[] getContenidoDocumento(java.lang.String csv, String entidad) throws java.rmi.RemoteException {

		byte[] content = null;
		try {
			String[] doc = recuperaInfoDoc(entidad, csv);
			String idModifReg = doc[0];
			String texto = doc[1];
						
			String[] infoReg = recuperaInfoRegistro(entidad, idModifReg); 
			String numeroRegistro = infoReg[0];
			String id_libro = infoReg[1];
						
			Integer fdrid = getFdrdid(Integer.parseInt(id_libro), entidad, numeroRegistro);
		
			Axpageh page = new Axpageh();
			JSONParser parser = new JSONParser();		
			JSONArray arrayContent = (JSONArray)parser.parse(texto);
			page.setDocId(Integer.valueOf(String.valueOf((Long)((JSONObject)arrayContent.get(0)).get("iddoc"))));
			page.setId(Integer.valueOf(String.valueOf((Long)((JSONObject)arrayContent.get(0)).get("idpag"))));
			page.setFdrid(fdrid);
			page.setLoc("pdf");
			page.setName(KeysRP.IR_REPORT_CERTIFICATE_NAME + fdrid + ".pdf");
			
			ISRepositoryRetrieveDocumentVO findVO = ISRepositoryDocumentHelper.getRepositoryRetrieveDocumentVO(Integer.parseInt(id_libro), fdrid, page.getId(), entidad, true);
			ISRepositoryRetrieveDocumentVO retrieveVO = RepositoryFactory.getCurrentPolicy().retrieveDocument(findVO);

			if (retrieveVO != null) {
				content = retrieveVO.getFileContent();
			}
		} catch (SigemException e) {
			LOGGER.error("getContenidoDocumento(java.lang.String, String)", e);
		} catch (Exception e) {
			LOGGER.error("getContenidoDocumento(java.lang.String, String)", e);
		} 
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getContenidoDocumento(java.lang.String, String) - end");
		}
		return content;
	}
	
	/**
	 * De momento no se implementa la opción de documento original firmado para el registro presencial
	 */
	public byte[] getContenidoDocumentoOriginal(java.lang.String csv, String entidad) throws java.rmi.RemoteException {

		return null;
	}
	
	private String[] recuperaInfoDoc(String entidad, String csv) throws SQLException {
		String[] resultado = new String[2];
		String sentencia = "";
		
		Connection con = null;		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = BBDDUtils.getConnection(entidad);
			
			sentencia = "SELECT ID, VALUE FROM SCR_VALSTR WHERE VALUE like '%,\"csv\":\"" + csv + "\"}]'";
			ps = con.prepareStatement(sentencia);
			rs = ps.executeQuery();
			
			if (!rs.next()) {
				throw new Exception();
			}
			int id = rs.getInt(1);
			StringClobType stringClobType = new StringClobType();
			String[] names = { "VALUE" };
			Object o = stringClobType.nullSafeGet(rs, names, null);
			resultado[0] = "" + id;
			resultado[1] = (String) o;			
		} catch (SQLException e) {
			LOGGER.error("recuperaInfoDoc. Error al ejecutar la consulta: " + sentencia, e);
		} catch (Throwable e) {
			LOGGER.error("recuperaInfoDoc. Error al ejecutar la consulta: " + sentencia, e);
		} finally {
			BBDDUtils.close(rs);
			BBDDUtils.close(ps);
			BBDDUtils.close(con);
		}
		return resultado;
	}
	
	private String[] recuperaInfoRegistro(String entidad, String id) throws SQLException {
		String[] resultado = new String[2];
		
		String sentencia = "";
		
		Connection con = null;		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = BBDDUtils.getConnection(entidad);
			
			sentencia = "SELECT NUM_REG, ID_ARCH FROM SCR_MODIFREG WHERE ID = " + id;
			ps = con.prepareStatement(sentencia);
			rs = ps.executeQuery();
			
			if (!rs.next()) {
				throw new Exception();
			}
			String num_reg = rs.getString(1);
			int id_libro = rs.getInt(2);
			
			resultado[0] = num_reg;
			resultado[1] = "" + id_libro; //"a" + id_libro + "sf"			
		} catch (SQLException e) {
			LOGGER.error("recuperaInfoRegistro. Error al ejecutar la consulta: " + sentencia, e);
		} catch (Throwable e) {
			LOGGER.error("recuperaInfoRegistro. Error al ejecutar la consulta: " + sentencia, e);
		} finally {
			BBDDUtils.close(rs);
			BBDDUtils.close(ps);
			BBDDUtils.close(con);
		}
		return resultado;
	}
	
	/**
	 * Obtiene el fdrid a partir del número de registro
	 * @param useCaseConf
	 * @param idBook
	 * @param numeroRegistro
	 * @return
	 * @throws BookException
	 * @throws SessionException
	 * @throws ValidationException
	 */
	private static Integer getFdrdid(int id_libro, String entidad, String numeroRegistro) throws BookException, SessionException, ValidationException {
		Integer fdrid = null;
		
		String sentencia = "";
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = BBDDUtils.getConnection(entidad);
		
			sentencia = "SELECT FDRID FROM A" + id_libro + "SF WHERE FLD1 = '" + numeroRegistro + "'";
			ps = con.prepareStatement(sentencia, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				fdrid = new Integer(rs.getInt(FDRID_FIELD));
			}
		} catch (NamingException e) {
			LOGGER.error("getFdrdid. Error al ejecutar la consulta: " + sentencia, e);
		} catch (SQLException e) {
			LOGGER.error("getFdrdid. Error al ejecutar la consulta: " + sentencia, e);
		}
		finally {
			BBDDUtils.close(rs);
			BBDDUtils.close(ps);
			BBDDUtils.close(con);
		}

		return fdrid;
	}
}
