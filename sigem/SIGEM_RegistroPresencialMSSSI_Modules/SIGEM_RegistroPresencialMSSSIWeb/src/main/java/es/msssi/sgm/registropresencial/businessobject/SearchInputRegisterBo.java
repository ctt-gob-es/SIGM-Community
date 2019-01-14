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
import com.ieci.tecdoc.common.isicres.AxSfIn;
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
import es.msssi.sgm.registropresencial.beans.RowSearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchDistributionBean;
import es.msssi.sgm.registropresencial.beans.SearchInputRegisterBean;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase que implementa la interfaz IGenericBo que contiene los métodos
 * relacionados con la búsqueda de registros de entrada.
 * 
 * @author cmorenog
 */
public class SearchInputRegisterBo extends LazyDataModel<RowSearchInputRegisterBean> implements IGenericBo, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(SearchInputRegisterBo.class);

	/** Variable con la configuración de la aplicación. */
	private UseCaseConf useCaseConf = null;
	/** contexto de faces. */
	private FacesContext facesContext;
	/** Bean con los criterios del buscador. */
	private SearchInputRegisterBean searchInputRegister = new SearchInputRegisterBean();
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
	public SearchInputRegisterBo() {
		super();
		interestedBo = new InterestedBo();
		openQuery();
	}

	/**
	 * Inicializa el contexto de faces.
	 */
	private void init() {
		LOG.trace("Entrando en SearchInputRegisterBo.init()" + " para iniciar el contexto de faces.");

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
	public List<RowSearchInputRegisterBean> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		
		LOG.trace("Entrando en SearchInputRegisterBo.load()");
		
		List<RowSearchInputRegisterBean> data = null;

		// Si es la primera carga no se realiza la búsqueda
		data = new ArrayList<RowSearchInputRegisterBean>();
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
				LOG.error(ErrorConstants.GET_INPUT_REGISTER_ERROR_MESSAGE, validationException);
				// Utils.redirectToErrorPage(null, validationException, null);
				
			} catch (BookException bookException) {
				LOG.error(ErrorConstants.GET_INPUT_REGISTER_ERROR_MESSAGE, bookException);
				// Utils.redirectToErrorPage(null, bookException, null);
				
			} catch (SessionException sessionException) {
				LOG.error(ErrorConstants.GET_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
				// Utils.redirectToErrorPage(null, sessionException, null);
			}
			
		} else {
			
			if (FacesContext.getCurrentInstance().getMessageList() != null && FacesContext.getCurrentInstance().getMessageList().size() > 0) {

				RequestContext.getCurrentInstance().addCallbackParam("isErrors", true);
				this.setRowCount(0);
				data = new ArrayList<RowSearchInputRegisterBean>();
				
			} else {
				try {
					axsfQuery = loadAsSfQuery(searchInputRegister, book.getIdocarchhdr().getId());

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
					LOG.error( ErrorConstants.LOAD_INPUT_REGISTER_QUERY_ERROR_MESSAGE, bookException);
					// Utils.redirectToErrorPage(null, bookException, null);
				}

				try {
					if (firstVar != 0) {
						firstVar++;
					}
					
					AxSfQueryResults queryResults = FolderSession.navigateToRowRegistersQuery(useCaseConf.getSessionID(), book.getIdocarchhdr().getId(), firstVar, useCaseConf.getLocale(), useCaseConf.getEntidadId(), orderBy);
					
					data = loadQueryResulttoList(queryResults, useCaseConf.getLocale());
					
				} catch (ValidationException validationException) {
					LOG.error( ErrorConstants.NAVIGATE_TO_INPUT_REGISTERS_ROW_ERROR_MESSAGE, validationException);
					// Utils.redirectToErrorPage(null, validationException, null);
					
				} catch (BookException bookException) {
					LOG.error( ErrorConstants.NAVIGATE_TO_INPUT_REGISTERS_ROW_ERROR_MESSAGE, bookException);
					// Utils.redirectToErrorPage(null, bookException, null);
					
				} catch (SessionException sessionException) {
					LOG.error( ErrorConstants.NAVIGATE_TO_INPUT_REGISTERS_ROW_ERROR_MESSAGE, sessionException);
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
	private List<RowSearchInputRegisterBean> loadQueryResulttoList( AxSfQueryResults queryResults, Locale locale) {
		
		LOG.trace("Entrando en SearchInputRegisterBo.loadQueryResulttoList()");
		
		List<RowSearchInputRegisterBean> data = new ArrayList<RowSearchInputRegisterBean>();		
		AxSfIn axSfIn;
		RowSearchInputRegisterBean bean;
		
		for (Iterator<AxSfIn> it = queryResults.getResults().iterator(); it.hasNext();) {
			
			axSfIn = (AxSfIn) it.next();
			
			bean = new RowSearchInputRegisterBean();
			bean.setFdrid(new Integer(axSfIn.getAttributeValueAsString("fdrid")));
			bean.setFld1(axSfIn.getAttributeValueAsString("fld1"));
			bean.setFld2((Date) axSfIn.getAttributeValue("fld2"));
			bean.setFld3(axSfIn.getAttributeValueAsString("fld3"));
			bean.setFld5(axSfIn.getFld5());
			bean.setFld5Name(axSfIn.getFld5Name());
			bean.setFld6(axSfIn.getAttributeValueAsString("fld6"));
			bean.setFld6Name(RBUtil.getInstance(locale).getProperty( "book.fld6." + axSfIn.getAttributeValueAsString("fld6")));
			bean.setFld7(axSfIn.getFld7());
			bean.setFld7Name(axSfIn.getFld7Name());
			bean.setFld8(axSfIn.getFld8());
			bean.setFld8Name(axSfIn.getFld8Name());
			bean.setFld9(interestedBo.fillSenderFieldFromSenderListToRelationsReport( bean.getFdrid(), book.getIdocarchhdr().getId(), useCaseConf));
			bean.setFld13(axSfIn.getFld13());			
			bean.setFld13Name((axSfIn.getFld13() != null) ? axSfIn.getFld13().getName() : null);
			bean.setFld16(axSfIn.getFld16());
			bean.setFld16Name((axSfIn.getFld16() != null) ? axSfIn.getFld16().getCode() : null);
			bean.setFld17(axSfIn.getAttributeValueAsString("fld17"));
			bean.setFld19(axSfIn.getAttributeValueAsString("fld19"));
			bean.setFld10(axSfIn.getAttributeValueAsString("fld10"));
			bean.setFld1002(axSfIn.getAttributeValueAsString("fld1002"));
			bean.setFld503(axSfIn.getAttributeValueAsString("fld503"));
			bean.setFld504(axSfIn.getAttributeValueAsString("fld504"));
			bean.setFld505(axSfIn.getAttributeValueAsString("fld505"));
			bean.setFld506(axSfIn.getAttributeValueAsString("fld506"));
			
			if (axSfIn.getExtendedFields() != null) {
				bean.setFld1001(axSfIn.getExtendedFields().get(1001) != null ? ((AxXf) axSfIn.getExtendedFields().get(1001)).getText() : null);
				bean.setFld1004(axSfIn.getExtendedFields().get(1004) != null ? ((AxXf) axSfIn.getExtendedFields().get(1004)).getText() : null);
			}
			
			bean.setStateDis(getStateDis(useCaseConf, axSfIn.getAttributeValueAsString("fld1")));
			bean.setStateOutput(getStateOutput( axSfIn.getAttributeValueAsString("fdrid"), String.valueOf(axSfIn.getFld5().getId())));
			data.add(bean);
		}
		return data;
	}

	/**
	 * Abre la query en SIGM.
	 */
	@SuppressWarnings("unchecked")
	public void openQuery() {
		
		LOG.trace("Entrando en SearchInputRegisterBo.openQuery()");
		init();
		AxSfQuery axsfQuery = null;
		
		try {
			axsfQuery = loadAsSfQuery(searchInputRegister, book.getIdocarchhdr().getId());
			axsfQuery.setOrderBy(" fld1 DESC");
			
		} catch (BookException bookException) {
			LOG.error(ErrorConstants.OPEN_INPUT_REGISTER_QUERY_ERROR_MESSAGE, bookException);
			Utils.redirectToErrorPage(null, bookException, null);
		}
		
		@SuppressWarnings("rawtypes")
		List bookIds = new ArrayList();
		bookIds.add(book.getIdocarchhdr().getId());

		try {
			int size = FolderSession.openRegistersQuery( useCaseConf.getSessionID(), axsfQuery, bookIds, 0, useCaseConf.getEntidadId());
			this.setRowCount(size);
			
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.OPEN_INPUT_REGISTER_QUERY_ERROR_MESSAGE, validationException);
			Utils.redirectToErrorPage(null, validationException, null);
			
		} catch (BookException bookException) {
			LOG.error(ErrorConstants.OPEN_INPUT_REGISTER_QUERY_ERROR_MESSAGE, bookException);
			Utils.redirectToErrorPage(null, bookException, null);
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.OPEN_INPUT_REGISTER_QUERY_ERROR_MESSAGE, sessionException);
			Utils.redirectToErrorPage(null, sessionException, null);
		}
	}

	/**
	 * Cierra la query en SIGM.
	 */
	public void closeQuery() {
		LOG.trace("Entrando en SearchInputRegisterBo.closeQuery()");
		
		try {
			init();
			FolderSession.closeRegistersQuery(useCaseConf.getSessionID(), book.getIdocarchhdr().getId());
			
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.CLOSE_INPUT_REGISTER_QUERY_ERROR_MESSAGE, validationException);
			Utils.redirectToErrorPage(null, validationException, null);
			
		} catch (BookException bookException) {
			LOG.error(ErrorConstants.CLOSE_INPUT_REGISTER_QUERY_ERROR_MESSAGE, bookException);
			Utils.redirectToErrorPage(null, bookException, null);
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.CLOSE_INPUT_REGISTER_QUERY_ERROR_MESSAGE, sessionException);
			Utils.redirectToErrorPage(null, sessionException, null);
		}
	}

	/**
	 * Carga los criterios de búsqueda del formulario en el objeto que forma la
	 * query.
	 * 
	 * @param searchInputRegister
	 *            Criterios de búsqueda.
	 * @param idBook
	 *            Id del libro.
	 * 
	 * @return axsfQuery Query completa con los criterios introducidos.
	 * 
	 * @throws BookException
	 *             si ha habido algún problema con el libro de registro.
	 */
	private AxSfQuery loadAsSfQuery( SearchInputRegisterBean searchInputRegister, Integer idBook) throws BookException {
		LOG.trace("Entrando en SearchInputRegisterBo.loadAsSfQuery()");
		AxSfQuery axsfQuery = new AxSfQuery();

		axsfQuery.setBookId(idBook);
		axsfQuery.setPageSize(Integer.parseInt(Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_DEFAULT_PAGE_TABLE_RESULTS_SIZE)));

		try {
			/* número de registro */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld1", idBook));
			/* fecha de registro */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld2", idBook));
			/* usuario */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld3", idBook));
			/* Fecha de Trabajo */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld4", idBook));
			/* Oficina de Registro */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld5", idBook));
			/* Estado */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld6", idBook));
			/* Origen */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld7", idBook));
			/* Destino */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld8", idBook));
			/* Remitentes */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld9", idBook));
			/* Tipos de Asunto */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld14", idBook));
			/* Resumen */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld15", idBook));
			/* Tipos de Asunto */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld16", idBook));
			/* Resumen */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld17", idBook));
			/* Referencia Expediente */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld19", idBook));
			/* Número de registro original */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld10", idBook));
			/* Impreso */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld1001", idBook));
			/* SIR */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld503", idBook));
			/* Comentario */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld18", idBook));
			/* observaciones */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld507", idBook));
			/* flag */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld1002", idBook));
			/* rojos */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld504", idBook));
			/* naranjas */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld505", idBook));
			/* verdes */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld506", idBook));
			
		} catch (IllegalArgumentException illegalArgumentException) {
			
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_QUERY_ERROR_MESSAGE, illegalArgumentException);
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
	public RowSearchInputRegisterBean getRowData(String rowKey) {
		
		LOG.trace("Entrando en SearchInputRegisterBo.getRowData()");
		
		RowSearchInputRegisterBean result = null;
		List<RowSearchInputRegisterBean> rowSearchInputRegisterBeans = (List<RowSearchInputRegisterBean>) getWrappedData();

		Iterator<RowSearchInputRegisterBean> iterator = rowSearchInputRegisterBeans.iterator();
		
		RowSearchInputRegisterBean rowSearchInputRegisterBean;
		
		while (iterator.hasNext() && result == null) {
			rowSearchInputRegisterBean = iterator.next();
			
			if (rowSearchInputRegisterBean.getFdrid().equals( new Integer(rowKey))) {
				result = rowSearchInputRegisterBean;
			}
		}
		return result;
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
			
			boolean isOfficeAsoc = Boolean.valueOf( Configurator.getInstance().getProperty( ConfigurationKeys.KEY_DESKTOP_DISTRIBUTION_OFFICE_ASOC)).booleanValue();

			searchDistributionRegister.setFld1(fld1);
			searchDistributionRegister.setState(0);
			searchDistributionRegister.setType(2);
			
			result = distributionBo.getStateDis(useCaseConf,searchDistributionRegister, isOfficeAsoc);
			
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
	 * Devuelve el identificador de la fila pasada como parámetro.
	 * 
	 * @param rowSearchInputRegisterBean
	 *            objeto fila.
	 * 
	 * @return rowSearchInputRegisterBean.getFdrid() El identificador de la
	 *         fila.
	 */
	@Override
	public Object getRowKey( RowSearchInputRegisterBean rowSearchInputRegisterBean) {
		
		LOG.trace("Entrando en SearchInputRegisterBo.getRowKey()");
		
		return rowSearchInputRegisterBean.getFdrid();
	}

	/**
	 * Introduce el número de fila seleccionada.
	 * 
	 * @param rowIndex
	 *            fila seleccionada.
	 */
	@Override
	public void setRowIndex(int rowIndex) {
		
		LOG.trace("Entrando en SearchInputRegisterBo.setRowIndex()");
		
		if (rowIndex == -1 || getPageSize() == 0) {
			super.setRowIndex(-1);
		} else {
			super.setRowIndex(rowIndex % getPageSize());
		}
	}

	/**
	 * Obtiene el valor del parámetro searchInputRegister.
	 * 
	 * @return searchInputRegister valor del campo a obtener.
	 */
	public SearchInputRegisterBean getSearchInputRegister() {
		return searchInputRegister;
	}

	/**
	 * Guarda el valor del parámetro searchInputRegister.
	 * 
	 * @param searchInputRegister
	 *            del campo a guardar.
	 */
	public void setSearchInputRegister( SearchInputRegisterBean searchInputRegister) {
		this.searchInputRegister = searchInputRegister;
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
