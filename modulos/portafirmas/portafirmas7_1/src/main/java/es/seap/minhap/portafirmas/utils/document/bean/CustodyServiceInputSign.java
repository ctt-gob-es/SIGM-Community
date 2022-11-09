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

package es.seap.minhap.portafirmas.utils.document.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CustodyServiceInputSign implements Serializable {
	private static final long serialVersionUID = 1L;

	private String identifier;
	private List<CustodyServiceOutputDocument> documents;
	private Date date;
	private String transaction;
	private String userIdentifier;
	private String userFullName;
	private String applicationServer;
	private String server;
	private String format;
	private String type;
	private BigDecimal size;
	private String refNasDir3;
	private String idEni;

	public BigDecimal getSize() {
		return size;
	}

	public void setSize(BigDecimal size) {
		this.size = size;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getApplicationServer() {
		return applicationServer;
	}

	public void setApplicationServer(String applicationServer) {
		this.applicationServer = applicationServer;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public List<CustodyServiceOutputDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<CustodyServiceOutputDocument> documents) {
		this.documents = documents;
	}

	public String getRefNasDir3() {
		return refNasDir3;
	}

	public void setRefNasDir3(String refNasDir3) {
		this.refNasDir3 = refNasDir3;
	}

	public String getIdEni() {
		return idEni;
	}

	public void setIdEni(String idEni) {
		this.idEni = idEni;
	}

}
