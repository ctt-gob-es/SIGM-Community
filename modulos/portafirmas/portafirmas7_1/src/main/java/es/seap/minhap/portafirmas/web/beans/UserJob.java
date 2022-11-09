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

public class UserJob {

	private String userJobPk;
	private String jobId;
	private String userId;
	private String fStart;
	private String fEnd;

	public String getUserJobPk() {
		return userJobPk;
	}
	public void setUserJobPk(String userJobPk) {
		this.userJobPk = userJobPk;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	
}
