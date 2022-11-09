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

import es.seap.minhap.portafirmas.ws.mobile.bean.MobileAccesoClave;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "solicitudAccesoClaveRespuesta"
})
@XmlRootElement(name = "solicitudAccesoClaveResponse")
public class SolicitudAccesoClaveResponse {
	@XmlElement(required = true)
    protected MobileAccesoClave solicitudAccesoClaveRespuesta;

	public MobileAccesoClave getSolicitudAccesoClaveRespuesta() {
		return solicitudAccesoClaveRespuesta;
	}

	public void setSolicitudAccesoClaveRespuesta(MobileAccesoClave solicitudAccesoClaveRespuesta) {
		this.solicitudAccesoClaveRespuesta = solicitudAccesoClaveRespuesta;
	}

    
}



