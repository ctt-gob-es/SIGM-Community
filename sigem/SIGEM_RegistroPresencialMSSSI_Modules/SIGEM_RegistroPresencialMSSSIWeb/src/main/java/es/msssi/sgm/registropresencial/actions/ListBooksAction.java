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

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.SecurityException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.isicres.web.util.ContextoAplicacionUtil;

import es.ieci.tecdoc.isicres.api.business.vo.ContextoAplicacionVO;
import es.msssi.sgm.registropresencial.beans.ListBooksBean;
import es.msssi.sgm.registropresencial.beans.ParamBookBean;
import es.msssi.sgm.registropresencial.businessobject.BooksBo;
import es.msssi.sgm.registropresencial.businessobject.PermissionsBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPBookException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Action que muestra la lista de libros disponible.
 * 
 * @author cmorenog
 */

public class ListBooksAction extends GenericActions {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ListBooksAction.class.getName());
    private BooksBo booksBo;
    private ListBooksBean listBook;
    private String id;
    private String type;
    private PermissionsBo permissionBo;

    /**
     * Constructor.
     */
    public ListBooksAction() {
    }

    /**
     * Crea el listado de libros.
     */
    @PostConstruct
    public void create() {
	if (booksBo == null) {
	    booksBo = new BooksBo();
	}
	if (listBook == null) {
	    try {
		listBook = booksBo.getBooks(useCaseConf);
	    }
	    catch (RPBookException rpBookException) {
		LOG.error(ErrorConstants.GET_BOOKS_ERROR_MESSAGE + ". Código: "
			+ rpBookException.getCode().getCode() + " . Mensaje: "
			+ rpBookException.getShortMessage());
		Utils.redirectToErrorPage(rpBookException, null, null);
	    }
	    catch (RPGenericException rpGenericException) {
		LOG.error(ErrorConstants.GET_BOOKS_ERROR_MESSAGE + ". Código: "
			+ rpGenericException.getCode().getCode() + " . Mensaje: "
			+ rpGenericException.getShortMessage());
		if ("RPG002".equals(rpGenericException.getCode())) {
		    ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
			    .getRequest()).getSession().invalidate();
		}
		Utils.redirectToErrorPage(rpGenericException, null, null);
	    }
	}

	ParamBookBean param = null;
	permissionBo = new PermissionsBo();
	try {
	    param =
		    permissionBo.getDisPermission(useCaseConf, facesContext.getExternalContext()
			    .getSessionMap().get(KeysRP.J_PARAMBOOK), null);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.GET_PERMISSIONS_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(null, sessionException, null);
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.GET_PERMISSIONS_ERROR_MESSAGE, validationException);
	    Utils.redirectToErrorPage(null, validationException, null);
	}
	catch (SecurityException securityException) {
	    LOG.error(ErrorConstants.GET_PERMISSIONS_ERROR_MESSAGE, securityException);
	    Utils.redirectToErrorPage(null, securityException, null);
	}
	 facesContext.getExternalContext().getSessionMap().put(KeysRP.J_PARAMBOOK, param);
	try {
	    if (facesContext.getExternalContext().getSessionMap().get("USERVO") == null){
	    ContextoAplicacionVO contextoAplicacion =
		    ContextoAplicacionUtil
			    .getContextoAplicacion((javax.servlet.http.HttpServletRequest) facesContext
				    .getExternalContext().getRequest());
	    facesContext.getExternalContext().getSessionMap()
		    .put("USERVO", contextoAplicacion.getUsuarioActual());
	    }
	}
	catch (TecDocException rpGenericException) {
	    LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE + ". Código: "
		    + rpGenericException.getCode() + " . Mensaje: "
		    + rpGenericException.getMessage());
	    Utils.redirectToErrorPage(null, rpGenericException, null);
	}
    }

    /**
     * Método que cargará el libro de entrada en el que estamos accediendo y
     * mostrará el formulario de búsqueda.
     */
    public void goSearchForm() {
	// parámetros de la llamada
	init();
	Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
	Integer archiveId = new Integer(params.get("id"));
	boolean readonly =
		(params.get("readonly") != null && "0".equals(params.get("readonly"))) ? false
			: true;

	/*
	 * Coger el libro y guardarlo en sesion. Si el libro está abierto de
	 * otra consulta hay que cerrarlo
	 */
	if (booksBo == null) {
	    booksBo = new BooksBo();
	}
	try {
	    clearView();
	    ScrRegstate bookID =
		    (ScrRegstate) facesContext.getExternalContext().getSessionMap()
			    .get(KeysRP.J_BOOK);
	    if (bookID != null && bookID.getIdocarchhdr().getId() != null) {
		booksBo.closeBook(useCaseConf, bookID.getIdocarchhdr().getId());
	    }
	    booksBo.openBook(useCaseConf, archiveId);
	    ScrRegstate book = booksBo.getBook(useCaseConf.getSessionID(), archiveId);
	    facesContext.getExternalContext().getSessionMap().put(KeysRP.J_BOOK, book);

	}
	catch (RPBookException rpBookException) {
	    LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE + ". Código: "
		    + rpBookException.getCode().getCode() + " . Mensaje: "
		    + rpBookException.getShortMessage());
	    Utils.redirectToErrorPage(rpBookException, null, null);
	}
	catch (RPGenericException rpGenericException) {
	    LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE + ". Código: "
		    + rpGenericException.getCode().getCode() + " . Mensaje: "
		    + rpGenericException.getShortMessage());
	    Utils.redirectToErrorPage(rpGenericException, null, null);
	}

	try {
	    /* Configurar permisos del usuario sobre el libro */
	    ParamBookBean param;
	    param =
		    permissionBo.getPermission(useCaseConf, facesContext.getExternalContext()
			    .getSessionMap().get(KeysRP.J_PARAMBOOK), archiveId, readonly);

	    facesContext.getExternalContext().getSessionMap().put(KeysRP.J_PARAMBOOK, param);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.GET_PERMISSIONS_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(null, sessionException, null);
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.GET_PERMISSIONS_ERROR_MESSAGE, validationException);
	    Utils.redirectToErrorPage(null, validationException, null);
	}
	catch (SecurityException securityException) {
	    LOG.error(ErrorConstants.GET_PERMISSIONS_ERROR_MESSAGE, securityException);
	    Utils.redirectToErrorPage(null, securityException, null);
	}
    }

    /**
     * goDistribucionForm.
     * 
     * @return null
     */
    public String goDistribucionForm() {
	clearView();
	return null;
    }

    /**
     * goIntRegistralForm.
     * 
     * @return null
     */
    public String goIntRegistralForm() {
	clearView();
	return null;
    }

    /**
     * goCambioOficinaForm.
     * 
     * @return null
     */
    public String goCambioOficinaForm() {
	return null;
    }

    /**
     * Obtiene el valor del parámetro listBook.
     * 
     * @return listBook valor del campo a obtener.
     */
    public ListBooksBean getListBook() {
	return listBook;
    }

    /**
     * Guarda el valor del parámetro listBook.
     * 
     * @param listBook
     *            valor del campo a guardar.
     */
    public void setListBook(ListBooksBean listBook) {
	this.listBook = listBook;
    }

    /**
     * Obtiene el valor del parámetro id.
     * 
     * @return id valor del campo a obtener.
     */
    public String getId() {
	return id;
    }

    /**
     * Guarda el valor del parámetro id.
     * 
     * @param id
     *            valor del campo a guardar.
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Obtiene el valor del parámetro type.
     * 
     * @return type valor del campo a obtener.
     */
    public String getType() {
	return type;
    }

    /**
     * Guarda el valor del parámetro type.
     * 
     * @param type
     *            valor del campo a guardar.
     */
    public void setType(String type) {
	this.type = type;
    }

    /**
     * Obtiene el valor del parámetro booksBo.
     * 
     * @return booksBo valor del campo a obtener.
     */
    public BooksBo getBooksBo() {
	return booksBo;
    }

    /**
     * Guarda el valor del parámetro booksBo.
     * 
     * @param booksBo
     *            valor del campo a guardar.
     */
    public void setBooksBo(BooksBo booksBo) {
	this.booksBo = booksBo;
    }

    /**
     * limpiar la vista desde el breadcrumb.
     */
    public void clean() {
	clearView();
    }
}