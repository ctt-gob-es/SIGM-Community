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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.exceptions.CSVNotFoundException;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ServletHelperBO {
	
	@Autowired 
	private BinaryDocumentsBO binaryDocumentsBO;
	
	@Autowired
	private CSVBO csvBO;
	
	/**
	 * Se encarga de obtener el documento y meterlo en el response.
	 * @param documentDTO
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 */
	@Transactional (readOnly=false)
	public byte[] obtenerDocumento (PfDocumentsDTO documentDTO) throws DocumentCantBeDownloadedException {
		return binaryDocumentsBO.getDocumentByDocumentDTO(documentDTO);
	}
	
	/**
	 * Se encarga de obtener la firma de un documento y meterla en el response.
	 * @param signDTO
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 */	
	@Transactional (readOnly=false)
	public byte[] obtenerFirma (PfSignsDTO signDTO) throws DocumentCantBeDownloadedException {
		return binaryDocumentsBO.getSignatureBySignDTO(signDTO);
	}
	
	/**
	 * Se encarga de obtener el informe de la firma de un documento y meterla en el response.
	 * @param signDTO
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 */
	@Transactional (readOnly=false)
	public byte[] obtenerInforme (PfSignsDTO signDTO, PfApplicationsDTO peticionario) throws DocumentCantBeDownloadedException {	
		return binaryDocumentsBO.getReportBySignDTO(signDTO, peticionario);
	}
	
	/**
	 * Se encarga de regenerar el informe de la firma de un documento.
	 * @param signDTO
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 */
	@Transactional (readOnly=false)
	public byte[] regenerarInforme (PfSignsDTO signDTO) throws DocumentCantBeDownloadedException {	
		return binaryDocumentsBO.getRegenerarReportBySignDTO(signDTO, signDTO.getPfDocument().getPfRequest().getPfApplication());
	}
	
	/**
	 * Se encarga de obtener el informe normalizado de la firma de un documento y meterla en el response.
	 * @param signDTO
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 */
	@Transactional (readOnly=false)
	public byte[] obtenerInformeNormalizado (PfSignsDTO signDTO) throws DocumentCantBeDownloadedException {	
		return binaryDocumentsBO.getNormalizedReportBySignDTO(signDTO, signDTO.getPfDocument().getPfRequest().getPfApplication());
	}
	
	/**
	 * Se encarga de obtener el ZIP con los informes de una lista de peticiones y meterlo en el response.
	 * @param documentHash
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 */
	@Transactional (readOnly=false) 
	public byte[] obtenerZipConInformes (String[] requestTagHashList, PfApplicationsDTO peticionario) throws DocumentCantBeDownloadedException {
		List<AbstractBaseDTO> documentDTOList = binaryDocumentsBO.getDocumentDTOListByRequestTagHashes (requestTagHashList);
		return binaryDocumentsBO.getZipByDocumentsDTO (documentDTOList, peticionario);
	}

	/**
	 * Se encarga de obtener el informe y meterlo en el response.
	 * @param docDTO
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 */
	@Transactional (readOnly=false) 
	public byte[] obtenerInforme (PfDocumentsDTO docDTO, PfApplicationsDTO peticionario, String nombreDeArchivoDeUsuario) throws DocumentCantBeDownloadedException {		
		PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash (docDTO.getChash());	
		return binaryDocumentsBO.getReportBySignDTO(signDTO, peticionario);
	}
	
	/**
	 * Obtiene el informe asociado a un csv y lo mete en el response.
	 * @param csv
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 * @throws CSVNotFoundException
	 */
	@Transactional (readOnly=false)
	public byte[] obtenerInformePorCSV  (PfSignsDTO signDTO, PfApplicationsDTO peticionario, boolean normalizado) throws DocumentCantBeDownloadedException, CSVNotFoundException {	
		return csvBO.getInformeByCSV(signDTO, peticionario, normalizado);
	}
	
	
	/**
	 * Obtiene la visualizacion de una facturae y la mete en el response.
	 * @param documentHash
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 */
	@Transactional (readOnly=false)
	public byte[] obtenerFacturae  (String documentHash) throws DocumentCantBeDownloadedException {	
		return binaryDocumentsBO.getFacturaeByDocumentHash(documentHash);
	}
	
	/**
	 * Obtiene la visualizacion de una firma previa de un documento y la mete en el response.
	 * @param documentHash
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 */
	@Transactional (readOnly=false)
	public byte[] obtenerPrevisualizacion  (PfDocumentsDTO documentDTO, PfApplicationsDTO peticionario) throws DocumentCantBeDownloadedException {	
		return binaryDocumentsBO.getPrevisualizacionByDocumentDTO(documentDTO, peticionario);
	}
	
	/**
	 * Obtiene la visualizacion del TCN de un documento y la mete en el response.
	 * @param documentDTO
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 */
	@Transactional (readOnly=false)
	public byte[] obtenerVisualizacionTCN  (PfDocumentsDTO documentDTO) throws DocumentCantBeDownloadedException {	
		return binaryDocumentsBO.getTCNVisualizacionByDocumentDTO(documentDTO);
	}
	

}
