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

/**
 * Exception class for DOCEL cliente errors.
 */
package es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.business.exception;

/**
 * @author MINHAP
 * Exception class for DOCEL cliente errors.
 */
public class NotificacionGenericaClientException extends Exception {

	/**
	 * The serial version OID.
	 */
	private static final long serialVersionUID = -2124028757129739900L;

	/**
	 * Default constructor.
	 */
	public NotificacionGenericaClientException() {
	}

	/**
	 * Create a new Exception with the given message.
	 * @param message The message
	 */
	public NotificacionGenericaClientException(String message) {
		super(message);
	}

	/**
	 * Create a new Exception with the given exception stack.
	 * @param cause The exception stack
	 */
	public NotificacionGenericaClientException(Throwable cause) {
		super(cause);
	}

	/**
	 * Create a new Exception with the given message and exception stack.
	 * @param message The message
	 * @param cause The excetion stack
	 */
	public NotificacionGenericaClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
