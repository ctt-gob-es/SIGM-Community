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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.event.data.SortEvent;
import org.primefaces.model.LazyDataModel;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesdoc.Iuserdepthdr;
import com.ieci.tecdoc.common.invesdoc.Iusergrouphdr;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.keys.ISicresKeys;

import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.RowSearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.businessobject.DistributionBo;
import es.msssi.sgm.registropresencial.businessobject.InputRegisterBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterDocumentsBo;
import es.msssi.sgm.registropresencial.businessobject.SearchInputRegisterBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPDistributionException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPInputRegisterException;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.validations.ValidationBo;
import es.msssi.sgm.registropresencial.validations.ValidationListBo;

/**
 * Action que gestiona la búsqueda de registros de entrada.
 * 
 * @author cmorenog
 */
public class SearchInputRegisterFormAction extends GenericActions implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SearchInputRegisterFormAction.class
	    .getName());

    /** Fila del datatable que se ha seleccionado. */
    private RowSearchInputRegisterBean selectedResult;
    /** Filas del datatable que se han seleccionado. */
    private RowSearchInputRegisterBean[] selectedResults;
    /** Implementación de lazydatamodel para gestionar el datatable. */
    private LazyDataModel<RowSearchInputRegisterBean> inputRegisterBo;
    /** Implementación de bo de registros de entrada. */
    private RegisterBo registerBo;
    // DISTRIBUCION
    /** tipo de destino seleccionado en la distribución. */
    private int typeDestinoRedis = 2;
    /** Motivo de la distribución. */
    private String motivoDistribucion;
    /** departamento seleccionado en la distribución. */
    private Iuserdepthdr selectdestinoRedisDepartamentos = null;
    /** grupo seleccionado en la distribución. */
    private Iusergrouphdr selectdestinoRedisGrupos = null;
    /** Implementación de las funcionalidades de distribución. */
    private DistributionBo distributionBo;
    private List<Axdoch> listDocuments;
    private RegisterDocumentsBo registerDocumentsBo;
    /** libro destino al que copiar **/
    private Integer bookDestination;
    /** lista de departamentos para la distribución. */
    private List<Iuserdepthdr> listDepartament = null;
    /**
     * Constructor.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public SearchInputRegisterFormAction() throws SessionException {
	LOG.trace("Entrando en el constructor de SearchInputRegisterFormAction");
	inputRegisterBo = new SearchInputRegisterBo();
	init();
	ScrRegstate book =
		(ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
	if (book == null) {
	    throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	}
	try {
	    validateIdBook(book.getId(), useCaseConf);
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.GET_INFORMATION_BOOK_ERROR_MESSAGE, bookException);
	    Utils.redirectToErrorPage(null, bookException, null);
	}
    }

    /**
     * Limpia el formulario y la tabla de resultados.
     */
    public void limpiar() {
	init();
	((SearchInputRegisterBo) inputRegisterBo).closeQuery();
	((SearchInputRegisterBo) inputRegisterBo).openQuery();
	clearView();
    }

    /**
     * Actualiza la páginación y la mapea con la de SIGM.
     * 
     * @param event
     *            Evento de paginación que se ha lanzado.
     */
    public void pageButton(PageEvent event) {
	((SearchInputRegisterBo) inputRegisterBo).setPage(event.getPage()); 
    }

    /**
     * Actualiza la ordenación y la mapea con la de SIGM.
     * 
     * @param event
     *            Evento de ordenación que se ha lanzado.
     */
    public void sortButton(SortEvent event) {
	((SearchInputRegisterBo) inputRegisterBo).setPage(0);
    }

    /**
     * Inicializa la consulta en SIGM.
     */
    public void initRegisterQuery() {
	((SearchInputRegisterBo) inputRegisterBo).setLastRegister(false);
	((SearchInputRegisterBo) inputRegisterBo).closeQuery();
	((SearchInputRegisterBo) inputRegisterBo).openQuery();
    }

    /**
     * Busca según el último registro realizado.
     */
    public void lastRegister() {
	((SearchInputRegisterBo) inputRegisterBo)
		.setSearchInputRegister(new SearchInputRegisterBean());
	((SearchInputRegisterBo) inputRegisterBo).setLastRegister(true);
    }

    /**
     * Redirecciona a la página de edición desde el datatable.
     * 
     * @param event
     *            evento que se ha lanzado.
     */
    public void onRowSelectNavigateEdit(SelectEvent event) {
	RowSearchInputRegisterBean row = ((RowSearchInputRegisterBean) event.getObject());
	Map<String, Object> parameter = new HashMap<String, Object>();
	parameter.put("registerSelect", row.getFdrid());
	Utils.navigate(parameter, false, "inputRegister.xhtml");
    }

    /**
     * Redirecciona a la página de consulta desde el datatable.
     * 
     * @param event
     *            evento que se ha lanzado.
     */
    public void onRowSelectNavigateView(SelectEvent event) {
	RowSearchInputRegisterBean row = ((RowSearchInputRegisterBean) event.getObject());
	Map<String, Object> parameter = new HashMap<String, Object>();
	parameter.put("registerSelect", row.getFdrid());
	Utils.navigate(parameter, true, "inputRegister.xhtml");
    }

    /**
     * Redirecciona a la página de edición desde el botón.
     */
    public void onRowSelectNavigateBottomEdit() {
	if (selectedResults != null && selectedResults.length == 1) {
	    Map<String, Object> parameter = new HashMap<String, Object>();
	    parameter.put("registerSelect", selectedResults[0].getFdrid());
	    Utils.navigate(parameter, false, "inputRegister.xhtml");
	}
	else {
	    ValidationBo.showDialog("Editar", new FacesMessage(FacesMessage.SEVERITY_WARN, "",
		    "Debe seleccionar un único registro"));
	}
    }

    /**
     * Redirecciona a la página de consulta desde el botón.
     */
    public void onRowSelectNavigateBottomView() {
	if (selectedResults != null && selectedResults.length == 1) {
	    Map<String, Object> parameter = new HashMap<String, Object>();
	    parameter.put("registerSelect", selectedResults[0].getFdrid());
	    Utils.navigate(parameter, true, "inputRegister.xhtml");
	}
	else {
	    ValidationBo.showDialog("Abrir", new FacesMessage(FacesMessage.SEVERITY_WARN, "",
		    "Debe seleccionar un único registro"));
	}
    }

    /**
     * Redirecciona a la página de copiar desde el botón.
     */
    public void onRowSelectNavigateBottomCopy() {
	if (selectedResults != null && selectedResults.length == 1) {
	    FacesContext.getCurrentInstance().getExternalContext().getFlash().clear();
	    Map<String, Object> parameter = new HashMap<String, Object>();
	    parameter.put("registerCopy", selectedResults[0].getFdrid());
	    parameter.put("registerNumCopy", selectedResults[0].getFld1());
	    Utils.navigate(parameter, false, "inputRegister.xhtml");
	}
	else {
	    ValidationBo.showDialog("Copiar", new FacesMessage(FacesMessage.SEVERITY_WARN, "",
		    "Debe seleccionar un único registro"));
	}
    }

    /**
     * Redirecciona a la página de nuevo desde el botón.
     */
    public void onRowSelectNavigateBottomNuevo() {
	FacesContext.getCurrentInstance().getExternalContext().getFlash().remove("registerCopy");
	FacesContext.getCurrentInstance().getExternalContext().getFlash().remove("registerSelect");
	FacesContext.getCurrentInstance().getExternalContext().getFlash().remove("readOnly");
    }

    /**
     * Devuelve el inputRegisterBo.
     * 
     * @return inputRegisterBo el objeto inputRegisterBo.
     */
    public LazyDataModel<RowSearchInputRegisterBean> getInputRegisterBo() {
	return inputRegisterBo;
    }

    /**
     * Recibe el objeto inputRegisterBo.
     * 
     * @param inputRegisterBo
     *            el objeto inputRegisterBo
     */
    public void setInputRegisterBo(LazyDataModel<RowSearchInputRegisterBean> inputRegisterBo) {
	this.inputRegisterBo = inputRegisterBo;
    }

    /**
     * Devuelve el objeto seleccionado del datatable.
     * 
     * @return selectedResult El objeto seleccionado.
     */
    public RowSearchInputRegisterBean getSelectedResult() {
	return selectedResult;
    }

    /**
     * Recibe el objecto seleccionado del datatable.
     * 
     * @param selectedResult
     *            El objeto seleccionado.
     */
    public void setSelectedResult(RowSearchInputRegisterBean selectedResult) {
	this.selectedResult = selectedResult;
    }

    /**
     * Devuelve los objetos seleccionados del datatable.
     * 
     * @return selectedResults El array con los objetos seleccionados.
     */
    public RowSearchInputRegisterBean[] getSelectedResults() {
	return selectedResults;
    }

    /**
     * Recibe los objectos seleccionados del datatable.
     * 
     * @param selectedResults
     *            El array de objetos seleccionados.
     */
    public void setSelectedResults(RowSearchInputRegisterBean[] selectedResults) {
	this.selectedResults = selectedResults;
    }

    /**
     * Devuelve del objeto seleccionado de la tabla su identificador.
     * 
     * @return fdrId selectedResults[0].getFdrid() El identificador del objeto.
     */
    public Integer selectResultId() {
	Integer fdrId = null;
	if (selectedResults == null || selectedResults.length == 0) {
	    fdrId = null;
	}
	else {
	    fdrId = selectedResults[0].getFdrid();
	}
	return fdrId;
    }

    /**
     * Obtiene el valor del parámetro motivoDistribucion.
     * 
     * @return motivoDistribucion valor del campo a obtener.
     */
    public String getMotivoDistribucion() {
	return motivoDistribucion;
    }

    /**
     * Guarda el valor del parámetro motivoDistribucion.
     * 
     * @param motivoDistribucion
     *            valor del campo a guardar.
     */
    public void setMotivoDistribucion(String motivoDistribucion) {
	this.motivoDistribucion = motivoDistribucion;
    }

    /**
     * Obtiene el valor del parámetro selectdestinoRedisDepartamentos.
     * 
     * @return selectdestinoRedisDepartamentos valor del campo a obtener.
     */
    public Iuserdepthdr getSelectdestinoRedisDepartamentos() {
	return selectdestinoRedisDepartamentos;
    }

    /**
     * Guarda el valor del parámetro selectdestinoRedisDepartamentos.
     * 
     * @param selectdestinoRedisDepartamentos
     *            valor del campo a guardar.
     */
    public void setSelectdestinoRedisDepartamentos(Iuserdepthdr selectdestinoRedisDepartamentos) {
	this.selectdestinoRedisDepartamentos = selectdestinoRedisDepartamentos;
    }

    /**
     * Método que distribuye de los elementos seleccionados.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public void distribuir() throws SessionException {
	init();
	ScrRegstate book =
		(ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
	if (book == null) {
	    throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	}
	if (distributionBo == null) {
	    distributionBo = new DistributionBo();
	}
	try {

	    Integer userId = selectdestinoRedisDepartamentos.getId();
	    List<Integer> ids = new ArrayList<Integer>();
	    for (RowSearchInputRegisterBean row : selectedResults) {
		ids.add(row.getFdrid());
	    }
	    String result =
		    distributionBo.createDistribution(useCaseConf, ids, motivoDistribucion,
			    typeDestinoRedis, userId, book.getId());
	    if ("".equals(result.trim())) {
		result = "Se han distribuido los registros correctamente.";
	    }
	    result = result.replaceAll(";", "<br/>");
	    FacesMessage message =
		    new FacesMessage(FacesMessage.SEVERITY_INFO, "Distribución", result);
	    RequestContext.getCurrentInstance().showMessageInDialog(message);
	}
	catch (RPDistributionException e) {
	    LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
	    Utils.redirectToErrorPage(e, null, null);
	}
	catch (RPGenericException e) {
	    LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
	    Utils.redirectToErrorPage(e, null, null);
	}
    }

    /**
     * Método que comprueba si hay seleccionado algún elemento. Si no hay
     * elementos seleccionados muestra un mensaje de error.
     */
    public void isSelect() {
	if (selectedResults == null || selectedResults.length == 0) {
	    ValidationBo.showDialog("", new FacesMessage(FacesMessage.SEVERITY_WARN, "",
		    "Debe seleccionar mínimo un registro"));
	}
    }

    /**
     * Método abre los registros seleccionados que estén cerrados.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public void openReg() throws SessionException {
	registerBo = new RegisterBo();
	if (selectedResults != null && selectedResults.length > 0) {
	    init();
	    ScrRegstate book =
		    (ScrRegstate) facesContext.getExternalContext().getSessionMap()
			    .get(KeysRP.J_BOOK);
	    if (book == null) {
		throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	    }
	    try {
		List<Integer> list = new ArrayList<Integer>();
		List<String> listOut = new ArrayList<String>();
		for (RowSearchInputRegisterBean row : selectedResults) {
		    if (row.getFld6().equals(
			    String.valueOf(ISicresKeys.SCR_ESTADO_REGISTRO_CERRADO))) {
			list.add(row.getFdrid());
		    }
		    else {
			listOut.add(row.getFld1());
		    }
		}
		registerBo.openRegisters(useCaseConf, book.getId(), list);

		if (listOut.size() > 0) {
		    StringBuilder result =
			    new StringBuilder("No se han podido abrir los siguientes registros:");
		    for (String id : listOut) {
			result.append(" " + id);
		    }
		    FacesMessage message =
			    new FacesMessage(FacesMessage.SEVERITY_INFO, "Abrir registros",
				    result.toString());
		    RequestContext.getCurrentInstance().showMessageInDialog(message);
		}
	    }
	    catch (RPRegisterException e) {
		LOG.error(ErrorConstants.OPENR_INPUT_REGISTER_ERROR_MESSAGE, e);
		Utils.redirectToErrorPage(e, null, null);
	    }
	    catch (RPGenericException e) {
		LOG.error(ErrorConstants.OPENR_INPUT_REGISTER_ERROR_MESSAGE, e);
		Utils.redirectToErrorPage(e, null, null);
	    }

	}
	else {
	    ValidationBo.showDialog("Abrir", new FacesMessage(FacesMessage.SEVERITY_WARN, "",
		    "Debe seleccionar mínimo un registro"));
	}
    }

    /**
     * Método que cierra los registros seleccionados.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public void closeReg() throws SessionException {
	registerBo = new RegisterBo();
	if (selectedResults != null && selectedResults.length > 0) {
	    init();
	    ScrRegstate book =
		    (ScrRegstate) FacesContext.getCurrentInstance().getExternalContext()
			    .getSessionMap().get(KeysRP.J_BOOK);
	    if (book == null) {
		throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	    }
	    try {
		List<Integer> list = new ArrayList<Integer>();
		List<String> listOut = new ArrayList<String>();
		for (RowSearchInputRegisterBean row : selectedResults) {
		    if (row.getFld6().equals(
			    String.valueOf(ISicresKeys.SCR_ESTADO_REGISTRO_COMPLETO))) {
			list.add(row.getFdrid());
		    }
		    else {
			listOut.add(row.getFld1());
		    }
		}
		registerBo.closeRegisters(useCaseConf, book.getId(), list);
		if (listOut.size() > 0) {
		    String result = "No se han podido cerrar los siguientes registros:";
		    for (String id : listOut) {
			result += " " + id;
		    }
		    FacesMessage message =
			    new FacesMessage(FacesMessage.SEVERITY_INFO, "Cerrar Registros", result);
		    RequestContext.getCurrentInstance().showMessageInDialog(message);
		}
	    }
	    catch (RPRegisterException e) {
		LOG.error(ErrorConstants.CLOSE_INPUT_REGISTER_ERROR_MESSAGE, e);
		Utils.redirectToErrorPage(e, null, null);
	    }
	    catch (RPGenericException e) {
		LOG.error(ErrorConstants.CLOSE_INPUT_REGISTER_ERROR_MESSAGE, e);
		Utils.redirectToErrorPage(e, null, null);
	    }
	}
	else {
	    ValidationBo.showDialog("Cerrar", new FacesMessage(FacesMessage.SEVERITY_WARN,
		    "Cerrar", "Debe seleccionar mínimo un registro"));
	}
    }

    /**
     * Método que actualiza el campo origen con el valor seleccionado en la
     * búsqueda avanzada de organismos.
     */
    public void updateOrigin() {
	ScrOrg unidad = null;
	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		.get("UNITSDIALOG") != null) {
	    unidad =
		    (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			    .get("UNITSDIALOG");
	    ((SearchInputRegisterBo) inputRegisterBo).getSearchInputRegister().setFld7Value(unidad);
	}
    }

    /**
     * Método que actualiza el campo destino con el valor seleccionado en la
     * búsqueda avanzada de organismos.
     */
    public void updateDestination() {
	ScrOrg unidad = null;
	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		.get("UNITSDIALOG") != null) {
	    unidad =
		    (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			    .get("UNITSDIALOG");
	    ((SearchInputRegisterBo) inputRegisterBo).getSearchInputRegister().setFld8Value(unidad);
	}
    }

    /**
     * Devuelve la lista de documentos adjuntos.
     */
    public void viewDoc() {
	if (selectedResult != null) {
	    ScrRegstate book =
		    (ScrRegstate) FacesContext.getCurrentInstance().getExternalContext()
			    .getSessionMap().get(KeysRP.J_BOOK);
	    listDocuments = new ArrayList<Axdoch>();
	    if (registerDocumentsBo == null) {
		registerDocumentsBo = new RegisterDocumentsBo();
	    }
	    try {
		LOG.info("Cargando documentos asociados al registro " + selectedResult.getFdrid());
		listDocuments =
			registerDocumentsBo.getDocumentsBasicInfo(useCaseConf, book.getId(),
				selectedResult.getFdrid(), false);
	    }
	    catch (RPRegisterException rPRegisterException) {
		LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, rPRegisterException);
	    }
	}
    }

    /**
     * Método que marca como impreso los elementos seleccionados.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public void impreso() throws SessionException {
	init();
	ScrRegstate book =
		(ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
	if (book == null) {
	    throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	}
	InputRegisterBean inputRegisterBean = null;
	for (RowSearchInputRegisterBean selectedResultBean : selectedResults) {
	    try {
		if (!("S".equals(selectedResultBean.getFld1001()))) {
		    inputRegisterBean = new InputRegisterBean();
		    inputRegisterBean.setFdrid(selectedResultBean.getFdrid());
		    inputRegisterBean.setFld1(selectedResultBean.getFld1());
		    inputRegisterBean.setFld1001("S");
		    if (selectedResultBean.getFld9() != null && !"".equals(selectedResultBean.getFld9().trim())){
			List<Interesado> interesados = new ArrayList<Interesado>();
			Interesado interesado = new Interesado();
			interesado.setNombre(selectedResultBean.getFld9());
			interesados.add(interesado);
			inputRegisterBean.setInteresados(interesados );
		    }
		    InputRegisterBo inputRegisterBeanBo = new InputRegisterBo();
		    inputRegisterBeanBo.updateOnlyFolder(useCaseConf, book.getId(),
			    selectedResultBean.getFdrid(),inputRegisterBean);
		}

	    }
	    catch (RPInputRegisterException rpInputRegisterException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpInputRegisterException.getCode().getCode() + " . Mensaje: "
			+ rpInputRegisterException.getShortMessage());
		Utils.redirectToErrorPage(rpInputRegisterException, null, null);
	    }
	    catch (RPGenericException rpGenericException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpGenericException.getCode().getCode() + " . Mensaje: "
			+ rpGenericException.getShortMessage());
		Utils.redirectToErrorPage(rpGenericException, null, null);
	    }
	}
    }

    /**
     * Método que marca como no impreso los elementos seleccionados.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public void noImpreso() throws SessionException {
	init();
	ScrRegstate book =
		(ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
	if (book == null) {
	    throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	}
	InputRegisterBean inputRegisterBean = null;
	for (RowSearchInputRegisterBean selectedResultBean : selectedResults) {
	    try {
		if (!("N".equals(selectedResultBean.getFld1001()))) {
		    inputRegisterBean = new InputRegisterBean();
		    inputRegisterBean.setFdrid(selectedResultBean.getFdrid());
		    inputRegisterBean.setFld1(selectedResultBean.getFld1());
		    inputRegisterBean.setFld1001("N");
		    if (selectedResultBean.getFld9() != null && !"".equals(selectedResultBean.getFld9().trim())){
			List<Interesado> interesados = new ArrayList<Interesado>();
			Interesado interesado = new Interesado();
			interesado.setNombre(selectedResultBean.getFld9());
			interesados.add(interesado);
			inputRegisterBean.setInteresados(interesados );
		    }
		    InputRegisterBo inputRegisterBeanBo = new InputRegisterBo();
		    inputRegisterBeanBo.updateOnlyFolder(useCaseConf, book.getId(),
			    selectedResultBean.getFdrid(), inputRegisterBean);
		}

	    }
	    catch (RPInputRegisterException rpInputRegisterException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpInputRegisterException.getCode().getCode() + " . Mensaje: "
			+ rpInputRegisterException.getShortMessage());
		Utils.redirectToErrorPage(rpInputRegisterException, null, null);
	    }
	    catch (RPGenericException rpGenericException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpGenericException.getCode().getCode() + " . Mensaje: "
			+ rpGenericException.getShortMessage());
		Utils.redirectToErrorPage(rpGenericException, null, null);
	    }
	}
    }

    /**
     * Método que marca como repetidos los elementos seleccionados.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public void repetido() throws SessionException {
	init();
	ScrRegstate book =
		(ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
	if (book == null) {
	    throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	}
	InputRegisterBean inputRegisterBean = null;
	for (RowSearchInputRegisterBean selectedResultBean : selectedResults) {
	    try {
		if (!("1".equals(selectedResultBean.getFld1002()))) {
		    inputRegisterBean = new InputRegisterBean();
		    inputRegisterBean.setFdrid(selectedResultBean.getFdrid());
		    inputRegisterBean.setFld1(selectedResultBean.getFld1());
		    inputRegisterBean.setFld1002(1);
		    if (selectedResultBean.getFld9() != null && !"".equals(selectedResultBean.getFld9().trim())){
			List<Interesado> interesados = new ArrayList<Interesado>();
			Interesado interesado = new Interesado();
			interesado.setNombre(selectedResultBean.getFld9());
			interesados.add(interesado);
			inputRegisterBean.setInteresados(interesados );
		    }
		    InputRegisterBo inputRegisterBeanBo = new InputRegisterBo();
		    inputRegisterBeanBo.updateOnlyFolder(useCaseConf, book.getId(),
			    selectedResultBean.getFdrid(),inputRegisterBean);
		}

	    }
	    catch (RPInputRegisterException rpInputRegisterException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpInputRegisterException.getCode().getCode() + " . Mensaje: "
			+ rpInputRegisterException.getShortMessage());
		Utils.redirectToErrorPage(rpInputRegisterException, null, null);
	    }
	    catch (RPGenericException rpGenericException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpGenericException.getCode().getCode() + " . Mensaje: "
			+ rpGenericException.getShortMessage());
		Utils.redirectToErrorPage(rpGenericException, null, null);
	    }
	}
    }
    
    /**
     * Método que marca como no repetido los elementos seleccionados.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public void noRepetido() throws SessionException {
	init();
	ScrRegstate book =
		(ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
	if (book == null) {
	    throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	}
	InputRegisterBean inputRegisterBean = null;
	for (RowSearchInputRegisterBean selectedResultBean : selectedResults) {
	    try {
		if (selectedResultBean.getFld1002() != null) {
		    inputRegisterBean = new InputRegisterBean();
		    inputRegisterBean.setFdrid(selectedResultBean.getFdrid());
		    inputRegisterBean.setFld1(selectedResultBean.getFld1());
		    inputRegisterBean.setFld1002(1);
		    inputRegisterBean.setFld1002(null);
		    if (selectedResultBean.getFld9() != null && !"".equals(selectedResultBean.getFld9().trim())){
			List<Interesado> interesados = new ArrayList<Interesado>();
			Interesado interesado = new Interesado();
			interesado.setNombre(selectedResultBean.getFld9());
			interesados.add(interesado);
			inputRegisterBean.setInteresados(interesados );
		    }
		    InputRegisterBo inputRegisterBeanBo = new InputRegisterBo();
		    inputRegisterBeanBo.updateOnlyFolder(useCaseConf, book.getId(),
			    selectedResultBean.getFdrid(), inputRegisterBean);
		}

	    }
	    catch (RPInputRegisterException rpInputRegisterException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpInputRegisterException.getCode().getCode() + " . Mensaje: "
			+ rpInputRegisterException.getShortMessage());
		Utils.redirectToErrorPage(rpInputRegisterException, null, null);
	    }
	    catch (RPGenericException rpGenericException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpGenericException.getCode().getCode() + " . Mensaje: "
			+ rpGenericException.getShortMessage());
		Utils.redirectToErrorPage(rpGenericException, null, null);
	    }
	}
    }
    
    /**
     * Método que marca como borrado los elementos seleccionados.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public void borrado() throws SessionException {
	init();
	ScrRegstate book =
		(ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
	if (book == null) {
	    throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	}
	InputRegisterBean inputRegisterBean = null;
	for (RowSearchInputRegisterBean selectedResultBean : selectedResults) {
	    try {
		if (!("2".equals(selectedResultBean.getFld1002()))) {
		    inputRegisterBean = new InputRegisterBean();
		    inputRegisterBean.setFdrid(selectedResultBean.getFdrid());
		    inputRegisterBean.setFld1(selectedResultBean.getFld1());
		    inputRegisterBean.setFld1002(2);
		    if (selectedResultBean.getFld9() != null && !"".equals(selectedResultBean.getFld9().trim())){
			List<Interesado> interesados = new ArrayList<Interesado>();
			Interesado interesado = new Interesado();
			interesado.setNombre(selectedResultBean.getFld9());
			interesados.add(interesado);
			inputRegisterBean.setInteresados(interesados );
		    }
		    InputRegisterBo inputRegisterBeanBo = new InputRegisterBo();
		    inputRegisterBeanBo.updateOnlyFolder(useCaseConf, book.getId(),
			    selectedResultBean.getFdrid(),inputRegisterBean);
		}

	    }
	    catch (RPInputRegisterException rpInputRegisterException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpInputRegisterException.getCode().getCode() + " . Mensaje: "
			+ rpInputRegisterException.getShortMessage());
		Utils.redirectToErrorPage(rpInputRegisterException, null, null);
	    }
	    catch (RPGenericException rpGenericException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpGenericException.getCode().getCode() + " . Mensaje: "
			+ rpGenericException.getShortMessage());
		Utils.redirectToErrorPage(rpGenericException, null, null);
	    }
	}
    }
    
    /**
     * Método que marca como no borrado los elementos seleccionados.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public void noBorrado() throws SessionException {
	init();
	ScrRegstate book =
		(ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
	if (book == null) {
	    throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	}
	InputRegisterBean inputRegisterBean = null;
	for (RowSearchInputRegisterBean selectedResultBean : selectedResults) {
	    try {
		if (selectedResultBean.getFld1002() != null && "2".equals(selectedResultBean.getFld1002())) {
		    inputRegisterBean = new InputRegisterBean();
		    inputRegisterBean.setFdrid(selectedResultBean.getFdrid());
		    inputRegisterBean.setFld1(selectedResultBean.getFld1());
		    inputRegisterBean.setFld1002(2);
		    inputRegisterBean.setFld1002(null);
		    if (selectedResultBean.getFld9() != null && !"".equals(selectedResultBean.getFld9().trim())){
			List<Interesado> interesados = new ArrayList<Interesado>();
			Interesado interesado = new Interesado();
			interesado.setNombre(selectedResultBean.getFld9());
			interesados.add(interesado);
			inputRegisterBean.setInteresados(interesados );
		    }
		    InputRegisterBo inputRegisterBeanBo = new InputRegisterBo();
		    inputRegisterBeanBo.updateOnlyFolder(useCaseConf, book.getId(),
			    selectedResultBean.getFdrid(), inputRegisterBean);
		}

	    }
	    catch (RPInputRegisterException rpInputRegisterException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpInputRegisterException.getCode().getCode() + " . Mensaje: "
			+ rpInputRegisterException.getShortMessage());
		Utils.redirectToErrorPage(rpInputRegisterException, null, null);
	    }
	    catch (RPGenericException rpGenericException) {
		LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE + ". Código: "
			+ rpGenericException.getCode().getCode() + " . Mensaje: "
			+ rpGenericException.getShortMessage());
		Utils.redirectToErrorPage(rpGenericException, null, null);
	    }
	}
    }
    
    /**
     * Método que copia un registro de un libro a otro.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public void moverReg() throws SessionException {
	LOG.trace("Entrando en el constructor de copiarReg");
	init();
	ScrRegstate book =
		(ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
	if (book == null) {
	    throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	}
	
	for (RowSearchInputRegisterBean selectedResultBean : selectedResults) {
	    
	    try {
		registerBo = new RegisterBo();
		registerBo.moveRegister(selectedResultBean.getFdrid(),
			book.getIdocarchhdr().getId(), bookDestination);
		FacesMessage message =
			    new FacesMessage(FacesMessage.SEVERITY_INFO, "Copiado", "Se ha copiado el registro correctamente");
		    RequestContext.getCurrentInstance().showMessageInDialog(message);
	    }
	    catch (RPRegisterException rPRegisterException) {
		LOG.error(ErrorConstants.MOVE_REGISTER_ERROR + ". Código: "
			+ rPRegisterException.getCode().getCode() + " . Mensaje: "
			+ rPRegisterException.getShortMessage());
		Utils.redirectToErrorPage(rPRegisterException, null, null);
	    }
	}
    }
    
    /**
     * Borra los datos de la distribución y crea una vacío.
     * 
     */
    @SuppressWarnings("unchecked")
    public void reinitDis() {
	init();
	
	try{
	    if (selectedResults == null || selectedResults.length == 0) {
		    ValidationBo.showDialog("", new FacesMessage(FacesMessage.SEVERITY_WARN, "",
			    "Debe seleccionar mínimo un registro"));
	    }else {
		 List<Integer> orgIds = new ArrayList<Integer>();
		 for (RowSearchInputRegisterBean row : selectedResults) {
		     if (row.getFld8() != null && row.getFld8().getId() != null){
			 orgIds.add(row.getFld8().getId());
		     }
		 }
		 if (orgIds != null && orgIds.size() > 0){
		     this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(),
                	typeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
		     this.setSelectdestinoRedisDepartamentos(Utils.defaultDepart(this.listDepartament,
			     orgIds.get(0)));
		} else {
		    this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(),
			    typeDestinoRedis, useCaseConf.getEntidadId(), null);
		    this.setSelectdestinoRedisDepartamentos(null);
		}
	    }
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(
		null, sessionException, null);
	}
	catch (ValidationException validationException) {
	    LOG.error(
		ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, validationException);
	    Utils.redirectToErrorPage(
		null, validationException, null);
	}
        this.setMotivoDistribucion(null);
    }

    
    /**
     * Obtiene el valor del parámetro listDocuments.
     * 
     * @return listDocuments valor del campo a obtener.
     */
    public List<Axdoch> getListDocuments() {
	return listDocuments;
    }

    /**
     * Guarda el valor del parámetro listDocuments.
     * 
     * @param listDocuments
     *            valor del campo a guardar.
     */
    public void setListDocuments(List<Axdoch> listDocuments) {
	this.listDocuments = listDocuments;
    }
    
    /**
     * Obtiene el valor del parámetro bookDestination.
     * 
     * @return bookDestination valor del campo a obtener.
     */
    public Integer getBookDestination() {
        return bookDestination;
    }
    /**
     * Guarda el valor del parámetro bookDestination.
     * 
     * @param bookDestination
     *            valor del campo a guardar.
     */
    public void setBookDestination(Integer bookDestination) {
        this.bookDestination = bookDestination;
    }
    /**
     * Obtiene el valor del parámetro listDepartament.
     * 
     * @return listDepartament valor del campo a obtener.
     */
    public List<Iuserdepthdr> getListDepartament() {
        return listDepartament;
    }
    /**
     * Guarda el valor del parámetro listDepartament.
     * 
     * @param listDepartament
     *            valor del campo a guardar.
     */
    public void setListDepartament(List<Iuserdepthdr> listDepartament) {
        this.listDepartament = listDepartament;
    }

}