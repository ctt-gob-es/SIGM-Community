package es.gob.afirma.signfolder.server.proxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.gob.afirma.core.AOException;
import es.gob.afirma.core.misc.AOUtil;
import es.gob.afirma.core.misc.Base64;
import es.gob.afirma.core.misc.http.UrlHttpManagerFactory;
import es.gob.afirma.core.misc.http.UrlHttpMethod;
import es.gob.afirma.core.signers.AOSignConstants;
import es.gob.afirma.core.signers.TriphaseData;
import es.gob.afirma.core.signers.TriphaseData.TriSign;

/**
 * Manejador para el uso est&aacute;tico de las operaciones de prefirma y postfirma.
 */
public class TriSigner {

	/** Identificador de la operaci&oacute;n de prefirma en servidor. */
	private static final String OPERATION_PRESIGN = "pre"; //$NON-NLS-1$

	/** Identificador de la operaci&oacute;n de postfirma en servidor. */
	private static final String OPERATION_POSTSIGN = "post"; //$NON-NLS-1$

	/** Nombre del par&aacute;metro que identifica la operaci&oacute;n trif&aacute;sica en la URL del servidor de firma. */
	private static final String PARAMETER_NAME_OPERATION = "op"; //$NON-NLS-1$

	/** Nombre del par&aacute;metro que identifica la operaci&oacute;n criptogr&aacute;fica en la URL del servidor de firma. */
	private static final String PARAMETER_NAME_CRYPTO_OPERATION = "cop"; //$NON-NLS-1$

	private static final String CRYPTO_OPERATION_TYPE_SIGN = "sign"; //$NON-NLS-1$
	private static final String CRYPTO_OPERATION_TYPE_COSIGN = "cosign"; //$NON-NLS-1$
	private static final String CRYPTO_OPERATION_TYPE_COUNTERSIGN = "countersign"; //$NON-NLS-1$

	private static final String HTTP_CGI = "?"; //$NON-NLS-1$
	private static final String HTTP_EQUALS = "="; //$NON-NLS-1$
	private static final String HTTP_AND = "&"; //$NON-NLS-1$

	// Parametros que necesitamos para la URL de las llamadas al servidor de firma
	private static final String PARAMETER_NAME_DOCID = "doc"; //$NON-NLS-1$
	private static final String PARAMETER_NAME_ALGORITHM = "algo"; //$NON-NLS-1$
	private static final String PARAMETER_NAME_FORMAT = "format"; //$NON-NLS-1$
	private static final String PARAMETER_NAME_CERT = "cert"; //$NON-NLS-1$
	private static final String PARAMETER_NAME_EXTRA_PARAM = "params"; //$NON-NLS-1$
	private static final String PARAMETER_NAME_SESSION_DATA = "session"; //$NON-NLS-1$

	/** Indicador de finalizaci&oacute;n correcta de proceso. */
	private static final String SUCCESS = "OK"; //$NON-NLS-1$

	/** Codificaci&oacute;n de texto por defecto. */
	private static final String DEFAULT_ENCODING = "utf-8"; //$NON-NLS-1$

