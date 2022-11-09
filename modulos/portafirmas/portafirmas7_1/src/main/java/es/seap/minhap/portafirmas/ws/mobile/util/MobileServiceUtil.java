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

package es.seap.minhap.portafirmas.ws.mobile.util;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringUtils;

import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;
import es.seap.minhap.portafirmas.utils.ws.WSUtil;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileDocument;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileRequest;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileRequestFilter;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileRequestFilterList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileSignLine;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileStringList;

public class MobileServiceUtil {

	private static MobileServiceUtil instance;
	
//	Logger log = Logger.getLogger(MobileServiceUtil.class);

	/**
	 * Obtiene una instancia del objeto actual.
	 * @return la instancia del objeto actual.
	 */
	public static synchronized MobileServiceUtil getInstance() {
		if (instance == null) {
			instance = new MobileServiceUtil();
		}
		return instance;

	}

	/**
	 * Método que convierte un objeto PfRequestTagDTO a otro MobileRequest
	 * @param requestTag etiqueta petición
	 * @return petición del Portafirmas móvil
	 */
	public MobileRequest requestTagToMobileRequest(PfRequestTagsDTO requestTag, boolean isQueryDetail) {

		MobileRequest mobileRequest = new MobileRequest();
		PfRequestsDTO request = requestTag.getPfRequest();

		mobileRequest.setApplication(request.getPfApplication().getDapplication());
		mobileRequest.setFentry(WSUtil.dateToGregorian(request.getFmodified()));
		mobileRequest.setIdentifier(request.getChash());
		mobileRequest.setImportanceLevel(request.getPfImportance().getCcodigonivel());
		mobileRequest.setRef(request.getDreference());
		mobileRequest.setSubject(request.getDsubject());
		mobileRequest.setForward(request.isForwarded());
		mobileRequest.setView(requestTag.getPfTag().getCtag());
		mobileRequest.setRequestTagId(requestTag.getChash());
		
		//Fecha de caducidad (si la tiene)
		Date fExpiration = requestTag.getPfRequest().getFexpiration();
		if(fExpiration != null){
			mobileRequest.setFexpiration(WSUtil.dateToGregorian(fExpiration));
		}

		//Si la petición está rechazada, se debe incluir un flag de petición rechazada y el motivo del rechazo
		if(this.isRejected(request) && isQueryDetail){
			mobileRequest.setRejected(true);
			Set<PfCommentsDTO> commentsDTO = request.getPfComments();
			Iterator<PfCommentsDTO> commentsIt = commentsDTO.iterator();
			boolean finished = false;
			while(commentsIt.hasNext() && !finished){
				PfCommentsDTO comment = commentsIt.next();
				if(comment.getDsubject().equals(Constants.C_TAG_REJECTED)){
					mobileRequest.setRejectedText(comment.getTcomment());
					finished = true;
				}
			}
			if(!finished){
				mobileRequest.setRejectedText(StringUtils.EMPTY);
			}
		}
		
		return mobileRequest;
	}
	
	private boolean isRejected(PfRequestsDTO request){
		Set<PfRequestTagsDTO> reqTags = request.getPfRequestsTags();
		Iterator<PfRequestTagsDTO> reqTagsIt = reqTags.iterator();
		while(reqTagsIt.hasNext()){
			PfRequestTagsDTO reqTag = reqTagsIt.next();
			if(reqTag.getPfTag().getCtag().equals(Constants.C_TAG_REJECTED)){
				return true;
			}
		}
		return false;
		
	}

