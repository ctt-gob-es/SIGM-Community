package es.sigem.dipcoruna.desktop.editlauncher.util;

import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.framework.service.util.ParametersWrapper;

public class ApplicationParams {
	private final String paramConfig;
	private final String paramUrlDoc;
	private final String paramUrlCheckForUpdates;
	private final String paramLang;
	
	private ApplicationParams(String paramConfig, String paramUrlDoc, String paramUrlCheckForUpdates, String paramLang) {
		super();
		this.paramConfig = paramConfig;
		this.paramUrlDoc = paramUrlDoc;
		this.paramUrlCheckForUpdates = paramUrlCheckForUpdates;
		this.paramLang = paramLang;
	}

	public static ApplicationParams build(ParametersWrapper parametersWrapper) {
		String paramConfig =  parametersWrapper.getSimpleValue("config");
		String paramUrlDoc = parametersWrapper.getSimpleValue("urlDoc");		
		String paramUrlCheckForUpdates = parametersWrapper.getSimpleValue("urlCheckForUpdates");
		String paramLang =  parametersWrapper.getSimpleValue("lang");
		return new ApplicationParams(paramConfig, paramUrlDoc, paramUrlCheckForUpdates, paramLang);
	}
	
	
	public boolean esModoConfiguracion() {
		 return !StringUtils.hasText(paramUrlDoc) ||"true".equalsIgnoreCase(paramConfig);
	}
	
	public String getLang() {
		return paramLang;
	}
	
	public String getUrlDoc() {
		return paramUrlDoc;
	}	
	
	public boolean seProporcionaUrlParaActualizaciones() {
		return StringUtils.hasText(paramUrlCheckForUpdates);
	}
	
	public String getUrlCheckForUpdates() {
		return paramUrlCheckForUpdates;
	}

	
	
	@Override
	public String toString() {
		return "ApplicationParams [paramConfig=" + paramConfig
				+ ", paramUrlDoc=" + paramUrlDoc + ", paramUrlCheckForUpdates="
				+ paramUrlCheckForUpdates + ", paramLang=" + paramLang + "]";
	}		
}
