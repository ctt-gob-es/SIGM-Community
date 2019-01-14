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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.ieci.tecdoc.common.exception.AttributesException;
import com.ieci.tecdoc.common.exception.DistributionException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.Validator;

import es.msssi.sgm.registropresencial.beans.SearchUnitsBean;
import es.msssi.sgm.registropresencial.beans.UnitsResultsBean;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;

/**
 * Clase q implementa IGenericBo que contiene los métodos relacionados con la
 * búsqueda de registros distribuidos.
 * 
 * @author cmorenog
 */
public class SearchUnitsBo extends LazyDataModel<ScrOrg> implements IGenericBo {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(SearchUnitsBo.class.getName());
	
	/** Variable con la configuración de la aplicación. */
	private UseCaseConf useCaseConf = null;
	/** contexto de faces. */
	private FacesContext facesContext;
	private SearchUnitsBean searchUnitsBean;
	/** página actual. */
	private int page;
	/** objeto clase de negocio. */
	private UnitsBo unitsBo;
	/** número de resultados por página. */
	private int pageSize = 6;

	/**
	 * Constructor.
	 */
	public SearchUnitsBo() {
		super();
		searchUnitsBean = new SearchUnitsBean();
		/*
		 * this.pageSize =
		 * Integer.parseInt(Configurator.getInstance().getProperty(
		 * ConfigurationKeys.KEY_DESKTOP_DEFAULT_PAGE_TABLE_RESULTS_SIZE));
		 */

		this.setRowCount(0);
		this.setPage(0);
	}

	/**
	 * Inicializa el contexto de faces.
	 */
	private void init() {
		if (facesContext == null) {
			facesContext = FacesContext.getCurrentInstance();
			Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
			useCaseConf = (UseCaseConf) map.get(Keys.J_USECASECONF);
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
	public List<ScrOrg> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		
		List<ScrOrg> data = null;
		int firstVar = first;
		data = new ArrayList<ScrOrg>();
		init();

		try {
			UnitsResultsBean result = getUnits(useCaseConf, firstVar);
			this.setRowCount(result.getTotalSize());
			data = result.getRows();
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.GET_LIST_UNIT_REGISTER_ERROR_MESSAGE,
					sessionException);
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.GET_LIST_UNIT_REGISTER_ERROR_MESSAGE,
					validationException);
		} catch (AttributesException attributesException) {
			LOG.error(ErrorConstants.GET_LIST_UNIT_REGISTER_ERROR_MESSAGE,
					attributesException);
		}
		return data;
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
	@Override
	public ScrOrg getRowData(String rowKey) {
		
		@SuppressWarnings("unchecked")
		List<ScrOrg> rowSearchUnitsBean = (List<ScrOrg>) getWrappedData();
		ScrOrg result = null;

		Iterator<ScrOrg> iterator = rowSearchUnitsBean.iterator();
		ScrOrg rowSearchUnitBean;
		while (iterator.hasNext() && result == null) {
			rowSearchUnitBean = iterator.next();
			if (rowSearchUnitBean.getId().equals(new Integer(rowKey))) {
				result = rowSearchUnitBean;
			}
		}
		return result;
	}

	/**
	 * Devuelve el identificador de la fila pasada como parámetro.
	 * 
	 * @param rowSearchUnitsBean
	 *            objeto fila.
	 * 
	 * @return rowSearchUnitsBean.getId() El identificador de la fila.
	 */
	@Override
	public Object getRowKey(ScrOrg rowSearchUnitsBean) {
		return rowSearchUnitsBean.getId();
	}

	/**
	 * Introduce el número de fila seleccionada.
	 * 
	 * @param rowIndex
	 *            fila seleccionada.
	 */
	@Override
	public void setRowIndex(int rowIndex) {
		if (rowIndex == -1 || getPageSize() == 0) {
			super.setRowIndex(-1);
		} else {
			super.setRowIndex(rowIndex % getPageSize());
		}
	}

	/**
	 * Método que construye la query y controla los permisos para la búsqueda de
	 * unidades.
	 * 
	 * @param useCaseConf
	 *            configuración de la aplicación.
	 * @param firstRow
	 *            primera fila.
	 * @param sortField
	 *            campo que ordena.
	 * @return objeto con los datos necesarios para la búsqueda.
	 * @throws ValidationException
	 *             error en la validación.
	 * @throws DistributionException
	 *             error en la distribución.
	 * @throws SessionException
	 *             error de sesión
	 * @throws AttributesException
	 */
	public UnitsResultsBean getUnits(UseCaseConf useCaseConf, int firstRow) throws ValidationException, SessionException, AttributesException {

		Validator.validate_String_NotNull_LengthMayorZero( useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);
		unitsBo = new UnitsBo();

		UnitsResultsBean unitsResults = unitsBo.searchUnits(useCaseConf, searchUnitsBean, firstRow, pageSize);

		LOG.debug("unitsResults.getTotalSize():" + unitsResults.getTotalSize());
		return unitsResults;
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
	 * Obtiene el valor del parámetro searchUnitsBean.
	 * 
	 * @return searchUnitsBean valor del campo a obtener.
	 */
	public SearchUnitsBean getSearchUnitsBean() {
		return searchUnitsBean;
	}

	/**
	 * Guarda el valor del parámetro searchUnitsBean.
	 * 
	 * @param searchUnitsBean
	 *            valor del campo a guardar.
	 */
	public void setSearchUnitsBean(SearchUnitsBean searchUnitsBean) {
		this.searchUnitsBean = searchUnitsBean;
	}

}