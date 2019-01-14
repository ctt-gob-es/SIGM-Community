package es.msssi.sgm.registropresencial.validations;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;

import es.ieci.tecdoc.isicres.api.business.vo.ContextoAplicacionVO;

import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

/**
 * Clase que valida si el numero de registro original es obligatorio.
 * 
 * @author cmorenog
 */
@FacesValidator("registerRequiredNumRegisterValidator")
public class RegisterRequiredNumRegisterValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(RegisterRequiredNumRegisterValidator.class);
    ContextoAplicacionVO contextoAplicacion = null;
    /**
     * Valida si existe el numero de registro original.
     * 
     * @param context
     *            Contexto de Faces.
     * @param component
     *            Componente UI de Faces.
     * @param value
     *            Valor a validar.
     * 
     * @throws ValidatorException
     *             si falla la validaciï¿?n.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
	    throws ValidatorException {
	LOG.trace("Entrando en RegisterOriginalNumberValidator.validate()");
	FacesMessage message = null;
	boolean result = true;
	Integer required = null;
	Integer fld503 = null;
	UISelectOne uiSelectOne = (UISelectOne) component.getAttributes().get("fld1003");
	//HtmlInputHidden inputHidden = (HtmlInputHidden) component.getAttributes().get("fld503");
	UISelectOne uiSelectOneEsIR = (UISelectOne) component.getAttributes().get("fld503");
	
	if (null != uiSelectOneEsIR && null != uiSelectOneEsIR.getSubmittedValue()) {
	    fld503 = Integer.valueOf(uiSelectOneEsIR.getSubmittedValue().toString());
	}
	if (uiSelectOne.getSubmittedValue() != null) {
	    required = Integer.valueOf(uiSelectOne.getSubmittedValue().toString());
	}
	if (fld503 == null || !new Integer(1).equals(fld503)){
        	if (value == null || "".equals(((String)value).trim())){
        	    if (new Integer(1).equals(required)){
        		result = false;
        	    }
        	}
        	 if (!result){
        		message = new FacesMessage();
        		message.setSeverity(FacesMessage.SEVERITY_ERROR);
        		message.setSummary("El número de registro original es obligatorio. Si no debe introducirlo seleccione No obligatorio.");
        		message.setDetail("El número de registro original es obligatorio. Si no debe introducirlo seleccione No obligatorio.");
        
        		throw new ValidatorException(message);
        	    }
	}
	return;
    }

}