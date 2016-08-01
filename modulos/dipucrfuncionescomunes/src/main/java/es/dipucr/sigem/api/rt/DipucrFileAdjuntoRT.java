package es.dipucr.sigem.api.rt;

import java.io.File;

public class DipucrFileAdjuntoRT {

	protected String nombre;
	protected String codAdjunto;
	protected String extensionAdjunto;
	protected File file;
	
	public DipucrFileAdjuntoRT() {
		super();
	}

	public DipucrFileAdjuntoRT(String nombre, String codAdjunto, String extensionAdjunto, File file) {
		
		this.nombre = nombre;
		this.codAdjunto = codAdjunto;
		this.extensionAdjunto = extensionAdjunto;
		this.file = file;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodAdjunto() {
		return codAdjunto;
	}

	public void setCodAdjunto(String codAdjunto) {
		this.codAdjunto = codAdjunto;
	}

	public String getExtensionAdjunto() {
		return extensionAdjunto;
	}

	public void setExtensionAdjunto(String extensionAdjunto) {
		this.extensionAdjunto = extensionAdjunto;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
}
