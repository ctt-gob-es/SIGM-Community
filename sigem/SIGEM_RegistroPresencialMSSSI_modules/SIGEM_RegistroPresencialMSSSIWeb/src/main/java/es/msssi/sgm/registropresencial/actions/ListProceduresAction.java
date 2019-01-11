package es.msssi.sgm.registropresencial.actions;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import es.msssi.sgm.registropresencial.beans.ItemBean;
import es.msssi.sgm.registropresencial.businessobject.ProceduresBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPProcedureException;
import es.msssi.sgm.registropresencial.utils.Utils;


/**
 * Clase que muestra la lista de procedimientos administrativos disponibles
 * @author eacuna
 *
 */
public class ListProceduresAction extends GenericActions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3834637790876682789L;
	private static final Logger LOG = Logger.getLogger(ListProceduresAction.class.getName());
	private ProceduresBo proceduresBo;
	private List<ItemBean> listProcedures = null;
	 /**
     * Constructor.
     */
    public ListProceduresAction() {
    }
    
    /**
     * Crea el listado de procedimientos disponibles.
     */
    @PostConstruct
    public void create() {
		if (proceduresBo == null) {
			proceduresBo = new ProceduresBo();
		}
		if (listProcedures == null) {
		    try {
		    	listProcedures = proceduresBo.getProcedures(useCaseConf);
		    }
		    catch (RPProcedureException rpProcedureException) {
			LOG.error(ErrorConstants.GET_PROCEDURES_ERROR_MESSAGE + ". Código: "
				+ rpProcedureException.getCode().getCode() + " . Mensaje: "
				+ rpProcedureException.getShortMessage());
			Utils.redirectToErrorPage(rpProcedureException, null, null);
		    }
		    catch (RPGenericException rpGenericException) {
			LOG.error(ErrorConstants.GET_PROCEDURES_ERROR_MESSAGE + ". Código: "
				+ rpGenericException.getCode().getCode() + " . Mensaje: "
				+ rpGenericException.getShortMessage());
			if ("RPG002".equals(rpGenericException.getCode())) {
			    ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				    .getRequest()).getSession().invalidate();
			}
			Utils.redirectToErrorPage(rpGenericException, null, null);
		    }
		}
    }
    
    /**
     * Obtiene el valor del parámetro listProcedures.
     * 
     * @return listProcedures valor del campo a obtener.
     */
    public List<ItemBean> getListProcedures() {
	return listProcedures;
    }

    /**
     * Guarda el valor del parámetro listProcedures.
     * 
     * @param listProcedures
     *            valor del campo a guardar.
     */
    public void setListProcedures(List<ItemBean> listProcedures) {
	this.listProcedures = listProcedures;
    }

    /**
     * Obtiene el valor del parámetro proceduresBo.
     * 
     * @return proceduresBo valor del campo a obtener.
     */
    public ProceduresBo getProceduresBo() {
	return proceduresBo;
    }

    /**
     * Guarda el valor del parámetro proceduresBo.
     * 
     * @param proceduresBo
     *            valor del campo a guardar.
     */
    public void setProceduresBo(ProceduresBo proceduresBo) {
	this.proceduresBo = proceduresBo;
    }

}
