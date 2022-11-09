/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfFilesDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputReport;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputSign;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputReport;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;

public class CustodyDAO {


	private byte[] data;

	Logger log = Logger.getLogger(CustodyDAO.class);

	@SuppressWarnings("unused")
	private EntityManager entityManager;
	private BaseDAO baseDAO;

	public CustodyDAO(BaseDAO baseDAO) {
		this.entityManager = baseDAO.getEntityManager();
		this.baseDAO = baseDAO;
	}

	public BigDecimal fileSize(String idDocument)
			throws CustodyServiceException {
		long number = 0;
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = baseDAO.getJdbcTemplate().getDataSource().getConnection();

			// Use PreparedStatement to avoid sql injection
			String sqlQuery = "SELECT LENGTH(B_ARCHIVO) FROM PF_ARCHIVOS "
					+ "JOIN PF_DOCUMENTOS ON PF_DOCUMENTOS.ARC_X_ARCHIVO = PF_ARCHIVOS.X_ARCHIVO "
					+ "WHERE PF_DOCUMENTOS.C_HASH = ?";
			pst = con.prepareStatement(sqlQuery);
			pst.setString(1, idDocument);
			rs = pst.executeQuery();
			if (rs != null && rs.next()) {
				number = rs.getLong("LENGTH(B_ARCHIVO)");
			}
		} catch (Exception e) {
			throw new CustodyServiceException(e.getMessage(), e);
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			if (con != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
		}

		return new BigDecimal(number);
	}

