package es.sigem.dipcoruna.desktop.scan.service.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.sigem.dipcoruna.desktop.scan.model.upload.DestinoUpload;

@Service
public class UploadServiceFactoryImpl implements UploadServiceFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceFactoryImpl.class);

    @Value("${param.destinoUpload}")
    private String destinoUpload;

    @Autowired
    @Qualifier("uploadServiceTramitador")
    private UploadService uploadServiceTramitador;

    @Autowired
    @Qualifier("uploadServiceRegistro")
    private UploadService uploadServiceRegistro;



    @Override
    public UploadService getUpladService() {
       if (DestinoUpload.TRAMITADOR.name().equals(destinoUpload)) {
           LOGGER.info("Devuelto servicio de upload al tramitador");
           return uploadServiceTramitador;
       }

       if (DestinoUpload.REGISTRO.name().equals(destinoUpload)) {
           LOGGER.info("Devuelto servicio de upload al registro");
           return uploadServiceRegistro;
       }

       LOGGER.error("No se ha encontrado un servicio de upload para el destino '{}' recibido por parámetros", destinoUpload);
       throw new IllegalArgumentException("No se ha encontrado un servicio de upload para el destino " + destinoUpload);
    }
}
