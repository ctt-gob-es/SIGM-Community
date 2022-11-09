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

package es.seap.minhap.portafirmas.business.ws.legacy.remotesign;

import java.sql.Timestamp;

public class RemoteSignDocument {
	private String hash;
	private Timestamp initSignDate;
	private String idTransaction;
	private String dataToSign;
	private String idUser;

	public RemoteSignDocument() {
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Timestamp getInitSignDate() {
		return initSignDate;
	}

	public void setInitSignDate(Timestamp initSignDate) {
		this.initSignDate = initSignDate;
	}

	public String getIdTransaction() {
		return idTransaction;
	}

	public void setIdTransaction(String idTransaction) {
		this.idTransaction = idTransaction;
	}

	public String getDataToSign() {
		return dataToSign;
	}

	public void setDataToSign(String dataToSign) {
		this.dataToSign = dataToSign;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

}