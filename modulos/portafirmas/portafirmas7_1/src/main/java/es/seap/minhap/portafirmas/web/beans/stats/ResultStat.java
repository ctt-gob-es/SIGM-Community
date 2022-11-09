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

package es.seap.minhap.portafirmas.web.beans.stats;

public class ResultStat {
	
	private String year;
	private String month;
	private String requestsNumber;
	private String seat;
	private String userName;
	private String application;
	private String signaturesNumber;
	private String usersNumber;
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getRequestsNumber() {
		return requestsNumber;
	}
	public void setRequestsNumber(String requestsNumber) {
		this.requestsNumber = requestsNumber;
	}
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getSignaturesNumber() {
		return signaturesNumber;
	}
	public void setSignaturesNumber(String signaturesNumber) {
		this.signaturesNumber = signaturesNumber;
	}
	public String getUsersNumber() {
		return usersNumber;
	}
	public void setUsersNumber(String usersNumber) {
		this.usersNumber = usersNumber;
	}

}
