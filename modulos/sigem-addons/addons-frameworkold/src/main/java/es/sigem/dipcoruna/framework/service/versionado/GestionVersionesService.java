package es.sigem.dipcoruna.framework.service.versionado;

public interface GestionVersionesService {
	/**
	 * Comprueba si es la última versión e informa al usuario en caso contrario
	 * @param version
	 * @param codigoAplicacion
	 */
	void comprobarSiUltimaVersion(String version, String codigoAplicacion);
	
	/**
	 * 
	 * @param version
	 * @param codigoAplicacion
	 * @param urlBaseDescargas
	 */
	void comprobarSiUltimaVersion(String version, String codigoAplicacion, String urlBaseDescargas);
}
