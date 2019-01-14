/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.businessobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.DistributionException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxSfOut;
import com.ieci.tecdoc.common.isicres.AxSfQuery;
import com.ieci.tecdoc.common.isicres.AxSfQueryResults;
import com.ieci.tecdoc.common.isicres.AxXf;
import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.desktopweb.utils.RBUtil;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.Validator;

import es.msssi.sgm.registropresencial.beans.RowSearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchDistributionBean;
import es.msssi.sgm.registropresencial.beans.SearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase que implementa la interfaz IGenericBo que contiene los métodos
 * relacionados con la búsqueda de registros de salida.
 * 
 * @author cmorenog
 */
public class SearchOutputRegisterBo extends LazyDataModel<RowSearchOutputRegisterBean> implements IGenericBo, Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(SearchOutputRegisterBo.class);
	
	/** Variable con la configuración de la aplicación. */
	private UseCaseConf useCaseConf = null;
	/** contexto de faces. */
	private FacesContext facesContext;
	/** Bean con los criterios del buscador. */
	private SearchOutputRegisterBean searchOutputRegister = new SearchOutputRegisterBean();
	/** página actual. */
	private int page;
	/** libro en sesión. */
	private ScrRegstate book;
	/** si se trata de la búsqueda del último registro introducido. */
	private boolean lastRegister = false;
	/** Lista de interesados. */
	private InterestedBo interestedBo;
	private DistributionBo distributionBo;
	private RegInterchangeBo regInterchangeBo;

	/**
	 * Constructor.
	 */
	public SearchOutputRegisterBo() {
		super();
		interestedBo = new InterestedBo();
		openQuery();
	}

	/**
	 * Inicializa el contexto de faces.
	 */
	private void init() {
		LOG.trace("Entrando en SearchOutputRegisterBo.init()" + " para iniciar el contexto de faces.");
		
		if (facesContext == null) {
			facesContext = FacesContext.getCurrentInstance();
			Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
			useCaseConf = (UseCaseConf) map.get(Keys.J_USECASECONF);
			book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
		}
	}

	/**
	 * Sobreescribe el load de LazyDataModel y gestiona la carga del datatable.
	 * 
	 * @param first
	 *            identificador primer registro de la paginación.
	 * @param pageSize
	 *            número de registro que se enseña en la paginación.
	 * @param sortField
	 *            campo por el que se ordena.
	 * @param sortOrder
	 *            Criterio de ordenación.
	 * @param filters
	 *            filtros de búsqueda.
	 * 
	 * @return lista de los resultados de la búsqueda.
	 */
	@Override
	public List<RowSearchOutputRegisterBean> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		
		LOG.trace("Entrando en SearchOutpurRegisterBo.load()");
		
		List<RowSearchOutputRegisterBean> data = null;

		// Si es la primera carga no se realiza la búsqueda
		data = new ArrayList<RowSearchOutputRegisterBean>();
		int firstVar = first;
		String orderBy = "";

		AxSfQuery axsfQuery = null;

		if (lastRegister) {
			try {
				init();
				AxSfQueryResults queryResults = FolderSession.getLastRegisterForUser(useCaseConf.getSessionID(), book.getIdocarchhdr().getId(), useCaseConf.getLocale(), useCaseConf.getEntidadId());
				this.setRowCount(queryResults.getPageSize());
				data = loadQueryResulttoList(queryResults, useCaseConf.getLocale());
				
			} catch (ValidationException validationException) {
				LOG.error(ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, validationException);
				
			} catch (BookException bookException) {
				LOG.error(ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
				
			} catch (SessionException sessionException) {
				LOG.error(ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, sessionException);
			}
			
		} else {
			if (FacesContext.getCurrentInstance().getMessageList() != null && FacesContext.getCurrentInstance().getMessageList().size() > 0) {

				RequestContext.getCurrentInstance().addCallbackParam( "isErrors", true);
				this.setRowCount(0);
				data = new ArrayList<RowSearchOutputRegisterBean>();
				
			} else {
				try {
					axsfQuery = loadAsSfQuery(searchOutputRegister, book.getIdocarchhdr().getId());

					if (sortField == null) {
						sortField = "fld2 DESC";
						orderBy = sortField;
					} else {
						orderBy = sortField;
						if (sortOrder != null) {
							if ("ASCENDING".equals(sortOrder.name())) {
								orderBy += " ASC ";
							} else if ("DESCENDING".equals(sortOrder.name())) {
								orderBy += " DESC ";
							}
						}
					}
					axsfQuery.setOrderBy(orderBy);
				} catch (BookException bookException) {
					LOG.error( ErrorConstants.LOAD_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, bookException);
				}

				try {
					if (firstVar != 0) {
						firstVar++;
					}
					AxSfQueryResults queryResults = FolderSession.navigateToRowRegistersQuery(useCaseConf.getSessionID(), book.getIdocarchhdr().getId(), firstVar, useCaseConf.getLocale(), useCaseConf.getEntidadId(), orderBy);
					data = loadQueryResulttoList(queryResults,useCaseConf.getLocale());
					
				} catch (ValidationException validationException) {
					LOG.error( ErrorConstants.NAVIGATE_TO_OUTPUT_REGISTERS_ROW_ERROR_MESSAGE, validationException);
					// Utils.redirectToErrorPage(null, validationException,
					// null);
					
				} catch (BookException bookException) {
					LOG.error( ErrorConstants.NAVIGATE_TO_OUTPUT_REGISTERS_ROW_ERROR_MESSAGE, bookException);					
					// Utils.redirectToErrorPage(null, bookException, null);
					
				} catch (SessionException sessionException) {
					LOG.error( ErrorConstants.NAVIGATE_TO_OUTPUT_REGISTERS_ROW_ERROR_MESSAGE, sessionException);
					// Utils.redirectToErrorPage(null, sessionException, null);
				}
			}
		}
		return data;
	}

	/**
	 * Mapea los resultados de las búsquedas de SIGM con el nuevo aplicativo.
	 * 
	 * @param queryResults
	 *            objeto del core de sigem que contiene los resultados de la
	 *            búsqueda.
	 * @param locale
	 *            el locale de la búsqueda.
	 * 
	 * @return data Lista con los resultados de la búsqueda.
	 */
	@SuppressWarnings("unchecked")
	private List<RowSearchOutputRegisterBean> loadQueryResulttoList( AxSfQueryResults queryResults, Locale locale) {
		
		LOG.trace("Entrando en SearchOutputRegisterBo.loadQueryResulttoList()");
		
		List<RowSearchOutputRegisterBean> data = new ArrayList<RowSearchOutputRegisterBean>();
		AxSfOut axSfOut;
		RowSearchOutputRegisterBean bean;
		
		for (Iterator<AxSfOut> it = queryResults.getResults().iterator(); it.hasNext();) {
			
			axSfOut = (AxSfOut) it.next();
			bean = new RowSearchOutputRegisterBean();
			bean.setFdrid(new Integer(axSfOut.getAttributeValueAsString("fdrid")));
			bean.setFld1(axSfOut.getAttributeValueAsString("fld1"));
			bean.setFld2((Date) axSfOut.getAttributeValue("fld2"));
			bean.setFld3(axSfOut.getAttributeValueAsString("fld3"));
			bean.setFld5(axSfOut.getFld5());
			bean.setFld5Name(axSfOut.getFld5Name());
			bean.setFld6(axSfOut.getAttributeValueAsString("fld6"));
			bean.setFld6Name(RBUtil.getInstance(locale).getProperty("book.fld6." + axSfOut.getAttributeValueAsString("fld6")));
			bean.setFld7(axSfOut.getFld7());
			bean.setFld7Name(axSfOut.getFld7Name());
			bean.setFld8(axSfOut.getFld8());
			bean.setFld8Name(axSfOut.getFld8Name());
			bean.setFld9(interestedBo.fillSenderFieldFromSenderListToRelationsReport( bean.getFdrid(), book.getIdocarchhdr().getId(), useCaseConf));
			bean.setFld12(axSfOut.getFld12());
			
			bean.setFld12Name((axSfOut.getFld12() != null) ? axSfOut.getFld12().getCode() : null);
			
			bean.setFld13(axSfOut.getAttributeValueAsString("fld13"));
			bean.setFld503(axSfOut.getAttributeValueAsString("fld503"));
			bean.setFld504(axSfOut.getAttributeValueAsString("fld504"));
			bean.setFld505(axSfOut.getAttributeValueAsString("fld505"));
			bean.setFld506(axSfOut.getAttributeValueAsString("fld506"));
			
			if (axSfOut.getExtendedFields() != null) {
				bean.setFld1004(axSfOut.getExtendedFields().get(1004) != null ? ((AxXf) axSfOut.getExtendedFields().get(1004)).getText() : null);
			}
			
			bean.setStateDis(getStateDis( useCaseConf, axSfOut.getAttributeValueAsString("fld1")));
			bean.setStateOutput(getStateOutput( axSfOut.getAttributeValueAsString("fdrid"), String.valueOf(axSfOut.getFld5().getId())));

			data.add(bean);
		}
		return data;
	}

	/**
	 * Abre la query en SIGM.
	 */
	@SuppressWarnings("unchecked")
	public void openQuery() {
		LOG.trace("Entrando en SearchOutputRegisterBo.openQuery()");
		init();
		AxSfQuery axsfQuery = null;
		
		try {
			axsfQuery = loadAsSfQuery(searchOutputRegister, book.getIdocarchhdr().getId());
			axsfQuery.setOrderBy(" fld1 DESC");
			
		} catch (BookException bookException) {
			LOG.error(ErrorConstants.OPEN_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, bookException);
			Utils.redirectToErrorPage(null, bookException, null);
		}
		
		@SuppressWarnings("rawtypes")
		List bookIds = new ArrayList();
		bookIds.add(book.getIdocarchhdr().getId());

		try {
			int size = FolderSession.openRegistersQuery( useCaseConf.getSessionID(), axsfQuery, bookIds, 0, useCaseConf.getEntidadId());
			this.setRowCount(size);
			
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.OPEN_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, validationException);
			Utils.redirectToErrorPage(null, validationException, null);
			
		} catch (BookException bookException) {
			LOG.error(ErrorConstants.OPEN_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, bookException);
			Utils.redirectToErrorPage(null, bookException, null);
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.OPEN_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, sessionException);
			Utils.redirectToErrorPage(null, sessionException, null);
		}
	}

	/**
	 * Cierra la query en SIGM.
	 */
	public void closeQuery() {
		
		LOG.trace("Entrando en SearchOutputRegisterBo.closeQuery()");
		
		try {
			init();
			FolderSession.closeRegistersQuery(useCaseConf.getSessionID(), book.getIdocarchhdr().getId());
			
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.CLOSE_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, validationException);
			Utils.redirectToErrorPage(null, validationException, null);
			
		} catch (BookException bookException) {
			LOG.error(ErrorConstants.CLOSE_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, bookException);
			Utils.redirectToErrorPage(null, bookException, null);
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.CLOSE_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, sessionException);
			Utils.redirectToErrorPage(null, sessionException, null);
		}
	}

	/**
	 * Carga los criterios de búsqueda del formulario en el objeto que forma la
	 * query.
	 * 
	 * @param searchOutputRegister
	 *            Criterios de búsqueda.
	 * @param idBook
	 *            Id del libro.
	 * 
	 * @return axsfQuery Query completa con los criterios introducidos.
	 * 
	 * @throws BookException
	 *             si ha habido algún problema con el libro de registro.
	 */
	private AxSfQuery loadAsSfQuery( SearchOutputRegisterBean searchOutputRegister, Integer idBook) throws BookException {
		LOG.trace("Entrando en SearchOutputRegisterBo.loadAsSfQuery()");
		AxSfQuery axsfQuery = new AxSfQuery();

		axsfQuery.setBookId(idBook);
		axsfQuery.setPageSize(Integer.parseInt(Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_DEFAULT_PAGE_TABLE_RESULTS_SIZE)));

		try {
			/* número de registro */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld1", idBook));
			/* fecha de registro */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld2", idBook));
			/* usuario */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld3", idBook));
			/* Fecha de Trabajo */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld4", idBook));
			/* Oficina de Registro */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld5", idBook));
			/* Estado */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld6", idBook));
			/* Origen */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld7", idBook));
			/* Destino */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld8", idBook));
			/* Remitentes */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld9", idBook));
			/* Tipos de Transporte */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld10", idBook));
			/* Numero Transporte */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld11", idBook));
			/* Tipos de Asunto */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld12", idBook));
			/* Resumen */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld13", idBook));
			/* Comentario */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld14", idBook));
			/* observaciones */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld507", idBook));
			/* SIR */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld503", idBook));
			/* rojos */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld504", idBook));
			/* naranjas */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld505", idBook));
			/* verdes */
			axsfQuery.addField(searchOutputRegister.fieldtoQuery("fld506", idBook));

		} catch (IllegalArgumentException illegalArgumentException) {
			LOG.error(ErrorConstants.LOAD_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, illegalArgumentException);
			Utils.redirectToErrorPage(null, null, illegalArgumentException);
		}

		if (axsfQuery.getPageSize() <= 0) {
			throw new BookException(BookException.ERROR_PAGE_SIZE);
		}
		return axsfQuery;
	}

	/**
	 * Devuelve con un identificador el objeto fila correspondiente del
	 * datatable.
	 * 
	 * @param rowKey
	 *            Identificador de la fila a devolver.
	 * 
	 * @return Fila del datatable con el identificador solicitado.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RowSearchOutputRegisterBean getRowData(String rowKey) {
		
		LOG.trace("Entrando en SearchOutputRegisterBo.getRowData()");
		
		RowSearchOutputRegisterBean result = null;
		List<RowSearchOutputRegisterBean> rowSearchOutputRegisterBeans = (List<RowSearchOutputRegisterBean>) getWrappedData();

		Iterator<RowSearchOutputRegisterBean> iterator = rowSearchOutputRegisterBeans.iterator();
		RowSearchOutputRegisterBean rowSearchOutputRegisterBean;
		
		while (iterator.hasNext() && result == null) {
			rowSearchOutputRegisterBean = iterator.next();
			
			if (rowSearchOutputRegisterBean.getFdrid().equals(new Integer(rowKey))) {
				result = rowSearchOutputRegisterBean;
			}
		}
		return result;
	}

	/**
	 * Devuelve el identificador de la fila pasada como parámetro.
	 * 
	 * @param rowSearchOutputRegisterBean
	 *            objeto fila.
	 * 
	 * @return rowSearchOutputRegisterBean.getFdrid() El identificador de la
	 *         fila.
	 */
	@Override
	public Object getRowKey( RowSearchOutputRegisterBean rowSearchOutputRegisterBean) {
		LOG.trace("Entrando en SearchOutputRegisterBo.getRowKey()");
		return rowSearchOutputRegisterBean.getFdrid();
	}

	/**
	 * Introduce el número de fila seleccionada.
	 * 
	 * @param rowIndex
	 *            fila seleccionada.
	 */
	@Override
	public void setRowIndex(int rowIndex) {
		LOG.trace("Entrando en SearchOutputRegisterBo.setRowIndex()");
		if (rowIndex == -1 || getPageSize() == 0) {
			super.setRowIndex(-1);
		} else {
			super.setRowIndex(rowIndex % getPageSize());
		}
	}

	/**
	 * Método que devuelve el estado de distribución de un registro.
	 * 
	 * @param useCaseConf
	 *            configuración de la aplicación.
	 * @param fld1
	 *            codigo del registro.
	 * @return estado del registro.
	 * @throws ValidationException
	 *             error en la validación.
	 * @throws DistributionException
	 *             error en la distribución.
	 * @throws SessionException
	 *             error de sesión
	 */
	private Integer getStateDis(UseCaseConf useCaseConf, String fld1) {
		Integer result = null;
		try {
			Validator.validate_String_NotNull_LengthMayorZero( useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);

			if (distributionBo == null) {
				distributionBo = new DistributionBo();
			}
			
			SearchDistributionBean searchDistributionRegister = new SearchDistributionBean();
			boolean isOfficeAsoc = Boolean.valueOf( Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_DISTRIBUTION_OFFICE_ASOC)).booleanValue();
			

			searchDistributionRegister.setFld1(fld1);
			searchDistributionRegister.setState(0);
			searchDistributionRegister.setType(2);
			result = distributionBo.getStateDis(useCaseConf, searchDistributionRegister, isOfficeAsoc);
			
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.GET_STATE_INPUT_REGISTER_ERROR_MESSAGE, validationException);
			
		} catch (DistributionException distributionException) {
			LOG.error(ErrorConstants.GET_STATE_INPUT_REGISTER_ERROR_MESSAGE, distributionException);
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.GET_STATE_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
		}

		return result;
	}

	/**
	 * Método que devuelve el estado de IR salida de un registro.
	 * 
	 * @param idRegister
	 *            codigo del registro.
	 * @return estado del registro.
	 * @throws ValidationException
	 *             error en la validación.
	 * @throws DistributionException
	 *             error en la distribución.
	 * @throws SessionException
	 *             error de sesión
	 */
	private Integer getStateOutput(String idRegister, String idOffice) {
		Integer result = null;
		
		try {
			
			if (regInterchangeBo == null) {
				regInterchangeBo = new RegInterchangeBo();
			}
			
			LOG.info("Cargando histórico de Intecambio Registral del registro " + idRegister);

			result = regInterchangeBo.getStateIROutput( String.valueOf(book.getIdocarchhdr().getId()), String.valueOf(idRegister), idOffice);

		} catch (RPRegistralExchangeException rPRegisterException) {
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, rPRegisterException);
		}

		return result;
	}

	/**
	 * Obtiene el valor del parámetro searchOutputRegister.
	 * 
	 * @return searchOutputRegister valor del campo a obtener.
	 */
	public SearchOutputRegisterBean getSearchOutputRegister() {
		return searchOutputRegister;
	}

	/**
	 * Guarda el valor del parámetro searchOutputRegister.
	 * 
	 * @param searchOutputRegister
	 *            del campo a guardar.
	 */
	public void setSearchOutputRegister( SearchOutputRegisterBean searchOutputRegister) {
		this.searchOutputRegister = searchOutputRegister;
	}

	/**
	 * Obtiene el valor del parámetro page.
	 * 
	 * @return page valor del campo a obtener.
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Guarda el valor del parámetro page.
	 * 
	 * @param page
	 *            valor del campo a guardar.
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * Obtiene el valor del parámetro lastRegister.
	 * 
	 * @return lastRegister valor del campo a obtener.
	 */
	public boolean isLastRegister() {
		return lastRegister;
	}

	/**
	 * Guarda el valor del parámetro lastRegister.
	 * 
	 * @param lastRegister
	 *            valor del campo a guardar.
	 */
	public void setLastRegister(boolean lastRegister) {
		this.lastRegister = lastRegister;
	}
}
