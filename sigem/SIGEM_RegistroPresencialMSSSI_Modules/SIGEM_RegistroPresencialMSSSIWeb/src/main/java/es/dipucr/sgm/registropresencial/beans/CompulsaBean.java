package es.dipucr.sgm.registropresencial.beans;

import ieci.tdw.ispac.api.errors.ISPACRuleException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.lowagie.text.Element;

import es.dipucr.sgm.registropresencial.bussinessobject.CompulsaBo;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.msssi.sgm.registropresencial.beans.GenericBean;

public class CompulsaBean extends GenericBean {
	
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = Logger.getLogger(CompulsaBean.class);
	
    private UseCaseConf useCaseConf  = null;
    private Object registerBean = null;
	private Integer bookId = null;
	private String sessionId = null;
	
	private String dirFirma = null;
	private String urlCompulsa;
	private String urlServer;
	
    private String nombreFirmante = null;
	private String numeroSerieCertificado = null;
	private String dniFirmante = null;
	
	private String posicionSello = CompulsaBo.IZQUIERDA;
	private int alineacionSello = Element.ALIGN_LEFT;
	
	private String listaDocsFirmar = null;
	
	private boolean esFirmaTodos = true;
	
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

	public String getNombreFirmante() {
		return nombreFirmante;
	}

	public void setNombreFirmante(String nombreFirmante) {
		this.nombreFirmante = nombreFirmante;
	}

	public String getNumeroSerieCertificado() {
		return numeroSerieCertificado;
	}

	public void setNumeroSerieCertificado(String numeroSerieCertificado) {
		this.numeroSerieCertificado = numeroSerieCertificado;
	}

	public String getDniFirmante() {
		return dniFirmante;
	}

	public void setDniFirmante(String dniFirmante) {
		this.dniFirmante = dniFirmante;
	}

	public String getDirFirma() {
		if(StringUtils.isEmpty(dirFirma)){
			FirmaConfiguration fc;
			try {
				fc = FirmaConfiguration.getInstanceNoSingleton(useCaseConf.getEntidadId());
				dirFirma = fc.getProperty("firmar.ip.https");
			} catch (ISPACRuleException e) {
				LOGGER.error("ERROR al recuperar la dirección del servidor de firma");
			}
		}
		return dirFirma;
	}

	public void setDirFirma(String dirFirma) {
		this.dirFirma = dirFirma;
	}

	public String getPosicionSello() {
		if(StringUtils.isEmpty(posicionSello)){
			posicionSello = CompulsaBo.IZQUIERDA;			
		}
		return posicionSello;
	}

	public void setPosicionSello(String posicionSello) {
		this.posicionSello = posicionSello;
	}

	public int getAlineacionSello() {
		if(Element.ALIGN_LEFT != alineacionSello && Element.ALIGN_RIGHT != alineacionSello){
			alineacionSello = Element.ALIGN_LEFT;
		}
		
		return alineacionSello;
	}

	public void setAlineacionSello(int alineacionSello) {
		this.alineacionSello = alineacionSello;
	}
	
	public String getListaDocsFirmar() {
		return listaDocsFirmar;
	}

	public void setListaDocsFirmar(String listaDocsFirmar) {
		this.listaDocsFirmar = listaDocsFirmar;
	}

	public boolean isEsFirmaTodos() {
		return esFirmaTodos;
	}

	public void setEsFirmaTodos(boolean esFirmaTodos) {
		this.esFirmaTodos = esFirmaTodos;
	}
	
	public String getUrlCompulsa() {
		urlCompulsa = CompulsaBo.getUrlCompulsa(this); 
		return urlCompulsa;
	}
	
	public String getUrlServer() {
		return urlServer;
	}

	public void setUrlServer(String urlServer) {
		this.urlServer = urlServer;
	}

	public void setUrlCompulsa(String urlCompulsa) {
		this.urlCompulsa = urlCompulsa;
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
}
