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

package es.seap.minhap.portafirmas.storage.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.RequestTagListDTO;
import es.seap.minhap.portafirmas.storage.domain.StoredRequest;
import es.seap.minhap.portafirmas.storage.util.StorageConstants;
import es.seap.minhap.portafirmas.storage.util.StorageDBConnectionManager;
import es.seap.minhap.portafirmas.web.beans.Paginator;

@Service
public class JdbcRequestStorageDAO implements RequestStorageDAO {

	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(JdbcRequestStorageDAO.class);

	@Autowired
	private StorageDBConnectionManager storageDBConnectionManager;

	/* (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.storage.dao.RequestStorageDAO#moveToStorage(java.util.List)
	 */
	public void moveToStorage(List<String> requests) throws Throwable {
		Connection connHIS = getConnection(StorageConstants.HISTORICO);
		Connection connPOR = getConnection(StorageConstants.PORTAFIRMA);
		moveToStorage(requests, connHIS, connPOR);
	}
	
	/**
	 * Método para la tarea quartz, que necesita pasar por parametro el objeto del que se
	 * obtiene la conexión o no será capaz de obtener dicha conexión
	 * @param requests
	 * @param storageDBConnectionManager
	 * @throws Throwable
	 */
	public void moveToStorage(List<String> requests, StorageDBConnectionManager storageDBConnectionManager) throws Throwable {
		moveToStorage(requests,
			storageDBConnectionManager.getConnection(StorageConstants.HISTORICO),
			storageDBConnectionManager.getConnection(StorageConstants.PORTAFIRMA));
	}
	
	/**
	 * @param requests
	 * @param connHIS
	 * @param connPOR
	 * @throws Throwable
	 */
	private void moveToStorage(List<String> requests, Connection connHIS, Connection connPOR) throws Throwable {

		log.debug("moveToStorage init");

		// Se guardan todos los registros que involucran a la petición
		try {
			
			moveList(connPOR, connHIS, requests);

			connHIS.commit();
			connPOR.commit();
			
		} catch (SQLException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Throwable t) {
			throw t;
		} finally {
			if(connHIS != null) {
				connHIS.rollback();
				connHIS.close();
			}
			if(connPOR != null) {
				connPOR.rollback();
				connPOR.close();
			}

			log.debug("moveToStorage end");
		}
	}
//
//	/**
//	 * Método que mueve una lista de peticiones entre bases de datos
//	 * @param connPOR Conexión origen
//	 * @param connHIS Conexión destino
//	 * @param requests Peticiones a mover
//	 * @throws SQLException
//	 * @throws IOException
//	 */
//	private void moveList_OLD(Connection connPOR, Connection connHIS, List<String> requests) throws SQLException, IOException {
//		int size = requests.size();
//		for (String request : requests) {
//			move(connPOR, connHIS, request);
//			log.debug("-----Movida peticion en posicion " + requests.indexOf(request) + " de " + size + "-----");
//		}
//	}

	public void moveList(Connection connPOR, Connection connHIS, List<String> requests) throws SQLException, IOException {
		int i = 0;
		int confirmadas = 0;
		int sinConfirmar = 0;
		int fallosTanda = 0;
		for (String request : requests) {
			try {
				move(connPOR, connHIS, request);
				if(i % 10 == 0 && i > 0) {
					connHIS.commit();
					connPOR.commit();
					fallosTanda = 0;
					//System.out.println("Procesada tanda 10. Subtotal: " + confirmadas);
				}
			} catch (Exception e) {
				connHIS.rollback();
				connPOR.rollback();
				confirmadas -= (i%10) + 1 - fallosTanda;
				fallosTanda =  (i%10) + 1 - fallosTanda;
				sinConfirmar += fallosTanda;
				log.error("Se hizo rollback y se dejaron de pasar " + fallosTanda + " peticiones.", e);
			}
			i++;
			confirmadas++;
		}
		log.info("Confirmadas: " + confirmadas);
		log.info("Sin confirmar: " + sinConfirmar);
		connHIS.commit();
		connPOR.commit();
	}
	
	/**
	 * Método que mueve una petición entre bases de datos
	 * @param connPOR Conexión origen
	 * @param connHIS Conexión destino
	 * @param request Petición a mover
	 * @throws Throwable 
	 */
	private void move(Connection connPOR, Connection connHIS, String request) throws SQLException, IOException {

		//log.debug("------------------------------------");
		log.debug("Moving request " + request + " starts");
		//log.debug("------------------------------------");

		PreparedStatement stDocuments = null;
		PreparedStatement stSignLines = null;
		PreparedStatement stSigners = null;
		PreparedStatement stSigns = null;
		PreparedStatement stComments = null;
		PreparedStatement stActions = null;
		PreparedStatement stRequestValues = null;
		PreparedStatement stRequestNotice = null;
		PreparedStatement stRequestHistoric = null;
		PreparedStatement stRequestTag = null;
		PreparedStatement stSenders = null;
		PreparedStatement stEmails = null;
		PreparedStatement stDwRequest = null;
		PreparedStatement deleteSigns = null;
		PreparedStatement deleteSenders = null;
		PreparedStatement deleteRequestTag = null;
		PreparedStatement deleteRequestHistoric = null;
		PreparedStatement deleteRequestNotice = null;
		PreparedStatement deleteRequestValues = null;
		PreparedStatement deleteActions = null;
		PreparedStatement deleteComments = null;
		
		PreparedStatement deleteCommentsUser = null;
		
		PreparedStatement deleteSigners = null;
		PreparedStatement deleteSignLines = null;
		PreparedStatement deleteEmails = null;
		PreparedStatement deleteRequests = null;

		try {
			// Se mueven las peticiones
			moveRequest(connPOR, connHIS, request);
	
			// Se mueven los documentos y sus archivos
			stDocuments = connPOR.prepareStatement("SELECT * FROM pf_documentos WHERE pet_x_peticion = " + request);
			ResultSet rsDocuments = stDocuments.executeQuery();
			moveDocuments(connPOR, connHIS, rsDocuments);
			rsDocuments.close();
	
			// Se mueven las líneas de firma
			stSignLines = connPOR.prepareStatement("SELECT * FROM pf_lineas_firma WHERE pet_x_peticion = " + request + " order by x_linea_firma");
			ResultSet rsSignLines = stSignLines.executeQuery();
			moveSignLines(connPOR, connHIS, rsSignLines);
			rsSignLines.close();
	
			// Se mueven los firmantes
			stSigners = connPOR.prepareStatement("SELECT fir.* FROM pf_firmantes fir," +
																   				 	 "pf_lineas_firma lfir," +
																   				 	 "pf_peticiones pet " +
																   "WHERE fir.lfir_x_linea_firma = lfir.x_linea_firma " +
																   "AND   lfir.pet_x_peticion = pet.x_peticion " +
																   "AND   pet_x_peticion = " + request + " order by fir.x_firmante");
			moveSigners(connPOR, connHIS, stSigners);
	
			// Se mueven las firmas
			stSigns = connPOR.prepareStatement("SELECT f.* FROM pf_firmas f," +
																				 "pf_firmantes fir," +
																  				 "pf_lineas_firma lfir," +
																  				 "pf_peticiones pet " +
																   "WHERE f.fir_x_firmante = fir.x_firmante " +
																   "AND   fir.lfir_x_linea_firma = lfir.x_linea_firma " +
																   "AND   lfir.pet_x_peticion = pet.x_peticion " +
																   "AND   pet_x_peticion = " + request);
			moveSigns(connPOR, connHIS, stSigns);
	
			// Se mueven los comentarios
			stComments = connPOR.prepareStatement("SELECT * FROM pf_comentarios WHERE pet_x_peticion = " + request);
			ResultSet rsComments = stComments.executeQuery();
			moveComments(connPOR, connHIS, rsComments);
			rsComments.close();
	
			// Se mueven las acciones
			stActions = connPOR.prepareStatement("SELECT * FROM pf_acciones WHERE pet_x_peticion = " + request);
			ResultSet rsActions = stActions.executeQuery();
			moveActions(connPOR, connHIS, rsActions);
			rsActions.close();
	
			// Se mueven las peticiones valor
			stRequestValues = connPOR.prepareStatement("SELECT * FROM pf_peticiones_valor WHERE pet_x_peticion = " + request);
			ResultSet rsRequestValues = stRequestValues.executeQuery();
			moveRequestValues(connPOR, connHIS, rsRequestValues);
			rsRequestValues.close();
	
			// Se mueven las peticiones aviso
			stRequestNotice = connPOR.prepareStatement("SELECT * FROM pf_peticiones_aviso WHERE pet_x_peticion = " + request);
			ResultSet rsRequestNotice = stRequestNotice.executeQuery();
			moveRequestNotices(connPOR, connHIS, rsRequestNotice);
			rsRequestNotice.close();
	
			// Se mueven las peticiones histórico
			stRequestHistoric = connPOR.prepareStatement("SELECT * FROM pf_peticiones_historico WHERE pet_x_peticion = " + request);
			ResultSet rsRequestHistoric = stRequestHistoric.executeQuery();
			moveRequestHistoric(connPOR, connHIS, rsRequestHistoric);
			rsRequestHistoric.close();
	
			// Se mueven las etiquetas petición
			stRequestTag = connPOR.prepareStatement("SELECT * FROM pf_etiquetas_peticion WHERE pet_x_peticion = " + request);
			ResultSet rsRequestTag = stRequestTag.executeQuery();
			moveRequestTags(connPOR, connHIS, rsRequestTag);
			rsRequestTag.close();
	
			// Se mueven los usuarios remitentes
			stSenders = connPOR.prepareStatement("SELECT * FROM pf_usuarios_remitente WHERE pet_x_peticion = " + request);
			ResultSet rsSenders = stSenders.executeQuery();
			moveSenders(connPOR, connHIS, rsSenders);
			rsSenders.close();
			
			// Se mueven los emails de notificación alternativos
			stEmails = connPOR.prepareStatement("SELECT * FROM pf_emails_peticion WHERE pet_x_peticion = " + request);
			ResultSet rsEmails = stEmails.executeQuery();
			moveEmails(connPOR, connHIS, rsEmails);
			rsEmails.close();
	
			// Se mueven las tablas de la interfaz genérica involucradas
			stDwRequest = connPOR.prepareStatement("SELECT * FROM pf_docelweb_solicitud_spfirma WHERE solicitud_x_peticion = " + request);
			ResultSet rsDwRequest = stDwRequest.executeQuery();
			moveGenericInterface(connPOR, connHIS, rsDwRequest);
						
	
			// Se borran las tablas de la interfaz genérica involucradas de Portafirmas
			rsDwRequest = stDwRequest.executeQuery();
			deleteGenericInterface(connPOR, rsDwRequest);			
			rsDwRequest.close();
	
			// Se borran las firmas de Portafirmas
			deleteSigns = connPOR.prepareStatement("SELECT f.x_firma " +
																	 "FROM pf_firmas f," +
																		  "pf_firmantes fir," +
																  		  "pf_lineas_firma lfir," +
																  		  "pf_peticiones pet " +
																  	 "WHERE f.fir_x_firmante = fir.x_firmante " +
																     "AND   fir.lfir_x_linea_firma = lfir.x_linea_firma " +
																     "AND   lfir.pet_x_peticion = pet.x_peticion " +
																     "AND   pet_x_peticion = " + request);
			ResultSet rsDelSigns = deleteSigns.executeQuery();			
			deleteSigns(connPOR, rsDelSigns);
			rsDelSigns.close();
	
			// Se borran los documentos y los archivos de peticiones de Portafirmas
			ResultSet rsDelDocuments = stDocuments.executeQuery();
			deleteDocumentsAndFiles(connPOR, rsDelDocuments);
			rsDelDocuments.close();
	
			// Se borran los usuarios remitentes de Portafirmas
			deleteSenders = connPOR.prepareStatement("DELETE FROM pf_usuarios_remitente WHERE pet_x_peticion = " + request);
			deleteSenders.executeUpdate();
	
			// Se borran las etiquetas petición de Portafirmas
			deleteRequestTag = connPOR.prepareStatement("DELETE FROM pf_etiquetas_peticion WHERE pet_x_peticion = " + request);
			deleteRequestTag.executeUpdate();
	
			// Se borran las peticiones histórico de Portafirmas
			deleteRequestHistoric = connPOR.prepareStatement("DELETE FROM pf_peticiones_historico WHERE pet_x_peticion IN " + request);
			deleteRequestHistoric.executeUpdate();
	
			// Se borran las peticiones aviso de Portafirmas
			deleteRequestNotice = connPOR.prepareStatement("DELETE FROM pf_peticiones_aviso WHERE pet_x_peticion IN " + request);
			deleteRequestNotice.executeUpdate();
	
			// Se borran las peticiones valor de Portafirmas
			deleteRequestValues = connPOR.prepareStatement("DELETE FROM pf_peticiones_valor WHERE pet_x_peticion IN " + request);
			deleteRequestValues.executeUpdate();
	
			// Se borran las acciones de Portafirmas
			deleteActions = connPOR.prepareStatement("DELETE FROM pf_acciones WHERE pet_x_peticion IN " + request);
			deleteActions.executeUpdate();
	
			deleteCommentsUser = connPOR.prepareStatement("DELETE FROM PF_USUARIOS_COMENTARIO WHERE COM_X_COMENTARIO in ( select X_COMENTARIO from pf_comentarios where pet_x_peticion = "+ request +" ) ");
			deleteCommentsUser.executeUpdate();
			
			// Se borran los comentarios de Portafirmas
			deleteComments = connPOR.prepareStatement("DELETE FROM pf_comentarios WHERE pet_x_peticion IN " + request);
			deleteComments.executeUpdate();
	
			// Se borran los firmantes de Portafirmas
			deleteSigners = connPOR.prepareStatement("SELECT fir.x_firmante " +
																	   "FROM pf_firmantes fir," +
																	  		"pf_lineas_firma lfir," +
																	  		"pf_peticiones pet " +
																	   "WHERE fir.lfir_x_linea_firma = lfir.x_linea_firma " +
																	   "AND   lfir.pet_x_peticion = pet.x_peticion " +
																	   "AND   pet_x_peticion IN " + request + " order by fir.x_firmante desc");
			ResultSet rsDelSigners = deleteSigners.executeQuery();
			deleteSigners(connPOR, rsDelSigners);
			rsDelSigners.close();
	
			// Se borran las líneas de firma de Portafirmas
			deleteSignLines = connPOR.prepareStatement("DELETE FROM pf_lineas_firma WHERE pet_x_peticion IN " + request);			
			deleteSignLines.executeUpdate();
			
			// Se borran los emails alternativos de notificación de Portafirmas
			deleteEmails = connPOR.prepareStatement("DELETE FROM pf_emails_peticion WHERE pet_x_peticion IN " + request);
			deleteEmails.executeUpdate();
			// Se borran las peticiones de Portafirmas
			deleteRequests = connPOR.prepareStatement("DELETE FROM pf_peticiones WHERE x_peticion IN " + request);
			deleteRequests.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (stDocuments != null) {
				stDocuments.close();
			}
			if (stSignLines != null) {
				stSignLines.close();
			}
			if (stSigners != null) {
				stSigners.close();
			}
			if (stSigns != null) {
				stSigns.close();
			}
			if (stComments != null) {
				stComments.close();
			}
			if (stActions != null) {
				stActions.close();
			}
			if (stRequestValues != null) {
				stRequestValues.close();
			}
			if (stRequestNotice != null) {
				stRequestNotice.close();
			}
			if (stRequestHistoric != null) {
				stRequestHistoric.close();
			}
			if (stRequestTag != null) {
				stRequestTag.close();
			}
			if (stSenders != null) {
				stSenders.close();
			}
			if (stEmails != null) {
				stEmails.close();
			}
			if (stDwRequest != null) {
				stDwRequest.close();
			}
			if (deleteSigns != null) {
				deleteSigns.close();
			}
			if (deleteSenders != null) {
				deleteSenders.close();
			}
			if (deleteRequestTag != null) {
				deleteRequestTag.close();
			}
			if (deleteRequestHistoric != null) {
				deleteRequestHistoric.close();
			}
			if (deleteRequestNotice != null) {
				deleteRequestNotice.close();
			}
			if (deleteRequestValues != null) {
				deleteRequestValues.close();
			}
			if (deleteActions != null) {
				deleteActions.close();
			}
			if (deleteComments != null) {
				deleteComments.close();
			}
			if (deleteSigners != null) {
				deleteSigners.close();
			}
			if (deleteSignLines != null) {
				deleteSignLines.close();
			}
			if (deleteEmails != null) {
				deleteEmails.close();
			}
			if (deleteRequests != null) {
				deleteRequests.close();
			}
		}

		log.debug("------------------------------------");
		log.debug("Moving request " + request + " finished");
		log.debug("------------------------------------");
	}

