package es.sigem.dipcoruna.desktop.scan.service;

import java.io.File;
import java.util.List;

public interface PdfService {
    File joinPdfs(List<File> files);
}