	/**
	 * Método que convierte un objeto PfDocumentDTO a otro MobileDocument
	 * @param document Documento
	 * @return Documento del Portafirmas móvil
	 */
	public MobileDocument documentDTOToMobileDocument(PfDocumentsDTO document, boolean viewPreSignActivated) {
		MobileDocument mobileDocument = new MobileDocument();

		mobileDocument.setIdentifier(document.getChash());
		mobileDocument.setName(document.getDname());
		mobileDocument.setMime(document.getDmime());
		if (document.getDmime().equalsIgnoreCase("text/tcn")
				|| Constants.C_DOCUMENTTYPE_FACTURAE.equals(document.getPfDocumentType().getCdocumentType())
				|| (document.getLissign() && viewPreSignActivated)){//Lo cambiamos porque cuando se recupere el documento se le servirá un PDF y la aplicación movil coge de aquí el mime del documento final
			mobileDocument.setMime(MobileConstants.MIME_PDF);
		}

		return mobileDocument;
	}
	
	/**
	 * Método que convierte un objeto PfSignLinesDTO a otro MobileSignLine
	 * @param signLine Línea de firma
	 * @return Línea de firma del Portafirmas móvil
	 */
	public MobileSignLine signLineDTOToMobileSignLine(PfSignLinesDTO signLine) {
		MobileSignLine mobileSignLine = new MobileSignLine();

		// Se añade el tipo de línea de firma (Firma o Visto Bueno)
		mobileSignLine.setType(signLine.getCtype());

		// Se añade la lista de firmantes
		MobileStringList signersList = new MobileStringList();
		Set<PfSignersDTO> signers = signLine.getPfSigners();
		for (PfSignersDTO signer : signers) {
			signersList.getStr().add(signer.getPfUser().getFullName());
		}
		mobileSignLine.setMobileSignerList(signersList);

		return mobileSignLine;
	}

	/**
	 * Método que devuelve el valor de un filtro de una lista de filtros para peticiones móviles
	 * @param filterList Lista de filtros
	 * @param filter Filtros a buscar
	 * @return Valor del filtro
	 */
	public String getFilterValue(MobileRequestFilterList filterList, String filter) {
		String value = null;
		for (MobileRequestFilter mobileFilter : filterList.getRequestFilter()) {
			if (mobileFilter.getKey().equals(filter)) {
				value = mobileFilter.getValue();
				break;
			}
		}
		return value;
	}

	/**
	 * Método que obtiene el contenido en bytes de un documento
	 * @param doc Documento
	 * @param custodyService Servicio de custodia de documentos
	 * @return Contenido del documento
	 * @throws CustodyServiceException
	 */
	public DataHandler getDocumentData(PfDocumentsDTO doc, CustodyServiceOutput custodyService) throws CustodyServiceException {
		final CustodyServiceOutputDocument custodyDocument = new CustodyServiceOutputDocument();
		custodyDocument.setIdentifier(doc.getChash());
		custodyDocument.setUri(doc.getPfFile().getCuri());
		custodyDocument.setIdEni(doc.getPfFile().getIdEni());
		custodyDocument.setRefNasDir3(doc.getPfFile().getRefNasDir3());
		DataHandler data = new DataHandler(new ByteArrayDataSource(custodyService.downloadFile(custodyDocument), doc.getDmime()));
		return data;
	}

	/**
	 * Método que obtiene el contenido en bytes de una firma
	 * @param sign Firma
	 * @param custodyService Servicio de custodia de firmas
	 * @return Contenido de la firma
	 * @throws Exception 
	 */
	public DataHandler getSignatureData(PfSignsDTO sign, CustodyServiceOutput custodyService) throws Exception {
		final CustodyServiceOutputSign custodySign = new CustodyServiceOutputSign();
		custodySign.setType(Constants.SIGN_TYPE_SERVER);
		custodySign.setIdentifier(sign.getPrimaryKeyString());
		custodySign.setUri(sign.getCuri());
		custodySign.setIdEni(sign.getRefNASIdEniFirma());
		custodySign.setRefNasDir3(sign.getRefNASDir3Firma());
		String mime = Util.getInstance().loadSignMime().get(sign.getCformat());
		DataHandler data = new DataHandler(new ByteArrayDataSource(custodyService.downloadSign(custodySign), mime));
		return data;
	}

}
