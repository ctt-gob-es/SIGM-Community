package es.sigem.dipcoruna.desktop.editlauncher.service;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import es.sigem.dipcoruna.desktop.editlauncher.events.model.ErrorGeneralEvent;

@Service("localFileService")
public class LocalFileServiceImpl implements LocalFileService {    
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileServiceImpl.class);    
    
    @Value("${user.home}/.sigemEdit/arquivos/")
    private String directorioBaseDescargas;
    
    @Autowired
    private RestTemplate restTemplate;       
        
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    
    @Override
    public String descargarDocumento(String urlDocumento) {
        final UriComponents uriComponents = UriComponentsBuilder.fromUriString(urlDocumento).build();                
                
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        final String ficheroDescarga = getLocalFileDestinationPath(uriComponents.getPathSegments());
        
        ResponseEntity<byte[]> responseEntity = ejecutarGET(uriComponents.toUri().toString(), requestEntity);
                              
        guardarFicheroADisco(responseEntity, ficheroDescarga);
                      
        return ficheroDescarga;
    }
       

    private ResponseEntity<byte[]> ejecutarGET(String url, HttpEntity<String> requestEntity) {        
        ResponseEntity<byte[]> responseEntity = null;        
        try {
           responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, byte[].class, "1");                     
        }
        catch(Exception e) {
            LOGGER.error("Error al recuperar el fichero de la URL {}", url,  e);
            lanzarEventoErrorDescarga();            
        }
              
        
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            LOGGER.error("Error al recuperar el fichero de la URL {}. El servidor responde error {}", url, responseEntity.getStatusCode());
            lanzarEventoErrorDescarga();            
        }
                    
        return responseEntity;
    }
    
  
      
    
    private void guardarFicheroADisco(final ResponseEntity<byte[]> responseEntity, final String pathFichero) {
        try {
            FileOutputStream output = new FileOutputStream(pathFichero);
            IOUtils.write(responseEntity.getBody(), output);
            output.close();
        }
        catch(Exception e) {
            LOGGER.error("Error al recuperar al descargar fichero a disco {}", pathFichero, e);
            applicationEventPublisher.publishEvent(ErrorGeneralEvent.buildErrorIrrecuperable(this, 
                    "appLauncherApplet.error.error.title", 
                    "appLauncherApplet.error.guardarFichero",
                    new String[]{}));
            throw new RuntimeException("Error al guardar fichero");
        }
    }
    
    
    
    @Override
    public void actualizarDocumentoRemoto(final String urlDocumento, final String pathDocumento) {
        LOGGER.info("Se va a enviar el documento {} a la URL {}", pathDocumento, urlDocumento);
        
        final Path uploadingFile = Paths.get(pathDocumento + ".uploading");
        
        try {
			Files.copy(Paths.get(pathDocumento), uploadingFile, REPLACE_EXISTING);
		        
			subirDocumento(urlDocumento, uploadingFile.toString());        		
        } catch (IOException e) {
			LOGGER.error("Error al copiar el fichero original para subirlo. Original '{}', Copia de subida: '{}'", e, uploadingFile);
			lanzarEventoErrorSubida();
		}
        finally {
        	limpiarDocumento(uploadingFile.toString());        	
        }
    }


	private void subirDocumento(final String urlDocumento,	final String pathDocumento) {
		URI targetUrl = UriComponentsBuilder.fromUriString(urlDocumento).build().toUri();
                
		final FileSystemResource fileSystemResource = new FileSystemResource(pathDocumento);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<FileSystemResource> request = new HttpEntity<FileSystemResource>(fileSystemResource, headers);
        
        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(targetUrl.toString(), HttpMethod.PUT, request, byte[].class, "1");
          
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {                             
                LOGGER.error("Error al enviar fichero. Error {}", responseEntity.getStatusCode());            
                lanzarEventoErrorSubida();
            }
            LOGGER.debug("Fichero enviado correctamente {}", pathDocumento);
        }
        catch(Exception e) {
            LOGGER.error("Error al enviar el fichero al servidor {}", urlDocumento, e);
            lanzarEventoErrorSubida();
        }
        finally {
        	cerrarResource(fileSystemResource);
        }              
	}

	private void cerrarResource(final FileSystemResource fileSystemResource) {
		try {
			if (fileSystemResource.getInputStream() != null) {
				fileSystemResource.getInputStream().close();		
			}
		}
		catch (Exception e) {
			LOGGER.error("Error al cerrar el inputstream del fichero que se va a subir", e);
		}
		System.gc();
	}
    

    
    @Override
    public void limpiarDocumento(final String pathDocumento) {      
       boolean borrado = FileUtils.deleteQuietly(new File(pathDocumento));
       if (!borrado) {
    	   LOGGER.error("El fichero no se ha borrado correctamente {}", pathDocumento);
       }     
    }
    
        
    private String getLocalFileDestinationPath(List<String> pathSegments) {
        String nombreFicheroRemoto = pathSegments.get(pathSegments.size() - 1); 
        //Se genera un nombre aleatorio para evitar colisiones con otros archivos descargados con ese mismo nombre y todavía abiertos
        return generateNewDirectory() + File.separator + new Random().nextInt(1000000) + "." + FilenameUtils.getExtension(nombreFicheroRemoto);
    }
    
    
    private String generateNewDirectory() {                   
        String directorio = directorioBaseDescargas + File.separator +  new SimpleDateFormat("yyyMMdd").format(new Date());            
        if (! new File(directorio).exists()) {                
            new File(directorio).mkdirs();
        }

        return directorio;            
    } 
    
        
    
    private void lanzarEventoErrorDescarga() {
        applicationEventPublisher.publishEvent(ErrorGeneralEvent.buildErrorIrrecuperable(this, 
                "appLauncherApplet.error.error.title", 
                "appLauncherApplet.error.descargaFichero",
                new String[]{}));
        throw new RuntimeException("Error al descargar documento");
    }
    
    private void lanzarEventoErrorSubida() {
        applicationEventPublisher.publishEvent(ErrorGeneralEvent.buildErrorRecuperable(this, 
                "appLauncherApplet.error.error.title", 
                "appLauncherApplet.error.subirFichero", 
                new String[] {}));
        throw new RuntimeException("Error al subir documento");
    }
}
