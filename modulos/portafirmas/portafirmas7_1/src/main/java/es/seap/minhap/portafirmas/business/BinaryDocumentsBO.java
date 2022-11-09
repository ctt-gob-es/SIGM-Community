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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.beans.binarydocuments.BinaryDocumentReport;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.SignData;
import es.seap.minhap.portafirmas.utils.SignDataUtil;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.ZipUtil;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputReport;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;
import es.seap.minhap.portafirmas.utils.facturae.FacturaeBO;
import es.seap.minhap.portafirmas.utils.previsualizacion.PrevisualizacionBO;
import es.seap.minhap.portafirmas.utils.tcn.VisualizacionTCNBO;
import es.seap.minhap.portafirmas.web.beans.FileAttachedDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class BinaryDocumentsBO  {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private CustodyServiceFactory custodyServiceFactory;
	
	@Autowired
	private ReportBO reportBO;
	
	@Autowired
	private FacturaeBO facturaeBO;
	
	@Autowired
	private PrevisualizacionBO previsualizacionBO;
	
	@Autowired
	private VisualizacionTCNBO visualizacionTCNBO;
	
	@Autowired
	UtilComponent utilComponent;
	
	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private SignDataUtil signDataUtil;

	/**
	 * Devuelve un documento
	 * @param documentDTO documento
	 * @return
	 * @throws DocumentCantBeDownloadedException si no se puede obtener el documento.
	 */
	public byte[] getDocumentByDocumentDTO (PfDocumentsDTO documentDTO) throws DocumentCantBeDownloadedException{
		byte[] bytes = null;		
		CustodyServiceOutput custodyService;
		try {
			custodyService = custodyServiceFactory
							.createCustodyServiceOutput(documentDTO.getPfFile().getCtype());
			
			CustodyServiceOutputDocument custodyDocument = new CustodyServiceOutputDocument();
			custodyDocument.setIdentifier(documentDTO.getChash());
			custodyDocument.setUri(documentDTO.getPfFile().getCuri());
			custodyDocument.setIdEni(documentDTO.getPfFile().getIdEni());
			custodyDocument.setRefNasDir3(documentDTO.getPfFile().getRefNasDir3());
			bytes = custodyService.downloadFile(custodyDocument);
		} catch (CustodyServiceException e) {
			throw new DocumentCantBeDownloadedException ("No se puede descargar el documento", e, documentDTO.getChash());
		}		
		
		// Este será el objeto que se devolverá, con un outputstream que contenga el contenido del documento.
		return bytes;

	}
	
	/**
	 * Devuelve una firma
	 * @param signDTO dto de la firma
	 * @return
	 * @throws DocumentCantBeDownloadedException
	 */
	public byte[] getSignatureBySignDTO (PfSignsDTO signDTO) throws DocumentCantBeDownloadedException {
		byte[] bytes = null;
		if (signDTO == null) {
			return null;
		}
		
		CustodyServiceOutputSign custodySign = new CustodyServiceOutputSign();
		String storageType = signDTO.getCtype();
		custodySign.setType(Constants.SIGN_TYPE_SERVER);
		custodySign.setIdentifier(signDTO.getPrimaryKeyString());
		custodySign.setUri(signDTO.getCuri());
		custodySign.setIdEni(signDTO.getRefNASIdEniFirma());
		custodySign.setRefNasDir3(signDTO.getRefNASDir3Firma());
		
		try {
			CustodyServiceOutput custodyService = custodyServiceFactory.createCustodyServiceOutput(storageType);		
			bytes = custodyService.downloadSign(custodySign);
		} catch (CustodyServiceException e) {
			throw new DocumentCantBeDownloadedException ("No se puede descargar la firma", e);
		}
		
		return bytes;
	}
	
	public byte[] getReportBySignDTO (PfSignsDTO signDTO, PfApplicationsDTO peticionario) throws DocumentCantBeDownloadedException {
		return getReportBySignDTO(signDTO, false, false, peticionario);
	}
	
	public byte[] getReportBySignDTO (PfSignsDTO signDTO, PfApplicationsDTO peticionario, boolean normalizado) throws DocumentCantBeDownloadedException {
		return getReportBySignDTO(signDTO, normalizado, false, peticionario);
	}
	
	public byte[] getRegenerarReportBySignDTO (PfSignsDTO signDTO, PfApplicationsDTO peticionario) throws DocumentCantBeDownloadedException {
		return getReportBySignDTO(signDTO, false, true, peticionario);
	}
	
	public byte[] getNormalizedReportBySignDTO (PfSignsDTO signDTO, PfApplicationsDTO peticionario) throws DocumentCantBeDownloadedException {
		return getReportBySignDTO(signDTO, true, false, peticionario);
	}
	
	private byte[] getReportBySignDTO (PfSignsDTO signDTO, boolean normalized, boolean regenerar, PfApplicationsDTO peticionario) throws DocumentCantBeDownloadedException {
		
		BinaryDocumentReport binaryReport = new BinaryDocumentReport ();
		binaryReport.setName(getNombreReport(signDTO.getPfDocument().getDname()));
		binaryReport.setMime(Constants.PDF_MIME);
		byte[] bytes = null;
		
		CustodyServiceOutputReport custodyReport = new CustodyServiceOutputReport();
		custodyReport.setIdentifier(signDTO.getPrimaryKeyString());
		if(!normalized) {
			try {
				custodyReport.setCsv(signDTO.getCsv());
				custodyReport.setRefNasDir3(signDTO.getRefNASDir3Informe());
				CustodyServiceOutput service = custodyServiceFactory.createCustodyServiceOutput(signDTO.getcTipoInforme());
				if (service != null){
					bytes = service.downloadReport(custodyReport);
				}
			} catch (CustodyServiceException e) {
				log.error("ERROR: BinaryDocumentsBO.getReportOutputStreamBySignDTO: no se puede descargar el informe para la firma con PK: " + 
						signDTO.getPrimaryKeyString() + " y documento con cHash: " + signDTO.getPfDocument().getChash() + ".", e);
				throw new DocumentCantBeDownloadedException ("No se puede descargar el informe para la firma", e, signDTO.getPrimaryKeyString());
			}
		} else {
			try {
				custodyReport.setCsv(signDTO.getCsvNormalizado());
				custodyReport.setRefNasDir3(signDTO.getRefNASDir3InfNormalizado());
				CustodyServiceOutput service = custodyServiceFactory.createCustodyServiceOutput(signDTO.getcTipoInformeNormalizado());
				if (service != null){
					bytes = service.downloadReport(custodyReport);
				}
			} catch (CustodyServiceException e) {
				log.error("ERROR: BinaryDocumentsBO.getReportOutputStreamBySignDTO: no se puede descargar el informe normalizado para la firma con PK: " + 
						signDTO.getPrimaryKeyString() + " y documento con cHash: " + signDTO.getPfDocument().getChash() + ".", e);
				throw new DocumentCantBeDownloadedException ("No se puede descargar el informe normalizado para la firma", e, signDTO.getPrimaryKeyString());
			}	
		}
		
		if (bytes == null || regenerar) {
			byte[] bytesFirma = getSignatureBySignDTO (signDTO);
			byte[] bytesDocument = getDocumentByDocumentDTO (signDTO.getPfDocument());
				
			byte[] documentBytes = bytesDocument;
			String mimeDocument = signDTO.getPfDocument().getDmime();
			
			try {
				if (signDTO.getPfDocument().getLissign()) {
					SignData datosFirma = signDataUtil.getDataFromSign(bytesDocument);
					documentBytes = datosFirma.getDocument();
					// Si el mime devuelto por Afirma es "application/octet-stream", lo ponemos a nulo, para que el servicio eeutil lo averigüe por su cuenta.
					if (datosFirma.getTipoMime().contentEquals(Constants.AFIRMA_MIME_GENERICO)) {
						mimeDocument = null;
					} else {
						mimeDocument = datosFirma.getTipoMime();				
					}
				}
				
				if(mimeDocument!=null && mimeDocument.equalsIgnoreCase("application/rtf")){
					mimeDocument = "text/rtf";
				}
				
				if(normalized) {
					bytes = reportBO.generarNormalizedReport(
							   bytesFirma, 
							   documentBytes, 
							   mimeDocument, 
							   peticionario,
							   signDTO);

				} else {
					log.debug("Generando informe para el documento con id: " + 
				signDTO.getPfDocument().getPrimaryKey() + ", y cHash: " + signDTO.getPfDocument().getChash());
					bytes = reportBO.generarReport(
							   bytesFirma,
							   documentBytes, 
							   mimeDocument, 
							   peticionario,
							   signDTO);
				}
				
			} catch (Exception e) {
				log.error("ERROR: No se puede obtener el informe del documentocon id: " + 
				signDTO.getPfDocument().getPrimaryKey() + ", y cHash: " + signDTO.getPfDocument().getChash() + ",", e);
				throw new DocumentCantBeDownloadedException ("No se puede obtener el informe", e, signDTO.getPfDocument().getPrimaryKeyString());
			}
		}
		
		
		return bytes;
		
	}
	
	public byte[] getZipByDocumentsDTO (List<AbstractBaseDTO> documentDTOList, PfApplicationsDTO peticionario) throws DocumentCantBeDownloadedException {
		ZipUtil zipUtil = new ZipUtil();

		//String fileZipName = Constants.PATH_TEMP + Constants.TEMP_FILE_PREFIX + "_" + System.currentTimeMillis() + "_" + ".zip";
		
		ZipOutputStream zos = null;
		
		byte [] binaryZip = null;
		
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			zos = new ZipOutputStream(output);
			for (AbstractBaseDTO abs : documentDTOList) {
				PfDocumentsDTO docDTO = (PfDocumentsDTO) abs;
				PfSignsDTO signDTO = getSignDTOByDocumentHash (docDTO.getChash());
				byte[] bytes = getReportBySignDTO(signDTO, peticionario);
			
				ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
			
				try {
					zipUtil.addFile(bis, getNombreReport(signDTO.getPfDocument().getDname()), zos);
				} catch (IOException ioe) {
					log.error("ERROR: Adding file to ZIP. BinaryDocumentsBO.getZipOutputStreamByDocumentsDTO del documento con id " +
					docDTO.getPrimaryKey() + ", y cHash " + docDTO.getChash(),  ioe); 
					String entryName = Util.getInstance().appendHashToFileName(getNombreReport(signDTO.getPfDocument().getDname()), docDTO.getChash());
					zipUtil.addFile(bis, entryName, zos);
				}
			}
			zipUtil.closeZip(zos);
			binaryZip = output.toByteArray();
			
		}catch (DocumentCantBeDownloadedException|IOException e) {
			log.error("ERROR: BinaryDocumentsBO.getZipOutputStreamByDocumentsDTO: ", e);
			throw new DocumentCantBeDownloadedException ("No se puede generar el ZIP con los informes", e);
		}
		
		return binaryZip;
		
	}
	
	/**
	 * Devuelve un documento de visualización de una facturae, con el nombre, mime y outputstream relleno (contenido de la facturae).
	 * @param docHash hash del documento
	 * @return
	 * @throws DocumentCantBeDownloadedException
	 */
	public byte[] getFacturaeByDocumentHash (String docHash) throws DocumentCantBeDownloadedException {
		
		PfDocumentsDTO documentDTO = getDocumentDTOByHash (docHash);
		byte[] bytes = this.getDocumentByDocumentDTO(documentDTO);
		byte[] facturae = null;
		try {
			facturae = facturaeBO.generateFacturae(bytes);
		} catch (Exception e) {
			throw new DocumentCantBeDownloadedException ("No se puede generar la facturae", e, docHash);
		}
		return facturae;
		
		
	}
	
	/**
	 * Devuelve un documento de visualización de un documento que tiene una firma previa
	 * @param docDTO dto del documento
	 * @return
	 * @throws DocumentCantBeDownloadedException
	 */
	public byte[] getPrevisualizacionByDocumentDTO (PfDocumentsDTO docDTO, PfApplicationsDTO peticionario) throws DocumentCantBeDownloadedException {
		
		byte[] bytes = this.getDocumentByDocumentDTO(docDTO);
		ByteArrayOutputStream bos = new ByteArrayOutputStream ();
		
		try {
			previsualizacionBO.previsualizacion(new ByteArrayInputStream (bytes), bos, peticionario);
		} catch (Exception e) {
			log.error("No se puede obtener la previsualización, ", e);
			throw new DocumentCantBeDownloadedException ("No se puede obtener la previsualización", e, docDTO.getChash());
		}
		return bos.toByteArray();
	}
	
	/**
	 * Devuelve un documento de la visualización de un TCN
	 * @param docDTO dto del documento
	 * @return
	 * @throws DocumentCantBeDownloadedException
	 */
	public byte[] getTCNVisualizacionByDocumentDTO(PfDocumentsDTO docDTO) throws DocumentCantBeDownloadedException  {
		
		byte[] bytes =  this.getDocumentByDocumentDTO(docDTO);
		
		try {
			return visualizacionTCNBO.visualizarTCN(bytes);
		} catch (Exception e) {
			throw new DocumentCantBeDownloadedException ("No se puede obtener la visualizacion del TCN", e, docDTO.getChash());
		}
		
		
		
	}
	
	/**
	 * Devuelve un documento de un File
	 * @param file
	 * @return
	 * @throws DocumentCantBeDownloadedException
	 */
	public byte [] getFileFromServerByFile(File file) throws DocumentCantBeDownloadedException  {
		byte [] bytes = null;
		try {
			
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			bytes = IOUtils.toByteArray(in);
			in.close();
			
		} catch (IOException e) {
			throw new DocumentCantBeDownloadedException ("No se puede descargar el documento del servidor", e, file.getAbsolutePath());
		}
		return bytes;
		
	}
	
	
	/**
	 * Obtiene todos los documentos asociados a una serie de peticiones.
	 * @param reqHashes
	 * @return
	 */
	public List<AbstractBaseDTO> getDocumentDTOListByRequestTagHashes (String[] reqTagHashes) {
		if (reqTagHashes == null || reqTagHashes.length == 0) {
			throw new IllegalArgumentException ("reqHashes no puede ser nulo");
		}
		List<String> reqTagHashesList = new ArrayList<String>();
		reqTagHashesList = Arrays.asList(reqTagHashes);
		Map<String, Object> queryParams = new HashMap<String, Object>();		
		queryParams.put("requestTagHashList", reqTagHashesList);
		return baseDAO.queryListMoreParameters("request.documentsAllByRequestTagHashList", queryParams);
		
	}
	
	/**
	 * Última firma realizada sobre un documento
	 * @param docHash
	 * @return
	 * @throws DocumentCantBeDownloadedException
	 */
	public PfSignsDTO getSignDTOByDocumentHash (String docHash) throws DocumentCantBeDownloadedException {
		if (docHash == null || "".contentEquals(docHash)) {
			log.error("ERROR: BinaryDocumentsBO.getSignDTOByDocumentHash: docHash no puede ser nulo.");
			throw new IllegalArgumentException ("docHash no puede ser nulo");
		}
		
		PfSignsDTO signDTO = custodyServiceFactory.signFileQuery(docHash);
		return signDTO;
	}
	
	public PfSignsDTO getSignDTOByCsv (String csv) throws DocumentCantBeDownloadedException {
		if (csv == null || "".contentEquals(csv)) {
			throw new IllegalArgumentException ("docHash no puede ser nulo");
		}
		
		PfSignsDTO signDTO = (PfSignsDTO) baseDAO.queryElementOneParameter("request.signByCsv", "csv", csv);
		
		return signDTO;
	}
	
	public PfSignsDTO getSignDTOByCsvNormalizado (String csv) throws DocumentCantBeDownloadedException {
		if (csv == null || "".contentEquals(csv)) {
			throw new IllegalArgumentException ("docHash no puede ser nulo");
		}
		
		PfSignsDTO signDTO = (PfSignsDTO) baseDAO.queryElementOneParameter("request.signByCsvNormalizado", "csv", csv);
		
		return signDTO;
	}
	
	/**
	 * DTO de un documento según su hash.
	 * @param docHash
	 * @return
	 * @throws DocumentCantBeDownloadedException
	 */
	public PfDocumentsDTO getDocumentDTOByHash (String docHash) throws DocumentCantBeDownloadedException{
		if (docHash == null || "".contentEquals(docHash)) {
			throw new IllegalArgumentException ("docHash no puede ser nulo");
		}
		
		PfDocumentsDTO doc = 
			(PfDocumentsDTO) baseDAO.queryElementOneParameter("file.documentFile", "hash", docHash);
		
		if (doc == null) {
			throw new DocumentCantBeDownloadedException ("No se encuentra el documento", docHash);
		}
		return doc;
	}
	
	/**
	 * Extensión de una firma, según el tipo.
	 * @param signDTO
	 * @return
	 */
	public String getSignExtension (PfSignsDTO signDTO) {
		String extension = null;
		if (signDTO.getCformat().toUpperCase().contains("XADES")) {
			extension =	Util.getInstance().loadSignExtensions().get(Constants.SIGN_FORMAT_XADES);
		}
		else {
			extension =	Util.getInstance().loadSignExtensions().get(signDTO.getCformat());
		}
		return extension;
	}
	
	
	/**
	 * Nombre del fichero descargado de informe.
	 * @param nombreDocumento
	 * @return
	 */
	public String getNombreReport (String nombreDocumento) {
		return Util.getInstance().getNombreFichero(nombreDocumento, 
				   false, Constants.REPORT_PREFFIX, null, Constants.PDF_LOWER);
	}
	
	/**
	 * Nombre del fichero descargado de firma.
	 * @param nombreDocumento
	 * @param signExtension
	 * @return
	 */
	public String getNombreFirma (String nombreDocumento, String signExtension) {
		return Util.getInstance().getNombreFichero(nombreDocumento, 
				true, null, Constants.SIGNATURE_SUFFIX, signExtension);
	}
	
	/**
	 * Nombre del fichero descargado de visualización.
	 * @param nombreDocumento
	 * @return
	 */
	public String getNombreVisor (String nombreDocumento) {
		return Util.getInstance().getNombreFichero(nombreDocumento, 
				   false, Constants.VISOR_PREFFIX, null, Constants.PDF_LOWER);
	}
	
	/**
	 * Nombre del fichero descargado de la visualización de TCN.
	 * @param nombreDocumento
	 * @return
	 */
	public String getNombreVisorTCN (String nombreDocumento) {
		return Util.getInstance().getNombreFichero(nombreDocumento, 
				   false, Constants.VISOR_TCN_PREFFIX, null, Constants.PDF_LOWER);
	}
	
	public String getNombreEni(String nombreDocumento) {
		return utilComponent.getNombreFichero(nombreDocumento, false, Constants.FILE_ENI_PREFIX, null, Constants.EXPORT_XML_TYPE);
	}
	
	public FileAttachedDTO obtenerFirma(String hashDocument) throws DocumentCantBeDownloadedException{
		
		PfSignsDTO signDTO = getSignDTOByDocumentHash(hashDocument);
		signDTO.getDocumentos();
		byte[] contenidoArchivo = signDTO.getDocumentos().getBFirma();
		String nombreArchivo = getNombreFirma(signDTO.getPfDocument().getDname(), getSignExtension(signDTO));
		String mime = "";
		if("pdf".equalsIgnoreCase(signDTO.getCformat())){
			mime= Constants.PDF_MIME;
		}
		else{
			mime="application/octet-stream";
		}
		return new FileAttachedDTO(contenidoArchivo,nombreArchivo, mime);
	}

	public FileAttachedDTO obtenerInforme(String hashDocument) throws DocumentCantBeDownloadedException{
		PfSignsDTO signDTO = getSignDTOByDocumentHash(hashDocument);
		
		byte[] contenidoArchivo = getReportBySignDTO(signDTO, false, true, applicationBO.queryApplicationPfirma());

		String nombreArchivo = getNombreReport(signDTO.getPfDocument().getDname());
		String mime = Constants.PDF_MIME;
		return new FileAttachedDTO(contenidoArchivo,nombreArchivo, mime);
	}
	
	public FileAttachedDTO setNormalizedReportIntoHttpResponseByDocumentHash (String docHash) throws DocumentCantBeDownloadedException {
		
		PfSignsDTO signDTO = getSignDTOByDocumentHash (docHash);

		byte[] contenidoArchivo = getNormalizedReportBySignDTO (signDTO, signDTO.getPfDocument().getPfRequest().getPfApplication());
		
		String nombreArchivo = "normalized_" + getNombreReport(signDTO.getPfDocument().getDname());
		String mime = Constants.PDF_MIME;
		return new FileAttachedDTO(contenidoArchivo,nombreArchivo, mime);

	}	
	

}
