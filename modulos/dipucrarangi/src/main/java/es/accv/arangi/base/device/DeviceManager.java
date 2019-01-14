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
package es.accv.arangi.base.device;

import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.log4j.Logger;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.device.util.EncryptWithSessionKeyResult;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.device.AliasNotFoundException;
import es.accv.arangi.base.exception.device.AliasNotFreeException;
import es.accv.arangi.base.exception.device.CertificateInvalidException;
import es.accv.arangi.base.exception.device.CipherException;
import es.accv.arangi.base.exception.device.DeletingObjectException;
import es.accv.arangi.base.exception.device.DeviceFullException;
import es.accv.arangi.base.exception.device.DeviceNotFoundException;
import es.accv.arangi.base.exception.device.DeviceNotInitializedException;
import es.accv.arangi.base.exception.device.IncorrectPINException;
import es.accv.arangi.base.exception.device.KeyPairException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.OpeningDeviceException;
import es.accv.arangi.base.exception.device.ReadingStreamException;
import es.accv.arangi.base.exception.device.SavingObjectException;
import es.accv.arangi.base.exception.device.SearchingException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.exception.signature.XMLDocumentException;




/**
 * Interfaz para los gestores de dispositivos criptográficos. Además provee métodos comunes a todos ellos.<br><br>
 * 
 * Trataremos como dispositivos criptográficos a los repositorios capaces de alamacenar objetos de tres tipos: certificados X509, 
 * claves públicas y claves privadas.<br><br>
 * 
 * El almacenamiento se realizará asociativamente (i.e: <i>a la</i> "HashMap"): se utilizará un alias como indexador de elementos, 
 * no puediendo haber dos elementos <b>de un mismo tipo</b> (p.ej. dos certificados) con el mismo alias.<br><br>
 * 
 * Si existe al menos un objeto con un alias X, entonces ese alias pasará a estar ocupado.<br><br>
 * 
 * Todos los objetos, sean del tipo que sea, podrán referenciarse individualmente por su alias.<br><br>
 *  
 * Todos los objetos de distinto tipo que compartan un alias, formarán un grupo que estará relacionado por algún vínculo (p.ej. 
 * un certificado y su clave privada; o una clave pública y su clave privada). Arangi entiende la semántica de esos vínculos. 
 * Por ello, algunas de las operaciones tienen comportamientos muy particulares (p.ej. borrados, búsquedas, etc.) comportamiento 
 * que se explica en cada caso concreto.<br><br>
 *
 */
public abstract class DeviceManager extends ArangiObject {
	/** Logger de clase */
	private static Logger logger = Logger.getLogger(DeviceManager.class);
	
	/** DN a incluir como SubjectDN y IssuerDN al crear certificados autofirmados */
	public static final String SELFSIGNED_CERTIFICATE_DN = "C=ES,O=Generalitat Valenciana,OU=Arangi,CN=Certificado autofirmado de Arangi,GIVENNAME=Arangi,SURNAME=Arangi";
	
	/**
	 * Parte del nombre del provider de SUN
	 */
	public static final String SUN_PROVIDER_PREFIX = "SunPKCS11-";
	

	/**
	 * Constructor por defecto para todos los gestores de dispositivos. 
	 */
	public DeviceManager() {
		super();
	}

	/**
	 * Cierra un dispositivo criptográfico. Libera los recursos creados durante la apertura. Deja el contenido del 
	 * dispositivo inmanejable, a no ser que vuelva a ser abierto 
	 */
	public abstract void close();
	
	/**
	 * Indica si existen objetos con cierto alias
	 * @param alias
	 * @return True si el alias está libre. False si existe al menos un objeto 
	 * indexado con ese alias
	 * @throws SearchingException Error buscando en el dispositivo
	 */
	public abstract boolean isAliasFree(String alias) throws SearchingException;
	
	/**
	 * Genera un par de claves dentro del dispositivo con el tamaño por defecto
	 * (1024 bytes) 
	 * 
	 * @param alias Alias que tendrá el par de claves creado dentro del 
	 * dispositivo
	 * @return Un par de claves. Si el dispositivo subyacente es un PKCS#11, la clave privada 
	 * 	del par no contiene la clave en sí sino una referencia a la existente dentro del 
	 * 	PKCS#11 (y de donde NO puede ser extraida y/o leida)
	 * @throws AliasNotFreeException El alias donde se guardará el par de claves ya está ocupado
	 * @throws DeviceNotInitializedException El dispositivo no se ha abierto
	 * @throws KeyPairException No se ha podido generar el par de claves
	 */
	public abstract KeyPair generateKeyPair(String alias)  throws AliasNotFreeException, DeviceNotInitializedException, KeyPairException;

