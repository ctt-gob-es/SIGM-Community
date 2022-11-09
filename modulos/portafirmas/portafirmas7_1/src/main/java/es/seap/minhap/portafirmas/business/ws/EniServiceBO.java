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

package es.seap.minhap.portafirmas.business.ws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.BinaryDocumentsBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.beans.binarydocuments.BinaryDocumentReport;
import es.seap.minhap.portafirmas.business.beans.binarydocuments.BinaryDocumentSign;
import es.seap.minhap.portafirmas.business.beans.binarydocuments.OutputStreamContent;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;
import es.seap.minhap.portafirmas.exceptions.GInsideException;
import es.seap.minhap.portafirmas.exceptions.MetadataNotValidException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;
import es.seap.minhap.portafirmas.utils.metadata.MetadataValidator;
import es.seap.minhap.portafirmas.utils.ws.WSUtil;
import es.seap.minhap.portafirmas.web.beans.DocumentEni;
import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.CsvJustificante;
import es.seap.minhap.portafirmas.ws.bean.Signature;
import es.seap.minhap.portafirmas.ws.bean.SignatureSerializable;
import es.seap.minhap.portafirmas.ws.eni.request.MetadataEni;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;
//import es.seap.minhap.portafirmas.ws.inside.eni.documentoe.metadatos.EnumeracionEstadoElaboracion;
//import es.seap.minhap.portafirmas.ws.inside.eni.documentoe.metadatos.TipoDocumental;
//import es.seap.minhap.portafirmas.ws.inside.eni.documentoe.metadatos.TipoEstadoElaboracion;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.EnumeracionEstadoElaboracion;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoDocumental;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoEstadoElaboracion;

