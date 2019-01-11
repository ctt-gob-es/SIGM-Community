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

import java.util.List;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.ieci.tecdoc.common.exception.AttributesException;
import com.ieci.tecdoc.common.exception.DistributionException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.Validator;

import es.msssi.sgm.registropresencial.beans.OrgBasicBean;
import es.msssi.sgm.registropresencial.beans.SearchUnitsBean;

/**
 * Clase q implementa IGenericBo que contiene los métodos relacionados con la
 * búsqueda de registros distribuidos.
 * 
 * @author cmorenog
 */
public class SearchUnitsTreeBo implements IGenericBo {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SearchUnitsTreeBo.class.getName());
    /** Variable con la configuración de la aplicación. */
    private SearchUnitsBean searchUnitsBean;
    /** objeto clase de negocio. */
    private UnitsBo unitsBo;


    /**
     * Constructor.
     */
    public SearchUnitsTreeBo() {
	super();
	searchUnitsBean = new SearchUnitsBean();
    }

    /**
     * Método que construye la query y controla los permisos para la búsqueda de
     * unidades.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @return objeto con los datos necesarios para la búsqueda.
     * @throws ValidationException
     *             error en la validación.
     * @throws DistributionException
     *             error en la distribución.
     * @throws SessionException
     *             error de sesión
     * @throws AttributesException 
     */
    public List<OrgBasicBean> getUnits(
	UseCaseConf useCaseConf)
	throws ValidationException, SessionException, AttributesException {
	Validator.validate_String_NotNull_LengthMayorZero(
	    useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);
	unitsBo = new UnitsBo();

	List<OrgBasicBean> unitsResults = unitsBo.searchUnitsTree(searchUnitsBean);

	return unitsResults;
    }

    /**
     * Método que construye la query y controla los permisos para la búsqueda de
     * unidades.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @return objeto con los datos necesarios para la búsqueda.
     * @throws ValidationException
     *             error en la validación.
     * @throws DistributionException
     *             error en la distribución.
     * @throws SessionException
     *             error de sesión
     * @throws AttributesException 
     */
    public ScrOrg getUnit(Integer idOrg, UseCaseConf useCaseConf)
	throws ValidationException, SessionException, AttributesException {
	Validator.validate_String_NotNull_LengthMayorZero(
	    useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);
	unitsBo = new UnitsBo();

	ScrOrg result = unitsBo.getOrgId(useCaseConf, idOrg);

	return result;
    }
    
    public TreeNode generateTree (List<OrgBasicBean> list, TreeNode parent){
	TreeNode root = null;
	TreeNode child = null;
	TreeNode dummy = null;
	OrgBasicBean dummyorg = new OrgBasicBean();
	dummyorg.setNameOrg("dummy");
	dummyorg.setIdOrg(-1);
	if (parent == null){
	    root = new DefaultTreeNode("root", null);
	    for (OrgBasicBean org: list){
		child  = new DefaultTreeNode(org, root); 
		if (org.getChildren() != null && org.getChildren().intValue() > 0){
		    dummy  = new DefaultTreeNode(dummyorg, child); 
		}
	    }
	}
	else {
	    if (parent.getChildCount() == 1 && 
		    "dummy".equals(((OrgBasicBean) parent.getChildren().get(0).getData()).getNameOrg())) {
		parent.getChildren().remove(0);
	    }
	    for (OrgBasicBean org: list){
		child  = new DefaultTreeNode(org, parent); 
		if (org.getChildren() != null && org.getChildren().intValue() > 0){
		    dummy  = new DefaultTreeNode(dummyorg, child); 
		}
	    }
	}
	return root;
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
    public void setSearchUnitsBean(
        SearchUnitsBean searchUnitsBean) {
        this.searchUnitsBean = searchUnitsBean;
    }


}