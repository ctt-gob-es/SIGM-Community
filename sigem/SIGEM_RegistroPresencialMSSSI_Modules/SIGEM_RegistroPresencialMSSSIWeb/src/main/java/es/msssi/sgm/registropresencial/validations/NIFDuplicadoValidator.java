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

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.ieci.tecdoc.isicres.terceros.business.manager.TerceroManager;
import es.msssi.sgm.registropresencial.beans.BusquedaInteresado;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.provider.ISicresBeanTercerosProvider;

/**
 * Clase que valida el nif, cif y nie.
 * 
 * @author cmorenog
 */
@FacesValidator("nifDuplicadoValidator")
public class NIFDuplicadoValidator implements Validator {
	
	private static final Logger LOG = Logger.getLogger(NIFDuplicadoValidator.class);
	
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
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		LOG.trace("Entrando en nifValidator.validate()");
		
		FacesMessage message = new FacesMessage();
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext == null) {
		    message.setSeverity(FacesMessage.SEVERITY_ERROR);
		    message.setSummary(ErrorConstants.VALIDATE_INPUT_REGISTER_DATE_ERROR_MESSAGE);
		    message.setDetail(ErrorConstants.VALIDATE_INPUT_REGISTER_DATE_ERROR_MESSAGE);
		    context.addMessage(
			"Fecha de registro", message);
		    LOG.error(message.getDetail() +
			"Se ha caducado la sesión");
		    throw new ValidatorException(
			message);
		}
		Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
		UseCaseConf useCaseConf = (UseCaseConf) map.get(Keys.J_USECASECONF);
		
		TerceroManager terceroManager = ISicresBeanTercerosProvider.getInstance().getTerceroManager();
		
		List<?> resultadosBusqueda = null;
		BusquedaInteresado busquedaInteresado = new BusquedaInteresado();
		busquedaInteresado.setTipoBusqueda1("2");
		busquedaInteresado.setDocIdentidad((String) value);

		
		busquedaInteresado.setTipo("P");
		resultadosBusqueda = busquedaInteresado.buscarInteresado(terceroManager, useCaseConf.getEntidadId());
		
		if( null == resultadosBusqueda || resultadosBusqueda.isEmpty()){
			busquedaInteresado.setTipo("J");
			resultadosBusqueda = busquedaInteresado.buscarInteresado(terceroManager, useCaseConf.getEntidadId());
		}
		
		if( null != resultadosBusqueda && !resultadosBusqueda.isEmpty()){
			message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			String mensaje = ("Ya existe un tercero con NIF/CIF: " + (String)value);
			
			if("J".equals(busquedaInteresado.getTipo())){
				mensaje += ", de tipo Persona Jurídica.";
			} else {
				mensaje += ", de tipo Persona Física.";
			}
			
			message.setSummary(mensaje);
			message.setDetail(mensaje);

			throw new ValidatorException(message);
		}
	}
}