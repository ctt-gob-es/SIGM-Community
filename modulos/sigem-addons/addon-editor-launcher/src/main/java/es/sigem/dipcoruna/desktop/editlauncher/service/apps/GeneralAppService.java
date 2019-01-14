package es.sigem.dipcoruna.desktop.editlauncher.service.apps;

import es.sigem.dipcoruna.desktop.editlauncher.model.apps.ProcessWrapper;

public interface GeneralAppService {
	String getPathAplicacionAsociadaConArchivosDeExtension(String extension);
	
	boolean existeLaAplicacion(String path);
	
	ProcessWrapper lanzarAplicacion(String path, String[] argumentos);
}
