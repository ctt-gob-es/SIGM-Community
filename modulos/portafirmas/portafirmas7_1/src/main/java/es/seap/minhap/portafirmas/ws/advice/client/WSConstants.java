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

package es.seap.minhap.portafirmas.ws.advice.client;

public class WSConstants {

	public static final String ADVICE_SERVICE = "ws.advice.address";

	public static final String USER_ID_PROP = "redaction.user.id";
	public static final String JOB_ID_PROP = "redaction.job.id";
	public static final String DOC_TYPE_PROP = "redaction.docType";
	public static final String APP_ID_PROP = "redaction.application";
	public static final String SUBJECT_PROP = "redaction.subject";
	public static final String REFERENCE_PROP = "redaction.reference";
	public static final String TEXT_PROP = "redaction.text";
	public static final String DOCUMENT_FILE_PATH_PROP = "redaction.document.path";
	public static final String DOCUMENT_FILE_NAME_PROP = "redaction.document.name";
	public static final String DOCUMENT_FILE_MIME_PROP = "redaction.document.mime";

	//TIPO DE FIRMA
	public static final String SIGN_TYPE_FIRSTSIGNER = "PRIMER FIRMANTE";
	public static final String SIGN_TYPE_CASCADE = "CASCADA";
	
	//FIRMA-VISTO BUENO
	public static final String PASS_TYPE = "VISTOBUENO";
	public static final String SIGN_TYPE = "FIRMA";
	
	// REQUEST STATE
	public static final String REQUEST_STATE_ACCEPTED = "ACEPTADO";
	public static final String REQUEST_STATE_REJECTED = "RECHAZADO";
	public static final String REQUEST_STATE_EXPIRED = "CADUCADO";
	public static final String REQUEST_STATE_AWAITING = "EN PROCESO";
	
	public static final String REQUEST_STATE_REMOVED = "RETIRADO";
	public static final String REQUEST_NEW_SIGN = "NUEVO.FIRMA";
	public static final String REQUEST_NEW_PASS = "NUEVO.VISTOBUENO";
	
}
