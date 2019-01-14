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
package es.accv.arangi.base.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.device.KeyStoreManager;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.certificate.validation.ServiceException;
import es.accv.arangi.base.exception.certificate.validation.ServiceNotFoundException;

/**
 * Diversas utilidades necesarias en los proyectos que integran Arangi
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class Util {

	/**
	 * Logger de la clase
	 */
	private static Logger logger = Logger.getLogger(Util.class);
	
	/**
	 * Parseador generico para fechas en formato español: dd/MM/yyyy HH:mm
	 */
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy HH:mm");

	/**
	 * Parseador generico para fechas en formato español con segundos: dd/MM/yyyy HH:mm
	 */
	public static final SimpleDateFormat dateFormatAccurate = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");

	/**
	 * Tamaño por defecto de las claves generadas por Arangi
	 */
	public static final int DEFAULT_KEY_LENGTH = 1024;

	/*
	 * Fichero PKCS12 de prueba 
	 */
	private static final String PRUEBA_PKCS12_FILE = "es/accv/arangi/base/resource/prueba.p12";

	/*
	 * PIN del fichero PKCS12 de prueba 
	 */
	private static final String PRUEBA_PKCS12_PIN = "Mu4lWyke";

  	/**
	* Instala el proveedor criptográfico pasado como parámetro
	*/
  	public static void setProvider(String providerName, Provider provider) {
  		
  		//-- Test if this provider is installed 
  		if (!existsCryptographicProvider(providerName)) {
  			//-- Install Provider
  			logger.debug ("[Util.setProvider] :: Installed " + provider + "provider");
  			Security.addProvider(provider);
  		}
  	}
  	
  	/**
	* Test if a cryptographic provider is installed in the system
	* 
	* @param providerID ID of the provider
	* @return Result of test
	*/
  	public static boolean existsCryptographicProvider(String providerID) {
  		
  		Provider[] arrayProviders = Security.getProviders();
		for (int i = 0; i < arrayProviders.length; i++) {
			if (arrayProviders[i].getName().equalsIgnoreCase(providerID)) {
				return true;
			}
		}
		
		return false;
  	}

	/**
	 * Obtiene un objeto X509Certificate a partir de su contenido
	 * 
	 * @param bCertificate Bytes del contenido del fichero certificado
	 * @return X.509 Certificate
	 * @throws Exception Error generando el certificado
	 */
	public static X509Certificate getCertificate(byte [] bCertificate) throws NormalizeCertificateException{
		//-- Get inputstream
		ByteArrayInputStream bais = new ByteArrayInputStream (bCertificate);
		
		//-- Get certificate
		return getCertificate(bais);
	}

	/**
	 * Obtiene un objeto X509Certificate a partir de su contenido
	 * 
	 * @param isCertificate Stream de lectura del contenido del fichero del certificado
	 * @return X.509 Certificate
	 * @throws NormalizeCertificateException Error generando el certificado
	 */
	public static X509Certificate getCertificate(InputStream isCertificate) throws NormalizeCertificateException{
		try {
			//-- BC provider
			setProvider (ArangiObject.CRYPTOGRAPHIC_PROVIDER_NAME, ArangiObject.CRYPTOGRAPHIC_PROVIDER);
			
			//-- Get X509Certificate
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509", ArangiObject.CRYPTOGRAPHIC_PROVIDER);
			return (X509Certificate) certFactory.generateCertificate(isCertificate);
		
		} catch (CertificateException e) {
			  logger.info ("Error generando el certificado");
			  throw new NormalizeCertificateException ("Error generando el certificado", e);
		} 
	}

	/**
	 * Gets a X509Certificate object from a file.
	 * 
	 * @param file File whose content is a X.509 certificate in DER or PEM format
	 * @return X.509 Certificate
	 * @throws FileNotFoundException El fichero no existe
	 * @throws NormalizeCertificateException Error generating the certificate
	 */
	public static X509Certificate getCertificate(File file) throws FileNotFoundException, NormalizeCertificateException{
		try {
			return getCertificate (new FileInputStream (file));
		} catch (NormalizeCertificateException e) {
			  logger.info ("Error generando el certificado desde " + file.getAbsolutePath());
			  throw new NormalizeCertificateException ("Error generando el certificado desde " + file.getAbsolutePath(), e);
		} 
	}
	
	/**
	 * Obtiene un X509Certificate en base a una estructura de certificado de
	 * Bouncy Castle.
	 * 
	 * @param certificateHolder Estructura de certificado
	 * @return X509Certificate
	 * @throws CertificateException No se puede obtener el X509Certificate
	 */
	public static X509Certificate getCertificate(X509CertificateHolder certificateHolder) throws CertificateException {
		return new JcaX509CertificateConverter().getCertificate(certificateHolder);
	}

	/**
	* Guarda en disco un array de bytes.
	*
	* @param file Fichero donde se guardará el contenido
	* @param contenido Contenido a guardar
	* @throws Exception No se puede escribir
	*/
	public static void saveFile(File file, byte[] contenido) throws IOException {
		
		try {
			// Los guardamos a disco.
			RandomAccessFile lObjSalida = new RandomAccessFile(file, "rw");
        	lObjSalida.write(contenido,0,contenido.length);
			lObjSalida.close();
		} catch (IOException e){
			  logger.info ("Error saving file at " + file, e);
			  throw e;
		}
	}

	/**
	* Lee un stream de lectura y escribe en el fichero destino
	*
	* @param file Fichero destino 
	* @param iStream Stream de lectura
	* @throws Exception No se puede leer o escribir
	*/
	public static void saveFile(File file, InputStream iStream) throws IOException {
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream (file);
	    	byte[] buffer = new byte [1024];
	        int len;
	        while ((len = iStream.read(buffer)) > -1) {
	        	fos.write(buffer, 0, len);
	        }
		} catch (IOException e){
			  logger.info ("Error saving file at " + file, e);
			  throw e;
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	* Guarda en un stream de escritura lo contenido en un stream de lectura
	*
	* @param out Stream de escritura 
	* @param iStream Stream de lectura
	* @throws Exception No se puede leer o escribir
	*/
	public static void save(OutputStream out, InputStream iStream) throws IOException {
		
		try {
	    	byte[] buffer = new byte [1024];
	        int len;
	        while ((len = iStream.read(buffer)) > -1) {
	        	out.write(buffer, 0, len);
	        }
		} catch (IOException e){
			  logger.info ("Error guardando en stream de escritura", e);
			  throw e;
		} 
	}

	/**
	* Guarda en un stream de escritura un array de bytes
	*
	* @param out Stream de escritura 
	* @param contenido Contenido a guardar
	* @throws Exception No se puede leer o escribir
	*/
	public static void save(OutputStream out, byte[] contenido) throws IOException {
		
		try {
        	out.write(contenido, 0, contenido.length);
		} catch (IOException e){
			  logger.info ("Error guardando en stream de escritura", e);
			  throw e;
		} 
	}

	/**
	* Lee un fichero en el classpath y escribe en el fichero destino
	*
	* @param file Fichero destino 
	* @param classPathFile Path al fichero dentro del classpath. Recordar que estos
	* 	path tienen separadores '/' y no '.'. O sea que si el recurso se encuentra
	* 	en el paquete org.java y se llama recurso.rec el path sería org/java/recurso.rec.
	* @throws FileNotFoundException No es posible leer el recurso dentro del classpath
	* @throws IOException No se puede escribir
	*/
	public static void saveFileFromClasspath(File file, String classPathFile) throws FileNotFoundException, IOException {
		
		InputStream iStream = new Util().getClass().getClassLoader().getResourceAsStream(classPathFile);
		if (iStream == null) {
			throw new FileNotFoundException("No es posible leer el fichero '" + classPathFile + "'dentro del classpath");
		}
		saveFile(file, iStream);
	}
	
    /**
	* Carga en un array de bytes la información contenida en el fichero.  
	*
	* @param file Fichero que contiene la información que se va a cargar	
	* @return Array de bytes que contienen la información guardada en el fichero	
	* @throws IOException No se puede leer
	*/
	public static byte[] loadFile(File file) throws IOException {

		try {
			// Leemos el fichero de disco.
			RandomAccessFile lObjFile = new RandomAccessFile(file, "r");
			byte lBytDatos[] = new byte[(int)lObjFile.length()];
            lObjFile.read(lBytDatos);
            lObjFile.close();
            
            return lBytDatos;
            	
		} catch (IOException e) {
  			  logger.info ("Error cargando el fichero de " + file, e);
  			  throw e;
  		}
	}
	
    /**
	* Carga en un array de bytes la información contenida en un stream de lectura
	*
	* @param is Stream de lectura que contiene la información que se va a cargar	
	* @return Array de bytes que contienen la información guardada en el fichero	
	* @throws IOException No se puede leer
	*/
	public static byte[] readStream(InputStream is) throws IOException {

		try {
	    	//-- Leer el mensaje SOAP 
	    	byte[] buffer = new byte [1024];
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        int len;
	        while ((len = is.read(buffer)) > -1) {
	            baos.write(buffer, 0, len);
	        }
	    	
            return baos.toByteArray();
            	
		} catch (IOException e) {
  			logger.info ("Error cargando el stream de lectura ", e);
  			throw e;
  		}
	}
	
	/**
	 * Copia de ficheros
	 * 
	 * @param srcFile Fichero origen
	 * @param dstFile Fichero destino
	 * @throws FileNotFoundException No existe el fichero origen
	 * @throws IOException Error de entrada / salida
	 */
	public static void copyFile (File srcFile, File dstFile) throws FileNotFoundException, IOException {

		InputStream in = null;
	    OutputStream out = null;
	    try {
	    	in = new FileInputStream(srcFile);
	    	out = new FileOutputStream(dstFile);
		    // Transfer bytes from in to out
		    byte[] buf = new byte[1024];
		    int len;
		    while ((len = in.read(buf)) > 0) {
		        out.write(buf, 0, len);
		    }
	    } finally {
	    	if (in != null) { in.close(); }
	    	if (out != null) { out.close(); }
	    }
	}
	
	/**
	 * Método para codificar en base64
	 * 
	 * @param toCode Array de bytes a codificar
	 * @return Resultado de codificar en base64 el array de bytes pasado como parámetro
	 */
	public static String encodeBase64 (byte [] toCode) {
		
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(toCode);
	}
	
	/**
	 * Método para codificar en base64
	 * 
	 * @param isToEncode Stream de entrada para codificar
	 * @return Resultado de codificar en base64 el array de bytes pasado como parámetro
	 */
	public static String encodeBase64 (InputStream isToEncode) {
		
		BASE64Encoder encoder = new BASE64Encoder();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			encoder.encode(isToEncode, baos);
		} catch (IOException e) {
			return null;
		}
		return baos.toString();
	}
	
	/**
	 * Método para descodificar de base64
	 * 
	 * @param toDecode Cadena a descodificar
	 * @return Resultado de descodificar de base64 la cadena pasada como parámetro
	 */
	public static byte[] decodeBase64 (String toDecode) {
		
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return decoder.decodeBuffer(toDecode);
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Método para descodificar de base64
	 * 
	 * @param isToDecode Cadena a descodificar
	 * @return Resultado de descodificar de base64 la cadena pasada como parámetro
	 */
	public static byte[] decodeBase64 (InputStream isToDecode) {
		
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return decoder.decodeBuffer(isToDecode);
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Método que a partir de un stream de lecture en formato DER obtiene un objeto.
	 * 
	 * @param  isDER Stream de lectura a un objeto DER
	 * @throws IOException Los bytes no contienen un objeto en formato DER
	 */
	public static ASN1Primitive toDER(InputStream isDER) throws IOException {
		
		ASN1Primitive lObjRes = null;
	
		ASN1InputStream  lObjDerOut = new ASN1InputStream(isDER);
		lObjRes = lObjDerOut.readObject();
		
		return lObjRes;							
	}

	/**
	 * Método que a partir de un array de bytes en formato DER obtiene un objeto.
	 * 
	 * @param  bytesDER Bytes con el objeto DER
	 * @throws IOException Los bytes no contienen un objeto en formato DER
	 */
	public static ASN1Primitive getDER(byte[] bytesDER) throws IOException {
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bytesDER);
		
		return toDER (bais);							
	}
	
	/**
	* Dado un array de bytes en formato <a href="http://www.unicode.org/">Unicode</a> 
	* lo devuelve sin este formato.
	* 
	* @param bytesUnicode Array de bytes en formato Unicode. 
	* @return Array de bytes sin caracteres Unicode 
	*/		
	public static byte[] fromUnicode(byte[] bytesUnicode){
		byte[] bytes = new byte[bytesUnicode.length/2];
		for (int i = 0; i< bytes.length; i++) {
			bytes[i] = bytesUnicode[2*i];
		}
		return bytes;
	}
	
	/**
	* Dado un contenido en formato <a href="http://www.unicode.org/">Unicode</a> 
	* lo devuelve sin este formato.<br><br>
	* 
	* Este método permite realizar la conversión sin tener que pasar por un
	* array de bytes, que en el caso de documentos grandes podría llegar a 
	* desbordar la memoria.
	* 
	* @param isUnicode Stream de lectura a un texto en formato Unicode. 
	* @return Stream de lectura sin caracteres Unicode 
	*/		
	public static InputStream fromUnicode(InputStream isUnicode){
		
		logger.debug ("[Util.fromUnicode]::Entrada");
		
		//-- Crear el fichero temporal, y en él escribir uno de cada dos bytes
		//-- del documento original
		FileOutputStream fos = null;
		try {
			File fileTemp = File.createTempFile("fromUnicode", null);
			fos = new FileOutputStream (fileTemp);
			byte[] buffer = new byte[2];
			while (isUnicode.read(buffer) >= 0) {
				fos.write(buffer[0]);
			}
			
			return new FileInputStream (fileTemp);

		} catch (IOException e) {
			logger.info("[Util.fromUnicode]::Un error de entrada/salida impide el paso desde Unicode", e);
			return null;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.info("[Util.fromUnicode]::No se puede cerrar el stream de escritura al fichero temporal", e);
					return null;
				}
			}
		}
	}
	
	/**
	* Dado un array de bytes en formato estándar lo devuelve en formato 
	* <a href="http://www.unicode.org/">Unicode</a>. 
	* 
	* @param bytes Array de bytes en formato estándar. 
	* @return Array de bytes en Unicode 
	*/	
	public static byte[] toUnicode(byte[] bytes){
		byte[] bytesUnicode = new byte[2*bytes.length];
		for (int j = 0; j< bytes.length; j++) {
			bytesUnicode[2*j] = bytes[j];
			bytesUnicode[2*j+1] = 0x00;   
		}
		return bytesUnicode;
	}
	
	/**
	* Dado un contenido en formato estándar lo devuelve en formato 
	* <a href="http://www.unicode.org/">Unicode</a>. 
	* 
	* Este método permite realizar la conversión sin tener que pasar por un
	* array de bytes, que en el caso de documentos grandes podría llegar a 
	* desbordar la memoria.
	* 
	* @param isNormal Stream de lectura a un texto sin formato Unicode. 
	* @return Stream de lectura con caracteres Unicode 
	*/		
	public static File toUnicode(InputStream isNormal){
		
		logger.debug ("[Util.toUnicode]::Entrada");
		
		//-- Crear el fichero temporal, y en él ir escribiendo un byte del documento original
		//-- y otro vacío
		FileOutputStream fos = null;
		try {
			File fileTemp = File.createTempFile("toUnicode", null);
			fos = new FileOutputStream (fileTemp);
			byte[] buffer = new byte[1];
			while (isNormal.read(buffer) >= 0) {
				fos.write(buffer[0]);
				fos.write(0x00);
			}
			
			return fileTemp;

		} catch (IOException e) {
			logger.info("[Util.toUnicode]::Un error de entrada/salida impide el paso a Unicode", e);
			return null;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.info("[Util.toUnicode]::No se puede cerrar el stream de escritura al fichero temporal", e);
					return null;
				}
			}
		}
	}
	
	/**
	 * Comprueba si las Java Cryptographic Extension (JCE) se encuentran instaladas
	 * 
	 * @return Cierto si las JCE se encuentran instaladas y falso en caso contrario
	 */
	public static boolean isJCEInstalled () {
		logger.debug ("[Util.isJCEInstalled]::Entrada");
		
		KeyStoreManager manager;
		try {
			
			manager = new KeyStoreManager(ClassLoader.getSystemResourceAsStream(PRUEBA_PKCS12_FILE), PRUEBA_PKCS12_PIN);
			
		} catch (Exception e) {
			logger.debug ("[Util.isJCEInstalled]::Error abriendo el PKCS12: " + e.getMessage());
			return false;
		} 

		logger.debug ("[Util.isJCEInstalled]::No ha habido error abriendo el PKCS12");
		return true;
		
	}
	
	/**
	 * Convierte un String a la codificación indicada
	 * 
	 * @param text Texto al que cambiar la codificación
	 * @param newCharset Nueva codificación
	 * @return Texto con la nueva codificación
	 * @throws IOException Errores durante la codificación
	 */
	public static String convertCharSet (String text, String newCharset) throws IOException {
		logger.debug ("[Util.convertCharSet]::Entrada");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter out = new OutputStreamWriter(baos, newCharset);
		out.write(text);
		out.flush();
		out.close();
		
		return new String (baos.toByteArray(), newCharset);
	}
	
	/**
	 * Obtiene la representación hexadecimal del array de bytes que se
	 * pasa como parámetro
	 * 
	 * @param bytes Array de bytes
	 * @return Representación hexadecimal del array
	 */
	public static String toHexadecimal (byte[] bytes) {
		logger.debug ("[Util.toHexadecimal]::Entrada::" + bytes);
		
		BigInteger bi = new BigInteger(1, bytes);
		String result = bi.toString(16).toUpperCase();            
		if (result.length() % 2 != 0) {
			result = "0" + result;
		}
		
		return result;
	}
	
	/**
	 * Lee el contenido de un fichero PEM y lo decodifica de base64 a un array de bytes.
	 * Los ficheros PEM contienen una linea de inicio y otra de fin que circunscriben
	 * el contenido en base64. Por ejemplo:
	 * <code>-----BEGIN CERTIFICATE REQUEST-----
	 * base64 encoded PKCS10 certification request -----END CERTIFICATE REQUEST----- </code>
	 * 
	 *  @param pemContent Contenido íntegro del fichero PEM
	 *  @return Contenido del fichero PEM sin cabeceras y decodificado de base64
	 *  @throws IOException Excepción si el fichero PEM no tiene el contenido correcto
	 */
	public static byte[] getBytesFromPEM(String pemContent) throws IOException {
		logger.debug("[Util.getBytesFromPEM]::Entrada::" + pemContent);
	
	    //-- Lineas de principio y fin de pem
	    String[] PKCS10_INIT  = new String[] {"-----BEGIN CERTIFICATE REQUEST-----", "-----BEGIN NEW CERTIFICATE REQUEST-----"};
	    String[] PKCS10_END   = new String[] {"-----END CERTIFICATE REQUEST-----", "-----END NEW CERTIFICATE REQUEST-----"};
	
	    //-- Detección del tipo de PEM
	    byte[] csrBin = null;
	    int headerType = -1;
	    //-- Deteccion del tipo de cabecera
	    for (int i = 0; i < PKCS10_INIT.length; i++) {
	      if (pemContent.startsWith(PKCS10_INIT[i])) {
	        logger.debug("[Util.getBytesFromPEM]:: Tipo de cabecera encontrado [" + i + "]");
	        headerType = i;
	        break;
	      } else {
	        logger.debug("[[Util.getBytesFromPEM]:: El pem no comienza por... '" + PKCS10_INIT[i] + "'");
	      }
	    }
	    
	    //-- Comprobar si el fichero contiene las cabeceras
	    if (headerType == -1) {
	    	logger.debug("[Util.getBytesFromPEM]::No se han encontrado cabeceras, se decodifica de base64");
	    	return decodeBase64(pemContent);
	    }
	    
	    //-- Tiene las cabeceras, leer lo que hay entre ellas
	    ByteArrayInputStream instream = new ByteArrayInputStream(pemContent.getBytes());
	    BufferedReader bufRdr = new BufferedReader(new InputStreamReader(instream));
	    ByteArrayOutputStream ostr = new ByteArrayOutputStream();
	    PrintStream opstr = new PrintStream(ostr);
	    String temp;
	
	    while (((temp = bufRdr.readLine()) != null) && !temp.equals(PKCS10_INIT[headerType])) {
	      continue;
	    }
	
	    if (temp == null) {
	      throw new IOException("[Util.getBytesFromPEM]::Error in input buffer, missing " + PKCS10_INIT[headerType] + " boundary");
	    }
	
	    while (((temp = bufRdr.readLine()) != null) && !temp.equals(PKCS10_END[headerType])) {
	      opstr.print(temp);
	    }
	
	    if (temp == null) {
	      throw new IOException("[Util.getBytesFromPEM]::Error in input buffer, missing " + PKCS10_END[headerType] + " boundary");
	    }
	
	    opstr.close();
	    byte[] internalBytes = decodeBase64(new String (ostr.toByteArray()));
	    logger.info("[Util.getBytesFromPEM]::Devuelve" + internalBytes.length);
	    return internalBytes;
	}
	
	/**
	 * Método que realiza una conexión a la URL pasándole con POST el mensaje.
	 * 
	 *  @param message Mensaje a enviar por post
	 *  @param url URL de la conexión
	 *  @return Respuesta devuelta en la conexión
	 *  @throws IOException
	 */
	public static StringBuffer sendPost (String message, URL url) throws ServiceNotFoundException, ServiceException {
		logger.debug("[Util.getPostConnectionResponse]::Entrada::" + url);
		
		HttpURLConnection uc = null;
		//-- Conectarse
		try {
			uc = (HttpURLConnection) url.openConnection();
	
			uc.setDoInput(true);
			uc.setDoOutput(true);
			uc.setUseCaches(false);
			uc.setRequestProperty("Content-Length", "" + message.length());
			uc.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			uc.setRequestProperty("SOAPAction", "");
			uc.setRequestMethod("POST");
	
			uc.connect();
		} catch (IOException e) {
			throw new ServiceNotFoundException(e);
		}

		//-- Escribir el mensaje
		StringBuffer sb = new StringBuffer ("");
		try {
			byte[] data = message.getBytes("UTF-8");
			DataOutputStream dos = new DataOutputStream(uc.getOutputStream());
			dos.write(data, 0, data.length);
			dos.flush();
			dos.close();
			
			//-- Obtener la respuesta
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String res = null;
			while ((res = br.readLine()) != null) {
				sb.append(res);
			}
			br.close();
			logger.debug("[Util.getPostConnectionResponse]::Response::" + sb.toString());
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		return sb;
	}
	
	/**
	 * Rellena un template con los parámetros que se pasan
	 * 
	 * @param template Plantilla
	 * @param parameters Parámetros para rellenar la plantilla
	 * @return Plantill rellenada
	 * @throws IOException Error leyendo el template
	   */
	  public static String fillTemplate (InputStream template, Map<String,String> parameters) throws IOException {
		  logger.debug("[Util.fillTemplate]::Entrada::" + Arrays.asList(new Object[] { template, parameters }));
		  
		  StringBuffer sb = new StringBuffer();
		  StringBuffer palabra = new StringBuffer();
		  int letra;
		  boolean leyendoPalabra = false;
		  while ((letra = template.read()) > -1) {
			  if ((char)letra == '$' && (char) template.read() == '{') {
				  leyendoPalabra = true;
			  } else {
				  if (leyendoPalabra) {
					  if ((char)letra != '}') {
						  palabra.append((char) letra);
					  } else {
						  if (parameters.containsKey(palabra.toString())) {
							  sb.append (parameters.get(palabra.toString()));
						  }
						  palabra = new StringBuffer();
						  leyendoPalabra = false;
					  }
				  } else {
					  sb.append((char) letra);
				  }
			  }
		  }
		  
		  logger.debug("[Util.fillTemplate]::Entrada::" + Arrays.asList(new Object[] { template, parameters }));
		  return sb.toString();
	
	  }

}
