/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.actions;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.SessionInformation;
import com.ieci.tecdoc.isicres.web.util.ContextoAplicacionUtil;

import es.ieci.tecdoc.isicres.api.business.vo.BaseOficinaVO;
import es.ieci.tecdoc.isicres.api.business.vo.ContextoAplicacionVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralSIRVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralVO;
import es.msssi.sgm.registropresencial.beans.SearchDestinationRegInterchangeBean;
import es.msssi.sgm.registropresencial.businessobject.RegInterchangeBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeErrorCode;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase que implementa el action del intercmbio registral.
 * 
 * @author cmorenog
 */
public class RegInterchangeAction extends GenericActions {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(RegInterchangeAction.class.getName());
    /** clase de negocio de intercambio. */
    private RegInterchangeBo regInterchangeBo;
    private SearchDestinationRegInterchangeBean searchDestinationRegInterchangeBean;
    private Integer registerId;
    private Integer docFisica;
    private String idInterchange;
    private boolean disabled = false;
    
    /**
     * Constructor.
     */
    public RegInterchangeAction() {
	try {
	    searchDestinationRegInterchange();
	}
	catch (RPGenericException e) {
	    LOG.error(ErrorConstants.SEARCH_DESTINATION_REG_INTERCHANGE_ERROR_MESSAGE +
		". Código: " + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
	    Utils.redirectToErrorPage(
		e, null, null);
	}
    }

    /**
     * Método que en un registro el formulario de destinatario por defecto.
     * 
     * @throws RPGenericException
     *             error genérico de la aplicación.
     */
    private void searchDestinationRegInterchange()
	throws RPGenericException {
	ScrRegstate book = null;

	if (regInterchangeBo == null) {
	    regInterchangeBo = new RegInterchangeBo();
	}
	// parámetros de la llamada
	init();
	book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
	    KeysRP.J_BOOK);
	if (book == null) {
	    LOG.error(RPGenericErrorCode.SESSION_ERROR +
		". Código: " + RPGenericErrorCode.SESSION_ERROR + " . Mensaje: " +
		ErrorConstants.SESSION_ERROR_MESSAGE);
	    throw new RPGenericException(
		RPGenericErrorCode.SESSION_ERROR, ErrorConstants.SESSION_ERROR_MESSAGE);
	}
	if (facesContext.getExternalContext().getFlash().get(
	    "registerId") != null) {
	    registerId = (Integer) facesContext.getExternalContext().getFlash().get(
		"registerId");
	}
	if (facesContext.getExternalContext().getFlash().get(
		    "docFisica") != null) {
	    docFisica = (Integer) facesContext.getExternalContext().getFlash().get(
			"docFisica");
	}
	try {
	    searchDestinationRegInterchangeBean =
		regInterchangeBo.getSearchDestinationRegInterchangeBean(
		    useCaseConf, book.getIdocarchhdr().getId(), registerId.intValue());
	    searchDestinationRegInterchangeBean.setBook(book);

	    if (searchDestinationRegInterchangeBean.getUnidTramPorDefecto() != null &&
		searchDestinationRegInterchangeBean.getUnidTramPorDefecto().getCodeTramunit() != null) {
		UnidadTramitacionIntercambioRegistralSIRVO unidadTramMapper =
		    mapperUnidTram(searchDestinationRegInterchangeBean.getUnidTramPorDefecto());
		searchDestinationRegInterchangeBean.setUnidadTramitadoraDestino(unidadTramMapper);

		if (searchDestinationRegInterchangeBean.getUnidTramPorDefecto().getCodeEntity() == null) {
		    throw new RPRegistralExchangeException(
			RPRegistralExchangeErrorCode.SEARCH_DESTINATION_REG_INTERCHANGE_ERROR_MESSAGE,
			"La unidad de tramitación seleccionado no tiene una oficina registral con servicios de SIR asociada.");
		}
	    }
	}
	catch (RPRegistralExchangeException rpRegistralExchangeException) {
	    LOG.error(ErrorConstants.SEARCH_DESTINATION_REG_INTERCHANGE_ERROR_MESSAGE +
		". Código: " + rpRegistralExchangeException.getCode().getCode() + "  . Mensaje: " +
		rpRegistralExchangeException.getShortMessage());
	    Utils.redirectToErrorPage(
		rpRegistralExchangeException, null, null);
	}
	catch (RPGenericException rpGenericException) {
	    LOG.error(ErrorConstants.SEARCH_DESTINATION_REG_INTERCHANGE_ERROR_MESSAGE +
		". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
		rpGenericException.getShortMessage());
	    Utils.redirectToErrorPage(
		rpGenericException, null, null);
	}
    }

