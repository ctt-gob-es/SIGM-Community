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
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import com.ieci.tecdoc.common.exception.AttributesException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrOrg;

import es.msssi.sgm.registropresencial.beans.OrgBasicBean;
import es.msssi.sgm.registropresencial.beans.SearchUnitsBean;
import es.msssi.sgm.registropresencial.businessobject.SearchUnitsTreeBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;

/**
 * Action buscar un organismo en el buscador avanzado en arbol.
 * 
 * @author cmorenog
 */
public class SearchUnitsTreeAction extends GenericActions {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SearchUnitsTreeAction.class);
    private TreeNode selectedUnit;
    /** Implementación de lazydatamodel para gestionar el datatable. */
    private SearchUnitsTreeBo searchUnitsTreeBo;
    private TreeNode treeOrgs = null;
    private int tabActive = 0;
    private SearchUnitsBean searchUnitsBean;

    /**
     * Post.
     */
    @PostConstruct
    public void init()  {
	searchUnitsTreeBo = new SearchUnitsTreeBo();
	searchUnitsBean = new SearchUnitsBean();
	try {
	    initMSSSI();
	}
	catch (RPGenericException e) {
	}
    }

    /**
     * Constructor.
     */
    public SearchUnitsTreeAction() {
	init();
    }

    /**
     * Método que abre el diálogo de búsqueda avanzada de organismos.
     */
    public void chooseUnits() throws RPGenericException{
	LOG.trace("Entrando en SearchUnitsAction.chooseUnits()");
	Map<String, Object> options = new HashMap<String, Object>();
	options.put("modal", true);
	options.put("draggable", true);
	options.put("resizable", true);
	// options.put("width", 700);
	RequestContext.getCurrentInstance().openDialog("dialog/selectUnitsTreeDialog", options,
		null);
    }

    /**
     * Método que inicializa una búsqueda de organismos propios de MSSSI.
     * @throws RPGenericException 
     */
    public void initMSSSI() throws RPGenericException {
	LOG.trace("Entrando en SearchUnitsAction.initMSSSI()");
	searchUnitsBean.setSearchType(1);
	searchUnitsBean.setIdFather(null);
	searchUnitsTreeBo.setSearchUnitsBean(searchUnitsBean);
	tabActive = 0;
	try {
	    List<OrgBasicBean> list =  searchUnitsTreeBo.getUnits(useCaseConf);
	    treeOrgs =  searchUnitsTreeBo.generateTree(list, null);
	}
	catch (ValidationException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (SessionException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (AttributesException e) {
	    LOG.error(ErrorConstants.VALIDATION_OFFICE_CODE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, e);
	}
    }

    /**
     * Método que inicializa una búsqueda de organismos estatales.
     * @throws RPGenericException 
     */
    public void initState() throws RPGenericException {
	LOG.trace("Entrando en SearchUnitsAction.initState()");
	searchUnitsBean.setSearchType(2);
	searchUnitsBean.setIdFather(null);
	searchUnitsTreeBo.setSearchUnitsBean(searchUnitsBean);
	tabActive = 1;
	try {
	    List<OrgBasicBean> list =  searchUnitsTreeBo.getUnits(useCaseConf);
	    treeOrgs =  searchUnitsTreeBo.generateTree(list, null);
	}
	catch (ValidationException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (SessionException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (AttributesException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
    }
    
    /**
     *  Método que inicializa una búsqueda de organismos autonómicos.
     * @throws RPGenericException 
     */
    public void initAutonomic() throws RPGenericException {
	LOG.trace("Entrando en SearchUnitsAction.initState()");
	searchUnitsBean.setSearchType(3);
	searchUnitsBean.setIdFather(null);
	searchUnitsTreeBo.setSearchUnitsBean(searchUnitsBean);
	tabActive = 2;
	try {
	    List<OrgBasicBean> list =  searchUnitsTreeBo.getUnits(useCaseConf);
	    treeOrgs =  searchUnitsTreeBo.generateTree(list, null);
	}
	catch (ValidationException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (SessionException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (AttributesException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
    }
    
    /**
     * Método que inicializa una búsqueda de organismos locales
     * @throws RPGenericException 
     */
    public void initLocal() throws RPGenericException {
	LOG.trace("Entrando en SearchUnitsAction.initMSSSI()");
	searchUnitsBean.setSearchType(4);
	searchUnitsBean.setIdFather(null);
	searchUnitsTreeBo.setSearchUnitsBean(searchUnitsBean);
	tabActive = 3;
	try {
	    List<OrgBasicBean> list =  searchUnitsTreeBo.getUnits(useCaseConf);
	    treeOrgs =  searchUnitsTreeBo.generateTree(list, null);
	}
	catch (ValidationException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (SessionException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (AttributesException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
    }
    
    /**
     *   Método que realiza una búsqueda de organismos varios.
     * @throws RPGenericException 
     */
    public void initOther() throws RPGenericException {
	LOG.trace("Entrando en SearchUnitsAction.initState()");
	searchUnitsBean.setSearchType(5);
	searchUnitsBean.setIdFather(null);
	searchUnitsTreeBo.setSearchUnitsBean(searchUnitsBean);
	tabActive = 5;
	try {
	    List<OrgBasicBean> list =  searchUnitsTreeBo.getUnits(useCaseConf);
	    treeOrgs =  searchUnitsTreeBo.generateTree(list, null);
	}
	catch (ValidationException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (SessionException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (AttributesException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
    }
    
    /**
     *   Método que realiza una búsqueda de organismos varios.
     * @throws RPGenericException 
     */
    public void initLab() throws RPGenericException {
	LOG.trace("Entrando en SearchUnitsAction.initState()");
	searchUnitsBean.setSearchType(9);
	searchUnitsBean.setIdFather(null);
	searchUnitsTreeBo.setSearchUnitsBean(searchUnitsBean);
	tabActive = 4;
	try {
	    List<OrgBasicBean> list =  searchUnitsTreeBo.getUnits(useCaseConf);
	    treeOrgs =  searchUnitsTreeBo.generateTree(list, null);
	}
	catch (ValidationException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (SessionException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (AttributesException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
    }
   /**
    * Evento de expandir un nodo.
    * @param event
    * @throws RPGenericException 
    */
    public void onNodeExpand(NodeExpandEvent event) throws RPGenericException {
	OrgBasicBean org = (OrgBasicBean) event.getTreeNode().getData();
	searchUnitsBean.setIdFather(org.getIdOrg());
	searchUnitsTreeBo.setSearchUnitsBean(searchUnitsBean);
	try {
	    List<OrgBasicBean> list =  searchUnitsTreeBo.getUnits(useCaseConf);
	    searchUnitsTreeBo.generateTree(list, event.getTreeNode());
	}
	catch (ValidationException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (SessionException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
	catch (AttributesException e) {
	    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
	}
       
    }
    
    public void onNodeSelect(NodeSelectEvent event) throws RPGenericException {
	selectedUnit = event.getTreeNode();
	selectUnitFromDialog();
    }
    
    
    /**
     * Método que oculta el diálogo de búsqueda avanzada de organismos.
     */
    public void hiddenDialog() {
	FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		.remove("UNITSDIALOG");
	RequestContext.getCurrentInstance().closeDialog("selectUnitsTreeDialog");
    }

    /**
     * Método que selecciona el organismo seleccionado en la búsqueda avanzada y
     * lo incluye como atributo en la sesión para ser recogido por el formulario
     * que lo ha llamado.
     * @throws RPGenericException 
     */
    public void selectUnitFromDialog() throws RPGenericException {
	LOG.trace("Entrando en SearchUnitsAction.selectUnitFromDialog()");
	if (selectedUnit != null) {
	    OrgBasicBean orgBasic = (OrgBasicBean)selectedUnit.getData();
	    ScrOrg org = null;
	    try {
		org = searchUnitsTreeBo.getUnit(orgBasic.getIdOrg(), useCaseConf);
		}
		catch (ValidationException e) {
		    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
		    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
			    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
		}
		catch (SessionException e) {
		    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
		    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
			    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
		}
		catch (AttributesException e) {
		    LOG.error(ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
		    throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
			    ErrorConstants.GET_ORGANISMS_TREE_ERROR_MESSAGE, e);
		}
	    FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		    .put("UNITSDIALOG", org);
	    RequestContext.getCurrentInstance().closeDialog("selectUnitsTreeDialog");
	}
	else {
	    RequestContext.getCurrentInstance()
		    .showMessageInDialog(
			    new FacesMessage(FacesMessage.SEVERITY_WARN, "",
				    "Debe seleccionar una unidad"));
	}
    }

    /**
     * Obtiene el valor del parámetro selectedUnit.
     * 
     * @return selectedUnit valor del campo a obtener.
     */
    public TreeNode getSelectedUnit() {
	return selectedUnit;
    }

    /**
     * Guarda el valor del parámetro selectedUnit.
     * 
     * @param selectedUnit
     *            valor del campo a guardar.
     */
    public void setSelectedUnit(TreeNode selectedUnit) {
	this.selectedUnit = selectedUnit;
    }

    /**
     * @return the searchUnitsTreeBo
     */
    public SearchUnitsTreeBo getSearchUnitsTreeBo() {
	return searchUnitsTreeBo;
    }

    /**
     * @param searchUnitsTreeBo
     *            the searchUnitsTreeBo to set
     */
    public void setSearchUnitsTreeBo(SearchUnitsTreeBo searchUnitsTreeBo) {
	this.searchUnitsTreeBo = searchUnitsTreeBo;
    }

  
    /**
     * Obtiene el valor del parámetro tabActive.
     * 
     * @return tabActive valor del campo a obtener.
     */
    public int getTabActive() {
	return tabActive;
    }

    /**
     * Guarda el valor del parámetro tabActive.
     * 
     * @param tabActive
     *            valor del campo a guardar.
     */
    public void setTabActive(int tabActive) {
	this.tabActive = tabActive;
    }

    /**
     * @return the treeOrgs
     */
    public TreeNode getTreeOrgs() {
	return treeOrgs;
    }

    /**
     * @param treeOrgs
     *            the treeOrgs to set
     */
    public void setTreeOrgs(TreeNode treeOrgs) {
	this.treeOrgs = treeOrgs;
    }

}