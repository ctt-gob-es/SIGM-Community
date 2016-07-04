/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.businessobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.isicres.web.util.ContextoAplicacionUtil;

import es.ieci.tecdoc.fwktd.server.pagination.PageInfo;
import es.ieci.tecdoc.fwktd.sir.core.types.CriterioEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.DocumentacionFisicaEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.EstadoAsientoRegistralEnum;
import es.ieci.tecdoc.fwktd.sir.core.types.OperadorCriterioEnum;
import es.ieci.tecdoc.fwktd.sir.core.vo.CriterioVO;
import es.ieci.tecdoc.fwktd.sir.core.vo.CriteriosVO;
import es.ieci.tecdoc.isicres.api.business.vo.BaseOficinaVO;
import es.ieci.tecdoc.isicres.api.business.vo.ContextoAplicacionVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaEntradaItemVO;
import es.msssi.sgm.registropresencial.beans.SearchBoxRegInterchangeBean;
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
public class SearchInputSIRBo extends LazyDataModel<BandejaEntradaItemVO> implements IGenericBo,
	Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SearchInputSIRBo.class);

    /** Bean con los criterios del buscador. */
    private SearchInputRegisterBean searchInputRegister = new SearchInputRegisterBean();
    /** página actual. */
    private int page;


    /** bean de la búsqueda. */
    private SearchBoxRegInterchangeBean searchbean = null;
    private RegInterchangeBo regInterchangeBo = null;

    /**
     * Constructor.
     */
    public SearchInputSIRBo() {
	super();
	regInterchangeBo = new RegInterchangeBo();
	searchbean = new SearchBoxRegInterchangeBean();
	searchbean.setType("1");
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
    public List<BandejaEntradaItemVO> load(int first, int pageSize, String sortField,
	    SortOrder sortOrder, Map<String, String> filters) {
	LOG.trace("Entrando en SearchInputRegisterBo.load()");
	List<BandejaEntradaItemVO> data = null;
	// Si es la primera carga no se realiza la búsqueda
	data = new ArrayList<BandejaEntradaItemVO>();

	ContextoAplicacionVO contextoAplicacion = null;
	BaseOficinaVO oficina = null;
	try {

	    contextoAplicacion =
		    ContextoAplicacionUtil
			    .getContextoAplicacion((javax.servlet.http.HttpServletRequest) FacesContext
				    .getCurrentInstance().getExternalContext().getRequest());
	    oficina = contextoAplicacion.getOficinaActual();
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.COPY_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(null, sessionException, null);
	}
	catch (TecDocException tecDocException) {
	    LOG.error(ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, tecDocException);
	    Utils.redirectToErrorPage(null, tecDocException, tecDocException);
	}
	try {
	    CriteriosVO criteriosVO = null;
	    criteriosVO = new CriteriosVO();
	    CriterioVO criterioEstado;
	    criterioEstado =
		    new CriterioVO(CriterioEnum.ASIENTO_ESTADO, OperadorCriterioEnum.IN,
			    new Integer[] { EstadoAsientoRegistralEnum.RECIBIDO.getValue() });
	    criteriosVO.addCriterioVO(criterioEstado);
	   
	    if (searchbean.getDocSupport() != null && !"0".equals(searchbean.getDocSupport())){
        	criterioEstado =
        	    new CriterioVO(CriterioEnum.ASIENTO_DOCUMENTACION_FISICA, OperadorCriterioEnum.EQUAL,
        		    searchbean.getDocSupport() );
        	criteriosVO.addCriterioVO(criterioEstado);
	    }
	    if (searchbean.getNregistroOrg() != null && !"".equals(searchbean.getNregistroOrg())){
        	criterioEstado =
        	    new CriterioVO(CriterioEnum.ASIENTO_NUMERO_REGISTRO_INICIAL, OperadorCriterioEnum.LIKE,
        		    searchbean.getNregistroOrg() );
        	criteriosVO.addCriterioVO(criterioEstado);
	    }
	    if (searchbean.getCodIntercambio() != null && !"".equals(searchbean.getCodIntercambio())){
        	criterioEstado =
        	    new CriterioVO(CriterioEnum.ASIENTO_IDENTIFICADOR_INTERCAMBIO, OperadorCriterioEnum.LIKE,
        		    searchbean.getCodIntercambio() );
        	criteriosVO.addCriterioVO(criterioEstado);
	    }
	    if (searchbean.getDocInteresado() != null && !"".equals(searchbean.getDocInteresado())){
        	criterioEstado =
        	    new CriterioVO(CriterioEnum.INTERESADO_DOCUMENTO_IDENTIFICACION_INTERESADO, OperadorCriterioEnum.LIKE,
        		    searchbean.getDocInteresado() );
        	criteriosVO.addCriterioVO(criterioEstado);
	    }
	    if (searchbean.getNombreInteresado() != null && !"".equals(searchbean.getNombreInteresado())){
        	criterioEstado =
        	    new CriterioVO(CriterioEnum.INTERESADO_NOMBRE_INTERESADO, OperadorCriterioEnum.LIKE,
        		    searchbean.getNombreInteresado() );
        	criteriosVO.addCriterioVO(criterioEstado);
	    }
	    if (searchbean.getPrimerApellidoInteresado() != null && !"".equals(searchbean.getPrimerApellidoInteresado())){
        	criterioEstado =
        	    new CriterioVO(CriterioEnum.INTERESADO_PRIMER_APELLIDO_INTERESADO, OperadorCriterioEnum.LIKE,
        		    searchbean.getPrimerApellidoInteresado() );
        	criteriosVO.addCriterioVO(criterioEstado);
	    }
	    if (searchbean.getSegundoApellidoInteresado() != null && !"".equals(searchbean.getSegundoApellidoInteresado())){
        	criterioEstado =
        	    new CriterioVO(CriterioEnum.INTERESADO_SEGUNDO_APELLIDO_INTERESADO, OperadorCriterioEnum.LIKE,
        		    searchbean.getSegundoApellidoInteresado() );
        	criteriosVO.addCriterioVO(criterioEstado);
	    }
	    if (searchbean.getResumen() != null && !"".equals(searchbean.getResumen())){
        	criterioEstado =
        	    new CriterioVO(CriterioEnum.ASIENTO_RESUMEN, OperadorCriterioEnum.LIKE,
        		    searchbean.getResumen() );
        	criteriosVO.addCriterioVO(criterioEstado);
	    }
	    /*if (searchbean.getSegundoApellidoInteresado() != null && !"".equals(searchbean.getSegundoApellidoInteresado())){
        	criterioEstado =
        	    new CriterioVO(CriterioEnum.ASIENTO_FECHA_RECEPCION, OperadorCriterioEnum.EQUAL_OR_GREATER_THAN,
        		    searchbean.getSegundoApellidoInteresado() );
        	criteriosVO.addCriterioVO(criterioEstado);
	    }
	    if (searchbean.getSegundoApellidoInteresado() != null && !"".equals(searchbean.getSegundoApellidoInteresado())){
        	criterioEstado =
        	    new CriterioVO(CriterioEnum.ASIENTO_FECHA_RECEPCION, OperadorCriterioEnum.EQUAL_OR_LESS_THAN,
        		    searchbean.getSegundoApellidoInteresado() );
        	criteriosVO.addCriterioVO(criterioEstado);
	    }*/
	    int rowcount = 0;
	    rowcount =
		    regInterchangeBo.getCountInboxIR(searchbean.getInState(),
			    Integer.parseInt(oficina.getId()), criteriosVO);
	   
	    this.setRowCount(rowcount);

	    if (rowcount > 0) {
		String orderString = null;
		if ((new Integer(0)).equals(searchbean.getInState())){
        		if (sortField != null) {
        		    String orden = "";
        		    if ("DESCENDING".equals(sortOrder.name())) {
        			orden = " DESC ";
        		    }
        		    if (sortField.equals("idIntercambioRegistral")) {
        			orderString = "cd_intercambio" + orden;
        		    }
        		    else if (sortField.equals("numeroRegistroOriginal")) {
        			orderString = "num_registro_inicial" + orden;
        		    }
        		    else if (sortField.equals("fechaRegistro")) {
        			orderString = "fe_registro" + orden;
        		    }
        		    else if (sortField.equals("fechaIntercambioRegistral")) {
        			orderString = "fe_recepcion" + orden;
        		    }
        		    else if (sortField.equals("origen")) {
        			orderString = "cd_ent_reg_origen" + orden;
        		    }
        		    else if (sortField.equals("codigoUnidadTramitacion")) {
        			orderString = "cd_org_origen" + orden;
        		    }
        		    else if (sortField.equals("resumen")) {
        			orderString = "ds_resumen" + orden;
        		    }
        		    if (orderString != null) {
        			criteriosVO.setOrderByString(orderString);
        		    }
        		} else {
        		    criteriosVO.setOrderByString("fe_recepcion DESC");
        		}
		} else {
		    if (sortField != null) {
			String orden = "";
        		    if ("DESCENDING".equals(sortOrder.name())) {
        			orden = " DESC ";
        		    }
        		    if (sortField.equals("idIntercambioRegistral")) {
        			orderString = "ID_EXCHANGE_SIR" + orden;
        		    }else if (sortField.equals("fechaIntercambioRegistral")){
        			orderString = "EXCHANGE_DATE" + orden;
        		    }else if (sortField.equals("origen")){
        			orderString = "CODE_ENTITY" + orden;
        		    }else if (sortField.equals("codigoUnidadTramitacion")){
        			orderString = "CODE_TRAMUNIT" + orden;
        		    }else if (sortField.equals("fechaEstado")){
        			orderString = "STATE_DATE" + orden;
        		    }else if (sortField.equals("username")){
        			orderString = "USERNAME" + orden;
        		    }
        		    if (orderString != null) {
        			criteriosVO.setOrderByString(orderString);
        		    }
		    }else {
			criteriosVO.setOrderByString("STATE_DATE DESC");
		    }
		}
		PageInfo pageInfo = new PageInfo();
		pageInfo.setObjectsPerPage(10);
		if (first == 0) {
		    pageInfo.setPageNumber(first);
		    pageInfo.setPageNumber(1);
		}
		else {
		    pageInfo.setPageNumber((first / 10) + 1);
		}
		criteriosVO.setPageInfo(pageInfo);
		// operativa de obtener la bandeja de entrada
		data =
			regInterchangeBo.getInboxIR(searchbean.getInState(),
				Integer.parseInt(oficina.getId()), criteriosVO);
	    }
	}
	catch (NumberFormatException numberFormatException) {
	    LOG.error(ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, numberFormatException);
	}
	catch (RPRegistralExchangeException e) {
	    LOG.error(ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, e);
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
    @SuppressWarnings("unchecked")
    @Override
    public BandejaEntradaItemVO getRowData(String rowKey) {
	LOG.trace("Entrando en SearchInputRegisterBo.getRowData()");
	BandejaEntradaItemVO result = null;
	List<BandejaEntradaItemVO> bandejaEntradaItemVOItemVO =
		(List<BandejaEntradaItemVO>) getWrappedData();

	Iterator<BandejaEntradaItemVO> iterator = bandejaEntradaItemVOItemVO.iterator();
	BandejaEntradaItemVO rowBandejaEntradaItemVO;
	while (iterator.hasNext() && result == null) {
	    rowBandejaEntradaItemVO = iterator.next();
	    if (rowBandejaEntradaItemVO.getIdIntercambioInterno().equals(new Long(rowKey))) {
		result = rowBandejaEntradaItemVO;
	    }
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
    public Object getRowKey(BandejaEntradaItemVO rowBandejaEntradaItemVO) {
	LOG.trace("Entrando en SearchInputRegisterBo.getRowKey()");
	return rowBandejaEntradaItemVO.getIdIntercambioInterno();
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
	}
	else {
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
    public void setSearchInputRegister(SearchInputRegisterBean searchInputRegister) {
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
     * Obtiene el valor del parámetro searchbean.
     * 
     * @return searchbean valor del campo a obtener.
     */
    public SearchBoxRegInterchangeBean getSearchbean() {
	return searchbean;
    }

    /**
     * Guarda el valor del parámetro searchbean.
     * 
     * @param searchbean
     *            valor del campo a guardar.
     */
    public void setSearchbean(SearchBoxRegInterchangeBean searchbean) {
	this.searchbean = searchbean;
    }

}
