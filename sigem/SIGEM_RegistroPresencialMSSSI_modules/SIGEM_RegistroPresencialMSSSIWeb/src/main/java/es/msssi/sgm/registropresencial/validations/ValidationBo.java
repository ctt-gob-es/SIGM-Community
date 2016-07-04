/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.validations;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.keys.HibernateKeys;
import com.ieci.tecdoc.common.keys.ISicresKeys;
import com.ieci.tecdoc.isicres.session.book.BookSession;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import es.msssi.sgm.registropresencial.businessobject.IGenericBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;

/**
 * Clase que implementa la interfaz IGenericBo que contiene los métodos
 * relacionados con las validaciones en los formularios.
 * 
 * @author cmorenog
 */
public class ValidationBo implements IGenericBo, HibernateKeys {
    private static final Logger LOG = Logger.getLogger(ValidationBo.class);

    /**
     * Metodo que realiza diferentes validaciones de seguridad para que el
     * usuario pueda editar un registro: <br/>
     * - Comprueba que el libro esta abierto. <br/>
     * - Comprueba que el usuario o su oficina tengan acceso al registro.
     * 
     * @param bookID
     *            ID del libro.
     * @param folderID
     *            ID del registro.
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * 
     * @throws BookException
     *             si ha habido algún problema con el libro de registro.
     * 
     * @throws RPGenericException
     *             si se ha producido un error genérico en el proceso.
     */
    public static void validationSecurityUser(
	Integer bookID, Integer folderID, UseCaseConf useCaseConf)
	throws BookException, RPGenericException {
	LOG.trace("Entrando en ValidationBo.validationSecurityUser()");
	// Comprobamos si el libro de registro esta abierto antes de seguir con
	// el proceso
	ScrRegstate scrregstate;
	try {
	    scrregstate = BookSession.getBook(
		useCaseConf.getSessionID(), bookID);
	    if (scrregstate.getState() == ISicresKeys.BOOK_STATE_CLOSED) {
		// El libro esta cerrado no se puede modificar los datos del
		// registro
		LOG.warn("El libro [" +
		    bookID + "] esta cerrado no se puede modificar el registro [" + folderID + "]");
		throw new BookException(
		    BookException.ERROR_BOOK_CLOSE);
	    }

	    // Comprobaremos si el usuario tiene acceso al registro
	    if ((folderID != null) &&
		(folderID.intValue() != -1)) {
		// buscamos el registro por id de registro e id de libro
		int size = FolderSession.getCountRegisterByIdReg(
		    useCaseConf.getSessionID(), useCaseConf.getEntidadId(), bookID, folderID);

		// Si no se encuentran datos
		if (size == 0) {
		    // El usuario no tiene acceso al registro con lo que no
		    // puede modificarlo
		    LOG.warn("El usuario [" +
			useCaseConf.getUserName() + "] no tiene acceso al registro [" + folderID +
			"] del Libro [" + bookID + "] con lo que no puede modificarlo");
		    throw new BookException(
			BookException.ERROR_UPDATE_FOLDER);
		}
	    }
	}
	catch (ValidationException validationException) {
	    LOG.error(
		ErrorConstants.USER_VALIDATION_ERROR_MESSAGE, validationException);
	    throw new RPGenericException(
		RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
		ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE, validationException);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.USER_VALIDATION_ERROR_MESSAGE, sessionException);
	    throw new RPGenericException(
		RPGenericErrorCode.SESSION_ERROR, ErrorConstants.SESSION_ERROR_MESSAGE,
		sessionException);
	}
    }

    /**
     * Muestra un mensaje en el contexto de Faces actual.
     * 
     * @param title
     *            Título a mostrar.
     * @param message
     *            Mensaje a mostrar.
     */
    public static void showDialog(
	String title, FacesMessage message) {
	LOG.trace("Entrando en ValidationBo.showDialog()");
	RequestContext.getCurrentInstance().addCallbackParam(
	    "mostrar", true);
	FacesContext.getCurrentInstance().addMessage(
	    title, message);
    }
}