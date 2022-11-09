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

package es.seap.minhap.portafirmas.web.beans.fire;

import java.util.List;

public class FireRequest {
	
	//Código hash de la peticion
	private String id;
	private String asunto;
	private List <FireDocument> documentos;
	private String errorPeticion;
	private String remitters;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	public List<FireDocument> getDocumentos() {
		return documentos;
	}
	public void setDocumentos(List<FireDocument> documentos) {
		this.documentos = documentos;
	}
	public String getErrorPeticion() {
		return errorPeticion;
	}
	public void setErrorPeticion(String errorPeticion) {
		this.errorPeticion = errorPeticion;
	}
	public String getRemitters() {
		return remitters;
	}
	public void setRemitters(String remitters) {
		this.remitters = remitters;
	}
	public boolean isContieneErrores() {
		boolean hayErrores = false;
		if(errorPeticion != null) {
			hayErrores = true;
		} else {
			if(documentos != null) {
				for (FireDocument fireDocument : documentos) {
					if(fireDocument.isContieneErrores()) {
						hayErrores = true;
						break;
					}
				}
			}
		}
		return hayErrores;
	}

}
