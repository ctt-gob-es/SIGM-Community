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
 * Este fichero forma parte del Cliente @firma.
 * El Cliente @firma es un applet de libre distribuci�n cuyo c�digo fuente puede ser consultado
 * y descargado desde www.ctt.map.es.
 * Copyright 2009,2010 Gobierno de Espa�a
 * Este fichero se distribuye bajo las licencias EUPL versi�n 1.1  y GPL versi�n 3, o superiores, seg�n las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompa�a.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aqu� las condiciones expresadas all�.
 */

/**
 * <b>File:</b><p>es.gob.afirma.signature.cades.P7ContentSignerParameters.java.</p>
 * <b>Description:</b><p>Class contains the necesary parameters used in CAdES signatures.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>28/06/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 28/06/2011.
 */
package es.gob.afirma.signature.cades;

import java.security.KeyStore.PrivateKeyEntry;
import java.util.Properties;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;

/**
 * <p>Class contains the necesary parameters used in CAdES signatures.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 28/06/2011.
 */
public final class P7ContentSignerParameters {

    /**
     * Attribute that represents document to sign.
     */
    private byte[ ] data;
    /**
     * Attribute that represents signer algorithm.
     */
    private String signAlgorithm;
    /**
     * Attribute that represents private key used in the the signing process.
     */
    private PrivateKeyEntry privateKey;
    /**
     * Attribute that represents signature value.
     */
    private byte[ ] signatureValue;

    /**
     * Attribute that represents optional parameters used in the signing process.
     */
    private Properties optionalParams;

    /**
     * Attribute that represents the digest value of document to sign.
     */
    private byte[ ] digestValue;

    /**
     * M&eacute;todo que asigna los datos pasados por par&aacute;metro a las propiedades de la clase.
     *
     * @param data2 Archivo de firmas.
     * @param signAlg Tipo de datos.
     * @param keyEntry Certificados del firmante
     * @throws IllegalArgumentException
     */

    /**
     * Constructor method for the class P7ContentSignerParameters.java.
     * @param content document to sign.
     * @param signatureAlgorithm sinature algorithm.
     * @param keyEntry key entry.
     */
    public P7ContentSignerParameters(final byte[ ] content, String signatureAlgorithm, PrivateKeyEntry keyEntry) {
	if (content != null) {
	    data = content.clone();
	}

	if (signatureAlgorithm == null || signatureAlgorithm.length() < 1) {
	    throw new IllegalArgumentException(Language.getResIntegra(ILogConstantKeys.PCSP_LOG001));
	}
	if (keyEntry == null) {
	    throw new IllegalArgumentException(Language.getResIntegra(ILogConstantKeys.PCSP_LOG002));
	}
	signAlgorithm = signatureAlgorithm;
	privateKey = keyEntry;
	signatureValue = new byte[0];

    }

    /**
     * Constructor method for the class P7ContentSignerParameters.java.
     * @param content data content
     * @param signatureAlgorithm signature algorithm
     * @param keyEntry private key entry.
     * @param optionalParameters optional parameters
     */
    public P7ContentSignerParameters(byte[ ] content, String signatureAlgorithm, PrivateKeyEntry keyEntry, Properties optionalParameters) {
	super();
	this.data = content == null ? content : content.clone();
	this.signAlgorithm = signatureAlgorithm;
	this.privateKey = keyEntry;
	this.optionalParams = optionalParameters;
    }

    /**
     * Constructor method for the class P7ContentSignerParameters.java.
     * @param signatureAlgorithm signAlgorithm
     * @param keyEntry private key
     * @param optionalParameters optionalParams
     */
    public P7ContentSignerParameters(String signatureAlgorithm, PrivateKeyEntry keyEntry, Properties optionalParameters) {
	super();
	this.signAlgorithm = signatureAlgorithm;
	this.privateKey = keyEntry;
	this.optionalParams = optionalParameters;
    }

    /**
     * 
     * @return content
     */
    public byte[ ] getContent() {
	return data;
    }

    /**
     * M&eacute;todo que devuelve el tipo.
     * @return  el tipo
     */
    public String getSignatureAlgorithm() {
	return signAlgorithm;
    }

    /**
     * Gets the value of the attribute {@link #privateKey}.
     * @return the value of the attribute {@link #privateKey}.
     */
    public PrivateKeyEntry getPrivateKey() {
	return privateKey;
    }

    /**
     * Gets the value of the attribute {@link #digestValue}.
     * @return the value of the attribute {@link #digestValue}.
     */
    public byte[ ] getDigestValue() {
	return digestValue;
    }

    /**
     * Sets the value of the attribute {@link #digestValue}.
     * @param digestValueParam The value for the attribute {@link #digestValue}.
     */
    public void setDigestValue(byte[ ] digestValueParam) {
	if (digestValueParam != null) {
	    this.digestValue = digestValueParam.clone();
	}
    }

    /**
     * Sets the value of the attribute {@link #optionalParams}.
     * @param optionalParameters The value for the attribute {@link #optionalParams}.
     */
    public void setOptionalParams(Properties optionalParameters) {
	this.optionalParams = optionalParameters;
    }

    /**
     * Gets the value of the attribute {@link #optionalParams}.
     * @return the value of the attribute {@link #optionalParams}.
     */
    public Properties getOptionalParams() {
	return optionalParams;
    }

    /**
     * Gets the value of the attribute {@link #signatureValue}.
     * @return the value of the attribute {@link #signatureValue}.
     */
    public byte[ ] getSignatureValue() {
	return signatureValue;
    }

    /**
     * Sets the value of the attribute {@link #signatureValue}.
     * @param signValue The value for the attribute {@link #signatureValue}.
     */
    public void setSignatureValue(byte[ ] signValue) {
	if (signValue != null) {
	    this.signatureValue = signValue.clone();
	}
    }

    /**
     * Sets the value of the attribute {@link #data}.
     * @param content The value for the attribute {@link #data}.
     */
    public void setContent(byte[ ] content) {
	if (content != null) {
	    this.data = content.clone();
	}
    }

}