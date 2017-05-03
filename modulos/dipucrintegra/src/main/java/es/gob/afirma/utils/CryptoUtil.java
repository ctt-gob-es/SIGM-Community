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
 * <b>File:</b><p>es.gob.afirma.signature.CryptoUtil.java.</p>
 * <b>Description:</b><p> Utility class contains encryption and hash functions for digital signature..</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>29/06/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 29/06/2011.
 */
package es.gob.afirma.utils;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import javax.xml.crypto.dsig.DigestMethod;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.signature.SigningException;

/**
 * <p>Utility class contains encryption and hash functions for digital signature.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 29/06/2011.
 */
public final class CryptoUtil {

    /**
     * Attribute that represents the name of hash algorithm for SHA1.
     */
    public static final String HASH_ALGORITHM_SHA1 = "SHA";

    /**
     * Attribute that represents the name of hash algorithm for SHA256.
     */
    public static final String HASH_ALGORITHM_SHA256 = "SHA-256";

    /**
     * Attribute that represents the name of hash algorithm for SHA384.
     */
    public static final String HASH_ALGORITHM_SHA384 = "SHA-384";

    /**
     * Attribute that represents the name of hash algorithm for SHA512.
     */
    public static final String HASH_ALGORITHM_SHA512 = "SHA-512";

    /**
     * Attribute that represents the name of hash algorithm for RIPEMD-160.
     */
    public static final String HASH_ALGORITHM_RIPEMD160 = "RIPEMD-160";

    /**
     * Attribute that represents RSA encryption algorithm.
     */
    public static final String ENCRYPTION_ALGORITHM_RSA = "RSA";

    /**
     * Attribute that represents number 1024.
     */
    private static final int NUMBER_1024 = 1024;

    /**
     * Constructor method for the class CryptoUtil.java.
     */
    private CryptoUtil() {
    }

    /**
     * Obtains the hash computation from a array bytes.
     * @param algorithm algorithm used in the hash computation.
     * @param data array bytes from source data.
     * @return the array of bytes for the resulting hash value.
     * @throws SigningException if there isn't given algorithm.
     */
    public static byte[ ] digest(String algorithm, byte[ ] data) throws SigningException {
	if (GenericUtils.assertStringValue(algorithm) && GenericUtils.assertArrayValid(data)) {
	    Provider provider = new BouncyCastleProvider();
	    Security.addProvider(provider);
	    try {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm, provider);
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		byte[ ] tmp = new byte[NUMBER_1024];
		int length = 0;
		while ((length = bais.read(tmp, 0, tmp.length)) >= 0) {
		    messageDigest.update(tmp, 0, length);
		}
		return messageDigest.digest();
	    } catch (NoSuchAlgorithmException e) {
		throw new SigningException(Language.getFormatResIntegra(ILogConstantKeys.CU_LOG001, new Object[ ] { algorithm }), e);
	    }
	}
	return null;
    }

    /**
     * Method compares two digests for equality. Does a simple byte compare.
     * 
     * @param hash1  one of the digests to compare.
     * @param hash2 the other digest to compare.
     * @return true if the digests are equal, false otherwise.
     */
    public static synchronized boolean equalHashes(byte[ ] hash1, byte[ ] hash2) {
	return MessageDigest.isEqual(hash1, hash2);
    }

    /**
     * Translates a {@link AlgorithmIdentifier} object to a digest algorithm string.
     * @param algorithmIdentifier algorithm identifier with OID format.
     * @return a digest algorithm name
     */
    public static String translateAlgorithmIdentifier(AlgorithmIdentifier algorithmIdentifier) {
	if (algorithmIdentifier == null) {
	    return null;
	}
	ASN1ObjectIdentifier algId = algorithmIdentifier.getAlgorithm();
	if (OIWObjectIdentifiers.idSHA1.equals(algId)) {
	    return HASH_ALGORITHM_SHA1;
	} else if (NISTObjectIdentifiers.id_sha256.equals(algId)) {
	    return HASH_ALGORITHM_SHA256;
	} else if (NISTObjectIdentifiers.id_sha384.equals(algId)) {
	    return HASH_ALGORITHM_SHA384;
	} else if (NISTObjectIdentifiers.id_sha512.equals(algId)) {
	    return HASH_ALGORITHM_SHA512;
	} else if (X509ObjectIdentifiers.ripemd160.equals(algId)) {
	    return HASH_ALGORITHM_RIPEMD160;
	} else {
	    return null;
	}
    }

    /**
     * Translates a {@link AlgorithmIdentifier} object to a digest algorithm string.
     * @param digestAlg digest method algorithm URI.
     * @return a digest algorithm name
     */
    public static String translateXmlDigestAlgorithm(String digestAlg) {
	if (digestAlg == null) {
	    return null;
	}
	if (DigestMethod.SHA1.equals(digestAlg)) {
	    return HASH_ALGORITHM_SHA1;
	} else if (DigestMethod.SHA256.equals(digestAlg)) {
	    return HASH_ALGORITHM_SHA256;
	} else if (DigestMethod.SHA512.equals(digestAlg)) {
	    return HASH_ALGORITHM_SHA512;
	} else {
	    return null;
	}
    }

    /**
     * Gets the name of digest algorithm by name or alias.
     * @param pseudoName name o alias of digest algorithm.
     * @return name of digest algorithm
     */
    public static String getDigestAlgorithmName(final String pseudoName) {
	String upperPseudoName = pseudoName.toUpperCase();
	if (upperPseudoName.equals("SHA") || upperPseudoName.startsWith("SHA1") || upperPseudoName.startsWith("SHA-1")) {
	    return HASH_ALGORITHM_SHA1;
	} else if (upperPseudoName.startsWith("SHA256") || upperPseudoName.startsWith("SHA-256")) {
	    return HASH_ALGORITHM_SHA256;
	} else if (upperPseudoName.startsWith("SHA384") || upperPseudoName.startsWith("SHA-384")) {
	    return HASH_ALGORITHM_SHA384;
	} else if (upperPseudoName.startsWith("SHA512") || upperPseudoName.startsWith("SHA-512")) {
	    return HASH_ALGORITHM_SHA512;
	} else if (upperPseudoName.startsWith("RIPEMD160") || upperPseudoName.startsWith("RIPEMD-160")) {
	    return HASH_ALGORITHM_SHA512;
	} else {
	    throw new IllegalArgumentException(Language.getFormatResIntegra(ILogConstantKeys.CU_LOG002, new Object[ ] { pseudoName }));
	}

    }
}
