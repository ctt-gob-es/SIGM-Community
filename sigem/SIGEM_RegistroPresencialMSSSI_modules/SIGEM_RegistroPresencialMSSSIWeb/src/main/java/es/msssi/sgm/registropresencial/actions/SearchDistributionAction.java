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

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.event.data.SortEvent;
import org.primefaces.model.LazyDataModel;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SecurityException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesdoc.Iuserdepthdr;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxSfIn;
import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;

import es.msssi.sgm.registropresencial.beans.ParamBookBean;
import es.msssi.sgm.registropresencial.beans.RowSearchDistributionBean;
import es.msssi.sgm.registropresencial.beans.RowSearchInputRegisterBean;
import es.msssi.sgm.registropresencial.businessobject.BooksBo;
import es.msssi.sgm.registropresencial.businessobject.DistributionBo;
import es.msssi.sgm.registropresencial.businessobject.PermissionsBo;
import es.msssi.sgm.registropresencial.businessobject.SearchDistributionBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPBookException;
import es.msssi.sgm.registropresencial.errors.RPDistributionException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.validations.ValidationBo;
import es.msssi.sgm.registropresencial.validations.ValidationListBo;

/**
 * Action que gestiona la búsqueda de registros de entrada.
 * 
 * @author cmorenog
 * */
