package es.sigem.dipcoruna.desktop.scan.repository;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository("scanFilesRepository")
public class ScanFilesRepositoryImpl implements ScanFilesRepository, InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScanFilesRepositoryImpl.class);


	@Value("${java.io.tmpdir}${file.separator}${param.destinoUpload}${file.separator}sigem-scan")
	private String directorioBase;

	@Override
	public void afterPropertiesSet() throws Exception {
	    LOGGER.info("Asignado directorio base de escaneado: {}", directorioBase);
		FileUtils.forceMkdir(getDirectorio());
		//FileUtils.cleanDirectory(getDirectorio());
	}

	private File getDirectorio() {
		return new File(directorioBase);
	}


	@Override
	public void moveFile(final File file) {
		if (! file.exists() || ! file.isFile()) {
			LOGGER.warn("No existe el fichero {}", file);
			throw new RuntimeException("No existe el fichero " + file);
		}

		
		File fileDestino = calcularFicheroDestino(file);		
		try {		    
			FileUtils.moveFile(file, fileDestino);
			LOGGER.debug("Se ha movido correctamente el fichero escaneado a {}", file);
		} catch (final IOException e) {
			LOGGER.error("Error al mover fichero a destino. Origen {}, Destino {}", file, fileDestino);
		}
	}


	@Override
	public void deleteFile(final File file) {
	    LOGGER.debug("Se va a borrar el fichero {}", file);
		if (! file.delete()) {
		    LOGGER.warn("Error al borrar el fichero {}", file);
		}
	}


	@Override
	public List<File> getFiles() {
		final File[] ficheros = getDirectorio().listFiles();
		if (ficheros == null || ficheros.length == 0) {
			return new ArrayList<File>();
		}
		Arrays.sort(ficheros, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
		return Arrays.asList(ficheros);
	}


    @Override
    public void deleteAllFiles() {
        try {
            FileUtils.cleanDirectory(getDirectorio());
        }
        catch (final IOException e) {
        }
    }

    @Override
    public void deleteFiles(final List<String> ficherosSubidos) {
        final List<File> ficheros = getFiles();
        for (final File file : ficheros) {
            if (ficherosSubidos.contains(file.getName())) {
                LOGGER.debug("Borrando el fichero {}", file);
                file.delete();
            }
        }
    }

    @Override
	public void renombraFile(final File file, String nombreFichero){
		if (! file.exists() || ! file.isFile()) {
			LOGGER.warn("No existe el fichero {}", file);
			throw new RuntimeException("No existe el fichero " + file);
		}

		final String nombre = nombreFichero + "." + FilenameUtils.getExtension(file.getName());

		File fileDestino = new File(getDirectorio(), nombre);		
		try {		    
			FileUtils.moveFile(file, fileDestino);
			LOGGER.debug("Se ha movido correctamente el fichero escaneado a {}", file);
		} catch (final IOException e) {
			LOGGER.error("Error al mover fichero a destino. Origen {}, Destino {}", file, fileDestino);
		}
	}

	private File calcularFicheroDestino(final File file) {
		final String fechaNombre = new SimpleDateFormat("yyyyMMddHHmmSS").format(new Date());
		final String nombre = fechaNombre + RandomStringUtils.randomAlphanumeric(5) + "." + FilenameUtils.getExtension(file.getName());
		return new File(getDirectorio(), nombre);
	}
}
