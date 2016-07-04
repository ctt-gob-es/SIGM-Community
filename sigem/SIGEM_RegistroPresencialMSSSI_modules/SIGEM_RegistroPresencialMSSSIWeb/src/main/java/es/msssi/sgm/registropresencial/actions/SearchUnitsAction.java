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
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.component.tabview.Tab;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.LazyDataModel;

import com.ieci.tecdoc.common.invesicres.ScrOrg;

import es.msssi.sgm.registropresencial.beans.SearchUnitsBean;
import es.msssi.sgm.registropresencial.beans.ibatis.DirOrgs;
import es.msssi.sgm.registropresencial.businessobject.SearchUnitsBo;
import es.msssi.sgm.registropresencial.businessobject.UnitsBo;

/**
 * Action buscar un organismo en el buscador avanzado.
 * 
 * @author cmorenog
 */
public class SearchUnitsAction extends GenericActions {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SearchUnitsAction.class);
    private String textSearch;
    private ScrOrg selectedUnit;
    private String ccaaSearch;
    private String provSearch;
    /** Implementación de lazydatamodel para gestionar el datatable. */
    private LazyDataModel<ScrOrg> searchUnitsBo;
    private ScrOrg selectOrg;
    private DirOrgs addressOrg;
    /**
     * Post.
     */
    @PostConstruct
    public void init() {
	searchUnitsBo = new SearchUnitsBo();
    }

    /**
     * Constructor.
     */
    public SearchUnitsAction() {
	init();
    }

    /**
     * Método que inicializa el contenido de las pestañas cuando se cambia de
     * una a otra.
     * 
     * @param event
     *            el evento de cambiar de pestaña.
     */
    public void onTabChange(
	TabChangeEvent event) {
	LOG.trace("Entrando en SearchUnitsAction.onTabChange()");
	searchUnitsBo = new SearchUnitsBo();
	Tab tab = event.getTab();
	int idTab = Integer.valueOf(
	    tab.getId().replaceAll(
		"tab", "")).intValue();
	SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
	searchUnitsBean.setSearchType(idTab);
	textSearch = null;
	ccaaSearch = null;
	provSearch = null;
	((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
    }

    /**
     * Método que abre el diálogo de búsqueda avanzada de organismos.
     */
    public void chooseUnits() {
	LOG.trace("Entrando en SearchUnitsAction.chooseUnits()");
	Map<String, Object> options = new HashMap<String, Object>();
	options.put(
	    "modal", true);
	options.put(
	    "draggable", true);
	options.put(
	    "resizable", true);
	//options.put("width", 700);
	RequestContext.getCurrentInstance().openDialog(
	    "dialog/selectUnitsDialog", options, null);
    }
    /**
     * Método que realiza una búsqueda de organismos propios de MSSSI.
     */
    public void searchMSSSI() {
	LOG.trace("Entrando en SearchUnitsAction.searchMSSSI()");
	SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
	searchUnitsBean.setSearchText(textSearch);
	searchUnitsBean.setSearchType(1);
	((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
    }

    /**
     * Método que realiza una búsqueda de organismos estatales.
     */
    public void searchState() {
	LOG.trace("Entrando en SearchUnitsAction.searchState()");
	SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
	searchUnitsBean.setSearchText(textSearch);
	searchUnitsBean.setSearchType(2);
	((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
    }

    /**
     * Método que realiza una búsqueda de organismos autonómicos.
     */
    public void searchAutonomic() {
	LOG.trace("Entrando en SearchUnitsAction.searchAutonomic()");
	SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
	if (ccaaSearch != null &&
	    !"".equals(ccaaSearch)) {
	    searchUnitsBean.setCcaaId(ccaaSearch);
	}
	searchUnitsBean.setSearchText(textSearch);
	searchUnitsBean.setSearchType(3);
	((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
    }

    /**
     * Método que realiza una búsqueda de organismos locales.
     */
    public void searchLocal() {
	LOG.trace("Entrando en SearchUnitsAction.searchLocal()");
	SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
	if (provSearch != null &&
	    !"".equals(provSearch)) {
	    searchUnitsBean.setProvId(Integer.valueOf(provSearch));
	}
	searchUnitsBean.setSearchText(textSearch);
	searchUnitsBean.setSearchType(4);
	((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
    }

    /**
     * Método que realiza una búsqueda de organismos varios.
     */
    public void searchOther() {
	LOG.trace("Entrando en SearchUnitsAction.searchOther()");
	SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
	searchUnitsBean.setSearchText(textSearch);
	searchUnitsBean.setSearchType(5);
	((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
    }

    /**
     * Método que realiza una búsqueda de laboratorios.
     */
    public void searchLab() {
	LOG.trace("Entrando en SearchUnitsAction.searchLab()");
	SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
	searchUnitsBean.setSearchText(textSearch);
	searchUnitsBean.setSearchType(9);
	((SearchUnitsBo) searchUnitsBo).setSearchUnitsBean(searchUnitsBean);
    }

    /**
     * Método que oculta el diálogo de búsqueda avanzada de organismos.
     */
    public void hiddenDialog() {
	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("UNITSDIALOG");
	RequestContext.getCurrentInstance().closeDialog(
		"selectUnitsDialog");
    }

    /**
     * Método que selecciona el organismo seleccionado en la búsqueda avanzada y
     * lo incluye como atributo en la sesión para ser recogido por el formulario
     * que lo ha llamado.
     */
    public void selectUnitFromDialog() {
	LOG.trace("Entrando en SearchUnitsAction.selectUnitFromDialog()");
	if (selectedUnit != null) {
	    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
		"UNITSDIALOG", selectedUnit);
	    RequestContext.getCurrentInstance().closeDialog(
		    "selectUnitsDialog");
	}
	else {
	    RequestContext.getCurrentInstance().showMessageInDialog(
		new FacesMessage(
		    FacesMessage.SEVERITY_WARN, "", "Debe seleccionar una unidad"));
	}
    }

    public void viewDirOrg (){
	if (selectOrg != null){
	    UnitsBo unitsBo = new UnitsBo ();
	    addressOrg = unitsBo.getAddressOrg(selectOrg.getId());
	}
    }
    /**
     * Obtiene el valor del parámetro textSearch.
     * 
     * @return textSearch valor del campo a obtener.
     */
    public String getTextSearch() {
	return textSearch;
    }

    /**
     * Guarda el valor del parámetro textSearch.
     * 
     * @param textSearch
     *            valor del campo a guardar.
     */
    public void setTextSearch(
	String textSearch) {
	this.textSearch = textSearch;
    }

    /**
     * Obtiene el valor del parámetro selectedUnit.
     * 
     * @return selectedUnit valor del campo a obtener.
     */
    public ScrOrg getSelectedUnit() {
	return selectedUnit;
    }

    /**
     * Guarda el valor del parámetro selectedUnit.
     * 
     * @param selectedUnit
     *            valor del campo a guardar.
     */
    public void setSelectedUnit(
	ScrOrg selectedUnit) {
	this.selectedUnit = selectedUnit;
    }

    /**
     * Obtiene el valor del parámetro searchUnitsBo.
     * 
     * @return searchUnitsBo valor del campo a obtener.
     */
    public LazyDataModel<ScrOrg> getSearchUnitsBo() {
	return searchUnitsBo;
    }

    /**
     * Guarda el valor del parámetro searchUnitsBo.
     * 
     * @param searchUnitsBo
     *            valor del campo a guardar.
     */
    public void setSearchUnitsBo(
	LazyDataModel<ScrOrg> searchUnitsBo) {
	this.searchUnitsBo = searchUnitsBo;
    }

    /**
     * Obtiene el valor del parámetro ccaaSearch.
     * 
     * @return ccaaSearch valor del campo a obtener.
     */
    public String getCcaaSearch() {
	return ccaaSearch;
    }

    /**
     * Guarda el valor del parámetro ccaaSearch.
     * 
     * @param ccaaSearch
     *            valor del campo a guardar.
     */
    public void setCcaaSearch(
	String ccaaSearch) {
	this.ccaaSearch = ccaaSearch;
    }

    /**
     * Obtiene el valor del parámetro provSearch.
     * 
     * @return provSearch valor del campo a obtener.
     */
    public String getProvSearch() {
	return provSearch;
    }

    /**
     * Guarda el valor del parámetro provSearch.
     * 
     * @param provSearch
     *            valor del campo a guardar.
     */
    public void setProvSearch(
	String provSearch) {
	this.provSearch = provSearch;
    }

    /**
     * Obtiene el valor del parámetro selectOrg.
     * 
     * @return selectOrg valor del campo a obtener.
     */
    public ScrOrg getSelectOrg() {
        return selectOrg;
    }
    /**
     * Guarda el valor del parámetro selectOrg.
     * 
     * @param selectOrg
     *            valor del campo a guardar.
     */
    public void setSelectOrg(ScrOrg selectOrg) {
        this.selectOrg = selectOrg;
    }
    /**
     * Obtiene el valor del parámetro addressOrg.
     * 
     * @return addressOrg valor del campo a obtener.
     */
    public DirOrgs getAddressOrg() {
        return addressOrg;
    }
    /**
     * Guarda el valor del parámetro addressOrg.
     * 
     * @param addressOrg
     *            valor del campo a guardar.
     */
    public void setAddressOrg(DirOrgs addressOrg) {
        this.addressOrg = addressOrg;
    }

}