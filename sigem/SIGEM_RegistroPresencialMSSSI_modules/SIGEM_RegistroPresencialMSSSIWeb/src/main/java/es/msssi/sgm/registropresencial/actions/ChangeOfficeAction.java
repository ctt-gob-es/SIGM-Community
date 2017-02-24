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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

import com.ieci.tecdoc.common.exception.AttributesException;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.isicres.SessionInformation;
import com.ieci.tecdoc.common.keys.ServerKeys;
import com.ieci.tecdoc.common.utils.ISicresGenPerms;
import com.ieci.tecdoc.isicres.session.book.BookSession;
import com.ieci.tecdoc.isicres.session.validation.ValidationSessionEx;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.msssi.sgm.registropresencial.businessobject.ChangeOfficeBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.validations.ValidationBo;

/**
 * Action para el cambio de oficina.
 * 
 * @author jortizs
 */
public class ChangeOfficeAction extends GenericActions {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ChangeOfficeAction.class);
    private List<ScrOfic> listOtherOffice;
    private ScrOfic selectedOffice;
    private ChangeOfficeBo changeOfficeBo;

    /**
     * Se crea el listado de oficinas a las que el usuario puede cambiar.
     */
    @SuppressWarnings("unchecked")
    @PostConstruct
    public void create() {
	LOG.trace("Entrando en ChangeOfficeAction.create()");
	init();
	try {
	    changeOfficeBo = new ChangeOfficeBo();
	    listOtherOffice = ValidationSessionEx.getOtherOffices(
		useCaseConf.getSessionID(), useCaseConf.getLocale(), useCaseConf.getEntidadId());
	}
	// Si falla, redireccionamos a la página de error
	catch (AttributesException attributesException) {
	    LOG.error(
		ErrorConstants.GET_OFFICES_ERROR_MESSAGE, attributesException);
	    Utils.redirectToErrorPage(
		null, attributesException, null);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_OFFICES_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(
		null, sessionException, null);
	}
	catch (ValidationException validationException) {
	    LOG.error(
		ErrorConstants.GET_OFFICES_ERROR_MESSAGE, validationException);
	    Utils.redirectToErrorPage(
		null, validationException, null);
	}
    }

    /**
     * Action listener para cambio de oficina.
     */
    public void change() {
	LOG.trace("Entrando en ChangeOfficeAction.change()");
	init();
	if (selectedOffice != null) {
	    try {
		changeOfficeBo.validateOfficeCode(
		    useCaseConf, selectedOffice.getCode());
		CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
		    useCaseConf.getSessionID());
		ISicresGenPerms permisos = (ISicresGenPerms) cacheBag.get(ServerKeys.GENPERMS_USER);

		SessionInformation sessionInformation =
		    BookSession.getSessionInformation(
			useCaseConf.getSessionID(), useCaseConf.getLocale(),
			useCaseConf.getEntidadId());

		((HttpServletRequest) facesContext.getExternalContext().getRequest()).getSession()
		    .setAttribute(
			KeysRP.J_SESSIONINF, sessionInformation);
		((HttpServletRequest) facesContext.getExternalContext().getRequest()).getSession()
		    .setAttribute(
			KeysRP.J_PERMSUSER, permisos);
		((HttpServletRequest) facesContext.getExternalContext().getRequest()).getSession()
		    .removeAttribute(
			"validationListAction");
		Utils.navigate("inicio.xhtml");
	    }
	    // Si falla, redireccionamos a la página de error
	    catch (BookException bookException) {
		LOG.error(
		    ErrorConstants.GET_SESSION_INFORMATION_BOOK_ERROR_MESSAGE, bookException);
		Utils.redirectToErrorPage(
		    null, bookException, null);
	    }
	    catch (TecDocException tecDocException) {
		LOG.error(
		    ErrorConstants.GET_SESSION_INFORMATION_BOOK_ERROR_MESSAGE, tecDocException);
		Utils.redirectToErrorPage(
		    null, tecDocException, null);
	    }
	    catch (RPGenericException rpGenericException) {
		LOG.error(ErrorConstants.VALIDATION_OFFICE_CODE_ERROR_MESSAGE +
		    ". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
		    rpGenericException.getShortMessage());
		Utils.redirectToErrorPage(
		    rpGenericException, null, null);
	    }
	}
	else {
	    ValidationBo.showDialog(
		"Cambiar Oficina", new FacesMessage(
		    FacesMessage.SEVERITY_WARN, "", "Debe seleccionar una oficina"));
	}
    }

    /**
     * Obtiene el valor del parámetro selectedOffice.
     * 
     * @return selectedOffice valor del campo a obtener.
     */
    public ScrOfic getSelectedOffice() {
	return selectedOffice;
    }

    /**
     * Guarda el valor del parámetro selectedOffice.
     * 
     * @param selectedOffice
     *            valor del campo a guardar.
     */
    public void setSelectedOffice(
	ScrOfic selectedOffice) {
	this.selectedOffice = selectedOffice;
    }

    /**
     * Obtiene el valor del parámetro listOtherOffice.
     * 
     * @return listOtherOffice valor del campo a obtener.
     */
    public List<ScrOfic> getListOtherOffice() {
	return listOtherOffice;
    }

    /**
     * Guarda el valor del parámetro listOtherOffice.
     * 
     * @param listOtherOffice
     *            valor del campo a guardar.
     */
    public void setListOtherOffice(
	List<ScrOfic> listOtherOffice) {
	this.listOtherOffice = listOtherOffice;
    }
    
    /**
     * Redirecciona a la página de edición desde el datatable.
     * 
     * @param event
     *            evento que se ha lanzado.
     */
    public void onRowSelectNavigateInit(
	SelectEvent event) {
	selectedOffice = ((ScrOfic) event.getObject());
	change();
    }
}