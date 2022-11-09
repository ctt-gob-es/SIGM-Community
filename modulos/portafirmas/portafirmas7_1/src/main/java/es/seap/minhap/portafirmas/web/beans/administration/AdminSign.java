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

package es.seap.minhap.portafirmas.web.beans.administration;

import es.seap.minhap.portafirmas.domain.RequestTagListDTO;
import es.seap.minhap.portafirmas.web.beans.Paginator;

public class AdminSign {

	private String fValidez;
	
	private Paginator paginatorResign;
	
	private RequestTagListDTO data;
	
	private String idSign;
	
	private Integer applicationMode;
	
	private String mensajeError;

	public String getfValidez() {
		return fValidez;
	}

	public void setfValidez(String fValidez) {
		this.fValidez = fValidez;
	}

	public Paginator getPaginatorResign() {
		return paginatorResign;
	}

	public void setPaginatorResign(Paginator paginatorResign) {
		this.paginatorResign = paginatorResign;
	}

	public RequestTagListDTO getData() {
		return data;
	}

	public void setData(RequestTagListDTO data) {
		this.data = data;
	}

	public String getIdSign() {
		return idSign;
	}

	public void setIdSign(String idSign) {
		this.idSign = idSign;
	}

	public Integer getApplicationMode() {
		return applicationMode;
	}

	public void setApplicationMode(Integer applicationMode) {
		this.applicationMode = applicationMode;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	
}
