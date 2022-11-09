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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "spUrl","spReturn" })
@XmlRootElement(name = "solicitudAccesoClave")
public class SolicitudAccesoClave {

	@XmlElement(required = false)
	protected String spUrl;
	
	@XmlElement(required = false)
	protected String spReturn;

	public String getSpUrl() {
		return spUrl;
	}

	public void setSpUrl(String spUrl) {
		this.spUrl = spUrl;
	}

	public String getSpReturn() {
		return spReturn;
	}

	public void setSpReturn(String spReturn) {
		this.spReturn = spReturn;
	}
}



