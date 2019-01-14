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

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.session.book.BookSession;
import com.ieci.tecdoc.isicres.session.distribution.DistributionSession;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase genérica de la que extenderán todos los actions de la aplicación.
 * 
 * @author cmorenog
 */
public class GenericActions implements Serializable {
	
	protected static final int NUMBER0 = 0;
	protected static final int NUMBER1 = 1;
	protected static final int NUMBER2 = 2;
	protected static final int NUMBER3 = 3;
	protected static final int NUMBER4 = 4;
	protected static final int NUMBER5 = 5;
	protected static final int NUMBER6 = 6;
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(GenericActions.class.getName());
	
	/** Variable con la configuración de la aplicación. */
	protected UseCaseConf useCaseConf = null;
	/** Contexto de faces. */
	protected FacesContext facesContext = null;

	/**
	 * Constructor.
	 */
	public GenericActions() {
		facesContext = FacesContext.getCurrentInstance();
		Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
		useCaseConf = (UseCaseConf) map.get(Keys.J_USECASECONF);
	}

	/**
	 * Método que inicializa las variables del contexto de faces.
	 */
	protected void init() {
		facesContext = FacesContext.getCurrentInstance();
		Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
		useCaseConf = (UseCaseConf) map.get(Keys.J_USECASECONF);
		MultiEntityContextHolder.setEntity(useCaseConf.getEntidadId());
	}

	/**
	 * Limpia el formulario y la tabla de resultados.
	 */
	protected void clearView() {
		init();
		Application application = facesContext.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(facesContext, facesContext.getViewRoot().getViewId());
		facesContext.setViewRoot(viewRoot);
		facesContext.renderResponse(); // Optional
	}

	/**
	 * Método que comprueba si el ID del libro esta cacheado. Si el usuario
	 * tiene acceso al libro la ejecución continúa. Si no, elevamos una
	 * excepcion {@link BookException}.
	 * 
	 * @param bookIDRequest
	 *            ID del libro a validar.
	 * @param useCaseConf
	 *            Usuario que realiza la petición.
	 * 
	 * @throws BookException
	 *             si se ha producido algún error al tratar la información del
	 *             libro.
	 */
	protected void validateIdBook(Integer bookIDRequest, UseCaseConf useCaseConf)
			throws BookException {
		LOG.trace("Entrando en GenericActions.validateIdBook()");
		try {
			// Recuperamos la caché
			CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
			// Es necesario tener el libro abierto para consultar su
			// si no se encuentra en la cacheBag es que no se ha podido abrir
			if (!cacheBag.containsKey(bookIDRequest)) {
				BookSession.openBook(useCaseConf.getSessionID(), bookIDRequest, useCaseConf.getEntidadId());
			}

			if (!cacheBag.containsKey(bookIDRequest)) {
				// el libro no esta cacheado para el usuario
				// con lo que no tiene acceso
				throw new BookException(BookException.ERROR_BOOK_NOTFOUND);
			}
		}
		// Si falla, redireccionamos a la página de error
		catch (TecDocException tecDocException) {
			LOG.error( ErrorConstants.GET_SESSION_INFORMATION_BOOK_ERROR_MESSAGE, tecDocException);
			Utils.redirectToErrorPage(null, tecDocException, null);
		}
	}

