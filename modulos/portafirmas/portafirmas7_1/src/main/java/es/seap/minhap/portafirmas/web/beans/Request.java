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

package es.seap.minhap.portafirmas.web.beans;

import java.util.ArrayList;
import java.util.List;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfHistoricRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfInvitedUsersDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.web.beans.signature.RequestSignatureConfig;

public class Request {

	private Long primaryKey;
	private String requestTagHash; // Código hash de la relación etiquetas-petición
	private String sender;
	private String subject;
	private String subject_sin_html;
	private String reference;
	private String startDate;
	private String modifiedDate;
	private String expirationDate;
	private String application;
	private String text;
	private boolean pass;
	private String signType;
	private PfTagsDTO stateTag;
	private PfImportanceLevelsDTO importanceLevel;
	private Long cScope;
	private List<SignLine> signLines;
	private List<UserTag> userTags;
	private List<PfDocumentsDTO> documentList;
	private List<PfDocumentsDTO> annexList;
	private List<PfCommentsDTO> commentsList;
	private List<PfHistoricRequestsDTO> historicList;
	private boolean paramUser;
	private RequestSignatureConfig signatureConfig;
	private boolean anulada;
	private String signConfig;
	private boolean lInvited;
	private boolean lAccepted;
	private PfInvitedUsersDTO invitedUser;
	private String cCreated;
	private boolean anySign;
	private boolean lSignMarked;
	private boolean paralela;
	private String comentarioXades;
	
	public String getSignConfig() {
		return signConfig;
	}

	public void setSignConfig(String signConfig) {
		this.signConfig = signConfig;
	}

	public boolean isAnulada() {
		return anulada;
	}

	public void setAnulada(boolean anulada) {
		this.anulada = anulada;
	}

	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getRequestTagHash() {
		return requestTagHash;
	}

	public void setRequestTagHash(String requestTagHash) {
		this.requestTagHash = requestTagHash;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
		this.setSubject_sin_html(subject);
	}	

	public String getSubject_sin_html() {
		return subject_sin_html;
	}

	public void setSubject_sin_html(String subject_sin_html) {		
		this.subject_sin_html = subject_sin_html.replaceAll("<br/>"," - ");
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<SignLine> getSignLines() {
		return signLines;
	}

	public void setSignLines(List<SignLine> signLines) {
		this.signLines = signLines;
	}

	public List<UserTag> getUserTags() {
		return userTags;
	}

	public void setUserTags(List<UserTag> userTags) {
		this.userTags = userTags;
	}

	public List<PfDocumentsDTO> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(List<PfDocumentsDTO> documentList) {
		this.documentList = documentList;
	}

	public List<PfCommentsDTO> getCommentsList() {
		return commentsList;
	}

	public void setCommentsList(List<PfCommentsDTO> commentsList) {
		this.commentsList = commentsList;
	}

	public List<PfHistoricRequestsDTO> getHistoricList() {
		return historicList;
	}

	public void setHistoricList(List<PfHistoricRequestsDTO> historicList) {
		this.historicList = historicList;
	}

	public void setHistoricListDTO(List<AbstractBaseDTO> historicListDTO) {
		List<PfHistoricRequestsDTO> list = new ArrayList<PfHistoricRequestsDTO>();
		for (AbstractBaseDTO dto : historicListDTO) {
			PfHistoricRequestsDTO historicRequest = (PfHistoricRequestsDTO) dto;
			list.add(historicRequest);
		}
		this.historicList = list;
	}

	public PfTagsDTO getStateTag() {
		return stateTag;
	}

	public void setStateTag(PfTagsDTO stateTag) {
		this.stateTag = stateTag;
	}

	public List<PfDocumentsDTO> getAnnexList() {
		return annexList;
	}

	public void setAnnexList(List<PfDocumentsDTO> annexList) {
		this.annexList = annexList;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public PfImportanceLevelsDTO getImportanceLevel() {
		return importanceLevel;
	}

	public void setImportanceLevel(PfImportanceLevelsDTO importanceLevel) {
		this.importanceLevel = importanceLevel;
	}

	public Long getcScope() {
		return cScope;
	}

	public void setcScope(Long cScope) {
		this.cScope = cScope;
	}

	public boolean isParamUser() {
		return paramUser;
	}

	public void setParamUser(boolean paramUser) {
		this.paramUser = paramUser;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public RequestSignatureConfig getSignatureConfig() {
		return signatureConfig;
	}

	public void setSignatureConfig(RequestSignatureConfig signatureConfig) {
		this.signatureConfig = signatureConfig;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public boolean islInvited() {
		return lInvited;
	}

	public void setlInvited(boolean lInvited) {
		this.lInvited = lInvited;
	}

	public boolean islAccepted() {
		return lAccepted;
	}

	public void setlAccepted(boolean lAccepted) {
		this.lAccepted = lAccepted;
	}

	public PfInvitedUsersDTO getInvitedUser() {
		return invitedUser;
	}

	public void setInvitedUser(PfInvitedUsersDTO invitedUser) {
		this.invitedUser = invitedUser;
	}

	public String getcCreated() {
		return cCreated;
	}

	public void setcCreated(String cCreated) {
		this.cCreated = cCreated;
	}

	public boolean isAnySign() {
		return anySign;
	}

	public void setAnySign(boolean anySign) {
		this.anySign = anySign;
	}

	public boolean islSignMarked() {
		return lSignMarked;
	}

	public void setlSignMarked(boolean lSignMarked) {
		this.lSignMarked = lSignMarked;
	}

	public boolean isParalela() {
		return paralela;
	}

	public void setParalela(boolean paralela) {
		this.paralela = paralela;
	}

	public String getComentarioXades() {
		return comentarioXades;
	}

	public void setComentarioXades(String comentarioXades) {
		this.comentarioXades = comentarioXades;
	}

}
