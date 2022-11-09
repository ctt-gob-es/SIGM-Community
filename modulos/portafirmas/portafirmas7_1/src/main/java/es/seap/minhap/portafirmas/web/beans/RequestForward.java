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

/**
 * @author juanmanuel.delgado
 *
 */
public class RequestForward {

	private String requestId;
	private PfRequestsDTO request;
	private String signer;
	private String signers;
	private String signLinesConfig;
	private String signlinesAccion;
	private String action;
	private String signlinesType;
	private String uploadedDocsError;
	
	private String nameCheckDeNuevosComentariosYAnexos;
	private String idCheckDeNuevosComentariosYAnexos;

	public String getNameCheckDeNuevosComentariosYAnexos() {
		return nameCheckDeNuevosComentariosYAnexos;
	}


	public void setNameCheckDeNuevosComentariosYAnexos(String nameCheckDeNuevosComentariosYAnexos) {
		this.nameCheckDeNuevosComentariosYAnexos = nameCheckDeNuevosComentariosYAnexos;
	}


	public String getIdCheckDeNuevosComentariosYAnexos() {
		return idCheckDeNuevosComentariosYAnexos;
	}


	public void setIdCheckDeNuevosComentariosYAnexos(String idCheckDeNuevosComentariosYAnexos) {
		this.idCheckDeNuevosComentariosYAnexos = idCheckDeNuevosComentariosYAnexos;
	}


	/**
	 * Constructor vacío
	 */
	public RequestForward() {
	}


	public String getRequestId() {
		return requestId;
	}


	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}


	public PfRequestsDTO getRequest() {
		return request;
	}


	public void setRequest(PfRequestsDTO request) {
		this.request = request;
	}


	public String getSigner() {
		return signer;
	}


	public void setSigner(String signer) {
		this.signer = signer;
	}


	public String getSigners() {
		return signers;
	}


	public void setSigners(String signers) {
		this.signers = signers;
	}


	public String getSignLinesConfig() {
		return signLinesConfig;
	}


	public void setSignLinesConfig(String signLinesConfig) {
		this.signLinesConfig = signLinesConfig;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public String getSignlinesType() {
		return signlinesType;
	}


	public void setSignlinesType(String signlinesType) {
		this.signlinesType = signlinesType;
	}


	public String getUploadedDocsError() {
		return uploadedDocsError;
	}


	public void setUploadedDocsError(String uploadedDocsError) {
		this.uploadedDocsError = uploadedDocsError;
	}


	public String getSignlinesAccion() {
		return signlinesAccion;
	}


	public void setSignlinesAccion(String signlinesAccion) {
		this.signlinesAccion = signlinesAccion;
	}



}
