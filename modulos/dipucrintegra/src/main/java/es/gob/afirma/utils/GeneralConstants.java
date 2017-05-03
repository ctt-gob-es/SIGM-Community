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
 * <b>File:</b><p>es.gob.afirma.utils.GeneralConstants.java</p>
 * <b>Description:</b><p>Class represents constants used in the call services.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>${date}.</p>
 * @author Gobierno de España.
 * @version 1.0, ${date}.
 */
package es.gob.afirma.utils;

/**
 * <p>Class represents constants used in the call services.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 17/03/2011.
 */
public final class GeneralConstants {

    /**
     * Constructor method for the class GeneralConstants.java.
     */
    private GeneralConstants() {
    }

    /**
     * Attribute that represents 'almacenar documento' service name.
     */
    public static final String ALMACENAR_DOCUMENTO_REQUEST = "AlmacenarDocumento";

    /**
     * Attribute that represents 'store Document' service name.
     */
    public static final String STORE_DOCUMENT_REQUEST = "StoreDocument";

    /**
     * Attribute that represents 'ValidarCertificado' service name.
     */
    public static final String VALIDACION_CERTIFICADO_REQUEST = "ValidarCertificado";

    /**
     * Attribute that represents 'validation certificate' service name.
     */
    public static final String CERTIFICATE_VALIDATION_REQUEST = "ValidateCertificate";

    /**
     * Attribute that represents 'ObtenerInfoCertificado' service name.
     */
    public static final String OBTENER_INFO_CERTIFICADO = "ObtenerInfoCertificado";

    /**
     * Attribute that represents the 'GetInfoCertificate' service name.
     */
    public static final String GET_INFO_CERTIFICATE = "GetInfoCertificate";

    /**
     * Attribute that represents the 'ValidarFirma' service name.
     */
    public static final String VALIDAR_FIRMA_REQUEST = "ValidarFirma";

    /**
     * Attribute that represents the 'SignatureValidation' service name.
     */
    public static final String SIGNATURE_VALIDATION_REQUEST = "SignatureValidation";

    /**
     * Attribute that represents the 'ServerSignature' service name.
     */
    public static final String SERVER_SIGNATURE_REQUEST = "ServerSignature";

    /**
     * Attribute that represents the 'FirmaServidor' service name .
     */
    public static final String FIRMA_SERVIDOR_REQUEST = "FirmaServidor";

    /**
     * Attribute that represents the 'FirmaServidorCoSign' service name.
     */
    public static final String FIRMA_SERVIDOR_COSIGN_REQUEST = "FirmaServidorCoSign";

    /**
     * Attribute that represents the 'ServerSignatureCoSign' service name.
     */
    public static final String SERVER_SIGNATURE_COSIGN = "ServerSignatureCoSign";

    /**
     * Constants that represents the FirmaServidorCounterSign request name.
     */
    public static final String FIRMA_SERVIDOR_COUNTERSIGN = "FirmaServidorCounterSign";

    /**
     * Constants that represents the ServerSignatureCounterSign request name.
     */
    public static final String SERVER_SIGNATURE_COUNTER_SIGN = "ServerSignatureCounterSign";

    /**
     * Constants that represents the dss signature request name.
     */
    public static final String DSS_AFIRMA_SIGN_REQUEST = "DSSAfirmaSign";

    /**
     * Constants that represents the name of the request for DSS services of TS@.
     */
    public static final String DSS_TSA_REQUEST = "DSSTSA";

    /**
     * Constants that represents method name for dss dss signature service.
     */
    public static final String DSS_AFIRMA_SIGN_METHOD = "sign";

    /**
     * Constants that represents service name for  three phase user signature F1.
     */
    public static final String THREE_PHASE_USER_SIGN_F1 = "ThreePhaseUserSignatureF1";

    /**
     * Constants that represents service name for  'FirmaUsuario3FasesF1'.
     */
    public static final String FIRMA_USUARIO_3FASES_F1 = "FirmaUsuario3FasesF1";

    /**
     * Constants that represents service name for  three phase user signature CoSign F1.
     */
    public static final String THREE_PHASE_USER_SIGN_COSIGN_F1 = "ThreePhaseUserSignatureF1CoSign";

    /**
     * Constants that represents service name for  'FirmaUsuario3FasesF1CoSign'.
     */
    public static final String FIRMA_USUARIO_3FASES_F1_COSIGN = "FirmaUsuario3FasesF1CoSign";

    /**
     * Constants that represents service name for  three phase user signature counterSign F1.
     */
    public static final String THREE_PHASE_USER_SIGN_COUNTERSIGN_F1 = "ThreePhaseUserSignatureF1CounterSign";

    /**
     * Constants that represents service name for 'FirmaUsuario3FasesF1CounterSign'.
     */
    public static final String FIRMA_USUARIO_3FASES_F1_COUNTER_SIGN = "FirmaUsuario3FasesF1CounterSign";

