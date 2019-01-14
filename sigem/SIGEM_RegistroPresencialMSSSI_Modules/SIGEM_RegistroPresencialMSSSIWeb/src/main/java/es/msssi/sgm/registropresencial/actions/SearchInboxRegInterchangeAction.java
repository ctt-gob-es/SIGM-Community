/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.actions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.SessionInformation;
import com.ieci.tecdoc.isicres.web.util.ContextoAplicacionUtil;

import es.ieci.tecdoc.fwktd.sir.core.types.TipoRechazoEnum;
import es.ieci.tecdoc.fwktd.sir.core.vo.AsientoRegistralVO;
import es.ieci.tecdoc.isicres.api.business.vo.BaseOficinaVO;
import es.ieci.tecdoc.isicres.api.business.vo.ContextoAplicacionVO;
import es.ieci.tecdoc.isicres.api.business.vo.OficinaVO;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaEntradaItemVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralSIRVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralVO;
import es.msssi.sgm.registropresencial.businessobject.BooksBo;
import es.msssi.sgm.registropresencial.businessobject.RegInterchangeBo;
import es.msssi.sgm.registropresencial.businessobject.SearchInboxRegInterchangeBo;
import es.msssi.sgm.registropresencial.businessobject.SearchInputSIRBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPBookException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.validations.ValidationBo;

/**
 * Action que gestiona la búsqueda de Intercambio Registral.
 * 
 * @author cmorenog
 * */
