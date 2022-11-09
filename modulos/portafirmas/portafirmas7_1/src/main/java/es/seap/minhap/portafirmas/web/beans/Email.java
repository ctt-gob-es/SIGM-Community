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

import java.util.List;

public class Email {
	private String target;
	private List<String> targets;
	private String requestId;
	private String chkboxFirma;
	private String chkboxInforme;
	private String chkboxNormalizado;
	
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	public List<String> getTargets() {
		return targets;
	}
	public void setTargets(List<String> targets) {
		this.targets = targets;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getChkboxFirma() {
		return chkboxFirma;
	}
	public void setChkboxFirma(String chkboxFirma) {
		this.chkboxFirma = chkboxFirma;
	}
	public String getChkboxInforme() {
		return chkboxInforme;
	}
	public void setChkboxInforme(String chkboxInforme) {
		this.chkboxInforme = chkboxInforme;
	}
	public String getChkboxNormalizado() {
		return chkboxNormalizado;
	}
	public void setChkboxNormalizado(String chkboxNormalizado) {
		this.chkboxNormalizado = chkboxNormalizado;
	}

}