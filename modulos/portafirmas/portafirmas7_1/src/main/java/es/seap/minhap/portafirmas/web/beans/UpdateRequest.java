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

public class UpdateRequest {

	private String job_name;
	private String job_group;
	private String description;
	private String job_class_name;
	private boolean durable;
	private boolean is_volatile;
	private boolean stateful;
	private boolean request_recovery;
	private String job_data;
	
	public UpdateRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public String getJob_group() {
		return job_group;
	}

	public void setJob_group(String job_group) {
		this.job_group = job_group;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJob_class_name() {
		return job_class_name;
	}

	public void setJob_class_name(String job_class_name) {
		this.job_class_name = job_class_name;
	}

	public boolean isDurable() {
		return durable;
	}

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public boolean isIs_volatile() {
		return is_volatile;
	}

	public void setIs_volatile(boolean is_volatile) {
		this.is_volatile = is_volatile;
	}

	public boolean isStateful() {
		return stateful;
	}

	public void setStateful(boolean stateful) {
		this.stateful = stateful;
	}

	public boolean isRequest_recovery() {
		return request_recovery;
	}

	public void setRequest_recovery(boolean request_recovery) {
		this.request_recovery = request_recovery;
	}

	public String getJob_data() {
		return job_data;
	}

	public void setJob_data(String job_data) {
		this.job_data = job_data;
	}
	
	
	
}