	/**
	 * Genera un par de claves dentro del dispositivo.
	 * 
	 * @param alias Alias que tendrá el par de claves creado dentro del 
	 * dispositivo
	 * @param keySize Tamaño de las claves
	 * @return Un par de claves. Si el dispositivo subyacente es un PKCS#11, la clave privada 
	 * 	del par no contiene la clave en sí sino una referencia a la existente dentro del 
	 * 	PKCS#11 (y de donde NO puede ser extraida y/o leida)
	 * @throws AliasNotFreeException El alias donde se guardará el par de claves ya está ocupado
	 * @throws DeviceNotInitializedException El dispositivo no se ha abierto
	 * @throws KeyPairException No se ha podido generar el par de claves
	 */
	public abstract KeyPair generateKeyPair(String alias, int keySize)  throws AliasNotFreeException, DeviceNotInitializedException, KeyPairException;

	/**
	 * Genera una solicitud de certificado (PKCS#10) lista para ser enviada como paso inicial durante el proceso de generación 
	 * de un certificado por parte de una CA 
	 * @param subjectDN Asunto del certificado
	 * @param subjectAlternativeDN Nombre alternativo del asunto
	 * @param rsaKeyPair Par de claves
	 * @return PKCS#10
	 */
	public abstract byte[] generatePKCS10(String subjectDN, String subjectAlternativeDN, KeyPair rsaKeyPair);
	
	/**
	 * Cambia el alias utilizado para indexar a un objeto o conjunto de objetos
	 * 
	 * @param oldAlias Alias origen
	 * @param newAlias Alias destino
	 * @throws SearchingException Se ha producido un error buscando los objetos del alias
	 * 	'oldAlias'
	 * @throws AliasNotFoundException El valor del parámetro 'oldAlias' no indica un alias
	 * 	existente en el dispositivo
	 * @throws AliasNotFreeException El alias cuyo nombre es el valor del parámetro 'newAlias'
	 * 	ya existe en el dispositivo
	 * @throws SavingObjectException No se ha podido grabar en el nuevo alias
	 * @throws DeletingObjectException No se ha podido eliminar el antiguo alias
	 */
	public abstract void changeAlias(String oldAlias, String newAlias) throws  SearchingException, AliasNotFoundException, AliasNotFreeException, SavingObjectException, DeletingObjectException;

	/**
	 * Cambia el PIN que permite abrir el dispositivo criptográfico subyacente.
	 * 
	 * @param newpin PIN nuevo
	 * @throws SavingObjectException Error cambiando el PIN
	 * @throws OpeningDeviceException Error abriendo el dispositivo con el nuevo PIN
	 */
	public abstract void changePin(String newpin) throws SavingObjectException, OpeningDeviceException;
	
	/**
	 * Busca certificados en el dispositivo permitiendo 4 criterios, todos ellos opcionales. En caso de indicarse dos o más 
	 * criterios, la búsqueda será <b>conjuntiva</b> (a <i>and</i> b). Basta con asignarle un <i>null</i> a un criterio para 
	 * que no se utilice en la búsqueda. Si no se indica ninguno entonces se obtendrán todos los certificados.
	 * 
	 * @param findInAlias String a buscar en los alias del dispositivo. Un certificado será incluido en el resultado si su alias 
	 * 	asociado <b>contiene</b> el texto indicado.
	 * @param findInSubjectDn String a buscar en los SubjectDN de los certificados. Un certificado será incluido en el resultado 
	 * 	si su SubjectDN <b>contiene</b> el texto indicado.
	 * @param findInIssuerDn String a buscar en los IssuerDN de los certificados. Un certificado será incluido en el resultado si 
	 * 	su IssuerDN <b>contiene</b> el texto indicado.
	 * @param findSerialNumber String con la representación en <b>hexadecimal</b> del número de serie del certificado a buscar. Un 
	 * 	certificado será incluido en el resultado si su SerialNumber <b>es exactamente</b> el texto indicado.
	 * @throws SearchingException Errores durante la búsqueda (se producirán sólo en dispositivos PKCS#11)
	 * @return Devuelve una lista de X509Certificate con los certificados que encajen con todos los criterios de búsqueda proporcionados. 
	 */
	public abstract List getCertificates(String findInAlias, String findInSubjectDn, String findInIssuerDn, String findSerialNumber) throws SearchingException;

	/**
	 * Obtiene el certificado asociado al alias.
	 * 
	 * @param alias Alias
	 * @return Certificado asociado al alias o null si el alias no tiene asociado ningún certificado
	 * @throws SearchingException Errores durante la búsqueda (se producirán sólo en dispositivos PKCS#11)
	 */
	public abstract X509Certificate getCertificate (String alias) throws SearchingException;
	
	/**
	 * Busca la clave pública asociada al alias
	 * 
	 * @return Clave pública asociada al alias o null si no existe el alias o éste
	 * no contiene una clave pública
	 * @throws LoadingObjectException No se puede obtener la clave. 
	 */
	public abstract PublicKey getPublicKey(String findInAlias) throws LoadingObjectException;

	/**
	 * Obtiene la clave privada asociada al alias. El password para recuperar la
	 * clave será el PIN del dispositivo (caso por defecto).
	 * 
	 * @param alias Alias donde se encuentra la clave
	 * @return Clave privada asociada al alias o null si no existe el alias o éste
	 * no contiene una clave privada
	 * 
	 * @throws LoadingObjectException No se puede obtener la clave. Una posible causa 
	 * podría ser que el password de la misma no coincide con el PIN del dispositivo.
	 */
	public abstract PrivateKey getPrivateKey(String alias) throws LoadingObjectException;
	
