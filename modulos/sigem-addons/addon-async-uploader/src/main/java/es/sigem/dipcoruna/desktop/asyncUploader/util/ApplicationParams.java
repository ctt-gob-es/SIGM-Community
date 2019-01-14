package es.sigem.dipcoruna.desktop.asyncUploader.util;

import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.framework.service.util.ParametersWrapper;

/**
 * Clase para encapsular los parametros del addon
 */
public final class ApplicationParams {
	
	private final String paramUser;
	private final String paramTramite;
	private final String paramTipoDocumento;
	private final String paramUrlCheckForUpdates;
	private final String paramLang;
	private final String paramEntidad;
	private final String fase;
	private final String tipoDestino;
	private final String idDestino;
	

	/**
	 * Constructor con todos los parametros
	 * @param user user
	 * @param tramite tramite
	 * @param tipoDocumento tipoDocumento
	 * @param entidad entidad
	 * @param fase fase
	 * @param tipoDestino tipoDestino
	 * @param idDestino idDestino
	 * @param urlCheckForUpdates urlCheckForUpdates
	 * @param lang lang
	 */
	private ApplicationParams(final String user, final String tramite, final String tipoDocumento, final String entidad, 
			final String fase, final String tipoDestino, final String idDestino, final String urlCheckForUpdates, final String lang) {
		super();
		this.paramUser = user;
		this.paramTramite = tramite;
		this.paramTipoDocumento = tipoDocumento;
		this.paramEntidad = entidad;
		this.fase = fase;
		this.tipoDestino = tipoDestino;
		this.idDestino = idDestino;
		this.paramUrlCheckForUpdates = urlCheckForUpdates;
		this.paramLang = lang;
	}

	/**
	 * Construye una instancia de ApplicationParams con el wrapper de parametros
	 * @param parametersWrapper parametersWrapper
	 * @return ApplicationParams con los valores correspondientes
	 */
	public static ApplicationParams build(final ParametersWrapper parametersWrapper) {
		String paramUser =  parametersWrapper.getSimpleValue("user");
		String paramTramite =  parametersWrapper.getSimpleValue("tramite");
		String paramTipoDocumento = parametersWrapper.getSimpleValue("tpdoc");
		String paramEntidad = parametersWrapper.getSimpleValue("entidad");
		String fase = parametersWrapper.getSimpleValue("fase");
		String tipoDestino = parametersWrapper.getSimpleValue("tipodestino");
		String idDestino = parametersWrapper.getSimpleValue("iddestino");
		String paramUrlCheckForUpdates = parametersWrapper.getSimpleValue("urlCheckForUpdates");
		String paramLang =  parametersWrapper.getSimpleValue("lang");
		return new ApplicationParams(paramUser, paramTramite, paramTipoDocumento, paramEntidad, fase, tipoDestino,
				idDestino, paramUrlCheckForUpdates, paramLang);
	}
	
	public String getLang() {
		return paramLang;
	}
	
	public String getUser() {
		return paramUser;
	}	

	public String getTramite() {
		return paramTramite;
	}	
	
	public boolean seProporcionaUrlParaActualizaciones() {
		return StringUtils.hasText(paramUrlCheckForUpdates);
	}
	
	public String getUrlCheckForUpdates() {
		return paramUrlCheckForUpdates;
	}

	public String getParamTipoDocumento() {
		return paramTipoDocumento;
	}		

	public String getParamEntidad() {
		return paramEntidad;
	}

	/**
	 * @return the paramUser
	 */
	public String getParamUser() {
		return paramUser;
	}

	/**
	 * @return the paramTramite
	 */
	public String getParamTramite() {
		return paramTramite;
	}

	/**
	 * @return the paramUrlCheckForUpdates
	 */
	public String getParamUrlCheckForUpdates() {
		return paramUrlCheckForUpdates;
	}

	/**
	 * @return the paramLang
	 */
	public String getParamLang() {
		return paramLang;
	}

	/**
	 * @return the fase
	 */
	public String getFase() {
		return fase;
	}

	/**
	 * @return the tipoDestino
	 */
	public String getTipoDestino() {
		return tipoDestino;
	}

	/**
	 * @return the idDestino
	 */
	public String getIdDestino() {
		return idDestino;
	}

	@Override
	public String toString() {
		return "ApplicationParams [paramUser=" + paramUser + ", paramTramite=" + paramTramite + ", paramTipoDocumento=" + paramTipoDocumento 
				+ ", paramEntidad=" + paramEntidad + ", fase=" + fase + ", tipoDestino=" + tipoDestino + ", idDestino=" + idDestino 
				+ ", paramUrlCheckForUpdates=" + paramUrlCheckForUpdates + ", paramLang=" + paramLang + "]";
	}

}
