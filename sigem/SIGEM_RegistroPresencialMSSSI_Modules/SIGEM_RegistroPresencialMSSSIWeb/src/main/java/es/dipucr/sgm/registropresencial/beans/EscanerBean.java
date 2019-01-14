package es.dipucr.sgm.registropresencial.beans;

import org.primefaces.model.StreamedContent;

import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.dipucr.sgm.registropresencial.bussinessobject.EscanerBo;
import es.msssi.sgm.registropresencial.beans.GenericBean;

public class EscanerBean extends GenericBean {
	
    private static final long serialVersionUID = 1L;
    
    private UseCaseConf useCaseConf  = null;
    private Object registerBean = null;
	private Integer bookId = null;
	private String sessionId = null;

	private String urlEscaner;
	private String urlServer;
	
	private String carpetaDestino;
	
	private StreamedContent instaladorEscaner;
		
	public UseCaseConf getUseCaseConf() {
		return useCaseConf;
	}

	public void setUseCaseConf(UseCaseConf useCaseConf) {
		this.useCaseConf = useCaseConf;
	}

	public Object getRegisterBean() {
		return registerBean;
	}

	public void setRegisterBean(Object registerBean) {
		this.registerBean = registerBean;
	}

	public String getUrlEscaner() {
		urlEscaner = EscanerBo.getUrlEscaner(this); 
		return urlEscaner;
	}
	
	public String getUrlServer() {
		return urlServer;
	}

	public void setUrlServer(String urlServer) {
		this.urlServer = urlServer;
	}

	public void setUrlEscaner(String urlEscaner) {
		this.urlEscaner = urlEscaner;
	}	

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCarpetaDestino() {
		return carpetaDestino;
	}

	public void setCarpetaDestino(String carpetaDestino) {
		this.carpetaDestino = carpetaDestino;
	}

	public StreamedContent getInstaladorEscaner() {
		
		return EscanerBo.getInstaladorEscaner();
	}

	public void setInstaladorEscaner(StreamedContent instaladorEscaner) {
		this.instaladorEscaner = instaladorEscaner;
	}
}
