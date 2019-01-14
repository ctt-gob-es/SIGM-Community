package es.sigem.dipcoruna.desktop.scan.model.upload;

import java.util.List;

/**
 * Si no hay ficheros de Error se considera que se subieron todos los ficheros posibles.
 *
 * @author Diputacion
 *
 */
public class UploadFilesResult {
    public List<String> ficherosOK;
    public List<String> ficherosError;

    public List<String> getFicherosOK() {
        return ficherosOK;
    }

    public void setFicherosOK(final List<String> ficherosOK) {
        this.ficherosOK = ficherosOK;
    }

    public List<String> getFicherosError() {
        return ficherosError;
    }

    public void setFicherosError(final List<String> ficherosError) {
        this.ficherosError = ficherosError;
    }
}