public class SearchDistributionAction extends GenericActions implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SearchDistributionAction.class.getName());
    /** Fila del datatable que se ha seleccionado. */
    private RowSearchDistributionBean selectedResult;
    /** Filas del datatable que se ha seleccionado. */
    private RowSearchDistributionBean[] selectedResults;
    /** Implementación de lazydatamodel para gestionar el datatable. */
    private LazyDataModel<RowSearchDistributionBean> searchDistributionBo;
    /** Implementación de las funcionalidades de distribución. */
    private DistributionBo distributionBo;
    /** Motivo del rechazo de una distribución. */
    private String motivoRechazo;
    /** Organismo seleccionado para le cambio de destino. */
    private ScrOrg destinoCambio;
    /** departamento seleccionado en la redistribución. */
    private Iuserdepthdr selectdestinoRedisDepartamentos = null;

    /** tipo de destino seleccionado en la redistribución. */
    private int typeDestinoRedis = 2;
    /** Motivo de la redireccion de una distribución. */
    private String motivoRedireccion;
    /** Contador de resultados. */
    private int countResult;
    private String tipoAceptacion;
    private int tipoRegistros;
    private Integer libroSelecAceptar;
    /** lista de departamentos para la distribución. */
    private List<Iuserdepthdr> listDepartament = null;
    /**
     * Constructor.
     * 
     * @throws SessionException
     *             si la sesión ha caducado.
     */
    public SearchDistributionAction() throws SessionException {
	searchDistributionBo = new SearchDistributionBo();
	distributionBo = new DistributionBo();
	init();
	int pageSize = Integer.parseInt(Configurator.getInstance().getProperty(
	    ConfigurationKeys.KEY_DESKTOP_DEFAULT_PAGE_TABLE_RESULTS_SIZE));
	searchDistributionBo.load(
	    1, pageSize, null, null, null);
	limpiar();
	initDistribution();
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
		 for (RowSearchDistributionBean row : selectedResults) {
		     if (row.getAxsf().getFld8() != null && row.getAxsf().getFld8().getId() != null){
			 orgIds.add(row.getAxsf().getFld8().getId());
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
	this.setMotivoRedireccion(null);
	this.setDestinoCambio(null);
	this.setMotivoRechazo(null);
	this.setTipoAceptacion(null);
	this.setTipoRegistros(-1);
	this.setLibroSelecAceptar(null);
    }

    /**
     * Obtiene el valor del parámetro searchDistributionBo.
     * 
     * @return searchDistributionBo valor del campo a obtener.
     */
    public LazyDataModel<RowSearchDistributionBean> getSearchDistributionBo() {
	return searchDistributionBo;
    }

    /**
     * Guarda el valor del parámetro searchDistributionBo.
     * 
     * @param searchDistributionBo
     *            valor del campo a guardar.
     */
    public void setSearchDistributionBo(
	LazyDataModel<RowSearchDistributionBean> searchDistributionBo) {
	this.searchDistributionBo = searchDistributionBo;
    }

    /**
     * Obtiene el valor del parámetro selectedResult.
     * 
     * @return selectedResult valor del campo a obtener.
     */
    public RowSearchDistributionBean getSelectedResult() {
	return selectedResult;
    }

    /**
     * Guarda el valor del parámetro selectedResult.
     * 
     * @param selectedResult
     *            valor del campo a guardar.
     */
    public void setSelectedResult(
	RowSearchDistributionBean selectedResult) {
	this.selectedResult = selectedResult;
    }

    /**
     * Obtiene el valor del parámetro selectedResults.
     * 
     * @return selectedResults valor del campo a obtener.
     */
    public RowSearchDistributionBean[] getSelectedResults() {
	return selectedResults;
    }

    /**
     * Guarda el valor del parámetro selectedResults.
     * 
     * @param selectedResults
     *            valor del campo a guardar.
     */
    public void setSelectedResults(
	RowSearchDistributionBean[] selectedResults) {
	this.selectedResults = selectedResults;
    }

    /**
     * Selecciona el Id de ResultId.
     * 
     * @return selectedResults[0].getId() valor del Id.
     */
    public Integer selectResultId() {
	Integer result = null;
	if (selectedResults == null ||
	    selectedResults.length == 0) {
	    result = null;
	}
	else {
	    result = selectedResults[0].getId();
	}
	return result;
    }

    /**
     * Limpia el formulario y la tabla de resultados.
     */
    public void limpiar() {
	init();

	Application application = facesContext.getApplication();
	ViewHandler viewHandler = application.getViewHandler();
	UIViewRoot viewRoot = viewHandler.createView(
	    facesContext, facesContext.getViewRoot().getViewId());
	facesContext.setViewRoot(viewRoot);
	facesContext.renderResponse(); // Optional
    }

    /**
     * Método de actualiza la paginación y la mapea con la de SIGM.
     * 
     * @param event
     *            Evento de paginación que se ha lanzado.
     */
    public void pageButton(
	PageEvent event) {
	((SearchDistributionBo) searchDistributionBo).setPage(event.getPage());
    }

    /**
     * Método de actualiza la ordenación y la mapea con la de SIGM.
     * 
     * @param event
     *            Evento de ordenación que se ha lanzado.
     */
    public void sortButton(
	SortEvent event) {
	((SearchDistributionBo) searchDistributionBo).setPage(0);
    }

    /**
     * Método de inicia la distribución.
     * 
     */
    public void initDistribution() {
	((SearchDistributionBo) searchDistributionBo).setPage(0);
	try {
	    countResult =
		((SearchDistributionBo) searchDistributionBo).countSearchDistributionRegister();
	}
	catch (RPDistributionException e) {
	    LOG.error(ErrorConstants.GET_COUNT_DISTRIBUTION_ERROR_MESSAGE +
		". Código: " + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
	    Utils.redirectToErrorPage(
		e, null, null);
	}
	catch (RPGenericException e) {
	    LOG.error(ErrorConstants.SAVE_INPUT_REGISTER_ERROR_MESSAGE +
		". Código: " + e.getCode().getCode() + " . Mensaje: " + e.getShortMessage());
	    Utils.redirectToErrorPage(
		e, null, null);
	}

    }

    /**
     * Redirección a la página de consulta desde el botón abrir.
     * 
     * @throws ValidationException
     *             Si se produce un error en la validación.
     * @throws SessionException
     *             Si se produce un error en la sesión.
     * @throws BookException
     *             Si se produce un error al tratar la información del libro.
     * */
    public void onRowSelectNavigateBottomOpen()
	throws BookException, SessionException, ValidationException {

	RowSearchDistributionBean result;
	if (selectedResults != null &&
	    selectedResults.length == 1) {
	    result = selectedResults[0];
	    goToOpenRegister(result);
	}
	else {
	    ValidationBo.showDialog(
		"Abrir", new FacesMessage(
		    FacesMessage.SEVERITY_WARN, "", "Debe seleccionar un único registro"));
	}
    }

    /**
     * Redirección a la página de consulta desde el datatable.
     * 
     * @param event
     *            evento que se ha lanzado
     * @throws ValidationException
     *             Si se produce un error en la validación.
     * @throws SessionException
     *             Si se produce un error en la sesión.
     * @throws BookException
     *             Si se produce un error al tratar la información del libro.
     * */
    public void onRowSelectNavigateOpen(
	SelectEvent event)
	throws BookException, SessionException, ValidationException {
	RowSearchDistributionBean row = ((RowSearchDistributionBean) event.getObject());
	goToOpenRegister(row);
    }

    /**
     * Redirección a la página de consulta desde el datatable.
     * 
     * @param result
     *            el objeto distribución
     * @throws ValidationException
     *             Si se produce un error en la validación.
     * @throws SessionException
     *             Si se produce un error en la sesión.
     * @throws BookException
     *             Si se produce un error al tratar la información del libro.
     * */
    private void goToOpenRegister(
	RowSearchDistributionBean result)
	throws BookException, SessionException, ValidationException {
	BooksBo booksBo = new BooksBo();
	ScrRegstate book = null;
	try {
	    if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(
		KeysRP.J_BOOK) != null) {
		book =
		    (ScrRegstate) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get(
			    KeysRP.J_BOOK);

		if (book.getId() == null ||
		    (book.getId() != null && !book.getId().equals(
			result.getIdArch()))) {

		    book = booksBo.getBook(
			useCaseConf.getSessionID(), result.getIdArch());
		    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
			KeysRP.J_BOOK, book);
		}
		else {
		    book = booksBo.getBook(
			useCaseConf.getSessionID(), result.getIdArch());
		    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
			KeysRP.J_BOOK, book);
		}
	    }
	    else {
		book = booksBo.getBook(
		    useCaseConf.getSessionID(), result.getIdArch());
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
		    KeysRP.J_BOOK, book);
	    }
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
	PermissionsBo permissionsBo = new PermissionsBo();
	try {
	    ParamBookBean param =
		permissionsBo.getPermission(
		    useCaseConf, FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get(
			    KeysRP.J_PARAMBOOK), result.getIdArch(), true);
	    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
		KeysRP.J_PARAMBOOK, param);
	}
	catch (SecurityException securityException) {
	    LOG.error(
		ErrorConstants.GET_PERMISSIONS_ERROR_MESSAGE, securityException);
	    Utils.redirectToErrorPage(
		null, securityException, null);
	}

	FacesContext.getCurrentInstance().getExternalContext().getFlash().put(
	    "distribucion", true);
	Map<String, Object> parameter = new HashMap<String, Object>();
	parameter.put(
	    "registerSelect", selectedResults[0].getIdFdr());
	RowSearchDistributionBean bean = selectedResults[0] ;
	if (bean.getAxsf() instanceof AxSfIn ){
        	Utils.navigate(
        	    parameter, true, "inputRegister.xhtml");
	}else {
	    Utils.navigate(
        	    parameter, true, "outputRegister.xhtml");
	}
    }

    /**
     * Método que acepta la distribución de los elementos seleccionados.
     */
    public void aceptarDist() {
	if (selectedResults != null &&
	    selectedResults.length > 0) {
	    init();
	    try {
		if ("1".equals(tipoAceptacion)){
		 // List<ScrDistreg> listDistr =
			distributionBo.accept(
			    useCaseConf, selectedResults, Integer.valueOf(-1),tipoRegistros);
		} else {
		    // List<ScrDistreg> listDistr =
			distributionBo.accept(
			    useCaseConf, selectedResults, libroSelecAceptar,tipoRegistros);
		}
		
	    }
	    catch (RPDistributionException e) {
		LOG.error(
		    ErrorConstants.ACCEPT_DISTRIBUTION_ERROR_MESSAGE, e);
		FacesMessage message = new FacesMessage(
		    FacesMessage.SEVERITY_INFO, "Aceptar Distribución", e.getMessage());
		RequestContext.getCurrentInstance().showMessageInDialog(
		    message);
	    }

	}
	else {
	    ValidationBo.showDialog(
		"Aceptar", new FacesMessage(
		    FacesMessage.SEVERITY_WARN, "", "Debe seleccionar mínimo un registro"));
	}
    }

    /**
     * Método que rechaza la distribución de los elementos seleccionados.
     */
    public void rechazarDist() {
	init();
	try {
	    // List<ScrDistreg> listDistr =
	    distributionBo.reject(
		useCaseConf, selectedResults, motivoRechazo);
	}
	catch (RPDistributionException e) {
	    LOG.error(
		ErrorConstants.REJECT_DISTRIBUTION_ERROR_MESSAGE, e);
	    FacesMessage message = new FacesMessage(
		FacesMessage.SEVERITY_INFO, "Rechazar Distribución", e.getMessage());
	    RequestContext.getCurrentInstance().showMessageInDialog(
		message);
	}
	catch (RPGenericException e) {
	    LOG.error(
		ErrorConstants.REJECT_DISTRIBUTION_ERROR_MESSAGE, e);
	    FacesMessage message = new FacesMessage(
		FacesMessage.SEVERITY_INFO, "Rechazar Distribución", e.getMessage());
	    RequestContext.getCurrentInstance().showMessageInDialog(
		message);
	}
    }

    /**
     * Método que comprueba si hay seleccionado algún elemento. Si no hay
     * elementos seleccionados muestra un mensaje de error.
     */
    public void isSelect() {
	if (selectedResults == null ||
	    selectedResults.length == 0) {
	    ValidationBo.showDialog(
		"Error", new FacesMessage(
		    FacesMessage.SEVERITY_WARN, "", "Debe seleccionar mínimo un registro"));
	}
	else {
	    reinitDis();
	}
    }

    /**
     * Método que comprueba si hay seleccionado algún elemento. Si no hay
     * elementos seleccionados muestra un mensaje de error.
     */
    public void isSelectAccept() {
	if (selectedResults == null ||
	    selectedResults.length == 0) {
	    ValidationBo.showDialog(
		"Error", new FacesMessage(
		    FacesMessage.SEVERITY_WARN, "", "Debe seleccionar mínimo un registro"));
	}
	else {
	    int countInput = 0;
	    int countOutput = 0;
	    for (RowSearchDistributionBean result: selectedResults){
		if (result.getAxsf() instanceof AxSfIn){
		    countInput ++;
		}else {
		    countOutput ++;
		}
	    }
	   
	    if (countInput > 0 && countOutput >0){
		ValidationBo.showDialog(
			"Error", new FacesMessage(
			    FacesMessage.SEVERITY_WARN, "", "No puede aceptar registros de distintos tipos. Seleccione registros de entrada o de salida"));
	    } else {
		reinitDis();
		 if (countInput > 0){
		     tipoRegistros = 1;
		 }else{
		     tipoRegistros = 2;
		 }

	    }
	}
    }
    
    /**
     * Método que cambia de destino la distribución de los elementos
     * seleccionados.
     */
    public void cambiarDestDist() {
	init();
	try {
	    distributionBo.change(useCaseConf, selectedResults, destinoCambio,
		((SearchDistributionBo) searchDistributionBo).getSearchDistributionRegister()
		    .getType());
	}
	catch (RPDistributionException e) {
	    LOG.error(
		ErrorConstants.CHANGE_DISTRIBUTION_ERROR_MESSAGE, e);
	    FacesMessage message = new FacesMessage(
		FacesMessage.SEVERITY_INFO, "Cambio de destino", e.getMessage());
	    RequestContext.getCurrentInstance().showMessageInDialog(
		message);
	}
	catch (RPGenericException e) {
	    LOG.error(
		ErrorConstants.CHANGE_DISTRIBUTION_ERROR_MESSAGE, e);
	    FacesMessage message = new FacesMessage(
		FacesMessage.SEVERITY_INFO, "Cambio de destino", e.getMessage());
	    RequestContext.getCurrentInstance().showMessageInDialog(
		message);
	}
    }

    /**
     * Método que redistribuye la distribución de los elementos seleccionados.
     */
    public void redistribuirDist() {
	init();
	try {

	    Integer userId = selectdestinoRedisDepartamentos.getId();

	    distributionBo.redistribution(useCaseConf, selectedResults,
		((SearchDistributionBo) searchDistributionBo).getSearchDistributionRegister()
		    .getType(), motivoRedireccion, typeDestinoRedis, userId);
	}
	catch (RPDistributionException e) {
	    LOG.error(
		ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
	    FacesMessage message = new FacesMessage(
		FacesMessage.SEVERITY_INFO, "Redistribuir Distribución", e.getMessage());
	    RequestContext.getCurrentInstance().showMessageInDialog(
		message);
	}
	catch (RPGenericException e) {
	    LOG.error(
		ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
	    FacesMessage message = new FacesMessage(
		FacesMessage.SEVERITY_INFO, "Redistribuir Distribución", e.getMessage());
	    RequestContext.getCurrentInstance().showMessageInDialog(
		message);
	}
    }

    /**
     * Método que archiva la distribución de los elementos seleccionados.
     */
    public void archivarDist() {
	if (selectedResults != null &&
	    selectedResults.length > 0) {
	    init();
	    try {
		// List<ScrDistreg> listDistr =
		distributionBo.archive(
		    useCaseConf, selectedResults);
	    }
	    catch (RPGenericException e) {
		LOG.error(
		    ErrorConstants.ARCHIVE_DISTRIBUTION_ERROR_MESSAGE, e);
		FacesMessage message = new FacesMessage(
		    FacesMessage.SEVERITY_INFO, "Archivar Distribución", e.getMessage());
		RequestContext.getCurrentInstance().showMessageInDialog(
		    message);
	    }
	    catch (RPDistributionException e) {
		LOG.error(
		    ErrorConstants.ARCHIVE_DISTRIBUTION_ERROR_MESSAGE, e);
		FacesMessage message = new FacesMessage(
		    FacesMessage.SEVERITY_INFO, "Archivar Distribución", e.getMessage());
		RequestContext.getCurrentInstance().showMessageInDialog(
		    message);
	    }

	}
	else {
	    ValidationBo.showDialog(
		"Archivar", new FacesMessage(
		    FacesMessage.SEVERITY_WARN, "", "Debe seleccionar mínimo un registro"));
	}
    }

    /**
     * Obtiene el valor del parámetro motivoRechazo.
     * 
     * @return motivoRechazo valor del campo a obtener.
     */
    public String getMotivoRechazo() {
	return motivoRechazo;
    }

    /**
     * Guarda el valor del parámetro motivoRechazo.
     * 
     * @param motivoRechazo
     *            valor del campo a guardar.
     */
    public void setMotivoRechazo(
	String motivoRechazo) {
	this.motivoRechazo = motivoRechazo;
    }

    /**
     * Obtiene el valor del parámetro destinoCambio.
     * 
     * @return destinoCambio valor del campo a obtener.
     */
    public ScrOrg getDestinoCambio() {
	return destinoCambio;
    }

    /**
     * Guarda el valor del parámetro destinoCambio.
     * 
     * @param destinoCambio
     *            valor del campo a guardar.
     */
    public void setDestinoCambio(
	ScrOrg destinoCambio) {
	this.destinoCambio = destinoCambio;
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
    public void setSelectdestinoRedisDepartamentos(
	Iuserdepthdr selectdestinoRedisDepartamentos) {
	this.selectdestinoRedisDepartamentos = selectdestinoRedisDepartamentos;
    }

    /**
     * Obtiene el valor del parámetro typeDestinoRedis.
     * 
     * @return typeDestinoRedis valor del campo a obtener.
     */
    public int getTypeDestinoRedis() {
	return typeDestinoRedis;
    }

    /**
     * Guarda el valor del parámetro typeDestinoRedis.
     * 
     * @param typeDestinoRedis
     *            valor del campo a guardar.
     */
    public void setTypeDestinoRedis(
	int typeDestinoRedis) {
	this.typeDestinoRedis = typeDestinoRedis;
    }

    /**
     * Obtiene el valor del parámetro motivoRedireccion.
     * 
     * @return motivoRedireccion valor del campo a obtener.
     */
    public String getMotivoRedireccion() {
	return motivoRedireccion;
    }

    /**
     * Guarda el valor del parámetro motivoRedireccion.
     * 
     * @param motivoRedireccion
     *            valor del campo a guardar.
     */
    public void setMotivoRedireccion(
	String motivoRedireccion) {
	this.motivoRedireccion = motivoRedireccion;
    }

    /**
     * Obtiene el valor del parámetro countResult.
     * 
     * @return countResult valor del campo a obtener.
     */
    public int getCountResult() {
	return countResult;
    }

    /**
     * Guarda el valor del parámetro countResult.
     * 
     * @param countResult
     *            valor del campo a guardar.
     */
    public void setCountResult(
	int countResult) {
	this.countResult = countResult;
    }
    /**
     * Método que actualiza el campo origen con el valor seleccionado en la
     * búsqueda avanzada de organismos.
     */
    public void updateOrigin() {
	ScrOrg unidad = null;
	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(
	    "UNITSDIALOG") != null) {
	    unidad =
		(ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		    .get(
			"UNITSDIALOG");
	    ((SearchDistributionBo) searchDistributionBo).getSearchDistributionRegister().setFld7(
		unidad);
	}
    }

    /**
     * Método que actualiza el campo destino con el valor seleccionado en la
     * búsqueda avanzada de organismos.
     */
    public void updateDestination() {
	ScrOrg unidad = null;
	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(
	    "UNITSDIALOG") != null) {
	    unidad =
		(ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		    .get(
			"UNITSDIALOG");
	    ((SearchDistributionBo) searchDistributionBo).getSearchDistributionRegister().setFld8(
		unidad);
	}
    }
    /**
     * Método que actualiza el campo destino con el valor seleccionado en la
     * búsqueda avanzada de organismos.
     */
    public void updateDestinationCambio() {
	ScrOrg unidad = null;
	if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(
	    "UNITSDIALOG") != null) {
	    unidad =
		(ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		    .get(
			"UNITSDIALOG");
	    setDestinoCambio(unidad);
	}
    }
    
    /**
     * Obtiene el valor del parámetro tipoAceptacion.
     * 
     * @return tipoAceptacion valor del campo a obtener.
     */
    public String getTipoAceptacion() {
        return tipoAceptacion;
    }

    /**
     * Guarda el valor del parámetro tipoAceptacion.
     * 
     * @param tipoAceptacion
     *            valor del campo a guardar.
     */
    public void setTipoAceptacion(String tipoAceptacion) {
        this.tipoAceptacion = tipoAceptacion;
    }
    /**
     * Obtiene el valor del parámetro tipoRegistros.
     * 
     * @return tipoRegistros valor del campo a obtener.
     */
    public int getTipoRegistros() {
        return tipoRegistros;
    }
    /**
     * Guarda el valor del parámetro tipoRegistros.
     * 
     * @param tipoRegistros
     *            valor del campo a guardar.
     */
    public void setTipoRegistros(int tipoRegistros) {
        this.tipoRegistros = tipoRegistros;
    }
    /**
     * Obtiene el valor del parámetro libroSelecAceptar.
     * 
     * @return libroSelecAceptar valor del campo a obtener.
     */
    public Integer getLibroSelecAceptar() {
        return libroSelecAceptar;
    }
    /**
     * Guarda el valor del parámetro libroSelecAceptar.
     * 
     * @param libroSelecAceptar
     *            valor del campo a guardar.
     */
    public void setLibroSelecAceptar(Integer libroSelecAceptar) {
        this.libroSelecAceptar = libroSelecAceptar;
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