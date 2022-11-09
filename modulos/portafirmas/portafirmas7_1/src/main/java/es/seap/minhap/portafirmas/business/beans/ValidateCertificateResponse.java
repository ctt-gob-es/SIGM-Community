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

package es.seap.minhap.portafirmas.business.beans;

import java.util.Date;

/**
 * @author domingo
 *
 */
public class ValidateCertificateResponse {
	
	private boolean error;
	private boolean valido;	
	private String mensaje;
	private String mensajeAmpliado;
	private String nifCif;
	private String numeroSerie;
	
	private Date dateValidSign;
	
	private boolean seudonimo = false;

	
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public boolean isValido() {
		return valido;
	}
	public void setValido(boolean valido) {
		this.valido = valido;
	}	
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje (String mensaje) {
		this.mensaje = mensaje;
	}	
	public String getMensajeAmpliado() {
		return mensajeAmpliado;
	}
	public void setMensajeAmpliado(String mensajeAmpliado) {
		this.mensajeAmpliado = mensajeAmpliado;
	}
	public String getNifCif() {
		return nifCif;
	}
	public void setNifCif(String nifCif) {
		this.nifCif = nifCif;
	}
	public String getNumeroSerie() {
		return numeroSerie;
	}
	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
	
	public Date getDateValidSign() {
		return dateValidSign;
	}
	public void setDateValidSign(Date dateValidSign) {
		this.dateValidSign = dateValidSign;
	}
	public boolean isSeudonimo() {
		return seudonimo;
	}
	public void setSeudonimo(boolean seudonimo) {
		this.seudonimo = seudonimo;
	}
	
}
