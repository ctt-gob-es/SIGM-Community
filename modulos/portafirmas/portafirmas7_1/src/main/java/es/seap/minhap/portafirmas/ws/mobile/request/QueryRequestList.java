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

package es.seap.minhap.portafirmas.ws.mobile.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.mobile.bean.MobileRequestFilterList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileStringList;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "certificate", "state", "initPage", "pageSize", "signFormats", "filters" })
@XmlRootElement(name = "queryRequestList")
public class QueryRequestList {

	@XmlElement(required = true)
	protected String certificate;
	@XmlElement(required = true)
	protected String state;
	@XmlElement(required = true)
	protected String initPage;
	@XmlElement(required = true)
	protected String pageSize;
	@XmlElement(required = true)
	protected MobileStringList signFormats;
	@XmlElement(required = true)
	protected MobileRequestFilterList filters;

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public MobileStringList getSignFormats() {
		return signFormats;
	}

	public void setSignFormats(MobileStringList signFormats) {
		this.signFormats = signFormats;
	}

	public MobileRequestFilterList getFilters() {
		return filters;
	}

	public void setFilters(MobileRequestFilterList filters) {
		this.filters = filters;
	}

	public String getInitPage() {
		return initPage;
	}

	public void setInitPage(String initPage) {
		this.initPage = initPage;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

}
