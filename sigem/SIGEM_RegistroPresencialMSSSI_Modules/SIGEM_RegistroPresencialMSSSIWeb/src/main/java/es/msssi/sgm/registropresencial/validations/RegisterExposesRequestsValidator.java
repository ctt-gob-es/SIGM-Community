/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.validations;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;

/**
 * Clase que valida si se ha seleccionado expone y/o solicita.
 * 
 * @author cmorenog
 */
@FacesValidator("registerExposesRequestsValidator")
public class RegisterExposesRequestsValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(RegisterExposesRequestsValidator.class);

    /**
     * Valida si se ha seleccionado asunto y/o resumen.
     * 
     * @param context
     *            Contexto de Faces.
     * @param component
     *            Componente UI de Faces.
     * @param value
     *            Valor a validar.
     * 
     * @throws ValidatorException
     *             si falla la validación.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
	    throws ValidatorException {
	LOG.trace("Entrando en RegisterSubjectSummaryValidator.validate()");
	FacesMessage message = null;
	String solicita = null;

	UIInput uiInput = (UIInput) component.getAttributes().get("fld502");

	if (uiInput.getSubmittedValue() != null) {
	    solicita = uiInput.getSubmittedValue().toString();
	}

	if ((solicita == null || "".equals(solicita.trim())) && 
		(value != null && (!"".equals(((String)value).trim())))){
		message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		message.setSummary("Si se indica Expone es obligatorio indicar Solicita.");
		message.setDetail("Si se indica Expone es obligatorio indicar Solicita.");

		throw new ValidatorException(message);
	}
	return;
    }

}