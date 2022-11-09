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

public class Storage {
	
	private String storage;
	private String remitter;
	private String application;
	private String sign;
	private String month;
	private String year;
	private String[] requestIds;
	
	private Paginator paginatorPf;
	private Paginator paginatorHist;

	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}
	public String getRemitter() {
		return remitter;
	}
	public void setRemitter(String remitter) {
		this.remitter = remitter;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String[] getRequestIds() {
		return requestIds;
	}
	public void setRequestIds(String[] requestIds) {
		this.requestIds = requestIds;
	}
	public Paginator getPaginatorPf() {
		return paginatorPf;
	}
	public void setPaginatorPf(Paginator paginatorPf) {
		this.paginatorPf = paginatorPf;
	}
	public Paginator getPaginatorHist() {
		return paginatorHist;
	}
	public void setPaginatorHist(Paginator paginatorHist) {
		this.paginatorHist = paginatorHist;
	}
	
}
