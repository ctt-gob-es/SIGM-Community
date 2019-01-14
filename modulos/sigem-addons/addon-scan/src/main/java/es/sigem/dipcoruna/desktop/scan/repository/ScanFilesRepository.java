package es.sigem.dipcoruna.desktop.scan.repository;

import java.io.File;
import java.util.List;

public interface ScanFilesRepository {
	void moveFile(File file);

	void deleteFile(File file);

	List<File> getFiles();

	void deleteAllFiles();

    void deleteFiles(List<String> ficherosSubidos);

	void renombraFile(final File file, String nombreFichero);
}
