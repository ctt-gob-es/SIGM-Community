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

import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralSIRVO;
import es.msssi.sgm.registropresencial.beans.SearchUnitsBean;
import es.msssi.sgm.registropresencial.beans.ibatis.DirOrgs;
import es.msssi.sgm.registropresencial.businessobject.SearchUnitsTramBo;
import es.msssi.sgm.registropresencial.businessobject.UnitsBo;

/**
 * Action buscar unidades tramitadoras en el buscador avanzado.
 * 
 * @author cmorenog
 */
public class SearchUnitsTramAction extends GenericActions {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SearchUnitsTramAction.class);
    private String textSearch;
    private UnidadTramitacionIntercambioRegistralSIRVO selectedUnit;
    private String ccaaSearch;
    private String provSearch;
    /** Implementación de lazydatamodel para gestionar el datatable. */
    private LazyDataModel<UnidadTramitacionIntercambioRegistralSIRVO> searchUnitsTramBo;
    private UnidadTramitacionIntercambioRegistralSIRVO selectUnitTram;
    private DirOrgs addressOrg;
    /**
     * Post.
     */
    @PostConstruct
    public void init() {
	searchUnitsTramBo = new SearchUnitsTramBo();
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
	LOG.trace("Entrando en SearchUnitsTramAction.onTabChange()");
	searchUnitsTramBo = new SearchUnitsTramBo();
	Tab tab = event.getTab();
	int idTab = Integer.valueOf(
	    tab.getId().replaceAll(
		"tab", "")).intValue();
	SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
	searchUnitsBean.setSearchType(idTab);
	textSearch = null;
	ccaaSearch = null;
	provSearch = null;
	((SearchUnitsTramBo) searchUnitsTramBo).setSearchUnitsBean(searchUnitsBean);
    }

    /**
     * Método que abre el diálogo de búsqueda avanzada de organismos.
     */
    public void chooseUnits() {
	LOG.trace("Entrando en SearchUnitsTramAction.chooseUnits()");
	Map<String, Object> options = new HashMap<String, Object>();
	options.put(
	    "modal", true);
	options.put(
	    "draggable", true);
	options.put(
	    "resizable", true);
	RequestContext.getCurrentInstance().openDialog(
	    "dialog/selectUnitsTramDialog", options, null);
    }


    /**
     * Método que realiza una búsqueda de organismos estatales.
     */
    public void searchState() {
	LOG.trace("Entrando en SearchUnitsTramAction.searchState()");
	SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
	searchUnitsBean.setSearchText(textSearch);
	searchUnitsBean.setSearchType(2);
	((SearchUnitsTramBo) searchUnitsTramBo).setSearchUnitsBean(searchUnitsBean);
    }

    /**
     * Método que realiza una búsqueda de organismos autonómicos.
     */
    public void searchAutonomic() {
	LOG.trace("Entrando en SearchUnitsTramAction.searchAutonomic()");
	SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
	if (ccaaSearch != null &&
	    !"".equals(ccaaSearch)) {
	    searchUnitsBean.setCcaaId(ccaaSearch);
	}
	searchUnitsBean.setSearchText(textSearch);
	searchUnitsBean.setSearchType(3);
	((SearchUnitsTramBo) searchUnitsTramBo).setSearchUnitsBean(searchUnitsBean);
    }

    /**
     * Método que realiza una búsqueda de organismos locales.
     */
    public void searchLocal() {
	LOG.trace("Entrando en SearchUnitsTramAction.searchLocal()");
	SearchUnitsBean searchUnitsBean = new SearchUnitsBean();
	if (provSearch != null &&
	    !"".equals(provSearch)) {
	    searchUnitsBean.setProvId(Integer.valueOf(provSearch));
	}
	searchUnitsBean.setSearchText(textSearch);
	searchUnitsBean.setSearchType(4);
	((SearchUnitsTramBo) searchUnitsTramBo).setSearchUnitsBean(searchUnitsBean);
    }


    /**
     * Método que oculta el diálogo de búsqueda avanzada de organismos.
     */
    public void hiddenDialog() {
	RequestContext.getCurrentInstance().closeDialog(
	    "selectUnitsTramDialog");
    }

    /**
     * Método que selecciona el organismo seleccionado en la búsqueda avanzada y
     * lo incluye como atributo en la sesión para ser recogido por el formulario
     * que lo ha llamado.
     */
    public void selectUnitFromDialog() {
	LOG.trace("Entrando en SearchUnitsTramAction.selectUnitFromDialog()");
	if (selectedUnit != null) {
	    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
		"UNITTRAMDIALOG", selectedUnit);
	    hiddenDialog();
	}
	else {
	    RequestContext.getCurrentInstance().showMessageInDialog(
		new FacesMessage(
		    FacesMessage.SEVERITY_WARN, "", "Debe seleccionar una unidad"));
	}
    }


    public void viewDirOrg (){
	if (selectUnitTram != null){
	    UnitsBo unitsBo = new UnitsBo ();
	    addressOrg = unitsBo.getAddressOrg(Integer.valueOf(selectUnitTram.getIdOrgs()));
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
    public UnidadTramitacionIntercambioRegistralSIRVO getSelectedUnit() {
	return selectedUnit;
    }

    /**
     * Guarda el valor del parámetro selectedUnit.
     * 
     * @param selectedUnit
     *            valor del campo a guardar.
     */
    public void setSelectedUnit(
	UnidadTramitacionIntercambioRegistralSIRVO selectedUnit) {
	this.selectedUnit = selectedUnit;
    }

    /**
     * Obtiene el valor del parámetro searchUnitsTramBo.
     * 
     * @return searchUnitsTramBo valor del campo a obtener.
     */
    public LazyDataModel<UnidadTramitacionIntercambioRegistralSIRVO> getSearchUnitsTramBo() {
	return searchUnitsTramBo;
    }

    /**
     * Guarda el valor del parámetro searchUnitsTramBo.
     * 
     * @param searchUnitsTramBo
     *            valor del campo a guardar.
     */
    public void setSearchUnitsTramBo(
	LazyDataModel<UnidadTramitacionIntercambioRegistralSIRVO> searchUnitsTramBo) {
	this.searchUnitsTramBo = searchUnitsTramBo;
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
     * Obtiene el valor del parámetro selectUnitTram.
     * 
     * @return selectUnitTram valor del campo a obtener.
     */
    public UnidadTramitacionIntercambioRegistralSIRVO getSelectUnitTram() {
        return selectUnitTram;
    }

    /**
     * Guarda el valor del parámetro selectUnitTram.
     * 
     * @param selectUnitTram
     *            valor del campo a guardar.
     */
    public void setSelectUnitTram(UnidadTramitacionIntercambioRegistralSIRVO selectUnitTram) {
        this.selectUnitTram = selectUnitTram;
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