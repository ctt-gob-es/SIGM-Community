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

import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralSIRVO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;

/**
 * Clase que implementa converter para el combo de unidades tramitadoras DCO.
 * Permite que los valores de un combo sean Beans.
 * 
 * @author cmorenog
 */
@FacesConverter(value = "unidadTramitadoraConverter")
public class UnidadTramitadoraConverter implements Converter {
    private static final Logger LOG = Logger.getLogger(UnidadTramitadoraConverter.class);
    private static final String PATTERN = "\\d+";
    private static Pattern digitPattern = null;
    private List<UnidadTramitacionIntercambioRegistralSIRVO> listaUnidadesTramitacionDCO = null;

    
    static {
	digitPattern = Pattern.compile(PATTERN);
    }
    /**
     * Convierte Un Id de unidad Tramitadora a Objeto.
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
	LOG.trace("Entrando en UnidadTramitadoraConverter.getAsObject()");
	Object result = null;
	UnidadTramitadoraBo unidadTramitadoraBo;
	if ("".equals(submittedValue.trim())) {
	    result = null;
	}
	else {
	    unidadTramitadoraBo = new UnidadTramitadoraBo();
	    try {
		if (digitPattern.matcher(
		    submittedValue.trim()).matches()) {

		    int number = Integer.parseInt(submittedValue.trim());
		    result = unidadTramitadoraBo.getUnidadTramitadora(number);
		}
		else {
		    List<UnidadTramitacionIntercambioRegistralSIRVO> listunidades = null;
		    listunidades =
			(List<UnidadTramitacionIntercambioRegistralSIRVO>) unidadTramitadoraBo
			    .buscarUnidadesTramitadoras(
				submittedValue.trim(), null);
		    if (listunidades == null ||
			listunidades.size() == 0 || listunidades.size() > 1) {
			LOG.error("Error al convertir la unidad tramitadora");
			result = null;
		    }
		    else {
			result = listunidades.get(0);
		    }
		}
		if (result == null) {
		    LOG.error("Error al convertir la unidad tramitadora");
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
			ErrorConstants.UNID_DOES_NOT_EXISTS_MESSAGE));
	    }
	    catch (RPRegistralExchangeException exception) {
		LOG.error(
		    "Error recuperando la unidad tramitadora", exception);
		throw new ConverterException(
		    new FacesMessage(
			FacesMessage.SEVERITY_ERROR, ErrorConstants.CONVERSION_ERROR_FACES_MESSAGE,
			ErrorConstants.UNID_DOES_NOT_EXISTS_MESSAGE));
	    }
	}
	return result;
    }

    /**
     * Obtiene un Id de Unidad Tramitadora a String.
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
	LOG.trace("Entrando en UnidadTramitacionDCO.getAsString()");
	if (value == null ||
	    "".equals(value)) {
	    result = "";
	}
	else {
	    result = String.valueOf(((UnidadTramitacionIntercambioRegistralSIRVO) value).getId());
	}
	return result;
    }

    /**
     * Obtiene el valor del parámetro listaUnidadesTramitacionDCO.
     * 
     * @return listaUnidadesTramitacionDCO valor del campo a obtener.
     */
    public List<UnidadTramitacionIntercambioRegistralSIRVO> getListaUnidadesTramitacionDCO() {
	return listaUnidadesTramitacionDCO;
    }

    /**
     * Guarda el valor del parámetro listaUnidadesTramitacionDCO.
     * 
     * @param listaUnidadesTramitacionDCO
     *            valor del campo a guardar.
     */
    public void setListaUnidadesTramitacionDCO(
	List<UnidadTramitacionIntercambioRegistralSIRVO> listaUnidadesTramitacionDCO) {
	this.listaUnidadesTramitacionDCO = listaUnidadesTramitacionDCO;
    }

}