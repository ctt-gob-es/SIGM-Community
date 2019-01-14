package es.msssi.sgm.registropresencial.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

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
import com.ieci.tecdoc.common.invesdoc.Iuseruserhdr;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.keys.ISicresKeys;

import es.dipucr.sgm.registropresencial.beans.DocumentoBean;
import es.dipucr.sgm.registropresencial.bussinessobject.DocumentoBo;
import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.msssi.sgm.registropresencial.beans.RowSearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.businessobject.DistributionBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterDocumentsBo;
import es.msssi.sgm.registropresencial.businessobject.SearchOutputRegisterBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPDistributionException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.validations.ValidationBo;
import es.msssi.sgm.registropresencial.validations.ValidationListBo;

/**
 * Action que gestiona la búsqueda de registros de salida.
 * 
 * @author cmorenog
 */
public class SearchOutputRegisterFormAction extends GenericActions implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(SearchOutputRegisterFormAction.class.getName());

	/** Fila del datatable que se ha seleccionado. */
	private RowSearchOutputRegisterBean selectedResult;
	/** Filas del datatable que se han seleccionado. */
	private RowSearchOutputRegisterBean[] selectedResults;
	/** Implementación de lazydatamodel para gestionar el datatable. */
	private LazyDataModel<RowSearchOutputRegisterBean> outputRegisterBo;
	/** Implementación de bo de registros. */
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
	/** usuario seleccionado en la distribución. */
	private Iuseruserhdr selectdestinoRedisUsuario = null;
	/** Implementación de las funcionalidades de distribución. */
	
	private DistributionBo distributionBo;
	private List<Axdoch> listDocuments;
	private RegisterDocumentsBo registerDocumentsBo;


	/** lista de departamentos para la distribución. */
	private List<Iuseruserhdr> listUsuarios = null;
	/** lista de departamentos para la distribución. */
	private List<Iuserdepthdr> listDepartament = null;
	/** lista de departamentos para la distribución. */
	private List<Iusergrouphdr> listGrupos = null;
	
	private DocumentoBean documentoBean;

	/**
	 * Constructor.
	 * 
	 * @throws SessionException
	 *             si la sesión ha caducado.
	 */
	public SearchOutputRegisterFormAction() throws SessionException {
		LOG.trace("Entrando en el constructor de SearchInputRegisterFormAction");
		outputRegisterBo = new SearchOutputRegisterBo();
		init();
		
		((SearchOutputRegisterBo) outputRegisterBo).setLastRegister(true);
		ScrRegstate book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
		
		if (book == null) {
			throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
		}
		
		try {
			validateIdBook(book.getIdocarchhdr().getId(), useCaseConf);
			iniciaDocumentoBean(book.getIdocarchhdr().getId());
			
		} catch (BookException bookException) {
			LOG.error(ErrorConstants.GET_INFORMATION_BOOK_ERROR_MESSAGE, bookException);
			Utils.redirectToErrorPage(null, bookException, null);
		}
	}
	
	private void iniciaDocumentoBean(Integer bookId){
		
		documentoBean = new DocumentoBean();
		
		documentoBean.setUseCaseConf(useCaseConf);
		documentoBean.setRegisterBean(selectedResult);
		documentoBean.setEntidadId(useCaseConf.getEntidadId());
		documentoBean.setBookId(bookId);
		
		DocumentoBo.actualizaArbol(false, documentoBean);
	}
			
	/**
	 * Limpia el formulario y la tabla de resultados.
	 */
	public void limpiar() {
		outputRegisterBo = new SearchOutputRegisterBo();
		((SearchOutputRegisterBo) outputRegisterBo).setLastRegister(true);	

		init();
		((SearchOutputRegisterBo) outputRegisterBo).closeQuery();
		((SearchOutputRegisterBo) outputRegisterBo).openQuery();		
	}

	/**
	 * Actualiza la páginación y la mapea con la de SIGM.
	 * 
	 * @param event
	 *            Evento de paginación que se ha lanzado.
	 */
	public void pageButton(PageEvent event) {
		((SearchOutputRegisterBo) outputRegisterBo).setPage(event.getPage());
	}

	/**
	 * Actualiza la ordenación y la mapea con la de SIGM.
	 * 
	 * @param event
	 *            Evento de ordenación que se ha lanzado.
	 */
	public void sortButton(SortEvent event) {
		((SearchOutputRegisterBo) outputRegisterBo).setPage(0);
	}

	/**
	 * Inicializa la consulta en SIGM.
	 */
	public void initRegisterQuery() {
		((SearchOutputRegisterBo) outputRegisterBo).setLastRegister(false);
		((SearchOutputRegisterBo) outputRegisterBo).closeQuery();
		((SearchOutputRegisterBo) outputRegisterBo).openQuery();
	}

	/**
	 * Busca según el último registro realizado.
	 */
	public void lastRegister() {
		((SearchOutputRegisterBo) outputRegisterBo).setSearchOutputRegister(new SearchOutputRegisterBean());
		((SearchOutputRegisterBo) outputRegisterBo).setLastRegister(true);
	}

	/**
	 * Redirecciona a la página de edición desde el datatable.
	 * 
	 * @param event
	 *            evento que se ha lanzado.
	 */
	public void onRowSelectNavigateEdit(SelectEvent event) {
		RowSearchOutputRegisterBean row = ((RowSearchOutputRegisterBean) event.getObject());
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("registerSelect", row.getFdrid());
		Utils.navigate(parameter, false, "outputRegister.xhtml");
	}

	/**
	 * Redirecciona a la página de consulta desde el datatable.
	 * 
	 * @param event
	 *            evento que se ha lanzado.
	 */
	public void onRowSelectNavigateView(SelectEvent event) {
		RowSearchOutputRegisterBean row = ((RowSearchOutputRegisterBean) event.getObject());
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("registerSelect", row.getFdrid());
		Utils.navigate(parameter, true, "outputRegister.xhtml");
	}

	/**
	 * Redirecciona a la página de edición desde el botón.
	 */
	public void onRowSelectNavigateBottomEdit() {
		if (selectedResults != null && selectedResults.length == 1) {
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("registerSelect", selectedResults[0].getFdrid());
			Utils.navigate(parameter, false, "outputRegister.xhtml");
			
		} else {
			ValidationBo.showDialog("Editar", new FacesMessage( FacesMessage.SEVERITY_WARN, "", "Debe seleccionar un único registro"));
			
		}
	}

	/**
	 * Redirecciona a la página de consulta desde el botón.
	 */
	public void onRowSelectNavigateBottomView() {
		if (selectedResults != null && selectedResults.length == 1) {
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("registerSelect", selectedResults[0].getFdrid());
			Utils.navigate(parameter, true, "outputRegister.xhtml");
		} else {
			ValidationBo.showDialog("Abrir", new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Debe seleccionar un único registro"));
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
			Utils.navigate(parameter, false, "outputRegister.xhtml");
		} else {
			ValidationBo.showDialog("Copiar", new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Debe seleccionar un único registro"));
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
	 * Devuelve el outputRegisterBo.
	 * 
	 * @return outputRegisterBo el objeto outputRegisterBo.
	 */
	public LazyDataModel<RowSearchOutputRegisterBean> getOutputRegisterBo() {
		return outputRegisterBo;
	}

	/**
	 * Recibe el objeto outputRegisterBo.
	 * 
	 * @param outputRegisterBo
	 *            el objeto inputRegisterBo
	 */
	public void setOutputRegisterBo( LazyDataModel<RowSearchOutputRegisterBean> outputRegisterBo) {
		this.outputRegisterBo = outputRegisterBo;
	}

	/**
	 * Devuelve el objeto seleccionado del datatable.
	 * 
	 * @return selectedResult El objeto seleccionado.
	 */
	public RowSearchOutputRegisterBean getSelectedResult() {
		return selectedResult;
	}

	/**
	 * Recibe el objecto seleccionado del datatable.
	 * 
	 * @param selectedResult
	 *            El objeto seleccionado.
	 */
	public void setSelectedResult(RowSearchOutputRegisterBean selectedResult) {
		this.selectedResult = selectedResult;
	}

	/**
	 * Devuelve los objetos seleccionados del datatable.
	 * 
	 * @return selectedResults El array con los objetos seleccionados.
	 */
	public RowSearchOutputRegisterBean[] getSelectedResults() {
		return selectedResults;
	}

	/**
	 * Recibe los objectos seleccionados del datatable.
	 * 
	 * @param selectedResults
	 *            El array de objetos seleccionados.
	 */
	public void setSelectedResults(RowSearchOutputRegisterBean[] selectedResults) {
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
		} else {
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
	public void setSelectdestinoRedisDepartamentos( Iuserdepthdr selectdestinoRedisDepartamentos) {
		this.selectdestinoRedisDepartamentos = selectdestinoRedisDepartamentos;
	}

	/**
	 * Obtiene el valor del parámetro selectdestinoRedisGrupos.
	 * 
	 * @return selectdestinoRedisGrupos valor del campo a obtener.
	 */
	public Iusergrouphdr getSelectdestinoRedisGrupos() {
		return selectdestinoRedisGrupos;
	}

	/**
	 * Guarda el valor del parámetro selectdestinoRedisGrupos.
	 * 
	 * @param selectdestinoRedisGrupos
	 *            valor del campo a guardar.
	 */
	public void setSelectdestinoRedisGrupos( Iusergrouphdr selectdestinoRedisGrupos) {
		this.selectdestinoRedisGrupos = selectdestinoRedisGrupos;
	}

	/**
	 * Obtiene el valor del parámetro selectdestinoRedisUsuario.
	 * 
	 * @return selectdestinoRedisUsuario valor del campo a obtener.
	 */
	public Iuseruserhdr getSelectdestinoRedisUsuario() {
		return selectdestinoRedisUsuario;
	}

	/**
	 * Guarda el valor del parámetro selectdestinoRedisUsuario.
	 * 
	 * @param selectdestinoRedisUsuario
	 *            valor del campo a guardar.
	 */
	public void setSelectdestinoRedisUsuario( Iuseruserhdr selectdestinoRedisUsuario) {
		this.selectdestinoRedisUsuario = selectdestinoRedisUsuario;
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
	public void setTypeDestinoRedis(int typeDestinoRedis) {
		this.typeDestinoRedis = typeDestinoRedis;
	}

	/**
	 * Método que distribuye de los elementos seleccionados.
	 * 
	 * @throws SessionException
	 *             si la sesión ha caducado.
	 */
	public void distribuir() throws SessionException {
		init();
		ScrRegstate book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
		
		if (book == null) {
			throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
		}
		
		if (distributionBo == null) {
			distributionBo = new DistributionBo();
		}
		
		try {
			Integer userId = null;
			
			if(1 == typeDestinoRedis){
				userId = selectdestinoRedisUsuario.getId();
			} else if(2 == typeDestinoRedis){
				userId = selectdestinoRedisDepartamentos.getId();
			} else {
				userId = selectdestinoRedisGrupos.getId();
			}
			/*
			 * Integer userId; if (typeDestinoRedis == 1) { userId =
			 * selectdestinoRedisUsuario.getId(); } else { if (typeDestinoRedis
			 * == 1) { userId = selectdestinoRedisDepartamentos.getId(); } else
			 * { userId = selectdestinoRedisGrupos.getId(); } }
			 */
			List<Integer> ids = new ArrayList<Integer>();
			for (RowSearchOutputRegisterBean row : selectedResults) {
				ids.add(row.getFdrid());
			}
			String result = distributionBo.createDistribution(useCaseConf, ids, motivoDistribucion, typeDestinoRedis, userId, book.getIdocarchhdr().getId());
			
			if ("".equals(result.trim())) {
				result = "Se han distribuido los registros correctamente.";
			}
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Distribución", result);
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			
		} catch (RPDistributionException e) {
			LOG.error(ErrorConstants.REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE, e);
			Utils.redirectToErrorPage(e, null, null);
			
		} catch (RPGenericException e) {
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
			ValidationBo.showDialog("Distribuir", new FacesMessage( FacesMessage.SEVERITY_WARN, "", "Debe seleccionar mínimo un registro"));
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
			ScrRegstate book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
			
			if (book == null) {
				throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
			}
			
			try {
				List<Integer> list = new ArrayList<Integer>();
				List<String> listOut = new ArrayList<String>();
				for (RowSearchOutputRegisterBean row : selectedResults) {
					if (row.getFld6().equals(String.valueOf(ISicresKeys.SCR_ESTADO_REGISTRO_CERRADO))) {
						list.add(row.getFdrid());
					} else {
						listOut.add(row.getFld1());
					}
				}
				
				registerBo.openRegisters(useCaseConf, book.getIdocarchhdr().getId(), list);
				
				if (listOut.size() > 0) {
					StringBuilder result = new StringBuilder("No se han podido abrir los siguientes registros:");
					
					for (String id : listOut) {
						result.append(" " + id);
					}
					
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Abrir registros", result.toString());
					RequestContext.getCurrentInstance().showMessageInDialog( message);
				}
			} catch (RPRegisterException e) {
				LOG.error(ErrorConstants.OPENR_OUTPUT_REGISTER_ERROR_MESSAGE, e);
				Utils.redirectToErrorPage(e, null, null);
				
			} catch (RPGenericException e) {
				LOG.error(ErrorConstants.OPENR_OUTPUT_REGISTER_ERROR_MESSAGE, e);
				Utils.redirectToErrorPage(e, null, null);
			}

		} else {
			ValidationBo.showDialog("Abrir", new FacesMessage( FacesMessage.SEVERITY_WARN, "", "Debe seleccionar mínimo un registro"));
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
			ScrRegstate book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
			
			if (book == null) {
				throw new SessionException( SessionException.ERROR_SESSION_EXPIRED);
			}
			
			try {
				List<Integer> list = new ArrayList<Integer>();
				List<String> listOut = new ArrayList<String>();
				
				for (RowSearchOutputRegisterBean row : selectedResults) {
					if (row.getFld6().equals(String.valueOf(ISicresKeys.SCR_ESTADO_REGISTRO_COMPLETO))) {
						list.add(row.getFdrid());
					} else {
						listOut.add(row.getFld1());
					}
				}
				
				registerBo.closeRegisters(useCaseConf, book.getIdocarchhdr().getId(), list);
				
				if (listOut.size() > 0) {
					StringBuilder result = new StringBuilder( "No se han podido cerrar los siguientes registros:");
					
					for (String id : listOut) {
						result.append(" " + id);
					}
					
					FacesMessage message = new FacesMessage( FacesMessage.SEVERITY_INFO, "Cerrar Registros", result.toString());					
					RequestContext.getCurrentInstance().showMessageInDialog( message);
				}
			} catch (RPRegisterException e) {
				LOG.error(ErrorConstants.CLOSE_OUTPUT_REGISTER_ERROR_MESSAGE, e);
				Utils.redirectToErrorPage(e, null, null);
				
			} catch (RPGenericException e) {
				LOG.error(ErrorConstants.CLOSE_OUTPUT_REGISTER_ERROR_MESSAGE, e);
				Utils.redirectToErrorPage(e, null, null);
			}

		} else {
			ValidationBo.showDialog("Cerrar", new FacesMessage( FacesMessage.SEVERITY_WARN, "", "Debe seleccionar mínimo un registro"));
		}
	}

	/**
	 * Método que actualiza el campo origen con el valor seleccionado en la
	 * búsqueda avanzada de organismos.
	 */
	public void updateOrigin() {
		ScrOrg unidad = null;
		
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG") != null) {
			unidad = (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG");
			((SearchOutputRegisterBo) outputRegisterBo).getSearchOutputRegister().setFld7Value(unidad);
		}
	}

	/**
	 * Método que actualiza el campo destino con el valor seleccionado en la
	 * búsqueda avanzada de organismos.
	 */
	public void updateDestination() {
		ScrOrg unidad = null;
		
		if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG") != null) {
			unidad = (ScrOrg) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UNITSDIALOG");
			((SearchOutputRegisterBo) outputRegisterBo).getSearchOutputRegister().setFld8Value(unidad);
		}
	}

	/**
	 * Devuelve la lista de documentos adjuntos.
	 */
	public void viewDoc() {
		if (selectedResult != null) {
			ScrRegstate book = (ScrRegstate) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
			listDocuments = new ArrayList<Axdoch>();
			
			if (registerDocumentsBo == null) {
				registerDocumentsBo = new RegisterDocumentsBo();
			}
			
			try {
				LOG.info("Cargando documentos asociados al registro " + selectedResult.getFdrid());
				listDocuments = registerDocumentsBo.getDocumentsBasicInfo( useCaseConf, book.getIdocarchhdr().getId(), selectedResult.getFdrid(), false);
				
			} catch (RPRegisterException rPRegisterException) {
				LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, rPRegisterException);
			}
		}
	}
	
	/**
	 * Devuelve la lista de documentos adjuntos.
	 */
	public void viewDocTree() {		
		if (selectedResult != null) {
			
			MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
			
			ScrRegstate book = (ScrRegstate) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
			iniciaDocumentoBean(book.getIdocarchhdr().getId());
		}
	}

	
	/**
	 * Borra los datos de la distribución y crea una vacío.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void reinitDis() {
		init();

		try {
			if (selectedResults == null || selectedResults.length == 0) {
				ValidationBo.showDialog("", new FacesMessage( FacesMessage.SEVERITY_WARN, "", "Debe seleccionar mínimo un registro"));
			} else {
				List<Integer> orgIds = new ArrayList<Integer>();
				
				for (RowSearchOutputRegisterBean row : selectedResults) {
					
					if (row.getFld8() != null && row.getFld8().getId() != null) {
						orgIds.add(row.getFld8().getId());
					}
				}
				
				this.listUsuarios = null;
				this.setSelectdestinoRedisUsuario(null);
				this.listDepartament = null;
				this.setSelectdestinoRedisDepartamentos(null);
				this.listGrupos = null;
				this.setSelectdestinoRedisGrupos(null);
				
				if (orgIds != null && orgIds.size() > 0) {
										
					if(1 == typeDestinoRedis){						
						this.listUsuarios = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
						this.setSelectdestinoRedisUsuario(Utils.defaultUser(this.listUsuarios, orgIds.get(0)));						
					} else if( 2 == typeDestinoRedis){
						this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
						this.setSelectdestinoRedisDepartamentos(Utils.defaultDepart(this.listDepartament, orgIds.get(0)));
					} else {
						this.listGrupos = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
						this.setSelectdestinoRedisGrupos(Utils.defaultGroup(this.listGrupos, orgIds.get(0)));
					}
					
				} else {
					
					if(1 == typeDestinoRedis){
						this.listUsuarios = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), null);
						this.setSelectdestinoRedisUsuario(null);
					} else if( 2 == typeDestinoRedis){
						this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), null);
						this.setSelectdestinoRedisDepartamentos(null);
					} else {
						this.listGrupos = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), typeDestinoRedis, useCaseConf.getEntidadId(), null);
						this.setSelectdestinoRedisGrupos(null);
					}
				}
			}
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, sessionException);
			Utils.redirectToErrorPage(null, sessionException, null);
			
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, validationException);
			Utils.redirectToErrorPage(null, validationException, null);
		}
		
		this.setMotivoDistribucion(null);
	}
	
	@SuppressWarnings("unchecked")
	public void reLoadUserDeptsGroupDistribucion(ValueChangeEvent event) {
		init();
		
		int nuevoTypeDestinoRedis = ((Integer) event.getNewValue()).intValue();

		try {
			if (selectedResults == null || selectedResults.length == 0) {
				ValidationBo.showDialog("", new FacesMessage( FacesMessage.SEVERITY_WARN, "", "Debe seleccionar mínimo un registro"));
			} else {
				List<Integer> orgIds = new ArrayList<Integer>();
				
				for (RowSearchOutputRegisterBean row : selectedResults) {
					
					if (row.getFld8() != null && row.getFld8().getId() != null) {
						orgIds.add(row.getFld8().getId());
					}
				}
				
				this.listUsuarios = null;
				this.setSelectdestinoRedisUsuario(null);
				this.listDepartament = null;
				this.setSelectdestinoRedisDepartamentos(null);
				this.listGrupos = null;
				this.setSelectdestinoRedisGrupos(null);
				
				if (orgIds != null && orgIds.size() > 0) {
					
					if(1 == nuevoTypeDestinoRedis){
						this.listUsuarios = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
						this.setSelectdestinoRedisUsuario(Utils.defaultUser(this.listUsuarios, orgIds.get(0)));
					} else if( 2 == nuevoTypeDestinoRedis){
						this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
						this.setSelectdestinoRedisDepartamentos(Utils.defaultDepart(this.listDepartament, orgIds.get(0)));
					} else {
						this.listGrupos = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), orgIds);
						this.setSelectdestinoRedisGrupos(Utils.defaultGroup(this.listGrupos, orgIds.get(0)));
					}
					
				} else {
					
					if(1 == nuevoTypeDestinoRedis){
						this.listUsuarios = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), null);
						this.setSelectdestinoRedisUsuario(null);
					} else if( 2 == nuevoTypeDestinoRedis){
						this.listDepartament = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), null);
						this.setSelectdestinoRedisDepartamentos(null);
					} else {
						this.listGrupos = ValidationListBo.getDeptsGroupsUsers(useCaseConf.getSessionID(), nuevoTypeDestinoRedis, useCaseConf.getEntidadId(), null);
						this.setSelectdestinoRedisGrupos(null);
					}
				}
			}
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, sessionException);
			Utils.redirectToErrorPage(null, sessionException, null);
			
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.GET_INFORMATION_LISTS_ERROR_MESSAGE, validationException);
			Utils.redirectToErrorPage(null, validationException, null);
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

	public DocumentoBean getDocumentoBean() {
		return documentoBean;
	}

	public void setDocumentoBean(DocumentoBean documentoBean) {
		this.documentoBean = documentoBean;
	}
	
	public void consultaDocumento(){
        DocumentoBo.consultaDocumento(documentoBean);        
	}
	
}