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
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;

/**
 * Clase que valida si se ha seleccionado asunto y/o resumen.
 * 
 * @author cmorenog
 */
@FacesValidator("registerSubjectSummaryValidator")
public class RegisterSubjectSummaryValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(RegisterSubjectSummaryValidator.class);

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
	String asunto = null;

	UISelectOne uiSelectOne = (UISelectOne) component.getAttributes().get("fld16");

	if (uiSelectOne.getValue() != null) {
	    asunto = uiSelectOne.getValue().toString();
	}

	if ((asunto == null || "".equals(asunto)) && 
		(value == null || (value != null && ("".equals(((String)value).trim()))))) {
		message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		message.setSummary("Es obligatorio indicar el Asunto y/o Resumen.");
		message.setDetail("Es obligatorio indicar el Asunto y/o Resumen.");

		throw new ValidatorException(message);
	}
	return;
    }

}