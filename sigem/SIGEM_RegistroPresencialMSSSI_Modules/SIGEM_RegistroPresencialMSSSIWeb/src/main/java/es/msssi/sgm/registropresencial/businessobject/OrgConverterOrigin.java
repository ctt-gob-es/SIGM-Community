/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.businessobject;

import java.util.List;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.AttributesException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.msssi.sgm.registropresencial.errors.ErrorConstants;

/**
 * Clase que implementa converter para el combo de Organismos de origen. Permite
 * que los valores de un combo sean Beans.
 * 
 * @author cmorenog
 */
@FacesConverter(value = "orgConverterOrigin")
public class OrgConverterOrigin implements Converter {
    private static final Logger LOG = Logger.getLogger(OrgConverterOrigin.class);
    private static final String PATTERN = "\\d+";
    private static Pattern digitPattern = null;
    protected UseCaseConf useCaseConf = null;

    static {
	digitPattern = Pattern.compile(PATTERN);
    }

    /**
     * Convierte Un Id de organismo a Objeto.
     * 
     * @param facesContext
     *            Contexto actual de Faces.
     * @param component
     *            Componente UI de Faces.
     * @param submittedValue
     *            Id a convertir.
     * 
     * @return El objeto convertido o error si no existe.
     */
    @Override
    public Object getAsObject(
	FacesContext facesContext, UIComponent component, String submittedValue) {
	LOG.trace("Entrando en orgConverterOrigin.getAsObject()");
	ScrOrg result = null;
	if ("".equals(submittedValue.trim())) {
	    result = null;
	}
	else {
	    try {
		useCaseConf =
		    (UseCaseConf) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get(
			    Keys.J_USECASECONF);
		UnitsBo unitsBo = new UnitsBo();
		if (digitPattern.matcher(
		    submittedValue.trim()).matches()) {
		    int number = Integer.parseInt(submittedValue.trim());

		    result = unitsBo.getOrgId(
			useCaseConf, number);
		}
		else {
		    List<ScrOrg> listOrg = null;
		    listOrg = (List<ScrOrg>) unitsBo.getScrOrg(
			useCaseConf, submittedValue.trim(), null);
		    if (listOrg == null ||
			listOrg.size() == 0 || listOrg.size() > 1) {
			LOG.error("Error al convertir la unidad tramitadora");
			result = null;
		    }
		    else {
			result = listOrg.get(0);
		    }
		}
		if (result == null) {
		    LOG.error("Error al convertir el organismo");
		    throw new ConverterException(
			new FacesMessage(
			    FacesMessage.SEVERITY_ERROR,
			    ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE,
			    ErrorConstants.ORGANISM_DOES_NOT_EXISTS_MESSAGE));
		}
	    }
	    catch (NumberFormatException exception) {
		LOG.error(
		    "Error al parsear el Id a convertir", exception);
		throw new ConverterException(
		    new FacesMessage(
			FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE,
			ErrorConstants.ORGANISM_DOES_NOT_EXISTS_MESSAGE));
	    }
	    catch (ValidationException exception) {
		LOG.error(
		    "Error recuperando organismo", exception);
		throw new ConverterException(
		    new FacesMessage(
			FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE,
			ErrorConstants.ORGANISM_DOES_NOT_EXISTS_MESSAGE));
	    }
	    catch (AttributesException exception) {
		LOG.error(
		    "Error recuperando organismo", exception);
		throw new ConverterException(
		    new FacesMessage(
			FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE,
			ErrorConstants.ORGANISM_DOES_NOT_EXISTS_MESSAGE));
	    }
	    catch (SessionException exception) {
		LOG.error(
		    "Error recuperando organismo", exception);
		throw new ConverterException(
		    new FacesMessage(
			FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE,
			ErrorConstants.ORGANISM_DOES_NOT_EXISTS_MESSAGE));
	    }
	    LOG.info("Organismo convertido con id " +
		result.getId());

	}
	return result;
    }

    /**
     * Obtiene un Id de organismo a String.
     * 
     * @param facesContext
     *            Contexto actual de Faces.
     * @param component
     *            Componente UI de Faces.
     * @param value
     *            Id a convertir.
     * 
     * @return objectInString El valor convertido.
     */
    @Override
    public String getAsString(
	FacesContext facesContext, UIComponent component, Object value) {
	String result = "";
	LOG.trace("Entrando en orgConverterOrigin.getAsString()");
	if (value == null ||
	    "".equals(value)) {
	    result = "";
	}
	else {
	    result = String.valueOf(((ScrOrg) value).getId());
	}
	return result;
    }

}