	/**
	 * Busca la clave privada asociada al alias.
	 * 
	 * @param alias Alias donde se encuentra la clave
	 * @param keyPassword Password de la clave privada
	 * @return Clave privada asociada al alias o null si no existe el alias o éste
	 * 	no contiene una clave privada
	 * 
	 * @throws LoadingObjectException No se puede obtener la clave. Probáblemente el 
	 * 	password pasado como parámetro no sea correcto.
	 */
	public abstract PrivateKey getPrivateKey(String alias, String keyPassword) throws LoadingObjectException;
	
	/**
	 * Elimina la clave privada del alias. 
	 * 
	 * ATENCION: Si el dispositivo no permite almacenar claves públicas sueltas
	 * (caso del PKCS#12 o KeyStore de SUN), no se permitirá esta operación. Se 
	 * puede utilizar el método {@link #deleteObjects(String) deleteObjects} para
	 * eliminar todos los objetos asociados al alias. 
	 * 
	 * @param label Alias que contiene la clave a eliminar
	 * @return 0 si el alias no existe o no contiene una clave privada. 1 si se
	 * ha eliminado la clave privada. 
	 * @throws DeletingObjectException Errores durante el borrado de la clave
	 */
	public abstract int deletePrivateKey(String label) throws DeletingObjectException;

	/**
	 * Importa un certificado
	 * 
	 * ATENCION: Si el dispositivo no permite almacenar claves públicas sueltas
	 * (caso del PKCS#12 o KeyStore de SUN), al importar un certificado a un
	 * alias vacío se lanzará una excepción.
	 * 
	 * @param cert Certificado
	 * @param label Etiqueta donde se guardará el certificado
	 * @throws DeviceFullException El dispositivo está lleno y no se puede importar
	 * 	el certificado
	 * @throws CertificateInvalidException El certificado es nulo o se ha producido
	 * 	algún tipo de error durante su manipulación
	 * @throws SavingObjectException No se puede guardar el certificado
	 */
	public abstract void importCertificate(X509Certificate cert, String label) throws DeviceFullException, CertificateInvalidException, SavingObjectException;
	
	/**
	 * Importa los elementos bajo el labelOrigen (certificado y/o clave privada) a 
	 * la etiqueta labelDestino. El almacén de claves origen puede ser en este caso de 
	 * tipo JKS o PKCS#12.<br>
	 * NOTA: Sólo se importarán claves privadas RSA.
	 * 
	 * @param is Stream de lectura para obtener el almacén de claves (JKS o PKCS#12)
	 * @param password Contraseña del almacén de claves
	 * @param labelOrigen Etiqueta donde se encuentra la información a importar en el 
	 * 	almacén de claves origen. Si tiene valor nulo se importan todas las etiquetas.
	 * @param labelDestino Etiqueta donde se importará la información en el almacén de
	 * 	claves destino. Si labelOrigen es nulo este parámetro no se utilizará. Si
	 * 	labelOrigen no es nulo pero este parámetro sí, la información se guardará bajo
	 * 	la etiqueta labelOrigen (o su nombre alternativo si la etiqueta labelOrigen ya
	 * 	existe en el almacén de claves destino).
	 * @throws DeviceFullException El dispositivo está lleno y no se puede realizar la
	 * 	importación
	 * @throws DeviceNotFoundException El stream de lectura es nulo
	 * @throws ReadingStreamException No ha sido posible leer el stream de lectura
	 * @throws AliasNotFoundException El alias 'labelOrigen' no existe en el keystore
	 * 	origen
	 * @throws LoadingObjectException Se ha producido un error al asociar una clave y
	 * 	un certificado en el almacen de claves destino
	 * @throws SavingObjectException No se pueden guardar los objetos en el dispositivo
	 * 	destino
	 * @throws OpeningDeviceException Error no controlado abriendo el dispositivo
	 * @throws IncorrectPINException El PIN proporcionado para abrir el dispositivo no es correcto
	 */
	public abstract void importAliasFromKeystore(InputStream is, String password, String labelOrigen, String labelDestino )  
		throws DeviceFullException, DeviceNotFoundException, ReadingStreamException, AliasNotFoundException, LoadingObjectException, SavingObjectException, OpeningDeviceException, IncorrectPINException;

