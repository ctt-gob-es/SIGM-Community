package es.dipucr.sgm.registropresencial.bussinessobject;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;

import es.dipucr.sgm.registropresencial.beans.EscanerBean;
import es.dipucr.sgm.registropresencial.configurations.EscanerConfiguration;
import es.ieci.tecdoc.fwktd.dm.business.util.MimeTypeUtils;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.businessobject.IGenericBo;

public class EscanerBo implements IGenericBo, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(EscanerBo.class);
	
	private static final String PROTOCOLO = "sigemScan";
	private static final String PATH_UPLOAD = "/FileUploadScanDipucr";
	private static final String DESTINO_UPLOAD = "REGISTRO";
	
	private static final String INSTALADOR_NAME = "Setup-addon-scan-";
	private static final String INSTALADOR_EXTENSION = ".exe";

	public static String getUrlEscaner(EscanerBean escanerBean) {
		String urlEscanerCarpeta  = "";
			
		Integer folderId = getFdrId(escanerBean);
		String sessionPId = escanerBean.getUseCaseConf().getSessionID();

		String urlServer = escanerBean.getUrlServer();

		String sessionId = escanerBean.getSessionId();
		String urlCheckForUpdates = Configurator.getInstance().getProperty(ConfigurationKeys.KEY_URL_CHECK_FOR_UPDATES_SCAN);
		
		String locale = escanerBean.getUseCaseConf().getLocale().getLanguage();

		String argumentosInvocacionEscanear = "config=false" + "&" +
				"bookId=" + escanerBean.getBookId() + "&" +
				"folderId=" + folderId + "&" +
				"sessionPId=" + sessionPId + "&" +
				"urlUploadAction=" + urlServer + PATH_UPLOAD + "&" +
				"destinoUpload=" + DESTINO_UPLOAD + "&" +
				"sessionId=" + sessionId + "&" +
				"urlCheckForUpdates=" + urlServer + File.separator +  urlCheckForUpdates + "&" +
				"entidadId=" + escanerBean.getUseCaseConf().getEntidadId() + "&";
		
		if(StringUtils.isNotEmpty(escanerBean.getCarpetaDestino())){
				argumentosInvocacionEscanear += "nombreCarpeta=" + escanerBean.getCarpetaDestino() + "&";
		}
		argumentosInvocacionEscanear += "lang=" + locale;
									 			
		urlEscanerCarpeta = PROTOCOLO + ":" + argumentosInvocacionEscanear;
			
		return urlEscanerCarpeta;
	}
	
	public static Integer getFdrId(EscanerBean compulsaBean){
		Integer folderId = new Integer(-1);
		if(compulsaBean.getRegisterBean() instanceof InputRegisterBean){
			folderId = ((InputRegisterBean)compulsaBean.getRegisterBean()).getFdrid();
		} else if(compulsaBean.getRegisterBean() instanceof OutputRegisterBean){
			folderId = ((OutputRegisterBean)compulsaBean.getRegisterBean()).getFdrid();
		}
		
		return folderId;
	}

	public static StreamedContent getInstaladorEscaner() {
		
		StreamedContent instaladorEscaner = null;
		
		try {
			String version = gerVersionInstalador();
			 
			String urlCheckForUpdates = Configurator.getInstance().getProperty(ConfigurationKeys.KEY_URL_CHECK_FOR_UPDATES_SCAN);
			String instaladorName = INSTALADOR_NAME + version + INSTALADOR_EXTENSION;

			String rutaAbsolutaInstalador = FacesContext.getCurrentInstance().getExternalContext().getRealPath(urlCheckForUpdates + instaladorName);
		
			File instalador = new File (rutaAbsolutaInstalador);
			FileInputStream instaladorInputStream = new FileInputStream(instalador);
		
			instaladorEscaner = new DefaultStreamedContent( instaladorInputStream, MimeTypeUtils.getMimeType(instalador), instalador.getName());
		} catch (FileNotFoundException e) {
			LOGGER.error("Error al recuperar la aplicación del escáner. " + e.getMessage(), e);
		}
		
		return instaladorEscaner;
	}

	public static String gerVersionInstalador() {
		
		String version = "0";
		
		EscanerConfiguration escanerConfiguration = new EscanerConfiguration();
		if(null != escanerConfiguration){
			version = escanerConfiguration.getProperty(EscanerConfiguration.VERSION);
		}
		
		return version;
	}
}
