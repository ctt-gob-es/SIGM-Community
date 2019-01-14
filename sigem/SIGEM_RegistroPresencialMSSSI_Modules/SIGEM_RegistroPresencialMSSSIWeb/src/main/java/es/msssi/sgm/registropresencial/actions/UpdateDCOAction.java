package es.msssi.sgm.registropresencial.actions;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.msssi.dir3.api.service.impl.UpdateServiceDCOImpl;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Action que actualiza DCO disponible.
 * 
 * @author cmorenog
 */
public class UpdateDCOAction extends GenericActions {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(UpdateDCOAction.class.getName());
    private UpdateServiceDCOImpl updateServiceDCO;
    private static ApplicationContext appContext;
    private String textFinal = "Pulse <b>Actualizar</b> para descargar los cambios de DIR3 y actualizar los organismos y unidades administrativas del registro. ";
    private Boolean disabled = false;
    private Date initFrom;
    private Date endTo;
    private Date lastUpdate;
    
    static {
	appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();
       }
    
    /**
     * Constructor.
     */
    public UpdateDCOAction() {
	updateServiceDCO = (UpdateServiceDCOImpl) appContext.getBean("apiUpdateServiceDCOImpl");
    }

    /**
     * Modifica DCO.
     * 
     * @return suggestions Lista de unidades tramitadoras según el texto
     *         insertado.
     */
    
    public void updateDCO() {
	try {
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
	    updateServiceDCO.updateDCO();
	    disabled = true;
	    textFinal = "Se ha realizado la actualización satisfactoriamente";
	}
	catch (DIR3Exception e) {
	    LOG.error(
		ErrorConstants.UPDATE_UNID_REGISTER_ERROR_MESSAGE, e);
	    Utils.redirectToErrorPage(
		null, null, e);
	}
    }
    
    /**
     * Modifica DCO.
     * 
     * @return suggestions Lista de unidades tramitadoras según el texto
     *         insertado.
     */
    
    public void reseteaDCO() {
    	try {
    		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
    		updateServiceDCO.reseteaDCO();
    		disabled = true;
    		textFinal = "Se ha realizado la actualización satisfactoriamente";
    		
    	} catch (DIR3Exception e) {
    		LOG.error( ErrorConstants.UPDATE_UNID_REGISTER_ERROR_MESSAGE, e);
    		Utils.redirectToErrorPage( null, null, e);
    	}
    }

    public String getTextFinal() {
        return textFinal;
    }

    public void setTextFinal(String textFinal) {
        this.textFinal = textFinal;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Date getInitFrom() {
        return initFrom;
    }

    public void setInitFrom(Date initFrom) {
        this.initFrom = initFrom;
    }

    public Date getEndTo() {
        return endTo;
    }

    public void setEndTo(Date endTo) {
        this.endTo = endTo;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}