	/**
	 * Valida la información del registro.
	 * 
	 * @param useCaseConf
	 *            Usuario que realiza la petición.
	 * @param bookID
	 *            ID del libro.
	 * @param regIdRequest
	 *            ID del registro.
	 */
	protected void validateRegister(UseCaseConf useCaseConf, Integer bookID,
			Integer regIdRequest) {
		LOG.trace("Entrando en GenericActions.validateRegister()");
		try {
			// consultamos si el usuario tiene acceso al regstro, si se
			// encuentra
			// nos devolvera mayor de 0
			int size = FolderSession.getCountRegisterByIdReg( useCaseConf.getSessionID(), useCaseConf.getEntidadId(), bookID, regIdRequest);

			// si el contador es menor o igual a 0, el usuario no tiene
			// acceso directo al registro
			if (size <= 0) {
				// el usuario no tiene acceso directo al registro,
				// con lo que comprobamos si el registro le ha llegado
				// mediante una distribucion
				validateIDRegisterByDistribution(useCaseConf, bookID, regIdRequest);
			}
		}
		// Si falla, redireccionamos a la página de error
		catch (TecDocException tecDocException) {
			LOG.error(ErrorConstants.VALIDATE_INPUT_REGISTER_ERROR_MESSAGE, tecDocException);
			Utils.redirectToErrorPage(null, tecDocException, null);
		} catch (IOException ioException) {
			LOG.error(ErrorConstants.VALIDATE_INPUT_REGISTER_ERROR_MESSAGE, ioException);
			Utils.redirectToErrorPage(null, null, ioException);
		} catch (ServletException servletException) {
			LOG.error(ErrorConstants.VALIDATE_INPUT_REGISTER_ERROR_MESSAGE, servletException);
			Utils.redirectToErrorPage(null, null, servletException);
		}
	}

	/**
	 * Metodo que valida si el registro ha sido distribuido al usuario, de esta
	 * forma se comprueba si tiene acceso al registro.
	 * 
	 * @param useCaseConf
	 *            Usuario que realiza la petición.
	 * @param bookID
	 *            ID del libro.
	 * @param regIdRequest
	 *            ID del registro.
	 * 
	 * @throws TecDocException
	 *             Si se ha producido un error TecDoc.
	 * @throws IOException
	 *             Si se ha producido un error de entrada/salida.
	 * @throws ServletException
	 *             Si se ha producido un error en el Servlet.
	 */
	private void validateIDRegisterByDistribution(UseCaseConf useCaseConf, Integer bookID, Integer regIdRequest) throws TecDocException, IOException, ServletException {
		LOG.trace("Entrando en GenericActions.validateIDRegisterByDistribution()");
		// obtenemos el contador con el numero de distribuciones en las que se
		// ha visto afectado el registro y el usuario logeado
		int size = DistributionSession.getAllDistributionByRegisterAndUser( useCaseConf.getSessionID(), bookID, regIdRequest, useCaseConf.getEntidadId());

		// si el contador es menor o igual a 0 se eleva una excepcion, el
		// usuario no tiene acceso al registro
		if (size <= 0) {
			// el usuario no tiene acceso al registro indicado
			LOG.error("El usuario no tiene permisos para realizar " + "operaciones sobre el registro con FDRID [" + regIdRequest + "] en el libro [" + bookID + "]");
			throw new BookException(BookException.ERROR_ROW_OUTSIDE);
		}
	}

	/**
	 * Método que guarda en el contexto el objeto useCaseConf.
	 * 
	 * @param useCaseConf
	 *            objeto de configuracion de la aplicacion.
	 * 
	 */
	protected void saveUseCaseConf(UseCaseConf useCaseConf) {
		facesContext = FacesContext.getCurrentInstance();
		Map<String, Object> map = facesContext.getExternalContext() .getSessionMap();
		map.put(Keys.J_USECASECONF, useCaseConf);
	}

	/**
	 * Método que resetea valores de campos editables.
	 */
	public void resetValues(String component) {
		UIComponent input = FacesContext.getCurrentInstance().getViewRoot().findComponent(component);
		if (input != null && input.isRendered() && input instanceof EditableValueHolder) {
			EditableValueHolder editableValueHolder = (EditableValueHolder) input;
			editableValueHolder.setSubmittedValue(null);
			editableValueHolder.setValue(null);
			editableValueHolder.setValid(true);
			editableValueHolder.setLocalValueSet(false);
		}
	}

	public void deleteSession(String var) {
		facesContext = FacesContext.getCurrentInstance();
		if (facesContext.getExternalContext().getSessionMap().get(var) != null) {
			facesContext.getExternalContext().getSessionMap().remove(var);
		}
	}
}