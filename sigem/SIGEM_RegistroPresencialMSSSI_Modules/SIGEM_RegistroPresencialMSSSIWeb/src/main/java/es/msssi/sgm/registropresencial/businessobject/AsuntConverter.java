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

import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.invesicres.ScrCa;

import es.msssi.sgm.registropresencial.actions.ValidationListAction;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;

/**
 * Clase que implementa el converter para el combo de Asuntos. Permite que los
 * valores de un combo sean Beans.
 * 
 * @author cmorenog
 */
@FacesConverter(value = "asuntConverter")
public class AsuntConverter implements Converter {
    private static final Logger LOG = Logger.getLogger(AsuntConverter.class);
    private List<ScrCa> listAsuntos;

    /**
     * Convierte un Id de asunto a Objeto.
     * 
     * @param facesContext
     *            Contexto actual de Faces.
     * @param component
     *            Componente UI de Faces.
     * @param submittedValue
     *            Id a convertir.
     * 
     * @return stringInObject El objeto convertido o error si no existe.
     */
    @Override
    public Object getAsObject(
	FacesContext facesContext, UIComponent component, String submittedValue) {
	LOG.trace("Entrando en AsuntConverter.getAsObject()");
	Object result = null;
	if ("".equals(submittedValue.trim())) {
	    LOG.info("El Id del asunto a convertir está vacío. El asunto queda nulo");
	    result = null;
	}
	else {
	    try {
		listAsuntos =
		    ((ValidationListAction) facesContext.getExternalContext().getSessionMap().get(
			"validationListAction")).getListAsuntos();
		int number = Integer.parseInt(submittedValue);

		Iterator<ScrCa> iterator = listAsuntos.iterator();
		ScrCa p = null;
		while (iterator.hasNext() &&
		    result == null) {
		    p = iterator.next();
		    if (p.getId() == number) {
			LOG.info("Asunto convertido con id " +
			    p.getId());
			result = p;
		    }
		}
	    }
	    catch (NumberFormatException exception) {
		LOG.error(
		    "Error al parsear el Id a convertir", exception);
		throw new ConverterException(
		    new FacesMessage(
			FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE,
			ErrorConstants.ASUNT_DOES_NOT_EXISTS_MESSAGE));
	    }
	    if (result == null) {
		LOG.error("Error al convertir el asunto");
		throw new ConverterException(
		    new FacesMessage(
			FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE,
			ErrorConstants.ASUNT_DOES_NOT_EXISTS_MESSAGE));
	    }
	}

	return result;
    }

    /**
     * Obtiene un Id de asunto a String.
     * 
     * @param facesContext
     *            Contexto actual de Faces.
     * @param component
     *            Componente UI de Faces.
     * @param value
     *            Id a convertir.
     * 
     * @return El String convertido.
     */
    @Override
    public String getAsString(
	FacesContext facesContext, UIComponent component, Object value) {
	LOG.trace("Entrando en AsuntConverter.getAsString()");
	String asuntIdConverted = null;
	if (value == null ||
	    "".equals(value)) {
	    LOG.info("El Id del asunto a convertir es nulo o está vacío."
		+ " El asunto queda nulo");
	    asuntIdConverted = null;
	}
	else {
	    asuntIdConverted = String.valueOf(((ScrCa) value).getId());
	    LOG.info("Asunto convertido con Id " +
		asuntIdConverted);
	}
	return asuntIdConverted;
    }

    /**
     * Obtiene la lista de asuntos.
     * 
     * @return listAsuntos Lista de asuntos obtenida.
     */
    public List<ScrCa> getListAsuntos() {
	return listAsuntos;
    }

    /**
     * Guarda la lista de asuntos.
     * 
     * @param listAsuntos
     *            Lista de asuntos a guardar.
     */
    public void setListAsuntos(
	List<ScrCa> listAsuntos) {
	this.listAsuntos = listAsuntos;
    }
}