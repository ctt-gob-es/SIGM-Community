package es.sigem.dipcoruna.desktop.scan.service.upload.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
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
import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.desktop.scan.exceptions.UploadFilesException;
import es.sigem.dipcoruna.desktop.scan.model.upload.UploadFilesResult;
import es.sigem.dipcoruna.desktop.scan.service.upload.UploadService;

@Service("uploadServiceRegistro")
public class UploadServiceRegistroImpl extends AbstractBaseUploadService implements UploadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceRegistroImpl.class);

    @Value("${param.registro.folderId}")
    private String folderId;

    @Value("${param.registro.sessionPId}")
    private String sessionPId;
    
    @Value("${param.registro.bookId}")
    private String bookId;
    
    @Value("${param.registro.entidadId}")
    private String entidadId;
    
    @Value("${param.registro.nombreCarpeta}")
    private String nombreCarpeta;
       

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


        if (StringUtils.hasLength(responseString)) {
            LOGGER.error("El servidor responde un mensaje de error");
            throw new UploadFilesException("El servidor responde un mensaje de error");
        }

        final UploadFilesResult result = new UploadFilesResult();
        result.setFicherosOK(Collections.<String>emptyList());
        result.setFicherosError(Collections.<String>emptyList());
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

        final StringBody documentTyepIdBody = new StringBody(sessionPId, ContentType.MULTIPART_FORM_DATA);
        builder.addPart("SessionPId", documentTyepIdBody);

        final StringBody folderIdIdBody = new StringBody(folderId, ContentType.MULTIPART_FORM_DATA);
        builder.addPart("FolderId", folderIdIdBody);
        
        final StringBody bookIdBody = new StringBody(bookId, ContentType.MULTIPART_FORM_DATA);
        builder.addPart("BookId", bookIdBody);
        
        final StringBody entidadIdBody = new StringBody(entidadId, ContentType.MULTIPART_FORM_DATA);
        builder.addPart("EntidadId", entidadIdBody);
        
        final StringBody nombreCarpetaBody = new StringBody(nombreCarpeta, ContentType.MULTIPART_FORM_DATA);
        builder.addPart("NombreCarpeta", nombreCarpetaBody);

        return builder;
    }
}
