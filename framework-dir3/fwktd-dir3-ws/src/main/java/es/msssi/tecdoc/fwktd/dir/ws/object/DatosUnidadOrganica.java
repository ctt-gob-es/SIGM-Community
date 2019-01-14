package es.msssi.tecdoc.fwktd.dir.ws.object;

import java.io.Serializable;
import java.util.Date;

/**
 * Datos básicos de una unidad orgánica.
 *
 * @author cmorenog
 *
 */
public class DatosUnidadOrganica  implements Serializable {
	
	private static final long serialVersionUID = 2776750599101372424L;
	private String id;

	/*
	 * =======================================================================
	 * Datos identificativos
	 *
	 * El campo "id" heredado de la clase Entity es el código único.
	 * =======================================================================
	 */

	/**
	 * Denominación de la unidad.
	 */
	private String nombre;

	/**
	 * Código del nivel de administración (Estatal, Autonómica o Local)
	 */
	private String nivelAdministracion;

	/**
	 * Descripción del nivel de administración.
	 */
	private String descripcionNivelAdministracion;

	/**
	 * Indicador de si la unidad pertenece a una Entidad de Derecho Público.
	 */
	private String indicadorEntidadDerechoPublico;

	/**
	 * Código externo utilizado por la entidad pública que aporta los datos al
	 * Directorio Común.
	 */
	private String idExternoFuente;

	/*
	 * =======================================================================
	 * Datos de dependencia jerárquica
	 * =======================================================================
	 */

	/**
	 *
	 * Unidad superior jerárquica (de la que depende directamente la unidad).
	 */
	private String idUnidadOrganicaSuperior;

	/**
	 * Denominación de la unidad superior jerárquica (de la que depende
	 * directamente la unidad).
	 */
	private String nombreUnidadOrganicaSuperior;

	/**
	 * Unidad raíz (máximo nivel jerárquico en la entidad pública a la que
	 * pertenece la unidad).
	 */
	private String idUnidadOrganicaPrincipal;

	/**
	 * Denominación de la unidad raíz (máximo nivel jerárquico en la entidad
	 * pública a la que pertenece la unidad).
	 */
	private String nombreUnidadOrganicaPrincipal;

	/**
	 * Unidad raíz que representa a la Administración a la que está vinculada
	 * una Entidad de Derecho Público.
	 */
	private String idUnidadOrganicaEntidadDerechoPublico;

	/**
	 * Denominación de la unidad raíz que representa a la Administración a la
	 * que está vinculada una Entidad de Derecho Público.
	 */
	private String nombreUnidadOrganicaEntidadDerechoPublico;

	/**
	 * Nivel jerárquico en la estructura de la entidad pública (secuencial).
	 */
	private Integer nivelJerarquico;

	/*
	 * =======================================================================
	 * Datos de vigencia
	 * =======================================================================
	 */

	/**
	 * Estado de la entidad.
	 */
	private String estado;

	/**
	 * Descripción del estado.
	 */
	private String descripcionEstado;

	/**
	 * Fecha de creación oficial.
	 */
	private Date fechaAltaOficial;

	/**
	 * Fecha de supresión oficial.
	 */
	private Date fechaBajaOficial;

	/**
	 * Fecha de extinción final.
	 */
	private Date fechaExtincion;

	/**
	 * Fecha de anulación.
	 */
	private Date fechaAnulacion;

	/**
	 * Constructor
	 */
	public DatosUnidadOrganica() {
	}

	/**
	 * Obtiene el valor del parámetro id.
	 * 
	 * @return id valor del campo a obtener.
	 *
	 */
	public String getId() {
		return id;
	}

	/**
	 * Guarda el valor del parámetro id.
	 * 
	 * @param id
	 *            valor del campo a guardar.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Obtiene el valor del parámetro nombre.
	 * 
	 * @return nombre valor del campo a obtener.
	 *
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Guarda el valor del parámetro nombre.
	 * 
	 * @param nombre
	 *            valor del campo a guardar.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Obtiene el valor del parámetro nivelAdministracion.
	 * 
	 * @return nivelAdministracion valor del campo a obtener.
	 *
	 */
	public String getNivelAdministracion() {
		return nivelAdministracion;
	}

	/**
	 * Guarda el valor del parámetro nivelAdministracion.
	 * 
	 * @param nivelAdministracion
	 *            valor del campo a guardar.
	 */
	public void setNivelAdministracion(String nivelAdministracion) {
		this.nivelAdministracion = nivelAdministracion;
	}

	/**
	 * Obtiene el valor del parámetro descripcionNivelAdministracion.
	 * 
	 * @return descripcionNivelAdministracion valor del campo a obtener.
	 *
	 */
	public String getDescripcionNivelAdministracion() {
		return descripcionNivelAdministracion;
	}

