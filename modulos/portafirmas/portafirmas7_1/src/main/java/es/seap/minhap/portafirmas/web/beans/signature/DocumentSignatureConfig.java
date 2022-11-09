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

public class DocumentSignatureConfig {

	private String mode;
	private String data;
	private String format;
	private String operation;
	private String documentHash;
	private String hashAlgorithm;
	private String signParameter;
	private String signFormatParameter;
	private boolean selloTiempo;
	private boolean xades;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	public String getDocumentHash() {
		return documentHash;
	}

	public void setDocumentHash(String documentHash) {
		this.documentHash = documentHash;
	}

	public String getSignParameter() {
		return signParameter;
	}

	public void setSignParameter(String signParameter) {
		this.signParameter = signParameter;
	}

	public String getSignFormatParameter() {
		return signFormatParameter;
	}

	public void setSignFormatParameter(String signFormatParameter) {
		this.signFormatParameter = signFormatParameter;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean isSelloTiempo() {
		return selloTiempo;
	}

	public void setSelloTiempo(boolean selloTiempo) {
		this.selloTiempo = selloTiempo;
	}

	public boolean isXades() {
		return xades;
	}

	public void setXades(boolean xades) {
		this.xades = xades;
	}

}
