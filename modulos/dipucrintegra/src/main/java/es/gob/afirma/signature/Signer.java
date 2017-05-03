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
 * <b>File:</b><p>es.gob.afirma.signature.Signer.java.</p>
 * <b>Description:</b><p>Interface defines methods to implement for sign documents.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>28/06/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 28/06/2011.
 */
package es.gob.afirma.signature;

import java.security.KeyStore.PrivateKeyEntry;
import java.util.Properties;

/**
 * <p>Interface defines methods to implement for sign documents.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 28/06/2011.
 */
public interface Signer {

    /**
     * Performs the signing of a document.
     * @param data document to sign.
     * @param algorithm signature algorithm.
     * @param signatureFormat signature format.
     * @param privateKey private key of signer certificate.
     * @param extraParams opcional parameters.
     * @return document signed.
     * @throws SigningException It's thrown in error cases signing documents.
     */
    byte[ ] sign(byte[ ] data, String algorithm, String signatureFormat, PrivateKeyEntry privateKey, Properties extraParams) throws SigningException;

    /**
     * Builds a co-signature (signature with signers in parallel).
     * @param signature signature data.
     * @param document data used to sign in original signature.
     * @param algorithm signature algorithm.
     * @param privateKey entry with private key and certificates chain.
     * @param extraParams optional parameters.
     * @return  a byte array of co-signature.
     * @throws SigningException if any error ocurrs in the signing process.
     */
    byte[ ] coSign(byte[ ] signature, byte[ ] document, String algorithm, PrivateKeyEntry privateKey, Properties extraParams) throws SigningException;

    /**
     * Builds a counter-signature (signature with signers in serial).
     * @param signature signature.
     * @param algorithm signature algorithm.
     * @param privateKey entry with private key and certificates chain.
     * @param optionalParams optional parameters.
     * @return a byte array of XAdES countersignature.
     * @throws SigningException if any error ocurrs in the signing process.
     */
    byte[ ] counterSign(byte[ ] signature, String algorithm, PrivateKeyEntry privateKey, Properties optionalParams) throws SigningException;

}
