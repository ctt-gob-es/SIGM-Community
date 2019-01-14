package es.sigem.dipcoruna.desktop.scan;


public interface ScanApp {
	void checkVersion(String urlCheckForUpdates);
	
	void lanzarModoConfiguracion();
	
	void lanzarOpcionEscaneo();
	
	boolean isDescargarMasTarde(); 
}