	/**
	 * Importa los elementos bajo el labelOrigen (certificado y/o clave privada) a 
	 * la etiqueta labelDestino. El almacén de claves origen puede ser cualquiera
	 * de los definidos por las clases hijas de ésta.<br>
	 * 
	 * NOTA: Sólo se importarán claves privadas RSA.<br>
	 * 
	 * Restricción: hay que tener en cuenta que si se importa de un PKCS#11 las 
	 * 	claves privadas no se importarán en ningún caso.	  
	 * 
	 * @param deviceManager Manager del almacén de claves origen
	 * @param labelOrigen Etiqueta donde se encuentra la información a importar en el 
	 * 	almacén de claves origen. Si tiene valor nulo se importan todas las etiquetas.
	 * @param labelDestino Etiqueta donde se importará la información en el almacén de
	 * 	claves destino. Si labelOrigen es nulo este parámetro no se utilizará. Si
	 * 	labelOrigen no es nulo pero este parámetro sí, la información se guardará bajo
	 * 	la etiqueta labelOrigen (o su nombre alternativo si la etiqueta labelOrigen ya
	 * 	existe en el almacén de claves destino).
	 * @throws DeviceFullException El dispositivo está lleno y no se puede realizar la
	 * 	importación
	 * @throws DeviceNotFoundException El dispositivo origen es nulo
	 * @throws AliasNotFoundException El alias 'labelOrigen' no existe en el keystore
	 * 	origen
	 * @throws LoadingObjectException Se ha producido un error al asociar una clave y
	 * 	un certificado en el almacen de claves destino
	 * @throws SavingObjectException No se pueden guardar los objetos en el dispositivo
	 * 	destino
	 */
	public abstract void importAliasFromKeystore(DeviceManager deviceManager, String labelOrigen, String labelDestino )  
		throws DeviceFullException, DeviceNotFoundException, AliasNotFoundException, LoadingObjectException, SavingObjectException;

	/**
	 * Importa todos los elementos de un almacén de claves. El almacén de claves origen 
	 * puede ser cualquiera de los definidos por las clases hijas de ésta. Los nombres
	 * originarios de las etiquetas se mantendrán en el destino, a no ser que ya existan, 
	 * en cuyo caso se buscará un nombre alternativo: por ejemplo 'firma' pasaría a ser
	 * 'firma0'.<br>
	 * 
	 * NOTA: Sólo se importarán claves privadas RSA.<br>
	 * 
	 * Restricción: hay que tener en cuenta que si se importa de un PKCS#11 las 
	 * 	claves privadas no se importarán en ningún caso.	 
	 * 
	 * @param deviceManager Manager del almacén de claves origen
	 * @throws DeviceFullException El dispositivo está lleno y no se puede realizar la
	 * 	importación
	 * @throws DeviceNotFoundException El dispositivo origen es nulo
	 * @throws LoadingObjectException Se ha producido un error al asociar una clave y
	 * 	un certificado en el almacen de claves destino
	 * @throws SavingObjectException No se pueden guardar los objetos en el dispositivo
	 * 	destino
	 */
	public abstract void importFromKeystore(DeviceManager deviceManager) 
		throws DeviceFullException, DeviceNotFoundException, LoadingObjectException, SavingObjectException;

	/**
	 * Elimina todos los objetos asociados al alias pasado como parámetro.
	 * 
	 * @param label Etiqueta a eliminar
	 * @return Número de objetos eliminados
	 * @throws DeletingObjectException Excepción borrando objetos
	 */
	public abstract int deleteObjects(String label) throws DeletingObjectException;

	/**
	 * Obtiene un array con los nombres de todos los alias del dispositivo
	 * 
	 * @return Array con los nombres de todos los alias del dispositivo
	 * @throws SearchingException Error buscando los elementos en el dispositivo
	 */
	public abstract String[] getAliasNamesList () throws SearchingException;
	
	/**
	 * Firma un documento, cuyo hash se pasa como parámetro, con la clave privada que se 
	 * encuentre en la etiqueta del alias. 
	 * 
	 * @param hash Hash del documento a firmar
	 * @param label Etiqueta que debe tener asociada una clave privada con la que se firmará
	 * el documento.
	 * @return Array de bytes con la firma. La firma será el cifrado RSA del hash con la clave
	 * privada asociada al alias.
	 * @throws HashingException El hash pasado es nulo o no se puede obtener con él el objeto
	 * 	DER para la firma
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws CipherException Error durante el proceso de cifrado
	 */
	public abstract byte[] signBytesHash (byte[] hash, String label) 
		throws HashingException, AliasNotFoundException, LoadingObjectException, CipherException;
	
	/**
	 * Firma un documento, cuyo hash se pasa como parámetro, con la clave privada que se 
	 * encuentre en la etiqueta del alias. 
	 * 
	 * @param hash Hash del documento a firmar
	 * @param label Etiqueta que debe tener asociada una clave privada con la que se firmará
	 * el documento.
	 * @param cipherAlgorithm Algoritmo de cifrado empleado para realizar la encriptación. Pueden
	 * encontrarse algunas constantes de dichos algoritmos en la clase {@link es.accv.arangi.base.algorithm.CipherAlgorithm CipherAlgorithm} 
	 * o se pueden utilizar las constantes para algoritmos de cifrado empleadas en Java. 
	 * @return Array de bytes con la firma. La firma será el cifrado del hash mediante el 
	 * algoritmo de cifrado con la clave privada asociada al alias.
	 * @throws HashingException El hash pasado es nulo o no se puede obtener con él el objeto
	 * 	DER para la firma
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws CipherException Error durante el proceso de cifrado
	 */
	public abstract byte[] signBytesHash (byte[] hash, String label, String cipherAlgorithm) 
		throws HashingException, AliasNotFoundException, LoadingObjectException, CipherException;
	
