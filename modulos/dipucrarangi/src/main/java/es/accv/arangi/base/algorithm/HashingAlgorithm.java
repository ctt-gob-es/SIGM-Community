/**
 * LICENCIA LGPL:
 * 
 * Esta librería es Software Libre; Usted puede redistribuirla y/o modificarla
 * bajo los términos de la GNU Lesser General Public License (LGPL) tal y como 
 * ha sido publicada por la Free Software Foundation; o bien la versión 2.1 de 
 * la Licencia, o (a su elección) cualquier versión posterior.
 * 
 * Esta librería se distribuye con la esperanza de que sea útil, pero SIN 
 * NINGUNA GARANTÍA; tampoco las implícitas garantías de MERCANTILIDAD o 
 * ADECUACIÓN A UN PROPÓSITO PARTICULAR. Consulte la GNU Lesser General Public 
 * License (LGPL) para más detalles
 * 
 * Usted debe recibir una copia de la GNU Lesser General Public License (LGPL) 
 * junto con esta librería; si no es así, escriba a la Free Software Foundation 
 * Inc. 51 Franklin Street, 5º Piso, Boston, MA 02110-1301, USA o consulte
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.algorithm;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import es.mityc.firmaJava.libreria.utilidades.UtilidadFirmaElectronica;

/**
 * Clase con constantes que identifican a algoritmos de obtención del hash de un documento.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class HashingAlgorithm {

	/**
	 * Algoritmo de digest MD2 definido en la RFC 1319.
	 */
	public final static String MD2	= "MD2";
	
	/**
	 * Algoritmo de digest MD5 definido en la RFC 1321.
	 */
	public final static String MD5	= "MD5";
	
	/**
	 * Algoritmo seguro de hash definido en el estándar de hash seguro NIST FIPS 180-1.
	 */
	public final static String SHA1	= "SHA-1";
	
	/**
	 * Algoritmo de hash definido por <a href="http://csrc.nist.gov/encryption/shs/dfips-180-2.pdf">
	 * Federal Information Processing Standard 180-2, Secure Hash Standard (SHS) </a>. 
	 * SHA-256 es una función de hash de 256 bits desarrollada para proporcionar 128 bits 
	 * de seguridad contra ataques por colisión.
	 */
	public final static String SHA256	= "SHA-256";
	
	/**
	 * Algoritmo de hash definido por <a href="http://csrc.nist.gov/encryption/shs/dfips-180-2.pdf">
	 * Federal Information Processing Standard 180-2, Secure Hash Standard (SHS) </a>. 
	 * SHA-512 es una función de hash de 512 bits desarrollada para proporcionar 256 bits 
	 * de seguridad contra ataques por colisión.
	 */
	public final static String SHA512	= "SHA-512";
	
	/**
	 * Algoritmo de hash definido por <a href="http://csrc.nist.gov/encryption/shs/dfips-180-2.pdf">
	 * Federal Information Processing Standard 180-2, Secure Hash Standard (SHS) </a>. 
	 * Se obtiene un hash de 384 bits truncando la salida del algoritmo SHA-512. 
	 */
	public final static String SHA384	= "SHA-384";
	
	/**
	 * Devuelve el algoritmo por defecto para digest (más utilizado)
	 * 
	 * @return SHA1
	 */
	public static final String getDefault() {
		return SHA1;
	}
	
	/*
	 * Mapa de para pasar de texto a OID
	 */
	private static Map<String,String> mapOIDs = new HashMap<String,String> ();
	
	/*
	 * Mapa de para pasar de OID a text
	 */
	private static Map<String,String> mapReverseOIDs = new HashMap<String,String> ();
	
	/*
	 * Mapa de para pasar de texto a tamaño del hash en bytes
	 */
	private static Map<String,Integer> mapBytesLengths = new HashMap<String,Integer> ();
	
	/*
	 * Mapa para pasar del algoritmo a la URI empleada en firmas XML
	 */
	private static Map<String,String> mapURIXMLSignatures = new HashMap<String,String> ();
	
	/*
	 * Mapa para pasar de la URI empleada en firmas XML al algoritmo
	 */
	private static Map<String,String> mapReverseURIXMLSignatures = new HashMap<String,String> ();
	
	static {
		mapOIDs.put(MD2, "1.3.14.7.2.2.1");
		mapOIDs.put(MD5, "1.2.840.113549.2.5");
		mapOIDs.put(SHA1, "1.3.14.3.2.26");
		mapOIDs.put(SHA256, "2.16.840.1.101.3.4.2.1");
		mapOIDs.put(SHA384, "2.16.840.1.101.3.4.2.2");
		mapOIDs.put(SHA512, "2.16.840.1.101.3.4.2.3");
		
		mapReverseOIDs.put("1.3.14.7.2.2.1", MD2);
		mapReverseOIDs.put("1.2.840.113549.2.5", MD5);
		mapReverseOIDs.put("1.3.14.3.2.26", SHA1);
		mapReverseOIDs.put("2.16.840.1.101.3.4.2.1", SHA256);
		mapReverseOIDs.put("2.16.840.1.101.3.4.2.2", SHA384);
		mapReverseOIDs.put("2.16.840.1.101.3.4.2.3", SHA512);
		
		mapBytesLengths.put(MD2, 16);
		mapBytesLengths.put(MD5, 16);
		mapBytesLengths.put(SHA1, 20);
		mapBytesLengths.put(SHA256, 32);
		mapBytesLengths.put(SHA384, 48);
		mapBytesLengths.put(SHA512, 64);
		
		mapURIXMLSignatures.put(MD2, UtilidadFirmaElectronica.DIGEST_ALG_MD2);
		mapURIXMLSignatures.put(MD5, UtilidadFirmaElectronica.DIGEST_ALG_MD5);
		mapURIXMLSignatures.put(SHA1, UtilidadFirmaElectronica.DIGEST_ALG_SHA1);
		mapURIXMLSignatures.put(SHA256, UtilidadFirmaElectronica.DIGEST_ALG_SHA256);
		mapURIXMLSignatures.put(SHA384, UtilidadFirmaElectronica.DIGEST_ALG_SHA384);
		mapURIXMLSignatures.put(SHA512, UtilidadFirmaElectronica.DIGEST_ALG_SHA512);

		mapReverseURIXMLSignatures.put(UtilidadFirmaElectronica.DIGEST_ALG_MD2, MD2);
		mapReverseURIXMLSignatures.put(UtilidadFirmaElectronica.DIGEST_ALG_MD5, MD5);
		mapReverseURIXMLSignatures.put(UtilidadFirmaElectronica.DIGEST_ALG_SHA1, SHA1);
		mapReverseURIXMLSignatures.put(UtilidadFirmaElectronica.DIGEST_ALG_SHA256, SHA256);
		mapReverseURIXMLSignatures.put(UtilidadFirmaElectronica.DIGEST_ALG_SHA384, SHA384);
		mapReverseURIXMLSignatures.put(UtilidadFirmaElectronica.DIGEST_ALG_SHA512, SHA512);

	}


	/**
	 * Devuelve el OID del algoritmo pasado como parámetro. Este algoritmo debe ser uno de
	 * los definidos como constantes en esta clase.
	 * 
	 * @param hashingAlgorithm Nombre del algoritmo
	 * @return OID del algoritmo o null si el algoritmo no existe
	 * @throws NoSuchAlgorithmException El algoritmo no existe
	 */
	public static String getOID(String hashingAlgorithm) throws NoSuchAlgorithmException {
		String oid = (String) mapOIDs.get(hashingAlgorithm);
		if (oid == null) {
			throw new NoSuchAlgorithmException ("El algoritmo de hashing '" + hashingAlgorithm + "' no existe dentro de Arangi");
		}
		
		return oid;
	}
	
	/**
	 * Devuelve el OID del algoritmo por defecto para digest (más utilizado)
	 * 
	 * @return SHA1 OID
	 */
	public static final String getDefaultOID() {
		try {
			return getOID(getDefault());
		} catch (NoSuchAlgorithmException e) {
			// No se va a dar
			return null;
		}
	}
	
	/**
	 * Devuelve el nombre del algoritmo en base a su OID. Este algoritmo debe ser uno de
	 * los definidos como constantes en esta clase.
	 * 
	 * @param oid OID del algoritmo
	 * @return Nombre del algoritmo
	 * @throws NoSuchAlgorithmException El algoritmo no existe
	 */
	public static String getAlgorithmName(String oid) throws NoSuchAlgorithmException {
		String nombre = (String) mapReverseOIDs.get(oid);
		if (nombre == null) {
			throw new NoSuchAlgorithmException ("El oid '" + oid + "' no existe dentro de Arangi");
		}
		
		return nombre;
	}

	/**
	 * Devuelve el nombre del algoritmo en base a su nombre en algún sistema externo
	 * 
	 * @param oid OID del algoritmo
	 * @return Nombre del algoritmo
	 * @throws NoSuchAlgorithmException El algoritmo no existe
	 */
	public static String getAlgorithmFromExternalName(String externalName) throws NoSuchAlgorithmException {
		
		//-- Ver si existe sin mas o haciendo un upper case
		if (mapOIDs.containsKey(externalName)) {
			return externalName;
		}
		if (mapOIDs.containsKey(externalName.toUpperCase())) {
			return externalName.toUpperCase();
		}
		
		//-- Lo más habitual es que no tengan guión siendo sha
		if (externalName.toLowerCase().startsWith("sha") && externalName.indexOf("-") == -1) {
			String alg = "SHA-" + externalName.substring(3);
			if (mapOIDs.containsKey(alg)) {
				return alg;
			}
		}
		
		throw new NoSuchAlgorithmException ("No se reconoce el algoritmo de hash: " + externalName);
	}

	/**
	 * Devuelve el tamaño del hash generado por el algoritmo
	 * 
	 * @param hashingAlgorithm Nombre del algoritmo
	 * @return Tamaño de los hash generados por el algoritmo o -1 si el
	 * 	algoritmo no existe en Arangí
	 */
	public static int getHashBytesLength(String hashingAlgorithm) {
		Integer length = (Integer) mapBytesLengths.get(hashingAlgorithm);
		if (length == null) {
			return -1;
		}
		
		return length;
	}
	
	/**
	 * Obtiene el nombre del algoritmo dependiendo del tamaño del hash
	 * 
	 * @param hash Hash
	 * @return Nombre del algoritmo con el que fue generado el hash
	 */
	public static String getAlgorithmNameFromHash (byte[] hash) {
		for(String key : mapBytesLengths.keySet()) {
			if (mapBytesLengths.get(key) == hash.length) {
				return key;
			}
		}
		//-- No se ha encontrado el algoritmo
		return getDefault();
	}
	
	/**
	 * Devuelve la URI a utilizar dentro de las firmas XML
	 * 
	 * @param hashingAlgorithm Nombre del algoritmo
	 * @return URI o null si el algoritmo no existe
	 * @throws NoSuchAlgorithmException El algoritmo no existe
	 */
	public static String getURIXMLSignatures(String hashingAlgorithm) throws NoSuchAlgorithmException {
		String uri = (String) mapURIXMLSignatures.get(hashingAlgorithm);
		if (uri == null) {
			throw new NoSuchAlgorithmException ("El algoritmo de hashing '" + hashingAlgorithm + "' no existe dentro de Arangi");
		}
		
		return uri;
	}
	
	/**
	 * Devuelve el nombre del algoritmo a partir de la URI empleada en firmas XML
	 * 
	 * @param uriURI empleada en firmas XML
	 * @return Nombre del algoritmo
	 * @throws NoSuchAlgorithmException El algoritmo no existe
	 */
	public static String getAlgorithmNameFromURIXMLSignatures(String uri) throws NoSuchAlgorithmException {
		String nombre = (String) mapReverseURIXMLSignatures.get(uri);
		if (nombre == null) {
			throw new NoSuchAlgorithmException ("La URI '" + uri + "' no existe dentro de Arangi");
		}
		
		return nombre;
	}

	/**
	 * Compara dos algoritmos para ver cuál tiene como resultado un hash mayor
	 * 
	 * @param algorithm1 Algoritmo 1
	 * @param algorithm2 Algoritmo 2
	 * @return Cierto si el algoritmo 1 es mayor que el algoritmo 2
	 */
	public static boolean isGreater (String algorithm1, String algorithm2) {
		// algoritmos de tipos diferentes
		if (algorithm1.toLowerCase().startsWith("sha") && !algorithm1.toLowerCase().startsWith("sha")) {
			return true;
		} else if(!algorithm1.toLowerCase().startsWith("sha") && algorithm1.toLowerCase().startsWith("sha")) {
			return false;
		}

		// algoritmos del mismo tipo
		try {
			int nivelAlgoritmo1 = getNivelAlgoritmo(algorithm1);
			int nivelAlgoritmo2 = getNivelAlgoritmo(algorithm2);
			if (nivelAlgoritmo1 > nivelAlgoritmo2) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// error de parseo
			return false;
		}
	}
	
	//-- Métodos privados

	private static int getNivelAlgoritmo (String algoritmo) {
		if (algoritmo.toLowerCase().startsWith("sha")) {
			// sha
			if (algoritmo.indexOf("-") > -1) {
				return Integer.parseInt((algoritmo.substring(4)));
			} else {
				return Integer.parseInt((algoritmo.substring(3)));
			}
		} else {
			// md
			if (algoritmo.indexOf("-") > -1) {
				return Integer.parseInt((algoritmo.substring(3)));
			} else {
				return Integer.parseInt((algoritmo.substring(2)));
			}
		}
	}
}
