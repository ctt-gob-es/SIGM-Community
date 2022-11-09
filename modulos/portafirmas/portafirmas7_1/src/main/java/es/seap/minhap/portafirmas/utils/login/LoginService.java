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

public interface LoginService {
	/**
	 * Devuelve una respuesta de conexi&oacute;n al servicio de login,
	 * este servicio puede ser por ejemplo un ldap o una base de datos,
	 * chequea si el usuario puede conectarse con el id y la password 
	 * que se pasa como par&aacute;metro.
	 * @param user id del usuario
	 * @param password la password del usuario
	 * @return el objeto de respuesta del servicio
	 */
	public LoginServiceResponse check(String user,String password);

}
