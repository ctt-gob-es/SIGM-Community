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

public class DSSResultConstantes {

	/* -------------------- AFIRMA DSS RESULTMAJOR --------------------*/
	public static String DSS_MAJOR_SUCCESS = "urn:oasis:names:tc:dss:1.0:resultmajor:Success";
	
	public static String DSS_MAJOR_REQUESTERERROR = "urn:oasis:names:tc:dss:1.0:resultmajor:RequesterError";
	
	public static String DSS_MAJOR_RESPONDERERROR = "urn:oasis:names:tc:dss:1.0:resultmajor:ResponderError";
	
	public static String DSS_MAJOR_INSUFFICIENTINFORMATION = "urn:oasis:names:tc:dss:1.0:resultmajor:InsufficientInformation";
	
	public static String DSS_MAJOR_VALIDSIGNATURE = "urn:afirma:dss:1.0:profile:XSS:resultmajor:ValidSignature";
	
	public static String DSS_MAJOR_INVALIDSIGNATURE = "urn:afirma:dss:1.0:profile:XSS:resultmajor:InvalidSignature";
	
	public static String DSS_MAJOR_WARNING = "urn:oasis:names:tc:dss:1.0:resultmajor:Warning";
	
	public static String DSS_MAJOR_PENDING = "urn:oasis:names:tc:dss:1.0:profiles:asynchronousprocessing:resultmajor:Pending";
	
	/* -------------------- END AFIRMA DSS RESULTMAJOR --------------------*/
	
	
	/* -------------------- AFIRMA DSS RESULTMINOR --------------------*/
	public static String DSS_MINOR_INCORRECTFORMAT = "urn:afirma:dss:1.0:profile:XSS:resultminor:SignatureFormat:IncorrectFormat";
	
	public static String DSS_MINOR_INCOMPLETEUPGRADEOP = "urn:afirma:dss:1.0:profile:XSS:resultminor:IncompleteUpgradeOperation";

	public static String DSS_MINOR_INCORRECTRETURNUPDATEDSIGNTYPE = "urn:afirma:dss:1.0:profile:XSS:resultminor:IncorrectUpdateSignatureType";

	public static String DSS_MINOR_UPDATEDSIGNTYPENOTPROVIDED = "urn:afirma:dss:1.0:profile:XSS:resultminor:UpdateSignatureTypeNotProvided";
	

	/* -------------------- END AFIRMA DSS RESULTMINOR --------------------*/
	
}