	/**
	 * Firma el documento pasado como parámetro, con la clave privada que se  encuentre en 
	 * la etiqueta del alias. Se firma con el algoritmo de firma por defecto: SHA-1whithRSA.
	 * 
	 * @param document Stream de lectura al contenido del documento a firmar
	 * @param label Etiqueta que debe tener asociada una clave privada con la que se firmará
	 * el documento.
	 * @return Array de bytes con la firma. La firma será el resultado de aplicar los algoritmos
	 * SHA-1 y RSA al documento.
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws HashingException No es posible obtener el hash del documento o su versión en 
	 * 	formato DER durante el proceso de firma
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws SignatureException Error durante el proceso de firma
	 */
	public abstract byte[] signDocument (InputStream document, String label) 
		throws AliasNotFoundException, HashingException, LoadingObjectException, SignatureException;
	
	/**
	 * Firma el documento pasado como parámetro, con la clave privada que se encuentre en la 
	 * etiqueta del alias. 
	 * 
	 * @param document Stream de lectura al contenido del documento a firmar
	 * @param label Etiqueta que debe tener asociada una clave privada con la que se firmará
	 * el documento.
	 * @param signatureAlgorithm Algoritmo de firma empleado para realizar la firma. Pueden
	 * encontrarse algunas constantes de dichos algoritmos en la clase {@link es.accv.arangi.base.algorithm.DigitalSignatureAlgorithm DigitalSignatureAlgorithm} 
	 * o se pueden utilizar las constantes para algoritmos de firma empleadas en Java. 
	 * @return Array de bytes con la firma. La firma será el resultado de aplicar los algoritmos
	 * de hashing y cifrado con la clave privada asociada al alias al documento.
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws HashingException No es posible obtener el hash del documento o su versión en 
	 * 	formato DER durante el proceso de firma
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws SignatureException Error durante el proceso de firma
	 */
	public abstract byte[] signDocument (InputStream document, String label, String signatureAlgorithm) 
		throws AliasNotFoundException, HashingException, LoadingObjectException, SignatureException;

	/**
	 * Firma el documento pasado como parámetro, con la clave privada que se  encuentre en 
	 * la etiqueta del alias. Se firma con el algoritmo de firma por defecto: SHA-1whithRSA.
	 * 
	 * @param document Documento a firmar
	 * @param label Etiqueta que debe tener asociada una clave privada con la que se firmará
	 * el documento.
	 * @return Array de bytes con la firma. La firma será el resultado de aplicar los algoritmos
	 * SHA-1 y RSA al documento.
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws HashingException No es posible obtener el hash del documento o su versión en 
	 * 	formato DER durante el proceso de firma
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws SignatureException Error durante el proceso de firma
	 */
	public abstract byte[] signDocument (IDocument document, String label) 
		throws AliasNotFoundException, HashingException, LoadingObjectException, SignatureException;
	
	/**
	 * Firma el documento pasado como parámetro, con la clave privada que se encuentre en la 
	 * etiqueta del alias. 
	 * 
	 * @param document Documento a firmar
	 * @param label Etiqueta que debe tener asociada una clave privada con la que se firmará
	 * el documento.
	 * @param signatureAlgorithm Algoritmo de firma empleado para realizar la firma. Pueden
	 * encontrarse algunas constantes de dichos algoritmos en la clase {@link es.accv.arangi.base.algorithm.DigitalSignatureAlgorithm DigitalSignatureAlgorithm} 
	 * o se pueden utilizar las constantes para algoritmos de firma empleadas en Java. 
	 * @return Array de bytes con la firma. La firma será el resultado de aplicar los algoritmos
	 * de hashing y cifrado con la clave privada asociada al alias al documento.
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws HashingException No es posible obtener el hash del documento o su versión en 
	 * 	formato DER durante el proceso de firma
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws SignatureException Error durante el proceso de firma
	 */
	public abstract byte[] signDocument (IDocument document, String label, String signatureAlgorithm) 
		throws AliasNotFoundException, HashingException, LoadingObjectException, SignatureException;

	/**
	 * Cifra un documento con la clave pública que se encuentre en la etiqueta del alias. Si
	 * se utiliza cifrado asimétrico hay que tener en cuenta que su coste temporal es elevado, 
	 * por lo que no es aconsejable cifrar documentos grandes. 
	 * 
	 * @param document Contenido del documento a cifrar
	 * @param label Etiqueta que debe tener asociada una clave privada con la que se cifrará
	 * el documento.
	 * @param cipherAlgorithm Algoritmo de cifrado empleado para realizar la encriptación. Pueden
	 * encontrarse algunas constantes de dichos algoritmos en la clase {@link es.accv.arangi.base.algorithm.CipherAlgorithm CipherAlgorithm} 
	 * o se pueden utilizar las constantes para algoritmos de cifrado empleadas en Java. 
	 * @return Array de bytes con el documento encriptado
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	cifrar no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	cifrar
	 * @throws CipherException Error durante el proceso de cifrado
	 */
	public abstract byte[] encrypt (byte[] document, String label, String cipherAlgorithm) 
		throws AliasNotFoundException, LoadingObjectException, CipherException;
	
