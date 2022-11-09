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

package es.seap.minhap.portafirmas.utils;

public class SignData {

	private String tipoMime;
	private byte[] document;
	private String tipoFirma;
	
	public SignData(String tipoMime, byte[] document) {
		super();
		this.tipoMime = tipoMime;
		this.document = document;
	}
	public String getTipoMime() {
		return tipoMime;
	}
	public void setTipoMime(String tipoMime) {
		this.tipoMime = tipoMime;
	}
	public byte[] getDocument() {
		return document;
	}
	public void setDocument(byte[] document) {
		this.document = document;
	}
	public String getTipoFirma() {
		return tipoFirma;
	}
	public void setTipoFirma(String tipoFirma) {
		this.tipoFirma = tipoFirma;
	}
	
	
}
