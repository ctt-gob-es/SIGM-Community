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

package es.seap.minhap.portafirmas.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Properties;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.business.Base64;

@Component
public class UtilesQR {

	@Resource(name = "configCSVQRProperties")
	private Properties properties;

	private Logger log = Logger.getLogger(UtilesQR.class);

	public String generaQRCSV(String organismo, String codigoCSV) {
		// QR
		String separador = properties.getProperty(CSVQRConstantes.CSV_SEPARADOR);
		int rango = new Integer(properties.getProperty(CSVQRConstantes.CSV_RANGO));
		String csvQR = properties.getProperty(CSVQRConstantes.CSV_AMBITO) + separador
				+ formatCSV(codigoCSV, rango, separador);
		StringBuffer paramQr = new StringBuffer();
		StringBuffer urlQr = new StringBuffer();

		if (properties.getProperty(CSVQRConstantes.CSVQR_URLQR) != null
				&& !("").equals(properties.getProperty(CSVQRConstantes.CSVQR_URLQR))) {
			urlQr.append(properties.getProperty(CSVQRConstantes.CSVQR_URLQR));

			int digitNumber = Integer.parseInt(properties.getProperty(CSVQRConstantes.CSVQR_DIGITNUMBER));
			int multiplicador = obtenerMultiplicador(digitNumber);

			String random0 = String.valueOf(Math.random() * multiplicador).substring(0, digitNumber);
			String random1 = String.valueOf(Math.random() * multiplicador).substring(0, digitNumber);
			String random2 = String.valueOf(Math.random() * multiplicador).substring(0, digitNumber);

			paramQr.append(properties.getProperty(CSVQRConstantes.CSVQR_SEPARADOR));
			paramQr.append(random0);
			paramQr.append(properties.getProperty(CSVQRConstantes.CSVQR_SEPARADOR));
			paramQr.append(organismo);
			paramQr.append(properties.getProperty(CSVQRConstantes.CSVQR_SEPARADOR));
			paramQr.append(random1);
			paramQr.append(properties.getProperty(CSVQRConstantes.CSVQR_SEPARADOR));
			paramQr.append(csvQR);
			paramQr.append(properties.getProperty(CSVQRConstantes.CSVQR_SEPARADOR));
			paramQr.append(random2);
			paramQr.append(properties.getProperty(CSVQRConstantes.CSVQR_SEPARADOR));

			String password = properties.getProperty(CSVQRConstantes.CSVQR_PASSWORD);
			String cadenaCodificada = encodeAES(paramQr.toString(), password);

			try {
				urlQr.append(URLEncoder.encode(cadenaCodificada, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				if (log.isDebugEnabled()) {
					e.printStackTrace();
				}
			}
		}

		return urlQr.toString().replace("%", "---");
	}

	public static int obtenerMultiplicador(int digitNumber) {

		String multiplicadorAux = "1";

		for (int i = 0; i < digitNumber; i++) {
			multiplicadorAux = multiplicadorAux + "0";
		}

		return Integer.parseInt(multiplicadorAux);
	}

	/**
	 * Codificacion del valor de la cookie
	 * 
	 * @param param
	 * @return
	 */
	public String encodeAES(String param, String password) {

		String encoded = "";

		try {

			/**
			 * Paso 1. Generación de la clave AES usando KeyGenerator. Se unsará
			 * un keysize de 128 bits (16 bytes)
			 * 
			 */
			SecretKey secretKey = new SecretKeySpec(password.getBytes(), "AES");

			/**
			 * Paso 2. Inicialización del vector (IV) a. Usamos SecureRandom
			 * para generar bits aleatorios El tamaño del vector IV debe
			 * coincidir con los blouqes a cifrar (128 bits parfa AES) b.
			 * Construimos objeto IvParameterSpec para almacenar la pass a
			 * cifrar con el método init() de la clase Cipher
			 */

			final int AES_KEYLENGTH = 128; // cambiar según el nivel de
											// seguridad que se desee
			byte[] iv = new byte[AES_KEYLENGTH / 8]; // SE guarda el vertor IV
														// en formato bytes o
														// texto plano con la
														// información
														// encriptada
			SecureRandom prng = new SecureRandom();
			prng.nextBytes(iv);

			/**
			 * Paso 3. Crear una instancia de Cipher con los paraámetros
			 * siguientes; a. Nombre del algoritmo - en nuestro caso AES b. Modo
			 * - en nuestro caso CBC c. Padding - ejemplo. PKCS7 or PKCS5
			 */

			// Cipher aesCipherForEncryption =
			// Cipher.getInstance("AES/CBC/PKCS7PADDING");
			Cipher aesCipherForEncryption = Cipher.getInstance("AES");// Debe
																		// especificar
																		// el
																		// modo
																		// más
																		// explícitamente
																		// como
																		// proveedores
																		// JCE
																		// predeterminado
																		// al
																		// modo
																		// BCE

			/**
			 * Paso 4. Se inicializa la clase Cipher para realizar la
			 * encriptación
			 */

			// aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey, new
			// IvParameterSpec(iv));
			aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey);

			/**
			 * Paso 5. Encriptación de los datos a. Declarar / Inicializar los
			 * datos. Los datos debe se un String b. Convertir los datos
			 * (definidos en String) en bytes c. Encriptamos los bytes usanod el
			 * método dFinal
			 */
			byte[] byteDataToEncrypt = param.getBytes();
			byte[] byteCipherText = aesCipherForEncryption.doFinal(byteDataToEncrypt);

			encoded = new String(Base64.encodeBytes(byteCipherText));
			log.debug("Cipher Text generated using AES is " + encoded);

		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
		}
		return encoded;
	}

	private static String formatCSV(String csv, int rango, String separador) {
		StringBuilder retorno = new StringBuilder();
		for (int i = 0; i < csv.length(); i++) {
			if (i > 1 && i % rango == 0) {
				retorno.append(separador + csv.charAt(i));
			} else {
				retorno.append(csv.charAt(i));
			}
		}
		return retorno.toString();
	}

}
