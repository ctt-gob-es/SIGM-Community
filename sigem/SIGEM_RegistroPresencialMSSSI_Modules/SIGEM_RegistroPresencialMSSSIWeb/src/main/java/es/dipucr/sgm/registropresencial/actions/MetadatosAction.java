package es.dipucr.sgm.registropresencial.actions;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.isicres.usecase.book.BookUseCase;

import es.dipucr.metadatos.beans.MetadatosDocumentoBean;
import es.dipucr.metadatos.bussinessobject.MetadatosBo;
import es.dipucr.metadatos.diccionarios.NombresMetadatos;
import es.dipucr.sgm.registropresencial.bussinessobject.HistoricoDocumentosBO;
import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.msssi.sgm.registropresencial.actions.GenericActions;

public class MetadatosAction extends GenericActions{

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(MetadatosAction.class);
	
	private MetadatosDocumentoBean metadatosDocumentoBean;
	
    public MetadatosAction() throws SessionException, ValidationException {
		init();
	}

	public MetadatosDocumentoBean getMetadatosDocumentoBean() {
		return metadatosDocumentoBean;
	}

	public void setMetadatosDocumentoBean(MetadatosDocumentoBean metadatosDocumentoBean) {
		this.metadatosDocumentoBean = metadatosDocumentoBean;
	}
	
	public void actualizaMetadatoOrigenCiudadanoAdministracion() {
		if(MetadatosBo.tieneMetadatos(this.metadatosDocumentoBean)){
			String valorViejo = MetadatosBo.getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.ORIGEN);
			String valorNuevo = metadatosDocumentoBean.getOrigen();
			
			if(!StringUtils.equalsNullEmpty(valorViejo, valorNuevo)){

				MetadatosBo.updateMetadato(metadatosDocumentoBean, metadatosDocumentoBean.getOrigen(), NombresMetadatos.ORIGEN);
				
				insertaHistoricoMetadato(HistoricoDocumentosBO.TIPO_DOCUMENTO, NombresMetadatos.ORIGEN, valorViejo, metadatosDocumentoBean.getOrigen());
			}
			
		} else {
			MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
			MetadatosBo.insertaMetadatos(useCaseConf.getSessionID(), metadatosDocumentoBean);
		}
	}
	
	public void actualizaMetadatoEstadoElaboracion() {
		if(MetadatosBo.tieneMetadatos(this.metadatosDocumentoBean)){
			String valorViejo = MetadatosBo.getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.ESTADO_ELABORACION);
			String valorNuevo = metadatosDocumentoBean.getEstadoElaboracion();
			
			if(!StringUtils.equalsNullEmpty(valorViejo, valorNuevo)){
				MetadatosBo.updateMetadato(metadatosDocumentoBean, metadatosDocumentoBean.getEstadoElaboracion(), NombresMetadatos.ESTADO_ELABORACION);

				insertaHistoricoMetadato(HistoricoDocumentosBO.TIPO_DOCUMENTO, NombresMetadatos.ESTADO_ELABORACION, valorViejo, metadatosDocumentoBean.getEstadoElaboracion());
			}
			
		} else {
			MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
			MetadatosBo.insertaMetadatos(useCaseConf.getSessionID(), metadatosDocumentoBean);
		}
	}
	
	public void actualizaMetadatoTipoDocumental() {
		if(MetadatosBo.tieneMetadatos(this.metadatosDocumentoBean)){
			String valorViejo = MetadatosBo.getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.TIPO_DOCUMENTAL);
			String valorNuevo = metadatosDocumentoBean.getTipoDocumental();
			
			if(!StringUtils.equalsNullEmpty(valorViejo, valorNuevo)){

				MetadatosBo.updateMetadato(metadatosDocumentoBean, metadatosDocumentoBean.getTipoDocumental(), NombresMetadatos.TIPO_DOCUMENTAL);
			
				insertaHistoricoMetadato(HistoricoDocumentosBO.TIPO_DOCUMENTO, NombresMetadatos.TIPO_DOCUMENTAL, valorViejo, metadatosDocumentoBean.getTipoDocumental());
			}
			
		} else {
			MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
			MetadatosBo.insertaMetadatos(useCaseConf.getSessionID(), metadatosDocumentoBean);
		}
	}
	
	public void actualizaMetadatoTipoFirma() {
		if(MetadatosBo.tieneMetadatos(this.metadatosDocumentoBean)){
			String valorViejo = MetadatosBo.getValorMetadatoByNombre(metadatosDocumentoBean, NombresMetadatos.TIPO_FIRMA);
			String valorNuevo = metadatosDocumentoBean.getTipoFirma();
			
			if(!StringUtils.equalsNullEmpty(valorViejo, valorNuevo)){
				MetadatosBo.updateMetadato(metadatosDocumentoBean, metadatosDocumentoBean.getTipoFirma(), NombresMetadatos.TIPO_FIRMA);
				insertaHistoricoMetadato(HistoricoDocumentosBO.TIPO_DOCUMENTO, NombresMetadatos.TIPO_FIRMA, valorViejo, metadatosDocumentoBean.getTipoFirma());
			}
			
		} else {
			MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
			MetadatosBo.insertaMetadatos(useCaseConf.getSessionID(), metadatosDocumentoBean);
		}
	}
	
	public void insertaHistoricoMetadato(int tipoDocumento, String nombreMetadato, String valorViejo, String valorNuevo){
		BookUseCase bookUseCase = new BookUseCase();
		AxSf axsf;
		try {
			axsf = bookUseCase.getBookFolder(useCaseConf, metadatosDocumentoBean.getBookId(), metadatosDocumentoBean.getFolderId());
			
			if(StringUtils.isEmpty(valorViejo)){
				valorViejo = "";
			}
			
			HistoricoDocumentosBO.historicoDocCambioMetadato(useCaseConf, axsf.getAttributeValueAsString("fld1"), metadatosDocumentoBean.getBookId(), tipoDocumento, metadatosDocumentoBean.getNombreDoc(), nombreMetadato, valorViejo, valorNuevo);
			
		} catch (ValidationException e) {
			LOGGER.error("ERROR al insertar el cambio del metadato: [" + nombreMetadato + "] -> [" + valorViejo + "] -> [" + valorNuevo + "]. " + e.getMessage(), e);
		} catch (BookException e) {
			LOGGER.error("ERROR al insertar el cambio del metadato: [" + nombreMetadato + "] -> [" + valorViejo + "] -> [" + valorNuevo + "]. " + e.getMessage(), e);
		} catch (SessionException e) {
			LOGGER.error("ERROR al insertar el cambio del metadato: [" + nombreMetadato + "] -> [" + valorViejo + "] -> [" + valorNuevo + "]. " + e.getMessage(), e);
		}
	}
}
