package es.sigem.dipcoruna.desktop.scan.service.upload;

import java.io.File;
import java.util.List;

import es.sigem.dipcoruna.desktop.scan.model.upload.UploadFilesResult;

public interface UploadService {

    UploadFilesResult subirFicheros(List<File> files);

}
