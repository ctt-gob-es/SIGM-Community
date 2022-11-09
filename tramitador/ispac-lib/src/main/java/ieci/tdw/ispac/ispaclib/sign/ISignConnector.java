package ieci.tdw.ispac.ispaclib.sign;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.sign.exception.InvalidSignatureValidationException;

import java.util.Locale;
import java.util.Map;



/**
 * Interfaz para los conectores de firma digital.
 * 
 */
public interface ISignConnector {

	/**
	 * Obtiene el c�digo HTML para incluir en la p�gina.
	 * @param locale Informaci�n del idioma del cliente.
	 * @param baseURL URL base.
	 * @param hashCode C�digo HASH del documento a firmar.
	 * @return C�digo HTML..  
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public String getHTMLCode(Locale locale, String baseURL, String hashCode) 
		throws ISPACException;
	
	/**
	 * Obtiene el c�digo HTML para incluir en la p�gina.
	 * @param locale Informaci�n del idioma del cliente.
	 * @param baseURL URL base.
	 * @param hashCodes C�digos HASH de los documentos a firmar.
	 * @return C�digo HTML.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public String getHTMLCode(Locale locale, String baseURL, String [] hashCodes) 
		throws ISPACException;
	
	
	/**
	 * Realiza una validaci�n de una firma o un hash.
	 * 
	 * @param signatureValue Valor de la firma
	 * @param signedContentB64 Contenido de la firma en base 64
	 * @throws InvalidSignatureValidationException
	 *             Si la firma no es v�lida.
	 * @return Detalles de la validaci�n.
	 * 
	 */
	public Map verify(String signatureValue, String signedContentB64) throws InvalidSignatureValidationException;
	
	/**
	 * Realiza la firma
	 * @param changeState Indica si hay que cambiar el estado de la firma del documento
	 * @throws ISPACException
	 */
	public void sign(boolean changeState) throws ISPACException;
	
	/**
	 * Inicializa el conector con el documento a firmar y el contexto del cliente
	 * @param signDocument
	 * @param clientContext
	 */
	public void initializate(SignDocument signDocument, IClientContext clientContext);
	
	/**
	 * Obtiene la informaci�n del certificado
	 * @param x509CertString
	 * @return
	 * @throws ISPACException
	 */
	public String getInfoCert(String x509CertString)throws ISPACException;

	
	/**
	 * @param x509CertString Certificado a validar
	 * @return Objeto con informacion de la validez del certificado
	 * @throws ISPACException
	 */
	public ResultadoValidacionCertificado validateCertificate(String x509CertString)throws ISPACException;
	
	/**
	 * [dipucr-Felipe #1366]
	 * @param changeState
	 * @param motivo
	 * @param nif
	 * @param nombre
	 * @throws ISPACException
	 */
	public void rechazarDocumento(boolean changeState, String motivo, String nif, String nombre) throws ISPACException;
	
	/****************************************************************************
	 * FUNCIONES NECESARIAS ANTES DE PORTAFIRMAS
	 * [dipucr-Felipe #1246] Eliminar cuando todos los aytos est�n migrados
	 ****************************************************************************/
	
	/**
	 * [Dipucr-Agustin #781]
	 * Realiza la prefirma
	 * @param changeState Indica si hay que cambiar el estado de la firma del documento
	 * @throws ISPACException
	 */
	public String presign(boolean changeState) throws ISPACException;
	
	/**
	 * [Dipucr-Agustin #781]
	 * Realiza la postfirma
	 * @param changeState Indica si hay que cambiar el estado de la firma del documento
	 * @param path del fichero temporal firmado
	 * @throws ISPACException
	 */
	public String postsign(String pathFicheroTempFirmado, boolean changeState) throws ISPACException;
	
}
