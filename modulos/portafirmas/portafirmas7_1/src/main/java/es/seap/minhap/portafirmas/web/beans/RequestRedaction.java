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

import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsTextDTO;

public class RequestRedaction {

	private String signer;
	private String signers;
	private String signLinesConfig;
	private String signatureConfig;
	private String importanceLevel;
	private String scopeType;
	private boolean addTimestamp;
	private String signType;
	private String fstart;
	private String fexpiration;
	private String[] notices;
	private PfRequestsDTO request;
	private PfRequestsTextDTO requestText;
	private boolean esEnveloped;
	private String userSeatFilter;
	private String action;
	private String templateCode;
	private String uploadedDocsError;
	private String referenceLengthError;
	private String subjectLengthError;
	private String signlinesType;
	private String signlinesAccion;
	private String emailEmptyError;
	private String emailPatternError;

	public RequestRedaction() {
		/*this.esEnveloped = false;
		this.signers = "";

		// Se crean los objetos de petición y de texto
		this.request = new PfRequestsDTO();
		this.requestText = new PfRequestsTextDTO();

		// Se definen los parámetros de firma por defecto
		this.signatureConfig = "default";
		this.signType = Constants.SIGN_TYPE_CASCADE;

		// Se definen los parámetros de notificación por defecto
		this.notices = new String[3];
		this.notices[0] = "noticeRead";
		this.notices[1] = "noticeReject";
		this.notices[2] = "noticeSign";
		this.getRequest().setLOnlyNotifyActionsToRemitter(false);*/
	}

	public String getSignatureConfig() {
		return signatureConfig;
	}

	public void setSignatureConfig(String signatureConfig) {
		this.signatureConfig = signatureConfig;
	}

	public String getImportanceLevel() {
		return importanceLevel;
	}

	public void setImportanceLevel(String importanceLevel) {
		this.importanceLevel = importanceLevel;
	}
	
	public String getScopeType() {
		return scopeType;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}

	public boolean isAddTimestamp() {
		return addTimestamp;
	}

	public void setAddTimestamp(boolean addTimestamp) {
		this.addTimestamp = addTimestamp;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String[] getNotices() {
		return notices;
	}

	public void setNotices(String[] notices) {
		this.notices = notices;
	}

	public PfRequestsDTO getRequest() {
		return request;
	}

	public void setRequest(PfRequestsDTO request) {
		this.request = request;
	}

	public PfRequestsTextDTO getRequestText() {
		return requestText;
	}

	public void setRequestText(PfRequestsTextDTO requestText) {
		this.requestText = requestText;
	}

	public String getFstart() {
		return fstart;
	}

	public void setFstart(String fstart) {
		this.fstart = fstart;
	}

	public String getFexpiration() {
		return fexpiration;
	}

	public void setFexpiration(String fexpiration) {
		this.fexpiration = fexpiration;
	}

	public boolean isEsEnveloped() {
		return esEnveloped;
	}

	public void setEsEnveloped(boolean esEnveloped) {
		this.esEnveloped = esEnveloped;
	}

	public String getUserSeatFilter() {
		return userSeatFilter;
	}

	public void setUserSeatFilter(String userSeatFilter) {
		this.userSeatFilter = userSeatFilter;
	}

	public String getSigners() {
		return signers;
	}

	public void setSigners(String signers) {
		this.signers = signers;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getSignLinesConfig() {
		return signLinesConfig;
	}

	public void setSignLinesConfig(String signLinesConfig) {
		this.signLinesConfig = signLinesConfig;
	}

	public String getUploadedDocsError() {
		return uploadedDocsError;
	}

	public void setUploadedDocsError(String uploadedDocsError) {
		this.uploadedDocsError = uploadedDocsError;
	}

	public String getSigner() {
		return signer;
	}

	public void setSigner(String signer) {
		this.signer = signer;
	}

	public String getReferenceLengthError() {
		return referenceLengthError;
	}

	public void setReferenceLengthError(String referenceLengthError) {
		this.referenceLengthError = referenceLengthError;
	}

	public String getSignlinesType() {
		return signlinesType;
	}

	public void setSignlinesType(String signlinesType) {
		this.signlinesType = signlinesType;
	}

	public String getEmailEmptyError() {
		return emailEmptyError;
	}

	public void setEmailEmptyError(String emailEmptyError) {
		this.emailEmptyError = emailEmptyError;
	}

	public String getSubjectLengthError() {
		return subjectLengthError;
	}

	public void setSubjectLengthError(String subjectLengthError) {
		this.subjectLengthError = subjectLengthError;
	}

	public String getEmailPatternError() {
		return emailPatternError;
	}

	public void setEmailPatternError(String emailPatternError) {
		this.emailPatternError = emailPatternError;
	}

	public String getSignlinesAccion() {
		return signlinesAccion;
	}

	public void setSignlinesAccion(String signlinesAccion) {
		this.signlinesAccion = signlinesAccion;
	}

}
