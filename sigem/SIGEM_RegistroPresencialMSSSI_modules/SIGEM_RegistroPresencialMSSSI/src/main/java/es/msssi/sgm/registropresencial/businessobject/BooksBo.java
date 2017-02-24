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
import java.util.List;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.utils.ScrRegStateByLanguage;
import com.ieci.tecdoc.isicres.session.book.BookSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.msssi.sgm.registropresencial.beans.ListBooksBean;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPBookErrorCode;
import es.msssi.sgm.registropresencial.errors.RPBookException;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;

/**
 * Clase que implementa la interfaz IGenericBo que contiene los métodos
 * relacionados con los libros.
 * 
 * @author cmorenog
 */
public class BooksBo implements IGenericBo, Serializable {
	private static final long serialVersionUID = 1L;
	// Se crea la instancia de log
	private static final Logger LOG = Logger.getLogger(BooksBo.class);
	
	/**
	 * Devuelve un bean con la lista de libros de entrada y salida.
	 * 
	 * @param useCaseConf
	 *            Usuario que realiza la petición.
	 * 
	 * @return listBooks Lista de libros obtenida.
	 * 
	 * @throws RPBookException
	 *             si se ha producido algún error al tratar la información del libro.
	 * 
	 * @throws RPGenericException
	 *             si se ha producido un error genérico en el proceso.
	 */
	@SuppressWarnings("unchecked")
	public ListBooksBean getBooks(UseCaseConf useCaseConf) throws RPGenericException,
			RPBookException {
		LOG.trace("Entrando en BooksBo.getBooks()");
		ListBooksBean listBooks = new ListBooksBean();
		try {
			LOG.info("Se intenta obtener el libro para el usuario de Id "
					+ useCaseConf.getSessionID() + " y de nombre "
					+ useCaseConf.getUserName());
			listBooks.setInList((List<ScrRegStateByLanguage>) BookSession.getInBooks(
					useCaseConf.getSessionID(), useCaseConf.getLocale(),
					useCaseConf.getEntidadId()));
			listBooks.setOutList((List<ScrRegStateByLanguage>) BookSession.getOutBooks(
					useCaseConf.getSessionID(), useCaseConf.getLocale(),
					useCaseConf.getEntidadId()));
			LOG.info("El tamaño de la lista de libros de entrada es "
					+ listBooks.getInList().size());
			LOG.info("El tamaño de la lista de libros de salida es "
					+ listBooks.getOutList().size());
		}
		catch (ValidationException validationException) {
			LOG.error(ErrorConstants.GET_BOOKS_ERROR_MESSAGE, validationException);
			throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
					ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE,
					validationException);
		}
		catch (BookException bookException) {
			LOG.error(ErrorConstants.GET_BOOKS_ERROR_MESSAGE, bookException);
			throw new RPBookException(RPBookErrorCode.GET_BOOKS_ERROR,
					ErrorConstants.GET_BOOKS_ERROR_MESSAGE, bookException);
		}
		catch (SessionException sessionException) {
			LOG.error(ErrorConstants.GET_BOOKS_ERROR_MESSAGE, sessionException);
			throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
					ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
		}
		return listBooks;
	}
	
	/**
	 * Devuelve el libro solicitado a partir de los IDs de sesión y del libro.
	 * 
	 * @param sessionID
	 *            Id de la sesión.
	 * @param bookID
	 *            Id del libro a obtener.
	 * 
	 * @return scrRegstate Libro obtenido.
	 * 
	 * @throws RPBookException
	 *             si se ha producido algún error al tratar la información del libro.
	 * 
	 * @throws RPGenericException
	 *             si se ha producido un error genérico en el proceso.
	 */
	public ScrRegstate getBook(String sessionID, Integer bookID)
			throws RPGenericException, RPBookException {
		LOG.trace("Entrando en BooksBo.getBook()");
		try {
			LOG.info("Se intenta obtener el libro con Id " + bookID);
			return BookSession.getBook(sessionID, bookID);
		}
		catch (ValidationException validationException) {
			LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE, validationException);
			throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
					ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE,
					validationException);
		}
		catch (BookException bookException) {
			LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE, bookException);
			throw new RPBookException(RPBookErrorCode.GET_BOOK_ERROR,
					ErrorConstants.GET_BOOK_ERROR_MESSAGE, bookException);
		}
		catch (SessionException sessionException) {
			LOG.error(ErrorConstants.GET_BOOK_ERROR_MESSAGE, sessionException);
			throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
					ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
		}
	}
	
	/**
	 * Abre un libro a partir del usuario y el ID del libro.
	 * 
	 * @param useCaseConf
	 *            Usuario que realiza la petición.
	 * @param bookID
	 *            Id del libro a abrir.
	 * 
	 * @throws RPBookException
	 *             si se ha producido algún error al tratar la información del libro.
	 * 
	 * @throws RPGenericException
	 *             si se ha producido un error genérico en el proceso.
	 */
	public void openBook(UseCaseConf useCaseConf, Integer bookID)
			throws RPGenericException, RPBookException {
		LOG.trace("Entrando en BooksBo.openBook()");
		try {
			LOG.info("Se intenta abrir el libro de Id " + bookID
					+ " del usuario de Id " + useCaseConf.getSessionID());
			BookSession.openBook(useCaseConf.getSessionID(), bookID,
					useCaseConf.getEntidadId());
		}
		catch (ValidationException validationException) {
			LOG.error(ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, validationException);
			throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
					ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE,
					validationException);
		}
		catch (BookException bookException) {
			LOG.error(ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, bookException);
			throw new RPBookException(RPBookErrorCode.OPEN_BOOK_ERROR,
					ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, bookException);
		}
		catch (SessionException sessionException) {
			LOG.error(ErrorConstants.OPEN_BOOK_ERROR_MESSAGE, sessionException);
			throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
					ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
		}
	}
	
	/**
	 * Cierra un libro a partir del usuario y el ID del libro,
	 * si ya estaba abierto por alguna otra consulta.
	 * 
	 * @param useCaseConf
	 *            Usuario que realiza la petición.
	 * @param bookID
	 *            Id del libro a cerrar.
	 * 
	 * @throws RPBookException
	 *             si se ha producido algún error al tratar la información del libro.
	 * 
	 * @throws RPGenericException
	 *             si se ha producido un error genérico en el proceso.
	 */
	public void closeBook(UseCaseConf useCaseConf, Integer bookID)
			throws RPGenericException, RPBookException {
		LOG.trace("Entrando en BooksBo.closeBook()");
		try {
			LOG.info("Se intenta cerrar el libro con Id " + bookID
					+ " del usuario con Id " + useCaseConf.getSessionID());
			BookSession.closeBook(useCaseConf.getSessionID(), bookID);
		}
		catch (ValidationException validationException) {
			LOG.error(ErrorConstants.CLOSE_BOOK_ERROR_MESSAGE, validationException);
			throw new RPGenericException(RPGenericErrorCode.PARAMETER_VALIDATION_ERROR,
					ErrorConstants.PARAMETERS_VALIDATION_ERROR_MESSAGE,
					validationException);
		}
		catch (BookException bookException) {
			LOG.error(ErrorConstants.CLOSE_BOOK_ERROR_MESSAGE, bookException);
			throw new RPBookException(RPBookErrorCode.CLOSE_BOOK_ERROR,
					ErrorConstants.CLOSE_BOOK_ERROR_MESSAGE, bookException);
		}
		catch (SessionException sessionException) {
			LOG.error(ErrorConstants.CLOSE_BOOK_ERROR_MESSAGE, sessionException);
			throw new RPGenericException(RPGenericErrorCode.SESSION_ERROR,
					ErrorConstants.SESSION_ERROR_MESSAGE, sessionException);
		}
	}
}