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

package es.seap.minhap.portafirmas.web.beans.fire;

import java.util.List;
import java.util.Map;

import es.seap.minhap.portafirmas.web.beans.signature.RequestSignatureConfig;

public class FireTransaction {
	
	private String[] hashes;
	private String transactionId;
	private String urlRedirect;
	private Map<String, String> mapVistoBueno;
	private Map<String, String> mapFormatoFirma;
	private List<RequestSignatureConfig> signatureConfigList;
	
	public String[] getHashes() {
		return hashes;
	}
	public void setHashes(String[] hashes) {
		this.hashes = hashes;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getUrlRedirect() {
		return urlRedirect;
	}
	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}
	public Map<String, String> getMapVistoBueno() {
		return mapVistoBueno;
	}
	public void setMapVistoBueno(Map<String, String> mapVistoBueno) {
		this.mapVistoBueno = mapVistoBueno;
	}
	public Map<String, String> getMapFormatoFirma() {
		return mapFormatoFirma;
	}
	public void setMapFormatoFirma(Map<String, String> mapFormatoFirma) {
		this.mapFormatoFirma = mapFormatoFirma;
	}
	public List<RequestSignatureConfig> getSignatureConfigList() {
		return signatureConfigList;
	}
	public void setSignatureConfigList(List<RequestSignatureConfig> signatureConfigList) {
		this.signatureConfigList = signatureConfigList;
	}

}
