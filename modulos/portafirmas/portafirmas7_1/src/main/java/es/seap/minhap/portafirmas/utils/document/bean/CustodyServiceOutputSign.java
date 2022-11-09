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

public class CustodyServiceOutputSign implements Serializable {
	private static final long serialVersionUID = 1L;

	private String identifier;
	private String uri;
	private String type;
	private String refNasDir3;
	private String idEni;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
