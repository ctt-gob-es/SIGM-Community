/**
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
package es.accv.arangi.base.mityc;

import es.accv.arangi.base.algorithm.DigitalSignatureAlgorithm;
import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.exception.signature.AlgorithmNotSuitableException;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.firmaJava.ts.ConstantesTSA;

/**
 * Utilidades XAdES
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 *
 */
public class XAdESUtil {

    /*
     * Pasar del algoritmo de Arangí al algoritmo que necesita MITyC
     */
	public static String getXAdESDigitalSignatureAlgorithm(String digitalSignatureAlgorithm) throws AlgorithmNotSuitableException {
		if (digitalSignatureAlgorithm == null) {
			digitalSignatureAlgorithm = DigitalSignatureAlgorithm.getDefault();
		}
		if (digitalSignatureAlgorithm.equals(DigitalSignatureAlgorithm.SHA1_RSA)) {
			return FirmaXML.XADES_ALGO_ID_SIGNATURE_RSA_SHA1;
		} else if (digitalSignatureAlgorithm.equals(DigitalSignatureAlgorithm.SHA256_RSA)) {
			return FirmaXML.XADES_ALGO_ID_SIGNATURE_RSA_SHA256;
		} else if (digitalSignatureAlgorithm.equals(DigitalSignatureAlgorithm.SHA384_RSA)) {
			return FirmaXML.XADES_ALGO_ID_SIGNATURE_RSA_SHA384;
		} else if (digitalSignatureAlgorithm.equals(DigitalSignatureAlgorithm.SHA512_RSA)) {
			return FirmaXML.XADES_ALGO_ID_SIGNATURE_RSA_SHA512;
		} 
		
		//-- Algoritmo no definido para firma XAdES
		throw new AlgorithmNotSuitableException ("El algoritmo " + digitalSignatureAlgorithm + " no está soportado para firma XAdES en Arangí");
	}

    /*
     * Pasar del algoritmo de Arangí al algoritmo que necesita MITyC
     */
	public static String getXAdESHashingAlgorithm(String tsaHashingAlgorithm) throws AlgorithmNotSuitableException {
		if (tsaHashingAlgorithm == null) {
			tsaHashingAlgorithm = HashingAlgorithm.getDefault();
		}
		if (tsaHashingAlgorithm.equals(HashingAlgorithm.SHA1)) {
			return ConstantesTSA.SHA1;
		} else if (tsaHashingAlgorithm.equals(HashingAlgorithm.SHA256)) {
			return ConstantesTSA.SHA256;
		} else if (tsaHashingAlgorithm.equals(HashingAlgorithm.SHA384)) {
			return ConstantesTSA.SHA384;
		} else if (tsaHashingAlgorithm.equals(HashingAlgorithm.SHA512)) {
			return ConstantesTSA.SHA512;
		} 
		
		//-- Algoritmo no definido para firma XAdES
		throw new AlgorithmNotSuitableException ("El algoritmo " + tsaHashingAlgorithm + " no está soportado para hashing en Arangí");
	}


}
