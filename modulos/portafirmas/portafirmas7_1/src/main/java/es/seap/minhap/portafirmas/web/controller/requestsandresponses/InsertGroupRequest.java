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

package es.seap.minhap.portafirmas.web.controller.requestsandresponses;

import java.io.Serializable;

import es.seap.minhap.portafirmas.web.beans.Group;

public class InsertGroupRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Group group;
	private Boolean ignoreWarnings;
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Boolean getIgnoreWarnings() {
		if(ignoreWarnings==null)
			ignoreWarnings = Boolean.FALSE;
		return ignoreWarnings;
	}
	public void setIgnoreWarnings(Boolean ignoreWarnings) {
		this.ignoreWarnings = ignoreWarnings;
	}
	public InsertGroupRequest(Group group, Boolean ignoreWarnings) {
		super();
		this.group = group;
		this.ignoreWarnings = ignoreWarnings;
	}
	public InsertGroupRequest() {
		super();
	}
	
	
	
}
