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

package es.seap.minhap.portafirmas.ws.afirma5;

public class Afirma5Constantes {
	
	public static String AFIRMA_VALIDAR_CERTIFICADO_SERVICE = "/ValidarCertificado?wsdl";
	public static String AFIRMA_VALIDAR_FIRMA_SERVICE = "/ValidarFirma?wsdl";
	public static String AFIRMA_DSS_AFIRMA_VERIFY_SERVICE = "/DSSAfirmaVerify?wsdl";
	
	public static String AFIRMA_VALIDAR_CERTIFICADO_OPERACION = "ValidarCertificado";
	public static String AFIRMA_VALIDAR_FIRMA_OPERACION = "ValidarFirma";
	public static String AFIRMA_CAMPO_NIF_RESPONSABLE = "NIFResponsable";
	public static String AFIRMA_CAMPO_NIF_CIF = "NIF-CIF";
	public static String AFIRMA_CAMPO_NUMERO_SERIE = "numeroSerie";
	public static String AFIRMA_CAMPO_SEUDONIMO = "seudonimo";
	
	public static final String AFIRMA_CAMPO_FECHA_VALIDEZ_CERTIFICADO = "validoHasta";
	public static final String AFIRMA_CAMPO_FECHA_VALIDEZ_CERTIFICADO_FORMATO = "yyyy-MM-dd";
	
	public static String AFIRMA_VERSION_MENSAJE = "1.0";
	public static Integer AFIRMA_NIVEL_VALIDACION = 2;
	
	public static String FIRMA_SECURITY_USER = "FIRMA.SECURITY.USER";
	public static String FIRMA_SECURITY_PASSWORD = "FIRMA.SECURITY.PASSWORD";
	public static String FIRMA_SECURITY_PASSWORD_TYPE = "FIRMA.SECURITY.PASSWORD.TYPE";
	public static String FIRMA_SECURITY_MODE = "FIRMA.SECURITY.MODE";
	public static String FIRMA_APLICACION = "FIRMA.APLICACION";
	public static String FIRMA_TRUSTEDSTORE = "FIRMA.TRUSTEDSTORE";
	public static String FIRMA_TRUSTEDSTORE_PASS = "FIRMA.TRUSTEDSTORE.PASS";
	public static String FIRMA_URL = "FIRMA.URL";
	public static String FIRMA_SECURITY_CERT_ALIAS = "FIRMA.SECURITY.CERT.ALIAS";
	public static String FIRMA_SECURITY_CERT_PWD = "FIRMA.SECURITY.CERT.PWD";
	public static String FIRMA_SECURITY_FILE_NAME = "FIRMA.SECURITY.FILE.NAME";
	
	public static String FIRMA_VALIDAR_CERTIFICADO_ACTIVO = "FIRMA.VALIDAR.CERTIFICADO";
	public static String FIRMA_VALIDAR_FIRMA_ACTIVO = "FIRMA.VALIDAR.FIRMA";
	public static String FIRMA_SELLO_ACTIVO = "FIRMA.SELLO";
	
	public static String UPGRADE_TIMESTAMP = "TIMESTAMP";
	public static String UPGRADE_TIMESTAMP_PDF = "TIMESTAMP_PDF";
	
	public static String USERNAME_TOKEN_MODE = "UsernameToken";
	public static String BINARY_SECURITY_TOKEN_MODE = "BinarySecurityToken";
	public static String NONE_MODE = "None";
	
	public static String WSSECURITY_DIRECT_REFERENCE = "DirectReference";
	
	

}
