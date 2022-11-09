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

public class StatsTotals {
	
	private String seatsNumber;
	private String usersNumber;
	private String yearsNumber;
	private String signaturesNumber;
	private String requestsNumber;
	private String applicationsNumber;
	
	public StatsTotals() {
		super();
		seatsNumber = "";
		usersNumber = "";
		yearsNumber = "";
		signaturesNumber = "";
		requestsNumber = "";
		applicationsNumber = "";
	}
	public String getSeatsNumber() {
		return seatsNumber;
	}
	public void setSeatsNumber(String seatsNumber) {
		this.seatsNumber = seatsNumber;
	}
	public String getUsersNumber() {
		return usersNumber;
	}
	public void setUsersNumber(String usersNumber) {
		this.usersNumber = usersNumber;
	}
	public String getYearsNumber() {
		return yearsNumber;
	}
	public void setYearsNumber(String yearsNumber) {
		this.yearsNumber = yearsNumber;
	}
	public String getSignaturesNumber() {
		return signaturesNumber;
	}
	public void setSignaturesNumber(String signaturesNumber) {
		this.signaturesNumber = signaturesNumber;
	}
	public String getRequestsNumber() {
		return requestsNumber;
	}
	public void setRequestsNumber(String requestsNumber) {
		this.requestsNumber = requestsNumber;
	}
	public String getApplicationsNumber() {
		return applicationsNumber;
	}
	public void setApplicationsNumber(String applicationsNumber) {
		this.applicationsNumber = applicationsNumber;
	}
	
	
		

}