	public byte[] downloadFile(String idDocument)
			throws CustodyServiceException {
		log.info("Begin downloadFile");
		byte[] bytes = null;
		
		PfDocumentsDTO pfDocumentsDTO = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			List<AbstractBaseDTO> listDocuments = baseDAO
					.queryListOneParameter("file.documentFile", "hash", idDocument);

			if (listDocuments != null && listDocuments.size() == 1) {
				pfDocumentsDTO = (PfDocumentsDTO) listDocuments.get(0);

				conn = baseDAO.getJdbcTemplate().getDataSource().getConnection();
				
				String sqlQuery = "SELECT B_ARCHIVO FROM PF_ARCHIVOS WHERE C_HASH = ?";
				pst = conn.prepareStatement(sqlQuery);
				pst.setString(1, pfDocumentsDTO.getPfFile().getChash());

				rs = pst.executeQuery();
				if (rs.next()) {
					InputStream inputStream = rs.getBinaryStream("B_ARCHIVO");
					bytes = fromInputStreamToByteArray(inputStream);
				} else {
					log.error("File not found");
					throw new CustodyServiceException("File not found");
				}
			} else {
				log.error("File not found");
				throw new CustodyServiceException("File not found");
			}
		} catch (Exception e) {
			throw new CustodyServiceException(e.getMessage(), e);
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
		}
		log.info("End downloadFile");
		return bytes;
	}

	public byte[] downloadDocelFile(String idFileHash) throws CustodyServiceException {
		log.info("Begin downloadDocelFile");
		byte[] bytes = null;
		
		PfFilesDTO pfFilesDTO = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			List<AbstractBaseDTO> listFiles = baseDAO.queryListOneParameter("file.parameterFileHash", "hash", idFileHash);

			if (listFiles != null && listFiles.size() == 1) {
				pfFilesDTO = (PfFilesDTO) listFiles.get(0);

				con = baseDAO.getJdbcTemplate().getDataSource().getConnection();

				String sqlQuery = "SELECT B_ARCHIVO FROM PF_ARCHIVOS WHERE C_HASH = ?";
				pst = con.prepareStatement(sqlQuery);
				pst.setString(1, pfFilesDTO.getChash());

				rs = pst.executeQuery();
				if (rs.next()) {
					InputStream inputStream = rs.getBinaryStream("B_ARCHIVO");
					bytes = fromInputStreamToByteArray(inputStream);
				} else {
					log.error("File not found");
					throw new CustodyServiceException("File not found");
				}
			} else {
				log.error("File not found");
				throw new CustodyServiceException("File not found");
			}
		} catch (Exception e) {
			throw new CustodyServiceException(e.getMessage(), e);
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			
		}
		log.info("End downloadFile");
		return bytes;
	}
	
	/**
	 * @param fileToUpload
	 * @param document
	 * @return
	 * @throws SQLException 
	 */
	public String uploadFile(final InputStream inputStream, final CustodyServiceInputDocument document)	throws CustodyServiceException {
		log.info("Begin uploadFile");

		try {
			final List<AbstractBaseDTO> listFiles = baseDAO.queryListOneParameter(
					"file.parameterFileHash", "hash", document.getCheckHash());
			if (listFiles.size() == 1) {

				// Se ejecuta el update del fichero dentro de la conexión actual
				baseDAO.getJdbcTemplate().execute(
					new ConnectionCallback<Object>() {

						@Override
						public Object doInConnection(Connection con) throws SQLException, DataAccessException {
							// update bytes
							PreparedStatement pst = con
									.prepareStatement("UPDATE PF_ARCHIVOS SET B_ARCHIVO=? WHERE C_HASH = ?");
							pst.setBinaryStream(1, inputStream, document.getSize()
									.intValue());
							pst.setString(2, document.getCheckHash());
							int result = pst.executeUpdate();
							log.info("File uploaded: " + result);
							pst.close();

							baseDAO.getEntityManager().refresh(listFiles.get(0));
							
							listFiles.get(0).setFmodified(Calendar.getInstance().getTime());
							baseDAO.update(listFiles.get(0));
							return null;
						}
					}
				);

			} else {
				log.error("File " + document.getCheckHash() + " not exist");
				throw new CustodyServiceException("File "
						+ document.getCheckHash() + " not exist");
			}

		} catch (Exception e) {
			throw new CustodyServiceException(e.getMessage(), e);
		}

		log.info("End uploadFile");
		return null;
	}

	/**
	 * Método que guarda una firma en base de datos
	 * @param sign
	 * @param input
	 * @return
	 * @throws CustodyServiceException
	 */
	public String uploadSign(final CustodyServiceInputSign sign, final InputStream input)
			throws CustodyServiceException {

		log.info("Begin uploadSign");
		try {
			if (input != null) {
				String namedParameter = "primaryKey";
				
				final AbstractBaseDTO signDTO = baseDAO.queryElementOneParameter(
						"sign.sign", namedParameter, new Long(sign.getIdentifier()));

				// Se ejecuta el update del fichero dentro de la conexión actual
				baseDAO.getJdbcTemplate().execute(
					new ConnectionCallback<Object>() {
						@Override
						public Object doInConnection(Connection con) throws SQLException, DataAccessException {
							// update bytes
							PreparedStatement st = con.prepareStatement("UPDATE PF_FIRMAS SET B_FIRMA=? WHERE X_FIRMA = ? ");
							st.setBinaryStream(1, input, sign.getSize().intValue());
							st.setLong(2, signDTO.getPrimaryKey());
			
							int result = st.executeUpdate();
							st.close();
							log.info("Sign uploaded: " + result);
							return null;
						}
					}
				);

				signDTO.setFmodified(Calendar.getInstance().getTime());
				baseDAO.update(signDTO);
			}

		} catch (Exception e) {
			throw new CustodyServiceException(e.getMessage(), e);
		}
		log.info("End uploadSign");
		return "";
	}
	public byte[] downloadSign(CustodyServiceOutputSign sign) throws CustodyServiceException {
		PreparedStatement st = null;
		Connection con = null;
		ResultSet rs = null;
		byte[] bytes = null;
		
		try {
			String querySql = null;
			String namedQuery = null;
			String namedParameter = "primaryKey";
			String blobName = null;
			if (Constants.SIGN_TYPE_BLOCK.equals(sign.getType())) {
				querySql = "SELECT B_BLOQUE FROM PF_BLOQUES WHERE X_BLOQUE = ?";
				namedQuery = "sign.block";
				blobName = "B_BLOQUE";
			} else {
				querySql = "SELECT B_FIRMA FROM PF_FIRMAS WHERE X_FIRMA = ?";
				namedQuery = "sign.sign";
				blobName = "B_FIRMA";
			}

			AbstractBaseDTO signDTO = baseDAO.queryElementOneParameter(
					namedQuery, namedParameter, new Long(sign.getIdentifier()));

			con = baseDAO.getJdbcTemplate().getDataSource().getConnection();

			st = con.prepareStatement(querySql);
			st.setLong(1, signDTO.getPrimaryKey());
			
			rs = st.executeQuery();

			if (rs.next()) {
				InputStream inputStream = rs.getBinaryStream(blobName);
				bytes = fromInputStreamToByteArray(inputStream);
			}
		} catch (Exception e) {
			throw new CustodyServiceException(e.getMessage(), e);
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
		}
		log.info("End downloadFile");
		return bytes;
	}

	public BigDecimal signSize(CustodyServiceOutputSign sign) throws CustodyServiceException {
		long number = 0;
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pst = null;

		String namedQuery = null;
		String namedParameter = "primaryKey";
		String querySql = null;
		String blobName = null;
		try {
			if (Constants.SIGN_TYPE_BLOCK.equals(sign.getType())) {
				querySql = "SELECT LENGTH(B_BLOQUE) FROM PF_BLOQUES WHERE X_BLOQUE = ?";
				namedQuery = "sign.block";
				blobName = "LENGTH(B_BLOQUE)";
			} else {
				querySql = "SELECT LENGTH(B_FIRMA) FROM PF_FIRMAS WHERE X_FIRMA = ?";
				namedQuery = "sign.sign";
				blobName = "LENGTH(B_FIRMA)";
			}

			AbstractBaseDTO signDTO = baseDAO.queryElementOneParameter(
					namedQuery, namedParameter, new Long(sign.getIdentifier()));

			con = baseDAO.getJdbcTemplate().getDataSource().getConnection();

			// Use PreparedStatement to avoid sql injection
			pst = con.prepareStatement(querySql);
			pst.setLong(1, signDTO.getPrimaryKey());

			rs = pst.executeQuery();
			if (rs != null && rs.next()) {
				number = rs.getLong(blobName);
			}
		} catch (Exception e) {
			throw new CustodyServiceException(e.getMessage(), e);
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
		}

		return new BigDecimal(number);
	}

	public String uploadReport(final CustodyServiceInputReport report, final InputStream input)
	throws CustodyServiceException {
		
		try {
			
			// Se ejecuta el update del fichero dentro de la conexión actual
			baseDAO.getJdbcTemplate().execute(
				new ConnectionCallback<Object>() {

					@Override
					public Object doInConnection(Connection con) throws SQLException, DataAccessException {
						// update bytes
						PreparedStatement statement = con.prepareStatement("UPDATE PF_FIRMAS SET B_INFORME = ?, F_MODIFICADO = ? WHERE X_FIRMA = ?");
						
						statement.setBinaryStream(1, input, report.getSize().intValue());
						statement.setDate(2, new Date(System.currentTimeMillis()));
						statement.setString(3, report.getIdentifier());
						
						int resultado = statement.executeUpdate();
						log.debug("Se actualiza el informe de la firma " + report.getIdentifier() + " con el resultado " + resultado);
						
						statement.close();						
						return null;
					}
				}
			);
			return null;
		} catch (Exception e) {			
			throw new CustodyServiceException("Error guardando el informe", e);
		}		
	}
	
	public String uploadNormalizedReport(final CustodyServiceInputReport report, final InputStream input)
	throws CustodyServiceException {
		
		try {
			
			// Se ejecuta el update del fichero dentro de la conexión actual
			baseDAO.getJdbcTemplate().execute(
				new ConnectionCallback<Object>() {

					@Override
					public Object doInConnection(Connection con) throws SQLException, DataAccessException {
						// update bytes
						PreparedStatement statement = con.prepareStatement("UPDATE PF_FIRMAS SET B_NORMALIZADO = ?, F_MODIFICADO = ? WHERE X_FIRMA = ?");
						
						statement.setBinaryStream(1, input, report.getSize().intValue());
						statement.setDate(2, new Date(System.currentTimeMillis()));
						statement.setString(3, report.getIdentifier());
						
						int resultado = statement.executeUpdate();
						log.debug("Se actualiza el informe normalizado de la firma " + report.getIdentifier() + " con el resultado " + resultado);
						
						statement.close();						
						return null;
					}
				}
			);
			return null;
		} catch (Exception e) {			
			throw new CustodyServiceException("Error guardando el informe normalizado", e);
		}		
	}
	
	/**
	 * Método que guarda en base de datos el informe generado de una firma
	 * @param signId Identificador de firma
	 * @param report Informe generado
	 * @param reportSize Tamaño del informe
	 * @param commit Indica si el método que realiza la llamada necesita que se haga commit al no haber una transacción activa (métodos no marcados con "@Transactional")
	 */
	public void uploadSignReport(final PfSignsDTO sign, final InputStream report, final Integer reportSize, final boolean commit) throws CustodyServiceException {
		log.info("Init uploadSignReport");
		Connection con = null;
		try {
			con = baseDAO.getJdbcTemplate().getDataSource().getConnection();

			// Se crea la query para subir el informe  
			PreparedStatement statement = con.prepareStatement("UPDATE PF_FIRMAS SET B_INFORME = ?, F_MODIFICADO = ? WHERE X_FIRMA = ?");

			// Se indican los parámetros
			statement.setBinaryStream(1, report, reportSize);
			statement.setDate(2, new Date(System.currentTimeMillis()));
			statement.setString(3, sign.getPrimaryKeyString());

			int resultado = statement.executeUpdate();
			log.info("Se actualiza el informe de la firma " + sign.getPrimaryKeyString() + " con el resultado " + resultado);
			
			statement.close();

			if (commit) {
				con.commit();
			}
			
		} catch (Throwable t) {
			throw new CustodyServiceException(t.getMessage(), t);
		}finally{
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
		}

		log.info("End uploadSignReport");
	}

	/**
	 * Método que obtiene el informe de firma a partir de la firma
	 * @param sign Firma
	 * @return Informe de firma
	 * @throws CustodyServiceException
	 */
	public byte[] downloadSignReport(PfSignsDTO sign) throws CustodyServiceException {
		log.info("Init downloadSignReport");
		byte[] report = null;

		try {
			Connection con = baseDAO.getJdbcTemplate().getDataSource().getConnection();

			// Se crea la query para subir el informe  
			PreparedStatement statement = con.prepareStatement("SELECT f.B_INFORME FROM PF_FIRMAS f WHERE f.X_FIRMA = ?");

			// Se indican los parámetros
			statement.setString(1, sign.getPrimaryKeyString());

			log.info("Se descarga el informe de la firma " + sign.getPrimaryKeyString());
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				Blob blob = resultSet.getBlob(1);
				if (blob != null) {
					int size = (int) blob.length();
					report = blob.getBytes(1, size);
				}
				resultSet.close();
			}
			statement.close();
			con.close();

		} catch (Throwable t) {
			throw new CustodyServiceException(t.getMessage(), t);
		}

		log.info("End downloadSignReport");
		return report;
	}

	/**
	 * Método que obtiene el informe de firma a partir de su código seguro de verificación
	 * @param cve CVE
	 * @return Informe de firma
	 * @throws CustodyServiceException
	 */
	public byte[] downloadSignReport(final String csv) throws CustodyServiceException {
		try {
			// Se ejecuta el update del fichero dentro de la conexión actual
			baseDAO.getJdbcTemplate().execute(
				new ConnectionCallback<Object>() {
					@Override
					public Object doInConnection(Connection con) throws SQLException, DataAccessException {
						// Se crea la query para subir el informe  
						PreparedStatement statement = con.prepareStatement("SELECT f.B_INFORME FROM PF_FIRMAS f WHERE f.CSV = ?");

						// Se indican los parámetros
						statement.setString(1, csv);

						log.info("Se descarga el informe de la firma con CVE = " + csv);
						ResultSet resultSet = statement.executeQuery();

						if (resultSet.next()) {
							Blob blob = resultSet.getBlob(1);
							if (blob != null) {
								int size = (int) blob.length();
								data = blob.getBytes(1, size);
							}
							resultSet.close();
						}
						statement.close();
						//con.close();
						return null;
					}
				}
			);
		} catch (Throwable t) {
			throw new CustodyServiceException(t.getMessage(), t);
		}
		return data;
	}
	
	/**
	 * Método que obtiene el informe de firma en el parámetro outputStream 
	 * @return true, si se puede descargar el informe, false si no existe en BBDD.
	 * @throws CustodyServiceException
	 */
	public byte[] downloadReport(CustodyServiceOutputReport reportOutput) throws CustodyServiceException {
		log.info("Init downloadSignReport");
		log.debug ("descargando informe de firma");
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		byte[] bytes = null;
		try {
			con = baseDAO.getJdbcTemplate().getDataSource().getConnection();

			String select = null;
			
			if (reportOutput.getIdentifier() != null) {
				log.debug ("recibido identificador de firma: " + reportOutput.getIdentifier());
				select = "SELECT f.B_INFORME FROM PF_FIRMAS f WHERE f.X_FIRMA = ? AND f.B_INFORME IS NOT NULL";
			} else if (reportOutput.getCsv() != null) {
				log.debug ("recibido csv de firma: " + reportOutput.getCsv());
				select = "SELECT f.B_INFORME FROM PF_FIRMAS f WHERE f.CSV = ? AND f.B_INFORME IS NOT NULL";
			}
			// Se crea la query para descargar el informe  
			statement = con.prepareStatement(select);

			if (reportOutput.getIdentifier() != null) {
				statement.setString(1, reportOutput.getIdentifier());
			} else if (reportOutput.getCsv() != null) {
				statement.setString(1, reportOutput.getCsv());
			}

			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				InputStream inputStream = resultSet.getBinaryStream("B_INFORME");
				bytes = fromInputStreamToByteArray(inputStream);
				resultSet.close();
			}


		} catch (Throwable t) {
			log.error("ERROR: CustodyDAO.downloadReport, ", t);
			throw new CustodyServiceException(t.getMessage(), t);
		} finally{
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error: ", e);
				}
			}
		}

		log.debug("End downloadReport");
		return bytes;
		
	}


	/**
	 * Método que comprueba si una firma tiene informe en base de datos
	 * @param signId Identificador de la firma
	 * @return True si tiene informe, false en caso contrario
	 */
	@SuppressWarnings("unchecked")
	public boolean signReportExists(String signId) {
		boolean existe = false;

		String query = "SELECT COUNT(1) FROM PF_FIRMAS f " +
					   "WHERE f.X_FIRMA = " + signId + " " +
					   "AND f.B_INFORME IS NOT NULL";

		List<Object[]> result =  baseDAO.querySQLStandardResult(query);

		Object[] row = result.get(0);
		if ("1".equals((String) row[0])) {
			existe = true;
		}

		return existe;
	}
	
	@SuppressWarnings("unused")
	private void fromInputStreamToOutputStream (InputStream is, OutputStream os) throws IOException {
		int read;
		int total = 0;
		byte[ ] buffer = new byte[Constants.BUFFER_SIZE];
		while ((read = is.read(buffer)) > 0) {
			total += read;
			os.write(buffer, 0, read);
		}
	}
	
	private byte[] fromInputStreamToByteArray (InputStream is) throws CustodyServiceException {
		byte[] bytes = null;
		try {			
			bytes = IOUtils.toByteArray(is);
		} catch (Exception e) {
			log.error("ERROR: fromInputStreamToByteArray, ", e);
			throw new CustodyServiceException(e.getMessage());
		}		
		return bytes;
	}
}
