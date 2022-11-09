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

public class ParameterStat {

	private String format;
	private String reqSig;
	private String type;
	private String totDesg;
	private String fStart;
	private String fEnd;
	private String seat;
	private String application;
	private String user;
	private String userName;
	
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getReqSig() {
		return reqSig;
	}
	public void setReqSig(String reqSig) {
		this.reqSig = reqSig;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTotDesg() {
		return totDesg;
	}
	public void setTotDesg(String totDesg) {
		this.totDesg = totDesg;
	}
	public String getfStart() {
		return fStart;
	}
	public void setfStart(String fStart) {
		this.fStart = fStart;
	}
	public String getfEnd() {
		return fEnd;
	}
	public void setfEnd(String fEnd) {
		this.fEnd = fEnd;
	}
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