	/**
	 * Guarda el valor del parámetro setDescripcionNivelAdministracion.
	 * 
	 * @param setDescripcionNivelAdministracion
	 *            valor del campo a guardar.
	 */
	public void setDescripcionNivelAdministracion(
			String descripcionNivelAdministracion) {
		this.descripcionNivelAdministracion = descripcionNivelAdministracion;
	}

	/**
	 * Obtiene el valor del parámetro indicadorEntidadDerechoPublico.
	 * 
	 * @return indicadorEntidadDerechoPublico valor del campo a obtener.
	 *
	 */
	public String getIndicadorEntidadDerechoPublico() {
		return indicadorEntidadDerechoPublico;
	}

	/**
	 * Guarda el valor del parámetro indicadorEntidadDerechoPublico.
	 * 
	 * @param indicadorEntidadDerechoPublico
	 *            valor del campo a guardar.
	 */
	public void setIndicadorEntidadDerechoPublico(
			String indicadorEntidadDerechoPublico) {
		this.indicadorEntidadDerechoPublico = indicadorEntidadDerechoPublico;
	}

	/**
	 * Obtiene el valor del parámetro idExternoFuente.
	 * 
	 * @return idExternoFuente valor del campo a obtener.
	 *
	 */
	public String getIdExternoFuente() {
		return idExternoFuente;
	}

	/**
	 * Guarda el valor del parámetro idExternoFuente.
	 * 
	 * @param idExternoFuente
	 *            valor del campo a guardar.
	 */
	public void setIdExternoFuente(String idExternoFuente) {
		this.idExternoFuente = idExternoFuente;
	}

	/**
	 * Obtiene el valor del parámetro idUnidadOrganicaSuperior.
	 * 
	 * @return idUnidadOrganicaSuperior valor del campo a obtener.
	 *
	 */
	public String getIdUnidadOrganicaSuperior() {
		return idUnidadOrganicaSuperior;
	}

	/**
	 * Guarda el valor del parámetro idUnidadOrganicaSuperior.
	 * 
	 * @param idUnidadOrganicaSuperior
	 *            valor del campo a guardar.
	 */
	public void setIdUnidadOrganicaSuperior(String idUnidadOrganicaSuperior) {
		this.idUnidadOrganicaSuperior = idUnidadOrganicaSuperior;
	}

	/**
	 * Obtiene el valor del parámetro nombreUnidadOrganicaSuperior.
	 * 
	 * @return nombreUnidadOrganicaSuperior valor del campo a obtener.
	 *
	 */
	public String getNombreUnidadOrganicaSuperior() {
		return nombreUnidadOrganicaSuperior;
	}

	/**
	 * Guarda el valor del parámetro nombreUnidadOrganicaSuperior.
	 * 
	 * @param nombreUnidadOrganicaSuperior
	 *            valor del campo a guardar.
	 */
	public void
			setNombreUnidadOrganicaSuperior(String nombreUnidadOrganicaSuperior) {
		this.nombreUnidadOrganicaSuperior = nombreUnidadOrganicaSuperior;
	}

	/**
	 * Obtiene el valor del parámetro idUnidadOrganicaPrincipal.
	 * 
	 * @return idUnidadOrganicaPrincipal valor del campo a obtener.
	 *
	 */
	public String getIdUnidadOrganicaPrincipal() {
		return idUnidadOrganicaPrincipal;
	}

	/**
	 * Guarda el valor del parámetro idUnidadOrganicaPrincipal.
	 * 
	 * @param idUnidadOrganicaPrincipal
	 *            valor del campo a guardar.
	 */
	public void setIdUnidadOrganicaPrincipal(String idUnidadOrganicaPrincipal) {
		this.idUnidadOrganicaPrincipal = idUnidadOrganicaPrincipal;
	}

	/**
	 * Obtiene el valor del parámetro nombreUnidadOrganicaPrincipal.
	 * 
	 * @return nombreUnidadOrganicaPrincipal valor del campo a obtener.
	 *
	 */
	public String getNombreUnidadOrganicaPrincipal() {
		return nombreUnidadOrganicaPrincipal;
	}

	/**
	 * Guarda el valor del parámetro nombreUnidadOrganicaPrincipal.
	 * 
	 * @param nombreUnidadOrganicaPrincipal
	 *            valor del campo a guardar.
	 */
	public void setNombreUnidadOrganicaPrincipal(
			String nombreUnidadOrganicaPrincipal) {
		this.nombreUnidadOrganicaPrincipal = nombreUnidadOrganicaPrincipal;
	}

	/**
	 * Obtiene el valor del parámetro idUnidadOrganicaEntidadDerechoPublico.
	 * 
	 * @return idUnidadOrganicaEntidadDerechoPublico valor del campo a obtener.
	 *
	 */
	public String getIdUnidadOrganicaEntidadDerechoPublico() {
		return idUnidadOrganicaEntidadDerechoPublico;
	}

