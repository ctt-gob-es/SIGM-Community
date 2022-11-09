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

package es.seap.minhap.portafirmas.exceptions;

/**
 * @author domingo
 *
 */
public class CertificateException extends Exception {

	private static final long serialVersionUID = 1L;
	private String code;
	
	// Códigos y mensajes de error
	public static final String COD_000 = "COD_000";
	public static final String MESSAGE_000 = "Error inesperado. Consulte con el administrador";
	public static final String COD_001 = "COD_001";
	public static final String MESSAGE_001 = "El servicio de validar certificado no está disponible. Inténtelo más tarde.";
	public static final String COD_002 = "COD_002";
	public static final String MESSAGE_002 = "El servicio de validar certificado no está disponible. Inténtelo más tarde.";
	public static final String COD_003 = "COD_003";
	public static final String MESSAGE_003 = "Error al validar certificado por EEUtil.";
	public static final String COD_004 = "COD_004";
	public static final String MESSAGE_004 = "Error al validar certificado por @Firma";
	
	public CertificateException (String code) {
		super();
		this.setCode(code);
	}
		
	public CertificateException (String message, String code) {
		super(message);
		this.setCode(code);
	}
	
	public CertificateException (String message, String code, Throwable t) {
		super (message, t);
		this.setCode(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
	