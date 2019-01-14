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
package es.accv.arangi.base.certificate.validation.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.certificate.validation.OCSPResponse;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.certificate.validation.ServiceException;
import es.accv.arangi.base.exception.certificate.validation.ServiceNotFoundException;
import es.accv.arangi.base.util.Util;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Clase que implementa la validación de certificados mediante llamadas
 * a los servicios web de &#64;Firma (puede verse la documentación en la zona
 * segura de la página web de la ACCV: 
 * <a href="https://www.accv.es:8445/secure_area/descargas/descargas/docsatFirma/bienvenida.htm" target="zonaSegura">
 * https://www.accv.es:8445/secure_area/descargas/descargas/docsatFirma/bienvenida.htm</a>).
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José Manuel Gutiérrez Núñez</a>
 *
 */
public class AFirmaCertificateValidationService implements CertificateValidationService{

	/**
	 * URL del servicio web de &#64;firma en explotación
	 */
	public static final String PRODUCTION_URL = "http://afirma.accv.es/afirmaws/services/ValidarCertificado";
	
	/**
	 * URL del servicio web de &#64;firma en test
	 */
	public static final String TEST_URL = "http://preafirma.accv.es/afirmaws/services/ValidarCertificado";
	
	/*
	 * Nombre de la plantilla para generar llamadas sin securizar
	 */
	private static final String TEMPLATE_WITHOUT_SECURITY = "es/accv/arangi/base/template/arangi-afirma_validate_template.xml";

	/*
	 * Nombre de la plantilla para generar llamadas mediante usuario y contraseña
	 */
	private static final String TEMPLATE_USER_PASSWORD = "es/accv/arangi/base/template/arangi-afirma_validate_secure_template.xml";

	/*
	 * Código de &#64;Firma para certificado válido
	 */
	private static final int AFIRMA_RESULT_OK		= 0;
	
	/*
	 * Código de &#64;Firma para resultado incorrecto
	 */
	private static final int AFIRMA_RESULT_NOK		= 1;
	
	/*
	 * Código de &#64;Firma para cadena de validación incorrecta
	 */
	private static final int AFIRMA_RESULT_CHAIN_VALIDATION_INVALID	= 2;
	
	/*
	 * Código de &#64;Firma para certificado revocado
	 */
	private static final int AFIRMA_RESULT_REVOKED		= 3;
	
	/*
	 * Código de &#64;Firma para error obteniendo el resultado
	 */
	private static final int AFIRMA_RESULT_ERROR		= 4;
	
	/**
	 * Formateador de fechas que vienen de &#64;Firma
	 */
	public static final SimpleDateFormat AFIRMA_DATE_FORMAT	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	/*
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(AFirmaCertificateValidationService.class);
	
	/*
	 * URL de acceso a los servicios web de &#64;Firma
	 */
	private URL url;
	
	/*
	 * Identificador de la aplicación en &#64;Firma
	 */
	private String idAplicacion;
	
	/*
	 * Usuario 
	 */
	private String user;
	
	/*
	 * Contraseña
	 */
	private String password;
	
	//-- Constructores	
	
	/**
	 * Constructor por defecto: si se usa este constructor será necesario inicializar
	 * el objeto.
	 */
	public AFirmaCertificateValidationService() {
		super();
	}
	
	/**
	 * Constructor en el que pasar la información necesaria para crear 
	 * este objeto.
	 * 
	 * @param url URL al servico web de &#64;Firma. Los posibles valores se pueden
	 * 	encontrar en los campos estáticos de esta clase PRODUCTION_URL y
	 *  TEST_URL.
	 * @param idAplicacion ID de su aplicación. Este valor se le entregó en
	 * 	el momento en que su aplicación fue dada de alta en la plataforma de
	 * 	.
	 * @param user Nombre de usuario para el caso en que se deba realizar la
	 * 	llamada securizada mediante usuario y contraseña.
	 * @param password Contraseña para el caso en que se deba realizar la
	 * 	llamada securizada mediante usuario y contraseña.
	 */
	public AFirmaCertificateValidationService(URL url, String idAplicacion, String user,
			String password) {
		super();
		this.url = url;
		this.idAplicacion = idAplicacion;
		this.user = user;
		this.password = password;
	}

	/**
	 * Inicializa el objeto
	 * 
	 * @param url URL al servico web de &#64;Firma. Los posibles valores se pueden
	 * 	encontrar en los campos estáticos de esta clase PRODUCTION_URL y
	 *  TEST_URL.
	 * @param idAplicacion ID de su aplicación. Este valor se le entregó en
	 * 	el momento en que su aplicación fue dada de alta en la plataforma de
	 * 	&#64;Firma.
	 * @param user Nombre de usuario para el caso en que se deba realizar la
	 * 	llamada securizada mediante usuario y contraseña.
	 * @param password Contraseña para el caso en que se deba realizar la
	 * 	llamada securizada mediante usuario y contraseña.
	 */
	public void initialize (URL url, String idAplicacion, String user, String password) {
		this.url = url;
		this.idAplicacion = idAplicacion;
		this.user = user;
		this.password = password;
	}