	/**
	 * Cifra un documento con la clave pública del certificado pasado como parámetro.
	 * 
	 * @param document Documento a cifrar
	 * @param certificate Certificado cuya clave pública cifrará el documento
	 * @param cipherAlgorithm Algoritmo de cifrado 
	 * @return Documento cifrado
	 * @throws CipherException Error durante el cifrado
	 */
	public static byte[] encrypt(byte[] document, es.accv.arangi.base.certificate.Certificate certificate, String cipherAlgorithm) throws CipherException {
		logger.debug ("[KeyStoreManager.encrypt]::Entrada::" + Arrays.asList(new Object[] { document, certificate.getPublicKey(), cipherAlgorithm }));
		
		//-- Obtener la clave pública del certificado
		PublicKey key = certificate.getPublicKey();
		
		//-- Cifrar el documento
		return encrypt(document, key, cipherAlgorithm);
	}

	/**
	 * Cifra un documento con la clave pasada como parámetro.
	 * 
	 * @param document Documento a cifrar
	 * @param key Clave que cifrará el documento
	 * @param cipherAlgorithm Algoritmo de cifrado 
	 * @return Documento cifrado
	 * @throws CipherException Error durante el cifrado
	 */
	public static byte[] encrypt(byte[] document, Key key, String cipherAlgorithm) throws CipherException {
		logger.debug ("[KeyStoreManager.encrypt]::Entrada::" + Arrays.asList(new Object[] { document, key, cipherAlgorithm }));
		
		//-- Cifrar
		try {
			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] bytesSignature = cipher.doFinal(document);
			logger.debug("[KeyStoreManager.encrypt]::Se ha completado el cifrado");
	        return bytesSignature;
	        
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.encrypt]::No existe el algoritmo de cifrado '" + cipherAlgorithm + "'", e);
			throw new CipherException ("No existe el algoritmo de cifrado '" + cipherAlgorithm + "'", e);
		} catch (InvalidKeyException e) {
			logger.info("[KeyStoreManager.encrypt]::La clave no es válida", e);
			throw new CipherException ("La clave no es válida", e);
		} catch (IllegalBlockSizeException e) {
			logger.info("[KeyStoreManager.encrypt]::El tamaño de la información de un bloque de cifrado es incorrecta", e);
			throw new CipherException ("El tamaño de la información de un bloque de cifrado es incorrecta", e);
		} catch (BadPaddingException e) {
			logger.info("[KeyStoreManager.encrypt]::El relleno de la información no es el adecuado para el algoritmo de relleno", e);
			throw new CipherException ("El relleno de la información no es el adecuado para el algoritmo de relleno", e);
		} catch (NoSuchPaddingException e) {
			logger.info("[KeyStoreManager.encrypt]::No existe el algoritmo de relleno", e);
			throw new CipherException ("No existe el algoritmo de relleno", e);
		} 
	}

	/**
	 * Cifra un documento con una clave de sesión y cifra dicha clave con la clave pública que 
	 * se encuentra bajo el alias pasado como parámetro. 
	 * 
	 * @param document Contenido del documento a cifrar
	 * @param label Etiqueta que debe tener asociada una certificado con cuya clave pública se 
	 * 	cifrará la clave de sesión.
	 * @return Objeto que incluye el documento encriptado, la clave de sesión simétrica y los
	 * 	datos aleatorios utilizados durante el cifrado.
	 * @throws SearchingException Error obteniendo el certificado desde el dispositivo
	 * @throws CipherException Error cifrando
	 * @throws XMLDocumentException Error obteniendo el documento resultado
	 */
	public EncryptWithSessionKeyResult encryptWithSessionKey (byte[] document, String label) throws SearchingException, CipherException, XMLDocumentException {
		logger.debug ("[KeyStoreManager.encryptWithSessionKey]::Entrada::" + Arrays.asList(new Object[] { document, label }));
		
		return encryptWithSessionKey(document, getCertificate(label));
	}
	
	/**
	 * Cifra un documento con una clave de sesión y cifra dicha clave con la clave pública del
	 * certificado pasado como parámetro. 
	 * 
	 * @param document Contenido del documento a cifrar
	 * @param x509Certificate Certificado que realizará el cifrado
	 * @return Objeto que incluye el documento encriptado, la clave de sesión simétrica y los
	 * 	datos aleatorios utilizados durante el cifrado.
	 * @throws SearchingException Error obteniendo el certificado desde el dispositivo
	 * @throws CipherException Error cifrando
	 * @throws XMLDocumentException Error obteniendo el documento resultado
	 */
	public EncryptWithSessionKeyResult encryptWithSessionKey (byte[] document, X509Certificate x509Certificate) throws SearchingException, CipherException, XMLDocumentException {
		logger.debug ("[KeyStoreManager.encryptWithSessionKey]::Entrada::" + Arrays.asList(new Object[] { document, x509Certificate }));
		
		//-- Obtener el certificado que se encuentra en el alias
		Certificate certificate;
		try {
			certificate = new Certificate (x509Certificate);
		} catch (NormalizeCertificateException e) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::El certificado no se encuentra normalizado", e);
			throw new SearchingException ("El certificado no se encuentra normalizado", e);
		}
		
		//-- Comprobar si tiene uso de cifrado
		if (!certificate.isKeyUsageDataEncipherment()) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::El certificado no tiene uso de clave de cifrado de datos");
			throw new CipherException ("El certificado no tiene uso de clave de cifrado de datos");
		}
		
		//-- Crear una llave (simétrica) para Cifrar
		KeyGenerator keyGenerator;
		try {
			keyGenerator = KeyGenerator.getInstance("DESede");
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::No existe el algoritmo DESede necesario para obtener la clave de sesión", e);
			throw new CipherException ("No existe el algoritmo DESede necesario para obtener la clave de sesión", e);
		}
		SecretKey sessionKey = keyGenerator.generateKey();
		
		//-- Crear el objeto para cifrar, cifrar los datos y obtener los datos aleatorios
		byte[] datosCifrados, randomData;
		try {
			Cipher cipher = Cipher.getInstance("1.2.840.113549.3.7");
			cipher.init(Cipher.ENCRYPT_MODE, sessionKey);
			
			//-- Cifrar los datos
			datosCifrados = cipher.doFinal(document);
			logger.debug ("[KeyStoreManager.encryptWithSessionKey]::Se han obtenido los datos cifrados::" + datosCifrados);
			
			//-- Random Data
			randomData = cipher.getIV();
			logger.debug ("[KeyStoreManager.encryptWithSessionKey]::Se han obtenido los datos aleatorios::" + randomData);
			
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::No existe el algoritmo DESedeCBC (1.2.840.113549.3.7) necesario para cifrar el documento", e);
			throw new CipherException ("No existe el algoritmo DESedeCBC (1.2.840.113549.3.7) necesario para cifrar el documento", e);
		} catch (InvalidKeyException e) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::La clave de sesión generada no es válida", e);
			throw new CipherException ("La clave de sesión generada no es válida", e);
		} catch (IllegalBlockSizeException e) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::Cifrando el documento: el tamaño de la información de un bloque de cifrado es incorrecta", e);
			throw new CipherException ("Cifrando el documento: el tamaño de la información de un bloque de cifrado es incorrecta", e);
		} catch (BadPaddingException e) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::Cifrando el documento: El relleno de la información no es el adecuado para el algoritmo de relleno", e);
			throw new CipherException ("Cifrando el documento: El relleno de la información no es el adecuado para el algoritmo de relleno", e);
		} catch (NoSuchPaddingException e) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::Cifrando el documento: no existe el algoritmo de relleno", e);
			throw new CipherException ("Cifrando el documento: no existe el algoritmo de relleno", e);
		} 
		
		//-- Encriptar la llave de sesion con la del certificado.
		byte[] wrappedKey;
		try {
			PublicKey publicKey = certificate.getPublicKey();
			Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
			cipher.init(Cipher.WRAP_MODE, publicKey);
			wrappedKey = cipher.wrap(sessionKey);
			logger.debug ("[KeyStoreManager.encryptWithSessionKey]::Se ha obtenido la llave de sesión cifrada::" + wrappedKey);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::No existe el algoritmo RSA/NONE/PKCS1Padding necesario para cifrar la clave de sesión", e);
			throw new CipherException ("No existe el algoritmo RSA/NONE/PKCS1Padding necesario para cifrar la clave de sesión", e);
		} catch (InvalidKeyException e) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::La clave pública del certificado no es válida", e);
			throw new CipherException ("La clave pública del certificado no es válida", e);
		} catch (IllegalBlockSizeException e) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::Cifrando la clave de sesión: el tamaño de la información de un bloque de cifrado es incorrecta", e);
			throw new CipherException ("Cifrando la clave de sesión: el tamaño de la información de un bloque de cifrado es incorrecta", e);
		} catch (NoSuchPaddingException e) {
			logger.info("[KeyStoreManager.encryptWithSessionKey]::Cifrando la clave de sesión: no existe el algoritmo de relleno", e);
			throw new CipherException ("Cifrando la clave de sesión: no existe el algoritmo de relleno", e);
		} 
		
		//-- Crear el objeto resupesta
		return new EncryptWithSessionKeyResult(datosCifrados, wrappedKey, randomData);
		
	}
	
	/**
	 * Descifra un objeto que es el resultado de un cifrado con clave de sesión.
	 * 
	 * @param result Resultado de un cifrado con clave de sesión
	 * @param label Etiqueta que debe tener asociada una clave privada con la que se cifrará
	 * el documento.
	 * @return documento desencriptado
	 * @throws LoadingObjectException No se puede cargar la clave privada
	 * @throws XMLDocumentException Error leyendo el objeto de cifrado
	 * @throws CipherException Error descifrando
	 */
	public byte[] decryptWithSessionKey (EncryptWithSessionKeyResult result, String label) throws LoadingObjectException, XMLDocumentException, CipherException {
		logger.debug ("[KeyStoreManager.decryptWithSessionKey]::Entrada::" + Arrays.asList(new Object[] { result, label }));
		
		//-- Obtener la clave privada que se encuentra en el alias
		PrivateKey privateKey = getPrivateKey(label);
		
		//-- Descifrar la clave de sesión
		Key sessionKey;
		try {
			Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
			cipher.init(Cipher.UNWRAP_MODE, privateKey);
			sessionKey = cipher.unwrap(result.getSessionKey(), "DESede", Cipher.SECRET_KEY);
			logger.debug ("[KeyStoreManager.decryptWithSessionKey]::Se ha obtenido la clave de sesión descifrada::" + sessionKey);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.decryptWithSessionKey]::No existe el algoritmo RSA/NONE/PKCS1Padding o DESede necesarios para descifrar la clave de sesión", e);
			throw new CipherException ("No existe el algoritmo RSA/NONE/PKCS1Padding o DESede necesarios para descifrar la clave de sesión", e);
		} catch (InvalidKeyException e) {
			logger.info("[KeyStoreManager.decryptWithSessionKey]::La clave privada no es válida", e);
			throw new CipherException ("La clave privada no es válida", e);
		} catch (NoSuchPaddingException e) {
			logger.info("[KeyStoreManager.decryptWithSessionKey]::Descifrando la clave de sesión: no existe el algoritmo de relleno", e);
			throw new CipherException ("Descifrando la clave de sesión: no existe el algoritmo de relleno", e);
		} 
		
		//-- Crear el objeto para descifrar y descifrar los datos
		try {
			Cipher cipher = Cipher.getInstance("1.2.840.113549.3.7");
			cipher.init(Cipher.DECRYPT_MODE, sessionKey, new IvParameterSpec(result.getRandomData()));
			
			//-- Descifrar los datos
			byte[] datosDescifrados = cipher.doFinal(result.getEncryptedDocument());
			logger.debug ("[KeyStoreManager.decryptWithSessionKey]::Se han obtenido los datos descifrados::" + datosDescifrados);
			
			return datosDescifrados;
			
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.decryptWithSessionKey]::No existe el algoritmo DESedeCBC (1.2.840.113549.3.7) necesario para descifrar el documento", e);
			throw new CipherException ("No existe el algoritmo DESedeCBC (1.2.840.113549.3.7) necesario para descifrar el documento", e);
		} catch (InvalidKeyException e) {
			logger.info("[KeyStoreManager.decryptWithSessionKey]::La clave de sesión generada no es válida", e);
			throw new CipherException ("La clave de sesión generada no es válida", e);
		} catch (IllegalBlockSizeException e) {
			logger.info("[KeyStoreManager.decryptWithSessionKey]::Descifrando el documento: el tamaño de la información de un bloque de cifrado es incorrecta", e);
			throw new CipherException ("Descifrando el documento: el tamaño de la información de un bloque de cifrado es incorrecta", e);
		} catch (BadPaddingException e) {
			logger.info("[KeyStoreManager.decryptWithSessionKey]::Descifrando el documento: El relleno de la información no es el adecuado para el algoritmo de relleno", e);
			throw new CipherException ("Descifrando el documento: El relleno de la información no es el adecuado para el algoritmo de relleno", e);
		} catch (NoSuchPaddingException e) {
			logger.info("[KeyStoreManager.decryptWithSessionKey]::Descifrando el documento: no existe el algoritmo de relleno", e);
			throw new CipherException ("Descifrando el documento: no existe el algoritmo de relleno", e);
		} catch (InvalidAlgorithmParameterException e) {
			logger.info("[KeyStoreManager.decryptWithSessionKey]::Algoritmo inválido durante el descifrado", e);
			throw new CipherException ("Algoritmo inválido durante el descifrado", e);
		} 
	}
	
	/**
	 * Descifra un documento encriptado con la clave privada que se encuentre en la etiqueta 
	 * del alias. 
	 * 
	 * @param encryptedDocument Contenido del documento encriptado
	 * @param label Etiqueta que debe tener asociada una clave privada con la que se cifrará
	 * el documento.
	 * @param cipherAlgorithm Algoritmo de cifrado empleado para realizar la encriptación. Pueden
	 * encontrarse algunas constantes de dichos algoritmos en la clase {@link es.accv.arangi.base.algorithm.CipherAlgorithm CipherAlgorithm} 
	 * o se pueden utilizar las constantes para algoritmos de cifrado empleadas en Java. 
	 * @return Array de bytes con el documento desencriptado
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	cifrar no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	cifrar
	 * @throws CipherException Error durante el proceso de descifrado
	 */
	public abstract byte[] decrypt (byte[] encryptedDocument, String label, String cipherAlgorithm) throws AliasNotFoundException, LoadingObjectException, CipherException;
	
	/**
	 * Elimina todos los objetos que contiene el dispositivo. Este borrado no incluye el reinicio 
	 * del dispositivo, por lo que el PIN y el PUK existentes no serán modificados.
	 * 
	 * @throws DeletingObjectException Durante el proceso de borrado no ha sido posible
	 * 	eliminar alguno de los objetos del dispositivo.
	 */
	public abstract void clear () throws DeletingObjectException;
	


}
