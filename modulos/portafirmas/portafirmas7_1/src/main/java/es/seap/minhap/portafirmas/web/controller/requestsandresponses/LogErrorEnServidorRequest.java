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
import java.util.Map;

public class LogErrorEnServidorRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, AccionesEnServidorRequest> accionesEnServidor;
	
	private String mensajeJS;
	private String urlDeError;
	private String lineaDeError;
	private String columnaDeError;
	private String error;

	public String getMensajeJS() {
		return mensajeJS;
	}
	public void setMensajeJS(String mensajeJS) {
		this.mensajeJS = mensajeJS;
	}
	public String getUrlDeError() {
		return urlDeError;
	}
	public void setUrlDeError(String urlDeError) {
		this.urlDeError = urlDeError;
	}
	public String getLineaDeError() {
		return lineaDeError;
	}
	public void setLineaDeError(String lineaDeError) {
		this.lineaDeError = lineaDeError;
	}
	public String getColumnaDeError() {
		return columnaDeError;
	}
	public void setColumnaDeError(String columnaDeError) {
		this.columnaDeError = columnaDeError;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public LogErrorEnServidorRequest() {
		super();
	}
	@Override
	public String toString() {
		return "LogErrorEnServidorRequest [accionesEnServidor=" + accionesEnServidor + ", mensajeJS=" + mensajeJS
				+ ", urlDeError=" + urlDeError + ", lineaDeError=" + lineaDeError + ", columnaDeError=" + columnaDeError
				+ ", error=" + error + "]";
	}
	public Map<String, AccionesEnServidorRequest> getAccionesEnServidor() {
		return accionesEnServidor;
	}
	public void setAccionesEnServidor(Map<String, AccionesEnServidorRequest> accionesEnServidor) {
		this.accionesEnServidor = accionesEnServidor;
	}
	public LogErrorEnServidorRequest(Map<String, AccionesEnServidorRequest> accionesEnServidor, String mensajeJS,
			String urlDeError, String lineaDeError, String columnaDeError, String error) {
		super();
		this.accionesEnServidor = accionesEnServidor;
		this.mensajeJS = mensajeJS;
		this.urlDeError = urlDeError;
		this.lineaDeError = lineaDeError;
		this.columnaDeError = columnaDeError;
		this.error = error;
	}
	
	
	
}