	/** Manejador del log. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TriSigner.class);

	/**
	 * Prefirma el documento de una petici&oacute;n y muta la propia peticion para almacenar en ella
	 * el resultado.
	 * @param docReq Petici&oacute;n de firma de un documento.
	 * @param signerCert Certificado de firma.
	 * @param signServiceUrl URL del servicio de firma.
	 * @param forcedExtraParams Par&aacute;metros de firma que se deben aplicar forzosamente.
	 * @throws IOException Cuando no se puede obtener el documento para prefirmar.
	 * @throws AOException Cuando ocurre un error al generar la prefirma.
	 */
	public static void doPreSign(final TriphaseSignDocumentRequest docReq,
			final X509Certificate signerCert,
			final String signServiceUrl,
			final String forcedExtraParams) throws IOException, AOException {

		// Configuramos el formato y la operacion criptografica adecuada
		String cop;
		final String format = normalizeSignatureFormat(docReq.getSignatureFormat());
		if (AOSignConstants.SIGN_FORMAT_PADES.equals(format)) {
			cop = CRYPTO_OPERATION_TYPE_SIGN;
		}
		else {
			 cop = normalizeOperationType(docReq.getCryptoOperation());
		}

		// Empezamos la prefirma
		try {
			// Llamamos a una URL pasando como parametros los datos necesarios para
			// configurar la operacion:
			//  - Operacion trifasica (prefirma o postfirma)
			//  - Operacion criptografica (firma, cofirma o contrafirma)
			//  - Formato de firma
			//  - Algoritmo de firma a utilizar
			//  - Certificado de firma
			//  - Parametros extra de configuracion
			//  - Datos o identificador del documento a firmar
			final StringBuffer urlBuffer = new StringBuffer();
			urlBuffer.append(signServiceUrl).append(HTTP_CGI).
			append(PARAMETER_NAME_OPERATION).append(HTTP_EQUALS).append(OPERATION_PRESIGN).append(HTTP_AND).
			append(PARAMETER_NAME_CRYPTO_OPERATION).append(HTTP_EQUALS).append(cop).append(HTTP_AND).
			append(PARAMETER_NAME_FORMAT).append(HTTP_EQUALS).append(format).append(HTTP_AND).
			append(PARAMETER_NAME_ALGORITHM).append(HTTP_EQUALS).append(digestToSignatureAlgorithmName(docReq.getMessageDigestAlgorithm())).append(HTTP_AND).
			append(PARAMETER_NAME_CERT).append(HTTP_EQUALS).append(Base64.encode(signerCert.getEncoded(), true)).append(HTTP_AND).
			append(PARAMETER_NAME_DOCID).append(HTTP_EQUALS).append(docReq.getContent());

			// Forzamos que se incluyan una serie de parametros en la configuracion de firma. Si ya
			// se incluia alguno de estos con otro valor acabara siendo pisado ya que en un properties
			// tiene preferencia el ultimo valor leido
			final Properties extraParams = buildExtraParams(docReq.getParams());
			addFormatExtraParam(extraParams, docReq.getSignatureFormat());
			addForcedExtraParams(extraParams, forcedExtraParams.split(";")); //$NON-NLS-1$
			urlBuffer.append(HTTP_AND).append(PARAMETER_NAME_EXTRA_PARAM)
			.append(HTTP_EQUALS).append(AOUtil.properties2Base64(extraParams));

			final byte[] triphaseResult = UrlHttpManagerFactory.getInstalledManager().readUrl(urlBuffer.toString(), UrlHttpMethod.POST);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Respuesta de prefirma del servicio de firma:\n{}", new String(triphaseResult)); //$NON-NLS-1$
			}
			final TriphaseData triphaseData = loadTriphaseResponse(triphaseResult);
			docReq.setPartialResult(triphaseData);
			urlBuffer.setLength(0);
		}
		catch (final CertificateEncodingException e) {
			throw new AOException("Error decodificando el certificado del firmante", e); //$NON-NLS-1$
		}
		catch (final IOException e) {
			throw new AOException("Error en la llamada de prefirma al servidor", e); //$NON-NLS-1$
		}
	}

	/**
	 * Compone un objeto de propiedades a partir de un listado de extraParams
	 * proporcionados en base 64.
	 *
	 * @param params Cadena base64 con un listado de propiedades.
	 * @return Conjunto de par&aacute;metros.
	 * @throws AOException Cuando no se pueden decodificar los par&aacute;metros.
	 */
	private static Properties buildExtraParams(final String params) throws AOException {

		 final Properties extraParams = new Properties();

		 if (params != null && params.length() > 0) {
			 byte[] paramsBytes;
			 try {
				 try {
					 paramsBytes = new String(Base64.decode(params), DEFAULT_ENCODING).replace("\\n", "\n").getBytes(DEFAULT_ENCODING); //$NON-NLS-1$ //$NON-NLS-2$
				 } catch (final UnsupportedEncodingException e) {
					 paramsBytes = new String(Base64.decode(params)).replace("\\n", "\n").getBytes(); //$NON-NLS-1$ //$NON-NLS-2$
				 }
				 extraParams.load(new ByteArrayInputStream(paramsBytes));
			 } catch (final IOException e) {
				 throw new AOException("Error al decodificar los parametros de firma", e); //$NON-NLS-1$
			 }
		 }

		 return extraParams;
	 }

	/**
	 * Agrega a los extraParams cualquier par&aacute;metro necesario en base al formato
	 * de firma establecido. Esto es necesario porque el Portafirmas utiliza el nombre
	 * de formato para configurar las firmas cuando el cliente @firma lo hace en base
	 * a extraParams.
	 * @param extraParams Conjunto de par&aacute;metros.
	 * @param format Formato de firma definido para el documento.
	 */
	private static void addFormatExtraParam(final Properties extraParams, final String format) {
		if (format != null) {
			if (format.toUpperCase().contains("XADES")) { //$NON-NLS-1$
				if (format.toUpperCase().contains("ENVELOPING")) { //$NON-NLS-1$
					extraParams.setProperty("format", AOSignConstants.SIGN_FORMAT_XADES_ENVELOPING); //$NON-NLS-1$
				}
				else if (format.toUpperCase().contains("ENVELOPED")) { //$NON-NLS-1$
					extraParams.setProperty("format", AOSignConstants.SIGN_FORMAT_XADES_ENVELOPED); //$NON-NLS-1$
				}
			}
		}
	}

	/**
	 * Agrega a los par&aacute;metros de configuraci&oacute;n de la firma unos
	 * par&aacute;metros adicionales dando preferencias a estos &uacute;ltimos
	 * en caso de pisarte.
	 * @param extraParams Par&aacute;metros de firma.
	 * @param forcedParams Listado de nuevos par&aacute;metros.
	 * @throws AOException Cuando no se pueden decodificar los par&aacute;metros de firma.
	 */
	private static void addForcedExtraParams(final Properties extraParams, final String[] forcedParams) throws AOException {

		// Si no hay prametros que agregar devolvemos los parametros originales
		if (forcedParams == null ||
				forcedParams.length == 0 ||
				forcedParams.length == 1 && forcedParams[0].trim().length() == 0) {
			return;
		}

		for (final String forcedParam : forcedParams) {
			final int sepPos = forcedParam.indexOf('=');
			if (sepPos != -1 && sepPos != forcedParam.length() - 1) {
				extraParams.setProperty(forcedParam.substring(0, sepPos), forcedParam.substring(sepPos + 1));
			}
		}
	}

	/**
	 * Postfirma el documento de una petici&oacute;n.
	 * @param docReq Petici&oacute;n de firma de un documento.
	 * @param signerCert Certificado de firma.
	 * @param signServiceUrl URL del servicio de firma.
	 * @param forcedExtraParams Par&aacute;metros de firma que se deben aplicar forzosamente.
	 * @throws IOException Cuando no se puede obtener el documento para postfirmar.
	 * @throws AOException Cuando ocurre un error al generar la postfirma.
	 */
	public static void doPostSign(final TriphaseSignDocumentRequest docReq,
			final X509Certificate signerCert,
			final String signServiceUrl,
			final String forcedExtraParams) throws IOException, AOException {

		// Configuramos el formato y la operacion criptografica adecuada
		String cop;
		final String format = normalizeSignatureFormat(docReq.getSignatureFormat());
		if (AOSignConstants.SIGN_FORMAT_PADES.equals(format)) {
			cop = CRYPTO_OPERATION_TYPE_SIGN;
		}
		else {
			 cop = normalizeOperationType(docReq.getCryptoOperation());
		}

		final byte[] triSignFinalResult;
		try {
			final StringBuffer urlBuffer = new StringBuffer();
			urlBuffer.append(signServiceUrl).append(HTTP_CGI).
			append(PARAMETER_NAME_OPERATION).append(HTTP_EQUALS).append(OPERATION_POSTSIGN).append(HTTP_AND).
			append(PARAMETER_NAME_CRYPTO_OPERATION).append(HTTP_EQUALS).append(cop).append(HTTP_AND).
			append(PARAMETER_NAME_FORMAT).append(HTTP_EQUALS).append(format).append(HTTP_AND).
			append(PARAMETER_NAME_ALGORITHM).append(HTTP_EQUALS).append(digestToSignatureAlgorithmName(docReq.getMessageDigestAlgorithm())).append(HTTP_AND).
			append(PARAMETER_NAME_CERT).append(HTTP_EQUALS).append(Base64.encode(signerCert.getEncoded(), true));

			// Forzamos que se incluyan una serie de parametros en la configuracion de firma. Si ya
			// se incluia alguno de estos con otro valor acabara siendo pisado ya que en un properties
			// tiene preferencia el ultimo valor leido
			final Properties extraParams = buildExtraParams(docReq.getParams());
			addFormatExtraParam(extraParams, docReq.getSignatureFormat());
			addForcedExtraParams(extraParams, forcedExtraParams.split(";")); //$NON-NLS-1$
			urlBuffer.append(HTTP_AND).append(PARAMETER_NAME_EXTRA_PARAM)
			.append(HTTP_EQUALS).append(AOUtil.properties2Base64(extraParams));

			// Datos de sesion en forma de properies codificado en Base64 URL SAFE
			if (docReq.getPartialResult() != null) {
				final String sessionData = docReq.getPartialResult().toString();
				urlBuffer.append(HTTP_AND).append(PARAMETER_NAME_SESSION_DATA).append(HTTP_EQUALS)
				.append(Base64.encode(sessionData.getBytes(DEFAULT_ENCODING), true));
			}

			final String content = docReq.getContent();
			if (content != null) {
				urlBuffer.append(HTTP_AND).append(PARAMETER_NAME_DOCID).append(HTTP_EQUALS).append(content);
			}

			triSignFinalResult = UrlHttpManagerFactory.getInstalledManager().readUrl(urlBuffer.toString(), UrlHttpMethod.POST);
			urlBuffer.setLength(0);
		}
		catch (final CertificateEncodingException e) {
			throw new AOException("Error decodificando el certificado del firmante", e); //$NON-NLS-1$
		}
		catch (final IOException e) {
			throw new AOException("Error en la llamada de postfirma al servidor", e); //$NON-NLS-1$
		}

		// Analizamos la respuesta del servidor
		final String stringTrimmedResult = new String(triSignFinalResult).trim();
		if (!stringTrimmedResult.startsWith(SUCCESS)) {
			throw new AOException("La firma trifasica no ha finalizado correctamente"); //$NON-NLS-1$
		}

		// Los datos no se devuelven, se quedan en el servidor
		try {
			docReq.setResult(Base64.decode(stringTrimmedResult.replace(SUCCESS + " NEWID=", ""), true)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch (final IOException e) {
			throw new AOException("El resultado de NEWID del servidor no estaba en Base64", e); //$NON-NLS-1$
		}
	}

	/**
	 * Transforma el nombre de un algoritmo de huella digital en uno de firma que
	 * utilice ese mismo algoritmo de huella digital y un cifrado RSA.
	 * @param digestAlgorithm Algoritmo de huella digital.
	 * @return Nombre del algoritmo de firma.
	 */
	private static String digestToSignatureAlgorithmName(final String digestAlgorithm) {
		return digestAlgorithm.replace("-", "").toUpperCase() + "withRSA";  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
	}

	/**
	 * Normalizamos el nombre del formato de firma.
	 * @param format Formato de firma.
	 * @return Nombre de formato normalizado o el mismo formato de entrada si no se ha encontrado correspondencia.
	 */
	private static String normalizeSignatureFormat(final String format) {
		String normalizeFormat = format;
		if (format.toLowerCase().contains("pdf") || format.toLowerCase().contains("pades")) { //$NON-NLS-1$ //$NON-NLS-2$
			normalizeFormat = AOSignConstants.SIGN_FORMAT_PADES;
		} else if (format.equalsIgnoreCase("cades")) { //$NON-NLS-1$
			normalizeFormat = AOSignConstants.SIGN_FORMAT_CADES;
		} else if (format.toLowerCase().contains("xades")) { //$NON-NLS-1$
			normalizeFormat = AOSignConstants.SIGN_FORMAT_XADES;
		}
		return normalizeFormat;
	}

	/**
	 * Normalizamos el nombre del tipo de operaci&oacute;n criptogr&aacute;fica..
	 * @param operationType Tipo de operaci&oacute;n.
	 * @return Nombre del tipo de operaci&oacute;n normalizado o el mismo de entrada
	 * si no se ha encontrado correspondencia.
	 */
	private static String normalizeOperationType(final String operationType) {
		String normalizedOp = operationType;
		if ("firmar".equalsIgnoreCase(normalizedOp)) { //$NON-NLS-1$
			normalizedOp = CRYPTO_OPERATION_TYPE_SIGN;
		} else if ("cofirmar".equalsIgnoreCase(normalizedOp)) { //$NON-NLS-1$
			normalizedOp = CRYPTO_OPERATION_TYPE_COSIGN;
		} else if ("contrafirmar".equalsIgnoreCase(normalizedOp)) { //$NON-NLS-1$
			normalizedOp = CRYPTO_OPERATION_TYPE_COUNTERSIGN;
		}

		return normalizedOp;
	}


	/** Obtiene una sesi&oacute;n de firma trif&aacute;sica a partir de un XML que lo describe.
	 * Un ejemplo de XML podr&iacute;a ser el siguiente:
	 * <pre>
	 * &lt;xml&gt;
	 *  &lt;firmas&gt;
	 *   &lt;firma Id=\"001\"&gt;
	 *    &lt;param n="NEED_PRE"&gt;true&lt;/param&gt;
	 *    &lt;param n="PRE"&gt;MYICXDAYBgkqhkiG9[...]w0BA=&lt;/param&gt;
	 *    &lt;param n="NEED_DATA"&gt;true&lt;/param&gt;
	 *    &lt;param n="PK1"&gt;EMijB9pJ0lj27Xqov[...]RnCM=&lt;/param&gt;
	 *   &lt;/firma&gt;
	 *  &lt;/firmas&gt;
	 * &lt;/xml&gt;
	 * </pre>
	 * @param triphaseResponse Texto XML con la informaci&oacute;n del mensaje.
	 * @return Listado con el resultado de datos de la prefirma de cada uno de los documentos.
	 * @throws IOException Cuando hay problemas en el tratamiento de datos. */
	private static TriphaseData loadTriphaseResponse(final byte[] triphaseResponse) throws IOException {
		if (triphaseResponse == null) {
			throw new IllegalArgumentException("El XML de entrada no puede ser nulo"); //$NON-NLS-1$
		}

		Document doc;
		try (InputStream is = new ByteArrayInputStream(Base64.decode(triphaseResponse, 0, triphaseResponse.length, true))) {
			try {
				doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			}
			catch (final Exception e) {
				throw new IOException("Error al cargar la respuesta XML", e); //$NON-NLS-1$
			}
		}

		final Element rootElement = doc.getDocumentElement();
		final NodeList childNodes = rootElement.getChildNodes();

		final int idx = nextNodeElementIndex(childNodes, 0);
		if (idx == -1 || !"firmas".equalsIgnoreCase(childNodes.item(idx).getNodeName())) { //$NON-NLS-1$
			throw new IllegalArgumentException("No se encontro el nodo 'firmas' en el XML proporcionado"); //$NON-NLS-1$
		}

		return parseSignsNode(childNodes.item(idx));
	}

	/** Analiza el nodo con el listado de firmas.
	 * @param signsNode Nodo con el listado de firmas.
	 * @return Listado con la informaci&oacute;n de cada operaci&oacute;n de firma. */
	private static TriphaseData parseSignsNode(final Node signsNode) {

		final NodeList childNodes = signsNode.getChildNodes();

		final List<TriSign> signs = new ArrayList<>();
		int idx = nextNodeElementIndex(childNodes, 0);
		while (idx != -1) {
			final Node currentNode = childNodes.item(idx);

			String id = null;

			final NamedNodeMap nnm = currentNode.getAttributes();
			if (nnm != null) {
				final Node tmpNode = nnm.getNamedItem("Id"); //$NON-NLS-1$
				if (tmpNode != null) {
					id = tmpNode.getNodeValue();
				}
			}
			signs.add(
				new TriSign(
					parseParamsListNode(currentNode),
					id
				)
			);
			idx = nextNodeElementIndex(childNodes, idx + 1);
		}

		return new TriphaseData(signs);
	}

	/** Obtiene una lista de par&aacute;metros del XML.
	 * @param paramsNode Nodo con la lista de par&aacute;metros.
	 * @return Mapa con los par&aacute;metro encontrados y sus valores. */
	private static Map<String, String> parseParamsListNode(final Node paramsNode) {

		final NodeList childNodes = paramsNode.getChildNodes();

		final Map<String, String> params = new HashMap<>();
		int idx = nextNodeElementIndex(childNodes, 0);
		while (idx != -1) {
			final Node paramNode = childNodes.item(idx);
			final String key = paramNode.getAttributes().getNamedItem("n").getNodeValue(); //$NON-NLS-1$
			final String value = paramNode.getTextContent().trim();
			params.put(key, value);

			idx = nextNodeElementIndex(childNodes, idx + 1);
		}

		return params;
	}

	/** Recupera el &iacute;ndice del siguiente nodo de la lista de tipo <code>Element</code>.
	 * Empieza a comprobar los nodos a partir del &iacute;ndice marcado. Si no encuentra un
	 * nodo de tipo <i>elemento</i> devuelve -1.
	 * @param nodes Listado de nodos.
	 * @param currentIndex &Iacute;ndice del listado a partir del cual se empieza la comprobaci&oacute;n.
	 * @return &Iacute;ndice del siguiente node de tipo Element o -1 si no se encontr&oacute;. */
	private static int nextNodeElementIndex(final NodeList nodes, final int currentIndex) {
		Node node;
		int i = currentIndex;
		while (i < nodes.getLength()) {
			node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				return i;
			}
			i++;
		}
		return -1;
	}


}
