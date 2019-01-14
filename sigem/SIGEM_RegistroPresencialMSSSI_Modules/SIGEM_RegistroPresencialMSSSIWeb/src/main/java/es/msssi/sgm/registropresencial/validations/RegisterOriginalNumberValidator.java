package es.msssi.sgm.registropresencial.validations;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.isicres.web.util.ContextoAplicacionUtil;

import es.ieci.tecdoc.isicres.api.business.vo.BaseOficinaVO;
import es.ieci.tecdoc.isicres.api.business.vo.ContextoAplicacionVO;
import es.msssi.sgm.registropresencial.businessobject.RegInterchangeBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase que valida si existe el numero de registro original.
 * 
 * @author cmorenog
 */
@FacesValidator("registerOriginalNumberValidator")
public class RegisterOriginalNumberValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(RegisterOriginalNumberValidator.class);
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
     *             si falla la validación.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
	    throws ValidatorException {
	LOG.trace("Entrando en RegisterOriginalNumberValidator.validate()");
	FacesMessage message = null;
	RegInterchangeBo regInterchangeBo = new RegInterchangeBo();
	BaseOficinaVO oficina = null;
	boolean result = true;
	try {

	    contextoAplicacion =
		    ContextoAplicacionUtil
			    .getContextoAplicacion((javax.servlet.http.HttpServletRequest) FacesContext
				    .getCurrentInstance().getExternalContext().getRequest());
	    oficina = contextoAplicacion.getOficinaActual();
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.COPY_INPUT_REGISTER_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(null, sessionException, null);
	}
	catch (TecDocException tecDocException) {
	    LOG.error(ErrorConstants.GET_INBOX_INTERCHANGE_ERROR_MESSAGE, tecDocException);
	    Utils.redirectToErrorPage(null, tecDocException, tecDocException);
	}
	
	if (value != null && !"".equals(((String)value).trim())){
	    result = regInterchangeBo.validateOriginalNumber(oficina.getId(), (String)value);
	    if (!result){
		message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		message.setSummary("El número de registro original existe en la bandeja de registros SIR pendientes.");
		message.setDetail("El número de registro original existe en la bandeja de registros SIR pendientes.");

		throw new ValidatorException(message);
	    }
	}

	return;
    }

}