	// Método que mueve una lista de peticiones al histórico
	private void moveRequest(Connection connPOR, Connection connHIS, String request) throws SQLException, IOException {

//		log.debug("moveRequest " + request + " init");

		PreparedStatement statementPOR = null;
		PreparedStatement statementHIS = null;
		ResultSet rsPOR = null;
		ResultSet rsPOR2 = null;

		try {
			String query = "SELECT * FROM pf_peticiones WHERE x_peticion = " + request;
			statementPOR = connPOR.prepareStatement(query);
			rsPOR = statementPOR.executeQuery();
				
			// Primero se comprueba que las aplicaciones estén dadas de alta en el histórico
			checkApplications(connPOR, connHIS, rsPOR);
			rsPOR.close();
			rsPOR2 = statementPOR.executeQuery();
	
			String insert = "INSERT INTO pf_peticiones (X_PETICION,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO,C_HASH,F_ENTRADA," +
													   "F_INICIO,F_CADUCIDAD,D_REFERENCIA,D_ASUNTO,L_FIRMA_EN_CASCADA," +
													   "L_FIRMA_PRIMER_FIRMANTE,T_PETICION,APL_X_APLICACION,NV_IMP_X_IMPORTANCIA," +
													   "L_SELLO_TIEMPO) " +
							"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);
	
			while (rsPOR2.next()) {
				statementHIS.setBigDecimal(1, rsPOR2.getBigDecimal(1));
				statementHIS.setString(2, rsPOR2.getString(2));
				statementHIS.setTimestamp(3, rsPOR2.getTimestamp(3));
				statementHIS.setString(4, rsPOR2.getString(4));
				statementHIS.setTimestamp(5, rsPOR2.getTimestamp(5));
				statementHIS.setString(6, rsPOR2.getString(6));
				statementHIS.setTimestamp(7, rsPOR2.getTimestamp(7));
				statementHIS.setTimestamp(8, rsPOR2.getTimestamp(8));
				statementHIS.setTimestamp(9, rsPOR2.getTimestamp(9));
				statementHIS.setString(10, rsPOR2.getString(10));
				statementHIS.setString(11, rsPOR2.getString(11));
				statementHIS.setString(12, rsPOR2.getString(12));
				statementHIS.setString(13, rsPOR2.getString(13));
				if (rsPOR2.getBinaryStream(14) != null) {
					 statementHIS.setString(14, rsPOR2.getString(14));
				} else {
					statementHIS.setNull(14, Types.CLOB);
				}
				statementHIS.setBigDecimal(15, rsPOR2.getBigDecimal(15));
				statementHIS.setBigDecimal(16, rsPOR2.getBigDecimal(16));
				statementHIS.setString(17, rsPOR2.getString(17));
				statementHIS.executeUpdate();
			}
			
			rsPOR2.close();
		} catch (SQLException e) {
			throw e;
//		} catch (IOException e) {
//			throw e;
		} finally {			
			if (statementPOR != null) {
				statementPOR.close();
			}
			if (statementHIS != null) {
				statementHIS.close();
			}
		}

		log.debug("moveRequest " + request + " end");
	}

