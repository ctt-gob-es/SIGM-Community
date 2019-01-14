package es.sigem.dipcoruna.desktop.scan.repository.transform;

import java.util.ArrayList;
import java.util.List;

import es.sigem.dipcoruna.desktop.scan.model.config.FormatoColor;
import es.sigem.dipcoruna.desktop.scan.model.config.FormatoFichero;
import es.sigem.dipcoruna.desktop.scan.model.config.Resolucion;
import es.sigem.dipcoruna.desktop.scan.model.config.ScanProfile;
import es.sigem.dipcoruna.scan.configuracion.dto.ScanProfileDto;

public class ConfiguracionTransform {
    private ConfiguracionTransform() {}

    public static List<ScanProfile> scanProfilesDtoToModelos(final List<ScanProfileDto> dtos, final String nombreDispositivo) {
        final List<ScanProfile> profiles = new ArrayList<ScanProfile>();
        for (final ScanProfileDto scanProfileDto : dtos) {
            profiles.add(scanProfileDtoToModelo(scanProfileDto, nombreDispositivo));
        }
        return profiles;
    }


    public static ScanProfile scanProfileDtoToModelo(final ScanProfileDto dto, final String nombreDispositivo) {
        final ScanProfile scanProfile = new ScanProfile();
        scanProfile.setNombreDispositivo(nombreDispositivo);
        scanProfile.setNombre(dto.getNombre());
        scanProfile.setFormatoFichero(FormatoFichero.valueOf(dto.getFormatoFichero()));
        scanProfile.setFormatoColor(FormatoColor.valueOf(dto.getFormatoColor()));
        scanProfile.setResolucion(Resolucion.valueOf(dto.getResolucion()));
        scanProfile.setDuplex(dto.isDuplex());
        scanProfile.setCargaAutomatica(dto.isCargaAutomatica());
        return scanProfile;
    }


    public static ScanProfileDto scanProfileToDto (final ScanProfile scanProfile) {
        final ScanProfileDto dto = new ScanProfileDto();
        dto.setNombre(scanProfile.getNombre());
        dto.setFormatoFichero(scanProfile.getFormatoFichero().name());
        dto.setFormatoColor(scanProfile.getFormatoColor().name());
        dto.setResolucion(scanProfile.getResolucion().name());
        dto.setDuplex(scanProfile.isDuplex());
        dto.setCargaAutomatica(scanProfile.isCargaAutomatica());
        return dto;
    }
}
