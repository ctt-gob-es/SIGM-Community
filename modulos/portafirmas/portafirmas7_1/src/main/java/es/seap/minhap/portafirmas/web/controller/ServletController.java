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

package es.seap.minhap.portafirmas.web.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.BinaryDocumentsBO;
import es.seap.minhap.portafirmas.business.ReportBO;
import es.seap.minhap.portafirmas.business.ServletHelperBO;
import es.seap.minhap.portafirmas.business.beans.binarydocuments.BinaryDocumentReport;
import es.seap.minhap.portafirmas.business.beans.binarydocuments.BinaryDocumentSign;
import es.seap.minhap.portafirmas.business.ws.EniServiceBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfResponseOrganismosDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;
import es.seap.minhap.portafirmas.exceptions.DownloadException;
import es.seap.minhap.portafirmas.exceptions.GInsideException;
import es.seap.minhap.portafirmas.exceptions.MetadataNotValidException;
import es.seap.minhap.portafirmas.exceptions.PortafirmasException;
import es.seap.minhap.portafirmas.servlet.restriction.DocumentPermissionRestrictionImpl;
import es.seap.minhap.portafirmas.servlet.restriction.RequestListPermissionRestrictionImpl;
import es.seap.minhap.portafirmas.servlet.restriction.RequestPermissionRestrictionImpl;
import es.seap.minhap.portafirmas.servlet.restriction.SessionAttributesRestrictionImpl;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.utils.MimeType;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.metadata.MetadataConverter;
import es.seap.minhap.portafirmas.web.beans.DocumentDownload;
import es.seap.minhap.portafirmas.web.beans.DocumentEni;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.MetadatoAdicional;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.InsideWSException;

