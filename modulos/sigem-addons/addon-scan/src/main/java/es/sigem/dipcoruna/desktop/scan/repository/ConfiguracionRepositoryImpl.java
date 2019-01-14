package es.sigem.dipcoruna.desktop.scan.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;
import es.sigem.dipcoruna.desktop.scan.repository.transform.ConfiguracionTransform;
import es.sigem.dipcoruna.scan.configuracion.dto.ConfiguracionDto;
import es.sigem.dipcoruna.scan.configuracion.dto.DispositivoDto;
import es.sigem.dipcoruna.scan.configuracion.dto.ScanProfileDto;

@Repository("configuracionRepository")
public class ConfiguracionRepositoryImpl implements ConfiguracionRepository, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfiguracionRepositoryImpl.class);

    @Value("${user.home}/.sigem-scan-config.xml")
    private String rutaFicheroConfiguracion;

    private File configFile;

    @Override
    public void afterPropertiesSet() throws Exception {
        configFile = new File(rutaFicheroConfiguracion);
        if (!configFile.exists() || configFile.length() == 0) {
            LOGGER.warn("No existía el fichero de configuración '{}' o estaba vacío. Se crea uno");
            configFile.createNewFile();
            guardarConfiguracion(new ConfiguracionDto());
        }
    }


    @Override
    public List<ScanProfile> getAllScanProfiles(final String nombreDispositivo) {
        final ConfiguracionDto configDto = leerConfiguracion();
        final List<ScanProfileDto> scanProfilesDtos = getScanProfilesDtoDeDispositivo(nombreDispositivo, configDto);
        return ConfiguracionTransform.scanProfilesDtoToModelos(scanProfilesDtos, nombreDispositivo);
    }



    @Override
    public boolean existsScanProfile(final String nombre, final String nombreDispositivo) {
        try {
            getScanProfile(nombre, nombreDispositivo);
            return true;
        }
        catch(final Exception e) {
            return false;
        }
    }

    @Override
    public ScanProfile getScanProfile(final String nombre, final String nombreDispositivo) {
        final ConfiguracionDto configDto = leerConfiguracion();
        final ScanProfileDto scanProfileDto = getScanProfileByNameFromList(nombre, nombreDispositivo, configDto);
        return ConfiguracionTransform.scanProfileDtoToModelo(scanProfileDto, nombreDispositivo);
    }


    @Override
    public void insertScanProfile(final ScanProfile scanProfile) {
        final ConfiguracionDto configDto = leerConfiguracion();
        crearDispositivoIfNecesary(configDto, scanProfile.getNombreDispositivo());

        final ScanProfileDto scanProfileDtoNuevo = ConfiguracionTransform.scanProfileToDto(scanProfile);

        final List<ScanProfileDto> scanProfilesDtoExistentes = getScanProfilesDtoDeDispositivo(scanProfile.getNombreDispositivo(), configDto);

        final int posScanProfile = getPosScanPfrofileDto(scanProfileDtoNuevo.getNombre(), scanProfilesDtoExistentes);
        if (posScanProfile != -1) {
            throw new IllegalArgumentException("Profile repetido: " + scanProfile.getNombre());
        }

        scanProfilesDtoExistentes.add(scanProfileDtoNuevo);
        guardarConfiguracion(configDto);
    }


    @Override
    public void updateScanProfile(final ScanProfile scanProfile) {
        final ConfiguracionDto configDto = leerConfiguracion();

        final ScanProfileDto scanProfileDtoActualizar = ConfiguracionTransform.scanProfileToDto(scanProfile);
        final List<ScanProfileDto> scanProfilesDtoExistentes = getScanProfilesDtoDeDispositivo(scanProfile.getNombreDispositivo(), configDto);

        final int posScanProfile = getPosScanPfrofileDto(scanProfileDtoActualizar.getNombre(), scanProfilesDtoExistentes);
        if (posScanProfile == -1) {
            throw new IllegalArgumentException("Profile no existe: " + scanProfile.getNombre());
        }

        scanProfilesDtoExistentes.set(posScanProfile, scanProfileDtoActualizar);
        guardarConfiguracion(configDto);
    }


    @Override
    public void deleteScanProfile(final String nombre, final String nombreDispositivo) {
        final ConfiguracionDto configDto = leerConfiguracion();
        final List<ScanProfileDto> scanProfilesDtoExistentes = getScanProfilesDtoDeDispositivo(nombreDispositivo, configDto);

        final int posScanProfile = getPosScanPfrofileDto(nombre, scanProfilesDtoExistentes);
        if (posScanProfile == -1) {
            throw new IllegalArgumentException("Profile no existe: " + nombre);
        }

        scanProfilesDtoExistentes.remove(posScanProfile);
        guardarConfiguracion(configDto);
    }


    private void crearDispositivoIfNecesary(final ConfiguracionDto configuracionDto, final String nombreDispositivo) {
        for (final DispositivoDto dispositivoDto : configuracionDto.getDispositivoDto()) {
            if (dispositivoDto.getNombre().equals(nombreDispositivo)) {
                return;
            }
        }

        final DispositivoDto dispositivoDto = new DispositivoDto();
        dispositivoDto.setNombre(nombreDispositivo);
        configuracionDto.getDispositivoDto().add(dispositivoDto);
    }


    private int getPosScanPfrofileDto(final String nombre, final List<ScanProfileDto> scanProfilesDtoExistentes) {
        for (int i=0; i<scanProfilesDtoExistentes.size(); i++) {
            if (scanProfilesDtoExistentes.get(i).getNombre().equals(nombre)){
                return i;
            }
        }
        return -1;
    }



    private ScanProfileDto getScanProfileByNameFromList(final String nombre, final String nombreDispositivo, final ConfiguracionDto configuracionDto) {
        for (final ScanProfileDto scanProfile : getScanProfilesDtoDeDispositivo(nombreDispositivo, configuracionDto)) {
            if (scanProfile.getNombre().equals(nombre)) {
                return scanProfile;
            }
        }
        throw new IllegalArgumentException("Profile no encontrado: " + nombre);
    }



    private List<ScanProfileDto> getScanProfilesDtoDeDispositivo(final String nombreDispositivo, final ConfiguracionDto configuracionDto) {
        for (final DispositivoDto dispositivoDto : configuracionDto.getDispositivoDto()) {
            if (dispositivoDto.getNombre().equals(nombreDispositivo)) {
                return dispositivoDto.getScanProfileDto();
            }
        }
        return new ArrayList<ScanProfileDto>();
    }

    // ********************************* //

    private ConfiguracionDto leerConfiguracion() {
        try {
            final JAXBContext context = JAXBContext.newInstance(ConfiguracionDto.class);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ConfiguracionDto) unmarshaller.unmarshal(new FileReader(configFile));
        }
        catch (final Exception e) {
            LOGGER.error("Error al leer el fichero de configuración {}", configFile, e);
            throw new RuntimeException("Error al leer el fichero de configuración " + configFile, e);
        }
    }

    private void guardarConfiguracion(final ConfiguracionDto configuracionDto){
        try {
            final JAXBContext context = JAXBContext.newInstance(ConfiguracionDto.class);
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(configuracionDto, new FileOutputStream(configFile));
        }
        catch(final Exception e) {
            LOGGER.error("Error al escribir el fichero de configuración {} con la información {}", configFile, configuracionDto, e);
            throw new RuntimeException("Error al escribir el fichero de configuración " + configFile, e);
        }
    }
}
