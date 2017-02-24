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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import es.msssi.sgm.registropresencial.businessobject.RegInterchangeBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.ValidationsUtil;

/**
 * Clase que valida el nif, cif y nie.
 * 
 * @author cmorenog
 */
@FacesValidator("nifValidator")
public class NIFValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(NIFValidator.class);
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
    public void validate(FacesContext context, UIComponent component, Object value)
	    throws ValidatorException {
	LOG.trace("Entrando en nifValidator.validate()");
	FacesMessage message = null;
	String tipoDoc = null;

	UIInput uiInput = (UIInput) component.getAttributes().get("tipoDOC");

	if (uiInput.getValue() != null) {
	    tipoDoc = uiInput.getValue().toString();
	}

	if (tipoDoc == null || "".equals(tipoDoc)) {
	    if ((value != null && !"".equals(value))) {
		message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		message.setSummary("Si indica un número de documento el tipo es obligatorio.");
		message.setDetail("Si indica un número de documento el tipo es obligatorio.");

		throw new ValidatorException(message);
	    }
	    else {
		return;
	    }
	}
	else {
	    if (value == null || "".equals(value)) {
		message = new FacesMessage();
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		message.setSummary("Si indica un tipo de documento el número es obligatorio.");
		message.setDetail("Si indica un tipo de documento el número es obligatorio.");

		throw new ValidatorException(message);
	    }
	    else {
		try {
		    validateNIF(tipoDoc, (String) value);
		}
		catch (ValidatorException e) {
			message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("El número de documento no es correcto.");
			message.setDetail("El número de documento no es correcto.");

			throw new ValidatorException(message);
		}
	    }
	}
    }

    private void validateNIF(String tipoDoc, String value) throws ValidatorException {
	try {
	    if ("2".equals(tipoDoc)) {
		ValidationsUtil.validarIdentificador("NIF", value);
	    }
	    else {
		if ("4".equals(tipoDoc)) {
		    ValidationsUtil.validarIdentificador("NIE", value);
		}
		else {
		    if ("1".equals(tipoDoc)) {
			ValidationsUtil.validarIdentificador("CIF", value);
		    }
		    if ("6".equals(tipoDoc)){
			RegInterchangeBo regInterchangeBo = new RegInterchangeBo();
			regInterchangeBo.isTramUnitByCode(value);
		    }
		}
	    }
	}
	catch (ValidatorException e) {
	    throw e;
	}
    }
}