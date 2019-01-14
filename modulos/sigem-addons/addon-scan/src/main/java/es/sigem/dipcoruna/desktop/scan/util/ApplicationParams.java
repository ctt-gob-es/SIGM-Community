package es.sigem.dipcoruna.desktop.scan.util;

import org.springframework.util.StringUtils;

import es.sigem.dipcoruna.framework.service.util.ParametersWrapper;

public class ApplicationParams {
    private String paramConfig;
    private String paramUrlUploadAction;
    private String paramDestinoUpload;	
	private String paramCookies;
	private String paramCookiesPath;
	private String paramCookiesDomain;
	private String paramSessionId;
	private String paramUploadToken;
	private String paramMaxUploadFiles;
	
	
	private String paramUrlCheckForUpdates;
	private String paramLang;	

	private String paramTramitadorDocumentId;
	private String paramRegistroFolderId;
    private String paramRegistroSessionPId;
    
    private String paramBookId;
    private String paramEntidadId;
    private String paramNombreCarpeta;


	private ApplicationParams() {
	}

	public static ApplicationParams build(final ParametersWrapper parametersWrapper) {
		final ApplicationParams appParams = new ApplicationParams();

		appParams.paramConfig =  parametersWrapper.getSimpleValue("config");
		appParams.paramUrlUploadAction = parametersWrapper.getSimpleValue("urlUploadAction");
		appParams.paramDestinoUpload = parametersWrapper.getSimpleValue("destinoUpload");
		appParams.paramCookies = parametersWrapper.getSimpleValue("cookies");
		appParams.paramCookiesPath = parametersWrapper.getSimpleValue("cookiesPath");
		appParams.paramCookiesDomain = parametersWrapper.getSimpleValue("cookiesDomain");
		appParams.paramSessionId = parametersWrapper.getSimpleValue("sessionId");
		appParams.paramUploadToken = parametersWrapper.getSimpleValue("uploadToken");
		appParams.paramMaxUploadFiles = parametersWrapper.getSimpleValue("maxUploadFiles");
		
		
		appParams.paramTramitadorDocumentId = parametersWrapper.getSimpleValue("documentTypeId");
		appParams.paramRegistroFolderId   = parametersWrapper.getSimpleValue("folderId");
		appParams.paramRegistroSessionPId = parametersWrapper.getSimpleValue("sessionPId");

		appParams.paramBookId = parametersWrapper.getSimpleValue("bookId");		
		appParams.paramEntidadId = parametersWrapper.getSimpleValue("entidadId");
		appParams.paramNombreCarpeta = parametersWrapper.getSimpleValue("nombreCarpeta");
		
		appParams.paramUrlCheckForUpdates = parametersWrapper.getSimpleValue("urlCheckForUpdates");
		appParams.paramLang =  parametersWrapper.getSimpleValue("lang");

		return appParams;
	}


	public boolean esModoConfiguracion() {
		 return "true".equalsIgnoreCase(paramConfig) || !StringUtils.hasText(paramUrlUploadAction);
	}

	
	public String getUrlUploadAction() {
		return paramUrlUploadAction;
	}

	public String getDestinoUpload() {
	    return paramDestinoUpload;
	}

	public String getCookies() {
		return paramCookies;
	}

	public String getCookiesPath() {
        return paramCookiesPath;
    }

	public String getCookiesDomain() {
        return paramCookiesDomain;
    }

    public String getSessionId() {
        return paramSessionId;
    }

    public String getUploadToken() {
        return paramUploadToken;
    }

    public String getTramitadorDocumentId() {
        return paramTramitadorDocumentId;
    }

    public String getRegistroFolderId() {
        return paramRegistroFolderId;
    }

    public String getRegistroSessionPId() {
        return paramRegistroSessionPId;
    }

	public String getLang() {
		return paramLang;
	}

	public String getUrlCheckForUpdates() {
		return paramUrlCheckForUpdates;
	}

    public String getMaxUploadFiles() {
        return paramMaxUploadFiles;
    }
    
    public String getEntidadId() {
        return paramEntidadId;
    }
    
    public String getBookId() {
        return paramBookId;
    }
    
    public String getNombreCarpeta() {
        return paramNombreCarpeta;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ApplicationParams [paramConfig=");
        builder.append(paramConfig);
        builder.append(", paramUrlUploadAction=");
        builder.append(paramUrlUploadAction);
        builder.append(", paramDestinoUpload=");
        builder.append(paramDestinoUpload);
        builder.append(", paramCookies=");
        builder.append(paramCookies);
        builder.append(", paramCookiesPath=");
        builder.append(paramCookiesPath);
        builder.append(", paramCookiesDomain=");
        builder.append(paramCookiesDomain);
        builder.append(", paramSessionId=");
        builder.append(paramSessionId);
        builder.append(", paramUploadToken=");
        builder.append(paramUploadToken);
        builder.append(", paramMaxUploadFiles=");
        builder.append(paramMaxUploadFiles);
        builder.append(", paramUrlCheckForUpdates=");
        builder.append(paramUrlCheckForUpdates);
        builder.append(", paramLang=");
        builder.append(paramLang);
        builder.append(", paramTramitadorDocumentId=");
        builder.append(paramTramitadorDocumentId);
        builder.append(", paramRegistroFolderId=");
        builder.append(paramRegistroFolderId);
        builder.append(", paramRegistroSessionPId=");
        builder.append(paramRegistroSessionPId);
        builder.append(", paramEntidadId=");
        builder.append(paramEntidadId);
        builder.append(", paramBookId=");
        builder.append(paramBookId);
        builder.append(", paramNombreCarpeta=");
        builder.append(paramNombreCarpeta);
        builder.append("]");
        return builder.toString();
    }
}