    private UnidadTramitacionIntercambioRegistralSIRVO mapperUnidTram(
	UnidadTramitacionIntercambioRegistralVO unidTramPorDefecto) {
	UnidadTramitacionIntercambioRegistralSIRVO result =
	    new UnidadTramitacionIntercambioRegistralSIRVO();
	if (unidTramPorDefecto != null) {
	    result.setId(unidTramPorDefecto.getId());
	    result.setIdOrgs(unidTramPorDefecto.getIdOrgs());
	    result.setCodeEntity(unidTramPorDefecto.getCodeEntity());
	    result.setNameEntity(unidTramPorDefecto.getNameEntity());
	    result.setCodeTramunit(unidTramPorDefecto.getCodeTramunit());
	    result.setNameTramunit(unidTramPorDefecto.getNameTramunit());
	}
	return result;
    }

    /**
     * Redirecciona a la página de modificar registro.
     */
    public void goInputRegisterForm() {
	Map<String, Object> parameter = new HashMap<String, Object>();
	parameter.put(
	    "registerSelect", registerId);
	Utils.navigate(
	    parameter, false, "inputRegister.xhtml");
    }

    /**
     * Redirecciona a la página de modificar registro.
     */
    public void goOutputRegisterForm() {
	Map<String, Object> parameter = new HashMap<String, Object>();
	parameter.put(
	    "registerSelect", registerId);
	Utils.navigate(
	    parameter, false, "outputRegister.xhtml");
    }

