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

package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.converter.dssprocessor.constantes;

public class DSSTiposFirmaConstantes {

	// ------------- AFIRMA DSS SIGNATURE TYPES ---------------------
	public static String DSS_SIGNATURE_TYPE_PKCS_7_1_5 = "urn:ietf:rfc:2315";
	
	public static String DSS_SIGNATURE_TYPE_PKCS_CMS = "urn:ietf:rfc:3369";
	
	public static String DSS_SIGNATURE_TYPE_PKCS_CMS_T = "urn:afirma:dss:1.0:profile:XSS:forms:CMSWithTST";
	
	public static String DSS_SIGNATURE_TYPE_XMLDSIG = "urn:ietf:rfc:3275";
	
	public static String DSS_SIGNATURE_TYPE_CADES = "http://uri.etsi.org/01733/v1.7.3#";
	
	public static String DSS_SIGNATURE_TYPE_XADES = "http://uri.etsi.org/01903/v1.3.2#";
	
	public static String DSS_SIGNATURE_TYPE_ODF = "urn:afirma:dss:1.0:profile:XSS:forms:ODF";
	
	public static String DSS_SIGNATURE_TYPE_ODF_T = "urn:afirma:dss:1.0:profile:XSS:forms:ODFWithTST";
	
	public static String DSS_SIGNATURE_TYPE_PDF = "urn:afirma:dss:1.0:profile:XSS:forms:PDF";
	
	public static String DSS_SIGNATURE_TYPE_PADES = "urn:afirma:dss:1.0:profile:XSS:forms:PAdES";
	// ------------- FIN AFIRMA DSS SIGNATURE TYPES
	
	
	// -------------- AFIRMA DSS SIGNATURE MODES ---------------------------
	
	public static String DSS_SIGNATURE_MODE_BES = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:BES";
	
	public static String DSS_SIGNATURE_MODE_EPES = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:EPES";
	
	public static String DSS_SIGNATURE_MODE_T = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-T";
	
	public static String DSS_SIGNATURE_MODE_C = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-C";
	
	public static String DSS_SIGNATURE_MODE_X = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X";
	
	public static String DSS_SIGNATURE_MODE_X_1 = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-1";
	
	public static String DSS_SIGNATURE_MODE_X_2 = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-2";
	
	public static String DSS_SIGNATURE_MODE_X_L = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-L";
	
	public static String DSS_SIGNATURE_MODE_X_L_1 = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-L-1";
	
	public static String DSS_SIGNATURE_MODE_X_L_2 = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-L-2";
	
	public static String SIGNATURE_MODE_A = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-A";
	
	public static String DSS_SIGNATURE_PADES_MODE_BES = "urn:afirma:dss:1.0:profile:XSS:PAdES:1.1.2:forms:BES";

	public static String DSS_SIGNATURE_PADES_MODE_EPES = "urn:afirma:dss:1.0:profile:XSS:PAdES:1.1.2:forms:EPES";
	
	public static String DSS_SIGNATURE_PADES_MODE_LTV = "urn:afirma:dss:1.0:profile:XSS:PAdES:1.1.2:forms:LTV";
	// -------------- FIN AFIRMA DSS SIGNATURE MODES --------------------------
	
}
