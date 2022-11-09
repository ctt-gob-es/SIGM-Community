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

package es.seap.minhap.portafirmas.business;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.beans.CSVInfo;
import es.seap.minhap.portafirmas.business.beans.binarydocuments.BinaryDocument;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.exceptions.CSVNotFoundException;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;
import es.seap.minhap.portafirmas.storage.dao.JdbcRequestStorageDAO;
import es.seap.minhap.portafirmas.storage.util.StorageConstants;
import es.seap.minhap.portafirmas.storage.util.StorageDBConnectionManager;
import es.seap.minhap.portafirmas.utils.Util;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)

public class CSVBO {
	
	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private StorageDBConnectionManager storageDBConnectionManager;
	
	@Autowired
	private JdbcRequestStorageDAO jdbcRequestStorageDAO;
	
	@Autowired
	private BinaryDocumentsBO binaryDocumentsBO;
	
	@Autowired
	private RequestBO requestBO;


	/**
	 * Devuelve el informe asociado a un CSV, y lo escribe en output
	 * @param csv
	 * @param scopeString
	 * @param output
	 * @return
	 * @throws CSVNotFoundException Si no se encuentra el csv asociado al ámbito
	 * @throws DocumentCantBeDownloadedException Si no se puede generar el informe.
	 */
	public byte[] getInformeByCSV (PfSignsDTO signDTO,
											PfApplicationsDTO peticionario, boolean normalizado) throws CSVNotFoundException, DocumentCantBeDownloadedException {
		
		String csv = "";
		if (normalizado){
			csv = signDTO.getCsvNormalizado();
		} else {
			csv = signDTO.getCsv();
		}
		CSVInfo storageRequest = checkCSVExists (csv, normalizado);
		
		byte[] bytes = binaryDocumentsBO.getReportBySignDTO(signDTO, peticionario, normalizado);
		
		checkMoveToHistorico (storageRequest);
		
		return bytes;
		
	}
	
	/**
	 * Devuelve información sobre el csv y ámbito consultados. Si está en el histórico, lo mueve a la BBDD central.
	 * @param csv
	 * @param scopeString
	 * @return
	 * @throws DocumentCantBeDownloadedException
	 */
	private CSVInfo getStorageRequestDTOPorCSV (String csv, boolean normalizado) throws DocumentCantBeDownloadedException, CSVNotFoundException{
		CSVInfo storageRequest = new CSVInfo ();
		
		// Recuperamos la firma de la BBDD primaria
		PfSignsDTO signDTO = null;
		if (normalizado) {
			signDTO = getSignDTOPorCSVNormalizadoBBDDPrimaria (csv);
		} else {
			signDTO = getSignDTOPorCSVBBDDPrimaria (csv);	
		}
		
		
		// Si existe, la devolvemos
		if (signDTO != null) {
			
			//Si la firma se encuentra anulada, devolvemos un error de que el documento no ha sido encontrado.
			PfRequestsDTO request = signDTO.getPfDocument().getPfRequest();
	    	List<PfRequestTagsDTO> reqTags = requestBO.getAnulledRequestTags(request);

	    	if(!Util.esVacioONulo(reqTags)){
	    		throw new CSVNotFoundException(csv);
	    	}
			
			storageRequest.setFoundInBBDDPortafirmas(true);
		}
				
		String requestIdHistorico = null;
		
		// Si no existe, comprobamos si el histórico está activado, e intentamos recuperarlo.
		if (signDTO == null) {
			boolean historicoActivado = applicationBO.historicoActivado();
			
			if (historicoActivado) {
				try {
					requestIdHistorico = getDocumentSignFromStorage (csv);
				} catch (Exception e) {
					throw new DocumentCantBeDownloadedException ("Error al intentar recuperar la firma del histórico");
				}
			}
			// Si lo encontramos, metemos la información en e objeto de retorno.
			if (requestIdHistorico != null) {				
				storageRequest.setIdRequestHistorico(requestIdHistorico);
				storageRequest.setFoundInBBDDPortafirmas(false);
			}
			
		}
		return storageRequest;
	}
	
