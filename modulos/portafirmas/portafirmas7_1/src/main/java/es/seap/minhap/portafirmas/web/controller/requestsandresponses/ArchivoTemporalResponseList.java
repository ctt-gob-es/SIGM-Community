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
import java.util.List;

public class ArchivoTemporalResponseList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ArchivoTemporalResponse> listArchivoTemporalResponse;
	private String peticionAsociada;
	private String comentarioDeAnexos;
	public List<ArchivoTemporalResponse> getListArchivoTemporalResponse() {
		return listArchivoTemporalResponse;
	}
	public void setListArchivoTemporalResponse(List<ArchivoTemporalResponse> listArchivoTemporalResponse) {
		this.listArchivoTemporalResponse = listArchivoTemporalResponse;
	}
	public String getPeticionAsociada() {
		return peticionAsociada;
	}
	public void setPeticionAsociada(String peticionAsociada) {
		this.peticionAsociada = peticionAsociada;
	}
	public String getComentarioDeAnexos() {
		return comentarioDeAnexos;
	}
	public void setComentarioDeAnexos(String comentarioDeAnexos) {
		this.comentarioDeAnexos = comentarioDeAnexos;
	}
	public ArchivoTemporalResponseList(List<ArchivoTemporalResponse> listArchivoTemporalResponse,
			String peticionAsociada, String comentarioDeAnexos) {
		super();
		this.listArchivoTemporalResponse = listArchivoTemporalResponse;
		this.peticionAsociada = peticionAsociada;
		this.comentarioDeAnexos = comentarioDeAnexos;
	}
	public ArchivoTemporalResponseList() {
		super();
	}
	
	
	
}
