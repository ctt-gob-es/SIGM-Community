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

public class ArchivoTemporalRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String contenidoDeArchivo;
	private String nombreDeArchivo;
	private String carpetaTemporal;
	private String tipoArchivo;
	public String getContenidoDeArchivo() {
		return contenidoDeArchivo;
	}
	public void setContenidoDeArchivo(String contenidoDeArchivo) {
		this.contenidoDeArchivo = contenidoDeArchivo;
	}
	public String getNombreDeArchivo() {
		return nombreDeArchivo;
	}
	public void setNombreDeArchivo(String nombreDeArchivo) {
		this.nombreDeArchivo = nombreDeArchivo;
	}
	public String getCarpetaTemporal() {
		return carpetaTemporal;
	}
	public void setCarpetaTemporal(String carpetaTemporal) {
		this.carpetaTemporal = carpetaTemporal;
	}
	public String getTipoArchivo() {
		return tipoArchivo;
	}
	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}
	public ArchivoTemporalRequest(String contenidoDeArchivo, String nombreDeArchivo, String carpetaTemporal,
			String tipoArchivo) {
		super();
		this.contenidoDeArchivo = contenidoDeArchivo;
		this.nombreDeArchivo = nombreDeArchivo;
		this.carpetaTemporal = carpetaTemporal;
		this.tipoArchivo = tipoArchivo;
	}
	public ArchivoTemporalRequest() {
		super();
	}
	
	
}