	/**
	 * Guarda el valor del parámetro idUnidadOrganicaEntidadDerechoPublico.
	 * 
	 * @param idUnidadOrganicaEntidadDerechoPublico
	 *            valor del campo a guardar.
	 */
	public void setIdUnidadOrganicaEntidadDerechoPublico(
			String idUnidadOrganicaEntidadDerechoPublico) {
		this.idUnidadOrganicaEntidadDerechoPublico =
				idUnidadOrganicaEntidadDerechoPublico;
	}

	/**
	 * Obtiene el valor del parámetro nombreUnidadOrganicaEntidadDerechoPublico.
	 * 
	 * @return nombreUnidadOrganicaEntidadDerechoPublico valor del campo a obtener.
	 *
	 */
	public String getNombreUnidadOrganicaEntidadDerechoPublico() {
		return nombreUnidadOrganicaEntidadDerechoPublico;
	}

	/**
	 * Guarda el valor del parámetro nombreUnidadOrganicaEntidadDerechoPublico.
	 * 
	 * @param nombreUnidadOrganicaEntidadDerechoPublico
	 *            valor del campo a guardar.
	 */
	public void setNombreUnidadOrganicaEntidadDerechoPublico(
			String nombreUnidadOrganicaEntidadDerechoPublico) {
		this.nombreUnidadOrganicaEntidadDerechoPublico =
				nombreUnidadOrganicaEntidadDerechoPublico;
	}

	/**
	 * Obtiene el valor del parámetro nivelJerarquico.
	 * 
	 * @return nivelJerarquico valor del campo a obtener.
	 *
	 */
	public Integer getNivelJerarquico() {
		return nivelJerarquico;
	}

	/**
	 * Guarda el valor del parámetro nivelJerarquico.
	 * 
	 * @param nivelJerarquico
	 *            valor del campo a guardar.
	 */
	public void setNivelJerarquico(Integer nivelJerarquico) {
		this.nivelJerarquico = nivelJerarquico;
	}

	/**
	 * Obtiene el valor del parámetro estado.
	 * 
	 * @return estado valor del campo a obtener.
	 *
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Guarda el valor del parámetro estado.
	 * 
	 * @param estado
	 *            valor del campo a guardar.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Obtiene el valor del parámetro descripcionEstado.
	 * 
	 * @return descripcionEstado valor del campo a obtener.
	 *
	 */
	public String getDescripcionEstado() {
		return descripcionEstado;
	}

	/**
	 * Guarda el valor del parámetro descripcionEstado.
	 * 
	 * @param descripcionEstado
	 *            valor del campo a guardar.
	 */
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}

	/**
	 * Obtiene el valor del parámetro fechaAltaOficial.
	 * 
	 * @return fechaAltaOficial valor del campo a obtener.
	 *
	 */
	public Date getFechaAltaOficial() {
		return fechaAltaOficial;
	}

	/**
	 * Guarda el valor del parámetro fechaAltaOficial.
	 * 
	 * @param fechaAltaOficial
	 *            valor del campo a guardar.
	 */
	public void setFechaAltaOficial(Date fechaAltaOficial) {
		this.fechaAltaOficial = fechaAltaOficial;
	}

	/**
	 * Obtiene el valor del parámetro fechaBajaOficial.
	 * 
	 * @return fechaBajaOficial valor del campo a obtener.
	 *
	 */
	public Date getFechaBajaOficial() {
		return fechaBajaOficial;
	}

	/**
	 * Guarda el valor del parámetro fechaBajaOficial.
	 * 
	 * @param fechaBajaOficial
	 *            valor del campo a guardar.
	 */
	public void setFechaBajaOficial(Date fechaBajaOficial) {
		this.fechaBajaOficial = fechaBajaOficial;
	}

	/**
	 * Obtiene el valor del parámetro fechaExtincion.
	 * 
	 * @return fechaExtincion valor del campo a obtener.
	 *
	 */
	public Date getFechaExtincion() {
		return fechaExtincion;
	}

	/**
	 * Guarda el valor del parámetro fechaExtincion.
	 * 
	 * @param fechaExtincion
	 *            valor del campo a guardar.
	 */
	public void setFechaExtincion(Date fechaExtincion) {
		this.fechaExtincion = fechaExtincion;
	}

	/**
	 * Obtiene el valor del parámetro fechaAnulacion.
	 * 
	 * @return fechaAnulacion valor del campo a obtener.
	 *
	 */
	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}

	/**
	 * Guarda el valor del parámetro fechaAnulacion.
	 * 
	 * @param fechaAnulacion
	 *            valor del campo a guardar.
	 */
	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}
}
