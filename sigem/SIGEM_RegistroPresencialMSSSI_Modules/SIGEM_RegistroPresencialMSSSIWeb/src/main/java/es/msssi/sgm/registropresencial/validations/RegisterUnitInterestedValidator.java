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
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

/**
 * Clase que valida si se ha seleccionado interesados y/u origen.
 * 
 * @author cmorenog
 */
@FacesValidator("registerUnitInterestedValidator")
public class RegisterUnitInterestedValidator implements Validator {
    private static final Logger LOG = Logger.getLogger(RegisterUnitInterestedValidator.class);

    /**
     * Valida si se ha seleccionado interesados y/u origen.
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
	LOG.trace("Entrando en registerOriginInterestedValidator.validate()");
	FacesMessage message = null;

	DataTable table = (DataTable) component.getAttributes().get("interested");
	String tipo = (String) component.getAttributes().get("type");
	Integer esSIR = null;
//	HtmlInputHidden inputHidden = (HtmlInputHidden) component.getAttributes().get("fld503");
	UISelectOne uiSelectOneEsIR = (UISelectOne) component.getAttributes().get("fld503");
	
	if (null != uiSelectOneEsIR && null != uiSelectOneEsIR.getSubmittedValue()) {	    
	    esSIR = Integer.valueOf(uiSelectOneEsIR.getSubmittedValue().toString());
	}
	//si es un registro de entrada
	if (tipo != null && "E".equals(tipo)) {
	    //si es un SIR
	    if (esSIR != null && new Integer(1).equals(esSIR)){
		if ((table == null || (table != null && table.getRowCount() == 0)) && (value == null)) {
			message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("Es obligatorio indicar Origen y/o Interesados.");
			message.setDetail("Es obligatorio indicar el Origen y/o Interesados.");

			throw new ValidatorException(message);
		 }
	    } 
//	    else {
//		if (table == null || (table != null && table.getRowCount() == 0)){
//		    message = new FacesMessage();
//			message.setSeverity(FacesMessage.SEVERITY_ERROR);
//			message.setSummary("Es obligatorio indicar Interesados.");
//			message.setDetail("Es obligatorio indicar Interesados.");
//
//			throw new ValidatorException(message);
//		}
//	    }
	}
	else {
	    if (tipo != null && "S".equals(tipo)) {
		if ((table == null || (table != null && table.getRowCount() == 0))
			&& (value == null)) {
		    message = new FacesMessage();
		    message.setSeverity(FacesMessage.SEVERITY_ERROR);
		    message.setSummary("Es obligatorio indicar Destino y/o Interesados.");
		    message.setDetail("Es obligatorio indicar el Destino y/o Interesados.");
		    throw new ValidatorException(message);
		}
	    }
	}

	/*if ((table == null || (table != null && table.getRowCount() == 0)) && (value == null)) {
	    message = new FacesMessage();
	    message.setSeverity(FacesMessage.SEVERITY_ERROR);
	    if (tipo != null && "E".equals(tipo)) {
		message.setSummary("Es obligatorio indicar Origen y/o Interesados.");
		message.setDetail("Es obligatorio indicar el Origen y/o Interesados.");
	    }
	    else {
		message.setSummary("Es obligatorio indicar Destino y/o Interesados.");
		message.setDetail("Es obligatorio indicar el Destino y/o Interesados.");
	    }
	    throw new ValidatorException(message);
	}*/
	return;
    }

}