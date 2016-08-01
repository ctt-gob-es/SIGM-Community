package es.dipucr.tablonEdictalUnico.objetos;

import java.util.GregorianCalendar;
import java.util.Vector;

import es.dipucr.tablonEdictalUnico.xml.Notificados;

public class InformacionTablonEdictal {
	private String numexp;
	/**
	 * Fecha de publicación solicitada para los anuncios. Si la fecha se correspondiese con un domingo, 
	 * la publicación se realizará el lunes siguiente. Si no se incluye o es incorrecta se procederá a 
	 * publicar en la fecha más temprana posible conforme al procedimiento de cierre y publicación que rige la publicación del BOE. 
	 * La fecha se especificará en formato ISO 8601:2004 (aaaa-mm-dd).
	 * **/
	private GregorianCalendar fechaPublicacion;
	/**
	 * Dirección de correo electrónico a efectos de comunicar las incidencias que se generen en el proceso de la información.
	 * **/
	private String email;
	/**
	 * Forma de publicación. Es un dato obligatorio imprescindible para el tratamiento posterior y la forma de mostrar el anuncio. Puede tomar dos valores:
	 * E: Publicación en extracto (cuando el anuncio no contiene el contenido del acto administrativo a notificar, sino únicamente la identificación del interesado 
	 * y del procedimiento)
	 * I: Publicación íntegra (cuando en el texto del anuncio se recoge completo el contenido del acto administrativo objeto de notificación)
	 * **/
	private String formaPublicacion;
	/**
	 * Informa sobre si el anuncio contiene datos de carácter personal. Puede tomar los siguientes valores:
	 * N: No incluye ningún dato de carácter personal.
	 * S: Incluye datos de carácter personal.
	 * **/
	private String datosPersonales;
	/**
	 * Tipo de anuncio. Por ejemplo: “catastro”, “impuestos”, “tasas”, “subvenciones” con el objetivo de facilitar la recuperación 
	 * posterior en base de datos. Contendrá tantos elementos “materia” como sean precisos para facilitar la búsqueda del anuncio. Clasificación a determinar.
	 * **/
	private String tipoAnuncio;
	/**
	 * El valor será “S” si el anuncio debe publicarse conforme a lo dispuesto en el artículo 112 de la Ley 58/2003 (Ley General Tributaria).
	 * **/
	private String lgt;
	/**
	 * Identificación del procedimiento. Es un texto libre que permitirá construir de manera automatizada el título 
	 * del anuncio y diferenciar entre los emitidos en igual fecha por el mismo emisor
	 * **/
	private String procedimiento;
	/**
	 * Aunque el elemento es opcional, deberá incluirse aquí la lista con los datos de los notificados si no es posible marcarlos 
	 * en el elemento contenido/texto que se describe en el punto siguiente. Contendrá tantos elementos “notificado” como notificados haya.
	 * **/
	private Notificados notificados;
	private Vector <String> parrafos;
	private GregorianCalendar fechaFirma;
	private String lugar;
	private String cargoNombre;
	public String getNumexp() {
		return numexp;
	}
	public void setNumexp(String numexp) {
		this.numexp = numexp;
	}
	public GregorianCalendar getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(GregorianCalendar fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFormaPublicacion() {
		return formaPublicacion;
	}
	public void setFormaPublicacion(String formaPublicacion) {
		this.formaPublicacion = formaPublicacion;
	}
	public String getDatosPersonales() {
		return datosPersonales;
	}
	public void setDatosPersonales(String datosPersonales) {
		this.datosPersonales = datosPersonales;
	}
	public String getTipoAnuncio() {
		return tipoAnuncio;
	}
	public void setTipoAnuncio(String tipoAnuncio) {
		this.tipoAnuncio = tipoAnuncio;
	}
	public String getLgt() {
		return lgt;
	}
	public void setLgt(String lgt) {
		this.lgt = lgt;
	}
	public String getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}
	public Notificados getNotificados() {
		return notificados;
	}
	public void setNotificados(Notificados notificados) {
		this.notificados = notificados;
	}
	public Vector <String> getParrafos() {
		return parrafos;
	}
	public void setParrafos(Vector <String> parrafos) {
		this.parrafos = parrafos;
	}
	public GregorianCalendar getFechaFirma() {
		return fechaFirma;
	}
	public void setFechaFirma(GregorianCalendar fechaFirma) {
		this.fechaFirma = fechaFirma;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public String getCargoNombre() {
		return cargoNombre;
	}
	public void setCargoNombre(String cargoNombre) {
		this.cargoNombre = cargoNombre;
	}
}
