package ieci.tecdoc.sgm.core.services.certificacion;

/**
 * Clase que almacena los datos relativos a una entidad
 * @author José Antonio Nogales
 */
public class Entidad {
	String codigo;
	String descripcion;
	private String identificador;
	private String nombre;
	private String nombreCorto;
	private String nombreLargo;
	private String codigoINE;
	private String dir3;
	private String sia;
	private String deh;
	
	
	/**
	 * Constructor de la clase entidad
	 * @param codigo Código de la entidad 
	 * @param descripcion Descripción de la entidad
	 */
	public Entidad(String codigo, String descripcion) {
		this.codigo = codigo;
		this.descripcion = descripcion;
	}
	
	/**
	 * Constructor de la clase sin parámetros
	 */
	public Entidad() {
		this.codigo = null;
		this.descripcion = null;
	}
	
	/**
	 * Métodos get y set de la clase 
	 */
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public String getNombreLargo() {
		return nombreLargo;
	}

	public void setNombreLargo(String nombreLargo) {
		this.nombreLargo = nombreLargo;
	}

	public String getCodigoINE() {
		return codigoINE;
	}

	public void setCodigoINE(String codigoINE) {
		this.codigoINE = codigoINE;
	}

	public String getDir3() {
		return dir3;
	}

	public void setDir3(String dir3) {
		this.dir3 = dir3;
	}

	public String getSia() {
		return sia;
	}

	public void setSia(String sia) {
		this.sia = sia;
	}

	public String getDeh() {
		return deh;
	}

	public void setDeh(String deh) {
		this.deh = deh;
	}
	
}
