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

package es.seap.minhap.portafirmas.utils.login;
/**
 * Contiene la informaci&oacute;n de respuesta de un servicio de conexi&oacute;n de login de usuario.
 * @author daniel.palacios
 *
 */
public class LoginServiceResponse {
	
	private int status;
	private String identifier;
	/**
	 * Retorna el c&oacute;digo de estado http de la petici&oacute;n que se ha realizado al servicio de login
	 * @return el c&oacute;digo de estado del servicio al que se realiza la llamada
	 */
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * Si la conexi&oacute;n se ha realizado con exito se retorna el identificador
	 * del usuario, en caso contrario no se rellena este campo
	 * @return el identificador del Usuario del servicio
	 */
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

}