    /**
     * Constants that represents service name for  three phase user signature F3.
     */
    public static final String THREE_PHASE_USER_SIGN_F3 = "ThreePhaseUserSignatureF3";

    /**
     * Constants that represents service name for  FirmaUsuario3FasesF3.
     */
    public static final String FIRMA_USUARIO_3FASES_F3 = "FirmaUsuario3FasesF3";

    /**
     * Constants that represents service name for  three phase user signature F2.
     */
    public static final String TWO_PHASE_USER_SIGN_F2 = "TwoPhaseUserSignatureF2";

    /**
     * Constants that represents service name for  FirmaUsuario2FasesF2.
     */
    public static final String FIRMA_USUARIO2_FASES2 = "FirmaUsuario2FasesF2";

    /**
     * Constants that represents the service name for dss afirma verify.
     */
    public static final String DSS_AFIRMA_VERIFY_REQUEST = "DSSAfirmaVerify";

    /**
     * Constants that represents the service name for DSS AfirmaVerify Certificate.
     */
    public static final String DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST = "DSSAfirmaVerifyCertificate";

    /**
     * Constants that represents method name for dss afirma verify service.
     */
    public static final String DSS_AFIRMA_VERIFY_METHOD = "verify";

    /**
     * Constants that represents the service name for DSS batch verify certificate.
     */
    public static final String DSS_BATCH_VERIFY_CERTIFICATE_REQUEST = "DSSBatchVerifyCertificate";

    /**
     * Constants that represents the service name for DSS batch verify signature.
     */
    public static final String DSS_BATCH_VERIFY_SIGNATURE_REQUESTS = "DSSBatchVerifySignature";

    /**
     * Constants that represents method name for dss verify certificates.
     */
    public static final String DSS_AFIRMA_VERIFY_CERTIFICATES_METHOD = "verifyCertificates";

    /**
     * Constants that represents method name for dss verify signatures.
     */
    public static final String DSS_AFIRMA_VERIFY_SIGNATURES_METHOD = "verifySignatures";

    /**
     * Constants that represents the service name for DSSAsyncRequestStatus.
     */
    public static final String DSS_ASYNC_REQUEST_STATUS = "DSSAsyncRequestStatus";

    /**
     * Constants that represents method name for DSSAsyncRequestStatus.
     */
    public static final String DSS_ASYNC_REQUEST_STATUS_METHOD = "getProcessResponse";

    /**
     * Constant attribute that represents the name of the the service for generating time-stamps from TS@. 
     */
    public static final String TSA_TIMESTAMP_SERVICE = "CreateTimeStampWS";

    /**
     * Constant attribute that represents the name of the the service for validating time-stamps from TS@. 
     */
    public static final String TSA_TIMESTAMP_VALIDATION_SERVICE = "VerifyTimeStampWS";

    /**
     * Constant attribute that represents the name of the the service for renewing time-stamps from TS@. 
     */
    public static final String TSA_RETIMESTAMP_SERVICE = "RenewTimeStampWS";

    // ALGORITHM TYPES

    /**Constant attribute that represents the algorithm MD2.
     */
    public static final String MD2 = "MD2";
    /**Constant attribute that represents the algorithm MD5.
     */
    public static final String MD5 = "MD5";
    /**
     * Constant attribute that represents the algorithm SHA1.
     */
    public static final String SHA1 = "SHA1";
    /**
     * Constant attribute that represents the algorithm SHA256.
     */
    public static final String SHA256 = "SHA256";
    /**
     * Constant attribute that represents the algorithm SHA384.
     */
    public static final String SHA384 = "SHA384";
    /**
     * Constant attribute that represents the algorithm SHA512.
     */
    public static final String SHA512 = "SHA512";
    //

    // SIGNATURE FORMATS

    /**
     * Constant attribute that represents the signature format CMS.
     */
    public static final String CMS = "CMS";

    /**
     * Constant attribute that represents the signature format PKCS7.
     */
    public static final String PKCS7 = "PKCS7";

    /**
     * Constant attribute that represents the signature format XMLDSIG.
     */
    public static final String XMLDSIG = "XMLDSIG";

    /**
     * Constant attribute that represents the signature format XADES.
     */
    public static final String XADES = "XADES";

    /**
     * Constant attribute that represents the signature format XADES-BES.
     */
    public static final String XADES_BES = "XADES-BES";

    /**
     * Constant attribute that represents the signature format XADES-T.
     */
    public static final String XADES_T = "XADES-T";

    /**
     * Constant attribute that represents the signature format PDF.
     */
    public static final String PDF = "PDF";

    /**
     * Constant attribute that represents the signature format ODF.
     */
    public static final String ODF = "ODF";

}
