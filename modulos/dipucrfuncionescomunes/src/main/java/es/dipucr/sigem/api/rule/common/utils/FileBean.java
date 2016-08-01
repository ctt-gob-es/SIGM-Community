package es.dipucr.sigem.api.rule.common.utils;

import java.io.File;

/**
 * Clase manejadora de ficheros que lleva dos parámetros
 * - Nombre del fichero
 * - Fichero físico (file) - Ruta en el sistema de archivos temporal
 * @author Felipe
 * @since [dipucr-Felipe #1148] 30.09.14
 */
public class FileBean {

	protected String name;
	protected File file;
	
	public FileBean() {
		super();
	}
	
	public FileBean(String name, File file) {
		super();
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
}
