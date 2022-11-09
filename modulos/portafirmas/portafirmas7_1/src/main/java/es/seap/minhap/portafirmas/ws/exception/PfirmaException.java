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

package es.seap.minhap.portafirmas.ws.exception;

import javax.xml.ws.WebFault;

import es.seap.minhap.portafirmas.ws.bean.ExceptionInfo;



/**
 * Exception thrown if ws returns an error.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@WebFault(name = "exceptionInfo", targetNamespace = "urn:juntadeandalucia:cice:pfirma:type:v2.0")
public class PfirmaException extends Exception {

	/**
	 * Class version number.
	 */
	public static final long serialVersionUID = 20090817122839L;

	private ExceptionInfo exceptionInfo;

	/**
	 * Sole constructor.
	 */
	public PfirmaException() {
		super();
	}

	/**
	 * Constructor with parameters.
	 * 
	 * @param message
	 *            Exception message.
	 */
	public PfirmaException(String message) {
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
	public PfirmaException(String message, Throwable cause) {
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
	public PfirmaException(String message, ExceptionInfo exceptionInfo) {
		super(message);
		this.exceptionInfo = exceptionInfo;
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
	public PfirmaException(String message, ExceptionInfo exceptionInfo,
			Throwable cause) {
		super(message, cause);
		this.exceptionInfo = exceptionInfo;
	}

	/**
	 * Return ws fault information.
	 * 
	 * @return Fault information.
	 */
	public ExceptionInfo getFaultInfo() {
		return this.exceptionInfo;
	}
}