    /**
     * Envía el intercambio Registral y redirecciona a la página de modificar
     * registro.
     * 
     * @throws RPGenericException
     *             error genérico de la aplicación.
     */
    public void sendRegInterchange()
	throws RPGenericException {
	init();
	try {
	    if (searchDestinationRegInterchangeBean.getUnidadTramitadoraDestino() == null) {
		FacesContext.getCurrentInstance()
		    .addMessage(
			null,
			new FacesMessage(
			    FacesMessage.SEVERITY_ERROR, "Validación",
			    "Debe rellenar todos los datos"));
	    }
	    else {
		UnidadTramitacionIntercambioRegistralVO unidadTramitacionDestino =
		    new UnidadTramitacionIntercambioRegistralVO();
		unidadTramitacionDestino.setCodeEntity(searchDestinationRegInterchangeBean
		    .getUnidadTramitadoraDestino().getCodeEntity());
		unidadTramitacionDestino.setNameEntity(searchDestinationRegInterchangeBean
		    .getUnidadTramitadoraDestino().getNameEntity());
		unidadTramitacionDestino.setCodeTramunit(searchDestinationRegInterchangeBean
		    .getUnidadTramitadoraDestino().getCodeTramunit());
		unidadTramitacionDestino.setNameTramunit(searchDestinationRegInterchangeBean
		    .getUnidadTramitadoraDestino().getNameTramunit());
		searchDestinationRegInterchangeBean.setUnidTramPorDefecto(unidadTramitacionDestino);

		ContextoAplicacionVO contextoAplicacion =
		    ContextoAplicacionUtil
			.getContextoAplicacion((javax.servlet.http.HttpServletRequest) facesContext
			    .getExternalContext().getRequest());
		SessionInformation sessionInformation =
		    (SessionInformation) facesContext.getExternalContext().getSessionMap().get(
			KeysRP.J_SESSIONINF);
		BaseOficinaVO oficina = contextoAplicacion.getOficinaActual();
		if (regInterchangeBo == null) {
		    regInterchangeBo = new RegInterchangeBo();
		}
		idInterchange =
		    regInterchangeBo.sendInterchangeOutput(
			searchDestinationRegInterchangeBean, sessionInformation.getUserName(),
			sessionInformation.getUserContact(),
			oficina.getId(), useCaseConf);
		FacesContext.getCurrentInstance().addMessage(
		    null,
		    new FacesMessage(
			FacesMessage.SEVERITY_INFO, "El envío se ha realizado con exito. " +
			    "El identificador del intercambio registral es: " + idInterchange,
			"El envío se ha realizado con exito. " +
			    "El identificador del intercambio registral es: " + idInterchange));
		disabled = true;
	    }
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.SEND_REG_INTERCHANGE_ERROR_MESSAGE +
		". Código: " + sessionException.getCode() + " . Mensaje: " +
		sessionException.getMessage());
	    Utils.redirectToErrorPage(
		new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.SEND_REG_INTERCHANGE_ERROR_MESSAGE,
		    ErrorConstants.SEND_REG_INTERCHANGE_ERROR_MESSAGE, sessionException), null,
		null);
	}
	catch (TecDocException tecDocException) {
	    LOG.error(ErrorConstants.SEND_REG_INTERCHANGE_ERROR_MESSAGE +
		". Código: " + tecDocException.getCode() + " . Mensaje: " +
		tecDocException.getMessage());
	    Utils
		.redirectToErrorPage(
		    new RPRegistralExchangeException(
			RPRegistralExchangeErrorCode.SEND_REG_INTERCHANGE_ERROR_MESSAGE,
			ErrorConstants.SEND_REG_INTERCHANGE_ERROR_MESSAGE, tecDocException), null,
		    null);
	}
	catch (RPRegistralExchangeException rPRegistralExchangeException) {
	    LOG.error(ErrorConstants.SEND_REG_INTERCHANGE_ERROR_MESSAGE +
		". Código: " + rPRegistralExchangeException.getCode() + " . Mensaje: " +
		rPRegistralExchangeException.getMessage() + ". " +
		rPRegistralExchangeException.getCause().getMessage());
	    FacesContext.getCurrentInstance().addMessage(
		null,
		new FacesMessage(
		    FacesMessage.SEVERITY_ERROR, rPRegistralExchangeException.getMessage() +
			". " + rPRegistralExchangeException.getCause().getMessage(),
		    rPRegistralExchangeException.getMessage() +
			". " + rPRegistralExchangeException.getCause().getMessage()));
	}
    }

    /**
     * Obtiene el valor del parámetro searchDestinationRegInterchangeBean.
     * 
     * @return searchDestinationRegInterchangeBean valor del campo a obtener.
     */
    public SearchDestinationRegInterchangeBean getSearchDestinationRegInterchangeBean() {
	return searchDestinationRegInterchangeBean;
    }

    /**
     * Guarda el valor del parámetro searchDestinationRegInterchangeBean.
     * 
     * @param searchDestinationRegInterchangeBean
     *            del campo a guardar.
     */
    public void setSearchDestinationRegInterchangeBean(
	SearchDestinationRegInterchangeBean searchDestinationRegInterchangeBean) {
	this.searchDestinationRegInterchangeBean = searchDestinationRegInterchangeBean;
    }

    /**
     * Obtiene el valor del parámetro registerId.
     * 
     * @return registerId valor del campo a obtener.
     */
    public Integer getRegisterId() {
	return registerId;
    }

    /**
     * Guarda el valor del parámetro registerId.
     * 
     * @param registerId
     *            del campo a guardar.
     */
    public void setRegisterId(
	Integer registerId) {
	this.registerId = registerId;
    }

    /**
     * Obtiene el valor del parámetro idInterchange.
     * 
     * @return idInterchange valor del campo a obtener.
     */
    public String getIdInterchange() {
	return idInterchange;
    }

    /**
     * Guarda el valor del parámetro idInterchange.
     * 
     * @param idInterchange
     *            del campo a guardar.
     */
    public void setIdInterchange(
	String idInterchange) {
	this.idInterchange = idInterchange;
    }

    /**
     * Cambiar entidad.
     * 
     */
    public void changeEntity() {

    }

    /**
     * Método que actualiza el campo destino con el valor seleccionado en la
     * búsqueda avanzada de organismos.
     */
    public void updateDestination() {
	UnidadTramitacionIntercambioRegistralSIRVO unidadTram = null;
	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(
	    "UNITTRAMDIALOG") != null) {
	    unidadTram =
		(UnidadTramitacionIntercambioRegistralSIRVO) FacesContext.getCurrentInstance()
		    .getExternalContext().getSessionMap().get(
			"UNITTRAMDIALOG");
	    getSearchDestinationRegInterchangeBean().setUnidadTramitadoraDestino(
		unidadTram);
	}
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getDocFisica() {
        return docFisica;
    }

    public void setDocFisica(Integer docFisica) {
        this.docFisica = docFisica;
    }
    
    
}
