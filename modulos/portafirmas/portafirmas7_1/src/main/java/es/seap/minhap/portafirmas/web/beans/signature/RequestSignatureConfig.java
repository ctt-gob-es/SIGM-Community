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

package es.seap.minhap.portafirmas.web.beans.signature;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class RequestSignatureConfig {

	private String type;
	private String requestTagHash; 
	private List<DocumentSignatureConfig> documentsConfig;
	private String errorMessage;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRequestTagHash() {
		return requestTagHash;
	}

	public void setRequestTagHash(String requestTagHash) {
		this.requestTagHash = requestTagHash;
	}

	public List<DocumentSignatureConfig> getDocumentsConfig() {
		return documentsConfig;
	}

	public void setDocumentsConfig(List<DocumentSignatureConfig> documentsConfig) {
		this.documentsConfig = documentsConfig;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
