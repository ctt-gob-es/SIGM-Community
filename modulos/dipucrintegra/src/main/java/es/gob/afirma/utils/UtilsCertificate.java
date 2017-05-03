/*
 * Este fichero forma parte de la plataforma TS@.
 * La plataforma TS@ es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2013-,2014 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.utils.UtilsCertificate.java.</p>
 * <b>Description:</b><p>Class that provides methods for managing certificates and private keys.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>13/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 13/01/2014.
 */
package es.gob.afirma.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>Class that provides methods for managing certificates and private keys.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 13/01/2014.
 */
public final class UtilsCertificate {

    /**
     * Constant that represents a comma separator.
     */
    public static final String COMMA_SEPARATOR = ",";

    /**
     * Constant that represents a equals character.
     */
    private static final String EQUALS_CHAR = "=";

    /**
     * Constructor method for the class UtilsCertificate.java.
     */
    private UtilsCertificate() {
    }

    /**
     * Method that canonicalizes a X.500 Principal of a certificate.
     * @param x500PrincipalName Parameter that represents the value of the X.500 Principal of the certificate to canonicalize.
     * @return the canonicalized X.500 Principal.
     */
    public static String canonicalizeX500Principal(String x500PrincipalName) {
	if (x500PrincipalName.indexOf(EQUALS_CHAR) != -1) {
	    String[ ] campos = x500PrincipalName.split(COMMA_SEPARATOR);
	    Set<String> ordenados = new TreeSet<String>();
	    StringBuffer sb = new StringBuffer();

	    String[ ] pair;
	    int i = 0;
	    while (i < campos.length) {
		/*Puede darse el caso de que haya campos que incluyan comas, ejemplo:
		 *[OU=Class 3 Public Primary Certification Authority, O=VeriSign\\,  Inc., C=US]
		 */
		boolean sum = false;
		// Lo primero es ver si estamos en el campo final y si el
		// siguiente campo no posee el símbolo igual, lo
		// concatenamos al actual
		if (i < campos.length - 1 && !campos[i + 1].contains(EQUALS_CHAR)) {
		    campos[i] += COMMA_SEPARATOR + campos[i + 1];
		    sum = true;
		}
		sb = new StringBuffer();
		pair = campos[i].trim().split(EQUALS_CHAR);
		sb.append(pair[0].toLowerCase());
		sb.append(EQUALS_CHAR);
		sb.append(pair[1]);
		ordenados.add(sb.toString());
		i++;
		if (sum) {
		    i++;
		}
	    }

	    Iterator<String> it = ordenados.iterator();
	    sb = new StringBuffer();
	    while (it.hasNext()) {
		sb.append(it.next());
		sb.append(COMMA_SEPARATOR);
	    }
	    return sb.substring(0, sb.length() - 1);
	} else {
	    // No es un identificador de certificado, no se canonicaliza.
	    return x500PrincipalName;
	}
    }

    /**
     * Method that obtains a certificate from the bytes array.
     * @param certificateBytes Parameter that represents the certificate.
     * @return an object that represents the certificate.
     * @throws CertificateException If there is a parsing error.
     */
    public static X509Certificate generateCertificate(byte[ ] certificateBytes) throws CertificateException {
	InputStream is = null;
	try {
	    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
	    is = new ByteArrayInputStream(certificateBytes);
	    return (X509Certificate) certFactory.generateCertificate(is);
	} finally {
	    UtilsResources.safeCloseInputStream(is);
	}
    }
}
