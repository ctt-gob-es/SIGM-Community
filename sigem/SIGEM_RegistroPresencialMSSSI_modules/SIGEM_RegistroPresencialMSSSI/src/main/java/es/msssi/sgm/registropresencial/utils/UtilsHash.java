/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ieci.tecdoc.fwktd.sir.core.exception.SIRException;

/**
 * Implementación del manager de generación de códigos hash.
 * 
 * @author cmorenog
 * 
 */
public class UtilsHash {

    private static final Logger logger = LoggerFactory.getLogger(UtilsHash.class);

    private static final String DEFAULT_ALGORITMO = "SHA-1";

    /** 
     */
    public byte[] generarHash(byte[] contenido) {

	byte[] hash = null;

	logger.info("Generando código hash");

	if (contenido != null) {

	    String algoritmoHash = null;

	    try {

		logger.info("Algorimo para el hash: [{}]", algoritmoHash);

		MessageDigest md = MessageDigest.getInstance(DEFAULT_ALGORITMO);
		md.update(contenido);
		hash = md.digest();

		if (logger.isInfoEnabled()) {
		    logger.info("Código hash generado [{}]: {}", algoritmoHash,
			    Base64.encodeBase64String(hash));
		}
	    }
	    catch (NoSuchAlgorithmException e) {
		logger.error("Error al generar el código hash", e);
		throw new SIRException("error.sir.generarHash", new String[] { algoritmoHash },
			e.getMessage());
	    }
	}

	return hash;
    }

    public static String getBase64Sring(byte[] dato) {
	String result = null;
	if (dato != null){
	    result = Base64.encodeBase64String(dato);
	}
	return result;
    }
}