public class SearchInboxRegInterchangeAction extends GenericActions implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SearchInboxRegInterchangeAction.class
	.getName());

    /** registro seleccionado . */
    private BandejaEntradaItemVO selectedRegister = null;
    private BandejaEntradaItemVO[] selectedRegisters = null;
    private RegInterchangeBo regInterchangeBo = null;
    private SearchInboxRegInterchangeBo searchInboxRegInterchangeBo;
    /** libro seleccionado. */
    private Integer selectbookByAccept = 1;
    private List<BandejaEntradaItemVO> inBoxList = null;
    private String motReject;
    private AsientoRegistralVO asientoRegistralVO;
    private String observForward;
    private UnidadTramitacionIntercambioRegistralSIRVO unidadTramitadoraDestino;
    /** Implementación de lazydatamodel para gestionar el datatable. */
    private LazyDataModel<BandejaEntradaItemVO> searchInputSIRBo;
    /**
     * Constructor.
     */
    public SearchInboxRegInterchangeAction() {
	regInterchangeBo = new RegInterchangeBo();
	searchInputSIRBo = new SearchInputSIRBo();
    }


    /**
     * acepta los registros seleccionados.
     */
    public void accept() {
	init();
	ContextoAplicacionVO contextoAplicacion = null;
	BaseOficinaVO oficina = null;
	String user = null;
	Integer idOficina = null;
	String codOficina = null;
	try {
	    if (selectedRegisters != null &&
		selectedRegisters.length > 0) {
		contextoAplicacion =
		    ContextoAplicacionUtil
			.getContextoAplicacion((javax.servlet.http.HttpServletRequest) facesContext
			    .getExternalContext().getRequest());
		oficina = contextoAplicacion.getOficinaActual();
		user = contextoAplicacion.getUsuarioActual().getLoginName();

		idOficina = Integer.parseInt(oficina.getId());
		codOficina = ((OficinaVO) oficina).getCodigoOficina();
	    }
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(
		null, sessionException, null);
	}
	catch (TecDocException tecDocException) {
	    LOG.error(
		ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, tecDocException);
	    Utils.redirectToErrorPage(
		null, tecDocException, tecDocException);
	}
	try {
	    regInterchangeBo.accept(
		selectedRegisters, String.valueOf(selectbookByAccept), contextoAplicacion.getUsuarioActual(), idOficina,
		codOficina);
	    clearView();
	}
	catch (RPRegistralExchangeException e) {
	    LOG.error(
		ErrorConstants.ACCEPT_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
	    Utils.redirectToErrorPage(
		e, null, null);
	}
    }

    /**
     * rechazar los registros seleccionados.
     */
    public void rechazar() {
	init();
	ContextoAplicacionVO contextoAplicacion = null;
	BaseOficinaVO oficina = null;
	UsuarioVO user = null;
	Integer idOficina = null;
	String codOficina = null;
	if (motReject == null ||
	    ("").equals(motReject.trim())) {
	    FacesMessage message = new FacesMessage(
		FacesMessage.SEVERITY_INFO, "Validación", "Debe rellenar todos los campos.");
	    RequestContext.getCurrentInstance().showMessageInDialog(
		message);
	}
	else {
	    try {
		contextoAplicacion =
			    ContextoAplicacionUtil
				.getContextoAplicacion((javax.servlet.http.HttpServletRequest) facesContext
				    .getExternalContext().getRequest());
		SessionInformation sessionInformation =
			    (SessionInformation) facesContext.getExternalContext().getSessionMap().get(
				KeysRP.J_SESSIONINF);
		
		oficina = contextoAplicacion.getOficinaActual();
		user = contextoAplicacion.getUsuarioActual();
		user.setUserContact(sessionInformation.getUserContact());
		user.setFullName(sessionInformation.getUserName());
		idOficina = Integer.parseInt(oficina.getId());
		codOficina = ((OficinaVO) oficina).getCodigoOficina();
		}
		catch (SessionException sessionException) {
		    LOG.error(
			ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, sessionException);
		    Utils.redirectToErrorPage(
			null, sessionException, null);
		}
		catch (TecDocException tecDocException) {
		    LOG.error(
			ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, tecDocException);
		    Utils.redirectToErrorPage(
			null, tecDocException, tecDocException);
		}
	    try {
		regInterchangeBo
		    .reject(selectedRegisters, motReject,
			Integer.toString(TipoRechazoEnum.RECHAZO_A_ENTIDAD_REGISTRAL_INICIAL
			    .getValue()), user, idOficina,
				codOficina);
		clearView();
	    }
	    catch (RPRegistralExchangeException e) {
		LOG.error(
		    ErrorConstants.REJECT_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
		Utils.redirectToErrorPage(
		    e, null, null);
	    }
	}
    }

    /**
     * reenviar los registros seleccionados a otro organismo.
     */
    public void reenviar() {
	init();
	ContextoAplicacionVO contextoAplicacion = null;
	BaseOficinaVO oficina = null;
	UsuarioVO user = null;
	Integer idOficina = null;
	String codOficina = null;

	UnidadTramitacionIntercambioRegistralVO unidadTramitacionDestino =
	    new UnidadTramitacionIntercambioRegistralVO();

	if (unidadTramitadoraDestino == null ||
	    observForward == null || "".equals(observForward.trim())) {
	    FacesMessage message = new FacesMessage(
		FacesMessage.SEVERITY_INFO, "Validación", "Debe rellenar todos los campos.");
	    RequestContext.getCurrentInstance().showMessageInDialog(
		message);
	}
	else {
	    unidadTramitacionDestino.setCodeEntity(unidadTramitadoraDestino.getCodeEntity());
	    unidadTramitacionDestino.setNameEntity(unidadTramitadoraDestino.getNameEntity());
	    unidadTramitacionDestino.setCodeTramunit(unidadTramitadoraDestino.getCodeTramunit());
	    unidadTramitacionDestino.setNameTramunit(unidadTramitadoraDestino.getNameTramunit());
	    try {
		contextoAplicacion =
			    ContextoAplicacionUtil
				.getContextoAplicacion((javax.servlet.http.HttpServletRequest) facesContext
				    .getExternalContext().getRequest());
		SessionInformation sessionInformation =
			    (SessionInformation) facesContext.getExternalContext().getSessionMap().get(
				KeysRP.J_SESSIONINF);
		
		oficina = contextoAplicacion.getOficinaActual();
		user = contextoAplicacion.getUsuarioActual();
		user.setUserContact(sessionInformation.getUserContact());
		user.setFullName(sessionInformation.getUserName());

		idOficina = Integer.parseInt(oficina.getId());
		codOficina = ((OficinaVO) oficina).getCodigoOficina();
		}
		catch (SessionException sessionException) {
		    LOG.error(
			ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, sessionException);
		    Utils.redirectToErrorPage(
			null, sessionException, null);
		}
		catch (TecDocException tecDocException) {
		    LOG.error(
			ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, tecDocException);
		    Utils.redirectToErrorPage(
			null, tecDocException, tecDocException);
		}
	    try {
		regInterchangeBo.forward(
		    selectedRegisters, observForward, unidadTramitacionDestino, user, idOficina,
			codOficina);
		clearView();
	    }
	    catch (RPRegistralExchangeException e) {
		LOG.error(
		    ErrorConstants.FORWARD_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
		Utils.redirectToErrorPage(
		    e, null, null);
	    }
	}
    }

    /**
     * Redirigir de la bandeja de entrada a la de salida.
     */
    public void goOutBoxRegInterchange() {
	if ("0".equals(((SearchInputSIRBo)searchInputSIRBo).getSearchbean().getType())) {
	    Utils.navigate("searchOutboxRegInterchange.xhtml");
	}
    }

    /**
     * Método que comprueba si hay seleccionado algún elemento. Si no hay
     * elementos seleccionados muestra un mensaje de error.
     */
    public void isSelect() {
	if (selectedRegisters == null ||
	    selectedRegisters.length == 0) {
	    ValidationBo.showDialog(
		"Validación", new FacesMessage(
		    FacesMessage.SEVERITY_WARN, "", "Debe seleccionar mínimo un registro"));
	}

    }

    /**
     * Reenviar a la página del detalle del registro.
     * 
     * @param event
     *            evento que genera la acción.
     */
    public void onInputRegister(
	SelectEvent event) {
	init();
	BandejaEntradaItemVO bandeja = ((BandejaEntradaItemVO) event.getObject());
	try {
	    clearView();
	    BooksBo booksBo = new BooksBo();

	    ScrRegstate bookID =
		(ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
		    KeysRP.J_BOOK);
	    if (bookID != null &&
		bookID.getIdocarchhdr().getId() != null) {
		booksBo.closeBook(
		    useCaseConf, bookID.getIdocarchhdr().getId());
	    }
	    booksBo.openBook(
		useCaseConf, Integer.valueOf(bandeja.getIdLibro().intValue()));
	    ScrRegstate book = booksBo.getBook(
		useCaseConf.getSessionID(), Integer.valueOf(bandeja.getIdLibro().intValue()));
	    facesContext.getExternalContext().getSessionMap().put(
		KeysRP.J_BOOK, book);

	    FacesContext.getCurrentInstance().getExternalContext().getFlash().put(
		"intercambioR", true);

	    // si es de entrada (1)
	    Map<String, Object> parameter = new HashMap<String, Object>();
	    parameter.put(
		"registerSelect", Integer.valueOf(bandeja.getIdRegistro().intValue()));
	    Utils.navigate(
		parameter, true, "inputRegister.xhtml");

	}
	catch (RPBookException rpBookException) {
	    LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE +
		". Código: " + rpBookException.getCode().getCode() + " . Mensaje: " +
		rpBookException.getShortMessage());
	    Utils.redirectToErrorPage(
		rpBookException, null, null);
	}
	catch (RPGenericException rpGenericException) {
	    LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE +
		". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
		rpGenericException.getShortMessage());
	    Utils.redirectToErrorPage(
		rpGenericException, null, null);
	}
    }

    /**
     * Devuelve la información de un movimiento registral.
     */
    public void moreInfo() {
	if (selectedRegister != null) {
	    try {
		asientoRegistralVO =
		    regInterchangeBo.getInterchange(String.valueOf(selectedRegister
			.getIdIntercambioInterno()));
	    }
	    catch (RPRegistralExchangeException e) {
		LOG.error(
		    ErrorConstants.GET_INPUT_INTERCHANGE_ERROR_MESSAGE, e);
		Utils.redirectToErrorPage(
		    e, null, null);
	    }
	}
    }

 
    /**
     * Obtiene el valor del parámetro inBoxList.
     * 
     * @return inBoxList valor del campo a obtener.
     */
    public List<BandejaEntradaItemVO> getInBoxList() {
	return inBoxList;
    }

    /**
     * Guarda el valor del parámetro inBoxList.
     * 
     * @param inBoxList
     *            valor del campo a guardar.
     */
    public void setInBoxList(
	List<BandejaEntradaItemVO> inBoxList) {
	this.inBoxList = inBoxList;
    }

    /**
     * Obtiene el valor del parámetro selectedRegister.
     * 
     * @return selectedRegister valor del campo a obtener.
     */
    public BandejaEntradaItemVO getSelectedRegister() {
	return selectedRegister;
    }

    /**
     * Guarda el valor del parámetro selectedRegister.
     * 
     * @param selectedRegister
     *            valor del campo a guardar.
     */
    public void setSelectedRegister(
	BandejaEntradaItemVO selectedRegister) {
	this.selectedRegister = selectedRegister;
    }

    /**
     * Obtiene el valor del parámetro selectedRegisters.
     * 
     * @return selectedRegisters valor del campo a obtener.
     */
    public BandejaEntradaItemVO[] getSelectedRegisters() {
	return selectedRegisters;
    }

    /**
     * Guarda el valor del parámetro selectedRegisters.
     * 
     * @param selectedRegisters
     *            valor del campo a guardar.
     */
    public void setSelectedRegisters(
	BandejaEntradaItemVO[] selectedRegisters) {
	this.selectedRegisters = selectedRegisters;
    }

    /**
     * Obtiene el valor del parámetro selectbookByAccept.
     * 
     * @return selectbookByAccept valor del campo a obtener.
     */
    public Integer getSelectbookByAccept() {
	return selectbookByAccept;
    }

    /**
     * Guarda el valor del parámetro selectbookByAccept.
     * 
     * @param selectbookByAccept
     *            valor del campo a guardar.
     */
    public void setSelectbookByAccept(
	Integer selectbookByAccept) {
	this.selectbookByAccept = selectbookByAccept;
    }

    /**
     * Obtiene el valor del parámetro searchInboxRegInterchangeBo.
     * 
     * @return searchInboxRegInterchangeBo valor del campo a obtener.
     */
    public SearchInboxRegInterchangeBo getSearchInboxRegInterchangeBo() {
	return searchInboxRegInterchangeBo;
    }

    /**
     * Guarda el valor del parámetro searchInboxRegInterchangeBo.
     * 
     * @param searchInboxRegInterchangeBo
     *            valor del campo a guardar.
     */
    public void setSearchInboxRegInterchangeBo(
	SearchInboxRegInterchangeBo searchInboxRegInterchangeBo) {
	this.searchInboxRegInterchangeBo = searchInboxRegInterchangeBo;
    }

    /**
     * Obtiene el valor del parámetro motReject.
     * 
     * @return motReject valor del campo a obtener.
     */
    public String getMotReject() {
	return motReject;
    }

    /**
     * Guarda el valor del parámetro motReject.
     * 
     * @param motReject
     *            valor del campo a guardar.
     */
    public void setMotReject(
	String motReject) {
	this.motReject = motReject;
    }

    /**
     * Obtiene el valor del parámetro asientoRegistralVO.
     * 
     * @return asientoRegistralVO valor del campo a obtener.
     */
    public AsientoRegistralVO getAsientoRegistralVO() {
	return asientoRegistralVO;
    }

    /**
     * Guarda el valor del parámetro asientoRegistralVO.
     * 
     * @param asientoRegistralVO
     *            valor del campo a guardar.
     */
    public void setAsientoRegistralVO(
	AsientoRegistralVO asientoRegistralVO) {
	this.asientoRegistralVO = asientoRegistralVO;
    }

    /**
     * Obtiene el valor del parámetro observForward.
     * 
     * @return observForward valor del campo a obtener.
     */
    public String getObservForward() {
	return observForward;
    }

    /**
     * Guarda el valor del parámetro observForward.
     * 
     * @param observForward
     *            valor del campo a guardar.
     */
    public void setObservForward(
	String observForward) {
	this.observForward = observForward;
    }

    /**
     * Obtiene el valor del parámetro unidadTramitadoraDestino.
     * 
     * @return unidadTramitadoraDestino valor del campo a obtener.
     */
    public UnidadTramitacionIntercambioRegistralSIRVO getUnidadTramitadoraDestino() {
	return unidadTramitadoraDestino;
    }

    /**
     * Guarda el valor del parámetro unidadTramitadoraDestino.
     * 
     * @param unidadTramitadoraDestino
     *            valor del campo a guardar.
     */
    public void setUnidadTramitadoraDestino(
	UnidadTramitacionIntercambioRegistralSIRVO unidadTramitadoraDestino) {
	this.unidadTramitadoraDestino = unidadTramitadoraDestino;
    }

    /**
     * Guarda la entidad nueva a la que se reenvía el movimiento registral.
     */
    public void changeEntity() {

    }
    /**
     * Método que actualiza el campo destino con el valor seleccionado en la
     * búsqueda avanzada de organismos.
     */
    public void updateDestinationDig() {
	UnidadTramitacionIntercambioRegistralSIRVO unidadTram = null;
	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(
	    "UNITTRAMDIALOG") != null) {
	    unidadTram =
		(UnidadTramitacionIntercambioRegistralSIRVO) FacesContext.getCurrentInstance()
		    .getExternalContext().getSessionMap().get(
			"UNITTRAMDIALOG");
	    setUnidadTramitadoraDestino(unidadTram);
	}
    }


    public LazyDataModel<BandejaEntradaItemVO> getSearchInputSIRBo() {
        return searchInputSIRBo;
    }


    public void setSearchInputSIRBo(LazyDataModel<BandejaEntradaItemVO> searchInputSIRBo) {
        this.searchInputSIRBo = searchInputSIRBo;
    }
    
}