	/**
	 * Valida un certificado mediante una llamada a un servicio externo.
	 * 
	 * @param certificate Certificado a validar
	 * @param extraParams Parámetros extra por si fueran necesarios para 
	 * 	realizar la validación
	 * @return Objeto con el resultado y, si el servicio web lo permite, los
	 * 	campos más significativos del certificado.
	 * @throws ServiceNotFoundException El servicio no se encuentra disponible
	 * @throws ServiceException La llamada al servicio devuelve un error
	 */
	public CertificateValidationServiceResult validate(Certificate certificate, 
			Map<String, Object> extraParams) throws ServiceNotFoundException, ServiceException {
		
		logger.debug("[AFirmaCertificateValidationService.validate]::Entrada::" + Arrays.asList(new Object[] { certificate, extraParams }));
		
		//-- Obtener el template
		InputStream isTemplate;
		if (this.user != null && this.password != null) {
			isTemplate = new Util().getClass().getClassLoader().getResourceAsStream(TEMPLATE_USER_PASSWORD);
		} else {
			isTemplate = new Util().getClass().getClassLoader().getResourceAsStream(TEMPLATE_WITHOUT_SECURITY);
		}

		//-- Obtener los parámetros
		HashMap<String, String> parameters = new HashMap<String, String>();
		if (this.user != null && this.password != null) {
			parameters.put("user", this.user);
			parameters.put("password", this.password);
		} 
		try {
			parameters.put("certificate", Util.encodeBase64(certificate.toDER()));
		} catch (NormalizeCertificateException e) {
			//-- El certificado ya se normalizó al entrar, no se dará el error
			logger.info ("[AFirmaCertificateValidationService.validate]", e);
		}
		parameters.put("idAplicacion", this.idAplicacion);

		//-- Obtener el mensaje
		String message;
		try {
			message = Util.fillTemplate(isTemplate, parameters);
		} catch (IOException e) {
			logger.info ("[AFirmaCertificateValidationService.validate]::Error construyendo el mensaje", e);
			throw new ServiceException("Error construyendo el mensaje", e);
		}
		logger.debug("[AFirmaCertificateValidationService.validate]::Se ha obtenido el mensaje a enviar a @Firma: " + message);
		
		//-- Enviar el mensaje
		StringBuffer respuesta = Util.sendPost(message, url);
		logger.debug("[AFirmaCertificateValidationService.validate]::Se ha obtenido la respuesta de @Firma: " + respuesta);

		//-- Variables para obtener el resultado
		Map<String, Object> campos = new HashMap<String, Object>();
		int resultado = ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED;
		Date fechaRevocacion = null;
		int motivoRevocacion = -1;
		OCSPResponse respuestaOCSP = null;
		
		//-- Comprobar si es mensaje de error
		if (respuesta.indexOf("codigoError") > -1) {
			//-- Comprobar si es porque no se trata el certificado (COD_066) o no se trata para
			//-- el ID de aplicación (COD_063) o el tipo de certificado se encuentra deshabilitado(COD_064)
			if (respuesta.indexOf("COD_066") > -1 || respuesta.indexOf("COD_063") > -1 || respuesta.indexOf("COD_064") > -1) {
				logger.debug("[AFirmaCertificateValidationService.validate]::El certificado es desconocido");
				resultado = ValidationResult.RESULT_CERTIFICATE_UNKNOWN;
				return new CertificateValidationServiceResult(resultado, campos);
			} else {
				logger.info("[AFirmaCertificateValidationService.validate]::La respuesta de @Firma es de&#64;Firma");
				throw new ServiceException(respuesta.substring(respuesta.indexOf("codigoError&gt;") + 15, respuesta.indexOf("&lt;/codigoError")) + " - " +
						respuesta.substring(respuesta.indexOf("descripcion&gt;") + 15, respuesta.indexOf("&lt;/descripcion")));
			}
		}
		
		//-- Mensaje OK, extraer la información
		logger.debug("[AFirmaCertificateValidationService.validate]::La respuesta de @Firma no es de error");
		String xml = respuesta.substring(respuesta.indexOf("&lt;?xml"), respuesta.indexOf("</ValidarCertificadoReturn>"));
		xml = xml.replaceAll("\\&lt;", "<").replaceAll("\\&gt;", ">");
		
		Document doc;
		XPath xpath;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder builder = dbf.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(xml)));
			XPathFactory factory = XPathFactory.newInstance();
			xpath = factory.newXPath();
		} catch (Exception e) {
			logger.info("[AFirmaCertificateValidationService.validate]::Error obteniendo el documento DOM o el XPath", e);
			throw new ServiceException("Error obteniendo el documento DOM o el XPath", e);
		}

		//-- Obtener el resultado
		try {
			logger.debug("[AFirmaCertificateValidationService.validate]::Obteniendo el resultado");
			XPathExpression expr = xpath.compile("//*[local-name()='ResultadoProcesamiento']/*[local-name()='ResultadoValidacion']/*[local-name()='resultado']");
			Node resultNode = (Node) expr.evaluate (doc, XPathConstants.NODE);
			int aFirmaResultado = Integer.parseInt(resultNode.getTextContent());
			switch (aFirmaResultado) {
				case AFIRMA_RESULT_OK:
					resultado = ValidationResult.RESULT_VALID;
					break;
				case AFIRMA_RESULT_NOK:
					resultado = ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED;
					break;
				case AFIRMA_RESULT_CHAIN_VALIDATION_INVALID:
					resultado = ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID;
					break;
				case AFIRMA_RESULT_REVOKED:
					resultado = ValidationResult.RESULT_CERTIFICATE_REVOKED;
					expr = xpath.compile("//*[local-name()='InfoMetodoVerificacion']/*[local-name()='fechaRevocacion']");
					resultNode = (Node) expr.evaluate (doc, XPathConstants.NODE);
					String sFecha = resultNode.getTextContent().substring(0,10) + " " + resultNode.getTextContent().substring(15,23);
					fechaRevocacion = AFIRMA_DATE_FORMAT.parse(sFecha);
					expr = xpath.compile("//*[local-name()='InfoMetodoVerificacion']/*[local-name()='motivo']");
					resultNode = (Node) expr.evaluate (doc, XPathConstants.NODE);
					motivoRevocacion = Integer.parseInt(resultNode.getTextContent());
					break;
				case AFIRMA_RESULT_ERROR:
					resultado = ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED;
					break;
			}
			logger.debug("[AFirmaCertificateValidationService.validate]::El resultado es " + resultado);
		} catch (Exception e) {
			logger.info("[AFirmaCertificateValidationService.validate]::Error obteniendo el resultado del XML de respuesta", e);
			throw new ServiceException("Error obteniendo el resultado del XML de respuesta", e);
		}
		
		//-- Obtener los campos
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='ResultadoProcesamiento']/*[local-name()='InfoCertificado']/*[local-name()='Campo']");
			NodeList fieldNodes = (NodeList) expr.evaluate (doc, XPathConstants.NODESET);
			for (int i=0;i<fieldNodes.getLength();i++) {
				Node fieldNode = fieldNodes.item(i);
				String nombre = fieldNode.getChildNodes().item(0).getTextContent();
				String valor = fieldNode.getChildNodes().item(1).getTextContent();
				campos.put(nombre, valor);
			}
			logger.debug("[AFirmaCertificateValidationService.validate]::Se han obtenido " + campos.size() + " campos");

		} catch (Exception e) {
			logger.info("[AFirmaCertificateValidationService.validate]::Error obteniendo los campos del XML de respuesta", e);
		}
		
		//-- Obtener la respuesta OCSP
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='ResultadoProcesamiento']/*[local-name()='ResultadoValidacion']/*[local-name()='ValidacionEstado']/*[local-name()='InfoMetodoVerificacion']/*[local-name()='tokenOCSP']");
			Node node = (Node) expr.evaluate (doc, XPathConstants.NODE);
			if (node != null) {
				logger.debug("[AFirmaCertificateValidationService.validate]::Existe la respuesta OCSP dentro de la respuesta de @Firma");
				try {
					respuestaOCSP = new OCSPResponse(Util.decodeBase64(node.getTextContent()));
				} catch (Exception e) {
					//-- Tal vez está devolviendo el basicOcspResponse
					ASN1Sequence responseBytes = new DERSequence(new ASN1Encodable[] { new ASN1ObjectIdentifier("1.3.6.1.5.5.7.48.1.1"), new DEROctetString(Util.decodeBase64(node.getTextContent())) });
					DERTaggedObject taggedObject = new DERTaggedObject(true, 0, responseBytes);
					ASN1Sequence sequence = new DERSequence(new ASN1Encodable[] { new ASN1Enumerated(0), taggedObject });
					respuestaOCSP = new OCSPResponse(sequence.getEncoded());
				}
			}

		} catch (Exception e) {
			logger.info("[AFirmaCertificateValidationService.validate]::Error obteniendo la respuesta OCSP", e);
		}

		//-- Devolver resultado
		CertificateValidationServiceResult certResult = new CertificateValidationServiceResult(resultado, campos);
		if (fechaRevocacion != null) {
			certResult.setRevocationDate(fechaRevocacion);
			certResult.setRevocationReason(motivoRevocacion);
		}
		if (respuestaOCSP != null) {
			certResult.setOcspResponse(respuestaOCSP);
		}
		
		return certResult;
	}
	
	/**
	 * Obtiene la URL de los servicios web de &#64;Firma en producción
	 * 
	 * @return URL
	 */
	public static URL getProductionURL () {
		try {
			return new URL(PRODUCTION_URL);
		} catch (MalformedURLException e) {
			// Siempore estará bien formada
			return null;
		}
	}

	/**
	 * Obtiene la URL de los servicios web de &#64;Firma en test
	 * 
	 * @return URL
	 */
	public static URL getTestURL () {
		try {
			return new URL(TEST_URL);
		} catch (MalformedURLException e) {
			// Siempore estará bien formada
			return null;
		}
	}


}
