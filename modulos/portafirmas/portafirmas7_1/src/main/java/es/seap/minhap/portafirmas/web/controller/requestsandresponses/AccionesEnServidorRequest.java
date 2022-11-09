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

public class AccionesEnServidorRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accion;
	private boolean notificarServidor;
	private Map<String, Object> parametros;
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public boolean isNotificarServidor() {
		return notificarServidor;
	}
	public void setNotificarServidor(boolean notificarServidor) {
		this.notificarServidor = notificarServidor;
	}
	public Map<String, Object> getParametros() {
		return parametros;
	}
	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}
	public AccionesEnServidorRequest(String accion, boolean notificarServidor, Map<String, Object> parametros) {
		super();
		this.accion = accion;
		this.notificarServidor = notificarServidor;
		this.parametros = parametros;
	}
	public AccionesEnServidorRequest() {
		super();
	}
	@Override
	public String toString() {
		return "AccionesEnServidorRequest [accion=" + accion + ", notificarServidor=" + notificarServidor
				+ ", parametros=" + parametros + "]";
	} 
	
}