	/**
	 * Método que comprueba si las aplicaciones del listado de peticiones existen en el histórico
	 * @param requests Listado de peticiones
	 * @throws SQLException
	 */
	public void checkApplications(Connection connPOR, Connection connHIS, ResultSet requests) throws SQLException {

//		log.debug("checkApplications init");

		PreparedStatement queryApplicationPOR = null;
		PreparedStatement queryApplicationHIS = null;
		PreparedStatement insertApplication = null;
		ResultSet rsHIS = null;
		ResultSet app = null;

		try {
			String queryApplication = "SELECT * FROM pf_aplicaciones WHERE x_aplicacion = ?";
			queryApplicationPOR = connPOR.prepareStatement(queryApplication);
			queryApplicationHIS = connHIS.prepareStatement(queryApplication);
	
			String insert = "INSERT INTO pf_aplicaciones (X_APLICACION,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
				 										 "C_APLICACION,D_APLICACION,APL_X_APLICACION,CON_X_CONFIGURACION, VISIBLE_EN_PORTAFIRMA_WEB, DESCRIPCION_WEB) " +
			 				"VALUES (?,?,?,?,?,?,?,?,?, ?, ?)";
	
			insertApplication = connHIS.prepareStatement(insert);
	
			while (requests.next()) {
				queryApplicationHIS.setBigDecimal(1, requests.getBigDecimal(15));
				rsHIS = queryApplicationHIS.executeQuery();
				if (!rsHIS.next()) {
					queryApplicationPOR.setBigDecimal(1, requests.getBigDecimal(15));
					app = queryApplicationPOR.executeQuery();
					if (app.next()) {
						insertApplication.setBigDecimal(1, app.getBigDecimal(1));
						insertApplication.setString(2, app.getString(2));
						insertApplication.setTimestamp(3, app.getTimestamp(3));
						insertApplication.setString(4, app.getString(4));
						insertApplication.setTimestamp(5, app.getTimestamp(5));
						insertApplication.setString(6, app.getString(6));
						insertApplication.setString(7, app.getString(7));
						insertApplication.setBigDecimal(8, app.getBigDecimal(8));
						insertApplication.setBigDecimal(9, app.getBigDecimal(9));
						insertApplication.setBigDecimal(10, app.getBigDecimal(10));
						insertApplication.setString(11, app.getString(11));
						insertApplication.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (queryApplicationPOR != null) {
				queryApplicationPOR.close();
			}
			if (queryApplicationHIS != null) {
				queryApplicationHIS.close();
			}
			if (insertApplication != null) {
				insertApplication.close();
			}
			if (rsHIS != null) {
				rsHIS.close();
			}
			if (app != null) {
				app.close();
			}
		}

//		log.debug("checkApplications end");
	}

	// Método que mueve una lista de líneas de firma al histórico
	private void moveSignLines(Connection connPOR, Connection connHIS, ResultSet signLines) throws SQLException {

//		log.debug("moveSignLines init");

		PreparedStatement statementHIS = null;

		try {
			String insert = "INSERT INTO pf_lineas_firma (X_LINEA_FIRMA,C_CREADO,F_CREADO,C_MODIFICADO," +
														 "F_MODIFICADO,C_TIPO,PET_X_PETICION,LFIR_X_LINEA_FIRMA) " +
							"VALUES (?,?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);
	
			while (signLines.next()) {
				statementHIS.setBigDecimal(1, signLines.getBigDecimal(1));
				statementHIS.setString(2, signLines.getString(2));
				statementHIS.setTimestamp(3, signLines.getTimestamp(3));
				statementHIS.setString(4, signLines.getString(4));
				statementHIS.setTimestamp(5, signLines.getTimestamp(5));
				statementHIS.setString(6, signLines.getString(6));
				statementHIS.setBigDecimal(7, signLines.getBigDecimal(7));
				statementHIS.setBigDecimal(8, signLines.getBigDecimal(8));
				statementHIS.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (statementHIS != null) {
				statementHIS.close();
			}
		}

//		log.debug("moveSignLines end");
	}

	/**
	 * Método que mueve una lista de firmantes al histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param signers firmantes 
	 * @throws SQLException 
	 */
	private void moveSigners(Connection connPOR, Connection connHIS, PreparedStatement stSigners) throws SQLException {

//		log.debug("moveSigners init");

		PreparedStatement statementHIS = null;

		try {

			// Primero se comprueba si los usuarios firmantes están en el histórico
			ResultSet signers = stSigners.executeQuery();			
			checkSignerUsers(connPOR, connHIS, signers);
			signers.close();
	
			ResultSet signers2 = stSigners.executeQuery();
			//signers.beforeFirst();
			String insert = "INSERT INTO pf_firmantes (X_FIRMANTE,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
													  "USU_X_USUARIO,LFIR_X_LINEA_FIRMA,FIR_X_FIRMANTE,FAUT_X_FILTRO_AUTORIZACION) " +
							"VALUES (?,?,?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);
	
			while (signers2.next()) {
				// Se inserta el firmante en el histórico
				statementHIS.setBigDecimal(1, signers2.getBigDecimal(1));
				statementHIS.setString(2, signers2.getString(2));
				statementHIS.setTimestamp(3, signers2.getTimestamp(3));
				statementHIS.setString(4, signers2.getString(4));
				statementHIS.setTimestamp(5, signers2.getTimestamp(5));
				statementHIS.setBigDecimal(6, signers2.getBigDecimal(6));
				statementHIS.setBigDecimal(7, signers2.getBigDecimal(7));
				statementHIS.setBigDecimal(8, signers2.getBigDecimal(8));
				statementHIS.setBigDecimal(9, signers2.getBigDecimal(9));
				statementHIS.executeUpdate();
			}
			signers2.close();
		} catch (SQLException e) {
			throw e;
		} finally {			
			if (statementHIS != null) {
				statementHIS.close();
			}
		}

//		log.debug("moveSigners end");
	}

	/**
	 * Método que mueve una lista de firmantes al histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param signers firmantes 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private void moveSigns(Connection connPOR, Connection connHIS, PreparedStatement stSigns) throws SQLException, IOException {

//		log.debug("moveSigns init");

		ResultSet signs = null;
		PreparedStatement statementHIS = null;

		try {
			signs = stSigns.executeQuery();
	
			String insert = "INSERT INTO pf_firmas (X_FIRMA,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
												   "F_ESTADO,T_OBSERVACIONES,C_TRANSACCION,C_APLICACION," +
												   "C_SERVIDOR,C_TIPO,C_URI,B_FIRMA,C_FORMATO,DOC_X_DOCUMENTO," +
												   "FIR_X_FIRMANTE,USU_X_USUARIO,CSV,B_INFORME, F_VALIDO) " +
							"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);
	
			while (signs.next()) {
				// Se inserta la firma en el histórico
				statementHIS.setBigDecimal(1, signs.getBigDecimal(1));
				statementHIS.setString(2, signs.getString(2));
				statementHIS.setTimestamp(3, signs.getTimestamp(3));
				statementHIS.setString(4, signs.getString(4));
				statementHIS.setTimestamp(5, signs.getTimestamp(5));
				statementHIS.setTimestamp(6, signs.getTimestamp(6));
				statementHIS.setString(7, signs.getString(7));
				statementHIS.setString(8, signs.getString(8));
				statementHIS.setString(9, signs.getString(9));
				statementHIS.setString(10, signs.getString(10));
				statementHIS.setString(11, signs.getString(11));
				statementHIS.setString(12, signs.getString(12));
				if (signs.getBinaryStream(13) != null) {
					statementHIS.setBinaryStream(13, signs.getBinaryStream(13), IOUtils.toByteArray(signs.getBinaryStream(13)).length);
				} else {
					statementHIS.setNull(13, Types.BLOB);
				}
				statementHIS.setString(14, signs.getString(14));
				statementHIS.setBigDecimal(15, signs.getBigDecimal(15));
				statementHIS.setBigDecimal(16, signs.getBigDecimal(16));
				statementHIS.setBigDecimal(17, signs.getBigDecimal(17));
				statementHIS.setString(18, signs.getString(18));
				if (signs.getBinaryStream(19) != null) {
					statementHIS.setBinaryStream(19, signs.getBinaryStream(19), IOUtils.toByteArray(signs.getBinaryStream(19)).length);
				} else {
					statementHIS.setNull(19, Types.BLOB);
				}
				statementHIS.setTimestamp(20, signs.getTimestamp(20));
				statementHIS.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (signs != null) {
				signs.close();
			}
			if (statementHIS != null) {
				statementHIS.close();
			}
		}

//		log.debug("moveSigns end");
	}

	/**
	 * Método que comprueba si una lista de usuarios existe en el histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param users Usuarios
	 * @throws SQLException 
	 */
	public void checkSignerUsers(Connection connPOR, Connection connHIS, ResultSet users) throws SQLException {

//		log.debug("checkSignerUsers init");

		PreparedStatement queryUserPOR = null;
		PreparedStatement queryUserHIS = null;
		PreparedStatement insertUser = null;		
		
		try {
			String queryUser = "SELECT * FROM pf_usuarios WHERE x_usuario = ?";
			queryUserPOR = connPOR.prepareStatement(queryUser);
			queryUserHIS = connHIS.prepareStatement(queryUser);
	
			String insert = "INSERT INTO pf_usuarios (X_USUARIO,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
			 										 "C_IDENTIFICADOR,C_ANAGRAMA,D_NOMBRE,D_APELL1,D_APELL2," +
			 										 "L_VIGENTE,C_TIPO,PROV_X_PROVINCIA,L_VISIBLE,L_ALERTA_NOTIF) " +
			 				"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
			insertUser = connHIS.prepareStatement(insert);
	
			while (users.next()) {
				queryUserHIS.setBigDecimal(1, users.getBigDecimal(6));
				ResultSet rsHIS = queryUserHIS.executeQuery();
				if (!rsHIS.next()) {
					queryUserPOR.setBigDecimal(1, users.getBigDecimal(6));
					ResultSet user = queryUserPOR.executeQuery();
					if (user.next()) {
						insertUser.setBigDecimal(1, user.getBigDecimal(1));
						insertUser.setString(2, user.getString(2));
						insertUser.setTimestamp(3, user.getTimestamp(3));
						insertUser.setString(4, user.getString(4));
						insertUser.setTimestamp(5, user.getTimestamp(5));
						insertUser.setString(6, user.getString(6));
						insertUser.setString(7, user.getString(7));
						insertUser.setString(8, user.getString(8));
						insertUser.setString(9, user.getString(9));
						insertUser.setString(10, user.getString(10));
						insertUser.setString(11, user.getString(11));
						insertUser.setString(12, user.getString(12));
						insertUser.setBigDecimal(13, user.getBigDecimal(13));
						insertUser.setString(14, user.getString(14));
						insertUser.setString(15, user.getString(15));
						insertUser.executeUpdate();
					}
					user.close();
				}
				rsHIS.close();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (queryUserPOR != null) {
				queryUserPOR.close();
			}
			if (queryUserHIS != null) {
				queryUserHIS.close();
			}
			if (insertUser != null) {
				insertUser.close();
			}			
		}

//		log.debug("checkSignerUsers end");
	}

	/**
	 * Método que comprueba si una lista de usuarios existe en el histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param users Usuarios
	 * @throws SQLException 
	 */
	public void checkSenderUsers(Connection connPOR, Connection connHIS, ResultSet users) throws SQLException {

//		log.debug("checkSenderUsers init");

		PreparedStatement queryUserPOR = null;
		PreparedStatement queryUserHIS = null;
		PreparedStatement insertUser = null;
		
		try {
			String queryUser = "SELECT * FROM pf_usuarios WHERE x_usuario = ?";
			queryUserPOR = connPOR.prepareStatement(queryUser);
			queryUserHIS = connHIS.prepareStatement(queryUser);
	
			String insert = "INSERT INTO pf_usuarios (X_USUARIO,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
			 										 "C_IDENTIFICADOR,C_ANAGRAMA,D_NOMBRE,D_APELL1,D_APELL2," +
			 										 "L_VIGENTE,C_TIPO,PROV_X_PROVINCIA,L_VISIBLE,L_ALERTA_NOTIF) " +
			 				"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
			insertUser = connHIS.prepareStatement(insert);
	
			while (users.next()) {
				queryUserHIS.setBigDecimal(1, users.getBigDecimal(1));
				ResultSet rsHIS = queryUserHIS.executeQuery();
				if (!rsHIS.next()) {
					queryUserPOR.setBigDecimal(1, users.getBigDecimal(1));
					ResultSet user = queryUserPOR.executeQuery();
					if (user.next()) {
						insertUser.setBigDecimal(1, user.getBigDecimal(1));
						insertUser.setString(2, user.getString(2));
						insertUser.setTimestamp(3, user.getTimestamp(3));
						insertUser.setString(4, user.getString(4));
						insertUser.setTimestamp(5, user.getTimestamp(5));
						insertUser.setString(6, user.getString(6));
						insertUser.setString(7, user.getString(7));
						insertUser.setString(8, user.getString(8));
						insertUser.setString(9, user.getString(9));
						insertUser.setString(10, user.getString(10));
						insertUser.setString(11, user.getString(11));
						insertUser.setString(12, user.getString(12));
						insertUser.setBigDecimal(13, user.getBigDecimal(13));
						insertUser.setString(14, user.getString(14));
						insertUser.setString(15, user.getString(15));
						insertUser.executeUpdate();
					}
					user.close();
				}
				rsHIS.close();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (queryUserPOR != null) {
				queryUserPOR.close();
			}
			if (queryUserHIS != null) {
				queryUserHIS.close();
			}
			if (insertUser != null) {
				insertUser.close();
			}
		}

//		log.debug("checkSenderUsers end");
	}

	/**
	 * Método que mueve una lista de comentarios al histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param comments Lista de comentarios
	 * @throws SQLException 
	 */
	private void moveComments(Connection connPOR, Connection connHIS, ResultSet comments) throws SQLException {

//		log.debug("moveComments init");

		PreparedStatement statementHIS = null;
		PreparedStatement stComentarioUsuarioPOR = null;
		PreparedStatement stComentarioUsuarioHIS = null;

		try {
			String insert = "INSERT INTO pf_comentarios (X_COMENTARIO,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
													  	"D_ASUNTO,T_COMENTARIO,PET_X_PETICION,USU_X_USUARIO) " +
							"VALUES (?,?,?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);
	
			while (comments.next()) {
				statementHIS.setBigDecimal(1, comments.getBigDecimal(1));
				statementHIS.setString(2, comments.getString(2));
				statementHIS.setTimestamp(3, comments.getTimestamp(3));
				statementHIS.setString(4, comments.getString(4));
				statementHIS.setTimestamp(5, comments.getTimestamp(5));
				statementHIS.setString(6, comments.getString(6));
				statementHIS.setString(7, comments.getString(7));
				statementHIS.setBigDecimal(8, comments.getBigDecimal(8));
				statementHIS.setBigDecimal(9, comments.getBigDecimal(9));
				statementHIS.executeUpdate();
				
				String queryStringComentariosUsuarios = "SELECT ' INSERT INTO PF_USUARIOS_COMENTARIO  (    X_USUARIO_COMENTARIO,    C_CREADO,    F_CREADO,    "
						+ "C_MODIFICADO,    F_MODIFICADO,    COM_X_COMENTARIO,    USU_X_USUARIO  )  VALUES  (    '||X_USUARIO_COMENTARIO||',   '''||C_CREADO||''',    "
						+ "TO_DATE( '''||TO_CHAR(F_CREADO, 'yyyy/MM/dd HH24:mi:ss')||''', ''yyyy/MM/dd HH24:mi:ss''),    '''||C_MODIFICADO||''',    "
						+ "TO_DATE( '''||TO_CHAR(F_MODIFICADO, 'yyyy/MM/dd HH24:mi:ss')||''', ''yyyy/MM/dd HH24:mi:ss''),    '||COM_X_COMENTARIO||',    '|| "
						+ " USU_X_USUARIO||'  ) ' FROM PF_USUARIOS_COMENTARIO where COM_X_COMENTARIO = ? ";

				stComentarioUsuarioPOR = connPOR.prepareStatement(queryStringComentariosUsuarios);
				
				stComentarioUsuarioPOR.setBigDecimal(1, comments.getBigDecimal(1));
				ResultSet resultadoUsuarioComentario = stComentarioUsuarioPOR.executeQuery();
				
				while (resultadoUsuarioComentario.next()){
					String insertUsuarioComentario = resultadoUsuarioComentario.getString(1);
					stComentarioUsuarioHIS = connHIS.prepareStatement(insertUsuarioComentario);
					stComentarioUsuarioHIS.executeUpdate();
				}
				
				resultadoUsuarioComentario.close();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (statementHIS != null) {
				statementHIS.close();
			}
		}

//		log.debug("moveComments end");
	}

	/**
	 * Método que mueve una lista de acciones al histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param actions Lista de acciones
	 * @throws SQLException 
	 */
	private void moveActions(Connection connPOR, Connection connHIS, ResultSet actions) throws SQLException {

//		log.debug("moveActions init");

		PreparedStatement statementHIS = null;

		try {
			String insert = "INSERT INTO pf_acciones (X_ACCION,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
													 "T_ACCION,C_TIPO,DOC_X_DOCUMENTO,PET_X_PETICION,ETIQ_X_ETIQUETA) " +
							"VALUES (?,?,?,?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);
	
			while (actions.next()) {
				statementHIS.setBigDecimal(1, actions.getBigDecimal(1));
				statementHIS.setString(2, actions.getString(2));
				statementHIS.setTimestamp(3, actions.getTimestamp(3));
				statementHIS.setString(4, actions.getString(4));
				statementHIS.setTimestamp(5, actions.getTimestamp(5));
				statementHIS.setString(6, actions.getString(6));
				statementHIS.setString(7, actions.getString(7));
				statementHIS.setBigDecimal(8, actions.getBigDecimal(8));
				statementHIS.setBigDecimal(9, actions.getBigDecimal(9));
				statementHIS.setBigDecimal(10, actions.getBigDecimal(10));
				statementHIS.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (statementHIS != null) {
				statementHIS.close();
			}
		}

//		log.debug("moveActions end");
	}

	/**
	 * Método que mueve una lista de peticiones valor al histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param requestValues Lista de peticiones valor
	 * @throws SQLException 
	 */
	private void moveRequestValues(Connection connPOR, Connection connHIS, ResultSet requestValues) throws SQLException {

//		log.debug("moveRequestValues init");

		PreparedStatement statementHIS = null;

		try {
			String insert = "INSERT INTO pf_peticiones_valor (X_PETICION_VALOR,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
													 		 "T_VALOR,PEIN_X_PETICION_INFORMACION,PET_X_PETICION) " +
							"VALUES (?,?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);
	
			while (requestValues.next()) {
				statementHIS.setBigDecimal(1, requestValues.getBigDecimal(1));
				statementHIS.setString(2, requestValues.getString(2));
				statementHIS.setTimestamp(3, requestValues.getTimestamp(3));
				statementHIS.setString(4, requestValues.getString(4));
				statementHIS.setTimestamp(5, requestValues.getTimestamp(5));
				statementHIS.setString(6, requestValues.getString(6));
				statementHIS.setBigDecimal(7, requestValues.getBigDecimal(7));
				statementHIS.setBigDecimal(8, requestValues.getBigDecimal(8));
				statementHIS.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (statementHIS != null) {
				statementHIS.close();
			}
		}

//		log.debug("moveRequestValues end");
	}

	/**
	 * Método que mueve una lista de peticiones aviso al histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param requestNotices Lista de peticiones aviso
	 * @throws SQLException 
	 */
	private void moveRequestNotices(Connection connPOR, Connection connHIS, ResultSet requestNotices) throws SQLException {

//		log.debug("moveRequestNotices init");

		PreparedStatement statementHIS = null;

		try {
			String insert = "INSERT INTO pf_peticiones_aviso (X_PETICION_AVISO,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
													 		 "PET_X_PETICION,ETIQ_X_ETIQUETA) " +
							"VALUES (?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);
	
			while (requestNotices.next()) {
				statementHIS.setBigDecimal(1, requestNotices.getBigDecimal(1));
				statementHIS.setString(2, requestNotices.getString(2));
				statementHIS.setTimestamp(3, requestNotices.getTimestamp(3));
				statementHIS.setString(4, requestNotices.getString(4));
				statementHIS.setTimestamp(5, requestNotices.getTimestamp(5));
				statementHIS.setBigDecimal(6, requestNotices.getBigDecimal(6));
				statementHIS.setBigDecimal(7, requestNotices.getBigDecimal(7));
				statementHIS.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (statementHIS != null) {
				statementHIS.close();
			}
		}

//		log.debug("moveRequestNotices end");
	}

	/**
	 * Método que mueve una lista de peticiones histórico al histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param requestNotices Lista de peticiones histórico
	 * @throws SQLException 
	 */
	private void moveRequestHistoric(Connection connPOR, Connection connHIS, ResultSet requestHistoric) throws SQLException {

//		log.debug("moveRequestHistoric init");

		PreparedStatement statementHIS = null;

		try {
			String insert = "INSERT INTO pf_peticiones_historico (X_PETICION_HISTORICO,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
													 		 	 "C_PETICION_HISTORICO,T_PETICION_HISTORICO,PET_X_PETICION,USU_X_USUARIO) " +
							"VALUES (?,?,?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);
	
			while (requestHistoric.next()) {
				statementHIS.setBigDecimal(1, requestHistoric.getBigDecimal(1));
				statementHIS.setString(2, requestHistoric.getString(2));
				statementHIS.setTimestamp(3, requestHistoric.getTimestamp(3));
				statementHIS.setString(4, requestHistoric.getString(4));
				statementHIS.setTimestamp(5, requestHistoric.getTimestamp(5));
				statementHIS.setString(6, requestHistoric.getString(6));
				statementHIS.setString(7, requestHistoric.getString(7));
				statementHIS.setBigDecimal(8, requestHistoric.getBigDecimal(8));
				statementHIS.setBigDecimal(9, requestHistoric.getBigDecimal(9));
				statementHIS.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (statementHIS != null) {
				statementHIS.close();
			}
		}

//		log.debug("moveRequestHistoric end");
	}

	/**
	 * Método que mueve una lista de etiquetas petición al histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param requestNotices Lista de etiquetas petición
	 * @throws SQLException 
	 */
	private void moveRequestTags(Connection connPOR, Connection connHIS, ResultSet requestTags) throws SQLException {

//		log.debug("moveRequestTags init");

		PreparedStatement statementHIS = null;

		try {
			String insert = "INSERT INTO pf_etiquetas_peticion (X_ETIQUETA_PETICION,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
													 		   "PET_X_PETICION,USU_X_USUARIO,ETIQ_X_ETIQUETA,LFIR_X_LINEA_FIRMA,C_HASH,GRO_X_GROUP) " +
							"VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);

			while (requestTags.next()) {
				statementHIS.setBigDecimal(1, requestTags.getBigDecimal(1));
				statementHIS.setString(2, requestTags.getString(2));
				statementHIS.setTimestamp(3, requestTags.getTimestamp(3));
				statementHIS.setString(4, requestTags.getString(4));
				statementHIS.setTimestamp(5, requestTags.getTimestamp(5));
				statementHIS.setBigDecimal(6, requestTags.getBigDecimal(6));
				statementHIS.setBigDecimal(7, requestTags.getBigDecimal(7));
				statementHIS.setBigDecimal(8, requestTags.getBigDecimal(8));
				statementHIS.setBigDecimal(9, requestTags.getBigDecimal(9));
				statementHIS.setString(10, requestTags.getString(10));
				statementHIS.setBigDecimal(11, requestTags.getBigDecimal(11));
				statementHIS.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (statementHIS != null) {
				statementHIS.close();
			}
		}

//		log.debug("moveRequestTags end");
	}

	/**
	 * Método que mueve una lista de usuarios remitentes al histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param requestNotices Lista de remitentes
	 * @throws SQLException 
	 */
	private void moveSenders(Connection connPOR, Connection connHIS, ResultSet senders) throws SQLException {

//		log.debug("moveSenders init");

		PreparedStatement statementHIS = null;
		PreparedStatement statementUsers = null;

		try {
			String insert = "INSERT INTO pf_usuarios_remitente (X_USUARIO_REMITENTE,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
													 		   "L_NOTIFICA_EMAIL,L_NOTIFICA_MOVIL,USU_X_USUARIO,PET_X_PETICION) " +
							"VALUES (?,?,?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);
	
			String users = "";
			boolean first = true;
			while (senders.next()) {
				// Se recupera el usuario
				if (first) {
					users += senders.getBigDecimal(8);
					first = false;
				} else {
					users += "," + senders.getBigDecimal(8);
				}
				statementHIS.setBigDecimal(1, senders.getBigDecimal(1));
				statementHIS.setString(2, senders.getString(2));
				statementHIS.setTimestamp(3, senders.getTimestamp(3));
				statementHIS.setString(4, senders.getString(4));
				statementHIS.setTimestamp(5, senders.getTimestamp(5));
				statementHIS.setString(6, senders.getString(6));
				statementHIS.setString(7, senders.getString(7));
				statementHIS.setBigDecimal(8, senders.getBigDecimal(8));
				statementHIS.setBigDecimal(9, senders.getBigDecimal(9));
				statementHIS.executeUpdate();
			}
	
			statementUsers = connPOR.prepareStatement("SELECT * FROM pf_usuarios WHERE x_usuario in (" + users + ")");
			ResultSet rsUsers = statementUsers.executeQuery();			
			checkSenderUsers(connPOR, connHIS, rsUsers);
			rsUsers.close();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (statementHIS != null) {
				statementHIS.close();
			}
			if (statementUsers != null) {
				statementUsers.close();
			}
		}

//		log.debug("moveSenders end");
	}
	
	
	/**
	 * Método que mueve una lista de emails  al histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param emails Lista de emails
	 * @throws SQLException 
	 */
	private void moveEmails(Connection connPOR, Connection connHIS, ResultSet emails) throws SQLException {

//		log.debug("moveEmails init");

		PreparedStatement statementHIS = null;
		PreparedStatement statementUsers = null;

		try {
			String insert = "INSERT INTO pf_emails_peticion (X_EMAIL_PETICION,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
													 		   "PET_X_PETICION, C_EMAIL) " +
							"VALUES (?,?,?,?,?,?,?)";
	
			statementHIS = connHIS.prepareStatement(insert);
	
			while (emails.next()) {
				statementHIS.setBigDecimal(1, emails.getBigDecimal(1));
				statementHIS.setString(2, emails.getString(2));
				statementHIS.setTimestamp(3, emails.getTimestamp(3));
				statementHIS.setString(4, emails.getString(4));
				statementHIS.setTimestamp(5, emails.getTimestamp(5));
				statementHIS.setBigDecimal(6, emails.getBigDecimal(6));
				statementHIS.setString(7, emails.getString(7));				
				statementHIS.executeUpdate();
			}
			
		} catch (SQLException e) {
			throw e;
		} finally {
			if (statementHIS != null) {
				statementHIS.close();
			}
			if (statementUsers != null) {
				statementUsers.close();
			}
		}

//		log.debug("moveEmails end");
	}
	

	/**
	 * Método que mueve una lista de documentos al histórico
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param requestNotices Lista de documentos
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private void moveDocuments(Connection connPOR, Connection connHIS, ResultSet documents) throws SQLException, IOException {

//		log.debug("moveDocuments init");

		PreparedStatement stFilePOR = null;
		PreparedStatement stDocumentoUsuarioPOR = null;
		PreparedStatement stDocumentoUsuarioHIS = null;
		PreparedStatement stFileHIS = null;
		PreparedStatement stFileByHashHIS = null;
		PreparedStatement stDocumentHIS = null;
		PreparedStatement stFileInsertHIS = null;
		
		try {
			// Se consultan los archivos de los documentos en Portafirmas
			String queryFile = "SELECT * FROM pf_archivos WHERE X_ARCHIVO = ?";
			// En el histórico, también se busca si hay algún fichero por el código mismo código hash
			String queryFileByHash = "SELECT x_archivo FROM pf_archivos WHERE C_HASH = ?";
			stFilePOR = connPOR.prepareStatement(queryFile);
			stFileHIS = connHIS.prepareStatement(queryFile);
			stFileByHashHIS = connHIS.prepareStatement(queryFileByHash);
	
			// Se guardan los documentos en el histórico
			String insertDocument = "INSERT INTO pf_documentos (X_DOCUMENTO,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
													   		   "C_HASH,D_NOMBRE,D_MIME,T_COMENTARIOS,L_FIRMABLE,C_PREFIRMA," +
													   		   "PET_X_PETICION,TDOC_X_TIPO_DOCUMENTO,ARC_X_ARCHIVO,AMB_X_AMBITO,L_ES_FIRMA, USU_X_USUARIO_ANEXA, C_COMENTARIO_ANEXO) " +
									"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stDocumentHIS = connHIS.prepareStatement(insertDocument);
	
			// Se guardan los archivos en el histórico
			String insertFile = "INSERT INTO pf_archivos (X_ARCHIVO,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
		   	   											 "C_HASH,C_HASH_ALG,C_TIPO,C_URI,B_ARCHIVO,T_COMENTARIOS,ARC_X_ARCHIVO) " +
		   	   					"VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			stFileInsertHIS = connHIS.prepareStatement(insertFile);
	
			while (documents.next()) {
				BigDecimal archivoPk = documents.getBigDecimal(14);
				// Se recupera el archivo correspondiente al documento
				stFileHIS.setBigDecimal(1, documents.getBigDecimal(14));
				ResultSet fileHIS = stFileHIS.executeQuery();
				
				// Se guarda el archivo en el histórico si no existía ya
				if (!fileHIS.next()) {		
					stFilePOR.setBigDecimal(1, documents.getBigDecimal(14));
					ResultSet filePOR = stFilePOR.executeQuery();
					if (filePOR.next()) {
						// Antes comprobamos que no existe un fichero con el mismo código hash..
						stFileByHashHIS.setString(1, filePOR.getString(6));
						ResultSet fileByHashHIS = stFileByHashHIS.executeQuery();
						if(fileByHashHIS.next()) {
							// .. si lo hay, no se hace la inserción y se le prepara al documento clave primaria del fichero encontrado
							archivoPk = fileByHashHIS.getBigDecimal(1);
						} else {
							stFileInsertHIS.setBigDecimal(1, filePOR.getBigDecimal(1));
							stFileInsertHIS.setString(2, filePOR.getString(2));
							stFileInsertHIS.setTimestamp(3, filePOR.getTimestamp(3));
							stFileInsertHIS.setString(4, filePOR.getString(4));
							stFileInsertHIS.setTimestamp(5, filePOR.getTimestamp(5));
							stFileInsertHIS.setString(6, filePOR.getString(6));
							stFileInsertHIS.setString(7, filePOR.getString(7));
							stFileInsertHIS.setString(8, filePOR.getString(8));
							stFileInsertHIS.setString(9, filePOR.getString(9));
							if (filePOR.getBinaryStream(10) != null) {
								stFileInsertHIS.setBinaryStream(10, filePOR.getBinaryStream(10), IOUtils.toByteArray(filePOR.getBinaryStream(10)).length);
							} else {
								stFileInsertHIS.setNull(10, Types.BLOB);
							}
							stFileInsertHIS.setString(11, filePOR.getString(11));
							stFileInsertHIS.setBigDecimal(12, filePOR.getBigDecimal(12));
							stFileInsertHIS.executeUpdate();
						}
						fileByHashHIS.close();
					}
					filePOR.close();
				}
				fileHIS.close();
	
				// Se guarda el documento en el histórico
				stDocumentHIS.setBigDecimal(1, documents.getBigDecimal(1));
				stDocumentHIS.setString(2, documents.getString(2));
				stDocumentHIS.setTimestamp(3, documents.getTimestamp(3));
				stDocumentHIS.setString(4, documents.getString(4));
				stDocumentHIS.setTimestamp(5, documents.getTimestamp(5));
				stDocumentHIS.setString(6, documents.getString(6));
				stDocumentHIS.setString(7, documents.getString(7));
				stDocumentHIS.setString(8, documents.getString(8));
				stDocumentHIS.setString(9, documents.getString(9));
				stDocumentHIS.setString(10, documents.getString(10));
				stDocumentHIS.setString(11, documents.getString(11));
				stDocumentHIS.setBigDecimal(12, documents.getBigDecimal(12));
				stDocumentHIS.setBigDecimal(13, documents.getBigDecimal(13));		
				stDocumentHIS.setBigDecimal(14, archivoPk);
				stDocumentHIS.setBigDecimal(15, documents.getBigDecimal(15));
				stDocumentHIS.setString(16, documents.getString(16));
				stDocumentHIS.setBigDecimal(17, documents.getBigDecimal(17));
				stDocumentHIS.setString(18, documents.getString(18));
				stDocumentHIS.executeUpdate();
				
				String selectRelacionusuarioDocumento = " select 'INSERT INTO PF_USUARIOS_DOC_VISIBLES (USU_X_USUARIO,DOC_X_DOCUMENTO) values ('||USU_X_USUARIO||','|| "
						+ " DOC_X_DOCUMENTO||')' from PF_USUARIOS_DOC_VISIBLES where DOC_X_DOCUMENTO = ? ";
				
				stDocumentoUsuarioPOR = connPOR.prepareStatement(selectRelacionusuarioDocumento);
				
				stDocumentoUsuarioPOR.setBigDecimal(1, documents.getBigDecimal(1));
				ResultSet resultadoUsuarioDocumento = stDocumentoUsuarioPOR.executeQuery();
				
				while (resultadoUsuarioDocumento.next()){
					String insertUsuarioDocumento = resultadoUsuarioDocumento.getString(1);
					stDocumentoUsuarioHIS = connHIS.prepareStatement(insertUsuarioDocumento);
					stDocumentoUsuarioHIS.executeUpdate();
				}
				
				resultadoUsuarioDocumento.close();
				
			}
		} catch (SQLException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (stFilePOR != null) {
				stFilePOR.close();
			}
			if (stFileHIS != null) {
				stFileHIS.close();
			}
			if (stFileByHashHIS != null) {
				stFileByHashHIS.close();
			}
			if (stDocumentHIS != null) {
				stDocumentHIS.close();
			}
			if (stFileInsertHIS != null) {
				stFileInsertHIS.close();
			}
		}

//		log.debug("moveDocuments end");
	}

	/**
	 * Método que mueve entre bases de datos las tablas involucradas de la interfaz genérica
	 * @param connPOR Conexión origen
	 * @param connHIS Conexión destino
	 * @throws SQLException
	 */
	private void moveGenericInterface(Connection connPOR, Connection connHIS, ResultSet requests) throws SQLException {

//		log.debug("moveGenericInterface init");

		PreparedStatement stDwDocs = null;
		PreparedStatement stDwDocsHIS = null;
		PreparedStatement stDwSigners = null;
		PreparedStatement stDwSignersHIS = null;
		PreparedStatement stDwRequestsHIS = null;
		ResultSet documents = null;
		ResultSet signers = null;

		try {
			// Consulta para recuperar los documentos de la petición
			String queryDwDocuments = "SELECT * FROM pf_docelweb_documento WHERE doc_x_solicitud = ?";
			stDwDocs = connPOR.prepareStatement(queryDwDocuments);
	
			// Consulta para insertar documentos
			String insertDocuments = "INSERT INTO pf_docelweb_documento (X_DOCUMENTO,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
															           "D_DESCRIPCION,D_RUTA_DOCUMENTO,D_TIPO_DOCEL,D_REQ_FIRMA," +
															           "D_DISPOSICION_URL,DOC_X_ARCHIVO,DOC_X_SOLICITUD,DOC_X_PFDOC)" +
									 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stDwDocsHIS = connHIS.prepareStatement(insertDocuments);
	
			// Consulta para recuperar la lista de firmantes de la petición
			String queryDwSigners = "SELECT * FROM pf_docelweb_x_lista_firmantes WHERE x_id_transaccion = ?";
			stDwSigners = connPOR.prepareStatement(queryDwSigners);
	
			// Consulta para insertar listas de firmantes
			String insertSignerList = "INSERT INTO pf_docelweb_x_lista_firmantes (X_ID_TRANSACCION,X_USUARIO)" +
									  "VALUES (?,?)";
			stDwSignersHIS = connHIS.prepareStatement(insertSignerList);
	
			// Consulta para insertar peticiones
			String insertRequests = "INSERT INTO pf_docelweb_solicitud_spfirma (X_ID_TRANSACCION,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
															 				   "D_ESTADO,D_DESCRIPCION,D_PRIORIDAD,D_FORMATO_FIRMA,D_VERSION_XADES," +
															 				   "D_TIPO_MULTIFIRMA,F_FECHA_LIMITE,D_REMITENTE,SOLICITUD_X_GESTOR,SOLICITUD_X_PETICION)" +
									"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			stDwRequestsHIS = connHIS.prepareStatement(insertRequests);
	
			// Se recorren todas las peticiones de la interfaz genérica
			while (requests.next()) {
				// Se mueven las peticiones
				stDwRequestsHIS.setBigDecimal(1, requests.getBigDecimal(1));
				stDwRequestsHIS.setString(2, requests.getString(2));
				stDwRequestsHIS.setTimestamp(3, requests.getTimestamp(3));
				stDwRequestsHIS.setString(4, requests.getString(4));
				stDwRequestsHIS.setTimestamp(5, requests.getTimestamp(5));
				stDwRequestsHIS.setString(6, requests.getString(6));
				stDwRequestsHIS.setString(7, requests.getString(7));
				stDwRequestsHIS.setString(8, requests.getString(8));
				stDwRequestsHIS.setString(9, requests.getString(9));
				stDwRequestsHIS.setString(10, requests.getString(10));
				stDwRequestsHIS.setString(11, requests.getString(11));
				stDwRequestsHIS.setTimestamp(12, requests.getTimestamp(12));
				stDwRequestsHIS.setString(13, requests.getString(13));
				stDwRequestsHIS.setBigDecimal(14, requests.getBigDecimal(14));
				stDwRequestsHIS.setBigDecimal(15, requests.getBigDecimal(15));
				stDwRequestsHIS.executeUpdate();
	
				// Se recuperan los documentos de cada petición
				stDwDocs.setBigDecimal(1, requests.getBigDecimal(1));
				documents = stDwDocs.executeQuery();
	
				// Se mueven los documentos
				while (documents.next()) {
					stDwDocsHIS.setBigDecimal(1, documents.getBigDecimal(1));
					stDwDocsHIS.setString(2, documents.getString(2));
					stDwDocsHIS.setTimestamp(3, documents.getTimestamp(3));
					stDwDocsHIS.setString(4, documents.getString(4));
					stDwDocsHIS.setTimestamp(5, documents.getTimestamp(5));
					stDwDocsHIS.setString(6, documents.getString(6));
					stDwDocsHIS.setString(7, documents.getString(7));
					stDwDocsHIS.setString(8, documents.getString(8));
					stDwDocsHIS.setString(9, documents.getString(9));
					stDwDocsHIS.setString(10, documents.getString(10));
					stDwDocsHIS.setBigDecimal(11, documents.getBigDecimal(11));
					stDwDocsHIS.setBigDecimal(12, documents.getBigDecimal(12));
					stDwDocsHIS.setBigDecimal(13, documents.getBigDecimal(13));
					stDwDocsHIS.executeUpdate();
				}
				documents.close();
	
				// Se recupera la lista de firmantes de cada petición
				stDwSigners.setBigDecimal(1, requests.getBigDecimal(1));
				signers = stDwSigners.executeQuery();
	
				// Se mueven las listas de firmantes
				while (signers.next()) {
					// Si el usuario no existe en el histórico se mueve
					checkUser(connPOR, connHIS, signers.getBigDecimal(2));
					stDwSignersHIS.setBigDecimal(1, signers.getBigDecimal(1));
					stDwSignersHIS.setBigDecimal(2, signers.getBigDecimal(2));
					stDwSignersHIS.executeUpdate();
				}
				signers.close();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (stDwDocs != null) {
				stDwDocs.close();
			}
			if (stDwDocsHIS != null) {
				stDwDocsHIS.close();
			}
			if (stDwSigners != null) {
				stDwSigners.close();
			}
			if (stDwSignersHIS != null) {
				stDwSignersHIS.close();
			}
			if (stDwRequestsHIS != null) {
				stDwRequestsHIS.close();
			}
		}

//		log.debug("moveGenericInterface end");
	}

	/**
	 * Método que comprueba si un usuario existe en el histórico, si no lo mueve
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param userId Identificador del usuario
	 */
	private void checkUser(Connection connPOR, Connection connHIS, BigDecimal userId) throws SQLException {

//		log.debug("checkUser init");

		PreparedStatement queryUserHIS = null;
		PreparedStatement queryUserPOR = null;
		PreparedStatement insertUser = null;
		
		try {
			// Consulta para los usuarios
			String queryUser = "SELECT * FROM pf_usuarios WHERE x_usuario = ?";
			queryUserHIS = connHIS.prepareStatement(queryUser);
			queryUserPOR = connPOR.prepareStatement(queryUser);
	
			// Consulta para insertar un usuario
			String queryInsertUser = "INSERT INTO pf_usuarios (X_USUARIO,C_CREADO,F_CREADO,C_MODIFICADO,F_MODIFICADO," +
			 											 	  "C_IDENTIFICADOR,C_ANAGRAMA,D_NOMBRE,D_APELL1,D_APELL2," +
			 											 	  "L_VIGENTE,C_TIPO,PROV_X_PROVINCIA,L_VISIBLE,L_ALERTA_NOTIF) " +
			 						 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			insertUser = connHIS.prepareStatement(queryInsertUser);
	
			// Si el usuario no existe en el histórico, se recupera de Portafirmas y se mueve
			queryUserHIS.setBigDecimal(1, userId);
			if (!queryUserHIS.execute()) {
				queryUserPOR.setBigDecimal(1, userId);
				ResultSet user = queryUserPOR.executeQuery();
				if (user.next()) {
					insertUser.setBigDecimal(1, user.getBigDecimal(1));
					insertUser.setString(2, user.getString(2));
					insertUser.setTimestamp(3, user.getTimestamp(3));
					insertUser.setString(4, user.getString(4));
					insertUser.setTimestamp(5, user.getTimestamp(5));
					insertUser.setString(6, user.getString(6));
					insertUser.setString(7, user.getString(7));
					insertUser.setString(8, user.getString(8));
					insertUser.setString(9, user.getString(9));
					insertUser.setString(10, user.getString(10));
					insertUser.setString(11, user.getString(11));
					insertUser.setString(12, user.getString(12));
					insertUser.setBigDecimal(13, user.getBigDecimal(13));
					insertUser.setString(14, user.getString(14));
					insertUser.setString(15, user.getString(15));
					insertUser.executeUpdate();
				}
				user.close();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (queryUserHIS != null) {
				queryUserHIS.close();
			}
			if (queryUserPOR != null) {
				queryUserPOR.close();
			}
			if (insertUser != null) {
				insertUser.close();
			}
		}

//		log.debug("checkUser end");
	}

	/**
	 * Método que borra las tablas que involucran peticiones de la interfaz genérica
	 * @param connPOR Conexión Portafirmas
	 * @param connHIS Conexión Histórico
	 * @param requests Peticiones a borrar
	 * @throws SQLException
	 */
	private void deleteGenericInterface(Connection connPOR, ResultSet requests) throws SQLException {

//		log.debug("deleteGenericInterface init");

		PreparedStatement deleteDocuments = null;
		PreparedStatement deleteSigners = null;
		PreparedStatement deleteRequests = null;

		try {
			// Consulta para borrar los documentos de la petición
			String queryDeleteDocuments = "DELETE FROM pf_docelweb_documento WHERE doc_x_solicitud = ?";
			deleteDocuments = connPOR.prepareStatement(queryDeleteDocuments);
	
			// Consulta para borrar la lista de firmantes de la petición
			String queryDeleteSigners = "DELETE FROM pf_docelweb_x_lista_firmantes WHERE x_id_transaccion = ?";
			deleteSigners = connPOR.prepareStatement(queryDeleteSigners);
	
			// Consulta para borrar peticiones
			String queryDeleteRequests = "DELETE FROM pf_docelweb_solicitud_spfirma WHERE x_id_transaccion = ?";
			deleteRequests = connPOR.prepareStatement(queryDeleteRequests);
	
			// Se recorren todas las peticiones de la interfaz genérica
			while (requests.next()) {
	
				// Se borran los documentos de cada petición
				deleteDocuments.setBigDecimal(1, requests.getBigDecimal(1));
				deleteDocuments.executeUpdate();
	
				// Se borra la lista de firmantes de cada petición
				deleteSigners.setBigDecimal(1, requests.getBigDecimal(1));
				deleteSigners.executeUpdate();
	
				// Se borran las peticiones
				deleteRequests.setBigDecimal(1, requests.getBigDecimal(1));
				deleteRequests.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (deleteDocuments != null) {
				deleteDocuments.close();
			}
			if (deleteSigners != null) {
				deleteSigners.close();
			}
			if (deleteRequests != null) {
				deleteRequests.close();
			}
		}

//		log.debug("deleteGenericInterface end");
	}

	/**
	 * Método que borra documentos y archivos de Portafirmas
	 * @param connPOR Conexión Portafirmas
	 * @param documents Documentos
	 * @throws SQLException 
	 */
	private void deleteDocumentsAndFiles(Connection connPOR, ResultSet documents) throws SQLException {

//		log.debug("deleteDocumentsAndFiles init");

		PreparedStatement deleteDocument = null;
		PreparedStatement deleteDocumentoUsuario = null;
		
		PreparedStatement deleteFile = null;
		PreparedStatement queryDocuments = null;
		PreparedStatement queryDocumentsGI = null;
		ResultSet rs = null;
		ResultSet rsGI = null;

		try {
			deleteDocument = connPOR.prepareStatement("DELETE FROM pf_documentos WHERE x_documento = ?");
			deleteDocumentoUsuario = connPOR.prepareStatement("DELETE FROM PF_USUARIOS_DOC_VISIBLES WHERE DOC_X_DOCUMENTO = ?");
			deleteFile = connPOR.prepareStatement("DELETE FROM pf_archivos WHERE x_archivo = ?");
			queryDocuments = connPOR.prepareStatement("SELECT COUNT(*) FROM pf_documentos WHERE arc_x_archivo = ?");
			queryDocumentsGI = connPOR.prepareStatement("SELECT COUNT(*) FROM pf_docelweb_documento WHERE doc_x_archivo = ?");
	
			while (documents.next()) {
				// Se calcula cuantos documentos enlazan con el archivo del documento
				queryDocuments.setBigDecimal(1, documents.getBigDecimal(14));
				rs = queryDocuments.executeQuery();
				rs.next();
				Integer count = rs.getInt(1);
				rs.close();
	
				// Se calcula cuantos documentos enlazan con el archivo del documento
				queryDocumentsGI.setBigDecimal(1, documents.getBigDecimal(14));
				rsGI = queryDocumentsGI.executeQuery();
				rsGI.next();
				Integer countGI = rsGI.getInt(1);
				rsGI.close();
	
				//Se borran las asociaciones de documento con usuario
				
				deleteDocumentoUsuario.setBigDecimal(1, documents.getBigDecimal(1));
				deleteDocumentoUsuario.executeUpdate();
				
				// Se borra el documento
				deleteDocument.setBigDecimal(1, documents.getBigDecimal(1));
				deleteDocument.executeUpdate();
	
				// Si sólo un documento enlaza con el archivo este se elimina
				if (count == 1 && countGI == 0) {
					deleteFile.setBigDecimal(1, documents.getBigDecimal(14));
					deleteFile.executeUpdate();
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (deleteDocument != null) {
				deleteDocument.close();
			}
			if (deleteFile != null) {
				deleteFile.close();
			}
			if (queryDocuments != null) {
				queryDocuments.close();
			}
			if (queryDocumentsGI != null) {
				queryDocumentsGI.close();
			}
		}

//		log.debug("deleteDocumentsAndFiles end");
	}

	/**
	 * Método que borra los firmantes de Portafirmas
	 * @param connPOR Conexión Portafirmas
	 * @param signers Firmantes
	 * @throws SQLException
	 */
	private void deleteSigners(Connection connPOR, ResultSet signers) throws SQLException {

//		log.debug("deleteSigners init");

		PreparedStatement deleteSigner = null; 

		try {
			deleteSigner = connPOR.prepareStatement("DELETE FROM pf_firmantes WHERE x_firmante = ?");
	
			while (signers.next()) {
				deleteSigner.setBigDecimal(1, signers.getBigDecimal(1));
				deleteSigner.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (deleteSigner != null) {
				deleteSigner.close();				
			}
		}

//		log.debug("deleteSigners end");
	}

	/**
	 * Método que borra las firmas de Portafirmas
	 * @param connPOR Conexión Portafirmas
	 * @param signers Firmas
	 * @throws SQLException
	 */
	private void deleteSigns(Connection connPOR, ResultSet signs) throws SQLException {

//		log.debug("deleteSigns init");

		PreparedStatement deleteSign = null;

		try {
			deleteSign = connPOR.prepareStatement("DELETE FROM pf_firmas WHERE x_firma = ?");
	
			while (signs.next()) {
				deleteSign.setBigDecimal(1, signs.getBigDecimal(1));
				deleteSign.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (deleteSign != null) {
				deleteSign.close();
			}
		}

//		log.debug("deleteSigns end");
	}

	public void returnFromStorage(List<String> requests) throws SQLException {

		Connection connHIS = null;
		Connection connPOR = null;

		// Se guardan todos los registros que involucran a la petición
		try {
			connHIS = getConnection(StorageConstants.HISTORICO);
			connPOR = getConnection(StorageConstants.PORTAFIRMA);

			moveList(connHIS, connPOR, requests);

			connHIS.commit();
			connPOR.commit();

		} catch (SQLException e) {
			log.error ("Error leyendo fichero de propiedades", e);			
		} catch (ClassNotFoundException e) {
			log.error ("Error en returnFromStorage", e);
			
		} catch (IOException e) {
			log.error ("Error en returnFromStorage", e);			
		} catch (Throwable t) {
			log.error ("Error en returnFromStorage", t);			
		} finally {
			connHIS.rollback();
			connPOR.rollback();
			connHIS.close();
			connPOR.close();
		}
	}


	/**
	 * Método que elimina una lista de peticiones del histórico (irrevocable)
	 * @param requests Lista de peticiones
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public void deleteForever(List<String> requests) throws Throwable {

		Connection conn = null;

		try {
			conn = getConnection(StorageConstants.HISTORICO);

			for (String request : requests) {
				deleteRequestForever(conn, request);
			}

			conn.commit();

		} catch (SQLException e) {
			log.error ("Error en deleteForever", e);			
		} catch (Throwable t) {
			log.error ("Error en deleteForever", t);			
		} finally {
			conn.rollback();
			conn.close();
		}
	}

	/**
	 * Método que borra para siempre una petición del histórico
	 * @param conn Conexión con la base de datos
	 * @param request Petición a borrar
	 * @throws SQLException 
	 */
	private void deleteRequestForever(Connection conn, String request) throws SQLException {

		log.debug("------------------------------------");
		log.debug("deleteRequestForever " + request + " init");
		log.debug("------------------------------------");

		PreparedStatement stDwRequest = null;
		PreparedStatement deleteSigns = null;
		PreparedStatement stDocuments = null;
		PreparedStatement deleteSenders = null;
		PreparedStatement deleteRequestTag = null;
		PreparedStatement deleteRequestHistoric = null;
		PreparedStatement deleteRequestNotice = null;
		PreparedStatement deleteRequestValues =  null;
		PreparedStatement deleteActions = null;
		PreparedStatement deleteComments = null;
		PreparedStatement deleteCommentsUser = null;
		PreparedStatement deleteSigners = null;
		PreparedStatement deleteSignLines = null;
		PreparedStatement deleteRequests = null;

		try {
			// Se borran las tablas de la interfaz genérica involucradas del histórico
			stDwRequest = conn.prepareStatement("SELECT * FROM pf_docelweb_solicitud_spfirma WHERE solicitud_x_peticion = " + request);
			ResultSet rsDwRequest = stDwRequest.executeQuery();
			deleteGenericInterface(conn, rsDwRequest);
			rsDwRequest.close();
	
			// Se borran las firmas del histórico
			deleteSigns = conn.prepareStatement("SELECT f.x_firma " +
																	 "FROM pf_firmas f," +
																		  "pf_firmantes fir," +
																  		  "pf_lineas_firma lfir," +
																  		  "pf_peticiones pet " +
																  	 "WHERE f.fir_x_firmante = fir.x_firmante " +
																     "AND   fir.lfir_x_linea_firma = lfir.x_linea_firma " +
																     "AND   lfir.pet_x_peticion = pet.x_peticion " +
																     "AND   pet_x_peticion = " + request);
			ResultSet rsDeleteSigns = deleteSigns.executeQuery();
			deleteSigns(conn, rsDeleteSigns);
			rsDeleteSigns.close();
	
			// Se borran los documentos y los archivos
			stDocuments = conn.prepareStatement("SELECT * FROM pf_documentos WHERE pet_x_peticion = " + request);
			ResultSet rsDocuments = stDocuments.executeQuery();
			deleteDocumentsAndFiles(conn, rsDocuments);
	
			// Se borran los usuarios remitentes del histórico
			deleteSenders = conn.prepareStatement("DELETE FROM pf_usuarios_remitente WHERE pet_x_peticion = " + request);
			deleteSenders.executeUpdate();
	
			// Se borran las etiquetas petición del histórico
			deleteRequestTag = conn.prepareStatement("DELETE FROM pf_etiquetas_peticion WHERE pet_x_peticion = " + request);
			deleteRequestTag.executeUpdate();
	
			// Se borran las peticiones histórico del histórico
			deleteRequestHistoric = conn.prepareStatement("DELETE FROM pf_peticiones_historico WHERE pet_x_peticion = " + request);
			deleteRequestHistoric.executeUpdate();
	
			// Se borran las peticiones aviso del histórico
			deleteRequestNotice = conn.prepareStatement("DELETE FROM pf_peticiones_aviso WHERE pet_x_peticion = " + request);
			deleteRequestNotice.executeUpdate();
	
			// Se borran las peticiones valor del histórico
			deleteRequestValues = conn.prepareStatement("DELETE FROM pf_peticiones_valor WHERE pet_x_peticion = " + request);
			deleteRequestValues.executeUpdate();
	
			// Se borran las acciones del histórico
			deleteActions = conn.prepareStatement("DELETE FROM pf_acciones WHERE pet_x_peticion = " + request);
			deleteActions.executeUpdate();
	
			// Se borran los comentarios del histórico
			deleteCommentsUser = conn.prepareStatement("DELETE FROM PF_USUARIOS_COMENTARIO WHERE COM_X_COMENTARIO in ( select X_COMENTARIO from pf_comentarios where pet_x_peticion = "+request+" ) " );
			deleteCommentsUser.executeUpdate();
			
			// Se borran los comentarios del histórico
			deleteComments = conn.prepareStatement("DELETE FROM pf_comentarios WHERE pet_x_peticion = " + request);
			deleteComments.executeUpdate();
	
			// Se borran los firmantes del histórico
			deleteSigners = conn.prepareStatement("SELECT fir.x_firmante " +
												  "FROM pf_firmantes fir," +
												       "pf_lineas_firma lfir," +
													   "pf_peticiones pet " +
													   "WHERE fir.lfir_x_linea_firma = lfir.x_linea_firma " +
													   "AND   lfir.pet_x_peticion = pet.x_peticion " +
													   "AND   pet_x_peticion = " + request);
			ResultSet rsDeleteSigners = deleteSigners.executeQuery();
			deleteSigners(conn, rsDeleteSigners);
	
			// Se borran las líneas de firma del histórico
			deleteSignLines = conn.prepareStatement("DELETE FROM pf_lineas_firma WHERE pet_x_peticion = " + request);
			deleteSignLines.executeUpdate();
	
			// Se borran las peticiones del histórico
			deleteRequests = conn.prepareStatement("DELETE FROM pf_peticiones WHERE x_peticion = " + request);
			deleteRequests.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (stDwRequest != null) {
				stDwRequest.close();
			}
			if (deleteSigns != null) {
				deleteSigns.close();
			}
			if (stDocuments != null) {
				stDocuments.close();
			}
			if (deleteSenders != null) {
				deleteSenders.close();
			}
			if (deleteRequestTag != null) {
				deleteRequestTag.close();
			}
			if (deleteRequestHistoric != null) {
				deleteRequestHistoric.close();
			}
			if (deleteRequestNotice != null) {
				deleteRequestNotice.close();
			}
			if (deleteRequestValues != null) {
				deleteRequestValues.close();
			}
			if (deleteActions != null) {
				deleteActions.close();
			}
			if (deleteComments != null) {
				deleteComments.close();
			}
			if (deleteSigners != null) {
				deleteSigners.close();
			}
			if (deleteSignLines != null) {
				deleteSignLines.close();
			}
			if (deleteRequests != null) {
				deleteRequests.close();
			}
			
			if (conn != null){
				
				conn.close();
			}
		}

		log.debug("------------------------------------");
		log.debug("deleteRequestForever " + request + " end");
		log.debug("------------------------------------");
	}

	public List<StoredRequest> queryRequests(Map<String, String> filters, Paginator paginator) {
		List<StoredRequest> data = queryRequestByDB(filters, StorageConstants.PORTAFIRMA); 
		
		paginator.setInboxSize(data.size());
		
		int firstPosition = (paginator.getCurrentPage() - 1) * paginator.getPageSize();
		int lastPosition = firstPosition + paginator.getPageSize() > data.size() ? data.size() : firstPosition + paginator.getPageSize();
		
		return data.subList(firstPosition, lastPosition);
	}

	public List<StoredRequest> queryStoredRequests(Map<String, String> filters, Paginator paginator) {
		List<StoredRequest> data = queryRequestByDB(filters, StorageConstants.HISTORICO); 
		
		paginator.setInboxSize(data.size());
		
		int firstPosition = (paginator.getCurrentPage() - 1) * paginator.getPageSize();
		int lastPosition = firstPosition + paginator.getPageSize() > data.size() ? data.size() : firstPosition + paginator.getPageSize();
		
		return data.subList(firstPosition, lastPosition);
	}

	/**
	 * Método que devuelve las peticiones de un determinado entorno
	 * @param filters Filtros de búsqueda de peticiones
	 * @param database Base de datos
	 * @return Listado de peticiones
	 * @throws SQLException 
	 */
	private List<StoredRequest> queryRequestByDB(Map<String, String> filters, String database) {

		List<StoredRequest> requests = new ArrayList<StoredRequest>();
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			Connection conn = getConnection(database);

			// Consulta
			String query = "";
			if (database.equals(StorageConstants.PORTAFIRMA)) {
				query = "SELECT usu.d_nombre || ' ' || usu.d_apell1 || ' ' || usu.d_apell2," +
						"		app.c_aplicacion," +
						"		q1.peticion," +
						"		q1.asunto," +
						"		EXTRACT(MONTH FROM q1.fecha)," +
						"		EXTRACT(YEAR FROM q1.fecha) " +
						"FROM pf_aplicaciones app," +
						"     pf_usuarios_remitente urem," +
						"     pf_usuarios usu," +
						"     (SELECT x_peticion AS peticion," +
						"             apl_x_aplicacion AS aplicacion," +
						"             d_asunto AS asunto," +
						"             f_modificado AS fecha " +
						"      FROM pf_peticiones " +
						"      WHERE x_peticion NOT IN (SELECT DISTINCT(pet_x_peticion)" +
						"								FROM pf_etiquetas_peticion etipet, pf_etiquetas eti" +
						"                               WHERE etipet.etiq_x_etiqueta = eti.x_etiqueta" +
						"								AND   eti.c_etiqueta in ('NUEVO','LEIDO','EN ESPERA','VALIDADO'))) q1 " +
						"WHERE q1.aplicacion = app.x_aplicacion " +
						"AND   urem.usu_x_usuario = usu.x_usuario " +
						"AND   urem.pet_x_peticion = q1.peticion ";
			} else {
				query = "SELECT usu.d_nombre || ' ' || usu.d_apell1 || ' ' || usu.d_apell2," +
						"		app.c_aplicacion," +
						"		q1.peticion," +
						"		q1.asunto," +
						"		EXTRACT(MONTH FROM q1.fecha)," +
						"		EXTRACT(YEAR FROM q1.fecha) " +
						"FROM pf_aplicaciones app," +
						"     pf_usuarios_remitente urem," +
						"     pf_usuarios usu," +
						"     (SELECT DISTINCT(pet.x_peticion) AS peticion," +
						"             pet.apl_x_aplicacion AS aplicacion," +
						"             pet.d_asunto AS asunto," +
						"             pet.f_modificado AS fecha " +
						"      FROM pf_peticiones pet) q1 " +
						"WHERE q1.aplicacion = app.x_aplicacion " +
						"AND   urem.usu_x_usuario = usu.x_usuario " +
						"AND   urem.pet_x_peticion = q1.peticion ";
			}

			// Filtros
			for (String filter : filters.keySet()) {
				if (StorageConstants.APPLICATION_FILTER.equals(filter)) {
					query += "AND app.c_aplicacion LIKE '%" + filters.get(filter) +"%' ";
				} else if (StorageConstants.REMITTER_FILTER.equals(filter)) {
					query += "AND usu.x_usuario = '" + filters.get(filter) +"' "; 
				} else if (StorageConstants.MONTH_FILTER.equals(filter)) {
					query += "AND EXTRACT(MONTH FROM q1.fecha) = " + filters.get(filter) + " ";
				} else if (StorageConstants.YEAR_FILTER.equals(filter)) {
					query += "AND EXTRACT(YEAR FROM q1.fecha) = " + filters.get(filter) + " ";
				}
			}

			statement = conn.prepareStatement(query);
			rs = statement.executeQuery();

			while (rs.next()) {
				StoredRequest storagedRequest = new StoredRequest();
				storagedRequest.setRemitter((String) rs.getObject(1));
				storagedRequest.setApplication((String) rs.getObject(2));
				storagedRequest.setId(((BigDecimal) rs.getObject(3)).toString());
				storagedRequest.setSubject((String) rs.getObject(4));
				storagedRequest.setMonth(((BigDecimal) rs.getObject(5)).toString());
				storagedRequest.setYear(((BigDecimal) rs.getObject(6)).toString());
				storagedRequest.setSelected(false);
				requests.add(storagedRequest);
			}
			rs.close();

			conn.close();

		} catch (SQLException e) {
			log.error ("Error en queryRequestByDB", e);
		} catch (ClassNotFoundException e) {
			log.error ("Error en queryRequestByDB", e);			
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}				
			} catch (SQLException e) {
				log.error("Error al cerrar los statements en queryRequestByDB", e);
			}
		}

		return requests;
	}

	public List<StoredRequest> queryRequestsBySigner(Map<String, String> filters, String signerQuery, Paginator paginator) {
		List<StoredRequest> data = queryRequestsBySignerDB(filters, signerQuery, StorageConstants.PORTAFIRMA); 
		
		paginator.setInboxSize(data.size());
		
		int firstPosition = (paginator.getCurrentPage() - 1) * paginator.getPageSize();
		int lastPosition = firstPosition + paginator.getPageSize() > data.size() ? data.size() : firstPosition + paginator.getPageSize();
		
		return data.subList(firstPosition, lastPosition);
	}

	public List<StoredRequest> queryStoredRequestsBySigner(Map<String, String> filters, String signerQuery, Paginator paginator) {
		List<StoredRequest> data = queryRequestsBySignerDB(filters, signerQuery, StorageConstants.HISTORICO);
		
		paginator.setInboxSize(data.size());
		
		int firstPosition = (paginator.getCurrentPage() - 1) * paginator.getPageSize();
		int lastPosition = firstPosition + paginator.getPageSize() > data.size() ? data.size() : firstPosition + paginator.getPageSize();
		
		return data.subList(firstPosition, lastPosition);
	}


	/**
	 * Método que devuelve la lista de peticiones filtradas por firmante
	 * @param signerQuery Consulta del firmante
	 * @param database Entorno de base de datos
	 * @return Listado de peticiones
	 */
	private List<StoredRequest> queryRequestsBySignerDB(Map<String, String> filters, String signerQuery, String database) {
		List<StoredRequest> requests = new ArrayList<StoredRequest>();
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			Connection conn = getConnection(database);

			String query = "";
			if (database.equals(StorageConstants.PORTAFIRMA)) {
				query = "SELECT usu.d_nombre || ' ' || usu.d_apell1 || ' ' || usu.d_apell2," +
				  	    "       app.c_aplicacion," +
						"       q1.peticion," +
						"       q1.asunto," +
						"       EXTRACT(MONTH FROM q1.fecha)," +
						"       EXTRACT(YEAR FROM q1.fecha) " +
						"FROM pf_aplicaciones app," +
						"     pf_usuarios_remitente urem," +
						"     pf_usuarios usu," +
						"     (SELECT DISTINCT(pet.x_peticion) AS peticion," +
						"             pet.apl_x_aplicacion AS aplicacion," +
						"             pet.d_asunto AS asunto," +
						"             pet.f_modificado AS fecha " +
						"      FROM pf_peticiones pet " +
						"      JOIN pf_etiquetas_peticion etipet ON (etipet.pet_x_peticion = pet.x_peticion) " +
						"      JOIN pf_lineas_firma lfir ON (lfir.pet_x_peticion = pet.x_peticion) " +
						"      JOIN pf_firmantes fir ON (fir.lfir_x_linea_firma = lfir.x_linea_firma) " +
						"      LEFT JOIN pf_firmas f ON (f.fir_x_firmante = fir.x_firmante) " +
						"      JOIN pf_usuarios ufir ON (fir.usu_x_usuario = ufir.x_usuario) " +
						"      WHERE pet.x_peticion NOT IN (SELECT DISTINCT(pet_x_peticion)" +
						"									FROM pf_etiquetas_peticion etipet, pf_etiquetas eti" +
						"                       	        WHERE etipet.etiq_x_etiqueta = eti.x_etiqueta" +
						"									AND   eti.c_etiqueta in ('NUEVO','LEIDO','EN ESPERA','VALIDADO'))" +
						"      AND ufir.x_usuario = '" + signerQuery + "') q1 " +
						"WHERE q1.aplicacion = app.x_aplicacion " +
						"AND   urem.usu_x_usuario = usu.x_usuario " +
						"AND   urem.pet_x_peticion = q1.peticion ";
			} else {
				query = "SELECT usu.d_nombre || ' ' || usu.d_apell1 || ' ' || usu.d_apell2," +
				  	    "       app.c_aplicacion," +
						"       q1.peticion," +
						"       q1.asunto," +
						"       EXTRACT(MONTH FROM q1.fecha)," +
						"       EXTRACT(YEAR FROM q1.fecha) " +
						"FROM pf_aplicaciones app," +
						"     pf_usuarios_remitente urem," +
						"     pf_usuarios usu," +
						"     (SELECT DISTINCT(pet.x_peticion) AS peticion," +
						"             pet.apl_x_aplicacion AS aplicacion," +
						"             pet.d_asunto AS asunto," +
						"             pet.f_modificado AS fecha " +
						"      FROM pf_peticiones pet " +
						"      JOIN pf_etiquetas_peticion etipet ON (etipet.pet_x_peticion = pet.x_peticion) " +
						"      JOIN pf_lineas_firma lfir ON (lfir.pet_x_peticion = pet.x_peticion) " +
						"      JOIN pf_firmantes fir ON (fir.lfir_x_linea_firma = lfir.x_linea_firma) " +
						"      LEFT JOIN pf_firmas f ON (f.fir_x_firmante = fir.x_firmante) " +
						"      JOIN pf_usuarios ufir ON (fir.usu_x_usuario = ufir.x_usuario) " +
						"      WHERE ufir.x_usuario = '" + signerQuery + "') q1 " +
						"WHERE q1.aplicacion = app.x_aplicacion " +
						"AND   urem.usu_x_usuario = usu.x_usuario " +
						"AND   urem.pet_x_peticion = q1.peticion ";
			}

			// Filtros
			for (String filter : filters.keySet()) {
				if (StorageConstants.APPLICATION_FILTER.equals(filter)) {
					query += "AND app.c_aplicacion LIKE '%" + filters.get(filter) +"%' ";
				} else if (StorageConstants.REMITTER_FILTER.equals(filter)) {
					query += "AND usu.x_usuario = '" + filters.get(filter) +"' "; 
				} else if (StorageConstants.MONTH_FILTER.equals(filter)) {
					query += "AND EXTRACT(MONTH FROM q1.fecha) = " + filters.get(filter) + " ";
				} else if (StorageConstants.YEAR_FILTER.equals(filter)) {
					query += "AND EXTRACT(YEAR FROM q1.fecha) = " + filters.get(filter) + " ";
				}
			}

			statement = conn.prepareStatement(query);
			rs = statement.executeQuery();

			while (rs.next()) {
				StoredRequest storagedRequest = new StoredRequest();
				storagedRequest.setRemitter((String) rs.getObject(1));
				storagedRequest.setApplication((String) rs.getObject(2));
				storagedRequest.setId(((BigDecimal) rs.getObject(3)).toString());
				storagedRequest.setSubject((String) rs.getObject(4));
				storagedRequest.setMonth(((BigDecimal) rs.getObject(5)).toString());
				storagedRequest.setYear(((BigDecimal) rs.getObject(6)).toString());
				storagedRequest.setSelected(false);
				requests.add(storagedRequest);
			}
			
			rs.close();

			conn.close();

		} catch (SQLException e) {
			log.error ("error en queryRequestsBySignerDB", e);			
		} catch (ClassNotFoundException e) {
			log.error ("error en queryRequestsBySignerDB", e);
		}  finally {
			try {
				if (statement != null) {
					statement.close();
				}				
			} catch (SQLException e) {
				log.error("Error al cerrar los statements en queryRequestsBySignerDB", e);
			}
		}

		return requests;
	}

	// Obtiene la conexión
	private Connection getConnection(String entorno) throws SQLException, ClassNotFoundException {
		return storageDBConnectionManager.getConnection(entorno);
	}
	
	public Long findIdRequestBySign(Long idSign) throws ClassNotFoundException, SQLException {
		PreparedStatement statement = null;
		ResultSet rs = null;
		Long retorno = null;
		Connection conn = null;
		try {
			conn = getConnection(StorageConstants.HISTORICO);
			
			String query = "SELECT doc.PET_X_PETICION "
					+ " FROM PF_FIRMAS firma"
					+ " JOIN PF_DOCUMENTOS doc on (doc.X_DOCUMENTO = firma.DOC_X_DOCUMENTO) "
					+ " WHERE firma.X_FIRMA = ?";
			
			statement = conn.prepareStatement(query);
			
			statement.setLong(1, idSign);
			
			rs = statement.executeQuery();

			while (rs.next()) {
				retorno = rs.getLong(1);
			}
			
			return retorno;
		} catch (ClassNotFoundException e) {
			log.error ("error en findIdRequestBySign", e);		
			throw e;
		} catch (SQLException e) {
			log.error ("error en findIdRequestBySign", e);	
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				
				if (statement != null) {
					statement.close();
				}
				
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				log.error("Error al cerrar los statements en queryRequestsBySignerDB", e);
				throw e;
			}
		}
	}
	
	public RequestTagListDTO getSignsPaginatedByValidDate(Date validDate, int firstPosition, int maxResults) throws ClassNotFoundException, SQLException {
		PreparedStatement statementData = null;
		ResultSet rsData = null;
		
		PreparedStatement statementCount = null;
		ResultSet rsCount = null;
		
		RequestTagListDTO retorno = null;
		Connection conn = null;
		
		long totalRecords = 0;
		try {
			conn = getConnection(StorageConstants.HISTORICO);
			
			String queryCount = "SELECT COUNT(*)"
					+ " FROM PF_FIRMAS firma"
					+ " JOIN PF_DOCUMENTOS doc on (doc.X_DOCUMENTO = firma.DOC_X_DOCUMENTO) "
					+ " JOIN PF_PETICIONES pet on (pet.X_PETICION = doc.PET_X_PETICION)"
					+ " JOIN PF_FIRMANTES firmante on (firmante.X_FIRMANTE = firma.FIR_X_FIRMANTE)"
					+ " JOIN PF_USUARIOS usuario on (usuario.X_USUARIO = firmante.USU_X_USUARIO)"
					+ " WHERE firma.F_VALIDO BETWEEN ? AND ?"
					+ " ORDER BY firma.F_VALIDO asc";
			statementCount = conn.prepareStatement(queryCount);
			statementCount.setDate(1, new java.sql.Date(new Date().getTime()));
			statementCount.setDate(2, new java.sql.Date(validDate.getTime()));
			rsCount = statementCount.executeQuery();
			while (rsCount.next()) {
				totalRecords = rsCount.getLong(1);
			}
			
			String query = "SELECT numLinea, X_FIRMA, D_ASUNTO, D_NOMBRE, F_VALIDO, U_NOMBRE,D_APELL1 FROM "
					+ "(SELECT rownum numLinea, firma.X_FIRMA X_FIRMA, pet.D_ASUNTO D_ASUNTO,doc.D_NOMBRE D_NOMBRE, firma.F_VALIDO F_VALIDO, usuario.D_NOMBRE U_NOMBRE, usuario.D_APELL1 D_APELL1"
					+ " FROM PF_FIRMAS firma"
					+ " JOIN PF_DOCUMENTOS doc on (doc.X_DOCUMENTO = firma.DOC_X_DOCUMENTO) "
					+ " JOIN PF_PETICIONES pet on (pet.X_PETICION = doc.PET_X_PETICION)"
					+ " JOIN PF_FIRMANTES firmante on (firmante.X_FIRMANTE = firma.FIR_X_FIRMANTE)"
					+ " JOIN PF_USUARIOS usuario on (usuario.X_USUARIO = firmante.USU_X_USUARIO)"
					+ " WHERE firma.F_VALIDO BETWEEN ? AND ?"
					+ " ORDER BY firma.F_VALIDO asc, pet.D_ASUNTO asc, doc.D_NOMBRE asc)"
					+ "WHERE numLinea > ? AND numLinea <= ?";
			
			statementData = conn.prepareStatement(query);
			statementData.setDate(1, new java.sql.Date(new Date().getTime()));
			statementData.setDate(2, new java.sql.Date(validDate.getTime()));
			statementData.setInt(3, firstPosition);
			statementData.setInt(4, maxResults);
			
			rsData = statementData.executeQuery();

			PfSignsDTO firmaDTO = null;
			List<AbstractBaseDTO> firmasDTO = new ArrayList<AbstractBaseDTO>();
			while (rsData.next()) {
				firmaDTO = new PfSignsDTO();
				firmaDTO.setPrimaryKey(rsData.getLong(2));
				
				PfRequestsDTO peticionDTO = new PfRequestsDTO();
				peticionDTO.setDsubject(rsData.getString(3));
				
				PfDocumentsDTO documentDTO = new PfDocumentsDTO();
				documentDTO.setPfRequest(peticionDTO);
				documentDTO.setDname(rsData.getString(4));
				firmaDTO.setPfDocument(documentDTO);
				
				firmaDTO.setfValid(rsData.getDate(5));
				
				PfSignersDTO signerDTO = new PfSignersDTO();
				PfUsersDTO userDTO = new PfUsersDTO();
				userDTO.setDname(rsData.getString(6));
				userDTO.setDsurname1(rsData.getString(7));
				signerDTO.setPfUser(userDTO);
				firmaDTO.setPfSigner(signerDTO);
				
				firmasDTO.add(firmaDTO);
			}
			
			retorno = new RequestTagListDTO(firmasDTO, totalRecords);
			
			return retorno;
		} catch (ClassNotFoundException e) {
			log.error ("error en findIdRequestBySign", e);		
			throw e;
		} catch (SQLException e) {
			log.error ("error en findIdRequestBySign", e);	
			throw e;
		} finally {
			try {
				if (rsCount != null) {
					rsCount.close();
				}
				
				if (statementCount != null) {
					statementCount.close();
				}
				
				if (rsData != null) {
					rsData.close();
				}
				
				if (statementData != null) {
					statementData.close();
				}
				
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				log.error("Error al cerrar los statements en queryRequestsBySignerDB", e);
				throw e;
			}
		}
	}

}
