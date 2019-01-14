package es.sigem.dipcoruna.desktop.asyncUploader.model;

/**
 * Clase que representa un estado de una operacion de subida de un fichero
 */
public class State {

	private String filePath;

	private String entidad;
	
	private String folder;
	
	private String tramite;
	
	private String tipoDocumento;
	
	private String usuario;
	
	private String nombre;
	
	private String fase;
	
	private String tipoDestino;
	
	private String idDestino;
	
	private long transferidos;


	/**
	 * @return the filePath
	 */
	public final String getFilePath() {
		return filePath;
	}

	/**
	 * @param value the filePath to set
	 */
	public final void setFilePath(final String value) {
		this.filePath = value;
	}

	/**
	 * @return the entidad
	 */
	public final String getEntidad() {
		return entidad;
	}

	/**
	 * @param value the entidad to set
	 */
	public final void setEntidad(final String value) {
		this.entidad = value;
	}

	/**
	 * @return the tramite
	 */
	public final String getTramite() {
		return tramite;
	}

	/**
	 * @param value the tramite to set
	 */
	public final void setTramite(final String value) {
		this.tramite = value;
	}

	/**
	 * @return the tipoDocumento
	 */
	public final String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param value the tipoDocumento to set
	 */
	public final void setTipoDocumento(final String value) {
		this.tipoDocumento = value;
	}

	/**
	 * @return the nombre
	 */
	public final String getNombre() {
		return nombre;
	}

	/**
	 * @param value the nombre to set
	 */
	public final void setNombre(final String value) {
		this.nombre = value;
	}

	/**
	 * @return the transferidos
	 */
	public final long getTransferidos() {
		return transferidos;
	}

	/**
	 * @param value the transferidos to set
	 */
	public final void setTransferidos(final long value) {
		this.transferidos = value;
	}

	/**
	 * @return the usuario
	 */
	public final String getUsuario() {
		return usuario;
	}

	/**
	 * @param value the usuario to set
	 */
	public final void setUsuario(final String value) {
		this.usuario = value;
	}

	/**
	 * @return the fase
	 */
	public final String getFase() {
		return fase;
	}

	/**
	 * @param value the fase to set
	 */
	public final void setFase(final String value) {
		this.fase = value;
	}

	/**
	 * @return the tipoDestino
	 */
	public final String getTipoDestino() {
		return tipoDestino;
	}

	/**
	 * @param value the tipoDestino to set
	 */
	public final void setTipoDestino(final String value) {
		this.tipoDestino = value;
	}

	/**
	 * @return the idDestino
	 */
	public final String getIdDestino() {
		return idDestino;
	}

	/**
	 * @param value the idDestino to set
	 */
	public final void setIdDestino(final String value) {
		this.idDestino = value;
	}

	/**
	 * @return the folder
	 */
	public final String getFolder() {
		return folder;
	}

	/**
	 * @param value the folder to set
	 */
	public final void setFolder(final String value) {
		this.folder = value;
	}
	
}