	/**
	 * Devuelve la firma asociada a un csv y un ámbito.
	 * @param csv
	 * @param scope
	 * @return
	 */
	private PfSignsDTO getSignDTOPorCSVBBDDPrimaria (String csv) {
		
		Map<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("csv", csv);
		PfSignsDTO signDTO = (PfSignsDTO) baseDAO.queryElementMoreParameters("request.signByCsv", parameters);
		return signDTO;
	}
	
	/**
	 * Devuelve la firma asociada a un csv y un ámbito.
	 * @param csv
	 * @param scope
	 * @return
	 */
	private PfSignsDTO getSignDTOPorCSVNormalizadoBBDDPrimaria (String csv) {
		
		Map<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("csv", csv);
		PfSignsDTO signDTO = (PfSignsDTO) baseDAO.queryElementMoreParameters("request.signByCsvNormalizado", parameters);
		return signDTO;
	}
	
	/**
	 * Método que busca una firma en la base de datos de histórico a partir de su CSV
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	private String getDocumentSignFromStorage(String csv) throws SQLException, ClassNotFoundException {

		String requestId = null;

		// Se busca la petición que contiene la firma en el histórico
		Connection conn = storageDBConnectionManager.getConnection(StorageConstants.HISTORICO);
		PreparedStatement statement = conn.prepareStatement("SELECT pet.x_peticion " +
															"FROM pf_peticiones pet," +
															"     pf_lineas_firma lfir," +
															"	  pf_firmantes fir," +
															"	  pf_firmas f, " +
															"	  pf_documentos d " +
															"WHERE f.fir_x_firmante = fir.x_firmante " +
															"AND   fir.lfir_x_linea_firma = lfir.x_linea_firma " +
															"AND   lfir.pet_x_peticion = pet.x_peticion " +
															"AND   f.csv = '" + csv + "'");
		ResultSet result = statement.executeQuery();

		// Si está, se mueve temporalmente a Portafirmas
		if (result.next()) {
			List<String> requests = new ArrayList<String>();
			requestId = result.getBigDecimal(1).toString();
			requests.add(requestId);
			jdbcRequestStorageDAO.returnFromStorage(requests);
		}

		return requestId;
	}
	
	/**
	 * Comprueba si ha de moverse al histórico, y de ser así, lo mueve.
	 * @param storageRequest
	 * @throws DocumentCantBeDownloadedException
	 */
	private void checkMoveToHistorico (CSVInfo storageRequest) throws DocumentCantBeDownloadedException{
		if (storageRequest.getIdRequestHistorico() != null) {
			List<String> requests = new ArrayList<String>();
			requests.add(storageRequest.getIdRequestHistorico());
			try {
				jdbcRequestStorageDAO.moveToStorage(requests);
			} catch (Throwable t) {
				throw new DocumentCantBeDownloadedException ("No se puede retornar la petición al histórico", t, storageRequest.getIdRequestHistorico());
			}
		}
	}
	
	/**
	 * Comprueba si existe un CSV en la BBDd primaria o bien en el histórico. 
	 * Si está en el histórico, lo mueve a la BBDD primaria.
	 * @param csv
	 * @param scopeString
	 * @return
	 * @throws CSVNotFoundException
	 * @throws DocumentCantBeDownloadedException
	 */
	private CSVInfo checkCSVExists (String csv, boolean normalizado) throws CSVNotFoundException, DocumentCantBeDownloadedException {
		CSVInfo storageRequest =  getStorageRequestDTOPorCSV (csv, normalizado);
		if (!storageRequest.isFoundInBBDDPortafirmas() && storageRequest.getIdRequestHistorico() == null) {
			throw new CSVNotFoundException(csv);
		}
		return storageRequest;
	}

}
