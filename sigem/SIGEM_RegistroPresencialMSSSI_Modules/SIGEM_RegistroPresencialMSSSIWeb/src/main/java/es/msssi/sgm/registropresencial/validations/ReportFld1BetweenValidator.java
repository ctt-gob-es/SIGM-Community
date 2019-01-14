package es.msssi.sgm.registropresencial.validations;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.isicres.Keys;

/**
 * Clase que valida si se ha seleccionado en el campo fld1 busqueda entre dos numeros, 
 * validar que los dos numeros estan rellenos.
 * 
 * @author cmorenog
 */
@FacesValidator("reportFld1BetweenValidator")
public class ReportFld1BetweenValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(ReportFld1BetweenValidator.class);

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
	LOG.trace("Entrando en ReportFld1BetweenValidator.validate()");
	FacesMessage message = null;

	String combo = (String) value;
	UIInput uiDesde = (UIInput) component.getAttributes().get("fld1Value");
	UIInput uiHasta = (UIInput) component.getAttributes().get("fld1ValueHasta");
	


	if ( Keys.QUERY_BETWEEN_TEXT_VALUE.equals(combo) &&  ( (uiDesde == null ||
		(uiDesde != null && ("".equals(((String)uiDesde.getSubmittedValue()).trim())))) || 
		(uiHasta == null || (uiHasta != null && ("".equals(((String)uiHasta.getSubmittedValue()).trim())))))) {
		message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		message.setSummary("Error en el campo Número de Registro.");
		message.setDetail("Es obligatorio indicar el valor entre y hasta del número de registro si desea buscar entre dos números.");

		throw new ValidatorException(message);
	}
	return;
    }

}