@Controller
@RequestMapping("servlet")
public class ServletController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired 
	private ServletHelperBO servletHelperBO;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private SessionAttributesRestrictionImpl sessionRestriction;
	
	@Autowired
	private DocumentPermissionRestrictionImpl documentPermisionRestriction;
	
	@Autowired
	private RequestPermissionRestrictionImpl requestPermissionRestriction;
	
	@Autowired
	private RequestListPermissionRestrictionImpl requestListPermissionRestriction;
	
	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	DateComponent dateComponent;
	
	@Autowired
	EniServiceBO eniServiceBO;
	
	@Autowired
	BinaryDocumentsBO binaryDocumentsBO;
	
	@Autowired
	UtilComponent util;
	
	@Autowired 
	private ReportBO reportBO;
	
	@Autowired
	private BaseDAO baseDAO;
	
	@ExceptionHandler({DownloadException.class})
	public ModelAndView informeError(DownloadException e) {
		ModelAndView model = new ModelAndView();
		model.addObject("errorMessage", e.getMessage());
		model.addObject("timeError", new Date(System.currentTimeMillis()));
		model.setViewName("error");
		return model;
	}

	/**
	 * Método que descarga un documento de la carpeta temporal de Portafirmas
	 * @param request Objeto que encapsula la petición http
	 * @param response Objeto que encapsula la respuesta http
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/DescargaFicheroDeServidorServlet", method = RequestMethod.GET)
	public @ResponseBody String downloadFileFromServer(final HttpServletRequest request,
			@RequestParam(value = "nombreDocumento") String nombreDocumento) throws Exception {
		try {
			if (!sessionRestriction.check(request)) {			
				throw new PortafirmasException("No tiene permisos para descargar el documento");
			}

			String sysFile = Constants.PATH_TEMP + nombreDocumento;		
			File fichero = new File(sysFile);
			
			return new String(binaryDocumentsBO.getFileFromServerByFile(fichero));
			
		} catch (PortafirmasException e) {
			log.error("Error descargando fichero de servidor " + nombreDocumento, e);
			throw e;
		} catch (DocumentCantBeDownloadedException e) {
			log.error("Error descargando fichero de servidor: " + e.getDetalleDocumento(), e);
			throw new Exception("Error obteniendo el documento a firmar");
		} catch (Exception e) {
			log.error("Error desconocido descargando fichero de servidor: ", e);
			throw new Exception("Error desconocido obteniendo el documento a firmar");
		}
	}

	
	/**
	 * Método que permite descargar un documento de Portafirmas
	 * @param request Petición
	 * @param response Respuesta
	 * @throws IOException
	 */
	@RequestMapping(value = "/DescargaDocumentoServlet", method = RequestMethod.GET)
	public void downloadDocument(final HttpServletRequest request,
			final HttpServletResponse response) throws DownloadException {

		String idDocument = request.getParameter("idDocument");
		String nombreDocumento = "";
		
		try {
			if (!sessionRestriction.check(request) || !documentPermisionRestriction.check(request)) {			
				throw new DownloadException(messageSource.getMessage("error.forbiddenDocumentDownload", null, LocaleContextHolder.getLocale()));
			}

			PfDocumentsDTO documentDTO = binaryDocumentsBO.getDocumentDTOByHash (idDocument);
			byte[] bytes = servletHelperBO.obtenerDocumento(documentDTO);
			
			nombreDocumento=documentDTO.getDname();
			
			if(documentDTO.getDmime().equals("application/pdf"))
			{
				if(!nombreDocumento.endsWith(".pdf"))
						nombreDocumento = nombreDocumento.concat(".pdf");
			}
			else
			{
				if(!nombreDocumento.endsWith(".xml"))
					nombreDocumento = nombreDocumento.concat(".doc");
			}
			
			sendResponse(response, nombreDocumento, documentDTO.getDmime(), bytes);

		} catch (DownloadException e) {
			log.error("Error descargando documento: " + idDocument, e);
			throw e;
		} catch (Exception e) {
			log.error("Error desconocido descargando documento: ", e);
			throw new DownloadException(messageSource.getMessage("error.documentDownload", null, LocaleContextHolder.getLocale()));
		}
	}
	
	/**
	 * Método que permite descargar los informes de una petición
	 * @param request Petición
	 * @param response Respuesta
	 * @throws IOException
	 */
	@RequestMapping(value = "/AbreRespuestaDIR3Servlet", method = RequestMethod.GET)
	public void AbreRespuestaDIR3Servlet(final HttpServletRequest request,
							     final HttpServletResponse response) throws IOException {
	
		String idResponse = request.getParameter("idResponse");
		
		try {
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("id", new BigDecimal(idResponse));
			List<?> returnQuery = baseDAO.excecNamedQueryWithoutAbstractDTO("PfResponseOrganismosDTO.findById", parametros);
			PfResponseOrganismosDTO actualizacion = (PfResponseOrganismosDTO) returnQuery.get(0);
			if (actualizacion.getDescripcionRespuesta().equals("Manual")) {
				sendResponse (response, "docResponse.xml" , "xml", actualizacion.getDocumentoObtenido());
			} else {
				sendResponse (response, "docResponse.zip" , "zip", actualizacion.getDocumentoObtenido());
			}
		} catch (Exception e) {
			log.error("Error desconocido descargando documento", e);
			response.sendError(500, "Error desconocido descargando documento");
			return;
		}
	}
	


	
	@RequestMapping(value = "/DescargaFirmaServlet", method = RequestMethod.GET)
	public void downloadSignature(final HttpServletRequest request,
			final HttpServletResponse response) throws DownloadException {

		String idDocument = request.getParameter("idDocument");
		try {
			if (!sessionRestriction.check(request) || !documentPermisionRestriction.check(request)) {			
				throw new DownloadException(messageSource.getMessage("error.forbiddenSignDownload", null, LocaleContextHolder.getLocale()));
			}

			PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash (idDocument);
			byte[] bytes = servletHelperBO.obtenerFirma (signDTO);
			String nombreFirma = binaryDocumentsBO.getNombreFirma (signDTO.getPfDocument().getDname(), binaryDocumentsBO.getSignExtension(signDTO));
			String mime = Util.getInstance().loadSignMime().get(signDTO.getCformat());
			
			sendResponse (response, nombreFirma, mime, bytes);
			
		} catch (DownloadException e) {
			log.error("Error descargando firma", e);
			throw e;
		} catch (Exception e) {
			log.error("Error desconocido descargando firma: ", e);
			throw new DownloadException(messageSource.getMessage("error.signDownload", null, LocaleContextHolder.getLocale()));
		}
	}

	@RequestMapping(value = "/DescargaPrevisorServlet", method = RequestMethod.GET)
	public void downloadPrevisualizacion(final HttpServletRequest request,
			final HttpServletResponse response) throws DownloadException {
		String idDocument = request.getParameter("idDocument");
		try {
			if (!sessionRestriction.check(request) || !documentPermisionRestriction.check(request)) {			
				throw new DownloadException(messageSource.getMessage("error.forbiddenDownloadPreview", null, LocaleContextHolder.getLocale()));
			}

			PfDocumentsDTO documentDTO = binaryDocumentsBO.getDocumentDTOByHash (idDocument);
			byte[] bytes = servletHelperBO.obtenerPrevisualizacion(documentDTO, applicationBO.queryApplicationPfirma());

			sendResponse (response, binaryDocumentsBO.getNombreVisor(documentDTO.getDname()), Constants.PDF_MIME, bytes);

		} catch (DownloadException e) {
			log.error("Error descargando previsualizacion: " + idDocument, e);
			throw e;
		}  catch (Exception e) {
			log.error("Error desconocido descargando previsualizacion: ", e);
			throw new DownloadException(messageSource.getMessage("error.preViewDownload", null, LocaleContextHolder.getLocale()));
		}
	}
	
	/**
	 * Método que permite descarga un informe de firmas de Portafirmas
	 * @param request Petición
	 * @param response Respuesta
	 * @throws Exception 
	 */	
	@RequestMapping(value = "/DescargaInformeServlet", method = RequestMethod.GET)
	public void downloadReport(final HttpServletRequest request, final HttpServletResponse response) throws DownloadException {
		String idDocument = request.getParameter("idDocument");
		try {
			if (!sessionRestriction.check(request) || !documentPermisionRestriction.check(request)) {			
				throw new DownloadException(messageSource.getMessage("error.forbiddenDownloadReport", null, LocaleContextHolder.getLocale()));
			}

			PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash (idDocument);
			byte[] bytes = servletHelperBO.obtenerInforme (signDTO, signDTO.getPfDocument().getPfRequest().getPfApplication());

			sendResponse (response, binaryDocumentsBO.getNombreReport(signDTO.getPfDocument().getDname()), Constants.PDF_MIME, bytes);
		
		} catch (DownloadException e) {
			log.error("Error desconocido descargando informe: " + idDocument, e);
			throw e;
		}  catch (Exception e) {
			log.error("Error desconocido descargando informe: " + idDocument, e);
			throw new DownloadException(messageSource.getMessage("error.reportDownload", null, LocaleContextHolder.getLocale()));
		}		
	}
	
	@RequestMapping(value = "/RegenerarInformeServlet", method = RequestMethod.GET)
	public void regenerateReport(final HttpServletRequest request,
			final HttpServletResponse response) throws DownloadException {

		String idDocument = request.getParameter("idDocument");

		try {
			if (!sessionRestriction.check(request) || !documentPermisionRestriction.check(request)) {			
				throw new DownloadException(messageSource.getMessage("error.forbiddenRegenerateReportDownload", null, LocaleContextHolder.getLocale()));
			}

			PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash (idDocument);
			byte [] bytes = servletHelperBO.regenerarInforme (signDTO);
			
			sendResponse (response, binaryDocumentsBO.getNombreReport(signDTO.getPfDocument().getDname()), Constants.PDF_MIME, bytes);

		} catch (DownloadException e) {
			log.error("Error regenerando informe: " + idDocument, e);
			throw e;
		}  catch (Exception e) {
			log.error("Error desconocido regenerando informe: ", e);
			throw new DownloadException(messageSource.getMessage("error.regenerateReportDownload", null, LocaleContextHolder.getLocale()));
		}
	}
	
	@RequestMapping(value = "/DescargaNormalizadoServlet", method = RequestMethod.GET)
	public void downloadNormalized(final HttpServletRequest request,
			final HttpServletResponse response) throws DownloadException {
		String idDocument = request.getParameter("idDocument");
		try {
			if (!sessionRestriction.check(request) || !documentPermisionRestriction.check(request)) {			
				throw new DownloadException(messageSource.getMessage("error.forbiddenDownloadNormalizedReport", null, LocaleContextHolder.getLocale()));
			}

			PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash (idDocument);
			byte[] bytes = servletHelperBO.obtenerInformeNormalizado (signDTO);
			
			sendResponse (response, binaryDocumentsBO.getNombreReport(signDTO.getPfDocument().getDname()), Constants.PDF_MIME, bytes);

		} catch (DownloadException e) {
			log.error("Error descargando informe normalizado "+ idDocument, e);
			throw e;
		}  catch (Exception e) {
			log.error("Error desconocido descargando informe normalizado: ", e);
			throw new DownloadException(messageSource.getMessage("error.normalizedReportDownload", null, LocaleContextHolder.getLocale()));
		}
	}
	
	/**
	 * Método que permite descarga un informe de firmas de Portafirmas
	 * @param request Petición
	 * @param response Respuesta
	 * @throws Exception
	 */
	@RequestMapping(value = "/DescargaZipVariasPeticionesServlet", method = RequestMethod.GET)
	public void downloadZipManyRequestTag(final HttpServletRequest request,
							   			final HttpServletResponse response) throws Exception {
		
		String idRequestsTags = request.getParameter("idRequestsTagList");
		log.debug("idRequestsTagList: " + idRequestsTags);

		if (!sessionRestriction.check(request) || !requestListPermissionRestriction.check(request)) {			
			response.sendError(403, "No tiene permisos para descargar el zip con los informes");
			return;
		}
				
		try {
			byte[] binaryZip = servletHelperBO.obtenerZipConInformes(idRequestsTags.split(","), applicationBO.queryApplicationPfirma());
			sendResponse (response,
					Constants.SIGN_REPORT_FILE_PREFIX+"_"+ "" + "_" + System.currentTimeMillis() + "_" + ".zip",
					MimeType.getInstance().extractTypeFromExtension(Constants.ZIP_EXTENSION),
					binaryZip);
			
		} catch (DocumentCantBeDownloadedException e) {
			log.error("Error descargando ZIP: " + e.getDetalleDocumento(), e);
			response.sendError(500, "Error descargando ZIP");
			return;
		} catch (Exception e) {
			log.error("Error desconocido descargando ZIP: ", e);
			response.sendError(500, "Error desconocido ZIP");
			return;
		}
	}


	/**
	 * Método que descarga una factura electrónica en formato legible
	 * @param request Petición al servidor
	 * @param response Respuesta del servidor
	 * @throws Exception
	 */
	@RequestMapping(value = "/DescargaFacturaeServlet", method = RequestMethod.GET)
	public void downloadFacturae(final HttpServletRequest request,
			final HttpServletResponse response) throws DownloadException {
		String idDocument = request.getParameter("idDocument");
		try {
			if (!sessionRestriction.check(request) || !documentPermisionRestriction.check(request)) {			
				throw new DownloadException(messageSource.getMessage("error.forbiddenFacturaeDownload", null, LocaleContextHolder.getLocale()));
			}

			byte[] bytes = servletHelperBO.obtenerFacturae(idDocument);
			
			sendResponse (response, Constants.HTML_MIME, bytes);
			
		} catch (DownloadException e) {
			log.error("Error descargando facturae: " + idDocument, e);
			throw e;
		} catch (Exception e) {
			log.error("Error desconocido descargando facturae", e);
			throw new DownloadException(messageSource.getMessage("error.facturaeDownload", null, LocaleContextHolder.getLocale()));
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/DescargaTCNServlet", method = RequestMethod.GET)
	public void downloadVisualizacionTCN(final HttpServletRequest request,
			final HttpServletResponse response) throws DownloadException {
		String idDocument = request.getParameter("idDocument");
		try {
			if (!sessionRestriction.check(request) || !documentPermisionRestriction.check(request)) {			
				throw new DownloadException(messageSource.getMessage("error.forbiddenTCNDownload", null, LocaleContextHolder.getLocale()));
			}

			PfDocumentsDTO documentDTO = binaryDocumentsBO.getDocumentDTOByHash (idDocument);
			byte [] bytes = servletHelperBO.obtenerVisualizacionTCN(documentDTO);
			
			sendResponse (response, binaryDocumentsBO.getNombreVisorTCN(documentDTO.getDname()), Constants.PDF_MIME, bytes);

		} catch (DownloadException e) {
			log.error("Error descargando visualización de TCN: " + idDocument, e);
			throw e;
		} catch (Exception e) {
			log.error("Error desconocido descargando visualización TCN", e);
			throw new DownloadException(messageSource.getMessage("error.TCNDownload", null, LocaleContextHolder.getLocale()));
		}
	}

	
	/**
	 * Método que permite descargar los informes de una petición
	 * @param request Petición
	 * @param response Respuesta
	 * @throws IOException
	 */
	@RequestMapping(value = "/DescargaZipServlet", method = RequestMethod.POST)
	public void downloadZip(final HttpServletRequest request,
							     final HttpServletResponse response) throws IOException {
	
		String idRequestTag = request.getParameter("idRequestTag");
		String nameFileReturn = request.getParameter("nameFileReturn");
		log.debug("idRequestTag: " + idRequestTag);
		
		if (!sessionRestriction.check(request) || !requestPermissionRestriction.check(request)) {			
			response.sendError(403, "No tiene permisos para descargar el ZIP con los informes");
			return;
		}
		try {
			if (reportBO.variosReport(idRequestTag)) {
				byte[] binaryZip = servletHelperBO.obtenerZipConInformes(new String[]{idRequestTag}, applicationBO.queryApplicationPfirma());
				String nombreDocumento = Constants.SIGN_REPORT_FILE_PREFIX+"_"+ (nameFileReturn!=null?nameFileReturn.trim():"") + "_" + System.currentTimeMillis() + "_" + ".zip";
				String mime = MimeType.getInstance().extractTypeFromExtension(Constants.ZIP_EXTENSION);
				sendResponse (response, nombreDocumento, mime, binaryZip);
			} else {
				List<AbstractBaseDTO> documentListDTO = binaryDocumentsBO.getDocumentDTOListByRequestTagHashes(new String[]{idRequestTag});
				// Sólo hay un documento.
				PfDocumentsDTO docDTO = (PfDocumentsDTO) documentListDTO.get(0);
				byte[] bytes = servletHelperBO.obtenerInforme(docDTO, applicationBO.queryApplicationPfirma(), nameFileReturn);
				sendResponse (response, binaryDocumentsBO.getNombreReport(docDTO.getDname()), Constants.PDF_MIME, bytes);
			}
			
		} catch (DocumentCantBeDownloadedException e) {
			log.error("Error descargando informe: " + e.getDetalleDocumento(), e);
			response.sendError(500, "Error descargando informe");
			return;
		} catch (Exception e) {
			log.error("Error desconocido descargando informe", e);
			response.sendError(500, "Error desconocido descargando informe");
			return;
		}
	}
	
	@RequestMapping(value = "/DescargaFirmaENIServlet", method = RequestMethod.POST)
	@ResponseBody
	public DocumentDownload downloadSignEni(@ModelAttribute("document") DocumentEni documentEni,
			HttpServletRequest request) throws PfirmaException {
		DocumentDownload retorno = new DocumentDownload();
		
		String idDocument = documentEni.getIdDocument();

		if (!sessionRestriction.check(request) || !documentPermisionRestriction.check(request)) {			
			throw new PfirmaException("No tiene permisos para descargar la firma ENI");
		}
				
		try {
			if(Util.esVacioONulo(documentEni.getMetadatosEni().getIdentificador())){
				String idMetadatosEniEmpty = messageSource.getMessage("field.required.idMetadatosEni",  null, LocaleContextHolder.getLocale());
				retorno.setMessage(idMetadatosEniEmpty);
				return retorno;
			}else{
				if(!Util.validateIDEni(documentEni.getMetadatosEni().getIdentificador())){
					String idMetadatosEniWrongFormat = messageSource.getMessage("field.wrong.idMetadatosEni", null, 
							LocaleContextHolder.getLocale());
					retorno.setMessage(idMetadatosEniWrongFormat);
					return retorno;
				}
			}
			
			if(Util.esVacioONulo(documentEni.getMetadatosEni().getOrgano())){
				String organoListEmpty = messageSource.getMessage("field.required.organoList",  null, LocaleContextHolder.getLocale());
				retorno.setMessage(organoListEmpty);
				return retorno;
			}
			
			if (util.isNotEmpty(documentEni.getMetadatosAdicionales())) {
				for (MetadatoAdicional metadatoAdicional : documentEni.getMetadatosAdicionales()) {
					if (StringUtils.isEmpty(metadatoAdicional.getTipo())) {
						metadatoAdicional.setTipo(MetadataConverter.DEFAULT_METADATA_ADITIONAL_TYPE);
					}
				}
			}
			
			setFechaCapturaEni(documentEni);
			
			PfDocumentsDTO documento = binaryDocumentsBO.getDocumentDTOByHash(idDocument);
			PfApplicationsDTO aplicacion = documento.getPfRequest().getPfApplication();
			
			BinaryDocumentSign binaryDocument = eniServiceBO.downloadSignEni(aplicacion, idDocument, documentEni);
			ByteArrayOutputStream bout = (ByteArrayOutputStream) binaryDocument.getContent().getContent();
			retorno = new DocumentDownload(binaryDocument.getName(), binaryDocument.getMime(), bout.toByteArray()); 
		} catch (DocumentCantBeDownloadedException e) {
			log.error("Error descargando firma ENI: " + e.getDetalleDocumento(), e);
			retorno.setMessage("Error descargando informe ENI" + e.getDetalleDocumento());
		} catch (GInsideException e) {
			log.error("Error convirtiendo firma ENI: " + e.getMessage(), e);
			retorno.setMessage("Error convirtiendo informe ENI: " + ((InsideWSException) e.getCause()).getFaultInfo().getDescripcion());
		} catch (MetadataNotValidException e) {
			log.error("Error metadatos adicionales ENI: " + e.getMessage(), e);
			retorno.setMessage("Error metadatos adicionales ENI: " + e.getMessage());
		} catch (Exception e) {
			log.error("Error desconocido descargando firma ENI: ", e);
			throw new PfirmaException("Error desconocido firma ENI");
		}
		return retorno;
	}
	
	@RequestMapping(value = "/DescargaInformeENIServlet", method = RequestMethod.POST)
	@ResponseBody
	public DocumentDownload downloadReportENI(@ModelAttribute("document")@Valid DocumentEni documentEni,
			 ModelMap model, HttpServletRequest request) throws PfirmaException {
		DocumentDownload retorno = new DocumentDownload();

		String idDocument = documentEni.getIdDocument();

		if (!sessionRestriction.check(request) || !documentPermisionRestriction.check(request)) {			
			throw new PfirmaException("No tiene permisos para descargar la firma ENI");
		}
				
		try {
			if(Util.esVacioONulo(documentEni.getMetadatosEni().getIdentificador())){
				String idMetadatosEniEmpty = messageSource.getMessage("field.required.idMetadatosEni",  null, LocaleContextHolder.getLocale());
				retorno.setMessage(idMetadatosEniEmpty);
				return retorno;
			}else{
				if(!Util.validateIDEni(documentEni.getMetadatosEni().getIdentificador())){
					String idMetadatosEniWrongFormat = messageSource.getMessage("field.wrong.idMetadatosEni", null, 
							LocaleContextHolder.getLocale());
					retorno.setMessage(idMetadatosEniWrongFormat);
					return retorno;
				}
			}
			
			if(Util.esVacioONulo(documentEni.getMetadatosEni().getOrgano())){
				String organoListEmpty = messageSource.getMessage("field.required.organoList",  null, LocaleContextHolder.getLocale());
				retorno.setMessage(organoListEmpty);
				return retorno;
			}
			
			if (util.isNotEmpty(documentEni.getMetadatosAdicionales())) {
				for (MetadatoAdicional metadatoAdicional : documentEni.getMetadatosAdicionales()) {
					if (StringUtils.isEmpty(metadatoAdicional.getTipo())) {
						metadatoAdicional.setTipo(MetadataConverter.DEFAULT_METADATA_ADITIONAL_TYPE);
					}
				}
			}
			
			setFechaCapturaEni(documentEni);
			
			PfDocumentsDTO documento = binaryDocumentsBO.getDocumentDTOByHash(idDocument);
			PfApplicationsDTO aplicacion = documento.getPfRequest().getPfApplication();
			
			BinaryDocumentReport binaryDocument = eniServiceBO.getReportEniByDocumentHash(idDocument, documentEni, aplicacion);
			ByteArrayOutputStream bout = (ByteArrayOutputStream) binaryDocument.getContent().getContent();
			retorno = new DocumentDownload(binaryDocument.getName(), binaryDocument.getMime(), bout.toByteArray()); 
		} catch (DocumentCantBeDownloadedException e) {
			log.error("Error descargando informe ENI: " + e.getDetalleDocumento(), e);
			retorno.setMessage("Error descargando informe ENI" + e.getDetalleDocumento());
		} catch (GInsideException e) {
			log.error("Error convirtiendo informe ENI: " + e.getMessage(), e);
			retorno.setMessage("Error convirtiendo informe ENI: " + e.getMessage());
		} catch (MetadataNotValidException e) {
			log.error("Error metadatos adicionales ENI: " + e.getMessage(), e);
			retorno.setMessage("Error metadatos adicionales ENI: " + e.getMessage());
		} catch (Exception e) {
			log.error("Error desconocido descargando informe ENI: ", e);
			throw new PfirmaException("Error desconocido informe ENI");
		}
		return retorno;
	}
	
	private void setFechaCapturaEni(DocumentEni document) throws DatatypeConfigurationException {
		if (document.getMetadatosEni().getFechaCaptura() == null
			&& StringUtils.isNotEmpty(document.getFcaptura())) {
			Date date = dateComponent.stringToDate(document.getFcaptura());
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			document.getMetadatosEni().setFechaCaptura(xmlDate);
		}
	}
	
	/**
	 * Cabeceras genéricas para descarga de un adjunto.
	 * @param response
	 * @param nameDocument
	 * @throws IOException 
	 */
	public void sendResponse (HttpServletResponse response, String nameDocument, String mimeDocument, byte[] bytes) throws IOException {
		response.setHeader("Content-Disposition", "attachment;filename=\"" + nameDocument + "\"");
		sendResponse (response, mimeDocument, bytes);
	}
	
	/**
	 * Cabeceras genéricas para descarga.
	 * @param response
	 * @throws IOException 
	 */
	public void sendResponse (HttpServletResponse response, String mimeDocument, byte[] bytes) throws IOException {
		response.setHeader("Content-Transfer-Encoding","binary");
		response.setHeader("Expires","0");
		response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma","public");
		response.setContentType(mimeDocument);
		
		IOUtils.copy(new ByteArrayInputStream (bytes), response.getOutputStream());
	}

}