import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoDocumentoConversionInside;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.MetadatoAdicional;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class EniServiceBO {

	private Logger log = Logger.getLogger(EniServiceBO.class);

	
	@Autowired
	private BinaryDocumentsBO binaryDocumentsBO;

	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private RequestBO requestBO;
	
	@Autowired
	MetadataValidator metadataValidator;
	
	@Autowired
	private AuthenticationWSHelper authenticationWSHelper;
	
	@Autowired
	private GInsideBO ginsideBO;
	 
	@Autowired
	SignatureServiceBO signatureServiceBO;
	
	@Autowired
	CustodyServiceFactory custodyServiceFactory;
	
	@Transactional (readOnly=false)
	public BinaryDocumentSign downloadSignEni (PfApplicationsDTO peticionario, String documentHash, DocumentEni document) throws DocumentCantBeDownloadedException, GInsideException, MetadataNotValidException {
		//validamos que los metadatos que llegan sean apropiadas para la aplicación
		metadataValidator.validarMetadatos(peticionario.getCapplication(), document.getMetadatosAdicionales());
		
		PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash(documentHash);
		
		BinaryDocumentSign retorno = getSignatureEniOutputStreamBySignDTO (signDTO, document, peticionario);
		retorno.setName("ENI_" + binaryDocumentsBO.getNombreEni(signDTO.getPfDocument().getDname()));
		retorno.setMime("application/xml");
		return retorno;		
	}

	/**
	 * M&eacute;todo que permite descargar un fichero de firma de la BD.
	 * @param authentication Par&aacute;metro de autenticaci&oacute;n.
	 * @param documentId Identificador del documento firmado.
	 * @return Firma.
	 * @throws PfirmaException Excepci&oacute;n del Portafirmas.
	 */
	
	@Transactional(readOnly=false)
	public Signature downloadSignEni(Authentication authentication, String documentId, 
			MetadataEni metadatosEni, List<MetadatoAdicional> metadatosAdicionales) throws PfirmaException {
		log.info("downloadSign init: " + documentId);
		try {
						
			PfUsersDTO usuario = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());				
			// Checking document
			PfDocumentsDTO doc = requestBO.checkDocument(documentId);
			//PfDocumentsDTO doc = null;
			authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, doc.getPfRequest());
			
			//validamos que los metadatos que llegan sean apropiadas para la aplicación
			metadataValidator.validarMetadatos(usuario.getCidentifier(), metadatosAdicionales);
			
			return getSignatureEni(doc, metadatosEni, metadatosAdicionales);			
		} catch (PfirmaException pf) { 
			throw pf;
		} catch (GInsideException e) {
			throw new PfirmaException(e.getMessage(), e);
		} catch (MetadataNotValidException e) {
			throw new PfirmaException(e.getMessage(), e);
		} catch (Exception e) {
			throw new PfirmaException("Unknown error", e);
		}
	}
	
	/**
	 * M&eacute;todo que obtiene la firma de un documento.
	 * @param doc Documento.
	 * @return Firma del documento.
	 * @throws IOException 
	 * @throws GInsideException 
	 * @throws CustodyServiceException
	 */
	private Signature getSignatureEni(PfDocumentsDTO doc, MetadataEni metadatosEni, List<MetadatoAdicional> metadatosAdicionales) throws DocumentCantBeDownloadedException, IOException, GInsideException {
		
		long idConf = doc.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
		Signature signature = new Signature();
		
		// Si el documento es firmable se obtiene su firma (si no, es un anexo y no tiene firmas)
		if (doc.getLsign()) {
			SignatureSerializable signatureSerializable = signatureServiceBO.getSignatureSerializable(doc);
			BeanUtils.copyProperties(signatureSerializable, signature);
			
			if (signatureSerializable.getContentBytes() != null) {
				DocumentEni documentEni = new DocumentEni();
				documentEni.setContenido(signatureSerializable.getContentBytes());
				documentEni.setMetadatosEni(metadatosToInside(metadatosEni));
				documentEni.setMetadatosAdicionales(metadatosAdicionales);

				byte[] docEni = ginsideBO.convertirDocumentoAEniConMAdicionales(documentEni, true, signatureSerializable.getMimeType() ,idConf);
				DataHandler content = new DataHandler(new ByteArrayDataSource(docEni, "application/xml"));
				signature.setContent(content);
			}
		}

		return signature;
	}
	

	/**
	 * Se encarga de obtener el informe de la firma de un documento y meterla en el response.
	 * @param documentHash
	 * @param response
	 * @throws DocumentCantBeDownloadedException
	 * @throws GInsideException 
	 * @throws MetadataNotValidException 
	 */
	@Transactional (readOnly=false)
	public BinaryDocumentReport getReportEniByDocumentHash(String documentHash, DocumentEni documentEni, PfApplicationsDTO peticionario) throws DocumentCantBeDownloadedException, GInsideException, MetadataNotValidException {
		try {
			//validamos que los metadatos que llegan sean apropiadas para la aplicación
			metadataValidator.validarMetadatos(peticionario.getCapplication(), documentEni.getMetadatosAdicionales());
			
			PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash(documentHash);
			byte[] bytes = binaryDocumentsBO.getReportBySignDTO(signDTO, peticionario);
			byte[] contenido = null;
			try {
				documentEni.setCsv(new TipoDocumentoConversionInside.Csv());
				documentEni.getCsv().setValorCSV(signDTO.getCsv());
				documentEni.getCsv().setRegulacionCSV(Constants.REGULACION_CSV);
			
				documentEni.setContenido(bytes);
				contenido = ginsideBO.convertirDocumentoAEniConMAdicionales(documentEni, false, Constants.PDF_MIME, peticionario.getPfConfiguration().getPrimaryKey());
			} catch (GInsideException e) {
				throw e;
			} catch (Exception e) {
				throw new DocumentCantBeDownloadedException ("No se puede descargar el informe de firma", e);
			} 
			
			
			ByteArrayOutputStream docEniOutputStream = null;
			try {
				docEniOutputStream = new ByteArrayOutputStream(contenido.length);
				docEniOutputStream.write(contenido, 0, contenido.length);
				if(docEniOutputStream!=null)
					docEniOutputStream.close();
			} catch (IOException e) {
				throw new DocumentCantBeDownloadedException ("No se puede descargar la firma", e);
			}
	
					
			//Este será el objeto que se devolverá, con un outputstream que contenga el contenido de la firma.
			BinaryDocumentReport binaryDocument = new BinaryDocumentReport();
			binaryDocument.setName(binaryDocumentsBO.getNombreEni(signDTO.getPfDocument().getDname()));
			binaryDocument.setMime("application/xml");
			binaryDocument.setContent(new OutputStreamContent (docEniOutputStream));
			binaryDocument.setCsv(signDTO.getCsv());
	
			return binaryDocument;
		} catch (Exception e) {
			throw new DocumentCantBeDownloadedException ("No se puede obtener el informe ", documentHash);
		}
	}
	
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public CsvJustificante getReportEniByDocumentHash(Authentication authentication, String documentId, MetadataEni metadatosEni, List<MetadatoAdicional> metadatosAdicionales) throws PfirmaException {
		CsvJustificante csvJustificante = new CsvJustificante();
					
		try {
			// Se comprueba la autenticidad del usuario
			PfUsersDTO pfUserDto = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			
			PfDocumentsDTO document = requestBO.checkDocument(documentId);
			if (document != null){
				log.debug("Checking user can access doocument");
				authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, document.getPfRequest());
			}
			else{
				throw new PfirmaException("El documento: "+documentId+" no existe");
			}
			
			//validamos que los metadatos que llegan sean apropiadas para la aplicación
			metadataValidator.validarMetadatos(pfUserDto.getCidentifier(), metadatosAdicionales);
			
			PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash (documentId);
			byte[] bytes = binaryDocumentsBO.getReportBySignDTO(signDTO, applicationBO.loadApplication(pfUserDto.getCidentifier()));		
		
			long idConf = document.getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
			
			DocumentEni documentEni = new DocumentEni();
			documentEni.setContenido(bytes);
			documentEni.setMetadatosEni(metadatosToInside(metadatosEni));
			documentEni.setCsv(new TipoDocumentoConversionInside.Csv());
			documentEni.getCsv().setValorCSV(signDTO.getCsv());
			documentEni.getCsv().setRegulacionCSV(Constants.REGULACION_CSV);
			documentEni.setMetadatosAdicionales(metadatosAdicionales);
			
			byte[] docEni = ginsideBO.convertirDocumentoAEniConMAdicionales(documentEni, false, Constants.PDF_MIME ,idConf);
			DataHandler content = new DataHandler(new ByteArrayDataSource(docEni, "application/xml"));
			 
			csvJustificante.setContent(content);
			csvJustificante.setCsv(signDTO.getCsv());
			csvJustificante.setMime("application/xml");
		} catch (GInsideException e) {
			throw new PfirmaException(e.getMessage(), e);
		} catch (MetadataNotValidException e) {
			throw new PfirmaException(e.getMessage(), e);
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Exception e) {
			log.error("Error en queryCSVyJustificanteEni", e);
			throw new PfirmaException("Error en queryCSVyJustificanteEni", e);
		}
		return csvJustificante;
	}
	
	private static TipoDocumentoConversionInside.MetadatosEni metadatosToInside(MetadataEni metadatosInside) {
		TipoDocumentoConversionInside.MetadatosEni metadatosEni = new TipoDocumentoConversionInside.MetadatosEni();
			TipoEstadoElaboracion estadoElaboracion = new TipoEstadoElaboracion();
			if(metadatosInside.getEstadoElaboracion() != null){
				estadoElaboracion.setValorEstadoElaboracion(EnumeracionEstadoElaboracion.fromValue(metadatosInside.getEstadoElaboracion().getValorEstadoElaboracion().value()));
				estadoElaboracion.setIdentificadorDocumentoOrigen(metadatosInside.getEstadoElaboracion().getIdentificadorDocumentoOrigen());
			}
			metadatosEni.setEstadoElaboracion((es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoEstadoElaboracion)estadoElaboracion);
			metadatosEni.setFechaCaptura((metadatosInside.getFechaCaptura()));
			metadatosEni.setIdentificador(metadatosInside.getIdentificador());
			metadatosEni.setOrigenCiudadanoAdministracion(metadatosInside.isOrigenCiudadanoAdministracion());
			metadatosEni.setTipoDocumental(TipoDocumental.fromValue(metadatosInside.getTipoDocumental().value()));
			metadatosEni.setVersionNTI(metadatosInside.getVersionNTI());
			metadatosEni.getOrgano().addAll(metadatosInside.getOrgano());			
			return metadatosEni;
		}
	
	/**
	 * Devuelve un BinaryDocument de una firma, con el nombre, mime y outputstream relleno (contenido de la firma).
	 * @param signDTO dto de la firma
	 * @param output outputstream donde se escribe el contenido de la firma.
	 * @return
	 * @throws DocumentCantBeDownloadedException
	 * @throws GInsideException 
	 */
	private BinaryDocumentSign getSignatureEniOutputStreamBySignDTO (PfSignsDTO signDTO, DocumentEni document, PfApplicationsDTO aplicacion) throws DocumentCantBeDownloadedException, GInsideException {
		if (signDTO == null) {
			return null;
		}
		byte[] bytes = null;

		CustodyServiceOutputSign custodySign = new CustodyServiceOutputSign();
		String storageType = signDTO.getCtype();
		custodySign.setType(Constants.SIGN_TYPE_SERVER);
		custodySign.setIdentifier(signDTO.getPrimaryKeyString());
		custodySign.setUri(signDTO.getCuri());
		custodySign.setIdEni(signDTO.getRefNASIdEniFirma());
		custodySign.setRefNasDir3(signDTO.getRefNASDir3Firma());
		
		try {
			CustodyServiceOutput service = custodyServiceFactory.createCustodyServiceOutput(storageType);		
			bytes = service.downloadSign(custodySign);
			
		} catch (CustodyServiceException e) {
			throw new DocumentCantBeDownloadedException ("No se puede descargar la firma", e);
		}
		
		//TODO Llamamos a ginside para obtener el documento ENI
		byte[] contenido = null;
		//ByteArrayOutputStream baos = null;
		try {		
			document.setContenido(bytes);
			contenido = ginsideBO.convertirDocumentoAEniConMAdicionales(document, true, Util.getInstance().loadSignMime().get(signDTO.getCformat()), aplicacion.getPfConfiguration().getPrimaryKey());
		} catch (GInsideException e) {
			throw e;
		} catch (Exception e) {
			throw new DocumentCantBeDownloadedException ("No se puede descargar la firma", e);
		} 
		
		
		ByteArrayOutputStream docEniOutputStream = null;
		try {
			docEniOutputStream = new ByteArrayOutputStream(contenido.length);
			docEniOutputStream.write(contenido, 0, contenido.length);
			if(docEniOutputStream!=null)
				docEniOutputStream.close();
		} catch (IOException e) {
			throw new DocumentCantBeDownloadedException ("No se puede descargar la firma", e);
		}

				
		//Este será el objeto que se devolverá, con un outputstream que contenga el contenido de la firma.
		BinaryDocumentSign binaryDocument = new BinaryDocumentSign (Util.getInstance().getNombreFichero(signDTO.getPfDocument().getDname(), true, null, Constants.SIGNATURE_SUFFIX, "xml"),
				   											"application/xml", 
				   											new OutputStreamContent (docEniOutputStream));
		binaryDocument.setFormat("application/xml");
		
		return binaryDocument;
	}
}
