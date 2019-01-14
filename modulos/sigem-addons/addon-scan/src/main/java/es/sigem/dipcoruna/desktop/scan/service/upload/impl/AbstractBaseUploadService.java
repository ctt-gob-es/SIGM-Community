package es.sigem.dipcoruna.desktop.scan.service.upload.impl;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.desktop.scan.exceptions.UploadFilesException;
import es.sigem.dipcoruna.desktop.scan.model.upload.UploadFilesResult;
import es.sigem.dipcoruna.desktop.scan.service.upload.UploadService;


public abstract class AbstractBaseUploadService implements UploadService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBaseUploadService.class);

	private static final int SOCKET_TIMEOUT = 600000;
	private static final int CONNECTION_TIMEOUT = 30000;

	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;

	@Value("${uploadFiles.truststore.file}")
	private String trustStoreFile;

	@Value("${uploadFiles.trustostre.pass}")
	private String trustSTorePass;

	@Value("${param.cookies}")
	private String cookies;

	@Value("${param.cookiesPath}")
    private String cookiesPath;

	@Value("${param.cookiesDomain}")
    private String cookiesDomain;

	@Value("${param.urlUploadAction}")
	private String urlUploadAction;

	@Value("${param.sessionId}")
	private String sessionId;
	
	@Value("${param.uploadToken}")
	private String uploadToken;


	@Override
	public UploadFilesResult subirFicheros(final List<File> files) {
	    try {
	        final HttpResponse response = invocarAccion(files);
	        return validarRespuesta(response);
	    }
	    catch (final UploadFilesException e) {
	        LOGGER.error("Error controlado al subir ficheros al servidor", e);
	        throw e;
	    }
	    catch(final Exception e) {
	        LOGGER.error("Error incontrolado al subir ficheros al servidor", e);
	        throw new UploadFilesException("Error incontrolado al subir ficheros", e);
	    }
	}


    private HttpResponse invocarAccion(final List<File> files) throws ClientProtocolException, IOException {
	    final URI actionUri = componerUploadActionUri();
	    LOGGER.info("Url de subida de documentos: {}", actionUri);

        final SSLConnectionSocketFactory sslsf = buildSSLConnectionSocketFactory();
        final RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();

        final HttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpClients.custom().setDefaultRequestConfig(requestConfig);


        final HttpEntity entity = buildRequestEntity(files);

        final HttpPost post = new HttpPost(actionUri);
        post.setEntity(entity);
        post.setHeader("connection", HTTP.CONN_KEEP_ALIVE);

        final HttpClientContext context = buildHttpClientContext();

        return httpClient.execute(post, context);
	}


    private URI componerUploadActionUri() {
		try {
			final URIBuilder uriBuilder = new URIBuilder(urlUploadAction);
			uriBuilder.setParameter("http.protocol.expect-continue", "true");
			return uriBuilder.build();
		}
		catch(final URISyntaxException e) {
			LOGGER.error("Error al crear la URI de la acción de subida de documentos {}", urlUploadAction, e);
			throw new RuntimeException(e);
		}
	}

	private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
		try {
			final Resource trustoreResource = new ClassPathResource(trustStoreFile);
			final KeyStore trustore = KeyStore.getInstance("JKS");
			trustore.load(trustoreResource.getInputStream(), trustSTorePass.toCharArray());

			final TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustFactory.init(trustore);

			final SSLContext sslcontext = SSLContext.getInstance("SSLv3");
			sslcontext.init(null, trustFactory.getTrustManagers(), null);

			return new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
		} catch (final Exception e) {
		    LOGGER.error("Error al preparar el acceso SSL usando el trustostore '{}'", trustStoreFile);
			throw new RuntimeException(e);
		}
	}


	private HttpClientContext buildHttpClientContext() {
		final CookieStore cookieStore = buildCookieStore();

		final HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		return context;
	}

	private CookieStore buildCookieStore() {
	    final CookieStore cookieStore = new BasicCookieStore();

	    if (StringUtils.hasLength(cookies)) {
    	    for (final String cookie : cookies.split(";")) {
    	        final String cookieName = cookie.substring(0, cookie.indexOf("="));
    	        final String cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());

    	        final BasicClientCookie basicClientCookie = new BasicClientCookie(cookieName, cookieValue);
    	        basicClientCookie.setDomain(cookiesDomain);
    	        basicClientCookie.setPath(cookiesPath);
    	        cookieStore.addCookie(basicClientCookie);
            }
	    }


	    final BasicClientCookie sessionClientCookie = new BasicClientCookie("JSESSIONID", sessionId);
	    sessionClientCookie.setDomain(cookiesDomain);
	    sessionClientCookie.setPath(cookiesPath);
        cookieStore.addCookie(sessionClientCookie);

		return cookieStore;
	}

	
	private HttpEntity buildRequestEntity(List<File> files) {
        MultipartEntityBuilder entityBuilder = createMultipartEntityBuilder(files);
       
        if (StringUtils.hasText(uploadToken)) {
            final StringBody documentTyepUploadToken = new StringBody(uploadToken, ContentType.MULTIPART_FORM_DATA);
            entityBuilder.addPart("uploadToken", documentTyepUploadToken);
        }
        
        return entityBuilder.build();
   }




	private UploadFilesResult validarRespuesta(final HttpResponse response) throws HttpResponseException, IOException {
	    if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
	          LOGGER.error("Respuesta errónea al intentar conectar con el servidor {}: {}", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
	          throw new UploadFilesException("Error al conectar con el servidor", Arrays.asList(response.getStatusLine().getStatusCode() + ":" + response.getStatusLine().getReasonPhrase()));
	    }

	    return validarRespuestaParticular(response);
	}




	/**
	 * Crea el multipart que contiene los ficheros, y el resto de parámetros si los hubiere, para enviar los ficheros
	 * @param files
	 * @return
	 */
	protected abstract MultipartEntityBuilder createMultipartEntityBuilder(final List<File> files);


	/**
	 * Valida la respuesta recibida del servidor
	 * @param response
	 * @return
	 */
	protected abstract UploadFilesResult validarRespuestaParticular(final HttpResponse response);
}
