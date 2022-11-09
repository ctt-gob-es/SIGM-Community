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

import java.util.Date;
import java.util.List;

public class RequestSignature {

	private String state;
	private String requestTagHash;
	private List<DocumentSignature> signatures;
	private String validarFirma;
	private Date fechaValidezCertificado;
	private boolean paralela;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<DocumentSignature> getSignatures() {
		return signatures;
	}

	public void setSignatures(List<DocumentSignature> signatures) {
		this.signatures = signatures;
	}

	public String getRequestTagHash() {
		return requestTagHash;
	}

	public void setRequestTagHash(String requestTagHash) {
		this.requestTagHash = requestTagHash;
	}

	public String getValidarFirma() {
		return validarFirma;
	}

	public void setValidarFirma(String validarFirma) {
		this.validarFirma = validarFirma;
	}

	public Date getFechaValidezCertificado() {
		return fechaValidezCertificado;
	}

	public void setFechaValidezCertificado(Date fechaValidezCertificado) {
		this.fechaValidezCertificado = fechaValidezCertificado;
	}

	public boolean isParalela() {
		return paralela;
	}

	public void setParalela(boolean paralela) {
		this.paralela = paralela;
	}

}
