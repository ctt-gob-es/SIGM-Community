package es.sigem.dipcoruna.desktop.scan.service.upload.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.sigem.dipcoruna.desktop.scan.exceptions.UploadFilesException;
import es.sigem.dipcoruna.desktop.scan.model.upload.UploadFilesResult;
import es.sigem.dipcoruna.desktop.scan.service.upload.UploadService;
import es.sigem.dipcoruna.desktop.scan.util.UploadFilesServerResponseParser;

@Service("uploadServiceTramitador")
public class UploadServiceTramitadorImpl extends AbstractBaseUploadService implements UploadService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceTramitadorImpl.class);

	@Value("${param.tramitador.documentTypeId}")
	private String documentTypeId;



	@Override
    protected UploadFilesResult validarRespuestaParticular(final HttpResponse response) {

        String responseString = null;
        try {
            responseString = new BasicResponseHandler().handleResponse(response);
            LOGGER.debug("Respuesta recibida del servidor: '{}'", responseString);
        }
        catch (final IOException e) {
            LOGGER.error("Error al recuperar el contenido de la respuesta {}", response, e);
        }


        if (responseString.contains("<html>")) {
            LOGGER.error("El servidor responde un HTML cuando se espera un texto plano");
            throw new UploadFilesException("Respuesta de servidor incorrecta");
        }


        final UploadFilesServerResponseParser parser = new UploadFilesServerResponseParser(responseString);
        final UploadFilesResult result = new UploadFilesResult();
        result.setFicherosOK(parser.getOks());
        result.setFicherosError(parser.getErrors());
        return result;
    }


	@Override
    protected MultipartEntityBuilder createMultipartEntityBuilder(final List<File> files) {
		final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		for (final File file : files) {
			final FileBody fileBody = new FileBody(file);
			final StringBody fileNameBody = new StringBody(file.getName(), ContentType.MULTIPART_FORM_DATA);

			builder.addPart(file.getName(), fileBody);
			builder.addPart("fileNames", fileNameBody);
		}

		final StringBody documentTyepIdBody = new StringBody(documentTypeId, ContentType.MULTIPART_FORM_DATA);
		builder.addPart("documentTypeId", documentTyepIdBody);

		return builder;
	}
}
