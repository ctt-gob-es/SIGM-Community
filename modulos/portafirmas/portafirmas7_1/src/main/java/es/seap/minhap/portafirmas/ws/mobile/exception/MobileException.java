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

package es.seap.minhap.portafirmas.ws.mobile.exception;

import javax.xml.ws.WebFault;

import es.seap.minhap.portafirmas.ws.mobile.bean.MobileError;

@WebFault(name = "mobileError", targetNamespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0")
public class MobileException extends Exception {

	/**
	 * Class version number.
	 */
	public static final long serialVersionUID = 20090817122839L;

	private MobileError faultBean;

	/**
	 * Sole constructor.
	 */
	public MobileException() {
		super();
	}

	/**
	 * Constructor with parameters.
	 * 
	 * @param message
	 *            Exception message.
	 */
	public MobileException(String message) {
		super(message);
	}

	/**
	 * Constructor with parameters.
	 * 
	 * @param message
	 *            Exception message.
	 * @param cause
	 *            Exception cause.
	 */
	public MobileException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor with parameters.
	 * 
	 * @param message
	 *            Exception message.
	 * @param exceptionInfo
	 *            Exception info.
	 */
	public MobileException(String message, MobileError error) {
		super(message);
		this.faultBean = error;
	}

	/**
	 * Constructor with parameters.
	 * 
	 * @param message
	 *            Exception message.
	 * @param cause
	 *            Exception cause.
	 * @param exceptionInfo
	 *            Exception info.
	 */
	public MobileException(String message, MobileError error,
			Throwable cause) {
		super(message, cause);
		this.faultBean = error;
	}

	/**
	 * Return ws fault information.
	 * 
	 * @return Fault information.
	 */
	public MobileError getFaultInfo() {
		return this.faultBean;
	}
}
