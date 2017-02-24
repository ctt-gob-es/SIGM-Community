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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.session.book.BookSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.KeysRP;

/**
 * Clase que valida la fecha de registro.
 * 
 * @author cmorenog
 */
@FacesValidator("fld2Validator")
public class Fld2Validator implements Validator {
    private static final Logger LOG = Logger.getLogger(Fld2Validator.class);
    private static final String COMPARE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    private static final int MINOR_DATE = 1970;

    /**
     * Valida la fecha de registro de un libro.
     * 
     * @param context
     *            Contexto de Faces.
     * @param component
     *            Componente UI de Faces.
     * @param value
     *            Fecha a validar.
     * 
     * @throws ValidatorException
     *             si falla la validación de la fecha de registro.
     */
    @Override
    public void validate(
	FacesContext context, UIComponent component, Object value)
	throws ValidatorException {
	LOG.trace("Entrando en Fld2Validator.validate()");
	FacesMessage message = new FacesMessage();
	FacesContext facesContext = FacesContext.getCurrentInstance();
	if (facesContext == null) {
	    message.setSeverity(FacesMessage.SEVERITY_ERROR);
	    message.setSummary(ErrorConstants.VALIDATE_INPUT_REGISTER_DATE_ERROR_MESSAGE);
	    message.setDetail(ErrorConstants.VALIDATE_INPUT_REGISTER_DATE_ERROR_MESSAGE);
	    context.addMessage(
		"Fecha de registro", message);
	    LOG.error(message.getDetail() +
		"Se ha caducado la sesión");
	    throw new ValidatorException(
		message);
	}

	Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
	UseCaseConf useCaseConf = (UseCaseConf) map.get(Keys.J_USECASECONF);
	ScrRegstate book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
	    KeysRP.J_BOOK);
	SimpleDateFormat longFormatter = new SimpleDateFormat(
	    com.ieci.tecdoc.isicres.desktopweb.utils.RBUtil.getInstance(
		useCaseConf.getLocale()).getProperty(
		com.ieci.tecdoc.isicres.desktopweb.Keys.I18N_DATE_LONGFORMAT));
	longFormatter.setLenient(false);

	Date dateMaxRegClose = null;
	Date dbDate = null;
	Date valor = null;
	try {
	    dbDate = BookSession.getDBDateServer(
		useCaseConf.getSessionID(), useCaseConf.getEntidadId());
	    // obtenemos la fecha maxima de cierre de los registros
	    dateMaxRegClose = BookSession.getMaxDateRegClose(
		useCaseConf.getSessionID(), useCaseConf.getEntidadId(), book.getId());
	}
	catch (Exception e) {
	    message.setSeverity(FacesMessage.SEVERITY_ERROR);
	    message.setSummary(ErrorConstants.VALIDATE_INPUT_REGISTER_DATE_ERROR_MESSAGE);
	    message.setDetail(ErrorConstants.VALIDATE_INPUT_REGISTER_DATE_ERROR_MESSAGE);
	    context.addMessage(
		KeysRP.INPUT_REGISTER_DATE_LITERAL, message);
	    LOG.error(
		message.getDetail(), e);
	    throw new ValidatorException(
		message, e);
	}
	if (value != null) {
	    valor = (Date) value;
	    longFormatter.format(valor);
	    if (valor.after(dbDate) ||
		longFormatter.getCalendar().get(
		    java.util.Calendar.YEAR) < MINOR_DATE) {
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		message.setSummary(ErrorConstants.INPUT_REGISTER_DATE_RANGE_ERROR_MESSAGE);
		message.setDetail(ErrorConstants.INPUT_REGISTER_DATE_RANGE_ERROR_MESSAGE);
		context.addMessage(
		    KeysRP.INPUT_REGISTER_DATE_LITERAL, message);
		throw new ValidatorException(
		    message);
	    }
	    else {
		// Validamos la fecha de registro respecto a la fecha maxima de
		// cierre
		if (!validateDateRegisterWithDateMaxClose(
		    valor, dateMaxRegClose)) {
		    LOG.warn("Error en la validacion de Fecha Registro: " +
			"La fecha del registro [" + valor +
			"] es anterior a la fecha maxima de cierre [" + dateMaxRegClose +
			"] de registros");
		    message.setSeverity(FacesMessage.SEVERITY_ERROR);
		    message.setSummary("La fecha de registro " +
			"es anterior a la fecha maxima de cierre [" + dateMaxRegClose +
			"] de registros");
		    message.setDetail("La fecha de registro " +
			"es anterior a la fecha maxima de cierre [" + dateMaxRegClose +
			"] de registros");
		    context.addMessage(
			KeysRP.INPUT_REGISTER_DATE_LITERAL, message);
		    throw new ValidatorException(
			message);
		}
	    }
	}
    }

    /**
     * Valida que la fecha de registro sea mayor que la fecha máxima de cierre.
     * 
     * @param date
     *            Fecha de registro
     * @param dateMaxRegClose
     *            Fecha máxima de cierre de los registros
     * 
     * @return boolean Booleano que devuelve <i>true</i> si la validación es
     *         correcta y <i>false</i> si la fecha no pasa la validación.
     */
    private boolean validateDateRegisterWithDateMaxClose(
	Date date, Date dateMaxRegClose) {
	LOG.trace("Entrando en Fld2Validator.validateDateRegisterWithDateMaxClose()");
	boolean result = true;
	// Generamos un formato para las fechas yyyy-MM-dd HH:mm:ss
	SimpleDateFormat compareFormatter = new SimpleDateFormat(
	    COMPARE_FORMATTER);

	if ((dateMaxRegClose != null) &&
	    (date != null)) {
	    // Fecha del registro
	    String strDateReg = compareFormatter.format(date);
	    // Fecha maxima de cierre
	    String strDateMaxClose = compareFormatter.format(dateMaxRegClose);

	    if (strDateReg.compareTo(strDateMaxClose) <= 0) {
		// La fecha de registro es anterior a la fecha máxima de cierre
		result = false;
	    }
	    else {
		// La fecha de registro es posterior que la fecha máxima de
		// cierre
		result = true;
	    }
	}
	return result;
    }
}