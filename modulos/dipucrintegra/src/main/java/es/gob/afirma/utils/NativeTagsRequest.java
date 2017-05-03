// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/*
 * Este fichero forma parte de la plataforma de @firma.
 * La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2009-,2011 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.utils.NativeTagsRequest.java.</p>
 * <b>Description:</b><p>Class represents constants that contains xpaths of tag of xml request for @Firma native services.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>25/03/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 25/03/2011.
 */
package es.gob.afirma.utils;

/**
 * <p>Class represents constants that contains xpaths of tag of xml request for @Firma native services.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 25/03/2011.
 */
public final class NativeTagsRequest {

	/**
	 * Constructor method for the class NativeTagsRequest.java.
	 */
	private NativeTagsRequest(){}

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters</code>.
	 */
	public static final String PARAMETERS = "parameters/";
	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/</code>.
	 */
	public static final String PARAMETROS = "parametros/";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/applicationId</code>.
	 */
	public static final String APPLICATION_ID = PARAMETERS + "applicationId";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/idAplicacion</code>.
	 */
	public static final String ID_APLICACION = PARAMETROS + "idAplicacion";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/document</code>.
	 */
	public static final String DOCUMENT = PARAMETERS + "document";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/documento</code>.
	 */
	public static final String DOCUMENTO = PARAMETROS + "documento";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/documentName</code>.
	 */
	public static final String DOCUMENT_NAME = PARAMETERS + "documentName";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/nombreDocumento</code>.
	 */
	public static final String NOMBRE_DOCUMENTO = PARAMETROS + "nombreDocumento";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/documentType</code>.
	 */
	public static final String DOCUMENT_TYPE = PARAMETERS + "documentType";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/tipoDocumento</code>.
	 */
	public static final String TIPO_DOCUMENTO = PARAMETROS + "tipoDocumento";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/idDocumento</code>.
	 */
	public static final String ID_DOCUMENTO = PARAMETROS + "idDocumento";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/documentId</code>.
	 */
	public static final String DOCUMENT_ID = PARAMETERS + "documentId";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/certificado</code>.
	 */
	public static final String CERTIFICADO = PARAMETROS + "certificado";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/certificate</code>.
	 */
	public static final String CERTIFICATE = PARAMETERS + "certificate";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/modoValidacion</code>.
	 */
	public static final String MODO_VALIDACION = PARAMETROS + "modoValidacion";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/validationMode</code>.
	 */
	public static final String VALIDATION_MODE = PARAMETERS + "validationMode";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/obtenerInfo</code>.
	 */
	public static final String OBTENER_INFO = PARAMETROS + "obtenerInfo";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/getInfo</code>.
	 */
	public static final String GET_INFO = PARAMETERS + "getInfo";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/firmante</code>.
	 */
	public static final String FIRMANTE = PARAMETROS + "firmante";
	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/signer</code>.
	 */
	public static final String SIGNER = PARAMETERS + "signer";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/algoritmoHash</code>.
	 */
	public static final String ALGORITMO_HASH = PARAMETROS + "algoritmoHash";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/hashAlgorithm</code>.
	 */
	public static final String HASH_ALGORITHM = PARAMETERS + "hashAlgorithm";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/formatoFirma</code>.
	 */
	public static final String FORMATO_FIRMA = PARAMETROS + "formatoFirma";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/eSignatureFormat</code>.
	 */
	public static final String ESIGNATURE_FORMAT = PARAMETERS + "eSignatureFormat";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/idTransaccion</code>.
	 */
	public static final String ID_TRANSACCION = PARAMETROS + "idTransaccion";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/transactionId</code>.
	 */
	public static final String TRANSACTION_ID = PARAMETERS + "transactionId";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/firmaElectronica</code>.
	 */
	public static final String FIRMA_ELECTRONICA = PARAMETROS + "firmaElectronica";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/certificadoFirmante</code>.
	 */
	public static final String CERTIFICADO_FIRMANTE = PARAMETROS + "certificadoFirmante";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/eSignature</code>.
	 */
	public static final String E_SIGNATURE = PARAMETERS + "eSignature";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/signerCertificate</code>.
	 */
	public static final String SIGNER_CERTIFICATE = PARAMETERS + "signerCertificate";
	/**
	 * Constant attribute that represents the xpath for the tag <code>parametros/custodiarDocumento</code>.
	 */
	public static final String CUSTODIAR_DOCUMENTO = PARAMETROS + "custodiarDocumento";

	/**
	 * Constant attribute that represents the xpath for the tag <code>parameters/storeDocument</code>.
	 */
	public static final String STORE_DOCUMENT = PARAMETERS + "storeDocument";



}
